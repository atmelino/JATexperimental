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

import jat.coreNOSA.math.MatrixVector.data.VectorN;
import jat.coreNOSA.spacecraft.Spacecraft;
import jat.coreNOSA.timeRef.EarthRef;

/**
 * This interface allows for uniformity among extensible and newly added
 * force models.
 * 
 * @author Richard C. Page III
 *
 */
public interface EarthForceModel {

	/** Acceleration
     * @param eRef Earth reference class
     * @param sc Spacecraft parameters and state
     * @return the acceleration [m/s^2]
     */
    public VectorN acceleration(EarthRef eRef, Spacecraft sc);	
	
}
