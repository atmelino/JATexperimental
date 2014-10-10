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

package jat.application.missionPlan;

import jat.core.ephemeris.DE405Frame.frame;
import jat.core.ephemeris.DE405Plus;
import jat.core.util.PathUtil;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

public class MissionPlanMain extends JApplet {

	private static final long serialVersionUID = 1L;
	public MissionPlanGUI mpGUI;
	public MissionPlanPlot mpPlot;
	public MissionPlanParameters mpParam;
	static int appletwidth = 900; // Width of Applet
	static int appletheight = 700;
	static com.tornadolabs.j3dtree.Java3dTree m_Java3dTree = null;
	private static boolean Java3dTree_debug = false;
	Container level1_Pane;
	JFrame sFrame;
	List<Flight> flightList = new ArrayList<Flight>();
	public JTextPane textPane;
	public JTextArea textArea;

	public void init() {
		// Create a text pane.
		textPane = new JTextPane();
//		JScrollPane paneScrollPane = new JScrollPane(textPane);
//		paneScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
//		paneScrollPane.setPreferredSize(new Dimension(300, 155));
//		paneScrollPane.setMinimumSize(new Dimension(10, 10));
//		getContentPane().add(paneScrollPane, BorderLayout.NORTH);

		// Redirect stdout and stderr to the text pane
		// MessageConsole mc = new MessageConsole(textPane);
		// mc.redirectOut();
		// mc.redirectErr(Color.RED, null);
		// mc.setMessageLines(100);

		textArea = new JTextArea(5, 20);
		JScrollPane paneScrollPane = new JScrollPane(textArea);
		paneScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		paneScrollPane.setPreferredSize(new Dimension(300, 155));
		paneScrollPane.setMinimumSize(new Dimension(10, 10));
		getContentPane().add(paneScrollPane, BorderLayout.NORTH);

		//System.out.println("[MissionPlanMain]");

	}

	public void start() {
		 //Message console
		// MissionPlanConsole E = new MissionPlanConsole();
		// JFrame jf = new JFrame();
		// jf.setSize(800, 400);
		// jf.getContentPane().add(E);
		// jf.setVisible(true);
		// E.init();

		mpParam = new MissionPlanParameters();
		mpParam.path = new PathUtil(this,mpParam.messages);
		mpParam.Eph = new DE405Plus(mpParam.path,mpParam.messages);
		mpParam.Eph.setFrame(frame.HEE);

		//mpParam.messages.printMessages();
		
		mpGUI = new MissionPlanGUI(this);
		mpPlot = new MissionPlanPlot(this);
		level1_Pane = getContentPane();
		level1_Pane.add(mpGUI, BorderLayout.WEST);
		level1_Pane.add(mpPlot, BorderLayout.CENTER);

		// if (debug)
		// System.out.println("[PathUtilTest] Console created");
		//System.out.println("[MissionPlanMain before starting timer]");
		mpGUI.mpE.timer.start();

		//mpParam.messages.printMessages();
		//mpParam.messages.printMessagesToTextArea(textArea);

	}

	/**
	 * Used when run as an application
	 */
	// public static void main(String[] args) {
	// MissionPlanMain mApplet = new MissionPlanMain();
	// mApplet.init();
	//
	// mApplet.sFrame = new JFrame();
	// mApplet.sFrame.setTitle("JAT Solar System Mission Planner");
	// mApplet.sFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	//
	// // When running this file as a stand-alone app, add the applet to
	// // the frame.
	// mApplet.sFrame.getContentPane().add(mApplet, BorderLayout.CENTER);
	// mApplet.sFrame.setSize(appletwidth, appletheight);
	// mApplet.sFrame.setVisible(true);
	//
	// // sApplet.ssp.mouseZoom.setupCallback(sApplet.ssE);
	// mApplet.mpPlot.requestFocusInWindow();
	//
	// // mApplet.mpGUI.mpE.timer.start();
	// mApplet.start();
	// if (Java3dTree_debug) {
	// m_Java3dTree = new com.tornadolabs.j3dtree.Java3dTree();
	// m_Java3dTree.recursiveApplyCapability(mApplet.mpPlot.jatScene);
	// m_Java3dTree.updateNodes(mApplet.mpPlot.universe);
	// }
	// }// End of main()

}
