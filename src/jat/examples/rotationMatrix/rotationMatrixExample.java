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

package jat.examples.rotationMatrix;

import jat.core.math.MathUtilsAPL;
import jat.core.math.arbaxisrot.RotationMatrixFull;

public class rotationMatrixExample {
	private double a = 0;
	private double b = 0;
	private double c = 0;
	private double u = 0;
	private double v = 0;
	private double w = 0;
	private double theta = 0;
	RotationMatrixFull rM;
	double degrees;
	double[] point;
	double[] direction;

	void zAxis(double degrees, double[] point) {
		// Test rotation about the z-axis
		// (u=0,v=0,w=1)
		w = 1;
		// degrees = 90;
		theta = MathUtilsAPL.Radians(degrees);

		rM = new RotationMatrixFull(a, b, c, u, v, w, theta);

		double[] result = rM.timesXYZ(point);

		// rM.logMatrix();

		System.out.println("Degrees: " + degrees);
		printVector("in ", point);
		printVector("out", result);
	}

	void lineThroughOrigin(double degrees, double[] direction, double[] point) {
		// Test rotation about the line (u=d[0],v=d[1],w=d[2])
		u = direction[0];
		v = direction[1];
		w = direction[2];
		theta = MathUtilsAPL.Radians(degrees);

		rM = new RotationMatrixFull(a, b, c, u, v, w, theta);

		double[] result = rM.timesXYZ(point);

		// rM.logMatrix();

		System.out.println("Degrees: " + degrees);
		printVector("in ", point);
		printVector("out", result);
	}

	void doExample() {
		point = new double[] { 1, 0, 0 };
		System.out.println("z=axis rotation");
		zAxis(00, point);
		zAxis(45, point);
		zAxis(90, point);
		zAxis(180, point);
		point = new double[] { 1, 0, 1 };
		zAxis(45, point);

		System.out.println("rotation axis through origin");
		direction = new double[] { 1, 1, 0 };
		point = new double[] { 1, 0, 0 };
		printVector("axis", direction);
		lineThroughOrigin(0, direction, point);
		lineThroughOrigin(90, direction, point);
		lineThroughOrigin(180, direction, point);
		lineThroughOrigin(270, direction, point);
		lineThroughOrigin(359, direction, point);

	}

	void printVector(String title, double[] v) {

		String format = title + " %10.5f %10.5f %10.5f";
		System.out.printf(format, v[0], v[1], v[2]);
		System.out.println();

	}

	public static void main(String[] args) {

		rotationMatrixExample m = new rotationMatrixExample();
		m.doExample();
	}

}
