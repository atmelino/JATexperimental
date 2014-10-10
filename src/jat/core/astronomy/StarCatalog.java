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

package jat.core.astronomy;

import jat.core.util.PathUtil;
import jat.core.util.jatMessages;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class StarCatalog {
	public List<Stardata> manystardata;
	PathUtil p;
	public int a;
	jatMessages messages;

	// boolean bugFound = true;

	public StarCatalog(PathUtil p) {
		this.p = p;
	}

	public StarCatalog(PathUtil p, jatMessages messages) {
		this.p = p;
		this.messages = messages;
	}

	public void load() {
		// if (bugFound) {

		manystardata = new ArrayList<Stardata>();

		String fileName = p.data_path + "core/astronomy/hyg_100.csv";
		if (messages != null)
			messages.addln("[StarCatalog] " + fileName);
		// System.out.println("[StarCatalog] " + fileName);

		try {
			// Create a URL for the desired page
			// If it is called from an applet, it starts with "file:" or
			// "http:"
			// If it's an application, we need to add "file:" so that
			// BufferReader works
			boolean application;
			if (fileName.startsWith("file") || fileName.startsWith("http"))
				application = false;
			else
				application = true;
			if (application)
				fileName = "file:" + fileName;
			URL url = new URL(fileName);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					url.openStream()));
			
//			Scanner s=new Scanner(in);
//            s.useLocale(Locale.US);
			
			
			String line;
			// read over first row header line
			in.readLine();

			while (null != (line = in.readLine())) {
				// System.out.println("[StarCatalog] " + line);
//				if (messages != null)
//					messages.addln("[StarCatalog] " + line);
				String[] tokens = line.split(",");
//				for (int i = 0; i < tokens.length; i++)
//					System.out.println("[StarCatalog] " + tokens[i]);
				//System.out.println("[StarCatalog] " + tokens[6]+" "+tokens[7]+" "+tokens[8]);
				Stardata sd=new Stardata(tokens[6],Double.parseDouble(tokens[7]), Double.parseDouble(tokens[8]));
				 manystardata.add(sd);

			}
			in.close();
		} catch (MalformedURLException e) {
			System.out.println("MalformedURLException");
		} catch (IOException e) {
		}

	}
	// System.out.println("[StarCatalog] load out");
	// }
}

// try {
// reader = new CSVReader(new FileReader(star_data_file));
// reader.readNext(); // read over header line
// while ((nextLine = reader.readNext()) != null) {
// // for (int i = 0; i < nextLine.length; i++)
// // System.out.print(nextLine[i] + " | ");
// // System.out.println();
// manystardata.add(new Stardata(nextLine[6],
// Double.parseDouble(nextLine[7]), Double
// .parseDouble(nextLine[8])));
// }
// } catch (IOException e) {
// System.out.println("Problem loading star database");
// e.printStackTrace();
// }

