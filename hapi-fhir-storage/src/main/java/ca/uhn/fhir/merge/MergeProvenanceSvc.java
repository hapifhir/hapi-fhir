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
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeableConcept;

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
	public void createProvenance(
			IIdType theTargetId,
			@Nullable IIdType theSourceId,
			List<Bundle> thePatchResultBundles,
			Date theStartTime,
			RequestDetails theRequestDetails,
			List<IProvenanceAgent> theProvenanceAgents) {

		super.createProvenance(
				theTargetId,
				theSourceId,
				thePatchResultBundles,
				theStartTime,
				theRequestDetails,
				theProvenanceAgents,
				// we need to create a Provenance resource even when there were no referencing resources,
				// because src and target resources are always updated in $merge operation
				true);
	}
}
