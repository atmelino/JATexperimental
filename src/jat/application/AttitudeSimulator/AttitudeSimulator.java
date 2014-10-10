/* JAT: Java Astrodynamics Toolkit
 *
 * Copyright (c) 2003 National Aeronautics and Space Administration. All rights reserved.
 * 
 * This file is part of JAT. JAT is free software; you can
 * redistribute it and/or modify it under the terms of the
 * NASA Open Source Agreement
 * 
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * NASA Open Source Agreement for more details.
 *
 * You should have received a copy of the NASA Open Source Agreement
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 */

package jat.application.AttitudeSimulator;

import jat.coreNOSA.attitude.DegToQuat;
import jat.jat3D.util;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

/**
 * <P>
 * AttitudeSimulator is a JApplet developed as a Master's thesis Project at
 * California Polytechnic State University, San Luis Obispo (Cal Poly)
 * 
 * @author Noriko Takada
 * @version 1.2 (03/09/2004) Modification since the last version Removed: import
 *          javax.swing.border.Border; import javax.swing.border.EtchedBorder;
 *          import javax.swing.border.LineBorder; import javax.swing.event.*;
 *          import jat.attitude.util.AnimationWindow;
 * @author Tobias Berthold
 * @version 1.3 (04/03/2012) Modifications: change layout so that start button
 *          is visible. Load help pages locally.
 * 
 */

public class AttitudeSimulator extends JApplet // implements ItemListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5907632930552441343L;
	private static AttitudeSimulator theApplet; // Applet itself!
	private static JFrame theFrame; // Frame is used in main()
	private static String LastUpdate = "Last Update: 03/31/2012, 12:25";

	// Instantiate a Font object
	Font fancyFont = new Font("Serif", Font.BOLD | Font.ITALIC, 25);
	Font titlef = new Font("Dialog", Font.BOLD, 16);
	Font boldf = new Font("Dialog", Font.BOLD, 12);
	Font italicf = new Font("Dialog", Font.ITALIC, 12);
	Font normalf = new Font("Dialog", Font.PLAIN, 12);

	/* GUI Dimensions */
	static int appletwidth = 750; // Width of Applet
	static int appletheight = 500;
	// static int comboBoxPane_width = 750;
	// static int thirdPanelWidth = 200;

	// Dimension of J components
	// Dimension maxSize = new Dimension(150, 20);
	// Dimension level3_Pane_simulation_size = new Dimension(750, appletheight);
	// // width, height
	// Dimension level3_Pane_conversion_size = new Dimension(200, appletheight);
	// Dimension panelSize = new Dimension(755, 400);
	// Dimension scrollPanelSize = new Dimension(755, 300);
	// Dimension inertiaPaneSize = new Dimension(525, 50);

	/* Buttons */
	JButton startButton = new JButton("Start Button");
	JButton convertButton = new JButton("Convert Button");
	JButton resetButton = new JButton("Reset Button");
	JButton simHelpButton = new JButton("Simulation Help");

	JComboBox combo;
	JCheckBox twoDeeBox;
	JCheckBox threeDeeBox;
	JRadioButton threeRWRadio;
	JRadioButton fourRWRadio;

	/* Note: Each case is identified by the number from 1 to 8 */
	/* (1) Constant Torque case */
	String input1row1[] = { "Ixx", "Iyy", "Izz" };
	String input1row2[] = { "M1", "M2", "M3" };
	String input1row3[] = { "w1", "w2", "w3" };
	String input1row4[] = { "q1", "q2", "q3", "q4" };
	int length1row1 = input1row1.length;
	int length1row2 = input1row2.length;
	int length1row3 = input1row3.length;
	int length1row4 = input1row4.length;
	JTextField inputField1row1[] = new JTextField[length1row1];
	JTextField inputField1row2[] = new JTextField[length1row2];
	JTextField inputField1row3[] = new JTextField[length1row3];
	JTextField inputField1row4[] = new JTextField[length1row4];
	// Default Initial Condition
	String defaultIc1row1[] = { "10.42", "35.42", "41.67" };
	String defaultIc1row2[] = { "0.0", "0.0", "1.0" };
	String defaultIc1row3[] = { "0.0", "0.0", "0.0" };
	String defaultIc1row4[] = { "0.0", "0.0", "0.0", "1.0" };

	/* (2) Gravity Gradient (Circular Orbit) case */
	String input2row1[] = { "Ixx", "Iyy", "Izz" };
	String input2row2[] = { "w1", "w2", "w3" };
	String input2row3[] = { "q1", "q2", "q3", "q4" };
	int length2row1 = input2row1.length;
	int length2row2 = input2row2.length;
	int length2row3 = input2row3.length;
	JTextField inputField2row1[] = new JTextField[length2row1];
	JTextField inputField2row2[] = new JTextField[length2row2];
	JTextField inputField2row3[] = new JTextField[length2row3];
	String defautlIc2row1[] = { "10.42", "35.42", "41.67" };
	String defaultIc2row2[] = { "0.1", "0.0", "1.0" };
	String defaultIc2row3[] = { "0.0", "0.0", "0.0", "1.0" };

	/* (3) Gravity Gradient (Eccentric Orbit) case */
	String input3row1[] = { "Ixx", "Iyy", "Izz" };
	String input3row2[] = { "e" };
	String input3row3[] = { "w1", "w2", "w3" };
	String input3row4[] = { "q1", "q2", "q3", "q4" };
	int length3row1 = input3row1.length;
	int length3row2 = input3row2.length;
	int length3row3 = input3row3.length;
	int length3row4 = input3row4.length;
	JTextField inputField3row1[] = new JTextField[length3row1];
	JTextField inputField3row2[] = new JTextField[length3row2];
	JTextField inputField3row3[] = new JTextField[length3row3];
	JTextField inputField3row4[] = new JTextField[length3row4];
	String defaultIc3row1[] = { "10.42", "35.42", "41.67" };
	String defaultIc3row2[] = { "0.3" };
	String defaultIc3row3[] = { "0.0", "0.0", "1.0" };
	String defaultIc3row4[] = { "0.0", "0.0", "0.0", "1.0", "0.3" };

	/* (4) Spherical Damper case */
	String input4row1[] = { "Ixx", "Iyy", "Izz" };
	String input4row2[] = { "c", "j" };
	String input4row3[] = { "w1", "w2", "w3" };
	String input4row4[] = { "q1", "q2", "q3", "q4" };
	String input4row5[] = { "alpha", "beta", "gamma" };
	int length4row1 = input4row1.length;
	int length4row2 = input4row2.length;
	int length4row3 = input4row3.length;
	int length4row4 = input4row4.length;
	int length4row5 = input4row5.length;
	JTextField inputField4row1[] = new JTextField[length4row1];
	JTextField inputField4row2[] = new JTextField[length4row2];
	JTextField inputField4row3[] = new JTextField[length4row3];
	JTextField inputField4row4[] = new JTextField[length4row4];
	JTextField inputField4row5[] = new JTextField[length4row5];
	String defaultIc4row1[] = { "10.42", "35.42", "41.67" };
	String defaultIc4row2[] = { "5", "5" };
	String defaultIc4row3[] = { "0.0", "0.0", "1.0" };
	String defaultIc4row4[] = { "0.0", "0.0", "0.0", "1.0" };
	String defaultIc4row5[] = { "0.0", "0.0", "0.0" };

	/* (5) 4RW Control case */
	String input5row1[] = { "Ixx", "Iyy", "Izz" };
	String input5row2[] = { "J", "RW angle" };
	String input5row3[] = { "psi", "theta", "phi" };
	String input5row4[] = { "w1", "w2", "w3" };
	String input5row5[] = { "q1", "q2", "q3", "q4" };
	String input5row6[] = { "Omega1", "Omega2", "Omega3", "Omega4" };
	int length5row1 = input5row1.length;
	int length5row2 = input5row2.length;
	int length5row3 = input5row3.length;
	int length5row4 = input5row4.length;
	int length5row5 = input5row5.length;
	int length5row6 = input5row6.length;
	JTextField inputField5row1[] = new JTextField[length5row1];
	JTextField inputField5row2[] = new JTextField[length5row2];
	JTextField inputField5row3[] = new JTextField[length5row3];
	JTextField inputField5row4[] = new JTextField[length5row4];
	JTextField inputField5row5[] = new JTextField[length5row5];
	JTextField inputField5row6[] = new JTextField[length5row6];
	String defaultIc5row1[] = { "10.42", "35.42", "41.67" };
	String defaultIc5row2[] = { "10.0", "25" };
	String defaultIc5row3[] = { "10", "60", "50" };
	String defaultIc5row4[] = { "0.0", "0.0", "0.0" };
	String defaultIc5row5[] = { "0.0", "0.0", "0.0", "1.0" };
	String defaultIc5row6[] = { "0.0", "0.0", "0.0", "0.0" };

	/* (6) 3CMG Control case */
	String input6row1[] = { "Ixx", "Iyy", "Izz" };
	String input6row2[] = { "J", "A" };
	String input6row3[] = { "psi", "theta", "phi" };
	String input6row4[] = { "w1", "w2", "w3" };
	String input6row5[] = { "q1", "q2", "q3", "q4" };
	String input6row6[] = { "Omega1", "Omega2", "Omega3" };
	int length6row1 = input6row1.length;
	int length6row2 = input6row2.length;
	int length6row3 = input6row3.length;
	int length6row4 = input6row4.length;
	int length6row5 = input6row5.length;
	int length6row6 = input6row6.length;
	JTextField inputField6row1[] = new JTextField[length6row1];
	JTextField inputField6row2[] = new JTextField[length6row2];
	JTextField inputField6row3[] = new JTextField[length6row3];
	JTextField inputField6row4[] = new JTextField[length6row4];
	JTextField inputField6row5[] = new JTextField[length6row5];
	JTextField inputField6row6[] = new JTextField[length6row6];
	String defaultIc6row1[] = { "10.42", "35.42", "41.67" };
	String defaultIc6row2[] = { "10.0", "25" };
	String defaultIc6row3[] = { "10", "60", "50" };
	String defaultIc6row4[] = { "0.0", "0.0", "0.1" };
	String defaultIc6row5[] = { "0.0", "0.0", "0.0", "1.0" };
	String defaultIc6row6[] = { "0.0", "0.0", "0.0" };

	/* (7) Bang-Bang Control */

	String input7row1[] = { "J" };
	String input7row2[] = { "wn", "damping" };
	String input7row3[] = { "K", "Kd", "K_Bang", "Kd_Bang" };
	String input7row4[] = { "Torque", "Dead Zone", "theta_com" };
	String input7row5[] = { "theta", "thetaDot", "thetaBang", "thetaDotBang" };
	int length7row1 = input7row1.length;
	int length7row2 = input7row2.length;
	int length7row3 = input7row3.length;
	int length7row4 = input7row4.length;
	int length7row5 = input7row5.length;
	JTextField inputField7row1[] = new JTextField[length7row1];
	JTextField inputField7row2[] = new JTextField[length7row2];
	JTextField inputField7row3[] = new JTextField[length7row3];
	JTextField inputField7row4[] = new JTextField[length7row4];
	JTextField inputField7row5[] = new JTextField[length7row5];
	String defaultIc7row1[] = { "600" };
	String defaultIc7row2[] = { "1", "1" };
	String defaultIc7row3[] = { "0", "0", "0", "0" };
	String defaultIc7row4[] = { "1", "0.001", "2.0" };
	String defaultIc7row5[] = { "0", "0", "0", "0" };

	/* (8) 2D Simple Flexible Spacecraft */

	String input8row1[] = { "J" };
	String input8row2[] = { "m", "a", "L", "EI" };
	String input8row3[] = { "K", "Kd", "alpha_com" };
	String input8row4[] = { "Alpha", "u1", "u2" };
	String input8row5[] = { "AlphaDot", "u1Dot", "u2Dot" };
	int length8row1 = input8row1.length;
	int length8row2 = input8row2.length;
	int length8row3 = input8row3.length;
	int length8row4 = input8row4.length;
	int length8row5 = input8row5.length;
	JTextField inputField8row1[] = new JTextField[length8row1];
	JTextField inputField8row2[] = new JTextField[length8row2];
	JTextField inputField8row3[] = new JTextField[length8row3];
	JTextField inputField8row4[] = new JTextField[length8row4];
	JTextField inputField8row5[] = new JTextField[length8row5];
	String defaultIc8row1[] = { "13.35" };
	String defaultIc8row2[] = { "1", "0.40", "1.80", "15.52" };
	String defaultIc8row3[] = { " 0.0", "0.0", "0.0" };
	String defaultIc8row4[] = { "0", "0.1", "0.1" };
	String defaultIc8row5[] = { "0", "0", "0" };

	/* (9) 3D Simple Flexible Spacecraft */

	String input9row1[] = { "Ixx", "Iyy", "Izz" };
	String input9row2[] = { "m", "a", "L", "EI" };
	String input9row3[] = { "u1", "u2", "psi", "theta", "phi" };
	String input9row4[] = { "u1Dot", "u2Dot", "psiDot", "thetaDot", "phiDot" };
	int length9row1 = input9row1.length;
	int length9row2 = input9row2.length;
	int length9row3 = input9row3.length;
	int length9row4 = input9row4.length;
	JTextField inputField9row1[] = new JTextField[length9row1];
	JTextField inputField9row2[] = new JTextField[length9row2];
	JTextField inputField9row3[] = new JTextField[length9row3];
	JTextField inputField9row4[] = new JTextField[length9row4];
	String defaultIc9row1[] = { "45.42", "35.42", "13.35" };
	String defaultIc9row2[] = { "1", "0.40", "1.80", "15.52" };
	String defaultIc9row3[] = { "0.1", "0.1", "0", "0", "0" };
	String defaultIc9row4[] = { "0", "0", "0", "0", "0" };

	/* Euler Angle Converter */

	JTextFieldPanel eulerPanel;
	JTextFieldPanel quaternionPanel;
	String eulerInput[] = { "Psi", "Theta", "Phi" };
	String quaternionOutput[] = { "q1", "q2", "q3", "q4" };
	int lengthEulerInput = eulerInput.length;
	int lengthQuaternionOutput = quaternionOutput.length;
	JTextField eulerInputField[] = new JTextField[lengthEulerInput];
	JTextField quaternionOutputField[] = new JTextField[lengthQuaternionOutput];
	String defaultEulerInput[] = { "0", "0", "0" };
	String defaultQuaternionOuputField[] = { "0", "0", "0", "1" };

	/* Simulation Control Panel */
	JTextFieldPanel controlPanel;
	String controlInput[] = { "time step", "time" };
	int lengthControlInput = controlInput.length;
	JTextField controlInputField[] = new JTextField[lengthControlInput];
	String defaultControlInput[] = { "0.1", "20" };

	/* CardLayout-JPanel for scenario specific inputs */
	JPanel inputCards;
	// JPanel instructionCards;
	final static String SCENARIO_1 = "1. Constant Torque";
	final static String SCENARIO_2 = "2. GG Circular";
	final static String SCENARIO_3 = "3. GG Eccentric";
	final static String SCENARIO_4 = "4. Spherical Damper";
	final static String SCENARIO_5 = "5. Reaction Wheel Maneuver";
	final static String SCENARIO_6 = "6. CMG Maneuver";
	final static String SCENARIO_7 = "7. 1 axis Bang-Bang Control";
	final static String SCENARIO_8 = "8. Simple 2D Flexible S/C";
	final static String SCENARIO_9 = "9. Simple 3D Flexible S/C";

	final static String[] helpfile = { "ConstantTorque.html",
			"GGCircular.html", "GGEccentric.html", "SphericalDamper.html",
			"FourRWControl.html", "CMGControl.html", "BangBangControl.html",
			"Simple2DFlexSC.html", "Simple3DFlexSC.html" };

	/* Declare Panels for simulation inputs for each panels */
	JTextFieldPanel inertiaPane;
	JTextFieldPanel inertiaJPane;
	JPanel scenario1Pane;
	JPanel scenario2Pane;
	JPanel scenario3Pane;
	JPanel scenario4Pane;
	JPanel scenario5Pane;
	JPanel scenario6Pane;
	JPanel scenario7Pane;
	JPanel scenario8Pane;
	JPanel scenario9Pane;

	// FileUtil2 f = new FileUtil2();

	/**
	 * Builds the applet's graphical user interface
	 */
	public void init() {

		/* Register ActionListener to the buttons */
		startButton.addActionListener(new ButtonHandler());
		convertButton.addActionListener(new ButtonHandler());
		resetButton.addActionListener(new ButtonHandler());
		simHelpButton.addActionListener(new ButtonHandler());

		/* Build Input Panels */
		// Put the JComboBox in a JPanel
		String comboBoxItems[] = { SCENARIO_1, SCENARIO_2, SCENARIO_3,
				SCENARIO_4, SCENARIO_5, SCENARIO_6, SCENARIO_7, SCENARIO_8,
				SCENARIO_9 };
		combo = new JComboBox(comboBoxItems);
		combo.setEditable(false);
		combo.setBackground(Color.cyan);
		combo.setAlignmentX(LEFT_ALIGNMENT);
		combo.addItemListener(new ChoiceHandler());

		JLabel comboLabel = new JLabel("Please select a scenario:");
		comboLabel.setAlignmentX(LEFT_ALIGNMENT);

		JPanel level5_Pane_top_left = new JPanel();
		level5_Pane_top_left.setLayout(new BoxLayout(level5_Pane_top_left,
				BoxLayout.Y_AXIS));
		level5_Pane_top_left.setBackground(Color.pink);
		level5_Pane_top_left.add(comboLabel);
		level5_Pane_top_left.add(Box.createRigidArea(new Dimension(0, 5)));
		level5_Pane_top_left.add(combo);
		level5_Pane_top_left.add(Box.createRigidArea(new Dimension(0, 5)));

		JPanel level5_Pane_top_right = new JPanel();
		level5_Pane_top_right.setLayout(new BoxLayout(level5_Pane_top_right,
				BoxLayout.Y_AXIS));
		level5_Pane_top_right.setBackground(Color.pink);
		level5_Pane_top_right.add(new JLabel(LastUpdate));
		level5_Pane_top_right.add(Box.createRigidArea(new Dimension(0, 5)));
		level5_Pane_top_right.add(simHelpButton);
		level5_Pane_top_right.add(Box.createRigidArea(new Dimension(0, 5)));

		JPanel level4_Pane_scenario = new JPanel();
		level4_Pane_scenario.setBackground(Color.pink);
		level4_Pane_scenario.setLayout(new BoxLayout(level4_Pane_scenario,
				BoxLayout.X_AXIS));
		level4_Pane_scenario.add(level5_Pane_top_left);
		level4_Pane_scenario.add(Box.createRigidArea(new Dimension(10, 0)));
		level4_Pane_scenario.add(level5_Pane_top_right);

		// Input panels
		makeInputPanels();

		/* Add Scenario panels to the card Panel */
		/* Associate the comboBox elements with the corresponding JPanel */
		inputCards = new JPanel();
		// inputCards.setPreferredSize(panelSize);
		// inputCards.setMinimumSize(panelSize);
		// inputCards.setMaximumSize(panelSize);
		inputCards.setAlignmentX(LEFT_ALIGNMENT);
		inputCards.setBackground(Color.white);

		inputCards.setLayout(new CardLayout());
		inputCards.add(SCENARIO_1, scenario1Pane);
		inputCards.add(SCENARIO_2, scenario2Pane);
		inputCards.add(SCENARIO_3, scenario3Pane);
		inputCards.add(SCENARIO_4, scenario4Pane);
		inputCards.add(SCENARIO_5, scenario5Pane);
		inputCards.add(SCENARIO_6, scenario6Pane);
		inputCards.add(SCENARIO_7, scenario7Pane);
		inputCards.add(SCENARIO_8, scenario8Pane);
		inputCards.add(SCENARIO_9, scenario9Pane);

		JScrollPane level5_inputScrollPane = new JScrollPane(inputCards);
		level5_inputScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		// level4_inputScrollPane.setPreferredSize(scrollPanelSize);
		// level4_inputScrollPane.setMinimumSize(scrollPanelSize);
		level5_inputScrollPane.setAlignmentX(LEFT_ALIGNMENT);
		level5_inputScrollPane.setBackground(Color.cyan);

		JPanel level4_inputPane = new JPanel();
		level4_inputPane.setLayout(new BorderLayout());
		level4_inputPane.add(level5_inputScrollPane);

		// Create the check boxes.
		twoDeeBox = new JCheckBox("2D Plots");
		twoDeeBox.setSelected(true);
		twoDeeBox.setBackground(Color.pink);
		threeDeeBox = new JCheckBox("3D Animation");
		threeDeeBox.setSelected(false);
		threeDeeBox.setBackground(Color.pink);
		// Register a listener for the check boxes.
		twoDeeBox.addItemListener(new ButtonHandler());
		threeDeeBox.addItemListener(new ButtonHandler());

		// Create a JPanel for putting check boxes
		JPanel level6_Pane_simcheckboxes = new JPanel();
		level6_Pane_simcheckboxes.setLayout(new BoxLayout(
				level6_Pane_simcheckboxes, BoxLayout.X_AXIS));
		// level6_Pane_simcheckboxes.setPreferredSize(new
		// Dimension(comboBoxPane_width, 30));
		// level5_Pane_simcheckboxes.setMaximumSize(new
		// Dimension(comboBoxPane_width, 30));
		// level5_Pane_simcheckboxes.setMinimumSize(new
		// Dimension(comboBoxPane_width, 30));
		level6_Pane_simcheckboxes.setBackground(Color.pink);
		level6_Pane_simcheckboxes.setAlignmentX(LEFT_ALIGNMENT);
		level6_Pane_simcheckboxes.add(twoDeeBox);
		level6_Pane_simcheckboxes.add(threeDeeBox);

		JPanel level5_Pane_bottom_left = new JPanel();
		level5_Pane_bottom_left.setLayout(new BoxLayout(
				level5_Pane_bottom_left, BoxLayout.Y_AXIS));
		// level5_Pane_bottom_left.setPreferredSize(new
		// Dimension(comboBoxPane_width/2, 100));
		// level5_Pane_bottom_left.setMaximumSize(new
		// Dimension(comboBoxPane_width/2, 100));
		// level5_Pane_left.setMinimumSize(new Dimension(comboBoxPane_width/2,
		// 100));
		level5_Pane_bottom_left.setBackground(Color.pink);
		level5_Pane_bottom_left.setAlignmentX(LEFT_ALIGNMENT);
		level5_Pane_bottom_left.add(Box.createRigidArea(new Dimension(0, 5)));
		level5_Pane_bottom_left.add(new JLabel("Simulation Time"));
		level5_Pane_bottom_left.add(Box.createRigidArea(new Dimension(0, 5)));
		for (int i = 0; i < lengthControlInput; ++i)
			controlInputField[i] = new JTextField(defaultControlInput[i], 5);
		controlPanel = new JTextFieldPanel(1, "", controlInput,
				controlInputField, Color.pink);
		level5_Pane_bottom_left.add(controlPanel);

		JPanel level5_Pane_bottom_center = new JPanel();
		level5_Pane_bottom_center.setLayout(new BoxLayout(
				level5_Pane_bottom_center, BoxLayout.Y_AXIS));
		level5_Pane_bottom_center.setBackground(Color.pink);
		level5_Pane_bottom_center.add(Box.createRigidArea(new Dimension(0, 5)));
		level5_Pane_bottom_center.add(new JLabel("Output Option"));
		level5_Pane_bottom_center.add(Box.createRigidArea(new Dimension(0, 5)));
		level5_Pane_bottom_center.add(level6_Pane_simcheckboxes);

		JPanel level5_Pane_bottom_right = new JPanel();
		level5_Pane_bottom_right.setLayout(new BoxLayout(
				level5_Pane_bottom_right, BoxLayout.Y_AXIS));
		// level5_Pane_bottom_right.setPreferredSize(new
		// Dimension(comboBoxPane_width/2, 80));
		// level5_Pane_bottom_right.setMaximumSize(new
		// Dimension(comboBoxPane_width/2, 80));
		// level5_Pane_right.setMinimumSize(new Dimension(comboBoxPane_width/2,
		// 80));
		level5_Pane_bottom_right.setBackground(Color.pink);
		level5_Pane_bottom_right.setAlignmentX(LEFT_ALIGNMENT);
		level5_Pane_bottom_right.add(Box.createRigidArea(new Dimension(0, 5)));
		level5_Pane_bottom_right.add(new JLabel("Simulation Start"));
		level5_Pane_bottom_right.add(Box.createRigidArea(new Dimension(0, 5)));
		level5_Pane_bottom_right.add(startButton);

		JPanel level4_Pane_simulation_control = new JPanel();
		level4_Pane_simulation_control.setBackground(Color.pink);
		level4_Pane_simulation_control.setLayout(new BoxLayout(
				level4_Pane_simulation_control, BoxLayout.X_AXIS));
		level4_Pane_simulation_control.add(level5_Pane_bottom_left);
		level4_Pane_simulation_control.add(Box.createRigidArea(new Dimension(
				10, 0)));
		level4_Pane_simulation_control.add(level5_Pane_bottom_center);
		level4_Pane_simulation_control.add(Box.createRigidArea(new Dimension(
				10, 0)));
		level4_Pane_simulation_control.add(level5_Pane_bottom_right);

		// Simulation Pane
		JPanel level3_Pane_simulation = new JPanel();
		// level3_Pane_simulation.setMaximumSize(level3_Pane_simulation_size);
		// level3_Pane_simulation.setPreferredSize(level3_Pane_simulation_size);
		// level3_Pane_simulation.setMinimumSize(level3_Pane_simulation_size);
		level3_Pane_simulation.setLayout(new BoxLayout(level3_Pane_simulation,
				BoxLayout.PAGE_AXIS));
		level3_Pane_simulation.setAlignmentX(LEFT_ALIGNMENT);
		level3_Pane_simulation.setBackground(Color.pink);
		TitledBorder titled;
		titled = BorderFactory.createTitledBorder("");
		level3_Pane_simulation.setBorder(titled);
		level3_Pane_simulation.add(level4_Pane_scenario);
		level3_Pane_simulation.add(level4_inputPane);
		level3_Pane_simulation.add(level4_Pane_simulation_control);

		// Conversion Pane
		JPanel level3_Pane_conversion = new JPanel();
		// level3_Pane_conversion.setMaximumSize(level3_Pane_conversion_size);
		// level3_Pane_conversion.setPreferredSize(level3_Pane_conversion_size);
		// level3_Pane_conversion.setMinimumSize(level3_Pane_conversion_size);
		level3_Pane_conversion.setLayout(new BoxLayout(level3_Pane_conversion,
				BoxLayout.Y_AXIS));
		level3_Pane_conversion.setBackground(Color.pink);
		level3_Pane_conversion.setBorder(BorderFactory.createTitledBorder(""));

		level3_Pane_conversion.add(new JLabel("Euler Angle Converter"));
		level3_Pane_conversion.add(new JLabel("Sequence: 3-2-1"));
		for (int i = 0; i < lengthEulerInput; ++i)
			eulerInputField[i] = new JTextField(defaultEulerInput[i], 5);
		eulerPanel = new JTextFieldPanel(2, "Input", eulerInput,
				eulerInputField, Color.pink);
		for (int i = 0; i < lengthQuaternionOutput; ++i)
			quaternionOutputField[i] = new JTextField(
					defaultQuaternionOuputField[i], 5);
		for (int i = 0; i < lengthQuaternionOutput; ++i)
			quaternionOutputField[i].setEditable(false);
		quaternionPanel = new JTextFieldPanel(2, "Output", quaternionOutput,
				quaternionOutputField, Color.pink);
		level3_Pane_conversion.add(eulerPanel);
		level3_Pane_conversion.add(quaternionPanel);
		level3_Pane_conversion.add(Box.createRigidArea(new Dimension(0, 5)));
		level3_Pane_conversion.add(convertButton);
		level3_Pane_conversion.add(Box.createRigidArea(new Dimension(0, 5)));
		level3_Pane_conversion.add(resetButton);
		level3_Pane_conversion.add(Box.createRigidArea(new Dimension(0, 5)));
		level3_Pane_conversion.add(Box.createRigidArea(new Dimension(0, 5)));
		level3_Pane_conversion.add(new JLabel("Suggested gain from"));
		level3_Pane_conversion.add(new JLabel("Linear Control Theory"));
		level3_Pane_conversion.add(new JLabel("K = wn*wn*J"));
		level3_Pane_conversion.add(new JLabel("Kd = 2*J*damping*wn"));
		level3_Pane_conversion.add(Box.createVerticalGlue());

		JPanel level2_Pane = new JPanel();
		level2_Pane.setLayout(new BoxLayout(level2_Pane, BoxLayout.X_AXIS));
		level2_Pane.setBackground(Color.pink);
		level2_Pane.add(level3_Pane_simulation);
		level2_Pane.add(level3_Pane_conversion);

		Container level1_Pane = getContentPane();
		level1_Pane.add(level2_Pane, BorderLayout.CENTER);
		this.setSize(appletwidth, appletheight);
		// System.out.println("applet init end");

	}// End of init()

	/**
	 * Makes panels for prompting users to input necessary fields
	 */
	void makeInputPanels() {

		scenario1Pane = new JPanel();
		for (int i = 0; i < length1row1; ++i)
			inputField1row1[i] = new JTextField(defaultIc1row1[i], 5);
		for (int i = 0; i < length1row2; ++i)
			inputField1row2[i] = new JTextField(defaultIc1row2[i], 5);
		for (int i = 0; i < length1row3; ++i)
			inputField1row3[i] = new JTextField(defaultIc1row3[i], 5);
		for (int i = 0; i < length1row4; ++i)
			inputField1row4[i] = new JTextField(defaultIc1row4[i], 5);

		JTextFieldPanel row1 = new JTextFieldPanel(1, "", input1row1,
				inputField1row1, Color.cyan);
		JTextFieldPanel row2 = new JTextFieldPanel(1, "", input1row2,
				inputField1row2, Color.cyan);
		JTextFieldPanel row3 = new JTextFieldPanel(1, "", input1row3,
				inputField1row3, Color.cyan);
		JTextFieldPanel row4 = new JTextFieldPanel(1, "", input1row4,
				inputField1row4, Color.cyan);
		// scenario1Pane.setMaximumSize(panelSize);
		// scenario1Pane.setPreferredSize(panelSize);
		// scenario1Pane.setMinimumSize(panelSize);
		scenario1Pane.setLayout(new BoxLayout(scenario1Pane, BoxLayout.Y_AXIS));
		scenario1Pane.setAlignmentX(LEFT_ALIGNMENT);
		scenario1Pane.setBackground(Color.cyan);
		scenario1Pane.setBorder(BorderFactory.createTitledBorder(""));
		JLabel scenario1Label = new JLabel("1.  Constant Torque");
		scenario1Label.setFont(fancyFont);
		scenario1Pane.add(scenario1Label);
		scenario1Pane.add(Box.createRigidArea(new Dimension(0, 5)));
		scenario1Pane.add(new JLabel("Principal Moments of Inertia"));
		scenario1Pane.add(row1);
		scenario1Pane.add(new JLabel("Constants"));
		scenario1Pane.add(row2);
		scenario1Pane.add(new JLabel("Initial Condition"));
		scenario1Pane.add(row3);
		scenario1Pane.add(row4);

		scenario2Pane = new JPanel();
		for (int i = 0; i < length2row1; ++i)
			inputField2row1[i] = new JTextField(defautlIc2row1[i], 5);
		for (int i = 0; i < length2row2; ++i)
			inputField2row2[i] = new JTextField(defaultIc2row2[i], 5);
		for (int i = 0; i < length2row3; ++i)
			inputField2row3[i] = new JTextField(defaultIc2row3[i], 5);
		row1 = new JTextFieldPanel(1, "", input2row1, inputField2row1,
				Color.cyan);
		row2 = new JTextFieldPanel(1, "", input2row2, inputField2row2,
				Color.cyan);
		row3 = new JTextFieldPanel(1, "", input2row3, inputField2row3,
				Color.cyan);
		// scenario2Pane.setMaximumSize(panelSize);
		// scenario2Pane.setPreferredSize(panelSize);
		// scenario2Pane.setMinimumSize(panelSize);
		scenario2Pane.setLayout(new BoxLayout(scenario2Pane, BoxLayout.Y_AXIS));
		scenario2Pane.setAlignmentX(LEFT_ALIGNMENT);
		scenario2Pane.setBackground(Color.cyan);
		scenario2Pane.setBorder(BorderFactory.createTitledBorder(""));
		JLabel scenario2Label = new JLabel("2. Gravity Gradient (Circular)");
		scenario2Label.setFont(fancyFont);
		scenario2Pane.add(scenario2Label);
		scenario2Pane.add(Box.createRigidArea(new Dimension(0, 5)));
		scenario2Pane.add(new JLabel("Principal Moments of Inertia"));
		scenario2Pane.add(row1);
		scenario2Pane.add(new JLabel("Initial Condition"));
		scenario2Pane.add(row2);
		scenario2Pane.add(row3);

		scenario3Pane = new JPanel();
		for (int i = 0; i < length3row1; ++i)
			inputField3row1[i] = new JTextField(defaultIc3row1[i], 5);
		for (int i = 0; i < length3row2; ++i)
			inputField3row2[i] = new JTextField(defaultIc3row2[i], 5);
		for (int i = 0; i < length3row3; ++i)
			inputField3row3[i] = new JTextField(defaultIc3row3[i], 5);
		for (int i = 0; i < length3row4; ++i)
			inputField3row4[i] = new JTextField(defaultIc3row4[i], 5);
		row1 = new JTextFieldPanel(1, "", input3row1, inputField3row1,
				Color.cyan);
		row2 = new JTextFieldPanel(1, "", input3row2, inputField3row2,
				Color.cyan);
		row3 = new JTextFieldPanel(1, "", input3row3, inputField3row3,
				Color.cyan);
		row4 = new JTextFieldPanel(1, "", input3row4, inputField3row4,
				Color.cyan);
		// scenario3Pane.setMaximumSize(panelSize);
		// scenario3Pane.setPreferredSize(panelSize);
		// scenario3Pane.setMinimumSize(panelSize);
		scenario3Pane.setLayout(new BoxLayout(scenario3Pane, BoxLayout.Y_AXIS));
		scenario3Pane.setAlignmentX(LEFT_ALIGNMENT);
		scenario3Pane.setBackground(Color.cyan);
		scenario3Pane.setBorder(BorderFactory.createTitledBorder(""));
		JLabel scenario3Label = new JLabel("3. Gravity Gradient (Eccentric)");
		scenario3Label.setFont(fancyFont);
		scenario3Pane.add(scenario3Label);
		scenario3Pane.add(Box.createRigidArea(new Dimension(0, 5)));
		scenario3Pane.add(new JLabel("Principal Moments of Inertia"));
		scenario3Pane.add(row1);
		scenario3Pane.add(new JLabel("Constans"));
		scenario3Pane.add(row2);
		scenario3Pane.add(new JLabel("Initial Condition"));
		scenario3Pane.add(row3);
		scenario3Pane.add(row4);

		scenario4Pane = new JPanel();
		for (int i = 0; i < length4row1; ++i)
			inputField4row1[i] = new JTextField(defaultIc4row1[i], 5);
		for (int i = 0; i < length4row2; ++i)
			inputField4row2[i] = new JTextField(defaultIc4row2[i], 5);
		for (int i = 0; i < length4row3; ++i)
			inputField4row3[i] = new JTextField(defaultIc4row3[i], 5);
		for (int i = 0; i < length4row4; ++i)
			inputField4row4[i] = new JTextField(defaultIc4row4[i], 5);
		for (int i = 0; i < length4row5; ++i)
			inputField4row5[i] = new JTextField(defaultIc4row5[i], 5);
		row1 = new JTextFieldPanel(1, "", input4row1, inputField4row1,
				Color.cyan);
		row2 = new JTextFieldPanel(1, "", input4row2, inputField4row2,
				Color.cyan);
		row3 = new JTextFieldPanel(1, "", input4row3, inputField4row3,
				Color.cyan);
		row4 = new JTextFieldPanel(1, "", input4row4, inputField4row4,
				Color.cyan);
		JTextFieldPanel row5 = new JTextFieldPanel(1, "", input4row5,
				inputField4row5, Color.cyan);
		// scenario4Pane.setMaximumSize(panelSize);
		// scenario4Pane.setPreferredSize(panelSize);
		// scenario4Pane.setMinimumSize(panelSize);
		scenario4Pane.setLayout(new BoxLayout(scenario4Pane, BoxLayout.Y_AXIS));
		scenario4Pane.setAlignmentX(LEFT_ALIGNMENT);
		scenario4Pane.setBackground(Color.cyan);
		scenario4Pane.setBorder(BorderFactory.createTitledBorder(""));
		JLabel scenario4Label = new JLabel("4. Spherical Damper");
		scenario4Label.setFont(fancyFont);
		scenario4Pane.add(scenario4Label);
		scenario4Pane.add(Box.createRigidArea(new Dimension(0, 5)));
		scenario4Pane.add(new JLabel("Principal Moments of Inertia"));
		scenario4Pane.add(row1);
		scenario4Pane.add(new JLabel("Constants"));
		scenario4Pane.add(row2);
		scenario4Pane.add(new JLabel("Initial Condition"));
		scenario4Pane.add(row3);
		scenario4Pane.add(row4);
		scenario4Pane.add(row5);

		scenario5Pane = new JPanel();
		for (int i = 0; i < length5row1; ++i)
			inputField5row1[i] = new JTextField(defaultIc5row1[i], 5);
		for (int i = 0; i < length5row2; ++i)
			inputField5row2[i] = new JTextField(defaultIc5row2[i], 5);
		for (int i = 0; i < length5row3; ++i)
			inputField5row3[i] = new JTextField(defaultIc5row3[i], 5);
		for (int i = 0; i < length5row4; ++i)
			inputField5row4[i] = new JTextField(defaultIc5row4[i], 5);
		for (int i = 0; i < length5row5; ++i)
			inputField5row5[i] = new JTextField(defaultIc5row5[i], 5);
		for (int i = 0; i < length5row6; ++i)
			inputField5row6[i] = new JTextField(defaultIc5row6[i], 5);
		row1 = new JTextFieldPanel(1, "", input5row1, inputField5row1,
				Color.cyan);
		row2 = new JTextFieldPanel(1, "", input5row2, inputField5row2,
				Color.cyan);
		row3 = new JTextFieldPanel(1, "", input5row3, inputField5row3,
				Color.cyan);
		row4 = new JTextFieldPanel(1, "", input5row4, inputField5row4,
				Color.cyan);
		row5 = new JTextFieldPanel(1, "", input5row5, inputField5row5,
				Color.cyan);
		JTextFieldPanel row6 = new JTextFieldPanel(1, "", input5row6,
				inputField5row6, Color.cyan);
		// scenario5Pane.setMaximumSize(panelSize);
		// scenario5Pane.setPreferredSize(panelSize);
		// scenario5Pane.setMinimumSize(panelSize);
		scenario5Pane.setLayout(new BoxLayout(scenario5Pane, BoxLayout.Y_AXIS));
		scenario5Pane.setAlignmentX(LEFT_ALIGNMENT);
		scenario5Pane.setBackground(Color.cyan);
		scenario5Pane.setBorder(BorderFactory.createTitledBorder(""));
		JLabel scenario5Label = new JLabel("5. Reaction Wheel Control");
		scenario5Label.setFont(fancyFont);
		scenario5Pane.add(scenario5Label);
		scenario5Pane.add(Box.createRigidArea(new Dimension(0, 5)));
		scenario5Pane.add(new JLabel(
				"Please select one from following options."));
		// Create the check boxes.
		threeRWRadio = new JRadioButton("3 RW");
		threeRWRadio.setSelected(false);
		threeRWRadio.setActionCommand("threeRW");
		threeRWRadio.setBackground(Color.cyan);

		fourRWRadio = new JRadioButton("4 RW");
		fourRWRadio.setSelected(true);
		fourRWRadio.setActionCommand("fourRW");
		fourRWRadio.setBackground(Color.cyan);

		ButtonGroup group = new ButtonGroup();
		group.add(threeRWRadio);
		group.add(fourRWRadio);

		// Register a listener for the check boxes.
		threeRWRadio.addActionListener(new ButtonHandler());
		fourRWRadio.addActionListener(new ButtonHandler());
		// Create a JPanel for putting check boxes
		JPanel boxPane = new JPanel();
		boxPane.setLayout(new BoxLayout(boxPane, BoxLayout.X_AXIS));
		// boxPane.setPreferredSize(new Dimension(comboBoxPane_width, 30));
		// boxPane.setMaximumSize(new Dimension(comboBoxPane_width, 30));
		// boxPane.setMinimumSize(new Dimension(comboBoxPane_width, 30));
		boxPane.setBackground(Color.cyan);
		boxPane.setAlignmentX(LEFT_ALIGNMENT);
		boxPane.add(threeRWRadio);
		boxPane.add(fourRWRadio);
		boxPane.add(Box.createHorizontalGlue());
		scenario5Pane.add(boxPane);
		scenario5Pane.add(Box.createRigidArea(new Dimension(0, 5)));
		scenario5Pane.add(new JLabel("Principal Moments of Inertia"));
		scenario5Pane.add(row1);
		scenario5Pane.add(new JLabel("Constants"));
		scenario5Pane.add(row2);
		scenario5Pane.add(new JLabel("Commanded Posision"));
		scenario5Pane.add(row3);
		scenario5Pane.add(new JLabel("Initial Condition"));
		scenario5Pane.add(row4);
		scenario5Pane.add(row5);
		scenario5Pane.add(row6);

		scenario6Pane = new JPanel();
		for (int i = 0; i < length6row1; ++i)
			inputField6row1[i] = new JTextField(defaultIc6row1[i], 5);
		for (int i = 0; i < length6row2; ++i)
			inputField6row2[i] = new JTextField(defaultIc6row2[i], 5);
		for (int i = 0; i < length6row3; ++i)
			inputField6row3[i] = new JTextField(defaultIc6row3[i], 5);
		for (int i = 0; i < length6row4; ++i)
			inputField6row4[i] = new JTextField(defaultIc6row4[i], 5);
		for (int i = 0; i < length6row5; ++i)
			inputField6row5[i] = new JTextField(defaultIc6row5[i], 5);
		for (int i = 0; i < length6row6; ++i)
			inputField6row6[i] = new JTextField(defaultIc6row6[i], 5);
		row1 = new JTextFieldPanel(1, "", input6row1, inputField6row1,
				Color.cyan);
		row2 = new JTextFieldPanel(1, "", input6row2, inputField6row2,
				Color.cyan);
		row3 = new JTextFieldPanel(1, "", input6row3, inputField6row3,
				Color.cyan);
		row4 = new JTextFieldPanel(1, "", input6row4, inputField6row4,
				Color.cyan);
		row5 = new JTextFieldPanel(1, "", input6row5, inputField6row5,
				Color.cyan);
		row6 = new JTextFieldPanel(1, "", input6row6, inputField6row6,
				Color.cyan);
		// scenario6Pane.setMaximumSize(panelSize);
		// scenario6Pane.setPreferredSize(panelSize);
		// scenario6Pane.setMinimumSize(panelSize);
		scenario6Pane.setLayout(new BoxLayout(scenario6Pane, BoxLayout.Y_AXIS));
		scenario6Pane.setAlignmentX(LEFT_ALIGNMENT);
		scenario6Pane.setBackground(Color.cyan);
		scenario6Pane.setBorder(BorderFactory.createTitledBorder(""));
		JLabel scenario6Label = new JLabel("6. CMG Control");
		scenario6Label.setFont(fancyFont);
		scenario6Pane.add(scenario6Label);
		scenario6Pane.add(Box.createRigidArea(new Dimension(0, 5)));
		scenario6Pane.add(new JLabel("Principal Moments of Inertia"));
		scenario6Pane.add(row1);
		scenario6Pane.add(new JLabel("Constants"));
		scenario6Pane.add(row2);
		scenario6Pane.add(new JLabel("Commanded Posision"));
		scenario6Pane.add(row3);
		scenario6Pane.add(new JLabel("Initial Condition"));
		scenario6Pane.add(row4);
		scenario6Pane.add(row5);
		scenario6Pane.add(row6);

		scenario7Pane = new JPanel();
		for (int i = 0; i < length7row1; ++i)
			inputField7row1[i] = new JTextField(defaultIc7row1[i], 5);
		for (int i = 0; i < length7row2; ++i)
			inputField7row2[i] = new JTextField(defaultIc7row2[i], 5);
		for (int i = 0; i < length7row3; ++i)
			inputField7row3[i] = new JTextField(defaultIc7row3[i], 5);
		for (int i = 0; i < length7row3; ++i)
			inputField7row3[i].setEditable(false);
		for (int i = 0; i < length7row4; ++i)
			inputField7row4[i] = new JTextField(defaultIc7row4[i], 5);
		for (int i = 0; i < length7row5; ++i)
			inputField7row5[i] = new JTextField(defaultIc7row5[i], 5);

		row1 = new JTextFieldPanel(1, "", input7row1, inputField7row1,
				Color.cyan);
		row2 = new JTextFieldPanel(1, "", input7row2, inputField7row2,
				Color.cyan);
		row3 = new JTextFieldPanel(1, "", input7row3, inputField7row3,
				Color.cyan);
		row4 = new JTextFieldPanel(1, "", input7row4, inputField7row4,
				Color.cyan);
		row5 = new JTextFieldPanel(1, "", input7row5, inputField7row5,
				Color.cyan);
		// scenario7Pane.setMaximumSize(panelSize);
		// scenario7Pane.setPreferredSize(panelSize);
		// scenario7Pane.setMinimumSize(panelSize);
		scenario7Pane.setLayout(new BoxLayout(scenario7Pane, BoxLayout.Y_AXIS));
		scenario7Pane.setAlignmentX(LEFT_ALIGNMENT);
		scenario7Pane.setBackground(Color.cyan);
		scenario7Pane.setBorder(BorderFactory.createTitledBorder(""));
		JLabel scenario7Label = new JLabel("7. Bang-Bang Control");
		scenario7Label.setFont(fancyFont);
		scenario7Pane.add(scenario7Label);
		scenario7Pane.add(Box.createRigidArea(new Dimension(0, 5)));
		scenario7Pane.add(new JLabel("Moment of Inertia"));
		scenario7Pane.add(row1);
		scenario7Pane.add(new JLabel("Constants"));
		scenario7Pane.add(row2);
		scenario7Pane.add(row3);
		scenario7Pane.add(row4);
		scenario7Pane.add(Box.createRigidArea(new Dimension(0, 5)));
		scenario7Pane.add(new JLabel("Initial Condition"));
		scenario7Pane.add(row5);

		scenario8Pane = new JPanel();
		for (int i = 0; i < length8row1; ++i)
			inputField8row1[i] = new JTextField(defaultIc8row1[i], 5);
		for (int i = 0; i < length8row2; ++i)
			inputField8row2[i] = new JTextField(defaultIc8row2[i], 5);
		for (int i = 0; i < length8row3; ++i)
			inputField8row3[i] = new JTextField(defaultIc8row3[i], 5);
		for (int i = 0; i < length8row4; ++i)
			inputField8row4[i] = new JTextField(defaultIc8row4[i], 5);
		for (int i = 0; i < length8row5; ++i)
			inputField8row5[i] = new JTextField(defaultIc8row5[i], 5);

		row1 = new JTextFieldPanel(1, "", input8row1, inputField8row1,
				Color.cyan);
		row2 = new JTextFieldPanel(1, "", input8row2, inputField8row2,
				Color.cyan);
		row3 = new JTextFieldPanel(1, "", input8row3, inputField8row3,
				Color.cyan);
		row4 = new JTextFieldPanel(1, "", input8row4, inputField8row4,
				Color.cyan);
		row5 = new JTextFieldPanel(1, "", input8row5, inputField8row5,
				Color.cyan);

		// scenario8Pane.setMaximumSize(panelSize);
		// scenario8Pane.setPreferredSize(panelSize);
		// scenario8Pane.setMinimumSize(panelSize);
		scenario8Pane.setLayout(new BoxLayout(scenario8Pane, BoxLayout.Y_AXIS));
		scenario8Pane.setAlignmentX(LEFT_ALIGNMENT);
		scenario8Pane.setBackground(Color.cyan);
		scenario8Pane.setBorder(BorderFactory.createTitledBorder(""));
		JLabel scenario8Label = new JLabel("8. 2D Flexible Spacecraft");
		scenario8Label.setFont(fancyFont);
		scenario8Pane.add(scenario8Label);
		scenario8Pane.add(Box.createRigidArea(new Dimension(0, 5)));
		scenario8Pane.add(new JLabel("Moment of Inertia"));
		scenario8Pane.add(row1);
		scenario8Pane.add(new JLabel("Constants"));
		scenario8Pane.add(row2);
		scenario8Pane.add(new JLabel("Control Option"));
		scenario8Pane.add(row3);
		scenario8Pane.add(new JLabel("Initial Condition"));
		scenario8Pane.add(row4);
		scenario8Pane.add(row5);

		scenario9Pane = new JPanel();
		for (int i = 0; i < length9row1; ++i)
			inputField9row1[i] = new JTextField(defaultIc9row1[i], 5);
		for (int i = 0; i < length9row2; ++i)
			inputField9row2[i] = new JTextField(defaultIc9row2[i], 5);
		for (int i = 0; i < length9row3; ++i)
			inputField9row3[i] = new JTextField(defaultIc9row3[i], 5);
		for (int i = 0; i < length9row4; ++i)
			inputField9row4[i] = new JTextField(defaultIc9row4[i], 5);
		row1 = new JTextFieldPanel(1, "", input9row1, inputField9row1,
				Color.cyan);
		row2 = new JTextFieldPanel(1, "", input9row2, inputField9row2,
				Color.cyan);
		row3 = new JTextFieldPanel(1, "", input9row3, inputField9row3,
				Color.cyan);
		row4 = new JTextFieldPanel(1, "", input9row4, inputField9row4,
				Color.cyan);
		// scenario9Pane.setMaximumSize(panelSize);
		// scenario9Pane.setPreferredSize(panelSize);
		// scenario9Pane.setMinimumSize(panelSize);
		scenario9Pane.setLayout(new BoxLayout(scenario9Pane, BoxLayout.Y_AXIS));
		scenario9Pane.setAlignmentX(LEFT_ALIGNMENT);
		scenario9Pane.setBackground(Color.cyan);
		scenario9Pane.setBorder(BorderFactory.createTitledBorder(""));
		JLabel scenario9Label = new JLabel("9. 3D Flexible Spacecraft");
		scenario9Label.setFont(fancyFont);
		scenario9Pane.add(scenario9Label);
		scenario9Pane.add(Box.createRigidArea(new Dimension(0, 5)));
		scenario9Pane.add(new JLabel("Principal Moments of Inertia"));
		scenario9Pane.add(row1);
		scenario9Pane.add(new JLabel("Constants"));
		scenario9Pane.add(row2);
		scenario9Pane.add(new JLabel("Initial Condition"));
		scenario9Pane.add(row3);
		scenario9Pane.add(row4);

	}// End of makeInputPanels()

	/**
	 * Used when run as an application
	 * 
	 * @param args
	 *            (String[]) Argument
	 */
	public static void main(String[] args) {
		theApplet = new AttitudeSimulator();
		theFrame = new JFrame();
		// Initialize the applet
		theApplet.init();
		theFrame.setTitle("Attitude Simulator");
		theFrame.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		// When running this file as a stand-alone app, add the applet to
		// the frame.
		theFrame.getContentPane().add(theApplet, BorderLayout.CENTER);

		theFrame.setSize(appletwidth, appletheight);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		theFrame.setLocation((d.width - theFrame.getSize().width) / 2,
				(d.height - theFrame.getSize().height) / 2);
		theFrame.setVisible(true);
	}// End of main()

	/**
	 * Handles the event associated with clicking of a mouse.
	 * 
	 */

	class ButtonHandler implements ActionListener, ItemListener {
		// double time_step = 0.1;
		// double timeDuration = 10;
		// double I1 = 10.42;
		// double I2 = 35.42;
		// double I3 = 41.67;

		public void itemStateChanged(ItemEvent e) {
			/** Listens to the check boxes. */
			Object source = e.getItemSelectable();

			if (source == threeDeeBox) {

				// System.out.println("3dbox");
			}
			// Now that we know which button was pushed, find out
			// whether it was selected or deselected.
			if (e.getStateChange() == ItemEvent.SELECTED) {
				// System.out.println("3dbox selected");
				util u = new util();
				// System.out.println(u.check_for_Java3D_b());
				if (!u.check_for_Java3D_b()) {
					JOptionPane.showMessageDialog(theFrame,
							"Java3D not installed.");
					threeDeeBox.setSelected(false);

				}
			}

		}

		public void actionPerformed(ActionEvent ev) {

			// Read in values
			if (ev.getSource() == startButton) {

				double time_step = Double.parseDouble(controlInputField[0]
						.getText());
				double timeDuration = Double.parseDouble(controlInputField[1]
						.getText());
				boolean errorFree = false;

				// Set up eomObject with new parameter values
				EomRunner eomObject = new EomRunner(time_step, timeDuration);
				// eomObject.setParameters(time_step, timeDuration, I1, I2, I3);
				if (threeDeeBox.isSelected() == true)
					eomObject.animationYes = 1;
				else
					eomObject.animationYes = 0;
				if (twoDeeBox.isSelected() == true)
					eomObject.plotYes = 1;
				else
					eomObject.plotYes = 0;

				if (combo.getSelectedItem() == SCENARIO_1) {
					// Read Inertia
					double I1 = Double
							.parseDouble(inputField1row1[0].getText());
					double I2 = Double
							.parseDouble(inputField1row1[1].getText());
					double I3 = Double
							.parseDouble(inputField1row1[2].getText());
					double M1 = Double
							.parseDouble(inputField1row2[0].getText()); // External
																		// torque
					double M2 = Double
							.parseDouble(inputField1row2[1].getText());
					double M3 = Double
							.parseDouble(inputField1row2[2].getText());
					// initialize the variables
					double[] x0 = new double[7];
					for (int i = 0; i < 3; ++i)
						x0[i] = Double
								.parseDouble(inputField1row3[i].getText());
					for (int i = 3; i < 7; ++i)
						x0[i] = Double.parseDouble(inputField1row4[i - 3]
								.getText());
					// Check for errors
					errorFree = checkForError(I1, I2, I3, x0[3], x0[4], x0[5],
							x0[6]);
					if (errorFree == true) {
						eomObject.setInertiaThreeD(I1, I2, I3);
						eomObject.doConstantTorque(x0, M1, M2, M3);
					} else
						JOptionPane.showMessageDialog(null,
								"Please correct the errors and try again.");
				}
				/*-------------------------------------------------------------------------------*/
				else if (combo.getSelectedItem() == SCENARIO_2) {
					// Read Inertia
					double I1 = Double
							.parseDouble(inputField2row1[0].getText());
					double I2 = Double
							.parseDouble(inputField2row1[1].getText());
					double I3 = Double
							.parseDouble(inputField2row1[2].getText());
					// initialize the variables
					double[] x0 = new double[7];
					for (int i = 0; i < 3; ++i)
						x0[i] = Double
								.parseDouble(inputField2row2[i].getText());
					for (int i = 3; i < 7; ++i)
						x0[i] = Double.parseDouble(inputField2row3[i - 3]
								.getText());
					// Check for errors
					errorFree = checkForError(I1, I2, I3, x0[3], x0[4], x0[5],
							x0[6]);
					if (errorFree == true) {
						eomObject.setInertiaThreeD(I1, I2, I3);
						eomObject.doGGCircular(x0);
					} else
						JOptionPane.showMessageDialog(null,
								"Please correct the errors and try again.");
				}
				/*--------------------------------------------------------------------------------*/
				else if (combo.getSelectedItem() == SCENARIO_3) {
					// Read Inertia
					double I1 = Double
							.parseDouble(inputField3row1[0].getText());
					double I2 = Double
							.parseDouble(inputField3row1[1].getText());
					double I3 = Double
							.parseDouble(inputField3row1[2].getText());
					double e = Double.parseDouble(inputField3row2[0].getText());
					// initialize the variables
					double[] x0 = new double[7];
					for (int i = 0; i < 3; ++i)
						x0[i] = Double
								.parseDouble(inputField3row3[i].getText());
					for (int i = 3; i < 7; ++i)
						x0[i] = Double.parseDouble(inputField3row4[i - 3]
								.getText());
					// Check for errors
					errorFree = checkForError(I1, I2, I3, x0[3], x0[4], x0[5],
							x0[6]);
					if (errorFree == true) {
						eomObject.setInertiaThreeD(I1, I2, I3);
						eomObject.doGGEccentric(x0, e);
					} else
						JOptionPane.showMessageDialog(null,
								"Please correct the errors and try again.");
				}
				/*--------------------------------------------------------------------------------*/

				else if (combo.getSelectedItem() == SCENARIO_4) {
					// Read Inertia
					double I1 = Double
							.parseDouble(inputField4row1[0].getText());
					double I2 = Double
							.parseDouble(inputField4row1[1].getText());
					double I3 = Double
							.parseDouble(inputField4row1[2].getText());
					double c = Double.parseDouble(inputField4row2[0].getText()); // damping
																					// coefficient
					double j = Double.parseDouble(inputField4row2[1].getText());
					// Initial Condition
					double[] x0 = new double[10];
					for (int i = 0; i < 3; ++i)
						x0[i] = Double
								.parseDouble(inputField4row3[i].getText());
					for (int i = 3; i < 7; ++i)
						x0[i] = Double.parseDouble(inputField4row4[i - 3]
								.getText());
					for (int i = 7; i < 10; ++i)
						x0[i] = Double.parseDouble(inputField4row5[i - 7]
								.getText());
					// Check for errors
					errorFree = checkForError(I1, I2, I3, x0[3], x0[4], x0[5],
							x0[6]);
					if (errorFree == true) {
						eomObject.setInertiaThreeD(I1, I2, I3);
						eomObject.doSphericalDamper(x0, c, j);
					} else
						JOptionPane.showMessageDialog(null,
								"Please correct the errors and try again.");
				}
				/*--------------------------------------------------------------------------------*/
				else if (combo.getSelectedItem() == SCENARIO_5) {
					if (threeRWRadio.isSelected() == true)
						eomObject.number_of_RW = 3;

					if (fourRWRadio.isSelected() == true)
						eomObject.number_of_RW = 4;

					// Read Inertia
					double I1 = Double
							.parseDouble(inputField5row1[0].getText());
					double I2 = Double
							.parseDouble(inputField5row1[1].getText());
					double I3 = Double
							.parseDouble(inputField5row1[2].getText());
					double J = Double.parseDouble(inputField5row2[0].getText());
					double angle = Double.parseDouble(inputField5row2[1]
							.getText());
					double psi = Double.parseDouble(inputField5row3[0]
							.getText());
					double theta = Double.parseDouble(inputField5row3[1]
							.getText());
					double phi = Double.parseDouble(inputField5row3[2]
							.getText());
					// initialize the variables
					double[] x0 = new double[11];
					for (int i = 0; i < 3; ++i)
						x0[i] = Double
								.parseDouble(inputField5row4[i].getText());
					for (int i = 3; i < 7; ++i)
						x0[i] = Double.parseDouble(inputField5row5[i - 3]
								.getText());
					for (int i = 7; i < 11; ++i)
						x0[i] = Double.parseDouble(inputField5row6[i - 7]
								.getText());
					// Check for errors
					errorFree = checkForError(I1, I2, I3, x0[3], x0[4], x0[5],
							x0[6]);
					if (errorFree == true) {
						eomObject.setInertiaThreeD(I1, I2, I3);
						eomObject.doFourRW(x0, J, angle, psi, theta, phi);
					} else
						JOptionPane.showMessageDialog(null,
								"Please correct the errors and try again.");
				}
				/*-------------------------------------------------------------------------------*/
				else if (combo.getSelectedItem() == SCENARIO_6) {
					// Read Inertia
					double I1 = Double
							.parseDouble(inputField6row1[0].getText());
					double I2 = Double
							.parseDouble(inputField6row1[1].getText());
					double I3 = Double
							.parseDouble(inputField6row1[2].getText());
					double J = Double.parseDouble(inputField6row2[0].getText());
					double A = Double.parseDouble(inputField6row2[1].getText());
					double psi = Double.parseDouble(inputField6row3[0]
							.getText());
					double theta = Double.parseDouble(inputField6row3[1]
							.getText());
					double phi = Double.parseDouble(inputField6row3[2]
							.getText());
					// initialize the variables
					double[] x0 = new double[10];
					for (int i = 0; i < 3; ++i)
						x0[i] = Double
								.parseDouble(inputField6row4[i].getText());
					for (int i = 3; i < 7; ++i)
						x0[i] = Double.parseDouble(inputField6row5[i - 3]
								.getText());
					for (int i = 7; i < 10; ++i)
						x0[i] = Double.parseDouble(inputField6row6[i - 7]
								.getText());
					// Check for errors
					errorFree = checkForError(I1, I2, I3, x0[3], x0[4], x0[5],
							x0[6]);
					if (errorFree == true) {
						eomObject.setInertiaThreeD(I1, I2, I3);
						eomObject.doCMGManeuver(x0, J, A, psi, phi, theta);
					} else
						JOptionPane.showMessageDialog(null,
								"Please correct the errors and try again.");
				}
				/*-------------------------------------------------------------------------------*/
				else if (combo.getSelectedItem() == SCENARIO_7) {
					double J = Double.parseDouble(inputField7row1[0].getText());
					double wn = Double
							.parseDouble(inputField7row2[0].getText());
					double damping = Double.parseDouble(inputField7row2[1]
							.getText());
					double K = wn * wn * J;
					double Kd = 2 * J * damping * wn;
					double K_bang = K;
					double Kd_bang = Kd;
					// Display calculated gain
					inputField7row3[0].setText(Double.toString(K));
					inputField7row3[1].setText(Double.toString(Kd));
					inputField7row3[2].setText(Double.toString(K_bang));
					inputField7row3[3].setText(Double.toString(Kd_bang));
					double torque = Double.parseDouble(inputField7row4[0]
							.getText());
					double dz = Double
							.parseDouble(inputField7row4[1].getText());
					double theta_com = Double.parseDouble(inputField7row4[2]
							.getText());
					// initialize the variables
					double[] x0 = new double[4];
					for (int i = 0; i < 4; ++i)
						x0[i] = Double
								.parseDouble(inputField7row5[i].getText());
					eomObject.setInertiaTwoD(J);
					eomObject.doBangBang(x0, wn, damping, K, Kd, K_bang,
							Kd_bang, torque, dz, theta_com);
				}
				/*-------------------------------------------------------------------------------*/
				else if (combo.getSelectedItem() == SCENARIO_8) {
					double J = Double.parseDouble(inputField8row1[0].getText());
					double m = Double.parseDouble(inputField8row2[0].getText());
					double a = Double.parseDouble(inputField8row2[1].getText());
					double L = Double.parseDouble(inputField8row2[2].getText());
					double EI = Double
							.parseDouble(inputField8row2[3].getText());
					double K = Double.parseDouble(inputField8row3[0].getText());
					double Kd = Double
							.parseDouble(inputField8row3[1].getText());
					double alpha_com = Double.parseDouble(inputField8row3[2]
							.getText());

					// initialize the variables
					double[] x0 = new double[6];
					for (int i = 0; i < 3; ++i)
						x0[i] = Double
								.parseDouble(inputField8row4[i].getText());
					for (int i = 3; i < 6; ++i)
						x0[i] = Double.parseDouble(inputField8row5[i - 3]
								.getText());
					eomObject.setInertiaTwoD(J);
					eomObject.doTwoDFlex(x0, a, L, EI, m, K, Kd, alpha_com);
				}
				/*-------------------------------------------------------------------------------*/
				else if (combo.getSelectedItem() == SCENARIO_9) {
					// Read Inertia
					double I1 = Double
							.parseDouble(inputField9row1[0].getText());
					double I2 = Double
							.parseDouble(inputField9row1[1].getText());
					double I3 = Double
							.parseDouble(inputField9row1[2].getText());
					double m = Double.parseDouble(inputField9row2[0].getText());
					double a = Double.parseDouble(inputField9row2[1].getText());
					double L = Double.parseDouble(inputField9row2[2].getText());
					double EI = Double
							.parseDouble(inputField9row2[3].getText());
					// initialize the variables
					double[] x0 = new double[10];
					for (int i = 0; i < 5; ++i)
						x0[i] = Double
								.parseDouble(inputField9row3[i].getText());
					for (int i = 5; i < 10; ++i)
						x0[i] = Double.parseDouble(inputField9row4[i - 5]
								.getText());
					// Check for errors
					errorFree = checkForError(I1, I2, I3);
					// System.out.println(t+" "+y[0]+" "+y[1]+" "+y[2]);
					System.out.println("I1 = " + I1 + "\n");
					System.out.println("I2 = " + I2 + "\n");
					System.out.println("I3 = " + I3 + "\n");
					System.out.println("x0[]= " + x0[0] + " " + x0[1] + " "
							+ x0[2] + " " + x0[3] + " " + x0[4] + " " + x0[5]
							+ " " + x0[6] + " " + x0[7] + " " + x0[8] + " "
							+ x0[9]);
					System.out.println("a= " + a);
					System.out.println("L= " + L);
					System.out.println("EI= " + EI);
					System.out.println("m= " + m);
					if (errorFree == true) {
						eomObject.setInertiaThreeD(I1, I2, I3);
						eomObject.doThreeDFlex(x0, a, L, EI, m);
					} else
						JOptionPane.showMessageDialog(null,
								"Please correct the errors and try again.");
				}

			}// End of if(ev.getSource() == startButton)

			if (ev.getSource() == convertButton) {
				double psi = Double.parseDouble(eulerInputField[0].getText());
				double theta = Double.parseDouble(eulerInputField[1].getText());
				double phi = Double.parseDouble(eulerInputField[2].getText());
				DegToQuat tester = new DegToQuat(psi, theta, phi);
				double[] quat = new double[4];
				quat = tester.calculateQuat();
				quaternionOutputField[0].setText(Double.toString(quat[0]));
				quaternionOutputField[1].setText(Double.toString(quat[1]));
				quaternionOutputField[2].setText(Double.toString(quat[2]));
				quaternionOutputField[3].setText(Double.toString(quat[3]));
			}

			if (ev.getSource() == resetButton) {
				eulerInputField[0].setText("0");
				eulerInputField[1].setText("0");
				eulerInputField[2].setText("0");
				quaternionOutputField[0].setText("0");
				quaternionOutputField[1].setText("0");
				quaternionOutputField[2].setText("0");
				quaternionOutputField[3].setText("1");
			}

			if (ev.getSource() == simHelpButton) {
				new HelpWindow("help/" + helpfile[combo.getSelectedIndex()]);

				if (ev.getSource() == threeDeeBox) {

					System.out.println("3dbox");
				}

			}

		}// End of ActionPerformed
	}// End of Button Handler

	/**
	 * Checks to see that (e1^2 + e2^2 + e3^2 + e4^2)^0.5 is close to 1.0 Checks
	 * to see principal inertias are correct sum of any two is larger than
	 * remaining inertia
	 */
	boolean checkForError(double I1, double I2, double I3, double q1,
			double q2, double q3, double q4) {
		boolean errorFree = true;

		float quattest = (float) Math.pow(
				(double) (q1 * q1 + q2 * q2 + q3 * q3 + q4 * q4), 0.5);
		if ((quattest < 0.99) || (quattest > 1.01)) {
			JOptionPane
					.showMessageDialog(
							null,
							"Quaternion values are unreasonable.\n(\u03B51^2 + \u03B52^2 + \u03B53^2 + \u03B54^2)^0.5 = 1");
			errorFree = false;
		}
		double inertiatotal = I1 + I2 + I3;
		double maxinertia = Math.max(I1, I2);
		maxinertia = Math.max(maxinertia, I3);
		double testinertia = inertiatotal - maxinertia;
		if (testinertia <= maxinertia) {
			JOptionPane
					.showMessageDialog(
							null,
							"Spacecraft inertias are unreasonable.\nSum of any two inertias must be greater than the third.");
			errorFree = false;
		}

		return errorFree;
	}

	/**
	 * Checks to see principal inertias are correct sum of any two is larger
	 * than remaining inertia
	 */
	boolean checkForError(double I1, double I2, double I3) {
		boolean errorFree = true;
		double inertiatotal = I1 + I2 + I3;
		// System.out.println(t+" "+y[0]+" "+y[1]+" "+y[2]);
		// System.out.println("Inertiatotal= " + inertiatotal+"\n");
		double maxinertia = Math.max(I1, I2);
		maxinertia = Math.max(maxinertia, I3);
		// System.out.println("maxinertia= " + maxinertia+"\n");
		double testinertia = inertiatotal - maxinertia;
		// System.out.println("testinertia=" + testinertia+"\n");
		if (testinertia <= maxinertia) {
			JOptionPane
					.showMessageDialog(
							null,
							"Spacecraft inertias are unreasonable.\nSum of any two inertias must be greater than the third.");
			errorFree = false;
		}

		return errorFree;
	}

	/**
	 * Handles the events associated with the MyChoice object assigned to
	 * simulationChoice. It does so by updating the input panel according to the
	 * selection
	 */
	class ChoiceHandler implements ItemListener {
		public void itemStateChanged(ItemEvent e) {

			CardLayout cl = (CardLayout) (inputCards.getLayout());
			// CardLayout c2 = (CardLayout) (instructionCards.getLayout());
			cl.show(inputCards, (String) e.getItem());
			// c2.show(instructionCards, (String) e.getItem());

		}

	}// End of ChoiceHandler

}// End of file

