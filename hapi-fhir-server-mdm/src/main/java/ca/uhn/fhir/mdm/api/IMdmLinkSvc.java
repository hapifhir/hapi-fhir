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
import org.hl7.fhir.instance.model.api.IAnyResource;

public interface IMdmLinkSvc {

	/**
	 * Update a link between a Golden Resource record and its source resource record. If a link does not exist between
	 * these two records, create it.
	 *
	 * @param theGoldenResource        the Golden Resource to link the source resource to.
	 * @param theSourceResource        the source resource, which can be of any of the MDM supported types
	 * @param theMatchResult           the current status of the match to set the link to.
	 * @param theLinkSource            MANUAL or AUTO: what caused the link.
	 * @param theMdmTransactionContext
	 */
	void updateLink(IAnyResource theGoldenResource, IAnyResource theSourceResource, MdmMatchOutcome theMatchResult, MdmLinkSourceEnum theLinkSource, MdmTransactionContext theMdmTransactionContext);

	/**
	 * Delete a link between given Golden Resource and the corresponding source resource
	 *
	 * @param theExistingGoldenResource
	 * @param theSourceResource
	 * @param theMdmTransactionContext
	 */
	void deleteLink(IAnyResource theExistingGoldenResource, IAnyResource theSourceResource, MdmTransactionContext theMdmTransactionContext);
}
