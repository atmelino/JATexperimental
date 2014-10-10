package jat.application.DE405Propagator.ParameterSet;

import jat.application.DE405Propagator.DE405PropagatorParameters;
import jat.core.ephemeris.DE405Body.body;
import jat.core.ephemeris.DE405Frame.frame;
import jat.core.spacetime.TimeAPL;

public class earthOrbitHEE extends DE405PropagatorParameters {

	public earthOrbitHEE() {
		super();

		bodyGravOnOff[body.SUN.ordinal()] = true;
		bodyGravOnOff[body.EARTH.ordinal()] = true;
		bodyGravOnOff[body.MOON.ordinal()] = true;

		Frame=frame.HEE;

		simulationDate = new TimeAPL(2003, 3, 1, 12, 0, 0);

		// earth orbit
		y0[0] = -1.394163164819393E8;
		y0[1] = 48928187.1;
		y0[2] = -6558.3;
		y0[4] = -30;
		tf = 10000.;

	}

}
