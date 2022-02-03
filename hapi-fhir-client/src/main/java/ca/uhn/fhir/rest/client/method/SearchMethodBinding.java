package ca.uhn.fhir.rest.client.method;

/*
 * #%L
 * HAPI FHIR - Client Framework
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
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.SearchStyleEnum;
import ca.uhn.fhir.rest.client.api.UrlSourceEnum;
import ca.uhn.fhir.rest.client.impl.BaseHttpClientInvocation;
import ca.uhn.fhir.rest.param.ParameterUtil;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.ParametersUtil;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class SearchMethodBinding extends BaseResourceReturningMethodBinding {
	private String myCompartmentName;
	private String myDescription;
	private Integer myIdParamIndex;
	private String myQueryName;

	public SearchMethodBinding(Class<? extends IBaseResource> theReturnResourceType, Method theMethod, FhirContext theContext, Object theProvider) {
		super(theReturnResourceType, theMethod, theContext, theProvider);
		Search search = theMethod.getAnnotation(Search.class);
		this.myQueryName = StringUtils.defaultIfBlank(search.queryName(), null);
		this.myCompartmentName = StringUtils.defaultIfBlank(search.compartmentName(), null);
		this.myIdParamIndex = ParameterUtil.findIdParameterIndex(theMethod, getContext());
		this.myDescription = ParametersUtil.extractDescription(theMethod);

		/*
		 * Check for parameter combinations and names that are invalid
		 */
		List<IParameter> parameters = getParameters();
		// List<SearchParameter> searchParameters = new ArrayList<SearchParameter>();
		for (int i = 0; i < parameters.size(); i++) {
			IParameter next = parameters.get(i);
			if (!(next instanceof SearchParameter)) {
				continue;
			}

			SearchParameter sp = (SearchParameter) next;
			if (sp.getName().startsWith("_")) {
				if (ALLOWED_PARAMS.contains(sp.getName())) {
					String msg = getContext().getLocalizer().getMessage(getClass().getName() + ".invalidSpecialParamName", theMethod.getName(), theMethod.getDeclaringClass().getSimpleName(),
							sp.getName());
					throw new ConfigurationException(Msg.code(1442) + msg);
				}
			}

			// searchParameters.add(sp);
		}
		// for (int i = 0; i < searchParameters.size(); i++) {
		// SearchParameter next = searchParameters.get(i);
		// // next.
		// }

		/*
		 * Only compartment searching methods may have an ID parameter
		 */
		if (isBlank(myCompartmentName) && myIdParamIndex != null) {
			String msg = theContext.getLocalizer().getMessage(getClass().getName() + ".idWithoutCompartment", theMethod.getName(), theMethod.getDeclaringClass());
			throw new ConfigurationException(Msg.code(1443) + msg);
		}

	}

	public String getDescription() {
		return myDescription;
	}

	@Override
	protected BundleTypeEnum getResponseBundleType() {
		return BundleTypeEnum.SEARCHSET;
	}

	@Override
	public RestOperationTypeEnum getRestOperationType() {
		return RestOperationTypeEnum.SEARCH_TYPE;
	}

	@Override
	public ReturnTypeEnum getReturnType() {
			return ReturnTypeEnum.BUNDLE;
	}


	@Override
	public BaseHttpClientInvocation invokeClient(Object[] theArgs) throws InternalErrorException {
		assert (myQueryName == null || ((theArgs != null ? theArgs.length : 0) == getParameters().size())) : "Wrong number of arguments: " + (theArgs != null ? theArgs.length : "null");

		Map<String, List<String>> queryStringArgs = new LinkedHashMap<String, List<String>>();

		if (myQueryName != null) {
			queryStringArgs.put(Constants.PARAM_QUERY, Collections.singletonList(myQueryName));
		}

		IIdType id = (IIdType) (myIdParamIndex != null ? theArgs[myIdParamIndex] : null);

		String resourceName = getResourceName();
		if (theArgs != null) {
			for (int idx = 0; idx < theArgs.length; idx++) {
				IParameter nextParam = getParameters().get(idx);
				nextParam.translateClientArgumentIntoQueryArgument(getContext(), theArgs[idx], queryStringArgs, null);
			}
		}

		BaseHttpClientInvocation retVal = createSearchInvocation(getContext(), resourceName, queryStringArgs, id, myCompartmentName, null);

		return retVal;
	}


	@Override
	protected boolean isAddContentLocationHeader() {
		return false;
	}


	@Override
	public String toString() {
		return getMethod().toString();
	}

	public static BaseHttpClientInvocation createSearchInvocation(FhirContext theContext, String theSearchUrl, UrlSourceEnum theUrlSource, Map<String, List<String>> theParams) {
		return new HttpGetClientInvocation(theContext, theParams, theUrlSource, theSearchUrl);
	}


	public static BaseHttpClientInvocation createSearchInvocation(FhirContext theContext, String theResourceName, Map<String, List<String>> theParameters, IIdType theId, String theCompartmentName,
			SearchStyleEnum theSearchStyle) {
		SearchStyleEnum searchStyle = theSearchStyle;
		if (searchStyle == null) {
			int length = 0;
			for (Entry<String, List<String>> nextEntry : theParameters.entrySet()) {
				length += nextEntry.getKey().length();
				for (String next : nextEntry.getValue()) {
					length += next.length();
				}
			}

			if (length < 5000) {
				searchStyle = SearchStyleEnum.GET;
			} else {
				searchStyle = SearchStyleEnum.POST;
			}
		}

		BaseHttpClientInvocation invocation;

		boolean compartmentSearch = false;
		if (theCompartmentName != null) {
			if (theId == null || !theId.hasIdPart()) {
				String msg = theContext.getLocalizer().getMessage(SearchMethodBinding.class.getName() + ".idNullForCompartmentSearch");
				throw new InvalidRequestException(Msg.code(1444) + msg);
			}
			compartmentSearch = true;
		}

		/*
		 * Are we doing a get (GET [base]/Patient?name=foo) or a get with search (GET [base]/Patient/_search?name=foo) or a post (POST [base]/Patient with parameters in the POST body)
		 */
		switch (searchStyle) {
		case GET:
		default:
			if (compartmentSearch) {
				invocation = new HttpGetClientInvocation(theContext, theParameters, theResourceName, theId.getIdPart(), theCompartmentName);
			} else {
				invocation = new HttpGetClientInvocation(theContext, theParameters, theResourceName);
			}
			break;
		case GET_WITH_SEARCH:
			if (compartmentSearch) {
				invocation = new HttpGetClientInvocation(theContext, theParameters, theResourceName, theId.getIdPart(), theCompartmentName, Constants.PARAM_SEARCH);
			} else {
				invocation = new HttpGetClientInvocation(theContext, theParameters, theResourceName, Constants.PARAM_SEARCH);
			}
			break;
		case POST:
			if (compartmentSearch) {
				invocation = new HttpPostClientInvocation(theContext, theParameters, theResourceName, theId.getIdPart(), theCompartmentName, Constants.PARAM_SEARCH);
			} else {
				invocation = new HttpPostClientInvocation(theContext, theParameters, theResourceName, Constants.PARAM_SEARCH);
			}
		}

		return invocation;
	}

}
