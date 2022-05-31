package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.util.CoordCalculatorTest;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Location;
import org.hl7.fhir.r4.model.PractitionerRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResourceProviderR4DistanceTest extends BaseResourceProviderR4Test {

	@BeforeEach
	@Override
	public void before() throws Exception {
		super.before();
		myDaoConfig.setReuseCachedSearchResultsForMillis(null);
	}

	@Test
	public void testNearSearchApproximate() {
		Location loc = new Location();
		double latitude = CoordCalculatorTest.LATITUDE_UHN;
		double longitude = CoordCalculatorTest.LONGITUDE_UHN;
		Location.LocationPositionComponent position = new Location.LocationPositionComponent().setLatitude(latitude).setLongitude(longitude);
		loc.setPosition(position);
		IIdType locId = myClient.create().resource(loc).execute().getId().toUnqualifiedVersionless();

		{ // In the box
			double bigEnoughDistance = CoordCalculatorTest.DISTANCE_KM_CHIN_TO_UHN * 2;
			String url = "/Location?" +
				Location.SP_NEAR + "=" + CoordCalculatorTest.LATITUDE_CHIN + "|" + CoordCalculatorTest.LONGITUDE_CHIN +
				"|" + bigEnoughDistance;

			Bundle actual = myClient
				.search()
				.byUrl(ourServerBase + "/" + url)
				.encodedJson()
				.prettyPrint()
				.returnBundle(Bundle.class)
				.execute();

			assertEquals(1, actual.getEntry().size());
			assertEquals(locId.getIdPart(), actual.getEntry().get(0).getResource().getIdElement().getIdPart());
		}
		{ // Outside the box
			double tooSmallDistance = CoordCalculatorTest.DISTANCE_KM_CHIN_TO_UHN / 2;
			String url = "/Location?" +
				Location.SP_NEAR + "=" + CoordCalculatorTest.LATITUDE_CHIN + "|" + CoordCalculatorTest.LONGITUDE_CHIN +
				"|" + tooSmallDistance;

			myCaptureQueriesListener.clear();
			Bundle actual = myClient
				.search()
				.byUrl(ourServerBase + "/" + url)
				.encodedJson()
				.prettyPrint()
				.returnBundle(Bundle.class)
				.execute();
			myCaptureQueriesListener.logSelectQueries();

			assertEquals(0, actual.getEntry().size());
		}
	}

	@Test
	public void testNearSearchDistanceNoDistanceChained() {
		Location loc = new Location();
		double latitude = CoordCalculatorTest.LATITUDE_CHIN;
		double longitude = CoordCalculatorTest.LONGITUDE_CHIN;
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
			.byUrl(ourServerBase + "/" + url)
			.encodedJson()
			.prettyPrint()
			.returnBundle(Bundle.class)
			.execute();

		assertEquals(1, actual.getEntry().size());
		assertEquals(prId.getIdPart(), actual.getEntry().get(0).getResource().getIdElement().getIdPart());
	}

	@Test
	public void testNearSearchApproximateChained() {
		Location loc = new Location();
		double latitude = CoordCalculatorTest.LATITUDE_UHN;
		double longitude = CoordCalculatorTest.LONGITUDE_UHN;
		Location.LocationPositionComponent position = new Location.LocationPositionComponent().setLatitude(latitude).setLongitude(longitude);
		loc.setPosition(position);
		myCaptureQueriesListener.clear();
		IIdType locId = myLocationDao.create(loc).getId().toUnqualifiedVersionless();
		myCaptureQueriesListener.logInsertQueries();

		PractitionerRole pr = new PractitionerRole();
		pr.addLocation().setReference(locId.getValue());
		IIdType prId = myPractitionerRoleDao.create(pr).getId().toUnqualifiedVersionless();
		{ // In the box
			double bigEnoughDistance = CoordCalculatorTest.DISTANCE_KM_CHIN_TO_UHN * 2;
			String url = "PractitionerRole?location." +
				Location.SP_NEAR + "=" + CoordCalculatorTest.LATITUDE_CHIN + "|" + CoordCalculatorTest.LONGITUDE_CHIN +
				"|" + bigEnoughDistance;

			myCaptureQueriesListener.clear();
			Bundle actual = myClient
				.search()
				.byUrl(ourServerBase + "/" + url)
				.encodedJson()
				.prettyPrint()
				.returnBundle(Bundle.class)
				.execute();
			myCaptureQueriesListener.logSelectQueries();

			assertEquals(1, actual.getEntry().size());
			assertEquals(prId.getIdPart(), actual.getEntry().get(0).getResource().getIdElement().getIdPart());
		}

		{ // Outside the box
			double tooSmallDistance = CoordCalculatorTest.DISTANCE_KM_CHIN_TO_UHN / 2;
			String url = "PractitionerRole?location." +
				Location.SP_NEAR + "=" + CoordCalculatorTest.LATITUDE_CHIN + "|" + CoordCalculatorTest.LONGITUDE_CHIN +
				"|" + tooSmallDistance;

			myCaptureQueriesListener.clear();
			Bundle actual = myClient
				.search()
				.byUrl(ourServerBase + "/" + url)
				.encodedJson()
				.prettyPrint()
				.returnBundle(Bundle.class)
				.execute();
			myCaptureQueriesListener.logSelectQueries();

			assertEquals(0, actual.getEntry().size());
		}
	}
}
