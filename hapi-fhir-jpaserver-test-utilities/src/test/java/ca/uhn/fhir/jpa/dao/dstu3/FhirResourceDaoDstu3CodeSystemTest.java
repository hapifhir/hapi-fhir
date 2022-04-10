package ca.uhn.fhir.jpa.dao.dstu3;

import ca.uhn.fhir.jpa.term.TermReindexingSvcImpl;
import ca.uhn.fhir.jpa.test.BaseJpaDstu3Test;
import ca.uhn.fhir.test.utilities.BatchJobHelper;
import org.apache.commons.io.IOUtils;
import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.Enumerations;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.charset.StandardCharsets;

import static ca.uhn.fhir.jpa.batch.config.BatchConstants.TERM_CODE_SYSTEM_DELETE_JOB_NAME;
import static ca.uhn.fhir.jpa.batch.config.BatchConstants.TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

public class FhirResourceDaoDstu3CodeSystemTest extends BaseJpaDstu3Test {

	@Autowired private BatchJobHelper myBatchJobHelper;

	@AfterAll
	public static void afterClassClearContext() {
		TermReindexingSvcImpl.setForceSaveDeferredAlwaysForUnitTest(false);
	}

	
	@Test
	public void testIndexContained() throws Exception {
		TermReindexingSvcImpl.setForceSaveDeferredAlwaysForUnitTest(true);
		
		String input = IOUtils.toString(getClass().getResource("/dstu3_codesystem_complete.json"), StandardCharsets.UTF_8);
		CodeSystem cs = myFhirContext.newJsonParser().parseResource(CodeSystem.class, input);
		myCodeSystemDao.create(cs, mySrd);


		myResourceReindexingSvc.markAllResourcesForReindexing();
		int outcome= myResourceReindexingSvc.forceReindexingPass();
		assertNotEquals(-1, outcome); // -1 means there was a failure
		
		myTerminologyDeferredStorageSvc.saveDeferred();
		
	}

	@Test
	public void testDeleteCodeSystemComplete() {
		TermReindexingSvcImpl.setForceSaveDeferredAlwaysForUnitTest(false);

		// Create the code system
		CodeSystem cs = new CodeSystem();
		cs.setUrl("http://foo");
		cs.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		cs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		cs.addConcept().setCode("A");
		IIdType id = myCodeSystemDao.create(cs, mySrd).getId().toUnqualifiedVersionless();
		runInTransaction(()->{
			assertEquals(1, myConceptDao.count());
		});

		// Update the code system
		cs = new CodeSystem();
		cs.setId(id);
		cs.setUrl("http://foo");
		cs.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		cs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		cs.addConcept().setCode("A");
		cs.addConcept().setCode("B");
		myCodeSystemDao.update(cs, mySrd);
		myTerminologyDeferredStorageSvc.saveAllDeferred();
		myBatchJobHelper.awaitAllBulkJobCompletions(TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME);
		runInTransaction(()->{
			assertEquals(2, myConceptDao.count());
		});

		// Update the code system to reduce the count again
		cs = new CodeSystem();
		cs.setId(id);
		cs.setUrl("http://foo");
		cs.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		cs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		cs.addConcept().setCode("C");
		myCodeSystemDao.update(cs, mySrd);
		myTerminologyDeferredStorageSvc.saveAllDeferred();
		myBatchJobHelper.awaitAllBulkJobCompletions(TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME);
		runInTransaction(()->{
			assertEquals(1, myConceptDao.count());
		});

		// Delete the code system
		runInTransaction(()->{
			myCodeSystemDao.delete(id);
		});
		myTerminologyDeferredStorageSvc.saveDeferred();
		myBatchJobHelper.awaitAllBulkJobCompletions(TERM_CODE_SYSTEM_DELETE_JOB_NAME);
		runInTransaction(()->{
			assertEquals(0L, myConceptDao.count());
		});

	}

	@Test
	public void testValidateCodeForCodeSystemOperationNotSupported() {
		try {
			myCodeSystemDao.validateCode(null, null, null, null, null, null, null, null);
			fail();
		} catch (UnsupportedOperationException theE) {
			assertNotNull(theE);
		}

	}



}
