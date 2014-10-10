package jat.application.DE405Propagator;

import jat.core.astronomy.SolarSystemBodies;
import jat.core.ephemeris.DE405Plus;
import jat.core.units.unitCheck;
import jat.core.util.jatMessages;

public class DE405PropagatorGlobals {
	unitCheck uc = new unitCheck();
	SolarSystemBodies sb = new SolarSystemBodies();
	DE405Plus Eph;
	// PathUtil path;
	jatMessages messages;

	public DE405PropagatorGlobals() {
		messages = new jatMessages();
	}

}
