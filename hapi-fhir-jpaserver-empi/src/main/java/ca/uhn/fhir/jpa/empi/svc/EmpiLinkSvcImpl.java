package ca.uhn.fhir.jpa.empi.svc;

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

import ca.uhn.fhir.empi.api.EmpiLinkSourceEnum;
import ca.uhn.fhir.empi.api.EmpiMatchOutcome;
import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.empi.api.IEmpiLinkSvc;
import ca.uhn.fhir.empi.log.Logs;
import ca.uhn.fhir.empi.model.CanonicalIdentityAssuranceLevel;
import ca.uhn.fhir.empi.model.MdmTransactionContext;
import ca.uhn.fhir.empi.util.AssuranceLevelUtil;
import ca.uhn.fhir.empi.util.PersonHelper;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.empi.dao.EmpiLinkDaoSvc;
import ca.uhn.fhir.jpa.entity.EmpiLink;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseBackboneElement;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * This class is in charge of managing EmpiLinks between Persons and target resources
 */
@Service
public class EmpiLinkSvcImpl implements IEmpiLinkSvc {
	private static final Logger ourLog = Logs.getEmpiTroubleshootingLog();

	@Autowired
	private EmpiResourceDaoSvc myEmpiResourceDaoSvc;
	@Autowired
	private EmpiLinkDaoSvc myEmpiLinkDaoSvc;
	@Autowired
	private PersonHelper myPersonHelper;
	@Autowired
	private IdHelperService myIdHelperService;

	@Override
	@Transactional
	public void updateLink(IAnyResource thePerson, IAnyResource theTarget, EmpiMatchOutcome theMatchOutcome, EmpiLinkSourceEnum theLinkSource, MdmTransactionContext theMdmTransactionContext) {
		IIdType resourceId = theTarget.getIdElement().toUnqualifiedVersionless();

		if (theMatchOutcome.isPossibleDuplicate() && personsLinkedAsNoMatch(thePerson, theTarget)) {
			log(theMdmTransactionContext, thePerson.getIdElement().toUnqualifiedVersionless() +
				" is linked as NO_MATCH with " +
				theTarget.getIdElement().toUnqualifiedVersionless() +
				" not linking as POSSIBLE_DUPLICATE.");
			return;
		}
		EmpiMatchResultEnum matchResultEnum = theMatchOutcome.getMatchResultEnum();
		validateRequestIsLegal(thePerson, theTarget, matchResultEnum, theLinkSource);

//		switch (matchResultEnum) {
//			case MATCH:
//				myPersonHelper.addOrUpdateLink(thePerson, resourceId, AssuranceLevelUtil.getAssuranceLevel(matchResultEnum, theLinkSource), theEmpiTransactionContext);
//				myEmpiResourceDaoSvc.updatePerson(thePerson);
//				break;
//			case POSSIBLE_MATCH:
//				myPersonHelper.addOrUpdateLink(thePerson, resourceId, AssuranceLevelUtil.getAssuranceLevel(matchResultEnum, theLinkSource), theEmpiTransactionContext);
//				break;
//			case NO_MATCH:
//				myPersonHelper.removeLink(thePerson, resourceId, theEmpiTransactionContext);
//				break;
//			case POSSIBLE_DUPLICATE:
//				break;
//		}
		myEmpiResourceDaoSvc.upsertSourceResource(thePerson, theMdmTransactionContext.getResourceType());
		createOrUpdateLinkEntity(thePerson, theTarget, theMatchOutcome, theLinkSource, theMdmTransactionContext);
	}

	private boolean personsLinkedAsNoMatch(IAnyResource thePerson, IAnyResource theTarget) {
		Long personId = myIdHelperService.getPidOrThrowException(thePerson);
		Long targetId = myIdHelperService.getPidOrThrowException(theTarget);
		// TODO perf collapse into one query
		return myEmpiLinkDaoSvc.getEmpiLinksByPersonPidTargetPidAndMatchResult(personId, targetId, EmpiMatchResultEnum.NO_MATCH).isPresent() ||
			myEmpiLinkDaoSvc.getEmpiLinksByPersonPidTargetPidAndMatchResult(targetId, personId, EmpiMatchResultEnum.NO_MATCH).isPresent();
	}

//	@Override
//	@Transactional
//	public void syncEmpiLinksToPersonLinks(IAnyResource thePersonResource, EmpiTransactionContext theEmpiTransactionContext) {
//		// int origLinkCount = myPersonHelper.getLinkCount(thePersonResource);
//		int origLinkCount = myEmpiLinkDaoSvc.findEmpiMatchLinksBySource(thePersonResource).size();
//
//		List<EmpiLink> empiLinks = myEmpiLinkDaoSvc.findEmpiLinksBySourceResource(thePersonResource);
//
//		List<IBaseBackboneElement> newLinks = empiLinks.stream()
//			.filter(link -> link.isMatch() || link.isPossibleMatch() || link.isRedirect())
//			.map(this::personLinkFromEmpiLink)
//			.collect(Collectors.toList());
//		myPersonHelper.setLinks(thePersonResource, newLinks);
//		if (newLinks.size() > origLinkCount) {
//			log(theEmpiTransactionContext, thePersonResource.getIdElement().toVersionless() + " links increased from " + origLinkCount + " to " + newLinks.size());
//		} else if (newLinks.size() < origLinkCount) {
//			log(theEmpiTransactionContext, thePersonResource.getIdElement().toVersionless() + " links decreased from " + origLinkCount + " to " + newLinks.size());
//		}
//
//	}

	@Override
	public void deleteLink(IAnyResource theSourceResource, IAnyResource theTargetResource, MdmTransactionContext theMdmTransactionContext) {
		// myPersonHelper.removeLink(theExistingPerson, theResource.getIdElement(), theEmpiTransactionContext);
//		 myEmpiLinkDaoSvc.deleteEmpiLinks(theSourceResource, theTargetResource);

		Optional<EmpiLink> oEmpiLink = getEmpiLinkForPersonTargetPair(theSourceResource, theTargetResource);
		if (oEmpiLink.isPresent()) {
			EmpiLink empiLink = oEmpiLink.get();
			log(theMdmTransactionContext, "Deleting EmpiLink [" + theSourceResource.getIdElement().toVersionless() + " -> " + theTargetResource.getIdElement().toVersionless() + "] with result: " + empiLink.getMatchResult());
			myEmpiLinkDaoSvc.deleteLink(empiLink);
		}
	}

	private IBaseBackboneElement personLinkFromEmpiLink(EmpiLink empiLink) {
		IIdType resourceId = myIdHelperService.resourceIdFromPidOrThrowException(empiLink.getTargetPid());
		CanonicalIdentityAssuranceLevel assuranceLevel = AssuranceLevelUtil.getAssuranceLevel(empiLink.getMatchResult(), empiLink.getLinkSource());
		return myPersonHelper.newPersonLink(resourceId, assuranceLevel);
	}

	/**
	 * Helper function which runs various business rules about what types of requests are allowed.
	 */
	private void validateRequestIsLegal(IAnyResource thePerson, IAnyResource theResource, EmpiMatchResultEnum theMatchResult, EmpiLinkSourceEnum theLinkSource) {
		Optional<EmpiLink> oExistingLink = getEmpiLinkForPersonTargetPair(thePerson, theResource);
		if (oExistingLink.isPresent() && systemIsAttemptingToModifyManualLink(theLinkSource, oExistingLink.get())) {
			throw new InternalErrorException("EMPI system is not allowed to modify links on manually created links");
		}

		if (systemIsAttemptingToAddNoMatch(theLinkSource, theMatchResult)) {
			throw new InternalErrorException("EMPI system is not allowed to automatically NO_MATCH a resource");
		}
	}

	/**
	 * Helper function which detects when the EMPI system is attempting to add a NO_MATCH link, which is not allowed.
	 */
	private boolean systemIsAttemptingToAddNoMatch(EmpiLinkSourceEnum theLinkSource, EmpiMatchResultEnum theMatchResult) {
		return theLinkSource == EmpiLinkSourceEnum.AUTO && theMatchResult == EmpiMatchResultEnum.NO_MATCH;
	}

	/**
	 * Helper function to let us catch when System EMPI rules are attempting to override a manually defined link.
	 */
	private boolean systemIsAttemptingToModifyManualLink(EmpiLinkSourceEnum theIncomingSource, EmpiLink theExistingSource) {
		return theIncomingSource == EmpiLinkSourceEnum.AUTO && theExistingSource.isManual();
	}

	private Optional<EmpiLink> getEmpiLinkForPersonTargetPair(IAnyResource thePerson, IAnyResource theCandidate) {
		if (thePerson.getIdElement().getIdPart() == null || theCandidate.getIdElement().getIdPart() == null) {
			return Optional.empty();
		} else {
			return myEmpiLinkDaoSvc.getLinkBySourceResourcePidAndTargetResourcePid(
				myIdHelperService.getPidOrNull(thePerson),
				myIdHelperService.getPidOrNull(theCandidate)
			);
		}
	}

	private void createOrUpdateLinkEntity(IBaseResource theSourceResource, IBaseResource theTargetResource, EmpiMatchOutcome theMatchOutcome, EmpiLinkSourceEnum theLinkSource, MdmTransactionContext theMdmTransactionContext) {
		myEmpiLinkDaoSvc.createOrUpdateLinkEntity(theSourceResource, theTargetResource, theMatchOutcome, theLinkSource, theMdmTransactionContext);
	}

	private void log(MdmTransactionContext theMdmTransactionContext, String theMessage) {
		theMdmTransactionContext.addTransactionLogMessage(theMessage);
		ourLog.debug(theMessage);
	}
}
