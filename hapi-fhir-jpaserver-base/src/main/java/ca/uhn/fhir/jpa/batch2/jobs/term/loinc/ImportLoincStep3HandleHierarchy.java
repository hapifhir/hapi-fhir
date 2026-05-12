package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.batch2.api.StepExecutionDetails;
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

import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_HIERARCHY_FILE;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_HIERARCHY_FILE_DEFAULT;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;

public class ImportLoincStep3HandleHierarchy extends BaseImportLoincStep<ImportLoincStep3HandleHierarchy.MyContext> {
	private static final Logger ourLog = LoggerFactory.getLogger(ImportLoincStep3HandleHierarchy.class);

	@Override
	protected MyContext newContextObject(
			StepExecutionDetails<LoincJobImportParameters, ImportLoincFileSetJson> theStepExecutionDetails) {
		return new MyContext(new HashMap<>());
	}

	@Nonnull
	@Override
	protected List<LoincFileNameSpecification> getFilesToProcess() {
		return List.of(new LoincFileNameSpecification(LOINC_HIERARCHY_FILE, LOINC_HIERARCHY_FILE_DEFAULT));
	}

	@Override
	protected void handleRecord(
            LoincJobImportParameters theJobParameters,
            MyContext theContext,
            CSVRecord theRecord,
            CodeSystem theCodeSystemToPopulate,
            ImportLoincFileSetJson theData, String theSourceFilename) {
		String parentCode = trim(theRecord.get("IMMEDIATE_PARENT"));
		String childCode = trim(theRecord.get("CODE"));

		if (isNotBlank(parentCode) && isNotBlank(childCode)) {
			Map<String, CodeSystem.ConceptDefinitionComponent> codeToConceptMap = theContext.codeToConcept();
			CodeSystem.ConceptDefinitionComponent parentConcept =
					codeToConceptMap.computeIfAbsent(parentCode, pc -> newConcept(pc));
			CodeSystem.ConceptDefinitionComponent childConcept =
					codeToConceptMap.computeIfAbsent(childCode, pc -> newConcept(pc));

			parentConcept.addConcept(childConcept);
		}
	}

	@Override
	protected void afterCsvProcessingComplete(
			MyContext theCodeExtractionContext,
			CodeSystem theCodeSystemToPopulate,
			StepExecutionDetails<LoincJobImportParameters, ImportLoincFileSetJson> theStepExecutionDetails) {
		super.afterCsvProcessingComplete(theCodeExtractionContext, theCodeSystemToPopulate, theStepExecutionDetails);

		// Figure out which concepts are at the very top of the hierarchy (i.e. not children of any other concept)
		// and put them at the root of the CodeSystem to upload

		Set<String> childCodes = new HashSet<>();

		Map<String, CodeSystem.ConceptDefinitionComponent> codeToConcept = theCodeExtractionContext.codeToConcept();
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
	}

	private CodeSystem.ConceptDefinitionComponent newConcept(String theCode) {
		CodeSystem.ConceptDefinitionComponent retVal = new CodeSystem.ConceptDefinitionComponent();
		retVal.setCode(theCode);
		return retVal;
	}

	protected record MyContext(Map<String, CodeSystem.ConceptDefinitionComponent> codeToConcept) {}
}
