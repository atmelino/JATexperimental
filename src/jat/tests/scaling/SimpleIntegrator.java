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

package jat.tests.scaling;

import jat.coreNOSA.algorithm.integrators.RungeKutta8;

public class SimpleIntegrator
{

	public static void main(String[] args)
	{

		RungeKutta8 rk8 = new RungeKutta8(0.0001);

		// initialize the variables
		double[] x0 = new double[2];
		x0[0] = 1.0;
		x0[1] = 0.0;

		// initial and final time
		double t0 = 0.0;
		double tf = Math.PI*1.4;//10.0;
	
		ScaledDerivs D=new ScaledDerivs(1.0);
		double[] xD=rk8.integrate(t0, x0, tf, D, D, true);
		
		double scalefactor=tf;
		ScaledDerivs SD=new ScaledDerivs(scalefactor);
		
		double[] xSD=rk8.integrate(t0, x0, tf/scalefactor, SD, SD, true);
		System.out.println("Final values");
		System.out.println("Unscaled "+xD[0]+" "+xD[1]);
		System.out.println("  Scaled "+xSD[0]+" "+xSD[1]);

	}
}
