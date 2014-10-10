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

package jat.application.porkChopPlot;

import jat.core.algorithm.optimization.DataArraySearch;
import jat.core.ephemeris.DE405Body.body;
import jat.core.spacetime.TimeAPL;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JOptionPane;

public class PorkChopPlotEvents implements ActionListener {

	PorkChopPlotGUI pcpGUI;
	PorkChopPlotMain main;
	PorkChopPlotParameters params;

	public PorkChopPlotEvents(PorkChopPlotGUI pcpGUI) {
		this.pcpGUI = pcpGUI;
	}

	public void setMain(PorkChopPlotMain main) {
		this.main = main;
	}

	public void actionPerformed(ActionEvent ev) {
		params = main.pcpParams;
		if (ev.getSource() == pcpGUI.btn_make_plot) {
			// System.out.println("make plot button");

			// retrieve parameters from GUI

			params.departure_planet=body.fromInt(main.pcpGUI.comboDepartPlanet.getSelectedIndex());
			params.arrival_planet=body.fromInt(main.pcpGUI.comboArrivalPlanet.getSelectedIndex());
			//System.out.println("[PorkChopPlot_Events.java] sel. index dep " + params.departure_planet + " sel. index planet " + params.arrival_planet);
			params.dep_year = pcpGUI.depart_date_picker.getModel().getYear();
			params.dep_month = pcpGUI.depart_date_picker.getModel().getMonth() + 1;
			params.dep_day = pcpGUI.depart_date_picker.getModel().getDay();
			params.arr_year = pcpGUI.arrival_date_picker.getModel().getYear();
			params.arr_month = pcpGUI.arrival_date_picker.getModel().getMonth() + 1;
			params.arr_day = pcpGUI.arrival_date_picker.getModel().getDay();

			// System.out.println(year+"/"+day);
			Object sp2 = main.pcpGUI.spinner_days.getValue();
			params.searchDays = Integer.parseInt(sp2.toString());
			Object sp1 = main.pcpGUI.spinner_steps.getValue();
			params.steps = Integer.parseInt(sp1.toString());

			params.search_depart_time_start = new TimeAPL(params.dep_year, params.dep_month,
					params.dep_day, 1, 1, 1);
			params.search_arrival_time_start = new TimeAPL(params.arr_year, params.arr_month,
					params.arr_day, 1, 1, 1);

			try {
				main.pcpPlot.pcplot_data.pcpPlot.make_porkchop_plot(
						params.departure_planet, params.arrival_planet,
						params.search_depart_time_start,
						params.search_arrival_time_start, params.searchDays,
						params.steps);
				main.pcpPlot.pcplot_data.update();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(main,
						"DE405 Ephemeris data file not found.");
				e.printStackTrace();
				System.exit(0);
			}

			main.pcpPlot.setData(main.pcpPlot.pcplot_data);
			main.pcpPlot.keyBehavior_u.reset();

			main.repaint();

		}

		if (ev.getSource() == pcpGUI.btnGoMin) {
			goToMinimum();
		}

		if (ev.getSource() == pcpGUI.btnStep) {
			step();
		}

		if (pcpGUI.rdbtnPlotRotate.isSelected()) {
			main.pcpPlot.requestFocusInWindow();
			main.pcpPlot.keyBehaviorSwitch.setWhichChild(0);
			main.pcpPlot.flightSelectorSwitch.setWhichChild(0);
			main.pcpGUI.btnGoMin.setEnabled(false);
			main.pcpGUI.btnStep.setEnabled(false);
		}

		if (pcpGUI.rdbtnFlightSelect.isSelected()) {
			// System.out.println("flightselect");
			main.pcpPlot.requestFocusInWindow();
			main.pcpPlot.keyBehaviorSwitch.setWhichChild(2);
			main.pcpPlot.flightSelectorSwitch.setWhichChild(1);
			main.pcpPlot.keyBehavior_u.updateMarker();
			main.pcpGUI.btnGoMin.setEnabled(true);
			main.pcpGUI.btnStep.setEnabled(true);
			// System.out.println("flightselect out");
		}
	}

	public void step() {
		int x_index = main.pcpPlot.keyBehavior_u.x_index;
		int y_index = main.pcpPlot.keyBehavior_u.y_index;
		DataArraySearch d = new DataArraySearch(main.pcpPlot.pcplot_data.data,
				x_index, y_index);
		d.step();
		main.pcpPlot.keyBehavior_u.x_index = d.x_index;
		main.pcpPlot.keyBehavior_u.y_index = d.y_index;
		main.pcpPlot.keyBehavior_u.updateMarker();
	}

	public void goToMinimum() {
		int x_index = main.pcpPlot.keyBehavior_u.x_index;
		int y_index = main.pcpPlot.keyBehavior_u.y_index;
		DataArraySearch d = new DataArraySearch(main.pcpPlot.pcplot_data.data,
				x_index, y_index);
		d.goToLocalMinimum();
		main.pcpPlot.keyBehavior_u.x_index = d.x_index;
		main.pcpPlot.keyBehavior_u.y_index = d.y_index;
		main.pcpPlot.keyBehavior_u.updateMarker();
	}

}
