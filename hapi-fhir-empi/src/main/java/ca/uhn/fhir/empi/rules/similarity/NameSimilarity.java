package ca.uhn.fhir.empi.rules.similarity;

/*-
 * #%L
 * HAPI FHIR - Enterprise Master Patient Index
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
import ca.uhn.fhir.empi.util.NameUtil;
import ca.uhn.fhir.util.StringNormalizer;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBase;

import java.util.List;
import java.util.stream.Collectors;

public class NameSimilarity implements IEmpiFieldSimilarity {
	private final EmpiPersonNameMatchModeEnum myMatchMode;

	public NameSimilarity(EmpiPersonNameMatchModeEnum theMatchMode) {
		myMatchMode = theMatchMode;
	}

	@Override
	public double similarity(FhirContext theFhirContext, IBase theLeftBase, IBase theRightBase) {
		String leftFamilyName = NameUtil.extractFamilyName(theFhirContext, theLeftBase);
		String rightFamilyName = NameUtil.extractFamilyName(theFhirContext, theRightBase);
		if (StringUtils.isEmpty(leftFamilyName) || StringUtils.isEmpty(rightFamilyName)) {
			return 0.0;
		}

		boolean match = false;
		boolean exact =
			myMatchMode == EmpiPersonNameMatchModeEnum.EXACT_ANY_ORDER ||
				myMatchMode == EmpiPersonNameMatchModeEnum.STANDARD_FIRST_AND_LAST;

		List<String> leftGivenNames = NameUtil.extractGivenNames(theFhirContext, theLeftBase);
		List<String> rightGivenNames = NameUtil.extractGivenNames(theFhirContext, theRightBase);

		if (!exact) {
			leftFamilyName = StringNormalizer.normalizeString(leftFamilyName);
			rightFamilyName = StringNormalizer.normalizeString(rightFamilyName);
			leftGivenNames = leftGivenNames.stream().map(StringNormalizer::normalizeString).collect(Collectors.toList());
			rightGivenNames = rightGivenNames.stream().map(StringNormalizer::normalizeString).collect(Collectors.toList());
		}

		for (String leftGivenName : leftGivenNames) {
			for (String rightGivenName : rightGivenNames) {
				match |= leftGivenName.equals(rightGivenName) && leftFamilyName.equals(rightFamilyName);
				if (myMatchMode == EmpiPersonNameMatchModeEnum.STANDARD_ANY_ORDER || myMatchMode == EmpiPersonNameMatchModeEnum.EXACT_ANY_ORDER) {
					match |= leftGivenName.equals(rightFamilyName) && leftFamilyName.equals(rightGivenName);
				}
			}
		}

		return match ? 1.0 : 0.0;
	}
}
