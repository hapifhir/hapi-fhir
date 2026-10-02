package ca.uhn.fhir.jpa.mdm.svc;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.IAnonymousInterceptor;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
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
import ca.uhn.fhir.mdm.model.MdmMatchAbortReason;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.mdm.util.MdmResourceUtil;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.param.StringOrListParam;
import ca.uhn.fhir.rest.param.StringParam;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

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
		myStorageSettings.setTagStorageMode(new JpaStorageSettings().getTagStorageMode());
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
		context.setMatchingAborted(MdmMatchAbortReason.BLOCKED); // won't matter

		// test
		myResourceDaoSvc.updateUnmatchedTags(patient, context);

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

		Patient saved = myPatientDao.read(outcome.getId().toUnqualifiedVersionless(), new SystemRequestDetails());

		// test
		myResourceDaoSvc.updateUnmatchedTags(saved, context);

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
	@EnumSource(value = MdmMatchAbortReason.class)
	public void tagResourceAsUnmatched_withUnmatchedCriteria_works(MdmMatchAbortReason theReason) {
		// setup
		Patient patient = buildFrankPatient();
		MdmTransactionContext context = new MdmTransactionContext();
		context.setMatchingAborted(theReason);

		String existingSystem = "http://hapi-fhir.example.com";
		String value = "abc123";
		patient.getMeta()
			.addTag()
			.setSystem(existingSystem)
			.setCode(value);

		DaoMethodOutcome outcome = myPatientDao.create(patient, new SystemRequestDetails());

		Patient saved = myPatientDao.read(outcome.getId().toUnqualifiedVersionless(), new SystemRequestDetails());

		// test
		myResourceDaoSvc.updateUnmatchedTags(saved, context);

		// validate
		saved = myPatientDao.read(outcome.getId().toUnqualifiedVersionless(), new SystemRequestDetails());

		assertNotNull(saved.getMeta());
		assertTrue(saved.getMeta()
			.getTag()
			.stream()
			.filter(t -> t.getSystem().equalsIgnoreCase(MdmConstants.MDM_UNMATCHED_TAG_NAMESPACE))
			.anyMatch(t -> {
				return t.getCode().equalsIgnoreCase(theReason.getCode());
			}));
		assertTrue(
			saved.getMeta()
				.getTag()
				.stream()
				.anyMatch(t -> t.getSystem().equalsIgnoreCase(existingSystem) && t.getCode().equalsIgnoreCase(value))
		);
	}

	@ParameterizedTest
	@EnumSource(value = MdmMatchAbortReason.class)
	public void updateMatchedTags_withOppositeTagSet_flipsThem(MdmMatchAbortReason theReason) {
		// setup
		Patient patient = buildFrankPatient();
		boolean isTooManyMatches = theReason == MdmMatchAbortReason.TOO_MANY_CANDIDATES;

		// set the opposite value onto it
		if (isTooManyMatches) {
			patient.getMeta()
				.addTag()
				.setSystem(MdmConstants.MDM_UNMATCHED_TAG_NAMESPACE)
				.setCode(MdmConstants.BLOCKED_VALUE);
		} else {
			patient.getMeta()
				.addTag()
				.setSystem(MdmConstants.MDM_UNMATCHED_TAG_NAMESPACE)
				.setCode(MdmConstants.TOO_MANY_CANDIDATES);
		}
		DaoMethodOutcome outcome = myPatientDao.create(patient, new SystemRequestDetails());

		// test
		MdmTransactionContext context = new MdmTransactionContext();
		context.setMatchingAborted(theReason);

		Patient saved = myPatientDao.read(outcome.getId().toUnqualifiedVersionless(), new SystemRequestDetails());
		myResourceDaoSvc.updateUnmatchedTags(saved, context);

		// validate
		String expectedCode = theReason.getCode();
		String unexpectedCode = isTooManyMatches ? MdmConstants.BLOCKED_VALUE : MdmConstants.TOO_MANY_CANDIDATES;
		Patient reread = myPatientDao.read(outcome.getId().toUnqualifiedVersionless(), new SystemRequestDetails());

		for (Patient tocheck : new Patient[] { saved, reread }) {
			assertTrue(tocheck.getMeta()
				.getTag()
				.stream()
				.anyMatch(t -> {
					return t.getSystem().equalsIgnoreCase(MdmConstants.MDM_UNMATCHED_TAG_NAMESPACE)
						&& t.getCode().equalsIgnoreCase(expectedCode);
				}));
			assertTrue(tocheck.getMeta()
				.getTag()
				.stream()
				.noneMatch(t -> {
					return t.getSystem().equalsIgnoreCase(MdmConstants.MDM_UNMATCHED_TAG_NAMESPACE)
						&& t.getCode().equalsIgnoreCase(unexpectedCode);
				}));
		}
	}

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	public void updateUnmatchedTags_withUnmatchedTag_removesOnlyThatTag(boolean theIsTooMany) {
		// setup
		String existingSystem = "http://hapi-fhir.example.com";
		String value = "abc123";
		Patient patient = buildFrankPatient();
		patient.getMeta()
			.addTag()
			.setSystem(existingSystem)
			.setCode(value);

		if (theIsTooMany) {
			patient.getMeta()
				.addTag()
				.setSystem(MdmConstants.MDM_UNMATCHED_TAG_NAMESPACE)
				.setCode(MdmConstants.TOO_MANY_CANDIDATES);
		} else {
			patient.getMeta()
				.addTag()
				.setSystem(MdmConstants.MDM_UNMATCHED_TAG_NAMESPACE)
				.setCode(MdmConstants.BLOCKED_VALUE);
		}
		DaoMethodOutcome outcome = myPatientDao.create(patient, new SystemRequestDetails());

		// test
		MdmTransactionContext context = new MdmTransactionContext();
		Patient saved = myPatientDao.read(outcome.getId().toUnqualifiedVersionless(), new SystemRequestDetails());
		myResourceDaoSvc.updateUnmatchedTags(saved, context);

		// validate
		Patient reread = myPatientDao.read(outcome.getId().toUnqualifiedVersionless(), new SystemRequestDetails());
		assertTrue(reread.getMeta()
			.getTag()
			.stream()
			.noneMatch(t -> t.getSystem().equalsIgnoreCase(MdmConstants.MDM_UNMATCHED_TAG_NAMESPACE)));
		// unrelated tags are not collateral damage
		assertTrue(reread.getMeta()
			.getTag()
			.stream()
			.anyMatch(t -> t.getSystem().equalsIgnoreCase(existingSystem) && t.getCode().equalsIgnoreCase(value)));
		// and the in-memory copy agrees with what was persisted
		assertTrue(saved.getMeta()
			.getTag()
			.stream()
			.noneMatch(t -> t.getSystem().equalsIgnoreCase(MdmConstants.MDM_UNMATCHED_TAG_NAMESPACE)));
	}

	/**
	 * Tags are added rather than replaced, so a resource can accumulate both codes under the unmatched system.
	 * Untagging has to clear the system, not one known code, or the next pass leaves two tags behind.
	 */
	@Test
	public void updateUnmatchedTags_bothCodesPresent_removesBoth() {
		// setup
		Patient patient = buildFrankPatient();
		patient.getMeta()
			.addTag()
			.setSystem(MdmConstants.MDM_UNMATCHED_TAG_NAMESPACE)
			.setCode(MdmConstants.BLOCKED_VALUE);
		patient.getMeta()
			.addTag()
			.setSystem(MdmConstants.MDM_UNMATCHED_TAG_NAMESPACE)
			.setCode(MdmConstants.TOO_MANY_CANDIDATES);

		DaoMethodOutcome outcome = myPatientDao.create(patient, new SystemRequestDetails());
		Patient saved = myPatientDao.read(outcome.getId().toUnqualifiedVersionless(), new SystemRequestDetails());
		assertEquals(2, saved.getMeta()
			.getTag()
			.stream()
			.filter(t -> t.getSystem().equalsIgnoreCase(MdmConstants.MDM_UNMATCHED_TAG_NAMESPACE))
			.count());

		// test
		myResourceDaoSvc.updateUnmatchedTags(saved, new MdmTransactionContext());

		// validate
		Patient reread = myPatientDao.read(outcome.getId().toUnqualifiedVersionless(), new SystemRequestDetails());
		assertTrue(reread.getMeta()
			.getTag()
			.stream()
			.noneMatch(t -> t.getSystem().equalsIgnoreCase(MdmConstants.MDM_UNMATCHED_TAG_NAMESPACE)));
	}

	@ParameterizedTest
	@EnumSource(value = JpaStorageSettings.TagStorageModeEnum.class)
	public void updateUnmatchedTags_allTagStorageModes_flipsThenClearsTag(JpaStorageSettings.TagStorageModeEnum theTagStorageMode) {
		// setup
		myStorageSettings.setTagStorageMode(theTagStorageMode);
		String existingSystem = "http://hapi-fhir.example.com";
		String value = "abc123";
		Patient patient = buildFrankPatient();
		patient.getMeta()
			.addTag()
			.setSystem(existingSystem)
			.setCode(value);
		patient.getMeta()
			.addTag()
			.setSystem(MdmConstants.MDM_UNMATCHED_TAG_NAMESPACE)
			.setCode(MdmConstants.BLOCKED_VALUE);
		DaoMethodOutcome outcome = myPatientDao.create(patient, new SystemRequestDetails());
		MdmTransactionContext context = new MdmTransactionContext();
		context.setMatchingAborted(MdmMatchAbortReason.TOO_MANY_CANDIDATES);

		// test - flip
		Patient saved = myPatientDao.read(outcome.getId().toUnqualifiedVersionless(), new SystemRequestDetails());
		myResourceDaoSvc.updateUnmatchedTags(saved, context);

		// validate
		Patient reread = myPatientDao.read(outcome.getId().toUnqualifiedVersionless(), new SystemRequestDetails());
		assertThat(reread.getMeta().getTag())
			.filteredOn(t -> MdmConstants.MDM_UNMATCHED_TAG_NAMESPACE.equals(t.getSystem()))
			.extracting(t -> t.getCode())
			.containsExactly(MdmConstants.TOO_MANY_CANDIDATES);
		assertThat(reread.getMeta().getTag(existingSystem, value)).isNotNull();

		// test - clear
		myResourceDaoSvc.updateUnmatchedTags(reread, new MdmTransactionContext());

		// validate
		reread = myPatientDao.read(outcome.getId().toUnqualifiedVersionless(), new SystemRequestDetails());
		assertThat(reread.getMeta().getTag())
			.noneMatch(t -> MdmConstants.MDM_UNMATCHED_TAG_NAMESPACE.equals(t.getSystem()));
		assertThat(reread.getMeta().getTag(existingSystem, value)).isNotNull();
	}

	/**
	 * A resource that is still omitted for the same reason is already correct. Writing to it anyway fires a resource
	 * update, which can send the resource back through MDM.
	 */
	@Test
	public void updateUnmatchedTags_desiredTagAlreadyStored_writesNothing() {
		// setup
		Patient patient = buildFrankPatient();
		MdmResourceUtil.tagResourceAsBlocked(patient);
		DaoMethodOutcome outcome = myPatientDao.create(patient, new SystemRequestDetails());
		Patient saved = myPatientDao.read(outcome.getId(), new SystemRequestDetails());

		MdmTransactionContext context = new MdmTransactionContext();
		context.setIsBlocked(true);

		AtomicInteger updateCount = new AtomicInteger();
		IAnonymousInterceptor updateCounter = (thePointcut, theArgs) -> updateCount.incrementAndGet();
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.STORAGE_PRECOMMIT_RESOURCE_UPDATED, updateCounter);
		try {
			// test
			myResourceDaoSvc.updateUnmatchedTags(saved, context);
		} finally {
			myInterceptorRegistry.unregisterInterceptor(updateCounter);
		}

		// validate
		assertThat(updateCount.get()).isZero();
	}

	/**
	 * When the current code is stored beside a stale one, only the stale one goes; the resource handed in must still
	 * carry the current code, since MDM keeps working with it after this call.
	 */
	@Test
	public void updateUnmatchedTags_desiredAndStaleTagsStored_keepsDesiredTagInMemory() {
		// setup
		Patient patient = buildFrankPatient();
		patient.getMeta().addTag().setSystem(MdmConstants.MDM_UNMATCHED_TAG_NAMESPACE).setCode(MdmConstants.BLOCKED_VALUE);
		patient.getMeta()
			.addTag()
			.setSystem(MdmConstants.MDM_UNMATCHED_TAG_NAMESPACE)
			.setCode(MdmConstants.TOO_MANY_CANDIDATES);
		DaoMethodOutcome outcome = myPatientDao.create(patient, new SystemRequestDetails());
		Patient saved = myPatientDao.read(outcome.getId(), new SystemRequestDetails());
		assertThat(saved.getMeta().getTag())
			.filteredOn(t -> MdmConstants.MDM_UNMATCHED_TAG_NAMESPACE.equalsIgnoreCase(t.getSystem()))
			.hasSize(2);

		MdmTransactionContext context = new MdmTransactionContext();
		context.setIsBlocked(true);

		// test
		myResourceDaoSvc.updateUnmatchedTags(saved, context);

		// validate
		Patient reread = myPatientDao.read(outcome.getId(), new SystemRequestDetails());
		for (Patient toCheck : new Patient[] {saved, reread}) {
			assertThat(toCheck.getMeta().getTag())
				.filteredOn(t -> MdmConstants.MDM_UNMATCHED_TAG_NAMESPACE.equalsIgnoreCase(t.getSystem()))
				.extracting(Coding::getCode)
				.containsExactly(MdmConstants.BLOCKED_VALUE);
		}
	}

	@Test
	public void updateUnmatchedTags_partitionedResource_storesTagInResourcePartition() {
		// setup
		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionLookupSvc.createPartition(new PartitionEntity().setId(1).setName(PARTITION_1), null);
		RequestPartitionId partitionId = RequestPartitionId.fromPartitionId(1);
		SystemRequestDetails partitionRequest = new SystemRequestDetails().setRequestPartitionId(partitionId);
		DaoMethodOutcome outcome = myPatientDao.create(buildFrankPatient(), partitionRequest);
		Patient saved = myPatientDao.read(outcome.getId().toUnqualifiedVersionless(), partitionRequest);

		MdmTransactionContext context = new MdmTransactionContext();
		context.setIsBlocked(true);

		// test
		myResourceDaoSvc.updateUnmatchedTags(saved, context);

		// validate
		Patient reread = myPatientDao.read(outcome.getId().toUnqualifiedVersionless(), partitionRequest);
		assertThat(reread.getMeta().getTag())
			.filteredOn(t -> MdmConstants.MDM_UNMATCHED_TAG_NAMESPACE.equalsIgnoreCase(t.getSystem()))
			.extracting(Coding::getCode)
			.containsExactly(MdmConstants.BLOCKED_VALUE);
	}

	/**
	 * A resource that doesn't carry its partition is tagged in the partition the partition interceptors choose, as
	 * the rest of the MDM flow does, rather than across all partitions.
	 */
	@Test
	public void updateUnmatchedTags_noResourcePartition_usesPartitionFromInterceptors() {
		// setup
		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionLookupSvc.createPartition(new PartitionEntity().setId(1).setName(PARTITION_1), null);
		RequestPartitionId partitionId = RequestPartitionId.fromPartitionId(1);
		SystemRequestDetails partitionRequest = new SystemRequestDetails().setRequestPartitionId(partitionId);
		DaoMethodOutcome outcome = myPatientDao.create(buildFrankPatient(), partitionRequest);
		Patient saved = myPatientDao.read(outcome.getId().toUnqualifiedVersionless(), partitionRequest);
		saved.setUserData(Constants.RESOURCE_PARTITION_ID, null);

		MdmTransactionContext context = new MdmTransactionContext();
		context.setIsBlocked(true);

		FixedPartitionInterceptor partitionInterceptor = new FixedPartitionInterceptor(partitionId);
		myInterceptorRegistry.registerInterceptor(partitionInterceptor);
		try {
			// test
			myResourceDaoSvc.updateUnmatchedTags(saved, context);
		} finally {
			myInterceptorRegistry.unregisterInterceptor(partitionInterceptor);
		}

		// validate
		assertThat(partitionInterceptor.getCallCount()).isPositive();
		Patient reread = myPatientDao.read(outcome.getId().toUnqualifiedVersionless(), partitionRequest);
		assertThat(reread.getMeta().getTag())
			.filteredOn(t -> MdmConstants.MDM_UNMATCHED_TAG_NAMESPACE.equalsIgnoreCase(t.getSystem()))
			.extracting(Coding::getCode)
			.containsExactly(MdmConstants.BLOCKED_VALUE);
	}

	@Interceptor
	public static class FixedPartitionInterceptor {
		private final RequestPartitionId myPartitionId;
		private final AtomicInteger myCallCount = new AtomicInteger();

		FixedPartitionInterceptor(RequestPartitionId thePartitionId) {
			myPartitionId = thePartitionId;
		}

		@Hook(Pointcut.STORAGE_PARTITION_IDENTIFY_ANY)
		public RequestPartitionId identifyPartition() {
			myCallCount.incrementAndGet();
			return myPartitionId;
		}

		int getCallCount() {
			return myCallCount.get();
		}
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
