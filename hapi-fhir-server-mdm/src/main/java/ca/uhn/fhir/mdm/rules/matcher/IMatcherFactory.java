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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.mdm.rules.matcher.models.IMdmFieldMatcher;
import ca.uhn.fhir.mdm.rules.matcher.models.MatchTypeEnum;

import java.util.Collections;
import java.util.Set;

/**
 * Factory interface for retrieving and registering field matchers by name.
 */
public interface IMatcherFactory {

	/**
	 * Retrieves the field matcher registered under the given name.
	 * <p>
	 * The default implementation delegates to the deprecated
	 * {@link #getFieldMatcherForMatchType(MatchTypeEnum)} for backward compatibility
	 * with implementations that only override the enum-based method.
	 *
	 * @param theName the matcher name (e.g. "STRING", "DATE")
	 */
	default IMdmFieldMatcher getFieldMatcherForName(String theName) {
		return getFieldMatcherForMatchType(MatchTypeEnum.valueOf(theName));
	}

	/**
	 * Registers a field matcher under the given name.
	 *
	 * @param theName the matcher name
	 * @param theMatcher the matcher implementation
	 */
	default void register(String theName, IMdmFieldMatcher theMatcher) {
		throw new UnsupportedOperationException(Msg.code(2852) + "This IMatcherFactory does not support registration");
	}

	/**
	 * @return the set of all registered matcher names
	 */
	default Set<String> getRegisteredNames() {
		return Collections.emptySet();
	}

	/**
	 * Retrieves the field matcher for the given {@link MatchTypeEnum}.
	 *
	 * @deprecated Use {@link #getFieldMatcherForName(String)} instead.
	 */
	@Deprecated(since = "8.10", forRemoval = true)
	default IMdmFieldMatcher getFieldMatcherForMatchType(MatchTypeEnum theMatchType) {
		return getFieldMatcherForName(theMatchType.name());
	}
}
