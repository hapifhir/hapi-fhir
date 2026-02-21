package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.entity.TermCodeSystem;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.test.Batch2JobHelper;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;

import static ca.uhn.fhir.batch2.jobs.termcodesystem.TermCodeSystemJobConfig.TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME;
import static org.assertj.core.api.Assertions.assertThat;
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

	/**
	 * Reproduces SMILE-7421: When a CodeSystem with content=NOTPRESENT is pre-seeded via
	 * dao.create() (simulating package install), and then the same CodeSystem URL is uploaded
	 * via storeNewCodeSystemVersion() (simulating $upload-external-code-system) with an
	 * explicit ID that differs from the pre-seeded resource, it should succeed by reusing the
	 * existing TermCodeSystem. Instead, it throws Msg.code(848) because
	 * checkForCodeSystemVersionDuplicate() sees a resource ID mismatch.
	 */
	@Test
	void testPreSeedCodeSystemThenUploadFullTerminology() {
		// Step 1: Pre-seed a CodeSystem with content=NOTPRESENT via dao.create()
		// This simulates what happens during package install with STORE_AND_INSTALL
		CodeSystem preSeedCs = new CodeSystem();
		preSeedCs.setUrl(URL_MY_CODE_SYSTEM);
		preSeedCs.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
		preSeedCs.setName("My Code System");
		myCodeSystemDao.create(preSeedCs, mySrd);

		// Process deferred storage to ensure TermCodeSystem entities are created
		validateCodeSystemUpdates(0);

		// Verify the pre-seed created the TermCodeSystem
		runInTransaction(() -> {
			assertThat(myTermCodeSystemDao.count()).isEqualTo(1);
			TermCodeSystem termCodeSystem = myTermCodeSystemDao.findByCodeSystemUri(URL_MY_CODE_SYSTEM);
			assertThat(termCodeSystem).isNotNull();
		});

		// Step 2: Upload full terminology via storeNewCodeSystemVersion()
		// This simulates $upload-external-code-system. The CodeSystem resource has an
		// explicit ID (like a loader-assigned ID) that differs from the pre-seeded resource.
		CodeSystem uploadCs = new CodeSystem();
		uploadCs.setId("custom-code-system");
		uploadCs.setUrl(URL_MY_CODE_SYSTEM);
		uploadCs.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		uploadCs.setName("My Code System");
		uploadCs.addConcept(new CodeSystem.ConceptDefinitionComponent(new CodeType("code1")).setDisplay("Code 1"));
		uploadCs.addConcept(new CodeSystem.ConceptDefinitionComponent(new CodeType("code2")).setDisplay("Code 2"));
		uploadCs.addConcept(new CodeSystem.ConceptDefinitionComponent(new CodeType("code3")).setDisplay("Code 3"));

		TermCodeSystemVersion csvVersion = new TermCodeSystemVersion();
		csvVersion.getConcepts().add(new TermConcept(csvVersion, "code1").setDisplay("Code 1"));
		csvVersion.getConcepts().add(new TermConcept(csvVersion, "code2").setDisplay("Code 2"));
		csvVersion.getConcepts().add(new TermConcept(csvVersion, "code3").setDisplay("Code 3"));

		// The upload should succeed — the desired behavior is that storeNewCodeSystemVersion()
		// reuses the existing TermCodeSystem and updates it with the full terminology content.
		// BUG: This throws UnprocessableEntityException with Msg.code(848) because the
		// pre-seeded resource ID differs from the one created by createOrUpdateCodeSystem().
		runInTransaction(() -> myTermCodeSystemStorageSvc.storeNewCodeSystemVersion(
			uploadCs,
			csvVersion,
			mySrd,
			Collections.emptyList(),
			Collections.emptyList()));

		// Process deferred storage
		validateCodeSystemUpdates(3);

		// Verify the TermCodeSystem is still 1, and concepts are present
		runInTransaction(() -> {
			assertThat(myTermCodeSystemDao.count()).isEqualTo(1);
			assertThat(myTermConceptDao.count()).isGreaterThanOrEqualTo(2);
		});
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
