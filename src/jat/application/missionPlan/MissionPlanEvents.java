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

package jat.application.missionPlan;

import jat.core.cm.TwoBodyAPL;
import jat.core.ephemeris.DE405Body.body;
import jat.core.ephemeris.DE405Plus;
import jat.core.spacetime.TimeAPL;
import jat.core.util.jatMessages;
import jat.coreNOSA.cm.Constants;
import jat.coreNOSA.cm.Lambert;
import jat.coreNOSA.cm.LambertException;
import jat.coreNOSA.spacetime.CalDate;
import jat.jat3D.Colors;
import jat.jat3D.Sphere3D;
import jat.jat3D.TwoBodyOrbit3D;
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

class MissionPlanEvents implements ActionListener, ItemListener {

	MissionPlanMain mpMain;
	MissionPlanGUI mpGUI;
	MissionPlanParameters param;
	jat_Rotate jat_rotate;
	jatMessages messages;
	public Timer timer;
	int i;
	int time_advance = 10; // seconds
	DE405Plus Eph; // Ephemeris class
	Flight f;
	Rainbow3f rainbow = new Rainbow3f();
	ManageFlightsDialog myDialog;
	boolean directionDown;

	public MissionPlanEvents(MissionPlanMain mpMain) {
		this.mpMain = mpMain;
		this.param = mpMain.mpParam;
		this.Eph = param.Eph;
		messages = param.messages;
		timer = new Timer(50, this);
		// timer = new Timer(1000, this);
		// timer.start();
	}

	public void actionPerformed(ActionEvent ev) {
		this.mpGUI = mpMain.mpGUI;
		this.jat_rotate = mpMain.mpPlot.jat_rotate;
		i++;

		if (ev.getSource() == mpGUI.btn_stop) {
			time_advance = 0;
		}

		if (ev.getSource() == mpGUI.btn_rewind) {
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
		if (ev.getSource() == mpGUI.btn_forward) {
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

		if (ev.getSource() == mpGUI.btnAddFlight) {
			messages.addln("[MissionPlanEvents add flight]");
			// System.out.println("add flight");
			AddFlightDialog myDialog = new AddFlightDialog(mpMain);
			// Get the resulting dates and delta-v's and add trajectory to
			// plot
			if (myDialog.pcpMain.pReturn.DepartureDate > 0.) {

				try {
					f = new Flight();
					f.flightName = "flight" + i;
					// retrieve selected values from dialog and store
					f.departure_planet = myDialog.pcpMain.pReturn.departure_planet;
					f.departurePlanetName = body.name[f.departure_planet.ordinal()];
					f.arrival_planet = myDialog.pcpMain.pReturn.arrival_planet;
					f.arrivalPlanetName = body.name[f.arrival_planet.ordinal()];
					f.departureDate = new TimeAPL(myDialog.pcpMain.pReturn.DepartureDate);
					f.arrivalDate = new TimeAPL(myDialog.pcpMain.pReturn.ArrivalDate);

					f.mu = Constants.GM_Sun / 1.e9;

					f.tof = TimeAPL.minus(f.arrivalDate, f.departureDate) * 86400.0;

					f.lambert = new Lambert(Constants.GM_Sun / 1.e9);
					f.r0 = Eph.get_planet_pos(f.departure_planet, f.departureDate);
					f.v0 = Eph.get_planet_vel(f.departure_planet, f.departureDate);
					f.rf = Eph.get_planet_pos(f.arrival_planet, f.arrivalDate);
					f.vf = Eph.get_planet_vel(f.arrival_planet, f.arrivalDate);
					try {
						f.totaldv = f.lambert.compute(f.r0, f.v0, f.rf, f.vf, f.tof);
						messages.addln("[MissionPlanEvents] total DV " + f.totaldv + "km/s");
						// totaldv = -1;

						// apply the first delta-v
						f.dv0 = f.lambert.deltav0;
						f.v0 = f.v0.plus(f.dv0);
						// System.out.println(ssmain.flightList.size());

						TwoBodyAPL temp = new TwoBodyAPL(f.mu, f.r0, f.v0);
						f.t0_on_orbit = temp.t_from_ta();
						// VectorN rot_r0 = f.r0;
						// VectorN rot_v0 = f.v0;
						f.color = rainbow.colorFor(10 * mpMain.flightList.size());
						// f.orbit = new TwoBodyOrbit3D(f.mu, rot_r0, rot_v0,
						// f.t0_on_orbit, f.t0_on_orbit + f.tof,
						// f.color);
						f.orbit = new TwoBodyOrbit3D(f.mu, f.r0, f.v0, f.t0_on_orbit, f.t0_on_orbit + f.tof, f.color);
						mpMain.mpPlot.jatScene.add(f.orbit, f.flightName);
						f.satellite = new Sphere3D(5000000.f, Colors.gold);
						mpMain.mpPlot.jatScene.add(f.satellite, f.satelliteName);
						mpMain.flightList.add(f);

					} catch (LambertException e) {
						messages.addln("Lambert failed"); // totaldv = -1;
						// System.out.println(e.message);
						// e.printStackTrace();
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

		} // End of button Add Flight pressed

		if (ev.getSource() == mpGUI.btnManageFlights) {
			// System.out.println("manage flights");
			myDialog = new ManageFlightsDialog(mpMain);
		}

		// Periodic timer events
		// System.out.println("alive");
		CalDate caldate;
		if (mpGUI.realtime_chk.isSelected()) {
			Calendar cal = Calendar.getInstance();
			int Y, M, D, h, m, s;
			Y = cal.get(Calendar.YEAR);
			M = cal.get(Calendar.MONTH);
			D = cal.get(Calendar.DAY_OF_MONTH);
			h = cal.get(Calendar.HOUR_OF_DAY);
			m = cal.get(Calendar.MINUTE);
			s = cal.get(Calendar.SECOND);
			caldate = new CalDate(Y, M, D, h, m, s);
			param.simulationDate = new TimeAPL(caldate);
		} else {
			param.simulationDate.step_seconds(time_advance);
			mpGUI.timestepfield.setText("" + time_advance);
			caldate = new CalDate(param.simulationDate.mjd_utc());
		}

		mpGUI.yearfield.setText("" + caldate.year());
		mpGUI.monthfield.setText("" + caldate.month());
		mpGUI.dayfield.setText("" + caldate.day());
		mpGUI.hourfield.setText("" + caldate.hour());
		mpGUI.minutefield.setText("" + caldate.min());
		mpGUI.secondfield.setText("" + (int) caldate.sec());

		update_scene(param.simulationDate);

		if (mpGUI.chckbxCameraRotate.isSelected()) {
			Vector3f sphereCoord = jat_rotate.getV_current_sphere();
			// System.out.println(sphereCoord.x + " " + sphereCoord.y + " " +
			// sphereCoord.z);

			if (sphereCoord.z > 1)
				directionDown = true;
			if (sphereCoord.z < -1)
				directionDown = false;
			if (directionDown)
				jat_rotate.jat_rotate(.005f, -.002f);
			else
				jat_rotate.jat_rotate(.005f, .002f);
		}
		if (messages.changed) {
			messages.printMessages();
			messages.printMessagesToTextArea(mpMain.textArea);
		}
	}// End of ActionPerformed

	public void itemStateChanged(ItemEvent e) {

		Object source = e.getItemSelectable();

		if (source == mpGUI.realtime_chk) {
			if (mpGUI.realtime_chk.isSelected()) {
				mpGUI.btn_stop.setEnabled(false);
				mpGUI.btn_forward.setEnabled(false);
				mpGUI.btn_rewind.setEnabled(false);
			} else {
				mpGUI.btn_stop.setEnabled(true);
				mpGUI.btn_forward.setEnabled(true);
				mpGUI.btn_rewind.setEnabled(true);
			}
		}
	}

	void update_scene(TimeAPL mytime) {

		try {

			for (int i = 1; i < 6; i++) {
				if (param.planetOnOff[i]) {

					mpMain.mpPlot.planets[i].set_position(Eph.get_planet_pos(body.fromInt(i), mytime));
				}
			}

		} catch (IOException e) {
			JOptionPane.showMessageDialog(mpGUI, "DE405 Ephemeris data file not found.");
			e.printStackTrace();
			System.exit(0);
			// e.printStackTrace();
		}

		for (int i = 0; i < mpMain.flightList.size(); i++) {

			double satelliteTime;
			Flight f = mpMain.flightList.get(i);
			satelliteTime = TimeAPL.minus(mytime, f.departureDate);

			mpMain.mpGUI.viewdistancefield.setText("" + satelliteTime);
			if (satelliteTime > 0 && satelliteTime < f.tof / 86400.) {
				f.satellite.set_position(f.orbit.sat.position(satelliteTime * 86400));

			} else
				f.satellite.set_position(0, 0, 0);

		}
	}

}
