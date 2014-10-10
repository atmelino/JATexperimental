package jat.coreNOSA.gps;

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
 * File Created on Jul 13, 2003
 */
 
import jat.coreNOSA.math.Interpolator;
import jat.coreNOSA.math.MatrixVector.data.Matrix;
import jat.coreNOSA.math.MatrixVector.data.VectorN;
import jat.coreNOSA.measurements.generatePolarization;
import jat.coreNOSA.measurements.generateYaw;
import jat.coreNOSA.timeRef.RSW_Frame;
import jat.coreNOSA.util.FileUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;

/**
 * <P>
 * The Geo_Blockage_Model Class provides a model of GPS signal blockage due to 
 * a spherical earth as well as the ability to include signal strength
 * effects for sidelobe signals
 *
 * @author 
 * @version 1.0
 */

public class GEO_Blockage_Models_LM implements ExpandedVisible {
	
	private double elevationMask;
	private static final double earthRadius = 6478140.0;
	private static final double earthRadiusWithIono = 7478140.0;
	private double L1freq = 1575.42e6;
	private double C      = 2.99792458e8;
	
	double Ae = 0;  //Attenuation due to the atmosthpere
	double As = 0 ;  //To account for signal loss in front of LNA
	double PowerSV = 13.0;  //Average transmit power of block IIA sats
	double L  = -1.1;  //Receiver implimentation loss
	double Nf = -3.4;  //Noise figure for Receiver/LNA
	double Ts =  290.0;  //System Noise Temperature (K) earth pointing antenna
	
	
	double pointingBias = 0;
	//The receiver performance figures
	double AcquisitionThreshold = 30.0;// (dB-Hz)
	double TrackingThreshold    = 28.0;// (dB-Hz)
	static boolean [] AcquisitionFlag = new boolean[33];
	
	static double [] receiverAntennaDeg = new double[14];
	static double [] receiverAntennaGain = new double[14];
	static double [] GPSAntennaDeg = new double[66];
	static double [] GPSAntennaGain = new double[66];
	public static double Cn0;
	public static double trackedCn0;
	boolean firsttime = true;
	public static double[] elevation = new double[33];
	public static double[] azimuth   = new double[33];
	Interpolator interpR, interpG;
	double LMNoise;
	double thetaR, thetaGPS;

	
    /**
     * Determine if the GPS satellite is visible, including earth blockage
     * Used by GPS Measurement Generator.
     * @param losu Line of sight unit vector
     * @param r    current position vector
     * @param rGPS current position vector of GPS Satellite
     * @return boolean true if the GPS satellite is visible
     */
	public boolean visible(VectorN losu, VectorN r, VectorN rGPS, VectorN v, VectorN vGPS, int prn, double mjd) {

		//Load the antenna gain maps.  Note:  0 = receiver, 1 = GPS Sv
		//NOTE: I believe that both GPS satellite and the receiver antenna
		//are currently pointed at the center of the Earth.   (ie, zero degree
		//is pointed at the center of the Earth)
		if(firsttime == true)
		{
            String fs, dir_in;
            fs = FileUtil.file_separator();
            try{
                dir_in = FileUtil.getClassFilePath("jat.gps","GEO_Blockage_Models")+"antennas"+fs;
            }catch(Exception e){
                dir_in = "C:/Code/Jat/jat/eph/DE405data/";
            }	
//          
			//readFromFile("C:\\GOESR\\omni_antenna.txt",receiverAntennaDeg,receiverAntennaGain);
			//eadFromFile("C:\\GOESR\\ballhybrid_10db_60deg.txt",receiverAntennaDeg,receiverAntennaGain);
			//readFromFile(dir_in+"patch.txt",receiverAntennaDeg,receiverAntennaGain);
			//readFromFile(dir_in+"GPSIIA_L1MEAN.txt",GPSAntennaDeg,GPSAntennaGain);
			//readFromFile("C:\\GOESR\\LMantenna.txt",receiverAntennaDeg,receiverAntennaGain);
			firsttime = false;
			//interpR = new Interpolator(receiverAntennaDeg,receiverAntennaGain);
			//interpG = new Interpolator(GPSAntennaDeg,GPSAntennaGain);
			interpG = new Interpolator(dir_in + "BlockIIRTrans.txt");
			//interpR = new Interpolator(dir_in + "LM22deg.txt");
			//LMNoise = 204.6;
			//interpR = new Interpolator(dir_in + "LM17deg.txt");
			//LMNoise = 204.6;
			interpR = new Interpolator(dir_in + "IIRM.txt");
			generatePolarization polar = new generatePolarization(dir_in+"BlockIIRTransAxial.txt",dir_in+"IIRMAxial.txt");
			LMNoise = 204.2;
			L = -1.2;
			Nf = -1.4;
		}
		
		
		// check elevation mask
		boolean visible = true;
		
		// First check for Earth Obscuration
		double dist = r.mag();
		
		//  Cut off the ionosphere by adding 1000 Km to the 
		//  Mask.  This negates having to model/correct for
		//  the ionosphere
		
		
		//double cone = Math.atan2(earthRadius, dist);
		double cone = Math.atan2(earthRadiusWithIono, dist);
		
		VectorN r_unit = r.unitVector().times(-1.0);
		double cos_delta = r_unit.dotProduct(losu);
		double delta = Math.acos(cos_delta);
		if (delta < cone) {
			visible = false;
		}
		
		//Only continue if the satellite isn't behind the Earth
		if(visible){
	
			//Determine the Angle between the GPS SV boresight 
			//and the SV receiver
			VectorN unitrGPS = rGPS.unitVector();
			VectorN minusUnitRGPS = unitrGPS.times(-1.0);
			VectorN minusLosu = losu.times(-1.0);
			thetaGPS = (180/(Math.PI))*Math.acos(minusUnitRGPS.dotProduct(minusLosu));
			if(thetaGPS > 80 || thetaGPS < -80)
			{
				visible = false;
			}
			
			//Lookup the appropriate gain from the transmitting antenna
			//double Gt = interpG.get_value(thetaGPS);
			
			//Determine the current yaw angle for the GPS receiver
			double yaw = generateYaw.getYawAngle(mjd,rGPS,vGPS,prn);
			
			
			//Rotate the LOS vector (negative since it needs to be from
			//the receiver to the GPS satellite) into the GPS SV's RSW frame
			VectorN losRSW = generateYaw.T.times(minusLosu);
			VectorN GPSRSW = generateYaw.T.times(rGPS);
			VectorN unitGPSRSW = GPSRSW.unitVector();
			unitGPSRSW = unitGPSRSW.times(-1.0); //Needs to point to the center of the Earth
			
			//the antenna angles provided are 180 degrees off from the yaw angle
			yaw = yaw + 180;
			double transmitAzimuth = yaw + (180/Math.PI)*Math.atan2(losRSW.get(1),losRSW.get(2));
			if(transmitAzimuth < 0)
				transmitAzimuth = transmitAzimuth + 360.0;
			if(transmitAzimuth > 360)
				transmitAzimuth = transmitAzimuth - 360.0;
			
			double transmitElevation = (180/(Math.PI))*Math.acos(unitGPSRSW.dotProduct(losRSW));
			if(transmitElevation > 69)
				transmitElevation = 65;
			double Gt = interpG.get_value(transmitElevation,transmitAzimuth);
			

	
			//compute the polarization losses
			//double Gt = interpG.get_value(thetaGPS);
			
			
			
			//Determine the angle between the receiver boresight 
			//and the GPS SV	
			//Generate a RTN rotation matrix and rotate the LOS vector into it
			Matrix T = RSW_Frame.ECI2RIC(r,v);
			VectorN losRTN = T.times(losu);
			azimuth[prn] = ((180)/Math.PI)*Math.atan2(losRTN.get(0),losRTN.get(1)*-1);

			//Compute the Elevaton to the GPS Satellite
			elevation[prn]=Math.abs((180/Math.PI)*Math.asin(losRTN.get(2)/losRTN.mag()));
			
			VectorN unitR = r.unitVector();
			VectorN minusUnitR = unitR.times(-1.0);
			thetaR = (180/(Math.PI))*Math.acos(minusUnitR.dotProduct(losu)) + pointingBias;
			
			
			if(thetaR > 80 || thetaR < -80)
			{
				visible = false;
			}
			
			
			//Lookup the approprate gain from the receiving antenna			
			//double Gr = interpR.get_value(thetaR);
			double receiveAzimuth = (180/Math.PI)*Math.atan2(losRTN.get(1),losRTN.get(2));
			if(receiveAzimuth < 0)
				receiveAzimuth= receiveAzimuth+ 360;
			if(thetaR > 69)
				thetaR = 65;
			double Gr = interpR.get_value(thetaR,receiveAzimuth);
			
			
			//Determine Polarizaiton Losses
			double pol = 0.0;
			if(visible)
			{
				pol = generatePolarization.returnPolarization(prn, receiveAzimuth, thetaR,transmitAzimuth, transmitElevation, 10.0*(Math.PI/180),yaw);
			}
			//Determine the free space propagation loss
			VectorN los = GPS_Utils.lineOfSight(r, rGPS);
			double Ad = 20*Math.log10((C/L1freq) / (4*Math.PI*los.mag()));
			
			//Determine the attenuated power
			// Attenuated power = transmitted power + transmitted gain 
			//                  + free space loss + atmospheric disturbances
			double Ap = PowerSV + Gt + Ad + Ae;
			
			//Determine the received power
			// Received Power = Attenuated Power + Receiver Gain + 
			//  				Losses prior to LNA
			double Rp = Ap + Gr + As - pol;
			
			
			//Determine the Carrier to Noise Ratio
			//Carrier to Noise ratio = Received power - scaling of system noise
			//						 +  288.6 (not sure what that is) + receiver noise figure
			//						 +  Losses in receiver/LNA (front end?)
			//Cn0 = Rp - 10*Math.log10(Ts) + 228.6 + Nf + L;
			Cn0 = Rp + LMNoise;// + Nf + L ;

			if(Cn0 < TrackingThreshold)
				visible = false;
			//System.out.println("thetaR: " + thetaR + " ThetaGPS: " + thetaGPS + " C/N0: " + Cn0);
			//If the satellite isn't visible, reset the AcquisitionFlag
			//to force it to reacquire
			if(visible == false)
				AcquisitionFlag[prn] = false;
			
			
			//System.out.println("Cn0 for PRN : " + prn + " is: "+Cn0);
			//If it is visible and strong enough, "Acquire" satellite
			if(visible && AcquisitionFlag[prn] == false && Cn0 > AcquisitionThreshold)
				AcquisitionFlag[prn] = true;

			//If we have "acquired" a satellite and if the Cn0 is greater than
			//the tracking threshold, then it is visible
			if(AcquisitionFlag[prn])
			{
				if(Cn0 > TrackingThreshold)
				{
	
					visible = true;
					trackedCn0 = Cn0;
				}
			}
			else
			{
				visible = false;
				trackedCn0 = 0;
			}
		
		
		}
		if(visible != true)
			elevation[prn] = 0.0;
		if(visible == true)
		{
			FileWriter out;
			try {
				
				out = new FileWriter("C:\\GOESR\\tmp.txt",true);
				BufferedWriter writer = new BufferedWriter(out);
				String pp = mjd + " " + Cn0 + " " + thetaR + " " + thetaGPS + "\n"; 
		
						
				writer.write(pp);
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return visible;
		
	}
	public void readFromFile(String file, double [] antennaDeg, double [] antennaGain) {
		try {
			FileReader fr = new FileReader(file);
			BufferedReader in = new BufferedReader(fr);
			String line;
			int j = 0;
			// loop through the file, one line at a time
			while ((line = in.readLine()) != null) {
				StringTokenizer tok = new StringTokenizer(line, "\t");
				int total = tok.countTokens();
				
				// check for consistent number of columns
				if (total != 2) {
					System.out.println(
					"AntennaGainPattern Error: Number of columns do not match");
					System.exit(-99);
				}
				//antennaDeg[0] = 1.0;
				//Parse out the valuse
				String token = tok.nextToken();
				antennaDeg[j] = Double.parseDouble(token);
				token = tok.nextToken();
				antennaGain[j] = Double.parseDouble(token);
				
				//System.out.println("Degree: "+ antennaDeg[j] + "Gain: " + antennaGain[j]);
				j++;
			}	
			in.close();
			fr.close();
		} catch (IOException e) {
			System.err.println("Error opening:" + file);
			return;
		}
	}
	public boolean visible(VectorN losu, VectorN r, VectorN rISS) {
		// TODO Auto-generated method stub
		return false;
	}


	//public boolean visible(VectorN losu, VectorN r, VectorN rGPS, VectorN v, int prn) {
	//	// TODO Auto-generated method stub
	//	return false;
	//}

}
