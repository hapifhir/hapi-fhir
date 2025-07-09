package ca.uhn.fhir.jpa.provider.merge;

import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_PARAM_SOURCE_PATIENT;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_PARAM_SOURCE_PATIENT_IDENTIFIER;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_PARAM_TARGET_PATIENT;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_PARAM_TARGET_PATIENT_IDENTIFIER;

public class PatientUndoMergeOperationInputParameters extends BaseMergeOperationsCommonInputParameters {

	@Override
	public String getSourceResourceParameterName() {
		return OPERATION_MERGE_PARAM_SOURCE_PATIENT;
	}

	@Override
	public String getTargetResourceParameterName() {
		return OPERATION_MERGE_PARAM_TARGET_PATIENT;
	}

	@Override
	public String getSourceIdentifiersParameterName() {
		return OPERATION_MERGE_PARAM_SOURCE_PATIENT_IDENTIFIER;
	}

	@Override
	public String getTargetIdentifiersParameterName() {
		return OPERATION_MERGE_PARAM_TARGET_PATIENT_IDENTIFIER;
	}
}
