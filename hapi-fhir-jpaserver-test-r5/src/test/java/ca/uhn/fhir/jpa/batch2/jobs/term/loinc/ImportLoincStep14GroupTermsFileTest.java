package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.nullable;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImportLoincStep14GroupTermsFileTest extends BaseImportLoincStepTest{

	@InjectMocks
	private ImportLoincStep14GroupTermsFile mySvc;

	@Test
	void testProcess() {
		// Setup
		String classpath = "loinc-ver/v269/AccessoryFiles/GroupFile/GroupLoincTerms.csv";
		mockFetchAttachment(classpath);
		when(myJobPersistence.fetchAttachmentByFilename(eq("my-instance-id"), eq(LoincUploadPropertiesEnum.LOINC_UPLOAD_PROPERTIES_FILE.getCode()))).thenThrow(new ResourceNotFoundException("Not found", null));
		when(myValueSetDao.read(any(), any())).thenThrow(new ResourceNotFoundException(new IdType("ValueSet/LL1000-0-1.234")));
		mockDaoRegistryValueSet();
		mockDaoRegistryConceptMap();

		// Test
		mySvc.run(newStepExecutionDetails(classpath), myDataSink);

		// Verify
		verify(myTermCodeSystemStorageSvc, never()).uploadCodeSystemConcepts(myCodeSystemCaptor.capture());

		verify(myDataSink, times(1)).accept(myFileSetCaptor.capture());
		assertThat(myFileSetCaptor.getAllValues().get(0).getResourcesToActivate()).containsExactlyInAnyOrder(
			"ValueSet/LG1695-8-1.234"
		);

		verify(myValueSetDao, times(1)).create(myValueSetCaptor.capture(), nullable(RequestDetails.class));
		List<ValueSet> allValueSets = myValueSetCaptor.getAllValues();
		allValueSets.sort(Comparator.comparing(a -> a.getIdElement().getIdPart()));
		ValueSet vs = allValueSets.get(0);
		assertEquals("LG1695-8-1.234", vs.getIdElement().getIdPart());
		assertEquals("http://loinc.org/vs/LG1695-8", vs.getUrl());
		assertEquals("1.234", vs.getVersion());

		String valueSetCompose = renderValueSetCompose(vs);
		String expected = """
			INCLUDE:
			http://loinc.org
			  17424-3
			  13006-2
			""";
		assertEquals(expected, valueSetCompose);

	}




}
