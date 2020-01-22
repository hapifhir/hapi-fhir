package ca.uhn.fhir.jpa.util;

import org.hibernate.search.spatial.impl.Point;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;

public class CoordCalculatorTest {
	private final Logger ourLog = LoggerFactory.getLogger(CoordCalculatorTest.class);
	// CHIN and UHN coordinates from Google Maps
	// Distance and bearing from https://www.movable-type.co.uk/scripts/latlong.html
	public static final double LATITUDE_CHIN = 43.65513;
	public static final double LONGITUDE_CHIN = -79.4170007;
	public static final double LATITUDE_UHN = 43.656765;
	public static final double LONGITUDE_UHN = -79.3987645;
	public static final double DISTANCE_KM_CHIN_TO_UHN = 1.478;
	public static final double BEARING_CHIN_TO_UHN = 82 + (55.0 / 60) + (46.0 / 3600);

	@Test
	public void testCHINToUHN() {
		Point result = CoordCalculator.findTarget(LATITUDE_CHIN, LONGITUDE_CHIN, BEARING_CHIN_TO_UHN, DISTANCE_KM_CHIN_TO_UHN);

		assertEquals(LATITUDE_UHN, result.getLatitude(), 0.0001);
		assertEquals(LONGITUDE_UHN, result.getLatitude(), 0.0001);
	}
}
