package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyMetadataAttachmentJson;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyFileSetJson;
import jakarta.annotation.Nonnull;
import org.apache.commons.csv.CSVRecord;
import org.hl7.fhir.r4.model.CodeSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.LoincUploadPropertiesEnum.LOINC_HIERARCHY_FILE;
import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.LoincUploadPropertiesEnum.LOINC_HIERARCHY_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.LoincUploadPropertiesEnum.LOINC_HIERARCHY_FILE_DEFAULT_NEW;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;

/**
 * @see ImportLoincJobAppCtx#importLoincStep4HandleHierarchy()
 */
public class ImportLoincStep4HandleHierarchy extends BaseImportLoincStep<ImportLoincStep4HandleHierarchy.MyContext> {
	private static final Logger ourLog = LoggerFactory.getLogger(ImportLoincStep4HandleHierarchy.class);

	@Override
	protected MyContext newContextObject(
			StepExecutionDetails<ImportLoincJobParameters, TerminologyFileSetJson> theStepExecutionDetails) {
		return new MyContext();
	}

	@Nonnull
	@Override
	protected List<LoincFileNameSpecification> getFilesToProcess(
			StepExecutionDetails<ImportLoincJobParameters, ?> theStepExecutionDetails) {
		return List.of(new LoincFileNameSpecification(
				FileHandlingType.CSV_SPLIT_WITH_REPEAT_HEADER_1000_LINE_CHUNKS,
				LOINC_HIERARCHY_FILE,
				LOINC_HIERARCHY_FILE_DEFAULT,
				LOINC_HIERARCHY_FILE_DEFAULT_NEW));
	}

	@Override
	protected void handleRecord(
			StepExecutionDetails<ImportLoincJobParameters, TerminologyFileSetJson> theStepExecutionDetails,
			ImportTerminologyMetadataAttachmentJson theJobMetadata,
			ImportLoincJobParameters theJobParameters,
			MyContext theContext,
			CSVRecord theRecord,
			CodeSystem theCodeSystemToPopulate,
			TerminologyFileSetJson theData,
			String theSourceFilename) {
		String parentCode = trim(theRecord.get("IMMEDIATE_PARENT"));
		String childCode = trim(theRecord.get("CODE"));

		if (isNotBlank(parentCode) && isNotBlank(childCode)) {
			Map<String, CodeSystem.ConceptDefinitionComponent> codeToConceptMap = theContext.getCodeToConcept();
			CodeSystem.ConceptDefinitionComponent parentConcept =
					codeToConceptMap.computeIfAbsent(parentCode, this::newConcept);
			CodeSystem.ConceptDefinitionComponent childConcept =
					codeToConceptMap.computeIfAbsent(childCode, this::newConcept);

			parentConcept.addConcept(childConcept);
		}
	}

	@Override
	protected void syncToDb(
			ImportTerminologyMetadataAttachmentJson theJobMetadata,
			MyContext theCodeExtractionContext,
			CodeSystem theCodeSystemToPopulate,
			StepExecutionDetails<ImportLoincJobParameters, TerminologyFileSetJson> theStepExecutionDetails) {
		// Figure out which concepts are at the very top of the hierarchy (i.e. not children of any other concept)
		// and put them at the root of the CodeSystem to upload

		Set<String> childCodes = new HashSet<>();

		Map<String, CodeSystem.ConceptDefinitionComponent> codeToConcept = theCodeExtractionContext.getCodeToConcept();
		for (CodeSystem.ConceptDefinitionComponent concept : codeToConcept.values()) {
			for (CodeSystem.ConceptDefinitionComponent childConcept : concept.getConcept()) {
				childCodes.add(childConcept.getCode());
			}
		}

		int rootConceptCount = 0;
		for (CodeSystem.ConceptDefinitionComponent concept : codeToConcept.values()) {
			if (!childCodes.contains(concept.getCode())) {
				theCodeSystemToPopulate.addConcept(concept);
				rootConceptCount++;
			}
		}

		ourLog.info(
				"Imported {} hierarchy entries including {} root entries into LOINC",
				codeToConcept.size(),
				rootConceptCount);

		super.syncToDb(theJobMetadata, theCodeExtractionContext, theCodeSystemToPopulate, theStepExecutionDetails);
	}

	private CodeSystem.ConceptDefinitionComponent newConcept(String theCode) {
		CodeSystem.ConceptDefinitionComponent retVal = new CodeSystem.ConceptDefinitionComponent();
		retVal.setCode(theCode);
		return retVal;
	}

	protected static class MyContext extends MyBaseContext {
		private final Map<String, CodeSystem.ConceptDefinitionComponent> myCodeToConcept = new HashMap<>();

		@Override
		public Map<String, CodeSystem.ConceptDefinitionComponent> getCodeToConcept() {
			return myCodeToConcept;
		}
	}
}
