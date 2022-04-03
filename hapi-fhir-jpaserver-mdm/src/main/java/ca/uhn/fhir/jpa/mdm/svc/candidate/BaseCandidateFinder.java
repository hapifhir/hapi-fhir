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

import ca.uhn.fhir.jpa.dao.index.IJpaIdHelperService;
import ca.uhn.fhir.jpa.mdm.dao.MdmLinkDaoSvc;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class BaseCandidateFinder {
	@Autowired
	IJpaIdHelperService myIdHelperService;
	@Autowired
	MdmLinkDaoSvc myMdmLinkDaoSvc;

	CandidateList findCandidates(IAnyResource theTarget) {
		CandidateList candidateList = new CandidateList(getStrategy());
		candidateList.addAll(findMatchGoldenResourceCandidates(theTarget));
		return candidateList;
	}

	protected abstract List<MatchedGoldenResourceCandidate> findMatchGoldenResourceCandidates(IAnyResource theTarget);

	protected abstract CandidateStrategyEnum getStrategy();
}
