package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.empi.rules.svc.EmpiResourceComparatorSvc;
import ca.uhn.fhir.jpa.api.EmpiLinkSourceEnum;
import ca.uhn.fhir.jpa.api.IEmpiLinkSvc;
import ca.uhn.fhir.jpa.api.MatchedCandidate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmpiMatchSvc {
	@Autowired
	private EmpiCandidateSearchSvc myEmpiCandidateSearchSvc;
	@Autowired
	private EmpiResourceComparatorSvc myEmpiResourceComparatorSvc;
	@Autowired
	private IEmpiLinkSvc myEmpiLinkSvc;

	public void updatePatientLinks(IBaseResource theResource) {
		Collection<IBaseResource> candidates = myEmpiCandidateSearchSvc.findCandidates("Patient", theResource);

		List<MatchedCandidate> matchedCandidates = candidates.stream()
			.map(candidate -> new MatchedCandidate(candidate, myEmpiResourceComparatorSvc.getMatchResult(theResource, candidate)))
			.collect(Collectors.toList());
		myEmpiLinkSvc.updateLinks(theResource, matchedCandidates, EmpiLinkSourceEnum.AUTO);

	}
}
