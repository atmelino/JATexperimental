/* JAT: Java Astrodynamics Toolkit
 *
 * Copyright (c) 2002 National Aeronautics and Space Administration and the Center for Space Research (CSR),
 * The University of Texas at Austin. All rights reserved.
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

import jat.coreNOSA.algorithm.integrators.LinePrinter;
import jat.coreNOSA.algorithm.integrators.RungeKuttaFehlberg78;
import jat.coreNOSA.cm.TwoBody;

import java.io.IOException;

/**
 * <P>
 * The SimpleIntegrator Class provides an example of how the RungeKutta8 class can be used to
 * integrate a set of ODEs and plot the solution.
 *
 * @author 
 * @version 1.0
 */

public class RK78F_Test
{

    /** Runs the example.
     * @param args Arguments.
     */
    public static void main(String[] args) throws IOException
    {

        System.out.println("Create twobody orbit data");
        // create a Two Body orbit (elliptical)
        TwoBody orbit = new TwoBody(10000.0, 0.2, 0.0, 0.0, 0.0, 0.0);

        // create a Line Printer, print to screen
         LinePrinter lp = new LinePrinter();

        // create an RungeKuttaFehlberg78 integrator
        RungeKuttaFehlberg78 rk78f = new RungeKuttaFehlberg78();
        rk78f.setAccuracy(1.e-9);

        // initialize the state variables
        double [] x0 = orbit.randv();

        // set the final time to one orbit period
        double tf = orbit.period();

        // set the initial time to zero
        double t0 = 0.0;

        // integrate the equations
        double [] xf = rk78f.integrate(t0, x0, tf, orbit, lp, true);

        // close the Line Printer
        lp.close();

    }

}
