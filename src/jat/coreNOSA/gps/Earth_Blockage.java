package jat.coreNOSA.gps;

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
 * File Created on Jul 13, 2003
 */
 
import jat.coreNOSA.math.MatrixVector.data.VectorN;

/**
 * <P>
 * The Earth_Blockage Class provides a model of GPS signal blockage due to 
 * a spherical earth.
 *
 * @author 
 * @version 1.0
 */

public class Earth_Blockage implements Visible {
	
	private double elevationMask;
	private static final double earthRadius = 6478140.0;
	
	
	
    /**
     * Determine if the GPS satellite is visible, including earth blockage
     * Used by GPS Measurement Generator.
     * @param losu Line of sight unit vector
     * @param r    current position vector
     * @param rISS current position vector of the ISS
     * @return boolean true if the GPS satellite is visible
     */
	public boolean visible(VectorN losu, VectorN r, VectorN rISS) {
		
		// check elevation mask
		boolean visible = true;
		
		// check ISS visibility
		double dist = r.mag();
		double cone = Math.atan2(earthRadius, dist);
		VectorN r_unit = r.unitVector().times(-1.0);
		double cos_delta = r_unit.dotProduct(losu);
		double delta = Math.acos(cos_delta);
		if (delta < cone) {
			visible = false;
		}
		
		return visible;
	}	

}
