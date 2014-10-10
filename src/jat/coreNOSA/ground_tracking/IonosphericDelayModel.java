package jat.coreNOSA.ground_tracking;
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

import jat.coreNOSA.groundstations.GroundStation;
import jat.coreNOSA.groundstations.SimpleGroundStation;
import jat.coreNOSA.math.MatrixVector.data.VectorN;
import jat.coreNOSA.spacetime.Time;

import java.io.FileOutputStream;
import java.io.PrintStream;


public class IonosphericDelayModel {
	
	//Mean height of the ionosphere in meters
	private double h = 350e3; 
	
	//Ionospheric Delay (meters)
	private double ionoDelay;
	
	//Nighttime ionospheric behavior coefficient
	private double N = 3.1928e18;
	
	//Daytime ionospheric behavior coefficient
	private double D = 3.1928e19;
	
	//Slant angle to the satellite
	private double slantAngle;
	
	//Period of Diurnal Variation (hours)
	private double Pbar = 32;  
	
	//Phase of Diurnal Variation (hours)
	private double thetaD = 14;
	
	//Frequency of signal
	private double freq = 1.6e9;
	
		
    /** create an ionospheric model from the ECEF station position, ECEF
     * satellite position, and the current Time. A default frequency of 1.6Ghz is used.
     * @param staPos VectorN containing the ground station ECEF coordinates.
     * @param satPos VectorN containing the satellites ECEF coordinats.
     * @param T	Time containing the current time
     */
	public IonosphericDelayModel(VectorN staPos, VectorN satPos, Time T)
	{
		
		//Create a Ground Station 
		SimpleGroundStation station = new SimpleGroundStation("tmp",staPos);
		
		double az = station.getAzimuth(satPos);
		double el = station.getElevation(satPos);
		double lat = station.getLatitude();
		double lon = station.getLongitude();
		
		//Compute the Delay
		double tLoc = computeLocalTime (T,lat,lon,az,el);
		computeDelay(tLoc);
			
	}
	
    /** create an ionospheric model from the geodetic latitude, longitude and height
     * the current satellite posion and the time.  A default frequency of 1.6Ghz is used.
     * @param lat double geodetic latitude
     * @param lon double geodetic longitude
     * @param height double geodetic height
     * @param satPos VectorN containing the satellites ECEF coordinats.
     * @param T	Time containing the current time
     */
	public IonosphericDelayModel(double lat, double lon, double height,
			VectorN satPos,Time T)
	{
		
		SimpleGroundStation station = new SimpleGroundStation("tmp",lat,lon,height);
		
		double az = station.getAzimuth(satPos);
		double el = station.getElevation(satPos);
		
		//Compute the delay
		double tLoc = computeLocalTime (T,lat,lon,az,el);
		computeDelay(tLoc);
		
	}
	 /** create an ionospheric model from the a current GroundStation,
     * the current satellite posion and the time. A default frequency of 1.6Ghz is used.
     * @param station GroundStation geodetic latitude
     * @param satPos VectorN containing the satellites ECEF coordinats.
     * @param T	Time containing the current time
     */
	public IonosphericDelayModel(GroundStation station,
			VectorN satPos,Time T)
	{
		
		double az = station.getAzimuth(satPos);
		double el = station.getElevation(satPos);
		double lat = station.getLatitude();
		double lon = station.getLongitude();
		
		//Compute the Delay
		double tLoc = computeLocalTime (T,lat,lon,az,el);
		computeDelay(tLoc);
		
	}
	 /** Compute the Local time of the sub ionospheric point following the
	  * algorithm in Burkhart's dissertation
     * @param T time current time
     * @param staLat double geodetic latitude of the ground station
     * @param staLon double geodetic longitude of the ground station
     * @param az double azimuth from the ground station to the satellite
     * @param el double elevation from the ground station to the satellite
     * @return tLocal double local time of sub ionospheric point
     */
	private double computeLocalTime(Time T,double staLat, double staLon,
					double az, double el)
	{
		
		
		//Determine Slant Height
		double re = jat.coreNOSA.constants.WGS84.R_Earth;
		this.slantAngle = Math.asin(re/(re+this.h)*Math.cos(el));
		
		
		//Determine the latitude of the sub-ionospheric point
		double ionoLat = Math.asin( Math.sin(staLat)*Math.sin(el+this.slantAngle) +
				Math.cos(staLat)*Math.cos(az)*Math.cos(el+this.slantAngle));
		
		//Determine the longitude of the sub-ionospheric point	
		double ionoLong = staLon + Math.asin( Math.sin(az)*Math.cos(el+this.slantAngle)/Math.cos(ionoLat) );
		
		//Determine the local time
		double UTCHours = T.secOfDay()/3600;  //Number of hours into current day
		
		double tLocal = ionoLong/15 + UTCHours;
		
		return tLocal;
		
	}
	 /** Compute the ionospheric delay between the satellite and the ground station
     * @param tLocal double local time of the subionospheric point
     */
	private void computeDelay(double tLocal)
	{
		
		double chi = (2*Math.PI/this.Pbar)*(tLocal-this.thetaD);
		
		if(chi > (Math.PI)/2)
		{
			chi = Math.PI/2;
		}
			
		
		this.ionoDelay = (1/Math.cos(this.slantAngle))*(N+D*Math.cos(chi))/(this.freq*this.freq);
	}
	
	/**Return the Slant Angle
	 * @return slantHeight
	 */
	public double returnSlantAngle(){
		return this.slantAngle;
	}
	
	/**Return the ionospheric delay
	 * @return ionosphericDelay
	 */
	public double returnDelay()
	{
		return this.ionoDelay;
	}
	
	/**Set the mean height of the ionosphere
	 * @param _h double new mean ionospheric height 
	 */
	public void set_h(double _h)
	{
		this.h = _h;
	}
	
	/**Set the Nighttime ionospheric behavior coefficient
	 * @param _N double new nighttime ionospheric behavior coefficient 
	 */
	public void set_N(double _N)
	{
		this.N = _N;
	}

	/**Set the Daytime ionospheric behavior coefficient
	 * @param _D double new Daytime ionospheric behavior coefficient 
	 */
	public void set_D(double _D)
	{
		this.D = _D;
	}

	/**Set the Period of the dinural variation
	 * @param _Pbar double new period of the dinural variation 
	 */
	public void set_Pbar(double _Pbar)
	{
		this.Pbar = _Pbar;
	}

	/**Set the Phase of the dinural variation
	 * @param _thetaD double new phase of the dinural variation 
	 */
	public void set_thetaD(double _thetaD)
	{
		this.thetaD = _thetaD;
	}
	
	/**Set the frequency of the signal
	 * @param double freq frequency of the signal
	 */
	public void set_freq(double _freq)
	{
		this.freq = _freq;
	}

	public static void main(String[] args) {
		
		double x,y,z;
		VectorN state;

		//Set the Ground Station Parameters
		double lat = 0;
		double lon = 0;
		double height = 10;

		//Set some variables for file output
		FileOutputStream out; 
		PrintStream p; 


		try
		{
			out = new FileOutputStream("iono.txt");
			p = new PrintStream( out );

			for(double deg = -90; deg<90; deg=deg+.01)
			{
				
				
				//Set the spacecraft's state 
				x = jat.coreNOSA.constants.WGS84.R_Earth*Math.cos(deg*Math.PI/180);
				y = jat.coreNOSA.constants.WGS84.R_Earth*Math.sin(deg*Math.PI/180);
				z = 0;
				state = new VectorN(x,y,z);

				//Set the time, assuming a 90 minute orbit
				Time T = new Time(54103.0 + 1.7361e-006); 

				//Create the Ionospheric Delay Model
				IonosphericDelayModel iono = new IonosphericDelayModel(lat,lon,height,state,T);

				double delay = iono.returnDelay()/(1.6e9*1.6e9);
				
				p.println (x + " " + y + " " + z + " " + delay );

			}
			p.close();
		}
		
		catch (Exception e)
		{
			System.err.println ("Error writing to file");
		}

	}


}
