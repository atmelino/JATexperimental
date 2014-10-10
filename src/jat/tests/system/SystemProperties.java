package jat.tests.system;

import jat.core.util.messageConsole.MessageConsole;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JApplet;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class SystemProperties extends JApplet{

	private static final long serialVersionUID = 7544071833875739222L;

	public SystemProperties() {
	
		
		getContentPane().setLayout(new BorderLayout(0, 0));

		// Create a text pane.
		JTextPane textPane = new JTextPane();
		JScrollPane paneScrollPane = new JScrollPane(textPane);
		paneScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		paneScrollPane.setPreferredSize(new Dimension(250, 155));
		paneScrollPane.setMinimumSize(new Dimension(10, 10));

		getContentPane().add(paneScrollPane, BorderLayout.CENTER);
		JScrollPane scrollPane_1 = new JScrollPane();
		getContentPane().add(scrollPane_1, BorderLayout.NORTH);

		// JScrollPane scrollPane = new JScrollPane();
		// getContentPane().add(scrollPane, BorderLayout.SOUTH);
		boolean mcb = true;
		if (mcb) {
			MessageConsole mc = new MessageConsole(textPane);
			mc.redirectOut();
			mc.redirectErr(Color.RED, null);
			mc.setMessageLines(100);
		}
		System.getProperties().list(System.out);
	//System.out.println(com.sun.deploy.config.Config.getCacheDirectory());

	}


	
}
