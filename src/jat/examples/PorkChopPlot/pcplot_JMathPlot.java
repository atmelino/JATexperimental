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

package jat.examples.PorkChopPlot;

import jat.core.cm.porkChopPlot;
import jat.core.ephemeris.DE405Body.body;
import jat.core.ephemeris.DE405Plus;
import jat.core.plot.plot.Plot3DPanel;
import jat.core.spacetime.TimeAPL;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;

import javax.swing.JApplet;
import javax.swing.JPanel;


public class pcplot_JMathPlot extends JApplet {

	private static final long serialVersionUID = 9137365030689531711L;
	Plot3DPanel plotpanel;
	JPanel panel;
	int steps = 50;
	int search_time = 86400 * 2000;
	DE405Plus my_eph;

	public void init() {
		plotpanel = new Plot3DPanel();
		plotpanel.addLegend("SOUTH");
		plotpanel.setSize(600, 600);
		plotpanel.setPreferredSize(new Dimension(600, 600));
		
		TimeAPL search_depart_time_start = new TimeAPL(2003, 5, 1, 1, 1, 1);
		TimeAPL search_arrival_time_start = new TimeAPL(2003, 12, 1, 1, 1, 1);

		my_eph = new DE405Plus();
		porkChopPlot p = new porkChopPlot(my_eph);
		try {
			p.make_porkchop_plot(body.EARTH, body.MARS,search_depart_time_start, search_arrival_time_start, 2000, steps);
			p.A.print();
		} catch (IOException e) {
			e.printStackTrace();
		}

		double[] Xdata = new double[steps];
		for (int i = 0; i < steps; i++) {
			Xdata[i] = i - steps / 2;
		}
		plotpanel.addGridPlot("data", Xdata,Xdata,p.A.A);

		panel = new JPanel(new BorderLayout());
		panel.add(plotpanel, BorderLayout.CENTER);

		this.add(panel);

	}
}
