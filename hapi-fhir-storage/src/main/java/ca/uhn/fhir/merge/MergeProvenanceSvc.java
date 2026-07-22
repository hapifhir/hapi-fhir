/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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

import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.model.api.IProvenanceAgent;
import ca.uhn.fhir.replacereferences.ReplaceReferencesProvenanceSvc;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.util.CanonicalIdentifier;
import ca.uhn.fhir.util.HapiExtensions;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Provenance;
import org.hl7.fhir.r4.model.Type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 *  Handles Provenance resources for the $merge operation.
 */
public class MergeProvenanceSvc extends ReplaceReferencesProvenanceSvc {

	private static final String ACTIVITY_CODE_MERGE = "merge";

	public MergeProvenanceSvc(DaoRegistry theDaoRegistry) {
		super(theDaoRegistry);
	}

	@Override
	protected CodeableConcept getActivityCodeableConcept() {
		CodeableConcept retVal = new CodeableConcept();
		retVal.addCoding().setSystem(ACTIVITY_CODE_SYSTEM).setCode(ACTIVITY_CODE_MERGE);
		return retVal;
	}

	@Override
	public IIdType createProvenance(
			IIdType theTargetId,
			IIdType theSourceId,
			List<IIdType> theChangedResourceIds,
			@Nullable String theProvenanceCorrelationId,
			Date theStartTime,
			RequestDetails theRequestDetails,
			List<IProvenanceAgent> theProvenanceAgents,
			List<IBaseResource> theContainedResources) {

		return super.createProvenance(
				theTargetId,
				theSourceId,
				theChangedResourceIds,
				theProvenanceCorrelationId,
				theStartTime,
				theRequestDetails,
				theProvenanceAgents,
				theContainedResources,
				// we need to create a Provenance resource even when there were no referencing resources,
				// because src and target resources are always updated in $merge operation
				true);
	}

	/**
	 * Finds all merge Provenances for the given target and source resource IDs.
	 * Returns all Provenances that share the same correlation id as the main Provenance (the one with
	 * target+source in correct order). If the main Provenance has no correlation id, returns just the main.
	 *
	 * @param theTargetId the target resource id
	 * @param theSourceId the source resource id
	 * @param theRequestDetails the request details
	 * @return a list where the first element is the main Provenance followed by sub-Provenances,
	 *         or empty if not found.
	 */
	public List<Provenance> findMergeProvenances(
			IIdType theTargetId, IIdType theSourceId, RequestDetails theRequestDetails) {

		List<Provenance> provenances =
				getProvenancesOfTargetsFilteredByActivity(List.of(theTargetId), theRequestDetails);

		// The main Provenance is identified by having target+source in correct order
		// and containing a Parameters resource (the merge input parameters) as the first contained resource.
		// Sub-Provenances (per-partition) do not have contained resources.
		Provenance mainProvenance = null;
		for (Provenance p : provenances) {
			if (isTargetAndSourceInCorrectOrder(p, theTargetId, theSourceId)
					&& p.hasContained()
					&& p.getContained().get(0) instanceof Parameters) {
				mainProvenance = p;
				break;
			}
		}

		if (mainProvenance == null) {
			return Collections.emptyList();
		}

		return collectCorrelatedProvenances(mainProvenance, provenances);
	}

	/**
	 * Finds all merge Provenances for the given target id and source identifiers.
	 * For Patient resources, tries both patient-specific and generic parameter names for backward compatibility.
	 * Returns all Provenances that share the same correlation id as the main Provenance. If no correlation id,
	 * returns just the main.
	 *
	 * @param theTargetId the target resource id
	 * @param theSourceIdentifiers the source identifiers to match
	 * @param theRequestDetails the request details
	 * @return a list where the first element is the main Provenance followed by sub-Provenances,
	 *         or empty if not found.
	 */
	public List<Provenance> findMergeProvenancesBySourceIdentifiers(
			IIdType theTargetId, List<CanonicalIdentifier> theSourceIdentifiers, RequestDetails theRequestDetails) {

		String resourceType = theTargetId.getResourceType();
		// Returns a list because Patient resources can be merged via two endpoints (Patient/$merge or
		// Patient/$hapi.fhir.merge), each storing different parameter names in the Provenance.
		// All other resource types only use the generic endpoint and parameter names.
		List<AbstractMergeOperationInputParameterNames> parameterNamesList =
				AbstractMergeOperationInputParameterNames.getParameterNamesForResourceType(resourceType);

		List<Provenance> provenances =
				getProvenancesOfTargetsFilteredByActivity(List.of(theTargetId), theRequestDetails);

		Provenance mainProvenance = provenances.stream()
				.filter(p -> containsSourceIdentifiersInInputParameters(p, parameterNamesList, theSourceIdentifiers))
				.findFirst()
				.orElse(null);

		if (mainProvenance == null) {
			return Collections.emptyList();
		}

		return collectCorrelatedProvenances(mainProvenance, provenances);
	}

	/**
	 * Given the main Provenance, collects all Provenances from {@code theAllProvenances} that share
	 * the same provenance correlation id. If the main Provenance has no correlation id (same-partition merge),
	 * returns a singleton list containing only the main Provenance.
	 *
	 * @return a list where the first element is always the main Provenance, followed by sub-Provenances.
	 */
	private List<Provenance> collectCorrelatedProvenances(
			Provenance theMainProvenance, List<Provenance> theAllProvenances) {
		String correlationId = getProvenanceCorrelationId(theMainProvenance);
		if (correlationId == null) {
			return List.of(theMainProvenance);
		}

		List<Provenance> result = new ArrayList<>();
		result.add(theMainProvenance);
		String mainId =
				theMainProvenance.getIdElement().toUnqualifiedVersionless().getValue();

		for (Provenance p : theAllProvenances) {
			if (mainId.equals(p.getIdElement().toUnqualifiedVersionless().getValue())) {
				continue;
			}
			if (Objects.equals(correlationId, getProvenanceCorrelationId(p))) {
				result.add(p);
			}
		}
		return result;
	}

	@Nullable
	private static String getProvenanceCorrelationId(Provenance theProvenance) {
		Extension ext = theProvenance.getExtensionByUrl(HapiExtensions.EXT_PROVENANCE_CORRELATION_ID);
		if (ext != null && ext.hasValue()) {
			return ext.getValueAsPrimitive().getValueAsString();
		}
		return null;
	}

	private boolean containsSourceIdentifiersInInputParameters(
			Provenance theProvenance,
			List<AbstractMergeOperationInputParameterNames> theParameterNamesList,
			List<CanonicalIdentifier> theSourceIdentifiers) {
		// The input parameters must be the first contained resource in Provenance's contained resources.
		if (!theProvenance.hasContained() || !(theProvenance.getContained().get(0) instanceof Parameters parameters)) {
			return false;
		}
		// Try each set of parameter names (e.g., patient-specific and generic for Patient resources).
		for (AbstractMergeOperationInputParameterNames paramNames : theParameterNamesList) {
			String sourceIdentifierParamName = paramNames.getSourceIdentifiersParameterName();
			if (parameters.hasParameter(sourceIdentifierParamName)) {
				List<Type> originalInputSrcIdentifiers = parameters.getParameterValues(sourceIdentifierParamName);
				if (hasIdentifiers(originalInputSrcIdentifiers, theSourceIdentifiers)) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean hasIdentifiers(List<Type> theIdentifiers, List<CanonicalIdentifier> theIdentifiersToLookFor) {
		for (CanonicalIdentifier identifier : theIdentifiersToLookFor) {
			boolean identifierFound = theIdentifiers.stream()
					.map(i -> (Identifier) i)
					.anyMatch(i -> CanonicalIdentifier.fromIdentifier(i).equals(identifier));

			if (!identifierFound) {
				return false;
			}
		}
		return true;
	}
}
