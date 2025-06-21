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
import ca.uhn.fhir.model.api.IProvenanceAgent;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.util.FhirTerser;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Period;
import org.hl7.fhir.r4.model.Provenance;
import org.hl7.fhir.r4.model.Reference;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This service is used to create a Provenance resource for the $replace-references operation
 * and also used as a base class for {@link ca.uhn.fhir.merge.MergeProvenanceSvc} used in $merge operations.
 * The two operations use different activity codes.
 */
public class ReplaceReferencesProvenanceSvc {

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

	@Nullable
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
			List<IProvenanceAgent> theProvenanceAgents) {
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
		if (theSourceReference != null) {
			provenance.addTarget(theSourceReference);
		}

		theUpdatedReferencingResources.forEach(provenance::addTarget);
		return provenance;
	}

	/**
	 * Creates a Provenance resource for the $replace-references and $merge operations.
	 *
	 * @param theTargetId           the versioned id of the target resource of the operation.
	 * @param theSourceId           the versioned id of the source resource of the operation. Can be null if the operation is $merge and the source resource is deleted.
	 * @param thePatchResultBundles the list of patch result bundles that contain the updated resources.
	 * @param theStartTime          the start time of the operation.
	 * @param theRequestDetails     the request details
	 * @param theProvenanceAgents   the list of agents to be included in the Provenance resource.
	 */
	public void createProvenance(
			IIdType theTargetId,
			@Nullable IIdType theSourceId,
			List<Bundle> thePatchResultBundles,
			Date theStartTime,
			RequestDetails theRequestDetails,
			List<IProvenanceAgent> theProvenanceAgents) {
		Reference targetReference = new Reference(theTargetId);
		Reference sourceReference = null;
		if (theSourceId != null) {
			sourceReference = new Reference(theSourceId);
		}
		List<Reference> references = extractUpdatedResourceReferences(thePatchResultBundles);
		Provenance provenance =
				createProvenanceObject(targetReference, sourceReference, references, theStartTime, theProvenanceAgents);
		myProvenanceDao.create(provenance, theRequestDetails);
	}

	protected List<Reference> extractUpdatedResourceReferences(List<Bundle> thePatchBundles) {
		List<Reference> patchedResourceReferences = new ArrayList<>();
		thePatchBundles.forEach(outputBundle -> {
			outputBundle.getEntry().forEach(entry -> {
				if (entry.getResponse() != null && entry.getResponse().hasLocation()) {
					Reference reference = new Reference(entry.getResponse().getLocation());
					patchedResourceReferences.add(reference);
				}
			});
		});
		return patchedResourceReferences;
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
