package jat.core.units;

import java.util.ArrayList;

public class unitCheck {

	public ArrayList<unitSet> unitUserList = new ArrayList<unitSet>();

	public void addUser(unitSet uS) {
		unitUserList.add(uS);
	}

	public boolean check_consistency() {

		// TODO check if all users use the same units
		return true;

	}

	public void printList() {

		String sf = "%8s ";
		String format = "%20s" + " " + sf + " " + sf + " " + sf;
		System.out.printf(format, "className", "distance", "time", "mass");
		System.out.println();

		for (unitSet uS : unitUserList) {
			System.out.printf(format, uS.className, uS.distance, uS.time, uS.mass);
			System.out.println();
		}

	}
}
