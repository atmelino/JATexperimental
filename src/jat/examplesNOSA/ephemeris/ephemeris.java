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
 /*
 *  File        :   ephemeris.java
 *  Author      :   Tobias Berthold
 *  Date        :   10-9-2002
 *  Change      :
 *  Description :   main program, JAT ephemeris demo
 */

package jat.examplesNOSA.ephemeris;

import jat.core.util.PathUtil;
import jat.coreNOSA.ephemeris.DE405;
import jat.coreNOSA.ephemeris.DE405_Body;
import jat.coreNOSA.math.MatrixVector.data.VectorN;
import jat.coreNOSA.spacetime.Time;

/**
 * @author Tobias Berthold
 * @version 1.0
 */
public class ephemeris
{
    public static void main (String argv[])
    {
        //double jd=cm.juliandate(2002, 2, 17, 12, 0, 0);
        Time mytime=new Time(2002, 2, 17, 12, 0, 0);
        PathUtil f=new PathUtil();
		String fs = f.fs;
        System.out.println(f.root_path);
        String DE405_data_folder=f.root_path+"data"+fs+"core"+fs+"ephemeris"+fs+"DE405data"+fs;
        System.out.println(DE405_data_folder);
		DE405 my_eph = new DE405(DE405_data_folder);
        VectorN rv=my_eph.get_planet_posvel(DE405_Body.MARS, mytime);

        System.out.println("The position of Mars on 10-17-2002 at 12:00pm was ");
        System.out.println("x= "+rv.get(0)+" km");
        System.out.println("y= "+rv.get(1)+" km");
        System.out.println("z= "+rv.get(2)+" km");


    }
}

