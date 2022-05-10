package ca.uhn.fhir.mdm.api;

/*-
 * #%L
 * HAPI FHIR - Master Data Management
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

import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import org.hl7.fhir.instance.model.api.IBase;

/**
 * Service that applies survivorship rules on target and golden resources.
 */
public interface IMdmSurvivorshipService {

	/**
	 * Applies survivorship rules to merge fields from the specified target resource to the golden resource. Survivorship
	 * rules may include, but not limited to the following data consolidation methods:
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
	 *  Quality of data - best quality data - e.g. data coming from a certain system is considered trusted and overrides
	 *  all other values
	 *  </li>
	 *  <li>
	 *  A hybrid approach combining all methods listed above as best fits
	 *  </li>
	 * </ul>
	 *
	 * @param theTargetResource        Target resource to merge fields from
	 * @param theGoldenResource        Golden resource to merge fields into
	 * @param theMdmTransactionContext Current transaction context
	 * @param <T>                      Resource type to apply the survivorship rules to
	 */
	<T extends IBase> void applySurvivorshipRulesToGoldenResource(T theTargetResource, T theGoldenResource, MdmTransactionContext theMdmTransactionContext);
}
