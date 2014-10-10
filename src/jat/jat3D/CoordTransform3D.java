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

import jat.core.math.MathUtilsAPL;

import javax.vecmath.Vector3f;

public class CoordTransform3D {

	//Test: (1.5,1.5,1) -> r=2.345207879911715, theta=1.1302856637901981, phi=0.7853981633974483
	
	public static Vector3f Cartesian_to_Spherical(Vector3f coord) {
		// r, theta, phi
		if (coord.x == 0)
			
		 coord.x = MathUtilsAPL.MACHEPS;
		Vector3f out = new Vector3f();
	
		out.x = (float) Math.sqrt((coord.x * coord.x) + (coord.y * coord.y) + (coord.z * coord.z));
		out.y = (float) Math.acos(coord.z / out.x);
		out.z = (float) Math.atan2((double)coord.y , (double)coord.x);
	
		return out;
	}

	//Test: (1.5,1.5,1) -> r=2.345207879911715, theta=1.1302856637901981, phi=0.7853981633974483
	
	public static Vector3f Spherical_to_Cartesian(Vector3f coord) {
		Vector3f out = new Vector3f();
		double r = coord.x;
		double theta = coord.y;
		double phi = coord.z;
		out.x = (float) (r * Math.sin(theta) * Math.cos(phi));
		out.y = (float) (r * Math.sin(theta) * Math.sin(phi));
		out.z = (float) (r * Math.cos(theta));
		return out;
	}

	
	
	
}
