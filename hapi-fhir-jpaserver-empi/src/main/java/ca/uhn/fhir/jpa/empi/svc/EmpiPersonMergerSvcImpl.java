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
import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.empi.api.IEmpiLinkSvc;
import ca.uhn.fhir.empi.api.IEmpiPersonMergerSvc;
import ca.uhn.fhir.empi.log.Logs;
import ca.uhn.fhir.empi.model.EmpiTransactionContext;
import ca.uhn.fhir.empi.util.PersonHelper;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.empi.dao.EmpiLinkDaoSvc;
import ca.uhn.fhir.jpa.entity.EmpiLink;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EmpiPersonMergerSvcImpl implements IEmpiPersonMergerSvc {
	private static final Logger ourLog = Logs.getEmpiTroubleshootingLog();

	@Autowired
	PersonHelper myPersonHelper;
	@Autowired
	EmpiLinkDaoSvc myEmpiLinkDaoSvc;
	@Autowired
	IEmpiLinkSvc myEmpiLinkSvc;
	@Autowired
	IdHelperService myIdHelperService;
	@Autowired
	EmpiResourceDaoSvc myEmpiResourceDaoSvc;

	@Override
	@Transactional
	public IAnyResource mergePersons(IAnyResource theFromPerson, IAnyResource theToPerson, EmpiTransactionContext theEmpiTransactionContext) {
		Long toPersonPid = myIdHelperService.getPidOrThrowException(theToPerson);

		myPersonHelper.mergePersonFields(theFromPerson, theToPerson);
		mergeLinks(theFromPerson, theToPerson, toPersonPid, theEmpiTransactionContext);
		refreshLinksAndUpdatePerson(theToPerson, theEmpiTransactionContext);

		Long fromPersonPid = myIdHelperService.getPidOrThrowException(theFromPerson);
		addMergeLink(fromPersonPid, toPersonPid);
		myPersonHelper.deactivatePerson(theFromPerson);

		refreshLinksAndUpdatePerson(theFromPerson, theEmpiTransactionContext);

		log(theEmpiTransactionContext, "Merged " + theFromPerson.getIdElement().toVersionless() + " into " + theToPerson.getIdElement().toVersionless());
		return theToPerson;
	}

	private void addMergeLink(Long theDeactivatedPersonPid, Long theActivePersonPid) {
		EmpiLink empiLink = myEmpiLinkDaoSvc.getOrCreateEmpiLinkByPersonPidAndTargetPid(theDeactivatedPersonPid, theActivePersonPid);
		empiLink
			.setEmpiTargetType("Person")
			.setMatchResult(EmpiMatchResultEnum.REDIRECT)
			.setLinkSource(EmpiLinkSourceEnum.MANUAL);
		myEmpiLinkDaoSvc.save(empiLink);
	}

	private void refreshLinksAndUpdatePerson(IAnyResource theToPerson, EmpiTransactionContext theEmpiTransactionContext) {
		myEmpiLinkSvc.syncEmpiLinksToPersonLinks(theToPerson, theEmpiTransactionContext);
		myEmpiResourceDaoSvc.updatePerson(theToPerson);
	}

	private void mergeLinks(IAnyResource theFromPerson, IAnyResource theToPerson, Long theToPersonPid, EmpiTransactionContext theEmpiTransactionContext) {
		List<EmpiLink> fromLinks = myEmpiLinkDaoSvc.findEmpiLinksByPerson(theFromPerson);
		List<EmpiLink> toLinks = myEmpiLinkDaoSvc.findEmpiLinksByPerson(theToPerson);

		// For each incomingLink, either ignore it, move it, or replace the original one

		for (EmpiLink fromLink : fromLinks) {
			Optional<EmpiLink> optionalToLink = findFirstLinkWithMatchingTarget(toLinks, fromLink);
			if (optionalToLink.isPresent()) {
				// The original links already contain this target, so move it over to the toPerson
				EmpiLink toLink = optionalToLink.get();
				if (fromLink.isManual()) {
					switch (toLink.getLinkSource()) {
						case AUTO:
							ourLog.trace("MANUAL overrides AUT0.  Deleting link {}", toLink);
							myEmpiLinkDaoSvc.deleteLink(toLink);
							break;
						case MANUAL:
							if (fromLink.getMatchResult() != toLink.getMatchResult()) {
								throw new InvalidRequestException("A MANUAL " + fromLink.getMatchResult() + " link may not be merged into a MANUAL " + toLink.getMatchResult() + " link for the same target");
							}
					}
				} else {
					// Ignore the case where the incoming link is AUTO
					continue;
				}
			}
			// The original links didn't contain this target, so move it over to the toPerson
			fromLink.setPersonPid(theToPersonPid);
			ourLog.trace("Saving link {}", fromLink);
			myEmpiLinkDaoSvc.save(fromLink);
		}
	}

	private Optional<EmpiLink> findFirstLinkWithMatchingTarget(List<EmpiLink> theEmpiLinks, EmpiLink theLinkWithTargetToMatch) {
		return theEmpiLinks.stream()
			.filter(empiLink -> empiLink.getTargetPid().equals(theLinkWithTargetToMatch.getTargetPid()))
			.findFirst();
	}

	private void log(EmpiTransactionContext theEmpiTransactionContext, String theMessage) {
		theEmpiTransactionContext.addTransactionLogMessage(theMessage);
		ourLog.debug(theMessage);
	}
}
