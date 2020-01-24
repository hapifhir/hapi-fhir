package ca.uhn.fhir.jpa.util;


import org.hibernate.search.spatial.impl.Point;

public class CoordCalculator {
	public static final double MAX_SUPPORTED_DISTANCE_KM = 10000.0; // Slightly less than a quarter of the earth's circumference
	private static final double RADIUS_EARTH_KM = 6378.1;

	// Source: https://stackoverflow.com/questions/7222382/get-lat-long-given-current-point-distance-and-bearing
	static Point findTarget(double theLatitudeDegrees, double theLongitudeDegrees, double theBearingDegrees, double theDistanceKm) {

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

	/**
	 * Find a box around my coordinates such that the closest distance to each edge is the provided distance
	 */
	public static SearchBox getBox(double theLatitudeDegrees, double theLongitudeDegrees, Double theDistanceKm) {
		double diagonalDistanceKm = theDistanceKm * Math.sqrt(2.0);

		Point northEast = CoordCalculator.findTarget(theLatitudeDegrees, theLongitudeDegrees, 45.0, diagonalDistanceKm);
		Point southWest = CoordCalculator.findTarget(theLatitudeDegrees, theLongitudeDegrees, 225.0, diagonalDistanceKm);

		return new SearchBox(southWest, northEast);
	}
}
