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
// Original code by Jim Baer from ftp://ssd.jpl.nasa.gov/pub/eph/planets/JAVA-version/
/*
 * This class contains the methods necessary to parse the JPL DE405
 * ephemeris files (text versions), and compute the position and velocity of
 * the planets, Moon, and Sun.
 * 
 * IMPORTANT: In order to use these methods, the user should: - save this
 * class in a directory of his/her choosing; - save to the same directory
 * the text versions of the DE405 ephemeris files, which must be named
 * "ascpxxxx.txt", where xxxx represents the start-year of the 20-year
 * block; - have at least Java 1.1.8 installed.
 * 
 * The input is the julian date (jultime) for which the ephemeris is needed.
 * Note that only julian dates from 2414992.5 to 2524624.5 are supported.
 * This input must be specified in the "main" method, which contains the
 * call to "planetary_ephemeris".
 * 
 * GENERAL IDEA: The "get_ephemeris_coefficients" method reads the ephemeris
 * file corresponding to the input julian day, and stores the ephemeris
 * coefficients needed to calculate planetary positions and velocities in
 * the array "ephemeris_coefficients". The "get_planet_posvel" method calls
 * "get_ephemeris_coefficients" if needed, then calculates the position and
 * velocity of the specified planet. The "planetary_ephemeris" method calls
 * "get_planet_posvel" for each planet, and resolves the position and
 * velocity of the Earth/Moon barycenter and geocentric Moon into the
 * position and velocity of the Earth and Moon.
 * 
 * Since the "ephemeris_coefficients" array is declared as an instance
 * variable, its contents will remain intact, should this code be modified
 * to call "planetary_ephemeris" more than once. As a result, assuming the
 * julian date of the subsequent call fell within the same 20-year file as
 * the initial call, there would be no need to reread the ephemeris file;
 * this would save on i/o time.
 * 
 * The outputs are the arrays "planet_r" and "planet_rprime", also declared
 * as instance variables.
 * 
 * Several key constants and variables follow. As noted, they are configured
 * for DE405; however, they could be adjusted to use the DE200 ephemeris,
 * whose format is quite similar.
 */
/*
 * Chebyshev coefficients for the DE405 ephemeris are contained in the files
 * "ascpxxxx.txt". These files are broken into intervals of length
 * "interval_duration", in days.
 */
/*
 * Each interval contains an interval number, length, start and end
 * jultimes, and Chebyshev coefficients. We keep only the coefficients.
 */

package jat.core.ephemeris;

import jat.core.util.PathUtil;
import jat.coreNOSA.math.MatrixVector.data.VectorN;
import jat.coreNOSA.spacetime.Time;

import java.applet.Applet;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class DE405APL {

	// static final double au = 149597870.691; // Length of an A.U., in km
	static double emrat = 81.30056; // Ratio of mass of Earth to mass of Moon
	static int interval_duration = 32; // duration of interval section in
										// ascpxxxx.txt files
	static int numbers_per_interval = 816;

	/*
	 * For each planet (and the Moon makes 10, and the Sun makes 11), each
	 * interval contains several complete sets of coefficients, each covering a
	 * fraction of the interval duration
	 */
	static int number_of_coef_sets_1 = 4;
	static int number_of_coef_sets_2 = 2;
	static int number_of_coef_sets_3 = 2;
	static int number_of_coef_sets_4 = 1;
	static int number_of_coef_sets_5 = 1;
	static int number_of_coef_sets_6 = 1;
	static int number_of_coef_sets_7 = 1;
	static int number_of_coef_sets_8 = 1;
	static int number_of_coef_sets_9 = 1;
	static int number_of_coef_sets_10 = 8;
	static int number_of_coef_sets_11 = 2;

	/*
	 * Each planet (and the Moon makes 10, and the Sun makes 11) has a different
	 * number of Chebyshev coefficients used to calculate each component of
	 * position and velocity.
	 */
	static int number_of_coefs_1 = 14;
	static int number_of_coefs_2 = 10;
	static int number_of_coefs_3 = 13;
	static int number_of_coefs_4 = 11;
	static int number_of_coefs_5 = 8;
	static int number_of_coefs_6 = 7;
	static int number_of_coefs_7 = 6;
	static int number_of_coefs_8 = 6;
	static int number_of_coefs_9 = 6;
	static int number_of_coefs_10 = 13;
	static int number_of_coefs_11 = 11;

	/* DEFINE INSTANCE VARIABLES */

	/* Define ephemeris dates and coefficients as instance variables */
	double[] ephemeris_coefficients = new double[187681];
	double[] ephemeris_dates = new double[3];

	/*
	 * Define the positions and velocities of the major planets as instance
	 * variables. Note that the first subscript is the planet number, while the
	 * second subscript specifies x, y, or z component.
	 */
	double[][] planet_r = new double[12][4];
	double[][] planet_rprime = new double[12][4];
	public VectorN posICRF[] = new VectorN[12];
	public VectorN velICRF[] = new VectorN[12];

	public PathUtil path;
	String DE405_path;

	public DE405APL() {

		path = new PathUtil();
		DE405_path = path.DE405Path;
		// System.out.println("[DE405APL 1 DE405_path] " + DE405_path);
	}

	public DE405APL(Applet myApplet) {

		path = new PathUtil(myApplet);
		DE405_path = path.DE405Path;
		// System.out.println("[DE405APL 2 DE405_path] " + DE405_path);
	}

	/**
	 * Procedure to calculate the position and velocity at jultime of the major
	 * planets. Note that the planets are enumerated as follows: Mercury = 1,
	 * Venus = 2, Earth-Moon barycenter = 3, Mars = 4, ... , Pluto = 9,
	 * Geocentric Moon = 10, Sun = 11.
	 * 
	 * @param jultime
	 * @throws IOException
	 */
	protected void update_planetary_ephemeris(Time t) throws IOException {

		double jultime = t.jd_tt();
		//System.out.println("[DE405APL jultime]" + jultime);
		int i = 0, j = 0;
		double[] ephemeris_r = new double[4];
		double[] ephemeris_rprime = new double[4];

		/* Get the ephemeris positions and velocities of each major planet */
		for (i = 1; i <= 11; i++) {
			get_ephemeris_posvel(jultime, i, ephemeris_r, ephemeris_rprime);
			for (j = 1; j <= 3; j++) {
				planet_r[i][j] = ephemeris_r[j];
				planet_rprime[i][j] = ephemeris_rprime[j];
			}

		}

		/*
		 * The positions and velocities of the Earth and Moon are found
		 * indirectly. We already have the pos/vel of the Earth-Moon barycenter
		 * (i = 3). We have also calculated planet_r(10,j), a geocentric vector
		 * from the Earth to the Moon. Using the ratio of masses, we get vectors
		 * from the Earth-Moon barycenter to the Moon and to the Earth.
		 */
		for (j = 1; j <= 3; j++) {
			planet_r[3][j] = planet_r[3][j] - planet_r[10][j] / (1 + emrat);
			planet_r[10][j] = planet_r[3][j] + planet_r[10][j];
			planet_rprime[3][j] = planet_rprime[3][j] - planet_rprime[10][j] / (1 + emrat);
			planet_rprime[10][j] = planet_rprime[3][j] + planet_rprime[10][j];
		}

		// in vector form
		// Sun
		posICRF[0] = new VectorN(0, 0, 0);
		velICRF[0] = new VectorN(0, 0, 0);
		double daysec = 3600. * 24.;
		for (i = 1; i <= 11; i++) {
			posICRF[i] = new VectorN(planet_r[i][1], planet_r[i][2], planet_r[i][3]);
			velICRF[i] = new VectorN(planet_rprime[i][1] / daysec, planet_rprime[i][2] / daysec, planet_rprime[i][3]
					/ daysec);
		}

	}

	/**
	 * Procedure to calculate the position and velocity of planet i, subject to
	 * the JPL DE405 ephemeris. The positions and velocities are calculated
	 * using Chebyshev polynomials, the coefficients of which are stored in the
	 * files "ascpxxxx.txt". The general idea is as follows: First, check to be
	 * sure the proper ephemeris coefficients (corresponding to jultime) are
	 * available. Then read the coefficients corresponding to jultime, and
	 * calculate the positions and velocities of the planet.
	 * 
	 * @param jultime
	 * @param i
	 * @param ephemeris_r
	 * @param ephemeris_rprime
	 * @throws IOException
	 */
	private void get_ephemeris_posvel(double jultime, int i, double ephemeris_r[], double ephemeris_rprime[])
			throws IOException {

		int interval = 0, numbers_to_skip = 0, pointer = 0, j = 0, k = 0, subinterval = 0;

		double interval_start_time = 0, subinterval_duration = 0, chebyshev_time = 0;

		double[] position_poly = new double[20];
		double[][] coef = new double[4][20];
		double[] velocity_poly = new double[20];

		int[] number_of_coef_sets = new int[12];
		int[] number_of_coefs = new int[12];

		/*
		 * Initialize arrays
		 */
		number_of_coefs[1] = number_of_coefs_1;
		number_of_coefs[2] = number_of_coefs_2;
		number_of_coefs[3] = number_of_coefs_3;
		number_of_coefs[4] = number_of_coefs_4;
		number_of_coefs[5] = number_of_coefs_5;
		number_of_coefs[6] = number_of_coefs_6;
		number_of_coefs[7] = number_of_coefs_7;
		number_of_coefs[8] = number_of_coefs_8;
		number_of_coefs[9] = number_of_coefs_9;
		number_of_coefs[10] = number_of_coefs_10;
		number_of_coefs[11] = number_of_coefs_11;
		number_of_coef_sets[1] = number_of_coef_sets_1;
		number_of_coef_sets[2] = number_of_coef_sets_2;
		number_of_coef_sets[3] = number_of_coef_sets_3;
		number_of_coef_sets[4] = number_of_coef_sets_4;
		number_of_coef_sets[5] = number_of_coef_sets_5;
		number_of_coef_sets[6] = number_of_coef_sets_6;
		number_of_coef_sets[7] = number_of_coef_sets_7;
		number_of_coef_sets[8] = number_of_coef_sets_8;
		number_of_coef_sets[9] = number_of_coef_sets_9;
		number_of_coef_sets[10] = number_of_coef_sets_10;
		number_of_coef_sets[11] = number_of_coef_sets_11;

		/*
		 * Begin by determining whether the current ephemeris coefficients are
		 * appropriate for jultime, or if we need to load a new set.
		 */
		if ((jultime < ephemeris_dates[1]) || (jultime > ephemeris_dates[2]))
			get_ephemeris_coefficients(jultime);

		interval = (int) (Math.floor((jultime - ephemeris_dates[1]) / interval_duration) + 1);
		interval_start_time = (interval - 1) * interval_duration + ephemeris_dates[1];
		subinterval_duration = interval_duration / number_of_coef_sets[i];
		subinterval = (int) (Math.floor((jultime - interval_start_time) / subinterval_duration) + 1);
		numbers_to_skip = (interval - 1) * numbers_per_interval;

		/*
		 * Starting at the beginning of the coefficient array, skip the first
		 * "numbers_to_skip" coefficients. This puts the pointer on the first
		 * piece of data in the correct interval.
		 */
		pointer = numbers_to_skip + 1;

		/* Skip the coefficients for the first (i-1) planets */
		for (j = 1; j <= (i - 1); j++)
			pointer = pointer + 3 * number_of_coef_sets[j] * number_of_coefs[j];

		/* Skip the next (subinterval - 1)*3*number_of_coefs(i) coefficients */
		pointer = pointer + (subinterval - 1) * 3 * number_of_coefs[i];

		for (j = 1; j <= 3; j++) {
			for (k = 1; k <= number_of_coefs[i]; k++) {
				/* Read the pointer'th coefficient as the array entry coef[j][k] */
				// System.out.println("[DE405APL j k pointer]" + j + " " + k +
				// " " + pointer);
				coef[j][k] = ephemeris_coefficients[pointer];
				pointer = pointer + 1;
			}
		}

		/*
		 * Calculate the chebyshev time within the subinterval, between -1 and
		 * +1
		 */
		chebyshev_time = 2 * (jultime - ((subinterval - 1) * subinterval_duration + interval_start_time))
				/ subinterval_duration - 1;

		/* Calculate the Chebyshev position polynomials */
		position_poly[1] = 1;
		position_poly[2] = chebyshev_time;
		for (j = 3; j <= number_of_coefs[i]; j++)
			position_poly[j] = 2 * chebyshev_time * position_poly[j - 1] - position_poly[j - 2];

		/* Calculate the position of the i'th planet at jultime */
		for (j = 1; j <= 3; j++) {
			ephemeris_r[j] = 0;
			for (k = 1; k <= number_of_coefs[i]; k++)
				ephemeris_r[j] = ephemeris_r[j] + coef[j][k] * position_poly[k];

			/* DON'T Convert from km to A.U. */
			// ephemeris_r[j] = ephemeris_r[j] / au;
		}

		/* Calculate the Chebyshev velocity polynomials */
		velocity_poly[1] = 0;
		velocity_poly[2] = 1;
		velocity_poly[3] = 4 * chebyshev_time;
		for (j = 4; j <= number_of_coefs[i]; j++)
			velocity_poly[j] = 2 * chebyshev_time * velocity_poly[j - 1] + 2 * position_poly[j - 1]
					- velocity_poly[j - 2];

		/* Calculate the velocity of the i'th planet */
		for (j = 1; j <= 3; j++) {
			ephemeris_rprime[j] = 0;
			for (k = 1; k <= number_of_coefs[i]; k++)
				ephemeris_rprime[j] = ephemeris_rprime[j] + coef[j][k] * velocity_poly[k];
			/*
			 * The next line accounts for differentiation of the iterative
			 * formula with respect to chebyshev time. Essentially, if dx/dt =
			 * (dx/dct) times (dct/dt), the next line includes the factor
			 * (dct/dt) so that the units are km/day
			 */
			ephemeris_rprime[j] = ephemeris_rprime[j] * (2.0 * number_of_coef_sets[i] / interval_duration);

			/* DON'T Convert from km to A.U. */
			// ephemeris_rprime[j] = ephemeris_rprime[j] / au;

		}

	}

	/**
	 * Procedure to read the DE405 ephemeris file corresponding to jultime. The
	 * start and end dates of the ephemeris file are returned, as are the
	 * Chebyshev coefficients for Mercury, Venus, Earth-Moon, Mars, Jupiter,
	 * Saturn, Uranus, Neptune, Pluto, Geocentric Moon, and Sun.
	 * 
	 * @param jultime
	 * @throws IOException
	 *             different method of reading a file that works with files read
	 *             from the Internet
	 */

	private void get_ephemeris_coefficients(double jultime) throws IOException {

		int mantissa1 = 0, mantissa2 = 0, exponent = 0, i = 0, records = 0, j = 0;
		String fileName = null;
		String line = " ";

		try {

			/* Select the proper ephemeris file */
			if ((jultime >= 2414992.5) && (jultime < 2422320.5)) {
				ephemeris_dates[1] = 2414992.5;
				ephemeris_dates[2] = 2422320.5;
				fileName = DE405_path + "ascp1900.405";
				records = 230;
			} else if ((jultime >= 2422320.5) && (jultime < 2429616.5)) {
				ephemeris_dates[1] = 2422320.5;
				ephemeris_dates[2] = 2429616.5;
				fileName = DE405_path + "ascp1920.405";
				records = 229;
			} else if ((jultime >= 2429616.5) && (jultime < 2436912.5)) {
				ephemeris_dates[1] = 2429616.5;
				ephemeris_dates[2] = 2436912.5;
				fileName = DE405_path + "ascp1940.405";
				records = 229;
			} else if ((jultime >= 2436912.5) && (jultime < 2444208.5)) {
				ephemeris_dates[1] = 2436912.5;
				ephemeris_dates[2] = 2444208.5;
				fileName = DE405_path + "ascp1960.405";
				records = 229;
			} else if ((jultime >= 2444208.5) && (jultime < 2451536.5)) {
				ephemeris_dates[1] = 2444208.5;
				ephemeris_dates[2] = 2451536.5;
				fileName = DE405_path + "ascp1980.405";
				records = 230;
			} else if ((jultime >= 2451536.5) && (jultime < 2458832.5)) {
				ephemeris_dates[1] = 2451536.5;
				ephemeris_dates[2] = 2458832.5;
				fileName = DE405_path + "ascp2000.405";
				records = 229;
			} else if ((jultime >= 2458832.5) && (jultime < 2466128.5)) {
				ephemeris_dates[1] = 2458832.5;
				ephemeris_dates[2] = 2466128.5;
				fileName = DE405_path + "ascp2020.405";
				records = 229;
			} else if ((jultime >= 2466128.5) && (jultime < 2473456.5)) {
				ephemeris_dates[1] = 2466128.5;
				ephemeris_dates[2] = 2473456.5;
				fileName = DE405_path + "ascp2040.405";
				records = 230;
			} else if ((jultime >= 2473456.5) && (jultime < 2480752.5)) {
				ephemeris_dates[1] = 2473456.5;
				ephemeris_dates[2] = 2480752.5;
				fileName = DE405_path + "ascp2060.405";
				records = 229;
			} else if ((jultime >= 2480752.5) && (jultime < 2488048.5)) {
				ephemeris_dates[1] = 2480752.5;
				ephemeris_dates[2] = 2488048.5;
				fileName = DE405_path + "ascp2080.405";
				records = 229;
			} else if ((jultime >= 2488048.5) && (jultime < 2495344.5)) {
				ephemeris_dates[1] = 2488048.5;
				ephemeris_dates[2] = 2495344.5;
				fileName = DE405_path + "ascp2100.405";
				records = 229;
			} else if ((jultime >= 2495344.5) && (jultime < 2502672.5)) {
				ephemeris_dates[1] = 2495344.5;
				ephemeris_dates[2] = 2502672.5;
				fileName = DE405_path + "ascp2120.405";
				records = 230;
			} else if ((jultime >= 2502672.5) && (jultime < 2509968.5)) {
				ephemeris_dates[1] = 2502672.5;
				ephemeris_dates[2] = 2509968.5;
				fileName = DE405_path + "ascp2140.405";
				records = 229;
			} else if ((jultime >= 2509968.5) && (jultime < 2517264.5)) {
				ephemeris_dates[1] = 2509968.5;
				ephemeris_dates[2] = 2517264.5;
				fileName = DE405_path + "ascp2160.405";
				records = 229;
			} else if ((jultime >= 2517264.5) && (jultime < 2524624.5)) {
				ephemeris_dates[1] = 2517264.5;
				ephemeris_dates[2] = 2524624.5;
				fileName = DE405_path + "ascp2180.405";
				records = 230;
			}

			// System.out.println("[DE405APL DE405_path] " + DE405_path);
			// System.out.println("[DE405APL filename] " + filename);

			if (fileName == null) {
				System.out.println("Time period unavailable");
				System.exit(0);
			}

			try {
				// Create a URL for the desired page
				// If it is called from an applet, it starts with "file:" or
				// "http:"
				// If it's an application, we need to add "file:" so that
				// BufferReader works
				boolean application;
				if (fileName.startsWith("file") || fileName.startsWith("http"))
					application = false;
				else
					application = true;
				if (application)
					fileName = "file:" + fileName;
				URL url = new URL(fileName);
				// System.out.println("[DE405APL filename] " + fileName);
				BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

				/* Read each record in the file */
				for (j = 1; j <= records; j++) {

					/* read line 1 and ignore */
					line = in.readLine();
					// System.out.println("[DE405APL line ] " + line);

					/* read lines 2 through 274 and parse as appropriate */
					for (i = 2; i <= 274; i++) {
						line = in.readLine();
						if (i > 2) {
							/* parse first entry */
							mantissa1 = Integer.parseInt(line.substring(4, 13));
							mantissa2 = Integer.parseInt(line.substring(13, 22));
							exponent = Integer.parseInt(line.substring(24, 26));
							if (line.substring(23, 24).equals("+"))
								ephemeris_coefficients[(j - 1) * 816 + (3 * (i - 2) - 1)] = mantissa1
										* Math.pow(10, (exponent - 9)) + mantissa2 * Math.pow(10, (exponent - 18));
							else
								ephemeris_coefficients[(j - 1) * 816 + (3 * (i - 2) - 1)] = mantissa1
										* Math.pow(10, -(exponent + 9)) + mantissa2 * Math.pow(10, -(exponent + 18));
							if (line.substring(1, 2).equals("-"))
								ephemeris_coefficients[(j - 1) * 816 + (3 * (i - 2) - 1)] = -ephemeris_coefficients[(j - 1)
										* 816 + (3 * (i - 2) - 1)];
						}
						if (i > 2) {
							/* parse second entry */
							mantissa1 = Integer.parseInt(line.substring(30, 39));
							mantissa2 = Integer.parseInt(line.substring(39, 48));
							exponent = Integer.parseInt(line.substring(50, 52));
							if (line.substring(49, 50).equals("+"))
								ephemeris_coefficients[(j - 1) * 816 + 3 * (i - 2)] = mantissa1
										* Math.pow(10, (exponent - 9)) + mantissa2 * Math.pow(10, (exponent - 18));
							else
								ephemeris_coefficients[(j - 1) * 816 + 3 * (i - 2)] = mantissa1
										* Math.pow(10, -(exponent + 9)) + mantissa2 * Math.pow(10, -(exponent + 18));
							if (line.substring(27, 28).equals("-"))
								ephemeris_coefficients[(j - 1) * 816 + 3 * (i - 2)] = -ephemeris_coefficients[(j - 1)
										* 816 + 3 * (i - 2)];
						}
						if (i < 274) {
							/* parse third entry */
							mantissa1 = Integer.parseInt(line.substring(56, 65));
							mantissa2 = Integer.parseInt(line.substring(65, 74));
							exponent = Integer.parseInt(line.substring(76, 78));
							if (line.substring(75, 76).equals("+"))
								ephemeris_coefficients[(j - 1) * 816 + (3 * (i - 2) + 1)] = mantissa1
										* Math.pow(10, (exponent - 9)) + mantissa2 * Math.pow(10, (exponent - 18));
							else
								ephemeris_coefficients[(j - 1) * 816 + (3 * (i - 2) + 1)] = mantissa1
										* Math.pow(10, -(exponent + 9)) + mantissa2 * Math.pow(10, -(exponent + 18));
							if (line.substring(53, 54).equals("-"))
								ephemeris_coefficients[(j - 1) * 816 + (3 * (i - 2) + 1)] = -ephemeris_coefficients[(j - 1)
										* 816 + (3 * (i - 2) + 1)];
						}
					}

					/* read lines 275 through 341 and ignore */
					for (i = 275; i <= 341; i++)
						line = in.readLine();

				}

				in.close();
			} catch (MalformedURLException e) {
				System.out.println("MalformedURLException");
			} catch (IOException e) {
			}

		} catch (StringIndexOutOfBoundsException e) {
			System.out.println("String index out of bounds at i = " + i);
		}

	}

}

// public static void main(String args[]) {
//
// /* USER MUST SPECIFY jultime HERE. Example value is 2451545.0 */
// double jultime = 2451545.0;
//
// int i = 0, j = 0;
//
// DE405APL testBody = new DE405APL();
//
// /*
// * This is the call to "planetary_ephemeris", which will put planetary
// * positions into the array "planet_r", and planetary velocities into
// * the array "planet_rprime".
// */
// try {
// testBody.planetary_ephemeris(jultime);
// } catch (IOException e) {
// System.out.println("exception caught in DE405APL main:");
// e.printStackTrace();
// }
//
// /* The following simply sends the output to the screen */
// for (i = 1; i <= 11; i++) {
//
// System.out.println("Planet " + i);
// System.out.println("     position");
// for (j = 1; j <= 3; j++)
// System.out.println(testBody.planet_r[i][j]);
// System.out.println("     velocity");
// for (j = 1; j <= 3; j++)
// System.out.println(testBody.planet_rprime[i][j]);
//
// }
//
// }
