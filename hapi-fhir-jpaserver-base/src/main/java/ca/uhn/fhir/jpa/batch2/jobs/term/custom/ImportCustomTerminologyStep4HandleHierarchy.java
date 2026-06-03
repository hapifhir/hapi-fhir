package ca.uhn.fhir.jpa.batch2.jobs.term.custom;

import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.BaseImportTerminologyFileCsvStep;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.BaseImportTerminologyFileStep;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyJobParameters;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyMetadataAttachmentJson;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyFileSetJson;
import jakarta.annotation.Nonnull;
import org.apache.commons.csv.CSVRecord;
import org.hl7.fhir.r4.model.CodeSystem;

import java.util.List;

import static ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyConstants.CUSTOM_HIERARCHY_FILE;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;

public class ImportCustomTerminologyStep4HandleHierarchy
		extends BaseImportTerminologyFileCsvStep<ImportTerminologyJobParameters, BaseImportTerminologyFileStep.MyBaseContext> {

	public static final String PARENT = "PARENT";
	public static final String CHILD = "CHILD";

	@Nonnull
	@Override
	public List<LoincFileNameSpecification> getFilesToProcess(
			StepExecutionDetails<ImportTerminologyJobParameters, ?> theStepExecutionDetails) {
		return List.of(new LoincFileNameSpecification(
				FileHandlingType.CSV_SPLIT_WITH_REPEAT_HEADER_1000_LINE_CHUNKS, t -> t.endsWith(CUSTOM_HIERARCHY_FILE)));
	}

	@Override
	protected void handleRecord(StepExecutionDetails<ImportTerminologyJobParameters, TerminologyFileSetJson> theStepExecutionDetails, ImportTerminologyMetadataAttachmentJson theJobMetadata, ImportTerminologyJobParameters theJobParameters, MyBaseContext theContext, CSVRecord theRecord, CodeSystem theCodeSystemToPopulate, TerminologyFileSetJson theData, String theSourceFilename) {
		String parent = trim(theRecord.get(PARENT));
		String child = trim(theRecord.get(CHILD));
		if (isNotBlank(parent) && isNotBlank(child)) {

			CodeSystem.ConceptDefinitionComponent childConcept = getOrAddConcept(theContext, child);

			CodeSystem.ConceptDefinitionComponent parentConcept = getOrAddConcept(theContext, parent);
			parentConcept.addConcept(childConcept);
		}

	}

	@Override
	protected MyBaseContext newContextObject(
			StepExecutionDetails<ImportTerminologyJobParameters, TerminologyFileSetJson> theStepExecutionDetails) {
		return new MyBaseContext();
	}
}
