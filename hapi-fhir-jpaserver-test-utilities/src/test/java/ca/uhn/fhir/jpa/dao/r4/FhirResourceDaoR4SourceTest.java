package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import org.apache.commons.text.RandomStringGenerator;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

@SuppressWarnings({"Duplicates"})
public class FhirResourceDaoR4SourceTest extends BaseJpaR4Test {

	@AfterEach
	public final void after() {
		when(mySrd.getRequestId()).thenReturn(null);
		myDaoConfig.setStoreMetaSourceInformation(new DaoConfig().getStoreMetaSourceInformation());
	}

	@BeforeEach
	public void before() {
		myDaoConfig.setStoreMetaSourceInformation(DaoConfig.StoreMetaSourceInformationEnum.SOURCE_URI_AND_REQUEST_ID);
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
		myDaoConfig.setStoreMetaSourceInformation(DaoConfig.StoreMetaSourceInformationEnum.NONE);
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

	public static void assertConflictException(String theResourceType, ResourceVersionConflictException e) {
		assertThat(e.getMessage(), matchesPattern(
			"Unable to delete [a-zA-Z]+/[0-9]+ because at least one resource has a reference to this resource. First reference found was resource " + theResourceType + "/[0-9]+ in path [a-zA-Z]+.[a-zA-Z]+"));

	}

}
