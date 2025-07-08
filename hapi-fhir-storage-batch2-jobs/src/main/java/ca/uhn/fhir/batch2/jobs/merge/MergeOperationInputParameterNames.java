package ca.uhn.fhir.batch2.jobs.merge;

import ca.uhn.fhir.rest.server.provider.ProviderConstants;

import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_PARAM_DELETE_SOURCE;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_PARAM_RESULT_PATIENT;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_PARAM_SOURCE_PATIENT;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_PARAM_SOURCE_PATIENT_IDENTIFIER;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_PARAM_TARGET_PATIENT;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_PARAM_TARGET_PATIENT_IDENTIFIER;

public class MergeOperationInputParameterNames {

	public String getSourceResourceParameterName() {
		return OPERATION_MERGE_PARAM_SOURCE_PATIENT;
	}

	public String getTargetResourceParameterName() {
		return OPERATION_MERGE_PARAM_TARGET_PATIENT;
	}

	public String getSourceIdentifiersParameterName() {
		return OPERATION_MERGE_PARAM_SOURCE_PATIENT_IDENTIFIER;
	}

	public String getTargetIdentifiersParameterName() {
		return OPERATION_MERGE_PARAM_TARGET_PATIENT_IDENTIFIER;
	}

	public String getResultResourceParameterName() {
		return OPERATION_MERGE_PARAM_RESULT_PATIENT;
	}

	public String getDeleteSourceParameterName() {
		return OPERATION_MERGE_PARAM_DELETE_SOURCE;
	}
}
