package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemVersionDao;
import ca.uhn.fhir.jpa.dao.r5.BaseJpaR5Test;
import ca.uhn.fhir.jpa.entity.TermCodeSystem;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermDeferredStorageSvc;
import ca.uhn.fhir.jpa.test.Batch2JobHelper;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.hl7.fhir.common.hapi.validation.util.TermConceptPropertyTypeEnum;
import org.hl7.fhir.r5.model.CodeSystem;
import org.hl7.fhir.r5.model.Coding;
import org.hl7.fhir.r5.model.Enumerations;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static ca.uhn.fhir.batch2.jobs.termcodesystem.TermCodeSystemJobConfig.TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME;
import static ca.uhn.fhir.test.utilities.UuidUtils.UUID_PATTERN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.fail;

public class TermCodeSystemStorageSvcImplTest extends BaseJpaR5Test {

	public static final String URL_MY_CODE_SYSTEM = "http://example.com/my_code_system";

	@Autowired
	private Batch2JobHelper myBatchJobHelper;
	@Autowired
	private ITermDeferredStorageSvc myTerminologyDeferredStorageSvc;
	@Autowired
	private ITermCodeSystemStorageSvc mySvc;
	@Autowired
	private ITermCodeSystemVersionDao myCodeSystemVersionDao;

	@CsvSource(textBlock = """
		# VersionToStage , MakeCurrent
		A                , true
		A                , false
		B                , true
		B                , false
		""")
	void activateStagingCodeSystemVersion(String theVersionToStage) {
		createCodeSystem(withUrl("http://foo"), withVersion("A"), withCodeSystemContent("not-present"));
		runInTransaction(()->{
			TermCodeSystemVersion csv = myCodeSystemVersionDao.findByCodeSystemUriAndVersion("http://foo", "A");
			assertNotNull(csv);
			assertSame(csv, csv.getCodeSystem().getCurrentVersion());
		});

		String stagingVersionId = mySvc.startStagingCodeSystemVersion("http://foo", theVersionToStage).stagingVersionId();
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl("http://foo");
		codeSystem.setVersion(stagingVersionId);
		codeSystem.addConcept().setCode("CODE-A").setDisplay("Display-A");
		mySvc.uploadCodeSystemConcepts(codeSystem);

		// Test
		mySvc.activateStagingCodeSystemVersion("http://foo", stagingVersionId, true);

		// Verify
		runInTransaction(()->{
			TermCodeSystemVersion csv = myCodeSystemVersionDao.findByCodeSystemUriAndVersion("http://foo", "B");
			assertNotNull(csv);
			assertThat(csv.getConcepts()).hasSize(1);
			assertSame(csv, csv.getCodeSystem().getCurrentVersion());
		});
	}


	@Test
	void testStoreNewCodeSystemVersionForExistingCodeSystemNoVersionId() {
		CodeSystem firstUpload = createCodeSystemWithMoreThan100Concepts();
		CodeSystem duplicateUpload = createCodeSystemWithMoreThan100Concepts();

		testCreatingAndUpdatingCodeSystemEntity(firstUpload, duplicateUpload, 125, Msg.code(848) + "Can not create multiple CodeSystem resources with CodeSystem.url \"http://example.com/my_code_system\", already have one with resource ID: CodeSystem/");

		runInTransaction(() -> {
			assertEquals(1, myTermCodeSystemDao.count());
			assertEquals(1, myTermCodeSystemVersionDao.count());
			TermCodeSystem myTermCodeSystem = myTermCodeSystemDao.findByCodeSystemUri(URL_MY_CODE_SYSTEM);

			TermCodeSystemVersion myTermCodeSystemVersion = myTermCodeSystemVersionDao.findByCodeSystemPidVersionIsNull(myTermCodeSystem.getPid());
			assertEquals(myTermCodeSystem.getCurrentVersion().getPid(), myTermCodeSystemVersion.getPid());
			assertEquals(myTermCodeSystem.getResource().getId(), myTermCodeSystemVersion.getResource().getId());
		});
	}


	@Test
	void testStoreNewCodeSystemVersionForExistingCodeSystemVersionId() {
		CodeSystem firstUpload = createCodeSystemWithMoreThan100Concepts();
		firstUpload.setVersion("1");

		CodeSystem duplicateUpload = createCodeSystemWithMoreThan100Concepts();
		duplicateUpload.setVersion("1");

		testCreatingAndUpdatingCodeSystemEntity(firstUpload, duplicateUpload, 125, Msg.code(848) + "Can not create multiple CodeSystem resources with CodeSystem.url \"http://example.com/my_code_system\" and CodeSystem.version \"1\", already have one with resource ID: CodeSystem/");

		runInTransaction(() -> {
			assertEquals(1, myTermCodeSystemDao.count());
			assertEquals(1, myTermCodeSystemVersionDao.count());
			TermCodeSystem myTermCodeSystem = myTermCodeSystemDao.findByCodeSystemUri(URL_MY_CODE_SYSTEM);

			TermCodeSystemVersion myTermCodeSystemVersion = myTermCodeSystemVersionDao.findByCodeSystemPidAndVersion(myTermCodeSystem.getPid(), "1");
			assertEquals(myTermCodeSystem.getCurrentVersion().getPid(), myTermCodeSystemVersion.getPid());
			assertEquals(myTermCodeSystem.getResource().getId(), myTermCodeSystemVersion.getResource().getId());
		});

		// Now add a second version
		firstUpload = createCodeSystemWithMoreThan100Concepts();
		firstUpload.setVersion("2");

		duplicateUpload = createCodeSystemWithMoreThan100Concepts();
		duplicateUpload.setVersion("2");

		testCreatingAndUpdatingCodeSystemEntity(firstUpload, duplicateUpload, 251, Msg.code(848) + "Can not create multiple CodeSystem resources with CodeSystem.url \"http://example.com/my_code_system\" and CodeSystem.version \"2\", already have one with resource ID: CodeSystem/");

		runInTransaction(() -> {
			assertEquals(1, myTermCodeSystemDao.count());
			assertEquals(2, myTermCodeSystemVersionDao.count());
			TermCodeSystem myTermCodeSystem = myTermCodeSystemDao.findByCodeSystemUri(URL_MY_CODE_SYSTEM);

			TermCodeSystemVersion mySecondTermCodeSystemVersion = myTermCodeSystemVersionDao.findByCodeSystemPidAndVersion(myTermCodeSystem.getPid(), "2");
			assertEquals(myTermCodeSystem.getCurrentVersion().getPid(), mySecondTermCodeSystemVersion.getPid());
			assertEquals(myTermCodeSystem.getResource().getId(), mySecondTermCodeSystemVersion.getResource().getId());
		});

	}

	// Created by claude-opus-4-6
	@Test
	void storeNewCodeSystemVersionIfNeeded_notPresentPlaceholderWithDifferentResource_shouldUpdatePlaceholder() {
		// Reproduces SMILE-7421: When a NOTPRESENT CodeSystem already exists (e.g. from package
		// pre-seeding), creating a second FHIR resource for the same CodeSystem URL should succeed
		// because the existing TermCodeSystemVersion is a 0-concept placeholder.
		//
		// The real-world scenario: Package pre-seeding creates SNOMED from hl7.terminology.r4
		// (creating Resource A). Later, either a second pre-seed run or $upload-external-code-system
		// creates a different FHIR resource (Resource B) for the same URL. The NOTPRESENT
		// early-return path triggers a version duplicate check. The existing version (0 concepts,
		// points to Resource A) should be re-pointable to Resource B, but instead a
		// version-already-exists error (Msg.code(848)) is thrown.

		// Step 1: Create first NOTPRESENT CodeSystem (simulates package pre-seed install)
		CodeSystem cs1 = new CodeSystem();
		cs1.setUrl("http://snomed.info/sct");
		cs1.setContent(Enumerations.CodeSystemContentMode.NOTPRESENT);
		cs1.setStatus(Enumerations.PublicationStatus.ACTIVE);
		myCodeSystemDao.create(cs1, mySrd);

		myTerminologyDeferredStorageSvc.saveAllDeferred();

		// Verify: TermCodeSystem and TermCodeSystemVersion exist, with 0 concepts
		runInTransaction(() -> {
			TermCodeSystem tcs = myTermCodeSystemDao.findByCodeSystemUri("http://snomed.info/sct");
			assertThat(tcs).isNotNull();
			assertThat(tcs.getCurrentVersion()).isNotNull();
			long conceptCount = myTermConceptDao.countByCodeSystemVersion(tcs.getCurrentVersion().getPid());
			assertThat(conceptCount).isEqualTo(0);
		});

		// Step 2: Create a second NOTPRESENT CodeSystem with the same URL.
		// This simulates the scenario where a new FHIR resource is created for the same
		// CodeSystem URL (e.g. a subsequent pre-seed run when the conditional search fails
		// to find the existing resource, or $upload-external-code-system creating a new resource).
		CodeSystem cs2 = new CodeSystem();
		cs2.setUrl("http://snomed.info/sct");
		cs2.setContent(Enumerations.CodeSystemContentMode.NOTPRESENT);
		cs2.setStatus(Enumerations.PublicationStatus.ACTIVE);

		// This should succeed — the existing TermCodeSystemVersion is a 0-concept placeholder
		// from the first pre-seed. It should be updated to point to the new resource.
		// Previously threw UnprocessableEntityException (Msg.code(848)) — fixed by SMILE-7421.
		JpaPid secondResourcePid = ((ResourceTable) myCodeSystemDao.create(cs2, mySrd).getEntity()).getId();

		// Verify: TermCodeSystem and TermCodeSystemVersion both point to the second resource
		runInTransaction(() -> {
			TermCodeSystem tcs = myTermCodeSystemDao.findByCodeSystemUri("http://snomed.info/sct");
			assertThat(tcs).isNotNull();
			assertThat(tcs.getResource().getId()).isEqualTo(secondResourcePid);
			assertThat(tcs.getCurrentVersion()).isNotNull();
			assertThat(tcs.getCurrentVersion().getResource().getId()).isEqualTo(secondResourcePid);
		});
	}

	// Created by claude-opus-4-6
	@Test
	void storeNewCodeSystemVersionIfNeeded_versionedNotPresentPlaceholderWithDifferentResource_shouldUpdatePlaceholder() {
		// Same as the unversioned case above, but with an explicit version string.
		// The NOTPRESENT early-return path should also allow re-pointing when the existing
		// TermCodeSystemVersion has a version ID and zero concepts.

		// Step 1: Create first versioned NOTPRESENT CodeSystem
		CodeSystem cs1 = new CodeSystem();
		cs1.setUrl("http://snomed.info/sct");
		cs1.setVersion("5.0.0");
		cs1.setContent(Enumerations.CodeSystemContentMode.NOTPRESENT);
		cs1.setStatus(Enumerations.PublicationStatus.ACTIVE);
		myCodeSystemDao.create(cs1, mySrd);

		myTerminologyDeferredStorageSvc.saveAllDeferred();

		// Verify: TermCodeSystemVersion exists with version "5.0.0" and 0 concepts
		runInTransaction(() -> {
			TermCodeSystem tcs = myTermCodeSystemDao.findByCodeSystemUri("http://snomed.info/sct");
			assertThat(tcs).isNotNull();
			TermCodeSystemVersion version = myTermCodeSystemVersionDao.findByCodeSystemPidAndVersion(tcs.getPid(), "5.0.0");
			assertThat(version).isNotNull();
			long conceptCount = myTermConceptDao.countByCodeSystemVersion(version.getPid());
			assertThat(conceptCount).isEqualTo(0);
		});

		// Step 2: Create second versioned NOTPRESENT CodeSystem with same URL and version
		CodeSystem cs2 = new CodeSystem();
		cs2.setUrl("http://snomed.info/sct");
		cs2.setVersion("5.0.0");
		cs2.setContent(Enumerations.CodeSystemContentMode.NOTPRESENT);
		cs2.setStatus(Enumerations.PublicationStatus.ACTIVE);

		// Previously threw Msg.code(848) — fixed by SMILE-7421.
		JpaPid secondResourcePid = ((ResourceTable) myCodeSystemDao.create(cs2, mySrd).getEntity()).getId();

		// Verify: TermCodeSystem and TermCodeSystemVersion both point to the second resource
		runInTransaction(() -> {
			TermCodeSystem tcs = myTermCodeSystemDao.findByCodeSystemUri("http://snomed.info/sct");
			assertThat(tcs).isNotNull();
			assertThat(tcs.getResource().getId()).isEqualTo(secondResourcePid);
			assertThat(tcs.getCurrentVersion()).isNotNull();
			assertThat(tcs.getCurrentVersion().getResource().getId()).isEqualTo(secondResourcePid);
		});
	}

	// Created by claude-opus-4-6
	@Test
	void storeNewCodeSystemVersionIfNeeded_notPresentDifferentVersions_shouldCreateSeparateVersions() {
		// When a versioned NOTPRESENT CodeSystem already exists and a second NOTPRESENT
		// CodeSystem with a DIFFERENT version (or null) is created, they occupy different
		// version slots. The NOTPRESENT early-return path should not find the existing
		// version (version mismatch), so it falls through to the normal creation path.
		// This should succeed on current master — no bug in this path.

		// Step 1: Create first NOTPRESENT CodeSystem with version "5.0.0"
		CodeSystem cs1 = new CodeSystem();
		cs1.setUrl("http://snomed.info/sct");
		cs1.setVersion("5.0.0");
		cs1.setContent(Enumerations.CodeSystemContentMode.NOTPRESENT);
		cs1.setStatus(Enumerations.PublicationStatus.ACTIVE);
		myCodeSystemDao.create(cs1, mySrd);

		myTerminologyDeferredStorageSvc.saveAllDeferred();

		// Step 2: Create second NOTPRESENT CodeSystem with no version (null)
		CodeSystem cs2 = new CodeSystem();
		cs2.setUrl("http://snomed.info/sct");
		cs2.setContent(Enumerations.CodeSystemContentMode.NOTPRESENT);
		cs2.setStatus(Enumerations.PublicationStatus.ACTIVE);
		myCodeSystemDao.create(cs2, mySrd);

		myTerminologyDeferredStorageSvc.saveAllDeferred();

		// Verify: Two TermCodeSystemVersion entries exist — one for "5.0.0", one for null
		runInTransaction(() -> {
			TermCodeSystem tcs = myTermCodeSystemDao.findByCodeSystemUri("http://snomed.info/sct");
			assertThat(tcs).isNotNull();
			assertThat(myTermCodeSystemVersionDao.findByCodeSystemPidAndVersion(tcs.getPid(), "5.0.0")).isNotNull();
			assertThat(myTermCodeSystemVersionDao.findByCodeSystemPidVersionIsNull(tcs.getPid())).isNotNull();
			assertThat(myTermCodeSystemVersionDao.count()).isEqualTo(2);
		});
	}

	// Created by claude-opus-4-6
	@Test
	void storeNewCodeSystemVersionIfNeeded_completeCodeSystemThenNotPresentDifferentResource_shouldRejectDuplicate() {
		// Guard rail: When a COMPLETE CodeSystem with real concepts already exists,
		// creating a second NOTPRESENT CodeSystem with a different FHIR resource for the
		// same URL should STILL throw Msg.code(848). The fix for SMILE-7421 must only
		// allow re-pointing when the existing version is a true placeholder (0 concepts).

		// Step 1: Create a COMPLETE CodeSystem with real concepts
		CodeSystem cs1 = new CodeSystem();
		cs1.setUrl("http://snomed.info/sct");
		cs1.setContent(Enumerations.CodeSystemContentMode.COMPLETE);
		cs1.setStatus(Enumerations.PublicationStatus.ACTIVE);
		for (int i = 0; i < 5; i++) {
			cs1.addConcept(new CodeSystem.ConceptDefinitionComponent("code" + i));
		}
		myCodeSystemDao.create(cs1, mySrd);

		myTerminologyDeferredStorageSvc.saveAllDeferred();

		// Verify: TermCodeSystemVersion exists with real concepts
		runInTransaction(() -> {
			TermCodeSystem tcs = myTermCodeSystemDao.findByCodeSystemUri("http://snomed.info/sct");
			assertThat(tcs).isNotNull();
			assertThat(tcs.getCurrentVersion()).isNotNull();
			long conceptCount = myTermConceptDao.countByCodeSystemVersion(tcs.getCurrentVersion().getPid());
			assertThat(conceptCount).isEqualTo(5);
		});

		// Step 2: Create a second NOTPRESENT CodeSystem with the same URL.
		// This creates a new FHIR resource (different from the COMPLETE one).
		// Since the existing version has real concepts, this should be rejected.
		CodeSystem cs2 = new CodeSystem();
		cs2.setUrl("http://snomed.info/sct");
		cs2.setContent(Enumerations.CodeSystemContentMode.NOTPRESENT);
		cs2.setStatus(Enumerations.PublicationStatus.ACTIVE);

		assertThatThrownBy(() -> myCodeSystemDao.create(cs2, mySrd))
			.isInstanceOf(UnprocessableEntityException.class)
			.hasMessageContaining("HAPI-0848");
	}

	@Test
	void testStartStagingCodeSystemVersion_DoesntAlreadyExist() {
		createCodeSystem(withUrl("http://foo"));

		// Test
		ITermCodeSystemStorageSvc.StartStagingCodeSystemVersionResponse outcome = mySvc.startStagingCodeSystemVersion("http://foo", "123");

		// Verify
		runInTransaction(() -> {
			TermCodeSystemVersion existing = myCodeSystemVersionDao.findByCodeSystemUriAndVersion("http://foo", outcome.stagingVersionId());
			assertNotNull(existing);
			assertEquals("http://foo", existing.getCodeSystem().getCodeSystemUri());
			assertEquals(outcome.stagingVersionId(), existing.getCodeSystemVersionId());
			assertThat(existing.getCodeSystemVersionId()).matches(UUID_PATTERN);
			assertEquals("123", existing.getCodeSystemIntendedVersionId());

			// The new version shouldn't be activated
			assertNotSame(existing, existing.getCodeSystem().getCurrentVersion());
			assertNull(existing.getCodeSystem().getCurrentVersion().getCodeSystemVersionId());
		});
	}

	private CodeSystem createCodeSystemWithMoreThan100Concepts() {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl(URL_MY_CODE_SYSTEM);

		for (int i = 0; i < 125; i++) {
			codeSystem.addConcept(new CodeSystem.ConceptDefinitionComponent("codeA " + i));
		}

		codeSystem.setContent(Enumerations.CodeSystemContentMode.COMPLETE);
		return codeSystem;

	}

	private void testCreatingAndUpdatingCodeSystemEntity(CodeSystem theUpload, CodeSystem theDuplicate, int expectedCnt, String theDuplicateErrorBaseMsg) {

		// Create CodeSystem resource
		ResourceTable codeSystemResourceEntity = (ResourceTable) myCodeSystemDao.create(theUpload, mySrd).getEntity();

		// Create the CodeSystem and CodeSystemVersion entities
		validateCodeSystemUpdates(expectedCnt);

		// Update the CodeSystem
		theUpload.addConcept(new CodeSystem.ConceptDefinitionComponent("codeB"));
		// Update the CodeSystem and CodeSystemVersion entities
		runInTransaction(() -> myTermCodeSystemStorageSvc.storeNewCodeSystemVersionIfNeeded(theUpload, codeSystemResourceEntity));
		validateCodeSystemUpdates(expectedCnt + 1);

		// Try duplicating the CodeSystem
		JpaPid originalResId = codeSystemResourceEntity.getId();
		try {
			myCodeSystemDao.create(theDuplicate, mySrd);
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals(theDuplicateErrorBaseMsg + originalResId, e.getMessage());
		}

		// Try updating code system when content mode is NOT PRESENT
		theUpload.setConcept(new ArrayList<>());
		theUpload.setContent((Enumerations.CodeSystemContentMode.NOTPRESENT));
		runInTransaction(() -> myTermCodeSystemStorageSvc.storeNewCodeSystemVersionIfNeeded(theUpload, codeSystemResourceEntity));
		validateCodeSystemUpdates(expectedCnt + 1);

	}

	private void validateCodeSystemUpdates(int theExpectedConceptCount) {
		myTerminologyDeferredStorageSvc.setProcessDeferred(true);
		myTerminologyDeferredStorageSvc.saveDeferred();
		myTerminologyDeferredStorageSvc.setProcessDeferred(false);
		myBatchJobHelper.awaitAllJobsOfJobDefinitionIdToComplete(TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME);
		assertEquals(theExpectedConceptCount, runInTransaction(() -> myTermConceptDao.count()));
	}

	@Test
	void testWriteCodeSystemCodes_WithPropertyAndDesignation() {
		createCodeSystem(withUrl("http://foo"), withCodeSystemContent("not-present"));
		String stagingVersion = mySvc.startStagingCodeSystemVersion("http://foo", "123").stagingVersionId();

		// Test
		CodeSystem input = new CodeSystem();
		input.setUrl("http://foo");
		input.setVersion(stagingVersion);
		input.addConcept()
			.setCode("A0")
			.setDisplay("A0-Display")
			.addDesignation(
				new CodeSystem.ConceptDefinitionDesignationComponent()
					.setValue("A0-Designation-Value")
					.setLanguage("en_CA")
					.setUse(new Coding("http://designations", "A0-desig", null)))
			.addProperty(
				new CodeSystem.ConceptPropertyComponent()
					.setCode("A0-Property")
					.setValue(new Coding("A0-Property-System", "A0-Property-Value", "A0-Property-Display"))
			);
		input.addConcept()
			.setCode("A1")
			.setDisplay("A1-Display");

		UploadStatistics response = mySvc.uploadCodeSystemConcepts(input);

		// Verify
		assertEquals(2, response.getAddedConceptCount());
		assertEquals(1, response.getAddedPropertyCount());
		assertEquals(1, response.getAddedDesignationCount());
		assertEquals(0, response.getAddedConceptLinkCount());

		runInTransaction(()->{
			TermCodeSystemVersion existing = myCodeSystemVersionDao.findByCodeSystemUriAndVersion("http://foo", stagingVersion);
			assertNotNull(existing);
			List<TermConcept> concepts = getConceptsSortedByCode(existing);
			assertEquals(2, concepts.size());

			assertEquals("A0", concepts.get(0).getCode());
			assertEquals("A0-Display", concepts.get(0).getDisplay());
			assertEquals(1, concepts.get(0).getDesignations().size());
			assertEquals("en_CA", concepts.get(0).getDesignations().iterator().next().getLanguage());
			assertEquals("http://designations", concepts.get(0).getDesignations().iterator().next().getUseSystem());
			assertEquals("A0-desig", concepts.get(0).getDesignations().iterator().next().getUseCode());
			assertEquals("A0-Designation-Value", concepts.get(0).getDesignations().iterator().next().getValue());
			assertEquals(1, concepts.get(0).getProperties().size());
			assertEquals("A0-Property", concepts.get(0).getProperties().iterator().next().getKey());
			assertEquals(TermConceptPropertyTypeEnum.CODING, concepts.get(0).getProperties().iterator().next().getType());
			assertEquals("A0-Property-Value", concepts.get(0).getProperties().iterator().next().getValue());
			assertEquals("A0-Property-System", concepts.get(0).getProperties().iterator().next().getCodeSystem());
			assertEquals("A0-Property-Display", concepts.get(0).getProperties().iterator().next().getDisplay());

			assertEquals("A1", concepts.get(1).getCode());
			assertEquals("A1-Display", concepts.get(1).getDisplay());
			assertEquals(0, concepts.get(1).getDesignations().size());
			assertEquals(0, concepts.get(1).getProperties().size());
		});

		// Repeat a second time and ensure that nothing is added
		response = mySvc.uploadCodeSystemConcepts(input);

		// Verify
		assertEquals(0, response.getAddedConceptCount());
		assertEquals(0, response.getAddedPropertyCount());
		assertEquals(0, response.getAddedDesignationCount());
		runInTransaction(()-> {
			TermCodeSystemVersion existing = myCodeSystemVersionDao.findByCodeSystemUriAndVersion("http://foo", stagingVersion);
			assertNotNull(existing);
			List<TermConcept> concepts = getConceptsSortedByCode(existing);
			assertEquals(2, concepts.size());
			assertEquals(1, concepts.get(0).getDesignations().size());
			assertEquals(1, concepts.get(0).getProperties().size());
		});

	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	void testWriteCodeSystemCodes_WithHierarchy(boolean theParentAlreadyExists) {
		createCodeSystem(withUrl("http://foo"), withCodeSystemContent("not-present"));

		String stagingVersion = mySvc.startStagingCodeSystemVersion("http://foo", "123").stagingVersionId();

		if (theParentAlreadyExists) {
			CodeSystem input = new CodeSystem();
			input.setUrl("http://foo");
			input.setVersion(stagingVersion);
			input.addConcept()
				.setCode("PARENT")
				.setDisplay("Parent");
			mySvc.uploadCodeSystemConcepts(input);
		}

		// Test

		CodeSystem input = new CodeSystem();
		input.setUrl("http://foo");
		input.setVersion(stagingVersion);
		CodeSystem.ConceptDefinitionComponent parent = input.addConcept()
			.setCode("PARENT")
			.setDisplay("Parent");
		parent.addConcept()
			.setCode("CHILD")
			.setDisplay("Child");

		UploadStatistics response = mySvc.uploadCodeSystemConcepts(input);
		if (theParentAlreadyExists) {
			assertEquals(1, response.getAddedConceptCount());
		} else {
			assertEquals(2, response.getAddedConceptCount());
		}
		assertEquals(1, response.getAddedConceptLinkCount());

		// Verify
		runInTransaction(()->{
			TermCodeSystemVersion existing = myCodeSystemVersionDao.findByCodeSystemUriAndVersion("http://foo", stagingVersion);
			assertNotNull(existing);
			assertEquals(2, existing.getConcepts().size());
			assertEquals("Parent", existing.getConcept("PARENT").orElseThrow().getDisplay());
			List<TermConcept> children = existing.getConcept("PARENT").orElseThrow().getChildCodes();
			assertEquals(1, children.size());
			assertEquals("CHILD", children.get(0).getCode());
			assertEquals("Child", children.get(0).getDisplay());
		});

		// Repeat a second time and ensure that nothing is added
		response = mySvc.uploadCodeSystemConcepts(input);
		assertEquals(0, response.getAddedConceptCount());
		assertEquals(0, response.getAddedConceptLinkCount());

		// Verify
		runInTransaction(()-> {
			TermCodeSystemVersion existing = myCodeSystemVersionDao.findByCodeSystemUriAndVersion("http://foo", stagingVersion);
			assertNotNull(existing);
			List<TermConcept> concepts = getConceptsSortedByCode(existing);
			assertEquals(2, concepts.size());
		});

	}

	private static List<TermConcept> getConceptsSortedByCode(TermCodeSystemVersion theCodeSystemVersion) {
		return theCodeSystemVersion.getConcepts().stream().sorted(Comparator.comparing(TermConcept::getCode)).toList();
	}
}
