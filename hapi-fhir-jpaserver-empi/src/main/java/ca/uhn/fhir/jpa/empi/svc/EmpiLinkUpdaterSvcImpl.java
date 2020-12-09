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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.api.EmpiConstants;
import ca.uhn.fhir.empi.api.EmpiLinkSourceEnum;
import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.empi.api.IEmpiLinkSvc;
import ca.uhn.fhir.empi.api.IEmpiLinkUpdaterSvc;
import ca.uhn.fhir.empi.log.Logs;
import ca.uhn.fhir.empi.model.EmpiTransactionContext;
import ca.uhn.fhir.empi.util.EmpiUtil;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.empi.dao.EmpiLinkDaoSvc;
import ca.uhn.fhir.jpa.entity.EmpiLink;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public class EmpiLinkUpdaterSvcImpl implements IEmpiLinkUpdaterSvc {
	private static final Logger ourLog = Logs.getEmpiTroubleshootingLog();

	@Autowired
	FhirContext myFhirContext;
	@Autowired
	IdHelperService myIdHelperService;
	@Autowired
	EmpiLinkDaoSvc myEmpiLinkDaoSvc;
	@Autowired
	IEmpiLinkSvc myEmpiLinkSvc;
	@Autowired
	EmpiResourceDaoSvc myEmpiResourceDaoSvc;
	@Autowired
	EmpiMatchLinkSvc myEmpiMatchLinkSvc;

	@Transactional
	@Override
	public IAnyResource updateLink(IAnyResource thePerson, IAnyResource theTarget, EmpiMatchResultEnum theMatchResult, EmpiTransactionContext theEmpiContext) {
		String targetType = myFhirContext.getResourceType(theTarget);

		validateUpdateLinkRequest(thePerson, theTarget, theMatchResult, targetType);

		Long personId = myIdHelperService.getPidOrThrowException(thePerson);
		Long targetId = myIdHelperService.getPidOrThrowException(theTarget);

		Optional<EmpiLink> oEmpiLink = myEmpiLinkDaoSvc.getLinkByPersonPidAndTargetPid(personId, targetId);
		if (!oEmpiLink.isPresent()) {
			throw new InvalidRequestException("No link exists between " + thePerson.getIdElement().toVersionless() + " and " + theTarget.getIdElement().toVersionless());
		}
		EmpiLink empiLink = oEmpiLink.get();
		if (empiLink.getMatchResult() == theMatchResult) {
			ourLog.warn("EMPI Link for " + thePerson.getIdElement().toVersionless() + ", " + theTarget.getIdElement().toVersionless() + " already has value " + theMatchResult + ".  Nothing to do.");
			return thePerson;
		}

		ourLog.info("Manually updating EMPI Link for " + thePerson.getIdElement().toVersionless() + ", " + theTarget.getIdElement().toVersionless() + " from " + empiLink.getMatchResult() + " to " + theMatchResult + ".");
		empiLink.setMatchResult(theMatchResult);
		empiLink.setLinkSource(EmpiLinkSourceEnum.MANUAL);
		myEmpiLinkDaoSvc.save(empiLink);
		myEmpiLinkSvc.syncEmpiLinksToPersonLinks(thePerson, theEmpiContext);
		myEmpiResourceDaoSvc.updatePerson(thePerson);
		if (theMatchResult == EmpiMatchResultEnum.NO_MATCH) {
			// Need to find a new Person to link this target to
			myEmpiMatchLinkSvc.updateEmpiLinksForEmpiTarget(theTarget, theEmpiContext);
		}
		return thePerson;
	}

	private void validateUpdateLinkRequest(IAnyResource thePerson, IAnyResource theTarget, EmpiMatchResultEnum theMatchResult, String theTargetType) {
		String personType = myFhirContext.getResourceType(thePerson);
		if (theMatchResult != EmpiMatchResultEnum.NO_MATCH &&
			theMatchResult != EmpiMatchResultEnum.MATCH) {
			throw new InvalidRequestException("Match Result may only be set to " + EmpiMatchResultEnum.NO_MATCH + " or " + EmpiMatchResultEnum.MATCH);
		}

		if (!"Person".equals(personType)) {
			throw new InvalidRequestException("First argument to " + ProviderConstants.EMPI_UPDATE_LINK + " must be a Person.  Was " + personType);
		}
		if (!EmpiUtil.supportedTargetType(theTargetType)) {
			throw new InvalidRequestException("Second argument to " + ProviderConstants.EMPI_UPDATE_LINK + " must be a Patient or Practitioner.  Was " + theTargetType);
		}

		if (!EmpiUtil.isEmpiManaged(thePerson)) {
			throw new InvalidRequestException("Only EMPI Managed Person resources may be updated via this operation.  The Person resource provided is not tagged as managed by hapi-empi");
		}

		if (!EmpiUtil.isEmpiAccessible(theTarget)) {
			throw new InvalidRequestException("The target is marked with the " + EmpiConstants.CODE_NO_EMPI_MANAGED + " tag which means it may not be EMPI linked.");
		}
	}

	@Transactional
	@Override
	public void notDuplicatePerson(IAnyResource thePerson, IAnyResource theTarget, EmpiTransactionContext theEmpiContext) {
		validateNotDuplicatePersonRequest(thePerson, theTarget);

		Long personId = myIdHelperService.getPidOrThrowException(thePerson);
		Long targetId = myIdHelperService.getPidOrThrowException(theTarget);

		Optional<EmpiLink> oEmpiLink = myEmpiLinkDaoSvc.getLinkByPersonPidAndTargetPid(personId, targetId);
		if (!oEmpiLink.isPresent()) {
			throw new InvalidRequestException("No link exists between " + thePerson.getIdElement().toVersionless() + " and " + theTarget.getIdElement().toVersionless());
		}

		EmpiLink empiLink = oEmpiLink.get();
		if (!empiLink.isPossibleDuplicate()) {
			throw new InvalidRequestException(thePerson.getIdElement().toVersionless() + " and " + theTarget.getIdElement().toVersionless() + " are not linked as POSSIBLE_DUPLICATE.");
		}
		empiLink.setMatchResult(EmpiMatchResultEnum.NO_MATCH);
		empiLink.setLinkSource(EmpiLinkSourceEnum.MANUAL);
		myEmpiLinkDaoSvc.save(empiLink);
	}

	private void validateNotDuplicatePersonRequest(IAnyResource thePerson, IAnyResource theTarget) {
		String personType = myFhirContext.getResourceType(thePerson);
		String targetType = myFhirContext.getResourceType(theTarget);
		if (!"Person".equals(personType)) {
			throw new InvalidRequestException("First argument to " + ProviderConstants.EMPI_UPDATE_LINK + " must be a Person.  Was " + personType);
		}
		if (!"Person".equals(targetType)) {
			throw new InvalidRequestException("Second argument to " + ProviderConstants.EMPI_UPDATE_LINK + " must be a Person .  Was " + targetType);
		}

		if (!EmpiUtil.isEmpiManaged(thePerson) || !EmpiUtil.isEmpiManaged(theTarget)) {
			throw new InvalidRequestException("Only EMPI Managed Person resources may be updated via this operation.  The Person resource provided is not tagged as managed by hapi-empi");
		}

	}
}
