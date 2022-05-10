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

import ca.uhn.fhir.mdm.rules.json.MdmResourceSearchParamJson;
import ca.uhn.fhir.jpa.mdm.svc.MdmSearchParamSvc;
import ca.uhn.fhir.util.UrlUtil;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MdmCandidateSearchCriteriaBuilderSvc {

	@Autowired
	private MdmSearchParamSvc myMdmSearchParamSvc;

	/*
	 * Given a list of criteria upon which to block, a resource search parameter, and a list of values for that given search parameter,
	 * build a query url. e.g.
	 *
	 * Patient?active=true&name.given=Gary,Grant&name.family=Graham
	 */
	@Nonnull
	public Optional<String> buildResourceQueryString(String theResourceType, IAnyResource theResource, List<String> theFilterCriteria, @Nullable MdmResourceSearchParamJson resourceSearchParam) {
		List<String> criteria = new ArrayList<>();

		// If there are candidate search params, then make use of them, otherwise, search with only the filters.
		if (resourceSearchParam != null) {
			resourceSearchParam.iterator().forEachRemaining(searchParam -> {
				//to compare it to all known GOLDEN_RESOURCE objects, using the overlapping search parameters that they have.
				List<String> valuesFromResourceForSearchParam = myMdmSearchParamSvc.getValueFromResourceForSearchParam(theResource, searchParam);
				if (!valuesFromResourceForSearchParam.isEmpty()) {
					criteria.add(buildResourceMatchQuery(searchParam, valuesFromResourceForSearchParam));
				}
			});
			if (criteria.isEmpty()) {
				//TODO GGG/KHS, re-evaluate whether we should early drop here.
				return Optional.empty();
			}
		}

		criteria.addAll(theFilterCriteria);
		return Optional.of(theResourceType + "?" +  String.join("&", criteria));
	}

	private String buildResourceMatchQuery(String theSearchParamName, List<String> theResourceValues) {
		String nameValueOrList = theResourceValues.stream()
			.map(UrlUtil::escapeUrlParam)
			.collect(Collectors.joining(","));
		return theSearchParamName + "=" + nameValueOrList;
	}
}
