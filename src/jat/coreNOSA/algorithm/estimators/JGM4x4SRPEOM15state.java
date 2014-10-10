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
 * 
 * File Created on May 8, 2003
 */
package jat.coreNOSA.algorithm.estimators;

import jat.coreNOSA.algorithm.integrators.Derivatives;
import jat.coreNOSA.cm.Constants;
import jat.coreNOSA.ephemeris.DE405;
import jat.coreNOSA.forces.GravityModel;
import jat.coreNOSA.forces.GravityModelType;
import jat.coreNOSA.forces.SolarRadiationPressure;
import jat.coreNOSA.gps.GPS_Utils;
import jat.coreNOSA.math.MatrixVector.data.Matrix;
import jat.coreNOSA.math.MatrixVector.data.VectorN;
import jat.coreNOSA.simulation.initializer;
import jat.coreNOSA.spacetime.EarthRef;
import jat.coreNOSA.spacetime.Time;
import jat.coreNOSA.spacetime.TimeUtils;
import jat.coreNOSA.spacetime.UniverseModel;

import java.util.HashMap;




/**
* This file has to be reconstructed for each state used.  It uses a 
* 4 x 4 gravity model as well as lunar and solar gravity and Solar
* radiation pressure modelds
* @author 
* @version 1.0
*/ 



public class JGM4x4SRPEOM15state implements Derivatives {

	private static double re = 6378136.3; // radius of earth in meters
	//private static double h_0 = 920000.0; // atmosphere model parameter
	//private static double rho_0 = 4.36E-14; // atmosphere model parameter
	//private static double gamma_0 = 5.381E-06; // atmosphere model parameter
	//private static double omega_e = 7.2921157746E-05; // earth rotation rate
	public static int n;
	public static HashMap hm;
	public static double mass0,mass1,area0,area1;
	public static double Cr0,Cr1;
	public DE405 jpl_ephem;
	public static double mjd0;
	double j2 = Constants.j2;
	double mu = Constants.mu*1e9;
	boolean firsttime;
	UniverseModel universe;


	GravityModel earth_grav;
	
	SolarRadiationPressure srp0; 
	SolarRadiationPressure srp1 ;
	public double hc;
	
	
	public JGM4x4SRPEOM15state(HashMap hm){
		this.hm = hm;
		//String fs, dir_in;
		//fs = FileUtil.file_separator();
//		try{
//			dir_in = FileUtil.getClassFilePath("jat.sim","SimModel")+"input"+fs;
//		}catch(Exception e){
//			dir_in = "";
//		}
		//hm = initializer.parse_file(dir_in+"initialConditions.txt");
		mass0 = initializer.parseDouble(hm,"jat.0.mass");
		area0 = initializer.parseDouble(hm,"jat.0.area");
		Cr0 = initializer.parseDouble(hm,"jat.0.Cr");
		mass1 = initializer.parseDouble(hm,"jat.1.mass");
		area1 = initializer.parseDouble(hm,"jat.1.area");
		Cr1 = initializer.parseDouble(hm,"jat.1.Cr");
		mjd0 = initializer.parseDouble(hm,"init.MJD0")+initializer.parseDouble(hm, "init.T0")/86400.0;

		
		hc = -2.87956633585E-10 * GPS_Utils.c;
		
		earth_grav = new GravityModel(4,4,GravityModelType.JGM3);
		
		srp0 = new SolarRadiationPressure(mass0, area0, Cr0);
		srp1 = new SolarRadiationPressure(mass1, area1, Cr1);
		
		//Set the Gravitational parameter path		
		jpl_ephem = new DE405();
		

		universe = new UniverseModel(mjd0);
		n = initializer.parseInt(hm,"FILTER.states");
	}
	
	
	/**
	 * Compute the time derivatives
	 * @param t time
	 * @param y double [] containing current state.
	 * @return double[] containing the time derivatives.
	 */
	public double[] derivs(double t, double[] y) {
		
		//The out vector is sized by the number of states squared 
		//plus the number of states.  (n^2 + n)
		
		
		double out[] = new double[n*n + n];
		
		//Obtain thet the correct time
		//int ctr = 0;
		Time tt = new Time(t/86400 + mjd0);
		double newttt = TimeUtils.UTCtoTT(t/86400 + mjd0);
		
		
		if(firsttime == false)
		{
			earth_grav.initialize();
			firsttime = true;
			universe.set_use_iers(true);
			srp0.updateArea(area0);
			srp1.updateArea(area1);
			srp0.updateMass(mass0);
			srp1.updateMass(mass1);
			
		}
		
		// Generate some vectors for use later on
		VectorN r0 = new VectorN(y[0], y[1], y[2]);
		VectorN v0 = new VectorN(y[3], y[4], y[5]);
		VectorN r1 = new VectorN(y[6], y[7], y[8]);
		VectorN v1 = new VectorN(y[9], y[10], y[11]);
		
		// store elements of incoming state in more familiar looking variables
		double xx0 = y[0];
		double yy0 = y[1];
		double zz0 = y[2];
		double vx0 = y[3];
		double vy0 = y[4];
		double vz0 = y[5];
		double xx1 = y[6];
		double yy1 = y[7];
		double zz1 = y[8];
		double vx1 = y[9];
		double vy1 = y[10];
		double vz1 = y[11];
		
		
		// compute derived variables
		double rmag0 = r0.mag();
		double rmag1 = r1.mag();
		
		double rcubed0 = rmag0 * rmag0 * rmag0;
		double rcubed1 = rmag1 * rmag1 * rmag1;
		
		double rsq0 = rmag0 * rmag0;
		double rsq1 = rmag1 * rmag1;
		
		double re_r0 = re / rmag0;
		double re_r1 = re / rmag1;
		
		re_r0 = re_r0 * re_r0;
		re_r1 = re_r1 * re_r1;
		
		
		double zsq_rsq0 = (5.0 * zz0 * zz0 / rsq0) - 1.0;
		double zsq_rsq1 = (5.0 * zz1 * zz1 / rsq1) - 1.0;
		
		//Get the acceleration directly from the model
		tt.update(t);
		universe.update(t);
		Matrix M = universe.earthRef.eci2ecef(universe.time.mjd_ut1(),universe.time.mjd_tt());
		VectorN acc0 = earth_grav.gravity(r0,M);
		VectorN acc1 = earth_grav.gravity(r1,M);
		
		//Use the acceleration model directly for integration
		//of the state.  For the generation of the state transition
		//Matirx, use the normal J2 forulation
		double ax0 = acc0.get(0);
		double ay0 = acc0.get(1);
		double az0 = acc0.get(2);
		
		double ax1 = acc1.get(0);
		double ay1 = acc1.get(1);
		double az1 = acc1.get(2);
		
		
		// compute accelerations due to gravity for each satellite
		double ll0 = -1.0 * (mu * xx0 / rcubed0) * (1.0 - 1.5 * re_r0 * j2 * zsq_rsq0);
		double mm0 = -1.0 * (mu * yy0 / rcubed0) * (1.0 - 1.5 * re_r0 * j2 * zsq_rsq0);
		double nn0 = -1.0 * (mu * zz0 / rcubed0) * (1.0 - 1.5 * re_r0 * j2 * (zsq_rsq0 - 2.0));
		
		double ll1 = -1.0 * (mu * xx1 / rcubed1) * (1.0 - 1.5 * re_r1 * j2 * zsq_rsq1);
		double mm1 = -1.0 * (mu * yy1 / rcubed1) * (1.0 - 1.5 * re_r1 * j2 * zsq_rsq1);
		double nn1 = -1.0 * (mu * zz1 / rcubed1) * (1.0 - 1.5 * re_r1 * j2 * (zsq_rsq1 - 2.0));

		// compute accelerations due to SRP
		double AU_sqrd = Constants.AU*Constants.AU;
				
		//compute acceleration due to lunar gravity
		double ttt = TimeUtils.TTtoTDB(newttt) + 2400000.5;
        VectorN r_moon = universe.earthRef.moonVector(newttt);
        
        VectorN d0 = r0.minus(r_moon);
        VectorN d1 = r1.minus(r_moon);
        
        double dmag0 = d0.mag();
        double dmag1 = d1.mag();
        
        double dcubed0 = dmag0 * dmag0 * dmag0;
        double dcubed1 = dmag1 * dmag1 * dmag1;
        
        VectorN temp0 = d0.divide(dcubed0);
        VectorN temp1 = d1.divide(dcubed1);
        
        double smag = r_moon.mag();
        double scubed = smag * smag * smag;
        
        VectorN temp2 = r_moon.divide(scubed);
        VectorN sum0 = temp0.plus(temp2);
        VectorN sum1 = temp1.plus(temp2);
        VectorN lunarAcceleration0 = sum0.times(Constants.GM_Moon);
        VectorN lunarAcceleration1 = sum1.times(Constants.GM_Moon);


        //Compute the acceleration due to the solar gravity
        VectorN r_sun = EarthRef.sunVector(newttt);
        d0 = r0.minus(r_sun);
        d1 = r1.minus(r_sun);
        
        dmag0 = d0.mag();
        dmag1 = d1.mag();
        
        dcubed0 = dmag0 * dmag0 *dmag0;
        dcubed1 = dmag1 * dmag1 *dmag1;
        
        temp0 = d0.divide(dcubed0);
        temp1 = d1.divide(dcubed1);
        
        smag = r_sun.mag();
        double magSun3 = smag * smag * smag;
        
        temp2 = r_sun.divide(magSun3);
        
        sum0 = temp0.plus(temp2);
        sum1 = temp1.plus(temp2);
        VectorN solarAcceleration0 = sum0.times(Constants.GM_Sun);
        VectorN solarAcceleration1 = sum1.times(Constants.GM_Sun);
        
        
        //Determine if the sun is visible
        double visible0 = srp0.partial_illumination(r0,r_sun);
		double visible1 = srp1.partial_illumination(r1,r_sun);
        
		double pressureConstant = 4.5344321837439e-06;
		double SRPscale0 = pressureConstant*(area0/mass0)*visible0;
        double SRPscale1 = pressureConstant*(area1/mass1)*visible1;
		
        //Compute the relevant SRP information
		double Xsun = r_sun.get(0);
		double Ysun = r_sun.get(1);
		double Zsun = r_sun.get(2);
		//double aa = 1.0 *Cr*(Xsun/magSun3)*AU_sqrd*visible;
		//double bb = 1.0 *Cr*(Ysun/magSun3)*AU_sqrd*visible;
		//double cc = 1.0 *Cr*(Zsun/magSun3)*AU_sqrd*visible;
        
		VectorN srpacc0 = srp0.accelSRP(r0,r_sun);
		VectorN srpacc1 = srp1.accelSRP(r1,r_sun);
		srpacc0 = srpacc0.times(visible0);
        srpacc1 = srpacc1.times(visible1);
		
		// compute state derivatives

        //Velocity for spacecraft 0
		out[0] = y[3];
		out[1] = y[4];
		out[2] = y[5];
		
		//acceleration for spacecraft 0
		out[3] = ax0 + srpacc0.get(0) - lunarAcceleration0.get(0) - solarAcceleration0.get(0);
		out[4] = ay0 + srpacc0.get(1) - lunarAcceleration0.get(1) - solarAcceleration0.get(1);
		out[5] = az0 + srpacc0.get(2) - lunarAcceleration0.get(2) - solarAcceleration0.get(2);
	
		//Velocity for spacecraft1
		out[6] = y[9];
		out[7] = y[10];
		out[8] = y[11];
		

		//acceleration for spacecraft 1
		out[9] =  ax1 + srpacc1.get(0) - lunarAcceleration1.get(0) - solarAcceleration1.get(0);
		out[10] = ay1 + srpacc1.get(1) - lunarAcceleration1.get(1) - solarAcceleration1.get(1);
		out[11] = az1 + srpacc1.get(2) - lunarAcceleration1.get(2) - solarAcceleration1.get(2);
		
		
		//Solar radiation Pressure states
		out[12] = 0;
		out[13] = 0;
		
		
		double w_f = (Math.random()-0.5)*2*.036;
		out[14] = w_f + hc;

		// compute A matrix

		Matrix a = new Matrix(15,15); // creates a (15x15 matrix with all zeros

	
		//Generate some utility variables.  It is a bit messy as we will need
		//two now.
		
		double r50 = rsq0 * rcubed0;
		double r51 = rsq1 * rcubed1;
		
		double mur50 = mu / r50;
		double mur51 = mu / r51;
		
		//double mur30 = mu / rcubed0;
		//double mur31 = mu / rcubed1;
		
		
		double sz2r20 = 7.0 * zz0 * zz0 / rsq0;
		double sz2r21 = 7.0 * zz1 * zz1 / rsq1;

		double muxyr50 = mu * xx0 * yy0 / r50;
		double muxyr51 = mu * xx1 * yy1 / r51;
		
		double muxzr50 = mu * xx0 * zz0 / r50;
		double muxzr51 = mu * xx1 * zz1 / r51;
		
		double muyzr50 = mu * yy0 * zz0 / r50;
		double muyzr51 = mu * yy1 * zz1 / r51;

		double bracket10 = 3.0 - 7.5 * re_r0 * j2 * (sz2r20 - 1.0);
		double bracket11 = 3.0 - 7.5 * re_r1 * j2 * (sz2r21 - 1.0);
		
		double bracket30 = 3.0 - 7.5 * re_r0 * j2 * (sz2r20 - 3.0);
		double bracket31 = 3.0 - 7.5 * re_r1 * j2 * (sz2r21 - 3.0);
		
		double bracket50 = 3.0 - 7.5 * re_r0 * j2 * (sz2r20 - 5.0);
		double bracket51 = 3.0 - 7.5 * re_r1 * j2 * (sz2r21 - 5.0);
		
		//double bracket20 = 1.5 * re_r0 * (5.0 * zz0 * zz0 / rsq0 - 1.0);
		//double bracket21 = 1.5 * re_r1 * (5.0 * zz1 * zz1 / rsq1 - 1.0);
		
		//Note:  use this formulation for ll to avoid a singularity
		ll0 = -1.0 * (mu  / rcubed0) * (1.0 - 1.5 * re_r0 * j2 * zsq_rsq0);
		ll1 = -1.0 * (mu  / rcubed1) * (1.0 - 1.5 * re_r1 * j2 * zsq_rsq1);
		
		double dldx0 = ll0 + mur50 * xx0 * xx0 * bracket10;
		double dldx1 = ll1 + mur51 * xx1 * xx1 * bracket11;
		
		double dldy0 = muxyr50 * bracket10;
		double dldy1 = muxyr51 * bracket11;
		
		double dldz0 = muxzr50 * bracket30;
		double dldz1 = muxzr51 * bracket31;
		
		//double dldj20 = mur30 * xx0 * bracket20;
		//double dldj21 = mur31 * xx1 * bracket21;
		
		double dmdx0 = dldy0;
		double dmdx1 = dldy1;
		
		
		//Note:  use a different definition of mm to avoid singularity issues
		
		mm0 = -1.0 * (mu / rcubed0) * (1.0 - 1.5 * re_r0 * j2 * zsq_rsq0);
		mm1 = -1.0 * (mu / rcubed1) * (1.0 - 1.5 * re_r1 * j2 * zsq_rsq1);
		
		double dmdy0 = mm0  + mur50 * yy0 * yy0 * bracket10;
		double dmdy1 = mm1  + mur51 * yy1 * yy1 * bracket11;
		
		double dmdz0 = muyzr50 * bracket30;
		double dmdz1 = muyzr51 * bracket31;
		
		//double dmdj20 = mur30 * yy0 * bracket20;
		//double dmdj21 = mur31 * yy1 * bracket21;
		
		double dndx0 = muxzr50 * bracket30;
		double dndx1 = muxzr51 * bracket31;
		
		double dndy0 = dmdz0;
		double dndy1 = dmdz1;
		
		//Note:  Use this definition of nn0.  It is uglier, but it removes a potential singularity
		nn0 = -1.0 * (mu / rcubed0) * (1.0 - 1.5 * re_r0 * j2 * (zsq_rsq0 - 2.0));
		nn1 = -1.0 * (mu / rcubed1) * (1.0 - 1.5 * re_r1 * j2 * (zsq_rsq1 - 2.0));
		
		//double dndz0 = nn0 / zz0 + mur50 * zz0 * zz0 * bracket50;
		double dndz0 = nn0 + mur50 * zz0 * zz0 * bracket50;
		double dndz1 = nn1 + mur51 * zz1 * zz1 * bracket51;
		
		
		//double dndj20 = mur30 * zz0 * (1.5 * re_r0 * (5.0 * zz0 * zz0 / rsq0 - 3.0));
		//double dndj21 = mur31 * zz1 * (1.5 * re_r1 * (5.0 * zz1 * zz1 / rsq1 - 3.0));

		a.A[0][3] = 1.0;
		a.A[1][4] = 1.0;
		a.A[2][5] = 1.0;
		
		a.A[3][0] = dldx0 ;
		a.A[3][1] = dldy0 ;
		a.A[3][2] = dldz0 ;
		a.A[3][3] = 0;
		a.A[3][4] = 0;
		a.A[3][5] = 0;
		a.A[3][12] = 1.0 *(Xsun/magSun3)*AU_sqrd*SRPscale0;

		
		a.A[4][0] = dmdx0;
		a.A[4][1] = dmdy0;
		a.A[4][2] = dmdz0;
		a.A[4][3] = 0;
		a.A[4][4] = 0;
		a.A[4][5] = 0;
		a.A[4][12] = 1.0 *(Ysun/magSun3)*AU_sqrd*SRPscale0;

		a.A[5][0] = dndx0;
		a.A[5][1] = dndy0;
		a.A[5][2] = dndz0;
		a.A[5][3] = 0;
		a.A[5][4] = 0;
		a.A[5][5] = 0;
		a.A[5][12] = 1.0 *(Zsun/magSun3)*AU_sqrd*SRPscale0;
		

		//For the second spacecraft
		a.A[6][9]  = 1.0;
		a.A[7][10] = 1.0;
		a.A[8][11] = 1.0;

		
		a.A[9][6]  = dldx1 ;
		a.A[9][7]  = dldy1 ;
		a.A[9][8]  = dldz1 ;
		a.A[9][9]  = 0;
		a.A[9][10] = 0;
		a.A[9][11] = 0;
		a.A[9][13] = 1.0 *(Xsun/magSun3)*AU_sqrd*SRPscale1;

		
		a.A[10][6]  = dmdx1;
		a.A[10][7]  = dmdy1;
		a.A[10][8]  = dmdz1;
		a.A[10][9]  = 0;
		a.A[10][10] = 0;
		a.A[10][11] = 0;
		a.A[10][13] = 1.0 *(Ysun/magSun3)*AU_sqrd*SRPscale1;

		a.A[11][6] = dndx1;
		a.A[11][7] = dndy1;
		a.A[11][8] = dndz1;
		a.A[11][9] = 0;
		a.A[11][10] = 0;
		a.A[11][11] = 0;
		a.A[11][13] = 1.0 *(Zsun/magSun3)*AU_sqrd*SRPscale1;
		
		
		// compute phi derivatives

		Matrix phi = this.phi(y);

		Matrix dphi = a.times(phi);

		//	   dphi.print("dphi");

		// put phi derivatives into output array
		int k = n;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				out[k] = dphi.A[i][j];
				k = k + 1;
			}
		}

		return out;
	}

	private Matrix phi(double[] in) {
		Matrix out = new Matrix(n, n);
		int k = n;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				out.A[i][j] = in[k];
				k = k + 1;
			}
		}
		return out;
	}

}
