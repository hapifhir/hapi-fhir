package ca.uhn.fhir.jpa.empi.svc;

/*-
 * #%L
 * HAPI FHIR JPA Server - Enterprise Master Patient Index
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.empi.api.IEmpiMatchFinderSvc;
import ca.uhn.fhir.empi.api.MatchedTarget;
import ca.uhn.fhir.empi.log.Logs;
import ca.uhn.fhir.empi.model.CanonicalEID;
import ca.uhn.fhir.empi.util.EIDHelper;
import ca.uhn.fhir.jpa.dao.EmpiLinkDaoSvc;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.entity.EmpiLink;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmpiPersonFindingSvc {
	private static final Logger ourLog = Logs.getEmpiTroubleshootingLog();

	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	IdHelperService myIdHelperService;
	@Autowired
	private EmpiLinkDaoSvc myEmpiLinkDaoSvc;
	@Autowired
	private EmpiResourceDaoSvc myEmpiResourceDaoSvc;
	@Autowired
	private IEmpiMatchFinderSvc myEmpiMatchFinderSvc;
	@Autowired
	private EIDHelper myEIDHelper;

	/**
	 * Given an incoming IBaseResource, limited to Patient/Practitioner, return a list of {@link MatchedPersonCandidate}
	 * indicating possible candidates for a matching Person. Uses several separate methods for finding candidates:
	 * <p>
	 * 0. First, check the incoming Resource for an EID. If it is present, and we can find a Person with this EID, it automatically matches.
	 * 1. First, check link table for any entries where this baseresource is the target of a person. If found, return.
	 * 2. If none are found, attempt to find Person Resources which link to this theBaseResource.
	 * 3. If none are found, attempt to find Persons similar to our incoming resource based on the EMPI rules and similarity metrics.
	 * 4. If none are found, attempt to find Persons that are linked to Patients/Practitioners that are similar to our incoming resource based on the EMPI rules and
	 * similarity metrics.
	 *
	 * @param theBaseResource the {@link IBaseResource} we are attempting to find matching candidate Persons for.
	 * @return A list of {@link MatchedPersonCandidate} indicating all potential Person matches.
	 */
	public List<MatchedPersonCandidate> findPersonCandidates(IBaseResource theBaseResource) {
		List<MatchedPersonCandidate> matchedPersonCandidates = attemptToFindPersonCandidateFromIncomingEID(theBaseResource);
		if (matchedPersonCandidates.isEmpty()) {
			matchedPersonCandidates = attemptToFindPersonCandidateFromEmpiLinkTable(theBaseResource);
		}
		if (matchedPersonCandidates.isEmpty()) {
			//OK, so we have not found any links in the EmpiLink table with us as a target. Next, let's find possible Patient/Practitioner
			//matches by following EMPI rules.

			matchedPersonCandidates = attemptToFindPersonCandidateFromSimilarTargetResource(theBaseResource);
		}
		return matchedPersonCandidates;
	}

	private List<MatchedPersonCandidate> attemptToFindPersonCandidateFromIncomingEID(IBaseResource theBaseResource) {
		List<MatchedPersonCandidate> retval = new ArrayList<>();

		List<CanonicalEID> eidFromResource = myEIDHelper.getExternalEid(theBaseResource);
		if (!eidFromResource.isEmpty()) {
			for (CanonicalEID eid : eidFromResource) {
				IBaseResource foundPerson = myEmpiResourceDaoSvc.searchPersonByEid(eid.getValue());
				if (foundPerson != null) {
					Long pidOrNull = myIdHelperService.getPidOrNull(foundPerson);
					MatchedPersonCandidate mpc = new MatchedPersonCandidate(new ResourcePersistentId(pidOrNull), EmpiMatchResultEnum.MATCH);
					ourLog.debug("Matched {} by EID {}", foundPerson.getIdElement(), eid);
					retval.add(mpc);
				}
			}
		}
		return retval;
	}

	/**
	 * Attempt to find a currently matching Person, based on the presence of an {@link EmpiLink} entity.
	 *
	 * @param theBaseResource the {@link IBaseResource} which we want to find candidate Persons for.
	 * @return an Optional list of {@link MatchedPersonCandidate} indicating matches.
	 */
	private List<MatchedPersonCandidate> attemptToFindPersonCandidateFromEmpiLinkTable(IBaseResource theBaseResource) {
		List<MatchedPersonCandidate> retval = new ArrayList<>();

		Long targetPid = myIdHelperService.getPidOrNull(theBaseResource);
		Optional<EmpiLink> oLink = myEmpiLinkDaoSvc.getMatchedLinkForTargetPid(targetPid);
		if (oLink.isPresent()) {
			ResourcePersistentId pid = new ResourcePersistentId(oLink.get().getPersonPid());
			// FIXME EMPI ensure we aren't changing this link
			ourLog.debug("Resource previously linked.  Using existing link.");
			retval.add(new MatchedPersonCandidate(pid, oLink.get().getMatchResult()));
		}
		return retval;
	}

	/**
	 * Attempt to find matching Persons by resolving them from similar Matching target resources, where target resource
	 * can be either Patient or Practitioner. Runs EMPI logic over the existing Patient/Practitioners, then finds their
	 * entries in the EmpiLink table, and returns all the matches found therein.
	 *
	 * @param theBaseResource the {@link IBaseResource} which we want to find candidate Persons for.
	 * @return an Optional list of {@link MatchedPersonCandidate} indicating matches.
	 */
	private List<MatchedPersonCandidate> attemptToFindPersonCandidateFromSimilarTargetResource(IBaseResource theBaseResource) {
		List<MatchedPersonCandidate> retval = new ArrayList<>();

		List<Long> personPidsToExclude = getNoMatchPersonPids(theBaseResource);
		List<MatchedTarget> matchedCandidates = myEmpiMatchFinderSvc.getMatchedTargets(myFhirContext.getResourceType(theBaseResource), theBaseResource);

		//Convert all possible match targets to their equivalent Persons by looking up in the EmpiLink table,
		//while ensuring that the matches aren't in our NO_MATCH list.
		// The data flow is as follows ->
		// MatchedTargetCandidate -> Person -> EmpiLink -> MatchedPersonCandidate
		matchedCandidates = matchedCandidates.stream().filter(mc -> mc.getMatchResult().equals(EmpiMatchResultEnum.MATCH) || mc.getMatchResult().equals(EmpiMatchResultEnum.POSSIBLE_MATCH)).collect(Collectors.toList());
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

}
