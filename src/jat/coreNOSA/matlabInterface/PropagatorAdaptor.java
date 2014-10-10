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
import jat.coreNOSA.algorithm.integrators.RungeKutta8;
import jat.coreNOSA.math.MatrixVector.data.VectorN;
import jat.coreNOSA.simulation.SimModel;
import jat.coreNOSA.spacecraft.SpacecraftModel;

import java.util.Arrays;
import java.util.Vector;

/**
* The JatAdaptor.java Class is an adaptor for Matlab to call the JAT Integrators in the same 
* way that Matlab calls it's own integrators.
*
* @author Kate Bradley, National Aeronautics and Space Administration
* date: 15 February 2006
* @version 1.0
*/

public class PropagatorAdaptor {
	
	/** This method calls the RK8 integrator based on the initial conditions from Matlab 
	 * for the Matlab defined EOMs
	 * 
	 * @param DerivativesName -- What is the name of the Matlab EOM file
	 * @param initialTime -- Time increments from the user
	 * @param x0 -- Initial State
	 * @param stepSize -- Step size for the integrator
	 * @return -- Returns state output for the output times to Matlab
	 */
	 public static double[][] RK8(String DerivativesName, double [] initialTime, double [] x0, double stepSize)
	    {		 	
		 	double mod =  ((initialTime[initialTime.length-1]-initialTime[0])%stepSize);
		 	int timeLength;
		 	if ((mod != 0.0) && (mod >= 1.0))
		 	{
		 		timeLength = (int) ((initialTime[initialTime.length-1]-initialTime[0])/stepSize)+2;
		 	}
		 	else
		 	{
			 	timeLength = (int) ((initialTime[initialTime.length-1]-initialTime[0])/stepSize)+1;
		 	}
		 	double[] time = new double[timeLength];
		 	double[] stepTime = new double[timeLength];
		 	
		 	if (initialTime.length<3)
       	{
       		stepTime = createStepTime(initialTime, stepSize, timeLength);
       		time = stepTime;
       		
       	}
       	else 
       	{
       		stepTime = createStepTime(initialTime, stepSize, timeLength);
       		time = new double[initialTime.length];
       		time = initialTime;
       	}
		    // Initialize start and finish times
	    	int neqns = x0.length;
	    	double [][] oldState = new double [time.length][neqns+1];
	    	
	    	// Use if calling from java
	        //JatDerivatives MD = new JatDerivatives();
	    	// Use if calling from Matlab
	    	MatlabDerivs MD = new MatlabDerivs(DerivativesName);

        	// Send to integrate the equations
        	oldState = jat_RK8(time, x0, stepSize, MD);
	    
        	return oldState;
	    }

	/** This method calls the RK8 integrator based on the initial conditions from Matlab 
	 * for the JGM2 & JGM3 cases
	 * 
	 * @param DerivativesName -- Whether it is JGM2 or JGM3 test case
	 * @param initialTime -- User defined times
	 * @param x0 -- Initial State
	 * @param stepSize -- Integrator step size
	 * @param cd -- Coefficient of drag
	 * @param cr -- Coefficient of reflectivity
	 * @param mass -- Mass of the satellite
	 * @param cArea -- Cross sectional area
	 * @param mjd_utc -- Mean Julian Date in UTC time
	 * @param path -- Path to JAT
	 * @param matOrder -- added by DMS on 5/18/07
	 * @param matDegree -- added by DMS on 5/18/07
	 * @return -- Return the states based on times to Matlab
	 */
	 public static double[][] RK8(String DerivativesName, double [] initialTime, double [] x0, double stepSize, double cd, double cr, double mass, double cArea, double mjd_utc, String path, double matOrder, double matDegree)
	    {
	     	int degree = (int) matDegree;  	// added by DMS 5/18/07
	     	int order = (int) matOrder;		// added by DMS 5/18/07

	     	double mod =  ((initialTime[initialTime.length-1]-initialTime[0])%stepSize);
		 	int timeLength;
		 	if (mod !=0.0 && mod>=1.0)
		 	{
		 		timeLength = (int) ((initialTime[initialTime.length-1]-initialTime[0])/stepSize)+2;
		 	}
		 	else
		 	{
			 	timeLength = (int) ((initialTime[initialTime.length-1]-initialTime[0])/stepSize)+1;
		 	}
		 	double[] time = new double[timeLength];
		 	double[] stepTime = new double[timeLength];
        	
		 	if (initialTime.length<3)
        	{
        		stepTime = createStepTime(initialTime, stepSize, timeLength);
        		time = stepTime;
        	}
        	else 
        	{
        		stepTime = createStepTime(initialTime, stepSize, timeLength);
        		time = new double[initialTime.length];
        		time = initialTime;
        	}
		    // Initialize start and finish times
	    	int neqns = x0.length;
	    	double[][] output = new double[stepTime.length][neqns+1];
	    	double [][] oldState = new double [time.length][neqns+1];
	    	
	        if (DerivativesName.equals("JatUniverseJGM2"))
	        {
	        	boolean use_JGM2 = true;
	        	// add order,degree to runJGMSimulation call (DMS 5/18/07)
	        	output = runJGMSimulation(x0,stepTime, mjd_utc, stepSize, cd, cr, mass, cArea, use_JGM2,neqns, path, order, degree);	
	        	oldState = checkOutput(output, initialTime, neqns);
	        	
	        }
	        
	        else
	        {
	        	boolean use_JGM2 = false;
	        	// add order,degree to runJGMSimulation call (DMS 5/18/07)
	        	output = runJGMSimulation(x0,stepTime, mjd_utc, stepSize, cd, cr, mass, cArea, use_JGM2, neqns, path, order, degree);  
	        	oldState = checkOutput(output,initialTime, neqns);
	        }

		    return oldState;
		        
	    }
	 
	 private static double[] createStepTime(double[] initialTime, double stepSize, int timeLength)
	 {
		double[] stepTime = new double[timeLength];
 		int counter = 1;
		stepTime[0] = initialTime[0];
		double newStep;
		double stop = initialTime[initialTime.length-1]-(stepSize/1000);
		
		while (stepTime[counter-1]<stop)
		{
			if ((stepTime[counter-1]+stepSize) > initialTime[initialTime.length-1])
			{
				newStep = stepSize - (stepTime[counter-1]+stepSize-initialTime[initialTime.length-1]);
				
			}
			else
			{
				newStep = stepSize;
			}
			
			stepTime[counter] = stepTime[counter-1]+newStep;
    		counter ++;        			
		}
		 return stepTime;
	 }
	 
	 private static double[][] checkOutput(double[][] output, double[] time, int neqns)
	 {
		 if(time.length<3)
		 {
		 }
		 /* When user specifies output times the code comes here to get the corresponding
	      * states. */
	     else
	     {
	    	 double[] timeArray = new double[output.length];
	    	 double[][] newStates = new double[output.length][neqns];

	    	 for (int i=0; i<output.length; i++)
	    	 {
	    		 timeArray[i] = output[i][0];
	    		 
		    	 for (int j=0; j<neqns; j++)
		    	 {
		    		 newStates[i][j] = output [i][j+1];
		    	 }
		    	 
	    	 }
	    	 output = interpolateStates(time, timeArray, newStates, neqns); 
	     }

		 return output;
	 }

	 // add order, degree DMS 5/18/07
	 private static double[][] runJGMSimulation(double[] x0,double[] time, double mjd_utc, double stepSize, double cd, double cr, double mass, double cArea, boolean use_JGM2, int neqns, String path, int order, int degree)
	 {
		//double start = System.currentTimeMillis();
		double[][] output = new double[neqns][];
//		* force_flag = {2-Body, Sun,   Moon, Harris Priester, Solar Radiation
//     	boolean[] force_flag = {true,false,false,false,false};  removed DMS 5/18/07
     	boolean[] force_flag = {false,false,false,false,false};  // DMS 5/18/07 for JGM testing
     	
     	VectorN r = new VectorN(x0[0], x0[1], x0[2]);
     	VectorN v = new VectorN(x0[3], x0[4], x0[5]);
     	SimModel sim = new SimModel();
        double t0 = time[0];
        double tf = time[time.length-1];
     	
     	// either NRL or HarrisPriester
     	String drag_model = "NRL";
     	SimModel JGM2All = new SimModel();
        SpacecraftModel sm = new SpacecraftModel(r,v,cr,cd,cArea,mass);
     	
     	//JGM2All.initialize(sm,t0,tf,mjd_utc, stepsize, 1, out);
//     	JGM2All.initialize(sm,t0,tf,mjd_utc, stepSize);  // added DMS 5/18/07
//     	JGM2All.initializeForces(force_flag, use_JGM2, drag_model, order, degree);  // order, degree added DMS 5/18/07
 
        
        for(int i=0; i<1; i++)
        {
        	sim.initialize(sm,t0,tf,mjd_utc, stepSize);
            String test = drag_model;
            sim.initializeForces(force_flag, use_JGM2, test, order, degree);  // order, degree added DMS 5/18/07
            output = sim.runloopMatlabAdaptor(x0, time);
        }	 
             
         //double elapsed = (System.currentTimeMillis()-start)*0.001/60;
         return output;
	 }
	 
	 /** Actually integrates the equations of motion using the RungeKutta8 step method */
	 private static double[][] jat_RK8(double[] time, double []x0, double stepSize, Derivatives derivs)
	 {
		 int neqns = x0.length;
	     double dt = stepSize;
	     double t = time[0];
	     double tf = time[time.length-1];
	     double mod =  ((tf-t)%dt);
	     //System.out.println("Mod: "+mod);
	     int timeLength;
	     
		 	if (mod !=0.0 && mod>=.09)
		 	{
		 		timeLength = (int) ((tf-t)/stepSize)+2;
		 	}
		 	else
		 	{
			 	timeLength = (int) ((tf-t)/stepSize)+1;
		 	}
	
	     double[] newstate = new double[neqns];
	     double[] oldstate = new double[neqns];
	     RungeKutta8 integrate = new RungeKutta8(stepSize);
	     double[] timeArray = new double[timeLength];
	     double[][] newStates = new double[timeLength][neqns];
	     
	     // put initial conditions into the previous state array
	     for (int i = 0; i < neqns; i++) 
	     {
	    	 oldstate[i] = x0[i];
	     }

	     if ((t + dt) > tf) 
	     {
	         dt = (tf - t);
	     }

	     int counter=1;
	     // Set the initial time in the time vector
	     timeArray[0]=t;
	     
	     /* Steps the integrator while t<tf and assigns the states to newState vector and
	      * time to the time vector. */
	     while (t < tf) 
	     {
	    	 newstate = integrate.step(t, oldstate, derivs);
	         for (int i = 0; i < neqns; i++) 
	         {
	        	oldstate[i] = newstate[i];
	         }
	         
	         t = t + dt;
	         
	         timeArray[counter]=t;
	         for (int i = 0; i < neqns; i++)
	         {
	        	 newStates[counter][i]=newstate[i];
	         }
	         counter ++;
	         
	         if ((t + dt) > tf) 
	         {
	        	dt = tf - t;
	         }
	      }
	     
	     // Sets the initial states in the newStates vector.
		 for (int j = 0; j<neqns; j++)
	     {
	    	 newStates[0][j]=x0[j];
	     }

		 // Creates Output matrix that will contain the time and state values. 
	     double[][] newOutput = new double[timeArray.length][neqns+1];

	    	 for (int i = 0; i<timeArray.length; i++)
	    	 {
	    		 for (int k = 0; k<neqns+1; k++)
	    		 {
	    			 if (k==0)
	    			 {
	    				 newOutput[i][k]= timeArray[i];
	    			 }
	    			 else
	    			 {
	    				 newOutput[i][k] = newStates[i][k-1];
	    			 }
	    		 }		 
	    	 }
	    	 newOutput = checkOutput(newOutput, time, neqns);

	      return newOutput;        
}
	 
	 /** Called if user specifies output other than step size based. Figures out if states
	  * and time need to be interpolated and then calls the interpolator to get the states
	  * corresponding to the user defined times. */
	 
	 private static double[][] interpolateStates(double[] OutputTimes, double[] timeArray, double[][] newStates, int neqns)
	 {
    	 int timeSpot = 0;
		 double currentTime; 
    	 double[] interpolatedState = new double[neqns];
    	 double[][] newOutput = new double[OutputTimes.length][neqns+1];

    	 for (int i=0; i<OutputTimes.length; i++)
    		 {
    		 	currentTime = OutputTimes[i];
    		 	//store.clear();
	 			
	 			try
	 			{
	 				timeSpot = Arrays.binarySearch(timeArray, currentTime);
	 				if (timeSpot < 0)
	 				{
	 					//interpolatedState = linearInterpolator(timeArray, currentTime, neqns, newStates);
	 					
	 					interpolatedState = LagrangianInterpolator(timeArray, currentTime, neqns, newStates);
	 				}
	 			}
	 			finally
	 			{
	 				
	 			}

	 			for (int k = 0; k<neqns+1; k++)
	 			{
	 				if (k==0)
	 				{
	 					newOutput[i][k]= currentTime;
	 				}
	 				else
	 				{
	 					if(timeSpot<0)
	 					{
	 						newOutput[i][k] = interpolatedState[k-1];
	 					}
	 					else
	 					{
	 						newOutput[i][k] = newStates[timeSpot][k-1];
	 					}
	 				}
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
