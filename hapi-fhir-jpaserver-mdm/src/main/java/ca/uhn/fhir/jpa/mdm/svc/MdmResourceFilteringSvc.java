package ca.uhn.fhir.jpa.mdm.svc;

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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.log.Logs;
import ca.uhn.fhir.mdm.rules.json.MdmResourceSearchParamJson;
import ca.uhn.fhir.mdm.util.MdmResourceUtil;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MdmResourceFilteringSvc {
	private static final Logger ourLog = Logs.getMdmTroubleshootingLog();

	@Autowired
	private IMdmSettings myMdmSettings;
	@Autowired
	MdmSearchParamSvc myMdmSearchParamSvc;
	@Autowired
	FhirContext myFhirContext;

	/**
	 * Given a resource from the MDM Channel, determine whether or not MDM processing should occur on it.
	 *
	 * MDM processing should occur if for any {@link MdmResourceSearchParamJson ) Search Param, the resource contains a value.
	 *
	 * If the resource has no attributes that appear in the candidate search params, processing should be skipped, as there is not
	 * sufficient information to perform meaningful MDM processing. (For example, how can MDM processing occur on a patient that has _no_ attributes?)
	 *
	 * @param theResource the resource that you wish to check against MDM rules.
	 *
	 * @return whether or not MDM processing should proceed
	 */
	public boolean shouldBeProcessed(IAnyResource theResource) {
		if (MdmResourceUtil.isMdmManaged(theResource)) {
			ourLog.debug("MDM Message handler is dropping [{}] as it is MDM-managed.", theResource.getId());
			return false;
		}

		String resourceType = myFhirContext.getResourceType(theResource);
		List<MdmResourceSearchParamJson> candidateSearchParams = myMdmSettings.getMdmRules().getCandidateSearchParams();

		if (candidateSearchParams.isEmpty()) {
			return true;
		}

		boolean containsValueForSomeSearchParam = candidateSearchParams.stream()
			.filter(csp -> myMdmSearchParamSvc.searchParamTypeIsValidForResourceType(csp.getResourceType(), resourceType))
			.flatMap(csp -> csp.getSearchParams().stream())
			.map(searchParam -> myMdmSearchParamSvc.getValueFromResourceForSearchParam(theResource, searchParam))
			.anyMatch(valueList -> !valueList.isEmpty());

		ourLog.trace("Is {} suitable for MDM processing? : {}", theResource.getId(), containsValueForSomeSearchParam);
		return containsValueForSomeSearchParam;
	}
}
