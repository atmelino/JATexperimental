package jat.coreNOSA.gps.filters;

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
 * File Created on Aug 31, 2003
 */
 
/**
* The DragProcessModel.java Class provides a process model for the 
* coefficient of drag state in the GPS-only filters
* 
* @author 
* @version 1.0
*/ 
public class DragProcessModel {

	public final static double correlationTime = 180000.0;
	
	public final static double sigma = 0.5;
	
	/**
	 * compute the derivative of the drag state
	 * @param in previous value of the drag state
	 * @return the time derivative of the drag state
	 */
	public static double dragProcess(double in){
		double out = -1.0 * in/correlationTime;
		return out;
	}
	
//	public static double dragQ(double dt){
//		double sig2 = sigma*sigma;
//		double exp = Math.exp(-2.0*dt/correlationTime);
//		double out = sig2 * (1.0 - exp);
//		return out;
//	}
	
	public static double Q(){
		double q = 2.0 * sigma*sigma / correlationTime;
		return q;
	}
	

}
