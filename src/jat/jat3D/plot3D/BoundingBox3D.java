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

// Original Code under LGPL
// http://java.freehep.org/freehep-java3d/license.html

package jat.jat3D.plot3D;

import jat.jat3D.Body3D;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.IndexedLineArray;
import javax.media.j3d.Shape3D;
import javax.vecmath.Point3d;

/**
 * @author Tobias Berthold
 * 
 *         BoundingBox3D: an adaption of the bounding box in FreeHEP. The
 *         feature of infinite zoom is added. Every time the observer moves to a
 *         position 10 times further or 1/10 closer, the box is updated in that
 *         the axes are scaled by a factor of 10. However, instead of creating a
 *         box that is 10 times larger or smaller, the whole scene is shrunk or
 *         enlarged by a factor of 10 while the axes labels are modified to
 *         reflect the new scale. The box always remains the same size. This
 *         prevents the problem that would be caused by calling up fonts of
 *         sizes that don't exist.
 * 
 *         Coordinates: The over-arching premise of plot3D is that the origin
 *         (0,0,0) of the coordinates for OpenGL is always, always at the zero
 *         (0,0,0) of the bounding box. The data are always normalized to 1, and
 *         the box size is always 1. The ticks go from any number to that number
 *         plus one, and the axis label indicates the maximum, the exponent, and
 *         the unit.
 * 
 */
public class BoundingBox3D extends Body3D {
	public final static int NUMERIC = 0;
	public final static int DATE = 1;
	public float lo;
	public float hi;
	public AxisBuilder xAxis;
	private AxisBuilder yAxis;
	public ZAxisBuilder zAxis;
	public String xAxisLabel = "X";
	public String yAxisLabel = "Y";
	public String zAxisLabel = "Z";
	public String exponentPart;
	public String xunitsPart = "";
	public String yunitsPart = "";
	public String zunitsPart = "";

	public BoundingBox3D(float lo, float hi) {

		this.lo = lo;
		this.hi = hi;

		IndexedLineArray xCube = new IndexedLineArray(8, IndexedLineArray.COORDINATES, 24);

		// Set coordinates for the cube //
		xCube.setCoordinate(0, new Point3d(lo, hi, lo));
		xCube.setCoordinate(1, new Point3d(hi, hi, lo));
		xCube.setCoordinate(2, new Point3d(hi, lo, lo));
		xCube.setCoordinate(3, new Point3d(lo, lo, lo));
		xCube.setCoordinate(4, new Point3d(lo, hi, hi));
		xCube.setCoordinate(5, new Point3d(hi, hi, hi));
		xCube.setCoordinate(6, new Point3d(hi, lo, hi));
		xCube.setCoordinate(7, new Point3d(lo, lo, hi));

		// Construct the vertical //
		xCube.setCoordinateIndex(0, 0);
		xCube.setCoordinateIndex(1, 1);
		xCube.setCoordinateIndex(2, 3);
		xCube.setCoordinateIndex(3, 2);
		xCube.setCoordinateIndex(4, 4);
		xCube.setCoordinateIndex(5, 5);
		xCube.setCoordinateIndex(6, 7);
		xCube.setCoordinateIndex(7, 6);

		// Construct the lower side //
		xCube.setCoordinateIndex(8, 0);
		xCube.setCoordinateIndex(9, 4);
		xCube.setCoordinateIndex(10, 4);
		xCube.setCoordinateIndex(11, 7);
		xCube.setCoordinateIndex(12, 7);
		xCube.setCoordinateIndex(13, 3);
		xCube.setCoordinateIndex(14, 3);
		xCube.setCoordinateIndex(15, 0);

		// Construct the upper side //
		xCube.setCoordinateIndex(16, 1);
		xCube.setCoordinateIndex(17, 5);
		xCube.setCoordinateIndex(18, 5);
		xCube.setCoordinateIndex(19, 6);
		xCube.setCoordinateIndex(20, 6);
		xCube.setCoordinateIndex(21, 2);
		xCube.setCoordinateIndex(22, 2);
		xCube.setCoordinateIndex(23, 1);
		setCapability(BranchGroup.ALLOW_DETACH);
		setUserData("BoundingBox");
		addChild(new Shape3D(xCube));
	}

	public void createAxes(int exponent, String xunitsPart,String yunitsPart,String zunitsPart) {
		this.xunitsPart=xunitsPart;
		this.yunitsPart=yunitsPart;
		this.zunitsPart=zunitsPart;
		createAxes(exponent);
	}

	public void createAxes(int exponent) {

		// Axes labels and tick marks
		float range = hi - lo;
		double[] tick = { 0, range / 4f, range / 2f, 3 * range / 4f, range };
		float[] pos = { lo, lo + range / 4, lo + range / 2, lo + 3 * range / 4, hi };
		String[] labels = { String.valueOf(pos[0]), String.valueOf(pos[1]), String.valueOf(pos[2]),
				String.valueOf(pos[3]), String.valueOf(pos[4]) };
		setExponentPart(exponent);
		xAxis = new XAxisBuilder(xAxisLabel + exponentPart + xunitsPart, labels, tick);
		yAxis = new YAxisBuilder(yAxisLabel + exponentPart + yunitsPart, labels, tick);
		zAxis = new ZAxisBuilder(zAxisLabel + exponentPart + zunitsPart, labels, tick);
		xAxis.lo = lo;
		xAxis.hi = hi;
		yAxis.lo = lo;
		yAxis.hi = hi;
		zAxis.lo = lo;
		zAxis.hi = hi;

		xAxis.apply();
		yAxis.apply();
		zAxis.apply();

		addChild(xAxis.getNode());
		addChild(yAxis.getNode());
		addChild(zAxis.getNode());

	}

	public void setLabels(int exponent) {
		setExponentPart(exponent);
		xAxis.setLabel(xAxisLabel + exponentPart + xunitsPart);
		yAxis.setLabel(yAxisLabel + exponentPart + yunitsPart);
		zAxis.setLabel(zAxisLabel + exponentPart + zunitsPart);

		xAxis.apply();
		yAxis.apply();
		zAxis.apply();

	}

	public void setExponentPart(int exponent) {
		// Why a separate method for this? To prevent creating the same string
		// three times
		exponentPart = new String("  [10^" + exponent + "]");
	}

}
