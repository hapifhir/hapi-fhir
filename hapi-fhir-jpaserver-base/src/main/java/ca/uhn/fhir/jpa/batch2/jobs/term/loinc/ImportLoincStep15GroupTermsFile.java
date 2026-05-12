package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum;
import jakarta.annotation.Nonnull;
import org.apache.commons.csv.CSVRecord;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.ValueSet;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.trim;

public class ImportLoincStep15GroupTermsFile
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
			LoincJobImportParameters theJobParameters,
			MyBaseContext theContext,
			CSVRecord theRecord,
			CodeSystem theCodeSystemToPopulate,
			ImportLoincFileSetJson theData) {
		String groupId = trim(theRecord.get("GroupId"));
		String loincNumber = trim(theRecord.get("LoincNumber"));

		ValueSet valueSet = getValueSet(
				theJobParameters,
				theData,
				theContext,
				groupId,
				ImportLoincStep14GroupFile.VS_URI_PREFIX + groupId,
				null,
				null);
		addCodeAsIncludeToValueSet(valueSet, ITermLoaderSvc.LOINC_URI, loincNumber, null);
	}
}
