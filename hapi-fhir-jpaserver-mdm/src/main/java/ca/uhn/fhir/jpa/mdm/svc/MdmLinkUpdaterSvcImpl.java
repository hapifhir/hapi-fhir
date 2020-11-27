package ca.uhn.fhir.jpa.mdm.svc;

/*-
 * #%L
 * HAPI FHIR JPA Server - Enterprise Master Patient Index
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.api.IMdmLinkSvc;
import ca.uhn.fhir.mdm.api.IMdmLinkUpdaterSvc;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.log.Logs;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.mdm.util.MdmUtil;
import ca.uhn.fhir.mdm.util.MessageHelper;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.mdm.dao.MdmLinkDaoSvc;
import ca.uhn.fhir.jpa.entity.MdmLink;
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
	IdHelperService myIdHelperService;
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

	@Transactional
	@Override
	public IAnyResource updateLink(IAnyResource theGoldenResource, IAnyResource theTarget, MdmMatchResultEnum theMatchResult, MdmTransactionContext theMdmContext) {
		String targetType = myFhirContext.getResourceType(theTarget);

		validateUpdateLinkRequest(theGoldenResource, theTarget, theMatchResult, targetType);

		Long goldenResourceId = myIdHelperService.getPidOrThrowException(theGoldenResource);
		Long targetId = myIdHelperService.getPidOrThrowException(theTarget);

		Optional<MdmLink> optionalMdmLink = myMdmLinkDaoSvc.getLinkBySourceResourcePidAndTargetResourcePid(goldenResourceId, targetId);
		if (!optionalMdmLink.isPresent()) {
			throw new InvalidRequestException(myMessageHelper.getMessageForNoLink(theGoldenResource, theTarget));
		}

		MdmLink mdmLink = optionalMdmLink.get();
		if (mdmLink.getMatchResult() == theMatchResult) {
			ourLog.warn("MDM Link for " + theGoldenResource.getIdElement().toVersionless() + ", " + theTarget.getIdElement().toVersionless() + " already has value " + theMatchResult + ".  Nothing to do.");
			return theGoldenResource;
		}

		ourLog.info("Manually updating MDM Link for " + theGoldenResource.getIdElement().toVersionless() + ", " + theTarget.getIdElement().toVersionless() + " from " + mdmLink.getMatchResult() + " to " + theMatchResult + ".");
		mdmLink.setMatchResult(theMatchResult);
		mdmLink.setLinkSource(MdmLinkSourceEnum.MANUAL);
		myMdmLinkDaoSvc.save(mdmLink);
		myMdmResourceDaoSvc.upsertSourceResource(theGoldenResource, theMdmContext.getResourceType());
		if (theMatchResult == MdmMatchResultEnum.NO_MATCH) {
			// Need to find a new Person to link this target to
			myMdmMatchLinkSvc.updateMdmLinksForMdmTarget(theTarget, theMdmContext);
		}
		return theGoldenResource;
	}

	private void validateUpdateLinkRequest(IAnyResource theGoldenRecord, IAnyResource theTarget, MdmMatchResultEnum theMatchResult, String theTargetType) {
		String goldenRecordType = myFhirContext.getResourceType(theGoldenRecord);

		if (theMatchResult != MdmMatchResultEnum.NO_MATCH &&
			theMatchResult != MdmMatchResultEnum.MATCH) {
			throw new InvalidRequestException(myMessageHelper.getMessageForUnsupportedMatchResult());
		}

		if (!myMdmSettings.isSupportedMdmType(goldenRecordType)) {
			throw new InvalidRequestException(myMessageHelper.getMessageForUnsupportedFirstArgumentTypeInUpdate(goldenRecordType));
		}

		if (!myMdmSettings.isSupportedMdmType(theTargetType)) {
			throw new InvalidRequestException(myMessageHelper.getMessageForUnsupportedSecondArgumentTypeInUpdate(theTargetType));
		}

		if (!Objects.equals(goldenRecordType, theTargetType)) {
			throw new InvalidRequestException(myMessageHelper.getMessageForArgumentTypeMismatchInUpdate(goldenRecordType, theTargetType));
		}

		if (!MdmUtil.isMdmManaged(theGoldenRecord)) {
			throw new InvalidRequestException(myMessageHelper.getMessageForUnmanagedResource());
		}

		if (!MdmUtil.isMdmAllowed(theTarget)) {
			throw new InvalidRequestException(myMessageHelper.getMessageForUnsupportedTarget());
		}
	}

	@Transactional
	@Override
	public void notDuplicatePerson(IAnyResource thePerson, IAnyResource theTarget, MdmTransactionContext theMdmContext) {
		validateNotDuplicatePersonRequest(thePerson, theTarget);

		Long personId = myIdHelperService.getPidOrThrowException(thePerson);
		Long targetId = myIdHelperService.getPidOrThrowException(theTarget);

		Optional<MdmLink> oMdmLink = myMdmLinkDaoSvc.getLinkBySourceResourcePidAndTargetResourcePid(personId, targetId);
		if (!oMdmLink.isPresent()) {
			throw new InvalidRequestException("No link exists between " + thePerson.getIdElement().toVersionless() + " and " + theTarget.getIdElement().toVersionless());
		}

		MdmLink mdmLink = oMdmLink.get();
		if (!mdmLink.isPossibleDuplicate()) {
			throw new InvalidRequestException(thePerson.getIdElement().toVersionless() + " and " + theTarget.getIdElement().toVersionless() + " are not linked as POSSIBLE_DUPLICATE.");
		}
		mdmLink.setMatchResult(MdmMatchResultEnum.NO_MATCH);
		mdmLink.setLinkSource(MdmLinkSourceEnum.MANUAL);
		myMdmLinkDaoSvc.save(mdmLink);
	}

	/**
	 * Ensure that the two resources are of the same type and both are managed by HAPI-MDM
	 */
	private void validateNotDuplicatePersonRequest(IAnyResource theGoldenResource, IAnyResource theTarget) {
		String goldenResourceType = myFhirContext.getResourceType(theGoldenResource);
		String targetType = myFhirContext.getResourceType(theTarget);
		if (!goldenResourceType.equalsIgnoreCase(targetType)) {
			throw new InvalidRequestException("First argument to " + ProviderConstants.MDM_UPDATE_LINK + " must be the same resource type as the second argument.  Was " + goldenResourceType + "/" + targetType);
		}

		if (!MdmUtil.isMdmManaged(theGoldenResource) || !MdmUtil.isMdmManaged(theTarget)) {
			throw new InvalidRequestException("Only MDM Managed Golden Resources may be updated via this operation.  The resource provided is not tagged as managed by hapi-mdm");
		}
	}
}
