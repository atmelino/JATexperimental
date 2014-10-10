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

package jat.core.util;

import java.applet.Applet;
import java.io.File;
import java.net.URL;

/**
 * @author Tobias Berthold Path Utilities returns the path to resources as a
 *         String possible scenarios: the class that uses PathUtil - is an
 *         application or an applet - inside a jar file or a file system with
 *         separate class files - on the Internet or a local file system the
 *         resource to be used - is inside a jar file or in a separate file
 * 
 *         Note: This relies on the root of the project being named "jat" !!!
 * 
 */
public class PathUtil {
	boolean debug = false;

	public String root_path;
	public String current_path;
	public String DE405Path;
	public String data_path;
	public String fs = File.separator;
	jatMessages messages;

	/**
	 * Use this if called from application
	 */
	public PathUtil() {
		if (debug)
			System.out.println("<PathUtil > constructor ");
		root_path = find_root();
		data_path = root_path + "data/";
		DE405Path = root_path + "data/core/ephemeris/DE405data/";

	}

	/**
	 * Use this if called from applet
	 */
	public PathUtil(Applet myapplet, jatMessages messages) {

		this.messages = messages;
		// current_path = find_current_path(myapplet);
		root_path = find_root(myapplet);
		data_path = root_path + "data/";
		DE405Path = root_path + "data/core/ephemeris/DE405data/";

		if (messages != null) {
			messages.addln("[PathUtil root path] " + root_path);
			// messages.addln("[PathUtil] " + data_path);
			// messages.addln("[PathUtil] " + DE405Path);
		}
	}

	/**
	 * Use this if called from applet
	 */
	public PathUtil(Applet myapplet) {

		// current_path = find_current_path(myapplet);
		root_path = find_root(myapplet);
		data_path = root_path + "data/";
		DE405Path = root_path + "data/core/ephemeris/DE405data/";

		// if (debug)
		// System.out.println("[PathUtil current_path] " + current_path);
	}

	/**
	 * @return path to root of the project Finds the path to the root of the
	 *         project. Starts with the path of the class from which it is
	 *         called, strips everything from the end until it finds the string
	 *         "jat" or "jatdevelop". Works with an open folder structure or
	 *         inside a jar file, on a local hard disk as well as on the
	 *         Internet.
	 */
	public String find_root(Applet myapplet) {

		URL pathURL = myapplet.getCodeBase();
		String pathName = pathURL.toExternalForm();
		//System.out.println("[PathUtil] getCodeBase " + myapplet.getCodeBase());
		//System.out.println("[PathUtil] getCodeBase " + pathName);

		// go forward in the directory tree until you find "jat"
		String[] numberSplit = pathName.split("/");
		String root_path = "";
		for (int i = 0; i < numberSplit.length; i++) {

			// System.out.println(numberSplit[i]);
			if (numberSplit[i].equals("jat"))
				break;
			if (numberSplit[i].equals("jatdevelop"))
				break;
			if (numberSplit[i].equals("jatexperimental"))
				break;
			root_path = root_path + numberSplit[i] + "/";
		}
		root_path = root_path + "jat" + "/";

		if (debug)
			System.out.println("[PathUtil root_path] " + root_path);

		return (root_path);
	}

	public String find_root() {

		String resource_path = PathUtil.class.getProtectionDomain()
				.getCodeSource().getLocation().getPath();
		if (debug)
			System.out.println("[PathUtil resource_path] " + resource_path);

		// go forward in the directory tree until you find "jat"
		String[] numberSplit = resource_path.split("/");
		String root_path = "/";
		for (int i = 1; i < numberSplit.length; i++) {

			// System.out.println(numberSplit[i]);
			if (numberSplit[i].equals("jat"))
				break;
			if (numberSplit[i].equals("jatdevelop"))
				break;
			if (numberSplit[i].equals("jatexperimental"))
				break;
			root_path = root_path + numberSplit[i] + "/";
		}
		root_path = root_path + "jat" + "/";

		if (debug)
			System.out.println("[PathUtil root_path] " + root_path);

		return (root_path);
	}

	/**
	 * @param a
	 * @return path where the calling applet resides Does not work when applet
	 *         is loaded from a web site
	 */
	public String find_current_path(Applet a) {
		if (debug)
			System.out.println("[PathUtil] current_path called");

		try {
			ResourceLoader c = new ResourceLoader();
			URL url = c.loadURL(a.getClass(), ".");
			// ResourceLoader c = new ResourceLoader();
			// URL url = c.loadURL(a.getClass(), "/");

			// ResourceLoader c = new ResourceLoader();
			// URL helpURL2 = c.loadURL(this.getClass(), relative_path);

			// System.out.println(url.getPath());
			return url.getPath();

		} catch (Exception e) {
			System.err
					.println("Couldn't find current path in jat.core.util.PathUtil");
			// System.exit(0);
			return "";
		}
	}

}

