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

// Original Code:
// Copyright (c) 2007 Sun Microsystems, Inc. All rights reserved.

package jat.jat3D.behavior;

import jat.jat3D.CoordTransform3D;
import jat.jat3D.plot3D.JatPlot3D;

import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.universe.ViewingPlatform;

/**
 * jat_Rotate is a Java3D behavior object that lets users control the rotation
 * of an object via a mouse or keyboard.
 * <p>
 * In JAT, it works a bit different from the typical use. Instead of rotating
 * the scene, it moves the viewing platform in orbits around the scene, while
 * always looking at the scene, with the z-direction being up. Then,
 */
public class jat_Rotate {
	double x_angle, y_angle;
	double x_factor = .01;
	double y_factor = .01;
	public ViewingPlatform myvp;
	Point3f viewingCenter = new Point3f(0, 0, 0);
	JatPlot3D jatplot3d;
	private Vector3f v_current_sphere;
	
	public Vector3f getV_current_sphere() {
		// The view platform
		TransformGroup myvpt = myvp.getViewPlatformTransform();
		Transform3D Trans = new Transform3D();
		myvpt.getTransform(Trans);

		// the current Cartesian view position in space
		Vector3f v_current_cart1 = new Vector3f();
		Trans.get(v_current_cart1);

		// the current Cartesian view position relative to the sphere
		Vector3f v_current_cart2 = new Vector3f();
		v_current_cart2.x = v_current_cart1.x - viewingCenter.x;
		v_current_cart2.y = v_current_cart1.y - viewingCenter.y;
		v_current_cart2.z = v_current_cart1.z - viewingCenter.z;

		// the current spherical view position relative to the sphere
		//update_v_current_sphere();
		v_current_sphere = CoordTransform3D.Cartesian_to_Spherical(v_current_cart2);
		// util.print("view sphere", v_current_sphere);
		return v_current_sphere;
	}

	public jat_Rotate(JatPlot3D jatplot3d) {
		super();
		this.jatplot3d = jatplot3d;
		myvp = jatplot3d.universe.getViewingPlatform();
		this.viewingCenter=jatplot3d.viewingCenter;
	}

	public  void jat_rotate(float x_angle, float y_angle) {
		// The view platform
		TransformGroup myvpt = myvp.getViewPlatformTransform();
		Transform3D Trans = new Transform3D();
		myvpt.getTransform(Trans);

		// the current Cartesian view position in space
		Vector3f v_current_cart1 = new Vector3f();
		Trans.get(v_current_cart1);

		// the current Cartesian view position relative to the sphere
		Vector3f v_current_cart2 = new Vector3f();
		v_current_cart2.x = v_current_cart1.x - viewingCenter.x;
		v_current_cart2.y = v_current_cart1.y - viewingCenter.y;
		v_current_cart2.z = v_current_cart1.z - viewingCenter.z;

		// the current spherical view position relative to the sphere
		//update_v_current_sphere();
		v_current_sphere = CoordTransform3D.Cartesian_to_Spherical(v_current_cart2);
		// util.print("view sphere", v_current_sphere);

		// the new spherical view position relative to the sphere
		v_current_sphere.y -= y_angle;
		v_current_sphere.z -= x_angle;

		// the new Cartesian view position relative to the sphere
		Vector3f v = CoordTransform3D.Spherical_to_Cartesian(v_current_sphere);

		// the new Cartesian view position in space
		v.x += viewingCenter.x;
		v.y += viewingCenter.y;
		v.z += viewingCenter.z;

		Transform3D lookAt = new Transform3D();
		lookAt.lookAt(new Point3d(v.x, v.y, v.z), new Point3d(viewingCenter), new Vector3d(0, 0, 1.0));
		lookAt.invert();

		myvpt.setTransform(lookAt);

	}

	
	void update_v_current_sphere()
	{
		// The view platform
		TransformGroup myvpt = myvp.getViewPlatformTransform();
		Transform3D Trans = new Transform3D();
		myvpt.getTransform(Trans);

		// the current Cartesian view position in space
		Vector3f v_current_cart1 = new Vector3f();
		Trans.get(v_current_cart1);

		// the current Cartesian view position relative to the sphere
		Vector3f v_current_cart2 = new Vector3f();
		v_current_cart2.x = v_current_cart1.x - viewingCenter.x;
		v_current_cart2.y = v_current_cart1.y - viewingCenter.y;
		v_current_cart2.z = v_current_cart1.z - viewingCenter.z;

		v_current_sphere = CoordTransform3D.Cartesian_to_Spherical(v_current_cart2);
}
	
	
}
