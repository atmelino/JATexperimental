package jat.core.plot.plot.plots;

import jat.core.plot.plot.FrameView;
import jat.core.plot.plot.Plot3DPanel;
import jat.core.plot.plot.PlotPanel;
import jat.core.plot.plot.render.AbstractDrawer;
import jat.coreNOSA.math.CoordTransform;
import jat.coreNOSA.math.MatrixVector.data.VectorN;

import java.awt.Color;


public class SpherePlot3D extends Plot {

	double radius;
	static double[][][] r;
	public boolean draw_lines = true;
	public boolean fill_shape = true;
	public boolean draw_dots = false;
	public int lat_divs=18, long_divs=18;

	public SpherePlot3D(String name, Color c, double radius) {
		super(name, c);
		this.radius = radius;
		buildXYZ_list();
	}

	private void buildXYZ_list() {

		int lat_inc=360/lat_divs;
		int long_inc=180/long_divs;
		r = new double[long_divs+1][lat_divs+1][3];
		double theta, phi;
		VectorN triple;

		//double radius = 1;
		int dimx = r.length;
		int dimy = r[0].length;
		for (int i = 0; i < dimx; i++) {
			phi = i * lat_inc;
			for (int j = 0; j < dimy; j++) {
				theta = j * long_inc;
				triple = CoordTransform.Spherical_to_Cartesian_deg(radius,
						theta, phi);
				// triple.print();

				r[i][j][0] = triple.x[0];
				r[i][j][1] = triple.x[1];
				r[i][j][2] = triple.x[2];
				//System.out.println(i + " " + j + " " + " " + r[i][j][0] + " "
					//	+ r[i][j][1] + " " + r[i][j][2]);
			}
		}
	}

	public void plot(AbstractDrawer draw, Color c) {

		if (!visible)
			return;

		draw.setColor(c);

		int dimx = r.length;
		int dimy = r[0].length;
		if (draw_lines) {
			draw.setLineType(AbstractDrawer.CONTINOUS_LINE);
			for (int i = 0; i < dimx; i++)
				for (int j = 0; j < dimy - 1; j++)
					draw.drawLine(r[i][j], r[i][j + 1]);

			for (int j = 0; j < dimy; j++)
				for (int i = 0; i < dimx - 1; i++)
					draw.drawLine(r[i][j], r[i + 1][j]);
		}

		if (draw_dots) {
			draw.setDotType(AbstractDrawer.ROUND_DOT);
			draw.setDotRadius(AbstractDrawer.DEFAULT_DOT_RADIUS);
			for (int i = 0; i < dimx; i++)
				for (int j = 0; j < dimy; j++)
					draw.drawDot(r[i][j]);
		}

		for (int i = 0; i < dimx - 1; i++) {
			for (int j = 0; j < dimy - 1; j++) {
				// System.out.println(i + " " + j + " " + " " + r[i][j][0]);
				if(i==dimx/2 && j==dimy/2-9)
					draw.setColor(Color.LIGHT_GRAY);
				draw.fillPolygon(0.2f, r[i][j], r[i + 1][j], r[i + 1][j + 1],
						r[i][j + 1]);
			}
		}
		// System.out.println();

		// draw.fillPolygon(0.2f, r[0][0], r[1][0], r[0][1], r[1][1]);

	}


	@Override
	public void setData(double[][] _Z) {
		r[0] = _Z;
		buildXYZ_list();
	}

	@Override
	public double[][] getData() {
		return r[0];
	}

	public double[] isSelected(int[] screenCoordTest, AbstractDrawer draw) {
		/*
		 * for (int i = 0; i < X.length; i++) { for (int j = 0; j < Y.length;
		 * j++) { double[] XY = { X[i], Y[j], Z[j][i] }; int[] screenCoord =
		 * draw.project(XY);
		 * 
		 * if ((screenCoord[0] + note_precision > screenCoordTest[0]) &&
		 * (screenCoord[0] - note_precision < screenCoordTest[0]) &&
		 * (screenCoord[1] + note_precision > screenCoordTest[1]) &&
		 * (screenCoord[1] - note_precision < screenCoordTest[1])) return XY; }
		 * }
		 */
		return null;
	}

	public static void main(String[] args) {
		double size=8000.;
		Plot3DPanel p = new Plot3DPanel();
		p.addSpherePlot("toto", size);
		p.setLegendOrientation(PlotPanel.SOUTH);
		p.setFixedBounds(0, -size, size);
		p.setFixedBounds(1, -size, size);
		p.setFixedBounds(2, -size, size);
		new FrameView(p);
		//System.out.println(r.length);
		//System.out.println(r[0].length);
		//System.out.println(r[0][0].length);

	}

}
