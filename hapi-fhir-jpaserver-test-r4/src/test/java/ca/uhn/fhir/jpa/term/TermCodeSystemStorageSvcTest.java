package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.entity.TermCodeSystem;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.test.Batch2JobHelper;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Enumerations;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

import static ca.uhn.fhir.batch2.jobs.termcodesystem.TermCodeSystemJobConfig.TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TermCodeSystemStorageSvcTest extends BaseJpaR4Test {

	public static final String URL_MY_CODE_SYSTEM = "http://example.com/my_code_system";

	@Autowired
	private Batch2JobHelper myBatchJobHelper;


	@Test
	public void testStoreNewCodeSystemVersionForExistingCodeSystemNoVersionId() {
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
	public void testStoreNewCodeSystemVersionForExistingCodeSystemVersionId() {
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
		// The real-world scenario: PackageInstallerSvc pre-seeds SNOMED from hl7.terminology.r4
		// (creating Resource A). Later, either a second pre-seed run or $upload-external-code-system
		// creates/resolves to a different FHIR resource (Resource B) for the same URL. The
		// NOTPRESENT early-return path in storeNewCodeSystemVersionIfNeeded() calls
		// getOrCreateDistinctTermCodeSystem() which triggers checkForCodeSystemVersionDuplicate().
		// The existing version (0 concepts, points to Resource A) should be re-pointable to
		// Resource B, but instead Msg.code(848) is thrown.

		// Step 1: Create first NOTPRESENT CodeSystem (simulates package pre-seed install)
		CodeSystem cs1 = new CodeSystem();
		cs1.setUrl("http://snomed.info/sct");
		cs1.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
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
		// CodeSystem URL (e.g. PackageInstallerSvc.install() calling dao.create() on a
		// subsequent pre-seed run when conditional search fails to find the existing resource,
		// or $upload-external-code-system's createOrUpdateCodeSystem() creating a new resource).
		CodeSystem cs2 = new CodeSystem();
		cs2.setUrl("http://snomed.info/sct");
		cs2.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
		cs2.setStatus(Enumerations.PublicationStatus.ACTIVE);

		// This should succeed — the existing TermCodeSystemVersion is a 0-concept placeholder
		// from the first pre-seed. It should be updated to point to the new resource.
		// BUG: Currently throws UnprocessableEntityException with Msg.code(848) because
		// checkForCodeSystemVersionDuplicate() doesn't allow a different resource even
		// when the existing version has zero concepts (i.e. is just a placeholder).
		myCodeSystemDao.create(cs2, mySrd);

		// Verify: TermCodeSystem still exists and points to the new resource
		runInTransaction(() -> {
			TermCodeSystem tcs = myTermCodeSystemDao.findByCodeSystemUri("http://snomed.info/sct");
			assertThat(tcs).isNotNull();
			assertThat(tcs.getCurrentVersion()).isNotNull();
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
		cs1.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
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
		cs2.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
		cs2.setStatus(Enumerations.PublicationStatus.ACTIVE);

		// BUG: Currently throws Msg.code(848) — same bug as unversioned case
		myCodeSystemDao.create(cs2, mySrd);

		// Verify: TermCodeSystem still exists and points to the new resource
		runInTransaction(() -> {
			TermCodeSystem tcs = myTermCodeSystemDao.findByCodeSystemUri("http://snomed.info/sct");
			assertThat(tcs).isNotNull();
			assertThat(tcs.getCurrentVersion()).isNotNull();
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
		cs1.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
		cs1.setStatus(Enumerations.PublicationStatus.ACTIVE);
		myCodeSystemDao.create(cs1, mySrd);

		myTerminologyDeferredStorageSvc.saveAllDeferred();

		// Step 2: Create second NOTPRESENT CodeSystem with no version (null)
		CodeSystem cs2 = new CodeSystem();
		cs2.setUrl("http://snomed.info/sct");
		cs2.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
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
		cs1.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		cs1.setStatus(Enumerations.PublicationStatus.ACTIVE);
		for (int i = 0; i < 5; i++) {
			cs1.addConcept(new CodeSystem.ConceptDefinitionComponent(new CodeType("code" + i)));
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
		cs2.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
		cs2.setStatus(Enumerations.PublicationStatus.ACTIVE);

		assertThatThrownBy(() -> myCodeSystemDao.create(cs2, mySrd))
			.isInstanceOf(UnprocessableEntityException.class)
			.hasMessageContaining("HAPI-0848");
	}

	private CodeSystem createCodeSystemWithMoreThan100Concepts() {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl(URL_MY_CODE_SYSTEM);

		for (int i = 0; i < 125; i++) {
			codeSystem.addConcept(new CodeSystem.ConceptDefinitionComponent(new CodeType("codeA " + i)));
		}

		codeSystem.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		return codeSystem;

	}

	private void testCreatingAndUpdatingCodeSystemEntity(CodeSystem theUpload, CodeSystem theDuplicate, int expectedCnt, String theDuplicateErrorBaseMsg) {

		// Create CodeSystem resource
		ResourceTable codeSystemResourceEntity = (ResourceTable) myCodeSystemDao.create(theUpload, mySrd).getEntity();

		// Create the CodeSystem and CodeSystemVersion entities
		validateCodeSystemUpdates(expectedCnt);

		// Update the CodeSystem
		theUpload.addConcept(new CodeSystem.ConceptDefinitionComponent(new CodeType("codeB")));
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
		theUpload.setContent((CodeSystem.CodeSystemContentMode.NOTPRESENT));
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

}
