package ca.uhn.fhir.jpa.provider.r4;


import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.gclient.ICriterion;
import ca.uhn.fhir.rest.gclient.ICriterionInternal;
import ca.uhn.fhir.rest.gclient.IParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Location;
import org.hl7.fhir.r4.model.PractitionerRole;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class UpliftedChainedNearSearchTest extends BaseResourceProviderR4Test {


	public static final String PRACTITIONER_ROLE_SYSTEM = "http://api.someSystem.com/PractitionerRole";


	@Test
	void shouldSortPractitionerRolesByLocationNear() {

		double latitude = 43.65513;
		double longitude = -79.4173869;
		double distance = 100.0; // km

		Location toronto = createLocation("Toronto", 43.70, -79.42);
		Location mississauga = createLocation("Mississauga", 43.59, -79.64);
		Location hamilton = createLocation("Hamilton", 43.26, -79.87);
		Location kitchener = createLocation("Kitchener", 43.45, -80.49);
		Location stCatharines = createLocation("St. Catharines", 43.16, -79.24);
		Location oshawa = createLocation("Oshawa", 43.92, -78.86);
		Location ottawa = createLocation("Ottawa", 45.42, -75.69);
		Location london = createLocation("London", 42.98, -81.25);
		Location barrie = createLocation("Barrie", 44.39, -79.69);
		Location windsor = createLocation("Windsor", 42.31, -83.04);

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

		assertArrayEquals(referenceList.toArray(), list.toArray());

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
		assertArrayEquals(referenceList.toArray(), list.toArray());

	}

	@Test
	void shouldSortPractitionerRoleByLocationNameAndThenByLocationNearInSortChain() {

		double latitude = 56.15392798473292;
		double longitude = 10.214247324883443;
		double distance = 1000.0; // km


		Location city1 = createLocation("city", 56.4572068307235, 10.03257493847164); // randers
		Location city2 = createLocation("city", 55.37805615936569, 10.373173394141986); // odense
		Location city3 = createLocation("city", 57.03839389334237, 9.897971178848938); // aalborg
		Location city4 = createLocation("city", 53.59504499156986, 9.94180650612504); // hamburg
		Location capital1 = createLocation("capital", 55.67252420131149, 12.521336649310285); // copenhagen
		Location capital2 = createLocation("capital", 59.91879265293977, 10.743073107764332); // oslo
		Location capital3 = createLocation("capital", 51.53542091927589, -0.1535161240530497); // london

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

		assertArrayEquals(referenceList.toArray(), sortedValues.toArray());

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
		assertArrayEquals(referenceList.toArray(), sortedValuesDesc.toArray());
	}

	@Test
	void shouldSortPractitionerRoleByLocationNearAndThenByLocationNameInSortChain() {

		double latitude = 56.15392798473292;
		double longitude = 10.214247324883443;
		double distance = 1000.0; // km

		createPractitionerRole(createLocation("a-close-city", 56.4572068307235, 10.03257493847164));
		createPractitionerRole(createLocation("b-close-city", 56.4572068307235, 10.03257493847164));
		createPractitionerRole(createLocation("c-close-city", 56.4572068307235, 10.03257493847164));
		createPractitionerRole(createLocation("x-far-city", 51.53542091927589, -0.1535161240530497));
		createPractitionerRole(createLocation("y-far-city", 51.53542091927589, -0.1535161240530497));
		createPractitionerRole(createLocation("z-far-city", 51.53542091927589, -0.1535161240530497));

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
		assertThrows(InvalidRequestException.class, () -> {
			myClient.search()
				.forResource(PractitionerRole.class)
				.sort(new SortSpec("location.near", SortOrderEnum.ASC))
				.execute();
		}, "HTTP 400 : HAPI-2307: Can not sort on coordinate parameter \"location\" unless this parameter is also specified as a search parameter with a latitude/longitude value");
	}


	private Location createLocation(String locationName, double latitude, double longitude) {
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
