package jat.tests.core.algorithm.integrators;

import jat.core.util.PrintMatrix;
import jat.coreNOSA.algorithm.integrators.Derivatives;
import jat.coreNOSA.algorithm.integrators.RKF78_StoreSteps;
import jat.coreNOSA.math.MatrixVector.data.Matrix;
import jat.coreNOSA.math.MatrixVector.data.VectorN;

/**
 * Test the Runge Kutta integrator by comparing it's output of a harmonic oscillator with the sine and cosine functions
 * @author Tobias Berthold
 */
public class RKF78_StoreSteps_Test1 implements Derivatives
{
	public double[] derivs(double t, double[] x)
	{
		double[] out = new double[2];
		out[0] = x[1];
		out[1] = -x[0];
		return out;
	}

	public static void main(String[] args)
	{
		double time1 = 0.;
		double time2 = 24 * Math.PI / 12;
		double h1 = 0.01;
		double hmin = 0.0;
		double[] state2;
		RKF78_StoreSteps_Test1 rt = new RKF78_StoreSteps_Test1();
		RKF78_StoreSteps r = new RKF78_StoreSteps();
		//
		r.eps = 1.e-4;
		double[] state1 = new double[2];
		state1[0] = 0.;
		state1[1] = 1.;
		// Integrate
		state2 = r.integrate(time1, state1, time2, rt);
		new VectorN(state2).print("state");
		Matrix M = new Matrix(r.steplist);
		PrintMatrix PM = new PrintMatrix(M);
		String Titles[] =
		{ "t", "x", "x'", "retried steps" };
		PM.titles = Titles;
		PM.setMinFracDig(8);
		PM.print();
		System.out.println("good " + r.nok + " bad " + r.nbad + " steps");
		// N.toFile("outrk45.txt");
		// again to verify that the same integrator can be reused
		state2 = r.integrate(time1, state1, time2, rt);
		new VectorN(state2).print("state");
		Matrix N = new Matrix(r.steplist);
		PrintMatrix PN = new PrintMatrix(N);
		PN.setMinFracDig(8);
		PN.print();
		System.out.println("good " + r.nok + " bad " + r.nbad + " steps");
	}
}
