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
 * Builder for creating MergeOperationInputParameters from FHIR Parameters resource.
 * This enables programmatic invocation of the merge operation outside of REST contexts.
 */
public class MergeOperationInputParametersBuilder {

	private final FhirContext myFhirContext;
	private final int myResourceLimit;

	/**
	 * Create a new builder for MergeOperationInputParameters.
	 *
	 * @param theFhirContext   the FHIR context
	 * @param theResourceLimit the maximum number of resources to process
	 */
	public MergeOperationInputParametersBuilder(FhirContext theFhirContext, int theResourceLimit) {
		myFhirContext = theFhirContext;
		myResourceLimit = theResourceLimit;
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
	public MergeOperationInputParameters fromOperationParams(
			List<?> theSourcePatientIdentifier,
			List<?> theTargetPatientIdentifier,
			IBaseReference theSourcePatient,
			IBaseReference theTargetPatient,
			IPrimitiveType<Boolean> thePreview,
			IPrimitiveType<Boolean> theDeleteSource,
			IBaseResource theResultPatient,
			List<IProvenanceAgent> theProvenanceAgents,
			IBaseResource theOriginalInputParameters) {

		MergeOperationInputParameters result = new MergeOperationInputParameters(myResourceLimit);

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
	public MergeOperationInputParameters fromParameters(IBaseParameters theParameters) {
		MergeOperationInputParameters result = new MergeOperationInputParameters(myResourceLimit);

		// Extract source-patient-identifier (list of identifiers)
		List<IBase> sourceIdentifierParams =
				ParametersUtil.getNamedParameters(myFhirContext, theParameters, "source-patient-identifier");
		if (!sourceIdentifierParams.isEmpty()) {
			List<CanonicalIdentifier> sourceIds = sourceIdentifierParams.stream()
					.map(param -> extractValueFromParameter(param, "value"))
					.filter(Objects::nonNull)
					.map(CanonicalIdentifier::fromIdentifier)
					.collect(Collectors.toList());
			if (!sourceIds.isEmpty()) {
				result.setSourceResourceIdentifiers(sourceIds);
			}
		}

		// Extract target-patient-identifier (list of identifiers)
		List<IBase> targetIdentifierParams =
				ParametersUtil.getNamedParameters(myFhirContext, theParameters, "target-patient-identifier");
		if (!targetIdentifierParams.isEmpty()) {
			List<CanonicalIdentifier> targetIds = targetIdentifierParams.stream()
					.map(param -> extractValueFromParameter(param, "value"))
					.filter(Objects::nonNull)
					.map(CanonicalIdentifier::fromIdentifier)
					.collect(Collectors.toList());
			if (!targetIds.isEmpty()) {
				result.setTargetResourceIdentifiers(targetIds);
			}
		}

		// Extract source-patient reference
		List<IBaseReference> sourcePatientRefs =
				ParametersUtil.getNamedParameterReferences(myFhirContext, theParameters, "source-patient");
		if (!sourcePatientRefs.isEmpty()) {
			result.setSourceResource(sourcePatientRefs.get(0));
		}

		// Extract target-patient reference
		List<IBaseReference> targetPatientRefs =
				ParametersUtil.getNamedParameterReferences(myFhirContext, theParameters, "target-patient");
		if (!targetPatientRefs.isEmpty()) {
			result.setTargetResource(targetPatientRefs.get(0));
		}

		// Extract preview flag
		Optional<String> previewValue =
				ParametersUtil.getNamedParameterValueAsString(myFhirContext, theParameters, "preview");
		previewValue.ifPresent(val -> result.setPreview(Boolean.parseBoolean(val)));

		// Extract delete-source flag
		Optional<String> deleteSourceValue =
				ParametersUtil.getNamedParameterValueAsString(myFhirContext, theParameters, "delete-source");
		deleteSourceValue.ifPresent(val -> result.setDeleteSource(Boolean.parseBoolean(val)));

		// Extract result-patient
		ParametersUtil.getNamedParameterResource(myFhirContext, theParameters, "result-patient")
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
	private IBase extractValueFromParameter(
			IBase theParameter, @SuppressWarnings("SameParameterValue") String theChildNamePrefix) {

		BaseRuntimeElementCompositeDefinition<?> parameterDef =
				(BaseRuntimeElementCompositeDefinition<?>) myFhirContext.getElementDefinition(theParameter.getClass());

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
