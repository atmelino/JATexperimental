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
package jat.core.math;

public class MathUtilsAPL {

	final static double factor1 = Math.PI / 180.;
	final static double factor2 = 180. / Math.PI;

	public static double Radians(double Degrees) {
		return Degrees * factor1;
	}

	public static double Degrees(double Radians) {
		return Radians * factor2;
	}

	/**
	 * Machine precision for float.
	 */
	public final static float MACHEPS = 1.1920929E-7f;

	public static void calculateMachineEpsilonFloat() {
		float machEps = 1.0f;

		do {
			machEps /= 2.0f;
		} while ((float) (1.0 + (machEps / 2.0)) != 1.0);

		System.out.println("Calculated machine epsilon (float): " + machEps);
	}

	public static void main(String[] args) {

		System.out.println("2 PI radians = "+Degrees(2*Math.PI)+" Degrees");
		System.out.println("180 Degrees = "+Radians(180.)+" Radians");
	
		calculateMachineEpsilonFloat();
	
	}


}
