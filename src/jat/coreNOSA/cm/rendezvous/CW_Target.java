package jat.coreNOSA.cm.rendezvous;

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
 * File Created on Aug 26, 2003
 */
 
import jat.coreNOSA.math.MatrixVector.data.VectorN;

import java.io.Serializable;

/**
 * <P>
 * The CW_Target Class represents a single guidance target.
 *
 * @author 
 * @version 1.0
 */

public class CW_Target implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8824206007429439739L;

	/** Epoch time of measurement in sim time (seconds) */
	public double t;
		
	/** target vector */
	public VectorN rtgt;
	
	
	/** Constructor
	 * @param tsim Time of the measurement in sim time (sec).
	 * @param tgt target position
	 */
	public CW_Target(double tsim, VectorN tgt) {
		this.t = tsim;
		this.rtgt = tgt;
	}
	

}
