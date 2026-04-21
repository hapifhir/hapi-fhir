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
package ca.uhn.fhir.mdm.svc;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.mdm.api.IMdmLinkExpandSvc;
import ca.uhn.fhir.mdm.model.CanonicalEID;
import ca.uhn.fhir.mdm.util.EIDHelper;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.*;

/**
 * MDM link expansion service that is used when MDM mode is Match-Only and eid systems are defined in Mdm rules.
 * Expands resources by finding other resources with the same eids.
 */
public class MdmEidMatchOnlyExpandSvc implements IMdmLinkExpandSvc {

	private final DaoRegistry myDaoRegistry;

	private EIDHelper myEidHelper;

	public MdmEidMatchOnlyExpandSvc(DaoRegistry theDaoRegistry) {
		myDaoRegistry = theDaoRegistry;
	}

	public void setMyEidHelper(EIDHelper theEidHelper) {
		myEidHelper = theEidHelper;
	}

	/**
	 * MDM expands the resource with the given id by finding all resources with the same eids.
	 * @param theRequestPartitionId the partition to search in
	 * @param theId the resource ID to expand
	 * @return set of resource IDs with matching EIDs
	 */
	@Override
	public Set<String> expandMdmBySourceResourceId(RequestPartitionId theRequestPartitionId, IIdType theId) {
		Set<String> result = new HashSet<>();
		// 1. Resolve resource
		String resourceType = theId.getResourceType();
		SystemRequestDetails srd = SystemRequestDetails.forRequestPartitionId(theRequestPartitionId);
		IFhirResourceDao<IBaseResource> resourceDao = myDaoRegistry.getResourceDao(resourceType);
		IBaseResource resource = resourceDao.read(theId, srd);
		// 2. Extract EIDs from resource using EIDHelper
		List<CanonicalEID> eids = myEidHelper.getExternalEid(resource);
		if (!eids.isEmpty()) {
			// 3. Search for resources of same type with the same eid
			var map = new SearchParameterMap();
			final TokenOrListParam tokenOrListParam = new TokenOrListParam();
			eids.forEach(eid -> tokenOrListParam.addOr(new TokenParam(eid.getSystem(), eid.getValue())));
			map.add("identifier", tokenOrListParam);
			List<IIdType> ids = resourceDao.searchForResourceIds(map, srd);
			for (IIdType id : ids) {
				result.add(id.toUnqualifiedVersionless().getValue());
			}
		}
		return result;
	}

	@Override
	public Set<String> expandMdmBySourceResource(RequestPartitionId theRequestPartitionId, IBaseResource theResource) {
		return expandMdmBySourceResourceId(theRequestPartitionId, theResource.getIdElement());
	}

	@Override
	public Set<String> expandMdmBySourceResourcePid(
			RequestPartitionId theRequestPartitionId, IResourcePersistentId<?> theSourceResourcePid) {
		throw new UnsupportedOperationException(
				Msg.code(2809) + "This operation is not implemented when using MDM in MATCH_ONLY mode.");
	}

	@Override
	public Set<String> expandMdmByGoldenResourceId(
			RequestPartitionId theRequestPartitionId, IResourcePersistentId<?> theGoldenResourcePid) {
		// This operation is not applicable when using MDM in MATCH_ONLY mode,
		// return an emtpy set to rather than an exception to not affect existing code
		return Collections.emptySet();
	}

	@Override
	public Set<String> expandMdmByGoldenResourcePid(
			RequestPartitionId theRequestPartitionId, IResourcePersistentId<?> theGoldenResourcePid) {
		// This operation is not applicable when using MDM in MATCH_ONLY mode,
		// return an emtpy set to rather than an exception to not affect existing code
		return Collections.emptySet();
	}

	@Override
	public Set<String> expandMdmByGoldenResourceId(RequestPartitionId theRequestPartitionId, IIdType theId) {
		// This operation is not applicable when using MDM in MATCH_ONLY mode,
		// return an emtpy set to rather than an exception to not affect existing code
		return Collections.emptySet();
	}
}
