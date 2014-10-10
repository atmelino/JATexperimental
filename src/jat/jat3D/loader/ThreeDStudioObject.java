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

package jat.jat3D.loader;

import jat.jat3D.Body3D;
import jat.jat3D.Colors;
import jat.jat3D.Cylinder3D;
import jat.jat3D.Sphere3D;

import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import com.mnstarfire.loaders3d.Inspector3DS;

/** 3DStudio Object class
 * @author Tobias Berthold
 */
public class ThreeDStudioObject extends Body3D
{
	Sphere3D left, right;
	Cylinder3D lc,rc;

	public ThreeDStudioObject( String filename, float scale)
	{
		super();
		super.scale = scale;
		String fullname;
		// Construct the Lw3d loader and load the file
		fullname = ThreeDStudio_path + filename;
		System.out.println(fullname);

		Inspector3DS loader = new Inspector3DS(fullname); // constructor
		loader.parseIt(); // process the file
		TransformGroup theModel = loader.getModel();

		//Orient vehicle such that its main axis is lined up with x-axis
		Transform3D Rotate = new Transform3D();
		Transform3D R1 = new Transform3D();
		Transform3D R2 = new Transform3D();
		Rotate.setIdentity();
		R1.setIdentity();
		R2.setIdentity();
		R1.rotZ(Math.PI / 2);
		R2.rotY(Math.PI / 2);
		R2.mul(R1); // R2 = R2 . R1
		Rotate = R2;

		// For MarsOrbit class
		//Rotate.rotY(0.6 * Math.PI);

		// These values compensate for the fact that carrier.3ds is not centered
		// at the center of mass. Different for every model- make parameter later
		Vector3d V = new Vector3d();
		Transform3D T_3D = new Transform3D();
		V.x = 875.f;
		V.y = -222.f;
		V.z = -209.f;
		T_3D.set(V);
		T_3D.mul(Rotate, T_3D);
		theModel.setTransform(T_3D);

		super.set_scale(scale);

		setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		setCapability(TransformGroup.ALLOW_TRANSFORM_READ);


		// Thrust plumes
		// values for sphere
//		Point3d lt = new Point3d(-380.f, -42.f, 0.f); //right thruster
//		Point3d rt = new Point3d(-380.f, 35.f, 0.f); //left thruster
		//addChild(left = new Sphere3D( 18, Colors.white,Colors.turquoise)); 
//		addChild(left  = new Sphere3D( 18, Colors.white,Colors.turquoise,lt.x, lt.y, lt.z)); 
//		addChild(right = new Sphere3D( 18, Colors.white,Colors.turquoise,rt.x, rt.y, rt.z));
//		left.set_scale(0);
//		right.set_scale(0);
		
 
		// values for cylinder
		Point3d rt = new Point3d(-485.f, -42.f, 0.f); //left thruster
		Point3d lt = new Point3d(-485.f, 35.f, 0.f); //right thruster
		addChild(rc = new Cylinder3D( 18,200, Colors.turquoise));
		addChild(lc = new Cylinder3D( 18,200, Colors.turquoise));
		rc.set_attitude(0,0,0.5*Math.PI);
		lc.set_attitude(0,0,0.5*Math.PI);
		rc.set_position(rt); 
		lc.set_position(lt); 
		rc.set_scale(0.01);
		lc.set_scale(0.01);

		addChild(theModel);

		//BoundingSphere bounds = new BoundingSphere(rt, 40.f);//100000000.0
		//addChild(jat_light.PointLight(bounds));
	}
	
	public void thrusters_on()
	{
		lc.set_scale(1.);
		rc.set_scale(1.);
	}
}
