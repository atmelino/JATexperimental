/*
 * Created on Jun 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package jat.coreNOSA.timeRef;

import jat.coreNOSA.cm.TwoBody;
import jat.coreNOSA.math.MathUtils;
import jat.coreNOSA.math.MatrixVector.data.Matrix;
import jat.coreNOSA.math.MatrixVector.data.VectorN;


public class EarthRefTest {

	public static void main(String[] args) {
		double mu = 3986004.418E08;
		double a = 7136635.001;
		double e = 0.00349497;
		double i = 90.01109;
		double raan = 0.8383513;
		double w = 260.51023;
		double ta = 0.18307E-05;
		TwoBody leo = new TwoBody(mu, a, e, i, raan, w, ta);
		CalDate epoch = new CalDate(1994, 10, 4, 0, 0, 0.0);
		EarthRef ref = new EarthRef(epoch);
		
		double mjd = epoch.mjd();
		System.out.println("mjd = "+mjd);
		
		
		VectorN r = leo.getR();
		VectorN v = leo.getV();
		r.print("ECI");
		
		Matrix eci2ecef = ref.ECI2ECEF();
		VectorN recef = eci2ecef.times(r);
		recef.print("ECEF");
//		double x = -0.073424*MathUtils.ARCSEC2RAD;
//		double y = 0.206629*MathUtils.ARCSEC2RAD;
//		ref.setIERS(x, y, 0.6155998);
		double x = -0.070976*MathUtils.ARCSEC2RAD;
		double y = 0.204837*MathUtils.ARCSEC2RAD;
		ref.setIERS(0.0, 0.0, 0.6084018);
		
		
		VectorN rmod = ref.PrecMatrix().times(r);
		rmod.print("MOD");
		
		VectorN rtod = ref.NutMatrix().times(rmod);
		rtod.print("TOD");
		
		VectorN ritrf = (ref.PoleMatrix().times(ref.GHAMatrix())).times(rtod);
		ritrf.print("ITRF");

		eci2ecef = ref.ECI2ECEF();
		recef = eci2ecef.times(r);
		recef.print("ECEF");
		
		
		CalDate epoch2 = new CalDate(1999, 3, 4, 0, 00, 0.0);
		EarthRef ref2 = new EarthRef(epoch2);
		ref2.setIERS(0.06740*MathUtils.ARCSEC2RAD, 0.24713*MathUtils.ARCSEC2RAD, 0.649232);
		ref2.PrecMatrix().print("P");
		ref2.NutMatrix().print("N");
		ref2.GHAMatrix().print("GHA");
		ref2.PoleMatrix().print("Pole");
		ref2.eci2ecef().print("eci2ecef");
		ref2.ECI2ECEF().print("ECI2ECEF");
		

	}
}
