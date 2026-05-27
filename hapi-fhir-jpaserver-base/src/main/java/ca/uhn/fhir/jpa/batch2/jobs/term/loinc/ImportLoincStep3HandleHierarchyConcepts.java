package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyMetadataAttachmentJson;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyFileSetJson;
import jakarta.annotation.Nonnull;
import org.apache.commons.csv.CSVRecord;
import org.hl7.fhir.r4.model.CodeSystem;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.LoincUploadPropertiesEnum.LOINC_HIERARCHY_FILE;
import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.LoincUploadPropertiesEnum.LOINC_HIERARCHY_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.LoincUploadPropertiesEnum.LOINC_HIERARCHY_FILE_DEFAULT_NEW;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;

/**
 * @see ImportLoincJobAppCtx#importLoincStep3HandleHierarchyConcepts()
 */
public class ImportLoincStep3HandleHierarchyConcepts
		extends BaseImportLoincStep<ImportLoincStep3HandleHierarchyConcepts.MyContext> {

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
		if (isNotBlank(parentCode) && theContext.getSeenCodes().add(parentCode)) {
			theCodeSystemToPopulate.addConcept().setCode(parentCode);
		}

		String childCode = trim(theRecord.get("CODE"));
		if (isNotBlank(childCode) && theContext.getSeenCodes().add(childCode)) {
			theCodeSystemToPopulate.addConcept().setCode(childCode);
		}
	}

	protected static class MyContext extends MyBaseContext {
		private final Set<String> mySeenCodes = new HashSet<>();

		public Set<String> getSeenCodes() {
			return mySeenCodes;
		}
	}
}
