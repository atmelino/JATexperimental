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

import jat.coreNOSA.algorithm.optimization.VarMetric;
import jat.tests.core.algorithm.optimization.functions.Rosenbrock;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class VarMetric_test
{
	static DecimalFormat df;
	static NumberFormat itf;

	public VarMetric_test()
	{
		df = (DecimalFormat)NumberFormat.getInstance();
		df.applyPattern(" ###.############;-###.###########");
		df.setMinimumFractionDigits(12);
		df.setMinimumIntegerDigits(4);
		itf = NumberFormat.getInstance();
		itf.setMinimumIntegerDigits(4);
	}

	static void print_line(double gamma, double total_it, boolean fail,double norm)
	{
		System.out.print(df.format(gamma) + " " + itf.format(total_it));
		System.out.println(" "+df.format(norm)+" " + fail);

	}

	public static void main(String argv[])
	{
		double[] x_init = new double[2];
		double[] x;
		double gamma[] = { 100., 80., 10, 1., 0., -.3,-.302302340040, -0.4, -1., -5., -10., -50., -80.,-100., -1000. };
		int i;

		System.out.println("Unconstrained Parameter Optimization Test");
		System.out.println("Rosenbrock, Numerical derivs, Variable Metric");
		System.out.println();

		// create instances of the classes
		VarMetric_test dt = new VarMetric_test();
		x_init[0] = -1.2;
		x_init[1] = 1.;
		VarMetric vm = new VarMetric(new Rosenbrock(), x_init);
		vm.err_ods = 1.e-6;
		vm.err_vm = 1.e-5;
		vm.eps_CD = 1.e-5;
		vm.max_it = 50;
		System.out.println("gamma              iter         |Gx|         fail");
		for (i = 0; i < gamma.length; i++)
		{
			vm.gamma = gamma[i];
			x = vm.find_min_VarMetric();
			print_line(gamma[i], vm.total_it, vm.ods_fail, vm.norm);
		}
	}
}

//Date start_time = new Date();
//Date stop_time = new Date();
//double etime = (stop_time.getTime() - start_time.getTime()) / 1000.;
//System.out.println("Elapsed Time = " + etime + " seconds");
