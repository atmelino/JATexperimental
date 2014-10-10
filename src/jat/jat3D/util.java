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

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;

import javax.media.j3d.GraphicsConfigTemplate3D;
import javax.media.j3d.Transform3D;
import javax.vecmath.Matrix4f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

public class util {

	public boolean check_for_Java3D_a() {

		try {
			Class.forName("javax.media.j3d.J3DBuffer");
		} catch (final ClassNotFoundException e) {
			// 
			return false;
		}
		return true;
	}

	public boolean check_for_Java3D_b() {

		try {
			GraphicsConfigTemplate3D gconfigTemplate = new GraphicsConfigTemplate3D();
			GraphicsConfiguration config = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
					.getBestConfiguration(gconfigTemplate);
		} catch (Error e) // You shouldn't normally catch java.lang.Error...
							// this is an exception
		{
			System.out.println("Java3D binaries not installed");
			return false;
		}
		return true;
	}

	public static void print(String title, Vector3f v) {
		System.out.println(title);
		System.out.println(v.x + " " + v.y + " " + v.z );
	}

	public static void print(String title, Point3d p) {
		System.out.println(title);
		System.out.println(p.x + " " + p.y + " " + p.z );
	}

	public static void print(String title, Matrix4f mat) {

		System.out.println(title);
		System.out.println(mat.m00 + " " + mat.m01 + " " + mat.m02 + " " + mat.m03);
		System.out.println(mat.m10 + " " + mat.m11 + " " + mat.m12 + " " + mat.m13);
		System.out.println(mat.m20 + " " + mat.m21 + " " + mat.m22 + " " + mat.m23);
		System.out.println(mat.m30 + " " + mat.m31 + " " + mat.m32 + " " + mat.m33);
		System.out.println();
	}

	public static void print_matrix_of_transform3D(String title, Transform3D t3) {
		Matrix4f mat = new Matrix4f();
		t3.get(mat);
		System.out.println(title);
		System.out.println(mat.m00 + " " + mat.m01 + " " + mat.m02 + " " + mat.m03);
		System.out.println(mat.m10 + " " + mat.m11 + " " + mat.m12 + " " + mat.m13);
		System.out.println(mat.m20 + " " + mat.m21 + " " + mat.m22 + " " + mat.m23);
		System.out.println(mat.m30 + " " + mat.m31 + " " + mat.m32 + " " + mat.m33);
		System.out.println();
	}
}
