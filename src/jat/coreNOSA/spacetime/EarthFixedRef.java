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
 * National Aeronautics and Space Administration
 * File created by Richard C. Page III 
 **/
package jat.coreNOSA.spacetime;

import jat.coreNOSA.ephemeris.DE405;
import jat.coreNOSA.ephemeris.DE405_Body;
import jat.coreNOSA.math.MatrixVector.data.Matrix;
import jat.coreNOSA.math.MatrixVector.data.VectorN;

/**
 * Represents the Earth-Centered Body-Fixed Reference Frame.
 * 
 * @author Rob Antonucci
 */
public class EarthFixedRef implements ReferenceFrame {

	/**
	 * Construct a ECF reference frame.
	 */
	public EarthFixedRef()
	{
		// Does nothing.
	}

	/**
	 * Compute the ECI to ECF transformation matrix.
	 */
	private Matrix computeECI2ECF(Time t) {
		// EarthRef already does this.
		EarthRef ref = new EarthRef(t);
		return ref.ECI2ECEF();
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
		ReferenceFrameTranslater xlater = null;
		if (other instanceof EarthFixedRef) {
			// Same reference frame.  No translation needed.
			xlater = new ReferenceFrameTranslater();
		}
		else if (other instanceof BodyCenteredInertialRef) {
			xlater = getTranslater((BodyCenteredInertialRef)other, t);
		}
		return xlater;
	}

	/**
	 * Returns a translater to translate to ECI or LCI or any
	 * other something-CI.
	 * @param inertialRef an inertial reference frame
	 * @param t time at which translation will be done
	 * @return translater object
	 */
	private ReferenceFrameTranslater 
	getTranslater(BodyCenteredInertialRef inertialRef, Time t)
	{
		// We determine the transformation matrix from ECF to ECI.
		// This can be used for transformation to any body-centered
		// inertial frame.
		Matrix eci2ecf = computeECI2ECF(t);
		Matrix xform = eci2ecf.transpose();

		// Determine the position of the other body relative to the Earth.
		// Then transform it to the ECF reference frame.
		DE405 jpl_ephem = new DE405();
		VectorN state1 = new VectorN(jpl_ephem.get_planet_posvel(DE405_Body.EARTH, t.mjd_tt()));
		VectorN state2 = new VectorN(6);
		if (!inertialRef.getBody().equals(DE405_Body.SOLAR_SYSTEM_BARY)){ // used to be EARTH
			state2 = jpl_ephem.get_planet_posvel(inertialRef.getBody(), t.mjd_tt());
		}

		// We difference and convert to meters (JPL reports kilometers)
		VectorN originDiff = state2.get(0, 3).minus(state1.get(0, 3)).times(1000);
		VectorN bodyPos = eci2ecf.times(originDiff);
		VectorN originVel = state2.get(3, 3).minus(state1.get(3, 3)).times(1000);
		VectorN bodyVel = eci2ecf.times(originVel);
		// We use -omega because we want the rotation of inertial frame with respect to
		// the fixed frame.
		double[] rotation = {0, 0, -EarthRef.omega_e};
		ReferenceFrameTranslater xlater =
			new ReferenceFrameTranslater(xform, bodyPos, bodyVel, new VectorN(rotation));

		return xlater;
	}
}
