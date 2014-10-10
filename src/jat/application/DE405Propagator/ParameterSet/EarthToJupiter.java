package jat.application.DE405Propagator.ParameterSet;

import jat.application.DE405Propagator.DE405PropagatorParameters;
import jat.core.ephemeris.DE405Body.body;
import jat.core.ephemeris.DE405Frame.frame;
import jat.core.spacetime.TimeAPL;

public class EarthToJupiter  extends DE405PropagatorParameters{

	public EarthToJupiter() {
		super();

		Name = "EarthToJupiter";

		bodyGravOnOff[body.SUN.ordinal()] = true;
		bodyGravOnOff[body.EARTH.ordinal()] = true;
		bodyGravOnOff[body.JUPITER.ordinal()] = true;
		
		Frame=frame.HEE;
		simulationDate = new TimeAPL(2003, 2, 1, 12, 0, 0);
	
		y0[0] = -98728754.;
		y0[1] = 108554675.;
		y0[2] = -1941.;
		y0[4] = 24.2;
		tf = 1000.;
	
	}
	
}
