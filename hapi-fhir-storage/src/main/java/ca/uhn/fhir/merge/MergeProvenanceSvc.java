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
import java.util.Date;
import java.util.List;

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
			@Nullable String theProvenanceGroupId,
			Date theStartTime,
			RequestDetails theRequestDetails,
			List<IProvenanceAgent> theProvenanceAgents,
			List<IBaseResource> theContainedResources) {

		return super.createProvenance(
				theTargetId,
				theSourceId,
				theChangedResourceIds,
				theProvenanceGroupId,
				theStartTime,
				theRequestDetails,
				theProvenanceAgents,
				theContainedResources,
				// we need to create a Provenance resource even when there were no referencing resources,
				// because src and target resources are always updated in $merge operation
				true);
	}

	/**
	 * Finds the merge Provenances for the given target and source resource IDs. The main Provenance is the one
	 * with target+source in the correct order whose first contained resource is the merge input {@link Parameters};
	 * when it belongs to a provenance group (a cross-partition merge records one sub-Provenance per involved
	 * partition), every Provenance sharing its group id prefix is returned with it.
	 *
	 * @param theTargetId the target resource id
	 * @param theSourceId the source resource id
	 * @param theRequestDetails the request details
	 * @return a list whose first element is the main Provenance followed by its sub-Provenances,
	 *         or empty if none was found.
	 */
	public List<Provenance> findMergeProvenances(
			IIdType theTargetId, IIdType theSourceId, RequestDetails theRequestDetails) {

		List<Provenance> provenances =
				getProvenancesOfTargetsFilteredByActivity(List.of(theTargetId), theRequestDetails);

		return provenances.stream()
				.filter(p -> isTargetAndSourceInCorrectOrder(p, theTargetId, theSourceId)
						&& p.hasContained()
						&& p.getContained().get(0) instanceof Parameters)
				.findFirst()
				.map(main -> collectGroupedProvenances(main, provenances))
				.orElseGet(List::of);
	}

	/**
	 * Finds the merge Provenances for the given target id and source identifiers. For Patient resources, tries both
	 * patient-specific and generic parameter names for backward compatibility. As with
	 * {@link #findMergeProvenances(IIdType, IIdType, RequestDetails)}, the main Provenance comes first, followed by
	 * the sub-Provenances sharing its group id prefix (if any).
	 *
	 * @param theTargetId the target resource id
	 * @param theSourceIdentifiers the source identifiers to match
	 * @param theRequestDetails the request details
	 * @return a list whose first element is the main Provenance followed by its sub-Provenances,
	 *         or empty if none was found.
	 */
	public List<Provenance> findMergeProvenancesBySourceIdentifiers(
			IIdType theTargetId, List<CanonicalIdentifier> theSourceIdentifiers, RequestDetails theRequestDetails) {

		String resourceType = theTargetId.getResourceType();
		// Patient resources can be merged via two endpoints (Patient/$merge or Patient/$hapi.fhir.merge), each
		// storing different parameter names in the Provenance; all other resource types use only the generic ones.
		List<AbstractMergeOperationInputParameterNames> parameterNamesList =
				AbstractMergeOperationInputParameterNames.getParameterNamesForResourceType(resourceType);

		List<Provenance> provenances =
				getProvenancesOfTargetsFilteredByActivity(List.of(theTargetId), theRequestDetails);

		return provenances.stream()
				.filter(p -> containsSourceIdentifiersInInputParameters(p, parameterNamesList, theSourceIdentifiers))
				.findFirst()
				.map(main -> collectGroupedProvenances(main, provenances))
				.orElseGet(List::of);
	}

	/**
	 * Given the main Provenance, collects all Provenances from {@code theAllProvenances} that belong to the same
	 * provenance group — i.e. whose {@link HapiExtensions#EXT_PROVENANCE_GROUP} value shares the main Provenance's
	 * group id prefix. If the main Provenance has no group id (a same-partition merge records a single Provenance),
	 * returns a singleton list containing only the main Provenance.
	 *
	 * @return a list whose first element is always the main Provenance, followed by its sub-Provenances.
	 */
	private List<Provenance> collectGroupedProvenances(
			Provenance theMainProvenance, List<Provenance> theAllProvenances) {
		String mainGroupId = getProvenanceGroupId(theMainProvenance);
		if (mainGroupId == null) {
			return List.of(theMainProvenance);
		}
		String groupIdPrefix = MergeProvenanceGroupIdUtil.extractGroupIdPrefix(mainGroupId);

		List<Provenance> result = new ArrayList<>();
		result.add(theMainProvenance);
		String mainId =
				theMainProvenance.getIdElement().toUnqualifiedVersionless().getValue();

		for (Provenance provenance : theAllProvenances) {
			if (mainId.equals(
					provenance.getIdElement().toUnqualifiedVersionless().getValue())) {
				continue;
			}
			if (MergeProvenanceGroupIdUtil.isInGroup(getProvenanceGroupId(provenance), groupIdPrefix)) {
				result.add(provenance);
			}
		}
		return result;
	}

	/**
	 * @return the value of the Provenance's {@link HapiExtensions#EXT_PROVENANCE_GROUP} extension, or {@code null}
	 * when absent.
	 */
	@Nullable
	public static String getProvenanceGroupId(Provenance theProvenance) {
		Extension ext = theProvenance.getExtensionByUrl(HapiExtensions.EXT_PROVENANCE_GROUP);
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
