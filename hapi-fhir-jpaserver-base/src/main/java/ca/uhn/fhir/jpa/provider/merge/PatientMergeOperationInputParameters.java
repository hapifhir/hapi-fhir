/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.jpa.provider.merge;

import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_PARAM_RESULT_PATIENT;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_PARAM_SOURCE_PATIENT;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_PARAM_SOURCE_PATIENT_IDENTIFIER;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_PARAM_TARGET_PATIENT;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_PARAM_TARGET_PATIENT_IDENTIFIER;

/**
 * See <a href="https://build.fhir.org/patient-operation-merge.html">Patient $merge spec</a>
 */
public class PatientMergeOperationInputParameters extends BaseMergeOperationInputParameters {
	public PatientMergeOperationInputParameters(int theResourceLimit) {
		super(theResourceLimit);
	}

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

	@Override
	public String getResultResourceParameterName() {
		return OPERATION_MERGE_PARAM_RESULT_PATIENT;
	}
}
