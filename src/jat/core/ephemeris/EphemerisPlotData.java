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

package jat.core.ephemeris;

import jat.core.ephemeris.DE405Body.body;
import jat.core.spacetime.TimeAPL;
import jat.coreNOSA.math.MatrixVector.data.VectorN;

import java.awt.Color;
import java.io.IOException;

/**
 * @author Tobias Berthold
 * 
 */
public class EphemerisPlotData {
	float size;
	DE405Plus myEph;
	body body;
	double jd;
	int steps;
	int seconds;
	Color Color = java.awt.Color.GRAY;
	TimeAPL startTime;
	public double[][] XYZ;

	public EphemerisPlotData(DE405Plus myEph, body body, TimeAPL startTime, double seconds, int steps) {
		this.myEph = myEph;
		this.body = body;
		this.startTime = startTime;
		this.seconds = (int) seconds;
		this.steps = steps;
		draw();
	}

	private void draw() {
		XYZ = new double[steps][3];
		VectorN r;
		TimeAPL time = new TimeAPL(startTime.mjd_utc());
		double step = seconds / steps;
		try {
			for (int k = 0; k < steps; k++) {

				r = new VectorN(myEph.get_planet_pos(body, time));
				// double x, y, z;
				XYZ[k][0] = r.get(0);
				XYZ[k][1] = r.get(1);
				XYZ[k][2] = r.get(2);

				time.step_seconds(step);
				// time.println();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
