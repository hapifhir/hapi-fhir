package ca.uhn.fhir.rest.server.util;

/*
 * #%L
 * HAPI FHIR - Server Framework
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

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.context.phonetic.IPhoneticEncoder;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.instance.model.api.IAnyResource;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

// TODO: JA remove default methods
public interface ISearchParamRegistry {

	/**
	 * @return Returns {@literal null} if no match
	 */
	RuntimeSearchParam getActiveSearchParam(String theResourceName, String theParamName);


	/**
	 * @return Returns all active search params for the given resource
	 */
	ResourceSearchParams getActiveSearchParams(String theResourceName);

	/**
	 * Request that the cache be refreshed now, in the current thread
	 */
	default void forceRefresh() {
	}

	/**
	 * Request that the cache be refreshed at the next convenient time (in a different thread)
	 */
	default void requestRefresh() {
	}

	/**
	 * When indexing a HumanName, if a StringEncoder is set in the context, then the "phonetic" search parameter will normalize
	 * the String using this encoder.
	 *
	 * @since 5.1.0
	 */
	default void setPhoneticEncoder(IPhoneticEncoder thePhoneticEncoder) {
	}

	default List<RuntimeSearchParam> getActiveComboSearchParams(String theResourceName) {
		return Collections.emptyList();
	}

	default List<RuntimeSearchParam> getActiveComboSearchParams(String theResourceName, Set<String> theParamNames) {
		return Collections.emptyList();
	}

	/**
	 * Returns a collection containing all of the valid active search parameters. This method is intended for
	 * creating error messages for users as opposed to actual search processing. It will include meta parameters
	 * such as <code>_id</code> and <code>_lastUpdated</code>.
	 */
	default Collection<String> getValidSearchParameterNamesIncludingMeta(String theResourceName) {
		TreeSet<String> retval;
		ResourceSearchParams activeSearchParams = getActiveSearchParams(theResourceName);
		if (activeSearchParams == null) {
			retval = new TreeSet<>();
		} else {
			retval = new TreeSet<>(activeSearchParams.getSearchParamNames());
		}
		retval.add(IAnyResource.SP_RES_ID);
		retval.add(Constants.PARAM_LASTUPDATED);
		return retval;
	}

	/**
	 * Fetch a SearchParameter by URL
	 *
	 * @return Returns <code>null</code> if it can't be found
	 */
	@Nullable
	RuntimeSearchParam getActiveSearchParamByUrl(String theUrl);

	/**
	 * Find a search param for a resource. First, check the resource itself, then check the top-level `Resource` resource.
	 *
	 * @param theResourceType the resource type.
	 * @param theParamName the search parameter name.
	 *
	 * @return the {@link RuntimeSearchParam} that is found.
	 */
	default RuntimeSearchParam getRuntimeSearchParam(String theResourceType, String theParamName) {
		RuntimeSearchParam availableSearchParamDef = getActiveSearchParam(theResourceType, theParamName);
		if (availableSearchParamDef == null) {
			availableSearchParamDef = getActiveSearchParam("Resource", theParamName);
		}
		if (availableSearchParamDef == null) {
			throw new InvalidRequestException(Msg.code(1209) + "Unknown parameter name: " + theResourceType + ':' + theParamName);
		}
		return availableSearchParamDef;
	}
}
