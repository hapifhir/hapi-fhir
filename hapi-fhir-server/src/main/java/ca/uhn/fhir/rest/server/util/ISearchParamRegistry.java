package ca.uhn.fhir.rest.server.util;

/*
 * #%L
 * HAPI FHIR Search Parameters
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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

import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.context.phonetic.IPhoneticEncoder;

import java.util.Collection;
import java.util.Map;

// TODO: JA remove default methods
public interface ISearchParamRegistry {

	/**
	 * @return Returns {@literal null} if no match
	 */
	RuntimeSearchParam getActiveSearchParam(String theResourceName, String theParamName);

	/**
	 * @return Returns all active search params for the given resource
	 */
	Map<String, RuntimeSearchParam> getActiveSearchParams(String theResourceName);

	/**
	 * Request that the cache be refreshed now, in the current thread
	 */
	default void forceRefresh() {
	}

	;

	/**
	 * Request that the cache be refreshed at the next convenient time (in a different thread)
	 */
	default void requestRefresh() {
	}



	default RuntimeSearchParam getSearchParamByName(RuntimeResourceDefinition theResourceDef, String theParamName) {
		throw new UnsupportedOperationException();
	}

	/**
	 * When indexing a HumanName, if a StringEncoder is set in the context, then the "phonetic" search parameter will normalize
	 * the String using this encoder.
	 *
	 * @since 5.1.0
	 */
	default void setPhoneticEncoder(IPhoneticEncoder thePhoneticEncoder) {
	}

	;

	/**
	 * Returns a collection containing all of the valid active search parameters. This method is intended for
	 * creating error messages for users as opposed to actual search processing. It will include meta parameters
	 * such as <code>_id</code> and <code>_lastUpdated</code>.
	 */
	default Collection<String> getValidSearchParameterNamesIncludingMeta(String theResourceName) {
//		return getActiveSearchParams().getValidSearchParameterNamesIncludingMeta(theResourceName);
		throw new UnsupportedOperationException();
	}

}
