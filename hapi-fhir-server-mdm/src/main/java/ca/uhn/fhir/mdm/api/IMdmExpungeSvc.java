package ca.uhn.fhir.mdm.api;

/*-
 * #%L
 * HAPI FHIR - Master Data Management
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

import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;

public interface IMdmExpungeSvc {

	/**
	 * Given a resource type, delete the underlying MDM links, and their related golden resource objects.
	 *
	 * @param theSourceResourceType The type of resources
	 * @param theRequestDetails
	 * @return the count of deleted MDM links
	 */
	long expungeAllMdmLinksOfSourceType(String theSourceResourceType, ServletRequestDetails theRequestDetails);

	/**
	 * Delete all MDM links, and their related golden resource objects.
	 *
	 * @return the count of deleted MDM links
	 * @param theRequestDetails
	 */
	long expungeAllMdmLinks(ServletRequestDetails theRequestDetails);
}
