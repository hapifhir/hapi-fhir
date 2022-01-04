package ca.uhn.fhir.rest.server.method;

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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.IRestfulServer;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.ParameterUtil;
import ca.uhn.fhir.rest.param.QualifierDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.ParametersUtil;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class SearchMethodBinding extends BaseResourceReturningMethodBinding {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchMethodBinding.class);

	private static final Set<String> SPECIAL_SEARCH_PARAMS;

	static {
		HashSet<String> specialSearchParams = new HashSet<>();
		specialSearchParams.add(IAnyResource.SP_RES_ID);
		specialSearchParams.add(Constants.PARAM_INCLUDE);
		specialSearchParams.add(Constants.PARAM_REVINCLUDE);
		SPECIAL_SEARCH_PARAMS = Collections.unmodifiableSet(specialSearchParams);
	}

	private final String myResourceProviderResourceName;
	private final List<String> myRequiredParamNames;
	private final List<String> myOptionalParamNames;
	private final String myCompartmentName;
	private String myDescription;
	private final Integer myIdParamIndex;
	private final String myQueryName;
	private final boolean myAllowUnknownParams;

	public SearchMethodBinding(Class<? extends IBaseResource> theReturnResourceType, Class<? extends IBaseResource> theResourceProviderResourceType, Method theMethod, FhirContext theContext, Object theProvider) {
		super(theReturnResourceType, theMethod, theContext, theProvider);
		Search search = theMethod.getAnnotation(Search.class);
		this.myQueryName = StringUtils.defaultIfBlank(search.queryName(), null);
		this.myCompartmentName = StringUtils.defaultIfBlank(search.compartmentName(), null);
		this.myIdParamIndex = ParameterUtil.findIdParameterIndex(theMethod, getContext());
		this.myAllowUnknownParams = search.allowUnknownParams();
		this.myDescription = ParametersUtil.extractDescription(theMethod);

		/*
		 * Only compartment searching methods may have an ID parameter
		 */
		if (isBlank(myCompartmentName) && myIdParamIndex != null) {
			String msg = theContext.getLocalizer().getMessage(getClass().getName() + ".idWithoutCompartment", theMethod.getName(), theMethod.getDeclaringClass());
			throw new ConfigurationException(Msg.code(412) + msg);
		}

		if (theResourceProviderResourceType != null) {
			this.myResourceProviderResourceName = theContext.getResourceType(theResourceProviderResourceType);
		} else {
			this.myResourceProviderResourceName = null;
		}

		myRequiredParamNames = getQueryParameters()
			.stream()
			.filter(t -> t.isRequired())
			.map(t -> t.getName())
			.collect(Collectors.toList());
		myOptionalParamNames = getQueryParameters()
			.stream()
			.filter(t -> !t.isRequired())
			.map(t -> t.getName())
			.collect(Collectors.toList());

	}

	public String getDescription() {
		return myDescription;
	}

	public String getQueryName() {
		return myQueryName;
	}

	public String getResourceProviderResourceName() {
		return myResourceProviderResourceName;
	}

	@Nonnull
	@Override
	public RestOperationTypeEnum getRestOperationType() {
		return RestOperationTypeEnum.SEARCH_TYPE;
	}

	@Override
	protected BundleTypeEnum getResponseBundleType() {
		return BundleTypeEnum.SEARCHSET;
	}

	@Override
	public ReturnTypeEnum getReturnType() {
		return ReturnTypeEnum.BUNDLE;
	}

	@Override
	public MethodMatchEnum incomingServerRequestMatchesMethod(RequestDetails theRequest) {

		if (!mightBeSearchRequest(theRequest)) {
			return MethodMatchEnum.NONE;
		}

		if (theRequest.getId() != null && myIdParamIndex == null) {
			ourLog.trace("Method {} doesn't match because ID is not null: {}", getMethod(), theRequest.getId());
			return MethodMatchEnum.NONE;
		}
		if (!StringUtils.equals(myCompartmentName, theRequest.getCompartmentName())) {
			ourLog.trace("Method {} doesn't match because it is for compartment {} but request is compartment {}", getMethod(), myCompartmentName, theRequest.getCompartmentName());
			return MethodMatchEnum.NONE;
		}

		if (myQueryName != null) {
			String[] queryNameValues = theRequest.getParameters().get(Constants.PARAM_QUERY);
			if (queryNameValues != null && StringUtils.isNotBlank(queryNameValues[0])) {
				String queryName = queryNameValues[0];
				if (!myQueryName.equals(queryName)) {
					ourLog.trace("Query name does not match {}", myQueryName);
					return MethodMatchEnum.NONE;
				}
			} else {
				ourLog.trace("Query name does not match {}", myQueryName);
				return MethodMatchEnum.NONE;
			}
		} else {
			String[] queryNameValues = theRequest.getParameters().get(Constants.PARAM_QUERY);
			if (queryNameValues != null && StringUtils.isNotBlank(queryNameValues[0])) {
				ourLog.trace("Query has name");
				return MethodMatchEnum.NONE;
			}
		}

		Set<String> unqualifiedNames = theRequest.getUnqualifiedToQualifiedNames().keySet();
		Set<String> qualifiedParamNames = theRequest.getParameters().keySet();

		MethodMatchEnum retVal = MethodMatchEnum.EXACT;
		for (String nextRequestParam : theRequest.getParameters().keySet()) {
			String nextUnqualifiedRequestParam = ParameterUtil.stripModifierPart(nextRequestParam);
			if (nextRequestParam.startsWith("_") && !SPECIAL_SEARCH_PARAMS.contains(nextUnqualifiedRequestParam)) {
				continue;
			}

			boolean parameterMatches = false;
			boolean approx = false;
			for (BaseQueryParameter nextMethodParam : getQueryParameters()) {

				if (nextRequestParam.equals(nextMethodParam.getName())) {
					QualifierDetails qualifiers = QualifierDetails.extractQualifiersFromParameterName(nextRequestParam);
					if (qualifiers.passes(nextMethodParam.getQualifierWhitelist(), nextMethodParam.getQualifierBlacklist())) {
						parameterMatches = true;
					}
				} else if (nextUnqualifiedRequestParam.equals(nextMethodParam.getName())) {
					List<String> qualifiedNames = theRequest.getUnqualifiedToQualifiedNames().get(nextUnqualifiedRequestParam);
					if (passesWhitelistAndBlacklist(qualifiedNames, nextMethodParam.getQualifierWhitelist(), nextMethodParam.getQualifierBlacklist())) {
						parameterMatches = true;
					}
				}

				// Repetitions supplied by URL but not supported by this parameter
				if (theRequest.getParameters().get(nextRequestParam).length > 1 != nextMethodParam.supportsRepetition()) {
					approx = true;
				}

			}


			if (parameterMatches) {

				if (approx) {
					retVal = retVal.weakerOf(MethodMatchEnum.APPROXIMATE);
				}

			} else {

				if (myAllowUnknownParams) {
					retVal = retVal.weakerOf(MethodMatchEnum.APPROXIMATE);
				} else {
					retVal = retVal.weakerOf(MethodMatchEnum.NONE);
				}

			}

			if (retVal == MethodMatchEnum.NONE) {
				break;
			}

		}

		if (retVal != MethodMatchEnum.NONE) {
			for (String nextRequiredParamName : myRequiredParamNames) {
				if (!qualifiedParamNames.contains(nextRequiredParamName)) {
					if (!unqualifiedNames.contains(nextRequiredParamName)) {
						retVal = MethodMatchEnum.NONE;
						break;
					}
				}
			}
		}
		if (retVal != MethodMatchEnum.NONE) {
			for (String nextRequiredParamName : myOptionalParamNames) {
				if (!qualifiedParamNames.contains(nextRequiredParamName)) {
					if (!unqualifiedNames.contains(nextRequiredParamName)) {
						retVal = retVal.weakerOf(MethodMatchEnum.APPROXIMATE);
					}
				}
			}
		}

		return retVal;
	}

	/**
	 * Is this request a request for a normal search - Ie. not a named search, nor a compartment
	 * search, just a plain old search.
	 *
	 * @since 5.4.0
	 */
	public static boolean isPlainSearchRequest(RequestDetails theRequest) {
		if (theRequest.getId() != null) {
			return false;
		}
		if (isNotBlank(theRequest.getCompartmentName())) {
			return false;
		}
		return mightBeSearchRequest(theRequest);
	}

	private static boolean mightBeSearchRequest(RequestDetails theRequest) {
		if (theRequest.getRequestType() == RequestTypeEnum.GET && theRequest.getOperation() != null && !Constants.PARAM_SEARCH.equals(theRequest.getOperation())) {
			return false;
		}
		if (theRequest.getRequestType() == RequestTypeEnum.POST && !Constants.PARAM_SEARCH.equals(theRequest.getOperation())) {
			return false;
		}
		if (theRequest.getRequestType() != RequestTypeEnum.GET && theRequest.getRequestType() != RequestTypeEnum.POST) {
			return false;
		}
		if (theRequest.getParameters().get(Constants.PARAM_PAGINGACTION) != null) {
			return false;
		}
		return true;
	}

	@Override
	public IBundleProvider invokeServer(IRestfulServer<?> theServer, RequestDetails theRequest, Object[] theMethodParams) throws InvalidRequestException, InternalErrorException {
		if (myIdParamIndex != null) {
			theMethodParams[myIdParamIndex] = theRequest.getId();
		}

		Object response = invokeServerMethod(theRequest, theMethodParams);

		return toResourceList(response);

	}

	@Override
	protected boolean isAddContentLocationHeader() {
		return false;
	}


	private boolean passesWhitelistAndBlacklist(List<String> theQualifiedNames, Set<String> theQualifierWhitelist, Set<String> theQualifierBlacklist) {
		if (theQualifierWhitelist == null && theQualifierBlacklist == null) {
			return true;
		}
		for (String next : theQualifiedNames) {
			QualifierDetails qualifiers = QualifierDetails.extractQualifiersFromParameterName(next);
			if (!qualifiers.passes(theQualifierWhitelist, theQualifierBlacklist)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String toString() {
		return getMethod().toString();
	}


}
