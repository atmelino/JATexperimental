/* JAT: Java Astrodynamics Toolkit
*
* Copyright (c) 2005 National Aeronautics and Space Administration. All rights reserved.
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
package jat.coreNOSA.forces;

/**
 * @author
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GravityModelType {
	
	private String name;
	  private GravityModelType (String nm) { name = nm; }

	  public String toString() { return name; }


	  public final static GravityModelType
	    EGM96 = new GravityModelType("EGM96"),
	    GEMT1 = new GravityModelType("GEMT1"),
	    JGM2 = new GravityModelType("JGM2"),
	    JGM3 = new GravityModelType("JGM3"),
	    WGS84 = new GravityModelType("WGS84"),
	    WGS84_EGM96 = new GravityModelType("WGS84_EGM96");

	  public final static GravityModelType[] index =  {
	  		EGM96, GEMT1, JGM2, JGM3, WGS84, WGS84_EGM96
	  };
	  
	  public final static int num_models = 6;
}
