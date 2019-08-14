package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.r4.model.CodeSystem;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class TerminologyLoaderSvcCustomTest extends BaseLoaderTest {
	private TerminologyLoaderSvcImpl mySvc;

	@Mock
	private IHapiTerminologySvc myTermSvc;

	private ZipCollectionBuilder myFiles;


	@Before
	public void before() {
		mySvc = new TerminologyLoaderSvcImpl();
		mySvc.setTermSvcForUnitTests(myTermSvc);

		myFiles = new ZipCollectionBuilder();
	}

	@Test
	public void testLoadComplete() throws Exception {
		myFiles.addFileZip("/custom_term/", TerminologyLoaderSvcImpl.CUSTOM_CODESYSTEM_JSON);
		myFiles.addFileZip("/custom_term/", TerminologyLoaderSvcImpl.CUSTOM_CONCEPTS_FILE);
		myFiles.addFileZip("/custom_term/", TerminologyLoaderSvcImpl.CUSTOM_HIERARCHY_FILE);

		// Actually do the load
		mySvc.loadCustom("http://example.com/labCodes", myFiles.getFiles(), mySrd);

		verify(myTermSvc, times(1)).storeNewCodeSystemVersion(mySystemCaptor.capture(), myCsvCaptor.capture(), any(RequestDetails.class), myValueSetsCaptor.capture(), myConceptMapCaptor.capture());
		Map<String, TermConcept> concepts = extractConcepts();

		// Verify codesystem
		assertEquals("http://example.com/labCodes", mySystemCaptor.getValue().getUrl());
		assertEquals(CodeSystem.CodeSystemContentMode.NOTPRESENT, mySystemCaptor.getValue().getContent());
		assertEquals("Example Lab Codes", mySystemCaptor.getValue().getName());

		// Root code
		TermConcept code;
		assertEquals(2, concepts.size());
		code = concepts.get("CHEM");
		assertEquals("CHEM", code.getCode());
		assertEquals("Chemistry", code.getDisplay());

		assertEquals(2, code.getChildren().size());
		assertEquals("HB", code.getChildren().get(0).getChild().getCode());
		assertEquals("Hemoglobin", code.getChildren().get(0).getChild().getDisplay());
		assertEquals("NEUT", code.getChildren().get(1).getChild().getCode());
		assertEquals("Neutrophils", code.getChildren().get(1).getChild().getDisplay());

	}

	@Test
	public void testLoadWithNoCodeSystem() throws Exception {
		myFiles.addFileZip("/custom_term/", TerminologyLoaderSvcImpl.CUSTOM_CONCEPTS_FILE);

		// Actually do the load
		mySvc.loadCustom("http://example.com/labCodes", myFiles.getFiles(), mySrd);

		verify(myTermSvc, times(1)).storeNewCodeSystemVersion(mySystemCaptor.capture(), myCsvCaptor.capture(), any(RequestDetails.class), myValueSetsCaptor.capture(), myConceptMapCaptor.capture());
		Map<String, TermConcept> concepts = extractConcepts();

		// Verify codesystem
		assertEquals("http://example.com/labCodes", mySystemCaptor.getValue().getUrl());
		assertEquals(CodeSystem.CodeSystemContentMode.NOTPRESENT, mySystemCaptor.getValue().getContent());

	}

	/**
	 * No hierarchy file supplied
	 */
	@Test
	public void testLoadCodesOnly() throws Exception {
		myFiles.addFileZip("/custom_term/", TerminologyLoaderSvcImpl.CUSTOM_CONCEPTS_FILE);

		// Actually do the load
		mySvc.loadCustom("http://example.com/labCodes", myFiles.getFiles(), mySrd);

		verify(myTermSvc, times(1)).storeNewCodeSystemVersion(mySystemCaptor.capture(), myCsvCaptor.capture(), any(RequestDetails.class), myValueSetsCaptor.capture(), myConceptMapCaptor.capture());
		Map<String, TermConcept> concepts = extractConcepts();

		TermConcept code;

		// Root code
		assertEquals(5, concepts.size());
		code = concepts.get("CHEM");
		assertEquals("CHEM", code.getCode());
		assertEquals("Chemistry", code.getDisplay());

	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
