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
package jat.coreNOSA.spacetime;

import jat.core.util.PathUtil;
import jat.coreNOSA.math.MatrixVector.data.Matrix;
import jat.coreNOSA.math.MatrixVector.data.VectorN;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * International Earth Rotation and Reference Service parameters. Parses and
 * calculates the polar motion for the Celestial Ephemeris Pole and the offset
 * UT1-UTC.
 * 
 * File format for the polar motion datafile is as follows: First line contains
 * a single number indicating the number of lines in the file time xpole
 * xpole_error ypole ypole_error UT1-UTC time_error Units: time - Modified
 * Julian Date in UTC xpole - arcsec ypole - arcsec UT1-UTC is the difference in
 * seconds
 * 
 * Error values are not currently used. If they are not available, format the
 * file with zeros.
 * 
 * @author Richard C. Page III
 */
public class FitIERS {

	/**
	 * IERS EOP file (Bulletin A)
	 */
	private String filename;
	private String prev_filename;
	// protected VectorN u,ax,ay;
	/**
	 * Time array
	 */
	protected static double[] mjd;
	protected static double mjd_1 = 0.0;
	// protected double tp=0.0;
	// private double pmx = 0;
	// private double pmy = 0;
	// private double diffUT1UTC = 0.0;
	protected double update_interval = 1800; // [s] IERS Update interval
	/**
	 * Parameters
	 */
	protected static double[][] eop;
	private static double out_mjd;
	private static double[] out_data;
	private static boolean initialized = false;

	/**
	 * Default constructor
	 */
	public FitIERS() {
		//String fs = FileUtil.file_separator();
		PathUtil p = new PathUtil();
		String directory=p.data_path+"/core/spacetime";

		System.out.println(directory);
		
//		try {
//			
//			
//			directory = FileUtil.getClassFilePath("jatcore.", "spacetime");
//			if (directory == null)
//				directory = "jatcore/spacetime";
//
//			System.out.println("dir = " + directory);
//		} catch (Exception ne) {
//			directory = "jatcore/spacetime";
//		}
		// filename = directory+"iers.dat";

		filename = directory + "/" + "EOP.dat";
		prev_filename = filename;
		process();
	}

	/**
	 * Constructor
	 * 
	 * @param fname
	 *            Filename of the IERS reference data (eg "iers.dat").
	 */
	public FitIERS(String fname) {
		filename = fname;
		prev_filename = filename;
		process();
	}

	/**
	 * Parse the file into data.
	 */
	public void process() {

		// System.out.println("Processing File: " + filename);
		if (!prev_filename.equals(filename) || !initialized) {
			FileReader fr;
			BufferedReader in = null;


			InputStream is;
			try {
				is = new FileInputStream(filename);
				InputStreamReader isr = new InputStreamReader(is);
				in = new BufferedReader(isr);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(0);
			}

			
//			try {
//
//				InputStream is = null;
//				InputStreamReader isr;
//				ClassLoader cl = this.getClass().getClassLoader();
//				System.out.println("filename = " + filename);
//				URL ut = cl.getResource(filename);
//				try {
//					is = ut.openConnection().getInputStream();
//				} catch (Exception ne) {
//					System.out.println("Can not get inputstream for = " + ut);
//				}
//
//				isr = new InputStreamReader(is);
//				// fr = new FileReader(is);
//				in = new BufferedReader(isr);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}

			
			
			
			
			ArrayList lineCollection = new ArrayList();

			// read in the file
			boolean eof = false;
			while (!eof) {
				String line;
				try {
					if ((line = in.readLine()) == null) {
						eof = true;
					} else {
						// add to the collection
						lineCollection.add(line);
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

			int n = lineCollection.size() - 1;
			// int n = 28;
			// int n = 48;
			// int n = 2191;
			// double[] mjd = new double[n];
			mjd = new double[n];
			double[] y = new double[n];
			Matrix hth = new Matrix(3, 3);
			Matrix ht = new Matrix(3, n);
			Matrix ata = new Matrix(5, 5);
			VectorN aty = new VectorN(5);
			VectorN atx = new VectorN(5);

			String str;
			StringTokenizer tok;

			eop = new double[n][3];

			for (int i = 0; i < n; i++) {
				// str = (String) lineCollection.get(i);
				// tok = new StringTokenizer(str, " ");
				// mjd[i] = Double.parseDouble(tok.nextToken());
				// double pmx = Double.parseDouble(tok.nextToken());
				// double pmy = Double.parseDouble(tok.nextToken());
				// y[i] = Double.parseDouble(tok.nextToken());

				// ***Read EOP.dat
				// System.out.println("parse:  "+filename);

				// str = (String) lineCollection.get(i+1860);//1870);
				str = (String) lineCollection.get(i + 1);// all
				// System.out.println("line "+i+":  "+str);
				tok = new StringTokenizer(str, " ");
				mjd[i] = Double.parseDouble(tok.nextToken());
				double pmx = Double.parseDouble(tok.nextToken());
				eop[i][0] = pmx;
				tok.nextToken();
				double pmy = Double.parseDouble(tok.nextToken());
				eop[i][1] = pmy;
				tok.nextToken();
				y[i] = Double.parseDouble(tok.nextToken());
				eop[i][2] = y[i];
				tok.nextToken();
				// }//*DEBUG
				// System.out.println(""+mjd[i]+"  "+pmx+"  "+pmy+"  "+y[i]);

				// double x = 0.0;
				// double dtleap = 0.0;
				// if (i > 0) {
				// //x = mjd[i] - mjd[i - 1];
				// //x += (mjd[i]-mjd[i-1]);
				// x = i * 1.0; // one day increment in data file
				// if (Math.abs(y[i] - y[i - 1]) > 0.5) {
				// dtleap = 1.0;
				// }
				// y[i] = y[i] - dtleap;
				// }

				// double x2 = x * x;
				// double x3 = x2 * x;
				// double x4 = x3 * x;
				// hth.set(2, 2, (hth.get(2, 2) + 1.0));
				// hth.set(2, 1, (hth.get(2, 1) + x));
				// hth.set(1, 2, (hth.get(2, 1)));
				// hth.set(2, 0, (hth.get(2, 0) + x2));
				// hth.set(1, 1, (hth.get(2, 0)));
				// hth.set(0, 2, (hth.get(2, 0)));
				// hth.set(1, 0, (hth.get(1, 0) + x3));
				// hth.set(0, 1, (hth.get(1, 0)));
				// hth.set(0, 0, (hth.get(0, 0) + x4));

				// ht.set(0, i, x2);
				// ht.set(1, i, x);
				// ht.set(2, i, 1.0);
				// // process polar motion data
				// tp = mjd[0] - 1.0;
				// double num = 2.0 * (mjd[i] - tp) * MathUtils.PI;
				// double ca = num / 365.25;
				// double cc = num / 435.0;
				// double cosa = Math.cos(ca);
				// double sina = Math.sin(ca);
				// double cosc = Math.cos(cc);
				// double sinc = Math.sin(cc);

				// ata.set(0, 0, (ata.get(0, 0) + 1.0));
				// ata.set(1, 0, (ata.get(1, 0) + cosa));
				// ata.set(2, 0, (ata.get(2, 0) + sina));
				// ata.set(3, 0, (ata.get(3, 0) + cosc));
				// ata.set(4, 0, (ata.get(4, 0) + sinc));
				// ata.set(0, 1, (ata.get(1, 0)));
				// ata.set(0, 2, (ata.get(2, 0)));
				// ata.set(0, 3, (ata.get(3, 0)));
				// ata.set(0, 4, (ata.get(4, 0)));
				// ata.set(1, 1, (ata.get(1, 1) + cosa * cosa));
				// ata.set(2, 1, (ata.get(2, 1) + cosa * sina));
				// ata.set(3, 1, (ata.get(3, 1) + cosa * cosc));
				// ata.set(4, 1, (ata.get(4, 1) + cosa * sinc));
				// ata.set(1, 2, (ata.get(2, 1)));
				// ata.set(1, 3, (ata.get(3, 1)));
				// ata.set(1, 4, (ata.get(4, 1)));
				// ata.set(2, 2, (ata.get(2, 2) + sina * sina));
				// ata.set(3, 2, (ata.get(3, 2) + sina * cosc));
				// ata.set(4, 2, (ata.get(4, 2) + sina * sinc));
				// ata.set(2, 3, (ata.get(3, 2)));
				// ata.set(2, 4, (ata.get(4, 2)));
				// ata.set(3, 3, (ata.get(3, 3) + cosc * cosc));
				// ata.set(3, 4, (ata.get(3, 4) + cosc * sinc));
				// ata.set(4, 3, (ata.get(3, 4)));
				// ata.set(4, 4, (ata.get(4, 4) + sinc * sinc));

				// atx.set(0, (atx.get(0) + pmx));
				// atx.set(1, (atx.get(1) + pmx * cosa));
				// atx.set(2, (atx.get(2) + pmx * sina));
				// atx.set(3, (atx.get(3) + pmx * cosc));
				// atx.set(4, (atx.get(4) + pmx * sinc));
				// aty.set(0, (aty.get(0) + pmy));
				// aty.set(1, (aty.get(1) + pmy * cosa));
				// aty.set(2, (aty.get(2) + pmy * sina));
				// aty.set(3, (aty.get(3) + pmy * cosc));
				// aty.set(4, (aty.get(4) + pmy * sinc));

				// }
				// //* Get inverse
				// Matrix hthinv = hth.inverse();
				// //* Get hty
				// VectorN ymat = new VectorN(y);
				// VectorN hty = ht.times(ymat);

				// //* solve
				// u = hthinv.times(hty);
				// //* invert ata (least squares fit for x- and y- parameters
				// Matrix atainv = ata.inverse();
				// //* multiply the inverses
				// ax = atainv.times(atx);
				// ay = atainv.times(aty);
			}
			mjd_1 = mjd[0];
			initialized = true;
		}
	}

	/**
	 * Interpolate (linear) the data to find the parameters.
	 * 
	 * @param mjd_arg
	 * @return [xpole ypole UT1-UTC]
	 */
	public double[] search(double mjd_arg) {
		// System.out.println("***SEARCH***");
		double[] output = new double[3];
		if (Math.abs(mjd_arg - FitIERS.out_mjd) < 1.0) {
			output = FitIERS.out_data;
		} else {
			int n = mjd.length;
			int out = n - 2;
			// System.out.println("mjd0: "+mjd[0]+"   mjdarg: "+mjd_arg);
			// System.out.println("mjd2: "+mjd[n/2]+"   mjdf: "+mjd[n-1]);
			// System.out.println("mjd_arg: "+mjd_arg);
			// System.out.println("length: "+mjd.length);
			// System.out.println("den:    "+(mjd[mjd.length-1]-mjd[0]));
			double ref = (mjd.length / (mjd[mjd.length - 1] - mjd[0]));
			int ai = (int) ((mjd_arg - mjd[0]) * ref - 5);
			for (int i = ai; i < ai + 10; i++) {
				// System.out.println("MATLAB: FitIERS search: "+i+"  ai: "+ai+"  ref: "+ref);
				if ((mjd_arg - mjd[i]) < 1.0) {
					out = i;
					// System.out.println("mjd1: "+mjd[i]+"  "+eop[i][0]);
					break;
				}
			}
			if (out == n - 2) {
				if (mjd_arg <= mjd[0])
					return eop[0];
				else if (mjd_arg < mjd[n / 2]) {
					for (int i = 0; i < n / 2; i++) {
						if ((mjd_arg - mjd[i]) < 1.0) {
							out = i;
							// System.out.println("mjd1: "+mjd[i]+"  "+eop[i][0]);
							break;
						}
					}
				} else if (mjd_arg >= mjd[n / 2] && mjd_arg <= mjd[n - 1]) {
					for (int i = n / 2; i < n; i++) {
						// System.out.println("i: "+i+"  "+mjd[i]+"  "+eop[i][0]);
						if ((mjd_arg - mjd[i]) < 1.0) {
							out = i;
							// System.out.println("mjd2: "+mjd[i]+"  "+eop[i][0]);
							break;
						}
					}
				}
			}
			// * linear interpolation

			output[0] = (eop[out + 1][0] - eop[out][0]) / (mjd[out + 1] - mjd[out]) * (mjd_arg - mjd[out])
					+ eop[out][0];
			output[1] = (eop[out + 1][1] - eop[out][1]) / (mjd[out + 1] - mjd[out]) * (mjd_arg - mjd[out])
					+ eop[out][1];
			output[2] = (eop[out + 1][2] - eop[out][2]) / (mjd[out + 1] - mjd[out]) * (mjd_arg - mjd[out])
					+ eop[out][2];
			FitIERS.out_mjd = mjd[out];
			FitIERS.out_data = output;
		}
		return output;

		// System.out.println("Search out: "+mjd[out]+"  "+eop[out][0]);
		// return eop[out];
	}

	// /**
	// * Fit the x and y poles to the given Julian Date
	// * @param mjd Modified Julian Date
	// */
	// public void fit(double mjd){
	// // Find the nearest UT1 value
	// // for(int i=0; i<this.mjd.length; i++){
	// // if(this.mjd[i] < mjd){
	// // UT1 = this.mjd[i];
	// //System.out.println("fit - UT1: "+UT1+"  mjd: "+mjd);
	// // }else
	// // break;
	// // }
	// double timeDiff = mjd-tp;
	// //double timeDiff = 5.000006;
	// double A1 = 2*MathUtils.PI/365.25*(timeDiff);
	// double C1 = 2*MathUtils.PI/435.0*(timeDiff);
	// pmx = ax.get(0)+ax.get(1)*Math.cos(A1)+ax.get(2)*Math.sin(A1)+
	// ax.get(3)*Math.cos(C1)+ax.get(4)*Math.sin(C1);
	// pmy = ay.get(0)+ay.get(1)*Math.cos(A1)+ay.get(2)*Math.sin(A1)+
	// ay.get(3)*Math.cos(C1)+ay.get(4)*Math.sin(C1);
	// // pmx = ax.get(4)+ax.get(3)*Math.cos(A1)+ax.get(2)*Math.sin(A1)+
	// // ax.get(1)*Math.cos(C1)+ax.get(0)*Math.sin(C1);
	// // pmy = ay.get(4)+ay.get(3)*Math.cos(A1)+ay.get(2)*Math.sin(A1)+
	// // ay.get(1)*Math.cos(C1)+ay.get(0)*Math.sin(C1);
	// diffUT1UTC =
	// u.get(2)+u.get(1)*(mjd-mjd_1)+u.get(0)*(mjd-mjd_1)*(mjd-mjd_1);
	// }

	// public void fit_value(double mjd_arg){
	// double[] fit = search(mjd_arg);
	// pmx = fit[0];
	// pmy = fit[1];
	// diffUT1UTC = fit[2];
	// }

	// public void print(){
	// if(u!=null && ax !=null && ay!=null){
	// System.out.println(""+mjd_1+"   "+u.get(0)+"   "+u.get(1)+"   "+u.get(2));
	// System.out.println(""+tp+"   "+ax.get(0)+"   "+ax.get(1)+"   "+ax.get(2)+"   "+ax.get(3)+"   "+ax.get(4));
	// System.out.println(""+tp+"   "+ay.get(0)+"   "+ay.get(1)+"   "+ay.get(2)+"   "+ay.get(3)+"   "+ay.get(4));
	// }
	// System.out.println("***");
	// System.out.println("x: "+pmx+"  y: "+pmy+"  diff: "+diffUT1UTC);
	// }

	public void set_update_interval(double val) {
		this.update_interval = val;
	}

	public double get_update_interval() {
		return this.update_interval;
	}

	// /**
	// * Returns the Earth's x pole;
	// * @return
	// */
	// public double getX(){ return pmx;}
	// /**
	// * Returns the Earth's y pole;
	// * @return
	// */
	// public double getY(){ return pmy;}
	// /**
	// * Returns the time difference between UT1 and UTC;
	// * @return
	// */
	// public double getDUT1(){ return diffUT1UTC;}

	// private void setCoeff(double[] u, double[] ax, double[] ay){
	// this.u = new VectorN(u);
	// this.ax = new VectorN(ax);
	// this.ay = new VectorN(ay);
	// }

	// public static void main(String[] args) {
	//
	// }
	//
	// public void printDiff(double[] u, double[] a){
	// if(u!=null && a!=null){
	// System.out.println("DIFFERENCE");
	// System.out.println(""+(u[0]-this.u.get(2))+"  "+(u[1]-this.u.get(1))+"  "+(u[2]-this.u.get(0)));
	// System.out.println(""+(a[0]-this.ax.get(0))+"  "+(a[1]-this.ax.get(1))+"  "+(a[2]-this.ax.get(2))+"  "+(a[3]-this.ax.get(3))+"  "+(a[4]-this.ax.get(4)));
	// System.out.println(""+(a[5]-this.ay.get(0))+"  "+(a[6]-this.ay.get(1))+"  "+(a[7]-this.ay.get(2))+"  "+(a[8]-this.ay.get(3))+"  "+(a[9]-this.ay.get(4)));
	// }
	// }
}
