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

public class jat_KeyBehavior_Translate extends Behavior {
	public TransformGroup transformGroup;
	private WakeupOnAWTEvent wup;
	Transform3D transformZ = new Transform3D();
	Transform3D currXform = new Transform3D();
	JatPlot3D jatPlot3D;


	public jat_KeyBehavior_Translate(JatPlot3D jatPlot3D) {
		super();
		this.wup = new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED);
		this.jatPlot3D = jatPlot3D;
		this.transformGroup = jatPlot3D.jatScene;
	}

	public void initialize() {
		wakeupOn(wup);
	}

	// public void setViewingPlatform(ViewingPlatform myvp) {
	// this.myvp = myvp;
	// }

	public void processStimulus(java.util.Enumeration criteria) {
		KeyEvent event = (KeyEvent) (wup.getAWTEvent())[0];
		int keyCode = event.getKeyCode();

		switch (keyCode) {
		case KeyEvent.VK_UP:
			jatPlot3D.jat_translate(0f, 0f, .05f);
			break;
		case KeyEvent.VK_DOWN:
			jatPlot3D.jat_translate(0f, 0f, -.05f);
			break;
		case KeyEvent.VK_LEFT:
			jatPlot3D.jat_translate(-.05f, 0f, 0f);
			break;
		case KeyEvent.VK_RIGHT:
			jatPlot3D.jat_translate(.05f, 0f, 0f);
			//System.out.println("right");
			break;
		default:
			break;
		}
		wakeupOn(wup);
	}

}
