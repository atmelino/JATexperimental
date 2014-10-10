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

import jat.jat3D.plot3D.JatPlot3D;

import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.behaviors.mouse.MouseBehaviorCallback;
import com.sun.j3d.utils.universe.ViewingPlatform;

/**
 * @author Tobias Berthold
 *
 * jat_Zoom is called by mouse as well as keyboard-driven (or future input device) 
 * zoom requests.  
 */
public class jat_Zoom {
	double x_angle, y_angle;
	double x_factor = .01;
	double y_factor = .01;
	public ViewingPlatform myvp;
	Point3f viewingCenter = new Point3f(0, 0, 0);
	JatPlot3D jatplot3d;

	private MouseBehaviorCallback callback = null;

	public jat_Zoom(JatPlot3D jatplot3d) {
		super();
		this.jatplot3d = jatplot3d;
		myvp = jatplot3d.universe.getViewingPlatform();
		this.viewingCenter=jatplot3d.viewingCenter;
	}

	public void jat_zoom(float dy) {

		// The zoom factor
		float zoom;
		if (dy > 0)
			zoom = 0.96f;
		else
			zoom = 1.04f;

		// The view platform
		TransformGroup myvpt = myvp.getViewPlatformTransform();
		Transform3D Trans = new Transform3D();
		myvpt.getTransform(Trans);
		
		
		// the view platform position, including displacement
		Vector3f vabs = new Vector3f();
		Trans.get(vabs);
		// util.print("v", v);
		

		// the view platform position, relative to viewing center
		Vector3f v = new Vector3f();
		v.x=vabs.x-viewingCenter.x;
		v.y=vabs.y-viewingCenter.y;
		v.z=vabs.z-viewingCenter.z;
		

		// zoom in or our view platform, relative
		Point3d p = new Point3d();
		p.x = zoom * v.x;
		p.y = zoom * v.y;
		p.z = zoom * v.z;
		// util.print("p", p);

		// get new absolute view platform position
		p.x+=viewingCenter.x;
		p.y+=viewingCenter.y;
		p.z+=viewingCenter.z;
		
		
		
		Transform3D lookAt = new Transform3D();
		lookAt.lookAt(p, new Point3d(viewingCenter), new Vector3d(0, 0, 1.0));
		lookAt.invert();
		// update_user();
		myvpt.setTransform(lookAt);
		// if (get_vp_t().length() > 10.f) {
		adjustbox();
		// }

		//if (callback != null)
			//callback.transformChanged(MouseBehaviorCallback.ZOOM, currXform);	
	
	}

	public void adjustbox() {
		boolean changed = false;
		float new_distance = jatplot3d.get_vp_t().length();

		if (new_distance > 10) {
			changed = true;
			jatplot3d.exponent += 1;
			// System.out.println("nd>10 cd<10");
		}
		if (new_distance < 1) {
			changed = true;
			jatplot3d.exponent -= 1;
			// System.out.println("nd>10 cd<10");
		}

		if (changed)
			new_box(new_distance);
	}

	void new_box(float new_distance) {
		float tf_factor = (float) Math.pow(10, jatplot3d.exponent);
		float factor;
		if (new_distance > 10.f)
			factor = 0.1f;
		else
			factor = 10.f;

		// scale scene to fit inside box
		jatplot3d.zoomScene(1.f / tf_factor);
		// and move viewer accordingly
		Vector3f v = jatplot3d.get_vp_t();
		Point3d p = new Point3d(v.x * factor, v.y * factor, v.z * factor);
		Transform3D lookAt = new Transform3D();
		lookAt.lookAt(p, new Point3d(0.0, 0.0, 0.0), new Vector3d(0, 0, 1.0));
		lookAt.invert();
		jatplot3d.universe.getViewingPlatform().getViewPlatformTransform().setTransform(lookAt);

		// System.out.println("zoom , label");
		jatplot3d.bbox.setLabels(jatplot3d.exponent);

		// jatplot3d.bbox.xAxis.setLabel("X 10^" + jatplot3d.exponent + " km");
		// jatplot3d.bbox.xAxis.apply();
	}



	/**
	 * Users can overload this method which is called every time the Behavior
	 * updates the 
	 * 
	 * Default implementation does nothing
	 */
	public void viewChanged(Transform3D transform) {
	}

	/**
	 * The transformChanged method in the callback class will be called every
	 * time the transform is updated
	 */
	public void setupCallback(MouseBehaviorCallback callback) {
		this.callback = callback;
	}


}
