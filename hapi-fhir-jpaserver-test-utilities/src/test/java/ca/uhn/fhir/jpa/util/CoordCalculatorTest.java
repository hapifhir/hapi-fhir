package ca.uhn.fhir.jpa.util;

import org.hibernate.search.engine.spatial.GeoBoundingBox;
import org.hibernate.search.engine.spatial.GeoPoint;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CoordCalculatorTest {

	@Test
	public void testCHINToUHN() {
		GeoPoint result = CoordCalculator.findTarget(CoordCalculatorTestUtil.LATITUDE_CHIN, CoordCalculatorTestUtil.LONGITUDE_CHIN, CoordCalculatorTestUtil.BEARING_CHIN_TO_UHN, CoordCalculatorTestUtil.DISTANCE_KM_CHIN_TO_UHN);

		assertEquals(CoordCalculatorTestUtil.LATITUDE_UHN, result.latitude(), 0.0001);
		assertEquals(CoordCalculatorTestUtil.LONGITUDE_UHN, result.longitude(), 0.0001);
	}

	@Test
	public void testBox() {
		GeoBoundingBox box = CoordCalculator.getBox(CoordCalculatorTestUtil.LATITUDE_CHIN, CoordCalculatorTestUtil.LONGITUDE_CHIN, 1.0);
		double expectedLatitudeDelta = 0.0090;
		assertEquals(CoordCalculatorTestUtil.LATITUDE_CHIN - expectedLatitudeDelta, box.bottomRight().latitude(), 0.0001);
		assertEquals(CoordCalculatorTestUtil.LATITUDE_CHIN + expectedLatitudeDelta, box.topLeft().latitude(), 0.0001);
		double expectedLongitudeDelta = 0.012414;
		assertEquals(CoordCalculatorTestUtil.LONGITUDE_CHIN - expectedLongitudeDelta, box.topLeft().longitude(), 0.0001);
		assertEquals(CoordCalculatorTestUtil.LONGITUDE_CHIN + expectedLongitudeDelta, box.bottomRight().longitude(), 0.0001);
	}

	@Test
	public void testOnPrimeMeridian() {
		double meridianLongitide = 0.0;
		GeoBoundingBox box = CoordCalculator.getBox(CoordCalculatorTestUtil.LATITUDE_CHIN, meridianLongitide, 1.0);
		double expectedLatitudeDelta = 0.0090;
		assertEquals(CoordCalculatorTestUtil.LATITUDE_CHIN - expectedLatitudeDelta, box.bottomRight().latitude(), 0.0001);
		assertEquals(CoordCalculatorTestUtil.LATITUDE_CHIN + expectedLatitudeDelta, box.topLeft().latitude(), 0.0001);
		double expectedLongitudeDelta = 0.012414;
		assertEquals(meridianLongitide - expectedLongitudeDelta, box.topLeft().longitude(), 0.0001);
		assertEquals(meridianLongitide + expectedLongitudeDelta, box.bottomRight().longitude(), 0.0001);
	}

	@Test
	public void testOnAntiMeridian() {
		GeoBoundingBox box = CoordCalculator.getBox(CoordCalculatorTestUtil.LATITUDE_TAVEUNI, CoordCalculatorTestUtil.LONGITIDE_TAVEUNI, 100.0);
		double expectedLatitudeDelta = 0.90;
		assertEquals(CoordCalculatorTestUtil.LATITUDE_TAVEUNI - expectedLatitudeDelta, box.bottomRight().latitude(), 0.01);
		assertEquals(CoordCalculatorTestUtil.LATITUDE_TAVEUNI + expectedLatitudeDelta, box.topLeft().latitude(), 0.01);
		double expectedLongitudeDelta = 0.94;
		assertEquals(CoordCalculatorTestUtil.LONGITIDE_TAVEUNI - expectedLongitudeDelta, box.topLeft().longitude(), 0.01);
		// This case wraps
		assertEquals(CoordCalculatorTestUtil.LONGITIDE_TAVEUNI + expectedLongitudeDelta - 360.0, box.bottomRight().longitude(), 0.01);
	}

}
