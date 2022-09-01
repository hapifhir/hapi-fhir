package ca.uhn.fhir.jpa.util;

public final class CoordCalculatorTestUtil {
	// CHIN and UHN coordinates from Google Maps
	// Distance and bearing from https://www.movable-type.co.uk/scripts/latlong.html
	public static final double LATITUDE_CHIN = 43.65513;
	public static final double LONGITUDE_CHIN = -79.4170007;
	public static final double LATITUDE_UHN = 43.656765;
	public static final double LONGITUDE_UHN = -79.3987645;
	public static final double DISTANCE_KM_CHIN_TO_UHN = 1.478;
	public static final double BEARING_CHIN_TO_UHN = 82 + (55.0 / 60) + (46.0 / 3600);
	// A Fiji island near the anti-meridian
	public static final double LATITUDE_TAVEUNI = -16.8488893;
	public static final double LONGITIDE_TAVEUNI = 179.889793;
	// enough distance from point to cross anti-meridian
	public static final double DISTANCE_TAVEUNI = 100.0;

	private CoordCalculatorTestUtil() {}
}
