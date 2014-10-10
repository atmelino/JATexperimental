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
 * File Created on May 19, 2003
 */
 
package jat.coreNOSA.gps;
import java.io.Serializable;


/**
* The GPS_Measurement.java Class represents a single GPS measurement.
*
* @author 
* @version 1.0
*/
public class GPS_Measurement implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4762790985636489712L;

	/** Epoch time of measurement in sim time (seconds) */
	private double t;
	
	/** Epoch time of measurement in MJD */
	private double t_mjd;
	
	/** C/A Code Range to SV in meters */
	private double coderange;
	
	/** Carrier Phase Range to SV in meters */
	private double phaserange;	
	
	/** SVID */
	public int svid;
	
	/** Constructor
	 * @param tsim Time of the measurement in sim time (sec).
	 * @param tmjd Time of the measurement in MJD
	 * @param r range to GPS SV in meters
	 * @param cpr carrier phase range to GPS SV in meters.
	 * @param sv SVID
	 */
	public GPS_Measurement(double tsim, double tmjd, double r, double cpr, int sv) {
		this.t = tsim;
		this.t_mjd = tmjd;
		this.coderange = r;
		this.phaserange = cpr;
		this.svid = sv;
	}
	
	/** Override toString()
	 * @return GPS measurement data on a single line
	 */
	public String toString() {
		String out = t+"\t"+t_mjd+"\t"+coderange+"\t"+phaserange+"\t"+svid;
		return out;
	}
	
	/**
	 * Return the measurement time
	 * @return the measurement time in seconds (sim time)
	 */
	public double t(){
		return this.t;
	}
	
	/**
	 * Return the measurement time
	 * @return the measurement time in MJD
	 */
	public double t_mjd(){
		return this.t_mjd;
	}
	
	/**
	 * Return the pseudorange measurement
	 * @return double containing the pseudorange measurement
	 */
	public double range() {
		return this.coderange;
	}
	
	/**
	 * Return the carrier phase range measurement
	 * @return double containing the carrier phase range measurement
	 */
	public double phaseRange(){
		return this.phaserange;
	}
	
	/**
	 * Return the SV index
	 * @return int containing the SV index
	 */
	public int svid() {
		return this.svid;
	}

}
