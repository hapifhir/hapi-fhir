package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.util.CoordCalculatorTestUtil;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Location;
import org.hl7.fhir.r4.model.OrganizationAffiliation;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

public class FhirResourceDaoR4SearchDistanceTest extends BaseJpaR4Test {
	@BeforeEach
	public void beforeDisableResultReuse() {
		myStorageSettings.setReuseCachedSearchResultsForMillis(null);
	}

	@Autowired
	MatchUrlService myMatchUrlService;

	@Test
	public void testNearSearchDistanceNoDistance() {
		Location loc = new Location();
		double latitude = CoordCalculatorTestUtil.LATITUDE_CHIN;
		double longitude = CoordCalculatorTestUtil.LATITUDE_CHIN;
		Location.LocationPositionComponent position = new Location.LocationPositionComponent().setLatitude(latitude).setLongitude(longitude);
		loc.setPosition(position);
		String locId = myLocationDao.create(loc).getId().toUnqualifiedVersionless().getValue();

		SearchParameterMap map = myMatchUrlService.translateMatchUrl(
			"Location?" +
				Location.SP_NEAR + "=" + latitude + "|" + longitude,
			myFhirContext.getResourceDefinition("Location"));

		List<String> ids = toUnqualifiedVersionlessIdValues(myLocationDao.search(map));
		assertThat(ids, contains(locId));
	}

	@Test
	public void testNearSearchDistanceChained() {
		Location location = new Location();
		double latitude = CoordCalculatorTestUtil.LATITUDE_CHIN;
		double longitude = CoordCalculatorTestUtil.LATITUDE_CHIN;
		Location.LocationPositionComponent position = new Location.LocationPositionComponent().setLatitude(latitude).setLongitude(longitude);
		location.setPosition(position);

		IIdType id = myLocationDao.create(location).getId();

		OrganizationAffiliation aff = new OrganizationAffiliation();
		aff.addLocation(new Reference(id.getIdPart()));
		String affId = myOrganizationAffiliationDao.create(aff).getId().getValueAsString();
		SearchParameterMap map = myMatchUrlService.translateMatchUrl("OrganizationAffiliation?location." + Location.SP_NEAR + "=" + latitude + "|" + longitude, myFhirContext.getResourceDefinition("Location"));

		List<String> ids = toUnqualifiedVersionlessIdValues(myOrganizationAffiliationDao.search(map));
		assertThat(ids, contains(affId));

	}

	@Test
	public void testNearSearchDistanceZero() {
		Location loc = new Location();
		double latitude = CoordCalculatorTestUtil.LATITUDE_CHIN;
		double longitude = CoordCalculatorTestUtil.LATITUDE_CHIN;
		Location.LocationPositionComponent position = new Location.LocationPositionComponent().setLatitude(latitude).setLongitude(longitude);
		loc.setPosition(position);
		String locId = myLocationDao.create(loc).getId().toUnqualifiedVersionless().getValue();
		{
			SearchParameterMap map = myMatchUrlService.translateMatchUrl(
				"Location?" +
					Location.SP_NEAR + "=" + latitude + "|" + longitude + "|0",
				myFhirContext.getResourceDefinition("Location"));

			List<String> ids = toUnqualifiedVersionlessIdValues(myLocationDao.search(map));
			assertThat(ids, contains(locId));
		}
		{
			SearchParameterMap map = myMatchUrlService.translateMatchUrl(
				"Location?" +
					Location.SP_NEAR + "=" + latitude + "|" + longitude + "|0.0",
				myFhirContext.getResourceDefinition("Location"));

			List<String> ids = toUnqualifiedVersionlessIdValues(myLocationDao.search(map));
			assertThat(ids, contains(locId));
		}
	}

	@Test
	public void testNearSearchApproximate() {
		Location loc = new Location();
		double latitude = CoordCalculatorTestUtil.LATITUDE_UHN;
		double longitude = CoordCalculatorTestUtil.LONGITUDE_UHN;
		Location.LocationPositionComponent position = new Location.LocationPositionComponent().setLatitude(latitude).setLongitude(longitude);
		loc.setPosition(position);
		String locId = myLocationDao.create(loc).getId().toUnqualifiedVersionless().getValue();

		{ // In the box
			double bigEnoughDistance = CoordCalculatorTestUtil.DISTANCE_KM_CHIN_TO_UHN * 2;
			SearchParameterMap map = myMatchUrlService.translateMatchUrl(
				"Location?" +
					Location.SP_NEAR + "=" + CoordCalculatorTestUtil.LATITUDE_CHIN + "|"
					+ CoordCalculatorTestUtil.LONGITUDE_CHIN + "|" +
					bigEnoughDistance, myFhirContext.getResourceDefinition("Location"));

			List<String> ids = toUnqualifiedVersionlessIdValues(myLocationDao.search(map));
			assertThat(ids, contains(locId));
		}
		{ // Outside the box
			double tooSmallDistance = CoordCalculatorTestUtil.DISTANCE_KM_CHIN_TO_UHN / 2;

			SearchParameterMap map = myMatchUrlService.translateMatchUrl(
				"Location?" +
					Location.SP_NEAR + "=" + CoordCalculatorTestUtil.LATITUDE_CHIN + "|"
					+ CoordCalculatorTestUtil.LONGITUDE_CHIN + "|" +
					tooSmallDistance, myFhirContext.getResourceDefinition("Location"));

			List<String> ids = toUnqualifiedVersionlessIdValues(myLocationDao.search(map));
			assertThat(ids.size(), is(0));
		}

	}

	@Test
	public void testNearSearchApproximateNearAntiMeridian() {
		Location loc = new Location();
		double latitude = CoordCalculatorTestUtil.LATITUDE_TAVEUNI;
		double longitude = CoordCalculatorTestUtil.LONGITIDE_TAVEUNI;
		Location.LocationPositionComponent position = new Location.LocationPositionComponent().setLatitude(latitude).setLongitude(longitude);
		loc.setPosition(position);
		String locId = myLocationDao.create(loc).getId().toUnqualifiedVersionless().getValue();

		{ // We match even when the box crosses the anti-meridian
			double bigEnoughDistance = CoordCalculatorTestUtil.DISTANCE_TAVEUNI;
			SearchParameterMap map = myMatchUrlService.translateMatchUrl(
				"Location?" +
					Location.SP_NEAR + "=" + CoordCalculatorTestUtil.LATITUDE_TAVEUNI + "|"
					+ CoordCalculatorTestUtil.LONGITIDE_TAVEUNI + "|" +
					bigEnoughDistance, myFhirContext.getResourceDefinition("Location"));

			List<String> ids = toUnqualifiedVersionlessIdValues(myLocationDao.search(map));
			assertThat(ids, contains(locId));
		}
		{ // We don't match outside a box that crosses the anti-meridian
			double tooSmallDistance = CoordCalculatorTestUtil.DISTANCE_TAVEUNI;
			SearchParameterMap map = myMatchUrlService.translateMatchUrl(
				"Location?" +
					Location.SP_NEAR + "=" + CoordCalculatorTestUtil.LATITUDE_CHIN + "|"
					+ CoordCalculatorTestUtil.LONGITUDE_CHIN + "|" +
					tooSmallDistance, myFhirContext.getResourceDefinition("Location"));

			List<String> ids = toUnqualifiedVersionlessIdValues(myLocationDao.search(map));
			assertThat(ids.size(), is(0));
		}
	}

}
