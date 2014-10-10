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
import java.util.ArrayList;

/**
* <P>
* The TrajectoryList.java Class provides a typesafe list for trajectory data.
* The trajectory data is stored in a double[] for each time (or row of data).
*
* @author 
* @version 1.0
*/

public class TrajectoryList implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1622362061452168406L;
	private ArrayList list = new ArrayList();
	private int index = 0;
	
	public void add (double[] row){
		list.add(row);
	}
	
	public void add (double t, double[] data){
		int n = data.length;
		double [] temp = new double[n+1];
		temp[0] = t;
		for(int i = 0; i < n; i++) {
			temp[i+1] = data[i];
		}
		this.add(temp);
	}
	
	public void add (double t, double[] y1, double[] y2){
		int n = y1.length;
		int m = y2.length;
		double [] temp = new double[n+m+1];
		temp[0] = t;
		for(int i = 0; i < n; i++) {
			temp[i+1] = y1[i];
		}
		for(int j = 0; j < m; j++) {
			temp[j+n+1] = y2[j];
		}
		this.add(temp);
	}	
	
	public double[] get (int i){
		return (double[]) list.get(i);
	}

	public double[] next(){
		double[] out = this.get(index);
		index++;
		return out;
	}
	
	public boolean hasNext(){
		if (this.index < this.size()){
			return true;
		}
		else {
			return false;
		}
	}	
	
	public int size(){
		return list.size();
	}

	public void reset() {
		this.index = 0;
	}
	

}
