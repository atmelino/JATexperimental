package jat.application.EarthMoon;

import jat.core.ephemeris.DE405Body;
import jat.core.ephemeris.DE405Plus;
import jat.core.spacetime.TimeAPL;
import jat.core.util.PathUtil;
import jat.core.util.jatMessages;

public class EarthMoonParameters {
	TimeAPL simulationDate;
	int numberOfBodies = DE405Body.body.values().length;
	boolean[] planetOnOff;
	float[] planetMagnification;
	PathUtil path;
	jatMessages messages;
	DE405Plus Eph;

	public EarthMoonParameters() {
		messages = new jatMessages();
		planetOnOff = new boolean[numberOfBodies];
		planetMagnification=new float[numberOfBodies];
		simulationDate = new TimeAPL(2003, 3, 1, 12, 0, 0);

		for (int i = 0; i < numberOfBodies; i++) {
			planetOnOff[i] = false;
			planetMagnification[i]=1.f;
		}
	}

	public EarthMoonParameters(boolean[] planetOnOff) {
		this.planetOnOff = planetOnOff;
	}

}
