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

//import jat.matvec.data.Matrix;
//import jat.timeRef.Time;
import jat.coreNOSA.cm.Constants;
import jat.coreNOSA.ephemeris.DE405;
import jat.coreNOSA.ephemeris.DE405_Body;
import jat.coreNOSA.math.MatrixVector.data.VectorN;
import jat.coreNOSA.spacecraft.Spacecraft;
import jat.coreNOSA.spacetime.BodyCenteredInertialRef;
import jat.coreNOSA.spacetime.BodyRef;
import jat.coreNOSA.spacetime.ReferenceFrame;
import jat.coreNOSA.spacetime.ReferenceFrameTranslater;
import jat.coreNOSA.spacetime.Time;
import jat.coreNOSA.timeRef.EarthRef;
import jat.coreNOSA.util.FileUtil;

/**
 * Simple class to obtain the gravitational effect of the Sun.
 * 
 * @author Richard C. Page III
 *
 */
public class Sun extends GravitationalBody {

    protected DE405 jpl_ephemeris;
    
    /** The reference frame in which the Sun computes its forces.
     * It is a sun-centered J2000 inertial reference frame. */
    private ReferenceFrame sunRef = new BodyCenteredInertialRef(DE405_Body.SUN);
    
    /**
     * Default constructor. 
     */
    public Sun() {
        super();
        this.mu = Constants.GM_Sun;
    }

    /**
     * Constructor.
     * @param grav_mass [m^3/s^2]
     * @param r position [m]
     * @param v velocity [m/s]
     */
    public Sun(double grav_mass, VectorN r, VectorN v) {
        super(grav_mass, r, v);
        String filesep = FileUtil.file_separator();
        String directory = FileUtil.getClassFilePath("jat.eph","DE405");
        directory = directory+filesep+"DE405data"+filesep;
        //jpl_ephemeris = new DE405(directory);
    }

    /**
     * Compute the acceleration.
     * @param eRef Earth Reference
     * @param sc Spacecraft Parameters and state
     */
    public VectorN acceleration(EarthRef eRef, Spacecraft sc){

      ReferenceFrameTranslater xlater = 
        new ReferenceFrameTranslater(eRef, sunRef, new Time(eRef.mjd_utc()));

      // Get a vector (in the passed in reference frame) to the
      // spacecraft and to the sun.
      VectorN r_sun = xlater.translatePointBack(new VectorN(3));
      VectorN r = sc.r();
      VectorN d = r.minus(r_sun);
      
      
      // Divide the sun vector and the sunn-to-spacecraft vector
      // by their magnitude cubed, add them, and scale by mu.
      double dmag = d.mag();
      double dcubed = dmag * dmag *dmag;
      VectorN temp1 = d.divide(dcubed);
      double smag = r_sun.mag();
      double scubed = smag * smag * smag;
      VectorN temp2 = r_sun.divide(scubed);
      VectorN sum = temp1.plus(temp2);
      VectorN accel = sum.times(-mu);
        
      return  accel;
    }
    /** Call the relevant methods to compute the acceleration.
	 * @param t Time reference class
     * @param bRef Body reference class
     * @param sc Spacecraft parameters and state
     * @return the acceleration [m/s^s]
     */
    public VectorN acceleration(Time t, BodyRef bRef, Spacecraft sc){

      ReferenceFrameTranslater xlater = 
        new ReferenceFrameTranslater(bRef, sunRef, t);

      // Get a vector (in the passed in reference frame) to the
      // spacecraft and to the sun.
      VectorN r_sun = xlater.translatePointBack(new VectorN(3));
      VectorN r = sc.r();
      VectorN d = r.minus(r_sun);
      
      
      // Divide the sun vector and the sunn-to-spacecraft vector
      // by their magnitude cubed, add them, and scale by mu.
      double dmag = d.mag();
      double dcubed = dmag * dmag *dmag;
      VectorN temp1 = d.divide(dcubed);
      double smag = r_sun.mag();
      double scubed = smag * smag * smag;
      VectorN temp2 = r_sun.divide(scubed);
      VectorN sum = temp1.plus(temp2);
      VectorN accel = sum.times(-mu);
        
      return  accel;
    }    
    
	public static void main(String[] args) throws java.io.IOException {
        String filesep = FileUtil.file_separator();
        String directory = FileUtil.getClassFilePath("jat.eph","DE405");
        directory = directory+filesep+"DE405data"+filesep;
        DE405 jpl_ephemeris = new DE405(directory);
        double mjd_tt = 53157.5;
        VectorN r_sun = new VectorN(jpl_ephemeris.get_planet_pos(DE405_Body.SUN,mjd_tt));
        VectorN r_earth = new VectorN(jpl_ephemeris.get_planet_pos(DE405_Body.EARTH,mjd_tt));
        VectorN r_body = r_sun.minus(r_earth);

	    r_body.print("r");
	    r_sun.print("r_sun ");
        

	}
    
}
