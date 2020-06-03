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

import ca.uhn.fhir.empi.api.IEmpiLinkSvc;
import ca.uhn.fhir.empi.api.IEmpiPersonMergerSvc;
import ca.uhn.fhir.empi.log.Logs;
import ca.uhn.fhir.empi.model.EmpiTransactionContext;
import ca.uhn.fhir.empi.util.PersonHelper;
import ca.uhn.fhir.jpa.dao.EmpiLinkDaoSvc;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
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
		// TODO EMPI replace this with a post containing the manually merged fields
		myPersonHelper.mergePersonFields(theFromPerson, theToPerson);
		mergeLinks(theFromPerson, theToPerson, theEmpiTransactionContext);
		myEmpiResourceDaoSvc.updatePerson(theToPerson);
		log(theEmpiTransactionContext, "Merged " + theFromPerson.getIdElement().toVersionless() + " into " + theToPerson.getIdElement().toVersionless());

		myPersonHelper.deactivatePerson(theFromPerson);
		myEmpiResourceDaoSvc.updatePerson(theFromPerson);
		log(theEmpiTransactionContext, "Deactivated " + theFromPerson.getIdElement().toVersionless());
		return theToPerson;
	}

	private void mergeLinks(IAnyResource theFromPerson, IAnyResource theToPerson, EmpiTransactionContext theEmpiTransactionContext) {
		long toPersonPid = myIdHelperService.getPidOrThrowException(theToPerson);
		List<EmpiLink> incomingLinks = myEmpiLinkDaoSvc.findEmpiLinksByPersonId(theFromPerson);
		List<EmpiLink> origLinks = myEmpiLinkDaoSvc.findEmpiLinksByPersonId(theToPerson);

		// For each incomingLink, either ignore it, move it, or replace the original one

		for (EmpiLink incomingLink : incomingLinks) {
			Optional<EmpiLink> optionalOrigLink = findLinkWithMatchingTarget(origLinks, incomingLink);
			if (optionalOrigLink.isPresent()) {
				// The original links already contain this target, so move it over to the toPerson
				EmpiLink origLink = optionalOrigLink.get();
				if (incomingLink.isManual()) {
					switch (origLink.getLinkSource()) {
						case AUTO:
							ourLog.trace("MANUAL overrides AUT0.  Deleting link {}", origLink);
							myEmpiLinkDaoSvc.deleteLink(origLink);
							break;
						case MANUAL:
							if (incomingLink.getMatchResult() != origLink.getMatchResult()) {
								throw new InvalidRequestException("A MANUAL " + incomingLink.getMatchResult() + " link may not be merged into a MANUAL " + origLink.getMatchResult() + " link for the same target");
							}
					}
				} else {
					// Ignore the case where the incoming link is AUTO
					continue;
				}
			}
			// The original links didn't contain this target, so move it over to the toPerson
			incomingLink.setPersonPid(toPersonPid);
			ourLog.trace("Saving link {}", incomingLink);
			myEmpiLinkDaoSvc.save(incomingLink);
		}

		myEmpiLinkSvc.syncEmpiLinksToPersonLinks(theFromPerson, theEmpiTransactionContext);
		myEmpiLinkSvc.syncEmpiLinksToPersonLinks(theToPerson, theEmpiTransactionContext);
	}

	private Optional<EmpiLink> findLinkWithMatchingTarget(List<EmpiLink> theEmpiLinks, EmpiLink theLinkWithTargetToMatch) {
		return theEmpiLinks.stream()
			.filter(empiLink -> empiLink.getTargetPid().equals(theLinkWithTargetToMatch.getTargetPid()))
			.findFirst();
	}

	private void log(EmpiTransactionContext theEmpiTransactionContext, String theMessage) {
		theEmpiTransactionContext.addTransactionLogMessage(theMessage);
		ourLog.debug(theMessage);
	}
}
