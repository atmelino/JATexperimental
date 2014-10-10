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
 * The TwoPlots Class provides a way to create a page with two plots
 * using Ptplot.
 *
 * @author 
 * @version 1.0
 */

public class TwoPlots extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7776468877249739810L;

	/** the top plot */
    public Plot topPlot = new Plot();
    
    /** the bottom plot */
    public Plot bottomPlot = new Plot();


    /** Default constructor.
     */
    public TwoPlots() {

        // Set the size of the toplevel window.
        setSize(560, 700);            // width, height in pixels

        // Create the top plot by calling methods.
        topPlot.setSize(350,300);
        topPlot.setButtons(true);

        // Create the bottom plot by calling methods.
        bottomPlot.setSize(350,300);
        bottomPlot.setButtons(true);


        // Layout the two plots
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        getContentPane().setLayout(gridbag);

        // Handle the topPlot
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        gridbag.setConstraints(topPlot, c);
        getContentPane().add(topPlot);

        // Handle the bottomPlot
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        gridbag.setConstraints(bottomPlot, c);
        getContentPane().add(bottomPlot);

    }

}
