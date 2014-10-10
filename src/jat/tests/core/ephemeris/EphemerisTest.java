package jat.tests.core.ephemeris;

import jat.core.ephemeris.DE405Body.body;
import jat.core.ephemeris.DE405Frame.frame;
import jat.core.ephemeris.DE405Plus;
import jat.core.util.PathUtil;
import jat.core.util.messageConsole.MessageConsole;
import jat.coreNOSA.math.MatrixVector.data.VectorN;
import jat.coreNOSA.spacetime.Time;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class EphemerisTest extends JApplet {

	private static final long serialVersionUID = 4507683576803709168L;

	public void init() {

		// Create a text pane.
		JTextPane textPane = new JTextPane();
		JScrollPane paneScrollPane = new JScrollPane(textPane);
		paneScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		paneScrollPane.setPreferredSize(new Dimension(300, 155));
		paneScrollPane.setMinimumSize(new Dimension(10, 10));
		getContentPane().add(paneScrollPane, BorderLayout.CENTER);

		// Redirect stdout and stderr to the text pane
		MessageConsole mc = new MessageConsole(textPane);
		mc.redirectOut();
		mc.redirectErr(Color.RED, null);
		mc.setMessageLines(100);
		System.out.println("Ephemeris Test");

	}

	public void start() {
		// Message console
		System.out.println("Creating Ephemeris Test Applet");
		EphemerisTestConsole E = new EphemerisTestConsole();
		JFrame jf = new JFrame();
		jf.setSize(600, 400);
		jf.getContentPane().add(E);
		jf.setVisible(true);
		E.init();
		System.out.println("Ephemeris Console created");

		
		// main task
		
		PathUtil path=new PathUtil(this);
		System.out.println("[EphemerisTest current_path] "+path.current_path);
		System.out.println("[EphemerisTest root_path] "+path.root_path);
		System.out.println("[EphemerisTest data_path] "+path.data_path);
		
		Time mytime = new Time(2002, 2, 17, 12, 0, 0);
		System.out.println("Loading DE405 Ephemeris File");
		DE405Plus ephem = new DE405Plus(path);
		ephem.setFrame(frame.HEE);
		System.out.println("DE405 Ephemeris File loaded");
		try {
			VectorN rv;
			//rv = ephem.get_planet_posvel(DE405Plus.body.MARS, mytime.jd_tt());
			rv = ephem.get_planet_posvel(body.MARS, mytime);
			System.out.println("Reference Frame: "+ephem.ephFrame);
			System.out.println("The position of Mars on 10-17-2002 at 12:00pm was ");
			System.out.println("x= " + rv.get(0) + " km");
			System.out.println("y= " + rv.get(1) + " km");
			System.out.println("z= " + rv.get(2) + " km");
			System.out.println("The velocity of Mars on 10-17-2002 at 12:00pm was ");
			System.out.println("vx= " + rv.get(3) + " km/s");
			System.out.println("vy= " + rv.get(4) + " km/s");
			System.out.println("vz= " + rv.get(5) + " km/s");
		} catch (IOException e) {
			System.out.println("Failed to get planet position velocity in get_planet_posvel()");
			e.printStackTrace();
		}

	}

}
