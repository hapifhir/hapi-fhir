package ca.uhn.fhir.empi.rules.svc;

/*-
 * #%L
 * hapi-fhir-empi-rules
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.rules.json.EmpiFieldMatchJson;
import ca.uhn.fhir.empi.rules.json.EmpiRulesJson;
import ca.uhn.fhir.jpa.api.EmpiMatchResultEnum;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmpiResourceComparatorSvc {
	private final FhirContext myFhirContext;
	private final EmpiRulesSvc myEmpiRulesSvc;

	private EmpiRulesJson myEmpiRulesJson;
	private final List<EmpiResourceFieldComparator> myFieldComparators = new ArrayList<>();

	@Autowired
	public EmpiResourceComparatorSvc(FhirContext theFhirContext, EmpiRulesSvc theEmpiRulesSvc) {
		myFhirContext = theFhirContext;
		myEmpiRulesSvc = theEmpiRulesSvc;
	}

	@PostConstruct
	public void init() {
		myEmpiRulesJson = myEmpiRulesSvc.getEmpiRules();
		for (EmpiFieldMatchJson matchFieldJson : myEmpiRulesJson.getMatchFields()) {
			myFieldComparators.add(new EmpiResourceFieldComparator(myFhirContext, matchFieldJson));
		}

	}
	public EmpiMatchResultEnum getMatchResult(IBaseResource theLeftResource, IBaseResource theRightResource) {
		double weight = compare(theLeftResource, theRightResource);
		return myEmpiRulesJson.getMatchResult(weight);
	}

	double compare(IBaseResource theLeftResource, IBaseResource theRightResource) {
		long matchVector = getMatchVector(theLeftResource, theRightResource);
		return myEmpiRulesJson.getWeight(matchVector);
	}

	private long getMatchVector(IBaseResource theLeftResource, IBaseResource theRightResource) {
		long retval = 0;
		for (int i = 0; i < myFieldComparators.size(); ++i) {
			EmpiResourceFieldComparator fieldComparator = myFieldComparators.get(i);
			if (fieldComparator.match(theLeftResource, theRightResource)) {
				retval |= (1 << i);
			}
		}
		return retval;
	}
}
