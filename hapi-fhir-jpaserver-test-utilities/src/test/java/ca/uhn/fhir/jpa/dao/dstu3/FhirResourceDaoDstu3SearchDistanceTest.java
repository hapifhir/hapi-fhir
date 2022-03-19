package ca.uhn.fhir.jpa.dao.dstu3;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaDstu3Test;
import ca.uhn.fhir.jpa.util.CoordCalculatorTest;
import ca.uhn.fhir.rest.param.TokenParam;
import org.hl7.fhir.dstu3.model.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class FhirResourceDaoDstu3SearchDistanceTest extends BaseJpaDstu3Test {
	@Autowired
	MatchUrlService myMatchUrlService;

	@BeforeEach
	public void beforeDisableResultReuse() {
		myDaoConfig.setReuseCachedSearchResultsForMillis(null);
	}

	@Test
	public void testNearSearchDistanceNoDistance() {
		Location loc = new Location();
		double latitude = CoordCalculatorTest.LATITUDE_CHIN;
		double longitude = CoordCalculatorTest.LONGITUDE_CHIN;
		Location.LocationPositionComponent position = new Location.LocationPositionComponent().setLatitude(latitude).setLongitude(longitude);
		loc.setPosition(position);
		String locId = myLocationDao.create(loc).getId().toUnqualifiedVersionless().getValue();

		SearchParameterMap map = myMatchUrlService.translateMatchUrl(
			"Location?" +
				Location.SP_NEAR + "=" + latitude + ":" + longitude,
			myFhirContext.getResourceDefinition("Location"));

		List<String> ids = toUnqualifiedVersionlessIdValues(myLocationDao.search(map));
		assertThat(ids, contains(locId));
	}

	@Test
	public void testNearSearchDistanceZero() {
		Location loc = new Location();
		double latitude = CoordCalculatorTest.LATITUDE_CHIN;
		double longitude = CoordCalculatorTest.LONGITUDE_CHIN;
		Location.LocationPositionComponent position = new Location.LocationPositionComponent().setLatitude(latitude).setLongitude(longitude);
		loc.setPosition(position);
		String locId = myLocationDao.create(loc).getId().toUnqualifiedVersionless().getValue();

		SearchParameterMap map = myMatchUrlService.translateMatchUrl(
			"Location?" +
				Location.SP_NEAR + "=" + latitude + ":" + longitude +
				"&" +
				Location.SP_NEAR_DISTANCE + "=0||",
			myFhirContext.getResourceDefinition("Location"));

		List<String> ids = toUnqualifiedVersionlessIdValues(myLocationDao.search(map));
		assertThat(ids, contains(locId));
	}

	@Test
	public void testNearSearchApproximate() {
		Location loc = new Location();
		double latitude = CoordCalculatorTest.LATITUDE_UHN;
		double longitude = CoordCalculatorTest.LONGITUDE_UHN;
		Location.LocationPositionComponent position = new Location.LocationPositionComponent().setLatitude(latitude).setLongitude(longitude);
		loc.setPosition(position);
		String locId = myLocationDao.create(loc).getId().toUnqualifiedVersionless().getValue();

		{ // In the box
			double bigEnoughDistance = CoordCalculatorTest.DISTANCE_KM_CHIN_TO_UHN * 2;
			SearchParameterMap map = myMatchUrlService.translateMatchUrl(
				"Location?" +
					Location.SP_NEAR + "=" + CoordCalculatorTest.LATITUDE_CHIN + ":" + CoordCalculatorTest.LONGITUDE_CHIN +
					"&" +
					Location.SP_NEAR_DISTANCE + "=" + bigEnoughDistance + "|http://unitsofmeasure.org|km", myFhirContext.getResourceDefinition("Location"));

			List<String> ids = toUnqualifiedVersionlessIdValues(myLocationDao.search(map));
			assertThat(ids, contains(locId));
		}
		{ // Outside the box
			double tooSmallDistance = CoordCalculatorTest.DISTANCE_KM_CHIN_TO_UHN / 2;

			SearchParameterMap map = myMatchUrlService.translateMatchUrl(
				"Location?" +
					Location.SP_NEAR + "=" + CoordCalculatorTest.LATITUDE_CHIN + ":" + CoordCalculatorTest.LONGITUDE_CHIN +
					"&" +
					Location.SP_NEAR_DISTANCE + "=" + tooSmallDistance + "|http://unitsofmeasure.org|km", myFhirContext.getResourceDefinition("Location"));

			List<String> ids = toUnqualifiedVersionlessIdValues(myLocationDao.search(map));
			assertThat(ids.size(), is(0));
		}

	}

	@Test
	public void testBadCoordsFormat() {
		assertInvalidNearFormat("1:2:3");
		assertInvalidNearFormat("1:");
		assertInvalidNearFormat(":");
		assertInvalidNearFormat("");
	}

	private void assertInvalidNearFormat(String theCoords) {
		SearchParameterMap map = new SearchParameterMap();
		map.add(Location.SP_NEAR, new TokenParam(theCoords));
		map.setLoadSynchronous(true);
		try {
			myLocationDao.search(map);
			fail();
		} catch (InvalidDataAccessApiUsageException e) {
			assertEquals(Msg.code(1228) + "Invalid position format '" + theCoords + "'.  Required format is 'latitude:longitude'", e.getCause().getMessage());
		}
	}

	@Test
	public void testNearMissingLat() {
		SearchParameterMap map = new SearchParameterMap();
		map.add(Location.SP_NEAR, new TokenParam(":2"));
		map.setLoadSynchronous(true);
		try {
			myLocationDao.search(map);
			fail();
		} catch (InvalidDataAccessApiUsageException e) {
			assertEquals(Msg.code(1229) + "Invalid position format ':2'.  Both latitude and longitude must be provided.", e.getCause().getMessage());
		}
	}

}
