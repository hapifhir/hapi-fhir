package ca.uhn.fhir.jpa.mdm.svc;

/*-
 * #%L
 * HAPI FHIR JPA Server - Master Data Management
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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
import ca.uhn.fhir.mdm.util.TerserUtil;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;

public class MdmSurvivorshipSvcImpl implements IMdmSurvivorshipService {

	@Autowired
	private FhirContext myFhirContext;

	/**
	 * TODO NG Update docs to tell taht we also do that in non GR merge - like linking
	 *
	 * Merges two golden resources by overwriting all field values on theGoldenResource param for all REST operation methods
	 * except MERGE_GOLDEN_RESOURCES. In case of MERGE_GOLDEN_RESOURCES, it will attempt to copy field values from
	 * theTargetResource that do not exist in theGoldenResource. PID, indentifiers and meta values are not affected by
	 * this operation.
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
