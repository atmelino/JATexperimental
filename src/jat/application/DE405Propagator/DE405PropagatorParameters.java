package jat.application.DE405Propagator;

import jat.core.ephemeris.DE405Body.body;
import jat.core.ephemeris.DE405Frame.frame;
import jat.core.spacetime.TimeAPL;

public class DE405PropagatorParameters {
	public String Name;
	public  TimeAPL simulationDate;
	int numberOfBodies = body.values().length;
	boolean[] showPlanet = new boolean[numberOfBodies];
	public boolean[] bodyGravOnOff = new boolean[numberOfBodies];
	public frame Frame;
	public double[] y0=new double[6] ; // initial position and velocity
	public double tf;

	public DE405PropagatorParameters() {

		for (int i = 0; i < numberOfBodies; i++) {
			this.showPlanet[i] = true;
			bodyGravOnOff[i] = false;
		}
	}

	public DE405PropagatorParameters(boolean[] planetOnOff) {
		this.showPlanet = planetOnOff;
	}

}
