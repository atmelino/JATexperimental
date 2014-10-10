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
/*
 File: CapturingCanvas3D.java

 University of Applied Science Berne,HTA-Biel/Bienne,
 Computer Science Department.

 Diploma thesis J3D Solar System Simulator
 Originally written by Marcel Portner & Bernhard Hari (c) 2000

 CVS - Information :

 $Header: /cvsroot/jat/jat/jat/vr/CapturingCanvas3D.java,v 1.8 2003/10/31 17:43:05 tberthold Exp $
 $Author: tberthold $
 $Date: 2005-01-11 13:33:39 -0500 (Tue, 11 Jan 2005) $
 $State: Exp $

*/

/**
 * Class CapturingCanvas3D, using the instructions from the Java3D
 * FAQ pages on how to capture a still image in jpeg format.
 *
 * A capture button would call a method that looks like
 *
 * <pre>
 *  public static void captureImage(CapturingCanvas3D MyCanvas3D) {
 *    MyCanvas3D.writeJPEG_ = true;
 *    MyCanvas3D.repaint();
 *  }
 * </pre>
 *
 * Peter Z. Kunszt
 * Johns Hopkins University
 * Dept of Physics and Astronomy
 * Baltimore MD
 *
 * @author Marcel Portner & Bernhard Hari
 * @version $Revision: 1 $
 */

package jat.jat3D;
import java.awt.GraphicsConfiguration;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.media.j3d.Canvas3D;
import javax.media.j3d.GraphicsContext3D;
import javax.media.j3d.ImageComponent;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.Raster;
import javax.vecmath.Point3f;

// TODO: change code to javax.imageio.ImageIO.write(image, format, stream) 
// http://www.java.net/node/662849


public class CapturingCanvas3D extends Canvas3D
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4329234650673891337L;
	//static String frames_path="frames/";
	public boolean writeJPEG_;
	private int postSwapCount_;
	String frames_path;

	/**
	* Constructor that generate a Canvas3D.
	*
	* @param gc  the GraphicsConfiguration
	*/
	public CapturingCanvas3D(GraphicsConfiguration gc, String frames_path)
	{
		super(gc);
		this.frames_path = frames_path;
		postSwapCount_ = 0;
		writeJPEG_ = false;
	}

	/**
	* Override Canvas3D's postSwap method to save a JPEG of the canvas.
	*/
	public void postSwap()
	{
		if (writeJPEG_)
		{
			System.out.println("Writing JPEG ");
			int dimX = this.getScreen3D().getSize().width;
			int dimY = this.getScreen3D().getSize().height;

			// The raster components need all be set!
			Raster ras =
				new Raster(
					new Point3f(-1.0f, -1.0f, -1.0f),
					Raster.RASTER_COLOR,
					0,
					0,
					dimX,
					dimY,
					new ImageComponent2D(
						ImageComponent.FORMAT_RGB,
						new BufferedImage(dimX, dimY, BufferedImage.TYPE_INT_RGB)),
					null);
			GraphicsContext3D ctx = getGraphicsContext3D();
			ctx.readRaster(ras);

			// Now strip out the image info
			BufferedImage img = ras.getImage().getImage();

			// write that to disk....
			try
			{
				FileOutputStream out = new FileOutputStream(frames_path + "Capture00" + postSwapCount_ + ".jpg");
				/*
				 * deprecated code, replace
				JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
				JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(img);
				param.setQuality(1.0f, false); // x% quality JPEG
				encoder.setJPEGEncodeParam(param);
				encoder.encode(img);
				*/
				writeJPEG_ = false;
				out.close();
			} catch (IOException e)
			{
				System.out.println("I/O exception!Maybe path" + frames_path + "does not exist?");
			}
			postSwapCount_++;
		}
	}

	public void takePicture()
	{
		System.out.println("Writing JPEG "+postSwapCount_);
		int dimX = this.getScreen3D().getSize().width;
		int dimY = this.getScreen3D().getSize().height;

		
		// The raster components need all be set!
		Raster ras =
			new Raster(	new Point3f(-1.0f, -1.0f, -1.0f),
				Raster.RASTER_COLOR,
				0,
				0,
				dimX,
				dimY,
				new ImageComponent2D(
					ImageComponent.FORMAT_RGB,
					new BufferedImage(dimX, dimY, BufferedImage.TYPE_INT_RGB)),
				null);
		GraphicsContext3D ctx = getGraphicsContext3D();
		ctx.readRaster(ras);

		// Now strip out the image info
		BufferedImage img = ras.getImage().getImage();
//		BufferedImage img = new BufferedImage(800,600,1);

		
		// write that to disk....
		try
		{
			String filename="Capture";
			if(postSwapCount_<10) filename="Capture00";
			if(postSwapCount_>=10&&postSwapCount_<100) filename="Capture0";
			FileOutputStream out = new FileOutputStream(frames_path + filename + postSwapCount_ + ".jpg");
/*
 * 				 * deprecated code, replace
 

			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(img);
			param.setQuality(1.0f, false); // x% quality JPEG
			encoder.setJPEGEncodeParam(param);
			encoder.encode(img);
*/			out.close();
		} catch (IOException e)
		{
			System.out.println("I/O exception!Maybe path" + frames_path + "does not exist?");
		}
		
		postSwapCount_++;
	}

}
