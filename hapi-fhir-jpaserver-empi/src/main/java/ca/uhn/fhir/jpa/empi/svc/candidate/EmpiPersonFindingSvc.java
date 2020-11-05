package ca.uhn.fhir.jpa.empi.svc.candidate;

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

import ca.uhn.fhir.empi.log.Logs;
import ca.uhn.fhir.jpa.empi.svc.EmpiResourceDaoSvc;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmpiPersonFindingSvc {
	private static final Logger ourLog = Logs.getEmpiTroubleshootingLog();

	@Autowired
	private EmpiResourceDaoSvc myEmpiResourceDaoSvc;

	@Autowired
	private FindCandidateByEidSvc myFindCandidateByEidSvc;

	@Autowired
	private FindCandidateByLinkSvc myFindCandidateByLinkSvc;

	@Autowired
	private FindCandidateByScoreSvc myFindCandidateByScoreSvc;

	/**
	 * Given an incoming IBaseResource, limited to Patient/Practitioner, return a list of {@link MatchedPersonCandidate}
	 * indicating possible candidates for a matching Person. Uses several separate methods for finding candidates:
	 * <p>
	 * 0. First, check the incoming Resource for an EID. If it is present, and we can find a Person with this EID, it automatically matches.
	 * 1. First, check link table for any entries where this baseresource is the target of a person. If found, return.
	 * 2. If none are found, attempt to find Person Resources which link to this theResource.
	 * 3. If none are found, attempt to find Person Resources similar to our incoming resource based on the EMPI rules and field matchers.
	 * 4. If none are found, attempt to find Persons that are linked to Patients/Practitioners that are similar to our incoming resource based on the EMPI rules and
	 * field matchers.
	 *
	 * @param theResource the {@link IBaseResource} we are attempting to find matching candidate Persons for.
	 * @return A list of {@link MatchedPersonCandidate} indicating all potential Person matches.
	 */
	public CandidateList findPersonCandidates(IAnyResource theResource) {
		CandidateList matchedPersonCandidates = myFindCandidateByEidSvc.findCandidates(theResource);

		if (matchedPersonCandidates.isEmpty()) {
			matchedPersonCandidates = myFindCandidateByLinkSvc.findCandidates(theResource);
		}

		if (matchedPersonCandidates.isEmpty()) {
			//OK, so we have not found any links in the EmpiLink table with us as a target. Next, let's find possible Patient/Practitioner
			//matches by following EMPI rules.

			matchedPersonCandidates = myFindCandidateByScoreSvc.findCandidates(theResource);
		}

		return matchedPersonCandidates;
	}

	public IAnyResource getPersonFromMatchedPersonCandidate(MatchedPersonCandidate theMatchedPersonCandidate) {
		ResourcePersistentId personPid = theMatchedPersonCandidate.getCandidatePersonPid();
		return myEmpiResourceDaoSvc.readPersonByPid(personPid);
	}
}
