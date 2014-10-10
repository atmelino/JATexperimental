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
import jat.coreNOSA.math.MathUtils;
import jat.coreNOSA.math.MatrixVector.data.Matrix;
import jat.coreNOSA.math.MatrixVector.data.RotationMatrix;
import jat.coreNOSA.math.MatrixVector.data.VectorN;

public class GroundStation {
	
	private String _STDN = null;
	
	private String _location = null;
	
	private String _NASA = null;
	
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

    // WGS-84
    private double f = 1.0/298.257223563;	
	
    /** create a Ground Station from latitude, longitude, HAE using WGS-84 Geoid.
     * @param stdn String containing the STDN code.
     * @param loc String containing the location.
     * @param nasa String containing the NASA number.
     * @param lat  double containing latitude in radians.
     * @param lon  double containing longitude in radians.
     * @param alt  double containing height above ellipsoid in meters.
     */
	public GroundStation(String stdn, String loc, String nasa, double lat, double lon, double alt){
		_STDN = stdn;
		_location = loc;
		_NASA = nasa;
		_lat = lat;
		_lon = lon;
		_alt = alt;
	}
	
    /** create a Ground Station from an ECEF Position Vector.
     * @param stdn String containing the STDN code.
     * @param loc String containing the location.
     * @param nasa String containing the NASA number.
     * @param r Geocentric position in m.
     */
    public GroundStation(String stdn, String loc, String nasa, VectorN r){
    	_STDN = stdn;
    	_location = loc;
		_NASA = nasa;
    	
        double  eps = 1000.0 * MathUtils.MACHEPS;   // Convergence criterion
        //double  eps = MathUtils.MACHEPS;   // Convergence criterion
        double  epsRequ = eps*R_equ;
        double  e2      = f*(2.0-f);        // Square of eccentricity

        double  X = r.x[0];                   // Cartesian coordinates
        double  Y = r.x[1];
        double  Z = r.x[2];
        double  rho2 = X*X + Y*Y;           // Square of distance from z-axis

        // Iteration
        double  SinPhi;
        double  ZdZ = 0.0;
        double Nh = 0.0;
        double N = 0.0;
        double dZ = e2*Z;
        double dZ_new = 0.0;

        if (r.mag() > 0.0){

            while (Math.abs(dZ-dZ_new) > epsRequ ) {
                ZdZ    =  Z + dZ;
                Nh     =  Math.sqrt ( rho2 + ZdZ*ZdZ );
                SinPhi =  ZdZ / Nh;                    // Sine of geodetic latitude
                N      =  R_equ / Math.sqrt(1.0-e2*SinPhi*SinPhi);
                dZ = dZ_new;
                dZ_new =  N*e2*SinPhi;
            }

            // Longitude, latitude, altitude
            _lon = Math.atan2 ( Y, X );
            _lat = Math.atan2 ( ZdZ, Math.sqrt(rho2) );
            _alt = Nh - N;
        }
        else{
            _lon = -9999.9;
            _lat = -9999.9;
            _alt = -9999.9;
        }
    }

    /** Return the STDN code.
     * @return STDN code.
     */
    public String getSTDN(){
        return _STDN;
    }    
    /** Return the Ground Station Location.
     * @return location.
     */
    public String getLocation(){
        return _location;
    }    
    /** Return the NASA number.
     * @return NASA number.
     */
    public String getNASA(){
        return _NASA;
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
    
    /** computes the ECI velocity vector.
     * @param eci2ecef transformation matrix between ECI and ECEF
     * @return ECI position in m.
     */    
    public VectorN getECIVelocity(Matrix eci2ecef) {
    	Matrix ecef2eci = eci2ecef.transpose();
    	
    	VectorN recef = this.getECEFPosition();
    	VectorN vecef = this.getECEFVelocity();
    	
    	double w = Constants.omega_e;
    	
    	Matrix wCross = new Matrix(3,3);
    	wCross.set(0, 1,-w);
    	wCross.set(1, 0, w);
    	
    	VectorN x1 = wCross.times( ecef2eci.times(recef) );
    	VectorN x2 = ecef2eci.times(vecef);
    	
    	VectorN out =  x1.plus(x2);  // this gives same answer as rotransf
    	
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

}
