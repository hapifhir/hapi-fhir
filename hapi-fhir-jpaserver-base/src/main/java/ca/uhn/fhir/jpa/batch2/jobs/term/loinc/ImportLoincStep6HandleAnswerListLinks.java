package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyMetadataAttachmentJson;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyFileSetJson;
import ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum;
import jakarta.annotation.Nonnull;
import org.apache.commons.csv.CSVRecord;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.StringType;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;

public class ImportLoincStep6HandleAnswerListLinks
		extends BaseImportLoincStepWithValueSetsAndConceptMaps<
				BaseImportLoincStepWithValueSetsAndConceptMaps.MyBaseContext> {

	@Override
	protected MyBaseContext newContextObject(
			StepExecutionDetails<ImportLoincJobParameters, TerminologyFileSetJson> theStepExecutionDetails) {
		return new MyBaseContext(theStepExecutionDetails);
	}

	@Nonnull
	@Override
	protected List<LoincFileNameSpecification> getFilesToProcess(
			StepExecutionDetails<ImportLoincJobParameters, ?> theStepExecutionDetails) {
		return List.of(new LoincFileNameSpecification(
				FileHandlingType.CSV_SPLIT_WITH_REPEAT_HEADER_50000_LINE_CHUNKS,
				LoincUploadPropertiesEnum.LOINC_ANSWERLIST_LINK_FILE,
				LoincUploadPropertiesEnum.LOINC_ANSWERLIST_LINK_FILE_DEFAULT));
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
		String applicableContext = trim(theRecord.get("ApplicableContext"));

		/*
		 * Per Dan V's Notes:
		 *
		 * Note: in our current format, we support binding of the same
		 * LOINC term to different answer lists depending on the panel
		 * context. I don’t believe there’s a way to handle that in
		 * the current FHIR spec, so I might suggest we discuss either
		 * only binding the “default” (non-context specific) list or
		 * if multiple bindings could be supported.
		 */
		if (isNotBlank(applicableContext)) {
			return;
		}

		String answerListId = trim(theRecord.get("AnswerListId"));
		if (isBlank(answerListId)) {
			return;
		}

		String loincNumber = trim(theRecord.get("LoincNumber"));
		if (isBlank(loincNumber)) {
			return;
		}

		CodeSystem.ConceptDefinitionComponent loincCode =
				getOrAddConcept(theContext, theCodeSystemToPopulate, loincNumber);
		loincCode.addProperty().setCode("answer-list").setValue(new StringType(answerListId));

		CodeSystem.ConceptDefinitionComponent answerListCode =
				getOrAddConcept(theContext, theCodeSystemToPopulate, answerListId);
		answerListCode.addProperty().setCode("answers-for").setValue(new StringType(loincNumber));
	}
}
