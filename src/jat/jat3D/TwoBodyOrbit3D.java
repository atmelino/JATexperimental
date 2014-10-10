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

package jat.jat3D;

import jat.core.cm.TwoBodyAPL;
import jat.coreNOSA.algorithm.integrators.Printable;
import jat.coreNOSA.cm.Constants;
import jat.coreNOSA.math.MatrixVector.data.VectorN;

import javax.media.j3d.GeometryArray;
import javax.media.j3d.LineStripArray;
import javax.media.j3d.Shape3D;
import javax.vecmath.Color3b;
import javax.vecmath.Color3f;

/**
 * @author Tobias Berthold
 * @version 1.0
 */
public class TwoBodyOrbit3D extends Shape3D implements Printable {
	public double[] coords;
	public double[] t, x, y, z;
	double t0; // initial time
	double tf; // final time
	int j = 0;
	// Color3f color = Colors.pink;
	Color3f color;
	Color3b colorb = new Color3b();
	private int steps = 500;
	Constants c = new Constants();
	double mu = Constants.GM_Sun / 1.e9;
	VectorN r = new VectorN(100000000, 0, 0);
	VectorN v = new VectorN(0, 30, 0);
	public TwoBodyAPL sat;

	public TwoBodyOrbit3D(double[] coords) {
		this.coords = coords;
	}

	public TwoBodyOrbit3D() {
		draw_orbit();
	}

	public TwoBodyOrbit3D(double mu, VectorN r, VectorN v) {
		this.mu = mu;
		this.r = r;
		this.v = v;
		// create a TwoBody orbit using orbit elements
		sat = new TwoBodyAPL(mu, r, v);
		// find out the period of the orbit
		tf = sat.period();
		color = Colors.pink;
		draw_orbit();
	}

	public TwoBodyOrbit3D(double mu, VectorN r, VectorN v, double t0, double tf) {

		this.t0 = t0;
		this.tf = tf;
		this.mu = mu;
		this.r = r;
		this.v = v;
		// create a TwoBody orbit using orbit elements
		sat = new TwoBodyAPL(mu, r, v);
		color = Colors.pink;
		draw_orbit();
	}

	public TwoBodyOrbit3D(double mu, VectorN r, VectorN v, double t0, double tf, Color3f color) {
		this.t0 = t0;
		this.tf = tf;
		this.mu = mu;
		this.r = r;
		this.v = v;
		// create a TwoBody orbit using orbit elements
		sat = new TwoBodyAPL(mu, r, v);
		this.color = color;
		draw_orbit();
	}

	public void print(double time, double[] pos) {
		// also print to the screen for warm fuzzy feeling
		//System.out.println(j + "  " + time + " " + pos[0] + " " + pos[1] + " " + pos[2]);
		t[j] = time;
		x[j] = pos[0];
		y[j] = pos[1];
		z[j] = pos[2];
		// coords[j + 0] = pos[0];
		// coords[j + 1] = pos[1];
		// coords[j + 2] = pos[2];
		j++;
	}

	public VectorN getPosition(double t0, double tf) {
		sat.propagate(t0, tf, this, true, steps);
		return sat.getR();
	}

	private void draw_orbit() {

		t = new double[steps + 2];
		x = new double[steps + 2];
		y = new double[steps + 2];
		z = new double[steps + 2];

		sat.propagate(t0, tf, this, true, steps);

		// Copy data into coords array
		// coords = new double[3 * steps + 6];
		int count = j;
		coords = new double[count * 3];
		for (int k = 0; k < count; k++) {
			coords[k * 3 + 0] = x[k];
			coords[k * 3 + 1] = y[k];
			coords[k * 3 + 2] = z[k];
		}
		int num_vert = coords.length / 3;
		int[] stripLengths = { num_vert };

		LineStripArray myLines = new LineStripArray(num_vert, GeometryArray.COORDINATES | GeometryArray.COLOR_3,
				stripLengths);
		Color3f colors[] = new Color3f[num_vert];
		for (int i = 0; i < num_vert; i++)
			colors[i] = color;

		// colors[i] = rainbow.colorFor(20);

		myLines.setColors(0, colors);
		myLines.setCoordinates(0, coords);

		this.setGeometry(myLines);
	}
}
