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
 * File Created on Aug 29, 2003
 */

package jat.coreNOSA.trajectory;
	
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
 * The ChaserEOM Class implements the EquationsOfMotion interface. It provides
 * the equations of motion for a body in a JGM3 gravity field with Harris-Priester drag.
 * @author 
 * @version 1.0
 */

public class ChaserEOM implements EquationsOfMotion{
	private Trajectory traj;
    private HarrisPriester hp; // earth atmosphere model
	private double t_mjd0;
	private double mass;
	private double area;
	private double cd;
	private JGM3 jgm3 = new JGM3(12,12);
//	private CIRA_ExponentialDrag ced;
	
	public ChaserEOM(Trajectory t, double mjd0, double m, double a, double c){
		this.traj = t;
		this.t_mjd0 = mjd0;
		this.mass = m;
		this.area = a;
		this.cd = c;
		this.hp = new HarrisPriester(this.cd, this.area, this.mass);
//		this.ced = new CIRA_ExponentialDrag(this.cd, this.area, this.mass);
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
		q.unitize();

		
		double Mjd = this.t_mjd0 + t/86400.0;
        EarthRef ref = new EarthRef(Mjd);
        ref.setIERS(3.3E-07, 1.17E-06, 0.649232);
        Matrix E = ref.eci2ecef();

        // Acceleration due to harmonic gravity field        
        VectorN g = jgm3.gravity(r, E);
        
//        TwoBody orbit = new TwoBody(Constants.GM_Earth, r, v);
//        VectorN ggg = orbit.local_grav();
//        VectorN diff = g.minus(ggg);
//        
//        J2Gravity j2 = new J2Gravity(r);
//        VectorN gg = j2.local_gravity();
//        VectorN diff2 = g.minus(gg);
        
        Time time = new Time(Mjd);
        hp.compute(time,ref, r, v);
        VectorN drag = hp.dragAccel();
        VectorN accel = drag.plus(g);
        
        // drag check
//        ced.compute(ref, r, v);
//        VectorN cedrag = ced.dragAccel();
//        System.out.println("hp: "+drag.mag()+" ced: "+cedrag.mag());
		
		// compute attitude rate
		
		RSW_Frame rsw = new RSW_Frame(r, v);
		VectorN w = rsw.omega();
		Matrix omega_true = Quaternion.omega(w);
		VectorN q_deriv = omega_true.times(q);
		

		// derivatives for true orbit
		out.set(0, v);
		out.set(3, accel);
		out.set(6, q_deriv);
		
		// accelerometer state
//		Matrix rsw2eci = q.quat2DCM();
//		Matrix eci2rsw = rsw2eci.transpose();
		Matrix eci2rsw = rsw.ECI2RSW();
		VectorN df = eci2rsw.times(drag);
		out.set(10, df);
		

//		diff = eci2rsw.times(diff);
//		diff2 = eci2rsw.times(diff2);
		
//		System.out.println("df = "+df);
		
		// gyro state
		out.set(13, w);
		

		return out.x;
	}
	
	
	

}
