package ca.uhn.fhir.jpa.mdm.svc;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.jpa.interceptor.PatientIdPartitionInterceptor;
import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.extractor.ISearchParamExtractor;
import ca.uhn.fhir.mdm.api.IMdmResourceDaoSvc;
import ca.uhn.fhir.mdm.api.MdmConstants;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.mdm.util.MdmResourceUtil;
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.param.StringOrListParam;
import ca.uhn.fhir.rest.param.StringParam;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MdmResourceDaoSvcTest extends BaseMdmR4Test {
	private static final String TEST_EID = "TEST_EID";
	@Autowired
	IMdmResourceDaoSvc myResourceDaoSvc;
	@Autowired
	private ISearchParamExtractor mySearchParamExtractor;

	private PatientIdPartitionInterceptor myPatientIdPartitionInterceptor;
	@Autowired
	private DaoRegistry myDaoRegistry;

	@Override
	@AfterEach
	public void after() throws IOException {
		myPartitionSettings.setPartitioningEnabled(new PartitionSettings().isPartitioningEnabled());
		super.after();
	}

	@Test
	public void testSearchPatientByEidExcludesNonGoldenPatients() {
		Patient goodSourcePatient = addExternalEID(createGoldenPatient(), TEST_EID);

		myPatientDao.update(goodSourcePatient);

		Patient badSourcePatient = addExternalEID(createRedirectedGoldenPatient(new Patient()), TEST_EID);
		MdmResourceUtil.setGoldenResourceRedirected(badSourcePatient);
		myPatientDao.update(badSourcePatient);

		Optional<IAnyResource> foundGoldenResource = myResourceDaoSvc.searchGoldenResourceByEID(TEST_EID, "Patient");
		assertThat(foundGoldenResource).isPresent();
		assertEquals(goodSourcePatient.getIdElement().toUnqualifiedVersionless().getValue(), foundGoldenResource.get().getIdElement().toUnqualifiedVersionless().getValue());
	}

	@Test
	public void testSearchGoldenResourceByEidExcludesNonMdmManaged() {
		Patient goodSourcePatient = addExternalEID(createGoldenPatient(), TEST_EID);
		myPatientDao.update(goodSourcePatient);

		Patient badSourcePatient = addExternalEID(createPatient(new Patient()), TEST_EID);
		myPatientDao.update(badSourcePatient);

		Optional<IAnyResource> foundSourcePatient = myResourceDaoSvc.searchGoldenResourceByEID(TEST_EID, "Patient");
		assertThat(foundSourcePatient).isPresent();
		assertEquals(goodSourcePatient.getIdElement().toUnqualifiedVersionless().getValue(), foundSourcePatient.get().getIdElement().toUnqualifiedVersionless().getValue());
	}

	@Test
	public void tagResourceAsUnmatched_noId_DoesntAddMeta() {
		// setup
		Patient patient = buildFrankPatient(); // not saved
		MdmTransactionContext context = new MdmTransactionContext();
		context.setIsBlocked(true); // won't matter

		// test
		myResourceDaoSvc.tagResourceAsUnmatched(patient, context);

		// validate
		assertTrue(patient.getMeta() == null
			|| patient.getMeta().getTag()
			.stream().noneMatch(t -> t.getSystem().equalsIgnoreCase(MdmConstants.MDM_UNMATCHED_TAG_NAMESPACE)));
	}

	@Test
	public void tagResourceAsUnmatched_notBlockedNotTooMany_nothingSet() {
		// setup
		String existingSystem = "http://hapi-fhir.example.com";
		String value = "abc123";
		Patient patient = buildFrankPatient();
		patient.getMeta()
			.addTag()
			.setSystem(existingSystem)
			.setCode(value);
		MdmTransactionContext context = new MdmTransactionContext();

		DaoMethodOutcome outcome = myPatientDao.create(patient, new SystemRequestDetails());

		Patient saved = myPatientDao.read(outcome.getId(), new SystemRequestDetails());

		// test
		myResourceDaoSvc.tagResourceAsUnmatched(saved, context);

		// validate
		assertNotNull(saved.getMeta());
		assertTrue(saved.getMeta()
			.getTag()
			.stream()
			.anyMatch(t -> t.getSystem().equalsIgnoreCase(existingSystem) && t.getCode().equalsIgnoreCase(value)));
		assertTrue(saved.getMeta()
			.getTag()
			.stream()
			.noneMatch(t -> t.getSystem().equalsIgnoreCase(MdmConstants.MDM_UNMATCHED_TAG_NAMESPACE)));
	}

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	public void tagResourceAsUnmatched_withUnmatchedCriteria_works(boolean theIsTooMany) {
		// setup
		Patient patient = buildFrankPatient();
		MdmTransactionContext context = new MdmTransactionContext();
		if (theIsTooMany) {
			context.setTooManyCandidatesMatched(true);
		} else {
			// blocked
			context.setIsBlocked(true);
		}
		String existingSystem = "http://hapi-fhir.example.com";
		String value = "abc123";
		patient.getMeta()
			.addTag()
			.setSystem(existingSystem)
			.setCode(value);

		DaoMethodOutcome outcome = myPatientDao.create(patient, new SystemRequestDetails());

		Patient saved = myPatientDao.read(outcome.getId(), new SystemRequestDetails());

		// test
		myResourceDaoSvc.tagResourceAsUnmatched(saved, context);

		// validate
		saved = myPatientDao.read(outcome.getId(), new SystemRequestDetails());

		assertNotNull(saved.getMeta());
		assertTrue(saved.getMeta()
			.getTag()
			.stream()
			.filter(t -> t.getSystem().equalsIgnoreCase(MdmConstants.MDM_UNMATCHED_TAG_NAMESPACE))
			.anyMatch(t -> {
				if (theIsTooMany) {
					return t.getCode().equalsIgnoreCase(MdmConstants.TOO_MANY_CANDIDATES);
				} else {
					return t.getCode().equalsIgnoreCase(MdmConstants.BLOCKED_VALUE);
				}
			}));
		assertTrue(
			saved.getMeta()
				.getTag()
				.stream()
				.anyMatch(t -> t.getSystem().equalsIgnoreCase(existingSystem) && t.getCode().equalsIgnoreCase(value))
		);
	}

	@Test
	public void testSearchGoldenResourceOnSamePartition() {
		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionLookupSvc.createPartition(new PartitionEntity().setId(1).setName(PARTITION_1), null);
		RequestPartitionId requestPartitionId = RequestPartitionId.fromPartitionId(1);
		Patient patientOnPartition = createPatientOnPartition(new Patient(), true, false, requestPartitionId);
		Patient goodSourcePatient = addExternalEID(patientOnPartition, TEST_EID);
		SystemRequestDetails systemRequestDetails = new SystemRequestDetails();
		systemRequestDetails.setRequestPartitionId(requestPartitionId);
		myPatientDao.update(goodSourcePatient, systemRequestDetails);

		Optional<IAnyResource> foundSourcePatient = myResourceDaoSvc.searchGoldenResourceByEID(TEST_EID, "Patient", requestPartitionId);
		assertThat(foundSourcePatient).isPresent();
		assertEquals(goodSourcePatient.getIdElement().toUnqualifiedVersionless().getValue(), foundSourcePatient.get().getIdElement().toUnqualifiedVersionless().getValue());
	}

	@Test
	public void testSearchForMultiplePatientsByIdInPartitionedEnvironment() {
		// setup
		int resourceCount = 3;
		// keep alphabetical
		String[] idPrefaces = new String[] {
			"BLUE", "GREEN", "RED"
		};

		SearchParameterMap map;
		IBundleProvider result;

		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionSettings.setUnnamedPartitionMode(true);
		myPartitionSettings.setIncludePartitionInSearchHashes(false);
		myPatientIdPartitionInterceptor = new PatientIdPartitionInterceptor(getFhirContext(), mySearchParamExtractor, myPartitionSettings, myDaoRegistry, myTransactionBundleNormalizer);
		myInterceptorRegistry.registerInterceptor(myPatientIdPartitionInterceptor);

		try {
			StringOrListParam patientIds = new StringOrListParam();
			for (int i = 0; i < resourceCount; i++) {
				String idPreface = idPrefaces[i];
				Patient patient = new Patient();
				patient.setId("Patient/" + idPreface + i);
				// patients must be created with a forced id for PatientId partitioning
				Patient patientOnPartition = createPatientWithUpdate(patient,
					true, false, true);
				patientIds.add(new StringParam("Patient/" +
					patientOnPartition.getIdElement().getIdPart()
				));
				await().atLeast(100, TimeUnit.MILLISECONDS);
			}

			// test
			map = SearchParameterMap.newSynchronous();
			map.add("_id", patientIds);
			// we'll use a sort to ensure consistent ordering of returned values
			SortSpec sort = new SortSpec();
			sort.setOrder(SortOrderEnum.ASC);
			sort.setParamName("_id");
			map.setSort(sort);
			result = myPatientDao.search(map, new SystemRequestDetails());

			// verify
			assertNotNull(result);
			assertFalse(result.isEmpty());
			List<IBaseResource> resources = result.getAllResources();
			assertThat(resources).hasSize(resourceCount);

			int count = 0;
			for (IBaseResource resource : resources) {
				String id = idPrefaces[count++];
				assertTrue(resource instanceof Patient);
				Patient patient = (Patient) resource;
				assertThat(patient.getId()).contains(id);
			}

			// ensure single id works too
			StringParam firstId = patientIds.getValuesAsQueryTokens().get(0);
			map = SearchParameterMap.newSynchronous();
			map.add("_id", firstId);
			result = myPatientDao.search(map, new SystemRequestDetails());

			// verify 2
			assertNotNull(result);
			resources = result.getAllResources();
			assertThat(resources).hasSize(1);
			assertTrue(result.getAllResources().get(0) instanceof Patient);
			Patient patient = (Patient) result.getAllResources().get(0);
			assertThat(patient.getId()).contains(firstId.getValue());
		} finally {
			myInterceptorRegistry.unregisterInterceptor(myPatientIdPartitionInterceptor);
		}
	}

	@Test
	public void testSearchGoldenResourceOnDifferentPartitions() {
		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionLookupSvc.createPartition(new PartitionEntity().setId(1).setName(PARTITION_1), null);
		RequestPartitionId requestPartitionId1 = RequestPartitionId.fromPartitionId(1);
		myPartitionLookupSvc.createPartition(new PartitionEntity().setId(2).setName(PARTITION_2), null);
		RequestPartitionId requestPartitionId2 = RequestPartitionId.fromPartitionId(2);
		Patient patientOnPartition = createPatientOnPartition(new Patient(), true, false, requestPartitionId1);
		Patient goodSourcePatient = addExternalEID(patientOnPartition, TEST_EID);
		SystemRequestDetails systemRequestDetails = new SystemRequestDetails();
		systemRequestDetails.setRequestPartitionId(requestPartitionId1);
		myPatientDao.update(goodSourcePatient, systemRequestDetails);

		Optional<IAnyResource> foundSourcePatient = myResourceDaoSvc.searchGoldenResourceByEID(TEST_EID, "Patient", requestPartitionId2);
		assertFalse(foundSourcePatient.isPresent());
	}
}
