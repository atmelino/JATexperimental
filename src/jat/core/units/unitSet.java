package jat.core.units;

public class unitSet {

	public enum distanceUnit {
		nm, um, mm, cm, m, km, AU, parsec, lightyear
	}

	public static String[] distanceName = { "nm", "um", "mm", "cm", "m", "km", "AU", "parsec", "lightyear" };

	public enum timeUnit {
		nsec, usec, msec, sec, min, hour, day, week, month, year
	}

	public static String[] timeName = { "nsec", "usec", "msec", "sec", "min", "hour", "day", "week", "month", "year" };

	public enum massUnit {
		ng, ug, mg, g, kg
	}
	public static String[] massName={
		"ng", "ug", "mg", "g", "kg"
	};
			
			
	String className;
	distanceUnit distance;
	timeUnit time;
	massUnit mass;

	public unitSet(String className, distanceUnit km, timeUnit sec, massUnit kg) {
		super();
		this.className = className;
		this.distance = km;
		this.time = sec;
		this.mass = kg;
	}

}
