package ca.uhn.fhir.jpa.searchparam.util;

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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.model.api.IQueryParameterAnd;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.QualifiedParamList;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.param.CompositeAndListParam;
import ca.uhn.fhir.rest.param.DateAndListParam;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.HasAndListParam;
import ca.uhn.fhir.rest.param.HasParam;
import ca.uhn.fhir.rest.param.NumberAndListParam;
import ca.uhn.fhir.rest.param.NumberParam;
import ca.uhn.fhir.rest.param.QuantityAndListParam;
import ca.uhn.fhir.rest.param.QuantityParam;
import ca.uhn.fhir.rest.param.ReferenceAndListParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.SpecialAndListParam;
import ca.uhn.fhir.rest.param.SpecialParam;
import ca.uhn.fhir.rest.param.StringAndListParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.UriAndListParam;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.rest.param.binder.QueryParameterAndBinder;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public enum JpaParamUtil {

	;

	/**
	 * This is a utility method intended provided to help the JPA module.
	 */
	public static IQueryParameterAnd<?> parseQueryParams(FhirContext theContext, RestSearchParameterTypeEnum paramType,
																		  String theUnqualifiedParamName, List<QualifiedParamList> theParameters) {
		QueryParameterAndBinder binder;
		switch (paramType) {
			case COMPOSITE:
				throw new UnsupportedOperationException(Msg.code(496));
			case DATE:
				binder = new QueryParameterAndBinder(DateAndListParam.class,
					Collections.emptyList());
				break;
			case NUMBER:
				binder = new QueryParameterAndBinder(NumberAndListParam.class,
					Collections.emptyList());
				break;
			case QUANTITY:
				binder = new QueryParameterAndBinder(QuantityAndListParam.class,
					Collections.emptyList());
				break;
			case REFERENCE:
				binder = new QueryParameterAndBinder(ReferenceAndListParam.class,
					Collections.emptyList());
				break;
			case STRING:
				binder = new QueryParameterAndBinder(StringAndListParam.class,
					Collections.emptyList());
				break;
			case TOKEN:
				binder = new QueryParameterAndBinder(TokenAndListParam.class,
					Collections.emptyList());
				break;
			case URI:
				binder = new QueryParameterAndBinder(UriAndListParam.class,
					Collections.emptyList());
				break;
			case HAS:
				binder = new QueryParameterAndBinder(HasAndListParam.class,
					Collections.emptyList());
				break;
			case SPECIAL:
				binder = new QueryParameterAndBinder(SpecialAndListParam.class,
					Collections.emptyList());
				break;
			default:
				throw new IllegalArgumentException(Msg.code(497) + "Parameter '" + theUnqualifiedParamName + "' has type " + paramType + " which is currently not supported.");
		}

		return binder.parse(theContext, theUnqualifiedParamName, theParameters);
	}

	/**
	 * This is a utility method intended provided to help the JPA module.
	 */
	public static IQueryParameterAnd<?> parseQueryParams(ISearchParamRegistry theSearchParamRegistry, FhirContext theContext, RuntimeSearchParam theParamDef,
																		  String theUnqualifiedParamName, List<QualifiedParamList> theParameters) {

		RestSearchParameterTypeEnum paramType = theParamDef.getParamType();

		if (paramType == RestSearchParameterTypeEnum.COMPOSITE) {

			List<RuntimeSearchParam> compositeList = resolveComponentParameters(theSearchParamRegistry, theParamDef);

			if (compositeList.size() != 2) {
				throw new ConfigurationException(Msg.code(498) + "Search parameter of type " + theUnqualifiedParamName
					+ " must have 2 composite types declared in parameter annotation, found "
					+ compositeList.size());
			}

			RuntimeSearchParam left = compositeList.get(0);
			RuntimeSearchParam right = compositeList.get(1);

			@SuppressWarnings({"unchecked", "rawtypes"})
			CompositeAndListParam<IQueryParameterType, IQueryParameterType> cp = new CompositeAndListParam(
				getCompositeBindingClass(left.getParamType(), left.getName()),
				getCompositeBindingClass(right.getParamType(), right.getName()));

			cp.setValuesAsQueryTokens(theContext, theUnqualifiedParamName, theParameters);

			return cp;
		} else {
			return parseQueryParams(theContext, paramType, theUnqualifiedParamName, theParameters);
		}
	}

	public static List<RuntimeSearchParam> resolveComponentParameters(ISearchParamRegistry theSearchParamRegistry, RuntimeSearchParam theParamDef) {
		List<RuntimeSearchParam> compositeList = new ArrayList<>();
		List<RuntimeSearchParam.Component> components = theParamDef.getComponents();
		for (RuntimeSearchParam.Component next : components) {
			String url = next.getReference();
			RuntimeSearchParam componentParam = theSearchParamRegistry.getActiveSearchParamByUrl(url);
			if (componentParam == null) {
				throw new InternalErrorException(Msg.code(499) + "Can not find SearchParameter: " + url);
			}
			compositeList.add(componentParam);
		}

		compositeList.sort((Comparator.comparing(RuntimeSearchParam::getName)));

		return compositeList;
	}

	private static Class<?> getCompositeBindingClass(RestSearchParameterTypeEnum paramType,
																	 String theUnqualifiedParamName) {

		switch (paramType) {
			case DATE:
				return DateParam.class;
			case NUMBER:
				return NumberParam.class;
			case QUANTITY:
				return QuantityParam.class;
			case REFERENCE:
				return ReferenceParam.class;
			case STRING:
				return StringParam.class;
			case TOKEN:
				return TokenParam.class;
			case URI:
				return UriParam.class;
			case HAS:
				return HasParam.class;
			case SPECIAL:
				return SpecialParam.class;

			case COMPOSITE:
			default:
				throw new IllegalArgumentException(Msg.code(500) + "Parameter '" + theUnqualifiedParamName + "' has type " + paramType
					+ " which is currently not supported.");
		}
	}
}
