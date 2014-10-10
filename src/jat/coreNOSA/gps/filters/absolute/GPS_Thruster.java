package jat.coreNOSA.gps.filters.absolute;

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
 * File Created on May 9, 2003
 */
import jat.coreNOSA.algorithm.estimators.ExtendedKalmanFilter;
import jat.coreNOSA.algorithm.estimators.MeasurementModel;
import jat.coreNOSA.algorithm.estimators.ProcessModel;
import jat.coreNOSA.algorithm.integrators.LinePrinter;
import jat.coreNOSA.gps.GPS_Constellation;
import jat.coreNOSA.gps.RGPS_MeasurementList;

/**
* The GPS_Thruster.java Class runs the GPS-only EKF with thruster model.
* 
* @author 
* @version 1.0
*/
public class GPS_Thruster {

	/**
	 * main - runs the demo.
	 * @params args none.
	 */
	public static void main(String[] args) {
		String dir = "C:\\Jat\\jat\\input\\";
		String gpsmeasfile = "gps\\gpsmeas_rbar_geom1_mp.jat";
		String rinexfile = "gps\\rinex.n";
		String burnfile = "burns\\rbar_burns.jat";
		RGPS_MeasurementList gps = RGPS_MeasurementList.recover(dir+gpsmeasfile);
		GPS_Constellation constellation = new GPS_Constellation(dir+rinexfile);
		LinePrinter lp1 = new LinePrinter("C:\\Jat\\jat\\output\\ekf_gps_thr_rbar_geom1_mp_1.txt");
		LinePrinter lp2 = new LinePrinter("C:\\Jat\\jat\\output\\ekf_gps_thr_rbar_geom1_mp_resid.txt");
		ProcessModel process = new GPS_Thruster_ProcessModel(constellation, lp1, lp2, dir+burnfile);
		MeasurementModel meas = new GPS_MeasurementModel(gps, constellation);
		ExtendedKalmanFilter ekf = new ExtendedKalmanFilter(meas, gps, process);
		long ts = System.currentTimeMillis();

		ekf.process();
		
		long tf = System.currentTimeMillis();
		double dtf = (tf - ts)/(60.0 * 1000.0);
		System.out.println("EKF Time Elapsed: "+dtf);
		

	}
}
