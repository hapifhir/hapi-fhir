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

import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.mdm.dao.MdmLinkDaoSvc;
import ca.uhn.fhir.mdm.api.IMdmLinkSvc;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchOutcome;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.log.Logs;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.mdm.util.GoldenResourceHelper;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * This class is in charge of managing MdmLinks between Golden Resources and target resources
 */
@Service
public class MdmLinkSvcImpl implements IMdmLinkSvc {
	private static final Logger ourLog = Logs.getMdmTroubleshootingLog();

	@Autowired
	private MdmResourceDaoSvc myMdmResourceDaoSvc;
	@Autowired
	private MdmLinkDaoSvc myMdmLinkDaoSvc;
	@Autowired
	private GoldenResourceHelper myGoldenResourceHelper;
	@Autowired
	private IdHelperService myIdHelperService;

	@Override
	@Transactional
	public void updateLink(IAnyResource thePerson, IAnyResource theTarget, MdmMatchOutcome theMatchOutcome, MdmLinkSourceEnum theLinkSource, MdmTransactionContext theMdmTransactionContext) {
		if (theMatchOutcome.isPossibleDuplicate() && personsLinkedAsNoMatch(thePerson, theTarget)) {
			log(theMdmTransactionContext, thePerson.getIdElement().toUnqualifiedVersionless() +
				" is linked as NO_MATCH with " +
				theTarget.getIdElement().toUnqualifiedVersionless() +
				" not linking as POSSIBLE_DUPLICATE.");
			return;
		}

		MdmMatchResultEnum matchResultEnum = theMatchOutcome.getMatchResultEnum();
		validateRequestIsLegal(thePerson, theTarget, matchResultEnum, theLinkSource);

		myMdmResourceDaoSvc.upsertSourceResource(thePerson, theMdmTransactionContext.getResourceType());
		createOrUpdateLinkEntity(thePerson, theTarget, theMatchOutcome, theLinkSource, theMdmTransactionContext);
	}

	private boolean personsLinkedAsNoMatch(IAnyResource thePerson, IAnyResource theTarget) {
		Long personId = myIdHelperService.getPidOrThrowException(thePerson);
		Long targetId = myIdHelperService.getPidOrThrowException(theTarget);
		// TODO perf collapse into one query
		return myMdmLinkDaoSvc.getMdmLinksByPersonPidTargetPidAndMatchResult(personId, targetId, MdmMatchResultEnum.NO_MATCH).isPresent() ||
			myMdmLinkDaoSvc.getMdmLinksByPersonPidTargetPidAndMatchResult(targetId, personId, MdmMatchResultEnum.NO_MATCH).isPresent();
	}

	@Override
	public void deleteLink(IAnyResource theSourceResource, IAnyResource theTargetResource, MdmTransactionContext theMdmTransactionContext) {

		Optional<MdmLink> optionalMdmLink = getMdmLinkForGoldenResourceTargetPair(theSourceResource, theTargetResource);
		if (optionalMdmLink.isPresent()) {
			MdmLink mdmLink = optionalMdmLink.get();
			log(theMdmTransactionContext, "Deleting MdmLink [" + theSourceResource.getIdElement().toVersionless() + " -> " + theTargetResource.getIdElement().toVersionless() + "] with result: " + mdmLink.getMatchResult());
			myMdmLinkDaoSvc.deleteLink(mdmLink);
		}
	}

	/**
	 * Helper function which runs various business rules about what types of requests are allowed.
	 */
	private void validateRequestIsLegal(IAnyResource theGoldenResource, IAnyResource theResource, MdmMatchResultEnum theMatchResult, MdmLinkSourceEnum theLinkSource) {
		Optional<MdmLink> oExistingLink = getMdmLinkForGoldenResourceTargetPair(theGoldenResource, theResource);
		if (oExistingLink.isPresent() && systemIsAttemptingToModifyManualLink(theLinkSource, oExistingLink.get())) {
			throw new InternalErrorException("MDM system is not allowed to modify links on manually created links");
		}

		if (systemIsAttemptingToAddNoMatch(theLinkSource, theMatchResult)) {
			throw new InternalErrorException("MDM system is not allowed to automatically NO_MATCH a resource");
		}
	}

	/**
	 * Helper function which detects when the MDM system is attempting to add a NO_MATCH link, which is not allowed.
	 */
	private boolean systemIsAttemptingToAddNoMatch(MdmLinkSourceEnum theLinkSource, MdmMatchResultEnum theMatchResult) {
		return theLinkSource == MdmLinkSourceEnum.AUTO && theMatchResult == MdmMatchResultEnum.NO_MATCH;
	}

	/**
	 * Helper function to let us catch when System MDM rules are attempting to override a manually defined link.
	 */
	private boolean systemIsAttemptingToModifyManualLink(MdmLinkSourceEnum theIncomingSource, MdmLink theExistingSource) {
		return theIncomingSource == MdmLinkSourceEnum.AUTO && theExistingSource.isManual();
	}

	private Optional<MdmLink> getMdmLinkForGoldenResourceTargetPair(IAnyResource thePerson, IAnyResource theCandidate) {
		if (thePerson.getIdElement().getIdPart() == null || theCandidate.getIdElement().getIdPart() == null) {
			return Optional.empty();
		} else {
			return myMdmLinkDaoSvc.getLinkBySourceResourcePidAndTargetResourcePid(
				myIdHelperService.getPidOrNull(thePerson),
				myIdHelperService.getPidOrNull(theCandidate)
			);
		}
	}

	private void createOrUpdateLinkEntity(IBaseResource theSourceResource, IBaseResource theTargetResource, MdmMatchOutcome theMatchOutcome, MdmLinkSourceEnum theLinkSource, MdmTransactionContext theMdmTransactionContext) {
		myMdmLinkDaoSvc.createOrUpdateLinkEntity(theSourceResource, theTargetResource, theMatchOutcome, theLinkSource, theMdmTransactionContext);
	}

	private void log(MdmTransactionContext theMdmTransactionContext, String theMessage) {
		theMdmTransactionContext.addTransactionLogMessage(theMessage);
		ourLog.debug(theMessage);
	}
}
