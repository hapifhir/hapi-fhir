/*-
 * #%L
 * HAPI FHIR JPA Server - Master Data Management
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.mdm.dao.MdmLinkDaoSvc;
import ca.uhn.fhir.jpa.model.entity.PartitionablePartitionId;
import ca.uhn.fhir.mdm.api.IMdmLink;
import ca.uhn.fhir.mdm.api.IMdmLinkCreateSvc;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.log.Logs;
import ca.uhn.fhir.mdm.model.MdmCreateOrUpdateParams;
import ca.uhn.fhir.mdm.model.mdmevents.MdmLinkEvent;
import ca.uhn.fhir.mdm.util.MdmPartitionHelper;
import ca.uhn.fhir.mdm.util.MdmResourceUtil;
import ca.uhn.fhir.mdm.util.MessageHelper;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class MdmLinkCreateSvcImpl implements IMdmLinkCreateSvc {
	private static final Logger ourLog = Logs.getMdmTroubleshootingLog();

	@Autowired
	FhirContext myFhirContext;

	@SuppressWarnings("rawtypes")
	@Autowired
	IIdHelperService myIdHelperService;

	@SuppressWarnings("rawtypes")
	@Autowired
	MdmLinkDaoSvc myMdmLinkDaoSvc;

	@Autowired
	IMdmSettings myMdmSettings;

	@Autowired
	MessageHelper myMessageHelper;

	@Autowired
	MdmPartitionHelper myMdmPartitionHelper;

	@Autowired
	private IMdmModelConverterSvc myModelConverter;

	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;

	@SuppressWarnings({"unchecked", "rawtypes"})
	@Transactional
	@Override
	public IAnyResource createLink(MdmCreateOrUpdateParams theParams) {
		IAnyResource sourceResource = theParams.getSourceResource();
		IAnyResource goldenResource = theParams.getGoldenResource();
		MdmMatchResultEnum matchResult = theParams.getMatchResult();

		String sourceType = myFhirContext.getResourceType(sourceResource);

		validateCreateLinkRequest(goldenResource, sourceResource, sourceType);

		IResourcePersistentId goldenResourceId = myIdHelperService.getPidOrThrowException(goldenResource);
		IResourcePersistentId targetId = myIdHelperService.getPidOrThrowException(sourceResource);

		// check if the golden resource and the source resource are in the same partition, throw error if not
		myMdmPartitionHelper.validateMdmResourcesPartitionMatches(goldenResource, sourceResource);

		Optional<? extends IMdmLink> optionalMdmLink =
				myMdmLinkDaoSvc.getLinkByGoldenResourcePidAndSourceResourcePid(goldenResourceId, targetId);
		if (optionalMdmLink.isPresent()) {
			throw new InvalidRequestException(
					Msg.code(753) + myMessageHelper.getMessageForPresentLink(goldenResource, sourceResource));
		}

		List<? extends IMdmLink> mdmLinks =
				myMdmLinkDaoSvc.getMdmLinksBySourcePidAndMatchResult(targetId, MdmMatchResultEnum.MATCH);
		if (mdmLinks.size() > 0 && matchResult == MdmMatchResultEnum.MATCH) {
			throw new InvalidRequestException(
					Msg.code(754) + myMessageHelper.getMessageForMultipleGoldenRecords(sourceResource));
		}

		IMdmLink mdmLink =
				myMdmLinkDaoSvc.getOrCreateMdmLinkByGoldenResourceAndSourceResource(goldenResource, sourceResource);
		mdmLink.setLinkSource(MdmLinkSourceEnum.MANUAL);
		mdmLink.setMdmSourceType(sourceType);
		if (matchResult == null) {
			mdmLink.setMatchResult(MdmMatchResultEnum.MATCH);
		} else {
			mdmLink.setMatchResult(matchResult);
		}
		// Add partition for the mdm link if it doesn't exist
		RequestPartitionId goldenResourcePartitionId =
				(RequestPartitionId) goldenResource.getUserData(Constants.RESOURCE_PARTITION_ID);
		if (goldenResourcePartitionId != null
				&& goldenResourcePartitionId.hasPartitionIds()
				&& goldenResourcePartitionId.getFirstPartitionIdOrNull() != null
				&& (mdmLink.getPartitionId() == null || mdmLink.getPartitionId().getPartitionId() == null)) {
			mdmLink.setPartitionId(new PartitionablePartitionId(
					goldenResourcePartitionId.getFirstPartitionIdOrNull(),
					goldenResourcePartitionId.getPartitionDate()));
		}
		ourLog.info("Manually creating a " + goldenResource.getIdElement().toVersionless() + " to "
				+ sourceResource.getIdElement().toVersionless() + " mdm link.");
		myMdmLinkDaoSvc.save(mdmLink);

		if (myInterceptorBroadcaster.hasHooks(Pointcut.MDM_POST_CREATE_LINK)) {
			// pointcut for MDM_POST_CREATE_LINK
			MdmLinkEvent event = new MdmLinkEvent();
			event.addMdmLink(myModelConverter.toJson(mdmLink));
			HookParams hookParams = new HookParams();
			hookParams.add(RequestDetails.class, theParams.getRequestDetails()).add(MdmLinkEvent.class, event);
			myInterceptorBroadcaster.callHooks(Pointcut.MDM_POST_CREATE_LINK, hookParams);
		}

		return goldenResource;
	}

	private void validateCreateLinkRequest(
			IAnyResource theGoldenRecord, IAnyResource theSourceResource, String theSourceType) {
		String goldenRecordType = myFhirContext.getResourceType(theGoldenRecord);

		if (!myMdmSettings.isSupportedMdmType(goldenRecordType)) {
			throw new InvalidRequestException(Msg.code(755)
					+ myMessageHelper.getMessageForUnsupportedFirstArgumentTypeInUpdate(goldenRecordType));
		}

		if (!myMdmSettings.isSupportedMdmType(theSourceType)) {
			throw new InvalidRequestException(
					Msg.code(756) + myMessageHelper.getMessageForUnsupportedSecondArgumentTypeInUpdate(theSourceType));
		}

		if (!Objects.equals(goldenRecordType, theSourceType)) {
			throw new InvalidRequestException(Msg.code(757)
					+ myMessageHelper.getMessageForArgumentTypeMismatchInUpdate(goldenRecordType, theSourceType));
		}

		if (!MdmResourceUtil.isMdmManaged(theGoldenRecord)) {
			throw new InvalidRequestException(Msg.code(758) + myMessageHelper.getMessageForUnmanagedResource());
		}

		if (!MdmResourceUtil.isMdmAllowed(theSourceResource)) {
			throw new InvalidRequestException(Msg.code(759) + myMessageHelper.getMessageForUnsupportedSourceResource());
		}
	}
}
