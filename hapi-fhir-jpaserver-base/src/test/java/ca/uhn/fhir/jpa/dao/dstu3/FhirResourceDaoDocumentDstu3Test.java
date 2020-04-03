package ca.uhn.fhir.jpa.dao.dstu3;

import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.hl7.fhir.dstu3.model.Bundle;
import org.junit.AfterClass;
import org.junit.Test;

import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.util.TestUtil;

public class FhirResourceDaoDocumentDstu3Test extends BaseJpaDstu3Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoDocumentDstu3Test.class);

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}


	@Test
	public void testPostDocument() throws Exception {
		String input = IOUtils.toString(getClass().getResourceAsStream("/sample-document.xml"), StandardCharsets.UTF_8);
		Bundle inputBundle = myFhirCtx.newXmlParser().parseResource(Bundle.class, input);
		DaoMethodOutcome responseBundle = myBundleDao.create(inputBundle, mySrd);
	}
}
