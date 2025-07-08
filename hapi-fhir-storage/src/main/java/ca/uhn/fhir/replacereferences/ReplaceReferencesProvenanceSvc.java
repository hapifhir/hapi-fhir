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
package ca.uhn.fhir.replacereferences;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.IProvenanceAgent;
import ca.uhn.fhir.model.api.StorageResponseCodeEnum;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.util.FhirTerser;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Period;
import org.hl7.fhir.r4.model.Provenance;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static ca.uhn.fhir.model.api.StorageResponseCodeEnum.SUCCESSFUL_PATCH_NO_CHANGE;

/**
 * This service is used to create a Provenance resource for the $replace-references operation
 * and also used as a base class for {@link ca.uhn.fhir.merge.MergeProvenanceSvc} used in $merge operations.
 * The two operations use different activity codes.
 */
public class ReplaceReferencesProvenanceSvc {

	private static final Logger ourLog = LoggerFactory.getLogger(ReplaceReferencesProvenanceSvc.class);
	private static final String ACT_REASON_CODE_SYSTEM = "http://terminology.hl7.org/CodeSystem/v3-ActReason";
	private static final String ACT_REASON_PATIENT_ADMINISTRATION_CODE = "PATADMIN";
	protected static final String ACTIVITY_CODE_SYSTEM = "http://terminology.hl7.org/CodeSystem/iso-21089-lifecycle";
	private static final String ACTIVITY_CODE_LINK = "link";
	private final IFhirResourceDao<IBaseResource> myProvenanceDao;
	private final FhirContext myFhirContext;

	public ReplaceReferencesProvenanceSvc(DaoRegistry theDaoRegistry) {
		myProvenanceDao = theDaoRegistry.getResourceDao("Provenance");
		myFhirContext = theDaoRegistry.getFhirContext();
	}

	protected CodeableConcept getActivityCodeableConcept() {
		CodeableConcept retVal = new CodeableConcept();
		retVal.addCoding().setSystem(ACTIVITY_CODE_SYSTEM).setCode(ACTIVITY_CODE_LINK);
		return retVal;
	}

	protected Provenance createProvenanceObject(
		Reference theTargetReference,
		@Nullable Reference theSourceReference,
		List<Reference> theUpdatedReferencingResources,
		Date theStartTime,
		List<IProvenanceAgent> theProvenanceAgents,
		List<IBaseResource> theContainedResources) {
		Provenance provenance = new Provenance();

		Date now = new Date();
		provenance.setOccurred(new Period()
				.setStart(theStartTime, TemporalPrecisionEnum.MILLI)
				.setEnd(now, TemporalPrecisionEnum.MILLI));
		provenance.setRecorded(now);

		addAgents(theProvenanceAgents, provenance);

		CodeableConcept activityCodeableConcept = getActivityCodeableConcept();
		if (activityCodeableConcept != null) {
			provenance.setActivity(activityCodeableConcept);
		}
		CodeableConcept activityReasonCodeableConcept = new CodeableConcept();
		activityReasonCodeableConcept
				.addCoding()
				.setSystem(ACT_REASON_CODE_SYSTEM)
				.setCode(ACT_REASON_PATIENT_ADMINISTRATION_CODE);

		provenance.addReason(activityReasonCodeableConcept);

		provenance.addTarget(theTargetReference);
		provenance.addTarget(theSourceReference);

		theUpdatedReferencingResources.forEach(provenance::addTarget);
		theContainedResources.forEach(c -> provenance.addContained((Resource) c));
		return provenance;
	}

	/**
	 * Creates a Provenance resource for the $replace-references and $merge operations.
	 *
	 * @param theTargetId           the versioned id of the target resource of the operation.
	 * @param theSourceId           the versioned id of the source resource of the operation.
	 * @param thePatchResultBundles the list of patch result bundles that contain the updated resources.
	 * @param theStartTime          the start time of the operation.
	 * @param theRequestDetails     the request details
	 * @param theProvenanceAgents   the list of agents to be included in the Provenance resource.
	 */
	public void createProvenance(
			IIdType theTargetId,
			IIdType theSourceId,
			List<Bundle> thePatchResultBundles,
			Date theStartTime,
			RequestDetails theRequestDetails,
			List<IProvenanceAgent> theProvenanceAgents,
			List<IBaseResource> theContainedResources) {
		createProvenance(
				theTargetId,
				theSourceId,
				thePatchResultBundles,
				theStartTime,
				theRequestDetails,
				theProvenanceAgents,
				theContainedResources,
				// if no referencing resource were updated, we don't need to create a Provenance resource, because
				// replace-references doesn't update the src and target resources, unlike the $merge operation
				false);
	}

	protected void createProvenance(
			IIdType theTargetId,
			IIdType theSourceId,
			List<Bundle> thePatchResultBundles,
			Date theStartTime,
			RequestDetails theRequestDetails,
			List<IProvenanceAgent> theProvenanceAgents,
			List<IBaseResource> theContainedResources,
			boolean theCreateEvenWhenNoReferencesWereUpdated) {
		Reference targetReference = new Reference(theTargetId);
		Reference sourceReference = new Reference(theSourceId);
		List<Reference> patchedReferences = extractUpdatedResourceReferences(thePatchResultBundles);
		if (!patchedReferences.isEmpty() || theCreateEvenWhenNoReferencesWereUpdated) {
			Provenance provenance = createProvenanceObject(
					targetReference, sourceReference, patchedReferences, theStartTime, theProvenanceAgents, theContainedResources);
			myProvenanceDao.create(provenance, theRequestDetails);
		}
	}

	/**
	 * Finds a Provenance resource that contain the given target and source references,
	 * and with the activity code this class generates Provenance resource with. If multiple Provenance resources
	 * found, returns the most recent one based on the 'recorded' field.
	 * @param theTargetId the target resource id
	 * @param theSourceId the source resource id
	 * @param theRequestDetails the request details
	 * @param theOperationName the name of operation trying to find the provenance resource, used for logging.
	 * @return the found Provenance resource, or null if not found.
	 */
	@Nullable
	public Provenance findProvenance(
			IIdType theTargetId, IIdType theSourceId, RequestDetails theRequestDetails, String theOperationName) {
		SearchParameterMap map = new SearchParameterMap();
		map.add("target", new ReferenceParam(theTargetId));
		// Add sort by recorded field, in case there are multiple Provenance resources for the same source and target,
		// we want the most recent one.
		map.setSort(new SortSpec("recorded", SortOrderEnum.DESC));

		IBundleProvider searchBundle = myProvenanceDao.search(map, theRequestDetails);
		// 'activity' is not available as a search parameter in r4, was added in r5,
		// so we need to filter the results manually.
		List<Provenance> provenances = filterByActivity(searchBundle.getAllResources());

		if (provenances.isEmpty()) {
			return null;
		}

		if (provenances.size() > 1) {
			// If there are multiple Provenance resources, we return the most recent one, but log a warning
			ourLog.warn(
					"There are multiple Provenance resources with the given source {} and target {} suitable for {} operation, "
							+ "will use the most recent one. Provenance count: {}",
					theSourceId,
					theTargetId,
					theOperationName,
					provenances.size());
		}

		Provenance provenance = provenances.get(0);
		if (isTargetAndSourceInCorrectOrder(provenance, theTargetId, theSourceId)) {
			return provenance;
		} else {
			return null;
		}
	}

	private List<Provenance> filterByActivity(List<IBaseResource> theResources) {
		List<Provenance> filteredProvenances = new ArrayList<>();
		for (IBaseResource resource : theResources) {
			Provenance provenance = (Provenance) resource;
			if (provenance.hasActivity() && provenance.getActivity().equalsDeep(getActivityCodeableConcept())) {
				filteredProvenances.add(provenance);
			}
		}
		return filteredProvenances;
	}

	/**
	 * Checks if the first 'Provenance.target' reference matches theTargetId and the second matches theSourceId.
	 * The $hapi.fhir.replace-references and $merge operations create their Provenance resource with targets in that order.
	 * @param provenance The Provenance resource to check.
	 * @param theTargetId The expected target IIdType for the first reference.
	 * @param theSourceId The expected source IIdType for the second reference.
	 * @return true if both match, false otherwise.
	 */
	public boolean isTargetAndSourceInCorrectOrder(Provenance provenance, IIdType theTargetId, IIdType theSourceId) {
		if (provenance.getTarget().size() < 2) {
			ourLog.error(
					"Provenance resource {} does not have enough targets. Expected at least 2, found {}.",
					provenance.getIdElement().getValue(),
					provenance.getTarget().size());
			return false;
		}
		Reference firstTargetRefInProv = provenance.getTarget().get(0);
		Reference secondTargetRefInProv = provenance.getTarget().get(1);

		boolean firstMatches = isEqualVersionlessId(theTargetId, firstTargetRefInProv);
		boolean secondMatches = isEqualVersionlessId(theSourceId, secondTargetRefInProv);

		boolean result = firstMatches && secondMatches;

		if (!result) {
			ourLog.error(
					"Provenance resource {} doesn't have the expected target and source references or they are in the wrong order. "
							+ "Expected target: {}, source: {}, but found target: {}, source: {}",
					provenance.getIdElement().getValue(),
					theTargetId.getValue(),
					theSourceId.getValue(),
					firstTargetRefInProv.getReference(),
					secondTargetRefInProv.getReference());
		}

		return result;
	}

	private boolean isEqualVersionlessId(IIdType theId, Reference theReference) {
		if (!theReference.hasReference()) {
			return false;
		}
		return theId.toUnqualifiedVersionless()
				.getValue()
				.equals(new IdDt(theReference.getReference())
						.toUnqualifiedVersionless()
						.getValue());
	}

	protected List<Reference> extractUpdatedResourceReferences(List<Bundle> thePatchBundles) {
		List<Reference> patchedResourceReferences = new ArrayList<>();
		thePatchBundles.forEach(outputBundle -> {
			outputBundle.getEntry().forEach(entry -> {
				if (entry.getResponse() != null && entry.getResponse().hasLocation()) {
					if (isNoopPatch(entry.getResponse())) {
						// in the unlikely event that the patch resulted in a no-op change,
						// don't add the reference to the Provenance since it wasn't really updated by the transaction.
						ourLog.warn(
								"Not adding reference {} to Provenance, because the patch resulted in a no-op change",
								entry.getResponse().getLocation());
						return;
					}
					Reference reference = new Reference(entry.getResponse().getLocation());
					patchedResourceReferences.add(reference);
				}
			});
		});
		return patchedResourceReferences;
	}

	private boolean isNoopPatch(Bundle.BundleEntryResponseComponent theResponse) {
		if (!theResponse.hasOutcome()) {
			return false;
		}

		OperationOutcome outcome = (OperationOutcome) theResponse.getOutcome();

		if (!outcome.hasIssue()) {
			return false;
		}

		List<OperationOutcome.OperationOutcomeIssueComponent> issues = outcome.getIssue();

		return issues.stream()
				.filter(issue -> issue.hasDetails() && issue.getDetails().hasCoding())
				.map(issue -> issue.getDetails().getCoding())
				.flatMap(List::stream)
				.anyMatch(coding -> StorageResponseCodeEnum.SYSTEM.equals(coding.getSystem())
						&& SUCCESSFUL_PATCH_NO_CHANGE.getCode().equals(coding.getCode()));
	}

	private Provenance.ProvenanceAgentComponent createR4ProvenanceAgent(IProvenanceAgent theProvenanceAgent) {
		Provenance.ProvenanceAgentComponent agent = new Provenance.ProvenanceAgentComponent();
		Reference whoRef = convertToR4Reference(theProvenanceAgent.getWho());
		agent.setWho(whoRef);
		Reference onBehalfOfRef = convertToR4Reference(theProvenanceAgent.getOnBehalfOf());
		agent.setOnBehalfOf(onBehalfOfRef);
		return agent;
	}

	private void addAgents(List<IProvenanceAgent> theProvenanceAgents, Provenance theProvenance) {
		if (theProvenanceAgents != null) {
			for (IProvenanceAgent agent : theProvenanceAgents) {
				Provenance.ProvenanceAgentComponent r4Agent = createR4ProvenanceAgent(agent);
				theProvenance.addAgent(r4Agent);
			}
		}
	}

	private Reference convertToR4Reference(IBaseReference sourceRef) {
		if (sourceRef == null) {
			return null;
		}
		FhirTerser terser = myFhirContext.newTerser();
		Reference targetRef = new Reference();
		terser.cloneInto(sourceRef, targetRef, false);
		return targetRef;
	}
}
