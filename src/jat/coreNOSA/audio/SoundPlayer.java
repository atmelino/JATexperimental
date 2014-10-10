package jat.coreNOSA.audio;

/* JAT: Java Astrodynamics Toolkit
 *
 * Copyright (c) 2003 National Aeronautics and Space Administration. All rights reserved.
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
 * 
 * File Created on Jul 2, 2003
 */
 
import jat.coreNOSA.util.FileUtil;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.net.URI;
import java.net.URL;

/**
 * <P>
 * The SoundPlayer Class provides a utility for playing sound files.
 *
 * @author 
 * @version 1.0
 */ 
public class SoundPlayer {
	
	/** Play the sound file.
	 * @param filename String containing the directory and filename
	 */	
	public static void play(String filename){

//		try {
//			File file = new File(filename);
//			URI uri = file.toURI();
//			System.out.println(uri);
//			URL url = uri.toURL();
//			System.out.println(url);
//			String url_string = url.toString();
////			url_string = url_string.replaceAll("%20", " ");
//			url_string = url_string.replaceAll("file:/C", "file:///C");
//			System.out.println("url_string = "+url_string);
//			URL url_final = new URL(url_string);
//			AudioClip clip = Applet.newAudioClip(url_final);
//			
//			clip.play();
//		} catch (Exception e){
//			e.printStackTrace();
//			System.exit(1);
//		}
		Runplay play = new Runplay(filename);
		play.run();
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("done");
	}
	
	public static void main(String[] args){
		String fs = FileUtil.file_separator();
		String dir = FileUtil.getClassFilePath("jat.audio", "SoundPlayer")+"sounds";
		String file = dir+fs+"justwhat.wav";
		System.out.println("file = "+file);
		SoundPlayer.play(file);
		SoundPlayer.play(file);
		
	}

	private static class Runplay implements Runnable {
		String file;

		public Runplay(String f){
			file = f; 
		}

		public void run() {
			try {
				File file = new File(this.file);
				URI uri = file.toURI();
				System.out.println(uri);
				URL url = uri.toURL();
				System.out.println(url);
				String url_string = url.toString();
//				url_string = url_string.replaceAll("%20", " ");
				url_string = url_string.replaceAll("file:/C", "file:///C");
				System.out.println("url_string = "+url_string);
				URL url_final = new URL(url_string);
				AudioClip clip = Applet.newAudioClip(url_final);
				
				clip.play();
			} catch (Exception e){
				e.printStackTrace();
				System.exit(1);
			}
		}
    }


}
