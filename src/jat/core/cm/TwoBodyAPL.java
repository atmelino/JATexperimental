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

package jat.core.cm;

import jat.coreNOSA.algorithm.integrators.Printable;
import jat.coreNOSA.cm.Constants;
import jat.coreNOSA.cm.TwoBody;
import jat.coreNOSA.math.MatrixVector.data.Matrix;
import jat.coreNOSA.math.MatrixVector.data.VectorN;

import java.util.ArrayList;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;

public class TwoBodyAPL extends TwoBody implements FirstOrderDifferentialEquations {
	double initial_ta;
	public ArrayList<Double> time = new ArrayList<Double>();
	public ArrayList<Double> xsol = new ArrayList<Double>();
	public ArrayList<Double> ysol = new ArrayList<Double>();
	public ArrayList<Double> zsol = new ArrayList<Double>();

	public TwoBodyAPL(double mu, VectorN r, VectorN v) {
		super(mu, r, v);
		initial_ta = ta;
	}

	public TwoBodyAPL(double a, double e, double i, double raan, double w, double ta) {
		super(a, e, i, raan, w, ta);
	}

	@Override
	public void computeDerivatives(double t, double[] y, double[] yDot) {

		Vector3D r = new Vector3D(y[0], y[1], y[2]);
		double rnorm = r.getNorm();
		double r3 = rnorm * rnorm * rnorm;
		double k = -1. * this.mu / r3;
		yDot[0] = y[3];
		yDot[1] = y[4];
		yDot[2] = y[5];
		yDot[3] = k * y[0];
		yDot[4] = k * y[1];
		yDot[5] = k * y[2];
	}

	public StepHandler stepHandler = new StepHandler() {
		public void init(double t0, double[] y0, double t) {
		}

		public void handleStep(StepInterpolator interpolator, boolean isLast) {
			double t = interpolator.getCurrentTime();
			double[] y = interpolator.getInterpolatedState();
			// System.out.println(t + " " + y[0] + " " + y[1]+ " " + y[2]);
			time.add(t);
			xsol.add(y[0]);
			ysol.add(y[1]);
			zsol.add(y[2]);
		}
	};

	@Override
	public int getDimension() {
		return 6;
	}

	public VectorN position(double t) {

		double[] temp = new double[6];

		// Determine step size
		double n = this.meanMotion();

		// determine initial E and M
		double sqrome2 = Math.sqrt(1.0 - this.e * this.e);
		double cta = Math.cos(this.ta);
		double sta = Math.sin(this.ta);
		double sine0 = (sqrome2 * sta) / (1.0 + this.e * cta);
		double cose0 = (this.e + cta) / (1.0 + this.e * cta);
		double e0 = Math.atan2(sine0, cose0);

		double ma = e0 - this.e * Math.sin(e0);

		ma = ma + n * t;
		double ea = solveKepler(ma, this.e);

		double sinE = Math.sin(ea);
		double cosE = Math.cos(ea);
		double den = 1.0 - this.e * cosE;

		double sinv = (sqrome2 * sinE) / den;
		double cosv = (cosE - this.e) / den;

		this.ta = Math.atan2(sinv, cosv);
		if (this.ta < 0.0) {
			this.ta = this.ta + 2.0 * Constants.pi;
		}

		temp = this.randv();
		this.rv = new VectorN(temp);

		// Reset everything to before
		this.ta = initial_ta;

		VectorN out = new VectorN(3);
		out.x[0] = temp[0];
		out.x[1] = temp[1];
		out.x[2] = temp[2];
		// out.print("sat pos at t");
		return out;

	}

	// propagates from whatever ta is starting from time t=0 relative to the
	// starting point
	public void propagate(double t0, double tf, Printable pr, boolean print_switch, double steps) {
		double[] temp = new double[6];
		// double ta_save = this.ta;
		this.steps = steps;

		// Determine step size
		double n = this.meanMotion();
		double period = this.period();
		double dt = period / steps;
		if ((t0 + dt) > tf) // check to see if we're going past tf
		{
			dt = tf - t0;
		}

		// determine initial E and M
		double sqrome2 = Math.sqrt(1.0 - this.e * this.e);
		double cta = Math.cos(this.ta);
		double sta = Math.sin(this.ta);
		double sine0 = (sqrome2 * sta) / (1.0 + this.e * cta);
		double cose0 = (this.e + cta) / (1.0 + this.e * cta);
		double e0 = Math.atan2(sine0, cose0);

		double ma = e0 - this.e * Math.sin(e0);

		// initialize t

		double t = t0;

		if (print_switch) {
			temp = this.randv();
			pr.print(t, temp);
		}

		while (t < tf) {
			ma = ma + n * dt;
			double ea = solveKepler(ma, this.e);

			double sinE = Math.sin(ea);
			double cosE = Math.cos(ea);
			double den = 1.0 - this.e * cosE;

			double sinv = (sqrome2 * sinE) / den;
			double cosv = (cosE - this.e) / den;

			this.ta = Math.atan2(sinv, cosv);
			if (this.ta < 0.0) {
				this.ta = this.ta + 2.0 * Constants.pi;
			}

			t = t + dt;

			temp = this.randv();
			this.rv = new VectorN(temp);

			if (print_switch) {
				pr.print(t, temp);
			}

			if ((t + dt) > tf) {
				dt = tf - t;
			}

		}
		// Reset everything to before
		this.ta = initial_ta;

	}

	public double[] randv(double ta) {
		double p = a * (1.0 - e * e);
		double cta = Math.cos(ta);
		double sta = Math.sin(ta);
		double opecta = 1.0 + e * cta;
		double sqmuop = Math.sqrt(this.mu / p);

		VectorN xpqw = new VectorN(6);
		xpqw.x[0] = p * cta / opecta;
		xpqw.x[1] = p * sta / opecta;
		xpqw.x[2] = 0.0;
		xpqw.x[3] = -sqmuop * sta;
		xpqw.x[4] = sqmuop * (e + cta);
		xpqw.x[5] = 0.0;

		Matrix cmat = PQW2ECI();

		VectorN rpqw = new VectorN(xpqw.x[0], xpqw.x[1], xpqw.x[2]);
		VectorN vpqw = new VectorN(xpqw.x[3], xpqw.x[4], xpqw.x[5]);

		VectorN rijk = cmat.times(rpqw);
		VectorN vijk = cmat.times(vpqw);

		double[] out = new double[6];

		for (int i = 0; i < 3; i++) {
			out[i] = rijk.x[i];
			out[i + 3] = vijk.x[i];
		}

		return out;
	}

	public double eccentricAnomaly(double ta) {
		double cta = Math.cos(ta);
		double e0 = Math.acos((e + cta) / (1.0 + e * cta));
		return e0;
	}

	public double meanAnomaly(double t) {

		return 2. * Math.PI * t / period();
	}

	public double t_from_ta() {

		double M = meanAnomaly();
		double P = period();

		return P * M / 2. / Math.PI;

	}

	public double ta_from_t(double t) {

		double M = meanAnomaly(t);
		double ea = solveKepler(M, this.e);

		double sinE = Math.sin(ea);
		double cosE = Math.cos(ea);
		double den = 1.0 - this.e * cosE;
		double sqrome2 = Math.sqrt(1.0 - this.e * this.e);
		double sinv = (sqrome2 * sinE) / den;
		double cosv = (cosE - this.e) / den;

		double ta = Math.atan2(sinv, cosv);
		if (this.ta < 0.0) {
			this.ta = this.ta + 2.0 * Constants.pi;
		}

		return ta;
	}

}
