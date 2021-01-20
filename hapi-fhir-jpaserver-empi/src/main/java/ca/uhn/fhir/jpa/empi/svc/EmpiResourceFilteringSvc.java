package ca.uhn.fhir.jpa.empi.svc;

/*-
 * #%L
 * HAPI FHIR JPA Server - Enterprise Master Patient Index
 * %%
 * Copyright (C) 2014 - 2021 University Health Network
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.api.IEmpiSettings;
import ca.uhn.fhir.empi.log.Logs;
import ca.uhn.fhir.empi.rules.json.EmpiResourceSearchParamJson;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpiResourceFilteringSvc {
	private static final Logger ourLog = Logs.getEmpiTroubleshootingLog();

	@Autowired
	private IEmpiSettings myEmpiSettings;
	@Autowired
	EmpiSearchParamSvc myEmpiSearchParamSvc;
	@Autowired
	FhirContext myFhirContext;

	/**
	 * Given a resource from the EMPI Channel, determine whether or not EMPI processing should occur on it.
	 *
	 * EMPI processing should occur if for any {@link EmpiResourceSearchParamJson) Search Param, the resource contains a value.
	 *
	 * If the resource has no attributes that appear in the candidate search params, processing should be skipped, as there is not
	 * sufficient information to perform meaningful EMPI processing. (For example, how can EMPI processing occur on a patient that has _no_ attributes?)
	 *
	 * @param theResource the resource that you wish to check against EMPI rules.
	 *
	 * @return whether or not EMPI processing should proceed
	 */
	public boolean shouldBeProcessed(IAnyResource theResource) {
		String resourceType = myFhirContext.getResourceType(theResource);
		List<EmpiResourceSearchParamJson> candidateSearchParams = myEmpiSettings.getEmpiRules().getCandidateSearchParams();

		if (candidateSearchParams.isEmpty()) {
			return true;
		}

		boolean containsValueForSomeSearchParam = candidateSearchParams.stream()
			.filter(csp -> myEmpiSearchParamSvc.searchParamTypeIsValidForResourceType(csp.getResourceType(), resourceType))
			.flatMap(csp -> csp.getSearchParams().stream())
			.map(searchParam -> myEmpiSearchParamSvc.getValueFromResourceForSearchParam(theResource, searchParam))
			.anyMatch(valueList -> !valueList.isEmpty());

		ourLog.trace("Is {} suitable for EMPI processing? : {}", theResource.getId(), containsValueForSomeSearchParam);
		return containsValueForSomeSearchParam;
	}
}
