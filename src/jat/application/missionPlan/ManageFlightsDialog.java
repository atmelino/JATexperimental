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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ManageFlightsDialog extends JDialog implements ActionListener, ListSelectionListener {

	private static final long serialVersionUID = 6771932082871532590L;
	JButton okButton;
	JButton cancelButton;
	MissionPlanMain ssmain;
	public boolean OK_pressed;
	private JList list;
	private DefaultListModel model = new DefaultListModel();
	private JPanel panel;
	private JPanel panel_1;
	private JLabel lblNewLabel;
	private JPanel panelFlightList;
	private JFormattedTextField field_departureDate;
	private JLabel lblNewLabel_1;
	private JFormattedTextField field_name;
	private JLabel lblNewLabel_2;
	private JFormattedTextField field_departurePlanet;
	private JLabel lblNewLabel_3;
	private JFormattedTextField field_arrivalPlanet;
	private JLabel lblNewLabel_4;
	private JFormattedTextField field_arrivalDate;
	private JButton btnDelete;
	private JLabel lblColor;
	private JButton btnColor;
	private JLabel lblNewLabel_5;
	private JFormattedTextField field_totaldv;

	public ManageFlightsDialog(MissionPlanMain ssmain) {
		super(ssmain.sFrame, true); // modal
		setTitle("Manage Flights");
		this.ssmain = ssmain;
		setBounds(50, 50, 900, 700);
		getContentPane().setLayout(new BorderLayout());
		{
			panel = new JPanel();
			getContentPane().add(panel, BorderLayout.NORTH);
			panel.setLayout(new GridLayout(0, 2, 0, 0));
			{
				panelFlightList = new JPanel();
				panelFlightList.setBorder(new TitledBorder(null, "Flight List", TitledBorder.LEADING, TitledBorder.TOP,
						null, null));
				panel.add(panelFlightList);
				{
					// list = new JList();
					list = new JList(model);
					for (int i = 0; i < ssmain.flightList.size(); i++)
						model.addElement(ssmain.flightList.get(i));
					// model.addElement("Java");
					// model.addElement("Visual Basic");
					// model.addElement("C++");
					// model.addElement("C");
					// list = new JList(ssmain.flightList.toArray());
					list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
					list.setModel(model);
					panelFlightList.add(list);
				}
				{
					btnDelete = new JButton("Delete");
					panelFlightList.add(btnDelete);
				}
				btnDelete.addActionListener(this);
			}
			{
				panel_1 = new JPanel();
				panel.add(panel_1);
				panel_1.setLayout(new GridLayout(20, 1, 0, 0));
				{
					lblNewLabel_1 = new JLabel("Flight Name");
					panel_1.add(lblNewLabel_1);
				}
				{
					field_name = new JFormattedTextField();
					panel_1.add(field_name);
				}
				{
					lblColor = new JLabel("Color");
					panel_1.add(lblColor);
				}
				{
					btnColor = new JButton("");
					btnColor.setBackground(new Color(175, 238, 238));
					panel_1.add(btnColor);
				}
				{
					lblNewLabel_2 = new JLabel("Departure Planet");
					panel_1.add(lblNewLabel_2);
				}
				{
					field_departurePlanet = new JFormattedTextField();
					panel_1.add(field_departurePlanet);
				}
				{
					lblNewLabel_3 = new JLabel("Arrival Planet");
					panel_1.add(lblNewLabel_3);
				}
				{
					field_arrivalPlanet = new JFormattedTextField();
					panel_1.add(field_arrivalPlanet);
				}
				{
					lblNewLabel = new JLabel("Departure Date");
					panel_1.add(lblNewLabel);
				}
				{
					field_departureDate = new JFormattedTextField();
					panel_1.add(field_departureDate);
				}
				{
					lblNewLabel_4 = new JLabel("Arrival Date");
					panel_1.add(lblNewLabel_4);
				}
				{
					field_arrivalDate = new JFormattedTextField();
					panel_1.add(field_arrivalDate);
				}
				{
					lblNewLabel_5 = new JLabel("Total Delta-V [km/sec]");
					panel_1.add(lblNewLabel_5);
				}
				{
					field_totaldv = new JFormattedTextField();
					panel_1.add(field_totaldv);
				}
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				okButton.addActionListener(this);
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				cancelButton.addActionListener(this);
				buttonPane.add(cancelButton);
			}
		}
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		list.addListSelectionListener(this);
		setVisible(true);

	}

	public void actionPerformed(ActionEvent e) {
		if (okButton == e.getSource()) {
			OK_pressed = true;
			setVisible(false);
		} else if (cancelButton == e.getSource()) {
			OK_pressed = false;
			setVisible(false);
		}
		if (e.getSource() == btnDelete) {
			// Flight f = ssmain.flightList.get(list.getSelectedIndex());
			int index = list.getSelectedIndex();
			System.out.println("removing flight list index " + index);

			ssmain.mpPlot.jatScene.remove(ssmain.flightList.get(index).flightName);
			ssmain.mpPlot.jatScene.remove(ssmain.flightList.get(index).satelliteName);

			model.removeElement(list.getSelectedValue()); // change the MODEL
			ssmain.flightList.remove(index);
		}
	}

	public void valueChanged(ListSelectionEvent evt) {
		if (ssmain.flightList.size() > 0) {
			if (list.getSelectedIndex() > -1) {
				Flight f = ssmain.flightList.get(list.getSelectedIndex());
				// System.out.println("Selected from " + evt.getFirstIndex() +
				// " to " +
				// evt.getLastIndex());
				field_name.setText(f.flightName);
				Color col=new Color(f.color.x,f.color.y,f.color.z);
				btnColor.setBackground(col);
				field_departurePlanet.setText(f.departurePlanetName);
				field_arrivalPlanet.setText(f.arrivalPlanetName);
				String dateformat = "%tD";
				String dateText = String.format(dateformat, f.departureDate.getCalendar());
				field_departureDate.setText(dateText);
				dateText = String.format(dateformat, f.arrivalDate.getCalendar());
				field_arrivalDate.setText(dateText);
				field_totaldv.setText(String.format("%f",f.totaldv));				
			}
		}
	}
}
