/*
 * JAT: Java Astrodynamics Toolkit
 * 
 * Created on Mar 1, 2006
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
package jat.coreNOSA.spacetime;

import jat.coreNOSA.math.MatrixVector.data.Matrix;
import jat.coreNOSA.math.MatrixVector.data.VectorN;

/**
 * TODO Javadoc
 * @author Richard C. Page III
 */
public class SolarBarycenterRef implements BodyRef {

	/* (non-Javadoc)
	 * @see jat.spacetime.BodyRef#get_spin_rate(jat.spacetime.Time)
	 */
	public double get_spin_rate(Time t) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see jat.spacetime.BodyRef#get_mean_radius()
	 */
	public double get_mean_radius() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see jat.spacetime.BodyRef#get_grav_const()
	 */
	public double get_grav_const() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see jat.spacetime.BodyRef#inertial_to_body(jat.spacetime.Time)
	 */
	public Matrix inertial_to_body(Time t) {
		// TODO Auto-generated method stub
		return new Matrix(3);
	}

	/* (non-Javadoc)
	 * @see jat.spacetime.BodyRef#body_to_inertial(jat.spacetime.Time)
	 */
	public Matrix body_to_inertial(Time t) {
		// TODO Auto-generated method stub
		return new Matrix(3);
	}

	/* (non-Javadoc)
	 * @see jat.spacetime.BodyRef#trueOfDate(jat.spacetime.Time)
	 */
	public Matrix trueOfDate(Time t) {
		// TODO Auto-generated method stub
		return new Matrix(3);
	}

	/* (non-Javadoc)
	 * @see jat.spacetime.BodyRef#get_JPL_Sun_Vector()
	 */
	public VectorN get_JPL_Sun_Vector(Time t) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see jat.spacetime.BodyRef#get_JPL_Moon_Vector()
	 */
	public VectorN get_JPL_Moon_Vector() {
		// TODO Auto-generated method stub
		return null;
	}
    
    /**
     * Returns a translater to translate into other reference frames.
     * @param other another reference frame
     * @param t time at which translation will be done
     * @return translater object or null if does not know how
     * to translate
     */
    public ReferenceFrameTranslater getTranslater(ReferenceFrame other, Time t)
    {
      // Currently does not translate anything (but itself).
      return (other instanceof SolarBarycenterRef ?
          new ReferenceFrameTranslater() : null);
    }


	public static void main(String[] args) {
	}
}
