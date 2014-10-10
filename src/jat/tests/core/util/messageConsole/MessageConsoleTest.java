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

// Description: The Java applet technology holds great promise for software deployment because no installation
// should be necessary. All the user has to do is to navigate a web browser to a given Web address. 
// However, in reality, when something goes wrong such as jar files or data files not being found, all the user sees is 
// a grey area in the web page and no indication as to what can be done to fix it. Java is very good at
// helping diagnose because of the exception management. The messages from the exception management can even 
// help pinpoint a line in the code where the error occurred, but it requires some effort to find the place where these 
// messages are printed. Web applets usually don't have a console window. 
// The goal of this package is to test a method where the first applet simply displays the stderr and stdout messages,
// and then the actual application is loaded. That way, the user always sees something other than a grey area, and
// as the parts and data files of the application are loaded, the user gets messages indicating the progress and 
// giving helpful messages if something goes wrong so that the user knows what to do to fix it.
//

package jat.tests.core.util.messageConsole;

import jat.core.util.messageConsole.MessageConsole;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class MessageConsoleTest extends JApplet {

	private static final long serialVersionUID = -1835188793980442880L;

	public MessageConsoleTest() {
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
		System.out.println("MessageConsoleTest");
		System.out.println("redirected stdout output:");
		for (int i = 0; i < 4; i++)
			System.out.println("line" + i);
		System.out.println("redirected stderr output:");

	}

	public static void main(String[] args) {
		// This class can run both as an applet or an application.
		// as an applet, the constructor of the class is called.
		// as an application, main is called. main will create a
		// new frame and then add the applet inside the frame.

		Component applet = new MessageConsoleTest();
		JFrame frame = new JFrame("My applet, as application");
		frame.getContentPane().add(applet);
		frame.setSize(600, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);

		// Frame frame =
		// new MainFrame(new deployWeb(), 256, 256);

	}

}
