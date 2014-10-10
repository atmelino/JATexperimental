/* JAT: Java Astrodynamics Toolkit
 *
 * Copyright (c) 2006 National Aeronautics and Space Administration. All rights reserved.
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
 */

package jat.coreNOSA.matlabInterface;

import jat.coreNOSA.algorithm.integrators.Derivatives;
import jat.coreNOSA.algorithm.integrators.RungeKutta4;
import jat.coreNOSA.algorithm.integrators.RungeKutta8;
import jat.coreNOSA.algorithm.integrators.RungeKuttaFehlberg78;

import java.util.Arrays;
import java.util.Vector;

/**
 * The ODToolboxIntegrators.java Class is an adaptor for Matlab to call the JAT Integrators in the same 
 * way that Matlab calls it's own integrators. These integrators will only work with JAT force models.
 *
 * @author Derek Surka, National Aeronautics and Space Administration
 * date: 10 August 2007
 * @version 1.0
 */

public class ODToolboxIntegrators {

	/** This method calls the RK8 integrator based on the initial conditions from Matlab
	 * @param inputTime - user defined times
	 * @param x0 		- initial state
	 * @param stepSize	- Integrator step size
	 * @param derivs	- ODToolboxJATModel object that contains force information
	 * @return  		- Return the states at the requested input times
	 */
	public static double[][] RK8(double[] inputTime, double[] x0, double stepSize, Derivatives derivs)
	{
		int              neqns = x0.length;
		double[]          xNew = new double[neqns];
		double[]          xOld = new double[neqns];
		RungeKutta8 integrator = new RungeKutta8(stepSize);

		// Generate output time vector based on user-provided inputs
		double tSimulation = inputTime[inputTime.length-1] - inputTime[0];
		int timeLength, i;

		if ( tSimulation%stepSize >= 1.0 )
			timeLength = (int) (tSimulation/stepSize)+2;
		else
			timeLength = (int) (tSimulation/stepSize)+1;

		double[]  outputTime = new double[timeLength];
		double[][] simStates = new double[timeLength][neqns];
		double[]     simTime = createStepTime(inputTime, stepSize, timeLength);

		if (inputTime.length<3)
			outputTime = simTime; // if user did not specify timesteps, use integrator steps
		else {
			outputTime = new double[inputTime.length];
			outputTime = inputTime;
		}

		// store the initial states
		for (i=0; i < neqns; i++) {
			simStates[0][i]= x0[i];
			xOld[i]        = x0[i];
		}

		System.out.println("Integrating...");

		/* Step the integrator and store new states. */
		for (int tCounter=1; tCounter<timeLength; tCounter++) {
			xNew = integrator.step(simTime[tCounter], xOld, derivs);

			for (i = 0; i < neqns; i++) {
				simStates[tCounter][i] = xNew[i];
				xOld[i]                = xNew[i];
			}
		}

		return interpolateStates(outputTime, simTime, simStates, neqns);

	}

	/** This method calls the RK4 integrator based on the initial conditions from Matlab
	 * @param inputTime - user defined times
	 * @param x0 		- initial state
	 * @param stepSize	- Integrator step size
	 * @param derivs	- ODToolboxJATModel object that contains force information
	 * @return  		- Return the states at the requested input times
	 */
	public static double[][] RK4(double[] inputTime, double[] x0, double stepSize, Derivatives derivs)
	{
		int              neqns = x0.length;
		double[]          xNew = new double[neqns];
		double[]          xOld = new double[neqns];
		RungeKutta4 integrator = new RungeKutta4(stepSize);

		// Generate output time vector based on user-provided inputs
		double tSimulation = inputTime[inputTime.length-1] - inputTime[0];
		int timeLength, i;

		if ( tSimulation%stepSize >= 1.0 )
			timeLength = (int) (tSimulation/stepSize)+2;
		else
			timeLength = (int) (tSimulation/stepSize)+1;

		double[]  outputTime = new double[timeLength];
		double[][] simStates = new double[timeLength][neqns];
		double[]     simTime = createStepTime(inputTime, stepSize, timeLength);

		if (inputTime.length<3)
			outputTime = simTime; // if user did not specify timesteps, use integrator steps
		else {
			outputTime = new double[inputTime.length];
			outputTime = inputTime;
		}

		// store the initial states
		for (i=0; i < neqns; i++) {
			simStates[0][i]= x0[i];
			xOld[i]        = x0[i];
		}

		System.out.println("Integrating...");

		/* Step the integrator and store new states. */
		for (int tCounter=1; tCounter<timeLength; tCounter++) {
			xNew = integrator.step(simTime[tCounter], xOld, derivs);

			for (i = 0; i < neqns; i++) {
				simStates[tCounter][i] = xNew[i];
				xOld[i]                = xNew[i];
			}
		}

		return interpolateStates(outputTime, simTime, simStates, neqns);

	}

	/** This method calls the RKF78 integrator based on the initial conditions from Matlab
	 * @param inputTime 	- user defined times
	 * @param x0 			- initial state
	 * @param stepSize		- Integrator step size
	 * @param minStepSize	- Integrator minimum step size
	 * @param accuracy		- Integrator accuracy tolerance
	 * @param derivs		- ODToolboxJATModel object that contains force information
	 * @return  			- Return the states at the requested input times
	 */
	public static double[][] RKF78(double[] inputTime, double[] x0, double stepSize, double minStepSize, double accuracy, Derivatives derivs)
	{
		double[] outputTime;
		RungeKuttaFehlberg78 integrator = new RungeKuttaFehlberg78();

		// Set integrator parameters
		integrator.setAccuracy(accuracy);
		integrator.setMinimumStepSize(minStepSize);
		integrator.setStepSize(stepSize);
		integrator.setAdaptive();
		integrator.setSaveSteps(true);

		System.out.println("Integrating...");
		integrator.integrate(inputTime[0], x0, inputTime[inputTime.length-1], derivs);
		double[]     simTime = integrator.getIntermediateTimes();
		double[][] simStates = integrator.getIntermediateStates();

		// Get output times
		if (inputTime.length<3)
			outputTime = simTime; // if user did not specify timesteps, use integrator steps
		else {
			outputTime = inputTime;
		}

		return interpolateStates(outputTime, simTime, simStates, x0.length);

	}

	private static double[] createStepTime(double[] inputTime, double stepSize, int timeLength)
	{
		double[] stepTime 	= new double[timeLength];
		stepTime[0] 		= inputTime[0];
		double tFinal 		= inputTime[inputTime.length-1];
		double stop 		= tFinal - (stepSize/1000);

		System.out.println("Creating Time Steps...");

		double newStep;
		int counter = 0;
		while (stepTime[counter]<stop) {
			if ( (stepTime[counter]+stepSize) > tFinal ) {
				newStep = tFinal - stepTime[counter];				
			}
			else {
				newStep = stepSize;
			}
			stepTime[counter+1] = stepTime[counter]+newStep;
			counter ++;        			
		}

		return stepTime;
	}

	/** Determines if states and time need to be interpolated and then calls the 
	 *  interpolator to get the states corresponding to the user defined times. 
	 *  Also packages time and sim states into one array formatted per Matlab ode outputs
	 *  	Each row is a different timestep
	 *  	First column is time
	 *  	Remaining columns are states */	 
	private static double[][] interpolateStates(double[] outputTime, double[] simTime, double[][] simStates, int neqns)
	{
		int timeSpot = 0;
		double currentTime; 
		double[] interpolatedState = new double[neqns];
		double[][] newOutput = new double[outputTime.length][neqns+1];

		System.out.println("Interpolating...");
		for (int i=0; i<outputTime.length; i++) {
			currentTime     = outputTime[i];
			newOutput[i][0] = currentTime;

			timeSpot = Arrays.binarySearch(simTime, currentTime);
			if (timeSpot < 0) {
//				interpolatedState = linearInterpolator(simTime, currentTime, neqns, simStates);

				interpolatedState = LagrangianInterpolator(simTime, currentTime, neqns, simStates);
				for (int k = 0; k<neqns; k++)
					newOutput[i][k+1] = interpolatedState[k];
			} else {
				for (int k = 0; k<neqns; k++)
					newOutput[i][k+1] = simStates[timeSpot][k];
			}
		}	

		return newOutput;
	}

	/** This is the linear interpolator. It will calculate the states based on the current 
	 * time and the states before and after the current state of interest.
	 * @param timeArray Array of times from the integrator.
	 * @param currentTime the current user defined output time.
	 * @param store vector to store the location in the timeArray where the currentTime is located.
	 * @param neqns is the number of states.
	 * @param newStates double array containing the states from the integrator.
	 * @return
	 */

	private static double[] linearInterpolator(double[] timeArray, double currentTime, int neqns, double[][] newStates)
	{
		double earlierState;
		double laterState;
		double nextTime;
		double earlierTime;
		double slope;
		double stateDifference=0.0;
		double[] interpolatedState = new double[neqns];
		Vector store = new Vector();

		for(int n=0; n<timeArray.length; n++)
		{
			if (currentTime<timeArray[n])
			{
				store.add(new Integer(n));
			}
			else{
				if (n==(timeArray.length-1))
				{
					store.add(new Integer(timeArray.length-1));
				}

			}
		}
		Integer timePosition = (Integer) store.get(0);
		store.clear();
		nextTime = timeArray[timePosition.intValue()];
		earlierTime = timeArray[timePosition.intValue()-1];

		for (int g =0; g<neqns; g++)
		{
			earlierState = newStates[timePosition.intValue()-1][g];
			laterState = newStates[timePosition.intValue()][g];
			slope = (nextTime - earlierTime)/(laterState-earlierState);
			double earlierDistance = currentTime-earlierTime;
			double laterDistance = nextTime - currentTime;

			if (laterDistance<earlierDistance)
			{
				stateDifference = laterState - (slope*(nextTime-currentTime));
			}
			else
			{
				stateDifference = earlierState + (slope*(currentTime-earlierTime));
			}

			interpolatedState[g] = stateDifference;
		}
		return interpolatedState;
	}

	/** 7th Order Lagrangian Interpolator adapted from Steve Hughes Matlab Code. 
	 * @author Kate Bradley
	 * @date 20 March 2006
	 * @param timeArray is the array of times from the interpolator.
	 * @param currentTime is the current user specified output time.
	 * @param neqns is the number of states.
	 * @param newStates is the double array of states from the interpolator.
	 * @return
	 */
	private static double[] LagrangianInterpolator(double[] timeArray, double currentTime, int neqns, double[][] newStates)
	{
		int n = timeArray.length;
		double product;
		double [] interpolatedState = new double[neqns];
		int position=0;
		int start;
		int end;

		for (int t=0; t<timeArray.length-1; t++)
		{
			if((currentTime > timeArray[t]) && (currentTime <timeArray[t+1]))
			{
				if (currentTime > timeArray[timeArray.length-1])
				{
					position = t;
				}
				else 
				{
					position=t+1;
				}
				break;
			}
		}
		if (currentTime > timeArray[timeArray.length-1])
		{
			// Use Linear Extrapolation
			for (int g=0; g<neqns; g++)
			{
				double state = newStates[timeArray.length-1][g];
				double yi = (state/timeArray[timeArray.length-1])*currentTime;
				interpolatedState[g] = yi;
			}
		}
		else
		{
			if (position<5)
			{
				/*Means we are at the beginning of the array and we need to take values 
				 * from the beginning of the array.
				 */
				start = 0;
				end = 8;
			}
			else if (timeArray.length <position+4)
			{
				/*Means we are at the end of the array and we need to take values from the 
				 * end of the array.
				 */
				start = timeArray.length-8;
				end = timeArray.length;
			}
			else
			{
				/*Means we are somewhere in the middle of the array.
				 * 
				 */
				start = position-4;
				end = position+3;
			}

			for (int g=0; g<neqns; g++)
			{
				double yi=0;
				for (int i=start; i<end; i++)
				{
					product=newStates[i][g];

					for (int j=start; j<end; j++)
					{
						if(i!=j)
						{
							product=product*(currentTime-timeArray[j])/(timeArray[i]-timeArray[j]);
						}
					}
					yi=yi+product;
				}
				interpolatedState[g] = yi;
			}
		}
		return interpolatedState;
	}

}
