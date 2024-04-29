/*-
 * #%L
 * HAPI FHIR JPA Server - Master Data Management
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.mdm.svc.candidate;

import ca.uhn.fhir.jpa.mdm.models.FindGoldenResourceCandidatesParams;
import ca.uhn.fhir.mdm.api.IMdmResourceDaoSvc;
import ca.uhn.fhir.mdm.log.Logs;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MdmGoldenResourceFindingSvc {

	private static final Logger ourLog = Logs.getMdmTroubleshootingLog();

	@Autowired
	private IMdmResourceDaoSvc myMdmResourceDaoSvc;

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
	 * @param theParams Params hold the {@link IBaseResource} for which we are attempting to find matching candidate Golden Resources,
	 *                  as well as the mdm context.
	 * @return A list of {@link MatchedGoldenResourceCandidate} indicating all potential Golden Resource matches.
	 */
	public CandidateList findGoldenResourceCandidates(FindGoldenResourceCandidatesParams theParams) {
		IAnyResource resource = theParams.getResource();

		CandidateList eidGoldenResources = myFindCandidateByEidSvc.findCandidates(resource);

		// if we have matches from eid, we'll return only these
		if (!eidGoldenResources.isEmpty()) {
			return eidGoldenResources;
		}

		boolean isUpdate =
				theParams.getContext().getRestOperation() == MdmTransactionContext.OperationType.UPDATE_RESOURCE;

		// find MdmLinks that have theResource as the source
		// (these are current golden resources matching this resource)
		CandidateList linkGoldenResources = myFindCandidateByLinkSvc.findCandidates(resource);

		if (!linkGoldenResources.isEmpty() && !isUpdate) {
			return linkGoldenResources;
		}

		// if we're updating, we might have existing resources that could *also* match
		// find other golden resources that could be matching to this resource
		// (we only need to do this for updates because otherwise they would already have matching resources
		CandidateList anyGoldenResources = myFindCandidateByExampleSvc.findCandidates(resource);

		if (linkGoldenResources.isEmpty()) {
			// only other resources are available - we'll return this
			return anyGoldenResources;
		}

		// else, we will combine the lists
		CandidateList matches = new CandidateList(CandidateStrategyEnum.ANY);
		matches.addAll(CandidateStrategyEnum.LINK, linkGoldenResources.getCandidates());
		matches.addAll(CandidateStrategyEnum.SCORE, anyGoldenResources.getCandidates());

		return matches;
	}

	public IAnyResource getGoldenResourceFromMatchedGoldenResourceCandidate(
			MatchedGoldenResourceCandidate theMatchedGoldenResourceCandidate, String theResourceType) {
		IResourcePersistentId goldenResourcePid = theMatchedGoldenResourceCandidate.getCandidateGoldenResourcePid();
		return myMdmResourceDaoSvc.readGoldenResourceByPid(goldenResourcePid, theResourceType);
	}
}
