package ca.uhn.fhir.jpa.searchparam;

/*-
 * #%L
 * HAPI FHIR Search Parameters
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
import ca.uhn.fhir.rest.api.SearchTotalModeEnum;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.ParameterUtil;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.util.ReflectionUtil;
import ca.uhn.fhir.util.UrlUtil;
import com.google.common.collect.ArrayListMultimap;
import org.apache.http.NameValuePair;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class MatchUrlService {

	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private ISearchParamRegistry mySearchParamRegistry;

	public SearchParameterMap translateMatchUrl(String theMatchUrl, RuntimeResourceDefinition theResourceDefinition, Flag... theFlags) {
		SearchParameterMap paramMap = new SearchParameterMap();
		List<NameValuePair> parameters = UrlUtil.translateMatchUrl(theMatchUrl);

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

			QualifiedParamList paramList = QualifiedParamList.splitQueryStringByCommasIgnoreEscape(qualifier, next.getValue());
			nameToParamLists.put(paramName, paramList);
		}

		for (String nextParamName : nameToParamLists.keySet()) {
			List<QualifiedParamList> paramList = nameToParamLists.get(nextParamName);

			if (theFlags != null) {
				for (Flag next : theFlags) {
					next.process(nextParamName, paramList, paramMap);
				}
			}

			if (Constants.PARAM_LASTUPDATED.equals(nextParamName)) {
				if (paramList != null && paramList.size() > 0) {
					if (paramList.size() > 2) {
						throw new InvalidRequestException(Msg.code(484) + "Failed to parse match URL[" + theMatchUrl + "] - Can not have more than 2 " + Constants.PARAM_LASTUPDATED + " parameter repetitions");
					} else {
						DateRangeParam p1 = new DateRangeParam();
						p1.setValuesAsQueryTokens(myFhirContext, nextParamName, paramList);
						paramMap.setLastUpdated(p1);
					}
				}
			} else if (Constants.PARAM_HAS.equals(nextParamName)) {
				IQueryParameterAnd<?> param = JpaParamUtil.parseQueryParams(myFhirContext, RestSearchParameterTypeEnum.HAS, nextParamName, paramList);
				paramMap.add(nextParamName, param);
			} else if (Constants.PARAM_COUNT.equals(nextParamName)) {
				if (paramList != null && paramList.size() > 0 && paramList.get(0).size() > 0) {
					String intString = paramList.get(0).get(0);
					try {
						paramMap.setCount(Integer.parseInt(intString));
					} catch (NumberFormatException e) {
						throw new InvalidRequestException(Msg.code(485) + "Invalid " + Constants.PARAM_COUNT + " value: " + intString);
					}
				}
			} else if (Constants.PARAM_SEARCH_TOTAL_MODE.equals(nextParamName)) {
				if (paramList != null && ! paramList.isEmpty() && ! paramList.get(0).isEmpty()) {
					String totalModeEnumStr = paramList.get(0).get(0);
					try {
						paramMap.setSearchTotalMode(SearchTotalModeEnum.valueOf(totalModeEnumStr));
					} catch (IllegalArgumentException e) {
						throw new InvalidRequestException(Msg.code(2078) + "Invalid " + Constants.PARAM_SEARCH_TOTAL_MODE + " value: " + totalModeEnumStr);
					}
				}
			} else if (Constants.PARAM_OFFSET.equals(nextParamName)) {
				if (paramList != null && paramList.size() > 0 && paramList.get(0).size() > 0) {
					String intString = paramList.get(0).get(0);
					try {
						paramMap.setOffset(Integer.parseInt(intString));
					} catch (NumberFormatException e) {
						throw new InvalidRequestException(Msg.code(486) + "Invalid " + Constants.PARAM_OFFSET + " value: " + intString);
					}
				}
			} else if (ResourceMetaParams.RESOURCE_META_PARAMS.containsKey(nextParamName)) {
				if (isNotBlank(paramList.get(0).getQualifier()) && paramList.get(0).getQualifier().startsWith(".")) {
					throw new InvalidRequestException(Msg.code(487) + "Invalid parameter chain: " + nextParamName + paramList.get(0).getQualifier());
				}
				IQueryParameterAnd<?> type = newInstanceAnd(nextParamName);
				type.setValuesAsQueryTokens(myFhirContext, nextParamName, (paramList));
				paramMap.add(nextParamName, type);
			} else if (Constants.PARAM_SOURCE.equals(nextParamName)) {
				IQueryParameterAnd<?> param = JpaParamUtil.parseQueryParams(myFhirContext, RestSearchParameterTypeEnum.TOKEN, nextParamName, paramList);
				paramMap.add(nextParamName, param);
			} else if (JpaConstants.PARAM_DELETE_EXPUNGE.equals(nextParamName)) {
				paramMap.setDeleteExpunge(true);
			} else if (Constants.PARAM_LIST.equals(nextParamName)) {
				IQueryParameterAnd<?> param = JpaParamUtil.parseQueryParams(myFhirContext, RestSearchParameterTypeEnum.TOKEN, nextParamName, paramList);
				paramMap.add(nextParamName, param);
			} else if (nextParamName.startsWith("_")) {
				// ignore these since they aren't search params (e.g. _sort)
			} else {
				RuntimeSearchParam paramDef = mySearchParamRegistry.getActiveSearchParam(theResourceDefinition.getName(), nextParamName);
				if (paramDef == null) {
					throw new InvalidRequestException(Msg.code(488) + "Failed to parse match URL[" + theMatchUrl + "] - Resource type " + theResourceDefinition.getName() + " does not have a parameter with name: " + nextParamName);
				}

				IQueryParameterAnd<?> param = JpaParamUtil.parseQueryParams(mySearchParamRegistry, myFhirContext, paramDef, nextParamName, paramList);
				paramMap.add(nextParamName, param);
			}
		}
		return paramMap;
	}

	private IQueryParameterAnd<?> newInstanceAnd(String theParamType) {
		Class<? extends IQueryParameterAnd<?>> clazz = ResourceMetaParams.RESOURCE_META_AND_PARAMS.get(theParamType);
		return ReflectionUtil.newInstance(clazz);
	}

	public IQueryParameterType newInstanceType(String theParamType) {
		Class<? extends IQueryParameterType> clazz = ResourceMetaParams.RESOURCE_META_PARAMS.get(theParamType);
		return ReflectionUtil.newInstance(clazz);
	}

	public ResourceSearch getResourceSearch(String theUrl, RequestPartitionId theRequestPartitionId) {
		RuntimeResourceDefinition resourceDefinition;
		resourceDefinition = UrlUtil.parseUrlResourceType(myFhirContext, theUrl);
		SearchParameterMap searchParameterMap = translateMatchUrl(theUrl, resourceDefinition);
		return new ResourceSearch(resourceDefinition, searchParameterMap, theRequestPartitionId);
	}

	public ResourceSearch getResourceSearch(String theUrl) {
		return getResourceSearch(theUrl, null);
	}

	public abstract static class Flag {

		/**
		 * Constructor
		 */
		Flag() {
			// nothing
		}

		abstract void process(String theParamName, List<QualifiedParamList> theValues, SearchParameterMap theMapToPopulate);

	}

	/**
	 * Indicates that the parser should process _include and _revinclude (by default these are not handled)
	 */
	public static Flag processIncludes() {
		return new Flag() {

			@Override
			void process(String theParamName, List<QualifiedParamList> theValues, SearchParameterMap theMapToPopulate) {
				if (Constants.PARAM_INCLUDE.equals(theParamName)) {
					for (QualifiedParamList nextQualifiedList : theValues) {
						for (String nextValue : nextQualifiedList) {
							theMapToPopulate.addInclude(new Include(nextValue, ParameterUtil.isIncludeIterate(nextQualifiedList.getQualifier())));
						}
					}
				} else if (Constants.PARAM_REVINCLUDE.equals(theParamName)) {
					for (QualifiedParamList nextQualifiedList : theValues) {
						for (String nextValue : nextQualifiedList) {
							theMapToPopulate.addRevInclude(new Include(nextValue, ParameterUtil.isIncludeIterate(nextQualifiedList.getQualifier())));
						}
					}
				}

			}
		};
	}

}
