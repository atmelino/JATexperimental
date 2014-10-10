package jat.tests.core.util.PathUtil;

import jat.core.util.PathUtil;
import jat.core.util.messageConsole.MessageConsole;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class PathUtilTest extends JApplet {

	private static final long serialVersionUID = 4507683576803709168L;
	boolean debug = true;

	public void init() {

		// Create a text pane.
		JTextPane textPane = new JTextPane();
		JScrollPane paneScrollPane = new JScrollPane(textPane);
		paneScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		paneScrollPane.setPreferredSize(new Dimension(300, 155));
		paneScrollPane.setMinimumSize(new Dimension(10, 10));
		getContentPane().add(paneScrollPane, BorderLayout.CENTER);

		// Redirect stdout and stderr to the text pane
		MessageConsole mc = new MessageConsole(textPane);
		mc.redirectOut();
		mc.redirectErr(Color.RED, null);
		mc.setMessageLines(100);
		System.out.println("[PathUtilTest]");

	}

	public void start() {
		// Message console
		PathUtilTestConsole E = new PathUtilTestConsole();
		JFrame jf = new JFrame();
		jf.setSize(600, 400);
		jf.getContentPane().add(E);
		jf.setVisible(true);
		E.init();
		if (debug)
			System.out.println("[PathUtilTest] Console created");
		// main task

		PathUtil p = new PathUtil(this);
		String fileName = p.data_path
				+ "tests/core/util/PathUtil/inputFile.txt";
		if (debug) {
			System.out.println("[PathUtilTest current_path] " + p.current_path);
			System.out.println("[PathUtilTest root_path] " + p.root_path);
			System.out.println("[PathUtilTest data_path] " + p.data_path);
			System.out.println("[PathUtilTest fileName] " + fileName);
		}

//		String fileName = "http://jat.sourceforge.net/jat/data/tests/core/util/PathUtil/inputFile.txt";

		try {
			// Create a URL for the desired page
			URL url = new URL(fileName);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					url.openStream()));
			String str;
			while ((str = in.readLine()) != null) {
				System.out.println(str);
			}
			in.close();
		} catch (MalformedURLException e) {
		} catch (IOException e) {
		}


	}

}



//try {
//URL url2 = new URL("/");
//System.out.println("[PathUtilTest] url . " + url2.toString());
//} catch (MalformedURLException e1) {
//// TODO Auto-generated catch block
//e1.printStackTrace();
//}

// EasyReader inFile = new EasyReader(fileName);
// String Line = inFile.readLine();
// if (Line != null){
// System.out.println("The first line is  : " + Line);
// Line = inFile.readLine();
// System.out.println("The second line is : " + Line);
// Line = inFile.readLine();
// System.out.println("The third line is : " + Line);
// }


//File here = new File(".");
//File root = new File(here.getAbsolutePath()); // is not it something?
//if (debug) {
//	System.out.println("[PathUtilTest] here getAbsolutePath "
//			+ here.getAbsolutePath());
//	System.out.println("[PathUtilTest] here getPath " + here.getPath());
//	System.out.println("[PathUtilTest] root getAbsolutePath "
//			+ root.getAbsolutePath());
//	try {
//		System.out.println("[PathUtilTest] getCanonicalPath "
//				+ root.getCanonicalPath());
//	} catch (IOException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}

//}

//System.out.println("[PathUtilTest] getCodeBase "
//			+ this.getCodeBase());	

