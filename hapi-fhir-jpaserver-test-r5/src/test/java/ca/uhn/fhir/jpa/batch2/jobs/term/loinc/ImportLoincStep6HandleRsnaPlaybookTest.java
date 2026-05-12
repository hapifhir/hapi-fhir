package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.batch2.api.*;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoConceptMap;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoValueSet;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyFileSetJson;
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
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.ImportLoincStep3HandleHierarchyTest.renderHierarchy;
import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.ImportLoincStep4HandleAnswerListsTest.renderValueSetCompose;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImportLoincStep6HandleRsnaPlaybookTest {

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
	private ImportLoincStep6HandleRsnaPlaybook mySvc;

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
		when(myJobPersistence.fetchAttachmentById(eq("my-instance-id"), eq("my-chunk-attachment-id"))).thenReturn(new AttachmentDetails(ClasspathUtil.loadResourceAsStream("loinc-ver/v269/AccessoryFiles/LoincRsnaRadiologyPlaybook/LoincRsnaRadiologyPlaybook.csv"), AttachmentContentTypeEnum.CSV, "Loinc.csv"));
		when(myValueSetDao.read(any(), any())).thenThrow(new ResourceNotFoundException(new IdType("ValueSet/LL1000-0-1.234")));
		when(myConceptMapDao.read(eq(new IdType("loinc-to-radlex-1.234")), any())).thenThrow(new ResourceNotFoundException(new IdType("ConceptMap/loinc-to-radlex-1.234")));
		when(myConceptMapDao.read(eq(new IdType("loinc-parts-to-radlex-1.234")), any())).thenThrow(new ResourceNotFoundException(new IdType("ConceptMap/loinc-to-radlex-1.234")));

		// Test
		JobInstance instance = new JobInstance();
		instance.setInstanceId("my-instance-id");

		ImportLoincFileSetJson importLoincFileSetJson = new ImportLoincFileSetJson();
		importLoincFileSetJson.setChunkForCurrentStep(new TerminologyFileSetJson.Chunk("file.csv", "my-chunk-attachment-id"));
		importLoincFileSetJson.setLoincCodeSystemXml(ClasspathUtil.loadResource("loinc-ver/v269/loinc.xml"));
		importLoincFileSetJson.getLoincCodeSystem().setVersion("1.234");

		StepExecutionDetails<LoincJobImportParameters, ImportLoincFileSetJson> stepExecutionDetails = new StepExecutionDetails<>(new LoincJobImportParameters(), importLoincFileSetJson, instance, new WorkChunk(), myJobExecutionServices, myJobDefinition, "step-1", "step-2");

		mySvc.run(stepExecutionDetails, myDataSink);

		// Verify
		verify(myTermCodeSystemStorageSvc, times(1)).uploadCodeSystemConcepts(myCodeSystemCaptor.capture());
		CodeSystem cs = (CodeSystem) myCodeSystemCaptor.getValue();
		String hierarchy = renderHierarchy(cs);
		String expected = """
			-17787-3
			-24531-6
			-24532-4
			""";
		assertEquals(expected, hierarchy);
		assertEquals("rad-anatomic-location-region-imaged", cs.getConcept().get(0).getProperty().get(0).getCode());
		assertEquals("{\"system\":\"http://loinc.org\",\"code\":\"LP199995-4\",\"display\":\"Neck\"}", FhirContext.forR4Cached().newJsonParser().encodeToString(cs.getConcept().get(0).getProperty().get(0).getValue()));

		verify(myDataSink, times(1)).accept(myFileSetCaptor.capture());
		assertThat(myFileSetCaptor.getAllValues().get(0).getResourcesToActivate()).containsExactlyInAnyOrder(
			"ValueSet/loinc-rsna-radiology-playbook-1.234"
		);

		verify(myValueSetDao, times(1)).update(myValueSetCaptor.capture(), nullable(RequestDetails.class));
		List<ValueSet> allValueSets = myValueSetCaptor.getAllValues();
		allValueSets.sort(Comparator.comparing(a -> a.getIdElement().getIdPart()));
		ValueSet vs = allValueSets.get(0);
		assertEquals("loinc-rsna-radiology-playbook-1.234", vs.getIdElement().getIdPart());
		assertEquals("http://loinc.org/vs/loinc-rsna-radiology-playbook", vs.getUrl());
		assertEquals("1.234", vs.getVersion());

		String valueSetCompose = renderValueSetCompose(vs);
		expected = """
			INCLUDE:
			http://loinc.org
			  17787-3
			  24531-6
			  24532-4
			""";
		assertEquals(expected, valueSetCompose);

		// ConceptMaps
		verify(myConceptMapDao, times(2)).update(myConceptMapCaptor.capture(), nullable(RequestDetails.class));
		ConceptMap actualConceptMap = myConceptMapCaptor.getAllValues().get(0);
		String actualRender = renderConceptMap(actualConceptMap);
		expected = """
			Group: http://loinc.org -> http://www.radlex.org
			  Code[24531-6] -> RPID2142
			""";
		assertEquals(expected, actualRender);

		actualConceptMap = myConceptMapCaptor.getAllValues().get(1);
		actualRender = renderConceptMap(actualConceptMap);
		expected = """
			Group: http://loinc.org -> http://www.radlex.org
			  Code[LP199995-4] -> RID7488
			  Code[LP206648-0] -> RID7578
			  Code[LP208891-4] -> RID10330
			  Code[LP207608-3] -> RID10326
			  Code[LP199943-4] -> RID431
			  Code[LP199956-6] -> RID56
			  Code[LP208105-9] -> RID29994
			""";
		assertEquals(expected, actualRender);
	}

	@Test
	void testProcess_ConceptMapAlreadyExists() {
		// Setup
		when(myJobPersistence.fetchAttachmentById(eq("my-instance-id"), eq("my-chunk-attachment-id"))).thenReturn(new AttachmentDetails(ClasspathUtil.loadResourceAsStream("loinc-ver/v269/AccessoryFiles/LoincRsnaRadiologyPlaybook/LoincRsnaRadiologyPlaybook.csv"), AttachmentContentTypeEnum.CSV, "Loinc.csv"));
		when(myValueSetDao.read(any(), any())).thenThrow(new ResourceNotFoundException(new IdType("ValueSet/LL1000-0-1.234")));
		when(myConceptMapDao.read(eq(new IdType("loinc-to-radlex-1.234")), any())).thenThrow(new ResourceNotFoundException(new IdType("ConceptMap/loinc-to-radlex-1.234")));

		ConceptMap conceptMap = new ConceptMap();
		ConceptMap.ConceptMapGroupComponent conceptMapGroup = conceptMap
			.addGroup()
			.setSource("http://loinc.org")
			.setTarget("http://www.radlex.org");
		// Will try to add this one again and shouldn't create duplicate
		conceptMapGroup.addElement()
			.setCode("LP199995-4")
			.addTarget()
			.setCode("RID7488");
		// Won't try to add this one again but should keep it
		conceptMapGroup.addElement()
			.setCode("EXISTING-SOURCE")
			.addTarget()
			.setCode("EXISTING_TARGET");
		when(myConceptMapDao.read(eq(new IdType("loinc-parts-to-radlex-1.234")), any())).thenReturn(conceptMap);

		// Test
		JobInstance instance = new JobInstance();
		instance.setInstanceId("my-instance-id");

		ImportLoincFileSetJson importLoincFileSetJson = new ImportLoincFileSetJson();
		importLoincFileSetJson.setChunkForCurrentStep(new TerminologyFileSetJson.Chunk("file.csv", "my-chunk-attachment-id"));
		importLoincFileSetJson.setLoincCodeSystemXml(ClasspathUtil.loadResource("loinc-ver/v269/loinc.xml"));
		importLoincFileSetJson.getLoincCodeSystem().setVersion("1.234");

		StepExecutionDetails<LoincJobImportParameters, ImportLoincFileSetJson> stepExecutionDetails = new StepExecutionDetails<>(new LoincJobImportParameters(), importLoincFileSetJson, instance, new WorkChunk(), myJobExecutionServices, myJobDefinition, "step-1", "step-2");

		mySvc.run(stepExecutionDetails, myDataSink);

		// Verify
		verify(myConceptMapDao, times(2)).update(myConceptMapCaptor.capture(), nullable(RequestDetails.class));
		ConceptMap actualConceptMap = myConceptMapCaptor.getAllValues().get(1);
		String actualRender = renderConceptMap(actualConceptMap);
		String expected = """
			Group: http://loinc.org -> http://www.radlex.org
			  Code[LP199995-4] -> RID7488
			  Code[EXISTING-SOURCE] -> EXISTING_TARGET
			  Code[LP206648-0] -> RID7578
			  Code[LP208891-4] -> RID10330
			  Code[LP207608-3] -> RID10326
			  Code[LP199943-4] -> RID431
			  Code[LP199956-6] -> RID56
			  Code[LP208105-9] -> RID29994
			""";
		assertEquals(expected, actualRender);
	}


	public static String renderConceptMap(ConceptMap theConceptMap) {
		StringBuilder builder = new StringBuilder();

		for (ConceptMap.ConceptMapGroupComponent group : theConceptMap.getGroup()) {
			builder
				.append("Group: ")
				.append(group.getSource())
				.append(" -> ")
				.append(group.getTarget())
				.append("\n");

			for (ConceptMap.SourceElementComponent sourceElement : group.getElement()) {
				builder
					.append("  Code[")
					.append(sourceElement.getCode())
					.append("] -> ")
					.append(sourceElement.getTarget().stream().map(ConceptMap.TargetElementComponent::getCode).collect(Collectors.joining(", ")))
					.append("\n");
			}

		}

		return builder.toString();
	}


}
