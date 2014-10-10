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

import jat.core.ephemeris.DE405Body.body;
import jat.jat3D.behavior.jat_KeyBehavior_UserCall;

import javax.vecmath.Point3d;

public class PorkChopPlotKeyBehavior extends jat_KeyBehavior_UserCall {
	Point3d r;
	private PorkChopPlotMain main;
	public int x_index, y_index;

	public PorkChopPlotKeyBehavior(PorkChopPlotMain main) {
		this.main = main;
		r = new Point3d();
	}

	public void keyup() {
		if (y_index < main.pcpPlot.pcplot_data.pcpPlot.steps - 1) {
			// r.y += main.surf.pcplot_data.p.step_size;
			y_index++;
			updateMarker();
		}
	}

	public void keydown() {
		if (y_index > 0) {
			// r.y -= main.surf.pcplot_data.p.step_size;
			y_index--;
			updateMarker();
		}
	}

	public void keyright() {
		// System.out.println("right");
		if (x_index < main.pcpPlot.pcplot_data.pcpPlot.steps - 1) {
			// r.x += main.surf.pcplot_data.p.step_size;
			x_index++;
			updateMarker();
		}
	}

	public void keyleft() {
		// System.out.println("left");
		if (x_index > 0) {
			// r.x -= main.surf.pcplot_data.p.step_size;
			x_index--;
			updateMarker();
		}
	}

	public void updateMarker() {
		float step_size = main.pcpPlot.pcplot_data.pcpPlot.step_size;
		r.x = x_index * step_size + step_size / 2;
		r.y = y_index * step_size + step_size / 2;
		r.z = main.pcpPlot.ndata.zAt(x_index, y_index);
		main.pcpPlot.m.set_position(r);
		// System.out.println(x_index + " " + y_index + " " + r.z);
		main.pcpGUI.field_total_deltav.setText("" + main.pcpPlot.pcplot_data.zAt(x_index, y_index));
		main.pcpGUI.field_selected_departure_date.setText(main.pcpPlot.pcplot_data.pcpPlot.A.RowLabels[x_index]);
		main.pcpGUI.field_selected_arrival_date.setText(main.pcpPlot.pcplot_data.pcpPlot.A.ColumnLabels[y_index]);

		// Keep selected dates as return value if package used in dialog
		// System.out.print("Dep ");
		// new
		// TimeAPL(main.pcpPlot.pcplot_data.p.DepartureDate[x_index]).print();
		// System.out.print("Arr ");
		// new TimeAPL(main.pcpPlot.pcplot_data.p.ArrivalDate[y_index]).print();
		main.pReturn.departure_planet = body.fromInt(main.pcpGUI.comboDepartPlanet.getSelectedIndex());
		main.pReturn.arrival_planet = body.fromInt(main.pcpGUI.comboArrivalPlanet.getSelectedIndex());			
		main.pReturn.DepartureDate = main.pcpPlot.pcplot_data.pcpPlot.DepartureDate[x_index];
		main.pReturn.ArrivalDate = main.pcpPlot.pcplot_data.pcpPlot.ArrivalDate[y_index];
	}

	public void reset() {
		r.x = 0;
		// r.x = main.surf.pcplot_data.p.step_size;
		r.y = 0;
		x_index = 0;
		y_index = 0;
		main.pcpPlot.m.set_position(r);
	}
}
