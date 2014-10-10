package jat.tests.core.algorithm.integrators;

import jat.coreNOSA.algorithm.integrators.Derivatives;
import jat.coreNOSA.algorithm.integrators.DormandPrince;
import jat.coreNOSA.math.MatrixVector.data.VectorN;

public class DPTest1 implements Derivatives
{

	public double[] derivs(double t, double[] x)
	{
		double[] out = new double[6];
		out[3] = -x[0];
		out[4] = -x[1];
		out[5] = -x[2];
		return out;
	}
	
	
	public static void main(String[] args)
	{
		System.out.println("Dormand Prince integrator test");
		DormandPrince dp = new DormandPrince();
		
		DPTest1 dpt1=new DPTest1();
		// double[] r1 = new double[4];
		// double[] r2 = new double[4];
		// double[] r1prime = new double[4];
		// double[] r2prime = new double[4];
		// r1prime[1] = 10.;
		// r1prime[2] = 0.;
		// double time1 = 0.;
		// double time2 = 2*Math.PI/12;
		// dp.update(r1, r1prime, time1, r2, r2prime, time2);
		// new VectorN(r2).print("r2");
		// new VectorN(r2prime).print("r2prime");
		// double[] r1 = new double[3];
		// double[] r2 = new double[3];
		// double[] r1prime = new double[3];
		// double[] r2prime = new double[3];
		// r1prime[0] = 10.;
		// r1prime[1] = 1.;
		// r1prime[2] = 1.;
		// double time1 = 0.;
		// double time2 = 2*Math.PI/12;
		// dp.integrate(r1, r1prime, time1, r2, r2prime, time2);
		// new VectorN(r2).print("r2");
		// new VectorN(r2prime).print("r2prime");
		double[] state1 = new double[6];
		state1[3] = 10.;
		state1[4] = 1.;
		state1[5] = 2.;
		double time1 = 0.;
		double time2 = 0 * Math.PI / 12;
		double[] state2 = dp.integrate(time1, state1, time2, dpt1);
		new VectorN(state2).print("state2");	}
}
