package ca.uhn.fhir.jpa.util;

import org.springframework.data.geo.Point;

public class CoordCalculator {
	public static Point findTarget(double theLatitude, double theLongitude, double theBearing, double theDistance) {
		double x;
		double y;
		x = theLatitude;
		y = theLatitude;
		return new Point(x, y);
	}
}
