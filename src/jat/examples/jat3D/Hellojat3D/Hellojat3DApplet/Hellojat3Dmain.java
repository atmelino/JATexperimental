package jat.examples.jat3D.Hellojat3D.Hellojat3DApplet;

import jat.core.util.messageConsole.MessageConsole;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class Hellojat3Dmain extends JApplet {

	private static final long serialVersionUID = -5807960663114666001L;

	public Hellojat3Dmain() {
		getContentPane().setLayout(new BorderLayout(0, 0));

		// Create a text pane.
		JTextPane textPane = new JTextPane();
		JScrollPane paneScrollPane = new JScrollPane(textPane);
		paneScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		paneScrollPane.setPreferredSize(new Dimension(250, 155));
		paneScrollPane.setMinimumSize(new Dimension(10, 10));

		getContentPane().add(paneScrollPane, BorderLayout.CENTER);
		JScrollPane scrollPane_1 = new JScrollPane();
		getContentPane().add(scrollPane_1, BorderLayout.NORTH);

		// JScrollPane scrollPane = new JScrollPane();
		// getContentPane().add(scrollPane, BorderLayout.SOUTH);
		boolean mcb = false;
		if (mcb) {
			MessageConsole mc = new MessageConsole(textPane);
			mc.redirectOut();
			mc.redirectErr(Color.RED, null);
			mc.setMessageLines(100);
		}
		System.out.println("Hellojat3DApplet started in main");
		System.out.println("redirected output");
		System.out.println("LD Library Path:" + System.getProperty("java.library.path"));
	for (int i = 0; i < 4; i++)
			System.out.println("line" + i);

	}

	public void start() {
		System.out.println("Init complete");
		System.out.println("Attempting to create Applet");

		JFrame frame = new JFrame("FrameDemo");
		frame.setSize(600, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Hellojat3DApplet h = new Hellojat3DApplet();
		frame.getContentPane().add(h, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
		System.out.println("Applet created");
	
	
	
//		JPanel panel = new JPanel();
//		getContentPane().add(new Hellojat3Dplot());		
//		Hellojat3Dplot hp=new Hellojat3Dplot();
//		panel.add(hp);

	
	
	
	}

}
