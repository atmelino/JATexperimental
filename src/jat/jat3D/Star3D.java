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

import jat.core.util.PathUtil;
import jat.core.util.jatMessages;
import jat.coreNOSA.cm.cm;

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
 * Star3D class
 * 
 * @author Tobias Berthold
 */
public class Star3D extends Body3D {
	float radius;
	Appearance app;
	Color3f Starcolor; // Star color if texture not found
	String images_path;

	public Star3D(PathUtil path, jatMessages messages, float scale) {
		super();
		this.messages=messages;
		this.scale = scale;
		images_path = path.root_path + "data/jat3D/images_hires/";
		radius = (float) cm.sun_radius;
		Starcolor = Colors.blue;
		
		String fileName= images_path + "sun.jpg";

		
		// Create a URL for the desired page
		// If it is called from an applet, it starts with "file:" or "http:"
		// If it's an application, we need to add "file:" so that BufferReader works
		boolean application;
		if (fileName.startsWith("file") || fileName.startsWith("http"))
			application = false;
		else
			application = true;
		if (application)
			fileName = "file:" + fileName;
		messages.addln("[Star3D] "+fileName);
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
			//e.printStackTrace();
		} catch (IOException e) {
			app = createMatAppear_star(Colors.blue, Colors.white, 10.0f);
			//e.printStackTrace();
		}
		
		addChild(new Sphere(scale * radius, Sphere.GENERATE_NORMALS | Sphere.GENERATE_TEXTURE_COORDS, 60, app));

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
