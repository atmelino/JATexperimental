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

public class MissionPlanPlot extends JatPlot3D {
	private static final long serialVersionUID = 599884902601254854L;
	Star3D sun;
	MissionPlanMain mpMain;
	MissionPlanParameters param;
	DE405Plus Eph; // Ephemeris class
	Planet3D[] planets;
	Ephemeris3D[] ephemerisPlanets;

	public MissionPlanPlot(MissionPlanMain mpMain) {
		super();
		this.mpMain = mpMain;
		this.param = mpMain.mpParam;
		this.Eph = param.Eph;
	}

	public Node createScene() {
		Group g = new Group();
		jatScene = new jatScene3D(param.messages);
		initialViewingPosition = new Point3d(1, -3, 1);
		sun = new Star3D(param.path, param.messages, 3.f);
		jatScene.add(sun, "sun");

		ephemerisPlanets = new Ephemeris3D[10];
		SolarSystemBodies sb = new SolarSystemBodies();
		planets = new Planet3D[10];

		for (int i = 1; i < 6; i++) {
			if (param.planetOnOff[i]) {
				planets[i] = new Planet3D(param.path, param.messages, body.fromInt(i), 30.f);
				jatScene.add(planets[i], body.name[i]);
				// if (i == 3)
				{
					ephemerisPlanets[i] = new Ephemeris3D(Eph, body.fromInt(i), param.simulationDate,
							sb.Bodies[i].orbitalPeriod);
					jatScene.add(ephemerisPlanets[i], "ephemeris" + body.name[i]);
				}
			}
		}

		g.addChild(jatScene);
		StarsBackground3D s = new StarsBackground3D(param.path, param.messages, 15f);
		g.addChild(s);
		// initial zoom: exponent of ten times kilometers
		exponent = 8;
		jatScene.add(new RGBAxes3D(1e8), "Axis");
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
