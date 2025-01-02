/*
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.rest.server.util;

import ca.uhn.fhir.context.ComboSearchParamType;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.context.phonetic.IPhoneticEncoder;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

// TODO: JA remove default methods
public interface ISearchParamRegistry {

	/**
	 * Return true if this registry is initialized and ready to handle
	 * searches and use its cache.
	 * Return false if cache has not been initialized.
	 */
	default boolean isInitialized() {
		// default initialized to not break current implementers
		return true;
	}

	/**
	 * @deprecated Use {@link #getActiveSearchParam(String, String, SearchParamLookupContextEnum)}
	 */
	@Deprecated(since = "8.0.0", forRemoval = true)
	default RuntimeSearchParam getActiveSearchParam(String theResourceName, String theParamName) {
		return getActiveSearchParam(theResourceName, theParamName, SearchParamLookupContextEnum.ALL);
	}

	/**
	 * @param theContext The context to return active search params for, or {@literal null} to return any active search params
	 * @return Returns {@literal null} if no match
	 */
	RuntimeSearchParam getActiveSearchParam(
			@Nonnull String theResourceName,
			@Nonnull String theParamName,
			@Nonnull SearchParamLookupContextEnum theContext);

	/**
	 * @deprecated Use {@link #getActiveSearchParam(String, String, SearchParamLookupContextEnum)}
	 */
	@Deprecated(since = "8.0.0", forRemoval = true)
	default ResourceSearchParams getActiveSearchParams(String theResourceName) {
		return getActiveSearchParams(theResourceName, SearchParamLookupContextEnum.ALL);
	}

	/**
	 * @param theContext The context to return active search params for, or {@literal null} to return any active search params
	 * @return Returns all active search params for the given resource
	 */
	ResourceSearchParams getActiveSearchParams(
			@Nonnull String theResourceName, @Nonnull SearchParamLookupContextEnum theContext);

	/**
	 * Request that the cache be refreshed now, in the current thread
	 */
	default void forceRefresh() {}

	/**
	 * Request that the cache be refreshed at the next convenient time (in a different thread)
	 */
	default void requestRefresh() {}

	/**
	 * When indexing a HumanName, if a StringEncoder is set in the context, then the "phonetic" search parameter will normalize
	 * the String using this encoder.
	 *
	 * @since 5.1.0
	 */
	default void setPhoneticEncoder(IPhoneticEncoder thePhoneticEncoder) {}

	/**
	 * @param theContext The context to return active search params for, or {@literal null} to return any active search params
	 */
	default List<RuntimeSearchParam> getActiveComboSearchParams(
			@Nonnull String theResourceName, @Nonnull SearchParamLookupContextEnum theContext) {
		return Collections.emptyList();
	}

	// TODO ND remove default implementation
	default List<RuntimeSearchParam> getActiveComboSearchParams(
			@Nonnull String theResourceName,
			@Nonnull ComboSearchParamType theParamType,
			@Nonnull SearchParamLookupContextEnum theContext) {
		return Collections.emptyList();
	}

	// TODO ND remove default implementation
	default Optional<RuntimeSearchParam> getActiveComboSearchParamById(
			@Nonnull String theResourceName, @Nonnull IIdType theId) {
		return Optional.empty();
	}

	/**
	 * @param theContext The context to return active search params for, or {@literal null} to return any active search params
	 */
	default List<RuntimeSearchParam> getActiveComboSearchParams(
			@Nonnull String theResourceName,
			@Nonnull Set<String> theParamNames,
			@Nonnull SearchParamLookupContextEnum theContext) {
		return Collections.emptyList();
	}

	/**
	 * Returns a collection containing all of the valid active search parameters. This method is intended for
	 * creating error messages for users as opposed to actual search processing. It will include meta parameters
	 * such as <code>_id</code> and <code>_lastUpdated</code>.
	 *
	 * @param theContext The context to return active search params for, or {@literal null} to return any active search params
	 */
	default Collection<String> getValidSearchParameterNamesIncludingMeta(
			@Nonnull String theResourceName, @Nonnull SearchParamLookupContextEnum theContext) {
		TreeSet<String> retval;
		ResourceSearchParams activeSearchParams = getActiveSearchParams(theResourceName, theContext);
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
	 * @param theContext The context to return active search params for, or {@literal null} to return any active search params
	 * @return Returns <code>null</code> if it can't be found
	 */
	@Nullable
	RuntimeSearchParam getActiveSearchParamByUrl(
			@Nonnull String theUrl, @Nonnull SearchParamLookupContextEnum theContext);

	/**
	 * Find a search param for a resource. First, check the resource itself, then check the top-level `Resource` resource.
	 *
	 * @param theResourceType the resource type.
	 * @param theParamName the search parameter name.
	 * @param theContext The context to return active search params for, or {@literal null} to return any active search params
	 * @return the {@link RuntimeSearchParam} that is found.
	 */
	default RuntimeSearchParam getRuntimeSearchParam(
			@Nonnull String theResourceType,
			@Nonnull String theParamName,
			@Nonnull SearchParamLookupContextEnum theContext) {
		RuntimeSearchParam availableSearchParamDef = getActiveSearchParam(theResourceType, theParamName, theContext);
		if (availableSearchParamDef == null) {
			availableSearchParamDef = getActiveSearchParam("Resource", theParamName, theContext);
		}
		if (availableSearchParamDef == null) {
			throw new InvalidRequestException(
					Msg.code(1209) + "Unknown parameter name: " + theResourceType + ':' + theParamName);
		}
		return availableSearchParamDef;
	}

	/**
	 * Get all the search params for a resource. First, check the resource itself, then check the top-level `Resource` resource and combine the two.
	 *
	 * @param theContext The context to return active search params for, or {@literal null} to return any active search params
	 * @param theResourceType the resource type.
	 * @return the {@link ResourceSearchParams} that has all the search params.
	 */
	default ResourceSearchParams getRuntimeSearchParams(
			@Nonnull String theResourceType, @Nonnull SearchParamLookupContextEnum theContext) {
		ResourceSearchParams availableSearchParams =
				getActiveSearchParams(theResourceType, theContext).makeCopy();
		ResourceSearchParams resourceSearchParams = getActiveSearchParams("Resource", theContext);
		resourceSearchParams
				.getSearchParamNames()
				.forEach(param -> availableSearchParams.addSearchParamIfAbsent(param, resourceSearchParams.get(param)));
		return availableSearchParams;
	}

	/**
	 * Describes the context for looking up individual search parameters or lists of search parameters.
	 * These can be thought of as filter criteria - Most search parameters generally apply to all
	 * context, but some may be explicitly defined to only work for some.
	 *
	 * @since 8.0.0
	 */
	enum SearchParamLookupContextEnum {
		/**
		 * Search parameter should be used when indexing a resource that is being persisted
		 */
		INDEX,
		/**
		 * Search parameter should be used for searching. This includes explicit searches such as
		 * standard REST FHIR searches, but also includes resolving match URLs, subscription criteria,
		 * etc.
		 */
		SEARCH,
		/**
		 * Search parameter should be used for sorting via the {@literal _sort} parameter.
		 */
		SORT,
		/**
		 * Return any search parameters that are known to the system for any context
		 */
		ALL
	}

	static boolean isAllowedForContext(
			@Nonnull RuntimeSearchParam theSearchParam, @Nullable SearchParamLookupContextEnum theContext) {
		/*
		 * I'm thinking that a future enhancement might be to allow a SearchParameter to declare that it
		 * is supported for searching or for sorting or for both - But for now these are one and the same.
		 */
		if (theContext == SearchParamLookupContextEnum.SEARCH || theContext == SearchParamLookupContextEnum.SORT) {
			return theSearchParam.isEnabledForSearching();
		}
		return true;
	}
}
