package ca.uhn.fhir.jpa.dao.r4;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.search.builder.SearchBuilder;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.SearchTotalModeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.util.DateRangeUtil;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public interface ILuceneSearchR4Test {
	DaoRegistry getDaoRegistry();

	@SuppressWarnings("rawtypes")
	private IFhirResourceDao getResourceDao(String theResourceType) {
		return getDaoRegistry()
			.getResourceDao(theResourceType);
	}

	void runInTransaction(Runnable theRunnable);

	@Test
	default void testNoOpUpdateDoesNotModifyLastUpdated() throws InterruptedException {
		IFhirResourceDao<Patient> patientDao = getResourceDao("Patient");

		Patient patient = new Patient();
		patient.getNameFirstRep().setFamily("graham").addGiven("gary");

		patient = (Patient) patientDao.create(patient).getResource();
		Date originalLastUpdated = patient.getMeta().getLastUpdated();

		patient = (Patient) patientDao.update(patient).getResource();
		Date newLastUpdated = patient.getMeta().getLastUpdated();

		assertThat(originalLastUpdated).isEqualTo(newLastUpdated);
	}

	@Test
	default void luceneSearch_forTagsAndLastUpdated_shouldReturn() {
		// setup
		SystemRequestDetails requestDeatils = new SystemRequestDetails();
		String system = "http://fhir";
		String code = "cv";
		Date start = Date.from(Instant.now().minus(1, ChronoUnit.SECONDS).truncatedTo(ChronoUnit.SECONDS));
		Date end = Date.from(Instant.now().plus(10, ChronoUnit.SECONDS).truncatedTo(ChronoUnit.SECONDS));

		@SuppressWarnings("unchecked")
		IFhirResourceDao<Patient> patientDao = getResourceDao("Patient");

		// create a patient with some tag
		Patient patient = new Patient();
		patient.getMeta()
			.addTag(system, code, "");
		patient.addName().addGiven("homer")
			.setFamily("simpson");
		patient.addAddress()
			.setCity("springfield")
			.addLine("742 evergreen terrace");
		Long id = patientDao.create(patient, requestDeatils).getId().toUnqualifiedVersionless().getIdPartAsLong();

		// create base search map
		SearchParameterMap map = new SearchParameterMap().setLoadSynchronous(true);
		TokenOrListParam goldenRecordStatusToken = new TokenOrListParam(system, code);
		map.add(Constants.PARAM_TAG, goldenRecordStatusToken);
		DateRangeParam lastUpdated = DateRangeUtil.narrowDateRange(map.getLastUpdated(), start, end);
		map.setLastUpdated(lastUpdated);

		runInTransaction(() -> {
			Stream<JpaPid> stream;
			List<JpaPid> list;
			Optional<JpaPid> first;

			// tag search only; should return our resource
			map.setLastUpdated(null);
			stream = patientDao.searchForIdStream(map, new SystemRequestDetails(), null);
			list = stream.toList();
			assertEquals(1, list.size());
			first = list.stream().findFirst();
			assertTrue(first.isPresent());
			assertEquals(id, first.get().getId());

			// last updated search only; should return our resource
			map.setLastUpdated(lastUpdated);
			map.remove(Constants.PARAM_TAG);
			stream = patientDao.searchForIdStream(map, new SystemRequestDetails(), null);
			list = stream.toList();
			assertEquals(1, list.size());
			first = list.stream().findFirst();
			assertTrue(first.isPresent());
			assertEquals(id, first.get().getId());

			// both last updated and tags; should return our resource
			map.add(Constants.PARAM_TAG, goldenRecordStatusToken);
			stream = patientDao.searchForIdStream(map, new SystemRequestDetails(), null);
			list = stream.toList();
			assertEquals(1, list.size());
			first = list.stream().findFirst();
			assertTrue(first.isPresent());
			assertEquals(id, first.get().getId());
		});
	}

	@Test
	default void searchLuceneAndJPA_withLuceneMatchingButJpaNot_returnsNothing() {
		// setup
		int numToCreate = 2 * SearchBuilder.getMaximumPageSize() + 10;
		SystemRequestDetails requestDetails = new SystemRequestDetails();

		@SuppressWarnings("unchecked")
		IFhirResourceDao<Patient> patientDao = getResourceDao("Patient");

		// create resources
		for (int i = 0; i < numToCreate; i++) {
			Patient patient = new Patient();
			patient.setActive(true);
			patient.addIdentifier()
				.setSystem("http://fhir.com")
				.setValue("ZYX");
			patient.getText().setDivAsString("<div>ABC</div>");
			patientDao.create(patient, requestDetails);
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
		IBundleProvider provider = patientDao.search(map, requestDetails);

		// verify
		assertEquals(0, provider.getAllResources().size());
	}

	@Test
	default void testLuceneNarrativeSearchQueryIntersectingJpaQuery() {
		final int numberOfPatientsToCreate = SearchBuilder.getMaximumPageSize() + 10;
		List<String> expectedActivePatientIds = new ArrayList<>(numberOfPatientsToCreate);

		SystemRequestDetails requestDetails = new SystemRequestDetails();

		@SuppressWarnings("unchecked")
		IFhirResourceDao<Patient> patientDao = getResourceDao("Patient");


		// create active and non-active patients with the same narrative
		for (int i = 0; i < numberOfPatientsToCreate; i++) {
			Patient activePatient = new Patient();
			activePatient.getText().setDivAsString("<div>AAAS<p>FOO</p> CCC    </div>");
			activePatient.setActive(true);
			String patientId = patientDao.create(activePatient, requestDetails).getId().toUnqualifiedVersionless().getIdPart();
			expectedActivePatientIds.add(patientId);

			Patient nonActivePatient = new Patient();
			nonActivePatient.getText().setDivAsString("<div>AAAS<p>FOO</p> CCC    </div>");
			nonActivePatient.setActive(false);
			patientDao.create(nonActivePatient, requestDetails);
		}

		SearchParameterMap map = new SearchParameterMap().setLoadSynchronous(true);

		TokenAndListParam tokenAndListParam = new TokenAndListParam();
		tokenAndListParam.addAnd(new TokenOrListParam().addOr(new TokenParam().setValue("true")));

		map.add("active", tokenAndListParam);
		map.add(Constants.PARAM_TEXT, new StringParam("AAAS"));

		IBundleProvider searchResultBundle = patientDao.search(map, requestDetails);
		List<String> resourceIdsFromSearchResult = searchResultBundle.getAllResourceIds();

		assertThat(resourceIdsFromSearchResult).containsExactlyInAnyOrderElementsOf(expectedActivePatientIds);
	}

	@Test
	default void testLuceneContentSearchQueryIntersectingJpaQuery() {
		final int numberOfPatientsToCreate = SearchBuilder.getMaximumPageSize() + 10;
		final String patientFamilyName = "Flanders";
		List<String> expectedActivePatientIds = new ArrayList<>(numberOfPatientsToCreate);

		SystemRequestDetails requestDetails = new SystemRequestDetails();

		@SuppressWarnings("unchecked")
		IFhirResourceDao<Patient> patientDao = getResourceDao("Patient");


		// create active and non-active patients with the same narrative
		for (int i = 0; i < numberOfPatientsToCreate; i++) {
			Patient activePatient = new Patient();
			activePatient.addName().setFamily(patientFamilyName);
			activePatient.setActive(true);
			String patientId = patientDao.create(activePatient, requestDetails).getId().toUnqualifiedVersionless().getIdPart();
			expectedActivePatientIds.add(patientId);

			Patient nonActivePatient = new Patient();
			nonActivePatient.addName().setFamily(patientFamilyName);
			nonActivePatient.setActive(false);
			patientDao.create(nonActivePatient, requestDetails);
		}

		SearchParameterMap map = new SearchParameterMap().setLoadSynchronous(true);
		TokenAndListParam tokenAndListParam = new TokenAndListParam();
		tokenAndListParam.addAnd(new TokenOrListParam().addOr(new TokenParam().setValue("true")));
		map.add("active", tokenAndListParam);
		map.add(Constants.PARAM_CONTENT, new StringParam(patientFamilyName));

		IBundleProvider searchResultBundle = patientDao.search(map, requestDetails);
		List<String> resourceIdsFromSearchResult = searchResultBundle.getAllResourceIds();

		assertThat(resourceIdsFromSearchResult).containsExactlyInAnyOrderElementsOf(expectedActivePatientIds);
	}

	@Test
	default void searchLuceneAndJPA_withLuceneBroadAndJPASearchNarrow_returnsFoundResults() {
		// setup
		int numToCreate = 2 * SearchBuilder.getMaximumPageSize() + 10;
		String identifierToFind = "bcde";
		SystemRequestDetails requestDetails = new SystemRequestDetails();

		@SuppressWarnings("unchecked")
		IFhirResourceDao<Patient> patientDao = getResourceDao("Patient");

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
			patientDao.create(patient, requestDetails);
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
		IBundleProvider provider = patientDao.search(map, requestDetails);

		// verify
		List<String> ids = provider.getAllResourceIds();
		assertEquals(1, ids.size());
	}

	@Test
	default void testSearchNarrativeWithLuceneSearch() {
		final int numberOfPatientsToCreate = SearchBuilder.getMaximumPageSize() + 10;
		List<String> expectedActivePatientIds = new ArrayList<>(numberOfPatientsToCreate);

		SystemRequestDetails requestDetails = new SystemRequestDetails();

		@SuppressWarnings("unchecked")
		IFhirResourceDao<Patient> patientDao = getResourceDao("Patient");


		for (int i = 0; i < numberOfPatientsToCreate; i++) {
			Patient patient = new Patient();
			patient.getText().setDivAsString("<div>AAAS<p>FOO</p> CCC    </div>");
			expectedActivePatientIds.add(patientDao.create(patient, requestDetails).getId().toUnqualifiedVersionless().getIdPart());
		}

		{
			Patient patient = new Patient();
			patient.getText().setDivAsString("<div>AAAB<p>FOO</p> CCC    </div>");
			patientDao.create(patient, requestDetails);
		}
		{
			Patient patient = new Patient();
			patient.getText().setDivAsString("<div>ZZYZXY</div>");
			patientDao.create(patient, requestDetails);
		}

		SearchParameterMap map = new SearchParameterMap().setLoadSynchronous(true);
		map.add(Constants.PARAM_TEXT, new StringParam("AAAS"));

		IBundleProvider searchResultBundle = patientDao.search(map, requestDetails);
		List<String> resourceIdsFromSearchResult = searchResultBundle.getAllResourceIds();

		assertThat(resourceIdsFromSearchResult).containsExactlyInAnyOrderElementsOf(expectedActivePatientIds);
	}

}
