/*-
 * #%L
 * HAPI FHIR JPA Server - Master Data Management
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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

import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.mdm.dao.MdmLinkDaoSvc;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class BaseCandidateFinder {
	@Autowired
	IIdHelperService myIdHelperService;
	@Autowired
	MdmLinkDaoSvc myMdmLinkDaoSvc;

	CandidateList findCandidates(IAnyResource theTarget) {
		CandidateList candidateList = new CandidateList(getStrategy());
		candidateList.addAll(findUniqueMatchGoldenResourceCandidates(theTarget));
		return candidateList;
	}

	/**
	 * Deprecated:
	 * prefer overriding findUniqueMatchGoldenResourceCandidates and implementing that.
	 */
	@Deprecated(forRemoval = true, since = "6.8.0")
	protected abstract List<MatchedGoldenResourceCandidate> findMatchGoldenResourceCandidates(IAnyResource theTarget);

	protected Set<MatchedGoldenResourceCandidate> findUniqueMatchGoldenResourceCandidates(IAnyResource theTarget) {
		// implementing to not break current api
		return new HashSet<>(findMatchGoldenResourceCandidates(theTarget));
	}

	protected abstract CandidateStrategyEnum getStrategy();
}
