package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.jpa.dao.r4.BaseJpaR4Test;
import ca.uhn.fhir.jpa.entity.TermCodeSystem;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TermCodeSystemStorageSvcTest extends BaseJpaR4Test {

	public static final String URL_MY_CODE_SYSTEM = "http://example.com/my_code_system";

	@Test
	public void testStoreNewCodeSystemVersionForExistingCodeSystemNoVersionId() {
		CodeSystem firstUpload = createCodeSystemWithMoreThan100Concepts();
		CodeSystem duplicateUpload = createCodeSystemWithMoreThan100Concepts();

		testCreatingAndUpdatingCodeSystemEntity(firstUpload, duplicateUpload, 125, "Can not create multiple CodeSystem resources with CodeSystem.url \"http://example.com/my_code_system\", already have one with resource ID: CodeSystem/");

		runInTransaction(() -> {
			assertEquals(1, myTermCodeSystemDao.count());
			assertEquals(1, myTermCodeSystemVersionDao.count());
			TermCodeSystem myTermCodeSystem = myTermCodeSystemDao.findByCodeSystemUri(URL_MY_CODE_SYSTEM);

			TermCodeSystemVersion myTermCodeSystemVersion = myTermCodeSystemVersionDao.findByCodeSystemPidVersionIsNull( myTermCodeSystem.getPid());
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

		testCreatingAndUpdatingCodeSystemEntity(firstUpload, duplicateUpload, 125,"Can not create multiple CodeSystem resources with CodeSystem.url \"http://example.com/my_code_system\" and CodeSystem.version \"1\", already have one with resource ID: CodeSystem/");

		runInTransaction(() -> {
			assertEquals(1, myTermCodeSystemDao.count());
			assertEquals(1, myTermCodeSystemVersionDao.count());
			TermCodeSystem myTermCodeSystem = myTermCodeSystemDao.findByCodeSystemUri(URL_MY_CODE_SYSTEM);

			TermCodeSystemVersion myTermCodeSystemVersion = myTermCodeSystemVersionDao.findByCodeSystemPidAndVersion( myTermCodeSystem.getPid(), "1");
			assertEquals(myTermCodeSystem.getCurrentVersion().getPid(), myTermCodeSystemVersion.getPid());
			assertEquals(myTermCodeSystem.getResource().getId(), myTermCodeSystemVersion.getResource().getId());
		});

		// Now add a second version
		firstUpload = createCodeSystemWithMoreThan100Concepts();
		firstUpload.setVersion("2");

		duplicateUpload = createCodeSystemWithMoreThan100Concepts();
		duplicateUpload.setVersion("2");

		testCreatingAndUpdatingCodeSystemEntity(firstUpload, duplicateUpload, 251,"Can not create multiple CodeSystem resources with CodeSystem.url \"http://example.com/my_code_system\" and CodeSystem.version \"2\", already have one with resource ID: CodeSystem/");

		runInTransaction(() -> {
			assertEquals(1, myTermCodeSystemDao.count());
			assertEquals(2, myTermCodeSystemVersionDao.count());
			TermCodeSystem myTermCodeSystem = myTermCodeSystemDao.findByCodeSystemUri(URL_MY_CODE_SYSTEM);

			TermCodeSystemVersion mySecondTermCodeSystemVersion = myTermCodeSystemVersionDao.findByCodeSystemPidAndVersion(myTermCodeSystem.getPid(), "2");
			assertEquals(myTermCodeSystem.getCurrentVersion().getPid(), mySecondTermCodeSystemVersion.getPid());
			assertEquals(myTermCodeSystem.getResource().getId(), mySecondTermCodeSystemVersion.getResource().getId());
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
		validateCodeSystemUpdates(expectedCnt+1);

		// Try duplicating the CodeSystem
		Long originalResId = codeSystemResourceEntity.getId();
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
		validateCodeSystemUpdates(expectedCnt+1);

	}

	private void validateCodeSystemUpdates(int theExpectedConceptCount) {
		myTerminologyDeferredStorageSvc.setProcessDeferred(true);
		myTerminologyDeferredStorageSvc.saveDeferred();
		myTerminologyDeferredStorageSvc.setProcessDeferred(false);
		assertEquals(theExpectedConceptCount, myTermConceptDao.count());

	}

}
