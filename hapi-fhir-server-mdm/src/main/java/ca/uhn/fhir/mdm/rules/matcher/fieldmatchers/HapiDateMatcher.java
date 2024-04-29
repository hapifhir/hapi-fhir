/*-
 * #%L
 * HAPI FHIR - Master Data Management
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.mdm.rules.matcher.fieldmatchers;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.mdm.rules.json.MdmMatcherJson;
import ca.uhn.fhir.mdm.rules.matcher.models.IMdmFieldMatcher;
import org.hl7.fhir.instance.model.api.IBase;

public class HapiDateMatcher implements IMdmFieldMatcher {

	private final FhirContext myFhirContext;

	public HapiDateMatcher(FhirContext theFhirContext) {
		myFhirContext = theFhirContext;
	}

	@Override
	public boolean matches(IBase theLeftBase, IBase theRightBase, MdmMatcherJson theParams) {
		DateTimeWrapper left = new DateTimeWrapper(myFhirContext, theLeftBase);
		DateTimeWrapper right = new DateTimeWrapper(myFhirContext, theRightBase);

		/*
		 * we use the precision to determine how we should equate.
		 * We should use the same precision (the precision of the less
		 * precise date)
		 */
		int comparison = left.getPrecision().compareTo(right.getPrecision());

		String leftString;
		String rightString;
		if (comparison == 0) {
			// same precision
			leftString = left.getValueAsString();
			rightString = right.getValueAsString();
		} else if (comparison > 0) {
			// left date is more precise than right date
			leftString = left.getValueAsStringWithPrecision(right.getPrecision());
			rightString = right.getValueAsString();
		} else {
			// right date is more precise than left date
			rightString = right.getValueAsStringWithPrecision(left.getPrecision());
			leftString = left.getValueAsString();
		}

		return leftString.equals(rightString);
	}
}
