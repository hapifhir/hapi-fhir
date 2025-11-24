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

// Created by claude-sonnet-4-5

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.util.ParametersUtil;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;

/**
 * Utility class for building FHIR Parameters resources for merge operations.
 */
public class MergeOperationParametersUtil {

	private MergeOperationParametersUtil() {
		// Utility class
	}

	/**
	 * Builds the output Parameters resource for a Patient $merge operation following the FHIR specification.
	 * <p>
	 * The output Parameters includes:
	 * <ul>
	 *   <li>input - The original input parameters</li>
	 *   <li>outcome - OperationOutcome describing the merge result</li>
	 *   <li>result - Updated target patient (optional, not included in preview mode)</li>
	 *   <li>task - Task resource tracking the merge (optional)</li>
	 * </ul>
	 * </p>
	 *
	 * @param theFhirContext FHIR context for building Parameters resource
	 * @param theMergeOutcome Merge operation outcome containing the result
	 * @param theInputParameters Original input parameters to include in output
	 * @return Parameters resource with merge operation output in FHIR $merge format
	 */
	public static IBaseParameters buildMergeOperationOutputParameters(
			FhirContext theFhirContext, MergeOperationOutcome theMergeOutcome, IBaseResource theInputParameters) {

		// Extract components from MergeOperationOutcome and delegate to overloaded method
		return buildMergeOperationOutputParameters(
				theFhirContext,
				theMergeOutcome.getOperationOutcome(),
				theMergeOutcome.getUpdatedTargetResource(),
				theMergeOutcome.getTask(),
				theInputParameters);
	}

	/**
	 * Builds the output Parameters resource for a Patient $merge operation from individual components.
	 * <p>
	 * This overload is useful when you have the merge result components separately rather than
	 * wrapped in a {@link MergeOperationOutcome} object.
	 * </p>
	 *
	 * @param theFhirContext FHIR context for building Parameters resource
	 * @param theOperationOutcome Operation outcome describing the merge result
	 * @param theUpdatedTargetResource Updated target patient resource (may be null in preview mode)
	 * @param theTask Task resource tracking the merge operation (may be null)
	 * @param theInputParameters Original input parameters to include in output
	 * @return Parameters resource with merge operation output in FHIR $merge format
	 */
	public static IBaseParameters buildMergeOperationOutputParameters(
			FhirContext theFhirContext,
			IBaseResource theOperationOutcome,
			IBaseResource theUpdatedTargetResource,
			IBaseResource theTask,
			IBaseResource theInputParameters) {

		IBaseParameters retVal = ParametersUtil.newInstance(theFhirContext);

		// Add input parameters
		ParametersUtil.addParameterToParameters(
				theFhirContext, retVal, ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_INPUT, theInputParameters);

		// Add operation outcome
		ParametersUtil.addParameterToParameters(
				theFhirContext, retVal, ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_OUTCOME, theOperationOutcome);

		// Add updated target resource if present
		if (theUpdatedTargetResource != null) {
			ParametersUtil.addParameterToParameters(
					theFhirContext,
					retVal,
					ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_RESULT,
					theUpdatedTargetResource);
		}

		// Add task if present
		if (theTask != null) {
			ParametersUtil.addParameterToParameters(
					theFhirContext, retVal, ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_TASK, theTask);
		}

		return retVal;
	}
}
