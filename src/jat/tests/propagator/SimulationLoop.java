/* JAT: Java Astrodynamics Toolkit
 *
 * Copyright (c) 2005 National Aeronautics and Space Administration. All rights reserved.
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
package jat.tests.propagator;

import jat.coreNOSA.algorithm.integrators.LinePrinter;
import jat.coreNOSA.algorithm.integrators.RungeKutta8;
import jat.coreNOSA.forces.ForceModelListOld;
import jat.coreNOSA.math.MathUtils;
import jat.coreNOSA.math.MatrixVector.data.VectorN;
import jat.coreNOSA.matlabInterface.MatlabControl;
import jat.coreNOSA.spacecraft.Spacecraft;
import jat.coreNOSA.timeRef.EarthRef;
import jat.coreNOSA.trajectory.Trajectory;

/**
 * SimulationLoop provides a way to encapsulate the feedback loop
 * used to propagate the satellite and implement a control law.
 * 
 * spacecraft[]-->(sum)-->[Dynamics]->[Control Law]-->acceleration[]
 *                  A -                             |
 *                  |_______________________________|
 *       
 * @author Richard C. Page III
 *
 */
public class SimulationLoop {

    /**
     * earthRef: Reference variable for the Earth fixed coordinate system. 
     */
    protected EarthRef earthRef;
//    /**
//     * iers: International Earth Rotation and Reference System Service 
//     * information.
//     */
//    protected FitIERS iers;
    /**
     * spacecraft: An array of the various spacecraft to propagate.
     * The usage of multiple spacecraft allows for implementation of 
     * spacecraft formation dependent controls.
     */
    protected Spacecraft[] sc;
    /**
     * num_sc: number of spacecraft;
     */
    protected int num_sc;
    /**
     * mjd_start: The start time of the simulation as a Modified Julian Date (UTC).
     */
    protected double mjd_start;
    /**
     * t: Current Simulation time expressed as seconds since mjd_start.
     */
    protected double t;
    /**
     * tf: Finish time expressed as seconds since mjd_start.
     */
    protected double tf;
    /**
     * counter: Counts the number of iterations.
     */
    protected int counter;
    /**
     * timestep: Simulation timestep.
     */
    protected double timestep;
    /**
     * forces: An array of force model lists representing the forces 
     * which act on the various spacecraft.
     */
    protected ForceModelListOld[] forces;
    /**
     * useJAT: A flag which determines whether to use the integrator from
     * JAT or to call the Matlab(R) integrator.
     */
    protected boolean useJAT = true;
    /**
     * out: Output printer which to print position and velocity data.
     */
    protected LinePrinter out;
    /**
     * rk8: 8th order RungeKutta integrator.
     */
    protected RungeKutta8 rk8;
    /**
     * acceleration: The acceleration developed from the simulation loop 
     * on each spacecraft.  (Implemented as a parallel array to SimulationLoop.sc)
     */
    //protected VectorN[] acceleration;
    protected MatlabControl matlab;
    
    protected Trajectory jat;
    protected double thinning;
    protected boolean use_IERS;
    
    public SimulationLoop(double mjd0,double dt, Spacecraft[] s, 
            ForceModelListOld[] f, boolean q){
        mjd_start = mjd0;
        timestep = dt;
        sc = s;
        num_sc = s.length;
        forces = f;
        this.use_IERS = q;
        t = 0;
        rk8 = new RungeKutta8();
        int size = s.length;
        earthRef = new EarthRef(mjd0);
        for(int i=0; i<forces.length; i++){
            forces[i].updateEarthModel(earthRef);
        }
//        acceleration = new VectorN[size];
//        for(int i=0; i<size; i++){
//            acceleration[i] = new VectorN(0,0,0);
//        }
        jat = new Trajectory();
    }
    
    private void step(double dt){

System.out.println("step: "+t+" / "+tf+"    stepsize: "+dt); 
        rk8.setStepSize(dt);
        //* update models
        double mjd_utc = earthRef.mjd_utc();
        double[] X = new double[6];
        VectorN rnew;
        VectorN vnew;
        double[] tmp = new double[6];
        for(int i=0; i<num_sc; i++){
            //* update force models
            forces[i].updateEarthModel(earthRef);
            //forces[i].computeAccel(sc[i].r(),sc[i].v());  //*** Debug
            //* call integrator
            X[0] = sc[i].r().get(0);
            X[1] = sc[i].r().get(1);
            X[2] = sc[i].r().get(2);
            X[3] = sc[i].v().get(0);
            X[4] = sc[i].v().get(1);
            X[5] = sc[i].v().get(2);
//            if(sc[i].hasControlLaw()){
//                X = rk8.step(t, X, sc[i].getControlLaw());
//            }else{
                X = rk8.step(t, X, forces[i]);
//            }
            //* store new values
            rnew = new VectorN(X[0],X[1],X[2]);
            vnew = new VectorN(X[3],X[4],X[5]);
//rnew.print();
//vnew.print();
            sc[i].updateMotion(rnew,vnew);
            //* update simulation time
            if((t+dt) > tf){
                dt = tf-t;
            }
            t = t+dt;
            earthRef.updateTimeSinceStart(t);
            counter++;
        }
    }
    
    private void step(double dt, LinePrinter lp){
        step(dt);
        if(MathUtils.mod(counter,lp.getThinning())== 0)
            print(lp);
    }
    
   
    /**
     * Simulation loop.  Propagates the spacecraft.
     * @param duration The time over which to propagate in seconds.
     */
    public void loop(double duration){
        //format input parameters
        tf = duration;
        //loop over time equal to "duration"
        while(t < tf){
            step(timestep);
        }
        //store output parameters and stop
    }
    
    public void loop(double duration, LinePrinter lp){
//      format input parameters
        tf = duration;
        thinning = lp.getThinning();
        double timestep_mod = timestep;
        //print initial condition
//        lp.println("time [days] \t x [km] \t y [km] \t z [km] \t xdot [km/s] \t ydot [km/s] \t zdot [km/s]");
        print(lp);
        //loop over time equal to "duration"
        while(t < tf){
            step(timestep,lp);
        }
        //store output parameters and stop
    }
    
    
    protected void print(LinePrinter lp){
        double[] X = new double[6];
        //VectorN a;
        for(int i=0;i<num_sc;i++){
            X[0] = sc[i].r().get(0)/1000.0;
            X[1] = sc[i].r().get(1)/1000.0;
            X[2] = sc[i].r().get(2)/1000.0;
            X[3] = sc[i].v().get(0)/1000.0;
            X[4] = sc[i].v().get(1)/1000.0;
            X[5] = sc[i].v().get(2)/1000.0;
            //lp.println(sc[i].name);
//            System.out.println("printing");
            //a = forces[i].acceleration();
            lp.println(earthRef.mjd_utc()+"\t"+X[0]+"\t"+X[1]+"\t"+X[2]+"\t"
                    +X[3]+"\t"+X[4]+"\t"+X[5]);
            
            //      +"\t"+a.get(0)+"\t"+a.get(1)+"\t"+a.get(2));
            //System.out.println(t+"\t"+X[0]+"\t"+X[1]+"\t"+X[2]+"\t"+X[3]+"\t"+X[4]+"\t"+X[5]);
            //System.out.println(""+t+"  a: "+a.get(0)+"  "+a.get(1)+"  "+a.get(2));
        }
        jat.add(mjd_start+sec2days(t),X);
    }

    private double sec2days(double s){
        return s*0.000011574074;
    }
    
    public void set_use_IERS(boolean b){ this.use_IERS = b;}
    
    public Trajectory getTrajectory(){ return jat;}

    
    //** See SimulationMatlab.java
//    public void loopMatlab(double duration, LinePrinter lp, String cmd){
//        matlab = new MatlabControl();
////      format input parameters
//        tf = duration;
//        //print initial condition
////        lp.println("time [days] \t x [km] \t y [km] \t z [km] \t xdot [km/s] \t ydot [km/s] \t zdot [km/s]");
//        print(lp);
//        //loop over time equal to "duration"
//        while(t < tf){
//            stepMatlab(timestep,cmd);
//            print(lp);
//        }
//        //store output parameters and stop        
//    }    
    
    //** See SimulationMatlab.java
//  protected void stepMatlab(double dt, String cmd){
//      t = t + dt;
//      counter++;
//      //* update models
//      earthRef.incrementTime(dt);
//      double mjd_current = earthRef.mjd_utc();
//      iers.fit(mjd_current);
//      earthRef.setIERS(iers.getX(),iers.getY(),iers.getDUT1());
//      //* update force models
//      forces[0].updateEarthModel(earthRef);
//      //* setup matlab inputs
//      Object[] inputArgs = new Object[3];
//      inputArgs[0] = forces[0];
//      inputArgs[1] = new Double(dt);
//      double[] X = new double[6];
//      X[0] = sc[0].r().get(0);
//      X[1] = sc[0].r().get(1);
//      X[2] = sc[0].r().get(2);
//      X[3] = sc[0].v().get(0);
//      X[4] = sc[0].v().get(1);
//      X[5] = sc[0].v().get(2);
//      inputArgs[2] = X; 
//      double[] returnVals;
//      try {
//			returnVals = (double[])matlab.blockingFeval(cmd, inputArgs);
////			returnVals = (double[])Matlab.mtFeval(cmd, inputArgs, 0);
//			sc[0].updateMotion(new VectorN(returnVals[0],returnVals[1],returnVals[2]),
//	        		   new VectorN(returnVals[3],returnVals[4],returnVals[5]));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}		
//      //* update simulation time
//      if((t+dt) > tf){
//          dt = tf-t;
//      }
//      //earthRef.incrementTime(dt);
//      t = t+dt;
//      earthRef.updateTimeSinceStart(t);
//      counter++;
//		
//  }
}

