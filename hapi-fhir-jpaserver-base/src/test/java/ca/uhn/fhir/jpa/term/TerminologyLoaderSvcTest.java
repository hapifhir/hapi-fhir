package ca.uhn.fhir.jpa.term;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInRelativeOrder;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.TestUtil;

@RunWith(MockitoJUnitRunner.class)
public class TerminologyLoaderSvcTest {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TerminologyLoaderSvcTest.class);
	private TerminologyLoaderSvc mySvc;
	
	@Mock
	private IHapiTerminologySvc myTermSvc;

	@Captor
	private ArgumentCaptor<TermCodeSystemVersion> myCsvCaptor;
	
	@Before
	public void before() {
		mySvc = new TerminologyLoaderSvc();
		mySvc.setTermSvcForUnitTests(myTermSvc);
	}
	
	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	@Test
	public void testLoadLoinc() throws Exception {
		ByteArrayOutputStream bos1 = new ByteArrayOutputStream();
		ZipOutputStream zos1 = new ZipOutputStream(bos1);
		addEntry(zos1,"/loinc/",  "loinc.csv");
		zos1.close();
		ourLog.info("ZIP file has {} bytes", bos1.toByteArray().length);
		
		ByteArrayOutputStream bos2 = new ByteArrayOutputStream();
		ZipOutputStream zos2 = new ZipOutputStream(bos2);
		addEntry(zos2,"/loinc/",  "LOINC_2.54_MULTI-AXIAL_HIERARCHY.CSV");
		zos2.close();
		ourLog.info("ZIP file has {} bytes", bos2.toByteArray().length);
		
		RequestDetails details = mock(RequestDetails.class);
		mySvc.loadLoinc(Arrays.asList(bos1.toByteArray(), bos2.toByteArray()), details);
	}

	@Test
	public void testLoadSnomedCt() throws Exception {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(bos);
		addEntry(zos, "/sct/", "sct2_Concept_Full_INT_20160131.txt");
		addEntry(zos, "/sct/", "sct2_Concept_Full-en_INT_20160131.txt");
		addEntry(zos, "/sct/", "sct2_Description_Full-en_INT_20160131.txt");
		addEntry(zos, "/sct/", "sct2_Identifier_Full_INT_20160131.txt");
		addEntry(zos,"/sct/",  "sct2_Relationship_Full_INT_20160131.txt");
		addEntry(zos,"/sct/",  "sct2_StatedRelationship_Full_INT_20160131.txt");
		addEntry(zos, "/sct/", "sct2_TextDefinition_Full-en_INT_20160131.txt");
		zos.close();
		
		ourLog.info("ZIP file has {} bytes", bos.toByteArray().length);
		
		RequestDetails details = mock(RequestDetails.class);
		mySvc.loadSnomedCt(Collections.singletonList(bos.toByteArray()), details);
		
		verify(myTermSvc).storeNewCodeSystemVersion(any(String.class), myCsvCaptor.capture(), any(RequestDetails.class));
		
		TermCodeSystemVersion csv = myCsvCaptor.getValue();
		TreeSet<String> allCodes = toCodes(csv);
		ourLog.info(allCodes.toString());
		
		assertThat(allCodes, containsInRelativeOrder("116680003"));
		assertThat(allCodes, not(containsInRelativeOrder("207527008")));
	}

	private TreeSet<String> toCodes(TermCodeSystemVersion theCsv) {
		TreeSet<String> retVal = new TreeSet<String>();
		for (TermConcept next : theCsv.getConcepts()) {
			toCodes(retVal, next);
		}
		return retVal;
	}

	private void toCodes(TreeSet<String> theCodes, TermConcept theConcept) {
		theCodes.add(theConcept.getCode());
		for (TermConceptParentChildLink next : theConcept.getChildren()) {
			toCodes(theCodes, next.getChild());
		}
	}

	@Test
	public void testLoadSnomedCtBadInput() throws Exception {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(bos);
		addEntry(zos, "/sct/", "sct2_StatedRelationship_Full_INT_20160131.txt");
		zos.close();
		
		ourLog.info("ZIP file has {} bytes", bos.toByteArray().length);
		
		RequestDetails details = mock(RequestDetails.class);
		try {
			mySvc.loadSnomedCt(Collections.singletonList(bos.toByteArray()), details);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("Invalid input zip file, expected zip to contain the following name fragments: [Terminology/sct2_Description_Full-en, Terminology/sct2_Relationship_Full, Terminology/sct2_Concept_Full_] but found: []", e.getMessage());
		}
	}

	private void addEntry(ZipOutputStream zos, String theClasspathPrefix, String theFileName) throws IOException {
		ourLog.info("Adding {} to test zip", theFileName);
		zos.putNextEntry(new ZipEntry("SnomedCT_Release_INT_20160131_Full/Terminology/" + theFileName));
		byte[] byteArray = IOUtils.toByteArray(getClass().getResourceAsStream(theClasspathPrefix + theFileName));
		Validate.notNull(byteArray);
		zos.write(byteArray);
		zos.closeEntry();
	}
	

}
