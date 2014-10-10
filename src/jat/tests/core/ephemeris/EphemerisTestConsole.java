package jat.tests.core.ephemeris;

import jat.core.util.messageConsole.MessageConsole;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JApplet;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class EphemerisTestConsole extends JApplet {

	private static final long serialVersionUID = -5288937301478318959L;

	public void init() {

		System.out.println("EphemerisTestConsole init");

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
		mc.setMessageLines(2000);
		System.out.println("EphemerisTestConsole init out");
		
		
	}

	public void start() {


	}

}
