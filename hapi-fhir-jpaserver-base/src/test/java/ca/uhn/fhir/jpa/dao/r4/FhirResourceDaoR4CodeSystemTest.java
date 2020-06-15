package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.term.TermReindexingSvcImpl;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.IOUtils;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.junit.AfterClass;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class FhirResourceDaoR4CodeSystemTest extends BaseJpaR4Test {

	@Test
	public void testIndexContained() throws Exception {
		TermReindexingSvcImpl.setForceSaveDeferredAlwaysForUnitTest(true);

		String input = IOUtils.toString(getClass().getResource("/r4/codesystem_complete.json"), StandardCharsets.UTF_8);
		CodeSystem cs = myFhirCtx.newJsonParser().parseResource(CodeSystem.class, input);
		myCodeSystemDao.create(cs, mySrd);

		myResourceReindexingSvc.markAllResourcesForReindexing();
		int outcome = myResourceReindexingSvc.forceReindexingPass();
		assertNotEquals(-1, outcome); // -1 means there was a failure

		myTerminologyDeferredStorageSvc.saveDeferred();

	}

	@Test
	public void testDeleteLargeCompleteCodeSystem() {

		CodeSystem cs = new CodeSystem();
		cs.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		cs.setUrl("http://foo");
		for (int i = 0; i < 222; i++) {
			cs.addConcept().setCode("CODE" + i);
		}
		IIdType id = myCodeSystemDao.create(cs).getId().toUnqualifiedVersionless();
		myTerminologyDeferredStorageSvc.saveDeferred();
		myTerminologyDeferredStorageSvc.saveDeferred();

		runInTransaction(() -> {
			assertEquals(1, myTermCodeSystemDao.count());
			assertNotNull(myTermCodeSystemDao.findByCodeSystemUri("http://foo"));
			assertEquals(1, myTermCodeSystemVersionDao.count());
			assertEquals(222, myTermConceptDao.count());
		});

		myCodeSystemDao.delete(id);

		// Nothing is deleted initially but the URI is changed so it can't be found
		runInTransaction(() -> {
			assertEquals(1, myTermCodeSystemDao.count());
			assertNull(myTermCodeSystemDao.findByCodeSystemUri("http://foo"));
			assertEquals(1, myTermCodeSystemVersionDao.count());
			assertEquals(222, myTermConceptDao.count());
		});

		// Now the background scheduler will do its thing
		myTerminologyDeferredStorageSvc.saveDeferred();
		runInTransaction(() -> {
			assertEquals(0, myTermCodeSystemDao.count());
			assertEquals(0, myTermCodeSystemVersionDao.count());
			assertEquals(0, myTermConceptDao.count());
		});

	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
		TermReindexingSvcImpl.setForceSaveDeferredAlwaysForUnitTest(false);
	}


}
