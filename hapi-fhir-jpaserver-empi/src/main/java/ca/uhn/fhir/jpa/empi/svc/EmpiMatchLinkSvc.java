package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.empi.api.EmpiLinkSourceEnum;
import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.empi.api.IEmpiLinkSvc;
import ca.uhn.fhir.empi.util.PersonHelper;
import ca.uhn.fhir.jpa.empi.util.EmpiUtil;
import ca.uhn.fhir.jpa.model.cross.ResourcePersistentId;
import com.mchange.util.impl.StringEnumerationHelperBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Lazy
@Service
public class EmpiMatchLinkSvc {
	@Autowired
	private EmpiResourceDaoSvc myEmpiResourceDaoSvc;
	@Autowired
	private IEmpiLinkSvc myEmpiLinkSvc;
	@Autowired
	private EmpiPersonFindingSvc myEmpiPersonFindingSvc;
	@Autowired
	private PersonHelper myPersonHelper;

	public void updateEmpiLinksForPatient(IBaseResource theResource) {
		if (EmpiUtil.isManagedByEmpi(theResource)) {
			doEmpiUpdate(theResource);
		}
	}


	private void doEmpiUpdate(IBaseResource theResource) {
		List<MatchedPersonCandidate> personCandidates = myEmpiPersonFindingSvc.findPersonCandidates(theResource);

		//0 candidates, in which case you should create a person
		if (personCandidates.isEmpty()) {
			IBaseResource newPerson = myPersonHelper.createPersonFromPatient(theResource);
			myEmpiLinkSvc.updateLink(newPerson, theResource, EmpiMatchResultEnum.MATCH, EmpiLinkSourceEnum.AUTO);
		//1 candidate, in which case you should use it
		} else if (personCandidates.size() == 1) {
			MatchedPersonCandidate matchedPersonCandidate = personCandidates.get(0);
			ResourcePersistentId personPid = matchedPersonCandidate.getCandidatePersonPid();
			IBaseResource person = myEmpiResourceDaoSvc.readPersonByPid(personPid);
			//FIXME EMPI QUESTION this is gross, can we pair to fix it??
			if (isPotentialDuplicate(theResource, person)) {
				IBaseResource newPerson = myPersonHelper.createPersonFromPatient(theResource);
				myEmpiLinkSvc.updateLink(newPerson, theResource, EmpiMatchResultEnum.MATCH, EmpiLinkSourceEnum.AUTO);
				myEmpiLinkSvc.updateLink(newPerson, person, EmpiMatchResultEnum.POSSIBLE_DUPLICATE, EmpiLinkSourceEnum.AUTO);
			} else {
				handleEidOverwrite(person, theResource);
				myEmpiLinkSvc.updateLink(person, theResource, matchedPersonCandidate.getEmpiLink().getMatchResult(), EmpiLinkSourceEnum.AUTO);
			}
		//multiple candidates, in which case they should all be tagged as POSSIBLE_MATCH. If one is already tagged as MATCH
		} else {

		}
	}

	/**
	 * An incoming resource is a potential duplicate if it matches a Patient that has a Person with an official EID, but
	 * the incoming resource also has an EID.
	 *
	 * @param theExistingPerson
	 * @param theComparingPerson
	 * @return
	 */
	private boolean isPotentialDuplicate(IBaseResource theExistingPerson, IBaseResource theComparingPerson) {
		PersonHelper.SystemAgnosticIdentifier firstEid = myPersonHelper.getEidFromResource(theExistingPerson);
		PersonHelper.SystemAgnosticIdentifier secondEid = myPersonHelper.getEidFromResource(theComparingPerson);
		return firstEid != null && firstEid.getUse().equals("official") && secondEid != null;
	}

	private void handleEidOverwrite(IBaseResource thePerson, IBaseResource theResource) {
		String eidFromResource = myPersonHelper.readEIDFromResource(theResource);
		if (eidFromResource != null)  {
			myPersonHelper.updatePersonFromPatient(thePerson, theResource);
		}
	}
}
