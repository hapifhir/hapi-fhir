/*-
 * #%L
 * HAPI FHIR JPA Server - Master Data Management
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
package ca.uhn.fhir.jpa.mdm.svc;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.model.entity.TagTypeEnum;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.mdm.api.IMdmResourceDaoSvc;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.api.MdmConstants;
import ca.uhn.fhir.mdm.log.Logs;
import ca.uhn.fhir.mdm.model.CanonicalEID;
import ca.uhn.fhir.mdm.model.MdmMatchAbortReason;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.mdm.util.MdmResourceUtil;
import ca.uhn.fhir.mdm.util.MdmSearchParamBuildingUtils;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
public class MdmResourceDaoSvcImpl implements IMdmResourceDaoSvc {
	private static final Logger ourLog = Logs.getMdmTroubleshootingLog();

	private static final int MAX_MATCHING_GOLDEN_RESOURCES = 1000;

	@Autowired
	DaoRegistry myDaoRegistry;

	@Autowired
	IMdmSettings myMdmSettings;

	@Autowired
	FhirContext myFhirContext;

	@Override
	public DaoMethodOutcome upsertGoldenResource(IAnyResource theGoldenResource, String theResourceType) {
		IFhirResourceDao resourceDao = myDaoRegistry.getResourceDao(theResourceType);
		RequestDetails requestDetails = new SystemRequestDetails().setRequestPartitionId((RequestPartitionId)
				theGoldenResource.getUserData(Constants.RESOURCE_PARTITION_ID));
		if (theGoldenResource.getIdElement().hasIdPart()) {
			return resourceDao.update(theGoldenResource, requestDetails);
		} else {
			return resourceDao.create(theGoldenResource, requestDetails);
		}
	}

	@Override
	public void removeGoldenResourceTag(IAnyResource theGoldenResource, String theResourcetype) {
		IFhirResourceDao resourceDao = myDaoRegistry.getResourceDao(theResourcetype);
		RequestDetails requestDetails = new SystemRequestDetails().setRequestPartitionId((RequestPartitionId)
				theGoldenResource.getUserData(Constants.RESOURCE_PARTITION_ID));
		resourceDao.removeTag(
				theGoldenResource.getIdElement(),
				TagTypeEnum.TAG,
				MdmConstants.SYSTEM_GOLDEN_RECORD_STATUS,
				MdmConstants.CODE_GOLDEN_RECORD,
				requestDetails);
	}

	@Override
	public IAnyResource readGoldenResourceByPid(IResourcePersistentId theGoldenResourcePid, String theResourceType) {
		IFhirResourceDao resourceDao = myDaoRegistry.getResourceDao(theResourceType);
		return (IAnyResource) resourceDao.readByPid(theGoldenResourcePid);
	}

	@Override
	public Optional<IAnyResource> searchGoldenResourceByEID(String theEid, String theResourceType) {
		return this.searchGoldenResourceByEID(theEid, theResourceType, null);
	}

	@Override
	public Optional<IAnyResource> searchGoldenResourceByEID(
			String theEid, String theResourceType, RequestPartitionId thePartitionId) {
		String eidSystem = myMdmSettings.getMdmRules().getEnterpriseEIDSystemForResourceType(theResourceType);
		List<IAnyResource> goldenResources = searchGoldenResourcesByEIDs(
				Collections.singletonList(new CanonicalEID(eidSystem, theEid, null)), theResourceType, thePartitionId);
		return goldenResources.stream().findFirst();
	}

	@Override
	public List<IAnyResource> searchGoldenResourcesByEIDs(
			Collection<CanonicalEID> theEids, String theResourceType, RequestPartitionId thePartitionId) {
		Optional<SearchParameterMap> map = MdmSearchParamBuildingUtils.buildEidSearchParameterMap(theEids);
		if (map.isEmpty()) {
			return Collections.emptyList();
		}

		IFhirResourceDao<?> resourceDao = myDaoRegistry.getResourceDao(theResourceType);
		SystemRequestDetails systemRequestDetails = new SystemRequestDetails();
		systemRequestDetails.setRequestPartitionId(thePartitionId);
		IBundleProvider search = resourceDao.search(map.get(), systemRequestDetails);
		List<IBaseResource> resources = search.getResources(0, MAX_MATCHING_GOLDEN_RESOURCES);

		validateNoEidResolvesToMultipleGoldenResources(theEids, resources);

		return resources.stream().map(IAnyResource.class::cast).collect(Collectors.toList());
	}

	/**
	 * Several golden resources may legitimately come back from one search - that is the case an incoming
	 * resource carrying EIDs previously assigned to separate golden resources produces. What remains an
	 * error is a single EID resolving to more than one golden resource, which means the golden resources
	 * themselves are corrupt.
	 */
	private void validateNoEidResolvesToMultipleGoldenResources(
			Collection<CanonicalEID> theEids, List<IBaseResource> theGoldenResources) {
		if (theGoldenResources.size() < 2) {
			return;
		}

		for (CanonicalEID eid : theEids) {
			List<IBaseResource> matches = theGoldenResources.stream()
					.filter(goldenResource -> carriesEid(goldenResource, eid))
					.toList();
			if (matches.size() > 1) {
				throw new InternalErrorException(
						Msg.code(737) + "Found more than one active " + MdmConstants.CODE_HAPI_MDM_MANAGED
								+ " Golden Resource with EID "
								+ eid.getValue()
								+ ": "
								+ matches.get(0).getIdElement().getValue()
								+ ", "
								+ matches.get(1).getIdElement().getValue());
			}
		}
	}

	private boolean carriesEid(IBaseResource theGoldenResource, CanonicalEID theEid) {
		return CanonicalEID.extractFromResource(
						myFhirContext, Collections.singletonList(theEid.getSystem()), theGoldenResource)
				.stream()
				.anyMatch(candidate -> Objects.equals(candidate.getValue(), theEid.getValue()));
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public void updateUnmatchedTags(@Nonnull IBaseResource theResource, @Nonnull MdmTransactionContext theContext) {
		if (!theResource.getIdElement().hasIdPart()) {
			ourLog.error("Cannot tag resources that have not first been persisted!");
			return;
		}

		String desiredCode =
				theContext.isMatchingAborted() ? theContext.getReason().getCode() : null;

		Set<String> codesToRemove = theResource.getMeta().getTag().stream()
				.filter(tag -> MdmConstants.MDM_UNMATCHED_TAG_NAMESPACE.equalsIgnoreCase(tag.getSystem()))
				.map(IBaseCoding::getCode)
				.filter(code -> !Objects.equals(desiredCode, code))
				.collect(Collectors.toSet());

		boolean needsTag = isNotBlank(desiredCode)
				&& theResource.getMeta().getTag(MdmConstants.MDM_UNMATCHED_TAG_NAMESPACE, desiredCode) == null;

		if (!needsTag && codesToRemove.isEmpty()) {
			// already correct
			return;
		}

		IFhirResourceDao resourceDao = myDaoRegistry.getResourceDao(theResource.fhirType());

		IIdType id = theResource.getIdElement().toUnqualifiedVersionless();
		SystemRequestDetails rd = getSystemRequestDetailsForResource(theResource);

		// tags stored outside the resource body are not removed by an update
		for (String code : codesToRemove) {
			resourceDao.removeTag(id, TagTypeEnum.TAG, MdmConstants.MDM_UNMATCHED_TAG_NAMESPACE, code, rd);
		}

		MdmResourceUtil.removeTagWithSystem(theResource, MdmConstants.MDM_UNMATCHED_TAG_NAMESPACE);
		if (theContext.getReason() == MdmMatchAbortReason.BLOCKED) {
			MdmResourceUtil.tagResourceAsBlocked(theResource);
		} else if (theContext.getReason() == MdmMatchAbortReason.TOO_MANY_CANDIDATES) {
			MdmResourceUtil.tagResourceAsTooManyMatchCandidates(theResource);
		}
		resourceDao.update(theResource, rd);
	}

	private SystemRequestDetails getSystemRequestDetailsForResource(IBaseResource theResource) {
		SystemRequestDetails rd = new SystemRequestDetails();
		RequestPartitionId partitionId = RequestPartitionId.getPartitionFromUserDataIfPresent(theResource)
				.orElse(null);
		rd.setRequestPartitionId(partitionId);
		return rd;
	}
}
