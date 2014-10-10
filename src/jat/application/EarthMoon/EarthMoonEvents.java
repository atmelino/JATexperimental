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

package jat.application.EarthMoon;

import jat.application.missionPlan.Flight;
import jat.core.ephemeris.DE405Body;
import jat.core.ephemeris.DE405Body.body;
import jat.core.ephemeris.DE405Plus;
import jat.core.spacetime.TimeAPL;
import jat.core.util.jatMessages;
import jat.coreNOSA.math.MatrixVector.data.VectorN;
import jat.coreNOSA.spacetime.CalDate;
import jat.jat3D.behavior.jat_Rotate;
import jat.jat3D.plot3D.Rainbow3f;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.util.Calendar;

import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.vecmath.Vector3f;

class EarthMoonEvents implements ActionListener, ItemListener {

	EarthMoonMain emMain;
	EarthMoonParameters param;
	EarthMoonGUI emGUI;
	jat_Rotate jat_rotate;
	jatMessages messages;
	public Timer timer;
	int i;
	int time_advance = 10; // seconds
	DE405Plus Eph; // Ephemeris class
	Flight f;
	Rainbow3f rainbow = new Rainbow3f();
	boolean directionDown;

	public EarthMoonEvents(EarthMoonMain mpmain) {
		this.emMain = mpmain;
		param = emMain.emParam;
		Eph = emMain.emParam.Eph;
		messages = emMain.emParam.messages;
		timer = new Timer(50, this);
		// timer = new Timer(1000, this);
		// timer.start();
	}

	public void actionPerformed(ActionEvent ev) {
		this.emGUI = emMain.emGUI;
		this.jat_rotate = emMain.emPlot.jat_rotate;
		i++;

		if (ev.getSource() == emGUI.btn_stop) {
			time_advance = 0;
		}

		if (ev.getSource() == emGUI.btn_rewind) {
			int sign = (int) Math.signum(time_advance);
			switch (sign) {
			case -1:
				time_advance *= 2;
				break;
			case -0:
				time_advance = -10;
				break;
			case 1:
				time_advance /= 2;
				break;
			}
		}
		if (ev.getSource() == emGUI.btn_forward) {
			int sign = (int) Math.signum(time_advance);
			switch (sign) {
			case -1:
				time_advance /= 2;
				break;
			case -0:
				time_advance = 10;
				break;
			case 1:
				time_advance *= 2;
				break;
			}
		}

		// Periodic timer events

		CalDate caldate;
		if (emGUI.realtime_chk.isSelected()) {

			Calendar cal = Calendar.getInstance();
			int Y, M, D, h, m, s;
			Y = cal.get(Calendar.YEAR);
			M = cal.get(Calendar.MONTH);
			D = cal.get(Calendar.DAY_OF_MONTH);
			h = cal.get(Calendar.HOUR_OF_DAY);
			m = cal.get(Calendar.MINUTE);
			s = cal.get(Calendar.SECOND);
			caldate = new CalDate(Y, M, D, h, m, s);
			emMain.emParam.simulationDate = new TimeAPL(caldate);
		} else {
			emMain.emParam.simulationDate.step_seconds(time_advance);
			emGUI.timestepfield.setText("" + time_advance);
			caldate = new CalDate(emMain.emParam.simulationDate.mjd_utc());
		}

		emGUI.yearfield.setText("" + caldate.year());
		emGUI.monthfield.setText("" + caldate.month());
		emGUI.dayfield.setText("" + caldate.day());
		emGUI.hourfield.setText("" + caldate.hour());
		emGUI.minutefield.setText("" + caldate.min());
		emGUI.secondfield.setText("" + (int) caldate.sec());

		update_scene(emMain.emParam.simulationDate);

		if (emGUI.chckbxCameraRotate.isSelected()) {
			Vector3f sphereCoord = jat_rotate.getV_current_sphere();
			// System.out.println(sphereCoord.x + " " + sphereCoord.y + " " +
			// sphereCoord.z);

			if (sphereCoord.z > 1)
				directionDown = true;
			if (sphereCoord.z < -1)
				directionDown = false;
			if (directionDown)
				jat_rotate.jat_rotate(.01f, -.002f);
			else
				jat_rotate.jat_rotate(.01f, .002f);
		}
	}// End of ActionPerformed

	public void itemStateChanged(ItemEvent e) {

		Object source = e.getItemSelectable();

		if (source == emGUI.realtime_chk) {
			if (emGUI.realtime_chk.isSelected()) {
				emGUI.btn_stop.setEnabled(false);
				emGUI.btn_forward.setEnabled(false);
				emGUI.btn_rewind.setEnabled(false);
			} else {
				emGUI.btn_stop.setEnabled(true);
				emGUI.btn_forward.setEnabled(true);
				emGUI.btn_rewind.setEnabled(true);
			}
		}
	}

	void update_scene(TimeAPL mytime) {

		try {
			for (int i = 1; i < 11; i++) {
				if (param.planetOnOff[i]) {

					emMain.emPlot.planets[i].set_position(Eph.get_planet_pos(body.fromInt(i), mytime));
				}
			}

		} catch (IOException e) {
			JOptionPane.showMessageDialog(emGUI, "DE405 Ephemeris data file not found.");
			e.printStackTrace();
			System.exit(0);
			// e.printStackTrace();
		}

		try {
			// for (int i = 1; i < 7; i++) {
			// mpmain.mpPlot.planet[i].set_position(ecliptic_obliquity_rotate(myEph.get_planet_pos(body.fromInt(i),
			// mytime)));

			// myEph.get_planet_pos(DE405Plus.body.MOON, mytime).print("Moon");
			// myEph.get_planet_pos(DE405Plus.body.EARTH_MOON_BARY,
			// mytime).print("EARTH_MOON_BARY");

			VectorN moonPos = Eph.get_planet_pos(DE405Body.body.MOON, mytime);
			VectorN earthPos = Eph.get_planet_pos(DE405Body.body.EARTH, mytime);

			// moonPos.print("Moon pos "+Eph.ephFrame);

			// VectorN moonPosEC=earthPosHC.minus(moonPosHC);
			// VectorN moonPosEC=new VectorN(3);

			// moonPos.x[0]=
			// mpmain.mpPlot.planet[1].set_position(myEph.get_planet_pos(body[10],
			// mytime));
			// mpmain.emPlot.bodies[DE405Plus.body.MOON.ordinal()].set_position(moonPosEC);

		} catch (IOException e) {
			JOptionPane.showMessageDialog(emGUI, "DE405 Ephemeris data file not found.");
			e.printStackTrace();
			System.exit(0);
			// e.printStackTrace();
		}

	}

}
