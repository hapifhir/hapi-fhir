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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.dao.index.IJpaIdHelperService;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.mdm.dao.MdmLinkDaoSvc;
import ca.uhn.fhir.mdm.api.IMdmLinkSvc;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchOutcome;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.log.Logs;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import javax.transaction.Transactional;
import java.util.Optional;

/**
 * This class is in charge of managing MdmLinks between Golden Resources and source resources
 */
@Service
public class MdmLinkSvcImpl implements IMdmLinkSvc {

	private static final Logger ourLog = Logs.getMdmTroubleshootingLog();

	@Autowired
	private MdmResourceDaoSvc myMdmResourceDaoSvc;
	@Autowired
	private MdmLinkDaoSvc myMdmLinkDaoSvc;
	@Autowired
	private IJpaIdHelperService myIdHelperService;

	@Override
	@Transactional
	public void updateLink(@Nonnull IAnyResource theGoldenResource, @Nonnull IAnyResource theSourceResource, MdmMatchOutcome theMatchOutcome, MdmLinkSourceEnum theLinkSource, MdmTransactionContext theMdmTransactionContext) {
		if (theMatchOutcome.isPossibleDuplicate() && goldenResourceLinkedAsNoMatch(theGoldenResource, theSourceResource)) {
			log(theMdmTransactionContext, theGoldenResource.getIdElement().toUnqualifiedVersionless() +
				" is linked as NO_MATCH with " +
				theSourceResource.getIdElement().toUnqualifiedVersionless() +
				" not linking as POSSIBLE_DUPLICATE.");
			return;
		}

		MdmMatchResultEnum matchResultEnum = theMatchOutcome.getMatchResultEnum();
		validateRequestIsLegal(theGoldenResource, theSourceResource, matchResultEnum, theLinkSource);

		myMdmResourceDaoSvc.upsertGoldenResource(theGoldenResource, theMdmTransactionContext.getResourceType());
		MdmLink link = createOrUpdateLinkEntity(theGoldenResource, theSourceResource, theMatchOutcome, theLinkSource, theMdmTransactionContext);
		theMdmTransactionContext.addMdmLink(link);
	}

	private boolean goldenResourceLinkedAsNoMatch(IAnyResource theGoldenResource, IAnyResource theSourceResource) {
		Long goldenResourceId = myIdHelperService.getPidOrThrowException(theGoldenResource);
		Long sourceId = myIdHelperService.getPidOrThrowException(theSourceResource);
		// TODO perf collapse into one query
		return myMdmLinkDaoSvc.getMdmLinksByGoldenResourcePidSourcePidAndMatchResult(goldenResourceId, sourceId, MdmMatchResultEnum.NO_MATCH).isPresent() ||
			myMdmLinkDaoSvc.getMdmLinksByGoldenResourcePidSourcePidAndMatchResult(sourceId, goldenResourceId, MdmMatchResultEnum.NO_MATCH).isPresent();
	}

	@Override
	public void deleteLink(IAnyResource theGoldenResource, IAnyResource theSourceResource, MdmTransactionContext theMdmTransactionContext) {
		if (theGoldenResource == null) {
			return;
		}
		Optional<MdmLink> optionalMdmLink = getMdmLinkForGoldenResourceSourceResourcePair(theGoldenResource, theSourceResource);
		if (optionalMdmLink.isPresent()) {
			MdmLink mdmLink = optionalMdmLink.get();
			log(theMdmTransactionContext, "Deleting MdmLink [" + theGoldenResource.getIdElement().toVersionless() + " -> " + theSourceResource.getIdElement().toVersionless() + "] with result: " + mdmLink.getMatchResult());
			myMdmLinkDaoSvc.deleteLink(mdmLink);
			theMdmTransactionContext.addMdmLink(mdmLink);
		}
	}

	/**
	 * Helper function which runs various business rules about what types of requests are allowed.
	 */
	private void validateRequestIsLegal(IAnyResource theGoldenResource, IAnyResource theResource, MdmMatchResultEnum theMatchResult, MdmLinkSourceEnum theLinkSource) {
		Optional<MdmLink> oExistingLink = getMdmLinkForGoldenResourceSourceResourcePair(theGoldenResource, theResource);
		if (oExistingLink.isPresent() && systemIsAttemptingToModifyManualLink(theLinkSource, oExistingLink.get())) {
			throw new InternalErrorException(Msg.code(760) + "MDM system is not allowed to modify links on manually created links");
		}

		if (systemIsAttemptingToAddNoMatch(theLinkSource, theMatchResult)) {
			throw new InternalErrorException(Msg.code(761) + "MDM system is not allowed to automatically NO_MATCH a resource");
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

	private Optional<MdmLink> getMdmLinkForGoldenResourceSourceResourcePair(@Nonnull IAnyResource theGoldenResource, @Nonnull IAnyResource theCandidate) {
		if (theGoldenResource.getIdElement().getIdPart() == null || theCandidate.getIdElement().getIdPart() == null) {
			return Optional.empty();
		} else {
			return myMdmLinkDaoSvc.getLinkByGoldenResourceAndSourceResource(theGoldenResource, theCandidate);
		}
	}

	private MdmLink createOrUpdateLinkEntity(IBaseResource theGoldenResource, IBaseResource theSourceResource, MdmMatchOutcome theMatchOutcome, MdmLinkSourceEnum theLinkSource, MdmTransactionContext theMdmTransactionContext) {
		return myMdmLinkDaoSvc.createOrUpdateLinkEntity(theGoldenResource, theSourceResource, theMatchOutcome, theLinkSource, theMdmTransactionContext);
	}

	private void log(MdmTransactionContext theMdmTransactionContext, String theMessage) {
		theMdmTransactionContext.addTransactionLogMessage(theMessage);
		ourLog.debug(theMessage);
	}
}
