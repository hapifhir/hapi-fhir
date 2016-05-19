package ca.uhn.fhir.jpa.term;

import static org.mockito.Mockito.mock;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import ca.uhn.fhir.util.TestUtil;

public class TerminologyLoaderSvcTest {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TerminologyLoaderSvcTest.class);
	private TerminologyLoaderSvc mySvc;
	private IHapiTerminologySvc myTermSvc;
	
	@Before
	public void before() {
		myTermSvc = mock(IHapiTerminologySvc.class);
		
		mySvc = new TerminologyLoaderSvc();
		mySvc.setTermSvcForUnitTests(myTermSvc);
	}
	
	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}
	
	@Test
//	@Ignore
	public void testLoadSnomedCt() throws Exception {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(bos);
		addEntry(zos, "sct2_Concept_Full_INT_20160131.txt");
		addEntry(zos, "sct2_Concept_Full-en_INT_20160131.txt");
		addEntry(zos, "sct2_Description_Full-en_INT_20160131.txt");
		addEntry(zos, "sct2_Identifier_Full_INT_20160131.txt");
		addEntry(zos, "sct2_Relationship_Full_INT_20160131.txt");
		addEntry(zos, "sct2_StatedRelationship_Full_INT_20160131.txt");
		addEntry(zos, "sct2_TextDefinition_Full-en_INT_20160131.txt");
		zos.close();
		
		ourLog.info("ZIP file has {} bytes", bos.toByteArray().length);
		
		mySvc.loadSnomedCt(bos.toByteArray());
	}

	private void addEntry(ZipOutputStream zos, String fileName) throws IOException {
		ourLog.info("Adding {} to test zip", fileName);
		zos.putNextEntry(new ZipEntry("SnomedCT_Release_INT_20160131_Full/Terminology/" + fileName));
		byte[] byteArray = IOUtils.toByteArray(getClass().getResourceAsStream("/sct/" + fileName));
		Validate.notNull(byteArray);
		zos.write(byteArray);
		zos.closeEntry();
	}
	

}
