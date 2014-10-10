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

package jat.tests.core.algorithm.solver;

import jat.coreNOSA.algorithm.solver.GaussElim;
import jat.coreNOSA.math.MatrixVector.data.Matrix;
import jat.coreNOSA.math.MatrixVector.data.VectorN;

class GaussElimTest
{
	public static void run_example(double[][] A_data, double[] y_data)
	{
		System.out.println("Gaussian Elimination Test");
		Matrix A = new Matrix(A_data);
		VectorN y = new VectorN(y_data);
		A.print("A");
		y.print("y");
		VectorN X = GaussElim.solve(A, y);
		GaussElim.C.print("C after solve in main");
		X.print("Solution x:");
		A.times(X).print("Test: Ax=");
	}
	
	public static void main(String argv[])
	{
		double[][] A1_data = { { 1, -1, 2, -1 }, {
				2, -2, 3, -3 }, {
				1, 1, 1, 0 }, {
				1, -1, 4, 3 }
		};
		double[] y1_data = { -8, -20, -2, 4 };
		run_example(A1_data,y1_data);

		double[][] A2_data={{1,0,1},{0,3,0},{1,0,2}};
		double[] y2_data={1,2,3};
		run_example(A2_data,y2_data);
	}
}
