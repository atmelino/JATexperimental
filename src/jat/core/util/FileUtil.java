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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * 
 * This class makes it possible to use the same code to open and read from a
 * file, regardless of whether the calling class is an application or an applet
 * - inside a jar file or a file system with separate class files - on the
 * Internet or a local file system the resource to be used - is inside a jar
 * file or in a separate file
 * 
 * 
 * @author Tobias Berthold
 * 
 */
public class FileUtil {

	BufferedReader in;

	public FileUtil(String fileName) throws IOException {

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
		URL url = new URL(fileName);
		in = new BufferedReader(new InputStreamReader(url.openStream()));
	}

	public String readLine() throws IOException {
		return in.readLine();
	}

	public void close() throws IOException
	{
		
		in.close();
	}	
}
