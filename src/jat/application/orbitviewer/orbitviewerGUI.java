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

package jat.application.orbitviewer;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class orbitviewerGUI extends JApplet {
	private static final long serialVersionUID = 1321470082814219656L;
	JFormattedTextField semimajorfield;
	JFormattedTextField EccentricityField;
	JFormattedTextField InclinationField;
	JCheckBox rotationCheckBox;
	JButton setButton;
	orbitviewerEvents myb;
	JPanel level2_Pane_Plot;
	orbitviewerplot o;
	private JLabel lblNewLabel_2;
	JFormattedTextField RightAscensionField;
	private JLabel lblNewLabel_3;
	JFormattedTextField ArgumentofPerigeeField;
	private JLabel lblNewLabel_4;
	JFormattedTextField TrueAnomalyField;

	/**
	 * Create the applet.
	 */
	public orbitviewerGUI() {
		myb = new orbitviewerEvents(this);
		getContentPane().setLayout(new BorderLayout(0, 0));
		JPanel level1_Pane = new JPanel();

		getContentPane().add(level1_Pane);
		level1_Pane.setLayout(new BoxLayout(level1_Pane, BoxLayout.LINE_AXIS));

		o = new orbitviewerplot();
		o.make_plot();
		JPanel level2_Pane_Control = new JPanel();
		JPanel level3_Pane_Control = new JPanel();
		level3_Pane_Control.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("146px:grow"),},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		level3_Pane_Control.setBorder(BorderFactory.createTitledBorder("Kepler Elements"));
		level2_Pane_Control.add(level3_Pane_Control);
		level1_Pane.add(level2_Pane_Control);

		JLabel lblJulianDate = new JLabel("Semimajor Axis [km]");
		level3_Pane_Control.add(lblJulianDate, "2, 1, fill, fill");

		semimajorfield = new JFormattedTextField();
		semimajorfield.setColumns(12);
		semimajorfield.setValue(8000.);
		level3_Pane_Control.add(semimajorfield, "2, 3, fill, fill");

		JLabel lblNewLabel = new JLabel("Eccentricity");
		level3_Pane_Control.add(lblNewLabel, "2, 5, fill, fill");

		EccentricityField = new JFormattedTextField();
		EccentricityField.setColumns(12);
		EccentricityField.setValue(0.1);
		level3_Pane_Control.add(EccentricityField, "2, 7, fill, fill");

		JLabel lblNewLabel_1 = new JLabel("Inclination");
		level3_Pane_Control.add(lblNewLabel_1, "2, 9, fill, fill");

		InclinationField = new JFormattedTextField();
		InclinationField.setColumns(12);
		InclinationField.setValue(15.);
		level3_Pane_Control.add(InclinationField, "2, 11, fill, fill");

		lblNewLabel_2 = new JLabel("Right Ascension");
		level3_Pane_Control.add(lblNewLabel_2, "2, 13, fill, fill");

		RightAscensionField = new JFormattedTextField();
		RightAscensionField.setColumns(12);
		RightAscensionField.setValue(0.);
		level3_Pane_Control.add(RightAscensionField, "2, 14, fill, fill");

		lblNewLabel_3 = new JLabel("Argument of Perigee");
		level3_Pane_Control.add(lblNewLabel_3, "2, 16, fill, fill");

		ArgumentofPerigeeField = new JFormattedTextField();
		ArgumentofPerigeeField.setColumns(12);
		ArgumentofPerigeeField.setValue(0.);
		level3_Pane_Control.add(ArgumentofPerigeeField, "2, 18, fill, fill");
		
		lblNewLabel_4 = new JLabel("True Anomaly");
		level3_Pane_Control.add(lblNewLabel_4, "2, 19");
		
		TrueAnomalyField = new JFormattedTextField();
		TrueAnomalyField.setColumns(12);
		TrueAnomalyField.setValue(0.);
		level3_Pane_Control.add(TrueAnomalyField, "2, 21, fill, default");

		setButton = new JButton("Set Orbit");
		level3_Pane_Control.add(setButton, "2, 23, fill, fill");

		setButton.addActionListener(myb);

		rotationCheckBox = new JCheckBox("rotation");
		level3_Pane_Control.add(rotationCheckBox, "2, 25, fill, fill");
		rotationCheckBox.addItemListener(myb);
		level2_Pane_Plot = new JPanel();

		level1_Pane.add(level2_Pane_Plot);

		level2_Pane_Plot.setLayout(new BorderLayout(0, 0));
		level2_Pane_Plot.add(o.plot);

	}
}
