/*-
 * #%L
 * HAPI FHIR - Master Data Management
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
import ca.uhn.fhir.mdm.model.CanonicalEID;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import org.hl7.fhir.instance.model.api.IAnyResource;

import java.util.Collection;
import java.util.List;
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

	/**
	 * @deprecated use {@link #searchGoldenResourcesByEIDs(Collection, String, RequestPartitionId)}, which
	 * matches on the EID system as well as the value. This overload pairs the value with the first EID
	 * system configured for the resource type.
	 */
	@Deprecated
	Optional<IAnyResource> searchGoldenResourceByEID(String theEid, String theResourceType);

	/**
	 * @deprecated use {@link #searchGoldenResourcesByEIDs(Collection, String, RequestPartitionId)}, which
	 * matches on the EID system as well as the value. This overload pairs the value with the first EID
	 * system configured for the resource type.
	 */
	@Deprecated
	Optional<IAnyResource> searchGoldenResourceByEID(
			String theEid, String theResourceType, RequestPartitionId thePartitionId);

	/**
	 * Finds every golden resource carrying any of the given EIDs, in a single search. A resource type may
	 * be identified by more than one EID system, so EIDs are matched on their system as well as their
	 * value.
	 * <p>
	 * More than one golden resource may legitimately be returned: that is what happens when an incoming
	 * resource carries EIDs that were previously assigned to separate golden resources. A single EID
	 * resolving to more than one golden resource is still an error.
	 * </p>
	 *
	 * @param theEids the EIDs to search for; an empty collection yields an empty result
	 * @param theResourceType the resource type to search
	 * @param thePartitionId the partition to search in, or {@literal null} for the default
	 * @return the matching golden resources; never {@literal null}
	 */
	List<IAnyResource> searchGoldenResourcesByEIDs(
			Collection<CanonicalEID> theEids, String theResourceType, RequestPartitionId thePartitionId);
}
