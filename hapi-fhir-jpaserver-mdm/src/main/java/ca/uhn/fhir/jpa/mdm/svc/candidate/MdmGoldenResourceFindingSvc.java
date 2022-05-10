package ca.uhn.fhir.jpa.mdm.svc.candidate;

/*-
 * #%L
 * HAPI FHIR JPA Server - Master Data Management
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.jpa.mdm.svc.MdmResourceDaoSvc;
import ca.uhn.fhir.mdm.log.Logs;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MdmGoldenResourceFindingSvc {

	private static final Logger ourLog = Logs.getMdmTroubleshootingLog();

	@Autowired
	private MdmResourceDaoSvc myMdmResourceDaoSvc;

	@Autowired
	private FindCandidateByEidSvc myFindCandidateByEidSvc;

	@Autowired
	private FindCandidateByLinkSvc myFindCandidateByLinkSvc;

	@Autowired
	private FindCandidateByExampleSvc myFindCandidateByExampleSvc;

	/**
	 * Given an incoming IBaseResource, limited to the supported MDM type, return a list of {@link MatchedGoldenResourceCandidate}
	 * indicating possible candidates for a matching Golden Resource. Uses several separate methods for finding candidates:
	 * <p>
	 * 0. First, check the incoming Resource for an EID. If it is present, and we can find a Golden Resource with this EID, it automatically matches.
	 * 1. First, check link table for any entries where this baseresource is the source of a Golden Resource. If found, return.
	 * 2. If none are found, attempt to find Golden Resources which link to this theResource.
	 * 3. If none are found, attempt to find Golden Resources similar to our incoming resource based on the MDM rules and field matchers.
	 * 4. If none are found, attempt to find Golden Resources that are linked to sources that are similar to our incoming resource based on the MDM rules and
	 * field matchers.
	 *
	 * @param theResource the {@link IBaseResource} we are attempting to find matching candidate Golden Resources for.
	 * @return A list of {@link MatchedGoldenResourceCandidate} indicating all potential Golden Resource matches.
	 */
	public CandidateList findGoldenResourceCandidates(IAnyResource theResource) {
		CandidateList matchedGoldenResourceCandidates = myFindCandidateByEidSvc.findCandidates(theResource);

		if (matchedGoldenResourceCandidates.isEmpty()) {
			matchedGoldenResourceCandidates = myFindCandidateByLinkSvc.findCandidates(theResource);
		}

		if (matchedGoldenResourceCandidates.isEmpty()) {
			//OK, so we have not found any links in the MdmLink table with us as a source. Next, let's find
			//possible Golden Resources matches by following MDM rules.
			matchedGoldenResourceCandidates = myFindCandidateByExampleSvc.findCandidates(theResource);
		}

		return matchedGoldenResourceCandidates;
	}

	public IAnyResource getGoldenResourceFromMatchedGoldenResourceCandidate(MatchedGoldenResourceCandidate theMatchedGoldenResourceCandidate, String theResourceType) {
		ResourcePersistentId goldenResourcePid = theMatchedGoldenResourceCandidate.getCandidateGoldenResourcePid();
		return myMdmResourceDaoSvc.readGoldenResourceByPid(goldenResourcePid, theResourceType);
	}
}
