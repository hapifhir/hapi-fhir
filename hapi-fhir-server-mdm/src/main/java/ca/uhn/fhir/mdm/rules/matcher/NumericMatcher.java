package ca.uhn.fhir.mdm.rules.matcher;

/*-
 * #%L
 * HAPI FHIR - Master Data Management
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

import ca.uhn.fhir.context.phonetic.NumericEncoder;

// Useful for numerical identifiers like phone numbers, address parts etc.
// This should not be used where decimals are important.  A new "quantity matcher" should be added to handle cases like that.
public class NumericMatcher implements IMdmStringMatcher {
	private final NumericEncoder encoder = new NumericEncoder();

	@Override
	public boolean matches(String theLeftString, String theRightString) {
		String left = encoder.encode(theLeftString);
		String right = encoder.encode(theRightString);
		return left.equals(right);
	}
}
