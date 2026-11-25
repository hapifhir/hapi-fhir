package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.model.WorkChunkStatusEnum;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.batch2.jobs.term.valueset.preexpand.PreExpandValueSetCleanupParameters;
import ca.uhn.fhir.jpa.dao.data.IBatch2WorkChunkRepository;
import ca.uhn.fhir.jpa.dao.r5.BaseJpaR5Test;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.entity.Batch2WorkChunkEntity;
import ca.uhn.fhir.jpa.entity.TermValueSet;
import ca.uhn.fhir.jpa.entity.TermValueSetConcept;
import ca.uhn.fhir.jpa.entity.TermValueSetConceptDesignation;
import ca.uhn.fhir.jpa.entity.TermValueSetPreExpansionStatusEnum;
import ca.uhn.fhir.jpa.term.api.ITermValueSetStorageSvc;
import ca.uhn.fhir.jpa.test.Batch2JobHelper;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.UrlUtil;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Multimaps;
import jakarta.persistence.LockModeType;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static ca.uhn.fhir.jpa.batch2.jobs.term.valueset.preexpand.PreExpandValueSetJobAppCtx.JOB_ID_PRE_EXPAND_VALUESET;
import static ca.uhn.fhir.jpa.batch2.jobs.term.valueset.preexpand.PreExpandValueSetJobAppCtx.JOB_ID_PRE_EXPAND_VALUESET_CLEANUP;
import static ca.uhn.fhir.jpa.term.TermValueSetStorageSvcImpl.INTENDED_VERSION_ID_NULL;
import static ca.uhn.fhir.test.utilities.UuidUtils.UUID_PATTERN;
import static org.apache.commons.lang3.ObjectUtils.getIfNull;
import static org.apache.commons.lang3.StringUtils.defaultIfBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TermValueSetStorageSvcImplTest extends BaseJpaR5Test {

	public static final String CS_URL = "http://system-0";
	private static final String VS_URI = "http://vs";
	private static final String THE_VALUE_SET_NAME = "The ValueSet Name";
	private static final String VS_ID = "VS0";
	private static final String VS_ID_TYPED_AND_VERSION_1 = "ValueSet/" + VS_ID + "/_history/1";
	private static final String FORMER_VERSION = "1.0_former";

	@Autowired
	private ITermValueSetStorageSvc mySvc;
	@Autowired
	private IBatch2WorkChunkRepository myWorkChunkRepository;
	private int myNextOrder = 0;

	@BeforeEach
	public void before() throws Exception {
		super.before();

		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionSettings.setDefaultPartitionId(-1);
	}

	@ParameterizedTest
	@ValueSource(strings = {"1.0", ""})
	void testStartStagingVersion(String theVersion) {
		String version = defaultIfBlank(theVersion, null);
		createValueSetResource(version);

		// Test
		String stagingVersion = mySvc.startStagingVersion(VS_URI, version);

		// Validate
		assertThat(stagingVersion).matches(UUID_PATTERN);
		runInTransaction(() -> {
			List<TermValueSet> valueSets = myTermValueSetDao.findAll();

			// Non-staging first, staging is second
			valueSets.sort(Comparator.comparing(TermValueSet::getId));
			assertEquals(version, valueSets.get(0).getVersion());
			assertEquals(stagingVersion, valueSets.get(1).getVersion());
			assertEquals(VS_URI, valueSets.get(0).getUrl());
			assertEquals(VS_URI, valueSets.get(1).getUrl());
			assertNull(valueSets.get(0).getIntendedVersionId());
			assertEquals(getIfNull(version, INTENDED_VERSION_ID_NULL), valueSets.get(1).getIntendedVersionId());
			assertEquals(TermValueSetPreExpansionStatusEnum.EXPANSION_IN_PROGRESS, valueSets.get(0).getExpansionStatus());
			assertEquals(TermValueSetPreExpansionStatusEnum.EXPANSION_IN_PROGRESS, valueSets.get(1).getExpansionStatus());
			assertEquals(THE_VALUE_SET_NAME, valueSets.get(0).getName());
			assertEquals(THE_VALUE_SET_NAME, valueSets.get(1).getName());
			assertEquals(VS_ID, valueSets.get(0).getResource().getFhirId());
			assertEquals(VS_ID, valueSets.get(1).getResource().getFhirId());
		});
	}

	@Test
	void testStartStagingVersion_UnknownValueSet() {
		assertThatThrownBy(()->mySvc.startStagingVersion("http://foo", "1.0"))
			.isInstanceOf(ResourceNotFoundException.class)
			.hasMessageContaining("No ValueSet found with URL[http://foo] and Version[1.0]");
	}

	@ParameterizedTest
	@ValueSource(strings = {"2.0", ""})
	void testStartStagingVersion_UnknownVersion(String theVersion) {
		createValueSetResource("1.0");

		String messageVersion = isNotBlank(theVersion) ? theVersion : "(none)";
		assertThatThrownBy(()->mySvc.startStagingVersion(VS_URI, defaultIfBlank(theVersion, null)))
			.isInstanceOf(ResourceNotFoundException.class)
			.hasMessageContaining("No ValueSet found with URL[http://vs] and Version[" + messageVersion + "]");
	}


	@Test
	void testAddConceptsToExpansion_Flat() {
		// Setup
		createValueSetResource("1.0");
		String stagingVersion = mySvc.startStagingVersion(VS_URI, "1.0");

		// Test
		ValueSet toAdd = new ValueSet();
		toAdd.setUrl(VS_URI);
		toAdd.setVersion(stagingVersion);
		toAdd.getExpansion()
			.addContains()
			.setSystem(CS_URL)
			.setCode("CODE0")
			.setDisplay("Code 0")
			.addDesignation(new ValueSet.ConceptReferenceDesignationComponent().setLanguage("en").setValue("Code 0 en"));
		toAdd.getExpansion()
			.addContains()
			.setSystem(CS_URL)
			.setCode("CODE1")
			.setDisplay("Code 1");
		UploadStatistics outcome = mySvc.addConceptsToExpansion(toAdd, 1000);

		// Verify
		assertEquals(VS_ID_TYPED_AND_VERSION_1, outcome.getTarget().getValue());
		assertEquals(2, outcome.getAddedConceptCount());
		assertEquals(1, outcome.getAddedDesignationCount());
		runInTransaction(() -> {
			TermValueSet valueSet = myTermValueSetDao.findTermValueSetByUrlAndVersion(VS_URI, stagingVersion).orElseThrow();
			String actual = renderExpansionHierarchy(valueSet);
			String expected = """
				-System[http://system-0]
				 -CODE0 (Code 0) Order[1000]
				  -Designation[lang=en, value=Code 0 en]
				 -CODE1 (Code 1) Order[1001]
				""";
			assertEquals(expected, actual);

			assertEquals(2, valueSet.getTotalConcepts());
			assertEquals(1, valueSet.getTotalConceptDesignations());
		});
		assertPartitionSet();
	}


	@Test
	void testAddConceptsToExpansion_SomeCodesAlreadyExist() {
		// Setup
		createValueSetResource("1.0");
		String stagingVersion = mySvc.startStagingVersion(VS_URI, "1.0");

		createTermValueSetConceptAndDesignation(stagingVersion, "CODE0", "Code 0", null, "en", "Code 0 en", null);

		// Test
		ValueSet toAdd = new ValueSet();
		toAdd.setUrl(VS_URI);
		toAdd.setVersion(stagingVersion);
		toAdd.getExpansion()
			.addContains()
			.setSystem(CS_URL)
			.setCode("CODE0")
			.setDisplay("Code 0")
			.addDesignation(new ValueSet.ConceptReferenceDesignationComponent().setLanguage("en").setValue("Code 0 en"));
		toAdd.getExpansion()
			.addContains()
			.setSystem(CS_URL)
			.setCode("CODE1")
			.setDisplay("Code 1")
			.addDesignation(new ValueSet.ConceptReferenceDesignationComponent().setLanguage("en").setValue("Code 1 en"));
		UploadStatistics outcome = mySvc.addConceptsToExpansion(toAdd, 1000);

		// Verify
		assertEquals(VS_ID_TYPED_AND_VERSION_1, outcome.getTarget().getValue());
		assertEquals(1, outcome.getAddedConceptCount());
		runInTransaction(() -> {
			TermValueSet valueSet = myTermValueSetDao.findTermValueSetByUrlAndVersion(VS_URI, stagingVersion).orElseThrow();
			String actual = renderExpansionHierarchy(valueSet);
			String expected = """
				-System[http://system-0]
				 -CODE0 (Code 0) Order[0]
				  -Designation[lang=en, value=Code 0 en]
				 -CODE1 (Code 1) Order[1000]
				  -Designation[lang=en, value=Code 1 en]
				""";
			assertEquals(expected, actual);

			assertEquals(2, valueSet.getTotalConcepts());
			assertEquals(2, valueSet.getTotalConceptDesignations());
		});
		assertPartitionSet();
	}

	@Test
	void testAddConceptsToExpansion_CodesAlreadyExistButWithoutDisplayOrDesignation() {
		// Setup
		createValueSetResource("1.0");
		String stagingVersion = mySvc.startStagingVersion(VS_URI, "1.0");

		// Create code with no display or designation
		createTermValueSetConceptAndDesignation(stagingVersion, "CODE0", null, null, null, null, null);

		// Test - Create code with display and designation
		ValueSet toAdd = new ValueSet();
		toAdd.setUrl(VS_URI);
		toAdd.setVersion(stagingVersion);
		toAdd.getExpansion()
			.addContains()
			.setSystem(CS_URL)
			.setCode("CODE0")
			.setDisplay("Code 0")
			.addDesignation(new ValueSet.ConceptReferenceDesignationComponent().setLanguage("en").setValue("Code 0 en"));
		UploadStatistics outcome = mySvc.addConceptsToExpansion(toAdd, 1000);

		// Verify
		assertEquals(VS_ID_TYPED_AND_VERSION_1, outcome.getTarget().getValue());
		assertEquals(0, outcome.getAddedConceptCount());
		assertEquals(1, outcome.getAddedDesignationCount());
		runInTransaction(() -> {
			TermValueSet valueSet = myTermValueSetDao.findTermValueSetByUrlAndVersion(VS_URI, stagingVersion).orElseThrow();
			String actual = renderExpansionHierarchy(valueSet);
			String expected = """
				-System[http://system-0]
				 -CODE0 (Code 0) Order[0]
				  -Designation[lang=en, value=Code 0 en]
				""";
			assertEquals(expected, actual);

			assertEquals(1, valueSet.getTotalConcepts());
			assertEquals(1, valueSet.getTotalConceptDesignations());
		});
		assertPartitionSet();
	}

	@Test
	void testAddConceptsToExpansion_VersionedSystem() {
		// Setup
		createValueSetResource("1.0");
		String stagingVersion = mySvc.startStagingVersion(VS_URI, "1.0");

		// Test
		ValueSet toAdd = new ValueSet();
		toAdd.setUrl(VS_URI);
		toAdd.setVersion(stagingVersion);

		toAdd.getExpansion()
			.addContains()
			.setSystem(CS_URL)
			.setVersion("1.0")
			.setCode("CODE0")
			.setDisplay("Code 0")
			.addDesignation(new ValueSet.ConceptReferenceDesignationComponent().setLanguage("en").setValue("Code 0 en"));
		toAdd.getExpansion()
			.addContains()
			.setSystem(CS_URL + "|2.0")
			.setCode("CODE1")
			.setDisplay("Code 1")
			.addDesignation(new ValueSet.ConceptReferenceDesignationComponent().setLanguage("en").setValue("Code 1 en"));
		UploadStatistics outcome = mySvc.addConceptsToExpansion(toAdd, 1000);

		// Verify
		assertEquals(VS_ID_TYPED_AND_VERSION_1, outcome.getTarget().getValue());
		assertEquals(2, outcome.getAddedConceptCount());
		assertEquals(2, outcome.getAddedDesignationCount());
		runInTransaction(() -> {
			TermValueSet valueSet = myTermValueSetDao.findTermValueSetByUrlAndVersion(VS_URI, stagingVersion).orElseThrow();
			String actual = renderExpansionHierarchy(valueSet);
			String expected = """
				-System[http://system-0|1.0]
				 -CODE0 (Code 0) Order[1000]
				  -Designation[lang=en, value=Code 0 en]
				-System[http://system-0|2.0]
				 -CODE1 (Code 1) Order[1001]
				  -Designation[lang=en, value=Code 1 en]
				""";
			assertEquals(expected, actual);

			assertEquals(2, valueSet.getTotalConcepts());
			assertEquals(2, valueSet.getTotalConceptDesignations());
		});
		assertPartitionSet();
	}

	@Test
	void testAddConceptsToExpansion_VersionedSystem_AlreadyExists() {
		// Setup
		createValueSetResource("1.0");
		String stagingVersion = mySvc.startStagingVersion(VS_URI, "1.0");

		createTermValueSetConcept(stagingVersion, "CODE0", "Code 0", "1.0");
		createTermValueSetConcept(stagingVersion, "CODE1", "Code 1", "2.0");

		// Test
		ValueSet toAdd = new ValueSet();
		toAdd.setUrl(VS_URI);
		toAdd.setVersion(stagingVersion);

		// We'll add the same code from two versions of the system. This isn't generally
		// something people will do, but let's make sure we don't throw any constraint errors.
		toAdd.getExpansion()
			.addContains()
			.setSystem(CS_URL)
			.setVersion("1.0")
			.setCode("CODE0")
			.setDisplay("Code 0")
			.addDesignation(new ValueSet.ConceptReferenceDesignationComponent().setLanguage("en").setValue("Code 0 en"));
		toAdd.getExpansion()
			.addContains()
			.setSystem(CS_URL + "|2.0")
			.setCode("CODE1")
			.setDisplay("Code 1")
			.addDesignation(new ValueSet.ConceptReferenceDesignationComponent().setLanguage("en").setValue("Code 1 en"));
		UploadStatistics outcome = mySvc.addConceptsToExpansion(toAdd, 1000);

		// Verify
		assertEquals(VS_ID_TYPED_AND_VERSION_1, outcome.getTarget().getValue());
		assertEquals(0, outcome.getAddedConceptCount());
		assertEquals(2, outcome.getAddedDesignationCount());
		runInTransaction(() -> {
			TermValueSet valueSet = myTermValueSetDao.findTermValueSetByUrlAndVersion(VS_URI, stagingVersion).orElseThrow();
			String actual = renderExpansionHierarchy(valueSet);
			String expected = """
				-System[http://system-0|1.0]
				 -CODE0 (Code 0) Order[0]
				  -Designation[lang=en, value=Code 0 en]
				-System[http://system-0|2.0]
				 -CODE1 (Code 1) Order[1]
				  -Designation[lang=en, value=Code 1 en]
				""";
			assertEquals(expected, actual);

			assertEquals(2, valueSet.getTotalConcepts());
			assertEquals(2, valueSet.getTotalConceptDesignations());
		});
		assertPartitionSet();
	}

	@Test
	void testRemoveConceptsFromExpansion() {
		// Setup
		createValueSetResource("1.0");
		String stagingVersion = mySvc.startStagingVersion(VS_URI, "1.0");

		for (int i = 0; i < 5; i++) {
			createTermValueSetConceptAndDesignation(stagingVersion, "CODE" + i, "Code " + i, null, "en", "Code " + i + " en", null);
			createTermValueSetConceptAndDesignation(stagingVersion, "CODE" + i + "-0", "Code " + i + " Child 0", null, null, null, null);
		}

		// Test
		ValueSet toDelete = new ValueSet();
		toDelete.setUrl(VS_URI);
		toDelete.setVersion(stagingVersion);
		toDelete.getExpansion()
			.addContains()
			.setSystem(CS_URL)
			.setCode("CODE0");
		toDelete.getExpansion()
			.addContains()
			.setSystem(CS_URL)
			.setCode("CODE1-0");
		toDelete.getExpansion()
			.addContains()
			.setSystem(CS_URL)
			.setCode("CODE-ABC-0"); // Doesn't exist
		toDelete.getExpansion()
			.addContains()
			.setSystem(CS_URL)
			.setCode("CODE-ABC-1"); // Doesn't exist
		UploadStatistics outcome = mySvc.removeConceptsFromExpansion(toDelete);

		// Verify
		assertEquals(VS_ID_TYPED_AND_VERSION_1, outcome.getTarget().getValue());
		assertEquals(2, outcome.getRemovedConceptCount());
		runInTransaction(() -> {
			TermValueSet valueSet = myTermValueSetDao.findTermValueSetByUrlAndVersion(VS_URI, stagingVersion).orElseThrow();
			String actual = renderExpansionHierarchy(valueSet);
			String expected = """
				-System[http://system-0]
				 -CODE0-0 (Code 0 Child 0) Order[1]
				 -CODE1 (Code 1) Order[2]
				  -Designation[lang=en, value=Code 1 en]
				 -CODE2 (Code 2) Order[4]
				  -Designation[lang=en, value=Code 2 en]
				 -CODE2-0 (Code 2 Child 0) Order[5]
				 -CODE3 (Code 3) Order[6]
				  -Designation[lang=en, value=Code 3 en]
				 -CODE3-0 (Code 3 Child 0) Order[7]
				 -CODE4 (Code 4) Order[8]
				  -Designation[lang=en, value=Code 4 en]
				 -CODE4-0 (Code 4 Child 0) Order[9]
				""";
			assertEquals(expected, actual);

			assertEquals(8, valueSet.getTotalConcepts());
			assertEquals(4, valueSet.getTotalConceptDesignations());
		});
		assertPartitionSet();
	}

	@ParameterizedTest
	@ValueSource(strings = {"1.0", ""})
	void testActivateStagingVersion(String theVersion) {
		// Setup

		String version = defaultIfBlank(theVersion, null);
		createValueSetResource(version);

		// Add some codes to version 1.0, including a code with a child code
		// and a designation
		runInTransaction(() -> {
			TermValueSet termValueSet;
			if (version == null) {
				termValueSet = myTermValueSetDao.findTermValueSetByUrlAndNullVersion(VS_URI).orElseThrow();
			} else {
				termValueSet = myTermValueSetDao.findTermValueSetByUrlAndVersion(VS_URI, version).orElseThrow();
			}

			TermValueSetConcept code0 = new TermValueSetConcept();
			code0.setSystem(CS_URL);
			code0.setCode("CODE0");
			code0.setDisplay("Code 0");
			code0.setValueSet(termValueSet);
			code0.setOrder(0);
			myTermValueSetConceptDao.save(code0);

			TermValueSetConceptDesignation designation = new TermValueSetConceptDesignation();
			designation.setLanguage("en");
			designation.setValue("Code 0 en");
			designation.setConcept(code0);
			designation.setValueSet(termValueSet);
			code0.getDesignations().add(designation);
			myTermValueSetConceptDesignationDao.save(designation);

			TermValueSetConcept code0child0 = new TermValueSetConcept();
			code0child0.setSystem(CS_URL);
			code0child0.setCode("CODE0-0");
			code0child0.setDisplay("Code 0 child 0");
			code0child0.setValueSet(termValueSet);
			code0child0.setOrder(1);
			myTermValueSetConceptDao.save(code0child0);

			termValueSet.setTotalConcepts(2L);
			termValueSet.setTotalConceptDesignations(1L);
			myTermValueSetDao.save(termValueSet);
		});

		// Create a staging version with different codes
		String stagingVersion = mySvc.startStagingVersion(VS_URI, version);
		ValueSet toAdd = new ValueSet();
		toAdd.setUrl(VS_URI);
		toAdd.setVersion(stagingVersion);
		toAdd.getExpansion()
			.addContains()
			.setSystem(CS_URL)
			.setCode("CODE1")
			.setDisplay("Code 1")
			.addDesignation(new ValueSet.ConceptReferenceDesignationComponent().setLanguage("en").setValue("Code 1 en"));
		mySvc.addConceptsToExpansion(toAdd, 1000);

		// Test

		mySvc.activateStagingVersion(VS_URI, stagingVersion);

		// Verify

		runInTransaction(() -> {
			TermValueSet termValueSet;
			if (version == null) {
				termValueSet = myTermValueSetDao.findTermValueSetByUrlAndNullVersion(VS_URI).orElseThrow();
			} else {
				termValueSet = myTermValueSetDao.findTermValueSetByUrlAndVersion(VS_URI, version).orElseThrow();
			}
			String actual = renderExpansionHierarchy(termValueSet);
			String expected = """
				-System[http://system-0]
				 -CODE1 (Code 1) Order[1000]
				  -Designation[lang=en, value=Code 1 en]
				""";
			assertEquals(expected, actual);

			assertEquals(1, termValueSet.getTotalConcepts());
			assertEquals(1, termValueSet.getTotalConceptDesignations());
		});
		assertPartitionSet();
	}

	@Test
	void testActivateStagingVersion_NonStagingVersionPassed() {
		// Setup

		createValueSetResource("1.0");

		// Test

		assertThatThrownBy(()->mySvc.activateStagingVersion(VS_URI, "1.0"))
			.isInstanceOf(InvalidRequestException.class)
			.hasMessageContaining("ValueSet URL[http://vs] Version[1.0] is not a staging version");

	}

	@Test
	void testActivateStagingVersion_UnknownVersionPassed() {
		// Test

		assertThatThrownBy(()->mySvc.activateStagingVersion(VS_URI, "1.0"))
			.isInstanceOf(ResourceNotFoundException.class)
			.hasMessageContaining("No ValueSet found with URL[http://vs] and Version[1.0]");

	}

	@Test
	void testDropStagingVersion() {
		// Setup
		createValueSetResource("1.0");
		String stagingVersion = mySvc.startStagingVersion(VS_URI, "1.0");

		ValueSet toAdd = new ValueSet();
		toAdd.setUrl(VS_URI);
		toAdd.setVersion(stagingVersion);
		toAdd.getExpansion()
			.addContains()
			.setSystem(CS_URL)
			.setCode("CODE0")
			.setDisplay("Code 0")
			.addDesignation(new ValueSet.ConceptReferenceDesignationComponent().setLanguage("en").setValue("Code 0 en"));
		toAdd.getExpansion()
			.addContains()
			.setSystem(CS_URL)
			.setCode("CODE1")
			.setDisplay("Code 1");
		mySvc.addConceptsToExpansion(toAdd, 1000);

		// Test

		mySvc.dropStagingVersion(VS_URI, stagingVersion);

		// Verify

		runInTransaction(()->{
			assertEquals(1, myTermValueSetDao.count());
			assertEquals(0, myTermValueSetConceptDao.count());
		});
	}

	@Test
	void testDropStagingVersion_NonStagingVersionSpecified() {
		// Setup
		createValueSetResource("1.0");

		// Test

		assertThatThrownBy(()->mySvc.dropStagingVersion(VS_URI, "1.0"))
			.isInstanceOf(InvalidRequestException.class)
			.hasMessageContaining("Cannot drop staging version of ValueSet[url=http://vs, version=1.0] because it is not a staging version");

	}

	@Test
	void startStagingVersion_stagingVersionIdGiven_usesItAndReusesTheRowWhenCalledAgain() {
		// Setup
		createValueSetResource("1.0");

		// Test
		String firstCall = mySvc.startStagingVersion(VS_URI, "1.0", "job-instance-1");
		String secondCall = mySvc.startStagingVersion(VS_URI, "1.0", "job-instance-1");

		// Verify
		assertThat(firstCall).isEqualTo("job-instance-1");
		assertThat(secondCall).isEqualTo("job-instance-1");
		runInTransaction(() -> {
			assertThat(myTermValueSetDao.findAll())
				.extracting(TermValueSet::getVersion)
				.containsExactlyInAnyOrder("1.0", "job-instance-1");
			TermValueSet stagingValueSet = myTermValueSetDao.findTermValueSetByUrlAndVersion(VS_URI, "job-instance-1").orElseThrow();
			assertThat(stagingValueSet.getIntendedVersionId()).isEqualTo("1.0");
		});
	}

	@Test
	void markValueSetAsFailedToExpandIfStaged_stagingVersionExists_marksTheValueSet() {
		// Setup
		createValueSetResource("1.0");
		String stagingVersion = mySvc.startStagingVersion(VS_URI, "1.0", "job-instance-1");

		// Test
		boolean marked = mySvc.markValueSetAsFailedToExpandIfStaged(VS_URI, "1.0", stagingVersion, "the failure");

		// Verify
		assertThat(marked).isTrue();
		runInTransaction(() -> {
			TermValueSet valueSet = myTermValueSetDao.findTermValueSetByUrlAndVersion(VS_URI, "1.0").orElseThrow();
			assertThat(valueSet.getExpansionStatus()).isEqualTo(TermValueSetPreExpansionStatusEnum.FAILED_TO_EXPAND);
			assertThat(valueSet.getExpansionError()).isEqualTo("the failure");
		});
	}

	@Test
	void markValueSetAsFailedToExpandIfStaged_stagingVersionMissing_leavesTheValueSetAlone() {
		// Setup
		createValueSetResource("1.0");

		// Test
		boolean marked = mySvc.markValueSetAsFailedToExpandIfStaged(VS_URI, "1.0", "job-instance-1", "the failure");

		// Verify
		assertThat(marked).isFalse();
		runInTransaction(() -> {
			TermValueSet valueSet = myTermValueSetDao.findTermValueSetByUrlAndVersion(VS_URI, "1.0").orElseThrow();
			assertThat(valueSet.getExpansionStatus()).isEqualTo(TermValueSetPreExpansionStatusEnum.NOT_EXPANDED);
			assertThat(valueSet.getExpansionError()).isNull();
		});
	}

	@ParameterizedTest
	@EnumSource(value = TermValueSetPreExpansionStatusEnum.class, names = {"EXPANDED", "FAILED_TO_EXPAND"})
	void markValueSetAsFailedToExpandIfStaged_outcomeAlreadyRecorded_leavesTheValueSetAlone(TermValueSetPreExpansionStatusEnum theStatus) {
		// Setup
		createValueSetResource("1.0");
		String stagingVersion = mySvc.startStagingVersion(VS_URI, "1.0", "job-instance-1");
		runInTransaction(() -> {
			TermValueSet valueSet = myTermValueSetDao.findTermValueSetByUrlAndVersion(VS_URI, "1.0").orElseThrow();
			valueSet.setExpansionStatus(theStatus);
			valueSet.setExpansionError("recorded by another job");
			myTermValueSetDao.save(valueSet);
		});

		// Test
		boolean marked = mySvc.markValueSetAsFailedToExpandIfStaged(VS_URI, "1.0", stagingVersion, "the failure");

		// Verify
		assertThat(marked).isFalse();
		runInTransaction(() -> {
			TermValueSet valueSet = myTermValueSetDao.findTermValueSetByUrlAndVersion(VS_URI, "1.0").orElseThrow();
			assertThat(valueSet.getExpansionStatus()).isEqualTo(theStatus);
			assertThat(valueSet.getExpansionError()).isEqualTo("recorded by another job");
		});
	}

	/**
	 * Activation renames the staging row to the real version and keeps its intended version, so the live row
	 * of an expanded ValueSet has an intended version too. It is still not a staging version.
	 */
	@ParameterizedTest
	@ValueSource(strings = {"1.0", ""})
	void dropStagingVersion_liveRowOfExpandedValueSet_isRejected(String theVersion) {
		// Setup
		String version = defaultIfBlank(theVersion, null);
		createValueSetResource(version);
		String stagingVersion = mySvc.startStagingVersion(VS_URI, version);
		addConceptToStagingVersion(stagingVersion, "CODE0");
		mySvc.activateStagingVersion(VS_URI, stagingVersion);

		// Test
		assertThatThrownBy(() -> mySvc.dropStagingVersion(VS_URI, version))
			.isInstanceOf(InvalidRequestException.class)
			.hasMessageContaining("because it is not a staging version");
		if (version != null) {
			assertThatThrownBy(() -> mySvc.dropStagingVersionIfExists(VS_URI, version))
				.isInstanceOf(InvalidRequestException.class)
				.hasMessageContaining("because it is not a staging version");
		} else {
			// a blank staging version ID never addresses the live row of a ValueSet without a version
			assertThat(mySvc.dropStagingVersionIfExists(VS_URI, "")).isFalse();
		}

		// Verify
		runInTransaction(() -> {
			assertThat(myTermValueSetDao.findAll()).extracting(TermValueSet::getVersion).containsExactly(version);
			assertThat(myTermValueSetConceptDao.findAll()).extracting(TermValueSetConcept::getCode).containsExactly("CODE0");
		});
	}

	@Test
	void dropStagingVersionIfExists_stagingVersionAlreadyGone_returnsFalse() {
		// Setup
		createValueSetResource("1.0");
		String stagingVersion = mySvc.startStagingVersion(VS_URI, "1.0");

		// Test
		boolean firstCall = mySvc.dropStagingVersionIfExists(VS_URI, stagingVersion);
		boolean secondCall = mySvc.dropStagingVersionIfExists(VS_URI, stagingVersion);

		// Verify
		assertThat(firstCall).isTrue();
		assertThat(secondCall).isFalse();
		runInTransaction(() ->
			assertThat(myTermValueSetDao.findAll()).extracting(TermValueSet::getVersion).containsExactly("1.0"));
	}

	/**
	 * A job that is cancelled while its last step runs can still activate its staging version. The cleanup
	 * that the cancellation starts must then leave the row alone, because it has become the live one.
	 */
	@Test
	void dropStagingVersionIfExists_stagingVersionActivatedMeanwhile_leavesTheLiveRowAlone() throws Exception {
		// Setup
		createValueSetResource("1.0");
		String stagingVersion = mySvc.startStagingVersion(VS_URI, "1.0");
		addConceptToStagingVersion(stagingVersion, "CODE0");

		CountDownLatch commitActivation = new CountDownLatch(1);
		ExecutorService executor = Executors.newFixedThreadPool(2);
		try {
			Future<?> activation = startActivationAndHoldItsTransaction(executor, stagingVersion, commitActivation);

			// Test
			Future<Boolean> drop = executor.submit(() -> mySvc.dropStagingVersionIfExists(VS_URI, stagingVersion));
			awaitACallWaitingForTheOpenTransaction();
			commitActivation.countDown();
			activation.get(30, TimeUnit.SECONDS);

			// Verify
			assertThat(drop.get(30, TimeUnit.SECONDS)).isFalse();
			runInTransaction(() -> {
				assertThat(myTermValueSetDao.findAll()).extracting(TermValueSet::getVersion).containsExactlyInAnyOrder("1.0", FORMER_VERSION);
				assertThat(myTermValueSetConceptDao.findAll()).extracting(TermValueSetConcept::getCode).containsExactly("CODE0");
			});
		} finally {
			commitActivation.countDown();
			executor.shutdownNow();
		}
	}

	/**
	 * The error handler of a cancelled job can run while the last step of that job still activates its staging
	 * version. Marking the ValueSet as failed must then not undo the renaming that the activation did.
	 */
	@Test
	void markValueSetAsFailedToExpandIfStaged_stagingVersionActivatedMeanwhile_keepsTheActivation() throws Exception {
		// Setup
		createValueSetResource("1.0");
		String stagingVersion = mySvc.startStagingVersion(VS_URI, "1.0", "job-instance-1");

		CountDownLatch commitActivation = new CountDownLatch(1);
		ExecutorService executor = Executors.newFixedThreadPool(2);
		try {
			Future<?> activation = startActivationAndHoldItsTransaction(executor, stagingVersion, commitActivation);

			// Test
			Future<Boolean> marking = executor.submit(() ->
				mySvc.markValueSetAsFailedToExpandIfStaged(VS_URI, "1.0", stagingVersion, "the failure"));
			awaitACallWaitingForTheOpenTransaction();
			commitActivation.countDown();
			activation.get(30, TimeUnit.SECONDS);

			// Verify
			assertThat(marking.get(30, TimeUnit.SECONDS)).isFalse();
			runInTransaction(() -> {
				assertThat(myTermValueSetDao.findAll()).extracting(TermValueSet::getVersion).containsExactlyInAnyOrder("1.0", FORMER_VERSION);
				TermValueSet valueSet = myTermValueSetDao.findTermValueSetByUrlAndVersion(VS_URI, "1.0").orElseThrow();
				assertThat(valueSet.getExpansionStatus()).isEqualTo(TermValueSetPreExpansionStatusEnum.EXPANDED);
				assertThat(valueSet.getExpansionError()).isNull();
			});
		} finally {
			commitActivation.countDown();
			executor.shutdownNow();
		}
	}

	@ParameterizedTest
	@ValueSource(strings = {"1.0", ""})
	void startCleanupOfAbandonedStagingVersions_expandedValueSetWithAbandonedStagingVersion_dropsOnlyTheStagingVersion(String theVersion) {
		// Setup - an expanded ValueSet, plus a staging version that no job instance owns
		disablePreExpansionOnStore();
		String version = defaultIfBlank(theVersion, null);
		createValueSetResource(version);
		String activatedStagingVersion = mySvc.startStagingVersion(VS_URI, version);
		addConceptToStagingVersion(activatedStagingVersion, "CODE0");
		mySvc.activateStagingVersion(VS_URI, activatedStagingVersion);
		String abandonedStagingVersion = mySvc.startStagingVersion(VS_URI, version);
		addConceptToStagingVersion(abandonedStagingVersion, "CODE1");

		// Test
		mySvc.startCleanupOfAbandonedStagingVersions(new SystemRequestDetails(), VS_URI, version, null);
		myBatch2JobHelper.awaitNoJobsRunning();

		// Verify
		assertThat(myBatch2JobHelper.findJobsByDefinition(JOB_ID_PRE_EXPAND_VALUESET_CLEANUP))
			.extracting(JobInstance::getStatus)
			.containsExactly(StatusEnum.COMPLETED);
		runInTransaction(() -> {
			assertThat(myTermValueSetDao.findAll()).extracting(TermValueSet::getVersion).containsExactly(version);
			assertThat(myTermValueSetConceptDao.findAll()).extracting(TermValueSetConcept::getCode).containsExactly("CODE0");
		});
	}

	@Test
	void startCleanupOfAbandonedStagingVersions_noStagingVersion_startsNoJob() {
		// Setup
		disablePreExpansionOnStore();
		createValueSetResource("1.0");

		// Test
		mySvc.startCleanupOfAbandonedStagingVersions(new SystemRequestDetails(), VS_URI, "1.0", null);

		// Verify
		assertThat(myBatch2JobHelper.findJobsByDefinition(JOB_ID_PRE_EXPAND_VALUESET_CLEANUP)).isEmpty();
	}

	/**
	 * A staging version that matches no job instance may have been staged by a job that ran before staging
	 * versions were named after their job. While such a job may still be running, the staging version is
	 * not dropped under it.
	 */
	@Test
	void startCleanupOfAbandonedStagingVersions_stagingVersionOfNoJobWhileAnotherJobRuns_isLeftAlone() {
		// Setup - storing the ValueSet queues a pre-expansion job, which never runs in this test
		createValueSetResource("1.0");
		String queuedJobInstanceId = findTheQueuedPreExpansionJobInstanceId();
		String stagingVersionOfNoJob = mySvc.startStagingVersion(VS_URI, "1.0");

		// Test - asked by something that is not that job
		mySvc.startCleanupOfAbandonedStagingVersions(new SystemRequestDetails(), VS_URI, "1.0", null);

		// Verify
		assertThat(myBatch2JobHelper.findJobsByDefinition(JOB_ID_PRE_EXPAND_VALUESET_CLEANUP)).isEmpty();

		// Test - asked by that job itself, so no other job is running
		mySvc.startCleanupOfAbandonedStagingVersions(new SystemRequestDetails(), VS_URI, "1.0", queuedJobInstanceId);

		// Verify
		assertThat(findStagingVersionIdsOfCleanupJobs()).containsExactly(List.of(stagingVersionOfNoJob));
	}

	@Test
	void startCleanupOfAbandonedStagingVersions_stagingVersionOfARunningJob_isLeftAlone() {
		// Setup - storing the ValueSet queues a pre-expansion job, which never runs in this test
		createValueSetResource("1.0");
		String queuedJobInstanceId = findTheQueuedPreExpansionJobInstanceId();
		mySvc.startStagingVersion(VS_URI, "1.0", queuedJobInstanceId);

		// Test
		mySvc.startCleanupOfAbandonedStagingVersions(new SystemRequestDetails(), VS_URI, "1.0", null);

		// Verify
		assertThat(myBatch2JobHelper.findJobsByDefinition(JOB_ID_PRE_EXPAND_VALUESET_CLEANUP)).isEmpty();
	}

	/**
	 * A work chunk of a job that has ended can still be writing to the staging version of that job. The drop
	 * then fails, and has to be tried again once the chunk has finished.
	 */
	@Test
	void cleanupJob_stagingVersionLockedByAWriter_isTriedAgainLaterAndThenDropsTheStagingVersion() throws Exception {
		// Setup
		disablePreExpansionOnStore();
		createValueSetResource("1.0");
		String stagingVersion = mySvc.startStagingVersion(VS_URI, "1.0");
		addConceptToStagingVersion(stagingVersion, "CODE0");

		CountDownLatch stagingVersionLocked = new CountDownLatch(1);
		CountDownLatch releaseStagingVersion = new CountDownLatch(1);
		ExecutorService writerExecutor = Executors.newSingleThreadExecutor();
		try {
			Future<?> writer = writerExecutor.submit(() -> runInTransaction(() -> {
				TermValueSet stagingValueSet = myTermValueSetDao.findTermValueSetByUrlAndVersion(VS_URI, stagingVersion).orElseThrow();
				myEntityManager.lock(stagingValueSet, LockModeType.PESSIMISTIC_WRITE);
				stagingVersionLocked.countDown();
				awaitUninterruptibly(releaseStagingVersion);
			}));
			assertThat(stagingVersionLocked.await(30, TimeUnit.SECONDS)).isTrue();

			// Test
			mySvc.startCleanupOfAbandonedStagingVersions(new SystemRequestDetails(), VS_URI, "1.0", null);

			// Verify - the drop failed, and waits to be tried again with the staging version untouched
			await().atMost(Batch2JobHelper.DEFAULT_WAIT_DURATION).until(() -> {
				myBatch2JobHelper.forceRunActiveJobMaintenancePass();
				return findWorkChunkStatusesOfCleanupJobs().contains(WorkChunkStatusEnum.POLL_WAITING);
			});
			runInTransaction(() -> {
				assertThat(myTermValueSetDao.findTermValueSetByUrlAndVersion(VS_URI, stagingVersion)).isPresent();
				assertThat(myTermValueSetConceptDao.findAll()).extracting(TermValueSetConcept::getCode).containsExactly("CODE0");
			});

			// Test - the writer finishes
			releaseStagingVersion.countDown();
			writer.get(30, TimeUnit.SECONDS);
			myBatch2JobHelper.awaitNoJobsRunning();

			// Verify
			assertThat(myBatch2JobHelper.findJobsByDefinition(JOB_ID_PRE_EXPAND_VALUESET_CLEANUP))
				.extracting(JobInstance::getStatus)
				.containsExactly(StatusEnum.COMPLETED);
			runInTransaction(() -> {
				assertThat(myTermValueSetDao.findAll()).extracting(TermValueSet::getVersion).containsExactly("1.0");
				assertThat(myTermValueSetConceptDao.count()).isZero();
			});
		} finally {
			releaseStagingVersion.countDown();
			writerExecutor.shutdownNow();
		}
	}

	private static void awaitUninterruptibly(CountDownLatch theLatch) {
		try {
			if (!theLatch.await(60, TimeUnit.SECONDS)) {
				throw new IllegalStateException("The latch was not released");
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new IllegalStateException(e);
		}
	}

	/**
	 * Does to the live row and the staging row of version {@code 1.0} what the first transaction of
	 * {@code activateStagingVersion} does, and keeps that transaction open until the given latch is released.
	 * Returns once both rows are written and not yet committed.
	 */
	private Future<?> startActivationAndHoldItsTransaction(
			ExecutorService theExecutor, String theStagingVersion, CountDownLatch theCommit) throws InterruptedException {
		CountDownLatch rowsWritten = new CountDownLatch(1);
		Future<?> activation = theExecutor.submit(() -> runInTransaction(() -> {
			TermValueSet liveValueSet = myTermValueSetDao.findTermValueSetByUrlAndVersion(VS_URI, "1.0").orElseThrow();
			liveValueSet.setVersion(FORMER_VERSION);
			myTermValueSetDao.saveAndFlush(liveValueSet);

			TermValueSet stagingValueSet = myTermValueSetDao.findTermValueSetByUrlAndVersion(VS_URI, theStagingVersion).orElseThrow();
			stagingValueSet.setExpansionStatus(TermValueSetPreExpansionStatusEnum.EXPANDED);
			stagingValueSet.setVersion("1.0");
			myTermValueSetDao.saveAndFlush(stagingValueSet);

			rowsWritten.countDown();
			awaitUninterruptibly(theCommit);
		}));
		assertThat(rowsWritten.await(30, TimeUnit.SECONDS)).isTrue();
		return activation;
	}

	/**
	 * Waits until a call on another thread waits for a row that an open transaction has written. H2 tells
	 * when that is: a session that waits for a lock names the session that holds it.
	 * Dev note: this is a little low-level(ish) but the considered alternative (waiting for TIMED_WAITING state
	 * was not precise, because wait could be for other reasons (i.e. a pooled connection).
	 */
	private void awaitACallWaitingForTheOpenTransaction() {
		await().atMost(10, TimeUnit.SECONDS).pollInterval(20, TimeUnit.MILLISECONDS).until(() -> runInTransaction(() -> {
			Number waitingSessions = (Number) myEntityManager
				.createNativeQuery("SELECT COUNT(*) FROM INFORMATION_SCHEMA.SESSIONS WHERE BLOCKER_ID IS NOT NULL")
				.getSingleResult();
			return waitingSessions.intValue() > 0;
		}));
	}

	private String findTheQueuedPreExpansionJobInstanceId() {
		List<JobInstance> jobInstances = myBatch2JobHelper.findJobsByDefinition(JOB_ID_PRE_EXPAND_VALUESET);
		assertThat(jobInstances).hasSize(1);
		assertThat(jobInstances.get(0).getStatus()).isEqualTo(StatusEnum.QUEUED);
		return jobInstances.get(0).getInstanceId();
	}

	private List<List<String>> findStagingVersionIdsOfCleanupJobs() {
		return myBatch2JobHelper.findJobsByDefinition(JOB_ID_PRE_EXPAND_VALUESET_CLEANUP).stream()
			.map(jobInstance -> jobInstance.getParameters(PreExpandValueSetCleanupParameters.class).getStagingVersionIds())
			.toList();
	}

	private List<WorkChunkStatusEnum> findWorkChunkStatusesOfCleanupJobs() {
		List<String> jobInstanceIds = myBatch2JobHelper.findJobsByDefinition(JOB_ID_PRE_EXPAND_VALUESET_CLEANUP).stream()
			.map(JobInstance::getInstanceId)
			.toList();
		return runInTransaction(() -> myWorkChunkRepository.findAll().stream()
			.filter(chunk -> jobInstanceIds.contains(chunk.getInstanceId()))
			.map(Batch2WorkChunkEntity::getStatus)
			.toList());
	}

	/**
	 * Storing a ValueSet queues a pre-expansion job. A test that lets batch jobs run does not want that job
	 * to expand the ValueSet under it.
	 */
	private void disablePreExpansionOnStore() {
		myStorageSettings.setEnableTaskPreExpandValueSets(false);
	}

	@AfterEach
	void afterRestorePreExpansionOnStore() {
		myStorageSettings.setEnableTaskPreExpandValueSets(new JpaStorageSettings().isEnableTaskPreExpandValueSets());
	}

	private void addConceptToStagingVersion(String theStagingVersion, String theCode) {
		ValueSet toAdd = new ValueSet();
		toAdd.setUrl(VS_URI);
		toAdd.setVersion(theStagingVersion);
		toAdd.getExpansion().addContains().setSystem(CS_URL).setCode(theCode).setDisplay(theCode);
		mySvc.addConceptsToExpansion(toAdd, myNextOrder++);
	}

	private void assertPartitionSet() {
		runInTransaction(() -> {
			for (TermValueSet valueSet : myTermValueSetDao.findAll()) {
				assertEquals(-1, valueSet.getPartitionId().getPartitionId());
			}
			for (TermValueSetConcept valueSet : myTermValueSetConceptDao.findAll()) {
				assertEquals(-1, valueSet.getPartitionId().getPartitionId());
			}
			for (TermValueSetConceptDesignation valueSet : myTermValueSetConceptDesignationDao.findAll()) {
				assertEquals(-1, valueSet.getPartitionId().getPartitionId());
			}
		});
	}

	private void createTermValueSetConcept(String theStagingVersion, String theCode, String theDisplay) {
		createTermValueSetConcept(theStagingVersion, theCode, theDisplay, null);
	}

	private void createTermValueSetConcept(String theStagingVersion, String theCode, String theDisplay, String theSystemVersion) {
		createTermValueSetConceptAndDesignation(theStagingVersion, theCode, theDisplay, theSystemVersion, null, null, null);
	}

	private TermValueSetConcept createTermValueSetConceptAndDesignation(String theStagingVersion, String theCode, String theDisplay, String theSystemVersion, String theDesignationLang, String theDesignationValue, TermValueSetConcept theParent) {
		return runInTransaction(() -> {
			TermValueSet termValueSet = myTermValueSetDao.findTermValueSetByUrlAndVersion(VS_URI, theStagingVersion).orElseThrow();

			TermValueSetConcept concept = new TermValueSetConcept();
			concept.setSystem(CS_URL);
			concept.setSystemVersion(theSystemVersion);
			concept.setCode(theCode);
			concept.setDisplay(theDisplay);
			concept.setValueSet(termValueSet);
			concept.setOrder(myNextOrder++);

			concept = myTermValueSetConceptDao.save(concept);

			if (theDesignationLang != null) {
				TermValueSetConceptDesignation designation = new TermValueSetConceptDesignation();
				designation.setLanguage(theDesignationLang);
				designation.setValue(theDesignationValue);
				designation.setConcept(concept);
				designation.setValueSet(termValueSet);
				concept.getDesignations().add(designation);
				myTermValueSetConceptDesignationDao.save(designation);

				termValueSet.setTotalConceptDesignations(getIfNull(termValueSet.getTotalConceptDesignations(), 0L) + 1);
			}

			termValueSet.setTotalConcepts(getIfNull(termValueSet.getTotalConcepts(), 0L) + 1);
			myTermValueSetDao.save(termValueSet);

			return concept;
		});
	}


	private void createValueSetResource(String version) {
		org.hl7.fhir.r5.model.ValueSet vs = new org.hl7.fhir.r5.model.ValueSet();
		vs.setId(VS_ID);
		vs.setUrl(VS_URI);
		vs.setVersion(version);
		vs.setName(THE_VALUE_SET_NAME);
		myValueSetDao.update(vs, newSrd());
	}

	String renderExpansionHierarchy(TermValueSet theValueSet) {
		HapiTransactionService.requireTransaction();

		StringBuilder target = new StringBuilder();
		List<TermValueSetConcept> rootConcepts = theValueSet
			.getConcepts()
			.stream()
			.toList();

		appendToHierarchy(rootConcepts, target);
		return target.toString();
	}

	private void appendToHierarchy(List<TermValueSetConcept> theConceptList, StringBuilder theTarget) {

		ImmutableListMultimap<UrlUtil.CanonicalUrlParts, TermValueSetConcept> systemToConcepts = Multimaps.index(theConceptList, c -> new UrlUtil.CanonicalUrlParts(c.getSystem(), c.getSystemVersion()));
		for (Map.Entry<UrlUtil.CanonicalUrlParts, Collection<TermValueSetConcept>> entry : systemToConcepts.asMap().entrySet()) {
			UrlUtil.CanonicalUrlParts system = entry.getKey();
			Collection<TermValueSetConcept> concepts = entry.getValue();
			theTarget.append("-System[");
			theTarget.append(system.toString()).append("]\n");
			appendConceptsToHierarchy(0, theTarget, concepts);
		}
	}

	private static void appendConceptsToHierarchy(int theDepth, StringBuilder theTarget, Collection<TermValueSetConcept> concepts) {
		for (TermValueSetConcept concept : concepts) {
			theTarget.append("  ".repeat(theDepth));
			theTarget.append(" -");
			theTarget.append(concept.getCode());
			if (isNotBlank(concept.getDisplay())) {
				theTarget.append(" (").append(concept.getDisplay()).append(")");
			}
			theTarget.append(" Order[").append(concept.getOrder()).append("]");
			theTarget.append("\n");

			for (TermValueSetConceptDesignation designation : concept.getDesignations()) {
				theTarget.append("  ".repeat(theDepth));
				theTarget.append("  -Designation[lang=");
				theTarget.append(designation.getLanguage());
				theTarget.append(", value=");
				theTarget.append(designation.getValue());
				theTarget.append("]\n");
			}

		}
	}

}
