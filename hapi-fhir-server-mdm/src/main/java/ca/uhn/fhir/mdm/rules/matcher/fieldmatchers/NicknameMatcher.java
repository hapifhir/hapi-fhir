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

import ca.uhn.fhir.jpa.nickname.INicknameSvc;
import ca.uhn.fhir.mdm.rules.json.MdmMatcherJson;
import ca.uhn.fhir.mdm.rules.matcher.models.IMdmFieldMatcher;
import ca.uhn.fhir.mdm.rules.matcher.util.StringMatcherUtils;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.util.Collection;
import java.util.Locale;

public class NicknameMatcher implements IMdmFieldMatcher {
	private final INicknameSvc myNicknameSvc;

	public NicknameMatcher(INicknameSvc theNicknameSvc) {
		myNicknameSvc = theNicknameSvc;
	}

	public boolean matches(String theLeftString, String theRightString) {
		String leftString = theLeftString.toLowerCase(Locale.ROOT);
		String rightString = theRightString.toLowerCase(Locale.ROOT);

		Collection<String> leftNames = myNicknameSvc.getEquivalentNames(leftString);
		if (leftNames.contains(rightString)) {
			return true;
		}

		Collection<String> rightNames = myNicknameSvc.getEquivalentNames(rightString);
		return rightNames.contains(leftString);
	}

	@Override
	public boolean matches(IBase theLeftBase, IBase theRightBase, MdmMatcherJson theParams) {
		if (theLeftBase instanceof IPrimitiveType && theRightBase instanceof IPrimitiveType) {
			String leftString = StringMatcherUtils.extractString((IPrimitiveType<?>) theLeftBase, theParams.getExact());
			String rightString =
					StringMatcherUtils.extractString((IPrimitiveType<?>) theRightBase, theParams.getExact());

			return matches(leftString, rightString);
		}
		return false;
	}
}
