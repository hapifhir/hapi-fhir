package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class TerminologyLoaderSvcLoincTest {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TerminologyLoaderSvcLoincTest.class);
	private TerminologyLoaderSvc mySvc;

	@Mock
	private IHapiTerminologySvc myTermSvc;

	@Mock
	private IHapiTerminologySvcDstu3 myTermSvcDstu3;

	@Captor
	private ArgumentCaptor<TermCodeSystemVersion> myCsvCaptor;
	private ArrayList<byte[]> myFiles;
	@Captor
	private ArgumentCaptor<CodeSystem> mySystemCaptor;
	@Mock
	private RequestDetails details;
	@Captor
	private ArgumentCaptor<List<ValueSet>> myValueSetsCaptor;


	private void addFile(String theClasspathPrefix, String theClasspathFileName, String theOutputFilename) throws IOException {
		ByteArrayOutputStream bos;
		bos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(bos);
		ourLog.info("Adding {} to test zip", theClasspathFileName);
		zos.putNextEntry(new ZipEntry("SnomedCT_Release_INT_20160131_Full/Terminology/" + theOutputFilename));
		String classpathName = theClasspathPrefix + theClasspathFileName;
		InputStream stream = getClass().getResourceAsStream(classpathName);
		Validate.notNull(stream, "Couldn't load " + classpathName);
		byte[] byteArray = IOUtils.toByteArray(stream);
		Validate.notNull(byteArray);
		zos.write(byteArray);
		zos.closeEntry();
		zos.close();
		ourLog.info("ZIP file has {} bytes", bos.toByteArray().length);
		myFiles.add(bos.toByteArray());
	}

	@Before
	public void before() {
		mySvc = new TerminologyLoaderSvc();
		mySvc.setTermSvcForUnitTests(myTermSvc);
		mySvc.setTermSvcDstu3ForUnitTest(myTermSvcDstu3);

		myFiles = new ArrayList<>();
	}

	@Test
	public void testLoadLoinc() throws Exception {
		addFile("/loinc/", "loinc.csv", TerminologyLoaderSvc.LOINC_FILE);
		addFile("/loinc/", "hierarchy.csv", TerminologyLoaderSvc.LOINC_HIERARCHY_FILE);
		addFile("/loinc/", "AnswerList_Beta_1.csv", TerminologyLoaderSvc.LOINC_ANSWERLIST_FILE);

		// Actually do the load
		mySvc.loadLoinc(myFiles, details);

		verify(myTermSvcDstu3, times(1)).storeNewCodeSystemVersion(mySystemCaptor.capture(), myCsvCaptor.capture(), any(RequestDetails.class), myValueSetsCaptor.capture());

		TermCodeSystemVersion ver = myCsvCaptor.getValue();

		Map<String, TermConcept> concepts = new HashMap<>();
		for (TermConcept next : ver.getConcepts()) {
			concepts.put(next.getCode(), next);
		}

		// Normal loinc code
		TermConcept code = concepts.get("10013-1");
		assertEquals("10013-1", code.getCode());
		assertEquals("Elpot", code.getProperty("PROPERTY"));
		assertEquals("Pt", code.getProperty("TIME_ASPCT"));
		assertEquals("R' wave amplitude in lead I", code.getDisplay());

		// Answer list
		code = concepts.get("LL1001-8");
		assertEquals("LL1001-8", code.getCode());
		assertEquals("PhenX05_14_30D freq amts", code.getDisplay());

		// Answer list code
		code = concepts.get("LA13834-9");
		assertEquals("LA13834-9", code.getCode());
		assertEquals("1-2 times per week", code.getDisplay());
		assertEquals(3, code.getSequence().intValue());

		// AnswerList valueSet
		Map<String, ValueSet> valueSets = new HashMap<>();
		for (ValueSet next : myValueSetsCaptor.getValue()) {
			valueSets.put(next.getId(), next);
		}
		ValueSet vs = valueSets.get("LL1001-8");
		assertEquals(IHapiTerminologyLoaderSvc.LOINC_URL, vs.getIdentifier().get(0).getSystem());
		assertEquals("LL1001-8", vs.getIdentifier().get(0).getValue());
		assertEquals("PhenX05_14_30D freq amts", vs.getName());
		assertEquals("urn:oid:1.3.6.1.4.1.12009.10.1.166", vs.getUrl());
		assertEquals(1, vs.getCompose().getInclude().size());
		assertEquals(6, vs.getCompose().getInclude().get(0).getConcept().size());
		assertEquals(IHapiTerminologyLoaderSvc.LOINC_URL, vs.getCompose().getInclude().get(0).getSystem());
		assertEquals("LA6270-8", vs.getCompose().getInclude().get(0).getConcept().get(0).getCode());
		assertEquals("Never", vs.getCompose().getInclude().get(0).getConcept().get(0).getDisplay());

	}


	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
