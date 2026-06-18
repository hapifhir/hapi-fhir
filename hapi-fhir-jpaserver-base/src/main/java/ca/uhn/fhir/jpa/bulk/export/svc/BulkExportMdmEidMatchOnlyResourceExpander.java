/*-
 * #%L
 * HAPI FHIR JPA Server
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
package ca.uhn.fhir.jpa.bulk.export.svc;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.api.svc.ResolveIdentityMode;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.svc.IBulkExportMdmEidMatchOnlyResourceExpander;
import ca.uhn.fhir.mdm.svc.IBulkExportMdmResourceExpander;
import ca.uhn.fhir.mdm.svc.MdmEidMatchOnlyExpandSvc;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.util.FhirTerser;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation of {@link IBulkExportMdmResourceExpander} that handles bulk export resource expansion
 * when MDM mode is Match-Only and Eid Systems defined in mdm rules.
 *
 * <p>This expander is used during bulk export operations to expand Group resources by resolving
 * MDM matching resources for the members in the group. Resources are
 * matched based on just eids rather than the full MDM golden resource relationships.</p>
 */
public class BulkExportMdmEidMatchOnlyResourceExpander implements IBulkExportMdmEidMatchOnlyResourceExpander<JpaPid> {

	private final DaoRegistry myDaoRegistry;
	private final MdmEidMatchOnlyExpandSvc myMdmEidMatchOnlyLinkExpandSvc;
	private final FhirContext myFhirContext;
	private final IIdHelperService<JpaPid> myIdHelperService;

	/**
	 * MDM settings. Mutable because settings are not available at construction time and are pushed in
	 * later via {@link #setMdmSettings(IMdmSettings)} by {@code MdmExpandersHolder}.
	 */
	private IMdmSettings myMdmSettings;

	/**
	 * Constructor
	 */
	public BulkExportMdmEidMatchOnlyResourceExpander(
			DaoRegistry theDaoRegistry,
			MdmEidMatchOnlyExpandSvc theMdmEidMatchOnlyLinkExpandSvc,
			FhirContext theFhirContext,
			IIdHelperService<JpaPid> theIdHelperService) {
		myDaoRegistry = theDaoRegistry;
		myMdmEidMatchOnlyLinkExpandSvc = theMdmEidMatchOnlyLinkExpandSvc;
		myFhirContext = theFhirContext;
		myIdHelperService = theIdHelperService;
	}

	@Override
	public void setMdmSettings(IMdmSettings theMdmSettings) {
		myMdmSettings = theMdmSettings;
	}

	/**
	 * Determines the partition to use when expanding group/patient members. When MDM is configured to
	 * search all partitions for matching, members may live on partitions other than the request partition
	 * (e.g. the Group's default partition), so expansion must widen to {@link RequestPartitionId#allPartitions()}.
	 * Otherwise the original request partition is used.
	 * <p>
	 * In practice {@code myMdmSettings} is always set before this expander is used (the holder only hands
	 * out this expander once MDM settings have been applied), so the null check is a defensive fallback:
	 * if settings are somehow unset, we degrade to the request partition rather than throwing an NPE.
	 */
	private RequestPartitionId determineExpansionPartition(RequestPartitionId theRequestPartitionId) {
		return (myMdmSettings != null && myMdmSettings.getSearchAllPartitionForMatch())
				? RequestPartitionId.allPartitions()
				: theRequestPartitionId;
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
		Validate.notBlank(groupResourceId, "Group resource ID must not be blank");

		// Read the Group resource
		SystemRequestDetails srd = SystemRequestDetails.forRequestPartitionId(requestPartitionId);
		IIdType groupId = myFhirContext.getVersion().newIdType(groupResourceId);
		IFhirResourceDao<?> groupDao = myDaoRegistry.getResourceDao("Group");
		IBaseResource groupResource = groupDao.read(groupId, srd);

		RequestPartitionId expansionPartition = determineExpansionPartition(requestPartitionId);

		Set<String> allResourceIds = new HashSet<>();
		FhirTerser terser = myFhirContext.newTerser();
		// Extract all member.entity references from the Group resource
		List<IBaseReference> memberEntities =
				terser.getValues(groupResource, "Group.member.entity", IBaseReference.class);
		// mdm expand each member based on eid
		for (IBaseReference entityRef : memberEntities) {
			if (!entityRef.getReferenceElement().isEmpty()) {
				IIdType memberId = entityRef.getReferenceElement();
				Set<String> expanded =
						myMdmEidMatchOnlyLinkExpandSvc.expandMdmBySourceResourceId(expansionPartition, memberId);
				allResourceIds.addAll(expanded);
			}
		}
		// Convert all resourceIds to IIdType and resolve in batch
		List<IIdType> idTypes = allResourceIds.stream()
				.map(id -> myFhirContext.getVersion().newIdType(id))
				.collect(Collectors.toList());
		List<JpaPid> pidList = myIdHelperService.resolveResourcePids(
				expansionPartition,
				idTypes,
				ResolveIdentityMode.excludeDeleted().cacheOk());
		return new HashSet<>(pidList);
	}

	/**
	 * Expands a single patient ID to include all patients linked via EID matching.
	 *
	 * @param thePatientId Patient ID to expand (e.g., "Patient/123")
	 * @param theRequestPartitionId Partition context for the request
	 * @return Set of String patient IDs including the original patient and all EID-matched patients
	 */
	@Override
	public Set<String> expandPatient(String thePatientId, RequestPartitionId theRequestPartitionId) {
		IIdType patientIdType =
				myFhirContext.getVersion().newIdType(thePatientId).withResourceType("Patient");
		RequestPartitionId expansionPartition = determineExpansionPartition(theRequestPartitionId);
		return myMdmEidMatchOnlyLinkExpandSvc.expandMdmBySourceResourceId(expansionPartition, patientIdType);
	}

	@Override
	public void annotateResource(IBaseResource resource) {
		// This function is normally used to add golden resource id to the exported resources,
		// but in the Eid-based match only mode, there isn't any golden resource, so nothing to do here
	}
}
