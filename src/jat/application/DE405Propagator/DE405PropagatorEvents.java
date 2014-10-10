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

package jat.application.DE405Propagator;

import jat.core.ephemeris.DE405Body.body;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

class DE405PropagatorEvents implements ActionListener, ItemListener {
	DE405PropagatorGUI dpGUI;
	DE405PropagatorMain dpMain;

	// DE405PropagatorParameters dpParam;

	public DE405PropagatorEvents(DE405PropagatorMain dpMain) {
		this.dpMain = dpMain;
		// this.dpParam = dpMain.dpParam;
	}

	public void actionPerformed(ActionEvent ev) {

		// Read in values
		if (ev.getSource() == dpMain.dpGUI.btnPlot) {
			this.dpGUI = dpMain.dpGUI;
			// System.out.println("plot button pressed");

			dpGUI.GUIToParam();
			dpMain.dpPlot.plot.removeAllPlots();
			dpMain.dpPlot.add_scene();
		}

		if (ev.getSource() == dpMain.dpGUI.comboBoxFrame) {
			System.out.println("[DE405PropagatorEvents] Frame combo box");
		}

		if (ev.getSource() == dpMain.dpGUI.comboBoxParameterSet) {
			//System.out.println("[DE405PropagatorEvents] ParameterSet combo box");

			boolean changed = true;
			if (changed) {

				int index = dpMain.dpGUI.comboBoxParameterSet.getSelectedIndex();
				dpMain.dpParam = dpMain.ParameterSetList.get(index);
				dpMain.dpGUI.ParamToGUI();
			}

		}

	}

	// }// End of ActionPerformed

	@Override
	public void itemStateChanged(ItemEvent e) {

		Object source = e.getItemSelectable();

		if (source == dpMain.dpGUI.chckbxRotation) {
			System.out.println("rot");
			if (dpMain.dpGUI.chckbxRotation.isSelected())
				dpMain.dpPlot.plot.plotCanvas.timer.start();
			else
				dpMain.dpPlot.plot.plotCanvas.timer.stop();
		}

		if (source == dpMain.dpGUI.chckbxSun)
			dpMain.dpParam.bodyGravOnOff[body.SUN.ordinal()] = dpMain.dpGUI.chckbxSun.isSelected();
		if (source == dpMain.dpGUI.chckbxMercury)
			dpMain.dpParam.bodyGravOnOff[body.MERCURY.ordinal()] = dpMain.dpGUI.chckbxMercury.isSelected();
		if (source == dpMain.dpGUI.chckbxVenus)
			dpMain.dpParam.bodyGravOnOff[body.VENUS.ordinal()] = dpMain.dpGUI.chckbxVenus.isSelected();
		if (source == dpMain.dpGUI.chckbxEarth)
			dpMain.dpParam.bodyGravOnOff[body.EARTH.ordinal()] = dpMain.dpGUI.chckbxEarth.isSelected();
		if (source == dpMain.dpGUI.chckbxMars)
			dpMain.dpParam.bodyGravOnOff[body.EARTH.ordinal()] = dpMain.dpGUI.chckbxMars.isSelected();
		if (source == dpMain.dpGUI.chckbxMoon)
			dpMain.dpParam.bodyGravOnOff[body.MOON.ordinal()] = dpMain.dpGUI.chckbxMoon.isSelected();
		if (source == dpMain.dpGUI.chckbxJupiter)
			dpMain.dpParam.bodyGravOnOff[body.JUPITER.ordinal()] = dpMain.dpGUI.chckbxJupiter.isSelected();
		if (source == dpMain.dpGUI.chckbxSaturn)
			dpMain.dpParam.bodyGravOnOff[body.JUPITER.ordinal()] = dpMain.dpGUI.chckbxSaturn.isSelected();

	}

}
