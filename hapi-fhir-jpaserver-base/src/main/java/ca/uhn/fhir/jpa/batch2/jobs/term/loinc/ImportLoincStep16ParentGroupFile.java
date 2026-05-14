package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum;
import jakarta.annotation.Nonnull;
import org.apache.commons.csv.CSVRecord;
import org.hl7.fhir.r4.model.CodeSystem;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.trim;

public class ImportLoincStep16ParentGroupFile
		extends BaseImportLoincStepWithValueSetsAndConceptMaps<
				BaseImportLoincStepWithValueSetsAndConceptMaps.MyBaseContext> {

	@Override
	protected MyBaseContext newContextObject(
			StepExecutionDetails<LoincJobImportParameters, ImportLoincFileSetJson> theStepExecutionDetails) {
		return new MyBaseContext(theStepExecutionDetails);
	}

	@Nonnull
	@Override
	protected List<LoincFileNameSpecification> getFilesToProcess() {
		return List.of(new LoincFileNameSpecification(
				LoincUploadPropertiesEnum.LOINC_GROUP_TERMS_FILE,
				LoincUploadPropertiesEnum.LOINC_GROUP_TERMS_FILE_DEFAULT));
	}

	@Override
	protected void handleRecord(
            StepExecutionDetails<LoincJobImportParameters, ImportLoincFileSetJson> theStepExecutionDetails, LoincJobImportParameters theJobParameters,
            MyBaseContext theContext,
            CSVRecord theRecord,
            CodeSystem theCodeSystemToPopulate,
            ImportLoincFileSetJson theData, String theSourceFilename) {
		String parentGroupId = trim(theRecord.get("ParentGroupId"));
		String parentGroupName = trim(theRecord.get("ParentGroup"));

		getValueSet(
				theJobParameters,
				theData,
				theContext,
				parentGroupId,
				ImportLoincStep14GroupFile.VS_URI_PREFIX + parentGroupId,
				parentGroupName,
				null);
	}
}
