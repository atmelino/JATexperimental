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

import jat.core.astronomy.SolarSystemBodies;
import jat.core.ephemeris.DE405Body.body;
import jat.core.ephemeris.DE405Plus;
import jat.jat3D.BodyGroup3D;
import jat.jat3D.Ephemeris3D;
import jat.jat3D.Planet3D;
import jat.jat3D.RGBAxes3D;
import jat.jat3D.Star3D;
import jat.jat3D.StarsBackground3D;
import jat.jat3D.jatScene3D;
import jat.jat3D.plot3D.BoundingBox3D;
import jat.jat3D.plot3D.JatPlot3D;

import javax.media.j3d.Group;
import javax.media.j3d.Node;
import javax.vecmath.Point3d;

public class EarthMoonPlot extends JatPlot3D {
	private static final long serialVersionUID = 599884902601254854L;
	Star3D sun;
	EarthMoonMain emMain;
	EarthMoonParameters param;
	DE405Plus Eph; // Ephemeris class
	Planet3D[] planets;
	Ephemeris3D[] ephemerisPlanets;

	public EarthMoonPlot(EarthMoonMain emMain) {
		super();
		this.emMain = emMain;
		this.param = emMain.emParam;
		this.Eph = param.Eph;
	}

	public Node createScene() {
		Group g = new Group();
		jatScene = new jatScene3D();
		initialViewingPosition = new Point3d(1, -3, 1);
		sun = new Star3D(param.path, param.messages, 3.f);
		jatScene.add(sun, "sun");

		ephemerisPlanets = new Ephemeris3D[emMain.emParam.numberOfBodies];
		planets = new Planet3D[emMain.emParam.numberOfBodies];
		SolarSystemBodies sb = new SolarSystemBodies();

		for (int i = 1; i < 11; i++) {
			if (param.planetOnOff[i]) {
				planets[i] = new Planet3D(param.path, param.messages, body.fromInt(i), param.planetMagnification[i]);
				jatScene.add(planets[i], body.name[i]);
				boolean later=true; 
				if (later)
				{
					ephemerisPlanets[i] = new Ephemeris3D(Eph, body.fromInt(i), param.simulationDate,
							sb.Bodies[i].orbitalPeriod);
					jatScene.add(ephemerisPlanets[i], "ephemeris" + body.name[i]);
				}
			}
		}

		g.addChild(jatScene);
		StarsBackground3D s = new StarsBackground3D(emMain.emParam.path, 15f);
		g.addChild(s);
		// initial zoom: exponent of ten times kilometers
		exponent = 4;
		jatScene.add(new RGBAxes3D(10e3), "Axis");
		// Bounding Box
		bbox = new BoundingBox3D(-.5f, .5f);
		bbox.createAxes(exponent, " km", " km", " km");
		bboxgroup = new BodyGroup3D(bbox, "Box");
		g.addChild(bboxgroup);
		return g;
	}

	public void update_user() {
		// mpGUI.viewdistancefield.setText("" + mpGUI.mpp.get_vp_t().length());
	}
}

// which planets
//emMain.emParam.planetOnOff[DE405Body.body.EARTH_MOON_BARY.ordinal()] = true;
//emMain.emParam.planetOnOff[DE405Body.body.MOON.ordinal()] = true;

// planet[0] = new Planet3D(DE405Plus.body.EARTH_MOON_BARY, 10.f);
// jatScene.add(planet[0], DE405Plus.name[3]);
// planet[1] = new Planet3D(DE405Plus.body.MOON, 10.f);
// jatScene.add(planet[1], DE405Plus.name[10]);

// sun = new Star3D(emMain.emParam.path,emMain.emParam.messages, .001f);
// jatScene.add(sun, "sun");

//moon = new Planet3D(emMain.emParam.path, emMain.emParam.messages, DE405Body.body.EARTH_MOON_BARY, 1.f);
//jatScene.add(moon, "moon");
// for (int i = 1; i < emMain.emParam.numberOfBodies; i++) {
// if (emMain.emParam.planetOnOff[i]) {
// bodies[i] = new
// Planet3D(emMain.emParam.path,emMain.emParam.messages,DE405Plus.body.fromInt(i),
// 1.f);
// jatScene.add(bodies[i], DE405Plus.name[i]);
// // if (i == 3)
// {
// ephemerisPlanet[i] = new Ephemeris3D(myEph, body[i],
// emMain.emParam.simulationDate,
// SolarSystemBodies.Bodies[i].orbitalPeriod);
// jatScene.add(ephemerisPlanet[i], "ephemeris" + DE405Plus.name[i]);
// }
// }
// }

