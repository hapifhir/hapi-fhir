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
package ca.uhn.fhir.mdm.rules.matcher.models;

import ca.uhn.fhir.mdm.rules.json.MdmMatcherJson;
import org.hl7.fhir.instance.model.api.IBase;

/**
 * Measure how similar two IBase (resource fields) are to one another.  1.0 means identical.  0.0 means completely different.
 */
public interface IMdmFieldMatcher {
	/**
	 * Checks if theLeftBase and theRightBase match, returning true if they do
	 * and false otherwise.
	 */
	boolean matches(IBase theLeftBase, IBase theRightBase, MdmMatcherJson theParams);

	/**
	 * True if matcher can/will match empty (null) fields,
	 * false otherwise.
	 */
	default boolean isMatchingEmptyFields() {
		// false because most people are overriding this
		return false;
	}
}
