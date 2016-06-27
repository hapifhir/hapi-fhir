package ca.uhn.fhir.jpa.term;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import ca.uhn.fhir.jpa.dao.dstu3.BaseJpaDstu3Test;
import ca.uhn.fhir.util.TestUtil;

public class TerminologyLoaderSvcIntegrationTest extends BaseJpaDstu3Test {

	private TerminologyLoaderSvc myLoader;

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	@Before
	public void beforeInitTest() {
		myLoader = new TerminologyLoaderSvc();
		myLoader.setTermSvcForUnitTests(myTermSvc);
	}

	@Test
	@Ignore
	public void testLoadAndStoreSnomedCt() {
//		myLoader.processSnomedCtFiles(files, mySrd);
	}

}
