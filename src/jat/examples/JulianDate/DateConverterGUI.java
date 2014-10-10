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

package jat.examples.JulianDate;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class DateConverterGUI extends JApplet {
	private static final long serialVersionUID = 1321470082814219656L;
	public JButton btn_Jul_to_Cal;
	JButton btn_Cal_to_Jul;
	JFormattedTextField yearfield;
	JFormattedTextField monthfield;
	JFormattedTextField dayfield, hourfield, minutefield, secondfield;
	JFormattedTextField JDfield;
	DateConverterEvents myb;

	/**
	 * Create the applet.
	 */
	public DateConverterGUI() {
		myb = new DateConverterEvents(this);
		getContentPane().setLayout(new BorderLayout(0, 0));
		JPanel level1_Pane = new JPanel();
		JPanel level2_Pane_Mid = new JPanel();
		JPanel level2_Pane_Jul = new JPanel();
		JPanel level2_Pane_Cal = new JPanel();
		JPanel level3_Pane_Mid1 = new JPanel();
		JPanel level3_Pane_Mid2 = new JPanel();
		level2_Pane_Jul.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JLabel lblJulianDate = new JLabel("Days");
		level2_Pane_Jul.add(lblJulianDate);
		level2_Pane_Jul.setBorder(BorderFactory
				.createTitledBorder("Julian Date"));

		getContentPane().add(level1_Pane);
		level1_Pane.setLayout(new BoxLayout(level1_Pane, BoxLayout.X_AXIS));
		level1_Pane.add(level2_Pane_Jul);

		JDfield = new JFormattedTextField();
		JDfield.setColumns(12);
		JDfield.setValue(2456033.1);
		level2_Pane_Jul.add(JDfield);
		level1_Pane.add(level2_Pane_Mid);
		level1_Pane.add(level2_Pane_Cal);
		level2_Pane_Mid.setLayout(new BoxLayout(level2_Pane_Mid,
				BoxLayout.Y_AXIS));
		btn_Jul_to_Cal = new JButton(">>");
		// btn_Jul_to_Cal.addActionListener(new ButtonHandler());
		btn_Jul_to_Cal.addActionListener(myb);
		level3_Pane_Mid1.setLayout(new BorderLayout(0, 0));
		level3_Pane_Mid1.add(btn_Jul_to_Cal);
		//level2_Pane_Mid.add(btn_Jul_to_Cal);
		level2_Pane_Mid.add(level3_Pane_Mid1);
		level2_Pane_Mid.add(level3_Pane_Mid2);
		level3_Pane_Mid2.setLayout(new BorderLayout(0, 0));
		
		btn_Cal_to_Jul = new JButton("<<");
		level3_Pane_Mid2.add(btn_Cal_to_Jul);
		btn_Cal_to_Jul.addActionListener(myb);

		level2_Pane_Cal
				.setLayout(new FormLayout(new ColumnSpec[] {
						FormFactory.RELATED_GAP_COLSPEC,
						ColumnSpec.decode("102px"),
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						ColumnSpec.decode("114px:grow"),
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						ColumnSpec.decode("32px"), }, new RowSpec[] {
						RowSpec.decode("25px"), RowSpec.decode("19px"),
						FormFactory.LINE_GAP_ROWSPEC, RowSpec.decode("19px"),
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC, }));

		JLabel lblCalendarDate = new JLabel("Year");
		level2_Pane_Cal.add(lblCalendarDate, "2, 1, right, center");

		yearfield = new JFormattedTextField();
		yearfield.setHorizontalAlignment(SwingConstants.RIGHT);
		yearfield.setValue(2012);
		yearfield.setColumns(6);
		level2_Pane_Cal.add(yearfield, "4, 1, left, top");

		JLabel lblYear = new JLabel("Month");
		level2_Pane_Cal.add(lblYear, "2, 2, right, center");
		level2_Pane_Cal.setBorder(BorderFactory
				.createTitledBorder("Calendar Date"));

		monthfield = new JFormattedTextField();
		monthfield.setHorizontalAlignment(SwingConstants.RIGHT);
		monthfield.setValue(4);
		monthfield.setColumns(6);
		level2_Pane_Cal.add(monthfield, "4, 2, left, top");

		JLabel lblNewLabel = new JLabel("Day");
		level2_Pane_Cal.add(lblNewLabel, "2, 4, right, default");
		dayfield = new JFormattedTextField();
		dayfield.setHorizontalAlignment(SwingConstants.RIGHT);
		// dayfield.setValue(new Integer(15));
		dayfield.setValue(15);
		dayfield.setColumns(6);
		level2_Pane_Cal.add(dayfield, "4, 4, left, top");

		JLabel lblHour = new JLabel("Hour");
		level2_Pane_Cal.add(lblHour, "2, 6, right, default");

		hourfield = new JFormattedTextField();
		hourfield.setColumns(6);
		hourfield.setHorizontalAlignment(SwingConstants.RIGHT);
		hourfield.setValue(14);
		level2_Pane_Cal.add(hourfield, "4, 6, left, top");

		JLabel lblMinute = new JLabel("Minute");
		level2_Pane_Cal.add(lblMinute, "2, 8, right, default");

		minutefield = new JFormattedTextField();
		minutefield.setHorizontalAlignment(SwingConstants.RIGHT);
		minutefield.setColumns(6);
		minutefield.setValue(54);
		level2_Pane_Cal.add(minutefield, "4, 8, left, top");

		JLabel lblSecond = new JLabel("Second");
		level2_Pane_Cal.add(lblSecond, "2, 10, right, default");

		secondfield = new JFormattedTextField();
		secondfield.setHorizontalAlignment(SwingConstants.RIGHT);
		secondfield.setColumns(6);
		secondfield.setValue(23);
		level2_Pane_Cal.add(secondfield, "4, 10, left, top");
	}

}
