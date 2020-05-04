package ca.uhn.fhir.jpa.searchparam;

/*-
 * #%L
 * HAPI FHIR Search Parameters
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import ca.uhn.fhir.model.api.IQueryParameterAnd;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.QualifiedParamList;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.ParameterUtil;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.ReflectionUtil;
import ca.uhn.fhir.util.UrlUtil;
import com.google.common.collect.ArrayListMultimap;
import org.apache.http.NameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class MatchUrlService {

	@Autowired
	private FhirContext myContext;
	@Autowired
	private ISearchParamRegistry mySearchParamRegistry;

	public SearchParameterMap translateMatchUrl(String theMatchUrl, RuntimeResourceDefinition theResourceDefinition) {
		SearchParameterMap paramMap = new SearchParameterMap();
		List<NameValuePair> parameters = translateMatchUrl(theMatchUrl);

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
			if (Constants.PARAM_LASTUPDATED.equals(nextParamName)) {
				if (paramList != null && paramList.size() > 0) {
					if (paramList.size() > 2) {
						throw new InvalidRequestException("Failed to parse match URL[" + theMatchUrl + "] - Can not have more than 2 " + Constants.PARAM_LASTUPDATED + " parameter repetitions");
					} else {
						DateRangeParam p1 = new DateRangeParam();
						p1.setValuesAsQueryTokens(myContext, nextParamName, paramList);
						paramMap.setLastUpdated(p1);
					}
				}
			} else if (Constants.PARAM_HAS.equals(nextParamName)) {
				IQueryParameterAnd<?> param = ParameterUtil.parseQueryParams(myContext, RestSearchParameterTypeEnum.HAS, nextParamName, paramList);
				paramMap.add(nextParamName, param);
			} else if (Constants.PARAM_COUNT.equals(nextParamName)) {
				if (paramList.size() > 0 && paramList.get(0).size() > 0) {
					String intString = paramList.get(0).get(0);
					try {
						paramMap.setCount(Integer.parseInt(intString));
					} catch (NumberFormatException e) {
						throw new InvalidRequestException("Invalid " + Constants.PARAM_COUNT + " value: " + intString);
					}
				}
			} else if (ResourceMetaParams.RESOURCE_META_PARAMS.containsKey(nextParamName)) {
				if (isNotBlank(paramList.get(0).getQualifier()) && paramList.get(0).getQualifier().startsWith(".")) {
					throw new InvalidRequestException("Invalid parameter chain: " + nextParamName + paramList.get(0).getQualifier());
				}
				IQueryParameterAnd<?> type = newInstanceAnd(nextParamName);
				type.setValuesAsQueryTokens(myContext, nextParamName, (paramList));
				paramMap.add(nextParamName, type);
			} else if (Constants.PARAM_SOURCE.equals(nextParamName)) {
				IQueryParameterAnd<?> param = ParameterUtil.parseQueryParams(myContext, RestSearchParameterTypeEnum.TOKEN, nextParamName, paramList);
				paramMap.add(nextParamName, param);
			} else if (nextParamName.startsWith("_")) {
				// ignore these since they aren't search params (e.g. _sort)
			} else {
				RuntimeSearchParam paramDef = mySearchParamRegistry.getSearchParamByName(theResourceDefinition, nextParamName);
				if (paramDef == null) {
					throw new InvalidRequestException(
						"Failed to parse match URL[" + theMatchUrl + "] - Resource type " + theResourceDefinition.getName() + " does not have a parameter with name: " + nextParamName);
				}

				IQueryParameterAnd<?> param = ParameterUtil.parseQueryParams(myContext, paramDef, nextParamName, paramList);
				paramMap.add(nextParamName, param);
			}
		}
		return paramMap;
	}

	public List<NameValuePair> translateMatchUrl(String theMatchUrl) {
		return UrlUtil.translateMatchUrl(theMatchUrl);
	}

	private IQueryParameterAnd newInstanceAnd(String theParamType) {
		Class<? extends IQueryParameterAnd> clazz = ResourceMetaParams.RESOURCE_META_AND_PARAMS.get(theParamType);
		return ReflectionUtil.newInstance(clazz);
	}

	public IQueryParameterType newInstanceType(String theParamType) {
		Class<? extends IQueryParameterType> clazz = ResourceMetaParams.RESOURCE_META_PARAMS.get(theParamType);
		return ReflectionUtil.newInstance(clazz);
	}
}
