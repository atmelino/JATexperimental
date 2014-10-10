package jat.tests.core.timeRef;

import jat.core.util.messageConsole.MessageConsole;
import jat.coreNOSA.cm.TwoBody;
import jat.coreNOSA.math.MathUtils;
import jat.coreNOSA.math.MatrixVector.data.Matrix;
import jat.coreNOSA.math.MatrixVector.data.VectorN;
import jat.coreNOSA.timeRef.CalDate;
import jat.coreNOSA.timeRef.EarthRef;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JApplet;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class EarthRefTest extends JApplet {

	private static final long serialVersionUID = 2177968966155908054L;

	public void init() {

		// Create a text pane.
		JTextPane textPane = new JTextPane();
		JScrollPane paneScrollPane = new JScrollPane(textPane);
		paneScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		paneScrollPane.setPreferredSize(new Dimension(250, 155));
		paneScrollPane.setMinimumSize(new Dimension(10, 10));
		getContentPane().add(paneScrollPane, BorderLayout.CENTER);

		// Redirect stdout and stderr to the text pane
		MessageConsole mc = new MessageConsole(textPane);
		mc.redirectOut();
		mc.redirectErr(Color.RED, null);
		mc.setMessageLines(100);
		System.out.println("Earth Reference Frame Test");

	}

	public void start() {
		double mu = 3986004.418E08;
		double a = 7136635.001;
		double e = 0.00349497;
		double i = 90.01109;
		double raan = 0.8383513;
		double w = 260.51023;
		double ta = 0.18307E-05;
		TwoBody leo = new TwoBody(mu, a, e, i, raan, w, ta);
		CalDate epoch = new CalDate(1994, 10, 4, 0, 0, 0.0);
		EarthRef ref = new EarthRef(epoch);

		double mjd = epoch.mjd();
		System.out.println("mjd = " + mjd);

		VectorN r = leo.getR();
		VectorN v = leo.getV();
		r.print("ECI");

		Matrix eci2ecef = ref.ECI2ECEF();
		VectorN recef = eci2ecef.times(r);
		recef.print("ECEF");
		// double x = -0.073424*MathUtils.ARCSEC2RAD;
		// double y = 0.206629*MathUtils.ARCSEC2RAD;
		// ref.setIERS(x, y, 0.6155998);
		double x = -0.070976 * MathUtils.ARCSEC2RAD;
		double y = 0.204837 * MathUtils.ARCSEC2RAD;
		ref.setIERS(0.0, 0.0, 0.6084018);

		VectorN rmod = ref.PrecMatrix().times(r);
		rmod.print("MOD");

		VectorN rtod = ref.NutMatrix().times(rmod);
		rtod.print("TOD");

		VectorN ritrf = (ref.PoleMatrix().times(ref.GHAMatrix())).times(rtod);
		ritrf.print("ITRF");

		eci2ecef = ref.ECI2ECEF();
		recef = eci2ecef.times(r);
		recef.print("ECEF");

		CalDate epoch2 = new CalDate(1999, 3, 4, 0, 00, 0.0);
		EarthRef ref2 = new EarthRef(epoch2);
		ref2.setIERS(0.06740 * MathUtils.ARCSEC2RAD, 0.24713 * MathUtils.ARCSEC2RAD, 0.649232);
		ref2.PrecMatrix().print("P");
		ref2.NutMatrix().print("N");
		ref2.GHAMatrix().print("GHA");
		ref2.PoleMatrix().print("Pole");
		ref2.eci2ecef().print("eci2ecef");
		ref2.ECI2ECEF().print("ECI2ECEF");
	}
	
	
	public static void main(String[] args) {


	}
}
