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

package jat.application.DE405Propagator;

import jat.core.astronomy.SolarSystemBodies;
import jat.core.ephemeris.DE405Body.body;
import jat.core.ephemeris.DE405Plus;
import jat.core.ephemeris.EphemerisPlotData;
import jat.core.plot.plot.Plot3DPanel;
import jat.core.plot.plot.PlotPanel;
import jat.core.plot.plot.plots.LinePlot;
import jat.coreNOSA.math.MatrixVector.data.VectorN;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JPanel;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.FirstOrderIntegrator;
import org.apache.commons.math3.ode.nonstiff.DormandPrince853Integrator;

public class DE405PropagatorPlot extends JPanel {

	private static final long serialVersionUID = 4814672707864836820L;
	Plot3DPanel plot;
	int steps = 200;
	double[][] XYZ = new double[steps][3];
	double[][] points = new double[1][3];
	int step;
	DE405PropagatorMain dpMain;
	DE405Plus Eph;
	static boolean print = false;
	double plotBounds;
	SolarSystemBodies sb = new SolarSystemBodies();

	public DE405PropagatorPlot(DE405PropagatorMain dpMain) {
		this.dpMain = dpMain;
		this.Eph = dpMain.dpGlobals.Eph;
	}

	public void make_plot() {
		// create your PlotPanel (you can use it as a JPanel) with a legend at
		// SOUTH
		plot = new Plot3DPanel("SOUTH");
		// add grid plot to the PlotPanel
		add_scene();
	}

	public void add_scene() {

		// Update Ephemeris to current user parameters
		for (body b : body.values()) {
			Eph.bodyGravOnOff[b.ordinal()] = dpMain.dpParam.bodyGravOnOff[b.ordinal()];
		}
		Eph.setIntegrationStartTime(dpMain.dpParam.simulationDate);
		try {
			Eph.setEarthMoonPlaneNormal(dpMain.dpParam.simulationDate);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Eph.setFrame(dpMain.dpParam.Frame);
		Eph.reset();

		// Spacecraft Trajectory
		FirstOrderIntegrator dp853 = new DormandPrince853Integrator(1.0e-8, dpMain.dpParam.tf, 1.0e-10, 1.0e-10);
		dp853.addStepHandler(Eph.stepHandler);
		FirstOrderDifferentialEquations ode = Eph;
		double[] y = new double[6];
		dp853.integrate(ode, 0.0, dpMain.dpParam.y0, dpMain.dpParam.tf, y);
		if (print) {
			String nf = "%10.3f ";
			String format = nf + nf + nf + nf + nf;
			System.out.printf(format, dpMain.dpParam.tf, y[0], y[1], y[2], Eph.energy(dpMain.dpParam.tf, y));
			System.out.println();
		}

		LinePlot l1 = new LinePlot("spacecraft", Color.RED, getXYZforPlot(Eph.xsol, Eph.ysol, Eph.zsol));
		l1.closed_curve = false;
		plot.addPlot(l1);

		addBodies();

		// Vector3D y0v=new Vector3D(dpParam.y0[0],dpParam.y0[1],dpParam.y0[2]);
		// double plotBounds = 2*y0v.getNorm();
		plot.setFixedBounds(0, -plotBounds, plotBounds);
		plot.setFixedBounds(1, -plotBounds, plotBounds);
		plot.setFixedBounds(2, -plotBounds, plotBounds);
		plot.setLegendOrientation(PlotPanel.SOUTH);
	}

	void addBodies() {
		int bodyNumber;
		// int x, y, z;
		VectorN BodyPos;
		EphemerisPlotData epd;
		LinePlot l;
		
		// central body gets as sphere

		switch (Eph.ephFrame) {
		case ICRF:
			plot.addSpherePlot("Sun", java.awt.Color.ORANGE, sb.Bodies[body.SUN.ordinal()].radius);
			break;
		case HEE:
			plot.addSpherePlot("Sun", java.awt.Color.ORANGE, sb.Bodies[body.SUN.ordinal()].radius);
			break;
		case ECI:
			plot.addSpherePlot("Earth", java.awt.Color.BLUE, sb.Bodies[body.EARTH.ordinal()].radius);
			break;
		case MEOP:
			plot.addSpherePlot("Earth", java.awt.Color.BLUE, sb.Bodies[body.EARTH.ordinal()].radius);
			break;
		default:
			break;
		}

		// other bodies get a point
		for (body b : body.values()) {
			bodyNumber = b.ordinal();
			if (Eph.bodyGravOnOff[bodyNumber] == true) {
				try {
					BodyPos = Eph.get_planet_pos(b, dpMain.dpParam.simulationDate);
					addPoint(plot, body.name[bodyNumber], sb.Bodies[bodyNumber].color, BodyPos.x[0], BodyPos.x[1],
							BodyPos.x[2]);

					epd = new EphemerisPlotData(Eph, b, dpMain.dpParam.simulationDate, dpMain.dpParam.tf, 100);
					l = new LinePlot(body.name[bodyNumber], sb.Bodies[bodyNumber].color, epd.XYZ);
					l.closed_curve = false;
					plot.addPlot(l);

				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}

		// VectorN EarthPos = null;
		// VectorN MoonPost0 = null;
		// VectorN MoonPostf = null;
		// VectorN SunPos = null;
		// try {
		// SunPos = Eph.get_planet_pos(body.SUN, dpMain.dpParam.simulationDate);
		// EarthPos = Eph.get_planet_pos(body.EARTH,
		// dpMain.dpParam.simulationDate);
		// MoonPost0 = Eph.get_planet_pos(body.MOON,
		// dpMain.dpParam.simulationDate);
		// MoonPostf = Eph.get_planet_pos(body.MOON,
		// dpMain.dpParam.simulationDate.plus(dpMain.dpParam.tf));
		// } catch (IOException e) {
		// e.printStackTrace();
		// }

		// addPoint(plot, "Sun", java.awt.Color.ORANGE, SunPos.x[0],
		// SunPos.x[1], SunPos.x[2]);
		// addPoint(plot, "Moon t0", java.awt.Color.GRAY, MoonPost0.x[0],
		// MoonPost0.x[1], MoonPost0.x[2]);
		// addPoint(plot, "Moon tf", java.awt.Color.GRAY, MoonPostf.x[0],
		// MoonPostf.x[1], MoonPostf.x[2]);
		// addPoint(plot, "Earth", java.awt.Color.MAGENTA, EarthPos.x[0],
		// EarthPos.x[1], EarthPos.x[2]);

//		EphemerisPlotData epd = new EphemerisPlotData(Eph, body.MOON, dpMain.dpParam.simulationDate, dpMain.dpParam.tf,
//				100);
//		LinePlot lMoon = new LinePlot("Moon", Color.green, epd.XYZ);
//		lMoon.closed_curve = false;
//		plot.addPlot(lMoon);

	}

	double[][] getXYZforPlot(ArrayList<Double> xsol, ArrayList<Double> ysol, ArrayList<Double> zsol) {
		int arraySize = xsol.size();
		double[] xsolArray = ArrayUtils.toPrimitive(xsol.toArray(new Double[arraySize]));
		double[] ysolArray = ArrayUtils.toPrimitive(ysol.toArray(new Double[arraySize]));
		double[] zsolArray = ArrayUtils.toPrimitive(zsol.toArray(new Double[arraySize]));
		double[][] XYZ = new double[arraySize][3];
		plotBounds = 0.;
		for (int i = 0; i < arraySize; i++) {
			XYZ[i][0] = xsolArray[i];
			XYZ[i][1] = ysolArray[i];
			XYZ[i][2] = zsolArray[i];
			plotBounds = Math.max(plotBounds, Math.abs(XYZ[i][0]));
			plotBounds = Math.max(plotBounds, Math.abs(XYZ[i][1]));
			plotBounds = Math.max(plotBounds, Math.abs(XYZ[i][2]));
		}
		plotBounds /= 1.5;
		return XYZ;
	}

	void addPoint(Plot3DPanel p, String s, Color c, double x, double y, double z) {
		double[][] points = new double[1][3];
		points[0][0] = x;
		points[0][1] = y;
		points[0][2] = z;
		p.addScatterPlot(s, c, points);
	}

}

// VectorN v = Eph.EarthMoonPlaneNormal.times(100000);
// addPoint(plot, "Moon-Earth normal", java.awt.Color.pink, v.x[0],
// v.x[1], v.x[2]);
//
// VectorN vr = Eph.rotationAxis.times(100000);
// addPoint(plot, "rot axis", java.awt.Color.MAGENTA, vr.x[0], vr.x[1],
// vr.x[2]);

