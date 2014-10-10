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

import jat.core.util.ResourceLoader;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class MissionPlanGUI extends JPanel {
	private static final long serialVersionUID = 1321470082814219656L;
	JFormattedTextField yearfield;
	JFormattedTextField monthfield;
	JFormattedTextField dayfield;
	JFormattedTextField hourfield;
	JFormattedTextField minutefield;
	JFormattedTextField secondfield;
	JFormattedTextField timestepfield;
	JFormattedTextField viewdistancefield;
	public MissionPlanMain mpMain;
	JPanel level2_Pane_Plot;
	JPanel button_panel;
	JPanel mission_panel;
	public JButton btn_stop;
	public JButton btn_forward;
	public JButton btn_rewind;
	JCheckBox realtime_chk;
	public JButton btnAddFlight;
	private JLabel label;
	private JLabel label_1;
	public JButton btnManageFlights;
	public MissionPlanEvents mpE;
	public JCheckBox chckbxCameraRotate;

	public MissionPlanGUI(MissionPlanMain mpMain) {
		this.mpMain = mpMain;
		mpE = new MissionPlanEvents(mpMain);

		setLayout(new BorderLayout(0, 0));
		JPanel level1_Pane = new JPanel();

		add(level1_Pane);
		level1_Pane.setLayout(new BoxLayout(level1_Pane, BoxLayout.X_AXIS));

		// o = new solarsystemplot(this);
		// o.make_plot();
		JPanel level2_Pane_Control = new JPanel();
		level1_Pane.add(level2_Pane_Control);
		level2_Pane_Control.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JPanel level3_panel_box = new JPanel();
		level2_Pane_Control.add(level3_panel_box);
		level3_panel_box.setLayout(new BoxLayout(level3_panel_box,
				BoxLayout.Y_AXIS));

		JPanel level4_panel_Date = new JPanel();
		level4_panel_Date.setBorder(new TitledBorder(null, "Date",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		level3_panel_box.add(level4_panel_Date);
		level4_panel_Date.setLayout(new BoxLayout(level4_panel_Date,
				BoxLayout.Y_AXIS));

		realtime_chk = new JCheckBox("Realtime");
		realtime_chk.setAlignmentX(Component.CENTER_ALIGNMENT);
		level4_panel_Date.add(realtime_chk);
		realtime_chk.setHorizontalAlignment(SwingConstants.CENTER);
		JPanel level5_Pane_numbers = new JPanel();
		level4_panel_Date.add(level5_Pane_numbers);
		level5_Pane_numbers.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("right:default"),
				FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("left:max(17dlu;default)"), }, new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC, }));
		level5_Pane_numbers.setBorder(null);

		JLabel lblY = new JLabel("Y");
		lblY.setHorizontalAlignment(SwingConstants.CENTER);
		level5_Pane_numbers.add(lblY, "2, 1, center, default");

		JLabel lblM = new JLabel("M");
		lblM.setHorizontalAlignment(SwingConstants.CENTER);
		level5_Pane_numbers.add(lblM, "4, 1");

		JLabel lblD = new JLabel("D");
		level5_Pane_numbers.add(lblD, "6, 1, center, default");

		yearfield = new JFormattedTextField();
		yearfield.setColumns(4);
		yearfield.setValue(8000.);
		level5_Pane_numbers.add(yearfield, "2, 3, left, fill");
		yearfield.requestFocusInWindow();

		monthfield = new JFormattedTextField();
		monthfield.setColumns(2);
		monthfield.setValue(0.1);
		level5_Pane_numbers.add(monthfield, "4, 3, left, fill");

		dayfield = new JFormattedTextField();
		dayfield.setColumns(2);
		level5_Pane_numbers.add(dayfield, "6, 3, left, fill");

		JLabel lblHr = new JLabel("hr");
		level5_Pane_numbers.add(lblHr, "2, 5");

		JLabel lblMi = new JLabel("mi");
		level5_Pane_numbers.add(lblMi, "4, 5");

		JLabel lblSc = new JLabel("sc");
		level5_Pane_numbers.add(lblSc, "6, 5");

		hourfield = new JFormattedTextField();
		hourfield.setColumns(2);
		level5_Pane_numbers.add(hourfield, "2, 7, right, fill");

		minutefield = new JFormattedTextField();
		minutefield.setColumns(2);
		level5_Pane_numbers.add(minutefield, "4, 7, left, fill");

		secondfield = new JFormattedTextField();
		secondfield.setColumns(2);
		level5_Pane_numbers.add(secondfield, "6, 7, left, fill");

		button_panel = new JPanel();
		level4_panel_Date.add(button_panel);
		button_panel.setLayout(new BoxLayout(button_panel, BoxLayout.X_AXIS));
		URL iconsUrl = null;
		try {
			ResourceLoader c = new ResourceLoader();
			iconsUrl = c.loadURL(this.getClass(), "icons/");
			//System.out.println("[MissionPlanGUI] " + iconsUrl);
			mpMain.mpParam.messages.addln("[MissionPlanGUI] " + iconsUrl);
			// displayURL(helpURL2, editorPane, relative_path);

		} catch (Exception e) {
			System.err.println("[MissionPlanGUI Exception]");
			System.exit(0);
		}
		URL rewindURL = null;
		try {
			rewindURL = new URL(iconsUrl.toExternalForm() + "Rewind12.gif");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		// String iconPath="/jat/application/missionPlan/icons/";
		// String iconPath=iconUrl.getFile();

		//System.out.println("[MissionPlanGUI before button icons] ");

		btn_rewind = new JButton("");
		// btn_rewind.setIcon(new ImageIcon(rewindURL));
		// btn_rewind.setIcon(new ImageIcon(MissionPlanGUI.class
		// .getResource(iconPath+"Rewind12.gif")));
		button_panel.add(btn_rewind);

		btn_stop = new JButton("");
		// btn_stop.setIcon(new ImageIcon(MissionPlanGUI.class
		// .getResource(iconPath+"Stop12.gif")));
		button_panel.add(btn_stop);

		btn_forward = new JButton("");
		// btn_forward.setIcon(new ImageIcon(MissionPlanGUI.class
		// .getResource(iconPath+"FastForward12.gif")));
		button_panel.add(btn_forward);

		//System.out.println("[MissionPlanGUI after button icons] ");

		JLabel lblNewLabel = new JLabel("time step");
		level4_panel_Date.add(lblNewLabel);

		timestepfield = new JFormattedTextField();
		timestepfield.setColumns(8);
		level4_panel_Date.add(timestepfield);

		JLabel lblNewLabel_1 = new JLabel("Mission Time");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.LEFT);
		level3_panel_box.add(lblNewLabel_1);

		viewdistancefield = new JFormattedTextField();
		viewdistancefield.setColumns(5);
		level3_panel_box.add(viewdistancefield);

		chckbxCameraRotate = new JCheckBox("Camera Rotate");
		chckbxCameraRotate.setAlignmentX(Component.CENTER_ALIGNMENT);
		chckbxCameraRotate.setHorizontalAlignment(SwingConstants.LEFT);
		level3_panel_box.add(chckbxCameraRotate);

		mission_panel = new JPanel();
		mission_panel.setBorder(new TitledBorder(null, "Mission",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		level3_panel_box.add(mission_panel);
		mission_panel.setLayout(new GridLayout(0, 1, 0, 0));

		btnAddFlight = new JButton("Add Flight");
		mission_panel.add(btnAddFlight);

		btnManageFlights = new JButton("Manage Flights");
		btnManageFlights.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		mission_panel.add(btnManageFlights);

		label = new JLabel("");
		mission_panel.add(label);

		label_1 = new JLabel("");
		mission_panel.add(label_1);

		realtime_chk.addItemListener(mpE);
		btnAddFlight.addActionListener(mpE);
		btnManageFlights.addActionListener(mpE);
		btn_stop.addActionListener(mpE);
		btn_rewind.addActionListener(mpE);
		btn_forward.addActionListener(mpE);
		chckbxCameraRotate.addItemListener(mpE);
	}
}
