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

package jat.tests.core.algorithm.optimization;

import jat.coreNOSA.algorithm.optimization.GradientSearch;
import jat.tests.core.algorithm.optimization.functions.Function4;
import jat.tests.core.algorithm.optimization.functions.QuadraticFunction;
import jat.tests.core.algorithm.optimization.functions.Rosenbrock;

public class GradientSearch_test
{

	public static void main(String argv[])
	{
		double[] x; // result
		double[] x_init = new double[2]; // initial guess
		double[] x_init3 = new double[3]; // initial guess

		//System.out.println("Unconstrained Parameter Optimization Test");

		// Quadratic Function
		System.out.println("Quadratic Function");
		x_init[0] = 1.;
		x_init[1] = 1.;
		GradientSearch g22 = new GradientSearch(new QuadraticFunction(), x_init);
		g22.err_ods = 1.e-4;
		g22.err_grad = 1.e-3;
		g22.eps_FD = 1.e-8;
		x = g22.find_min_gradient();
		System.out.println();

		// Rosenbrock function
		System.out.println("Rosenbrock function");
		x_init[0] = -1.2;
		x_init[1] = 1.;
		GradientSearch g23 = new GradientSearch(new Rosenbrock(), x_init);
		g23.err_ods = 1.e-4;
		g23.err_grad = 1.e-3;
		g23.eps_FD = 1.e-8;
		g23.max_it = 20;
		x = g23.find_min_gradient();
		System.out.println();

		// n=3 function
		System.out.println("Function of 3 vars");
		x_init3[0] = -1.2;
		x_init3[1] = 1.;
		x_init3[2] = 1.;
		GradientSearch g3 = new GradientSearch(new Function4(), x_init3);
		g3.err_ods = 1.e-4;
		g3.err_grad = 1.e-3;
		g3.eps_FD = 1.e-8;
		g3.max_it = 20;
		x = g3.find_min_gradient();
	}
}

//Date start_time = new Date();
//Date stop_time = new Date();
//double etime = (stop_time.getTime() - start_time.getTime()) / 1000.;
//System.out.println("Elapsed Time = " + etime + " seconds");
