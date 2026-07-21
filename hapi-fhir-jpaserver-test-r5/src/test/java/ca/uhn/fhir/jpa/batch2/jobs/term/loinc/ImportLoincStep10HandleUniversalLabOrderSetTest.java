package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.r5.model.IdType;
import org.hl7.fhir.r5.model.ValueSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.nullable;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImportLoincStep10HandleUniversalLabOrderSetTest extends BaseImportLoincStepTest{

	@InjectMocks
	private ImportLoincStep10HandleUniversalLabOrderSet mySvc;

	@Test
	void testProcess() {
		// Setup
		String classpath = "loinc-ver/v269/AccessoryFiles/LoincUniversalLabOrdersValueSet/LoincUniversalLabOrdersValueSet.csv";
		mockFetchAttachment(classpath);
		mockFetchJobMetadataAttachment();
		mockFetchPropertiesFileAttachmentNotFound();
		when(myValueSetDao.read(any(), any())).thenThrow(new ResourceNotFoundException(new IdType("ValueSet/LL1000-0-1.234")));
		mockDaoRegistryValueSet();
		mockDaoRegistryConceptMap();

		// Test
		mySvc.run(newStepExecutionDetails(classpath), myDataSink);

		// Verify
		verify(myTermCodeSystemStorageSvc, never()).addCodeSystemConcepts(any(), myCodeSystemCaptor.capture());

		verify(myDataSink, times(1)).acceptForFutureStep(myStepIdCaptor.capture(), myFileSetCaptor.capture());
		assertThat(renderEmittedChunks()).containsExactly(
			"finalize-import -> ResourcesToActivate[ValueSet/loinc-universal-order-set-1.234]",
			"finalize-import -> RecordsAdded: From[step-1] Counts[valueSetsAdded=1,valueSetCodesAdded=9]"
		);

		verify(myValueSetDao, times(1)).create(myValueSetCaptor.capture(), nullable(RequestDetails.class));
		List<ValueSet> allValueSets = myValueSetCaptor.getAllValues();
		allValueSets.sort(Comparator.comparing(a -> a.getIdElement().getIdPart()));
		ValueSet vs = allValueSets.get(0);
		assertEquals("loinc-universal-order-set-1.234", vs.getIdElement().getIdPart());
		assertEquals("http://loinc.org/vs/loinc-universal-order-set", vs.getUrl());
		assertEquals("1.234", vs.getVersion());

		String valueSetCompose = renderValueSetCompose(vs);
		String expected = """
			INCLUDE:
			http://loinc.org
			  42176-8
			  53835-5
			  31019-3
			  6765-2
			  1668-3
			  32854-2
			  49054-0
			  62292-8
			  44907-4
			""";
		assertEquals(expected, valueSetCompose);

	}



}
