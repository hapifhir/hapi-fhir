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
		Optional<List<MatchedPersonCandidate>> matchedPersonCandidates;
			matchedPersonCandidates= attemptToFindPersonCandidateFromEmpiLinkTable(theBaseResource);

		if (matchedPersonCandidates.isPresent()) {
			return matchedPersonCandidates.get();
		}

		// 2. Next, find Person resources that link to this resource. GGG there shouldnt be any.... as our previous query returned 0.
		// 3. Next, try to find Persons that are similar to our incoming resource based on metrics.
		//FIXME EMPI QUESTION this is what I was going to do for fetching candidate Persons, but I can't search across resources.
		matchedPersonCandidates =  attemptToFindPersonCandidateFromSimilarPersons(theBaseResource);
		if (matchedPersonCandidates.isPresent()) {
			return matchedPersonCandidates.get();
		}

		matchedPersonCandidates =  attemptToFindPersonCandidateFromSimilarTargetResource(theBaseResource);
		if (matchedPersonCandidates.isPresent()) {
			return matchedPersonCandidates.get();
		}

		return Collections.emptyList();
	}

	/**
	 * Attempt to find a currently matching Person, based on the presence of an {@link EmpiLink} entity.
	 *
	 * @param theBaseResource the {@link IBaseResource} which we want to find candidate Persons for.
	 * @return an Optional list of {@link MatchedPersonCandidate} indicating matches.
	 */
	private Optional<List<MatchedPersonCandidate>> attemptToFindPersonCandidateFromEmpiLinkTable(IBaseResource theBaseResource) {
		Optional<EmpiLink> oLink = findEmpiLinkByTargetId(theBaseResource);
		if (oLink.isPresent()) {
			ResourcePersistentId pid = new ResourcePersistentId(oLink.get().getPersonPid());
			return Optional.of(Collections.singletonList(new MatchedPersonCandidate(pid, oLink.get())));
		} else {
			return Optional.empty();
		}
	}

	/**
	 * Attempt to find matching persons by performing EMPI matching between the incoming resource (Patient/Practitioner)
	 * and the attributes of existing Persons.
	 *
	 * FIXME EMPI QUESTION Having trouble working out how to query across the resource boundary with our current EmpiRules setup.
	 *
	 * @param theBaseResource the {@link IBaseResource} which we want to find candidate Persons for.
	 * @return an Optional list of {@link MatchedPersonCandidate} indicating matches.
	 */
	private Optional<List<MatchedPersonCandidate>> attemptToFindPersonCandidateFromSimilarPersons(IBaseResource theBaseResource) {
		return Optional.empty();
		//Collection<IBaseResource> personCandidates = myEmpiCandidateSearchSvc.findCandidates("Person", theBaseResource);
		//List<EmpiMatchResultEnum> collect = personCandidates.stream()
	  //	.map(pc -> myEmpiResourceComparatorSvc.getMatchResult(theBaseResource, pc))
	  //		.collect(Collectors.toList());
	}

	/**
	 * Attempt to find matching Persons by resolving them from similar Matching target resources, where target resource
	 * can be either Patient or Practitioner. Runs EMPI logic over the existing Patient/Practitioners, then finds their
	 * entries in the EmpiLink table, and returns all the matches found therein.
	 *
	 * @param theBaseResource the {@link IBaseResource} which we want to find candidate Persons for.
	 * @return an Optional list of {@link MatchedPersonCandidate} indicating matches.
	 */
	private Optional<List<MatchedPersonCandidate>> attemptToFindPersonCandidateFromSimilarTargetResource(IBaseResource theBaseResource) {

		//OK, so we have not found any links in the EmpiLink table with us as a target. Next, let's find possible Patient/Practitioner
		//matches by following EMPI rules.
		Collection<IBaseResource> targetCandidates = myEmpiCandidateSearchSvc.findCandidates(
			myFhirContext.getResourceDefinition(theBaseResource).getName(),
			theBaseResource
		);

		List<MatchedTargetCandidate> matchedCandidates = targetCandidates.stream()
			.map(candidate -> new MatchedTargetCandidate(candidate, myEmpiResourceComparatorSvc.getMatchResult(theBaseResource, candidate)))
			.collect(Collectors.toList());


		//Convert all possible match targets to their equivalent Persons by looking up in the EmpiLink table.
		List<MatchedPersonCandidate> matchedPersons = matchedCandidates.stream()
			.filter(mc -> mc.getMatchResult().equals(EmpiMatchResultEnum.MATCH))
			.map(MatchedTargetCandidate::getCandidate)
			.map(candidate -> myEmpiLinkDaoSvc.getLinkByTargetResourceId(myResourceTableHelper.getPidOrNull(candidate)))
			.map(link -> new MatchedPersonCandidate(getResourcePersistentId(link.getPersonPid()), link))
			.collect(Collectors.toList());
		if (matchedPersons.isEmpty()) {
			return Optional.empty();
		} else {
			return Optional.of(matchedPersons);
		}

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
