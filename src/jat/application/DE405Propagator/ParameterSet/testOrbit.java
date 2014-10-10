package jat.application.DE405Propagator.ParameterSet;

import jat.application.DE405Propagator.DE405PropagatorParameters;
import jat.core.ephemeris.DE405Body.body;
import jat.core.ephemeris.DE405Frame.frame;
import jat.core.spacetime.TimeAPL;

public class testOrbit  extends DE405PropagatorParameters{

	public testOrbit() {
		super();

		bodyGravOnOff[body.SUN.ordinal()] = true;
//		bodyGravOnOff[body.MARS.ordinal()] = true;
//		bodyGravOnOff[body.JUPITER.ordinal()] = true;
		
		Frame=frame.HEE;
//		Frame=frame.ICRF;
		simulationDate = new TimeAPL(2003, 3, 1, 12, 0, 0);
	
		y0[0] = 150.;
		y0[1] = 0;
		y0[2] = 0;
		y0[4] = 40;
		tf = 10.;
	
	}


	
	
}
