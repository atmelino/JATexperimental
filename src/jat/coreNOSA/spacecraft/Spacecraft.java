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
package jat.coreNOSA.spacecraft;

import jat.coreNOSA.math.MatrixVector.data.Matrix;
import jat.coreNOSA.math.MatrixVector.data.Quaternion;
import jat.coreNOSA.math.MatrixVector.data.VectorN;
import jat.coreNOSA.timeRef.RSW_Frame;

/**
 * The Spacecraft class encapsulates physical parameters for a spacecraft and acts
 * as an object representing the spacecraft hardware.  For a complete model of spacecraft
 * dynamics see SpacecraftModel.java
 * 
 * @see jat.coreNOSA.spacecraft.SpacecraftModel
 * 
 * @author Richard C. Page III
 *
 */
public class Spacecraft {

    /**
     * name: The name of the spacecraft (e.g. "NCC-1701-D") 
     */
    public String id;
    /**
     * r: Position [m] (Vector3)
     */
	protected VectorN r;
	/**
	 * v: Velocity [m/s] (Vector3)
	 */
	protected VectorN v;
	/**
	 * q: Quaternion representing attitude (Inertial to Body frame) (Vector4)
	 */
	protected Quaternion q;
	/**
	 * RIC: Radial Intrack Crosstrack frame transformations.
	 */
	protected RSW_Frame RIC;
	/**
	 * CR: Coefficient of Reflectivity
	 */
	protected double CR;
	/**
	 * cd: Coefficient of drag
	 */
	protected double cd;
	/**
	 * area: Surface area [m^2]  Should be deprecated because separated drag and SRP areas
	 */
	protected double area;
	/**
	 * area: Surface area [m^2] for drag computation
	 */
	protected double dragArea;
	/**
	 * area: Surface area [m^2] for solar radiation pressure computation
	 */
	protected double srpArea;
	/**
	 * mass: mass [kg]
	 */
	protected double mass;
//	/**
//	 * control: Spacecraft control law
//	 */
//	protected ControlLaw control;
//	/**
//	 * hasControlLaw: flag indicating whether this spacecraft has a
//	 * control law associated with it.
//	 */
//	protected boolean hasControlLaw = false;
	
	/**
	 * Sets whether to print spacecraft coefficients into the state vector.  Default: false.
	 */
	protected boolean use_params_in_state = false;
	
	/**
	 * Default Constructor
	 */
	public Spacecraft(){
		r = new VectorN(0,0,0);
		v = new VectorN(0,0,0);
		q = new Quaternion();
		CR = 0;
		cd = 0;
		area = 0;
	    dragArea = area;
	    srpArea  = area;
		mass = 0;
		RIC = new RSW_Frame(r,v);
	}
	/**
	 * Constructor
	 * @param ar Position vector [m]
	 * @param av Velocity vector [m/s]
	 * @param acr Coefficient of Reflectivity
	 * @param acd Coefficient of Drag
	 * @param aa Surface Area [m^2]
	 * @param am Mass [kg]
	 */
	public Spacecraft(VectorN ar, VectorN av, double acr, double acd,
			double aa, double am){
		r = ar;
		v = av;
		q = new Quaternion();
		CR = acr;
		cd = acd;
		area = aa;
	    dragArea = area;
	    srpArea  = area;
		mass = am;
		RIC = new RSW_Frame(r,v);
	}
	/**
	 * Constructor
	 * @param ar Position vector [m]
	 * @param av Velocity vector [m/s]
	 * @param att Spacecraft Attitude as a quaternion
	 * @param acr Coefficient of Reflectivity
	 * @param acd Coefficient of Drag
	 * @param aa Surface Area [m^2]
	 * @param am Mass [kg]
	 */
	public Spacecraft(VectorN ar, VectorN av, Quaternion att,
	        double acr, double acd,double aa, double am){
		r = ar;
		v = av;
		q = att;
		CR = acr;
		cd = acd;
		area = aa;
	    dragArea = area;
	    srpArea  = area;
		mass = am;
		RIC = new RSW_Frame(r,v);
	}

	/**
	 * Constructs a spacecraft out of a statevector with spacecraft properties
	 * appended at the end of the vector.  [x y z xdot ydot zdot CR cd area mass]
	 * 
	 * @param X
	 */
	public Spacecraft(double[] X){
	    r = new VectorN(X[0],X[1],X[2]);
	    v = new VectorN(X[3],X[4],X[5]);
	    q = new Quaternion();
	    CR = X[6];
	    cd = X[7];
	    area = X[8];
	    dragArea = area;
	    srpArea  = area;
	    mass = X[9];
	    RIC = new RSW_Frame(r,v);
	}
	
//	/**
//	 * @return hasControlLaw
//	 */
//	public boolean hasControlLaw(){
//	    return hasControlLaw;
//	}
//	/**
//	 * Sets the control law.
//	 * @param c Control law to be set.
//	 */
//	public void setControlLaw(ControlLaw c){
//	    control = c;
//	    hasControlLaw = true;
//	}
//	/**
//	 * Returns the control law
//	 * @return ControlLaw
//	 */
//	public ControlLaw getControlLaw(){
//	    if(hasControlLaw) return control;
//	    else return new ControlLaw(this);
//	}

	/**
	 * Get position.
	 */
	public VectorN r(){ return r;}
	/**
	 * Get velocity.
	 */
	public VectorN v(){ return v;}
	/**
	 * Get attitude.
	 */
	public Quaternion q(){ return q;}
	/**
	 * Get Coefficient of Reflectivity.
	 */
	public double cr(){ return CR;}
	/**
	 * Get Coefficient of Drag.
	 */
	public double cd(){ return cd;}
	/**
	 * Get spacecraft mass.
	 */
	public double mass(){ return mass;}
	/**
	 * Get spacecraft cross section. Should be deprecated
	 */
	public double area(){ return area;}
	/**
	 * Get spacecraft cross section for drag computation
	 */
	public double dragArea(){ return dragArea;}
	/**
	 * Get spacecraft cross section for solar radiation pressure computation
	 */
	public double srpArea(){ return srpArea;}
	
	/**
	 * Set Coefficient of Reflectivity.
	 */
	public void set_cr(double x){ CR=x;}
	/**
	 * Set Coefficient of Drag.
	 */
	public void set_cd(double x){ cd=x;}
	/**
	 * Set spacecraft cross section. Should be deprecated
	 */
	public void set_area(double x){ 
		area=x;
	    dragArea = area;
	    srpArea  = area;
		}
	/**
	 * Set spacecraft drag area
	 */
	public void set_dragArea(double x){ dragArea=x;}
	/**
	 * Set spacecraft srp area
	 */
	public void set_srpArea(double x){ 
		srpArea=x;
		}
	/**
	 * Set spacecraft mass.
	 */
	public void set_mass(double x){ mass=x;}
	
	public void set_RIC_frame(VectorN r_primary, VectorN v_primary){
	    RIC = new RSW_Frame(r_primary,v_primary);
	}
	
	/**
	 * Return the rotation matrix to the body frame.
	 * @return Direction Cosine Matrix
	 */
	public Matrix get_inertial2Body(){
        return q.quat2DCM();
	}
	/**
	 * Return the rotation matrix to the Radial-Intrack-Crosstrack frame.
	 * @return Direction Cosine Matrix
	 */
	public Matrix get_inertial2RIC(){
	    return RSW_Frame.ECI2RIC(this.r,this.v);
	}
	/**
	 * Increment spacecraft mass.
	 * @param deltam Change in mass.
	 */
	public void incrementMass(double deltam){
		mass = mass+deltam;
	}
	
	/**
	 * Update the spacecraft position and velocity.
	 * @param rr Position
	 * @param vv Velocity
	 */
	public void updateMotion(VectorN rr, VectorN vv){
		r = rr;
		v = vv;
	}
	/**
	 * Update the spacecraft position and velocity based on a state vector.
	 * @param x State vector [x y z xdot ydot zdot ... ]
	 */
	public void updateMotion(double[] x){
	    r = new VectorN(x[0],x[1],x[2]);
	    v = new VectorN(x[3],x[4],x[5]);
	}
	/**
	 * Update attitude.
	 * @param qq Attitude quaternion.
	 */
	public void updateAttitude(Quaternion qq){
	    q = qq;
	}
	/**
	 * Set whether to use parameter in the state vector.
	 * @param b Boolean Flag.
	 */
	public void set_use_params_in_state(boolean b){
	    this.use_params_in_state = b;
	}
	/**
	 * Convert spacecraft into a state vector.
	 * @return The state vector.
	 */
	public double[] toStateVector(){
	    double[] out = new double[6];
	    return toStateVector(this.use_params_in_state);
	}

//	/**
//	 * Convert spacecraft into a state vector in the Radial Intrack Crosstrack frame.
//	 * @return The state vector.
//	 */
//	public double[] toStateVectorRIC(){
//	    double[] out = new double[6];
//	    if(use_params_in_state){
//	        out = new double[10];
//	        out[0] = r.get(0);
//	        out[1] = r.get(1);
//	        out[2] = r.get(2);
//	        out[3] = v.get(0);
//	        out[4] = v.get(1);
//	        out[5] = v.get(2);
//	        out[6] = CR;
//	        out[7] = cd;
//	        out[8] = area;
//	        out[9] = mass;
//	    }else{
//	        out = new double[6];
//	        out[0] = r.get(0);
//	        out[1] = r.get(1);
//	        out[2] = r.get(2);
//	        out[3] = v.get(0);
//	        out[4] = v.get(1);
//	        out[5] = v.get(2);
//	    }
//	    return out;
//	}

	
	/**
	 * Convert spacecraft into a state vector. 
	 * This does not include new drag and srp areas
	 * @param use_params Flag indicating whether to append parameters.
	 * @return The state vector.
	 */
	public double[] toStateVector(boolean use_params){
	    double[] out = new double[6];
	    if(use_params){
	        out = new double[10];
	        out[0] = r.get(0);
	        out[1] = r.get(1);
	        out[2] = r.get(2);
	        out[3] = v.get(0);
	        out[4] = v.get(1);
	        out[5] = v.get(2);
	        out[6] = CR;
	        out[7] = cd;
	        out[8] = area;
	        out[9] = mass;        
	    }else{
	        out = new double[6];
	        out[0] = r.get(0);
	        out[1] = r.get(1);
	        out[2] = r.get(2);
	        out[3] = v.get(0);
	        out[4] = v.get(1);
	        out[5] = v.get(2);
	    }
	    return out;
	}
	
	public void updateState(double[] Xnew){
		updateState(Xnew,this.use_params_in_state);
	}
	/**
	 * Update the state given a state vector of the same format as "toStateVector()".
	 * This sets the drag and srp areas equal to area - they are not updated indpendently
	 * @param Xnew new state vector
	 */
	public void updateState(double[] Xnew, boolean use_params){
	    if(use_params){
	        r.set(0,Xnew[0]);
	        r.set(1,Xnew[1]);
	        r.set(2,Xnew[2]);
	        v.set(0,Xnew[3]);
	        v.set(1,Xnew[4]);
	        v.set(2,Xnew[5]);
	        CR = Xnew[6];
	        cd = Xnew[7];
	        area = Xnew[8];
		    dragArea = area;
		    srpArea  = area;	        
	        mass = Xnew[9];        
	    }else{
	    	r.set(0,Xnew[0]);
	        r.set(1,Xnew[1]);
	        r.set(2,Xnew[2]);
	        v.set(0,Xnew[3]);
	        v.set(1,Xnew[4]);
	        v.set(2,Xnew[5]);
	    }	    
	}
}
