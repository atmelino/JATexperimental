/* JAT: Java Astrodynamics Toolkit
 *
 * Copyright (c) 2002 National Aeronautics and Space Administration and the Center for Space Research (CSR),
 * The University of Texas at Austin. All rights reserved.
 *
 * This file is part of JAT. JAT is free software; you can
 * redistribute it and/or modify it under the terms of the
 * NASA Open Source Agreement
 * 
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * NASA Open Source Agreement for more details.
 *
 * You should have received a copy of the NASA Open Source Agreement
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 */
package jat.jat3D;

import javax.media.j3d.Appearance;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.LineArray;
import javax.media.j3d.Shape3D;
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
public class Axes3D extends Body3D {

	float length;
	// Color3f gray=new Color3f(0.3f,0.3f,0.3f);
	// Color3f pink=new Color3f(0.9f,0.1f,0.1f);
	Shape3D s;

	/**
	 * creates a 3D x-y-z axis object
	 */
	public Axes3D() {
		super();
		length = 15000.0f;
		s = new Shape3D();
		addChild(s);
	}

	/**
	 * creates a 3D x-y-z axis object
	 * 
	 * @param factor
	 *            size of the x-y-z axes
	 * 
	 */
	public Axes3D(float factor) {
		// this.length = factor;
		// s = new Shape3D();
		// s.setGeometry(createGeometry());
		// addChild(s);
		addChild(Axis(length));

	}

	public Axes3D(double length) {
		this.length = (float) length;
		// s = new Shape3D();
		// s.setGeometry(createGeometry());
		// addChild(s);
		// addChild(Arrowhead());
		addChild(Axis(this.length));
	}

	private Body3D Axis(float length) {

		LineArray a = new LineArray(6, LineArray.COORDINATES | LineArray.COLOR_3);

		a.setCoordinate(0, new Point3f(0.0f, 0.0f, 0.0f));
		a.setCoordinate(1, new Point3f(length, 0.0f, 0.0f));
		a.setCoordinate(2, new Point3f(0.0f, 0.0f, 0.0f));
		a.setCoordinate(3, new Point3f(0.0f, length, 0.0f));
		a.setCoordinate(4, new Point3f(0.0f, 0.0f, 0.0f));
		a.setCoordinate(5, new Point3f(0.0f, 0.0f, length));

		a.setColor(0, Colors.gray);
		a.setColor(1, Colors.gray);
		a.setColor(2, Colors.gray);
		a.setColor(3, Colors.gray);
		a.setColor(4, Colors.gray);
		a.setColor(5, Colors.gray);
		Body3D b = new Body3D();
		b.addChild(new Shape3D(a));
		b.addChild(Arrowhead());
		return b;
	}

	private TransformGroup Arrowhead() {
		// X axis
		Appearance appearance = new Appearance();
		TransparencyAttributes ta = new TransparencyAttributes();
		ta.setTransparencyMode(TransparencyAttributes.BLENDED);
		ta.setTransparency(0.5f);
		appearance.setTransparencyAttributes(ta);
		Color3f col = new Color3f(0.0f, 0.0f, 1.0f);
		ColoringAttributes ca = new ColoringAttributes(col, ColoringAttributes.NICEST);
		appearance.setColoringAttributes(ca);
		Cone cone = new Cone(length/40, length/10, appearance);
		TransformGroup tg = new TransformGroup();
		Transform3D transform1 = new Transform3D();
		Vector3f vector = new Vector3f(length, .0f, .0f);
		transform1.setTranslation(vector);
		Transform3D transform2 = new Transform3D();
		transform2.rotZ(-Math.PI/2);
		transform1.mul(transform2);
		tg.setTransform(transform1);
		tg.addChild(cone);
		return tg;
	}


	/*
	// private

	private Geometry createGeometry() {
		IndexedLineArray axisLines = new IndexedLineArray(18, GeometryArray.COORDINATES | GeometryArray.COLOR_3, 30);

		// create line for X axis
		axisLines.setCoordinate(0, new Point3f(-1.0f * length, 0.0f, 0.0f));
		axisLines.setCoordinate(1, new Point3f(1.0f * length, 0.0f, 0.0f));
		axisLines.setCoordinate(2, new Point3f(0.9f * length, 0.1f * length, 0.1f * length));
		axisLines.setCoordinate(3, new Point3f(0.9f * length, -0.1f * length, 0.1f * length));
		axisLines.setCoordinate(4, new Point3f(0.9f * length, 0.1f * length, -0.1f * length));
		axisLines.setCoordinate(5, new Point3f(0.9f * length, -0.1f * length, -0.1f * length));
		// create line for Y axis
		axisLines.setCoordinate(6, new Point3f(0.0f, -1.0f * length, 0.0f));
		axisLines.setCoordinate(7, new Point3f(0.0f, 1.0f * length, 0.0f));
		axisLines.setCoordinate(8, new Point3f(0.1f * length, 0.9f * length, 0.1f * length));
		axisLines.setCoordinate(9, new Point3f(-0.1f * length, 0.9f * length, 0.1f * length));
		axisLines.setCoordinate(10, new Point3f(0.1f * length, 0.9f * length, -0.1f * length));
		axisLines.setCoordinate(11, new Point3f(-0.1f * length, 0.9f * length, -0.1f * length));
		// create line for Z axis
		axisLines.setCoordinate(12, new Point3f(0.0f, 0.0f, -1.0f * length));
		axisLines.setCoordinate(13, new Point3f(0.0f, 0.0f, 1.0f * length));
		axisLines.setCoordinate(14, new Point3f(0.1f * length, 0.1f * length, 0.9f * length));
		axisLines.setCoordinate(15, new Point3f(-0.1f * length, 0.1f * length, 0.9f * length));
		axisLines.setCoordinate(16, new Point3f(0.1f * length, -0.1f * length, 0.9f * length));
		axisLines.setCoordinate(17, new Point3f(-0.1f * length, -0.1f * length, 0.9f * length));

		axisLines.setCoordinateIndex(0, 0);
		axisLines.setCoordinateIndex(1, 1);
		axisLines.setCoordinateIndex(2, 2);
		axisLines.setCoordinateIndex(3, 1);
		axisLines.setCoordinateIndex(4, 3);
		axisLines.setCoordinateIndex(5, 1);
		axisLines.setCoordinateIndex(6, 4);
		axisLines.setCoordinateIndex(7, 1);
		axisLines.setCoordinateIndex(8, 5);
		axisLines.setCoordinateIndex(9, 1);
		axisLines.setCoordinateIndex(10, 6);
		axisLines.setCoordinateIndex(11, 7);
		axisLines.setCoordinateIndex(12, 8);
		axisLines.setCoordinateIndex(13, 7);
		axisLines.setCoordinateIndex(14, 9);
		axisLines.setCoordinateIndex(15, 7);
		axisLines.setCoordinateIndex(16, 10);
		axisLines.setCoordinateIndex(17, 7);
		axisLines.setCoordinateIndex(18, 11);
		axisLines.setCoordinateIndex(19, 7);
		axisLines.setCoordinateIndex(20, 12);
		axisLines.setCoordinateIndex(21, 13);
		axisLines.setCoordinateIndex(22, 14);
		axisLines.setCoordinateIndex(23, 13);
		axisLines.setCoordinateIndex(24, 15);
		axisLines.setCoordinateIndex(25, 13);
		axisLines.setCoordinateIndex(26, 16);
		axisLines.setCoordinateIndex(27, 13);
		axisLines.setCoordinateIndex(28, 17);
		axisLines.setCoordinateIndex(29, 13);

		axisLines.setColor(0, Colors.gray);
		// axisLines.setColor(1,pink);
		// axisLines.setColor(7,pink);

		return axisLines;

	} // end of Axis createGeometry()

*/


} // end of class Axis
