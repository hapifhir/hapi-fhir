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
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoConceptMap;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoValueSet;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyFileSetJson;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.svc.MockHapiTransactionService;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import ca.uhn.fhir.util.ClasspathUtil;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;
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

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
abstract class BaseImportLoincStepTest {
	@Spy
	IHapiTransactionService myTransactionService = new MockHapiTransactionService();
	@Mock
	IValidationSupport myValidationSupport;
	@Mock(strictness = Mock.Strictness.STRICT_STUBS)
	IJobPersistence myJobPersistence;
	@Mock
	ITermCodeSystemStorageSvc myTermCodeSystemStorageSvc;
	@Mock
	IJobDataSink<ImportLoincFileSetJson> myDataSink;
	@Mock
	IJobStepExecutionServices myJobExecutionServices;
	@Mock
	JobDefinition<ImportLoincJobParameters> myJobDefinition;
	@Mock
	IFhirResourceDaoValueSet<ValueSet> myValueSetDao;
	@Mock
	IFhirResourceDaoConceptMap<ConceptMap> myConceptMapDao;

	@Captor
	ArgumentCaptor<IBaseResource> myCodeSystemCaptor;
	@Captor
	ArgumentCaptor<ImportLoincFileSetJson> myFileSetCaptor;
	@Captor
	ArgumentCaptor<ValueSet> myValueSetCaptor;
	@Captor
	ArgumentCaptor<ConceptMap> myConceptMapCaptor;


	@Nonnull
	StepExecutionDetails<ImportLoincJobParameters, ImportLoincFileSetJson> newStepExecutionDetails(String classpath) {
		JobInstance instance = new JobInstance();
		instance.setInstanceId("my-instance-id");

		ImportLoincFileSetJson importLoincFileSetJson = new ImportLoincFileSetJson();
		importLoincFileSetJson.setCodeSystemStagingVersionId("my-staging-version-id");
		importLoincFileSetJson.setChunkForCurrentStep(new TerminologyFileSetJson.Chunk(classpath, "my-chunk-attachment-id"));
		importLoincFileSetJson.setLoincCodeSystemXml(ClasspathUtil.loadResource("loinc-ver/v269/loinc.xml"));

		importLoincFileSetJson.getLoincCodeSystem().setVersion("1.234");

		return new StepExecutionDetails<>(new ImportLoincJobParameters(), importLoincFileSetJson, instance, new WorkChunk(), myJobExecutionServices, myJobDefinition, "step-1", "step-2");
	}

	void mockFetchAttachment(String classpath) {
		when(myJobPersistence.fetchAttachmentById(eq("my-instance-id"), eq("my-chunk-attachment-id"))).thenReturn(new AttachmentDetails(ClasspathUtil.loadResourceAsStream(classpath), AttachmentContentTypeEnum.CSV, "Loinc.csv"));
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

}
