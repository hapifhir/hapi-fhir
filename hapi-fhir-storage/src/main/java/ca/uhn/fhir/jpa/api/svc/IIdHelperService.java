/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.api.svc;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.model.PersistentIdToForcedIdMap;
import ca.uhn.fhir.jpa.model.cross.IResourceLookup;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This interface is used to translate between {@link IResourcePersistentId}
 * and actual resource IDs.
 */
public interface IIdHelperService<T extends IResourcePersistentId<?>> {

	/**
	 * Given a persistent ID, returns the associated resource ID
	 */
	@Nonnull
	IIdType translatePidIdToForcedId(FhirContext theCtx, String theResourceType, T theId);

	/**
	 * @param theResourceType Note that it is inefficient to call this method
	 *                        with a null resource type, so this should be avoided
	 *                        unless strictly necessary.
	 * @throws ResourceNotFoundException If the ID can not be found
	 */
	@Nonnull
	IResourceLookup<T> resolveResourceIdentity(
			@Nonnull RequestPartitionId theRequestPartitionId,
			@Nullable String theResourceType,
			@Nonnull String theResourceId,
			@Nonnull ResolveIdentityMode theMode)
			throws ResourceNotFoundException;

	/**
	 * @param theResourceType Note that it is inefficient to call this method
	 *                        with a null resource type, so this should be avoided
	 *                        unless strictly necessary.
	 * @throws ResourceNotFoundException If the ID can not be found
	 */
	@Nonnull
	default T resolveResourceIdentityPid(
			@Nonnull RequestPartitionId theRequestPartitionId,
			@Nullable String theResourceType,
			@Nonnull String theResourceId,
			@Nonnull ResolveIdentityMode theMode)
			throws ResourceNotFoundException {
		return resolveResourceIdentity(theRequestPartitionId, theResourceType, theResourceId, theMode)
				.getPersistentId();
	}

	/**
	 * Given a collection of resource IDs, resolve the resource identities, including the persistent ID,
	 * deleted status, resource type, etc.
	 *
	 * @since 8.0.0
	 */
	@Nonnull
	Map<IIdType, IResourceLookup<T>> resolveResourceIdentities(
			@Nonnull RequestPartitionId theRequestPartitionId, Collection<IIdType> theIds, ResolveIdentityMode theMode);

	/**
	 * Given a collection of resource IDs, resolve the resource persistent IDs.
	 *
	 * @since 8.0.0
	 */
	default List<T> resolveResourcePids(
			RequestPartitionId theRequestPartitionId,
			List<IIdType> theTargetIds,
			ResolveIdentityMode theResolveIdentityMode) {
		return resolveResourceIdentities(theRequestPartitionId, theTargetIds, theResolveIdentityMode).values().stream()
				.map(IResourceLookup::getPersistentId)
				.collect(Collectors.toList());
	}

	/**
	 * Returns true if the given resource ID should be stored in a forced ID. Under default config
	 * (meaning client ID strategy is {@link JpaStorageSettings.ClientIdStrategyEnum#ALPHANUMERIC})
	 * this will return true if the ID has any non-digit characters.
	 * <p>
	 * In {@link JpaStorageSettings.ClientIdStrategyEnum#ANY} mode it will always return true.
	 */
	boolean idRequiresForcedId(String theId);

	/**
	 * Value will be an empty Optional if the PID doesn't exist, or
	 * a typed resource ID if so (Patient/ABC).
	 */
	Optional<String> translatePidIdToForcedIdWithCache(T theResourcePersistentId);

	/**
	 * Values in the returned map are typed resource IDs (Patient/ABC)
	 */
	PersistentIdToForcedIdMap<T> translatePidsToForcedIds(Set<T> theResourceIds);

	/**
	 * This method can be called to pre-emptively add entries to the ID cache. It should
	 * be called by DAO methods if they are creating or changing the deleted status
	 * of a resource. This method returns immediately, but the data is not
	 * added to the internal caches until the current DB transaction is successfully
	 * committed, and nothing is added if the transaction rolls back.
	 */
	void addResolvedPidToFhirIdAfterCommit(
			@Nonnull T theResourcePersistentId,
			@Nonnull RequestPartitionId theRequestPartitionId,
			@Nonnull String theResourceType,
			@Nonnull String theFhirId,
			@Nullable Date theDeletedAt);

	@Nullable
	T getPidOrNull(RequestPartitionId theRequestPartitionId, IBaseResource theResource);

	@Nonnull
	default T getPidOrThrowException(RequestPartitionId theRequestPartitionId, IIdType theId) {
		IResourceLookup<T> identity = resolveResourceIdentity(
				theRequestPartitionId,
				theId.getResourceType(),
				theId.getIdPart(),
				ResolveIdentityMode.includeDeleted().cacheOk());
		if (identity == null) {
			throw new InvalidRequestException(Msg.code(2295) + "Invalid ID was provided: [" + theId.getIdPart() + "]");
		}
		return identity.getPersistentId();
	}

	@Nonnull
	T getPidOrThrowException(@Nonnull IAnyResource theResource);

	IIdType resourceIdFromPidOrThrowException(T thePid, String theResourceType);

	/**
	 * Given a set of PIDs, return a set of public FHIR Resource IDs.
	 * This function will resolve a forced ID if it resolves, and if it fails to resolve to a forced it, will just return the pid
	 * Example:
	 * Let's say we have Patient/1(pid == 1), Patient/pat1 (pid == 2), Patient/3 (pid == 3), their pids would resolve as follows:
	 * <p>
	 * [1,2,3] -> ["1","pat1","3"]
	 *
	 * @param thePids The Set of pids you would like to resolve to external FHIR Resource IDs.
	 * @return A Set of strings representing the FHIR IDs of the pids.
	 */
	Set<String> translatePidsToFhirResourceIds(Set<T> thePids);

	/**
	 * @deprecated Use {@link #newPid(Object, Integer)}
	 */
	@Deprecated
	T newPid(Object thePid);

	T newPid(Object thePid, Integer thePartitionId);

	T newPidFromStringIdAndResourceName(Integer thePartitionId, String thePid, String theResourceType);
}
