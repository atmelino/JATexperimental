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


package jat.tests.core.math;


public class interpolator_test
{

    public static void main(String[] args)
    {
        // Test interpolator
        double[] data={1.,2.,3.,5.};
        double[] vari={1.,2.,3.,4.};
        double test_x;
        jat.coreNOSA.math.Interpolator in1=new jat.coreNOSA.math.Interpolator(vari,data);

        // too low: expect error
        test_x=in1.get_value(0.8);
        System.out.println("Interpolated value: "+test_x);

        // on the lower boundary: expect 1.0
        test_x=in1.get_value(1.0);
        System.out.println("Interpolated value: "+test_x);

        // Expect 2.3
        test_x=in1.get_value(2.3);
        System.out.println("Interpolated value: "+test_x);

        // on the upper boundary: Expect 5.0
        test_x=in1.get_value(4.0);
        System.out.println("Interpolated value: "+test_x);

        // too high: expect error
        test_x=in1.get_value(4.5);
        System.out.println("Interpolated value: "+test_x);
    }
}
