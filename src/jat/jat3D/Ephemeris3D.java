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

package jat.jat3D;

import jat.core.ephemeris.DE405Body.body;
import jat.core.ephemeris.DE405Plus;
import jat.core.spacetime.TimeAPL;
import jat.coreNOSA.math.MatrixVector.data.VectorN;

import java.io.IOException;
import java.util.ArrayList;

import javax.media.j3d.GeometryArray;
import javax.media.j3d.LineStripArray;
import javax.media.j3d.Shape3D;
import javax.vecmath.Color3f;

/**
 * @author Tobias Berthold
 * 
 */
public class Ephemeris3D extends Body3D {
	float size;
	DE405Plus myEph;
	body body;
	double jd;
	int steps = 100;
	int days;
	Color3f Color = Colors.gray;
	Shape3D s;
	TimeAPL startTime;

	public Ephemeris3D(DE405Plus myEph, body body, TimeAPL startTime,
			double days) {
		this.myEph = myEph;
		this.body = body;
		this.startTime = startTime;
		this.days = (int) days;

		draw();
	}

	private void draw() {
		ArrayList<Double> coordinates = new ArrayList<Double>();
		VectorN r;
		TimeAPL time = new TimeAPL(startTime.mjd_utc());
		boolean allDataLoaded = true;
		double step = days * 86400 / steps;
		for (int k = 0; k < steps; k++) {

			time.step_seconds(step);
			// time.println();
			try {
				r = new VectorN(myEph.get_planet_pos(body, time));
				double x, y, z;
				x = r.get(0);
				y = r.get(1);
				z = r.get(2);

				coordinates.add(x);
				coordinates.add(y);
				coordinates.add(z);

			} catch (IOException e) {
				allDataLoaded = false;
				break;// e.printStackTrace();
			}
		}

		// Create coords array
		double[] coords;
		if (allDataLoaded)
			coords = new double[coordinates.size() + 3];
		else
			coords = new double[coordinates.size()];

		for (int i = 0; i < coordinates.size(); i++)
			coords[i] = coordinates.get(i);

		int numberOfVertices = coords.length / 3;
		if (allDataLoaded) {
			// Close the loop
			coords[steps * 3 + 0] = coords[0];
			coords[steps * 3 + 1] = coords[1];
			coords[steps * 3 + 2] = coords[2];
		}
		int[] stripLengths = { numberOfVertices };

		LineStripArray myLines = new LineStripArray(numberOfVertices,
				GeometryArray.COORDINATES | GeometryArray.COLOR_3, stripLengths);
		Color3f colors[] = new Color3f[numberOfVertices];
		for (int i = 0; i < numberOfVertices; i++)
			colors[i] = Color;
		myLines.setColors(0, colors);
		myLines.setCoordinates(0, coords);
		s = new Shape3D();
		s.setGeometry(myLines);
		addChild(s);

	}
}
