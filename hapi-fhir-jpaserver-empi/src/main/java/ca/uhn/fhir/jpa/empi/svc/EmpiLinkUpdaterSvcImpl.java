package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.api.EmpiConstants;
import ca.uhn.fhir.empi.api.EmpiLinkSourceEnum;
import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.empi.api.IEmpiLinkSvc;
import ca.uhn.fhir.empi.api.IEmpiLinkUpdaterSvc;
import ca.uhn.fhir.empi.log.Logs;
import ca.uhn.fhir.empi.model.EmpiTransactionContext;
import ca.uhn.fhir.empi.util.EmpiUtil;
import ca.uhn.fhir.jpa.dao.EmpiLinkDaoSvc;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.entity.EmpiLink;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

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

	@Transactional
	@Override
	public IAnyResource updateLink(IAnyResource thePerson, IAnyResource theTarget, EmpiMatchResultEnum theMatchResult, EmpiTransactionContext theEmpiContext) {
		if (theMatchResult != EmpiMatchResultEnum.NO_MATCH &&
		theMatchResult != EmpiMatchResultEnum.MATCH) {
			throw new InvalidRequestException("Match Result may only be set to " + EmpiMatchResultEnum.NO_MATCH + " or " + EmpiMatchResultEnum.MATCH);
		}

		String personType = myFhirContext.getResourceType(thePerson);
		if (!"Person".equals(personType)) {
			throw new InvalidRequestException("First argument to updateLink must be a Person.  Was " + personType);
		}
		String targetType = myFhirContext.getResourceType(theTarget);
		if (!EmpiUtil.supportedTargetType(targetType)) {
			throw new InvalidRequestException("Second argument to updateLink must be a Patient or Practitioner.  Was " + targetType);
		}

		if (!EmpiUtil.isEmpiManaged(thePerson)) {
			throw new InvalidRequestException("Only EMPI Managed Person resources may be updated via this operation.  The Person resource provided is not tagged as managed by hapi-empi");
		}

		if (!EmpiUtil.isEmpiAccessible(theTarget)) {
			throw new InvalidRequestException("The target is marked with the " + EmpiConstants.CODE_NO_EMPI_MANAGED + " tag which means it may not be EMPI linked.");
		}

		Long personId = myIdHelperService.getPidOrThrowException(thePerson);
		Long targetId = myIdHelperService.getPidOrThrowException(theTarget);

		EmpiLink empiLink = myEmpiLinkDaoSvc.getLinkByPersonPidAndTargetPid(personId, targetId);
		if (empiLink.getMatchResult() == theMatchResult) {
			ourLog.warn("EMPI Link for " + thePerson.getIdElement().toVersionless() + ", " + theTarget.getIdElement().toVersionless() + " already has value " + theMatchResult + ".  Nothing to do.");
			return thePerson;
		}

		ourLog.info("Manually updating EMPI Link for " + thePerson.getIdElement().toVersionless() + ", " + theTarget.getIdElement().toVersionless() + " from " + empiLink.getMatchResult() + " to " + theMatchResult + ".");
		empiLink.setMatchResult(theMatchResult);
		empiLink.setLinkSource(EmpiLinkSourceEnum.MANUAL);
		myEmpiLinkDaoSvc.update(empiLink);
		myEmpiLinkSvc.syncEmpiLinksToPersonLinks(thePerson, theEmpiContext);
		return thePerson;
	}
}
