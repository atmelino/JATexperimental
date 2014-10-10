package jat.coreNOSA.trajectory;

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
 * File Created on Aug 26, 2003
 */
 
import jat.coreNOSA.algorithm.integrators.EquationsOfMotion;
import jat.coreNOSA.forces.HarrisPriester;
import jat.coreNOSA.forces.JGM3;
import jat.coreNOSA.math.MatrixVector.data.Matrix;
import jat.coreNOSA.math.MatrixVector.data.Quaternion;
import jat.coreNOSA.math.MatrixVector.data.VectorN;
import jat.coreNOSA.spacetime.Time;
import jat.coreNOSA.timeRef.EarthRef;
import jat.coreNOSA.timeRef.RSW_Frame;

/**
 * <P>
 * The JGM3DragEOM Class implements the EquationsOfMotion interface. It provides
 * the equations of motion for a body in a JGM3 gravity field with Harris-Priester drag
 * and finite duration thrust.
 * @author 
 * @version 1.0
 */
public class JGM3DragEOM implements EquationsOfMotion{
	private Trajectory traj;
    private HarrisPriester hp; // earth atmosphere model
	private double t_mjd0;
	private double mass;
	private double area;
	private double cd;
	private JGM3 jgm3 = new JGM3(12,12);
	
	public JGM3DragEOM(Trajectory t, double mjd0, double m, double a, double c){
		this.traj = t;
		this.t_mjd0 = mjd0;
		this.mass = m;
		this.area = a;
		this.cd = c;
		this.hp = new HarrisPriester(this.cd, this.area, this.mass);
	}
	
	/** Implements the Printable interface to get the data out of the propagator and pass it to the trajectory.
	 *  This method is executed by the propagator at each integration step.
	 * @param t Time.
	 * @param y Data array.
	 */
	public void print(double t, double[] y) {
		traj.add(t, y);
		
	}
	
	/** Computes the derivatives for integration.
	 * @param t Time (not used).
	 * @param y State array.
	 * @return Array containing the derivatives.
	 */
	public double[] derivs(double t, double[] y) {

		VectorN out = new VectorN(y.length);

		// strip out incoming data    
		VectorN r = new VectorN(y[0], y[1], y[2]);
		VectorN v = new VectorN(y[3], y[4], y[5]);
		Quaternion q = new Quaternion(y[6], y[7], y[8], y[9]);

//		// construct the true orbit
//		TwoBody true_orbit = new TwoBody(Constants.GM_Earth, r, v);
//
//		// compute local gravity
//		VectorN g = true_orbit.local_grav();
		
		double Mjd = this.t_mjd0 + t/86400.0;
        EarthRef ref = new EarthRef(Mjd);
        ref.setIERS(3.3E-07, 1.17E-06, 0.649232);
        Matrix E = ref.eci2ecef();

        // Acceleration due to harmonic gravity field        
        VectorN g = jgm3.gravity(r, E);

        Time time = new Time(Mjd);
        hp.compute(time,ref, r, v);
        VectorN drag = hp.dragAccel();
        VectorN accel = drag.plus(g);
		
		// compute attitude rate
		
		RSW_Frame rsw = new RSW_Frame(r, v);
		VectorN w = rsw.omega();
		Matrix omega_true = Quaternion.omega(w);
		VectorN q_deriv = omega_true.times(q);

		// derivatives for true orbit
		out.set(0, v);
		out.set(3, accel);
		out.set(6, q_deriv);

		return out.x;
	}
	
	

}
