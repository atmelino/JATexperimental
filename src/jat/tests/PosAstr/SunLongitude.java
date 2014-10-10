/* JAT: Java Astrodynamics Toolkit
 *
 * Copyright (c) 2002 National Aeronautics and Space Administration and the Center for Space Research (CSR),
 * The University of Texas at Austin. All rights reserved.
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

package jat.tests.PosAstr;

import jat.coreNOSA.astronomy.Angle;

public class SunLongitude
{
	public static void main(String[] args)
	{
		boolean plus = true, minus = false;
		Angle L, LT2, tau, tauT2, coa;

		System.out.println("Newcombs Sun");
		L = new Angle(plus, 279, 41, 48.04, Angle.ARCDEGREES);
		tau = new Angle(plus, 18, 38, 45.836, Angle.HOURANGLE);
		coa = new Angle(L.radians - tau.radians, Angle.RADIANS);
		LT2 = new Angle(plus, 0, 0, 1.089, Angle.ARCDEGREES);
		tauT2 = new Angle(plus, 0, 0, 0.00929, Angle.HOURANGLE);

		System.out.println("Constant Term");
		L.println("L    ", Angle.ARCDEGREES);
		tau.println("tau  ", Angle.ARCDEGREES);
		tau.println("tau  ", Angle.HOURANGLE);
		coa.println("Difference ", Angle.ARCDEGREES);
		System.out.println("");

		//		L.println("L  ",Angle.HOURANGLE);
		//		tau.println("tau",Angle.HOURANGLE);
		//		System.out.println("Quadratic Term");
		//		LT2.println("L T^2 ",Angle.ARCDEGREES);
		//		LT2.println("L T^2 ",Angle.HOURANGLE);
		//		tauT2.println("tau T^2",Angle.HOURANGLE);
	}
}
