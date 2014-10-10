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
package jat.coreNOSA.forces;


import jat.coreNOSA.algorithm.integrators.Derivatives;
import jat.coreNOSA.math.MatrixVector.data.VectorN;
import jat.coreNOSA.spacecraft.Spacecraft;
import jat.coreNOSA.timeRef.EarthRef;
import jat.coreNOSA.timeRef.FitIERS;

import java.util.ArrayList;

/**
 * <p>
 * ForceModel creates an interface for computing the acceleration
 * acting on a body from an arbitrary source.
 * 
 * @author Richard C. Page III
 *
 */
public class ForceModelListOld implements Derivatives {
	
    protected int iers_counter = 1;
    
	/** Sum of all accelerations 
	 */
	protected VectorN acceleration;

	protected EarthRef earthRef;
	
	protected FitIERS iers;

	protected Spacecraft sc;
	
	protected ArrayList grav_forces;
	
	protected ArrayList other_forces;
	
	protected boolean use_iers = true;
	protected boolean use_sun = true;
	protected boolean use_moon = true;
	protected boolean use_update = true;
	
	
	/** Default Constructor
	 * 
	 */
	public ForceModelListOld(){
		acceleration = new VectorN(0,0,0);
		grav_forces = new ArrayList();
		other_forces = new ArrayList();
		sc = new Spacecraft();
	}
	
	public ForceModelListOld(Spacecraft s, double mjd){
		acceleration = new VectorN(0,0,0);
		grav_forces = new ArrayList();
		other_forces = new ArrayList();
		sc = s;
        earthRef = new EarthRef(mjd);
        earthRef.use_moon = this.use_moon;
        earthRef.use_sun = this.use_sun;
        if(this.use_iers){
            iers = new FitIERS();
//        iers.fit(earthRef.mjd_tt());
//        earthRef.setIERS(iers.getX(),iers.getY(),iers.getDUT1());
            double[] param = iers.search(earthRef.mjd_tt());
            earthRef.setIERS(param[0],param[1],param[2]);        
        }
	}
    
    public ForceModelListOld(Spacecraft s, double mjd, EarthRef eRef){
        acceleration = new VectorN(0,0,0);
        grav_forces = new ArrayList();
        other_forces = new ArrayList();
        sc = s;
        earthRef = eRef;
        earthRef.use_moon = this.use_moon;
        earthRef.use_sun = this.use_sun;
        if(this.use_iers){
            iers = new FitIERS();
//        iers.fit(earthRef.mjd_tt());
//        earthRef.setIERS(iers.getX(),iers.getY(),iers.getDUT1());
            double[] param = iers.search(earthRef.mjd_tt());
            earthRef.setIERS(param[0],param[1],param[2]);        
        }
    }

	
	public ForceModelListOld(double mjd, VectorN r, VectorN v, double CR, double cd,
	        double area, double mass){
	    sc = new Spacecraft(r,v,CR,cd,area,mass);
	    grav_forces = new ArrayList();
		other_forces = new ArrayList();
		earthRef = new EarthRef(mjd);
		earthRef.use_moon = this.use_moon;
        earthRef.use_sun = this.use_sun;
		if(this.use_iers){
		    iers = new FitIERS();
//		iers.fit(earthRef.mjd_tt());
//        earthRef.setIERS(iers.getX(),iers.getY(),iers.getDUT1());
		    double[] param = iers.search(earthRef.mjd_tt());
		    earthRef.setIERS(param[0],param[1],param[2]);
		}
	}
	
	public ForceModelListOld(Spacecraft s){
	    acceleration = new VectorN(0,0,0);
		grav_forces = new ArrayList();
		other_forces = new ArrayList();
		sc = s;
	}
	
    public ForceModelListOld(Spacecraft s, EarthRef eRef){
        acceleration = new VectorN(0,0,0);
        grav_forces = new ArrayList();
        other_forces = new ArrayList();
        sc = s;
        earthRef = eRef;
    }
    
	/**
	 * Adds a generic force to the list
	 * @param f Object which implements the ForceModel interface
	 */
	public void addOtherForce(EarthForceModel f){
		other_forces.add(f);
	}
	
	/**
	 * Adds a point mass gravitational force to the list
	 * @param f Object which extends GravitationalBody
	 */
	public void addGravForce(GravitationalBody f){
	    grav_forces.add(f);
	}
	
	/** Compute the acceleration
	 * @return acceleration [m/s^2]
     */
	public VectorN computeAccel(VectorN r, VectorN v){
		//retrieve/update acceleration
		sc.updateMotion(r,v);
		acceleration = new VectorN(3);
		EarthForceModel temp;
		VectorN accel;
		for(int i=0; i<other_forces.size(); i++){
			temp = (EarthForceModel)other_forces.get(i);
			accel = temp.acceleration(earthRef,sc);
			acceleration = acceleration.plus(accel);
		}
		GravitationalBody temp2;
		for(int i=0; i<grav_forces.size(); i++){
		    temp2 = (GravitationalBody)grav_forces.get(i);
		    accel = temp2.acceleration(earthRef,sc);
		    acceleration = acceleration.plus(accel);
//		    acceleration.print("accel "+i+"  "+earthRef.mjd_utc());
		}
		return acceleration;
	}

	/** Get the acceleration
	 * @return acceleration
	 */
	public VectorN acceleration(){
		return acceleration;
	}	
	
	
	public void updateEarthModel(EarthRef ref){
	    earthRef = ref;
	    if(this.use_iers && use_update){// && (this.iers_counter*iers.get_update_interval() <= ref.get_sim_time())){
	        double[] param = iers.search(earthRef.mjd_tt());
            earthRef.setIERS(param[0],param[1],param[2]);
            //this.iers_counter++;
	    }
	}
	
	public GravitationalBody get_GravForce(int i){
	    return (GravitationalBody)grav_forces.get(i);
	}
	
	public EarthForceModel get_OtherForce(int i){
	    return (EarthForceModel)other_forces.get(i);
	}
	
	public void set_use_update(boolean b){
	    this.use_update = b;
	}
	
	public void set_use_IERS(boolean b){
	    this.use_iers = b;
	    if(b) iers = new FitIERS();
	    if(earthRef != null && b){
	        double[] param = iers.search(earthRef.mjd_tt());
            earthRef.setIERS(param[0],param[1],param[2]); 
	    }
	}
	public void set_use_sun(boolean b){
	    this.use_sun = b;
	    this.earthRef.set_use_sun(b);
	}
	public void set_use_moon(boolean b){
	    this.use_moon = b;
	    this.earthRef.set_use_moon(b);
	}
	
	/** Implement Derivatives interface
	 * 
	 */
	public double[] derivs(double t, double[] x){
		double[] out = new double[6];
		VectorN r = new VectorN(x[0],x[1],x[2]);
		VectorN v = new VectorN(x[3],x[4],x[5]);		
		if(use_iers && use_update){
		    //iers.fit(earthRef.mjd_tt());
		    //earthRef.setIERS(iers.getX(),iers.getY(),iers.getDUT1());
		    double[] param = iers.search(earthRef.mjd_tt());
		    earthRef.setIERS(param[0],param[1],param[2]);
		}
		earthRef.updateTimeSinceStart(t);
		computeAccel(r,v);
		out[0] = x[3];
		out[1] = x[4];
		out[2] = x[5];
		out[3] = acceleration.x[0];
		out[4] = acceleration.x[1];
		out[5] = acceleration.x[2];
		return out;
	}
	
	public double[] matlab_derivs(double mjd, double[] x){
	    double[] out = new double[6];
		VectorN r = new VectorN(x[0],x[1],x[2]);
		VectorN v = new VectorN(x[3],x[4],x[5]);
		computeAccel(r,v);
		out[0] = x[3];
		out[1] = x[4];
		out[2] = x[5];
		out[3] = acceleration.x[0];
		out[4] = acceleration.x[1];
		out[5] = acceleration.x[2];
		return out;
	}
}
