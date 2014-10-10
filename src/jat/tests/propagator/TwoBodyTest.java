/*
 * Created on May 10, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package jat.tests.propagator;

import jat.coreNOSA.algorithm.integrators.LinePrinter;
import jat.coreNOSA.algorithm.integrators.RungeKutta8;
import jat.coreNOSA.cm.TwoBody;

/**
 * @author
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TwoBodyTest {

	public static void main(String[] args) {
		TwoBody tb = new TwoBody(398600.4415);
		RungeKutta8 rk8 = new RungeKutta8(5.0);
		LinePrinter lp = new LinePrinter("");
		lp.setThinning(600.0);
        // initialize the variables
        double [] x0 = new double[6];
        					
        x0[0] = 6740.83104800;
        x0[1] = -6.23521800;
        x0[2] = -462.64447700;
        x0[3] = 0.41275600;
        x0[4] = 4.78381400;
        x0[5] = 5.99847500;

        // set the final time
        double tf = 345600.0;

        // set the initial time to zero
        double t0 = 0.0;

        // integrate the equations
        rk8.integrate(t0, x0, tf, tb, lp, true);
		
	}
}
