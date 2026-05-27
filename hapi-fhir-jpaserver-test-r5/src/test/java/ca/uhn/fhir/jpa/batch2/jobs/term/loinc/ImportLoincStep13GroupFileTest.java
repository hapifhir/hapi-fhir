package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyFileSetJson;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.nullable;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImportLoincStep13GroupFileTest extends BaseImportLoincStepTest{

	@InjectMocks
	private ImportLoincStep13GroupFile mySvc;

	@Test
	void testProcess() {
		// Setup
		String classpath = "loinc-ver/v269/AccessoryFiles/GroupFile/Group.csv";
		mockFetchAttachment(classpath);
		mockFetchJobMetadataAttachment();
		mockFetchPropertiesFileAttachmentNotFound();
		when(myValueSetDao.read(any(), any())).thenThrow(new ResourceNotFoundException(new IdType("ValueSet/LL1000-0-1.234")));
		mockDaoRegistryValueSet();
		mockDaoRegistryConceptMap();

		// Test
		JobInstance instance = new JobInstance();
		instance.setInstanceId("my-instance-id");

		TerminologyFileSetJson importLoincFileSetJson = new TerminologyFileSetJson();
		importLoincFileSetJson.setSourceFilename("file.csv");
		importLoincFileSetJson.setAttachmentId("my-chunk-attachment-id");

		StepExecutionDetails<ImportLoincJobParameters, TerminologyFileSetJson> stepExecutionDetails = new StepExecutionDetails<>(new ImportLoincJobParameters(), importLoincFileSetJson, instance, new WorkChunk(), myJobExecutionServices, myJobDefinition, "step-1", "step-2");

		mySvc.run(stepExecutionDetails, myDataSink);

		// Verify
		verify(myTermCodeSystemStorageSvc, never()).uploadCodeSystemConcepts(myCodeSystemCaptor.capture());

		verify(myDataSink, times(1)).acceptForFutureStep(myStepIdCaptor.capture(), myFileSetCaptor.capture());
		assertThat(renderEmittedChunks()).containsExactly(
			"finalize-import -> ResourcesToActivate[ValueSet/LG100-4-1.234, ValueSet/LG1695-8-1.234]",
			"finalize-import -> RecordsAdded: From[step-1] Counts[valueSetsAdded=2,valueSetInclusionsAdded=1]"
		);

		verify(myValueSetDao, times(2)).create(myValueSetCaptor.capture(), nullable(RequestDetails.class));
		List<ValueSet> allValueSets = myValueSetCaptor.getAllValues();
		allValueSets.sort(Comparator.comparing(a -> a.getIdElement().getIdPart()));
		ValueSet vs = allValueSets.get(0);
		assertEquals("LG100-4-1.234", vs.getIdElement().getIdPart());
		assertEquals("http://loinc.org/vs/LG100-4", vs.getUrl());
		assertEquals("1.234", vs.getVersion());
		assertNull(vs.getName());

		String valueSetCompose = renderValueSetCompose(vs);
		String expected = """
			INCLUDE:
			ValueSet: http://loinc.org/vs/LG1695-8
			""";
		assertEquals(expected, valueSetCompose);

		vs = allValueSets.get(1);
		assertEquals("LG1695-8-1.234", vs.getIdElement().getIdPart());
		assertEquals("http://loinc.org/vs/LG1695-8", vs.getUrl());
		assertEquals("1.234", vs.getVersion());
		assertEquals("1,4-Dichlorobenzene|MCnc|Pt|ANYBldSerPl", vs.getName());

		valueSetCompose = renderValueSetCompose(vs);
		expected = """
			""";
		assertEquals(expected, valueSetCompose);

	}

	@Test
	void testProcess_ValueSetAlreadyExists_WithOtherCodes() {
		// Setup
		String classpath = "loinc-ver/v269/AccessoryFiles/GroupFile/Group.csv";
		mockFetchAttachment(classpath);
		mockFetchJobMetadataAttachment();
		mockFetchPropertiesFileAttachmentNotFound();
		when(myJobExecutionServices.newRequestDetails(any())).thenReturn(new SystemRequestDetails());
		when(myValueSetDao.read(eq(new IdType("LG1695-8-1.234")), any())).thenThrow(new ResourceNotFoundException(new IdType("ValueSet/LL1000-0-1.234")));
		mockDaoRegistryValueSet();
		mockDaoRegistryConceptMap();

		ValueSet valueSet = new ValueSet();
		valueSet.setId("LG100-4-1.234");
		valueSet.setUrl("http://loinc.org/vs/LG100-4");
		valueSet.setVersion("1.234");
		valueSet
			.getCompose()
			.addInclude()
			.setSystem("http://foo")
			.addConcept(new ValueSet.ConceptReferenceComponent().setCode("A0"))
			.addConcept(new ValueSet.ConceptReferenceComponent().setCode("A1"));
		when(myValueSetDao.read(eq(new IdType("LG100-4-1.234")), any())).thenReturn(valueSet);
		when(myValueSetDao.update(any(), any(RequestDetails.class))).thenReturn(new DaoMethodOutcome());
		mockJobExecutionServices();

		// Test
		mySvc.run(newStepExecutionDetails(classpath), myDataSink);

		// Verify
		verify(myTermCodeSystemStorageSvc, never()).uploadCodeSystemConcepts(myCodeSystemCaptor.capture());

		verify(myDataSink, times(1)).acceptForFutureStep(myStepIdCaptor.capture(), myFileSetCaptor.capture());
		assertThat(renderEmittedChunks()).containsExactly(
			"finalize-import -> ResourcesToActivate[ValueSet/LG100-4-1.234, ValueSet/LG1695-8-1.234]",
			"finalize-import -> RecordsAdded: From[step-1] Counts[valueSetsAdded=1,valueSetInclusionsAdded=1]"
		);

		verify(myValueSetDao, times(1)).update(myValueSetCaptor.capture(), nullable(RequestDetails.class));
		List<ValueSet> allValueSets = myValueSetCaptor.getAllValues();
		allValueSets.sort(Comparator.comparing(a -> a.getIdElement().getIdPart()));
		ValueSet vs = allValueSets.get(0);
		assertEquals("LG100-4-1.234", vs.getIdElement().getIdPart());
		assertEquals("http://loinc.org/vs/LG100-4", vs.getUrl());
		assertEquals("1.234", vs.getVersion());

		String valueSetCompose = renderValueSetCompose(vs);
		String expected = """
			INCLUDE:
			http://foo
			  A0
			  A1
			INCLUDE:
			ValueSet: http://loinc.org/vs/LG1695-8
			""";
		assertEquals(expected, valueSetCompose);
	}

	@Test
	void testProcess_ValueSetAlreadyExists_SameValueSetInclusion() {
		// Setup
		String classpath = "loinc-ver/v269/AccessoryFiles/GroupFile/Group.csv";
		mockFetchAttachment(classpath);
		mockFetchJobMetadataAttachment();
		mockFetchPropertiesFileAttachmentNotFound();
		when(myJobExecutionServices.newRequestDetails(any())).thenReturn(new SystemRequestDetails());
		when(myValueSetDao.read(eq(new IdType("LG1695-8-1.234")), any())).thenThrow(new ResourceNotFoundException(new IdType("ValueSet/LL1000-0-1.234")));
		mockDaoRegistryValueSet();
		mockDaoRegistryConceptMap();

		ValueSet valueSet = new ValueSet();
		valueSet.setId("LG100-4-1.234");
		valueSet.setUrl("http://loinc.org/vs/LG100-4");
		valueSet.setVersion("1.234");
		valueSet
			.getCompose()
			.addInclude()
			.addValueSet("http://loinc.org/vs/LG1695-8");
		when(myValueSetDao.read(eq(new IdType("LG100-4-1.234")), any())).thenReturn(valueSet);
		when(myValueSetDao.update(any(), any(RequestDetails.class))).thenReturn(new DaoMethodOutcome());

		// Test
		mySvc.run(newStepExecutionDetails(classpath), myDataSink);

		// Verify
		verify(myTermCodeSystemStorageSvc, never()).uploadCodeSystemConcepts(myCodeSystemCaptor.capture());

		verify(myDataSink, times(1)).acceptForFutureStep(myStepIdCaptor.capture(), myFileSetCaptor.capture());
		assertThat(renderEmittedChunks()).containsExactly(
			"finalize-import -> ResourcesToActivate[ValueSet/LG100-4-1.234, ValueSet/LG1695-8-1.234]",
			"finalize-import -> RecordsAdded: From[step-1] Counts[valueSetsAdded=1,valueSetInclusionsAdded=1]"
		);

		verify(myValueSetDao, times(1)).update(myValueSetCaptor.capture(), nullable(RequestDetails.class));
		List<ValueSet> allValueSets = myValueSetCaptor.getAllValues();
		allValueSets.sort(Comparator.comparing(a -> a.getIdElement().getIdPart()));
		ValueSet vs = allValueSets.get(0);
		assertEquals("LG100-4-1.234", vs.getIdElement().getIdPart());
		assertEquals("http://loinc.org/vs/LG100-4", vs.getUrl());
		assertEquals("1.234", vs.getVersion());

		// The http://loinc.org/vs/LG1695-8 was already in the ValueSet, and this pass
		// tried to add it again, make sure we have no duplicates
		String valueSetCompose = renderValueSetCompose(vs);
		String expected = """
			INCLUDE:
			ValueSet: http://loinc.org/vs/LG1695-8
			""";
		assertEquals(expected, valueSetCompose);
	}


}
