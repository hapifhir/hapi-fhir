package ca.uhn.fhir.rest.method;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.CompositeOrListParam;
import ca.uhn.fhir.rest.param.DateOrListParam;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.NumberOrListParam;
import ca.uhn.fhir.rest.param.NumberParam;
import ca.uhn.fhir.rest.param.QuantityOrListParam;
import ca.uhn.fhir.rest.param.QuantityParam;
import ca.uhn.fhir.rest.param.ReferenceOrListParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringOrListParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.UriOrListParam;
import ca.uhn.fhir.rest.server.IDynamicSearchResourceProvider;
import ca.uhn.fhir.rest.server.SearchParameterMap;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public class DynamicSearchParameter implements IParameter {

	private Map<String, RuntimeSearchParam> myNameToParam = new HashMap<String, RuntimeSearchParam>();

	public DynamicSearchParameter(IDynamicSearchResourceProvider theProvider) {
		for (RuntimeSearchParam next : theProvider.getSearchParameters()) {
			myNameToParam.put(next.getName(), next);
		}
	}

	@Override
	public void translateClientArgumentIntoQueryArgument(FhirContext theContext, Object theSourceClientArgument, Map<String, List<String>> theTargetQueryArguments, IBaseResource theTargetResource) throws InternalErrorException {
		throw new UnsupportedOperationException("Dynamic search is not supported in client mode (use fluent client for dynamic-like searches)");
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object translateQueryParametersIntoServerArgument(RequestDetails theRequest, BaseMethodBinding<?> theMethodBinding) throws InternalErrorException, InvalidRequestException {
		SearchParameterMap retVal = new SearchParameterMap();

		for (String next : theRequest.getParameters().keySet()) {
			String qualifier = null;
			String qualifiedParamName = next;
			String unqualifiedParamName = next;
			RuntimeSearchParam param = myNameToParam.get(next);
			if (param == null) {
				int colonIndex = next.indexOf(':');
				int dotIndex = next.indexOf('.');
				if (colonIndex != -1 || dotIndex != -1) {
					int index;
					if (colonIndex != -1 && dotIndex != -1) {
						index = Math.min(colonIndex, dotIndex);
					} else {
						index = (colonIndex != -1) ? colonIndex : dotIndex;
					}
					qualifier = next.substring(index);
					next = next.substring(0, index);
					unqualifiedParamName = next;
					param = myNameToParam.get(next);
				}
			}

			if (param != null) {

				for (String nextValue : theRequest.getParameters().get(qualifiedParamName)) {
					QualifiedParamList paramList = QualifiedParamList.splitQueryStringByCommasIgnoreEscape(qualifier, nextValue);

					FhirContext ctx = theRequest.getServer().getFhirContext();
					
					switch (param.getParamType()) {
					case COMPOSITE:
						Class<? extends IQueryParameterType> left = toParamType(param.getCompositeOf().get(0));
						Class<? extends IQueryParameterType> right = toParamType(param.getCompositeOf().get(0));
						@SuppressWarnings({ "rawtypes" })
						CompositeOrListParam compositeOrListParam = new CompositeOrListParam(left, right);
						compositeOrListParam.setValuesAsQueryTokens(ctx, unqualifiedParamName, paramList);
						retVal.add(next, compositeOrListParam);
						break;
					case DATE:
						DateOrListParam dateOrListParam = new DateOrListParam();
						dateOrListParam.setValuesAsQueryTokens(ctx, unqualifiedParamName, paramList);
						retVal.add(next, dateOrListParam);
						break;
					case NUMBER:
						NumberOrListParam numberOrListParam = new NumberOrListParam();
						numberOrListParam.setValuesAsQueryTokens(ctx, unqualifiedParamName, paramList);
						retVal.add(next, numberOrListParam);
						break;
					case QUANTITY:
						QuantityOrListParam quantityOrListParam = new QuantityOrListParam();
						quantityOrListParam.setValuesAsQueryTokens(ctx, unqualifiedParamName, paramList);
						retVal.add(next, quantityOrListParam);
						break;
					case REFERENCE:
						ReferenceOrListParam referenceOrListParam = new ReferenceOrListParam();
						referenceOrListParam.setValuesAsQueryTokens(ctx, unqualifiedParamName, paramList);
						retVal.add(next, referenceOrListParam);
						break;
					case STRING:
						StringOrListParam stringOrListParam = new StringOrListParam();
						stringOrListParam.setValuesAsQueryTokens(ctx, unqualifiedParamName, paramList);
						retVal.add(next, stringOrListParam);
						break;
					case TOKEN:
						TokenOrListParam tokenOrListParam = new TokenOrListParam();
						tokenOrListParam.setValuesAsQueryTokens(ctx, unqualifiedParamName, paramList);
						retVal.add(next, tokenOrListParam);
						break;
					case URI:
						UriOrListParam uriOrListParam = new UriOrListParam();
						uriOrListParam.setValuesAsQueryTokens(ctx, unqualifiedParamName, paramList);
						retVal.add(next, uriOrListParam);
						break;
					case HAS:
						// Should not happen
						break;
					}
				}
			}
		}

		return retVal;
	}

	private Class<? extends IQueryParameterType> toParamType(RuntimeSearchParam theRuntimeSearchParam) {
		switch (theRuntimeSearchParam.getParamType()) {
		case COMPOSITE:
			throw new IllegalStateException("Composite subtype");
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
		default:
			throw new IllegalStateException("null type");
		}
	}

	@Override
	public void initializeTypes(Method theMethod, Class<? extends Collection<?>> theOuterCollectionType, Class<? extends Collection<?>> theInnerCollectionType, Class<?> theParameterType) {
		// nothing
	}

}
