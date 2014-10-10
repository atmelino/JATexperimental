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
package jat.core.cm;

import jat.core.ephemeris.DE405Body.body;
import jat.core.ephemeris.DE405Plus;
import jat.core.math.MatrixVector.util.LabeledMatrix;
import jat.core.spacetime.TimeAPL;
import jat.coreNOSA.cm.Constants;
import jat.coreNOSA.cm.Lambert;
import jat.coreNOSA.cm.LambertException;
import jat.coreNOSA.math.MatrixVector.data.VectorN;

import java.io.IOException;

public class porkChopPlot {
	public LabeledMatrix A;
	public double mintotaldv, maxtotaldv;
	public double DepartureDate[];
	public double ArrivalDate[];
	public int steps;
	public float step_size;
	DE405Plus Eph;

//	public porkChopPlot() {
//		my_eph = new DE405Plus();
//	}

	public porkChopPlot(DE405Plus Eph) {
		this.Eph = Eph;
	}

	public void make_porkchop_plot(body departure_planet, body arrival_planet, TimeAPL search_depart_time_start,
			TimeAPL search_arrival_time_start, int searchDays, int steps) throws IOException {
		double totaldv;
		this.steps = steps;
		step_size = 1.f / steps;

		TimeAPL search_depart_time = new TimeAPL(search_depart_time_start.mjd_utc());
		TimeAPL search_arrival_time = new TimeAPL(search_arrival_time_start.mjd_utc());
		int search_time = 86400 * searchDays;
		int time_increment = search_time / steps;

		// System.out.print(" days " + days);
		// System.out.print(" search_time " + search_time);
		// System.out.print(" steps " + steps);
		// System.out.println(" time_increment " + time_increment);
		// search_arrival_time_start.print();

		A = new LabeledMatrix(steps, steps);
		DepartureDate = new double[steps];
		ArrivalDate = new double[steps];
		mintotaldv = 1e9;
		maxtotaldv = 0;

		// System.out.println("[porkChopPlot.java] dep planet " +
		// departure_planet + " arr planet " + arrival_planet);
		// callLambert(departure_planet, arrival_planet,search_depart_time,
		// search_arrival_time);

		A.cornerlabel = "Dep / Arr";
		String dateformat = "%tD";
		for (int i = 0; i < steps; i++) {
			A.RowLabels[i] = String.format(dateformat, search_depart_time.getCalendar());
			DepartureDate[i] = search_depart_time.mjd_utc();
			// System.out.println(search_depart_time.mjd_utc());
			for (int j = 0; j < steps; j++) {

				A.ColumnLabels[j] = String.format(dateformat, search_arrival_time.getCalendar());
				ArrivalDate[j] = search_arrival_time.mjd_utc();
				// System.out.println(search_arrival_time.mjd_utc());

				double tof = TimeAPL.minus(search_arrival_time, search_depart_time) * 86400.0;

				Lambert lambert = new Lambert(Constants.GM_Sun / 1.e9);
				VectorN r0 = Eph.get_planet_pos(departure_planet, search_depart_time);
				VectorN v0 = Eph.get_planet_vel(departure_planet, search_depart_time);
				VectorN rf = Eph.get_planet_pos(arrival_planet, search_arrival_time);
				VectorN vf = Eph.get_planet_vel(arrival_planet, search_arrival_time);
				// r0.print("r0");
				// v0.print("v0");
				// System.out.println("orbital velocity of departure planet " +
				// v0.mag());
				// rf.print("rf");
				// vf.print("vf");
				// System.out.println("orbital velocity of arrival planet " +
				// vf.mag());

				try {
					totaldv = lambert.compute(r0, v0, rf, vf, tof);
				} catch (LambertException e) {
					totaldv = -1;
					// System.out.println(e.message);
					// e.printStackTrace();
				}
				if (totaldv > maxtotaldv)
					maxtotaldv = totaldv;
				if (totaldv > 0 && totaldv < mintotaldv)
					mintotaldv = totaldv;
				// lambert.deltav0.print("deltav0");
				// lambert.deltavf.print("deltavf");
				// System.out.println("Total DeltaV " + totaldv);
				A.A[i][j] = totaldv;
				search_arrival_time.step_seconds(time_increment);
			}
			search_arrival_time = new TimeAPL(search_arrival_time_start.mjd_utc());
			search_depart_time.step_seconds(time_increment);
		}
		// A.print();

	}

	public double callLambert(body departure_planet, body arrival_planet, TimeAPL search_depart_time,
			TimeAPL search_arrival_time) {
		double tof = TimeAPL.minus(search_arrival_time, search_depart_time) * 86400.0;
		double totaldv = -1.;

		Lambert lambert = new Lambert(Constants.GM_Sun / 1.e9);

		try {
			VectorN r0 = Eph.get_planet_pos(departure_planet, search_depart_time);
			System.out.println("length of r0 " + r0.mag());
			VectorN v0 = Eph.get_planet_vel(departure_planet, search_depart_time);
			VectorN rf = Eph.get_planet_pos(arrival_planet, search_arrival_time);
			VectorN vf = Eph.get_planet_vel(arrival_planet, search_arrival_time);
			totaldv = lambert.compute(r0, v0, rf, vf, tof);
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (LambertException e) {
			totaldv = -1;
			// System.out.println(e.message);
			// e.printStackTrace();
		}

		return totaldv;
	}

}
