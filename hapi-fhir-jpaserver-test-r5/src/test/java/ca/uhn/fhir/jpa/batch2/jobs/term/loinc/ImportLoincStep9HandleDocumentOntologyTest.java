package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.jpa.term.UploadStatistics;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.nullable;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImportLoincStep9HandleDocumentOntologyTest extends BaseImportLoincStepTest{

	@InjectMocks
	private ImportLoincStep9HandleDocumentOntology mySvc;

	@Test
	void testProcess() {
		// Setup
		String classpath = "loinc-ver/v269/AccessoryFiles/DocumentOntology/DocumentOntology.csv";
		mockFetchAttachment(classpath);
		mockFetchJobMetadataAttachment();
		mockFetchPropertiesFileAttachnemtNotFound();
		when(myValueSetDao.read(any(), any())).thenThrow(new ResourceNotFoundException(new IdType("ValueSet/LL1000-0-1.234")));
		when(myTermCodeSystemStorageSvc.uploadCodeSystemConcepts(any())).thenReturn(new UploadStatistics(new IdType()));
		mockDaoRegistryValueSet();
		mockDaoRegistryConceptMap();

		// Test
		mySvc.run(newStepExecutionDetails(classpath), myDataSink);

		// Verify
		verify(myTermCodeSystemStorageSvc, times(1)).uploadCodeSystemConcepts(myCodeSystemCaptor.capture());
		CodeSystem cs = (CodeSystem) myCodeSystemCaptor.getValue();
		String hierarchy = renderHierarchy(cs, true);
		String expected = """
			-11488-4
			  -Property[document-kind: {"system":"http://loinc.org","code":"LP173418-7","display":"Note"}
			  -Property[document-type-of-service: {"system":"http://loinc.org","code":"LP173110-0","display":"Consultation"}
			  -Property[document-setting: {"system":"http://loinc.org","code":"LP173061-5","display":"{Setting}"}
			  -Property[document-role: {"system":"http://loinc.org","code":"LP187187-2","display":"{Role}"}
			-11490-0
			  -Property[document-kind: {"system":"http://loinc.org","code":"LP173418-7","display":"Note"}
			  -Property[document-type-of-service: {"system":"http://loinc.org","code":"LP173221-5","display":"Discharge summary"}
			  -Property[document-setting: {"system":"http://loinc.org","code":"LP173061-5","display":"{Setting}"}
			  -Property[document-role: {"system":"http://loinc.org","code":"LP173084-7","display":"Physician"}
			-11492-6
			  -Property[document-kind: {"system":"http://loinc.org","code":"LP173418-7","display":"Note"}
			""";
		assertEquals(expected, hierarchy);

		verify(myDataSink, times(1)).acceptForFutureStep(myStepIdCaptor.capture(), myFileSetCaptor.capture());
		assertThat(renderEmittedChunks()).containsExactly(
			"finalize-import -> ResourcesToActivate[ValueSet/loinc-document-ontology-1.234]",
			"finalize-import -> RecordsAdded: From[step-1] Counts[valueSetsAdded=1,valueSetCodesAdded=3]"
		);

		verify(myValueSetDao, times(1)).create(myValueSetCaptor.capture(), nullable(RequestDetails.class));
		List<ValueSet> allValueSets = myValueSetCaptor.getAllValues();
		allValueSets.sort(Comparator.comparing(a -> a.getIdElement().getIdPart()));
		ValueSet vs = allValueSets.get(0);
		assertEquals("loinc-document-ontology-1.234", vs.getIdElement().getIdPart());
		assertEquals("http://loinc.org/vs/loinc-document-ontology", vs.getUrl());
		assertEquals("1.234", vs.getVersion());

		String valueSetCompose = renderValueSetCompose(vs);
		expected = """
			INCLUDE:
			http://loinc.org
			  11488-4
			  11490-0
			  11492-6
			""";
		assertEquals(expected, valueSetCompose);

	}



}
