package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.empi.api.IEmpiMatchFinderSvc;
import ca.uhn.fhir.empi.api.MatchedTargetCandidate;
import ca.uhn.fhir.empi.rules.svc.EmpiResourceComparatorSvc;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmpiMatchFinderSvc implements IEmpiMatchFinderSvc {
	@Autowired
	private EmpiCandidateSearchSvc myEmpiCandidateSearchSvc;
	@Autowired
	private EmpiResourceComparatorSvc myEmpiResourceComparatorSvc;

	@Override
	@Nonnull
	public List<MatchedTargetCandidate> getMatchedTargetCandidates(String theResourceType, IBaseResource theBaseResource) {
		Collection<IBaseResource> targetCandidates = myEmpiCandidateSearchSvc.findCandidates(theResourceType, theBaseResource);

		return targetCandidates.stream()
			.map(candidate -> new MatchedTargetCandidate(candidate, myEmpiResourceComparatorSvc.getMatchResult(theBaseResource, candidate)))
			.collect(Collectors.toList());
	}

	@Override
	@Nonnull
	public Collection<IBaseResource> findMatches(String theResourceType, IBaseResource theResource) {
		List<MatchedTargetCandidate> targetCandidates = getMatchedTargetCandidates(theResourceType, theResource);
		return targetCandidates.stream()
			.filter(candidate -> candidate.getMatchResult() == EmpiMatchResultEnum.MATCH)
			.map(MatchedTargetCandidate::getCandidate)
			.collect(Collectors.toList());
	}
}
