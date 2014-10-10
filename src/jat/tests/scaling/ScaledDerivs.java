/* JAT: Java Astrodynamics Toolkit
 *
 * Copyright (c) 2003 National Aeronautics and Space Administration. All rights reserved.
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

import jat.coreNOSA.algorithm.VectorTimeFunction;
import jat.coreNOSA.algorithm.integrators.Derivatives;
import jat.coreNOSA.algorithm.integrators.Printable;

/**
 * Equations of motion
 *
 * @author Tobias Berthold
 */
public class ScaledDerivs implements Derivatives, Printable
{
	double scalefactor = 1.2;

	int n = 1;
	double T; // Thrust magnitude (parameter but always max when on)
	double g0 = 0.00981;
	VectorTimeFunction u; // thrust direction

	public ScaledDerivs()
	{
	}

	public ScaledDerivs(double scalefactor)
	{
		this.scalefactor=scalefactor;
	}

	public double[] derivs(double t, double[] x)
	{
		double[] out = new double[2];
		out[0] = x[1];
		out[1] = -x[0];

		out[0] = scalefactor * out[0];
		out[1] = scalefactor * out[1];
		return out;
	}

	public void print(double t, double[] y)
	{
		// print to the screen for warm fuzzy feeling
		//System.out.println(t + " " + y[0] + " " + y[1] );

	}

}
