package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.nullable;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImportLoincStep15ParentGroupFileTest extends BaseImportLoincStepTest{
	@InjectMocks
	private ImportLoincStep15ParentGroupFile mySvc;


	@Test
	void testProcess() {
		// Setup
		String classpath = "loinc-ver/v269/AccessoryFiles/GroupFile/ParentGroup.csv";
		mockFetchAttachment(classpath);
		mockFetchJobMetadataAttachment();
		mockFetchPropertiesFileAttachnemtNotFound();
		when(myValueSetDao.read(any(), any())).thenThrow(new ResourceNotFoundException(new IdType("ValueSet/LL1000-0-1.234")));
		mockDaoRegistryValueSet();
		mockDaoRegistryConceptMap();

		// Test
		mySvc.run(newStepExecutionDetails(classpath), myDataSink);

		// Verify
		verify(myTermCodeSystemStorageSvc, never()).uploadCodeSystemConcepts(myCodeSystemCaptor.capture());

		verify(myDataSink, times(1)).acceptForFutureStep(myStepIdCaptor.capture(), myFileSetCaptor.capture());
		assertThat(renderEmittedChunks()).containsExactly(
			"finalize-import -> ResourcesToActivate[ValueSet/LG100-4-1.234]",
			"finalize-import -> RecordsAdded: From[step-1] Counts[valueSetsAdded=1]"
		);

		verify(myValueSetDao, times(1)).create(myValueSetCaptor.capture(), nullable(RequestDetails.class));
		List<ValueSet> allValueSets = myValueSetCaptor.getAllValues();
		allValueSets.sort(Comparator.comparing(a -> a.getIdElement().getIdPart()));
		ValueSet vs = allValueSets.get(0);
		assertEquals("LG100-4-1.234", vs.getIdElement().getIdPart());
		assertEquals("http://loinc.org/vs/LG100-4", vs.getUrl());
		assertEquals("1.234", vs.getVersion());
		assertEquals("Chem_DrugTox_Chal_Sero_Allergy<SAME:Comp|Prop|Tm|Syst (except intravascular and urine)><ANYBldSerPlas,ANYUrineUrineSed><ROLLUP:Method>", vs.getName());

		String valueSetCompose = renderValueSetCompose(vs);
		String expected = """
			""";
		assertEquals(expected, valueSetCompose);

	}

	@Test
	void testProcess_AlreadyExists() {
		// Setup
		String classpath = "loinc-ver/v269/AccessoryFiles/GroupFile/ParentGroup.csv";
		mockFetchAttachment(classpath);
		mockFetchJobMetadataAttachment();
		mockFetchPropertiesFileAttachnemtNotFound();
		mockDaoRegistryValueSet();
		mockDaoRegistryConceptMap();
		when(myValueSetDao.update(any(), any(RequestDetails.class))).thenReturn(new DaoMethodOutcome());
		mockJobExecutionServices();

		// This step adds names to valueset, so populate everything except the name
		ValueSet existing = new ValueSet();
		existing.setId("LG100-4-1.234");
		existing.setUrl("http://loinc.org/vs/LG100-4");
		existing.setVersion("1.234");

		when(myValueSetDao.read(any(), any())).thenReturn(existing);

		// Test
		mySvc.run(newStepExecutionDetails(classpath), myDataSink);

		// Verify
		verify(myTermCodeSystemStorageSvc, never()).uploadCodeSystemConcepts(myCodeSystemCaptor.capture());

		verify(myDataSink, times(1)).acceptForFutureStep(myStepIdCaptor.capture(), myFileSetCaptor.capture());
		assertThat(renderEmittedChunks()).containsExactly(
			"finalize-import -> ResourcesToActivate[ValueSet/LG100-4-1.234]",
			"finalize-import -> RecordsAdded: From[step-1] Counts[otherChanges=1]"
		);

		verify(myValueSetDao, times(1)).update(myValueSetCaptor.capture(), nullable(RequestDetails.class));
		List<ValueSet> allValueSets = myValueSetCaptor.getAllValues();
		allValueSets.sort(Comparator.comparing(a -> a.getIdElement().getIdPart()));
		ValueSet vs = allValueSets.get(0);
		assertEquals("LG100-4-1.234", vs.getIdElement().getIdPart());
		assertEquals("http://loinc.org/vs/LG100-4", vs.getUrl());
		assertEquals("1.234", vs.getVersion());
		assertEquals("Chem_DrugTox_Chal_Sero_Allergy<SAME:Comp|Prop|Tm|Syst (except intravascular and urine)><ANYBldSerPlas,ANYUrineUrineSed><ROLLUP:Method>", vs.getName());

		String valueSetCompose = renderValueSetCompose(vs);
		String expected = """
			""";
		assertEquals(expected, valueSetCompose);

	}




}
