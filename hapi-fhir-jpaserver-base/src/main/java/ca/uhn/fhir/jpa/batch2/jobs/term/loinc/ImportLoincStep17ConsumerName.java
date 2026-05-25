package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyMetadataAttachmentJson;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyFileSetJson;
import jakarta.annotation.Nonnull;
import org.apache.commons.csv.CSVRecord;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.Coding;

import java.util.List;

import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.LoincUploadPropertiesEnum.LOINC_CONSUMER_NAME_FILE;
import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.LoincUploadPropertiesEnum.LOINC_CONSUMER_NAME_FILE_DEFAULT;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.trim;

public class ImportLoincStep17ConsumerName
		extends BaseImportLoincStepWithValueSetsAndConceptMaps<
				BaseImportLoincStepWithValueSetsAndConceptMaps.MyBaseContext> {

	@Override
	protected MyBaseContext newContextObject(
			StepExecutionDetails<ImportLoincJobParameters, TerminologyFileSetJson> theStepExecutionDetails) {
		return new MyBaseContext();
	}

	@Nonnull
	@Override
	protected List<LoincFileNameSpecification> getFilesToProcess(
			StepExecutionDetails<ImportLoincJobParameters, ?> theStepExecutionDetails) {
		return List.of(new LoincFileNameSpecification(
				FileHandlingType.CSV_SPLIT_WITH_REPEAT_HEADER_1000_LINE_CHUNKS,
				LOINC_CONSUMER_NAME_FILE,
				LOINC_CONSUMER_NAME_FILE_DEFAULT));
	}

	@Override
	protected void handleRecord(
			StepExecutionDetails<ImportLoincJobParameters, TerminologyFileSetJson> theStepExecutionDetails,
			ImportTerminologyMetadataAttachmentJson theJobMetadata,
			ImportLoincJobParameters theJobParameters,
			MyBaseContext theContext,
			CSVRecord theRecord,
			CodeSystem theCodeSystemToPopulate,
			TerminologyFileSetJson theData,
			String theSourceFilename) {
		String loincNumber = trim(theRecord.get("LoincNumber"));
		if (isBlank(loincNumber)) {
			return;
		}

		String consumerName = trim(theRecord.get("ConsumerName"));
		if (isBlank(consumerName)) {
			return;
		}

		CodeSystem.ConceptDefinitionComponent loincCode =
				getOrAddConcept(theContext, theCodeSystemToPopulate, loincNumber);
		loincCode
				.addDesignation()
				.setUse(new Coding(null, null, "ConsumerName"))
				.setValue(consumerName);
	}
}
