/* JAT: Java Astrodynamics Toolkit
 *
 * Copyright (c) 2002 National Aeronautics and Space Administration. All rights reserved.
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
import jat.coreNOSA.math.MatrixVector.data.VectorN;
import jat.coreNOSA.spacecraft.Spacecraft;
import jat.coreNOSA.spacetime.BodyRef;
import jat.coreNOSA.spacetime.Time;
import jat.coreNOSA.timeRef.EarthRef;

/**
 * <P>
 * The ExponentialDrag class computes the acceleration due to drag on a satellite
 * using an exponential Earth atmosphere model.
 *
 * @author 
 * @version 1.0
 */

public class ExponentialDrag extends AtmosphericDrag{
    private static final double re = 6378136.3;              // radius of earth in meters
    private static final double h_0 = 920000.0;              // atmosphere model parameter
    private static final double rho_0 = 4.36E-14;            // atmosphere model parameter
    private static final double gamma_0 = 5.381E-06;         // atmosphere model parameter

	/**
	 * Constructor
	 * @param cd coefficient of drag
	 * @param area drag cross-sectional area
	 * @param mass mass
	 */	
    public ExponentialDrag (double cd, double area, double mass){
    	super(cd, area, mass);
    }
    /**
     * Constructor 
	 * @param sc Spacecraft parameters
	 */
	public ExponentialDrag(Spacecraft sc) {
		super(sc);
	}

    /** Compute the atmospheric density using an exponential atmosphere model.
     * @param ref EarthRef object. Not used.
     * @param r ECI position vector in meters.
     * @return Atmospheric density in kg/m^3.
     */
    public double computeDensity(EarthRef ref, VectorN r){
        r.checkVectorDimensions(3);
        double rmag = r.mag();
        double r0 = re + h_0;
        double exp = -gamma_0*(rmag - r0);
        double rho = rho_0 * Math.exp(exp);
        return rho;
    }
    
    /** Compute the atmospheric density using an exponential atmosphere model.
     * @param t Time reference object. Not used.
     * @param ref EarthRef object. Not used.
     * @param r ECI position vector in meters.
     * @return Atmospheric density in kg/m^3.
     */
    public double computeDensity(Time t, BodyRef ref, VectorN r) {
        r.checkVectorDimensions(3);
        double rmag = r.mag();
        double r0 = re + h_0;
        double exp = -gamma_0*(rmag - r0);
        double rho = rho_0 * Math.exp(exp);
        return rho;
    }    
}
