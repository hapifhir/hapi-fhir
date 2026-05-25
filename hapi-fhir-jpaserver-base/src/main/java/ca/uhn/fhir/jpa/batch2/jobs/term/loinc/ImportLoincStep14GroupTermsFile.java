package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyMetadataAttachmentJson;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyFileSetJson;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum;
import jakarta.annotation.Nonnull;
import org.apache.commons.csv.CSVRecord;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.ValueSet;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.trim;

public class ImportLoincStep14GroupTermsFile
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
				LoincUploadPropertiesEnum.LOINC_GROUP_TERMS_FILE,
				LoincUploadPropertiesEnum.LOINC_GROUP_TERMS_FILE_DEFAULT));
	}

	// FIXME: remove
	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull StepExecutionDetails<ImportLoincJobParameters, TerminologyFileSetJson> theStepExecutionDetails,
			@Nonnull IJobDataSink<TerminologyFileSetJson> theDataSink)
			throws JobExecutionFailedException {
		return super.run(theStepExecutionDetails, theDataSink);
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
		String groupId = trim(theRecord.get("GroupId"));
		String loincNumber = trim(theRecord.get("LoincNumber"));

		ValueSet valueSet = getValueSet(
				theStepExecutionDetails,
				theJobMetadata,
				theJobParameters,
				theData,
				theContext,
				groupId,
				ImportLoincStep13GroupFile.VS_URI_PREFIX + groupId,
				null,
				null);
		addCodeAsIncludeToValueSet(valueSet, ITermLoaderSvc.LOINC_URI, loincNumber, null);
	}
}
