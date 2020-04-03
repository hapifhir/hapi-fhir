package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.empi.api.IEmpiLinkSvc;
import ca.uhn.fhir.empi.api.MatchedTargetCandidate;
import ca.uhn.fhir.empi.rules.svc.EmpiResourceComparatorSvc;
import ca.uhn.fhir.jpa.empi.dao.IEmpiLinkDao;
import ca.uhn.fhir.jpa.empi.entity.EmpiLink;
import ca.uhn.fhir.jpa.empi.util.PersonUtil;
import ca.uhn.fhir.jpa.model.cross.ResourcePersistentId;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Lazy
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
	private IEmpiLinkSvc myEmpiLinkSvc;
	@Autowired
	private PersonUtil myPersonUtil;
	@Autowired
	private EmpiResourceDaoSvc myEmpiResourceDaoSvc;

	/**
	 * Given an incoming IBaseResource, limited to Patient/Practitioner, return a list of {@link MatchedPersonCandidate}
	 * indicating possible candidates for a matching Person. Uses several separate methods for finding candidates:
	 *
	 *  0. First, check the incoming Resource for an EID. If it is present, and we can find a Person with this EID, it automatically matches.
    *  1. First, check link table for any entries where this baseresource is the target of a person. If found, return.
	 *  2. If none are found, attempt to find Person Resources which link to this theBaseResource.
	 *  3. If none are found, attempt to find Persons similar to our incoming resource based on the EMPI rules and similarity metrics.
	 *  4. If none are found, attempt to find Persons that are linked to Patients/Practitioners that are similar to our incoming resource based on the EMPI rules and
	 *  similarity metrics.
	 *
	 * @param theBaseResource the {@link IBaseResource} we are attempting to find matching candidate Persons for.
	 * @return A list of {@link MatchedPersonCandidate} indicating all potential Person matches.
	 */
	public List<MatchedPersonCandidate> findPersonCandidates(IBaseResource theBaseResource) {
		Optional<List<MatchedPersonCandidate>> matchedPersonCandidates;

		matchedPersonCandidates = attemptToFindPersonCandidateFromIncomingEID(theBaseResource);
		if (matchedPersonCandidates.isPresent()) {
			return matchedPersonCandidates.get();
		}

		matchedPersonCandidates= attemptToFindPersonCandidateFromEmpiLinkTable(theBaseResource);
		if (matchedPersonCandidates.isPresent()) {
			return matchedPersonCandidates.get();
		}

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

	private Optional<List<MatchedPersonCandidate>> attemptToFindPersonCandidateFromIncomingEID(IBaseResource theBaseResource) {
		String eidFromResource = myPersonUtil.readEIDFromResource(theBaseResource);
		if (!StringUtils.isBlank(eidFromResource)) {
		IBaseResource iBaseResource = myEmpiResourceDaoSvc.searchPersonByEid(eidFromResource);
			if (iBaseResource != null) {
				Long pidOrNull = myResourceTableHelper.getPidOrNull(iBaseResource);
				//We make a fake link here as there no link for this association yet.
				//FIXME EMPI proobably have to re-model MatchedPersonCandidate?? I don't like making this fake thing to throw around.
				EmpiLink fakeEmpiLink = new EmpiLink();
				fakeEmpiLink.setMatchResult(EmpiMatchResultEnum.MATCH);
				fakeEmpiLink.setPersonPid(pidOrNull);
				MatchedPersonCandidate mpc = new MatchedPersonCandidate(new ResourcePersistentId(pidOrNull), fakeEmpiLink);
				return Optional.of(Collections.singletonList(mpc));
			}
		}
		return Optional.empty();
	}

	/**
	 * Attempt to find a currently matching Person, based on the presence of an {@link EmpiLink} entity.
	 *
	 * @param theBaseResource the {@link IBaseResource} which we want to find candidate Persons for.
	 * @return an Optional list of {@link MatchedPersonCandidate} indicating matches.
	 */
	private Optional<List<MatchedPersonCandidate>> attemptToFindPersonCandidateFromEmpiLinkTable(IBaseResource theBaseResource) {
		Long targetPid = myResourceTableHelper.getPidOrNull(theBaseResource);
		Optional<EmpiLink> oLink = myEmpiLinkDaoSvc.getMatchedLinkForTargetPid(targetPid);
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
		List<Long> unmatchablePersonPids = getNoMatchPersonPids(theBaseResource);
		Collection<IBaseResource> targetCandidates = myEmpiCandidateSearchSvc.findCandidates(
			myFhirContext.getResourceDefinition(theBaseResource).getName(),
			theBaseResource
		);

		List<MatchedTargetCandidate> matchedCandidates = targetCandidates.stream()
			.map(candidate -> new MatchedTargetCandidate(candidate, myEmpiResourceComparatorSvc.getMatchResult(theBaseResource, candidate)))
			.collect(Collectors.toList());


		//Convert all possible match targets to their equivalent Persons by looking up in the EmpiLink table,
		//while ensuring that the matches aren't in our NO_MATCH list.
		// The data flow is as follows ->
		// MatchedTargetCandidate -> Person -> EmpiLink -> MatchedPersonCandidate
		List<MatchedPersonCandidate> matchedPersons = matchedCandidates.stream()
			.filter(mc -> mc.getMatchResult().equals(EmpiMatchResultEnum.MATCH))
			.map(MatchedTargetCandidate::getCandidate)
			.map(candidate -> myEmpiLinkDaoSvc.getMatchedLinkForTargetPid(myResourceTableHelper.getPidOrNull(candidate)))
			.filter(Optional::isPresent)
			.map(Optional::get)
			.filter(candidateLink -> !unmatchablePersonPids.contains(candidateLink.getPersonPid()))
			.map(link -> new MatchedPersonCandidate(getResourcePersistentId(link.getPersonPid()), link))
			.collect(Collectors.toList());

		if (matchedPersons.isEmpty()) {
			return Optional.empty();
		} else {
			return Optional.of(matchedPersons);
		}

	}

	private List<Long> getNoMatchPersonPids(IBaseResource theBaseResource) {
		Long targetPid = myResourceTableHelper.getPidOrNull(theBaseResource);
		return myEmpiLinkDaoSvc.getEmpiLinksByTargetPidAndMatchResult(targetPid, EmpiMatchResultEnum.NO_MATCH)
			.stream()
			.map(EmpiLink::getPersonPid)
			.collect(Collectors.toList());
	}

	private boolean hasNoMatchLink(IBaseResource theBaseResource, Long thePersonPid) {
		Optional<EmpiLink> noMatchLink = myEmpiLinkDaoSvc.getEmpiLinksByPersonPidTargetPidAndMatchResult(thePersonPid, myResourceTableHelper.getPidOrNull(theBaseResource), EmpiMatchResultEnum.NO_MATCH);
		return noMatchLink.isPresent();
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
