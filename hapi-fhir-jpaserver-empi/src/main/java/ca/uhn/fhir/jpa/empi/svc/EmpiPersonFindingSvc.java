package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.rules.svc.EmpiResourceComparatorSvc;
import ca.uhn.fhir.jpa.api.MatchedTargetCandidate;
import ca.uhn.fhir.jpa.empi.dao.IEmpiLinkDao;
import ca.uhn.fhir.jpa.empi.entity.EmpiLink;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmpiPersonFindingSvc {

	@Autowired
	IEmpiLinkDao myEmpiLinkDao;
	@Autowired
	ResourceTableHelper myResourceTableHelper;
	@Autowired
	private EmpiCandidateSearchSvc myEmpiCandidateSearchSvc;
	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private EmpiResourceComparatorSvc myEmpiResourceComparatorSvc;


	public List<MatchedPersonCandidate> findPersonCandidates(IBaseResource theBaseResource) {
	 // 1. First, check link table for any entries where this baseresource is the target of a person. If found, return.
		EmpiLink empiLink = new EmpiLink().setTargetPid(myResourceTableHelper.getPidOrNull(theBaseResource));

		Example<EmpiLink> example = Example.of(empiLink);
		Optional<EmpiLink> oLink = myEmpiLinkDao.findOne(example);
		if (oLink.isPresent()) {
			return Collections.singletonList(new MatchedPersonCandidate(oLink.get().getPerson(), oLink.get()));
		}

		// 4. NExt, find Patient/Pract resource candidates, then perform matching to narrow them down. Then see if any of those
		// candidates point to any persons via either link table or person records.
		//OK, so we have not found any links in the EmpiLink table with us as a target. Next, let's find possible Patient/Practitioner
		//matches by following EMPI rules.
		Collection<IBaseResource> candidates = myEmpiCandidateSearchSvc.findCandidates(
			myFhirContext.getResourceDefinition(theBaseResource).getName(),
			theBaseResource
		);

		List<MatchedTargetCandidate> matchedCandidates = candidates.stream()
			.map(candidate -> new MatchedTargetCandidate(candidate, myEmpiResourceComparatorSvc.getMatchResult(theBaseResource, candidate)))
			.collect(Collectors.toList());



		return Collections.emptyList();
		// 2. Next, find Person resources that link to this resource. GGG there shouldnt be any.... as our previous query returned 0.
		// 3. Next, find Person resource candidates and perform matching to narrow them down.

		// 5. If there are still no candidates, create a Person and populate it with theBaseResource information.
	}

}
