package ca.uhn.fhir.jpa.mdm.svc;

/*-
 * #%L
 * HAPI FHIR JPA Server - Master Data Management
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.mdm.api.IMdmSurvivorshipService;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.util.TerserUtil;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;

public class MdmSurvivorshipSvcImpl implements IMdmSurvivorshipService {

	@Autowired
	private FhirContext myFhirContext;

	/**
	 * Merges two golden resources by overwriting all field values on theGoldenResource param for CREATE_RESOURCE,
	 * UPDATE_RESOURCE, SUBMIT_RESOURCE_TO_MDM, UPDATE_LINK (when setting to MATCH) and MANUAL_MERGE_GOLDEN_RESOURCES.
	 * PID, identifiers and meta values are not affected by this operation.
	 *
	 * @param theTargetResource        Target resource to retrieve fields from
	 * @param theGoldenResource        Golden resource to merge fields into
	 * @param theMdmTransactionContext Current transaction context
	 * @param <T>
	 */
	@Override
	public <T extends IBase> void applySurvivorshipRulesToGoldenResource(T theTargetResource, T theGoldenResource, MdmTransactionContext theMdmTransactionContext) {
		switch (theMdmTransactionContext.getRestOperation()) {
			case MERGE_GOLDEN_RESOURCES:
				TerserUtil.mergeFields(myFhirContext, (IBaseResource) theTargetResource, (IBaseResource) theGoldenResource, TerserUtil.EXCLUDE_IDS_AND_META);
				break;
			default:
				TerserUtil.replaceFields(myFhirContext, (IBaseResource) theTargetResource, (IBaseResource) theGoldenResource, TerserUtil.EXCLUDE_IDS_AND_META);
				break;
		}
	}
}
