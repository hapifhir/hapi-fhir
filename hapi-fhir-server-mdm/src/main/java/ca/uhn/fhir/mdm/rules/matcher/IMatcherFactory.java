/*-
 * #%L
 * HAPI FHIR - Master Data Management
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.mdm.rules.matcher;

import ca.uhn.fhir.mdm.rules.matcher.models.IMdmFieldMatcher;
import ca.uhn.fhir.mdm.rules.matcher.models.MatchTypeEnum;

import java.util.Set;

/**
 * Factory interface for retrieving and registering field matchers by name.
 * Implementations pre-populate built-in matchers from {@link MatchTypeEnum}
 * and allow registration of custom matchers at runtime.
 */
public interface IMatcherFactory {

	/**
	 * Retrieves the field matcher for the given MatchTypeEnum.
	 *
	 * @deprecated Use {@link #getFieldMatcherForName(String)} instead
	 */
	@Deprecated
	IMdmFieldMatcher getFieldMatcherForMatchType(MatchTypeEnum theMdmMatcherEnum);

	/**
	 * Retrieves the field matcher registered under the given name.
	 *
	 * @param theName the algorithm name (e.g. "STRING", "DATE", or a custom name)
	 * @return the matcher instance, or {@code null} if no matcher is registered under that name
	 */
	default IMdmFieldMatcher getFieldMatcherForName(String theName) {
		try {
			MatchTypeEnum matchType = MatchTypeEnum.valueOf(theName);
			return getFieldMatcherForMatchType(matchType);
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

	/**
	 * Registers a custom field matcher under the given name.
	 *
	 * @param theName the algorithm name
	 * @param theMatcher the matcher implementation
	 * @throws IllegalArgumentException if a matcher is already registered under that name
	 */
	default void register(String theName, IMdmFieldMatcher theMatcher) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return the set of all registered algorithm names
	 */
	default Set<String> getRegisteredNames() {
		throw new UnsupportedOperationException();
	}
}
