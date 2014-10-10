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

import jat.core.astronomy.SolarSystemBodies;
import jat.core.ephemeris.DE405Body.body;
import jat.core.util.PathUtil;
import jat.core.util.jatMessages;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.media.j3d.Appearance;
import javax.media.j3d.Material;
import javax.media.j3d.TextureAttributes;
import javax.vecmath.Color3f;

import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.image.TextureLoader;

/**
 * Planet3D class
 * 
 * @author Tobias Berthold
 */
public class Planet3D extends Body3D {
	float radius;
	Appearance app;
	Color3f Planetcolor; // planet color if texture not found
	Appearance appear;
	String images_path;
	SolarSystemBodies sb;

	// int divisions = 60; // number of divisions for sphere

	public Planet3D(PathUtil p, jatMessages messages, body planet, float scale) {
		super();
		this.scale = scale;
		// radius = (float) 1000f;
		this.messages = messages;
		images_path = p.root_path + "data/jat3D/images_hires/";
		sb = new SolarSystemBodies();

		String fileName = null;

		switch (planet) {
		case MERCURY:
			fileName = images_path + "mercury.jpg";
			radius = (float) sb.Bodies[body.MERCURY.ordinal()].radius;
			Planetcolor = Colors.red;
			break;
		case VENUS:
			fileName = images_path + "venus.jpg";
			radius = (float) sb.Bodies[body.VENUS.ordinal()].radius;
			Planetcolor = Colors.green;
			break;
		case EARTH:
			fileName = images_path + "earth.jpg";
			radius = (float) sb.Bodies[body.EARTH.ordinal()].radius;
			Planetcolor = Colors.blue;
			break;
		case MARS:
			fileName = images_path + "mars.jpg";
			radius = (float) sb.Bodies[body.MARS.ordinal()].radius;
			Planetcolor = Colors.blue;
			break;
		case JUPITER:
			fileName = images_path + "jupiter.jpg";
			radius = (float) sb.Bodies[body.JUPITER.ordinal()].radius;
			Planetcolor = Colors.orange;
			break;
		case SATURN:
			fileName = images_path + "saturn.jpg";
			radius = (float) sb.Bodies[body.SATURN.ordinal()].radius;
			Planetcolor = Colors.orange;
			break;
		case URANUS:
			fileName = images_path + "uranus.jpg";
			radius = (float) sb.Bodies[body.URANUS.ordinal()].radius;
			Planetcolor = Colors.orange;
			break;
		case NEPTUNE:
			fileName = images_path + "neptune.jpg";
			radius = (float) sb.Bodies[body.NEPTUNE.ordinal()].radius;
			Planetcolor = Colors.orange;
			break;
		case PLUTO:
			fileName = images_path + "pluto.jpg";
			radius = (float) sb.Bodies[body.PLUTO.ordinal()].radius;
			Planetcolor = Colors.orange;
			break;
		case MOON:
			fileName = images_path + "moon.jpg";
			radius = (float) sb.Bodies[body.MOON.ordinal()].radius;
			Planetcolor = Colors.blue;
			break;
		}

		// Create a URL for the desired page
		// If it is called from an applet, it starts with "file:" or "http:"
		// If it's an application, we need to add "file:" so that BufferReader
		// works
		boolean application;
		if (fileName.startsWith("file") || fileName.startsWith("http"))
			application = false;
		else
			application = true;
		if (application)
			fileName = "file:" + fileName;
		messages.addln("[Planet3D] " + fileName);
		try {
			URL TextureURL = new URL(fileName);
			BufferedImage img = ImageIO.read(TextureURL);
			TextureLoader tex = new TextureLoader(img);
			TextureAttributes ta = new TextureAttributes();
			ta.setTextureMode(TextureAttributes.MODULATE);
			app = createMatAppear_star(Colors.white, Colors.white, 10.0f);
			app.setTextureAttributes(ta);
			app.setTexture(tex.getTexture());
		} catch (MalformedURLException e) {
			app = createMatAppear_star(Colors.blue, Colors.white, 10.0f);
			System.out.println("MalformedURLException");
			// e.printStackTrace();
		} catch (IOException e) {
			app = createMatAppear_star(Colors.blue, Colors.white, 10.0f);
			System.out.println("IOException");
			// e.printStackTrace();
		}

		addChild(new Sphere(scale * radius, Sphere.GENERATE_NORMALS | Sphere.GENERATE_TEXTURE_COORDS, 60, app));

	}

	protected static Appearance createMatAppear_planet(Color3f dColor, Color3f sColor, float shine) {
		Appearance appear = new Appearance();
		Material material = new Material();
		material.setDiffuseColor(dColor);
		material.setSpecularColor(sColor);
		material.setShininess(shine);
		material.setEmissiveColor(0.1f, 0.1f, 0.1f);
		appear.setMaterial(material);
		return appear;
	}

	static Appearance createMatAppear_star(Color3f dColor, Color3f sColor, float shine) {
		Appearance appear = new Appearance();
		Material material = new Material();
		material.setDiffuseColor(dColor);
		material.setSpecularColor(sColor);
		material.setShininess(shine);
		material.setEmissiveColor(1.f, 1.f, 1.f);
		appear.setMaterial(material);
		return appear;
	}

}
