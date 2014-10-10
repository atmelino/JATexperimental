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

package jat.core.algorithm.optimization;

public class DataArraySearch {
	float[][] fdata;
	double[][] ddata;
	int size;
	public int x_index;
	public int y_index;

	public DataArraySearch(float[][] data) {
		this(data, 1, 2);
	}

	public DataArraySearch(float[][] data, int x_index, int y_index) {
		this.fdata = data;
		this.size = data.length;
		this.x_index = x_index;
		this.y_index = y_index;
		ddata = new double[size][size];
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++)
				ddata[i][j] = fdata[i][j];
	}

	public DataArraySearch(double[][] data, int x_index, int y_index) {
		this.ddata = data;
		this.size = data.length;
		this.x_index = x_index;
		this.y_index = y_index;
	}

	
	public int goToLocalMinimum()
	{
		
		while (step() > 0);

		return 0;
	}
	
	
	public int step() {
		double left_x, right_x, left_y, right_y, current_z;
		int success;

		current_z = ddata[x_index][y_index];

		// on x=0 edge
		if (x_index == 0) {
			// on x=0 corner
			if (y_index == 0 || y_index == size - 1) {
				if (y_index == 0) {
					right_x = ddata[1][0];
					right_y = ddata[0][1];
					success = find_min(current_z, right_x, right_y);
					if (success == 1)
						x_index = 1;
					if (success == 2)
						y_index = 1;
					return success;
				}
				if (y_index == size - 1) {
					right_x = ddata[1][size - 1];
					right_y = ddata[0][size - 2];
					success = find_min(current_z, right_x, right_y);
					if (success == 1)
						x_index = 1;
					if (success == 2)
						y_index = size - 2;
					return success;
				}
			} else {
				// not on corner
				// System.out.println("left x edge");
				right_x = ddata[x_index + 1][y_index];
				left_y = ddata[x_index][y_index - 1];
				right_y = ddata[x_index][y_index + 1];
				success = find_min(current_z, right_x, left_y, right_y);
				if (success == 1)
					x_index++;
				if (success == 2)
					y_index--;
				if (success == 3)
					y_index++;
				return success;
			}
		}

		// on x=size-1 edge
		if (x_index == size - 1) {
			// on x=size-1 corner
			if (y_index == 0 || y_index == size - 1) {
				if (y_index == 0) {
					right_x = ddata[size - 2][0];
					right_y = ddata[size - 1][1];
					success = find_min(current_z, right_x, right_y);
					if (success == 1)
						x_index = size - 2;
					if (success == 2)
						y_index = 1;
					return success;
				}
				if (y_index == size - 1) {
					right_x = ddata[size - 2][size - 1];
					right_y = ddata[size - 1][size - 2];
					success = find_min(current_z, right_x, right_y);
					if (success == 1)
						x_index = size - 2;
					if (success == 2)
						y_index = size - 2;
					return success;
				}
			} else {
				// not on corner
				// System.out.println("right x edge");
				left_x = ddata[x_index - 1][y_index];
				left_y = ddata[x_index][y_index - 1];
				right_y = ddata[x_index][y_index + 1];
				success = find_min(current_z, left_x, left_y, right_y);
				if (success == 1)
					x_index--;
				if (success == 2)
					y_index--;
				if (success == 3)
					y_index++;
				return success;
			}
		}

		// on y=0 edge
		if (y_index == 0) {
			right_y = ddata[x_index][y_index + 1];
			left_x = ddata[x_index - 1][y_index];
			right_x = ddata[x_index + 1][y_index];
			success = find_min(current_z, right_y, left_x, right_x);
			if (success == 1)
				y_index++;
			if (success == 2)
				x_index--;
			if (success == 3)
				x_index++;
			return success;
		}

		// on y=size-1 edge
		if (y_index == size - 1) {
			left_y = ddata[x_index][y_index - 1];
			left_x = ddata[x_index - 1][y_index];
			right_x = ddata[x_index + 1][y_index];
			success = find_min(current_z, left_y, left_x, right_x);
			if (success == 1)
				y_index--;
			if (success == 2)
				x_index--;
			if (success == 3)
				x_index++;
			return success;
		}

		// in the middle
		left_x = ddata[x_index - 1][y_index];
		right_x = ddata[x_index + 1][y_index];
		left_y = ddata[x_index][y_index - 1];
		right_y = ddata[x_index][y_index + 1];
		success = find_min(current_z, left_x, right_x, left_y, right_y);
		if (success == 1)
			x_index--;
		if (success == 2)
			x_index++;
		if (success == 3)
			y_index--;
		if (success == 4)
			y_index++;
		return success;
	}

	private int find_min(double current_z, double x, double y) {
		int result = 0;
		double temp;
		if (x < current_z) {
			result = 1;
			temp = x;
		} else
			temp = current_z;
		if (y < temp)
			result = 2;
		return result;
	}

	private int find_min(double current_z, double z1, double z2, double z3) {
		int result = 0;
		double temp;
		if (z1 < z2) {
			temp = z1;
			result = 1;
		} else {
			temp = z2;
			result = 2;
		}
		if (z3 < temp)
			result = 3;
		return result;
	}

	private int find_min(double current_z, double left_x, double right_x, double left_y, double right_y) {
		int result = 0;
		int result_x, result_y, result_xy;
		double temp_x, temp_y;
		if (left_x < right_x) {
			temp_x = left_x;
			result_x = 1;
		} else {
			temp_x = right_x;
			result_x = 2;
		}
		if (left_y < right_y) {
			temp_y = left_y;
			result_y = 3;
		} else {
			temp_y = right_y;
			result_y = 4;
		}
		if (temp_x < temp_y)
			result_xy = result_x;
		else
			result_xy = result_y; // picks y in case of equality
		if (temp_x < current_z)
			result = result_xy;

		return result;
	}

}
