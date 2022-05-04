package ca.uhn.fhir.jpa.dao.index;

/*-
 * #%L
 * HAPI FHIR JPA Server
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.dao.index.IdHelperService.RESOURCE_PID;

/**
 * See {@link IJpaIdHelperService} for an explanation of this class.
 */
public class JpaIdHelperService extends IdHelperService implements IJpaIdHelperService, IIdHelperService {
	@Autowired
	protected IResourceTableDao myResourceTableDao;

	/**
	 * @deprecated This method doesn't take a partition ID as input, so it is unsafe. It
	 * should be reworked to include the partition ID before any new use is incorporated
	 */
	@Override
	@Deprecated
	@Nonnull
	public List<Long> getPidsOrThrowException(List<IIdType> theIds) {
		List<ResourcePersistentId> resourcePersistentIds = super.resolveResourcePersistentIdsWithCache(RequestPartitionId.allPartitions(), theIds);
		return resourcePersistentIds.stream().map(ResourcePersistentId::getIdAsLong).collect(Collectors.toList());
	}


	/**
	 * @deprecated This method doesn't take a partition ID as input, so it is unsafe. It
	 * should be reworked to include the partition ID before any new use is incorporated
	 */
	@Override
	@Deprecated
	@Nullable
	public Long getPidOrNull(IBaseResource theResource) {

		IAnyResource anyResource = (IAnyResource) theResource;
		Long retVal = (Long) anyResource.getUserData(RESOURCE_PID);
		if (retVal == null) {
			IIdType id = theResource.getIdElement();
			try {
				retVal = super.resolveResourcePersistentIds(RequestPartitionId.allPartitions(), id.getResourceType(), id.getIdPart()).getIdAsLong();
			} catch (ResourceNotFoundException e) {
				return null;
			}
		}
		return retVal;
	}


	/**
	 * @deprecated This method doesn't take a partition ID as input, so it is unsafe. It
	 * should be reworked to include the partition ID before any new use is incorporated
	 */
	@Override
	@Deprecated
	@Nonnull
	public Long getPidOrThrowException(IIdType theId) {
		assert TransactionSynchronizationManager.isSynchronizationActive();

		List<IIdType> ids = Collections.singletonList(theId);
		List<ResourcePersistentId> resourcePersistentIds = super.resolveResourcePersistentIdsWithCache(RequestPartitionId.allPartitions(), ids);
		return resourcePersistentIds.get(0).getIdAsLong();
	}

	@Override
	@Nonnull
	public Long getPidOrThrowException(@Nonnull IAnyResource theResource) {
		Long retVal = (Long) theResource.getUserData(RESOURCE_PID);
		if (retVal == null) {
			throw new IllegalStateException(Msg.code(1102) + String.format("Unable to find %s in the user data for %s with ID %s", RESOURCE_PID, theResource, theResource.getId())
			);
		}
		return retVal;
	}

	@Override
	public IIdType resourceIdFromPidOrThrowException(Long thePid) {
		Optional<ResourceTable> optionalResource = myResourceTableDao.findById(thePid);
		if (!optionalResource.isPresent()) {
			throw new ResourceNotFoundException(Msg.code(1103) + "Requested resource not found");
		}
		return optionalResource.get().getIdDt().toVersionless();
	}

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
	@Override
	public Set<String> translatePidsToFhirResourceIds(Set<Long> thePids) {
		assert TransactionSynchronizationManager.isSynchronizationActive();

		Map<Long, Optional<String>> pidToForcedIdMap = super.translatePidsToForcedIds(thePids);

		//If the result of the translation is an empty optional, it means there is no forced id, and we can use the PID as the resource ID.
		Set<String> resolvedResourceIds = pidToForcedIdMap.entrySet().stream()
			.map(entry -> entry.getValue().isPresent() ? entry.getValue().get() : entry.getKey().toString())
			.collect(Collectors.toSet());

		return resolvedResourceIds;

	}

}
