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

import jat.coreNOSA.algorithm.integrators.Printable;
import jat.coreNOSA.algorithm.integrators.RungeKuttaFehlberg78;
import jat.coreNOSA.cm.TwoBody;

public class TwoBodyCompare implements Printable {

	public TwoBodyCompare() {

	}

	public static void main(String[] args) {

		System.out.println("Comparing twobody orbit data");
		// create a Two Body orbit (elliptical)
		TwoBody orbit = new TwoBody(10000.0, 0.2, 0.0, 0.0, 0.0, 0.0);

		TwoBodyCompare rkt = new TwoBodyCompare();

		// create an RungeKuttaFehlberg78 integrator
		RungeKuttaFehlberg78 rk78f = new RungeKuttaFehlberg78();
		rk78f.setAccuracy(1.e-9);

		// initialize the state variables
		double[] x0 = orbit.randv();

		// set the final time to one orbit period
		double tf = orbit.period();

		// set the initial time to zero
		double t0 = 0.0;

		// integrate the equations
		double[] xf = rk78f.integrate(t0, x0, tf, orbit, rkt, true);

	}

	public void print(double t, double[] y) {

		System.out.println(t + " " + y[0] + " " + y[1]);

	}

}
