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

package jat.examples.Plot;

import jat.core.plot.plot.FrameView;
import jat.core.plot.plot.Plot3DPanel;
import jat.core.plot.plot.PlotPanel;

public class plotsphere {

	public static void main(String[] args) {

		Plot3DPanel p = new Plot3DPanel();

		double radius=1;
	
		p.addSpherePlot("toto", radius);
		p.setFixedBounds(0, -1, 1);
		p.setFixedBounds(1, -1, 1);
		p.setFixedBounds(2, -1, 1);
		p.setLegendOrientation(PlotPanel.SOUTH);
		new FrameView(p);
	}
}
