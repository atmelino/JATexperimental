package jat.coreNOSA.measurements;


import jat.coreNOSA.algorithm.estimators.EKF;
import jat.coreNOSA.algorithm.estimators.MeasurementFileModel;
import jat.coreNOSA.algorithm.estimators.MeasurementModel;
import jat.coreNOSA.math.MatrixVector.data.VectorN;
import jat.coreNOSA.simulation.EstimatorSimModel;
import jat.coreNOSA.simulation.initializer;

import java.util.HashMap;
import java.util.Random;

public class stateUpdateMeasurementModel implements MeasurementFileModel,MeasurementModel{
	
	public static VectorN R;
	public static int numStates;
	HashMap hm;// = closedLoopSim.hm;
	//* *NOTE* Added argument to default constructor instead of call to static var
	//*        at instantiation
	Random generator;
	
	public stateUpdateMeasurementModel(HashMap h) {
		hm = h;
		/*Add a sleep in here to insure that the Random Number
		 * Seeds don't allign with any other random number generator
		 */
		try {
			Thread.sleep(20);
			generator = new Random();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
	}
	
	public VectorN getMeasurement()
	{
		String tmp;
		VectorN pointSolution = new VectorN(6);
		
		tmp = "MEAS."+EKF.measNum+".satellite";
		int sat = initializer.parseInt(hm,tmp);
		
		//double[] truth = closedLoopSim.truth[sat].sc.get_spacecraft().toStateVector();
		double[] truth = EstimatorSimModel.truth[sat].get_spacecraft().toStateVector();
		
		//Add in the measurement noise read out of the file
		for(int j = 0; j < 6; j++)
		{
			tmp = "MEAS."+EKF.measNum+".R."+j;
			double R = initializer.parseDouble(hm,tmp);
			
			/*Scale the error as Gaussian noise times the 
			 square of the measurement noise*/
			pointSolution.x[j] =  truth[j] +generator.nextGaussian()*R*R; 
		}	
		return pointSolution;
	}
	
	public VectorN predictMeasurement(VectorN state){
		
		
		String tmp = "MEAS."+EKF.measNum+".satellite";
		int sat = initializer.parseInt(hm,tmp);
		
		VectorN range = new VectorN(6);
		range.set(0,state.get(0+(6*sat)));
		range.set(1,state.get(1+(6*sat)));
		range.set(2,state.get(2+(6*sat)));
		range.set(3,state.get(3+(6*sat)));
		range.set(4,state.get(4+(6*sat)));
		range.set(5,state.get(5+(6*sat)));
		
		return range;
		
	}
	
	public double  zPred(int i, double time, VectorN state){
		String tmp = "MEAS."+EKF.measNum+".satellite";
		int sat = initializer.parseInt(hm,tmp);
		
		VectorN oMinusC;
		VectorN pred = predictMeasurement(state);
		VectorN obs = getMeasurement();
		oMinusC      = obs.minus(pred);
		
		//Ensure we are returning the correct state when there is more than
		//one satellite
		int j = i - 6*sat; 
		return oMinusC.get(j);
	}
	public double  zPred(ObservationMeasurement om, int i, double time, VectorN state){
		String tmp = "MEAS."+EKF.measNum+".satellite";
		int sat = initializer.parseInt(hm,tmp);
		
		VectorN oMinusC;
		VectorN pred = predictMeasurement(state);
		//VectorN obs = om.get_obs_data(ObservationMeasurement.DATA_);
		//*TODO
		VectorN obs = om.get_state(3);
		oMinusC      = obs.minus(pred);
		
		//Ensure we are returning the correct state when there is more than
		//one satellite
		int j = i - 6*sat; 
		return oMinusC.get(j);
	}
	
	/** Return the measurement noise value for this measurement
	 * 
	 *   
	 */
	public double R()
	{
		int whichState = EKF.stateNum;
		int measNum    = EKF.measNum;
		
		
		//Ensure we are returning the correct state when there is more than
		//one satellite
		String tmp = "MEAS."+EKF.measNum+".satellite";
		int sat = initializer.parseInt(hm,tmp);
		int j = whichState - 6*sat; 
		

		tmp = "MEAS."+measNum+".R."+j;
		double R = initializer.parseDouble(hm,tmp);
		return R;
	}
	public double R(ObservationMeasurement om){
		return R();
	}
	
	public VectorN H(VectorN state)
	{
		/*Determine the number of states*/
		int whichState = EKF.stateNum;
		int numStates = initializer.parseInt(hm,"FILTER.states");
		
	
		/*for a Range measurement, the current state has H = 1, all other states H = 0 */
		VectorN H = new VectorN(numStates);
		H.set(0.0);
		H.set(whichState,1.0);
		return H;
	}
	public VectorN H(ObservationMeasurement om, VectorN state){
		return H(state);
	}
}