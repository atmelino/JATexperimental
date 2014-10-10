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

import javax.vecmath.Color3f;

import com.sun.j3d.utils.geometry.ColorCube;


/**
 * @author Tobias Berthold
 * Generic color cube
 *
 */
public class ColorCube3D extends Body3D
{
	/**
	 * Constructor.
	 * @param size size
	 */
	public ColorCube3D(float size)
	{
//		this.size=size;
		addChild(new ColorCube(size));
	}

	public ColorCube3D(double size)
	{
//		this.size=size;
		addChild(new ColorCube(size));
	}

	public ColorCube3D(float size, Color3f color, float x, float y, float z)
	{
		addChild(new ColorCube(size));
		set_position(x, y, z);
	}
}
