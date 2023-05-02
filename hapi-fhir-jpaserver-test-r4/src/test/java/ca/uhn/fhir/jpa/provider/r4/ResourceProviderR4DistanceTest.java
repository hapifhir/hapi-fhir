package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.search.builder.QueryStack;
import ca.uhn.fhir.jpa.util.CoordCalculatorTestUtil;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Location;
import org.hl7.fhir.r4.model.PractitionerRole;
import org.hl7.fhir.r4.model.SearchParameter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ResourceProviderR4DistanceTest extends BaseResourceProviderR4Test {

	@BeforeEach
	@Override
	public void before() throws Exception {
		super.before();
		myStorageSettings.setReuseCachedSearchResultsForMillis(null);
	}

	@Test
	public void testNearSearchApproximate() {
		Location loc = new Location();
		double latitude = CoordCalculatorTestUtil.LATITUDE_UHN;
		double longitude = CoordCalculatorTestUtil.LONGITUDE_UHN;
		Location.LocationPositionComponent position = new Location.LocationPositionComponent().setLatitude(latitude).setLongitude(longitude);
		loc.setPosition(position);
		IIdType locId = myClient.create().resource(loc).execute().getId().toUnqualifiedVersionless();

		{ // In the box
			double bigEnoughDistance = CoordCalculatorTestUtil.DISTANCE_KM_CHIN_TO_UHN * 2;
			String url = "/Location?" +
				Location.SP_NEAR + "=" + CoordCalculatorTestUtil.LATITUDE_CHIN + "|" + CoordCalculatorTestUtil.LONGITUDE_CHIN +
				"|" + bigEnoughDistance;

			Bundle actual = myClient
				.search()
				.byUrl(myServerBase + "/" + url)
				.returnBundle(Bundle.class)
				.execute();

			assertEquals(1, actual.getEntry().size());
			assertEquals(locId.getIdPart(), actual.getEntry().get(0).getResource().getIdElement().getIdPart());
		}
		{ // Outside the box
			double tooSmallDistance = CoordCalculatorTestUtil.DISTANCE_KM_CHIN_TO_UHN / 2;
			String url = "/Location?" +
				Location.SP_NEAR + "=" + CoordCalculatorTestUtil.LATITUDE_CHIN + "|" + CoordCalculatorTestUtil.LONGITUDE_CHIN +
				"|" + tooSmallDistance;

			myCaptureQueriesListener.clear();
			Bundle actual = myClient
				.search()
				.byUrl(myServerBase + "/" + url)
				.returnBundle(Bundle.class)
				.execute();
			myCaptureQueriesListener.logSelectQueries();

			assertEquals(0, actual.getEntry().size());
		}
	}

	@Test
	public void testNearSearchDistanceNoDistanceChained() {
		Location loc = new Location();
		double latitude = CoordCalculatorTestUtil.LATITUDE_CHIN;
		double longitude = CoordCalculatorTestUtil.LONGITUDE_CHIN;
		Location.LocationPositionComponent position = new Location.LocationPositionComponent().setLatitude(latitude).setLongitude(longitude);
		loc.setPosition(position);
		IIdType locId = myClient.create().resource(loc).execute().getId().toUnqualifiedVersionless();

		PractitionerRole pr = new PractitionerRole();
		pr.addLocation().setReference(locId.getValue());
		IIdType prId = myClient.create().resource(pr).execute().getId().toUnqualifiedVersionless();

		String url = "PractitionerRole?location." +
			Location.SP_NEAR + "=" + latitude + "|" + longitude;

		Bundle actual = myClient
			.search()
			.byUrl(myServerBase + "/" + url)
			.returnBundle(Bundle.class)
			.execute();

		assertEquals(1, actual.getEntry().size());
		assertEquals(prId.getIdPart(), actual.getEntry().get(0).getResource().getIdElement().getIdPart());
	}

	@Test
	public void testNearSearchApproximateChained() {
		Location loc = new Location();
		double latitude = CoordCalculatorTestUtil.LATITUDE_UHN;
		double longitude = CoordCalculatorTestUtil.LONGITUDE_UHN;
		Location.LocationPositionComponent position = new Location.LocationPositionComponent().setLatitude(latitude).setLongitude(longitude);
		loc.setPosition(position);
		myCaptureQueriesListener.clear();
		IIdType locId = myLocationDao.create(loc, mySrd).getId().toUnqualifiedVersionless();
		myCaptureQueriesListener.logInsertQueries();

		PractitionerRole pr = new PractitionerRole();
		pr.addLocation().setReference(locId.getValue());
		IIdType prId = myPractitionerRoleDao.create(pr, mySrd).getId().toUnqualifiedVersionless();
		{ // In the box
			double bigEnoughDistance = CoordCalculatorTestUtil.DISTANCE_KM_CHIN_TO_UHN * 2;
			String url = "PractitionerRole?location." +
				Location.SP_NEAR + "=" + CoordCalculatorTestUtil.LATITUDE_CHIN + "|" + CoordCalculatorTestUtil.LONGITUDE_CHIN +
				"|" + bigEnoughDistance;

			myCaptureQueriesListener.clear();
			Bundle actual = myClient
				.search()
				.byUrl(myServerBase + "/" + url)
				.returnBundle(Bundle.class)
				.execute();
			myCaptureQueriesListener.logSelectQueries();

			assertEquals(1, actual.getEntry().size());
			assertEquals(prId.getIdPart(), actual.getEntry().get(0).getResource().getIdElement().getIdPart());
		}

		{ // Outside the box
			double tooSmallDistance = CoordCalculatorTestUtil.DISTANCE_KM_CHIN_TO_UHN / 2;
			String url = "PractitionerRole?location." +
				Location.SP_NEAR + "=" + CoordCalculatorTestUtil.LATITUDE_CHIN + "|" + CoordCalculatorTestUtil.LONGITUDE_CHIN +
				"|" + tooSmallDistance;

			myCaptureQueriesListener.clear();
			Bundle actual = myClient
				.search()
				.byUrl(myServerBase + "/" + url)
				.returnBundle(Bundle.class)
				.execute();
			myCaptureQueriesListener.logSelectQueries();

			assertEquals(0, actual.getEntry().size());
		}
	}

	@Test
	public void testNearSearchDistanceNotInKm() {
		createFourCityLocations();

		String url = "Location?" +
			Location.SP_NEAR +
			"=" +
			CoordCalculatorTestUtil.LATITUDE_CHIN +
			"|" +
			CoordCalculatorTestUtil.LONGITUDE_CHIN +
			"|" +
			"300000" +
			"|" +
			"m";

		Bundle actual = myClient
			.search()
			.byUrl(myServerBase + "/" + url)
			.returnBundle(Bundle.class)
			.execute();
		List<String> ids = toUnqualifiedVersionlessIdValues(actual);
		assertThat(ids.toString(), ids, contains(
			"Location/toronto",
			"Location/belleville",
			"Location/kingston"
		));
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testSortNear(boolean theAscending) {
		createFourCityLocations();

		String url = "Location?" +
			Location.SP_NEAR +
			"=" +
			CoordCalculatorTestUtil.LATITUDE_CHIN +
			"|" +
			CoordCalculatorTestUtil.LONGITUDE_CHIN +
			"|" +
			"300" +
			"|" +
			"km" +
			"&_sort=" +
			(theAscending ? "" : "-") +
			Location.SP_NEAR;

		logAllCoordsIndexes();

		myCaptureQueriesListener.clear();
		Bundle actual = myClient
			.search()
			.byUrl(myServerBase + "/" + url)
			.returnBundle(Bundle.class)
			.execute();
		List<String> ids = toUnqualifiedVersionlessIdValues(actual);
		myCaptureQueriesListener.logSelectQueries();
		if (theAscending) {
			assertThat(ids.toString(), ids, contains(
				"Location/toronto",
				"Location/belleville",
				"Location/kingston"
			));
		} else {
			assertThat(ids.toString(), ids, contains(
				"Location/kingston",
				"Location/belleville",
				"Location/toronto"
			));
		}

	}

	/**
	 * This is kind of a contrived test where we create a second search parameter that
	 * also has the {@literal Location.position} path, so that we can make sure we don't crash with
	 * two nearness search parameters in the sort expression
	 */
	@Test
	public void testSortNearWithTwoParameters() {
		SearchParameter sp = new SearchParameter();
		sp.setCode("near2");
		sp.setName("near2");
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.setExpression(QueryStack.LOCATION_POSITION);
		sp.setType(Enumerations.SearchParamType.SPECIAL);
		sp.addBase("Location");
		mySearchParameterDao.create(sp, mySrd);
		mySearchParamRegistry.forceRefresh();

		createFourCityLocations();

		String url = "Location?" +
			"near2=" +
			CoordCalculatorTestUtil.LATITUDE_CHIN + "|" +
			CoordCalculatorTestUtil.LONGITUDE_CHIN + "|" +
			"300" + "|" + "km" +
			"&near=" +
			CoordCalculatorTestUtil.LATITUDE_CHIN + "|" +
			CoordCalculatorTestUtil.LONGITUDE_CHIN + "|" +
			"300" + "|" + "km" +
			"&_sort=near2,near";

		logAllCoordsIndexes();

		myCaptureQueriesListener.clear();
		Bundle actual = myClient
			.search()
			.byUrl(myServerBase + "/" + url)
			.returnBundle(Bundle.class)
			.execute();
		List<String> ids = toUnqualifiedVersionlessIdValues(actual);
		myCaptureQueriesListener.logSelectQueries();
		assertThat(ids.toString(), ids, contains(
			"Location/toronto",
			"Location/belleville",
			"Location/kingston"
		));
	}

	@Test
	public void testSortNearWithNoNearParameter() {
		String url = "Location?_sort=near";
		try {
			myClient
				.search()
				.byUrl(myServerBase + "/" + url)
				.returnBundle(Bundle.class)
				.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), containsString("Can not sort on coordinate parameter \"near\" unless this parameter is also specified as a search parameter"));
		}

	}

	private void createFourCityLocations() {
		createLocation("Location/toronto", CoordCalculatorTestUtil.LATITUDE_TORONTO, CoordCalculatorTestUtil.LONGITUDE_TORONTO);
		createLocation("Location/belleville", CoordCalculatorTestUtil.LATITUDE_BELLEVILLE, CoordCalculatorTestUtil.LONGITUDE_BELLEVILLE);
		createLocation("Location/kingston", CoordCalculatorTestUtil.LATITUDE_KINGSTON, CoordCalculatorTestUtil.LONGITUDE_KINGSTON);
		createLocation("Location/ottawa", CoordCalculatorTestUtil.LATITUDE_OTTAWA, CoordCalculatorTestUtil.LONGITUDE_OTTAWA);
	}

	@Test
	public void testInvalid_SortWithNoParameter() {

		String url = "Location?" +
			"_sort=" +
			Location.SP_NEAR;

		try {
			myClient
				.search()
				.byUrl(myServerBase + "/" + url)
				.returnBundle(Bundle.class)
				.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), containsString("Can not sort on coordinate parameter \"near\" unless this parameter is also specified"));
		}
	}

	@ParameterizedTest
	@CsvSource({
		"foo,      -79.4170007, 300, km,  Invalid lat/lon parameter value: foo",
		"43.65513, foo,         300, km,  Invalid lat/lon parameter value: foo",
		"43.65513, -79.4170007, foo, km,  Invalid lat/lon parameter value: foo",
		"43.65513, -79.4170007, 300, foo, The unit 'foo' is unknown"
	})
	public void testInvalid_InvalidLatitude(String theLatitude, String theLongitude, String theDistance, String theDistanceUnits, String theExpectedErrorMessageContains) {

		String url = "Location?" +
			Location.SP_NEAR +
			"=" +
			theLatitude +
			"|" +
			theLongitude +
			"|" +
			theDistance +
			"|" +
			theDistanceUnits;

		try {
			myClient
				.search()
				.byUrl(myServerBase + "/" + url)
				.returnBundle(Bundle.class)
				.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), containsString(theExpectedErrorMessageContains));
		}
	}

	private void createLocation(String id, double latitude, double longitude) {
		Location loc = new Location();
		loc.setId(id);
		loc.getPosition()
			.setLatitude(latitude)
			.setLongitude(longitude);
		myLocationDao.update(loc, mySrd);
	}


}
