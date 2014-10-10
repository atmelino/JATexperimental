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
import javax.media.j3d.Material;
import javax.vecmath.Color3f;

import com.sun.j3d.utils.geometry.Sphere;

/** Sphere class
 * @author Tobias Berthold
 */
public class Sphere3D extends Body3D
{
	public static float size_factor = 1.0f;
	float radius;
	static Color3f EmissiveColor = Colors.darkgray;

	/**
	 * Constructor Sphere3D.
	 * @param radius radius of sphere
	 * @param DiffuseColor color of sphere
	 */
	public Sphere3D(float radius, Color3f DiffuseColor)
	{
		this.radius = radius;
		addChild(
			new Sphere(
				radius,
				Sphere.GENERATE_NORMALS,
				60,
				createMatAppear(DiffuseColor, Colors.white, EmissiveColor, 10.0f)));
	}

	public Sphere3D(float radius, Color3f DiffuseColor, Color3f EmissiveColor)
	{
		this.radius = radius;
		addChild(
			new Sphere(
				radius,
				Sphere.GENERATE_NORMALS,
				60,
				createMatAppear(DiffuseColor, Colors.white, EmissiveColor, 10.0f)));
	}

	public Sphere3D(float radius, Color3f DiffuseColor, Color3f EmissiveColor, double x, double y, double z)
	{
		this.radius = radius;
		addChild(
			new Sphere(
				radius,
				Sphere.GENERATE_NORMALS,
				60,
				createMatAppear(DiffuseColor, Colors.white, EmissiveColor, 10.0f)));
		set_position(x, y, z);
	}

	public Sphere3D(float radius, Color3f DiffuseColor, float x, float y, float z)
	{
		this.radius = radius;
		addChild(
			new Sphere(
				radius,
				Sphere.GENERATE_NORMALS,
				60,
				createMatAppear(DiffuseColor, Colors.white, EmissiveColor, 10.0f)));
		set_position(x, y, z);
	}

	public Sphere3D(float radius, Color3f DiffuseColor, double x, double y, double z)
	{
		this.radius = radius;
		addChild(
			new Sphere(
				radius,
				Sphere.GENERATE_NORMALS,
				60,
				createMatAppear(DiffuseColor, Colors.white, EmissiveColor, 10.0f)));
		set_position(x, y, z);
	}

	protected static Appearance createMatAppear(
		Color3f DiffuseColor,
		Color3f SpecularColor,
		Color3f EmissiveColor,
		float shine)
	{
		Appearance appear = new Appearance();
		Material material = new Material();
		material.setDiffuseColor(DiffuseColor);
		material.setSpecularColor(SpecularColor);
		material.setShininess(shine);
		material.setEmissiveColor(EmissiveColor);
		appear.setMaterial(material);

		//For Transparency
		//		TransparencyAttributes ta = new TransparencyAttributes();
		//		ta.setTransparency(0.5f);
		//		ta.setTransparencyMode(TransparencyAttributes.BLENDED);
		//		appear.setTransparencyAttributes(ta);

		return appear;
	}

	//    // Methods to be implemented in subclasses
	//    public void add()
	//    {
	//		//TG_scene.addChild(new Sphere(radius, Sphere.GENERATE_NORMALS, 60, createMatAppear_planet(red, white, 10.0f)));
	//		addChild(new Sphere(radius, Sphere.GENERATE_NORMALS, 60, createMatAppear_planet(red, white, 10.0f)));
	//    }

}
