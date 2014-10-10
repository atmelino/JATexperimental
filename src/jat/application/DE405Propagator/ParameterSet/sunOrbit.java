package jat.application.DE405Propagator.ParameterSet;

import jat.application.DE405Propagator.DE405PropagatorParameters;
import jat.core.ephemeris.DE405Body.body;
import jat.core.ephemeris.DE405Frame.frame;
import jat.core.spacetime.TimeAPL;

public class sunOrbit  extends DE405PropagatorParameters{

	public sunOrbit() {
		super();

		Name = "sunOrbit";

		bodyGravOnOff[body.SUN.ordinal()] = true;
//		bodyGravOnOff[body.MARS.ordinal()] = true;
//		bodyGravOnOff[body.JUPITER.ordinal()] = true;
		
		Frame=frame.HEE;
//		Frame=frame.ICRF;
		simulationDate = new TimeAPL(2010, 2, 10, 12, 0, 0);
	
		y0[0] = 200000000.;
		y0[1] = 0;
		y0[2] = 0;
		y0[4] = 24.2;
		tf = 40000000.;
	
	}
	
}
