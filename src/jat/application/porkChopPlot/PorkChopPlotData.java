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

package jat.application.porkChopPlot;

import jat.core.cm.porkChopPlot;
import jat.jat3D.plot3D.Binned2DData;
import jat.jat3D.plot3D.Rainbow;

import java.io.IOException;

import javax.vecmath.Color3b;

public class PorkChopPlotData implements Binned2DData {
	private int xBins;
	private int yBins;
	private Rainbow rainbow = new Rainbow(0, 100);
	public float[][] data;
	porkChopPlot pcpPlot;
	PorkChopPlotParameters pcpParams;

	public PorkChopPlotData(PorkChopPlotMain pcMain) throws IOException {
		this.pcpParams = pcMain.pcpParams;
		pcpPlot = new porkChopPlot(pcpParams.Eph);
		pcpPlot.make_porkchop_plot(pcpParams.departure_planet, pcpParams.arrival_planet, pcpParams.search_depart_time_start,
				pcpParams.search_arrival_time_start, pcpParams.searchDays, pcpParams.steps);
		update();
	}

	public void update() {
		xBins = pcpPlot.steps;
		yBins = pcpPlot.steps;
		data = new float[xBins][yBins];
		for (int i = 0; i < xBins; i++)
			for (int j = 0; j < yBins; j++)
				if (pcpPlot.A.A[i][j] < 0)
					data[i][j] = (float) pcpPlot.maxtotaldv;
				else
					data[i][j] = (float) pcpPlot.A.A[i][j];
	}

	public int xBins() {
		return xBins;
	}

	public int yBins() {
		return yBins;
	}

	public float xMin() {
		return 0f;
	}

	public float xMax() {
		return 1f;
	}

	public float yMin() {
		return 0f;
	}

	public float yMax() {
		return 1f;
	}

	public float zMin() {
		return (float) pcpPlot.mintotaldv;
	}

	public float zMax() {
		return (float) pcpPlot.maxtotaldv;
	}

	public float zAt(int xIndex, int yIndex) {
		return data[xIndex][yIndex];
	}

	public Color3b colorAt(int xIndex, int yIndex) {
		return rainbow.colorFor(zAt(xIndex, yIndex));
	}
}
