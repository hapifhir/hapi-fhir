package ca.uhn.fhir.jpa.term;

import static org.hamcrest.Matchers.containsInRelativeOrder;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Ignore;
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
		addEntry(zos1, "/loinc/", "loinc.csv");
		zos1.close();
		ourLog.info("ZIP file has {} bytes", bos1.toByteArray().length);

		ByteArrayOutputStream bos2 = new ByteArrayOutputStream();
		ZipOutputStream zos2 = new ZipOutputStream(bos2);
		addEntry(zos2, "/loinc/", "LOINC_2.54_MULTI-AXIAL_HIERARCHY.CSV");
		zos2.close();
		ourLog.info("ZIP file has {} bytes", bos2.toByteArray().length);

		RequestDetails details = mock(RequestDetails.class);
		mySvc.loadLoinc(list(bos1.toByteArray(), bos2.toByteArray()), details);

		verify(myTermSvc, times(1)).storeNewCodeSystemVersion(mySystemCaptor.capture(), myCsvCaptor.capture(), any(RequestDetails.class));
		
		TermCodeSystemVersion ver = myCsvCaptor.getValue();
		TermConcept code = ver.getConcepts().iterator().next();
		assertEquals("10013-1", code.getCode());
		
	}

	@Captor
	private ArgumentCaptor<String> mySystemCaptor;
	
	
	/**
	 * This is just for trying stuff, it won't run without
	 * local files external to the git repo
	 */
	@Ignore
	@Test
	public void testLoadSnomedCtAgainstRealFile() throws Exception {
		byte[] bytes = IOUtils.toByteArray(new FileInputStream("/Users/james/Downloads/SnomedCT_Release_INT_20160131_Full.zip"));
		
		RequestDetails details = mock(RequestDetails.class);
		mySvc.loadSnomedCt(list(bytes), details);
	}
	
	
	@Test
	public void testLoadSnomedCt() throws Exception {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(bos);
		addEntry(zos, "/sct/", "sct2_Concept_Full_INT_20160131.txt");
		addEntry(zos, "/sct/", "sct2_Concept_Full-en_INT_20160131.txt");
		addEntry(zos, "/sct/", "sct2_Description_Full-en_INT_20160131.txt");
		addEntry(zos, "/sct/", "sct2_Identifier_Full_INT_20160131.txt");
		addEntry(zos, "/sct/", "sct2_Relationship_Full_INT_20160131.txt");
		addEntry(zos, "/sct/", "sct2_StatedRelationship_Full_INT_20160131.txt");
		addEntry(zos, "/sct/", "sct2_TextDefinition_Full-en_INT_20160131.txt");
		zos.close();

		ourLog.info("ZIP file has {} bytes", bos.toByteArray().length);

		RequestDetails details = mock(RequestDetails.class);
		mySvc.loadSnomedCt(list(bos.toByteArray()), details);

		verify(myTermSvc).storeNewCodeSystemVersion(any(String.class), myCsvCaptor.capture(), any(RequestDetails.class));

		TermCodeSystemVersion csv = myCsvCaptor.getValue();
		TreeSet<String> allCodes = toCodes(csv, true);
		ourLog.info(allCodes.toString());

		assertThat(allCodes, containsInRelativeOrder("116680003"));
		assertThat(allCodes, not(containsInRelativeOrder("207527008")));

		allCodes = toCodes(csv, false);
		ourLog.info(allCodes.toString());
		assertThat(allCodes, hasItem("126816002"));
	}

	private List<byte[]> list(byte[]... theByteArray) {
		return new ArrayList<byte[]>(Arrays.asList(theByteArray));
	}

	private TreeSet<String> toCodes(TermCodeSystemVersion theCsv, boolean theAddChildren) {
		TreeSet<String> retVal = new TreeSet<String>();
		for (TermConcept next : theCsv.getConcepts()) {
			toCodes(retVal, next, theAddChildren);
		}
		return retVal;
	}

	private void toCodes(TreeSet<String> theCodes, TermConcept theConcept, boolean theAddChildren) {
		theCodes.add(theConcept.getCode());
		if (theAddChildren) {
			for (TermConceptParentChildLink next : theConcept.getChildren()) {
				toCodes(theCodes, next.getChild(), theAddChildren);
			}
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
