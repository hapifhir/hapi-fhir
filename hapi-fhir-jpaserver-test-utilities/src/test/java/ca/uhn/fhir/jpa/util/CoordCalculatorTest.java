package ca.uhn.fhir.jpa.util;

import org.hibernate.search.engine.spatial.GeoBoundingBox;
import org.hibernate.search.engine.spatial.GeoPoint;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

public class CoordCalculatorTest {

	@Test
	public void testCHINToUHN() {
		GeoPoint result = CoordCalculator.findTarget(CoordCalculatorTestUtil.LATITUDE_CHIN, CoordCalculatorTestUtil.LONGITUDE_CHIN, CoordCalculatorTestUtil.BEARING_CHIN_TO_UHN, CoordCalculatorTestUtil.DISTANCE_KM_CHIN_TO_UHN);

		assertThat(result.latitude()).isCloseTo(CoordCalculatorTestUtil.LATITUDE_UHN, within(0.0001));
		assertThat(result.longitude()).isCloseTo(CoordCalculatorTestUtil.LONGITUDE_UHN, within(0.0001));
	}

	@Test
	public void testBox() {
		GeoBoundingBox box = CoordCalculator.getBox(CoordCalculatorTestUtil.LATITUDE_CHIN, CoordCalculatorTestUtil.LONGITUDE_CHIN, 1.0);
		double expectedLatitudeDelta = 0.0090;
		assertThat(box.bottomRight().latitude()).isCloseTo(CoordCalculatorTestUtil.LATITUDE_CHIN - expectedLatitudeDelta, within(0.0001));
		assertThat(box.topLeft().latitude()).isCloseTo(CoordCalculatorTestUtil.LATITUDE_CHIN + expectedLatitudeDelta, within(0.0001));
		double expectedLongitudeDelta = 0.012414;
		assertThat(box.topLeft().longitude()).isCloseTo(CoordCalculatorTestUtil.LONGITUDE_CHIN - expectedLongitudeDelta, within(0.0001));
		assertThat(box.bottomRight().longitude()).isCloseTo(CoordCalculatorTestUtil.LONGITUDE_CHIN + expectedLongitudeDelta, within(0.0001));
	}

	@Test
	public void testOnPrimeMeridian() {
		double meridianLongitide = 0.0;
		GeoBoundingBox box = CoordCalculator.getBox(CoordCalculatorTestUtil.LATITUDE_CHIN, meridianLongitide, 1.0);
		double expectedLatitudeDelta = 0.0090;
		assertThat(box.bottomRight().latitude()).isCloseTo(CoordCalculatorTestUtil.LATITUDE_CHIN - expectedLatitudeDelta, within(0.0001));
		assertThat(box.topLeft().latitude()).isCloseTo(CoordCalculatorTestUtil.LATITUDE_CHIN + expectedLatitudeDelta, within(0.0001));
		double expectedLongitudeDelta = 0.012414;
		assertThat(box.topLeft().longitude()).isCloseTo(meridianLongitide - expectedLongitudeDelta, within(0.0001));
		assertThat(box.bottomRight().longitude()).isCloseTo(meridianLongitide + expectedLongitudeDelta, within(0.0001));
	}

	@Test
	public void testOnAntiMeridian() {
		GeoBoundingBox box = CoordCalculator.getBox(CoordCalculatorTestUtil.LATITUDE_TAVEUNI, CoordCalculatorTestUtil.LONGITIDE_TAVEUNI, 100.0);
		double expectedLatitudeDelta = 0.90;
		assertThat(box.bottomRight().latitude()).isCloseTo(CoordCalculatorTestUtil.LATITUDE_TAVEUNI - expectedLatitudeDelta, within(0.01));
		assertThat(box.topLeft().latitude()).isCloseTo(CoordCalculatorTestUtil.LATITUDE_TAVEUNI + expectedLatitudeDelta, within(0.01));
		double expectedLongitudeDelta = 0.94;
		assertThat(box.topLeft().longitude()).isCloseTo(CoordCalculatorTestUtil.LONGITIDE_TAVEUNI - expectedLongitudeDelta, within(0.01));
		// This case wraps
		assertThat(box.bottomRight().longitude()).isCloseTo(CoordCalculatorTestUtil.LONGITIDE_TAVEUNI + expectedLongitudeDelta - 360.0, within(0.01));
	}

}
