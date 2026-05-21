package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.batch2.api.AttachmentContentTypeEnum;
import ca.uhn.fhir.batch2.api.AttachmentDetails;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.IJobStepExecutionServices;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoConceptMap;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoValueSet;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyFileSetJson;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.ClasspathUtil;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Comparator;
import java.util.List;

import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.ImportLoincStep3HandleHierarchyTest.renderHierarchy;
import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.ImportLoincStep4HandleAnswerListsTest.renderValueSetCompose;
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
class ImportLoincStep10HandleTop2000CodesSiTest extends BaseImportLoincStepTest{

	@InjectMocks
	private ImportLoincStep10HandleTop2000CodesSi mySvc;

	@Test
	void testProcess() {
		// Setup
		String classpath = "loinc-ver/v269/AccessoryFiles/Top2000Results/SI/Top2000CommonLabResultsSi.csv";
		mockFetchAttachment(classpath);
		when(myJobPersistence.fetchAttachmentByFilename(eq("my-instance-id"), eq(LoincUploadPropertiesEnum.LOINC_UPLOAD_PROPERTIES_FILE.getCode()))).thenThrow(new ResourceNotFoundException("Not found", null));
		when(myValueSetDao.read(any(), any())).thenThrow(new ResourceNotFoundException(new IdType("ValueSet/LL1000-0-1.234")));
		mockDaoRegistryValueSet();
		mockDaoRegistryConceptMap();

		// Test
		JobInstance instance = new JobInstance();
		instance.setInstanceId("my-instance-id");

		ImportLoincFileSetJson importLoincFileSetJson = new ImportLoincFileSetJson();
		importLoincFileSetJson.setChunkForCurrentStep(new TerminologyFileSetJson.Chunk("file.csv", "my-chunk-attachment-id"));
		importLoincFileSetJson.setLoincCodeSystemXml(ClasspathUtil.loadResource("loinc-ver/v269/loinc.xml"));
		importLoincFileSetJson.getLoincCodeSystem().setVersion("1.234");

		StepExecutionDetails<ImportLoincJobParameters, ImportLoincFileSetJson> stepExecutionDetails = new StepExecutionDetails<>(new ImportLoincJobParameters(), importLoincFileSetJson, instance, new WorkChunk(), myJobExecutionServices, myJobDefinition, "step-1", "step-2");

		mySvc.run(stepExecutionDetails, myDataSink);

		// Verify
		verify(myTermCodeSystemStorageSvc, never()).uploadCodeSystemConcepts(myCodeSystemCaptor.capture());

		verify(myDataSink, times(1)).accept(myFileSetCaptor.capture());
		assertThat(myFileSetCaptor.getAllValues().get(0).getResourcesToActivate()).containsExactlyInAnyOrder(
			"ValueSet/top-2000-lab-observations-si-1.234"
		);

		verify(myValueSetDao, times(1)).update(myValueSetCaptor.capture(), nullable(RequestDetails.class));
		List<ValueSet> allValueSets = myValueSetCaptor.getAllValues();
		allValueSets.sort(Comparator.comparing(a -> a.getIdElement().getIdPart()));
		ValueSet vs = allValueSets.get(0);
		assertEquals("top-2000-lab-observations-si-1.234", vs.getIdElement().getIdPart());
		assertEquals("http://loinc.org/vs/top-2000-lab-observations-si", vs.getUrl());
		assertEquals("1.234", vs.getVersion());

		String valueSetCompose = renderValueSetCompose(vs);
		String expected = """
			INCLUDE:
			http://loinc.org
			  14682-9
			  718-7
			  2823-3
			  14749-6
			  2951-2
			  3094-0
			  2028-9
			  2075-0
			  789-8
			""";
		assertEquals(expected, valueSetCompose);

	}



}
