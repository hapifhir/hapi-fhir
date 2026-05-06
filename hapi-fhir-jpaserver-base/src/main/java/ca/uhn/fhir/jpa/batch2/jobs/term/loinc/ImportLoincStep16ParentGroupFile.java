package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum;
import jakarta.annotation.Nonnull;
import org.apache.commons.csv.CSVRecord;
import org.hl7.fhir.r4.model.CodeSystem;

import java.util.List;

import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_GROUP_TERMS_FILE;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_GROUP_TERMS_FILE_DEFAULT;
import static org.apache.commons.lang3.StringUtils.trim;

public class ImportLoincStep16ParentGroupFile extends BaseImportLoincStepWithValueSetsAndConceptMaps<BaseImportLoincStepWithValueSetsAndConceptMaps.MyBaseContext> {

	@Override
	protected MyBaseContext newContextObject(StepExecutionDetails<LoincJobImportParameters, ImportLoincFileSetJson> theStepExecutionDetails) {
		return new MyBaseContext();
	}

	@Nonnull
	@Override
	protected List<PropertyNameAndDefault> getFilesToProcess() {
		return List.of(
			new PropertyNameAndDefault(LoincUploadPropertiesEnum.LOINC_GROUP_TERMS_FILE, LoincUploadPropertiesEnum.LOINC_GROUP_TERMS_FILE_DEFAULT)
		);
	}

	@Override
	protected void handleRecord(LoincJobImportParameters theJobParameters, MyBaseContext theContext, CSVRecord theRecord, CodeSystem theCodeSystemToPopulate, ImportLoincFileSetJson theData) {
		String parentGroupId = trim(theRecord.get("ParentGroupId"));
		String parentGroupName = trim(theRecord.get("ParentGroup"));

		getValueSet(theJobParameters, theData, theContext, parentGroupId, ImportLoincStep14GroupFile.VS_URI_PREFIX + parentGroupId, parentGroupName, null);
	}

}
