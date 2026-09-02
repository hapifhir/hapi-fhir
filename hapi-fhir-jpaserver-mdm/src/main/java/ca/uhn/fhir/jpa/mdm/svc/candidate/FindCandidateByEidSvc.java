/*-
 * #%L
 * HAPI FHIR JPA Server - Master Data Management
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.mdm.dao.MdmLinkDaoSvc;
import ca.uhn.fhir.mdm.api.IMdmLink;
import ca.uhn.fhir.mdm.api.IMdmResourceDaoSvc;
import ca.uhn.fhir.mdm.api.MdmMatchOutcome;
import ca.uhn.fhir.mdm.log.Logs;
import ca.uhn.fhir.mdm.model.CanonicalEID;
import ca.uhn.fhir.mdm.util.EIDHelper;
import ca.uhn.fhir.mdm.util.MdmPartitionHelper;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class FindCandidateByEidSvc extends BaseCandidateFinder {

	private static final Logger ourLog = Logs.getMdmTroubleshootingLog();

	@Autowired
	private EIDHelper myEIDHelper;

	@Autowired
	private IMdmResourceDaoSvc myMdmResourceDaoSvc;

	@Autowired
	private MdmLinkDaoSvc myMdmLinkDaoSvc;

	@Autowired
	MdmPartitionHelper myMdmPartitionHelper;

	@Override
	protected List<MatchedGoldenResourceCandidate> findMatchGoldenResourceCandidates(IAnyResource theIncomingResource) {
		List<MatchedGoldenResourceCandidate> retval = new ArrayList<>();

		List<CanonicalEID> eidFromResource = myEIDHelper.getExternalEid(theIncomingResource);
		if (eidFromResource.isEmpty()) {
			return retval;
		}

		// A resource type may be identified by several EID systems, so resolve every EID the incoming
		// resource carries in one search rather than one search apiece.
		List<IAnyResource> foundGoldenResources = myMdmResourceDaoSvc.searchGoldenResourcesByEIDs(
				eidFromResource,
				theIncomingResource.getIdElement().getResourceType(),
				myMdmPartitionHelper.getRequestPartitionIdFromResourceForSearch(theIncomingResource));

		// Several of the incoming EIDs may resolve to the same golden resource. That is one candidate, not
		// several - reporting it more than once would send the resource down the multiple-candidate path
		// and flag a duplicate that does not exist.
		Set<IResourcePersistentId<?>> seenGoldenResourcePids = new LinkedHashSet<>();
		for (IAnyResource foundGoldenResource : foundGoldenResources) {
			// Exclude manually declared NO_MATCH links from candidates
			if (isNoMatch(foundGoldenResource, theIncomingResource)) {
				continue;
			}
			IResourcePersistentId<?> pidOrNull =
					myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), foundGoldenResource);
			if (!seenGoldenResourcePids.add(pidOrNull)) {
				continue;
			}
			ourLog.debug(
					"Incoming Resource {} matched Golden Resource {} by EID",
					theIncomingResource.getIdElement().toUnqualifiedVersionless(),
					foundGoldenResource.getIdElement().toUnqualifiedVersionless());

			retval.add(new MatchedGoldenResourceCandidate(pidOrNull, MdmMatchOutcome.EID_MATCH));
		}
		return retval;
	}

	private boolean isNoMatch(IAnyResource theGoldenResource, IAnyResource theSourceResource) {
		Optional<? extends IMdmLink> oLink =
				myMdmLinkDaoSvc.getLinkByGoldenResourceAndSourceResource(theGoldenResource, theSourceResource);
		if (oLink.isEmpty()) {
			return false;
		}
		IMdmLink link = oLink.get();
		return link.isNoMatch();
	}

	@Override
	protected CandidateStrategyEnum getStrategy() {
		return CandidateStrategyEnum.EID;
	}
}
