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

package jat.application.AttitudeSimulator;


import jat.core.util.ResourceLoader;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.io.IOException;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;



public class HelpWindow extends JFrame {

	private static final long serialVersionUID = 1L;

	public HelpWindow(String name) throws HeadlessException {

		super("Simulation Instruction");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		JLabel emptyLabel = new JLabel("");
		emptyLabel.setPreferredSize(new Dimension(175, 100));
		
		JScrollPane helpScrollPane = new JScrollPane(createEditorPane(name));

		getContentPane().add(helpScrollPane, BorderLayout.CENTER);

		pack();
		setVisible(true);
	}

	private JEditorPane createEditorPane(String relative_path) {

		
		JEditorPane editorPane = new JEditorPane();
		editorPane.setEditable(false);
		editorPane.setBackground(Color.white);
		Dimension mysize = new Dimension(600, 400);
		editorPane.setPreferredSize(mysize);

		try {
			ResourceLoader c=new ResourceLoader();
			URL helpURL2 = c.loadURL(this.getClass(), relative_path);

			displayURL(helpURL2, editorPane, relative_path);

		} catch (Exception e) {
			System.err.println("Couldn't create help URL!!: " + relative_path);
			System.exit(0);
		}

		return editorPane;
	}

	/**
	 * Displays URL
	 * 
	 * @author Sun's Swing tutorial example
	 * @param url
	 *            (URL)
	 * @param editorPane
	 *            (JEditorPane)
	 * @param fileName
	 *            (String)
	 */
	private void displayURL(URL url, JEditorPane editorPane, String fileName) {
		try {
			editorPane.setPage(url);
		} catch (IOException e) {
			System.err.println("Attempted to read a bad URL: " + url);
		}
	}

}
