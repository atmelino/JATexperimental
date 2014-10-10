/* JAT: Java Astrodynamics Toolkit
 *
 * Copyright (c) 2006 National Aeronautics and Space Administration. All rights reserved.
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
package jat.examplesNOSA.Lambert;

import jat.coreNOSA.algorithm.integrators.Printable;
import jat.coreNOSA.cm.Constants;
import jat.coreNOSA.cm.Lambert;
import jat.coreNOSA.cm.LambertException;
import jat.coreNOSA.cm.TwoBody;
import jat.coreNOSA.math.MatrixVector.data.VectorN;
import jat.coreNOSA.plotutil.SinglePlot;

/**
 * @author Tobias Berthold
 * 
 *         Lambert Targeting example
 * 
 */
public class Lambert_test implements Printable {
	SinglePlot traj_plot = new SinglePlot();
	private int plotnum = 0;

	/** Creates a new instance of TwoBodyExample */
	public Lambert_test() {
		// set up the trajectory plot
		traj_plot.setTitle("Lambert Targeting");
		traj_plot.plot.setXLabel("x (km)");
		traj_plot.plot.setYLabel("y (km)");
	}

	/**
	 * Implements the Printable interface to get the data out of the propagator
	 * and pass it to the plot. This method is executed by the propagator at
	 * each integration step.
	 * 
	 * @param t
	 *            Time.
	 * @param y
	 *            Data array.
	 */
	public void print(double t, double[] y) {
		// handle the first variable for plotting - this is a little mystery but
		// it works
		boolean first = true;
		if (t == 0.0)
			first = false;
		// add data point to the plot
		traj_plot.plot.addPoint(plotnum, y[0], y[1], first);
		// also print to the screen for warm fuzzy feeling
		// if(!first) System.out.println("t x y");
		// System.out.println(t+" "+y[0]+" "+y[1]);
	}

	public static void main(String[] args) {
		double muforthisproblem = 1. * Constants.mu;
		double tof = 50000.;

		System.out.println("mu=" + muforthisproblem);

		Lambert_test x = new Lambert_test();
		// create a TwoBody orbit using orbit elements
		TwoBody initpos = new TwoBody(muforthisproblem, 40000.0, 0., 0.0, 0.0, 0., 0.0);
		TwoBody finalpos = new TwoBody(muforthisproblem, 80000.0, 0., 0.0, 0.0, 0.0, 120.0);

		// propagate the orbits for plotting
		initpos.propagate(0., initpos.period(), x, true);
		x.plotnum++;
		finalpos.propagate(0., finalpos.period(), x, true);
		x.plotnum++;

		// Get position and velocity vector according to orbit elements
		VectorN r0 = initpos.getR();
		VectorN v0 = initpos.getV();
		VectorN rf = finalpos.getR();
		VectorN vf = finalpos.getV();

		initpos.print("initpos");

		Lambert lambert = new Lambert(muforthisproblem);
		// double totaldv = lambert.compute(r0, v0, rf, vf,initpos.period()/1.5
		// );
		double totaldv;
		try {
			totaldv = lambert.compute(r0, v0, rf, vf, tof);
		} catch (LambertException e) {
			totaldv = -1;
			e.printStackTrace();
		}

		// apply the first delta-v
		VectorN dv0 = lambert.deltav0;
		v0 = v0.plus(dv0);
		System.out.println("tof = " + lambert.tof);
		TwoBody chaser = new TwoBody(muforthisproblem, r0, v0);
		chaser.print("chaser orbit");
		chaser.propagate(0.0, tof, x, true);

		// Plotting
		x.traj_plot.plot.setMarksStyle("dots", 3);
		x.traj_plot.plot.addPoint(3, initpos.getR().x[0], initpos.getR().x[1], false);
		x.traj_plot.plot.addPoint(3, finalpos.getR().x[0], finalpos.getR().x[1], false);
		x.traj_plot.plot.addLegend(0, "initial orbit");
		x.traj_plot.plot.addLegend(1, "target orbit");
		x.traj_plot.plot.addLegend(2, "chaser");
		// make the plot visible and square
		x.traj_plot.setVisible(true);
		int size = 100000;
		x.traj_plot.plot.setXRange(-size, size);
		x.traj_plot.plot.setYRange(-size, size);

		System.out.println("Total delta-v: " + totaldv);
	}
}
