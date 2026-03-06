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
	 * The default implementation attempts to resolve the name as a built-in
	 * {@link MatchTypeEnum} and delegates to the deprecated
	 * {@link #getFieldMatcherForMatchType(MatchTypeEnum)} for backward compatibility
	 * with implementations that only override the enum-based method. For names that
	 * do not correspond to a built-in enum constant, the default throws
	 * {@link UnsupportedOperationException}.
	 * <p>
	 * Implementations that support custom (non-enum) algorithm names must override
	 * this method.
	 *
	 * @param theName the matcher name (e.g. "STRING", "DATE")
	 */
	default IMdmFieldMatcher getFieldMatcherForName(String theName) {
		try {
			MatchTypeEnum matchType = MatchTypeEnum.valueOf(theName);
			return getFieldMatcherForMatchType(matchType);
		} catch (IllegalArgumentException e) {
			throw new UnsupportedOperationException(
					Msg.code(2848) + "IMatcherFactory does not implement getFieldMatcherForName(String). "
							+ "Override this method to support string-based matcher lookup.");
		}
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
	 * Unregisters the field matcher with the given name.
	 * Built-in matchers cannot be unregistered.
	 *
	 * @param theName the matcher name to unregister
	 */
	default void unregister(String theName) {
		throw new UnsupportedOperationException(
				Msg.code(2853) + "This IMatcherFactory does not support unregistration");
	}

	/**
	 * @return the set of all registered matcher names
	 */
	default Set<String> getRegisteredNames() {
		return Collections.emptySet();
	}

	/**
	 * Retrieves the field matcher for the given {@link MatchTypeEnum}.
	 * <p>
	 * The default implementation throws {@link UnsupportedOperationException}.
	 * Legacy implementations that override this method are still reachable from
	 * the default {@link #getFieldMatcherForName(String)}, which delegates here
	 * for built-in enum names.
	 *
	 * @deprecated Use {@link #getFieldMatcherForName(String)} instead.
	 */
	@Deprecated(since = "8_10_0", forRemoval = true)
	default IMdmFieldMatcher getFieldMatcherForMatchType(MatchTypeEnum theMatchType) {
		throw new UnsupportedOperationException(
				Msg.code(2849) + "getFieldMatcherForMatchType is deprecated. Use getFieldMatcherForName instead.");
	}
}
