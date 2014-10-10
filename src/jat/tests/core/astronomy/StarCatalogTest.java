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

package jat.tests.core.astronomy;

import jat.core.astronomy.StarCatalog;
import jat.core.astronomy.Stardata;
import jat.core.util.PathUtil;

import java.io.IOException;

public class StarCatalogTest {

	public static void main(String[] args) throws IOException {
		StarCatalog s;
		PathUtil p = new PathUtil();
		s = new StarCatalog(p);
		s.load();
		int number_of_stars = s.manystardata.size();
		Stardata sd;

		for (int i = 0; i < number_of_stars; i++) {

			sd = (Stardata) s.manystardata.get(i);
			System.out.println(sd.ProperName + " " + sd.RA + " " + sd.dec);

		}
	}
}
