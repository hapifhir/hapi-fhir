package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.rest.param.UriParamQualifierEnum;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.MethodNotAllowedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.apache.commons.text.RandomStringGenerator;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static ca.uhn.fhir.rest.api.Constants.PARAM_SOURCE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

@SuppressWarnings({"Duplicates"})
public class FhirResourceDaoR4SourceTest extends BaseJpaR4Test {

	@AfterEach
	public final void after() {
		when(mySrd.getRequestId()).thenReturn(null);
		myStorageSettings.setStoreMetaSourceInformation(new JpaStorageSettings().getStoreMetaSourceInformation());
	}

	@BeforeEach
	public void before() {
		myStorageSettings.setStoreMetaSourceInformation(JpaStorageSettings.StoreMetaSourceInformationEnum.SOURCE_URI_AND_REQUEST_ID);
	}

	@Test
	public void testSourceStoreAndSearch() {
		String requestId = "a_request_id";

		when(mySrd.getRequestId()).thenReturn(requestId);
		Patient pt0 = new Patient();
		pt0.getMeta().setSource("urn:source:0");
		pt0.setActive(true);
		IIdType pt0id = myPatientDao.create(pt0, mySrd).getId().toUnqualifiedVersionless();

		Patient pt1 = new Patient();
		pt1.getMeta().setSource("urn:source:1");
		pt1.setActive(true);
		IIdType pt1id = myPatientDao.create(pt1, mySrd).getId().toUnqualifiedVersionless();

		// Search by source URI
		SearchParameterMap params = new SearchParameterMap();
		params.setLoadSynchronous(true);
		params.add(Constants.PARAM_SOURCE, new TokenParam("urn:source:0"));
		IBundleProvider result = myPatientDao.search(params);
		assertThat(toUnqualifiedVersionlessIdValues(result), containsInAnyOrder(pt0id.getValue()));
		pt0 = (Patient) result.getResources(0, 1).get(0);
		assertEquals("urn:source:0#a_request_id", pt0.getMeta().getSource());

		// Search by request ID
		params = new SearchParameterMap();
		params.setLoadSynchronous(true);
		params.add(Constants.PARAM_SOURCE, new TokenParam("#a_request_id"));
		result = myPatientDao.search(params);
		assertThat(toUnqualifiedVersionlessIdValues(result), containsInAnyOrder(pt0id.getValue(), pt1id.getValue()));

		// Search by source URI and request ID
		params = new SearchParameterMap();
		params.setLoadSynchronous(true);
		params.add(Constants.PARAM_SOURCE, new TokenParam("urn:source:0#a_request_id"));
		result = myPatientDao.search(params);
		assertThat(toUnqualifiedVersionlessIdValues(result), containsInAnyOrder(pt0id.getValue()));

	}

	@Test
	public void testSearchSource_whenSameSourceForMultipleResourceTypes_willMatchSearchResourceTypeOnly(){
		String sourceUrn = "urn:source:0";
		String requestId = "a_request_id";

		when(mySrd.getRequestId()).thenReturn(requestId);
		Patient patient = new Patient();
		patient.getMeta().setSource(sourceUrn);
		patient.setActive(true);
		IIdType ptId = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();

		Observation observation = new Observation();
		observation.setStatus(Observation.ObservationStatus.FINAL);
		observation.getMeta().setSource(sourceUrn);
		myObservationDao.create(observation, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap params = new SearchParameterMap();
		params.setLoadSynchronous(true);
		params.add(Constants.PARAM_SOURCE, new TokenParam("urn:source:0"));
		IBundleProvider result = myPatientDao.search(params);

		assertThat(toUnqualifiedVersionlessIdValues(result), containsInAnyOrder(ptId.getValue()));

	}

	@Test
	public void testSearchWithOr() {
		String requestId = "a_request_id";

		when(mySrd.getRequestId()).thenReturn(requestId);
		Patient pt0 = new Patient();
		pt0.getMeta().setSource("urn:source:0");
		pt0.setActive(true);
		IIdType pt0id = myPatientDao.create(pt0, mySrd).getId().toUnqualifiedVersionless();

		Patient pt1 = new Patient();
		pt1.getMeta().setSource("urn:source:1");
		pt1.setActive(true);
		IIdType pt1id = myPatientDao.create(pt1, mySrd).getId().toUnqualifiedVersionless();

		Patient pt2 = new Patient();
		pt2.getMeta().setSource("urn:source:2");
		pt2.setActive(true);
		myPatientDao.create(pt2, mySrd).getId().toUnqualifiedVersionless();

		// Search
		SearchParameterMap params = new SearchParameterMap();
		params.setLoadSynchronous(true);
		params.add(Constants.PARAM_SOURCE, new TokenOrListParam()
			.addOr(new TokenParam("urn:source:0"))
			.addOr(new TokenParam("urn:source:1")));
		IBundleProvider result = myPatientDao.search(params);
		assertThat(toUnqualifiedVersionlessIdValues(result), containsInAnyOrder(pt0id.getValue(), pt1id.getValue()));

	}

	@Test
	public void testSearchWithAnd() {
		String requestId = "a_request_id";

		when(mySrd.getRequestId()).thenReturn(requestId);
		Patient pt0 = new Patient();
		pt0.getMeta().setSource("urn:source:0");
		pt0.setActive(true);
		IIdType pt0id = myPatientDao.create(pt0, mySrd).getId().toUnqualifiedVersionless();

		Patient pt1 = new Patient();
		pt1.getMeta().setSource("urn:source:1");
		pt1.setActive(true);
		IIdType pt1id = myPatientDao.create(pt1, mySrd).getId().toUnqualifiedVersionless();

		Patient pt2 = new Patient();
		pt2.getMeta().setSource("urn:source:2");
		pt2.setActive(true);
		myPatientDao.create(pt2, mySrd).getId().toUnqualifiedVersionless();

		// Search
		SearchParameterMap params = new SearchParameterMap();
		params.setLoadSynchronous(true);
		params.add(Constants.PARAM_SOURCE, new TokenAndListParam()
			.addAnd(new TokenParam("urn:source:0"), new TokenParam("@a_request_id")));
		IBundleProvider result = myPatientDao.search(params);
		assertThat(toUnqualifiedVersionlessIdValues(result), containsInAnyOrder(pt0id.getValue()));

	}

	@Test
	public void testSearchLongRequestId() {
		String requestId = new RandomStringGenerator.Builder().build().generate(5000);
		when(mySrd.getRequestId()).thenReturn(requestId);

		Patient pt0 = new Patient();
		pt0.getMeta().setSource("urn:source:0");
		pt0.setActive(true);
		IIdType pt0id = myPatientDao.create(pt0, mySrd).getId().toUnqualifiedVersionless();

		// Search
		SearchParameterMap params = new SearchParameterMap();
		params.setLoadSynchronous(true);
		params.add(Constants.PARAM_SOURCE, new TokenAndListParam()
			.addAnd(new TokenParam("urn:source:0"), new TokenParam("#" + requestId)));
		IBundleProvider result = myPatientDao.search(params);
		assertThat(toUnqualifiedVersionlessIdValues(result), containsInAnyOrder(pt0id.getValue()));

	}

	@Test
	public void testSourceNotPreservedAcrossUpdate() {

		Patient pt0 = new Patient();
		pt0.getMeta().setSource("urn:source:0");
		pt0.setActive(true);
		IIdType pt0id = myPatientDao.create(pt0, mySrd).getId().toUnqualifiedVersionless();

		pt0 = myPatientDao.read(pt0id);
		assertEquals("urn:source:0", pt0.getMeta().getSource());

		pt0.getMeta().setSource(null);
		pt0.setActive(false);
		myPatientDao.update(pt0);

		pt0 = myPatientDao.read(pt0id.withVersion("2"));
		assertEquals(null, pt0.getMeta().getSource());

	}

	@Test
	public void testSourceDisabled() {
		myStorageSettings.setStoreMetaSourceInformation(JpaStorageSettings.StoreMetaSourceInformationEnum.NONE);
		when(mySrd.getRequestId()).thenReturn("0000000000000000");

		Patient pt0 = new Patient();
		pt0.getMeta().setSource("urn:source:0");
		pt0.setActive(true);
		IIdType pt0id = myPatientDao.create(pt0, mySrd).getId().toUnqualifiedVersionless();

		pt0 = myPatientDao.read(pt0id);
		assertEquals(null, pt0.getMeta().getSource());

		pt0.getMeta().setSource("urn:source:1");
		pt0.setActive(false);
		myPatientDao.update(pt0);

		pt0 = myPatientDao.read(pt0id.withVersion("2"));
		assertEquals(null, pt0.getMeta().getSource());

		// Search without source param
		SearchParameterMap params = new SearchParameterMap();
		params.setLoadSynchronous(true);
		IBundleProvider result = myPatientDao.search(params);
		assertThat(toUnqualifiedVersionlessIdValues(result), containsInAnyOrder(pt0id.getValue()));

		// Search with source param
		 params = new SearchParameterMap();
		params.setLoadSynchronous(true);
		params.add(Constants.PARAM_SOURCE, new TokenAndListParam()
			.addAnd(new TokenParam("urn:source:0"), new TokenParam("@a_request_id")));
		try {
			myPatientDao.search(params);
		} catch (InvalidRequestException e) {
			assertEquals(e.getMessage(), Msg.code(1216) + "The _source parameter is disabled on this server");
		}
	}

	@Test
	public void deleteWithSource() {
		Patient patient = new Patient();
		String patientId = "Patient/pt-001";
		patient.setId(patientId);
		String source = "urn:source:0";
		patient.getMeta().setSource(source);
		patient.addName().setFamily("Presley");
		myPatientDao.update(patient);
		SearchParameterMap map = SearchParameterMap.newSynchronous();
		map.add(Constants.PARAM_SOURCE, new StringParam(source));
		{
			IBundleProvider result = myPatientDao.search(map);
			assertThat(toUnqualifiedVersionlessIdValues(result), containsInAnyOrder(patientId));
		}
		myPatientDao.delete(new IdType(patientId));
		{
			myCaptureQueriesListener.clear();
			IBundleProvider result = myPatientDao.search(map);
			myCaptureQueriesListener.logSelectQueries();
			assertEquals(0, result.size());
		}

	}

	@Test
	public void testSearchSource_withContainsModifier_returnsContainedSources() {
		myStorageSettings.setAllowContainsSearches(true);

		IIdType p1Id = createPatientWithSource("http://some-source");
		IIdType p2Id = createPatientWithSource("http://some-source/v1/321");
		createPatientWithSource("http://another-source");

		UriParam uriParam = new UriParam("some-source").setQualifier(UriParamQualifierEnum.CONTAINS);
		IBundleProvider results = myPatientDao.search(createSourceSearchParameterMap(uriParam));
		assertThat(toUnqualifiedVersionlessIdValues(results), containsInAnyOrder(p1Id.getValue(), p2Id.getValue()));
	}

	@Test
	public void testSearchSource_withContainsModifierAndContainsSearchesDisabled_throwsException() {
		myStorageSettings.setAllowContainsSearches(false);

		UriParam uriParam = new UriParam("some-source").setQualifier(UriParamQualifierEnum.CONTAINS);
		try {
			myPatientDao.search(createSourceSearchParameterMap(uriParam));
			fail();
		} catch (MethodNotAllowedException e) {
			assertEquals(Msg.code(2417) + ":contains modifier is disabled on this server", e.getMessage());
		}
	}

	@Test
	public void testSearchSource_withMissingModifierFalse_returnsNonEmptySources() {
		IIdType p1Id = createPatientWithSource("http://some-source/v1");
		createPatientWithSource(null);

		UriParam uriParam = new UriParam();
		uriParam.setMissing(false);
		IBundleProvider result = myPatientDao.search(createSourceSearchParameterMap(uriParam));
		assertThat(toUnqualifiedVersionlessIdValues(result), containsInAnyOrder(p1Id.getValue()));
	}

	@Test
	public void testSearchSource_withMissingModifierTrue_returnsEmptySources() {
		createPatientWithSource("http://some-source/v1");

		// created Patient without source
		Patient p1 = new Patient();
		p1.setActive(true);
		// requestId should be added to requestDetails
		// without both source and requestId HFJ_RES_VER_PROV table will not be updated
		// as a result search will return empty Bundle
		RequestDetails requestDetails = new ServletRequestDetails();
		requestDetails.setRequestId("requestId");
		DaoMethodOutcome outcome = myPatientDao.create(p1, requestDetails);

		UriParam uriParam = new UriParam();
		uriParam.setMissing(true);
		IBundleProvider result = myPatientDao.search(createSourceSearchParameterMap(uriParam));
		assertThat(toUnqualifiedVersionlessIdValues(result), containsInAnyOrder(outcome.getId().toUnqualifiedVersionless().getValue()));
	}

	@ParameterizedTest
	@CsvSource({
		"invalid_uri",
		"http://some-source/ with_invalid_uri"
	})
	public void testSearchSource_withAboveModifierAndInvalidURI_throwsException(String theUri) {
		try {
			UriParam uriParam = new UriParam(theUri).setQualifier(UriParamQualifierEnum.ABOVE);
			myPatientDao.search(createSourceSearchParameterMap(uriParam));
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(2419) + "_source is not valid URI: " + theUri, e.getMessage());
		}
	}

	@Test
	public void testSearchSource_withAboveModifier_returnsSourcesAbove() {
		IIdType p1Id = createPatientWithSource("http://some-source/v1/123");
		IIdType p2Id = createPatientWithSource("http://some-source/v1/321");
		IIdType p3Id = createPatientWithSource("http://some-source/v1/321/v2");
		IIdType p4Id = createPatientWithSource("http://another-source");

		UriParam uriParam = new UriParam("http://some-source/v1/321/v2/456").setQualifier(UriParamQualifierEnum.ABOVE);
		IBundleProvider results = myPatientDao.search(createSourceSearchParameterMap(uriParam));
		assertThat(toUnqualifiedVersionlessIdValues(results), containsInAnyOrder(p2Id.getValue(), p3Id.getValue()));

		uriParam = new UriParam("http://some-source/v1/321/v2").setQualifier(UriParamQualifierEnum.ABOVE);
		results = myPatientDao.search(createSourceSearchParameterMap(uriParam));
		assertThat(toUnqualifiedVersionlessIdValues(results), containsInAnyOrder(p2Id.getValue(), p3Id.getValue()));

		uriParam = new UriParam("http://some-source/v1/321").setQualifier(UriParamQualifierEnum.ABOVE);
		results = myPatientDao.search(createSourceSearchParameterMap(uriParam));
		assertThat(toUnqualifiedVersionlessIdValues(results), containsInAnyOrder(p2Id.getValue()));

		uriParam = new UriParam("http://some-source/fhir/v5/789").setQualifier(UriParamQualifierEnum.ABOVE);
		results = myPatientDao.search(createSourceSearchParameterMap(uriParam));
		assertThat(toUnqualifiedVersionlessIdValues(results), empty());

		uriParam = new UriParam("http://some-source").setQualifier(UriParamQualifierEnum.ABOVE);
		results = myPatientDao.search(createSourceSearchParameterMap(uriParam));
		assertThat(toUnqualifiedVersionlessIdValues(results), empty());

		uriParam = new UriParam("http://another-source").setQualifier(UriParamQualifierEnum.ABOVE);
		results = myPatientDao.search(createSourceSearchParameterMap(uriParam));
		assertThat(toUnqualifiedVersionlessIdValues(results), containsInAnyOrder(p4Id.getValue()));
	}

	@Test
	public void testSearchSource_withBelowModifier_returnsSourcesBelow() {
		IIdType p1Id = createPatientWithSource("http://some-source/v1/123");
		IIdType p2Id = createPatientWithSource("http://some-source/v1");
		IIdType p3Id = createPatientWithSource("http://some-source");

		createPatientWithSource("http://another-source");

		UriParam uriParam = new UriParam("http://some-source").setQualifier(UriParamQualifierEnum.BELOW);
		IBundleProvider results = myPatientDao.search(createSourceSearchParameterMap(uriParam));
		List<String> values = toUnqualifiedVersionlessIdValues(results);

		assertThat(values, containsInAnyOrder(p1Id.getValue(), p2Id.getValue(), p3Id.getValue()));
	}

	private SearchParameterMap createSourceSearchParameterMap(UriParam theUriParam) {
		SearchParameterMap searchParameterMap = new SearchParameterMap();
		searchParameterMap.setLoadSynchronous(true);
		searchParameterMap.add(PARAM_SOURCE, theUriParam);
		return searchParameterMap;
	}

	private IIdType createPatientWithSource(String theSource) {
		Patient p1 = new Patient();
		p1.setActive(true);
		p1.setMeta(new Meta().setSource(theSource));
		return myPatientDao.create(p1).getId().toUnqualifiedVersionless();
	}

	public static void assertConflictException(String theResourceType, ResourceVersionConflictException e) {
		assertThat(e.getMessage(), matchesPattern(
			"Unable to delete [a-zA-Z]+/[0-9]+ because at least one resource has a reference to this resource. First reference found was resource " + theResourceType + "/[0-9]+ in path [a-zA-Z]+.[a-zA-Z]+"));

	}

}
