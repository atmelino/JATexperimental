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

package jat.tests.core.algorithm.optimization.functions;

import jat.coreNOSA.algorithm.ScalarfromArrayFunction;

public class Function5 implements ScalarfromArrayFunction
{
	public double evaluate(double[] x)
	{
		//return ( (x[1]-10.)*(x[1]-10.)+(x[2]-10.)*(x[2]-10.)+1. );
		//return ( (x[1]-10.)*(x[1]-10.)+(x[2]-10.)*(x[2]-10.) );
		// Example 1
		return ((x[0] - 5.) * (x[0] - 5.) + (x[1] - 5.) * (x[1] - 5.));
	}
}
// Test cases:
/*		
x_guess[1] = 6.;
x_guess[2] = 6.;
d[1] = -1.;
d[2] = 1.;
x_guess[1] = 1.;
x_guess[2] = 1.;
d[1] = -1.;
d[2] = 1.;
x_guess[1] = 5.;
x_guess[2] = 5.;
d[1] = 1.;
d[2] = 1.;
*/		
