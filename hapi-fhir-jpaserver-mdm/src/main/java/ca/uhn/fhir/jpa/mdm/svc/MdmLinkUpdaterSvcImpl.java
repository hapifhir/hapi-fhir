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
import ca.uhn.fhir.mdm.api.IMdmLinkUpdaterSvc;
import ca.uhn.fhir.mdm.api.IMdmResourceDaoSvc;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.api.IMdmSurvivorshipService;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.log.Logs;
import ca.uhn.fhir.mdm.model.MdmCreateOrUpdateParams;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.mdm.model.MdmUnduplicateGoldenResourceParams;
import ca.uhn.fhir.mdm.model.mdmevents.MdmLinkEvent;
import ca.uhn.fhir.mdm.util.MdmPartitionHelper;
import ca.uhn.fhir.mdm.util.MdmResourceUtil;
import ca.uhn.fhir.mdm.util.MessageHelper;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class MdmLinkUpdaterSvcImpl implements IMdmLinkUpdaterSvc {

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
	IMdmResourceDaoSvc myMdmResourceDaoSvc;

	@Autowired
	MdmMatchLinkSvc myMdmMatchLinkSvc;

	@Autowired
	IMdmSettings myMdmSettings;

	@Autowired
	MessageHelper myMessageHelper;

	@Autowired
	IMdmSurvivorshipService myMdmSurvivorshipService;

	@Autowired
	MdmPartitionHelper myMdmPartitionHelper;

	@Autowired
	IInterceptorBroadcaster myInterceptorBroadcaster;

	@Autowired
	private IMdmModelConverterSvc myModelConverter;

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Transactional
	@Override
	public IAnyResource updateLink(MdmCreateOrUpdateParams theParams) {
		IAnyResource sourceResource = theParams.getSourceResource();
		IAnyResource goldenResource = theParams.getGoldenResource();
		MdmTransactionContext mdmContext = theParams.getMdmContext();
		MdmMatchResultEnum matchResult = theParams.getMatchResult();

		String sourceType = myFhirContext.getResourceType(sourceResource);

		validateUpdateLinkRequest(goldenResource, sourceResource, matchResult, sourceType);

		IResourcePersistentId goldenResourceId = myIdHelperService.getPidOrThrowException(goldenResource);
		IResourcePersistentId sourceResourceId = myIdHelperService.getPidOrThrowException(sourceResource);

		// check if the golden resource and the source resource are in the same partition if cross partition mdm is not
		// allowed, throw error if not
		myMdmPartitionHelper.validateMdmResourcesPartitionMatches(goldenResource, sourceResource);

		Optional<? extends IMdmLink> optionalMdmLink =
				myMdmLinkDaoSvc.getLinkByGoldenResourcePidAndSourceResourcePid(goldenResourceId, sourceResourceId);
		if (optionalMdmLink.isEmpty()) {
			throw new InvalidRequestException(
					Msg.code(738) + myMessageHelper.getMessageForNoLink(goldenResource, sourceResource));
		}

		IMdmLink mdmLink = optionalMdmLink.get();

		validateNoMatchPresentWhenAcceptingPossibleMatch(sourceResource, goldenResourceId, matchResult);

		if (mdmLink.getMatchResult() == matchResult) {
			ourLog.warn("MDM Link for " + goldenResource.getIdElement().toVersionless() + ", "
					+ sourceResource.getIdElement().toVersionless() + " already has value " + matchResult
					+ ".  Nothing to do.");
			return goldenResource;
		}

		ourLog.info("Manually updating MDM Link for "
				+ goldenResource.getIdElement().toVersionless() + ", "
				+ sourceResource.getIdElement().toVersionless() + " from " + mdmLink.getMatchResult() + " to "
				+ matchResult + ".");
		mdmLink.setMatchResult(matchResult);
		mdmLink.setLinkSource(MdmLinkSourceEnum.MANUAL);

		// Add partition for the mdm link if it doesn't exist
		addPartitioninfoForLinkIfNecessary(goldenResource, mdmLink);
		myMdmLinkDaoSvc.save(mdmLink);

		if (matchResult == MdmMatchResultEnum.MATCH) {
			// only apply survivorship rules in case of a match
			myMdmSurvivorshipService.applySurvivorshipRulesToGoldenResource(sourceResource, goldenResource, mdmContext);
		}

		/**
		 * We use the versionless id
		 * because we call update on the goldenResource in 2 places:
		 * here and below where we rebuild goldenresources if we have set
		 * a link to NO_MATCH.
		 *
		 * This can be a problem when a source resource is deleted.
		 * then {@link MdmStorageInterceptor} will update all links
		 * connecting to any golden resource that was connected to the now deleted
		 * source resource to NO_MATCH before deleting orphaned golden resources.
		 */
		goldenResource.setId(goldenResource.getIdElement().toVersionless());
		myMdmResourceDaoSvc.upsertGoldenResource(goldenResource, mdmContext.getResourceType());
		if (matchResult == MdmMatchResultEnum.NO_MATCH) {
			/*
			 * link is broken. We need to do 2 things:
			 * * update links for the source resource (if no other golden resources exist, for instance)
			 * * rebuild the golden resource from scratch, using current survivorship rules
			 * 	and the current set of links
			 */
			List<?> links =
					myMdmLinkDaoSvc.getMdmLinksBySourcePidAndMatchResult(sourceResourceId, MdmMatchResultEnum.MATCH);
			if (links.isEmpty()) {
				// No more links to source; Find a new Golden Resource to link this target to
				myMdmMatchLinkSvc.updateMdmLinksForMdmSource(sourceResource, mdmContext);
			}

			// with the link broken, the golden resource has delta info from a resource
			// that is no longer matched to it; we need to remove this delta. But it's
			// easier to just rebuild the resource from scratch using survivorship rules/current links
			goldenResource =
					myMdmSurvivorshipService.rebuildGoldenResourceWithSurvivorshipRules(goldenResource, mdmContext);
		}

		if (myInterceptorBroadcaster.hasHooks(Pointcut.MDM_POST_UPDATE_LINK)) {
			// pointcut for MDM_POST_UPDATE_LINK
			MdmLinkEvent event = new MdmLinkEvent();
			event.addMdmLink(myModelConverter.toJson(mdmLink));
			HookParams hookParams = new HookParams();
			hookParams.add(RequestDetails.class, theParams.getRequestDetails()).add(MdmLinkEvent.class, event);
			myInterceptorBroadcaster.callHooks(Pointcut.MDM_POST_UPDATE_LINK, hookParams);
		}

		return goldenResource;
	}

	private static void addPartitioninfoForLinkIfNecessary(IAnyResource goldenResource, IMdmLink mdmLink) {
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
	}

	/**
	 * When updating POSSIBLE_MATCH link to a MATCH we need to validate that a MATCH to a different golden resource
	 * doesn't exist, because a resource mustn't be a MATCH to more than one golden resource
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	private void validateNoMatchPresentWhenAcceptingPossibleMatch(
			IAnyResource theSourceResource,
			IResourcePersistentId theGoldenResourceId,
			MdmMatchResultEnum theMatchResult) {

		// if theMatchResult != MATCH, we are not accepting POSSIBLE_MATCH so there is nothing to validate
		if (theMatchResult != MdmMatchResultEnum.MATCH) {
			return;
		}

		IResourcePersistentId sourceResourceId = myIdHelperService.getPidOrThrowException(theSourceResource);
		List<? extends IMdmLink> mdmLinks =
				myMdmLinkDaoSvc.getMdmLinksBySourcePidAndMatchResult(sourceResourceId, MdmMatchResultEnum.MATCH);

		// if a link for a different golden resource exists, throw an exception
		for (IMdmLink mdmLink : mdmLinks) {
			if (mdmLink.getGoldenResourcePersistenceId() != theGoldenResourceId) {
				IAnyResource existingGolden = myMdmResourceDaoSvc.readGoldenResourceByPid(
						mdmLink.getGoldenResourcePersistenceId(), mdmLink.getMdmSourceType());
				throw new InvalidRequestException(Msg.code(2218)
						+ myMessageHelper.getMessageForAlreadyAcceptedLink(existingGolden, theSourceResource));
			}
		}
	}

	private void validateUpdateLinkRequest(
			IAnyResource theGoldenRecord,
			IAnyResource theSourceResource,
			MdmMatchResultEnum theMatchResult,
			String theSourceType) {
		String goldenRecordType = myFhirContext.getResourceType(theGoldenRecord);

		if (theMatchResult != MdmMatchResultEnum.NO_MATCH && theMatchResult != MdmMatchResultEnum.MATCH) {
			throw new InvalidRequestException(Msg.code(739) + myMessageHelper.getMessageForUnsupportedMatchResult());
		}

		if (!myMdmSettings.isSupportedMdmType(goldenRecordType)) {
			throw new InvalidRequestException(Msg.code(740)
					+ myMessageHelper.getMessageForUnsupportedFirstArgumentTypeInUpdate(goldenRecordType));
		}

		if (!myMdmSettings.isSupportedMdmType(theSourceType)) {
			throw new InvalidRequestException(
					Msg.code(741) + myMessageHelper.getMessageForUnsupportedSecondArgumentTypeInUpdate(theSourceType));
		}

		if (!Objects.equals(goldenRecordType, theSourceType)) {
			throw new InvalidRequestException(Msg.code(742)
					+ myMessageHelper.getMessageForArgumentTypeMismatchInUpdate(goldenRecordType, theSourceType));
		}

		if (!MdmResourceUtil.isMdmManaged(theGoldenRecord)) {
			throw new InvalidRequestException(Msg.code(743) + myMessageHelper.getMessageForUnmanagedResource());
		}

		if (!MdmResourceUtil.isMdmAllowed(theSourceResource)) {
			throw new InvalidRequestException(Msg.code(744) + myMessageHelper.getMessageForUnsupportedSourceResource());
		}
	}

	@Transactional
	@Override
	@SuppressWarnings({"unchecked", "rawtypes"})
	public void unduplicateGoldenResource(MdmUnduplicateGoldenResourceParams theParams) {
		IAnyResource goldenResource = theParams.getGoldenResource();
		IAnyResource targetGoldenResource = theParams.getTargetGoldenResource();

		validateNotDuplicateGoldenResourceRequest(goldenResource, targetGoldenResource);

		IResourcePersistentId goldenResourceId = myIdHelperService.getPidOrThrowException(goldenResource);
		IResourcePersistentId targetId = myIdHelperService.getPidOrThrowException(targetGoldenResource);

		Optional<? extends IMdmLink> oMdmLink =
				myMdmLinkDaoSvc.getLinkByGoldenResourcePidAndSourceResourcePid(goldenResourceId, targetId);
		if (oMdmLink.isEmpty()) {
			throw new InvalidRequestException(Msg.code(745) + "No link exists between "
					+ goldenResource.getIdElement().toVersionless() + " and "
					+ targetGoldenResource.getIdElement().toVersionless());
		}

		IMdmLink mdmLink = oMdmLink.get();
		if (!mdmLink.isPossibleDuplicate()) {
			throw new InvalidRequestException(
					Msg.code(746) + goldenResource.getIdElement().toVersionless() + " and "
							+ targetGoldenResource.getIdElement().toVersionless()
							+ " are not linked as POSSIBLE_DUPLICATE.");
		}
		mdmLink.setMatchResult(MdmMatchResultEnum.NO_MATCH);
		mdmLink.setLinkSource(MdmLinkSourceEnum.MANUAL);
		IMdmLink retval = myMdmLinkDaoSvc.save(mdmLink);

		if (myInterceptorBroadcaster.hasHooks(Pointcut.MDM_POST_NOT_DUPLICATE)) {
			// MDM_POST_NOT_DUPLICATE hook
			MdmLinkEvent event = new MdmLinkEvent();
			event.addMdmLink(myModelConverter.toJson(retval));

			HookParams params = new HookParams();
			params.add(RequestDetails.class, theParams.getRequestDetails());
			params.add(MdmLinkEvent.class, event);
			myInterceptorBroadcaster.callHooks(Pointcut.MDM_POST_NOT_DUPLICATE, params);
		}
	}

	/**
	 * Ensure that the two resources are of the same type and both are managed by HAPI-MDM
	 */
	private void validateNotDuplicateGoldenResourceRequest(IAnyResource theGoldenResource, IAnyResource theTarget) {
		String goldenResourceType = myFhirContext.getResourceType(theGoldenResource);
		String targetType = myFhirContext.getResourceType(theTarget);
		if (!goldenResourceType.equalsIgnoreCase(targetType)) {
			throw new InvalidRequestException(Msg.code(747) + "First argument to " + ProviderConstants.MDM_UPDATE_LINK
					+ " must be the same resource type as the second argument.  Was " + goldenResourceType + "/"
					+ targetType);
		}

		if (!MdmResourceUtil.isMdmManaged(theGoldenResource) || !MdmResourceUtil.isMdmManaged(theTarget)) {
			throw new InvalidRequestException(
					Msg.code(748)
							+ "Only MDM Managed Golden Resources may be updated via this operation.  The resource provided is not tagged as managed by HAPI-MDM");
		}
	}
}
