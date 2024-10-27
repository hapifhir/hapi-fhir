package ca.uhn.fhir.jpa.provider.r4;

import static org.junit.jupiter.api.Assertions.assertEquals;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.search.builder.QueryStack;
import ca.uhn.fhir.jpa.util.CoordCalculatorTestUtil;
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.gclient.ICriterion;
import ca.uhn.fhir.rest.gclient.ICriterionInternal;
import ca.uhn.fhir.rest.gclient.IParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Location;
import org.hl7.fhir.r4.model.PractitionerRole;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.SearchParameter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

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

			assertThat(actual.getEntry()).hasSize(1);
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

			assertThat(actual.getEntry()).isEmpty();
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

		assertThat(actual.getEntry()).hasSize(1);
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

			assertThat(actual.getEntry()).hasSize(1);
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

			assertThat(actual.getEntry()).isEmpty();
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
		assertThat(ids).as(ids.toString()).containsExactly("Location/toronto", "Location/belleville", "Location/kingston");
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
			assertThat(ids).as(ids.toString()).containsExactly("Location/toronto", "Location/belleville", "Location/kingston");
		} else {
			assertThat(ids).as(ids.toString()).containsExactly("Location/kingston", "Location/belleville", "Location/toronto");
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
		assertThat(ids).as(ids.toString()).containsExactly("Location/toronto", "Location/belleville", "Location/kingston");
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
			assertThat(e.getMessage()).contains("Can not sort on coordinate parameter \"near\" unless this parameter is also specified as a search parameter");
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
			assertThat(e.getMessage()).contains("Can not sort on coordinate parameter \"near\" unless this parameter is also specified");
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
			assertThat(e.getMessage()).contains(theExpectedErrorMessageContains);
		}
	}


	@Test
	void shouldSortPractitionerRolesByLocationNear() {

		double latitude = 43.65513;
		double longitude = -79.4173869;
		double distance = 100.0; // km

		Location toronto = createLocationWithName("Toronto", 43.70, -79.42);
		Location mississauga = createLocationWithName("Mississauga", 43.59, -79.64);
		Location hamilton = createLocationWithName("Hamilton", 43.26, -79.87);
		Location kitchener = createLocationWithName("Kitchener", 43.45, -80.49);
		Location stCatharines = createLocationWithName("St. Catharines", 43.16, -79.24);
		Location oshawa = createLocationWithName("Oshawa", 43.92, -78.86);
		Location ottawa = createLocationWithName("Ottawa", 45.42, -75.69);
		Location london = createLocationWithName("London", 42.98, -81.25);
		Location barrie = createLocationWithName("Barrie", 44.39, -79.69);
		Location windsor = createLocationWithName("Windsor", 42.31, -83.04);

		createPractitionerRole(toronto);
		createPractitionerRole(ottawa);
		createPractitionerRole(mississauga);
		createPractitionerRole(hamilton);
		createPractitionerRole(kitchener);
		createPractitionerRole(london);
		createPractitionerRole(stCatharines);
		createPractitionerRole(oshawa);
		createPractitionerRole(windsor);
		createPractitionerRole(barrie);

		Bundle sortedPractitionerRoles = (Bundle) myClient.search()
			.forResource(PractitionerRole.class)
			.where(new RawSearchCriterion("location.near", latitude + "|" + longitude + "|" + distance + "|km"))
			.sort(new SortSpec("location.near", SortOrderEnum.ASC))
			.execute();

		List<String> list = sortedPractitionerRoles.getEntry()
			.stream()
			.map(entry -> (PractitionerRole) entry.getResource())
			.flatMap(practitionerRole -> practitionerRole.getIdentifier().stream())
			.filter(identifier -> PRACTITIONER_ROLE_SYSTEM.equals(identifier.getSystem()))
			.map(Identifier::getValue).toList();

		List<String> referenceList = Arrays.asList(
			"PractitionerRole-Toronto",
			"PractitionerRole-Mississauga",
			"PractitionerRole-St. Catharines",
			"PractitionerRole-Oshawa",
			"PractitionerRole-Hamilton",
			"PractitionerRole-Barrie",
			"PractitionerRole-Kitchener");

		assertThat(list.toArray())
			.containsExactly(referenceList.toArray());

		Bundle sortedPractitionerRolesDesc = (Bundle) myClient.search()
			.forResource(PractitionerRole.class)
			.where(new RawSearchCriterion("location.near", latitude + "|" + longitude + "|" + distance + "|km"))
			.sort(new SortSpec("location.near", SortOrderEnum.DESC))
			.execute();

		list = sortedPractitionerRolesDesc.getEntry()
			.stream()
			.map(entry -> (PractitionerRole) entry.getResource())
			.flatMap(practitionerRole -> practitionerRole.getIdentifier().stream())
			.filter(identifier -> PRACTITIONER_ROLE_SYSTEM.equals(identifier.getSystem()))
			.map(Identifier::getValue).toList();

		Collections.reverse(referenceList);
		assertThat(list.toArray())
			.containsExactly(referenceList.toArray());

	}

	@Test
	void shouldSortPractitionerRoleByLocationNameAndThenByLocationNearInSortChain() {

		double latitude = 56.15392798473292;
		double longitude = 10.214247324883443;
		double distance = 1000.0; // km


		Location city1 = createLocationWithName("city", 56.4572068307235, 10.03257493847164); // randers
		Location city2 = createLocationWithName("city", 55.37805615936569, 10.373173394141986); // odense
		Location city3 = createLocationWithName("city", 57.03839389334237, 9.897971178848938); // aalborg
		Location city4 = createLocationWithName("city", 53.59504499156986, 9.94180650612504); // hamburg
		Location capital1 = createLocationWithName("capital", 55.67252420131149, 12.521336649310285); // copenhagen
		Location capital2 = createLocationWithName("capital", 59.91879265293977, 10.743073107764332); // oslo
		Location capital3 = createLocationWithName("capital", 51.53542091927589, -0.1535161240530497); // london

		createPractitionerRole(city1, "city1");
		createPractitionerRole(city2, "city2");
		createPractitionerRole(city3, "city3");
		createPractitionerRole(city4, "city4");
		createPractitionerRole(capital1, "capital1");
		createPractitionerRole(capital2, "capital2");
		createPractitionerRole(capital3, "capital3");

		Bundle sortedPractitionerRoles = (Bundle) myClient.search()
			.forResource(PractitionerRole.class)
			.where(new RawSearchCriterion("location.near", latitude + "|" + longitude + "|" + distance + "|km"))
			.sort(new SortSpec("location.name", SortOrderEnum.ASC, new SortSpec("location.near", SortOrderEnum.ASC)))
			.execute();

		List<String> sortedValues = sortedPractitionerRoles
			.getEntry()
			.stream()
			.map(entry -> ((PractitionerRole) entry.getResource()).getIdentifier()
				.stream()
				.filter(identifier -> PRACTITIONER_ROLE_SYSTEM.equals(identifier.getSystem()))
				.map(Identifier::getValue)
				.collect(Collectors.toList()))
			.flatMap(List::stream)
			.toList();

		List<String> referenceList = Arrays.asList(
			"PractitionerRole-capital1",
			"PractitionerRole-capital2",
			"PractitionerRole-capital3",
			"PractitionerRole-city1",
			"PractitionerRole-city2",
			"PractitionerRole-city3",
			"PractitionerRole-city4");

		assertThat(sortedValues.toArray())
			.containsExactly(referenceList.toArray());

		Bundle sortedPractitionerRolesDesc = (Bundle) myClient.search()
			.forResource(PractitionerRole.class)
			.where(new RawSearchCriterion("location.near", latitude + "|" + longitude + "|" + distance + "|km"))
			.sort(new SortSpec("location.name", SortOrderEnum.ASC, new SortSpec("location.near", SortOrderEnum.DESC)))
			.execute();

		List<String> sortedValuesDesc = sortedPractitionerRolesDesc
			.getEntry()
			.stream()
			.map(entry -> ((PractitionerRole) entry.getResource()).getIdentifier()
				.stream()
				.filter(identifier -> PRACTITIONER_ROLE_SYSTEM.equals(identifier.getSystem()))
				.map(Identifier::getValue)
				.collect(Collectors.toList()))
			.flatMap(List::stream)
			.toList();

		referenceList = Arrays.asList(
			"PractitionerRole-capital3",
			"PractitionerRole-capital2",
			"PractitionerRole-capital1",
			"PractitionerRole-city4",
			"PractitionerRole-city3",
			"PractitionerRole-city2",
			"PractitionerRole-city1"
		);
		assertThat(sortedValuesDesc.toArray())
			.containsExactly(referenceList.toArray());
	}

	@Test
	void shouldSortPractitionerRoleByLocationNearAndThenByLocationNameInSortChain() {

		double latitude = 56.15392798473292;
		double longitude = 10.214247324883443;
		double distance = 1000.0; // km

		createPractitionerRole(createLocationWithName("a-close-city", 56.4572068307235, 10.03257493847164));
		createPractitionerRole(createLocationWithName("b-close-city", 56.4572068307235, 10.03257493847164));
		createPractitionerRole(createLocationWithName("c-close-city", 56.4572068307235, 10.03257493847164));
		createPractitionerRole(createLocationWithName("x-far-city", 51.53542091927589, -0.1535161240530497));
		createPractitionerRole(createLocationWithName("y-far-city", 51.53542091927589, -0.1535161240530497));
		createPractitionerRole(createLocationWithName("z-far-city", 51.53542091927589, -0.1535161240530497));

		Bundle sortedPractitionerRoles = (Bundle) myClient.search()
			.forResource(PractitionerRole.class)
			.where(new RawSearchCriterion("location.near", latitude + "|" + longitude + "|" + distance + "|km"))
			.sort(new SortSpec("location.near", SortOrderEnum.ASC, new SortSpec("location.name")))
			.execute();

		List<String> sortedValues = sortedPractitionerRoles
			.getEntry()
			.stream()
			.map(entry -> ((PractitionerRole) entry.getResource()).getIdentifier()
				.stream()
				.filter(identifier -> PRACTITIONER_ROLE_SYSTEM.equals(identifier.getSystem()))
				.map(Identifier::getValue)
				.collect(Collectors.toList()))
			.flatMap(List::stream)
			.toList();

		assertArrayEquals(
			Arrays.asList(
				"PractitionerRole-a-close-city",
				"PractitionerRole-b-close-city",
				"PractitionerRole-c-close-city",
				"PractitionerRole-x-far-city",
				"PractitionerRole-y-far-city",
				"PractitionerRole-z-far-city"
			).toArray(), sortedValues.toArray());

		Bundle sortedPractitionerRolesDesc = (Bundle) myClient.search()
			.forResource(PractitionerRole.class)
			.where(new RawSearchCriterion("location.near", latitude + "|" + longitude + "|" + distance + "|km"))
			.sort(new SortSpec("location.near", SortOrderEnum.DESC, new SortSpec("location.name")))
			.execute();

		List<String> sortedValuesDesc = sortedPractitionerRolesDesc
			.getEntry()
			.stream()
			.map(entry -> ((PractitionerRole) entry.getResource()).getIdentifier()
				.stream()
				.filter(identifier -> PRACTITIONER_ROLE_SYSTEM.equals(identifier.getSystem()))
				.map(Identifier::getValue)
				.collect(Collectors.toList()))
			.flatMap(List::stream)
			.toList();


		assertArrayEquals(
			Arrays.asList(
				"PractitionerRole-x-far-city",
				"PractitionerRole-y-far-city",
				"PractitionerRole-z-far-city",
				"PractitionerRole-a-close-city",
				"PractitionerRole-b-close-city",
				"PractitionerRole-c-close-city"
			).toArray(), sortedValuesDesc.toArray());
	}


	@Test
	void shouldThrowExceptionWhenSortingByChainedNearWithoutProvidingNearValue() {
		assertThatThrownBy(() ->
			myClient.search()
				.forResource(PractitionerRole.class)
				.sort(new SortSpec("location.near", SortOrderEnum.ASC))
				.execute())
			.isInstanceOf(InvalidRequestException.class)
			.hasMessage("HTTP 400 Bad Request: HAPI-2497: Can not sort on coordinate parameter \"location\" unless this parameter is also specified as a search parameter with a latitude/longitude value");
	}

	private void createLocation(String id, double latitude, double longitude) {
		Location loc = new Location();
		loc.setId(id);
		loc.getPosition()
			.setLatitude(latitude)
			.setLongitude(longitude);
		myLocationDao.update(loc, mySrd);
	}

	public static final String PRACTITIONER_ROLE_SYSTEM = "http://api.someSystem.com/PractitionerRole";


	private Location createLocationWithName(String locationName, double latitude, double longitude) {
		Location location = new Location();
		location.setStatus(Location.LocationStatus.ACTIVE);
		location.setName(locationName);
		location.addIdentifier()
			.setSystem("http://api.someSystem.com/Location")
			.setValue("TestLocation-" + locationName + "-" + UUID.randomUUID());
		location.getPosition().setLatitude(new BigDecimal(latitude));
		location.getPosition().setLongitude(new BigDecimal(longitude));

		return doCreateResourceAndReturnInstance(location);
	}

	private void createPractitionerRole(Location location, String city) {
		PractitionerRole practitionerRole = new PractitionerRole();
		practitionerRole.setActive(true);
		practitionerRole.addLocation(new Reference(location));
		practitionerRole.addIdentifier()
			.setSystem(PRACTITIONER_ROLE_SYSTEM)
			.setValue("PractitionerRole-" + city);

		doCreateResourceAndReturnInstance(practitionerRole);
	}

	private void createPractitionerRole(Location location) {
		PractitionerRole practitionerRole = new PractitionerRole();
		practitionerRole.setActive(true);
		practitionerRole.addLocation(new Reference(location));
		practitionerRole.addIdentifier()
			.setSystem(PRACTITIONER_ROLE_SYSTEM)
			.setValue("PractitionerRole-" + location.getName());

		doCreateResourceAndReturnInstance(practitionerRole);
	}

	public <T extends IBaseResource> T doCreateResourceAndReturnInstance(IBaseResource theResource) {
		IFhirResourceDao dao = myDaoRegistry.getResourceDao(theResource.getClass());
		return (T) dao.create(theResource, mySrd).getResource();
	}


	static class RawSearchCriterion implements ICriterion<IParam>, ICriterionInternal {
		private final String name;
		private final String value;

		RawSearchCriterion(String name, String value) {
			this.name = name;
			this.value = value;
		}

		@Override
		public String getParameterValue(FhirContext theContext) {
			return value;
		}

		@Override
		public String getParameterName() {
			return name;
		}
	}

}
