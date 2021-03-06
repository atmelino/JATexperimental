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
* The TimeUnits.java Class provides the means for specifying the 
* time units used in creating a trajectory.
*
* @author 
* @version 1.0
*/

public final class TimeUnits implements Serializable {

  /**
	 * 
	 */
	private static final long serialVersionUID = -6348915627577412589L;


private String name;

  private TimeUnits(String nm) { name = nm; }

  public String toString() { return name; }

  public final static TimeUnits
    SECONDS = new TimeUnits("s"),
    DAYS = new TimeUnits("days"),
    OTHER = new TimeUnits("Other");


  public final static TimeUnits[] index =  {
    SECONDS, DAYS, OTHER
  };


  public static void main(String[] args) {
    TimeUnits m = TimeUnits.SECONDS;
    System.out.println(m);
    m = TimeUnits.index[1];
    System.out.println(m);
    System.out.println(m == TimeUnits.SECONDS);
    System.out.println(m.equals(TimeUnits.DAYS));
  }
}