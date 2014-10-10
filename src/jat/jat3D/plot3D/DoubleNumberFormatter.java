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

// Original Code under LGPL
// http://java.freehep.org/freehep-java3d/license.html

package jat.jat3D.plot3D;

import java.text.NumberFormat;


final class DoubleNumberFormatter
{
   DoubleNumberFormatter(int power)
   {
	   if (formatter == null)
		   formatter = NumberFormat.getInstance();
	   this.power = power;
   }
   void setFractionDigits(int fractDigits)
   {
	   formatter.setMinimumFractionDigits(fractDigits);
	   formatter.setMaximumFractionDigits(fractDigits);
   }
   String format(final double d)
   {
	   return formatter.format(power != 0 ? d / Math.pow(10.0, power) : d);
   }
   private static NumberFormat formatter = null;
   private int power;
}

