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

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.mdm.dao.MdmLinkDaoSvc;
import ca.uhn.fhir.jpa.mdm.svc.MdmResourceDaoSvc;
import ca.uhn.fhir.mdm.api.MdmMatchOutcome;
import ca.uhn.fhir.mdm.log.Logs;
import ca.uhn.fhir.mdm.model.CanonicalEID;
import ca.uhn.fhir.mdm.util.EIDHelper;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FindCandidateByEidSvc extends BaseCandidateFinder {

	private static final Logger ourLog = Logs.getMdmTroubleshootingLog();

	@Autowired
	private EIDHelper myEIDHelper;
	@Autowired
	private MdmResourceDaoSvc myMdmResourceDaoSvc;
	@Autowired
	private MdmLinkDaoSvc myMdmLinkDaoSvc;

	@Override
	protected List<MatchedGoldenResourceCandidate> findMatchGoldenResourceCandidates(IAnyResource theBaseResource) {
		List<MatchedGoldenResourceCandidate> retval = new ArrayList<>();

		List<CanonicalEID> eidFromResource = myEIDHelper.getExternalEid(theBaseResource);
		if (!eidFromResource.isEmpty()) {
			for (CanonicalEID eid : eidFromResource) {
				Optional<IAnyResource> oFoundGoldenResource = myMdmResourceDaoSvc.searchGoldenResourceByEID(eid.getValue(), theBaseResource.getIdElement().getResourceType(), (RequestPartitionId) theBaseResource.getUserData(Constants.RESOURCE_PARTITION_ID));
				if (oFoundGoldenResource.isPresent()) {
					IAnyResource foundGoldenResource = oFoundGoldenResource.get();
					// Exclude manually declared NO_MATCH links from candidates
					if (isNoMatch(foundGoldenResource, theBaseResource)) {
						continue;
					}
					Long pidOrNull = myIdHelperService.getPidOrNull(foundGoldenResource);
					MatchedGoldenResourceCandidate mpc = new MatchedGoldenResourceCandidate(new ResourcePersistentId(pidOrNull), MdmMatchOutcome.EID_MATCH);
					ourLog.debug("Matched {} by EID {}", foundGoldenResource.getIdElement(), eid);
					retval.add(mpc);
				}
			}
		}
		return retval;
	}

	private boolean isNoMatch(IAnyResource theGoldenResource, IAnyResource theSourceResource) {
		Optional<MdmLink> oLink = myMdmLinkDaoSvc.getLinkByGoldenResourceAndSourceResource(theGoldenResource, theSourceResource);
		if (oLink.isEmpty()) {
			return false;
		}
		MdmLink link = oLink.get();
		return link.isNoMatch();
	}

	@Override
	protected CandidateStrategyEnum getStrategy() {
		return CandidateStrategyEnum.EID;
	}
}
