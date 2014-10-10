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
* The CoordinateSystem.java Class provides the means for specifying the 
* coordinate system used in creating a trajectory.
*
* @author 
* @version 1.0
*/ 

public final class CoordinateSystem implements Serializable {

  /**
	 * 
	 */
	private static final long serialVersionUID = -2568071532784654133L;


private String name;

  private CoordinateSystem(String nm) { name = nm; }

  public String toString() { return name; }

  public final static CoordinateSystem
    INERTIAL = new CoordinateSystem("Inertial"),
    PLANETFIXED = new CoordinateSystem("PlanetFixed"),
    LLH = new CoordinateSystem("LLH"),
    OTHER = new CoordinateSystem("Other");


  public final static CoordinateSystem[] index =  {
    INERTIAL, PLANETFIXED, LLH, OTHER
  };


  public static void main(String[] args) {
    CoordinateSystem m = CoordinateSystem.INERTIAL;
    System.out.println(m);
    m = CoordinateSystem.index[1];
    System.out.println(m);
    System.out.println(m == CoordinateSystem.PLANETFIXED);
    System.out.println(m.equals(CoordinateSystem.INERTIAL));
  }
}