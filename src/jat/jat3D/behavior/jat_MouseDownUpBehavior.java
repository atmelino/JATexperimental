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

import java.awt.AWTEvent;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

import javax.media.j3d.Behavior;
import javax.media.j3d.Bounds;
import javax.media.j3d.Transform3D;
import javax.media.j3d.WakeupCondition;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnAWTEvent;
import javax.media.j3d.WakeupOr;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.universe.ViewingPlatform;

public class jat_MouseDownUpBehavior extends Behavior {
	WakeupOnAWTEvent w1 = new WakeupOnAWTEvent(MouseEvent.MOUSE_RELEASED);
	WakeupCriterion[] w2 = { w1 };
	WakeupCondition w = new WakeupOr(w2);
	WakeupOnAWTEvent wu1 = new WakeupOnAWTEvent(MouseEvent.MOUSE_PRESSED);
	WakeupCriterion[] wu2 = { wu1 };
	WakeupCondition wu = new WakeupOr(wu2);
	public ViewingPlatform myvp;
	JatPlot3D jatPlot3D;

	public jat_MouseDownUpBehavior(Bounds bound) {
		this.setSchedulingBounds(bound);
	}

	public jat_MouseDownUpBehavior(JatPlot3D jatPlot3D) {
		this.jatPlot3D = jatPlot3D;
	}

	public void setViewingPlatform(ViewingPlatform myvp) {
		this.myvp = myvp;
	}

	public void initialize() {
		// Establish initial wakeup criteria
		wakeupOn(wu);
	}

	/**
	 * Override Behavior's stimulus method to handle the event.
	 */
	public void processStimulus(Enumeration criteria) {
		WakeupOnAWTEvent ev;
		WakeupCriterion genericEvt;
		AWTEvent[] events;

		while (criteria.hasMoreElements()) {
			genericEvt = (WakeupCriterion) criteria.nextElement();
			if (genericEvt instanceof WakeupOnAWTEvent) {
				ev = (WakeupOnAWTEvent) genericEvt;
				events = ev.getAWTEvent();
				processSwitchEvent(events);
			}
		}
	}

	/**
	 * Process a mouse up or down event
	 */
	void processSwitchEvent(AWTEvent[] events) {

		for (int i = 0; i < events.length; ++i) {
			if (events[i] instanceof MouseEvent) {
				MouseEvent event = (MouseEvent) events[i];
				if (event.getID() == MouseEvent.MOUSE_PRESSED) {
					// default
					// Set wakeup criteria for next time
					//System.out.println("mouse pressed");
					wakeupOn(w);
					break;
				} else if (event.getID() == MouseEvent.MOUSE_RELEASED) {
					//System.out.println("mouse released");
					Transform3D tf = new Transform3D();
					myvp.getViewPlatformTransform().getTransform(tf);
					Vector3f vf = new Vector3f();
					tf.get(vf);
					//System.out.println("distance from center " + vf.length());
					//jatPlot3D.adjustbox();
					// Set wakeup criteria for next time
					wakeupOn(wu);
					break;
				}
			}
		}
	}

}
