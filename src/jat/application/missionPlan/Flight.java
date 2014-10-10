package jat.application.missionPlan;

import jat.core.ephemeris.DE405Body;
import jat.core.spacetime.TimeAPL;
import jat.coreNOSA.cm.Lambert;
import jat.coreNOSA.math.MatrixVector.data.VectorN;
import jat.jat3D.Sphere3D;
import jat.jat3D.TwoBodyOrbit3D;

import javax.vecmath.Color3f;

public class Flight {
	String flightName;
	Color3f color;
	String satelliteName;
	public double mu;
	DE405Body.body departure_planet;
	String departurePlanetName;
	String arrivalPlanetName;
	DE405Body.body arrival_planet;
	TimeAPL departureDate;
	TimeAPL arrivalDate;
	double tof;
	Lambert lambert;
	VectorN r0, v0, rf, vf, dv0, dvf;
	public double totaldv;
	double t0_on_orbit;
	TwoBodyOrbit3D orbit;
	Sphere3D satellite;

}
