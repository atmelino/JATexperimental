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

package jat.tests.core.attitude;

import jat.coreNOSA.attitude.QuatToDeg;




/**
 * <P>
 * QuatToDegTester demonstrate the use of the QuatToDeg class
 * via both applet and application.
 *
 * @author Noriko Takada
 * @version 1.2
 * Modification Record
 * 		1. Removed: import jat.attitude
 */

public class QuatToDegSimpleTester
{

    public static void main(String[] args)
    {
     	double q1 = 0.0;
     	double q2 = 0.0;
     	double q3 = 0.0;
     	double q4 = 1.0;
     	double theta = 10.0;
     	double psi = 20.0;
     	double phi = 30.0;
    
    	q1 = 0.7254;
        q2 = 0.4691;
        q3 = 0.1837;
        q4 = 0.4691;
    	QuatToDeg tester = new QuatToDeg(q1, q2, q3, q4);
    	
    	double[] angle = new double[3];
    	angle = tester.calculateAngle();
    	theta = angle[0];
    	psi = angle[1];
    	phi = angle[2];

        System.out.println("\nInput: ["+q1+", "+q2+", "+q3+", "+q4+"]");
        System.out.println("\nOutput: theta="+theta+" psi="+psi+" phi="+phi);
        
    }

}// End of this file: QuatToDegTester
