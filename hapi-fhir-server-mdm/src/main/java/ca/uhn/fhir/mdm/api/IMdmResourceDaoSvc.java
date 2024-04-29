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

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import org.hl7.fhir.instance.model.api.IAnyResource;

import java.util.Optional;

public interface IMdmResourceDaoSvc {
	DaoMethodOutcome upsertGoldenResource(IAnyResource theGoldenResource, String theResourceType);

	/**
	 * Given a resource, remove its Golden Resource tag.
	 *
	 * @param theGoldenResource the {@link IAnyResource} to remove the tag from.
	 * @param theResourcetype   the type of that resource
	 */
	void removeGoldenResourceTag(IAnyResource theGoldenResource, String theResourcetype);

	IAnyResource readGoldenResourceByPid(IResourcePersistentId theGoldenResourcePid, String theResourceType);

	Optional<IAnyResource> searchGoldenResourceByEID(String theEid, String theResourceType);

	Optional<IAnyResource> searchGoldenResourceByEID(
			String theEid, String theResourceType, RequestPartitionId thePartitionId);
}
