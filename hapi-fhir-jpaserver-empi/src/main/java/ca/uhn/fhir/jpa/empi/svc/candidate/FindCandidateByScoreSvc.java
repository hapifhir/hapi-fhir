package ca.uhn.fhir.jpa.empi.svc.candidate;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.empi.api.IEmpiMatchFinderSvc;
import ca.uhn.fhir.empi.api.MatchedTarget;
import ca.uhn.fhir.empi.log.Logs;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.empi.dao.EmpiLinkDaoSvc;
import ca.uhn.fhir.jpa.entity.EmpiLink;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FindCandidateByScoreSvc extends BaseCandidateFinder {
	private static final Logger ourLog = Logs.getEmpiTroubleshootingLog();

	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	IdHelperService myIdHelperService;
	@Autowired
	private EmpiLinkDaoSvc myEmpiLinkDaoSvc;
	@Autowired
	private IEmpiMatchFinderSvc myEmpiMatchFinderSvc;

	/**
	 * Attempt to find matching Persons by resolving them from similar Matching target resources, where target resource
	 * can be either Patient or Practitioner. Runs EMPI logic over the existing Patient/Practitioners, then finds their
	 * entries in the EmpiLink table, and returns all the matches found therein.
	 *
	 * @param theTarget the {@link IBaseResource} which we want to find candidate Persons for.
	 * @return an Optional list of {@link MatchedPersonCandidate} indicating matches.
	 */
	@Override
	protected List<MatchedPersonCandidate> findMatchPersonCandidates(IAnyResource theTarget) {
		List<MatchedPersonCandidate> retval = new ArrayList<>();

		List<Long> personPidsToExclude = getNoMatchPersonPids(theTarget);
		List<MatchedTarget> matchedCandidates = myEmpiMatchFinderSvc.getMatchedTargets(myFhirContext.getResourceType(theTarget), theTarget);

		//Convert all possible match targets to their equivalent Persons by looking up in the EmpiLink table,
		//while ensuring that the matches aren't in our NO_MATCH list.
		// The data flow is as follows ->
		// MatchedTargetCandidate -> Person -> EmpiLink -> MatchedPersonCandidate
		matchedCandidates = matchedCandidates.stream().filter(mc -> mc.isMatch() || mc.isPossibleMatch()).collect(Collectors.toList());
		for (MatchedTarget match : matchedCandidates) {
			Optional<EmpiLink> optMatchEmpiLink = myEmpiLinkDaoSvc.getMatchedLinkForTargetPid(myIdHelperService.getPidOrNull(match.getTarget()));
			if (!optMatchEmpiLink.isPresent()) {
				continue;
			}

			EmpiLink matchEmpiLink = optMatchEmpiLink.get();
			if (personPidsToExclude.contains(matchEmpiLink.getPersonPid())) {
				ourLog.info("Skipping EMPI on candidate person with PID {} due to manual NO_MATCH", matchEmpiLink.getPersonPid());
				continue;
			}

			MatchedPersonCandidate candidate = new MatchedPersonCandidate(getResourcePersistentId(matchEmpiLink.getPersonPid()), match.getMatchResult());
			retval.add(candidate);
		}
		return retval;
	}

	private List<Long> getNoMatchPersonPids(IBaseResource theBaseResource) {
		Long targetPid = myIdHelperService.getPidOrNull(theBaseResource);
		return myEmpiLinkDaoSvc.getEmpiLinksByTargetPidAndMatchResult(targetPid, EmpiMatchResultEnum.NO_MATCH)
			.stream()
			.map(EmpiLink::getPersonPid)
			.collect(Collectors.toList());
	}

	private ResourcePersistentId getResourcePersistentId(Long thePersonPid) {
		return new ResourcePersistentId(thePersonPid);
	}

	@Override
	protected CandidateStrategyEnum getStrategy() {
		return CandidateStrategyEnum.SCORE;
	}
}
