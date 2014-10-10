/* JAT: Java Astrodynamics Toolkit
 * 
  Copyright 2012 Tobias Berthold

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package jat.application.orbitviewer;

import jat.core.plot.plot.Plot3DPanel;
import jat.coreNOSA.algorithm.integrators.Printable;
import jat.coreNOSA.cm.TwoBody;
import jat.coreNOSA.cm.cm;


public class orbitviewerplot implements Printable {

	public double a = 8000.; // sma in km
	double e = 0.1; // eccentricity
	double i = 15.; // inclination in degrees
	double raan; // right ascension of ascending node in degrees
	double w; // argument of perigee in degrees
	double ta; // true anomaly in degrees
	double max;

	Plot3DPanel plot;
	int steps = 200;
	double[][] XYZ = new double[steps][3];
    double[][] points = new double[1][3];
	int step;

	public void add_scene() {
		System.out.println("a " + a);

		TwoBody sat = new TwoBody(a, e, i, raan, w, ta);

		// find out the period of the orbit
		double period = sat.period();

		// set the final time = one orbit period
		double tf = period;

		// set the initial time to zero
		double t0 = 0.0;
		sat.setSteps(steps);
		// propagate the orbit

		step = 0;
		max = 0;
		sat.propagate(t0, tf, this, true);

		points=new double[1][3] ;
		points[0][0]=sat.rv.x[0];
		points[0][1]=sat.rv.x[1];
		points[0][2]=sat.rv.x[2];
				
				
		plot.addSpherePlot("earth", cm.earth_radius);
		plot.addLinePlot("orbit", XYZ, true);
        plot.addScatterPlot("satellite" ,1,5, points);

		double size = max;
		plot.setFixedBounds(0, -size, size);
		plot.setFixedBounds(1, -size, size);
		plot.setFixedBounds(2, -size, size);
	}

	public void make_plot() {
		// create your PlotPanel (you can use it as a JPanel) with a legend at
		// SOUTH
		plot = new Plot3DPanel("SOUTH");
		// add grid plot to the PlotPanel
		add_scene();
	}

	public void print(double t, double[] y) {

		// add data point to the plot
		if (step < XYZ.length) {
			XYZ[step][0] = y[0];
			XYZ[step][1] = y[1];
			XYZ[step][2] = y[2];
			if (y[0] > max)
				max = y[0];
			if (y[1] > max)
				max = y[1];
			if (y[2] > max)
				max = y[2];

			step++;
		}
		// also print to the screen for warm fuzzy feeling
		// System.out.println(t + " " + y[0] + " " + y[1]);
	}

	/*
	 * public static void main(String[] args) {
	 * 
	 * 
	 * // put the PlotPanel in a JFrame like a JPanel JFrame frame = new
	 * JFrame("a plot panel"); frame.setSize(600, 600);
	 * frame.setContentPane(plot); frame.setVisible(true);
	 * 
	 * }
	 */
}
