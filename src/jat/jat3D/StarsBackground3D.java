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

import jat.core.astronomy.StarCatalog;
import jat.core.astronomy.Stardata;
import jat.core.util.PathUtil;
import jat.core.util.jatMessages;
import jat.coreNOSA.math.CoordTransform;
import jat.coreNOSA.math.MathUtils;
import jat.coreNOSA.math.MatrixVector.data.VectorN;

import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.PointArray;
import javax.media.j3d.Shape3D;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;

/**
 * Planet class
 * 
 * @author Tobias Berthold
 */
public class StarsBackground3D extends BranchGroup {
	float radius;
	String Texturefilename;
	Appearance app;
	Color3f Starcolor; // Star color if texture not found
	public StarCatalog s;
	PathUtil p;
	jatMessages messages;
	boolean bugFound = true;

	public StarsBackground3D(PathUtil p,  jatMessages messages, float radius) {
		super();
		this.p = p;
		this.messages=messages;
		this.radius = radius;
		Starcolor = Colors.blue;
		addChild(createStarSphere());
		// addChild(createPointCloud());
	}

	public StarsBackground3D(PathUtil p,   float radius) {
		super();
		this.p = p;
		this.radius = radius;
		Starcolor = Colors.blue;
		addChild(createStarSphere());
		// addChild(createPointCloud());
	}

	public BranchGroup createStarSphere() {

		BranchGroup bg = new BranchGroup();

		s = new StarCatalog(p,messages);

		s.load();
		if (bugFound) {

			PointArray starfield = new PointArray(99, PointArray.COORDINATES | PointArray.COLOR_3);
			float[] brightness = new float[3];
			Stardata sd;
			for (int i = 0; i < s.manystardata.size(); i++) {
				// for (int i = 0; i < manystardata.size(); i++)
				sd = (Stardata) s.manystardata.get(i);

				// System.out.println(sd.ProperName + " " + sd.RA + " " +
				// sd.dec);
				VectorN point3 = CoordTransform.Spherical_to_Cartesian_deg(radius, sd.RA / MathUtils.DEG2RAD, sd.dec
						/ MathUtils.DEG2RAD);

				// point[0] = 1;
				// point[1] = 1;
				// point[2] = 1;
				// point[0] *= 1.e8;
				// point[1] *= 1.e8;
				// point[2] *= 1.e8;
				starfield.setCoordinate(i, point3.x);
				// final float mag = rand.nextFloat();
				final float mag = 1.f;
				brightness[0] = mag;
				brightness[1] = mag;
				brightness[2] = mag;
				starfield.setColor(i, brightness);
			}
			bg.addChild(new Shape3D(starfield));
		}
		return bg;

	}

	private BranchGroup createPointCloud() {

		BranchGroup bg = new BranchGroup();

		final java.util.Random rand = new java.util.Random();
		PointArray starfield = new PointArray(20000, PointArray.COORDINATES | PointArray.COLOR_3);
		float[] point = new float[3];
		float[] brightness = new float[3];
		for (int i = 0; i < 20000; i++) {
			point[0] = (rand.nextInt(2) == 0) ? rand.nextFloat() * -1.0f : rand.nextFloat();
			point[1] = (rand.nextInt(2) == 0) ? rand.nextFloat() * -1.0f : rand.nextFloat();
			point[2] = (rand.nextInt(2) == 0) ? rand.nextFloat() * -1.0f : rand.nextFloat();
			point[0] *= 1.e8;
			point[1] *= 1.e8;
			point[2] *= 1.e8;

			starfield.setCoordinate(i, point);
			final float mag = rand.nextFloat();
			brightness[0] = mag;
			brightness[1] = mag;
			brightness[2] = mag;
			starfield.setColor(i, brightness);
		}
		bg.addChild(new Shape3D(starfield));

		return bg;

	}

	private Background createBackGraph() {
		Background background = new Background();
		final BoundingSphere infiniteBounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), Double.MAX_VALUE);
		background.setApplicationBounds(infiniteBounds);
		BranchGroup bg = new BranchGroup();

		final java.util.Random rand = new java.util.Random();
		PointArray starfield = new PointArray(20000, PointArray.COORDINATES | PointArray.COLOR_3);
		float[] point = new float[3];
		float[] brightness = new float[3];
		for (int i = 0; i < 20000; i++) {
			point[0] = (rand.nextInt(2) == 0) ? rand.nextFloat() * -1.0f : rand.nextFloat();
			point[1] = (rand.nextInt(2) == 0) ? rand.nextFloat() * -1.0f : rand.nextFloat();
			point[2] = (rand.nextInt(2) == 0) ? rand.nextFloat() * -1.0f : rand.nextFloat();
			starfield.setCoordinate(i, point);
			final float mag = rand.nextFloat();
			brightness[0] = mag;
			brightness[1] = mag;
			brightness[2] = mag;
			starfield.setColor(i, brightness);
		}
		bg.addChild(new Shape3D(starfield));

		background.setGeometry(bg);
		return background;
	}

}
