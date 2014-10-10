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
/**
 * Interface representing the primary or central spacecraft of a formation.
 * 
 * @author Richard C. Page III
 *
 */
public interface PrimarySpacecraft {

    /**
     * Get absolute position
     * @return position [m]
     */
    public VectorN get_abs_pos();
    /**
     * Get absolute velocity
     * @return velocity [m/s]
     */
    public VectorN get_abs_vel();
    /**
     * Get attitude
     * @return quaternion
     */
    public Quaternion get_attitude();
    
    /**
     * Get the rotation matrix from inertial to radial-intrack-crosstrack
     * @return Transformation Matrix
     */
    //* Get the local Radial/In-track/Cross-track frame
    public Matrix get_inertial2RIC();
    /**
     * Get the vector angular rotation of the radial-intrack-crosstrack frame
     * @return omega vector
     */
    //* Get the rotation rate of the RIC frame
    public VectorN get_omegaRIC();
    
}
