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
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyFileSetJson;
import ca.uhn.fhir.jpa.term.UploadStatistics;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import ca.uhn.fhir.rest.server.interceptor.ResponseSizeCapturingInterceptor;
import ca.uhn.fhir.util.ClasspathUtil;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.IdType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImportLoincStep3HandleHierarchyTest {

	@Mock
	private IJobPersistence myJobPersistence;
	@Mock
	private ITermCodeSystemStorageSvc myTermCodeSystemStorageSvc;
	@Mock
	private IJobDataSink<ImportLoincFileSetJson> myDataSink;
	@Mock
	private IJobStepExecutionServices myJobExecutionServices;
	@Mock
	private JobDefinition<ImportLoincJobParameters> myJobDefinition;

	@InjectMocks
	private ImportLoincStep3HandleHierarchy mySvc;

	@Captor
	private ArgumentCaptor<IBaseResource> myCodeSystemCaptor;
	@Captor
	private ArgumentCaptor<ImportLoincFileSetJson> myFileSetCaptor;


	@Test
	void run_LoadCodes() {
		// Setup
		when(myJobPersistence.fetchAttachmentById(eq("my-instance-id"), eq("my-chunk-attachment-id"))).thenReturn(new AttachmentDetails(ClasspathUtil.loadResourceAsStream("loinc-ver/v269/AccessoryFiles/MultiAxialHierarchy/MultiAxialHierarchy.csv"), AttachmentContentTypeEnum.CSV, "Loinc.csv"));
		when(myTermCodeSystemStorageSvc.uploadCodeSystemConcepts(any())).thenReturn(new UploadStatistics(new IdType()));

		// Test
		JobInstance instance = new JobInstance();
		instance.setInstanceId("my-instance-id");

		ImportLoincFileSetJson importLoincFileSetJson = new ImportLoincFileSetJson();
		importLoincFileSetJson.setChunkForCurrentStep(new TerminologyFileSetJson.Chunk("file.csv", "my-chunk-attachment-id"));
		importLoincFileSetJson.setLoincCodeSystemXml(ClasspathUtil.loadResource("loinc-ver/v269/loinc.xml"));

		StepExecutionDetails<ImportLoincJobParameters, ImportLoincFileSetJson> stepExecutionDetails = new StepExecutionDetails<>(new ImportLoincJobParameters(), importLoincFileSetJson, instance, new WorkChunk(), myJobExecutionServices, myJobDefinition, "step-1", "step-2");

		mySvc.run(stepExecutionDetails, myDataSink);

		// Verify
		verify(myTermCodeSystemStorageSvc, times(1)).uploadCodeSystemConcepts(myCodeSystemCaptor.capture());
		CodeSystem cs = (CodeSystem) myCodeSystemCaptor.getValue();
		String hierarchy = renderHierarchy(cs);
		String expected = """
			-LP31755-9
			  -LP14559-6
			    -LP98185-9
			      -LP14082-9
			        -LP52258-8
			          -41599-2
			        -LP52260-4
			          -41602-4
			        -LP52960-9
			""";
		assertEquals(expected, hierarchy);

		verify(myDataSink, times(1)).accept(myFileSetCaptor.capture());
	}

	public static String renderHierarchy(CodeSystem theCs) {
		return renderHierarchy(theCs, false);
	}
	public static String renderHierarchy(CodeSystem theCs, boolean theIncludeProperties) {
		StringBuilder target = new StringBuilder();
		appendToHierarchy(theCs.getConcept(), 0, target, theIncludeProperties);
		return target.toString();
	}

	private static void appendToHierarchy(List<CodeSystem.ConceptDefinitionComponent> theConceptList, int theDepth, StringBuilder theTarget, boolean theIncludeProperties) {
		for (CodeSystem.ConceptDefinitionComponent next : theConceptList) {
			theTarget.append("  ".repeat(theDepth));
			theTarget.append("-");
			theTarget.append(next.getCode()).append("\n");
			if (theIncludeProperties) {
				for (CodeSystem.ConceptPropertyComponent property : next.getProperty()) {
					theTarget.append("  ".repeat(theDepth));
					theTarget.append("  -Property[").append(property.getCode()).append(": ").append(FhirContext.forR4Cached().newJsonParser().encodeToString(property.getValue())).append("\n");
				}
				for (CodeSystem.ConceptDefinitionDesignationComponent designation : next.getDesignation()) {
					theTarget.append("  ".repeat(theDepth));
					theTarget.append("  -Designation[lang=");
					theTarget.append(designation.getLanguage()).append(", use=");
					theTarget.append(FhirContext.forR4Cached().newJsonParser().encodeToString(designation.getUse()));
					theTarget.append("]: ").append(designation.getValue()).append("\n");
				}
			}
			appendToHierarchy(next.getConcept(), theDepth + 1, theTarget, theIncludeProperties);
		}
	}

}
