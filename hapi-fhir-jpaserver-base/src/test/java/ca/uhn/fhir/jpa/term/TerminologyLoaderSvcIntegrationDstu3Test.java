package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.jpa.dao.dstu3.BaseJpaDstu3Test;
import ca.uhn.fhir.util.TestUtil;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

public class TerminologyLoaderSvcIntegrationDstu3Test extends BaseJpaDstu3Test {

	private TerminologyLoaderSvcImpl myLoader;

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	@Before
	public void beforeInitTest() {
		myLoader = new TerminologyLoaderSvcImpl();
		myLoader.setTermSvcForUnitTests(myTermSvc);
	}

	@Test
	@Ignore
	public void testLoadAndStoreLoinc() {
		List<byte[]> files;
//		myLoader.processSnomedCtFiles(files, mySrd);
	}

}
