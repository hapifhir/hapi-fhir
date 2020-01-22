package ca.uhn.fhir.jpa.util;


import org.hibernate.search.spatial.impl.Point;

public class CoordCalculator {
	public static final double RADIUS_EARTH_KM = 6378.1;

	public static Point findTarget(double theLatitudeDegrees, double theLongitudeDegrees, double theBearingDegrees, double theDistanceKm) {

		double latitudeRadians = Math.toRadians(theLatitudeDegrees);
		double longitudeRadians = Math.toRadians(theLongitudeDegrees);
		double bearingRadians = Math.toRadians(theBearingDegrees);
		double distanceRadians = theDistanceKm / RADIUS_EARTH_KM;

		double targetLatitude = Math.asin( Math.sin(latitudeRadians) * Math.cos(distanceRadians) +
			Math.cos(latitudeRadians) * Math.sin(distanceRadians) * Math.cos(bearingRadians));

		double targetLongitude = longitudeRadians + Math.atan2(Math.sin(bearingRadians) * Math.sin(distanceRadians) * Math.cos(latitudeRadians),
			Math.cos(distanceRadians)-Math.sin(latitudeRadians) * Math.sin(targetLatitude));

		return Point.fromDegrees(Math.toDegrees(targetLatitude), Math.toDegrees(targetLongitude));
	}
}
