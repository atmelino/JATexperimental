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

package jat.coreNOSA.algorithm;
import jat.coreNOSA.math.MatrixVector.data.VectorN;

/**
 * The VectorTimeFunction interface provides the mechanism for passing a method
 * that evaluates a function to a solver.
 *
 * @author Tobias Berthold
 * @version 1.0
 */
public interface VectorTimeFunction
{
	/**
	 * @param x VectorN containing the required data.
	 * @param t separate data, typically time
	 * @return VectorN containing the result of the function.
	 */
	public VectorN evaluate(VectorN x, double t);
}
