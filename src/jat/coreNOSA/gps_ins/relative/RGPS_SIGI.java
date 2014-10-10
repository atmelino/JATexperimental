package jat.coreNOSA.gps_ins.relative;

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
import jat.coreNOSA.ins.INS_MeasurementList;

/**
* The RGPS_SIGI.java Class runs the RGPS/SIGI EKF.
* @author 
* @version 1.0
*/
public class RGPS_SIGI {

	/**
	 * main - runs the ekf.
	 * @params args none.
	 */
	public static void main(String[] args) {
		String dir = "C:\\Jat\\jat\\input\\";
		String gpsmeasfile = "gps\\rgpsmeas_vbar_geom4_mp.jat";
//		String gpsmeasfile = "gpsmeasblk.txt";
		String insmeasfile = "ins\\sigi_vbar.jat";
		String rinexfile = "gps\\rinex.n";
//		RGPS_MeasurementList gps = new RGPS_MeasurementList();
//		gps.readFromFile(dir+gpsmeasfile);
		RGPS_MeasurementList gps = RGPS_MeasurementList.recover(dir+gpsmeasfile);
		INS_MeasurementList ins = INS_MeasurementList.recover(dir+insmeasfile);
//		int big = ins.size();
//		System.out.println("ins size = "+big);
		GPS_Constellation constellation = new GPS_Constellation(dir+rinexfile);
		LinePrinter lp1 = new LinePrinter("C:\\Jat\\jat\\output\\ekf_sigi_vbar_geom4_new_th_1.txt");
		LinePrinter lp2 = new LinePrinter("C:\\Jat\\jat\\output\\ekf_sigi_vbar_geom4_new_th_2.txt");
		LinePrinter lp3 = new LinePrinter("C:\\Jat\\jat\\output\\ekf_sigi_vbar_geom4_th_resid.txt");
		ProcessModel process = new RGPS_SIGI_ProcessModel(ins, constellation, lp1, lp2, lp3);
		MeasurementModel meas = new RGPS_INS_MeasurementModel(gps, constellation);
		ExtendedKalmanFilter ekf = new ExtendedKalmanFilter(meas, gps, process);
		long ts = System.currentTimeMillis();

		ekf.process();
		
		long tf = System.currentTimeMillis();
		double dtf = (tf - ts)/(60.0 * 1000.0);
		System.out.println("EKF Time Elapsed: "+dtf);
		

	}
}
