/*-
 * #%L
 * HAPI FHIR JPA Server
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
package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.api.svc.ResolveIdentityMode;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.model.cross.IResourceLookup;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	IIdHelperService<JpaPid> myIdHelperService;

	@Override
	@Nonnull
	public ResourceVersionMap getVersionMap(
			RequestPartitionId theRequestPartitionId, String theResourceName, SearchParameterMap theSearchParamMap) {
		if (ourLog.isDebugEnabled()) {
			ourLog.debug("About to retrieve version map for resource type: {}", theResourceName);
		}

		IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(theResourceName);
		SystemRequestDetails request = new SystemRequestDetails().setRequestPartitionId(theRequestPartitionId);

		List<IIdType> fhirIds = dao.searchForResourceIds(theSearchParamMap, request);

		return ResourceVersionMap.fromIdsWithVersions(fhirIds);
	}

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
	 */
	@Override
	public ResourcePersistentIdMap getLatestVersionIdsForResourceIds(
			RequestPartitionId theRequestPartitionId, List<IIdType> theIds) {
		ResourcePersistentIdMap idToPID = new ResourcePersistentIdMap();

		ResourcePersistentIdMap idAndPID = getIdsOfExistingResources(theRequestPartitionId, theIds);
		idToPID.putAll(idAndPID);

		return idToPID;
	}

	/**
	 * Helper method to determine if some resources exist in the DB (without throwing).
	 * Returns a set that contains the IIdType for every resource found.
	 * If it's not found, it won't be included in the set.
	 *
	 * @param theIds - list of IIdType ids (for the same resource)
	 */
	private ResourcePersistentIdMap getIdsOfExistingResources(
			RequestPartitionId thePartitionId, Collection<IIdType> theIds) {
		// these are the found Ids that were in the db
		ResourcePersistentIdMap retVal = new ResourcePersistentIdMap();

		if (theIds == null || theIds.isEmpty()) {
			return retVal;
		}

		Map<IIdType, IResourceLookup<JpaPid>> identities = myIdHelperService.resolveResourceIdentities(
				thePartitionId,
				new ArrayList<>(theIds),
				ResolveIdentityMode.includeDeleted().cacheOk());

		Map<String, JpaPid> entriesWithoutVersion = new HashMap<>(identities.size());

		for (Map.Entry<IIdType, IResourceLookup<JpaPid>> entry : identities.entrySet()) {
			IResourceLookup<JpaPid> lookup = entry.getValue();
			JpaPid persistentId = lookup.getPersistentId();
			retVal.put(entry.getKey(), persistentId);
			if (persistentId.getVersion() == null) {
				entriesWithoutVersion.put(
						entry.getKey().toUnqualifiedVersionless().getValue(), persistentId);
			}
		}

		// set any versions we don't already have
		if (!entriesWithoutVersion.isEmpty()) {
			Collection<Object[]> resourceEntries =
					myResourceTableDao.getResourceVersionsForPid(entriesWithoutVersion.values());

			for (Object[] nextRecord : resourceEntries) {
				// order matters!
				JpaPid retPid = (JpaPid) nextRecord[0];
				String resType = (String) nextRecord[1];
				String fhirId = (String) nextRecord[2];
				Long version = (Long) nextRecord[3];
				JpaPid jpaPid = entriesWithoutVersion.get(resType + "/" + fhirId);
				if (jpaPid != null) {
					jpaPid.setVersion(version);
				}
			}
		}

		return retVal;
	}
}
