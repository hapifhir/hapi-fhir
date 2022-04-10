package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermDeferredStorageSvc;
import ca.uhn.fhir.jpa.term.custom.CustomTerminologySet;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.IdType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TerminologyLoaderSvcCustomTest extends BaseLoaderTest {
	private TermLoaderSvcImpl mySvc;

	@Mock
	private ITermCodeSystemStorageSvc myTermCodeSystemStorageSvc;
	private ZipCollectionBuilder myFiles;
	@Captor
	private ArgumentCaptor<CustomTerminologySet> myCustomTerminologySetCaptor;
	@Mock
	private ITermDeferredStorageSvc myTermDeferredStorageSvc;

	@BeforeEach
	public void before() {
		mySvc = TermLoaderSvcImpl.withoutProxyCheck(myTermDeferredStorageSvc, myTermCodeSystemStorageSvc);

		myFiles = new ZipCollectionBuilder();
	}

	@Test
	public void testLoadComplete() throws Exception {
		myFiles.addFileZip("/custom_term/", TermLoaderSvcImpl.CUSTOM_CODESYSTEM_JSON);
		myFiles.addFileZip("/custom_term/", TermLoaderSvcImpl.CUSTOM_CONCEPTS_FILE);
		myFiles.addFileZip("/custom_term/", TermLoaderSvcImpl.CUSTOM_HIERARCHY_FILE);

		// Actually do the load
		mySvc.loadCustom("http://example.com/labCodes", myFiles.getFiles(), mySrd);

		verify(myTermCodeSystemStorageSvc, times(1)).storeNewCodeSystemVersion(mySystemCaptor.capture(), myCsvCaptor.capture(), any(RequestDetails.class), myValueSetsCaptor.capture(), myConceptMapCaptor.capture());
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
		myFiles.addFileZip("/custom_term/", TermLoaderSvcImpl.CUSTOM_CONCEPTS_FILE);

		// Actually do the load
		mySvc.loadCustom("http://example.com/labCodes", myFiles.getFiles(), mySrd);

		verify(myTermCodeSystemStorageSvc, times(1)).storeNewCodeSystemVersion(mySystemCaptor.capture(), myCsvCaptor.capture(), any(RequestDetails.class), myValueSetsCaptor.capture(), myConceptMapCaptor.capture());
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
		myFiles.addFileZip("/custom_term/", TermLoaderSvcImpl.CUSTOM_CONCEPTS_FILE);

		// Actually do the load
		mySvc.loadCustom("http://example.com/labCodes", myFiles.getFiles(), mySrd);

		verify(myTermCodeSystemStorageSvc, times(1)).storeNewCodeSystemVersion(mySystemCaptor.capture(), myCsvCaptor.capture(), any(RequestDetails.class), myValueSetsCaptor.capture(), myConceptMapCaptor.capture());
		Map<String, TermConcept> concepts = extractConcepts();

		TermConcept code;

		// Root code
		assertEquals(5, concepts.size());
		code = concepts.get("CHEM");
		assertEquals("CHEM", code.getCode());
		assertEquals("Chemistry", code.getDisplay());

	}

	@Test
	public void testDeltaAdd() throws IOException {

		myFiles.addFileText(loadResource("/custom_term/concepts.csv"), "concepts.csv");
		myFiles.addFileText(loadResource("/custom_term/hierarchy.csv"), "hierarchy.csv");

		UploadStatistics stats = new UploadStatistics(100, new IdType("CodeSystem/100"));
		when(myTermCodeSystemStorageSvc.applyDeltaCodeSystemsAdd(eq("http://foo/system"), any())).thenReturn(stats);

		UploadStatistics outcome = mySvc.loadDeltaAdd("http://foo/system", myFiles.getFiles(), mySrd);
		assertSame(stats, outcome);

		verify(myTermCodeSystemStorageSvc, times(1)).applyDeltaCodeSystemsAdd(eq("http://foo/system"), myCustomTerminologySetCaptor.capture());
		CustomTerminologySet set = myCustomTerminologySetCaptor.getValue();

		// Root concepts
		assertEquals(2, set.getRootConcepts().size());
		assertEquals("CHEM", set.getRootConcepts().get(0).getCode());
		assertEquals("Chemistry", set.getRootConcepts().get(0).getDisplay());
		assertEquals("MICRO", set.getRootConcepts().get(1).getCode());
		assertEquals("Microbiology", set.getRootConcepts().get(1).getDisplay());

		// Child concepts
		assertEquals(2, set.getRootConcepts().get(0).getChildren().size());
		assertEquals("HB", set.getRootConcepts().get(0).getChildren().get(0).getChild().getCode());
		assertEquals("Hemoglobin", set.getRootConcepts().get(0).getChildren().get(0).getChild().getDisplay());
		assertEquals(null, set.getRootConcepts().get(0).getChildren().get(0).getChild().getSequence());
		assertEquals("NEUT", set.getRootConcepts().get(0).getChildren().get(1).getChild().getCode());
		assertEquals("Neutrophils", set.getRootConcepts().get(0).getChildren().get(1).getChild().getDisplay());

	}

	@Test
	public void testDeltaRemove() throws IOException {

		myFiles.addFileText(loadResource("/custom_term/concepts.csv"), "concepts.csv");

		// Hierarchy should be ignored for remove, but we'll add one just
		// to make sure it's ignored..
		myFiles.addFileText(loadResource("/custom_term/hierarchy.csv"), "hierarchy.csv");

		UploadStatistics stats = new UploadStatistics(100, new IdType("CodeSystem/100"));
		when(myTermCodeSystemStorageSvc.applyDeltaCodeSystemsRemove(eq("http://foo/system"), any())).thenReturn(stats);

		UploadStatistics outcome = mySvc.loadDeltaRemove("http://foo/system", myFiles.getFiles(), mySrd);
		assertSame(stats, outcome);

		verify(myTermCodeSystemStorageSvc, times(1)).applyDeltaCodeSystemsRemove(eq("http://foo/system"), myCustomTerminologySetCaptor.capture());
		CustomTerminologySet set = myCustomTerminologySetCaptor.getValue();

		// Root concepts
		assertEquals(5, set.getRootConcepts().size());
		assertEquals("CHEM", set.getRootConcepts().get(0).getCode());
		assertEquals("Chemistry", set.getRootConcepts().get(0).getDisplay());
		assertEquals("HB", set.getRootConcepts().get(1).getCode());
		assertEquals("Hemoglobin", set.getRootConcepts().get(1).getDisplay());
		assertEquals("NEUT", set.getRootConcepts().get(2).getCode());
		assertEquals("Neutrophils", set.getRootConcepts().get(2).getDisplay());

	}


}
