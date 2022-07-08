package ca.uhn.fhir.jpa.mdm.svc;

/*-
 * #%L
 * HAPI FHIR JPA Server - Master Data Management
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.mdm.dao.MdmLinkDaoSvc;
import ca.uhn.fhir.jpa.mdm.util.MdmPartitionHelper;
import ca.uhn.fhir.jpa.model.entity.PartitionablePartitionId;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.dao.index.IJpaIdHelperService;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.mdm.dao.MdmLinkDaoSvc;
import ca.uhn.fhir.mdm.api.IMdmLinkSvc;
import ca.uhn.fhir.mdm.api.IMdmLinkUpdaterSvc;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.api.IMdmSurvivorshipService;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.log.Logs;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.mdm.util.MdmResourceUtil;
import ca.uhn.fhir.mdm.util.MessageHelper;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

public class MdmLinkUpdaterSvcImpl implements IMdmLinkUpdaterSvc {

	private static final Logger ourLog = Logs.getMdmTroubleshootingLog();

	@Autowired
	FhirContext myFhirContext;
	@Autowired
	IJpaIdHelperService myIdHelperService;
	@Autowired
	MdmLinkDaoSvc myMdmLinkDaoSvc;
	@Autowired
	IMdmLinkSvc myMdmLinkSvc;
	@Autowired
	MdmResourceDaoSvc myMdmResourceDaoSvc;
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

	@Transactional
	@Override
	public IAnyResource updateLink(IAnyResource theGoldenResource, IAnyResource theSourceResource, MdmMatchResultEnum theMatchResult, MdmTransactionContext theMdmContext) {
		String sourceType = myFhirContext.getResourceType(theSourceResource);

		validateUpdateLinkRequest(theGoldenResource, theSourceResource, theMatchResult, sourceType);

		Long goldenResourceId = myIdHelperService.getPidOrThrowException(theGoldenResource);
		Long targetId = myIdHelperService.getPidOrThrowException(theSourceResource);

		// check if the golden resource and the source resource are in the same partition, throw error if not
		myMdmPartitionHelper.validateResourcesInSamePartition(theGoldenResource, theSourceResource);

		Optional<MdmLink> optionalMdmLink = myMdmLinkDaoSvc.getLinkByGoldenResourcePidAndSourceResourcePid(goldenResourceId, targetId);
		if (!optionalMdmLink.isPresent()) {
			throw new InvalidRequestException(Msg.code(738) + myMessageHelper.getMessageForNoLink(theGoldenResource, theSourceResource));
		}

		MdmLink mdmLink = optionalMdmLink.get();
		if (mdmLink.getMatchResult() == theMatchResult) {
			ourLog.warn("MDM Link for " + theGoldenResource.getIdElement().toVersionless() + ", " + theSourceResource.getIdElement().toVersionless() + " already has value " + theMatchResult + ".  Nothing to do.");
			return theGoldenResource;
		}

		ourLog.info("Manually updating MDM Link for " + theGoldenResource.getIdElement().toVersionless() + ", " + theSourceResource.getIdElement().toVersionless() + " from " + mdmLink.getMatchResult() + " to " + theMatchResult + ".");
		mdmLink.setMatchResult(theMatchResult);
		mdmLink.setLinkSource(MdmLinkSourceEnum.MANUAL);

		// Add partition for the mdm link if it doesn't exist
		RequestPartitionId goldenResourcePartitionId = (RequestPartitionId) theGoldenResource.getUserData(Constants.RESOURCE_PARTITION_ID);
		if (goldenResourcePartitionId != null && goldenResourcePartitionId.hasPartitionIds() && goldenResourcePartitionId.getFirstPartitionIdOrNull() != null &&
			(mdmLink.getPartitionId() == null || mdmLink.getPartitionId().getPartitionId() == null)) {
			mdmLink.setPartitionId(new PartitionablePartitionId(goldenResourcePartitionId.getFirstPartitionIdOrNull(), goldenResourcePartitionId.getPartitionDate()));
		}
		myMdmLinkDaoSvc.save(mdmLink);

		if (theMatchResult == MdmMatchResultEnum.MATCH) {
			// only apply survivorship rules in case of a match
			myMdmSurvivorshipService.applySurvivorshipRulesToGoldenResource(theSourceResource, theGoldenResource, theMdmContext);
		}

		myMdmResourceDaoSvc.upsertGoldenResource(theGoldenResource, theMdmContext.getResourceType());
		if (theMatchResult == MdmMatchResultEnum.NO_MATCH) {
			// Need to find a new Golden Resource to link this target to
			myMdmMatchLinkSvc.updateMdmLinksForMdmSource(theSourceResource, theMdmContext);
		}
		return theGoldenResource;
	}

	private void validateUpdateLinkRequest(IAnyResource theGoldenRecord, IAnyResource theSourceResource, MdmMatchResultEnum theMatchResult, String theSourceType) {
		String goldenRecordType = myFhirContext.getResourceType(theGoldenRecord);

		if (theMatchResult != MdmMatchResultEnum.NO_MATCH &&
			theMatchResult != MdmMatchResultEnum.MATCH) {
			throw new InvalidRequestException(Msg.code(739) + myMessageHelper.getMessageForUnsupportedMatchResult());
		}

		if (!myMdmSettings.isSupportedMdmType(goldenRecordType)) {
			throw new InvalidRequestException(Msg.code(740) + myMessageHelper.getMessageForUnsupportedFirstArgumentTypeInUpdate(goldenRecordType));
		}

		if (!myMdmSettings.isSupportedMdmType(theSourceType)) {
			throw new InvalidRequestException(Msg.code(741) + myMessageHelper.getMessageForUnsupportedSecondArgumentTypeInUpdate(theSourceType));
		}

		if (!Objects.equals(goldenRecordType, theSourceType)) {
			throw new InvalidRequestException(Msg.code(742) + myMessageHelper.getMessageForArgumentTypeMismatchInUpdate(goldenRecordType, theSourceType));
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
	public void notDuplicateGoldenResource(IAnyResource theGoldenResource, IAnyResource theTargetGoldenResource, MdmTransactionContext theMdmContext) {
		validateNotDuplicateGoldenResourceRequest(theGoldenResource, theTargetGoldenResource);

		Long goldenResourceId = myIdHelperService.getPidOrThrowException(theGoldenResource);
		Long targetId = myIdHelperService.getPidOrThrowException(theTargetGoldenResource);

		Optional<MdmLink> oMdmLink = myMdmLinkDaoSvc.getLinkByGoldenResourcePidAndSourceResourcePid(goldenResourceId, targetId);
		if (!oMdmLink.isPresent()) {
			throw new InvalidRequestException(Msg.code(745) + "No link exists between " + theGoldenResource.getIdElement().toVersionless() + " and " + theTargetGoldenResource.getIdElement().toVersionless());
		}

		MdmLink mdmLink = oMdmLink.get();
		if (!mdmLink.isPossibleDuplicate()) {
			throw new InvalidRequestException(Msg.code(746) + theGoldenResource.getIdElement().toVersionless() + " and " + theTargetGoldenResource.getIdElement().toVersionless() + " are not linked as POSSIBLE_DUPLICATE.");
		}
		mdmLink.setMatchResult(MdmMatchResultEnum.NO_MATCH);
		mdmLink.setLinkSource(MdmLinkSourceEnum.MANUAL);
		myMdmLinkDaoSvc.save(mdmLink);
	}

	/**
	 * Ensure that the two resources are of the same type and both are managed by HAPI-MDM
	 */
	private void validateNotDuplicateGoldenResourceRequest(IAnyResource theGoldenResource, IAnyResource theTarget) {
		String goldenResourceType = myFhirContext.getResourceType(theGoldenResource);
		String targetType = myFhirContext.getResourceType(theTarget);
		if (!goldenResourceType.equalsIgnoreCase(targetType)) {
			throw new InvalidRequestException(Msg.code(747) + "First argument to " + ProviderConstants.MDM_UPDATE_LINK + " must be the same resource type as the second argument.  Was " + goldenResourceType + "/" + targetType);
		}

		if (!MdmResourceUtil.isMdmManaged(theGoldenResource) || !MdmResourceUtil.isMdmManaged(theTarget)) {
			throw new InvalidRequestException(Msg.code(748) + "Only MDM Managed Golden Resources may be updated via this operation.  The resource provided is not tagged as managed by HAPI-MDM");
		}
	}
}
