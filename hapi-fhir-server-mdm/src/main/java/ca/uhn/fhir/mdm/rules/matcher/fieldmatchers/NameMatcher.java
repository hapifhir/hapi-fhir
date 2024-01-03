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
import ca.uhn.fhir.mdm.util.NameUtil;
import ca.uhn.fhir.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBase;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Similarity measure for two IBase name fields
 */
public class NameMatcher implements IMdmFieldMatcher {

	private final MdmNameMatchModeEnum myMatchMode;

	private final FhirContext myFhirContext;

	public NameMatcher(FhirContext theFhirContext, MdmNameMatchModeEnum theMatchMode) {
		myMatchMode = theMatchMode;
		myFhirContext = theFhirContext;
	}

	@Override
	public boolean matches(IBase theLeftBase, IBase theRightBase, MdmMatcherJson theParams) {
		String leftFamilyName = NameUtil.extractFamilyName(myFhirContext, theLeftBase);
		String rightFamilyName = NameUtil.extractFamilyName(myFhirContext, theRightBase);
		if (StringUtils.isEmpty(leftFamilyName) || StringUtils.isEmpty(rightFamilyName)) {
			return false;
		}

		boolean match = false;

		List<String> leftGivenNames = NameUtil.extractGivenNames(myFhirContext, theLeftBase);
		List<String> rightGivenNames = NameUtil.extractGivenNames(myFhirContext, theRightBase);

		if (!theParams.getExact()) {
			leftFamilyName = StringUtil.normalizeStringForSearchIndexing(leftFamilyName);
			rightFamilyName = StringUtil.normalizeStringForSearchIndexing(rightFamilyName);
			leftGivenNames = leftGivenNames.stream()
					.map(StringUtil::normalizeStringForSearchIndexing)
					.collect(Collectors.toList());
			rightGivenNames = rightGivenNames.stream()
					.map(StringUtil::normalizeStringForSearchIndexing)
					.collect(Collectors.toList());
		}

		for (String leftGivenName : leftGivenNames) {
			for (String rightGivenName : rightGivenNames) {
				match |= leftGivenName.equals(rightGivenName) && leftFamilyName.equals(rightFamilyName);
				if (myMatchMode == MdmNameMatchModeEnum.ANY_ORDER) {
					match |= leftGivenName.equals(rightFamilyName) && leftFamilyName.equals(rightGivenName);
				}
			}
		}

		return match;
	}
}
