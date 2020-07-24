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

import ca.uhn.fhir.empi.rules.json.EmpiResourceSearchParamJson;
import ca.uhn.fhir.jpa.empi.svc.EmpiSearchParamSvc;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmpiCandidateSearchCriteriaBuilderSvc {
	@Autowired
	private EmpiSearchParamSvc myEmpiSearchParamSvc;

	/*
	 * Given a list of criteria upon which to block, a resource search parameter, and a list of values for that given search parameter,
	 * build a query url. e.g.
	 *
	 * Patient?active=true&name.given=Gary,Grant&name.family=Graham
	 */
	@Nonnull
	public Optional<String> buildResourceQueryString(String theResourceType, IAnyResource theResource, List<String> theFilterCriteria, @Nullable EmpiResourceSearchParamJson resourceSearchParam) {
		List<String> criteria = new ArrayList<>();

		// If there are candidate search params, then make use of them, otherwise, search with only the filters.
		if (resourceSearchParam != null) {
			resourceSearchParam.iterator().forEachRemaining(searchParam -> {
				//to compare it to all known PERSON objects, using the overlapping search parameters that they have.
				List<String> valuesFromResourceForSearchParam = myEmpiSearchParamSvc.getValueFromResourceForSearchParam(theResource, searchParam);
				if (!valuesFromResourceForSearchParam.isEmpty()) {
					criteria.add(buildResourceMatchQuery(searchParam, valuesFromResourceForSearchParam));
				}
			});

			//TODO GGG/KHS, here's a question: What scenario would be actually want to return this empty optional.
			//In the case where the resource being matched doesnt have any of the values that the EmpiResourceSearchParamJson wants?
			//e.g. if i have a patient with name 'gary', but no birthdate, and i have a search param saying
			//		{
			//			"resourceType": "Patient",
			//			"searchParams": ["birthdate"]
			//		},
			// do I actually want it to return Zero candidates? if so, this following conditional is valid. However
			// What if I still want to match that person? Will they be unmatchable since they have no birthdate?

			if (criteria.isEmpty()) {
				return Optional.empty();
			}
		}

		criteria.addAll(theFilterCriteria);
		return Optional.of(theResourceType + "?" +  String.join("&", criteria));
	}

	private String buildResourceMatchQuery(String theSearchParamName, List<String> theResourceValues) {
		return theSearchParamName + "=" + String.join(",", theResourceValues);
	}
}
