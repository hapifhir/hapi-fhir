/*-
 * #%L
 * HAPI FHIR JPA Server - Master Data Management
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.mdm.svc;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.mdm.svc.candidate.MatchedGoldenResourceCandidate;
import ca.uhn.fhir.jpa.mdm.svc.candidate.MdmCandidateSearchSvc;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.mdm.api.IMdmMatchFinderSvc;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.api.MatchedTarget;
import ca.uhn.fhir.mdm.api.MdmMatchOutcome;
import ca.uhn.fhir.mdm.log.Logs;
import ca.uhn.fhir.mdm.model.CanonicalEID;
import ca.uhn.fhir.mdm.rules.json.MdmRulesJson;
import ca.uhn.fhir.mdm.rules.svc.MdmResourceMatcherSvc;
import ca.uhn.fhir.mdm.util.EIDHelper;
import ca.uhn.fhir.mdm.util.MdmSearchParamBuildingUtils;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.rest.param.TokenParam;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.mdm.svc.candidate.CandidateSearcher.idOrType;
import static org.hl7.fhir.dstu2016may.model.Basic.SP_IDENTIFIER;

@Service
public class MdmMatchFinderSvcImpl implements IMdmMatchFinderSvc {

	private static final Logger ourLog = Logs.getMdmTroubleshootingLog();

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Autowired
	private MdmCandidateSearchSvc myMdmCandidateSearchSvc;

	@Autowired
	private MdmResourceMatcherSvc myMdmResourceMatcherSvc;

	@Autowired
	private EIDHelper myEIDHelper;

	@Autowired
	IMdmSettings myMdmSettings;

	@Override
	@Nonnull
	@Transactional
	public List<MatchedTarget> getMatchedTargets(
		String theResourceType, IAnyResource theResource, RequestPartitionId theRequestPartitionId) {

		List<MatchedTarget> retval = matchBasedOnEid(theResourceType, theResource, theRequestPartitionId);
		if (!retval.isEmpty()) {
			return retval;
		}

		Collection<IAnyResource> targetCandidates = myMdmCandidateSearchSvc.findCandidates(theResourceType, theResource, theRequestPartitionId);

		List<MatchedTarget> matches = targetCandidates.stream()
			.map(candidate ->
				new MatchedTarget(candidate, myMdmResourceMatcherSvc.getMatchResult(theResource, candidate)))
			.collect(Collectors.toList());

		ourLog.trace("Found {} matched targets for {}.", matches.size(), idOrType(theResource, theResourceType));
		return matches;
	}

	private List<MatchedTarget> matchBasedOnEid(String theResourceType, IAnyResource theResource, RequestPartitionId theRequestPartitionId) {
		List<CanonicalEID> eidFromResource = myEIDHelper.getExternalEid(theResource);
		List<MatchedTarget> retval = new ArrayList<>();
		for (CanonicalEID eid : eidFromResource) {
			retval.addAll(searchForResourceByEID(theResource.getIdElement().toUnqualifiedVersionless(),
				eid.getValue(),
				theResourceType,
				theRequestPartitionId));
		}
		return retval;
	}

	private Collection<? extends MatchedTarget> searchForResourceByEID(IIdType theResourceIdToExclude, String theEid, String theResourceType, RequestPartitionId theRequestPartitionId) {
		SearchParameterMap map = SearchParameterMap.newSynchronous();
		map.add(SP_IDENTIFIER, new TokenParam(myMdmSettings.getMdmRules().getEnterpriseEIDSystemForResourceType(theResourceType), theEid));

		IFhirResourceDao<?> resourceDao = myDaoRegistry.getResourceDao(theResourceType);
		SystemRequestDetails systemRequestDetails = new SystemRequestDetails();
		systemRequestDetails.setRequestPartitionId(theRequestPartitionId);
		IBundleProvider search = resourceDao.search(map, systemRequestDetails);
		return search.getAllResources().stream()
			.map(IAnyResource.class::cast)
			// Exclude the incoming resource from the matched results
			.filter(resource -> !theResourceIdToExclude.equals(resource.getIdElement().toUnqualifiedVersionless()))
			.map(resource -> new MatchedTarget(resource, MdmMatchOutcome.EID_MATCH))
			.toList();
	}
}
