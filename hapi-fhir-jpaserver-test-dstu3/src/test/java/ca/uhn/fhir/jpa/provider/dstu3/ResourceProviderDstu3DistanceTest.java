package ca.uhn.fhir.jpa.provider.dstu3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import ca.uhn.fhir.jpa.util.CoordCalculatorTestUtil;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Location;
import org.hl7.fhir.dstu3.model.PractitionerRole;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URLEncoder;

import static org.assertj.core.api.Assertions.assertThat;

public class ResourceProviderDstu3DistanceTest extends BaseResourceProviderDstu3Test {

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
				Location.SP_NEAR + "=" + CoordCalculatorTestUtil.LATITUDE_CHIN + URLEncoder.encode(":") + CoordCalculatorTestUtil.LONGITUDE_CHIN +
				"&" +
				Location.SP_NEAR_DISTANCE + "=" + bigEnoughDistance + URLEncoder.encode("|http://unitsofmeasure.org|km");

			Bundle actual = myClient
				.search()
				.byUrl(myServerBase + "/" + url)
				.encodedJson()
				.prettyPrint()
				.returnBundle(Bundle.class)
				.execute();

			assertThat(actual.getEntry()).hasSize(1);
			assertEquals(locId.getIdPart(), actual.getEntry().get(0).getResource().getIdElement().getIdPart());
		}
		{ // Outside the box
			double tooSmallDistance = CoordCalculatorTestUtil.DISTANCE_KM_CHIN_TO_UHN / 2;
			String url = "/Location?" +
				Location.SP_NEAR + "=" + CoordCalculatorTestUtil.LATITUDE_CHIN + URLEncoder.encode(":") + CoordCalculatorTestUtil.LONGITUDE_CHIN +
				"&" +
				Location.SP_NEAR_DISTANCE + "=" + tooSmallDistance + URLEncoder.encode("|http://unitsofmeasure.org|km");

			myCaptureQueriesListener.clear();
			Bundle actual = myClient
				.search()
				.byUrl(myServerBase + "/" + url)
				.encodedJson()
				.prettyPrint()
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
			Location.SP_NEAR + "=" + latitude + URLEncoder.encode(":") + longitude;

		Bundle actual = myClient
			.search()
			.byUrl(myServerBase + "/" + url)
			.encodedJson()
			.prettyPrint()
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
		IIdType locId = myLocationDao.create(loc).getId().toUnqualifiedVersionless();
		myCaptureQueriesListener.logInsertQueries();

		PractitionerRole pr = new PractitionerRole();
		pr.addLocation().setReference(locId.getValue());
		IIdType prId = myPractitionerRoleDao.create(pr).getId().toUnqualifiedVersionless();
		{ // In the box
			double bigEnoughDistance = CoordCalculatorTestUtil.DISTANCE_KM_CHIN_TO_UHN * 2;
			String url = "PractitionerRole?location." +
				Location.SP_NEAR + "=" + CoordCalculatorTestUtil.LATITUDE_CHIN + URLEncoder.encode(":") + CoordCalculatorTestUtil.LONGITUDE_CHIN +
				"&" +
				"location." + Location.SP_NEAR_DISTANCE + "=" + bigEnoughDistance + URLEncoder.encode("|http://unitsofmeasure.org|km");

			myCaptureQueriesListener.clear();
			Bundle actual = myClient
				.search()
				.byUrl(myServerBase + "/" + url)
				.encodedJson()
				.prettyPrint()
				.returnBundle(Bundle.class)
				.execute();
			myCaptureQueriesListener.logSelectQueries();

			assertThat(actual.getEntry()).hasSize(1);
			assertEquals(prId.getIdPart(), actual.getEntry().get(0).getResource().getIdElement().getIdPart());
		}

		{ // Outside the box
			double tooSmallDistance = CoordCalculatorTestUtil.DISTANCE_KM_CHIN_TO_UHN / 2;
			String url = "PractitionerRole?location." +
				Location.SP_NEAR + "=" + CoordCalculatorTestUtil.LATITUDE_CHIN + URLEncoder.encode(":") + CoordCalculatorTestUtil.LONGITUDE_CHIN +
				"&" +
				"location." + Location.SP_NEAR_DISTANCE + "=" + tooSmallDistance + URLEncoder.encode("|http://unitsofmeasure.org|km");

			myCaptureQueriesListener.clear();
			Bundle actual = myClient
				.search()
				.byUrl(myServerBase + "/" + url)
				.encodedJson()
				.prettyPrint()
				.returnBundle(Bundle.class)
				.execute();
			myCaptureQueriesListener.logSelectQueries();

			assertThat(actual.getEntry()).isEmpty();
		}
	}
}
