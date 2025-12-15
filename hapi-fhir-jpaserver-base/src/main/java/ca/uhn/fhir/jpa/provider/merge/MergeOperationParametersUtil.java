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
import ca.uhn.fhir.model.api.IProvenanceAgent;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.util.CanonicalIdentifier;
import ca.uhn.fhir.util.ParametersUtil;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Resource;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_PARAM_DELETE_SOURCE;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_PARAM_PREVIEW;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_PARAM_RESULT_PATIENT;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_PARAM_SOURCE_PATIENT;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_PARAM_SOURCE_PATIENT_IDENTIFIER;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_PARAM_TARGET_PATIENT;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_PARAM_TARGET_PATIENT_IDENTIFIER;

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

		// pass in a copy of the result patient as we don't want it to be modified. It will be
		// returned back to the client as part of the response.
		if (theResultPatient != null) {
			result.setResultResource(((Patient) theResultPatient).copy());
		}

		// Set provenance and original parameters
		result.setProvenanceAgents(theProvenanceAgents);
		if (theOriginalInputParameters != null) {
			result.setOriginalInputParameters(((Resource) theOriginalInputParameters).copy());
		}

		return result;
	}

	/**
	 * Build MergeOperationInputParameters from a FHIR Parameters resource.
	 * Extracts all merge operation parameters according to the FHIR spec.
	 *
	 * @param theParameters       FHIR Parameters resource containing merge operation inputs
	 * @param theProvenanceAgents the obtained provenance agents
	 * @return MergeOperationInputParameters ready for use with ResourceMergeService
	 */
	public static MergeOperationInputParameters inputParamsFromParameters(
			FhirContext theFhirContext,
			IBaseParameters theParameters,
			int theResourceLimit,
			List<IProvenanceAgent> theProvenanceAgents) {

		// Extract source-patient-identifier (list of identifiers)
		List<Identifier> sourceIdentifiers = null;
		List<IBase> sourceIdentifierParams = ParametersUtil.getNamedParameters(
				theFhirContext, theParameters, OPERATION_MERGE_PARAM_SOURCE_PATIENT_IDENTIFIER);
		if (!sourceIdentifierParams.isEmpty()) {
			sourceIdentifiers = sourceIdentifierParams.stream()
					.map(param -> extractIdentifierFromParameter(theFhirContext, param))
					.filter(Objects::nonNull)
					.collect(Collectors.toList());
		}

		// Extract target-patient-identifier (list of identifiers)
		List<Identifier> targetIdentifiers = null;
		List<IBase> targetIdentifierParams = ParametersUtil.getNamedParameters(
				theFhirContext, theParameters, OPERATION_MERGE_PARAM_TARGET_PATIENT_IDENTIFIER);
		if (!targetIdentifierParams.isEmpty()) {
			targetIdentifiers = targetIdentifierParams.stream()
					.map(param -> extractIdentifierFromParameter(theFhirContext, param))
					.filter(Objects::nonNull)
					.collect(Collectors.toList());
		}

		// Extract source-patient reference
		IBaseReference sourcePatient = null;
		List<IBaseReference> sourcePatientRefs = ParametersUtil.getNamedParameterReferences(
				theFhirContext, theParameters, OPERATION_MERGE_PARAM_SOURCE_PATIENT);
		if (!sourcePatientRefs.isEmpty()) {
			sourcePatient = sourcePatientRefs.get(0);
		}

		// Extract target-patient reference
		IBaseReference targetPatient = null;
		List<IBaseReference> targetPatientRefs = ParametersUtil.getNamedParameterReferences(
				theFhirContext, theParameters, OPERATION_MERGE_PARAM_TARGET_PATIENT);
		if (!targetPatientRefs.isEmpty()) {
			targetPatient = targetPatientRefs.get(0);
		}

		// Extract preview flag
		IPrimitiveType<Boolean> previewValue = ParametersUtil.getNamedParameterValueAsString(
						theFhirContext, theParameters, OPERATION_MERGE_PARAM_PREVIEW)
				.map(b -> new BooleanDt(Boolean.parseBoolean(b)))
				.orElse(new BooleanDt(false));

		// Extract delete-source flag
		IPrimitiveType<Boolean> deleteSourceValue = ParametersUtil.getNamedParameterValueAsString(
						theFhirContext, theParameters, OPERATION_MERGE_PARAM_DELETE_SOURCE)
				.map(b -> new BooleanDt(Boolean.parseBoolean(b)))
				.orElse(new BooleanDt(false));

		// Extract result-patient
		IBaseResource resultPatient = ParametersUtil.getNamedParameterResource(
						theFhirContext, theParameters, OPERATION_MERGE_PARAM_RESULT_PATIENT)
				.orElse(null);

		return inputParamsFromOperationParams(
				sourceIdentifiers,
				targetIdentifiers,
				sourcePatient,
				targetPatient,
				previewValue,
				deleteSourceValue,
				resultPatient,
				theProvenanceAgents,
				theParameters,
				theResourceLimit);
	}

	/**
	 * Extract Identifier value from a Parameters.parameter element.
	 *
	 * @param theParameter the parameter element
	 * @return the child value or null if not found
	 */
	private static Identifier extractIdentifierFromParameter(FhirContext theFhirContext, IBase theParameter) {
		return theFhirContext.newTerser().getSingleValueOrNull(theParameter, "valueIdentifier", Identifier.class);
	}
}
