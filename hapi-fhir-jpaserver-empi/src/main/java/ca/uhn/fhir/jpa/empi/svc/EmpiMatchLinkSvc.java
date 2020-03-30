package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.rules.svc.EmpiResourceComparatorSvc;
import ca.uhn.fhir.jpa.api.EmpiLinkSourceEnum;
import ca.uhn.fhir.jpa.api.EmpiMatchResultEnum;
import ca.uhn.fhir.jpa.api.IEmpiLinkSvc;
import ca.uhn.fhir.jpa.api.MatchedTargetCandidate;
import ca.uhn.fhir.jpa.empi.util.PersonUtil;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmpiMatchLinkSvc {
	@Autowired
	private IEmpiResourceDaoSvc myEmpiResourceDaoSvc;
	@Autowired
	private EmpiCandidateSearchSvc myEmpiCandidateSearchSvc;
	@Autowired
	private EmpiResourceComparatorSvc myEmpiResourceComparatorSvc;
	@Autowired
	private IEmpiLinkSvc myEmpiLinkSvc;
	@Autowired
	private EmpiPersonFindingSvc myEmpiPersonFindingSvc;
	@Autowired
	private PersonUtil myPersonUtil;


	public void updateEmpiLinksForPatient(IBaseResource theResource) {

		//This will either return:
		//0 candidates, in which case you should create a person
		//1 candidate, in which case you should use it
		//multiple candidates, in which case they should all be tagged as POSSIBLE_MATCH. If one is already tagged as MATCH
		List<MatchedPersonCandidate> personCandidates = myEmpiPersonFindingSvc.findPersonCandidates(theResource);
		if (personCandidates.isEmpty()) {
			IBaseResource newPerson = myPersonUtil.createPersonFromPatient(theResource);
			myEmpiLinkSvc.updateLink(newPerson, theResource, EmpiMatchResultEnum.MATCH, EmpiLinkSourceEnum.AUTO);
		} else if (personCandidates.size() == 1) {
			MatchedPersonCandidate matchedPersonCandidate = personCandidates.get(0);
			IBaseResource person = myEmpiResourceDaoSvc.readPerson(matchedPersonCandidate.getCandidate().getIdDt());
			myEmpiLinkSvc.updateLink(person, theResource, matchedPersonCandidate.getEmpiLink().getMatchResult(), EmpiLinkSourceEnum.AUTO);
		}
		//myEmpiLinkSvc.updateLinks(theResource, matchedCandidates, EmpiLinkSourceEnum.AUTO);

	}
}
