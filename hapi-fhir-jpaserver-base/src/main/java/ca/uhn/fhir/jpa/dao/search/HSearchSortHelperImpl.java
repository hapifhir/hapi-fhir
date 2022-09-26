package ca.uhn.fhir.jpa.dao.search;

/*-
 * #%L
 * HAPI FHIR JPA Server
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
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.rest.server.util.ResourceSearchParams;
import com.google.common.annotations.VisibleForTesting;
import org.hibernate.search.engine.search.sort.dsl.SearchSortFactory;
import org.hibernate.search.engine.search.sort.dsl.SortFinalStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.model.search.HSearchIndexWriter.IDX_STRING_LOWER;
import static ca.uhn.fhir.jpa.model.search.HSearchIndexWriter.NESTED_SEARCH_PARAM_ROOT;
import static ca.uhn.fhir.jpa.model.search.HSearchIndexWriter.NUMBER_VALUE;
import static ca.uhn.fhir.jpa.model.search.HSearchIndexWriter.INDEX_TYPE_QUANTITY;
import static ca.uhn.fhir.jpa.model.search.HSearchIndexWriter.QTY_VALUE;
import static ca.uhn.fhir.jpa.model.search.HSearchIndexWriter.QTY_VALUE_NORM;
import static ca.uhn.fhir.jpa.model.search.HSearchIndexWriter.SEARCH_PARAM_ROOT;
import static ca.uhn.fhir.jpa.model.search.HSearchIndexWriter.URI_VALUE;

/**
 * Used to build HSearch sort clauses.
 */
public class HSearchSortHelperImpl implements IHSearchSortHelper {
	private static final Logger ourLog = LoggerFactory.getLogger(HSearchSortHelperImpl.class);

	/** Indicates which HSearch properties must be sorted for each RestSearchParameterTypeEnum **/
	private Map<RestSearchParameterTypeEnum, List<String>> mySortPropertyListMap = Map.of(
		RestSearchParameterTypeEnum.STRING, 	List.of(SEARCH_PARAM_ROOT + ".*.string." + IDX_STRING_LOWER),
		RestSearchParameterTypeEnum.TOKEN, 		List.of(
			String.join(".", NESTED_SEARCH_PARAM_ROOT, "*", "token", "system"),
			String.join(".", NESTED_SEARCH_PARAM_ROOT, "*", "token", "code") ),
		RestSearchParameterTypeEnum.REFERENCE, List.of(SEARCH_PARAM_ROOT + ".*.reference.value"),
		RestSearchParameterTypeEnum.DATE, 		List.of(SEARCH_PARAM_ROOT + ".*.dt.lower"),
		RestSearchParameterTypeEnum.QUANTITY, 	List.of(
			String.join(".", NESTED_SEARCH_PARAM_ROOT, "*", INDEX_TYPE_QUANTITY, QTY_VALUE_NORM),
			String.join(".", NESTED_SEARCH_PARAM_ROOT, "*", INDEX_TYPE_QUANTITY, QTY_VALUE) ),
		RestSearchParameterTypeEnum.URI, 		List.of(SEARCH_PARAM_ROOT + ".*." + URI_VALUE),
		RestSearchParameterTypeEnum.NUMBER, 	List.of(SEARCH_PARAM_ROOT + ".*." + NUMBER_VALUE)
	);

	private final ISearchParamRegistry mySearchParamRegistry;

	public HSearchSortHelperImpl(ISearchParamRegistry theSearchParamRegistry) {
		mySearchParamRegistry = theSearchParamRegistry;
	}

	/**
	 * Builds and returns sort clauses for received sort parameters
	 */
	@Override
	public SortFinalStep getSortClauses(SearchSortFactory theSortFactory, SortSpec theSortParams, String theResourceType) {
		var sortStep = theSortFactory.composite();
		Optional<SortFinalStep> sortClauseOpt = getSortClause(theSortFactory, theSortParams, theResourceType);
		sortClauseOpt.ifPresent(sortStep::add);

		SortSpec nextParam = theSortParams.getChain();
		while( nextParam != null ) {
			sortClauseOpt = getSortClause(theSortFactory, nextParam, theResourceType);
			sortClauseOpt.ifPresent(sortStep::add);

			nextParam = nextParam.getChain();
		}

		return sortStep;
	}


	/**
	 * Builds sort clauses for the received SortSpec by
	 *  _ finding out the corresponding RestSearchParameterTypeEnum for the parameter
	 *  _ obtaining the list of properties to sort for the found parameter type
	 *  _ building the sort clauses for the found list of properties
	 */
	@VisibleForTesting
	Optional<SortFinalStep> getSortClause(SearchSortFactory theF, SortSpec theSortSpec, String theResourceType) {
		Optional<RestSearchParameterTypeEnum> paramTypeOpt = getParamType(theResourceType, theSortSpec.getParamName());
		if (paramTypeOpt.isEmpty()) {
			ourLog.warn("Sprt parameter type couldn't be determined for parameter: " + theSortSpec.getParamName() +
				". Result will not be properly sorted");
			return Optional.empty();
		}
		List<String> paramFieldNameList = getSortPropertyList(paramTypeOpt.get(), theSortSpec.getParamName());
		if (paramFieldNameList.isEmpty()) {
			ourLog.warn("Unable to sort by parameter '" + theSortSpec.getParamName() + "'. Sort parameter ignored.");
			return Optional.empty();
		}

		var sortFinalStep = theF.composite();
		for (String fieldName : paramFieldNameList) {
			var sortStep = theF.field(fieldName);

			if (theSortSpec.getOrder().equals(SortOrderEnum.DESC)) {
				sortStep.desc();
			} else {
				sortStep.asc();
			}

			// field could have no value
			sortFinalStep.add( sortStep.missing().last() );
		}

		return Optional.of(sortFinalStep);
	}


	/**
	 * Finds out and returns the parameter type for each parameter name
	 */
	@VisibleForTesting
	Optional<RestSearchParameterTypeEnum> getParamType(String theResourceTypeName, String theParamName) {
		ResourceSearchParams activeSearchParams = mySearchParamRegistry.getActiveSearchParams(theResourceTypeName);
		RuntimeSearchParam searchParam = activeSearchParams.get(theParamName);
		if (searchParam == null) {
			return Optional.empty();
		}

		return Optional.of(searchParam.getParamType());
	}


	/**
	 * Retrieves the generic property names (* instead of parameter name) from the configured map and
	 * replaces the '*' segment by theParamName before returning the final property name list
	 */
	@VisibleForTesting
	List<String> getSortPropertyList(RestSearchParameterTypeEnum theParamType, String theParamName) {
		List<String> paramFieldNameList = mySortPropertyListMap.get(theParamType);
		// replace '*' names segment by theParamName
		return paramFieldNameList.stream().map(s -> s.replace("*", theParamName)).collect(Collectors.toList());
	}





}
