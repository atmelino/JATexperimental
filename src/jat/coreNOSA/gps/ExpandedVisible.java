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
 * The Visible interface is used to provide a means for GPS SV visibility checking. 
 * @author 
 * @version 1.0
 */
public interface ExpandedVisible extends Visible{
	/**
	 * check to see if the GPS SV is visible
	 * @param losu GPS SV line of sight unit vector
	 * @param r receiver position vector
	 * @param rISS ISS position vector
	 * @return boolean true = visible, false = blocked
	 */
	public boolean visible(VectorN losu, VectorN r, VectorN v, VectorN rGPS, VectorN vGPS, int prn, double mjd);
}
