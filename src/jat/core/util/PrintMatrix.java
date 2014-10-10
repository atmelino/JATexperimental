/* JAT: Java Astrodynamics Toolkit
 * 
  Copyright 2012 Tobias Berthold

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package jat.core.util;

import jat.coreNOSA.math.MatrixVector.data.Matrix;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Prints a matrix to the screen, nicely formatted so that columns line up.
 * @author Tobias Berthold
 */
public class PrintMatrix
{
	Matrix M;

	enum mode
	{
		FIXED, VARIABLE
	};

	enum decexp
	{
		DECIMAL, EXPONENTIAL
	};

	DecimalFormat df;
	NumberFormat itf;
	private int MinFracDig = 10;
	private int MinIntDig = 8;
	private int[] fractionDigits;
	private int[] integerDigits;
	public String[] titles ;

	public PrintMatrix(Matrix M)
	{
		this.M = M;
		//default values
		int cols = M.getColumnDimension();
		integerDigits=new int[cols];
		fractionDigits=new int[cols];
		titles=new String[cols];		
		for(int i=0;i<cols;i++)
		{
			integerDigits[i]=MinIntDig;
			fractionDigits[i]=MinFracDig;
			titles[i]="Title";
		}
	}

	public void setMinIntDig(int minIntDig)
	{
		MinIntDig = minIntDig;
	}

	public void setMinFracDig(int minFracDig)
	{
		MinFracDig = minFracDig;
		int cols = M.getColumnDimension();
		for(int i=0;i<cols;i++)
		{
			fractionDigits[i]=MinFracDig;
		}
	}

	private void set_print_formats()
	{
		df = (DecimalFormat) NumberFormat.getInstance();
		df.applyPattern("  ###.########; -###.#######");
		// df.applyPattern("####0.000000; -###0.000000");
		// df.setMinimumFractionDigits(MinFracDig);
		df.setMinimumIntegerDigits(5);
		itf = NumberFormat.getInstance();
		itf.setMinimumIntegerDigits(4);
	}

	public void print()
	{ // i:rows j:columns
		// Column Titles
		for (int j = 0; j < M.n; j++)
		{
			int integerwidth= integerDigits[j];
			int fractionwidth= fractionDigits[j];
			int width=integerwidth+fractionwidth+3;
			String s = titles[j] + "                              ";
			String t = s.substring(0,width);
			System.out.print(t);
		}
		System.out.println("");
		// Data
		set_print_formats();
		// System.out.println(M.m + " X " + M.n + " Matrix:");
		for (int i = 0; i < M.m; i++)
		{
			for (int j = 0; j < M.n; j++)
			{
				df.setMinimumIntegerDigits(integerDigits[j]);
				df.setMinimumFractionDigits(fractionDigits[j]);
				// System.out.print("\t");
				System.out.print(df.format(M.A[i][j]));
			}
			System.out.println("");
		}
	}

	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
	}


}
