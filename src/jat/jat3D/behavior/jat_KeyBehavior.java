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

// Original Code under LGPL
// http://java.freehep.org/freehep-java3d/license.html

package jat.jat3D.behavior;

import jat.jat3D.plot3D.JatPlot3D;

import java.awt.event.KeyEvent;

import javax.media.j3d.Behavior;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.WakeupOnAWTEvent;
import javax.vecmath.Point3f;

import com.sun.j3d.utils.universe.ViewingPlatform;

/**
 * A behaviour for 3d plots which defines certain keyboard events This is used
 * instead of the default KeyNavigatorBehavior to work around bug 4376368 which
 * causes the CPU used to go to 100% see
 * http://developer.java.sun.com/developer/bugParade/bugs/4376368.html
 * 
 * Use the arrow keys and page up/page down to move. Hold the shift key to
 * rotate. Use the Home key to restore the default rotation.
 * 
 * @author Joy Kyriakopulos (joyk@fnal.gov)
 * @version $Id: PlotKeyNavigatorBehavior.java 8584 2006-08-10 23:06:37Z duns $
 * 
 */
public class jat_KeyBehavior extends Behavior {
	private Transform3D init;
	public TransformGroup transformGroup;
	private WakeupOnAWTEvent wup;
	private float angle;
	Transform3D transformZ = new Transform3D();
	Transform3D currXform = new Transform3D();
	JatPlot3D jatplot3d;
	public ViewingPlatform myvp;
	Point3f viewingCenter = new Point3f(0, 0, 0);
	jat_Zoom jat_zoom;
	jat_Rotate jat_rotate;
	
		
	public jat_KeyBehavior(jat_Zoom jat_zoom,jat_Rotate jat_rotate) {
		super();
		this.jat_zoom = jat_zoom;
		this.jat_rotate = jat_rotate;
		this.wup = new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED);
		this.jatplot3d=jat_zoom.jatplot3d;
	}

	public void setViewingCenter(Point3f viewingCenter) {
		this.viewingCenter = viewingCenter;
	}

	public void setViewingPlatform(ViewingPlatform myvp) {
		this.myvp = myvp;
	}

	public jat_KeyBehavior(TransformGroup transformGroup, float moveStep, float rotStep) {
		super();
		new Transform3D();
		this.init = new Transform3D();
		this.transformGroup = transformGroup;
		this.wup = new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED);
		this.angle = (float) Math.toRadians(rotStep);
	}

	public jat_KeyBehavior(JatPlot3D jatPlot3D) {
		super();
		this.init = new Transform3D();
		this.wup = new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED);
		this.jatplot3d = jatPlot3D;
		this.transformGroup = jatPlot3D.jatScene;
	}

	public void initialize() {
		wakeupOn(wup);
		//transformGroup.getTransform(init);
	}

	// public void setViewingPlatform(ViewingPlatform myvp) {
	// this.myvp = myvp;
	// }

	public void processStimulus(java.util.Enumeration criteria) {
		KeyEvent event = (KeyEvent) (wup.getAWTEvent())[0];
		int keyCode = event.getKeyCode();
		boolean shift = (event.getModifiers() & event.SHIFT_MASK) != 0;

		switch (keyCode) {
		case KeyEvent.VK_UP:
			jat_rotate.jat_rotate(0, -.02f);
			// jatPlot3D.jat_rotate(0, .02f);
			break;
		case KeyEvent.VK_DOWN:
			jat_rotate.jat_rotate(0, .02f);
			break;
		case KeyEvent.VK_LEFT:
			jat_rotate.jat_rotate(-.02f, 0);
			break;
		case KeyEvent.VK_RIGHT:
			jat_rotate.jat_rotate(.02f, 0);
			break;
		case KeyEvent.VK_PAGE_UP:
			// move(0f, 0f, 1f, shift);
			break;
		case KeyEvent.VK_PAGE_DOWN:
			// move(0f, 0f, -1f, shift);
			break;
		case KeyEvent.VK_HOME:
			transformGroup.setTransform(init);
			break;
		case KeyEvent.VK_EQUALS:
			// System.out.println("plus pressed");
			jat_zoom.jat_zoom(1);
			break;
		case KeyEvent.VK_MINUS:
			// System.out.println("minus pressed");
			jat_zoom.jat_zoom(-1);
			break;
		}
		wakeupOn(wup);
	}

}
