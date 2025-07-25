/*-
 * #%L
 * HAPI FHIR JPA - Search Parameters
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
package ca.uhn.fhir.jpa.searchparam;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.searchparam.util.JpaParamUtil;
import ca.uhn.fhir.model.api.IQueryParameterAnd;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.QualifiedParamList;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.api.SearchIncludeDeletedEnum;
import ca.uhn.fhir.rest.api.SearchTotalModeEnum;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.ParameterUtil;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.rest.server.util.MatchUrlUtil;
import ca.uhn.fhir.util.ReflectionUtil;
import ca.uhn.fhir.util.UrlUtil;
import com.google.common.collect.ArrayListMultimap;
import org.apache.http.NameValuePair;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ca.uhn.fhir.jpa.searchparam.ResourceMetaParams.STRICT_RESOURCE_META_PARAMS;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class MatchUrlService {

	public static final Set<String> COMPATIBLE_PARAMS_NO_RES_TYPE =
			Set.of(Constants.PARAM_INCLUDE_DELETED, Constants.PARAM_LASTUPDATED);
	public static final Set<String> COMPATIBLE_PARAMS_GIVEN_RES_TYPE =
			Set.of(Constants.PARAM_INCLUDE_DELETED, Constants.PARAM_LASTUPDATED, Constants.PARAM_ID);

	@Autowired
	private FhirContext myFhirContext;

	@Autowired
	private ISearchParamRegistry mySearchParamRegistry;

	public MatchUrlService() {
		super();
	}

	public SearchParameterMap translateMatchUrl(
			String theMatchUrl, RuntimeResourceDefinition theResourceDefinition, Flag... theFlags) {
		SearchParameterMap paramMap = new SearchParameterMap();
		List<NameValuePair> parameters = MatchUrlUtil.translateMatchUrl(theMatchUrl);

		ArrayListMultimap<String, QualifiedParamList> nameToParamLists = ArrayListMultimap.create();
		for (NameValuePair next : parameters) {
			if (isBlank(next.getValue())) {
				continue;
			}

			String paramName = next.getName();
			String qualifier = null;
			for (int i = 0; i < paramName.length(); i++) {
				switch (paramName.charAt(i)) {
					case '.':
					case ':':
						qualifier = paramName.substring(i);
						paramName = paramName.substring(0, i);
						i = Integer.MAX_VALUE - 1;
						break;
				}
			}

			QualifiedParamList paramList =
					QualifiedParamList.splitQueryStringByCommasIgnoreEscape(qualifier, next.getValue());
			nameToParamLists.put(paramName, paramList);
		}

		boolean hasNoResourceType = hasNoResourceTypeInUrl(theMatchUrl, theResourceDefinition);

		if (hasNoResourceType && !isSupportedQueryForNoProvidedResourceType(nameToParamLists.keySet())) {
			// Of all the general FHIR search parameters: https://hl7.org/fhir/R4/search.html#table
			// We can _only_ process the parameters on resource.meta fields for server requests
			// The following require a provided resource type because:
			// - Both _text and _content requires the FullTextSearchSvc and can only be performed on DomainResources
			// - _id since it is part of the unique constraint in the DB (see ResourceTableDao)
			// - Both _list and _has allows complex chaining with other resource-specific search params
			String errorMsg = myFhirContext.getLocalizer().getMessage(MatchUrlService.class, "noResourceType");
			throw new IllegalArgumentException(Msg.code(2742) + errorMsg);
		}

		for (String nextParamName : nameToParamLists.keySet()) {
			List<QualifiedParamList> paramList = nameToParamLists.get(nextParamName);

			if (theFlags != null) {
				for (Flag next : theFlags) {
					next.process(nextParamName, paramList, paramMap);
				}
			}

			if (Constants.PARAM_INCLUDE_DELETED.equals(nextParamName)) {
				validateParamsAreCompatibleForDeleteOrThrow(nameToParamLists.keySet(), hasNoResourceType);
				paramMap.setSearchIncludeDeletedMode(
						SearchIncludeDeletedEnum.fromCode(paramList.get(0).get(0)));
			} else if (Constants.PARAM_LASTUPDATED.equals(nextParamName)) {
				if (!paramList.isEmpty()) {
					if (paramList.size() > 2) {
						throw new InvalidRequestException(Msg.code(484) + "Failed to parse match URL[" + theMatchUrl
								+ "] - Can not have more than 2 " + Constants.PARAM_LASTUPDATED
								+ " parameter repetitions");
					} else {
						DateRangeParam p1 = new DateRangeParam();
						p1.setValuesAsQueryTokens(myFhirContext, nextParamName, paramList);
						paramMap.setLastUpdated(p1);
					}
				}
			} else if (Constants.PARAM_HAS.equals(nextParamName)) {
				IQueryParameterAnd<?> param = JpaParamUtil.parseQueryParams(
						myFhirContext, RestSearchParameterTypeEnum.HAS, nextParamName, paramList);
				paramMap.add(nextParamName, param);
			} else if (Constants.PARAM_COUNT.equals(nextParamName)) {
				if (!paramList.isEmpty() && !paramList.get(0).isEmpty()) {
					String intString = paramList.get(0).get(0);
					try {
						paramMap.setCount(Integer.parseInt(intString));
					} catch (NumberFormatException e) {
						throw new InvalidRequestException(
								Msg.code(485) + "Invalid " + Constants.PARAM_COUNT + " value: " + intString);
					}
				}
			} else if (Constants.PARAM_SEARCH_TOTAL_MODE.equals(nextParamName)) {
				if (!paramList.isEmpty() && !paramList.get(0).isEmpty()) {
					String totalModeEnumStr = paramList.get(0).get(0);
					SearchTotalModeEnum searchTotalMode = SearchTotalModeEnum.fromCode(totalModeEnumStr);
					if (searchTotalMode == null) {
						// We had an oops here supporting the UPPER CASE enum instead of the FHIR code for _total.
						// Keep supporting it in case someone is using it.
						try {
							searchTotalMode = SearchTotalModeEnum.valueOf(totalModeEnumStr);
						} catch (IllegalArgumentException e) {
							throw new InvalidRequestException(Msg.code(2078) + "Invalid "
									+ Constants.PARAM_SEARCH_TOTAL_MODE + " value: " + totalModeEnumStr);
						}
					}
					paramMap.setSearchTotalMode(searchTotalMode);
				}
			} else if (Constants.PARAM_OFFSET.equals(nextParamName)) {
				if (!paramList.isEmpty() && !paramList.get(0).isEmpty()) {
					String intString = paramList.get(0).get(0);
					try {
						paramMap.setOffset(Integer.parseInt(intString));
					} catch (NumberFormatException e) {
						throw new InvalidRequestException(
								Msg.code(486) + "Invalid " + Constants.PARAM_OFFSET + " value: " + intString);
					}
				}
			} else if (ResourceMetaParams.RESOURCE_META_PARAMS.containsKey(nextParamName)) {
				if (isNotBlank(paramList.get(0).getQualifier())
						&& paramList.get(0).getQualifier().startsWith(".")) {
					throw new InvalidRequestException(Msg.code(487) + "Invalid parameter chain: " + nextParamName
							+ paramList.get(0).getQualifier());
				}
				IQueryParameterAnd<?> type = newInstanceAnd(nextParamName);
				type.setValuesAsQueryTokens(myFhirContext, nextParamName, (paramList));
				paramMap.add(nextParamName, type);
			} else if (Constants.PARAM_SOURCE.equals(nextParamName)) {
				IQueryParameterAnd<?> param = JpaParamUtil.parseQueryParams(
						myFhirContext, RestSearchParameterTypeEnum.URI, nextParamName, paramList);
				paramMap.add(nextParamName, param);
			} else if (JpaConstants.PARAM_DELETE_EXPUNGE.equals(nextParamName)) {
				paramMap.setDeleteExpunge(true);
			} else if (Constants.PARAM_LIST.equals(nextParamName)) {
				IQueryParameterAnd<?> param = JpaParamUtil.parseQueryParams(
						myFhirContext, RestSearchParameterTypeEnum.TOKEN, nextParamName, paramList);
				paramMap.add(nextParamName, param);
			} else if (nextParamName.startsWith("_") && !Constants.PARAM_LANGUAGE.equals(nextParamName)) {
				// ignore these since they aren't search params (e.g. _sort)
			} else {
				if (hasNoResourceType) {
					// It is a resource specific search parameter being done on the server
					throw new InvalidRequestException(Msg.code(2743) + "Failed to parse match URL [" + theMatchUrl
							+ "] - Unknown search parameter " + nextParamName + " for operation on server base.");
				}

				RuntimeSearchParam paramDef = mySearchParamRegistry.getActiveSearchParam(
						theResourceDefinition.getName(),
						nextParamName,
						ISearchParamRegistry.SearchParamLookupContextEnum.SEARCH);
				if (paramDef == null) {
					throw throwUnrecognizedParamException(theMatchUrl, theResourceDefinition, nextParamName);
				}

				IQueryParameterAnd<?> param = JpaParamUtil.parseQueryParams(
						mySearchParamRegistry, myFhirContext, paramDef, nextParamName, paramList);
				paramMap.add(nextParamName, param);
			}
		}
		return paramMap;
	}

	private static boolean isSupportedQueryForNoProvidedResourceType(Set<String> theParamNames) {
		if (theParamNames == null || theParamNames.isEmpty()) {
			// Query with no resource type in URL (ie. `[server base]?`)
			return false;
		}
		Set<String> acceptableServerParams = new HashSet<>(STRICT_RESOURCE_META_PARAMS);
		acceptableServerParams.add(Constants.PARAM_INCLUDE_DELETED);
		return acceptableServerParams.containsAll(theParamNames);
	}

	private static boolean hasNoResourceTypeInUrl(String theMatchUrl, RuntimeResourceDefinition theResourceDefinition) {
		return theResourceDefinition == null && theMatchUrl.indexOf('?') == 0;
	}

	/**
	 * The _includeDeleted parameter should only be supported with _lastUpdated, and _id iff resource type is given
	 * This is because these are the common search parameter values that are still stored on the deleted resource version in DB
	 * However, since resources are unique by type and id, only _lastUpdated is supported if no resource type is given
	 * @param theParamsToCheck the list of parameters found in the URL
	 * @param theHasNoResourceType whether the request is on the base URL (ie `?_param` - without resource type)
	 */
	private static void validateParamsAreCompatibleForDeleteOrThrow(
			Set<String> theParamsToCheck, boolean theHasNoResourceType) {
		Set<String> compatibleParams =
				theHasNoResourceType ? COMPATIBLE_PARAMS_NO_RES_TYPE : COMPATIBLE_PARAMS_GIVEN_RES_TYPE;

		if (!compatibleParams.containsAll(theParamsToCheck)) {
			throw new IllegalArgumentException(Msg.code(2744) + "The " + Constants.PARAM_INCLUDE_DELETED
					+ " parameter is only compatible with the following parameters: " + compatibleParams);
		}
	}

	public static class UnrecognizedSearchParameterException extends InvalidRequestException {

		private final String myResourceName;
		private final String myParamName;

		UnrecognizedSearchParameterException(String theMessage, String theResourceName, String theParamName) {
			super(theMessage);
			myResourceName = theResourceName;
			myParamName = theParamName;
		}

		public String getResourceName() {
			return myResourceName;
		}

		public String getParamName() {
			return myParamName;
		}
	}

	private InvalidRequestException throwUnrecognizedParamException(
			String theMatchUrl, RuntimeResourceDefinition theResourceDefinition, String nextParamName) {
		return new UnrecognizedSearchParameterException(
				Msg.code(488) + "Failed to parse match URL[" + theMatchUrl + "] - Resource type "
						+ theResourceDefinition.getName() + " does not have a parameter with name: " + nextParamName,
				theResourceDefinition.getName(),
				nextParamName);
	}

	private IQueryParameterAnd<?> newInstanceAnd(String theParamType) {
		Class<? extends IQueryParameterAnd<?>> clazz = ResourceMetaParams.RESOURCE_META_AND_PARAMS.get(theParamType);
		return ReflectionUtil.newInstance(clazz);
	}

	public IQueryParameterType newInstanceType(String theParamType) {
		Class<? extends IQueryParameterType> clazz = ResourceMetaParams.RESOURCE_META_PARAMS.get(theParamType);
		return ReflectionUtil.newInstance(clazz);
	}

	public ResourceSearch getResourceSearch(String theUrl, RequestPartitionId theRequestPartitionId, Flag... theFlags) {
		RuntimeResourceDefinition resourceDefinition;
		resourceDefinition = UrlUtil.parseUrlResourceType(myFhirContext, theUrl);
		SearchParameterMap searchParameterMap = translateMatchUrl(theUrl, resourceDefinition, theFlags);
		return new ResourceSearch(resourceDefinition, searchParameterMap, theRequestPartitionId);
	}

	public ResourceSearch getResourceSearch(String theUrl) {
		return getResourceSearch(theUrl, null);
	}

	/**
	 * Parse a URL that contains _include or _revinclude parameters and return a {@link ResourceSearch} object
	 * @param theUrl
	 * @return the ResourceSearch object that can be used to create a SearchParameterMap
	 */
	public ResourceSearch getResourceSearchWithIncludesAndRevIncludes(String theUrl) {
		return getResourceSearch(theUrl, null, MatchUrlService.processIncludes());
	}

	public interface Flag {
		void process(String theParamName, List<QualifiedParamList> theValues, SearchParameterMap theMapToPopulate);
	}

	/**
	 * Indicates that the parser should process _include and _revinclude (by default these are not handled)
	 */
	public static Flag processIncludes() {
		return (theParamName, theValues, theMapToPopulate) -> {
			if (Constants.PARAM_INCLUDE.equals(theParamName)) {
				for (QualifiedParamList nextQualifiedList : theValues) {
					for (String nextValue : nextQualifiedList) {
						theMapToPopulate.addInclude(new Include(
								nextValue, ParameterUtil.isIncludeIterate(nextQualifiedList.getQualifier())));
					}
				}
			} else if (Constants.PARAM_REVINCLUDE.equals(theParamName)) {
				for (QualifiedParamList nextQualifiedList : theValues) {
					for (String nextValue : nextQualifiedList) {
						theMapToPopulate.addRevInclude(new Include(
								nextValue, ParameterUtil.isIncludeIterate(nextQualifiedList.getQualifier())));
					}
				}
			}
		};
	}
}
