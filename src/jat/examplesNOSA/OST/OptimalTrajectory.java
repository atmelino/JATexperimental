/* JAT: Java Astrodynamics Toolkit
 *
 * Copyright (c) 2002 National Aeronautics and Space Administration and the Center for Space Research (CSR),
 * The University of Texas at Austin. All rights reserved.
 *
 * This file is part of JAT. JAT is free software; you can
 * redistribute it and/or modify it under the terms of the
 * NASA Open Source Agreement
 * 
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * NASA Open Source Agreement for more details.
 *
 * You should have received a copy of the NASA Open Source Agreement
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 */

package jat.examplesNOSA.OST;

import jat.coreNOSA.algorithm.optimization.DFP;
import jat.coreNOSA.algorithm.optimization.GradientSearch;
import jat.coreNOSA.cm.Constants;
import jat.coreNOSA.cm.KeplerElements;
import jat.coreNOSA.cm.cm;
import jat.coreNOSA.cm.eom.TwoBodyCSI;
import jat.coreNOSA.math.MatrixVector.data.VectorN;
import jat.coreNOSA.plotutil.SinglePlot;

import java.io.IOException;

/**
 * @author Tobias Berthold
 *
 */
public class OptimalTrajectory
{
	static SinglePlot plot;
	static PerformanceIndex J;
	static ThrustDirection thrust;
		
	public OptimalTrajectory()
	{}

	/** Runs the example.
	 * @param args Arguments.
	 */
	public static void main(String[] args) throws IOException
	{
		System.out.println("Trajectory optimization example");

		// Set up the trajectory plot
		plot = new SinglePlot();
		plot.setTitle("Transfer CSI Trajectory in a Two Body Force Field");
		plot.plot.setXLabel("x (km)");
		plot.plot.setYLabel("y (km)");
		double plotsize = 2.e4;
		plot.plot.setXRange(-plotsize, plotsize);
		plot.plot.setYRange(-plotsize, plotsize);
		plot.plot.setMarksStyle("dots");
		plot.plot.addPoint(2, 0., 0., false);
		plot.plot.setMarksStyle("none");
		plot.setVisible(true);	// Make the plot visible


		// Create two body orbits (elliptical)
		KeplerElements k_initial = new KeplerElements(10000.0, 0., 0.0, 0.0, 0.0, 0.0);
		//KeplerElements k_final = new KeplerElements(12500., 0., 0.0, 0.0, 0., 120. * Constants.deg2rad);
		//KeplerElements k_final = new KeplerElements(14721., 0.46, 0.0, 0.0, 2.57 * Constants.deg2rad, 120. * Constants.deg2rad);
		KeplerElements k_final = new KeplerElements(14721., 0.5, 0.0, 0.0, 2.57 * Constants.deg2rad, 120. * Constants.deg2rad);
		/*
		KeplerElements k_final =
			new KeplerElements(
				14721.542737298156,
				0.45841936033455066,
				0.0,
				0.0,
				2.5695865408349197 * Constants.deg2rad,
				120.14931430671902 * Constants.deg2rad);
		//120 * Constants.deg2rad);
		 */
		Orbit orbit_init=new Orbit(k_initial);
		Orbit orbit_final=new Orbit(k_final);
		// Plot the initial and the final orbit
		orbit_init.plot_orbit(plot);
		orbit_final.plot_orbit(plot);

		// Create instances of the classes
		OptimalTrajectory example = new OptimalTrajectory();
		thrust = new ThrustDirection(plot);
		TwoBodyCSI trajectory = new TwoBodyCSI(k_initial, cm.mu, 3000., thrust, 1.5e0, 10000);

		// initialize the state variables and times
		double[] x0 = trajectory.rvm.getArray();
		double[] x0final = orbit_final.twobody.randv();
		double t0 = 0.0;
		double tf_CSI = 4000.; // final time for transfer trajectory
		//new VectorN(x0).print("initial");
		//new VectorN(x0final).print("final");

		// Find the optimal trajectory
		double[] x_guess = new double[4]; // initial guess of search vector
		double[] x_search; //  search vector x=[ tf theta0 theta1 .. ]
		x_guess[0] = 4000.;
		x_guess[1] = Math.toRadians(50.);
		x_guess[2] = Math.toRadians(120.);
		x_guess[3] = Math.toRadians(150.);
		J = new PerformanceIndex( x0, x_guess[0], x0final, trajectory, thrust);

		int mode = 2;
		if (mode == 0)
		{
			print_result(x_guess);
		}
		if (mode == 1)
		{
			J.create_grid();
		}
		if (mode == 2)
		{
			GradientSearch OST = new GradientSearch(J, x_guess);
			OST.err_ods = 1.e-4;
			OST.err_grad = 1.e-1;
			OST.eps_FD = 1.e-8; //-8;
			thrust.plot_yes_no = true;
			x_search = OST.find_min_gradient();
			thrust.plot_yes_no = true;
			print_result(x_search);
		}
		if (mode == 3)
		{
			DFP OST = new DFP(J, x_guess);
			OST.err_ods = 1.e-4;
			OST.err_dfp = 1.e0;
			OST.eps_CD = 1.e-4;
			OST.max_it = 100;
			thrust.plot_yes_no = false;
			x_search = OST.find_min_DFP();
			print_result(x_search);
		}
		System.out.println("Done");

	}

	static void print_result(double[] x_search)
	{
		thrust.plot_yes_no = true;
		double result = J.evaluate(x_search);
		System.out.println("Result");
		System.out.println("J= "+result);
		new VectorN(x_search).print();
		
	}

}
