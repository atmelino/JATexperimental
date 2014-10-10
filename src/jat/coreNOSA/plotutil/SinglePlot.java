/* JAT: Java Astrodynamics Toolkit
 *
 * Copyright (c) 2002 National Aeronautics and Space Administration. All rights reserved.
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

package jat.coreNOSA.plotutil;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;

import ptolemy.plot.Plot;
/**
 * <P>
 * The SinglePlot Class provides a way to create a page with one plots
 * using Ptplot.
 *
 * @author 
 * @version 1.0
 */

public class SinglePlot extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 674515018881954664L;
	/** The plot object */
    public Plot plot = new Plot();


    /** Default constructor.
     */
    public SinglePlot() {

        // Set the size of the toplevel window.
        setSize(560, 560);            // width, height in pixels

        // Create the top plot by calling methods.
        plot.setSize(300,300);
        plot.setButtons(true);


        // Layout the two plots
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        getContentPane().setLayout(gridbag);

        // Handle the Plot
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.BOTH;
       c.weightx = 1.0;
        c.weighty = 1.0;
        gridbag.setConstraints(plot, c);
        getContentPane().add(plot);

    }

}
