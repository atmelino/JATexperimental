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

import java.awt.event.KeyEvent;

import javax.media.j3d.Behavior;
import javax.media.j3d.WakeupOnAWTEvent;

public class jat_KeyBehavior_UserCall extends Behavior {
	private WakeupOnAWTEvent wup;

	public jat_KeyBehavior_UserCall() {
		super();
		this.wup = new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED);
	}

	public void initialize() {
		wakeupOn(wup);
	}

	public void processStimulus(java.util.Enumeration criteria) {
		KeyEvent event = (KeyEvent) (wup.getAWTEvent())[0];
		int keyCode = event.getKeyCode();

		switch (keyCode) {
		case KeyEvent.VK_UP:
			keyup();
			break;
		case KeyEvent.VK_DOWN:
			keydown();
			break;
		case KeyEvent.VK_LEFT:
			keyleft();
			break;
		case KeyEvent.VK_RIGHT:
			keyright();
			break;
		default:
			break;
		}
		wakeupOn(wup);
	}

	public void keyup(){}
	public void keydown(){}
	public void keyleft(){}
	public void keyright(){}
	
}
