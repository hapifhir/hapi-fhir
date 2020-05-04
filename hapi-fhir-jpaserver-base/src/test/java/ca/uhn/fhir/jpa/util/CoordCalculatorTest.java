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

	// A Fiji island near the anti-meridian
	public static final double LATITUDE_TAVEUNI = -16.8488893;
	public static final double LONGITIDE_TAVEUNI = 179.889793;
	// enough distance from point to cross anti-meridian
	public static final double DISTANCE_TAVEUNI = 100.0;

	@Test
	public void testCHINToUHN() {
		Point result = CoordCalculator.findTarget(LATITUDE_CHIN, LONGITUDE_CHIN, BEARING_CHIN_TO_UHN, DISTANCE_KM_CHIN_TO_UHN);

		assertEquals(LATITUDE_UHN, result.getLatitude(), 0.0001);
		assertEquals(LONGITUDE_UHN, result.getLongitude(), 0.0001);
	}

	@Test
	public void testBox() {
		SearchBox box = CoordCalculator.getBox(LATITUDE_CHIN, LONGITUDE_CHIN, 1.0);
		double expectedLatitudeDelta = 0.0090;
		assertEquals(LATITUDE_CHIN - expectedLatitudeDelta, box.getSouthWest().getLatitude(), 0.0001);
		assertEquals(LATITUDE_CHIN + expectedLatitudeDelta, box.getNorthEast().getLatitude(), 0.0001);
		double expectedLongitudeDelta = 0.012414;
		assertEquals(LONGITUDE_CHIN - expectedLongitudeDelta, box.getSouthWest().getLongitude(), 0.0001);
		assertEquals(LONGITUDE_CHIN + expectedLongitudeDelta, box.getNorthEast().getLongitude(), 0.0001);
	}

	@Test
	public void testOnPrimeMeridian() {
		double meridianLongitide = 0.0;
		SearchBox box = CoordCalculator.getBox(LATITUDE_CHIN, meridianLongitide, 1.0);
		double expectedLatitudeDelta = 0.0090;
		assertEquals(LATITUDE_CHIN - expectedLatitudeDelta, box.getSouthWest().getLatitude(), 0.0001);
		assertEquals(LATITUDE_CHIN + expectedLatitudeDelta, box.getNorthEast().getLatitude(), 0.0001);
		double expectedLongitudeDelta = 0.012414;
		assertEquals(meridianLongitide - expectedLongitudeDelta, box.getSouthWest().getLongitude(), 0.0001);
		assertEquals(meridianLongitide + expectedLongitudeDelta, box.getNorthEast().getLongitude(), 0.0001);
	}

	@Test
	public void testOnAntiMeridian() {
		SearchBox box = CoordCalculator.getBox(LATITUDE_TAVEUNI, LONGITIDE_TAVEUNI, 100.0);
		double expectedLatitudeDelta = 0.90;
		assertEquals(LATITUDE_TAVEUNI - expectedLatitudeDelta, box.getSouthWest().getLatitude(), 0.01);
		assertEquals(LATITUDE_TAVEUNI + expectedLatitudeDelta, box.getNorthEast().getLatitude(), 0.01);
		double expectedLongitudeDelta = 0.94;
		assertEquals(LONGITIDE_TAVEUNI - expectedLongitudeDelta, box.getSouthWest().getLongitude(), 0.01);
		// This case wraps
		assertEquals(LONGITIDE_TAVEUNI + expectedLongitudeDelta - 360.0, box.getNorthEast().getLongitude(), 0.01);
	}

}
