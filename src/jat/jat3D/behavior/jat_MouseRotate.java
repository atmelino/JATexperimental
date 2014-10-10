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

import java.awt.AWTEvent;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

import javax.media.j3d.Transform3D;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnAWTEvent;
import javax.media.j3d.WakeupOnBehaviorPost;
import javax.vecmath.Point3f;

import com.sun.j3d.utils.behaviors.mouse.MouseBehavior;
import com.sun.j3d.utils.behaviors.mouse.MouseBehaviorCallback;
import com.sun.j3d.utils.universe.ViewingPlatform;


public class jat_MouseRotate extends MouseBehavior {
	double x_angle, y_angle;
	double x_factor = .01;
	double y_factor = .01;
	public ViewingPlatform myvp;
	int i = 0;
	private MouseBehaviorCallback callback = null;
	Point3f viewingCenter = new Point3f(0, 0, 0);
	jat_Rotate jat_rotate;
	

	public jat_MouseRotate(jat_Rotate jat_rotate) {
		super(0);
		this.jat_rotate=jat_rotate;
	}

	public void setViewingCenter(Point3f viewingCenter) {
		this.viewingCenter = viewingCenter;
	}


	public void initialize() {
		super.initialize();
		x_angle = 0;
		y_angle = 0;
		if ((flags & INVERT_INPUT) == INVERT_INPUT) {
			invert = true;
			x_factor *= -1;
			y_factor *= -1;
		}
	}

	/**
	 * Return the x-axis movement multipler.
	 **/
	public double getXFactor() {
		return x_factor;
	}

	/**
	 * Return the y-axis movement multipler.
	 **/
	public double getYFactor() {
		return y_factor;
	}

	/**
	 * Set the x-axis amd y-axis movement multipler with factor.
	 **/
	public void setFactor(double factor) {
		x_factor = y_factor = factor;
	}

	/**
	 * Set the x-axis amd y-axis movement multipler with xFactor and yFactor
	 * respectively.
	 **/
	public void setFactor(double xFactor, double yFactor) {
		x_factor = xFactor;
		y_factor = yFactor;
	}

	public void processStimulus(Enumeration criteria) {
		WakeupCriterion wakeup;
		AWTEvent[] events;
		MouseEvent evt;
		// int id;
		// int dx, dy;

		while (criteria.hasMoreElements()) {
			wakeup = (WakeupCriterion) criteria.nextElement();
			if (wakeup instanceof WakeupOnAWTEvent) {
				events = ((WakeupOnAWTEvent) wakeup).getAWTEvent();
				if (events.length > 0) {
					evt = (MouseEvent) events[events.length - 1];
					doProcess(evt);
				}
			}

			else if (wakeup instanceof WakeupOnBehaviorPost) {
				while (true) {
					// access to the queue must be synchronized
					synchronized (mouseq) {
						if (mouseq.isEmpty())
							break;
						evt = (MouseEvent) mouseq.remove(0);
						// consolidate MOUSE_DRAG events
						while ((evt.getID() == MouseEvent.MOUSE_DRAGGED) && !mouseq.isEmpty()
								&& (((MouseEvent) mouseq.get(0)).getID() == MouseEvent.MOUSE_DRAGGED)) {
							evt = (MouseEvent) mouseq.remove(0);
						}
					}
					doProcess(evt);
				}
			}
		}
		wakeupOn(mouseCriterion);
	}

	void doProcess(MouseEvent evt) {
		int id;
		int dx, dy;

		// System.out.println("enter doProcess "+i++);
		processMouseEvent(evt);
		if (((buttonPress) && ((flags & MANUAL_WAKEUP) == 0)) || ((wakeUp) && ((flags & MANUAL_WAKEUP) != 0))) {
			id = evt.getID();
			if ((id == MouseEvent.MOUSE_DRAGGED) && !evt.isMetaDown() && !evt.isAltDown()) {
				x = evt.getX();
				y = evt.getY();
				// System.out.println("x " + x + "y " + y);

				dx = x - x_last;
				dy = y - y_last;

				if (!reset) {
					x_angle = dx * x_factor;
					y_angle = dy * y_factor;

					jat_rotate.jat_rotate((float) x_angle, (float) y_angle);

					if (callback != null)
						callback.transformChanged(MouseBehaviorCallback.ROTATE, currXform);
				} else {
					reset = false;
				}

				x_last = x;
				y_last = y;
			} else if (id == MouseEvent.MOUSE_PRESSED) {
				x_last = evt.getX();
				y_last = evt.getY();
			}
		}
		// System.out.println("leave doProcess");
	}

	//float last_theta, last_phi;

	// public void jat_rotate(float x_angle, float y_angle) {
	// // The view platform
	// TransformGroup myvpt = myvp.getViewPlatformTransform();
	// Transform3D Trans = new Transform3D();
	// myvpt.getTransform(Trans);
	//
	// // the current Cartesian view position in space
	// Vector3f v_current_cart1 = new Vector3f();
	// Trans.get(v_current_cart1);
	//
	// // the current Cartesian view position relative to the sphere
	// Vector3f v_current_cart2 = new Vector3f();
	// v_current_cart2.x = v_current_cart1.x - viewingCenter.x;
	// v_current_cart2.y = v_current_cart1.y - viewingCenter.y;
	// v_current_cart2.z = v_current_cart1.z - viewingCenter.z;
	//
	// // the current spherical view position relative to the sphere
	// Vector3f v_current_sphere;
	// v_current_sphere =
	// CoordTransform3D.Cartesian_to_Spherical(v_current_cart2);
	// // util.print("view sphere", v_current_sphere);
	//
	// // the new spherical view position relative to the sphere
	// v_current_sphere.y -= y_angle;
	// v_current_sphere.z -= x_angle;
	//
	// // the new Cartesian view position relative to the sphere
	// Vector3f v = CoordTransform3D.Spherical_to_Cartesian(v_current_sphere);
	//
	// // the new Cartesian view position in space
	// v.x += viewingCenter.x ;
	// v.y += viewingCenter.y ;
	// v.z += viewingCenter.z ;
	//
	// Transform3D lookAt = new Transform3D();
	// lookAt.lookAt(new Point3d(v.x, v.y, v.z), new Point3d(viewingCenter), new
	// Vector3d(0, 0, 1.0));
	// lookAt.invert();
	//
	// myvpt.setTransform(lookAt);
	// }

	/**
	 * Users can overload this method which is called every time the Behavior
	 * updates the transform
	 * 
	 * Default implementation does nothing
	 */
	public void transformChanged(Transform3D transform) {
	}

	/**
	 * The transformChanged method in the callback class will be called every
	 * time the transform is updated
	 */
	public void setupCallback(MouseBehaviorCallback callback) {
		this.callback = callback;
	}
}
