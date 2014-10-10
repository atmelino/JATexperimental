package jat.coreNOSA.measurements;


import jat.coreNOSA.algorithm.integrators.LinePrinter;
import jat.coreNOSA.math.MatrixVector.data.Matrix;
import jat.coreNOSA.math.MatrixVector.data.VectorN;
import jat.coreNOSA.spacetime.TimeUtils;
import jat.coreNOSA.timeRef.RSW_Frame;


public class generateYaw{
	
	public static double yawAngle;
	public static double alpha;
	public static double beta;
	public static Matrix T;
	static public  LinePrinter yawResults;
	static boolean firstTime = true;

	/**
	 * Internal function to return the Transformation matrix
	 * to rotate the statellite into a frame useful for determining
	 * the yaw angle for a GPS satellite.  This frame is an RTN frame
	 * with the N defined in the oppsite direction (negative orbit normal)
	 * @param r  Position of the GPS satellite
	 * @param v  Velocity of the GPS satellite
	 * @return
	 */
	
	public static Matrix YawRotationMatrix(VectorN r, VectorN v)
	{
		//Note:  Make sure the position and velocity have
		//       been rotated into the ECI frame 
		Matrix T = new Matrix(3);
		T = RSW_Frame.ECI2SWR(r,v); 
		return T;
		
	}

	/**
	 * Return the unit sun vector
	 * @param MJD_TT
	 * @return
	 */
	public static VectorN unitSun(double MJD_TT,VectorN r)
	{
		
		VectorN SunV = new VectorN(jat.coreNOSA.spacetime.EarthRef.sunVector(MJD_TT));
		VectorN deltasun = SunV.minus(r);
		SunV = deltasun.unitVector();
		return SunV;
	}
	
	/**
	 * Generate the appropirate yaw angle of a GPS satellite to 
	 * keep it in a favorable allignement for solar pannel pointing
	 * @param MJD_TT
	 * @param r  GPS satellite State in ECI
	 * @param v  GPS satellite State in ECI
	 * @return
	 */
	public static double getYawAngle(double MJD, VectorN r, VectorN v, int prn)
	{
		if (firstTime)
		{
			String out_directory = "C:\\GOESR\\output\\";
			String alphaFile = "yawResults.txt";
		    yawResults = new LinePrinter(out_directory+alphaFile);
			firstTime = false;

		}
		
		
		//Get the transformation matrix from ECI to RTN with the
		//understanding that the normal component is defined to be
		//negative
		T = new Matrix(YawRotationMatrix(r,v));
		
		//Determine the Sun Vector
		double MJD_TT = TimeUtils.UTCtoTT(MJD);
		VectorN uSun = unitSun(MJD_TT,r);
	 
		//Rotate the Sun Vector into the Orbit Frame for 
		VectorN uSunRSW = T.times(uSun);
		
		//Determine the elevation of the sun above the orbit plane
		//b = asin(-Ry);
		beta = Math.asin(uSunRSW.get(2)*(-1.0));
		
		//Determine the azimuth of the sun vector in the orbit plane
		//This angle is the angle from Noon to the spacecraft in a 
		//Counter-Clockwise direction
		//a = atan2(-Rx/-Rz);
		alpha = Math.atan2(((-1.0)*uSunRSW.get(0)),(-1.0)*uSunRSW.get(1));
		
		   
		
		//Use alpha and beta to determine the yaw angle
		//yaw = atan2(sin(b)/(cos(b)*sin(a));
		yawAngle = (180/Math.PI)*Math.atan2(Math.sin(beta),Math.cos(beta)*Math.sin(alpha));
		alpha = alpha *(180/Math.PI);
		beta  = beta *(180/Math.PI);
		
		VectorN results = new VectorN(7);
		results.set(0,prn);
		results.set(1,alpha);
		results.set(2,beta);
		results.set(3,(yawAngle));
		results.set(4,r.get(0));
		results.set(5,r.get(1));
		results.set(6,r.get(2));
		yawResults.println(results.toString());
		
		return yawAngle;
	}

}

