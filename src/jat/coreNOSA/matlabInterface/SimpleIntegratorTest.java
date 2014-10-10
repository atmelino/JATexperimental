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

package jat.coreNOSA.matlabInterface;
import jat.coreNOSA.algorithm.integrators.Printable;
import jat.coreNOSA.algorithm.integrators.RungeKutta8;
import jat.coreNOSA.plotutil.TwoPlots;

/**
 * <P>
 * The SimpleIntegrator Class provides an example of how the RungeKutta8 class can be used to
 * integrate a set of ODEs and plot the solution.
 *
 * @author 
 * @version 1.0
 */

public class SimpleIntegratorTest implements Printable {

    TwoPlots traj_plot = new TwoPlots();

    /** Creates a new instance of SimpleIntegrator */
    public SimpleIntegratorTest() {
        // set up the trajectory plot
        traj_plot.setTitle("Simple Oscillator");
        traj_plot.topPlot.setXLabel("t");
        traj_plot.topPlot.setYLabel("x");
        traj_plot.bottomPlot.setXLabel("t");
        traj_plot.bottomPlot.setYLabel("y");

    }


    /** Implements the Printable interface to get the data out of the propagator and pass it to the plot.
     *  This method is executed by the propagator at each integration step.
     * @param t Time.
     * @param y Data array.
     */
    public void print(double t, double [] y){

        // handle the first variable for plotting - this is a little mystery but it works
        boolean first = true;
        if (t == 0.0) first = false;

        // print to the screen for warm fuzzy feeling
        System.out.println(t+" "+y[0]+" "+y[1]+" "+first);

        // add data point to the plot
        traj_plot.topPlot.addPoint(0, t, y[0], first);
        traj_plot.bottomPlot.addPoint(0, t, y[1], first);

    }

    /** Runs the example.
     * @param args Arguments.
     */
    public static void main(String[] args)
    {

       // create an instance
        SimpleIntegratorTest si = new SimpleIntegratorTest();
        
        // make it go
        si.go();

    }
    
    public void go(){
        // create an RungeKutta8 integrator with step-size of 0.1
        RungeKutta8 rk8 = new RungeKutta8(0.1);
        
        MatlabDerivs derivs = new MatlabDerivs("eom");

        // initialize the variables
        double [] x0 = new double[2];
        x0[0] = 1.0;
        x0[1] = 0.0;

        // set the final time
        double tf = 10.0;

        // set the initial time to zero
        double t0 = 0.0;

        // integrate the equations
        rk8.integrate(t0, x0, tf, derivs, this, true);

        // make the plot visible
        this.traj_plot.setVisible(true);
   	
    }
}
