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

package jat.examples.TwoBodyExample;

import jat.core.cm.TwoBodyAPL;
import jat.core.plot.plot.FrameView;
import jat.core.plot.plot.Plot2DPanel;
import jat.core.plot.plot.PlotPanel;
import jat.core.plot.plot.plots.ScatterPlot;

import java.awt.Color;
import java.text.DecimalFormat;

import javax.swing.JFrame;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVectorFormat;
import org.apache.commons.math3.ode.FirstOrderIntegrator;
import org.apache.commons.math3.ode.nonstiff.DormandPrince853Integrator;

public class TwoBodyExample {

	public TwoBodyExample() {
	}

	public static void main(String[] args) {

		TwoBodyExample x = new TwoBodyExample();

		// create a TwoBody orbit using orbit elements
		TwoBodyAPL sat = new TwoBodyAPL(7000.0, 0.3, 0.0, 0.0, 0.0, 0.0);

		double[] y = sat.randv();

		ArrayRealVector v = new ArrayRealVector(y);

		DecimalFormat df2 = new DecimalFormat("#,###,###,##0.00");
		RealVectorFormat format = new RealVectorFormat(df2);
		System.out.println(format.format(v));

		// find out the period of the orbit
		double period = sat.period();

		// set the final time = one orbit period
		double tf = period;

		// set the initial time to zero
		double t0 = 0.0;

		// propagate the orbit
		FirstOrderIntegrator dp853 = new DormandPrince853Integrator(1.0e-8, 100.0, 1.0e-10, 1.0e-10);
		dp853.addStepHandler(sat.stepHandler);
		// double[] y = new double[] { 7000.0, 0, 0, .0, 8, 0 }; // initial
		// state

		dp853.integrate(sat, 0.0, y, 8000, y); // now y contains final state at
												// tf

		Double[] objArray = sat.time.toArray(new Double[sat.time.size()]);
		double[] timeArray = ArrayUtils.toPrimitive(objArray);
		double[] xsolArray = ArrayUtils.toPrimitive(sat.xsol.toArray(new Double[sat.time.size()]));
		double[] ysolArray = ArrayUtils.toPrimitive(sat.ysol.toArray(new Double[sat.time.size()]));

		double[][] XY = new double[timeArray.length][2];

		// int a=0;
		// System.arraycopy(timeArray,0,XY[a],0,timeArray.length);
		// System.arraycopy(ysolArray,0,XY[1],0,ysolArray.length);

		for (int i = 0; i < timeArray.length; i++) {
			XY[i][0] = xsolArray[i];
			XY[i][1] = ysolArray[i];
		}

		Plot2DPanel p = new Plot2DPanel();

		// Plot2DPanel p = new Plot2DPanel(min, max, axesScales, axesLabels);

		ScatterPlot s = new ScatterPlot("orbit", Color.RED, XY);
		// LinePlot l = new LinePlot("sin", Color.RED, XY);
		// l.closed_curve = false;
		// l.draw_dot = true;
		p.addPlot(s);
		p.setLegendOrientation(PlotPanel.SOUTH);
		double plotSize = 10000.;
		double[] min = { -plotSize, -plotSize };
		double[] max = { plotSize, plotSize };
		p.setFixedBounds(min, max);

		new FrameView(p).setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		System.out.println("end");

	}
}
