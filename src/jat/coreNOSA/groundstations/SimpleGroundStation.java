/* JAT: Java Astrodynamics Toolkit
 *
 * Copyright (c) 2007 National Aeronautics and Space Administration. All rights reserved.
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
 * File Created on May 20, 2007
 */
package jat.coreNOSA.groundstations;

import jat.coreNOSA.cm.Constants;
import jat.coreNOSA.constants.IERS_1996;
import jat.coreNOSA.math.MathUtils;
import jat.coreNOSA.math.MatrixVector.data.Matrix;
import jat.coreNOSA.math.MatrixVector.data.RotationMatrix;
import jat.coreNOSA.math.MatrixVector.data.VectorN;
import jat.coreNOSA.spacetime.ITRF;
import jat.coreNOSA.spacetime.Time;

public class SimpleGroundStation {
	
	private static final double c = IERS_1996.c;
	
	private String _name = null;
	
    /** latitude in radians.
     */
	private double _lat = -9999.9;
	
    /** longitude in radians.
     */
	private double _lon = -9999.9;
	
    /** height above the ellipsoid in m.
     */	
	private double _alt = -9999.9;
	
	// WGS-84
    private double R_equ = 6378.137e3;
    private static double R_static = 6378.137e3;

    // WGS-84
    private double f = 1.0/298.257223563;	
	private static double f_static = 1.0/298.257223563;
    
    /** create a Ground Station from latitude, longitude, HAE using WGS-84 Geoid.
     * @param name String containing the name.
     * @param lat  double containing latitude in radians.
     * @param lon  double containing longitude in radians.
     * @param alt  double containing height above ellipsoid in meters.
     */
	public SimpleGroundStation(String name, double lat, double lon, double alt){
		_name = name;
		_lat = lat;
		_lon = lon;
		_alt = alt;
	}
	
	   /** create a Ground Station from ECEF Position Vector.
     * @param name String containing the name.
     * @param staPos VectorN containing ECEF position
     */
	public SimpleGroundStation(String name, VectorN staPos){
		_name = name;
		
		//Set the Latitude, Longitude and Height
		ECEF2LLH(staPos);
		
	}
	
	   /** ODToolbox interface 
	    * create a Ground Station from ECEF Position Vector.
     * @param name String containing the name.
     * @param staPos VectorN containing ECEF position
     */
	public SimpleGroundStation(String name, double[] staPos){
		_name = name;
		
		//Set the Latitude, Longitude and Height
		ECEF2LLH(new VectorN(staPos));
		
	}
	
 
    /** Return the name.
     * @return name.
     */
    public String getName(){
        return _name;
    }        
    /** Return the latitude.
     * @return latitude in radians.
     */
    public double getLatitude(){
        return _lat;
    }
    /** return the longitude.
     * @return longitude in radians.
     */
    public double getLongitude(){
        return _lon;
    }
    /** return the height above the ellipsoid.
     * @return height above the ellipsoid in m.
     */
    public double getHAE(){
        return _alt;
    }


    /** computes the ECEF position vector.
     * @return ECEF position in m.
     */
    public VectorN getECEFPosition () {
        double  e2     = f*(2.0-f);        // Square of eccentricity
        double  CosLat = Math.cos(_lat);         // (Co)sine of geodetic latitude
        double  SinLat = Math.sin(_lat);
        double  CosLon = Math.cos(_lon);         // (Co)sine of geodetic latitude
        double  SinLon = Math.sin(_lon);
        double  N;
        VectorN  r = new VectorN(3);

        // Position vector

        N = this.R_equ / Math.sqrt(1.0-e2*SinLat*SinLat);
        double Nh = N + _alt;
        r.x[0] =  Nh*CosLat*CosLon;
        r.x[1] =  Nh*CosLat*SinLon;
        r.x[2] =  ((1.0-e2)*Nh)*SinLat;
        return r;
    }
    /** computes the ECEF velocity vector.
     * @return ECEF velocity in m/s.
     */
    public VectorN getECEFVelocity () { 
    	VectorN  r = this.getECEFPosition();
        VectorN  w = new VectorN(0,0,Constants.omega_e);
       	VectorN  v = w.crossProduct(r);
        return v;
    }
    /** computes the ECI position vector.
     * @param eci2ecef transformation matrix between ECI and ECEF
     * @return ECI position in m.
     */    
    public VectorN getECIPosition(Matrix eci2ecef) {
    	VectorN ecef = this.getECEFPosition();
    	VectorN out = eci2ecef.transpose().times(ecef);
    	return out;
    }
    
    /** computes the ECI position vector.
     * @param eci2ecef transformation matrix between ECI and ECEF
     * @return ECI position in m.
     */    
    public VectorN getECIPosition(ITRF ref) {
    	VectorN ecef = this.getECEFPosition();
    	VectorN out = ref.ECI2ECEF().times(ecef);
    	return out;
    }
    /** Compute the transformation from ECEF to SEZ (topocentric horizon) reference frame.
     * @return ECEF to SEZ transformation matrix
     */
    public Matrix ECEF2SEZ (){
        double lambda = _lon;
        double phi = _lat;

        RotationMatrix M = new RotationMatrix( 3, lambda, 2, (Constants.pi/2.0 - phi));
        Matrix out = new Matrix(M);
        return  out;
    }
    
    /**
     * Computes the elevation angle in radians of a spacecraft relative to a groundstation
     * @param satPos VectorN containing the ECEF ground station to spacecraft vector
     * @return elevation angle in radians (-pi/2 to +pi/2)
     */
    public double getElevation(VectorN satPos)
    {
    	Matrix T = ECEF2SEZ();
    	VectorN Rho = T.times(satPos);
    	double el = elevation(Rho);
    	return el;
    }
    
    /** OD Toolbox Interface
     * Computes the elevation angle in radians of a spacecraft relative to a groundstation
     * @param satPos VectorN containing the ECEF ground station to spacecraft vector
     * @return elevation angle in radians (-pi/2 to +pi/2)
     */
    public double getElevation(double[] satPos)
    {
    	return getElevation(new VectorN(satPos));
    }
    
    
	/**
	 * Returns the azimuth angle in radians of a spacecraft relative to a ground station
	 * @param rho VectorN containing the ground station to spacecraft vector in ECEF frame 
	 * @return elevation angle in radians (0 to 2pi)
	 */
    public double getAzimuth(VectorN satPos)
    {
    	Matrix T = ECEF2SEZ();
    	VectorN Rho = T.times(satPos);
    	double ez = azimuth(Rho);
    	return ez;
    }
    
	/**
	 * Returns the elevation angle in radians of a spacecraft relative to a ground station
	 * @param rho VectorN containing the ground station to spacecraft vector in SEZ frame 
	 * @return elevation angle in radians (-pi/2 to +pi/2)
	 */
	public static double elevation(VectorN rho){
		return Math.asin(rho.get(2)/rho.mag());
	}

	/**
	 * Returns the azimuth angle in radians of a spacecraft relative to a ground station
	 * @param rho VectorN containing the ground station to spacecraft vector in SEZ frame 
	 * @return elevation angle in radians (0 to 2pi)
	 */
	public static double azimuth(VectorN rho){
		double x = rho.get(0);
		double y = rho.get(1);
		double rxy = Math.sqrt(x*x + y*y);
		return MathUtils.arcsin(x, rxy);
	}
 
	/**
	 * Determine if a spacecraft is visible (above a given min elevation)
	 * @param rho VectorN containing the ground station to spacecraft vector in ENU frame 
	 * @param minElevation minimum elevation for visibility in radians
	 * @return boolean, true if elevation >= minElevation, otherwise false
	 */
	public static boolean isVisible(VectorN rho, double minElevation){
		boolean out = false;
		if (elevation(rho) >= minElevation) out = true;
		return out;
	}

	/**
	 * Compute the Geodetic Latitude, Longitude and Height usint the algorithm
	 * from Montebruck's book "Satellite Oribts".
	 * @param rho VectorN containing the ground station to spacecraft vector in ENU frame 
	 * @param minElevation minimum elevation for visibility in radians
	 * @return boolean, true if elevation >= minElevation, otherwise false
	 */
	public void ECEF2LLH(VectorN staPos)
	{
		double theta,newRange;
		double N = 0.0;
		
		double x = staPos.get(0);
		double y = staPos.get(1);
		double z = staPos.get(2);
		
		//Set the initial conditions for the iteration
		double e = Math.sqrt(1-(1-this.f)*(1-this.f));
		double deltaZ = 1000; //Set high to force into loop
		double deltaZNew = e*e*z;
		
		//Iterate until the error is small
		while( Math.abs(deltaZNew - deltaZ) > 1e-16)
		{
			deltaZ = deltaZNew;
			
			newRange = Math.sqrt(x*x + y*y + (z+deltaZ)*(z+deltaZ));
			theta = Math.asin(( z + deltaZ)/newRange);
			
			N = this.R_equ/Math.sqrt(1-e*e*Math.sin(theta)*Math.sin(theta));
			
			deltaZNew = N*e*e*Math.sin(theta);
			
		}

		//Compute the Latitude, Longitude and Heigth
		this._lon = Math.atan2(y, x);
		this._lat = Math.atan2( (z+deltaZNew),Math.sqrt(x*x + y*y));
		this._alt = Math.sqrt(x*x + y*y + (z+deltaZ)*(z+deltaZ)) - N;
		
	}

	/**
	 * Compute the Geodetic Latitude, Longitude and Height usint the algorithm
	 * from Montebruck's book "Satellite Oribts".
	 * @param rho double containing the ground station to spacecraft vector in ENU frame 
	 * @return double - [lat;lon;alt]
	 */
	public static double[] ECEF2LLH(double[] rho)
	{
		double theta,newRange;
		double N = 0.0;
		
		double x = rho[0];
		double y = rho[1];
		double z = rho[2];
		
		double[] out = new double[3];
		
		//Set the initial conditions for the iteration
		double e = Math.sqrt(1-(1-f_static)*(1-f_static));
		double deltaZ = 1000; //Set high to force into loop
		double deltaZNew = e*e*z;
		
		//Iterate until the error is small
		while( Math.abs(deltaZNew - deltaZ) > 1e-16)
		{
			deltaZ = deltaZNew;
			
			newRange = Math.sqrt(x*x + y*y + (z+deltaZ)*(z+deltaZ));
			theta = Math.asin(( z + deltaZ)/newRange);
			
			N = R_static/Math.sqrt(1-e*e*Math.sin(theta)*Math.sin(theta));
			
			deltaZNew = N*e*e*Math.sin(theta);
			
		}

		//Compute the Latitude, Longitude and Heigth
		out[0] = Math.atan2(y, x);
		out[1] = Math.atan2( (z+deltaZNew),Math.sqrt(x*x + y*y));
		out[2] = Math.sqrt(x*x + y*y + (z+deltaZ)*(z+deltaZ)) - N;
		
		return out;
	}
	

	
	/**
	 * Returns inertial satellite position at time of signal transmission, r(t-tau)
	 * computed via Taylor series expansion (to avoid interpolation)
	 * @param tau double containing signal travel time (s)
	 * @param r VectorN containing satellite inertial position at signal reception time (t)
	 * @param v VectorN containing satellite inertial velocity at signal reception time (t)
	 * @param a VectorN containing satellite inertial acceleration at signal reception time (t)
	 * @return VectorN containing satellite inertial position at time of signal transmission, r(t-tau)
	 */
	public static VectorN r_trans_time(double tau, VectorN r, VectorN v, VectorN a){
		VectorN term1 = v.times(-1.0 * tau);
		VectorN term2 = a.times(0.5 * tau * tau);
		VectorN dr = term1.plus(term2);
		return r.plus(dr);
	}
	

	
	
	public double two_way_range(Time t, VectorN r, VectorN v, VectorN a){
		int maxit = 500;
		double eps = 1.0E-16;
		
		// downlink light time iteration
		int i = 0;
		double tau_d_new = 0.0;
		double tau_d_old = t.mjd_tt();
		double rho_down = 0.0;
		double diff = Math.abs(tau_d_new - tau_d_old);
		VectorN r_trans = new VectorN(3);
		while ((diff > eps)&&(i < maxit)) {
			r_trans = r_trans_time(tau_d_old, r, v, a);
			ITRF itrf = new ITRF(t, 0.0, 0.0, 0.0);
			VectorN R = this.getECIPosition(itrf);
			VectorN los = r_trans.minus(R);
			double rho = los.mag();
			tau_d_new = rho/c;
			diff = Math.abs(tau_d_new - tau_d_old);
			tau_d_old = tau_d_new;
			rho_down = rho;			
			i = i + 1;
			if (i >= maxit){
				System.out.println("transmitTime too many iterations, diff = "+ diff);
				if (diff > 1.0E-10) {
					throw new RuntimeException("GPS_Utils.transmitTime too many iterations at t_mjd = "+t.mjd_tt()+", diff = "+ diff);
				}
			}
		}
		Time tt = new Time(t.mjd_utc()+tau_d_new/86400.0);
		
		//uplink time iteration
		i = 0;
		double tau_u_new = 0.0;
		double tau_u_old = t.mjd_tt();
		double rho_up = 0.0;
		diff = Math.abs(tau_u_new - tau_u_old);
		while ((diff > eps)&&(i < maxit)) {
			Time ttt = tt.plus(-1.0*tau_u_old);  
			ITRF itrf = new ITRF(ttt, 0.0, 0.0, 0.0);  
			VectorN R = this.getECIPosition(itrf);
			VectorN los = r_trans.minus(R);
			double rho = los.mag();
			tau_u_new = rho/c;
			diff = Math.abs(tau_u_new - tau_u_old);
			tau_u_old = tau_u_new;
			rho_up = rho;			
			i = i + 1;
			if (i >= maxit){
				System.out.println("transmitTime too many iterations, diff = "+ diff);
				if (diff > 1.0E-10) {
					throw new RuntimeException("GPS_Utils.transmitTime too many iterations at t_mjd = "+t.mjd_tt()+", diff = "+ diff);
				}
			}
		}
		double range = 0.5*(rho_up + rho_down);
		return range;
	}

	public static void main(String[] args) {
		
		//Create a Ground station using latitude longitude and height
		double staLat = 55*Math.PI/180;
		double staLon = -120*Math.PI/180;
		double staHeight = 25;
		SimpleGroundStation sta1 = new SimpleGroundStation("tmp1",staLat,staLon,staHeight);
		
		//Get the ECEF coordinates
		VectorN sta1Pos = sta1.getECEFPosition();
		
		
		//Convert it back to make sure we get the same answer
		//VectorN sta1Pos = new VectorN(1917032.190, 6029782.349, -801376.113);
		SimpleGroundStation station = new SimpleGroundStation("tmp",sta1Pos);
		double lat = station.getLatitude();
		double lon = station.getLongitude();
		double height = station.getHAE();
		System.out.println("Latitude: " + lat*180/Math.PI);
		System.out.println("Longitude: " + lon*180/Math.PI);
		System.out.println("Height: " + height);
		
		
		
	}
	
}
