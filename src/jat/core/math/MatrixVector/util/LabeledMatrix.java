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

package jat.core.math.MatrixVector.util;

import jat.coreNOSA.math.MatrixVector.data.Matrix;
import jat.coreNOSA.math.MatrixVector.data.VectorN;

public class LabeledMatrix extends Matrix {
	private static final long serialVersionUID = -2882712126083407423L;
	public String cornerlabel;
	public String[] RowLabels;
	public String[] ColumnLabels;
	public String rowlabelformat;
	public String collabelformat;

	public LabeledMatrix(int m, int n) {
		super(m, n);
		defaultLabels();
	}

	public LabeledMatrix(int n) {
		super(n);
		defaultLabels();
	}

	public LabeledMatrix(Matrix in) {
		super(in);
		// TODO Auto-generated constructor stub
	}

	public LabeledMatrix(int m, int n, double s) {
		super(m, n, s);
		// TODO Auto-generated constructor stub
	}

	public LabeledMatrix(VectorN X) {
		super(X);
		// TODO Auto-generated constructor stub
	}

	public LabeledMatrix(double[][] B) {
		super(B);
		// TODO Auto-generated constructor stub
	}

	public LabeledMatrix(double[][] B, int m, int n) {
		super(B, m, n);
		// TODO Auto-generated constructor stub
	}

	public LabeledMatrix(double[] vals, int m) {
		super(vals, m);
		// TODO Auto-generated constructor stub
	}

	private void defaultLabels() {
		RowLabels = new String[m];
		ColumnLabels = new String[n];
		cornerlabel = "r/c";
		for (int i = 0; i < m; i++)
			RowLabels[i] = "" + i;
		for (int j = 0; j < n; j++)
			ColumnLabels[j] = "" + j;
	}

	public void print() {

		int width = 9;
		int precision = 2;
		String numberformat = "%" + width + "." + precision + "f";
		rowlabelformat = "%" + width + "s";
		collabelformat = "%" + width + "s";
		System.out.println(this.m + " X " + this.n + " Matrix:");

		System.out.printf(collabelformat, cornerlabel);
		for (int j = 0; j < this.n; j++) {
			System.out.printf(collabelformat, ColumnLabels[j]);
		}
		System.out.println();
		for (int i = 0; i < this.m; i++) {
			System.out.printf(rowlabelformat, RowLabels[i]);
			for (int j = 0; j < this.n; j++) {
				System.out.printf(numberformat, this.A[i][j]);
			}

			System.out.println();
		}
		System.out.println();

	}

	public static void main(String args[]) {

		int rows = 6;
		int cols = 4;
		LabeledMatrix A = new LabeledMatrix(rows, cols);

		A.cornerlabel = "row/col";
		for (int i = 0; i < rows; i++) {
			A.RowLabels[i] = new String("row" + (i + 1));
			for (int j = 0; j < cols; j++) {
				A.ColumnLabels[j] = new String("col" + (j + 1));
				A.A[i][j] = i * 10 + j + 11;
			}
		}

		A.print();

	}

}
