package jat.examples.JulianDate;

import jat.coreNOSA.cm.cm;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;

class DateConverterEvents implements ActionListener {
	DateConverterGUI d;

	public DateConverterEvents(DateConverterGUI d) {
		this.d = d;
	}

	public void actionPerformed(ActionEvent ev) {

		// Read in values
		if (ev.getSource() == d.btn_Jul_to_Cal) {
			System.out.println("button 1 pressed");
			JD_to_Cal();
		}// End of if(ev.getSource() == startButton)
		if (ev.getSource() == d.btn_Cal_to_Jul) {
			System.out.println("button 2 pressed");
			Cal_to_JD();
		}
	}// End of ActionPerformed

	void JD_to_Cal() {
		SimpleDateFormat sdf;
		sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aaa");
		Calendar cal = cm.JD_to_Calendar((Double) d.JDfield.getValue());
		System.out.println("JD: " + (Double) d.JDfield.getValue() + "   Greg: "
				+ sdf.format(cal.getTime()));
		d.yearfield.setValue(cal.get(Calendar.YEAR));
		d.monthfield.setValue(cal.get(Calendar.MONTH)+1);
		d.dayfield.setValue(cal.get(Calendar.DAY_OF_MONTH));
		d.hourfield.setValue(cal.get(Calendar.HOUR_OF_DAY));
		d.minutefield.setValue(cal.get(Calendar.MINUTE));
		d.secondfield.setValue(cal.get(Calendar.SECOND));
	}

	void Cal_to_JD() {
		Integer year, month, day, hour, minute, second;
		year = (Integer) d.yearfield.getValue();
		month = (Integer) d.monthfield.getValue();
		day = (Integer) d.dayfield.getValue();
		hour = (Integer) d.hourfield.getValue();
		minute = (Integer) d.minutefield.getValue();
		second = (Integer) d.secondfield.getValue();
		d.JDfield.setValue((Double) cm.juliandate(year, month, day, hour, minute, second));
		// System.out.println(year);
		// System.out.println(cm.juliandate(year, month, 1, 12, 0, 0));
	}

}// End of Button Handler
