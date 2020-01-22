package ca.uhn.fhir.jpa.util;

import org.junit.Test;
import org.springframework.data.geo.Point;

import static org.junit.Assert.*;

public class CoordCalculatorTest {
	@Test
	public void testCoordCalculator() {
		double latitude = 52.20472;
		double longitude = 0.14056;
		double bearing = 1.57;
		double distance = 15.0;

		Point result = CoordCalculator.findTarget(latitude, longitude, bearing, distance);
		assertEquals(52.20444, result.getX(), 0.00001);
		assertEquals(0.36056, result.getX(), 0.00001);
	}
}
