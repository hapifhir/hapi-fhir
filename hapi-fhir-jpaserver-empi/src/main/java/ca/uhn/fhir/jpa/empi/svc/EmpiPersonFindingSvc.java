package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.rules.svc.EmpiResourceComparatorSvc;
import ca.uhn.fhir.jpa.api.EmpiMatchResultEnum;
import ca.uhn.fhir.jpa.api.IEmpiLinkSvc;
import ca.uhn.fhir.jpa.api.MatchedTargetCandidate;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.empi.dao.IEmpiLinkDao;
import ca.uhn.fhir.jpa.empi.entity.EmpiLink;
import ca.uhn.fhir.jpa.model.cross.ResourcePersistentId;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Person;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
	@Autowired
	private EmpiLinkDaoSvc myEmpiLinkDaoSvc;
	@Autowired
	private EmpiResourceDaoSvc myEmpiResourceDaoSvc;
	@Autowired
	private IFhirResourceDao<Person> myPersonDao;


	public List<MatchedPersonCandidate> findPersonCandidates(IBaseResource theBaseResource) {
	 // 1. First, check link table for any entries where this baseresource is the target of a person. If found, return.
		Optional<EmpiLink> oLink = findEmpiLinkByTargetId(theBaseResource);
		if (oLink.isPresent()) {
			ResourcePersistentId pid = new ResourcePersistentId(oLink.get().getPersonPid());
			return Collections.singletonList(new MatchedPersonCandidate(pid, oLink.get()));
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


		//Convert all possible match targets to their equivalent Persons by looking up in the EmpiLink table.
		List<MatchedPersonCandidate> matchedPersons = matchedCandidates.stream()
			.filter(mc -> mc.getMatchResult().equals(EmpiMatchResultEnum.MATCH))
			.map(MatchedTargetCandidate::getCandidate)
			.map(candidate -> myEmpiLinkDaoSvc.getLinkByTargetResourceId(myResourceTableHelper.getPidOrNull(candidate)))
			.map(link -> new MatchedPersonCandidate(getResourcePersistentId(link.getPersonPid()), link))
			.collect(Collectors.toList());

		if (!matchedPersons.isEmpty()) {
			return matchedPersons;
		}


		return Collections.emptyList();
		// 2. Next, find Person resources that link to this resource. GGG there shouldnt be any.... as our previous query returned 0.
		// 3. Next, find Person resource candidates and perform matching to narrow them down.

		// 5. If there are still no candidates, create a Person and populate it with theBaseResource information.
	}

	private ResourcePersistentId getResourcePersistentId(Long thePersonPid) {
		return new ResourcePersistentId(thePersonPid);
	}

	@NotNull
	public Optional<EmpiLink> findEmpiLinkByTargetId(IBaseResource theBaseResource) {
		EmpiLink empiLink = new EmpiLink().setTargetPid(myResourceTableHelper.getPidOrNull(theBaseResource));

		Example<EmpiLink> example = Example.of(empiLink);
		return myEmpiLinkDao.findOne(example);
	}

}
