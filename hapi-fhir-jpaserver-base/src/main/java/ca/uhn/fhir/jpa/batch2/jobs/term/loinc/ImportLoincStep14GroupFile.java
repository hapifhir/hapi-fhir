package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum;
import jakarta.annotation.Nonnull;
import org.apache.commons.csv.CSVRecord;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.ValueSet;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.trim;

public class ImportLoincStep14GroupFile
		extends BaseImportLoincStepWithValueSetsAndConceptMaps<
				BaseImportLoincStepWithValueSetsAndConceptMaps.MyBaseContext> {
	public static final String VS_URI_PREFIX = "http://loinc.org/vs/";

	@Override
	protected MyBaseContext newContextObject(
			StepExecutionDetails<LoincJobImportParameters, ImportLoincFileSetJson> theStepExecutionDetails) {
		return new MyBaseContext(theStepExecutionDetails);
	}

	@Nonnull
	@Override
	protected List<PropertyNameAndDefault> getFilesToProcess() {
		return List.of(new PropertyNameAndDefault(
				LoincUploadPropertiesEnum.LOINC_GROUP_FILE, LoincUploadPropertiesEnum.LOINC_GROUP_FILE_DEFAULT));
	}

	@Override
	protected void handleRecord(
			LoincJobImportParameters theJobParameters,
			MyBaseContext theContext,
			CSVRecord theRecord,
			CodeSystem theCodeSystemToPopulate,
			ImportLoincFileSetJson theData) {
		String parentGroupId = trim(theRecord.get("ParentGroupId"));
		String groupId = trim(theRecord.get("GroupId"));
		String groupName = trim(theRecord.get("Group"));

		String parentGroupValueSetId;
		String groupValueSetId;
		parentGroupValueSetId = parentGroupId;
		groupValueSetId = groupId;

		ValueSet parentValueSet = getValueSet(
				theJobParameters,
				theData,
				theContext,
				parentGroupValueSetId,
				VS_URI_PREFIX + parentGroupId,
				null,
				null);
		parentValueSet.getCompose().getIncludeFirstRep().addValueSet(VS_URI_PREFIX + groupId);

		// Create group to set its name (terms are added in a different
		// handler)
		getValueSet(theJobParameters, theData, theContext, groupValueSetId, VS_URI_PREFIX + groupId, groupName, null);
	}
}
