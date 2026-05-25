package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyMetadataAttachmentJson;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyFileSetJson;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import jakarta.annotation.Nonnull;
import org.apache.commons.csv.CSVRecord;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.StringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.*;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;

public class ImportLoincStep16PartFile
		extends BaseImportLoincStepWithValueSetsAndConceptMaps<ImportLoincStep16PartFile.MyBaseContext> {
	private static final Logger ourLog = LoggerFactory.getLogger(ImportLoincStep16PartFile.class);

	@Override
	protected MyBaseContext newContextObject(
			StepExecutionDetails<ImportLoincJobParameters, TerminologyFileSetJson> theStepExecutionDetails) {
		return new MyBaseContext(theStepExecutionDetails);
	}

	@Nonnull
	@Override
	protected List<LoincFileNameSpecification> getFilesToProcess(
			StepExecutionDetails<ImportLoincJobParameters, ?> theStepExecutionDetails) {
		return List.of(
				new LoincFileNameSpecification(
						FileHandlingType.CSV_SPLIT_WITH_REPEAT_HEADER_50000_LINE_CHUNKS,
						LoincUploadPropertiesEnum.LOINC_PART_FILE,
						LoincUploadPropertiesEnum.LOINC_PART_FILE_DEFAULT));
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

// this is the code for the list (will repeat)
		String partNumber = trim(theRecord.get("PartNumber"));
//		String partTypeName = trim(theRecord.get("PartTypeName"));
//		String partName = trim(theRecord.get("PartName"));
		String partDisplayName = trim(theRecord.get("PartDisplayName"));

		// Per Dan's note, we include deprecated parts
		//		String status = trim(theRecord.get("Status"));
		//		if (!"ACTIVE".equals(status)) {
		//			return;
		//		}

		if (isNotBlank(partDisplayName)) {
			getOrAddConcept(theContext, theCodeSystemToPopulate, partNumber)
			.addDesignation()
				.setUse(new Coding(null, null, "PartDisplayName"))
				.setValue(partDisplayName);
		}


	}
}
