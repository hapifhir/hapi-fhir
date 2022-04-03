package ca.uhn.fhir.jpa.api.svc;

/*-
 * #%L
 * HAPI FHIR Storage api
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
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.cross.IResourceLookup;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.instance.model.api.IIdType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * This interface is used to translate between {@link ResourcePersistentId}
 * and actual resource IDs.
 */
public interface IIdHelperService {

	/**
	 * Given a collection of resource IDs (resource type + id), resolves the internal persistent IDs.
	 * <p>
	 * This implementation will always try to use a cache for performance, meaning that it can resolve resources that
	 * are deleted (but note that forced IDs can't change, so the cache can't return incorrect results)
	 *
	 * @param theOnlyForcedIds If <code>true</code>, resources which are not existing forced IDs will not be resolved
	 */
	@Nonnull
	List<ResourcePersistentId> resolveResourcePersistentIdsWithCache(@Nonnull RequestPartitionId theRequestPartitionId, List<IIdType> theIds, boolean theOnlyForcedIds);

	/**
	 * Given a resource type and ID, determines the internal persistent ID for the resource.
	 *
	 * @throws ResourceNotFoundException If the ID can not be found
	 */
	@Nonnull
	ResourcePersistentId resolveResourcePersistentIds(@Nonnull RequestPartitionId theRequestPartitionId, String theResourceType, String theId);

	/**
	 * Returns a mapping of Id -> ResourcePersistentId.
	 * If any resource is not found, it will throw ResourceNotFound exception
	 * (and no map will be returned)
	 */
	@Nonnull
	Map<String, ResourcePersistentId> resolveResourcePersistentIds(@Nonnull RequestPartitionId theRequestPartitionId, String theResourceType, List<String> theIds);

	/**
	 * Given a persistent ID, returns the associated resource ID
	 */
	@Nonnull
	IIdType translatePidIdToForcedId(FhirContext theCtx, String theResourceType, ResourcePersistentId theId);

	/**
	 * Given a forced ID, convert it to it's Long value. Since you are allowed to use string IDs for resources, we need to
	 * convert those to the underlying Long values that are stored, for lookup and comparison purposes.
	 *
	 * @throws ResourceNotFoundException If the ID can not be found
	 */
	@Nonnull
	IResourceLookup resolveResourceIdentity(@Nonnull RequestPartitionId theRequestPartitionId, String theResourceType, String theResourceId) throws ResourceNotFoundException;

	/**
	 * Returns true if the given resource ID should be stored in a forced ID. Under default config
	 * (meaning client ID strategy is {@link ca.uhn.fhir.jpa.api.config.DaoConfig.ClientIdStrategyEnum#ALPHANUMERIC})
	 * this will return true if the ID has any non-digit characters.
	 * <p>
	 * In {@link ca.uhn.fhir.jpa.api.config.DaoConfig.ClientIdStrategyEnum#ANY} mode it will always return true.
	 */
	boolean idRequiresForcedId(String theId);

	/**
	 * Given a collection of resource IDs (resource type + id), resolves the internal persistent IDs.
	 * <p>
	 * This implementation will always try to use a cache for performance, meaning that it can resolve resources that
	 * are deleted (but note that forced IDs can't change, so the cache can't return incorrect results)
	 */
	@Nonnull
	List<ResourcePersistentId> resolveResourcePersistentIdsWithCache(RequestPartitionId theRequestPartitionId, List<IIdType> theIds);

	Optional<String> translatePidIdToForcedIdWithCache(ResourcePersistentId theResourcePersistentId);

	Map<Long, Optional<String>> translatePidsToForcedIds(Set<Long> thePids);

	/**
	 * Pre-cache a PID-to-Resource-ID mapping for later retrieval by {@link #translatePidsToForcedIds(Set)} and related methods
	 */
	void addResolvedPidToForcedId(ResourcePersistentId theResourcePersistentId, @Nonnull RequestPartitionId theRequestPartitionId, String theResourceType, @Nullable String theForcedId, @Nullable Date theDeletedAt);

}
