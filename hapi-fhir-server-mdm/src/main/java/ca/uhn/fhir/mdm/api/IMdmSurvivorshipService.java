/*-
 * #%L
 * HAPI FHIR - Master Data Management
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.mdm.api;

import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import org.hl7.fhir.instance.model.api.IBase;

/**
 * Service that applies survivorship rules on target and golden resources.
 */
public interface IMdmSurvivorshipService {

	/**
	 * Merges two golden resources by overwriting all field values on theGoldenResource param for CREATE_RESOURCE,
	 * UPDATE_RESOURCE, SUBMIT_RESOURCE_TO_MDM, UPDATE_LINK (when setting to MATCH) and MANUAL_MERGE_GOLDEN_RESOURCES.
	 * PID, identifiers and meta values are not affected by this operation.
	 *
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
	<T extends IBase> void applySurvivorshipRulesToGoldenResource(
			T theTargetResource, T theGoldenResource, MdmTransactionContext theMdmTransactionContext);

	/**
	 * GoldenResources can have non-empty field data created from changes to the various
	 * resources that are matched to it (using some pre-defined survivorship rules).
	 *
	 * If a match link between a source and golden resource is broken, this method
	 * will rebuild/repopulate the GoldenResource based on the current links
	 * and current survivorship rules.
	 *
	 * @param theGoldenResource - the golden resource to rebuild
	 * @param theMdmTransactionContext - the transaction context
	 * @param <T> - Resource type to apply the survivorship rules to
	 */
	<T extends IBase> T rebuildGoldenResourceWithSurvivorshipRules(
			T theGoldenResource, MdmTransactionContext theMdmTransactionContext);
}
