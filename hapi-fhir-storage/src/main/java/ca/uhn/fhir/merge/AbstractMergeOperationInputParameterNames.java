/*-
 * #%L
 * HAPI FHIR Storage api
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
package ca.uhn.fhir.merge;

import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_PARAM_DELETE_SOURCE;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_UNDO_MERGE;

/**
 * Abstract base class for merge operation input parameter names.
 * Provides parameter names for different merge operations:
 * - Patient-specific: {@code $merge} and {@code $hapi.fhir.undo-merge}
 * - Generic: {@code $hapi.fhir.merge} (and future {@code $hapi.fhir.undo-merge} for non-Patient resources)
 */
public abstract class AbstractMergeOperationInputParameterNames {

	/**
	 * Factory method to get the appropriate parameter names implementation for a given operation.
	 *
	 * @param theOperationName the operation name (e.g., "$merge", "$hapi.fhir.merge", "$hapi.fhir.undo-merge"), or null for default (Patient merge)
	 * @return the appropriate implementation for the operation
	 */
	public static AbstractMergeOperationInputParameterNames forOperation(String theOperationName) {
		if (theOperationName == null
				|| OPERATION_MERGE.equals(theOperationName)
				|| OPERATION_UNDO_MERGE.equals(theOperationName)) {
			return new PatientMergeOperationInputParameterNames();
		} else {
			return new GenericMergeOperationInputParameterNames();
		}
	}

	/**
	 * @return the parameter name for the source resource (e.g., "source-patient" or "source-resource")
	 */
	public abstract String getSourceResourceParameterName();

	/**
	 * @return the parameter name for the target resource (e.g., "target-patient" or "target-resource")
	 */
	public abstract String getTargetResourceParameterName();

	/**
	 * @return the parameter name for source identifiers (e.g., "source-patient-identifier" or "source-resource-identifier")
	 */
	public abstract String getSourceIdentifiersParameterName();

	/**
	 * @return the parameter name for target identifiers (e.g., "target-patient-identifier" or "target-resource-identifier")
	 */
	public abstract String getTargetIdentifiersParameterName();

	/**
	 * @return the parameter name for the result resource (e.g., "result-patient" or "result-resource")
	 */
	public abstract String getResultResourceParameterName();

	/**
	 * @return the parameter name for the delete source flag (always "delete-source")
	 */
	public String getDeleteSourceParameterName() {
		return OPERATION_MERGE_PARAM_DELETE_SOURCE;
	}
}
