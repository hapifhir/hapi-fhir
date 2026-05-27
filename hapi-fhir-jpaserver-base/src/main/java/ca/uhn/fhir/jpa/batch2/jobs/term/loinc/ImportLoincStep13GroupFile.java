package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyMetadataAttachmentJson;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyFileSetJson;
import jakarta.annotation.Nonnull;
import org.apache.commons.csv.CSVRecord;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.PrimitiveType;
import org.hl7.fhir.r4.model.ValueSet;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.trim;

/**
 * @see ImportLoincJobAppCtx#importLoincStep13GroupFile()
 */
public class ImportLoincStep13GroupFile extends BaseImportLoincStep<BaseImportLoincStep.MyBaseContext> {
	public static final String VS_URI_PREFIX = "http://loinc.org/vs/";

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
				FileHandlingType.CSV_SPLIT_WITH_REPEAT_HEADER_50000_LINE_CHUNKS,
				LoincUploadPropertiesEnum.LOINC_GROUP_FILE,
				LoincUploadPropertiesEnum.LOINC_GROUP_FILE_DEFAULT));
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
		String parentGroupId = trim(theRecord.get("ParentGroupId"));
		String groupId = trim(theRecord.get("GroupId"));
		String groupName = trim(theRecord.get("Group"));

		String parentGroupValueSetId;
		String groupValueSetId;
		parentGroupValueSetId = parentGroupId;
		groupValueSetId = groupId;

		ValueSet parentValueSet = getOrAddValueSet(
				theStepExecutionDetails,
				theJobMetadata,
				theData,
				theContext,
				parentGroupValueSetId,
				VS_URI_PREFIX + parentGroupId,
				null,
				null);

		ValueSet.ConceptSetComponent include = parentValueSet.getCompose().getIncludeFirstRep();

		Set<String> existingInclusions = include.getValueSet().stream()
				.map(PrimitiveType::getValue)
				.filter(Objects::nonNull)
				.collect(Collectors.toSet());
		if (!existingInclusions.contains(VS_URI_PREFIX + groupId)) {
			getRecordsAddedCounter(theStepExecutionDetails).incrementValueSetInclusionsAdded(1);
			include.addValueSet(VS_URI_PREFIX + groupId);
		}

		// Create group to set its name (terms are added in a different
		// handler)
		getOrAddValueSet(
				theStepExecutionDetails,
				theJobMetadata,
				theData,
				theContext,
				groupValueSetId,
				VS_URI_PREFIX + groupId,
				groupName,
				null);
	}
}
