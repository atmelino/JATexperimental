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
import jat.core.ephemeris.DE405Plus;
import jat.core.spacetime.TimeAPL;
import jat.core.util.PathUtil;

public class PorkChopPlotParameters {
	body departure_planet;
	body arrival_planet;
	int dep_year, dep_month, dep_day;
	int arr_year, arr_month, arr_day;
	TimeAPL search_depart_time_start;
	TimeAPL search_arrival_time_start;
	int searchDays;
	int steps;
	PathUtil path;
	DE405Plus Eph;

	public PorkChopPlotParameters(body departure_planet, body arrival_planet, int dep_year, int dep_month,
			int dep_day, int arr_year, int arr_month, int arr_day, int searchDays, int steps) {
		super();
		this.departure_planet = departure_planet;
		this.arrival_planet = arrival_planet;
		this.dep_year = dep_year;
		this.dep_month = dep_month;
		this.dep_day = dep_day;
		this.arr_year = arr_year;
		this.arr_month = arr_month;
		this.arr_day = arr_day;
		this.searchDays = searchDays;
		this.steps = steps;
		search_depart_time_start = new TimeAPL(dep_year, dep_month, dep_day, 1, 1, 1);
		search_arrival_time_start = new TimeAPL(arr_year, arr_month, arr_day, 1, 1, 1);

	}

}
