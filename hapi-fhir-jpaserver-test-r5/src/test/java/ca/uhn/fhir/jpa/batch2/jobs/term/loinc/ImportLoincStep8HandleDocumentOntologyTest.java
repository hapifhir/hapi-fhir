package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.batch2.api.*;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoConceptMap;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoValueSet;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.ClasspathUtil;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.CodeSystem;
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
import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.ImportLoincStep6HandleRsnaPlaybookTest.renderConceptMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImportLoincStep8HandleDocumentOntologyTest {

	@Mock
	private IJobPersistence myJobPersistence;
	@Mock
	private ITermCodeSystemStorageSvc myTermCodeSystemStorageSvc;
	@Mock
	private IJobDataSink<ImportLoincFileSetJson> myDataSink;
	@Mock
	private IJobStepExecutionServices myJobExecutionServices;
	@Mock
	private JobDefinition<LoincJobImportParameters> myJobDefinition;
	@Mock
	private IFhirResourceDaoValueSet<ValueSet> myValueSetDao;
	@Mock
	private IFhirResourceDaoConceptMap<ConceptMap> myConceptMapDao;

	@InjectMocks
	private ImportLoincStep8HandleDocumentOntology mySvc;

	@Captor
	private ArgumentCaptor<IBaseResource> myCodeSystemCaptor;
	@Captor
	private ArgumentCaptor<ImportLoincFileSetJson> myFileSetCaptor;
	@Captor
	private ArgumentCaptor<ValueSet> myValueSetCaptor;
	@Captor
	private ArgumentCaptor<ConceptMap> myConceptMapCaptor;


	@Test
	void testProcess() {
		// Setup
		when(myJobPersistence.fetchAttachmentById(eq("my-instance-id"), eq("my-chunk-attachment-id"))).thenReturn(new AttachmentDetails(ClasspathUtil.loadResourceAsStream("loinc-ver/v269/AccessoryFiles/DocumentOntology/DocumentOntology.csv"), AttachmentContentTypeEnum.CSV, "Loinc.csv"));
		when(myValueSetDao.read(any(), any())).thenThrow(new ResourceNotFoundException(new IdType("ValueSet/LL1000-0-1.234")));

		// Test
		JobInstance instance = new JobInstance();
		instance.setInstanceId("my-instance-id");

		ImportLoincFileSetJson importLoincFileSetJson = new ImportLoincFileSetJson();
		importLoincFileSetJson.setChunkAttachmentIdForCurrentStepId("my-chunk-attachment-id");
		importLoincFileSetJson.setLoincCodeSystemXml(ClasspathUtil.loadResource("loinc-ver/v269/loinc.xml"));
		importLoincFileSetJson.getLoincCodeSystem().setVersion("1.234");

		StepExecutionDetails<LoincJobImportParameters, ImportLoincFileSetJson> stepExecutionDetails = new StepExecutionDetails<>(new LoincJobImportParameters(), importLoincFileSetJson, instance, new WorkChunk(), myJobExecutionServices, myJobDefinition, "step-1", "step-2");

		mySvc.run(stepExecutionDetails, myDataSink);

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

		verify(myDataSink, times(1)).accept(myFileSetCaptor.capture());
		assertThat(myFileSetCaptor.getAllValues().get(0).getResourcesToActivate()).containsExactlyInAnyOrder(
			"ValueSet/loinc-document-ontology-1.234"
		);

		verify(myValueSetDao, times(1)).update(myValueSetCaptor.capture(), nullable(RequestDetails.class));
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
