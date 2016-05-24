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
		Map<String, File> files = new HashMap<String, File>();
		files.put(TerminologyLoaderSvc.SCT_FILE_CONCEPT, new File("/Users/james/tmp/sct/SnomedCT_Release_INT_20160131_Full/Terminology/sct2_Concept_Full_INT_20160131.txt"));
		files.put(TerminologyLoaderSvc.SCT_FILE_DESCRIPTION, new File("/Users/james/tmp/sct/SnomedCT_Release_INT_20160131_Full/Terminology/sct2_Description_Full-en_INT_20160131.txt"));
		files.put(TerminologyLoaderSvc.SCT_FILE_RELATIONSHIP, new File("/Users/james/tmp/sct/SnomedCT_Release_INT_20160131_Full/Terminology/sct2_Relationship_Full_INT_20160131.txt"));
		myLoader.processSnomedCtFiles(files, mySrd);

	}

}
