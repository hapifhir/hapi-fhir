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
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_HIERARCHY_FILE_DEFAULT_NEW;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;

public class ImportLoincStep3aHandleHierarchyConcepts extends BaseImportLoincStep<ImportLoincStep3aHandleHierarchyConcepts.MyContext> {

	@Override
	protected MyContext newContextObject(
			StepExecutionDetails<ImportLoincJobParameters, ImportLoincFileSetJson> theStepExecutionDetails) {
		return new MyContext(new HashSet<>());
	}

	@Nonnull
	@Override
	protected List<LoincFileNameSpecification> getFilesToProcess(StepExecutionDetails<ImportLoincJobParameters, ?> theStepExecutionDetails) {
		return List.of(new LoincFileNameSpecification(LOINC_HIERARCHY_FILE,
			LOINC_HIERARCHY_FILE_DEFAULT,
			LOINC_HIERARCHY_FILE_DEFAULT_NEW
		));
	}

	@Nonnull
	@Override
	public FileHandlingType getFileHandlingType() {
		return FileHandlingType.CSV_SPLIT_WITH_REPEAT_HEADER_1000_LINE_CHUNKS;
	}

	@Override
	protected void handleRecord(
		StepExecutionDetails<ImportLoincJobParameters, ImportLoincFileSetJson> theStepExecutionDetails, ImportLoincJobParameters theJobParameters,
		MyContext theContext,
		CSVRecord theRecord,
		CodeSystem theCodeSystemToPopulate,
		ImportLoincFileSetJson theData, String theSourceFilename) {

		String parentCode = trim(theRecord.get("IMMEDIATE_PARENT"));
		if (isNotBlank(parentCode) && theContext.seenCodes().add(parentCode)) {
			theCodeSystemToPopulate.addConcept().setCode(parentCode);
		}

		String childCode = trim(theRecord.get("CODE"));
		if (isNotBlank(childCode) && theContext.seenCodes().add(childCode)) {
			theCodeSystemToPopulate.addConcept().setCode(childCode);
		}
	}


	protected record MyContext(Set<String> seenCodes) {}
}
