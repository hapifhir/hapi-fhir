package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.term.TermReindexingSvcImpl;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.IOUtils;
import org.hl7.fhir.r4.model.CodeSystem;
import org.junit.AfterClass;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertNotEquals;

public class FhirResourceDaoR4CodeSystemTest extends BaseJpaR4Test {

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
		TermReindexingSvcImpl.setForceSaveDeferredAlwaysForUnitTest(false);
	}

	
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

	
}
