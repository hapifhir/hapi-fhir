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

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IProvenanceAgent;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.util.CanonicalIdentifier;
import ca.uhn.fhir.util.ParametersUtil;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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

	/**
	 * Build MergeOperationInputParameters from REST operation parameters.
	 * This method is used by REST providers that receive individual operation parameters.
	 *
	 * @param theSourcePatientIdentifier list of source patient identifiers
	 * @param theTargetPatientIdentifier list of target patient identifiers
	 * @param theSourcePatient source patient reference
	 * @param theTargetPatient target patient reference
	 * @param thePreview preview flag
	 * @param theDeleteSource delete source flag
	 * @param theResultPatient result patient resource
	 * @param theProvenanceAgents provenance agents for audit
	 * @param theOriginalInputParameters original input parameters for provenance
	 * @return MergeOperationInputParameters ready for use with ResourceMergeService
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	public static MergeOperationInputParameters inputParamsFromOperationParams(
			List<?> theSourcePatientIdentifier,
			List<?> theTargetPatientIdentifier,
			IBaseReference theSourcePatient,
			IBaseReference theTargetPatient,
			IPrimitiveType<Boolean> thePreview,
			IPrimitiveType<Boolean> theDeleteSource,
			IBaseResource theResultPatient,
			List<IProvenanceAgent> theProvenanceAgents,
			IBaseResource theOriginalInputParameters,
			int theResourceLimit) {

		MergeOperationInputParameters result = new MergeOperationInputParameters(theResourceLimit);

		// Set identifiers
		if (theSourcePatientIdentifier != null && !theSourcePatientIdentifier.isEmpty()) {
			List sourceIds = theSourcePatientIdentifier.stream()
					.map(id -> CanonicalIdentifier.fromIdentifier((IBase) id))
					.collect(Collectors.toList());
			result.setSourceResourceIdentifiers(sourceIds);
		}

		if (theTargetPatientIdentifier != null && !theTargetPatientIdentifier.isEmpty()) {
			List targetIds = theTargetPatientIdentifier.stream()
					.map(id -> CanonicalIdentifier.fromIdentifier((IBase) id))
					.collect(Collectors.toList());
			result.setTargetResourceIdentifiers(targetIds);
		}

		// Set references
		result.setSourceResource(theSourcePatient);
		result.setTargetResource(theTargetPatient);

		// Set flags
		result.setPreview(thePreview != null && thePreview.getValue());
		result.setDeleteSource(theDeleteSource != null && theDeleteSource.getValue());

		// Set result patient (make a copy to avoid modification)
		if (theResultPatient != null) {
			result.setResultResource(((org.hl7.fhir.r4.model.Patient) theResultPatient).copy());
		}

		// Set provenance and original parameters
		result.setProvenanceAgents(theProvenanceAgents);
		if (theOriginalInputParameters != null) {
			result.setOriginalInputParameters(((org.hl7.fhir.r4.model.Resource) theOriginalInputParameters).copy());
		}

		return result;
	}

	/**
	 * Build MergeOperationInputParameters from a FHIR Parameters resource.
	 * Extracts all merge operation parameters according to the FHIR spec.
	 *
	 * @param theParameters FHIR Parameters resource containing merge operation inputs
	 * @return MergeOperationInputParameters ready for use with ResourceMergeService
	 */
	public static MergeOperationInputParameters inputParamsFromParameters(
			FhirContext theFhirContext, IBaseParameters theParameters, int theResourceLimit) {

		MergeOperationInputParameters result = new MergeOperationInputParameters(theResourceLimit);

		// Extract source-patient-identifier (list of identifiers)
		List<IBase> sourceIdentifierParams =
				ParametersUtil.getNamedParameters(theFhirContext, theParameters, "source-patient-identifier");
		if (!sourceIdentifierParams.isEmpty()) {
			List<CanonicalIdentifier> sourceIds = sourceIdentifierParams.stream()
					.map(param -> extractValueFromParameter(theFhirContext, param, "value"))
					.filter(Objects::nonNull)
					.map(CanonicalIdentifier::fromIdentifier)
					.collect(Collectors.toList());
			if (!sourceIds.isEmpty()) {
				result.setSourceResourceIdentifiers(sourceIds);
			}
		}

		// Extract target-patient-identifier (list of identifiers)
		List<IBase> targetIdentifierParams =
				ParametersUtil.getNamedParameters(theFhirContext, theParameters, "target-patient-identifier");
		if (!targetIdentifierParams.isEmpty()) {
			List<CanonicalIdentifier> targetIds = targetIdentifierParams.stream()
					.map(param -> extractValueFromParameter(theFhirContext, param, "value"))
					.filter(Objects::nonNull)
					.map(CanonicalIdentifier::fromIdentifier)
					.collect(Collectors.toList());
			if (!targetIds.isEmpty()) {
				result.setTargetResourceIdentifiers(targetIds);
			}
		}

		// Extract source-patient reference
		List<IBaseReference> sourcePatientRefs =
				ParametersUtil.getNamedParameterReferences(theFhirContext, theParameters, "source-patient");
		if (!sourcePatientRefs.isEmpty()) {
			result.setSourceResource(sourcePatientRefs.get(0));
		}

		// Extract target-patient reference
		List<IBaseReference> targetPatientRefs =
				ParametersUtil.getNamedParameterReferences(theFhirContext, theParameters, "target-patient");
		if (!targetPatientRefs.isEmpty()) {
			result.setTargetResource(targetPatientRefs.get(0));
		}

		// Extract preview flag
		Optional<String> previewValue =
				ParametersUtil.getNamedParameterValueAsString(theFhirContext, theParameters, "preview");
		previewValue.ifPresent(val -> result.setPreview(Boolean.parseBoolean(val)));

		// Extract delete-source flag
		Optional<String> deleteSourceValue =
				ParametersUtil.getNamedParameterValueAsString(theFhirContext, theParameters, "delete-source");
		deleteSourceValue.ifPresent(val -> result.setDeleteSource(Boolean.parseBoolean(val)));

		// Extract result-patient
		ParametersUtil.getNamedParameterResource(theFhirContext, theParameters, "result-patient")
				.ifPresent(result::setResultResource);

		// Store original parameters for provenance
		result.setOriginalInputParameters(theParameters);

		return result;
	}

	/**
	 * Extract a value[x] from a Parameters.parameter element.
	 * In FHIR Parameters, values are stored as value[x] which expands to valueString, valueIdentifier, etc.
	 *
	 * @param theParameter the parameter element
	 * @param theChildNamePrefix the child name prefix (e.g., "value" will match valueIdentifier, valueString, etc.)
	 * @return the child value or null if not found
	 */
	private static IBase extractValueFromParameter(
			FhirContext theFhirContext,
			IBase theParameter,
			@SuppressWarnings("SameParameterValue") String theChildNamePrefix) {

		BaseRuntimeElementCompositeDefinition<?> parameterDef =
				(BaseRuntimeElementCompositeDefinition<?>) theFhirContext.getElementDefinition(theParameter.getClass());

		// Try to find a child that starts with the prefix (e.g., "value" matches "valueIdentifier")
		for (BaseRuntimeChildDefinition childDef : parameterDef.getChildren()) {
			String childName = childDef.getElementName();
			if (childName.startsWith(theChildNamePrefix)) {
				List<IBase> values = childDef.getAccessor().getValues(theParameter);
				if (!values.isEmpty()) {
					return values.get(0);
				}
			}
		}
		return null;
	}
}
