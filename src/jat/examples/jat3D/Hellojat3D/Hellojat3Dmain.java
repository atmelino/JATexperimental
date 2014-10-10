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

package jat.examples.jat3D.Hellojat3D;

import java.awt.BorderLayout;

import javax.swing.JFrame;

public class Hellojat3Dmain {
	private static Hellojat3Dplot helloplot;
	static int appletwidth = 900; // Width of Applet
	static int appletheight = 700;

	/**
	 * Used when run as an application
	 * 
	 * @param args
	 *            (String[]) Argument
	 */
	public static void main(String[] args) {
		helloplot = new Hellojat3Dplot();

		JFrame sFrame = new JFrame();
		sFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// When running this file as a stand-alone app, add the applet to
		// the frame.
		sFrame.getContentPane().add(helloplot, BorderLayout.CENTER);
		sFrame.setSize(appletwidth, appletheight);
		sFrame.setVisible(true);

	}// End of main()

}
