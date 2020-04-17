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

import ca.uhn.fhir.empi.api.EmpiLinkSourceEnum;
import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.empi.api.IEmpiLinkSvc;
import ca.uhn.fhir.empi.util.CanonicalEID;
import ca.uhn.fhir.empi.util.EIDHelper;
import ca.uhn.fhir.empi.util.PersonHelper;
import ca.uhn.fhir.jpa.empi.util.EmpiUtil;
import ca.uhn.fhir.jpa.model.cross.ResourcePersistentId;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
	@Autowired
	private EIDHelper myEIDHelper;

	/**
	 * Given an Empi Target (consisting of either a Patient or a Practitioner), find a suitable Person candidate for them,
	 * or create one if one does not exist. Performs matching based on rules defined in empi-rules.json.
	 * Does nothing if resource is determined to be not managed by EMPI.
	 *
	 * @param theResource the incoming EMPI target, which is either a Patient or Practitioner.
	 */
	public void updateEmpiLinksForEmpiTarget(IBaseResource theResource) {
		if (EmpiUtil.isManagedByEmpi(theResource)) {
			doEmpiUpdate(theResource);
		}
	}

	private void doEmpiUpdate(IBaseResource theResource) {
		List<MatchedPersonCandidate> personCandidates = myEmpiPersonFindingSvc.findPersonCandidates(theResource);
		if (personCandidates.isEmpty()) {
			handleEmpiWithNoCandidates(theResource);
		} else if (personCandidates.size() == 1) {
			handleEmpiWithSingleCandidate(theResource, personCandidates);
		} else {
			handleEmpiWithMultipleCandidates(theResource, personCandidates);
		}
	}

	private void handleEmpiWithMultipleCandidates(IBaseResource theResource, List<MatchedPersonCandidate> thePersonCandidates) {
		Long samplePersonPid = thePersonCandidates.get(0).getCandidatePersonPid().getIdAsLong();
		boolean allSamePerson = thePersonCandidates.stream()
			.allMatch(candidate -> candidate.getCandidatePersonPid().getIdAsLong().equals(samplePersonPid));

		if (allSamePerson) {
			handleEmpiWithSingleCandidate(theResource, thePersonCandidates);
		} else {

			//Set them all as POSSIBLE_MATCH
			List<IBaseResource> persons = thePersonCandidates.stream().map(mpc -> getPersonFromMatchedPersonCandidate(mpc)).collect(Collectors.toList());
				persons.forEach(person -> {
					myEmpiLinkSvc.updateLink(person, theResource, EmpiMatchResultEnum.POSSIBLE_MATCH, EmpiLinkSourceEnum.AUTO);
				});

			//Set all Persons as POSSIBLE_DUPLICATE of the first person.
			IBaseResource samplePerson = persons.get(0);
			persons.subList(1, persons.size()).stream()
				.forEach(possibleDuplicatePerson -> {
					myEmpiLinkSvc.updateLink(samplePerson, possibleDuplicatePerson, EmpiMatchResultEnum.POSSIBLE_DUPLICATE, EmpiLinkSourceEnum.AUTO);
				});
		}
	}

	private void handleEmpiWithNoCandidates(IBaseResource theResource) {
		IBaseResource newPerson = myPersonHelper.createPersonFromEmpiTarget(theResource);
		myEmpiLinkSvc.updateLink(newPerson, theResource, EmpiMatchResultEnum.MATCH, EmpiLinkSourceEnum.AUTO);
	}

	private void handleEmpiWithSingleCandidate(IBaseResource theResource, List<MatchedPersonCandidate> thePersonCandidates) {
		MatchedPersonCandidate matchedPersonCandidate = thePersonCandidates.get(0);
		IBaseResource person = getPersonFromMatchedPersonCandidate(matchedPersonCandidate);
		if (myPersonHelper.isPotentialDuplicate(person, theResource)) {
			IBaseResource newPerson = myPersonHelper.createPersonFromEmpiTarget(theResource);
			myEmpiLinkSvc.updateLink(newPerson, theResource, EmpiMatchResultEnum.MATCH, EmpiLinkSourceEnum.AUTO);
			myEmpiLinkSvc.updateLink(newPerson, person, EmpiMatchResultEnum.POSSIBLE_DUPLICATE, EmpiLinkSourceEnum.AUTO);
		} else {
			if (matchedPersonCandidate.getMatchResult().equals(EmpiMatchResultEnum.MATCH)) {
				handleEidOverwrite(person, theResource);
			}
			myEmpiLinkSvc.updateLink(person, theResource, matchedPersonCandidate.getMatchResult(), EmpiLinkSourceEnum.AUTO);
		}
	}

	private IBaseResource getPersonFromMatchedPersonCandidate(MatchedPersonCandidate theMatchedPersonCandidate) {
		ResourcePersistentId personPid = theMatchedPersonCandidate.getCandidatePersonPid();
		return myEmpiResourceDaoSvc.readPersonByPid(personPid);
	}

	private void handleEidOverwrite(IBaseResource thePerson, IBaseResource theResource) {
		Optional<CanonicalEID> eidFromResource = myEIDHelper.getExternalEid(theResource);
		if (eidFromResource.isPresent()) {
			myPersonHelper.updatePersonFromEmpiTarget(thePerson, theResource);
		}
	}
}
