package jat.coreNOSA.measurements;

import jat.coreNOSA.algorithm.estimators.MeasurementModel;
import jat.coreNOSA.math.MatrixVector.data.VectorN;
import jat.coreNOSA.simulation.initializer;

import java.util.HashMap;


public class createMeasurements{

//	public static String []measurementTypes;
//	public static VectorN measurementValues;
//	public static int numMeasurementTypes;
//	public static MeasurementModel [] mm;
//	public static double [] frequency;
	public String []measurementTypes;
	public VectorN measurementValues;
	public int numMeasurementTypes;
	public MeasurementModel [] mm;
	public double [] frequency;
	
	//* *NOTE* added HashMap as an argument rather than calling static closedLoopSim.hm
	public createMeasurements(HashMap hm)
	{
		//Read in all of the required measurements
		//HashMap hm = closedLoopSim.hm;
		numMeasurementTypes = initializer.parseInt(hm,"MEAS.types");
		measurementTypes = new String[numMeasurementTypes];
		mm = new MeasurementModel [numMeasurementTypes];
		frequency = new double[numMeasurementTypes];
		
		for(int i = 0;i<numMeasurementTypes;i++)
		{
			String meas = "MEAS."+i+".desc";
			measurementTypes[i] = initializer.parseString(hm,meas);
			String freq = "MEAS."+i+".frequency";
			frequency[i] = initializer.parseDouble(hm,freq);
			System.out.println(measurementTypes[i]);
			if(measurementTypes[i].equals("position"))
			{
				mm[i] = new stateMeasurementModel(hm);
				//MeasurementModel pp = new stateMeasurementModel();
			}
			else if(measurementTypes[i].equals("range"))
			{
				mm[i] = new rangeMeasurementModel(hm);
			}
			else if(measurementTypes[i].equals("GPS"))
			{
				mm[i] = new GPSmeasurementModel(hm);
			}
			else if(measurementTypes[i].equals("pseudoGPS"))
			{
				mm[i] = new GPSStateMeasurementModel(hm);
			}
			else if(measurementTypes[i].equals("stateUpdate"))
			{
				mm[i] = new stateUpdateMeasurementModel(hm);
			}
			else if(measurementTypes[i].equals("OPT")){
				mm[i] = new OpticalMeasurementModel(hm,i);
			}
			else
			{
				System.out.println("Invalid measurement type.");
				System.exit(1);
				
			}
			if(measurementTypes[i].equalsIgnoreCase("OPT")){
				measurementTypes[i] = initializer.parseString(hm,"MEAS."+i+".type");
			}
		}
	}
	
	public int getNumberMeasurements()
	{	
		/*For most measurement types, there will be only
		 * one measurement per epoch.  Caution must be
		 * taken for measurement types with multiple
		 * measurements (such as GPS)
		 */

		
		return numMeasurementTypes;
	}

}

