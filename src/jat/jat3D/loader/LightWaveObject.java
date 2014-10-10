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

import com.sun.j3d.loaders.Loader;
import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.lw3d.Lw3dLoader;

/** LightWaveObject class
 * @author Tobias Berthold
 */
public class LightWaveObject extends Body3D
{

	public LightWaveObject( String filename, float scale)
	{
		super();
		this.scale = scale;
		String fullname;
		Scene s = null;

		// Construct the Lw3d loader and load the file
		fullname = Lightwave_path + filename;
		System.out.println(fullname);
		Loader lw3dLoader = new Lw3dLoader(Loader.LOAD_ALL);
		try
		{
			s = lw3dLoader.load(fullname);
		} catch (Exception e)
		{
			System.err.println("Exception loading file: " + e);
		}
		addChild(s.getSceneGroup());
	}
}
