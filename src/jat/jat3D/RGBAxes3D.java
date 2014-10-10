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

import java.awt.Font;

import javax.media.j3d.Appearance;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Font3D;
import javax.media.j3d.FontExtrusion;
import javax.media.j3d.Geometry;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.IndexedLineArray;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Text3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Cone;

/**
 * @author Tobias Berthold
 * 
 */
public class RGBAxes3D extends Body3D {

	float axislength;
	Shape3D s;
	TransformGroup axislabelTrans;

	/**
	 * creates a 3D x-y-z axis object
	 */
	public RGBAxes3D() {
		super();
		axislength = 15000.0f;
		common_to_all_constructors();
	}

	/**
	 * creates a RGB-colored 3D x-y-z axis object
	 * 
	 * @param axislength
	 *            size of the x-y-z axes
	 * 
	 */
	public RGBAxes3D(float axislength) {
		this.axislength = axislength;
		common_to_all_constructors();
	}

	public RGBAxes3D(double axislength) {
		this.axislength = (float) axislength;
		common_to_all_constructors();
	}

	void common_to_all_constructors() {
		s = new Shape3D();
		s.setGeometry(createGeometry());
		addChild(s);
		addChild(Arrowhead(Colors.red, new Vector3f(axislength, .0f, .0f), -Math.PI / 2,0));
		addChild(Arrowhead(Colors.green, new Vector3f(.0f, axislength, .0f), 0,0));
		addChild(Arrowhead(Colors.blue, new Vector3f(.0f, .0f,axislength ), 0,Math.PI / 2));
		//addChild(Arrowhead(Colors.blue, new Vector3f(.0f, .0f,axislength ), 0,0));
		// addChild(Arrowhead(Colors.red,new Vector3f(0, .0f, .0f),0));
		addChild(generateAxesLabels());
	}

	private Geometry createGeometry() {
		IndexedLineArray axisLines = new IndexedLineArray(6, GeometryArray.COORDINATES | GeometryArray.COLOR_3, 30);

		// create line for X axis
		axisLines.setCoordinate(0, new Point3f(0.0f * axislength, 0.0f, 0.0f));
		axisLines.setCoordinate(1, new Point3f(1.0f * axislength, 0.0f, 0.0f));
		// create line for Y axis
		axisLines.setCoordinate(2, new Point3f(0.0f, 0.0f * axislength, 0.0f));
		axisLines.setCoordinate(3, new Point3f(0.0f, 1.0f * axislength, 0.0f));
		// create line for Z axis
		axisLines.setCoordinate(4, new Point3f(0.0f, 0.0f, 0.0f * axislength));
		axisLines.setCoordinate(5, new Point3f(0.0f, 0.0f, 1.0f * axislength));

		axisLines.setCoordinateIndex(0, 0);
		axisLines.setCoordinateIndex(1, 1);
		axisLines.setCoordinateIndex(2, 2);
		axisLines.setCoordinateIndex(3, 3);
		axisLines.setCoordinateIndex(4, 4);
		axisLines.setCoordinateIndex(5, 5);

		Color3f colors[] = new Color3f[6];
		for (int i = 0; i < 2; i++)
			colors[i] = Colors.red;
		for (int i = 2; i < 4; i++)
			colors[i] = Colors.green;
		for (int i = 4; i < 6; i++)
			colors[i] = Colors.blue;
		axisLines.setColors(0, colors);

		axisLines.setColorIndex(0, 0);
		axisLines.setColorIndex(1, 1);
		axisLines.setColorIndex(2, 2);
		axisLines.setColorIndex(3, 3);
		axisLines.setColorIndex(4, 4);
		axisLines.setColorIndex(5, 5);

		return axisLines;

	} // end of Axis createGeometry()

	private TransformGroup Arrowhead(Color3f col, Vector3f trans, double zRot, double xRot) {
		// X axis
		Appearance appearance = new Appearance();
		TransparencyAttributes ta = new TransparencyAttributes();
		ta.setTransparencyMode(TransparencyAttributes.BLENDED);
		ta.setTransparency(0.5f);
		appearance.setTransparencyAttributes(ta);
		ColoringAttributes ca = new ColoringAttributes(col, ColoringAttributes.NICEST);
		appearance.setColoringAttributes(ca);
		Cone cone = new Cone(axislength / 40, axislength / 10, appearance);
		TransformGroup tg = new TransformGroup();
		Transform3D arrowHeadTransform = new Transform3D();
		arrowHeadTransform.setTranslation(trans);
		Transform3D zRotTransform = new Transform3D();
		zRotTransform.rotZ(zRot);
		arrowHeadTransform.mul(zRotTransform);
		Transform3D xRotTransform = new Transform3D();
		xRotTransform.rotX(xRot);
		arrowHeadTransform.mul(xRotTransform);
		tg.setTransform(arrowHeadTransform);
		tg.addChild(cone);
		return tg;
	}

	public TransformGroup generateAxesLabels() {
		TransformGroup axislabelTrans = new TransformGroup();

		Font3D font3d = new Font3D(new Font("Display", Font.PLAIN, 6), new FontExtrusion());

		float axeslength = axislength;
		float i_xaxes = axislength;
		float i_yaxes = axislength;
		// X-Axis Label
		// Text3D xfont = new Text3D(font3d, new String("X"), new
		// Point3f((i_xaxes + axeslength + 0.5f),
		// (i_yaxes - 0.25f), 0.0f));
		// Shape3D xshape = new Shape3D(xfont);
		Text3D xfont = new Text3D(font3d, new String("X"), new Point3f(axislength, axislength, 0));
		Shape3D xshape = new Shape3D(xfont);

		// Y-Axis Label
		Text3D yfont = new Text3D(font3d, new String("Y"), new Point3f((i_xaxes - 0.25f),
				(i_yaxes + axeslength + 0.5f), 0.0f));
		Shape3D yshape = new Shape3D(yfont);

		// Z-Axis Label
		Text3D zfont = new Text3D(font3d, new String("Z"), new Point3f((i_xaxes - 0.5f), (i_yaxes - 0.5f),
				(axeslength + 0.5f)));
		Shape3D zshape = new Shape3D(zfont);

		axislabelTrans.addChild(xshape);
		axislabelTrans.addChild(yshape);
		axislabelTrans.addChild(zshape);

		return axislabelTrans;
	} // End generateAxesLabels

} // end of class Axis

