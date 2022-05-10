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
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.dao.index.IJpaIdHelperService;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.mdm.dao.MdmLinkDaoSvc;
import ca.uhn.fhir.jpa.mdm.util.MdmPartitionHelper;
import ca.uhn.fhir.jpa.model.entity.PartitionablePartitionId;
import ca.uhn.fhir.mdm.api.IMdmLinkCreateSvc;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.log.Logs;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.mdm.util.MdmResourceUtil;
import ca.uhn.fhir.mdm.util.MessageHelper;
import ca.uhn.fhir.rest.api.Constants;
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
	@Autowired
	IJpaIdHelperService myIdHelperService;
	@Autowired
	MdmLinkDaoSvc myMdmLinkDaoSvc;
	@Autowired
	IMdmSettings myMdmSettings;
	@Autowired
	MessageHelper myMessageHelper;
	@Autowired
	MdmPartitionHelper myMdmPartitionHelper;

	@Transactional
	@Override
	public IAnyResource createLink(IAnyResource theGoldenResource, IAnyResource theSourceResource, MdmMatchResultEnum theMatchResult, MdmTransactionContext theMdmContext) {
		String sourceType = myFhirContext.getResourceType(theSourceResource);

		validateCreateLinkRequest(theGoldenResource, theSourceResource, sourceType);

		Long goldenResourceId = myIdHelperService.getPidOrThrowException(theGoldenResource);
		Long targetId = myIdHelperService.getPidOrThrowException(theSourceResource);

		// check if the golden resource and the source resource are in the same partition, throw error if not
		myMdmPartitionHelper.validateResourcesInSamePartition(theGoldenResource, theSourceResource);

		Optional<MdmLink> optionalMdmLink = myMdmLinkDaoSvc.getLinkByGoldenResourcePidAndSourceResourcePid(goldenResourceId, targetId);
		if (optionalMdmLink.isPresent()) {
			throw new InvalidRequestException(Msg.code(753) + myMessageHelper.getMessageForPresentLink(theGoldenResource, theSourceResource));
		}

		List<MdmLink> mdmLinks = myMdmLinkDaoSvc.getMdmLinksBySourcePidAndMatchResult(targetId, MdmMatchResultEnum.MATCH);
		if (mdmLinks.size() > 0 && theMatchResult == MdmMatchResultEnum.MATCH) {
			throw new InvalidRequestException(Msg.code(754) + myMessageHelper.getMessageForMultipleGoldenRecords(theSourceResource));
		}

		MdmLink mdmLink = myMdmLinkDaoSvc.getOrCreateMdmLinkByGoldenResourcePidAndSourceResourcePid(goldenResourceId, targetId);
		mdmLink.setLinkSource(MdmLinkSourceEnum.MANUAL);
		if (theMatchResult == null) {
			mdmLink.setMatchResult(MdmMatchResultEnum.MATCH);
		} else {
			mdmLink.setMatchResult(theMatchResult);
		}
		// Add partition for the mdm link if it doesn't exist
		RequestPartitionId goldenResourcePartitionId = (RequestPartitionId) theGoldenResource.getUserData(Constants.RESOURCE_PARTITION_ID);
		if (goldenResourcePartitionId != null && goldenResourcePartitionId.hasPartitionIds() && goldenResourcePartitionId.getFirstPartitionIdOrNull() != null &&
			(mdmLink.getPartitionId() == null || mdmLink.getPartitionId().getPartitionId() == null)) {
			mdmLink.setPartitionId(new PartitionablePartitionId(goldenResourcePartitionId.getFirstPartitionIdOrNull(), goldenResourcePartitionId.getPartitionDate()));
		}
		ourLog.info("Manually creating a " + theGoldenResource.getIdElement().toVersionless() + " to " + theSourceResource.getIdElement().toVersionless() + " mdm link.");
		myMdmLinkDaoSvc.save(mdmLink);

		return theGoldenResource;
	}

	private void validateCreateLinkRequest(IAnyResource theGoldenRecord, IAnyResource theSourceResource, String theSourceType) {
		String goldenRecordType = myFhirContext.getResourceType(theGoldenRecord);

		if (!myMdmSettings.isSupportedMdmType(goldenRecordType)) {
			throw new InvalidRequestException(Msg.code(755) + myMessageHelper.getMessageForUnsupportedFirstArgumentTypeInUpdate(goldenRecordType));
		}

		if (!myMdmSettings.isSupportedMdmType(theSourceType)) {
			throw new InvalidRequestException(Msg.code(756) + myMessageHelper.getMessageForUnsupportedSecondArgumentTypeInUpdate(theSourceType));
		}

		if (!Objects.equals(goldenRecordType, theSourceType)) {
			throw new InvalidRequestException(Msg.code(757) + myMessageHelper.getMessageForArgumentTypeMismatchInUpdate(goldenRecordType, theSourceType));
		}

		if (!MdmResourceUtil.isMdmManaged(theGoldenRecord)) {
			throw new InvalidRequestException(Msg.code(758) + myMessageHelper.getMessageForUnmanagedResource());
		}

		if (!MdmResourceUtil.isMdmAllowed(theSourceResource)) {
			throw new InvalidRequestException(Msg.code(759) + myMessageHelper.getMessageForUnsupportedSourceResource());
		}
	}
}
