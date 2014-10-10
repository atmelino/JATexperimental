package jat.examples.DatePicker;

import javax.swing.JApplet;
import javax.swing.JComponent;

import net.sourceforge.jdatepicker.JDateComponentFactory;
import net.sourceforge.jdatepicker.JDatePicker;

public class DatePickerExample extends JApplet{

	private static final long serialVersionUID = 1920676464239324135L;
	JDatePicker depart_date_picker;

	public void init() {
		depart_date_picker = JDateComponentFactory.createJDatePicker();
		depart_date_picker.setTextEditable(true);
		depart_date_picker.setShowYearButtons(true);
		
		add((JComponent) depart_date_picker);
		
	}
	
	public void start() {

		depart_date_picker.getModel().setYear(2010);
		depart_date_picker.getModel().setMonth(1);
		//depart_date_picker.getModel().setMonth(1);
		depart_date_picker.getModel().setDay(15);
		depart_date_picker.getModel().setSelected(true);	
	}

}
