package jat.application.DE405Propagator.ParameterSet;

import jat.application.DE405Propagator.DE405PropagatorParameters;
import jat.core.ephemeris.DE405Body.body;
import jat.core.ephemeris.DE405Frame.frame;
import jat.core.spacetime.TimeAPL;

public class earthMoonMEOP extends DE405PropagatorParameters {

	public earthMoonMEOP() {
		super();

		Name = "earthMoonMEOP";
		// bodyGravOnOff[body.SUN.ordinal()] = true;
		bodyGravOnOff[body.EARTH.ordinal()] = true;
		bodyGravOnOff[body.MOON.ordinal()] = true;

		Frame = frame.MEOP;

		simulationDate = new TimeAPL(2003, 2, 28, 13, 30, 0);
		//simulationDate = new TimeAPL();

		// earth orbit to moon and back

		// SolarSystemBodies sb = new SolarSystemBodies();
		// double altitude = 150.;
		// y0[0] = sb.Bodies[body.EARTH.ordinal()].radius+altitude;

		
		y0[0] = -7000.;
		y0[1] = 0.;
		y0[2] = 0;
		y0[3] = 0.;
		y0[4] = -10.6;
		y0[5] = 0;
		tf = 700000.;

	}

}
