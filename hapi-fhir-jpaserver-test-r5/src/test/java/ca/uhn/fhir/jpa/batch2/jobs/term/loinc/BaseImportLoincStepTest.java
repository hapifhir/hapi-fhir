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
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.LookupCodeRequest;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoCodeSystem;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoConceptMap;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoValueSet;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyMetadataAttachmentJson;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyFileSetJson;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.svc.MockHapiTransactionService;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.ClasspathUtil;
import ca.uhn.fhir.util.JsonUtil;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.r4.model.CanonicalType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static org.apache.commons.lang3.StringUtils.compareIgnoreCase;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
abstract class BaseImportLoincStepTest {
	@Spy
	IHapiTransactionService myTransactionService = new MockHapiTransactionService();
	@Mock
	IValidationSupport myValidationSupport;
	@Mock
	IJobPersistence myJobPersistence;
	@Mock
	ITermCodeSystemStorageSvc myTermCodeSystemStorageSvc;
	@Mock
	IJobDataSink<TerminologyFileSetJson> myDataSink;
	@Mock
	IJobStepExecutionServices myJobExecutionServices;
	@Mock
	JobDefinition<ImportLoincJobParameters> myJobDefinition;
	@Mock
	IFhirResourceDaoValueSet<ValueSet> myValueSetDao;
	@Mock
	IFhirResourceDaoConceptMap<ConceptMap> myConceptMapDao;
	@Mock
	IFhirResourceDaoCodeSystem<CodeSystem> myCodeSystemDao;
	@Mock
	DaoRegistry myDaoRegistry;

	@Captor
	ArgumentCaptor<CodeSystem> myCodeSystemCaptor;
	@Captor
	ArgumentCaptor<String> myStepIdCaptor;
	@Captor
	ArgumentCaptor<TerminologyFileSetJson> myFileSetCaptor;
	@Captor
	ArgumentCaptor<ValueSet> myValueSetCaptor;
	@Captor
	ArgumentCaptor<ConceptMap> myConceptMapCaptor;
	@Captor
	ArgumentCaptor<AttachmentDetails> myAttachmentDetailsCaptor;


	@Nonnull
	StepExecutionDetails<ImportLoincJobParameters, TerminologyFileSetJson> newStepExecutionDetails(String classpath) {
		JobInstance instance = new JobInstance();
		instance.setInstanceId("my-instance-id");

		TerminologyFileSetJson importLoincFileSetJson = newData();

		if (classpath != null) {
			importLoincFileSetJson.setChunkForCurrentStep(new TerminologyFileSetJson.Chunk(classpath, "my-chunk-attachment-id"));
		}

		return new StepExecutionDetails<>(new ImportLoincJobParameters(), importLoincFileSetJson, instance, new WorkChunk(), myJobExecutionServices, myJobDefinition, "step-1", "step-2");
	}

	@Nonnull
	TerminologyFileSetJson newData() {
		return new TerminologyFileSetJson();
	}

	void mockFetchAttachment(String classpath) {
		when(myJobPersistence.fetchAttachmentById(eq("my-instance-id"), eq("my-chunk-attachment-id"))).thenReturn(new AttachmentDetails(ClasspathUtil.loadResourceAsStream(classpath), AttachmentContentTypeEnum.CSV, "Loinc.csv"));
	}

	void mockJobExecutionServices() {
		when(myJobExecutionServices.newRequestDetails(any())).thenReturn(new SystemRequestDetails());
	}

	void mockFetchPropertiesFileAttachnemtNotFound() {
		when(myJobPersistence.fetchAttachmentByFilename(eq("my-instance-id"), eq(LoincUploadPropertiesEnum.LOINC_UPLOAD_PROPERTIES_FILE.getCode()))).thenThrow(new ResourceNotFoundException("Not found", null));
	}

	void mockValidationSupportLookupCodeAlwaysSucceed() {
		when(myValidationSupport.lookupCode(any(), any(LookupCodeRequest.class))).thenReturn(new IValidationSupport.LookupCodeResult().setFound(true));
	}

	void mockFetchJobMetadataAttachment() {
		CodeSystem codeSystem = ClasspathUtil.loadResource(FhirContext.forR4Cached(), CodeSystem.class, "loinc-ver/v269/loinc.xml");
		codeSystem.setVersion("1.234");

		ImportTerminologyMetadataAttachmentJson jobMetadata = new ImportTerminologyMetadataAttachmentJson();
		jobMetadata.setCodeSystemStagingVersionId("my-staging-version-id");
		jobMetadata.setCodeSystem(codeSystem);
		String jobMetadataSerialized = JsonUtil.serialize(jobMetadata);
		when(myJobPersistence.fetchAttachmentByFilename(eq("my-instance-id"), eq(ImportTerminologyMetadataAttachmentJson.ATTACHMENT_FILENAME))).thenReturn(new AttachmentDetails(jobMetadataSerialized.getBytes(StandardCharsets.UTF_8), AttachmentContentTypeEnum.CSV, "Loinc.csv"));
	}


	void mockDaoRegistryConceptMap() {
		when(myDaoRegistry.getResourceDao(eq("ConceptMap"))).thenReturn(myConceptMapDao);
	}

	void mockDaoRegistryValueSet() {
		when(myDaoRegistry.getResourceDao(eq("ValueSet"))).thenReturn(myValueSetDao);
	}


	static String renderHierarchy(CodeSystem theCs) {
		return renderHierarchy(theCs, false);
	}

	static String renderHierarchy(CodeSystem theCs, boolean theIncludeProperties) {
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

	static String renderValueSetCompose(ValueSet theVs) {
		StringBuilder builder = new StringBuilder();

		List<ValueSet.ConceptSetComponent> include = theVs.getCompose().getInclude();
		for (ValueSet.ConceptSetComponent next : include) {
			builder.append("INCLUDE:\n");
			for (CanonicalType valueSet : next.getValueSet()) {
				builder.append("ValueSet: ").append(valueSet.getValue()).append("\n");
			}
			if (isNotBlank(next.getSystem())) {
				builder.append(next.getSystem()).append("\n");
				for (ValueSet.ConceptReferenceComponent concept : next.getConcept()) {
					builder.append("  ").append(concept.getCode()).append("\n");
				}
			}
		}

		return builder.toString();
	}

	@Nonnull
	protected List<String> renderEmittedChunks() {
		return BaseImportLoincStepTest.renderEmittedChunks(myStepIdCaptor, myFileSetCaptor);
	}

	@Nonnull
    public static List<String> renderEmittedChunks(ArgumentCaptor<String> theStepIdCaptor, ArgumentCaptor<TerminologyFileSetJson> theTerminologyFileSetCaptor) {
        assertEquals(theStepIdCaptor.getAllValues().size(), theTerminologyFileSetCaptor.getAllValues().size());
        List<String> emittedChunks = new ArrayList<>();
        for (int i = 0; i < theTerminologyFileSetCaptor.getAllValues().size(); i++) {
            String targetStep = theStepIdCaptor.getAllValues().get(i);
			TerminologyFileSetJson data = theTerminologyFileSetCaptor.getAllValues().get(i);

	        TerminologyFileSetJson.Chunk chunk = data.getChunkForCurrentStep();
			if (chunk != null) {
				emittedChunks.add(targetStep + " -> Chunk[" + chunk.getSourceFilename() + " | " + chunk.getAttachmentId() + "]");
			}

			Set<String> resourcesToActivate = data.getResourcesToActivate();
			if (!resourcesToActivate.isEmpty()) {
				emittedChunks.add(targetStep + " -> ResourcesToActivate" + new TreeSet<>(resourcesToActivate));
			}

			Map<String, TerminologyFileSetJson.RecordsAddedCounter> stepIdToRecordsAdded = data.getStepIdToRecordsAdded();
			if (!stepIdToRecordsAdded.isEmpty()) {
				for (Map.Entry<String, TerminologyFileSetJson.RecordsAddedCounter> next : stepIdToRecordsAdded.entrySet()) {
					String sourceStep = next.getKey();
					TerminologyFileSetJson.RecordsAddedCounter recordsCounter = next.getValue();
					emittedChunks.add(targetStep + " -> RecordsAdded: From[" + sourceStep + "] Counts" + recordsCounter);
				}
			}


        }
        return emittedChunks;
    }
}
