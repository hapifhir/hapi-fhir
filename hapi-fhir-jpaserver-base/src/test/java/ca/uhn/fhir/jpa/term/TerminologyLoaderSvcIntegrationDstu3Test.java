package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.jpa.dao.dstu3.BaseJpaDstu3Test;
import ca.uhn.fhir.util.TestUtil;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

public class TerminologyLoaderSvcIntegrationDstu3Test extends BaseJpaDstu3Test {

	@Autowired
	private TerminologyLoaderSvcImpl myLoader;

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	@Test
	public void testLoadAndStoreLoinc() throws Exception {
		ZipCollectionBuilder files = new ZipCollectionBuilder();
		TerminologyLoaderSvcLoincTest.createLoincBundle(files);

		myLoader.loadLoinc(files.getFiles(), mySrd);

		Thread.sleep(120000);
	}

}
