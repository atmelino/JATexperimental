package jat.examplesNOSA.OrbitDetermination;

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
import jat.core.util.PathUtil;
import jat.coreNOSA.algorithm.estimators.ExtendedKalmanFilter;
import jat.coreNOSA.algorithm.estimators.ProcessModel;
import jat.coreNOSA.algorithm.integrators.LinePrinter;

import javax.swing.JApplet;

/**
 * The ODdemo.java Class is a demonstration of Orbit Determination. It processes
 * range data in the file OBSDATA2 with an observation model defined by the
 * ObsData class and dynamics model defined by J2DragProcss using an Extended
 * Kalman Filter.
 * 
 * @author
 * @version 1.0 
 * 2012: added PathUtil, JApplet
 */
public class ODdemo extends JApplet {

	private static final long serialVersionUID = -8949808323540904434L;

	public ODdemo() {
		PathUtil p = new PathUtil(this);
		ObsData obs = new ObsData(p.current_path);
		LinePrinter lp = new LinePrinter();
		LinePrinter lp2 = new LinePrinter("C:\\Jat\\output\\resid.txt");
		ProcessModel process = new J2DragProcess(lp, lp2);
		ExtendedKalmanFilter ekf = new ExtendedKalmanFilter(obs, obs, process);
		System.out.println("Processing..");
		ekf.process();
		System.out.println("Processing completed");

	}

	/**
	 * main - runs the demo.
	 * 
	 * @params args none.
	 */
	public static void main(String[] args) {

		//ODdemo O = new ODdemo();

	}
}
