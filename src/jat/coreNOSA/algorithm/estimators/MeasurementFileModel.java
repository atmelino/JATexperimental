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
package jat.coreNOSA.algorithm.estimators;

import jat.coreNOSA.math.MatrixVector.data.VectorN;
import jat.coreNOSA.measurements.ObservationMeasurement;

public interface MeasurementFileModel {

	/**
	 * Returns the H matrix
	 * @param xref VectorN containing the current state
	 * @return H matrix (measurement state relation)
	 */
	public VectorN H (ObservationMeasurement obs, VectorN xref);

	/**
	 * Returns the measurement noise value
	 * @return measurement noise (sigma^2)
	 */
	public double R (ObservationMeasurement obs);

	/**
	 * Returns the predicted measurement based on the current state
	 * @param index measurement index
	 * @param t time of the measurement
	 * @param xref VectorN with the current state at the measurement time
	 */
	public double zPred (ObservationMeasurement obs, int index, double t, VectorN xref);

	/**
	 * Checks for remaining measurements. 
	 * True = more measurements left to be processed.
	 * @param index measurement index.
	 */

}
