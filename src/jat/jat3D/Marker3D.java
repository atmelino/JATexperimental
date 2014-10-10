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

import javax.vecmath.Point3f;


/**
 * @author Tobias Berthold
 * 
 */
public class Marker3D extends Body3D {

	public Sphere3D s;

	public Marker3D() {
		super();
		s=new Sphere3D(.05f, Colors.red, 0, 0, 0);
		addChild(s);
		addChild(new Line3D(new Point3f(0,0,-1.1f),new Point3f(0,0,1.1f),Colors.blue));
		
		
	}



} 

