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
package jat.tests.core.algorithm.optimization;

import jat.core.algorithm.optimization.DataArraySearch;
import jat.core.math.MatrixVector.util.LabeledMatrix;
import jat.coreNOSA.math.MatrixVector.data.Matrix;

import java.util.ArrayList;
import java.util.List;

public class DataArraySearchTest {

	Matrix M;

	public static void main(String[] args) {

		List<TestSet> a = new ArrayList<TestSet>();
		a.add(new TestSet("corner 1", 0, 0));
		a.add(new TestSet("corner 2", 0, 9));
		a.add(new TestSet("corner 3", 9, 9));
		a.add(new TestSet("corner 4", 9, 0));
		a.add(new TestSet("edge x=0", 0, 5));
		a.add(new TestSet("edge x=9", 9, 5));
		a.add(new TestSet("edge y=0", 7, 0));
		a.add(new TestSet("edge y=9", 7, 9));
		a.add(new TestSet("middle 1", 5, 8));

		// Create the test data
		DataArraySearchTest da = new DataArraySearchTest();
		da.M = new LabeledMatrix(10);
		for (int i = 0; i < 10; i++)
			for (int j = 0; j < 10; j++)
				da.M.A[i][j] = (float) 2*i * i + j * j;

		for (int i = 0; i < a.size(); i++) {
			TestSet t = (TestSet) a.get(i);
			DataArraySearch d = new DataArraySearch(da.M.A, t.start_x, t.start_y);
			da.M.print();
			System.out.println(t.title);
			System.out.println("Start position: " + t.start_x + " " + t.start_y);
			while (d.step() > 0)
				System.out.println(d.x_index + " " + d.y_index+"  "+da.M.A[d.x_index][d.y_index]);
		}

	}
}
