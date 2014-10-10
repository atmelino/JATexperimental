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

package jat.core.ephemeris;

import java.util.EnumSet;

public class DE405Body {

	public enum body {
		SUN, MERCURY, VENUS, EARTH, MARS, JUPITER, SATURN, URANUS, NEPTUNE, PLUTO, MOON;
		static final int amount = EnumSet.allOf(body.class).size();
		private static body[] val = new body[amount];
		static {
			for (body q : EnumSet.allOf(body.class)) {
				val[q.ordinal()] = q;
			}
		}

		public static body fromInt(int i) {
			return val[i];
		}

		public body next() {
			return fromInt((ordinal() + 1) % amount);
		}
		public static String[] name = { "===", "Mercury", "Venus", "Earth", "Mars", "Jupiter", "Saturn", "Uranus",
			"Neptune", "Pluto", "Moon" };

	};


}
