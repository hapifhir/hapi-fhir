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

import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.model.api.IProvenanceAgent;
import ca.uhn.fhir.replacereferences.ReplaceReferencesProvenanceSvc;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.util.CanonicalIdentifier;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Provenance;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.Type;

import java.util.Date;
import java.util.List;

/**
 *  Handles Provenance resources for the $merge operation.
 */
public class MergeProvenanceSvc extends ReplaceReferencesProvenanceSvc {

	private static final String ACTIVITY_CODE_MERGE = "merge";
	private final MergeOperationInputParameterNames myInputParamNames;

	public MergeProvenanceSvc(DaoRegistry theDaoRegistry) {
		super(theDaoRegistry);
		myInputParamNames = new MergeOperationInputParameterNames();
	}

	@Override
	protected CodeableConcept getActivityCodeableConcept() {
		CodeableConcept retVal = new CodeableConcept();
		retVal.addCoding().setSystem(ACTIVITY_CODE_SYSTEM).setCode(ACTIVITY_CODE_MERGE);
		return retVal;
	}

	@Override
	public void createProvenance(
			IIdType theTargetId,
			IIdType theSourceId,
			List<Bundle> thePatchResultBundles,
			Date theStartTime,
			RequestDetails theRequestDetails,
			List<IProvenanceAgent> theProvenanceAgents,
			List<IBaseResource> theContainedResources) {

		super.createProvenance(
				theTargetId,
				theSourceId,
				thePatchResultBundles,
				theStartTime,
				theRequestDetails,
				theProvenanceAgents,
				theContainedResources,
				// we need to create a Provenance resource even when there were no referencing resources,
				// because src and target resources are always updated in $merge operation
				true);
	}

	public Provenance findProvenanceByTargetIdAndSourceIdentifiers(
			IIdType theTargetId, List<CanonicalIdentifier> theSourceIdentifiers, RequestDetails theRequestDetails) {
		String sourceIdentifierParameterName = myInputParamNames.getSourceIdentifiersParameterName();
		List<Provenance> provenances =
				getProvenancesOfTargetsFilteredByActivity(List.of(theTargetId), theRequestDetails);
		// the input parameters are in a contained resource, find the one that matches the source identifiers
		for (Provenance provenance : provenances) {
			for (Resource contained : provenance.getContained()) {
				if (contained instanceof Parameters parameters
						&& parameters.hasParameter(sourceIdentifierParameterName)) {
					List<Type> originalInputSrcIdentifiers =
							parameters.getParameterValues(sourceIdentifierParameterName);
					if (hasIdentifiers(originalInputSrcIdentifiers, theSourceIdentifiers)) {
						return provenance;
					}
				}
			}
		}
		return null;
	}

	private boolean hasIdentifiers(List<Type> theIdentifiers, List<CanonicalIdentifier> theIdentifiersToLookFor) {
		for (CanonicalIdentifier identifier : theIdentifiersToLookFor) {
			boolean identifierFound = theIdentifiers.stream()
					.map(i -> (Identifier) i)
					.anyMatch(i -> i.getSystem()
									.equals(identifier.getSystemElement().getValueAsString())
							&& i.getValue().equals(identifier.getValueElement().getValueAsString()));

			if (!identifierFound) {
				return false;
			}
		}
		return true;
	}
}
