package jat.coreNOSA.trajectory;



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
 */
 
 import java.io.Serializable;
 
/**
* <P>
* The CentralBody.java Class provides the means for specifying the 
* central body used in creating a trajectory.
*
* @author 
* @version 1.0
*/
public final class CentralBody implements Serializable {

  /**
	 * 
	 */
	private static final long serialVersionUID = -3323167295069025124L;


private String name;

  private CentralBody(String nm) { name = nm; }

  public String toString() { return name; }

  public final static CentralBody
    EARTH = new CentralBody("Earth"),
    SUN = new CentralBody("Sun"),
    MERCURY = new CentralBody("Mercury"),
    VENUS = new CentralBody("Venus"),
    MARS = new CentralBody("Mars"),
    JUPITER = new CentralBody("Jupiter"),
    SATURN = new CentralBody("Saturn"),
    URANUS = new CentralBody("Uranus"),
    NEPTUNE = new CentralBody("Neptune"),
    PLUTO = new CentralBody("Pluto"),
    MOON = new CentralBody("Moon"),
    OTHER = new CentralBody("Other");


  public final static CentralBody[] index =  {
    EARTH, SUN, MERCURY, VENUS, MARS, JUPITER, SATURN,
    URANUS, NEPTUNE, PLUTO, MOON, OTHER
  };


  public static void main(String[] args) {
    CentralBody m = CentralBody.EARTH;
    System.out.println(m);
    m = CentralBody.index[4];
    System.out.println(m);
    System.out.println(m == CentralBody.MARS);
    System.out.println(m.equals(CentralBody.JUPITER));
  }
}