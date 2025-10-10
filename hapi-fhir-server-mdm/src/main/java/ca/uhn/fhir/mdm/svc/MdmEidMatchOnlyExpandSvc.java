/*-
 * #%L
 * HAPI FHIR - Master Data Management
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
package ca.uhn.fhir.mdm.svc;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.api.svc.ResolveIdentityMode;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.mdm.api.IMdmLinkExpandSvc;
import ca.uhn.fhir.mdm.model.CanonicalEID;
import ca.uhn.fhir.mdm.util.EIDHelper;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.util.FhirTerser;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.stream.Collectors;
import java.util.*;

/**
 * MDM link expansion service that is used when MDM mode is Match-Only and eid systems are defined in Mdm rules.
 * Expands resources by finding other resources with the same eids.
 */
public class MdmEidMatchOnlyExpandSvc implements IMdmLinkExpandSvc {

	private final DaoRegistry myDaoRegistry;

	private EIDHelper myEidHelper;

	private IIdHelperService myIdHelperService;

	private FhirContext myFhirContext;

	public MdmEidMatchOnlyExpandSvc(
		DaoRegistry theDaoRegistry, FhirContext theFhirContext, IIdHelperService theIdHelperService, EIDHelper theEidHelper) {
		myDaoRegistry = theDaoRegistry;
		myFhirContext = theFhirContext;
		myIdHelperService = theIdHelperService;
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
	/**
	 * Expands a Group resource and returns the Group members' resource persistent ids.
	 * The returned ids consists of group members + all MDM matched resources based on EID only.
	 *
	 * <p>This method:</p>
	 * <ol>
	 *   <li>Reads the specified Group resource</li>
	 *   <li>Extracts all member entity references from the Group</li>
	 *   <li>For each member, uses EID matching to find all resources that have the same EID as the member, using eid system specified in mdm rules</li>
	 *   <li>Converts the expanded resource IDs to persistent IDs (PIDs)</li>
	 * </ol>
	 *
	 * @param groupResourceId The ID of the Group resource to expand
	 * @param requestPartitionId The request partition ID
	 * @return A set of {@link JpaPid} objects representing all expanded resources
	 */
	@Override
	public Set<JpaPid> expandGroup(String groupResourceId, RequestPartitionId requestPartitionId) {
		// Read the Group resource
		SystemRequestDetails srd = SystemRequestDetails.forRequestPartitionId(requestPartitionId);
		IIdType groupId = myFhirContext.getVersion().newIdType(groupResourceId);
		IFhirResourceDao<?> groupDao = myDaoRegistry.getResourceDao("Group");
		IBaseResource groupResource = groupDao.read(groupId, srd);

		Set<String> allResourceIds = new HashSet<>();
		FhirTerser terser = myFhirContext.newTerser();
		// Extract all member.entity references from the Group resource
		List<IBaseReference> memberEntities =
				terser.getValues(groupResource, "Group.member.entity", IBaseReference.class);
		// mdm expand each member based on eid
		for (IBaseReference entityRef : memberEntities) {
			if (!entityRef.getReferenceElement().isEmpty()) {
				IIdType memberId = entityRef.getReferenceElement();
				Set<String> expanded = this.expandMdmBySourceResourceId(requestPartitionId, memberId);
				allResourceIds.addAll(expanded);
			}
		}
		// Convert all resourceIds to IIdType and resolve in batch
		List<IIdType> idTypes = allResourceIds.stream()
				.map(id -> myFhirContext.getVersion().newIdType(id))
				.collect(Collectors.toList());
		List<JpaPid> pidList = myIdHelperService.resolveResourcePids(
				requestPartitionId,
				idTypes,
				ResolveIdentityMode.excludeDeleted().cacheOk());
		return new HashSet<>(pidList);
	}

	@Override
	public void annotateResource(IBaseResource resource) {
		// This function is normally used to add golden resource id to the exported resources,
		// but in the Eid-based match only mode, there isn't any golden resource, so nothing to do here
	}
}
