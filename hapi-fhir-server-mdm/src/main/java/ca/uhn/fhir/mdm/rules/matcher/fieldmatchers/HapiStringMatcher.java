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

import ca.uhn.fhir.mdm.rules.json.MdmMatcherJson;
import ca.uhn.fhir.mdm.rules.matcher.models.IMdmFieldMatcher;
import ca.uhn.fhir.mdm.rules.matcher.util.StringMatcherUtils;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

/**
 * Similarity measure for two IBase fields whose similarity can be measured by their String representations.
 */
public class HapiStringMatcher implements IMdmFieldMatcher {

	@Override
	public boolean matches(IBase theLeftBase, IBase theRightBase, MdmMatcherJson theExtraMatchParams) {
		if (theLeftBase instanceof IPrimitiveType && theRightBase instanceof IPrimitiveType) {
			String leftString =
					StringMatcherUtils.extractString((IPrimitiveType<?>) theLeftBase, theExtraMatchParams.getExact());
			String rightString =
					StringMatcherUtils.extractString((IPrimitiveType<?>) theRightBase, theExtraMatchParams.getExact());

			return leftString.equals(rightString);
		}
		return false;
	}
}
