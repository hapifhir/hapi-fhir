package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.dao.IFulltextSearchSvc;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.search.builder.SearchBuilder;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.SearchTotalModeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.param.StringAndListParam;
import ca.uhn.fhir.rest.param.StringOrListParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FhirSearchDaoR4Test extends BaseJpaR4Test implements IR4SearchIndexTests {

	private static final Logger ourLog = LoggerFactory.getLogger(FhirSearchDaoR4Test.class);

	@Autowired
	private IFulltextSearchSvc mySearchDao;

	@Autowired
	private DataSource myDataSource;

	@BeforeEach
	public void before() throws Exception {
		super.before();
		SearchBuilder.setMaxPageSizeForTest(10);
	}

	@AfterEach
	public void after() {
		SearchBuilder.setMaxPageSizeForTest(null);
	}

	@Override
	public IInterceptorService getInterceptorService() {
		return myInterceptorRegistry;
	}

	@Override
	public Logger getLogger() {
		return ourLog;
	}

	@Override
	public DaoRegistry getDaoRegistry() {
		return myDaoRegistry;
	}

	@Override
	public DataSource getDataSource() {
		return myDataSource;
	}

	@Test
	public void testDaoCallRequiresTransaction() {

		try {
			myResourceTableDao.count();
		} catch (InvalidDataAccessApiUsageException e) {
			// good
		}

		assert !TransactionSynchronizationManager.isActualTransactionActive();
	}

	@Test
	public void testSearchReturnsExpectedPatientsWhenContentTypeUsed() {
		// setup
		String content = "yui";

		Long id1;
		{
			Patient patient = new Patient();
			patient.addName().addGiven(content).setFamily("hirasawa");
			id1 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless().getIdPartAsLong();
		}

		{
			Patient patient = new Patient();
			patient.addName().addGiven("mio").setFamily("akiyama");
			myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless().getIdPartAsLong();
		}

		SearchParameterMap params = new SearchParameterMap();
		params.add("_content", new StringParam(content));

		// test
		List<JpaPid> ids = mySearchDao.search("Patient", params, SystemRequestDetails.newSystemRequestAllPartitions());

		// verify results
		assertThat(ids).hasSize(1);
		assertEquals(id1, ids.get(0).getId());
	}

	@Test
	public void testSearchesWithAccurateCountReturnOnlyExpectedResults() {
		// create 2 patients
		Patient patient = new Patient();
		patient.addName().setFamily("hirasawa");
		myPatientDao.create(patient);

		Patient patient2 = new Patient();
		patient2.addName().setFamily("akiyama");
		myPatientDao.create(patient2);

		// construct searchmap with Accurate search mode
		SearchParameterMap map = new SearchParameterMap();
		map.add(Constants.PARAM_CONTENT, new StringParam("hirasawa"));
		map.setSearchTotalMode(SearchTotalModeEnum.ACCURATE);

		// test
		IBundleProvider ret = myPatientDao.search(map);

		// only one should be returned
		assertEquals(1, ret.size());
		Patient retPatient = (Patient) ret.getAllResources().get(0);
		assertEquals(patient.getName().get(0).getFamily(), retPatient.getName().get(0).getFamily());
	}

	@Test
	public void testContentSearch() {
		Long id1;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			patient.addName().addGiven("testSearchStringParamWithNonNormalized_h\u00F6ra");
			patient.addName().addGiven("AAAS");
			patient.addName().addGiven("CCC");
			id1 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless().getIdPartAsLong();
		}
		Long id2;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("002");
			patient.addName().addGiven("testSearchStringParamWithNonNormalized_HORA");
			patient.addName().addGiven("AAAB");
			patient.addName().addGiven("CCC");
			id2 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless().getIdPartAsLong();
		}
		Long id3;
		{
			Organization org = new Organization();
			org.setName("DDD");
			id3 = myOrganizationDao.create(org, mySrd).getId().toUnqualifiedVersionless().getIdPartAsLong();
		}

		SearchParameterMap map;
		String resourceName = "Patient";

		// One term
		{
			StringAndListParam content = new StringAndListParam();
			content.addAnd(new StringOrListParam().addOr(new StringParam("AAAS")));

			map = new SearchParameterMap();
			map.add(Constants.PARAM_CONTENT, content);
			List<JpaPid> found = mySearchDao.search(resourceName, map, SystemRequestDetails.newSystemRequestAllPartitions());
			assertThat(JpaPid.toLongList(found)).containsExactlyInAnyOrder(id1);
		}
		// OR
		{
			StringAndListParam content = new StringAndListParam();
			content.addAnd(new StringOrListParam().addOr(new StringParam("AAAS")).addOr(new StringParam("AAAB")));

			map = new SearchParameterMap();
			map.add(Constants.PARAM_CONTENT, content);
			map.add(Constants.PARAM_CONTENT, content);
			List<JpaPid> found = mySearchDao.search(resourceName, map, SystemRequestDetails.newSystemRequestAllPartitions());
			assertThat(JpaPid.toLongList(found)).containsExactlyInAnyOrder(id1, id2);
		}
		// AND
		{
			StringAndListParam content = new StringAndListParam();
			content.addAnd(new StringOrListParam().addOr(new StringParam("AAAS")));
			content.addAnd(new StringOrListParam().addOr(new StringParam("CCC")));

			map = new SearchParameterMap();
			map.add(Constants.PARAM_CONTENT, content);
			List<JpaPid> found = mySearchDao.search(resourceName, map, SystemRequestDetails.newSystemRequestAllPartitions());
			assertThat(JpaPid.toLongList(found)).containsExactlyInAnyOrder(id1);
		}
		// AND OR
		{
			StringAndListParam content = new StringAndListParam();
			content.addAnd(new StringOrListParam().addOr(new StringParam("AAAB")).addOr(new StringParam("AAAS")));
			content.addAnd(new StringOrListParam().addOr(new StringParam("CCC")));

			map = new SearchParameterMap();
			map.add(Constants.PARAM_CONTENT, content);
			List<JpaPid> found = mySearchDao.search(resourceName, map, SystemRequestDetails.newSystemRequestAllPartitions());
			assertThat(JpaPid.toLongList(found)).containsExactlyInAnyOrder(id1, id2);
		}
		// All Resource Types
		{
			StringAndListParam content = new StringAndListParam();
			content.addAnd(new StringOrListParam().addOr(new StringParam("CCC")).addOr(new StringParam("DDD")));

			map = new SearchParameterMap();
			map.add(Constants.PARAM_CONTENT, content);
			List<JpaPid> found = mySearchDao.search(null, map, SystemRequestDetails.newSystemRequestAllPartitions());
			assertThat(JpaPid.toLongList(found)).containsExactlyInAnyOrder(id1, id2, id3);
		}

	}

	@Test
	public void testNarrativeSearch() {
		Long id1;
		{
			Patient patient = new Patient();
			patient.getText().setDivAsString("<div>AAAS<p>FOO</p> CCC    </div>");
			id1 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless().getIdPartAsLong();
		}
		Long id2;
		{
			Patient patient = new Patient();
			patient.getText().setDivAsString("<div>AAAB<p>FOO</p> CCC    </div>");
			id2 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless().getIdPartAsLong();
		}
		{
			Patient patient = new Patient();
			patient.getText().setDivAsString("<div>ZZYZXY</div>");
			myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless().getIdPartAsLong();
		}

		SearchParameterMap map;
		String resourceName = "Patient";

		// One term
		{
			StringAndListParam content = new StringAndListParam();
			content.addAnd(new StringOrListParam().addOr(new StringParam("AAAS")));

			map = new SearchParameterMap();
			map.add(Constants.PARAM_TEXT, content);
			List<JpaPid> found = mySearchDao.search(resourceName, map, SystemRequestDetails.newSystemRequestAllPartitions());
			assertThat(JpaPid.toLongList(found)).containsExactlyInAnyOrder(id1);
		}
		// OR
		{
			StringAndListParam content = new StringAndListParam();
			content.addAnd(new StringOrListParam().addOr(new StringParam("AAAS")).addOr(new StringParam("AAAB")));

			map = new SearchParameterMap();
			map.add(Constants.PARAM_TEXT, content);
			List<JpaPid> found = mySearchDao.search(resourceName, map, SystemRequestDetails.newSystemRequestAllPartitions());
			assertThat(JpaPid.toLongList(found)).containsExactlyInAnyOrder(id1, id2);
		}
		// AND
		{
			StringAndListParam content = new StringAndListParam();
			content.addAnd(new StringOrListParam().addOr(new StringParam("AAAS")));
			content.addAnd(new StringOrListParam().addOr(new StringParam("CCC")));

			map = new SearchParameterMap();
			map.add(Constants.PARAM_TEXT, content);
			List<JpaPid> found = mySearchDao.search(resourceName, map, SystemRequestDetails.newSystemRequestAllPartitions());
			assertThat(JpaPid.toLongList(found)).containsExactlyInAnyOrder(id1);
		}
		// AND OR
		{
			StringAndListParam content = new StringAndListParam();
			content.addAnd(new StringOrListParam().addOr(new StringParam("AAAB")).addOr(new StringParam("AAAS")));
			content.addAnd(new StringOrListParam().addOr(new StringParam("CCC")));

			map = new SearchParameterMap();
			map.add(Constants.PARAM_TEXT, content);
			List<JpaPid> found = mySearchDao.search(resourceName, map, SystemRequestDetails.newSystemRequestAllPartitions());
			assertThat(JpaPid.toLongList(found)).containsExactlyInAnyOrder(id1, id2);
		}
		// Tag Contents
		{
			StringAndListParam content = new StringAndListParam();
			content.addAnd(new StringOrListParam().addOr(new StringParam("div")));

			map = new SearchParameterMap();
			map.add(Constants.PARAM_TEXT, content);
			List<JpaPid> found = mySearchDao.search(resourceName, map, SystemRequestDetails.newSystemRequestAllPartitions());
			assertThat(JpaPid.toLongList(found)).isEmpty();
		}
	}

	@Test
	public void testSearchNarrativeWithLuceneSearch() {
		final int numberOfPatientsToCreate = SearchBuilder.getMaximumPageSize() + 10;
		List<String> expectedActivePatientIds = new ArrayList<>(numberOfPatientsToCreate);

		for (int i = 0; i < numberOfPatientsToCreate; i++) {
			Patient patient = new Patient();
			patient.getText().setDivAsString("<div>AAAS<p>FOO</p> CCC    </div>");
			expectedActivePatientIds.add(myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless().getIdPart());
		}

		{
			Patient patient = new Patient();
			patient.getText().setDivAsString("<div>AAAB<p>FOO</p> CCC    </div>");
			myPatientDao.create(patient, mySrd);
		}
		{
			Patient patient = new Patient();
			patient.getText().setDivAsString("<div>ZZYZXY</div>");
			myPatientDao.create(patient, mySrd);
		}

		SearchParameterMap map = new SearchParameterMap().setLoadSynchronous(true);
		map.add(Constants.PARAM_TEXT, new StringParam("AAAS"));

		IBundleProvider searchResultBundle = myPatientDao.search(map, mySrd);
		List<String> resourceIdsFromSearchResult = searchResultBundle.getAllResourceIds();

		assertThat(resourceIdsFromSearchResult).containsExactlyInAnyOrderElementsOf(expectedActivePatientIds);
	}

	@Test
	public void searchLuceneAndJPA_withLuceneMatchingButJpaNot_returnsNothing() {
		// setup
		int numToCreate = 2 * SearchBuilder.getMaximumPageSize() + 10;

		// create resources
		for (int i = 0; i < numToCreate; i++) {
			Patient patient = new Patient();
			patient.setActive(true);
			patient.addIdentifier()
				.setSystem("http://fhir.com")
				.setValue("ZYX");
			patient.getText().setDivAsString("<div>ABC</div>");
			myPatientDao.create(patient, mySrd);
		}

		// test
		SearchParameterMap map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.setSearchTotalMode(SearchTotalModeEnum.ACCURATE);
		TokenAndListParam tokenAndListParam = new TokenAndListParam();
		tokenAndListParam.addAnd(new TokenOrListParam().addOr(new TokenParam().setValue("true")));
		map.add("active", tokenAndListParam);
		map.add(Constants.PARAM_TEXT, new StringParam("ABC"));
		map.add("identifier", new TokenParam(null, "not found"));
		IBundleProvider provider = myPatientDao.search(map, mySrd);

		// verify
		assertEquals(0, provider.getAllResources().size());
	}

	@Test
	public void searchLuceneAndJPA_withLuceneBroadAndJPASearchNarrow_returnsFoundResults() {
		// setup
		int numToCreate = 2 * SearchBuilder.getMaximumPageSize() + 10;
		String identifierToFind = "bcde";

		// create patients
		for (int i = 0; i < numToCreate; i++) {
			Patient patient = new Patient();
			patient.setActive(true);
			String identifierVal = i == numToCreate - 10 ? identifierToFind:
			"abcd";
			patient.addIdentifier()
				.setSystem("http://fhir.com")
				.setValue(identifierVal);

			patient.getText().setDivAsString(
				"<div>FINDME</div>"
			);
			myPatientDao.create(patient, mySrd);
		}

		// test
		SearchParameterMap map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.setSearchTotalMode(SearchTotalModeEnum.ACCURATE);
		TokenAndListParam tokenAndListParam = new TokenAndListParam();
		tokenAndListParam.addAnd(new TokenOrListParam().addOr(new TokenParam().setValue("true")));
		map.add("active", tokenAndListParam);
		map.add(Constants.PARAM_TEXT, new StringParam("FINDME"));
		map.add("identifier", new TokenParam(null, identifierToFind));
		IBundleProvider provider = myPatientDao.search(map, mySrd);

		// verify
		List<String> ids = provider.getAllResourceIds();
		assertEquals(1, ids.size());
	}

	@Test
	public void testLuceneNarrativeSearchQueryIntersectingJpaQuery() {
		final int numberOfPatientsToCreate = SearchBuilder.getMaximumPageSize() + 10;
		List<String> expectedActivePatientIds = new ArrayList<>(numberOfPatientsToCreate);

		// create active and non-active patients with the same narrative
		for (int i = 0; i < numberOfPatientsToCreate; i++) {
			Patient activePatient = new Patient();
			activePatient.getText().setDivAsString("<div>AAAS<p>FOO</p> CCC    </div>");
			activePatient.setActive(true);
			String patientId = myPatientDao.create(activePatient, mySrd).getId().toUnqualifiedVersionless().getIdPart();
			expectedActivePatientIds.add(patientId);

			Patient nonActivePatient = new Patient();
			nonActivePatient.getText().setDivAsString("<div>AAAS<p>FOO</p> CCC    </div>");
			nonActivePatient.setActive(false);
			myPatientDao.create(nonActivePatient, mySrd);
		}

		SearchParameterMap map = new SearchParameterMap().setLoadSynchronous(true);

		TokenAndListParam tokenAndListParam = new TokenAndListParam();
		tokenAndListParam.addAnd(new TokenOrListParam().addOr(new TokenParam().setValue("true")));

		map.add("active", tokenAndListParam);
		map.add(Constants.PARAM_TEXT, new StringParam("AAAS"));

		IBundleProvider searchResultBundle = myPatientDao.search(map, mySrd);
		List<String> resourceIdsFromSearchResult = searchResultBundle.getAllResourceIds();

		assertThat(resourceIdsFromSearchResult).containsExactlyInAnyOrderElementsOf(expectedActivePatientIds);
	}

	@Test
	public void testLuceneContentSearchQueryIntersectingJpaQuery() {
		final int numberOfPatientsToCreate = SearchBuilder.getMaximumPageSize() + 10;
		final String patientFamilyName = "Flanders";
		List<String> expectedActivePatientIds = new ArrayList<>(numberOfPatientsToCreate);

		// create active and non-active patients with the same narrative
		for (int i = 0; i < numberOfPatientsToCreate; i++) {
			Patient activePatient = new Patient();
			activePatient.addName().setFamily(patientFamilyName);
			activePatient.setActive(true);
			String patientId = myPatientDao.create(activePatient, mySrd).getId().toUnqualifiedVersionless().getIdPart();
			expectedActivePatientIds.add(patientId);

			Patient nonActivePatient = new Patient();
			nonActivePatient.addName().setFamily(patientFamilyName);
			nonActivePatient.setActive(false);
			myPatientDao.create(nonActivePatient, mySrd);
		}

		SearchParameterMap map = new SearchParameterMap().setLoadSynchronous(true);
		TokenAndListParam tokenAndListParam = new TokenAndListParam();
		tokenAndListParam.addAnd(new TokenOrListParam().addOr(new TokenParam().setValue("true")));
		map.add("active", tokenAndListParam);
		map.add(Constants.PARAM_CONTENT, new StringParam(patientFamilyName));

		IBundleProvider searchResultBundle = myPatientDao.search(map, mySrd);
		List<String> resourceIdsFromSearchResult = searchResultBundle.getAllResourceIds();

		assertThat(resourceIdsFromSearchResult).containsExactlyInAnyOrderElementsOf(expectedActivePatientIds);
	}
}
