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
	 * Survivorship rules may include the following data consolidation methods:
	 *
	 * <ul>
	 *  <li>
	 *  Length of field - apply the field value containing most or least number of characters - e.g. longest name
	 *  </li>
	 *  <li>
	 *  Date time - all the field value from the oldest or the newest recrod - e.g. use the most recent phone number
	 *  </li>
	 *  <li>
	 *  Frequency - use the most or least frequent number of occurrence - e.g. most common phone number
	 *  </li>
	 *  <li>
	 *  Integer - number functions (largest, sum, avg) - e.g. number of patient encounters
	 *  </li>
	 *  <li>
	 *  Quality of data - best quality data - e.g. data coming from a certain system is considered trusted and overrides all other values
	 *  </li>
	 *  <li>
	 *  A hybrid approach combining all methods listed above as best fits
	 *  </li>
	 * </ul>
	 *
	 * @param theTargetResource        Target resource to merge fields from
	 * @param theGoldenResource        Golden resource to merge fields into
	 * @param theMdmTransactionContext Current transaction context
	 * @param <T>
	 */
	@Override
	public <T extends IBase> void applySurvivorshipRulesToGoldenResource(T theTargetResource, T theGoldenResource, MdmTransactionContext theMdmTransactionContext) {
		switch (theMdmTransactionContext.getRestOperation()) {
			case MERGE_GOLDEN_RESOURCES:
				if (theMdmTransactionContext.isForceResourceUpdate()) {
					TerserUtil.overwriteFields(myFhirContext, (IBaseResource) theTargetResource, (IBaseResource) theGoldenResource, TerserUtil.EXCLUDE_IDS_AND_META);
					break;
				}
				TerserUtil.mergeFields(myFhirContext, (IBaseResource) theTargetResource, (IBaseResource) theGoldenResource, TerserUtil.EXCLUDE_IDS_AND_META);
				break;
			default:
				TerserUtil.overwriteFields(myFhirContext, (IBaseResource) theTargetResource, (IBaseResource) theGoldenResource, TerserUtil.EXCLUDE_IDS_AND_META);
				break;
		}
	}
}
