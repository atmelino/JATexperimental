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

package jat.examplesNOSA.TwoBodyExample;

import jat.coreNOSA.algorithm.integrators.Printable;
import jat.coreNOSA.cm.TwoBody;
import jat.coreNOSA.plotutil.SinglePlot;

/**
 * <P>
 * The TwoBodyExample Class provides an example of how the TwoBody class can be used to
 * propagate a two body orbit and plot the trajectory.
 *
 * @author 
 * @version 1.0
 */

public class TwoBodyExample implements Printable
{

    SinglePlot traj_plot = new SinglePlot();

    /** Creates a new instance of TwoBodyExample */
    public TwoBodyExample()
    {
        // set up the trajectory plot
        traj_plot.setTitle("Two Body Trajectory");
        traj_plot.plot.setXLabel("x (km)");
        traj_plot.plot.setYLabel("y (km)");
    }

    /** Implements the Printable interface to get the data out of the propagator and pass it to the plot.
     *  This method is executed by the propagator at each integration step.
     * @param t Time.
     * @param y Data array.
     */
    public void print(double t, double [] y)
    {

        // handle the first variable for plotting - this is a little mystery but it works
        boolean first = true;
        if (t == 0.0) first = false;

        // add data point to the plot
        traj_plot.plot.addPoint(0, y[0], y[1], first);

        // also print to the screen for warm fuzzy feeling
        System.out.println(t+" "+y[0]+" "+y[1]);
    }

    /** Runs the example.
     * @param args Arguments.
     */
    public static void main(String[] args)
    {
        TwoBodyExample x = new TwoBodyExample();

        // create a TwoBody orbit using orbit elements
        TwoBody sat = new TwoBody(7000.0, 0.3, 0.0, 0.0, 0.0, 0.0);

        // find out the period of the orbit
        double period = sat.period();

        // set the final time = one orbit period
        double tf = period;

        // set the initial time to zero
        double t0 = 0.0;

        // propagate the orbit
        sat.propagate(t0, tf, x, true);

        // make the plot visible
        x.traj_plot.setVisible(true);
        x.traj_plot.plot.setXRange(-15000.,15000.);
        x.traj_plot.plot.setYRange(-15000.,15000.);

    }
}
