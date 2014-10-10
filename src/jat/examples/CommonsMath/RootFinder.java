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

package jat.examples.CommonsMath;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;


public class RootFinder {

	private static class MyFunction implements UnivariateFunction {
		public double value(double x) {
			double y = x * x - 2.;
			return y;
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final double relativeAccuracy = 1.0e-12;
		final double absoluteAccuracy = 1.0e-8;

		UnivariateFunction function = new MyFunction();
		UnivariateSolver nonBracketing = new BrentSolver(relativeAccuracy, absoluteAccuracy);
//		double baseRoot = bs.solve(100, function, -2.0, 0);

		System.out.println("Roots of f(x)=x^2-2: " );
		double baseRoot;
		baseRoot= nonBracketing.solve(100, function, -2.0, 0);
		System.out.println("root1: " + baseRoot);
		baseRoot = nonBracketing.solve(100, function, 0, 2.0);
		System.out.println("root2: " + baseRoot);

//		System.out.println("rs: " + Math.sqrt(2.));

	}

}

//double rp = 1, M=10000, Mp = 500;
//double rrp = rp*rp, rp2 = 2.0*rp; // shorthand variables for powers
//of rp
//double[] c = { -rrp*rrp, rp2*rrp, -(Mp/M+1)*rrp, rrp, rp2, 1.0 };
//
//PolynomialFunction lagrangian = new PolynomialFunction(c);
//LaguerreSolver solver = new LaguerreSolver();
//double rs = solver.solve(100, lagrangian, rp, 2*rp);
//System.out.println("rs: "+rs);
