package ca.uhn.fhir.jpa.cache;

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

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.util.QueryChunker;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * This service builds a map of resource ids to versions based on a SearchParameterMap.
 * It is used by the in-memory resource-version cache to detect when resource versions have been changed by remote processes.
 */
@Service
public class ResourceVersionSvcDaoImpl implements IResourceVersionSvc {
	private static final Logger ourLog = getLogger(ResourceVersionSvcDaoImpl.class);

	@Autowired
	DaoRegistry myDaoRegistry;
	@Autowired
	IResourceTableDao myResourceTableDao;
	@Autowired
	IIdHelperService myIdHelperService;

	@Override
	@Nonnull
    @Transactional
	public ResourceVersionMap getVersionMap(RequestPartitionId theRequestPartitionId, String theResourceName, SearchParameterMap theSearchParamMap) {
		IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(theResourceName);

		if (ourLog.isDebugEnabled()) {
			ourLog.debug("About to retrieve version map for resource type: {}", theResourceName);
		}

		List<Long> matchingIds = dao.searchForIds(theSearchParamMap, new SystemRequestDetails().setRequestPartitionId(theRequestPartitionId)).stream()
			.map(ResourcePersistentId::getIdAsLong)
			.collect(Collectors.toList());

		List<ResourceTable> allById = new ArrayList<>();
		new QueryChunker<Long>().chunk(matchingIds, t -> {
			List<ResourceTable> nextBatch = myResourceTableDao.findAllById(t);
			allById.addAll(nextBatch);
		});

		return ResourceVersionMap.fromResourceTableEntities(allById);
	}

	@Override
	/**
	 * Retrieves the latest versions for any resourceid that are found.
	 * If they are not found, they will not be contained in the returned map.
	 * The key should be the same value that was passed in to allow
	 * consumer to look up the value using the id they already have.
	 *
	 * This method should not throw, so it can safely be consumed in
	 * transactions.
	 *
	 * @param theRequestPartitionId - request partition id
	 * @param theIds - list of IIdTypes for resources of interest.
	 * @return
	 */
	public ResourcePersistentIdMap getLatestVersionIdsForResourceIds(RequestPartitionId theRequestPartitionId, List<IIdType> theIds) {
		ResourcePersistentIdMap idToPID = new ResourcePersistentIdMap();
		HashMap<String, List<IIdType>> resourceTypeToIds = new HashMap<>();

		for (IIdType id : theIds) {
			String resourceType = id.getResourceType();
			if (!resourceTypeToIds.containsKey(resourceType)) {
				resourceTypeToIds.put(resourceType, new ArrayList<>());
			}
			resourceTypeToIds.get(resourceType).add(id);
		}

		for (String resourceType : resourceTypeToIds.keySet()) {
			ResourcePersistentIdMap idAndPID = getIdsOfExistingResources(theRequestPartitionId,
				resourceTypeToIds.get(resourceType));
			idToPID.putAll(idAndPID);
		}

		return idToPID;
	}

	/**
	 * Helper method to determine if some resources exist in the DB (without throwing).
	 * Returns a set that contains the IIdType for every resource found.
	 * If it's not found, it won't be included in the set.
	 *
	 * @param theIds - list of IIdType ids (for the same resource)
	 * @return
	 */
	private ResourcePersistentIdMap getIdsOfExistingResources(RequestPartitionId thePartitionId,
																				 Collection<IIdType> theIds) {
		// these are the found Ids that were in the db
		ResourcePersistentIdMap retval = new ResourcePersistentIdMap();

		if (theIds == null || theIds.isEmpty()) {
			return retval;
		}

		List<ResourcePersistentId> resourcePersistentIds = myIdHelperService.resolveResourcePersistentIdsWithCache(thePartitionId,
			theIds.stream().collect(Collectors.toList()));

		// we'll use this map to fetch pids that require versions
		HashMap<Long, ResourcePersistentId> pidsToVersionToResourcePid = new HashMap<>();

		// fill in our map
		for (ResourcePersistentId pid : resourcePersistentIds) {
			if (pid.getVersion() == null) {
				pidsToVersionToResourcePid.put(pid.getIdAsLong(), pid);
			}
			Optional<IIdType> idOp = theIds.stream()
				.filter(i -> i.getIdPart().equals(pid.getAssociatedResourceId().getIdPart()))
				.findFirst();
			// this should always be present
			// since it was passed in.
			// but land of optionals...
			idOp.ifPresent(id -> {
				retval.put(id, pid);
			});
		}

		// set any versions we don't already have
		if (!pidsToVersionToResourcePid.isEmpty()) {
			Collection<Object[]> resourceEntries = myResourceTableDao
				.getResourceVersionsForPid(new ArrayList<>(pidsToVersionToResourcePid.keySet()));

			for (Object[] record : resourceEntries) {
				// order matters!
				Long retPid = (Long) record[0];
				String resType = (String) record[1];
				Long version = (Long) record[2];
				pidsToVersionToResourcePid.get(retPid).setVersion(version);
			}
		}

		return retval;
	}
}
