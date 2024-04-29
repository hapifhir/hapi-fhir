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
import ca.uhn.fhir.mdm.util.IdentifierUtil;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.util.CanonicalIdentifier;
import org.hl7.fhir.instance.model.api.IBase;

public class IdentifierMatcher implements IMdmFieldMatcher {

	private boolean isEmpty(StringDt theValue) {
		if (theValue == null) {
			return true;
		}
		return theValue.isEmpty();
	}

	/**
	 * @return true if the two fhir identifiers are the same.  If @param theIdentifierSystem is not null, then the
	 * matcher only returns true if the identifier systems also match this system.
	 * @throws UnsupportedOperationException if either Base is not an Identifier instance
	 */
	@Override
	public boolean matches(IBase theLeftBase, IBase theRightBase, MdmMatcherJson theParams) {
		CanonicalIdentifier left = IdentifierUtil.identifierDtFromIdentifier(theLeftBase);
		if (theParams.getIdentifierSystem() != null) {
			if (!theParams.getIdentifierSystem().equals(left.getSystemElement().getValueAsString())) {
				return false;
			}
		}
		CanonicalIdentifier right = IdentifierUtil.identifierDtFromIdentifier(theRightBase);
		if (isEmpty(left.getValueElement()) || isEmpty(right.getValueElement())) {
			return false;
		}
		return left.equals(right);
	}
}
