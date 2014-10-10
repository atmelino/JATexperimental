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

package jat.examples.CRTBP;

import jat.core.cm.CRTBP;
import jat.core.plot.plot.FrameView;
import jat.core.plot.plot.Plot2DPanel;
import jat.core.plot.plot.PlotPanel;
import jat.core.plot.plot.plots.LinePlot;

import java.awt.Color;

import javax.swing.JApplet;
import javax.swing.JFrame;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.FirstOrderIntegrator;
import org.apache.commons.math3.ode.nonstiff.DormandPrince853Integrator;

public class CRTBPPlot extends JApplet {
	static boolean print = false;

	public void init() {
		doExample();
	}

	void doExample() {
		double mu = 0.15;
		double[] y0 = { .11, 0, 0, 1.35, 1.33, 0 }; // initial state

		// double mu = 0.1;
		// double mu = 3.035909999e-6;

		// double mu = 0.012277471;
		// double[] y0 = { .1, 0, 0, 2.69, 2.69, 0 }; // initial state

		// double mu = 0.2;

		CRTBP myCRTBP = new CRTBP(mu);
		FirstOrderIntegrator dp853 = new DormandPrince853Integrator(1.0e-8, 100.0, 1.0e-10, 1.0e-10);
		dp853.addStepHandler(myCRTBP.stepHandler);

		FirstOrderDifferentialEquations ode = myCRTBP;

		double tf;
		double[] y = new double[6]; // initial state

		// for (int i = 1; i < 2; i++) {
		// tf = i * 20.;
		tf = 40.;
		System.arraycopy(y0, 0, y, 0, 6);

		dp853.integrate(ode, 0.0, y, tf, y); // now y contains final state
												// at
												// time tf
		if (print) {
			System.out.printf("%9.6f %9.6f %9.6f %9.6f %9.6f", tf, y[0], y[1], y[2], myCRTBP.JacobiIntegral(y));
			System.out.println();
		}

		int arraySize = myCRTBP.time.size();
		double[] timeArray = ArrayUtils.toPrimitive(myCRTBP.time.toArray(new Double[arraySize]));
		double[] xsolArray = ArrayUtils.toPrimitive(myCRTBP.xsol.toArray(new Double[arraySize]));
		double[] ysolArray = ArrayUtils.toPrimitive(myCRTBP.ysol.toArray(new Double[arraySize]));
		double[][] XY = new double[timeArray.length][2];
		for (int i = 0; i < timeArray.length; i++) {
			XY[i][0] = xsolArray[i];
			XY[i][1] = ysolArray[i];
		}

		Plot2DPanel p = new Plot2DPanel();
		LinePlot l = new LinePlot("spacecraft", Color.RED, XY);
		l.closed_curve = false;
		l.draw_dot = true;
		p.addPlot(l);
		double plotSize = 1.2;
		myCRTBP.findLibrationPoints();
		Color darkGreen = new java.awt.Color(0, 190, 0);

		addPoint(p, "Earth", java.awt.Color.BLUE, -mu, 0);
		addPoint(p, "Moon", java.awt.Color.gray, 1 - mu, 0);
		addPoint(p, "L1", darkGreen, myCRTBP.LibPoints[0].getX(), 0);
		addPoint(p, "L2", darkGreen, myCRTBP.LibPoints[1].getX(), 0);
		addPoint(p, "L3", darkGreen, myCRTBP.LibPoints[2].getX(), 0);

		String Labelmu = "mu = " + myCRTBP.mu;
		p.addLabel(Labelmu, java.awt.Color.black, 1, .9 * plotSize);
		String initial = "initial x,v = (" + y0[0] + "," + y0[1] + "),(" + y0[3] + "," + y0[4] + ")";
		p.addLabel(initial, java.awt.Color.black, 1, .8 * plotSize);
		String Jacobi = "spacecraft C = " + myCRTBP.C;
		p.addLabel(Jacobi, java.awt.Color.black, 1, .7 * plotSize);
		String L1C = "L1 C = " + myCRTBP.C1;
		p.addLabel(L1C, java.awt.Color.black, 1, .6 * plotSize);

		myCRTBP.findZeroVelocity();
		int size = myCRTBP.xzv.size();
		double[] xzvArray = ArrayUtils.toPrimitive(myCRTBP.xzv.toArray(new Double[size]));
		double[] yzvArray = ArrayUtils.toPrimitive(myCRTBP.yzv.toArray(new Double[size]));
		double[][] XYzv = new double[size][2];
		for (int i = 0; i < size; i++) {
			XYzv[i][0] = xzvArray[i];
			XYzv[i][1] = yzvArray[i];
		}
		LinePlot lzv = new LinePlot("zero vel", Color.blue, XYzv);
		lzv.closed_curve = false;
		lzv.draw_dot = true;
		p.addPlot(lzv);

		p.setLegendOrientation(PlotPanel.SOUTH);
		p.setFixedBounds(0, -plotSize, plotSize);
		p.setFixedBounds(1, -plotSize, plotSize);
		new FrameView(p).setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	void addPoint(Plot2DPanel p, String s, Color c, double x, double y) {
		double[][] points = new double[1][2];
		points[0][0] = x;
		points[0][1] = y;
		p.addScatterPlot(s, c, points);
	}

	// public static void main(String[] args) {
	//
	// CRTBPPlot ex = new CRTBPPlot();
	// ex.doExample();
	//
	// }

}

// double[][] points1 = new double[1][2];
// points1[0][0] = -mu;
// points1[0][1] = 0;
// p.addScatterPlot("Earth", java.awt.Color.BLUE, points1);
// double[][] points2 = new double[1][2];
// points2[0][0] = 1. - mu;
// points2[0][1] = 0;
// p.addScatterPlot("Moon", java.awt.Color.gray, points2);
