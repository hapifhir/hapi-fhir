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

public interface IGoldenResourceMergerSvc {
	/**
	 * Move all links from the theFromGoldenResource to theToGoldenResource and then set active=false on theFromGoldenResource.
	 * Merge all Golden Resource fields subject to survivorship rules.
	 *
	 * @param theFromGoldenResource the golden resource we are merging from
	 * @param theManuallyMergedResource an optional golden resource that was manually merged
	 * @param theToGoldenResource the golden resource we are merging to
	 * @return updated theToGoldenResource with the merged fields and links.
	 */
	IAnyResource mergeGoldenResources(IAnyResource theFromGoldenResource, IAnyResource theManuallyMergedResource, IAnyResource theToGoldenResource, MdmTransactionContext theMdmTransactionContext);
}
