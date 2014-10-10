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

package jat.core.spacetime;

import jat.coreNOSA.cm.cm;
import jat.coreNOSA.spacetime.CalDate;
import jat.coreNOSA.spacetime.Time;

import java.util.Calendar;

public class TimeAPL extends Time {

	public TimeAPL() {
		// TODO Auto-generated constructor stub
	}

	public TimeAPL(double mjd_UTC) {
		super(mjd_UTC);
		// TODO Auto-generated constructor stub
	}

	public TimeAPL(CalDate date) {
		super(date);
	}

	public TimeAPL(int Yr, int Mon, int D, int Hr, int Mn, double S) {
		super(Yr, Mon, D, Hr, Mn, S);
		// TODO Auto-generated constructor stub
	}

	public TimeAPL plus(double seconds) {
		double mjd = this.mjd_utc() + seconds / 86400.;
		return new TimeAPL(mjd);
	}

	/**
	 * @param t1
	 *            later time
	 * @param t2
	 *            earlier time
	 * @return time difference in days
	 */
	public static double minus(TimeAPL t1, TimeAPL t2) {
		return t1.jd_tt() - t2.jd_tt();
	}

	public Calendar getCalendar() {
		Calendar cal = cm.JD_to_Calendar(jd_tt());
		return cal;
	}

	public int getYear() {
		Calendar cal = cm.JD_to_Calendar(jd_tt());
		return cal.get(Calendar.YEAR);
	}

	public int getMonth() {
		Calendar cal = cm.JD_to_Calendar(jd_tt());
		return cal.get(Calendar.MONTH)+1;
	}

	public int getDay() {
		Calendar cal = cm.JD_to_Calendar(jd_tt());
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	public int getHour() {
		Calendar cal = cm.JD_to_Calendar(jd_tt());
		return cal.get(Calendar.HOUR_OF_DAY);
	}

	public int getMinute() {
		Calendar cal = cm.JD_to_Calendar(jd_tt());
		return cal.get(Calendar.MINUTE);
	}

	public void print() {
		String dateformat = "%tD";

		String.format(dateformat, getCalendar());

		System.out.print(String.format(dateformat, getCalendar()));
	}

	public void printspace() {
		print();
		System.out.print(" ");
	}

	public void println() {
		print();
		System.out.println();

	}

}
