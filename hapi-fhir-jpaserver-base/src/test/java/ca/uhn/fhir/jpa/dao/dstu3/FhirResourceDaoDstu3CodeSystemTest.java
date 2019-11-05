package ca.uhn.fhir.jpa.dao.dstu3;

import ca.uhn.fhir.jpa.term.TermReindexingSvcImpl;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.IOUtils;
import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.Enumerations;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.AfterClass;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class FhirResourceDaoDstu3CodeSystemTest extends BaseJpaDstu3Test {
	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
		TermReindexingSvcImpl.setForceSaveDeferredAlwaysForUnitTest(false);
	}

	
	@Test
	public void testIndexContained() throws Exception {
		TermReindexingSvcImpl.setForceSaveDeferredAlwaysForUnitTest(true);
		
		String input = IOUtils.toString(getClass().getResource("/dstu3_codesystem_complete.json"), StandardCharsets.UTF_8);
		CodeSystem cs = myFhirCtx.newJsonParser().parseResource(CodeSystem.class, input);
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
		runInTransaction(()->{
			assertEquals(2, myConceptDao.count());
		});

		// Delete the code system
		myCodeSystemDao.delete(id);
		runInTransaction(()->{
			assertEquals(0L, myConceptDao.count());
		});

	}


}
