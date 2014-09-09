package ca.uhn.fhir.rest.method;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 University Health Network
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

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationSystemEnum;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationTypeEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.client.BaseHttpClientInvocation;
import ca.uhn.fhir.rest.param.BaseQueryParameter;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.IBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class SearchMethodBinding extends BaseResourceReturningMethodBinding {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchMethodBinding.class);

	private String myCompartmentName;
	private Class<? extends IResource> myDeclaredResourceType;
	private String myDescription;
	private Integer myIdParamIndex;

	private String myQueryName;

	@SuppressWarnings("unchecked")
	public SearchMethodBinding(Class<? extends IResource> theReturnResourceType, Method theMethod, FhirContext theContext, Object theProvider) {
		super(theReturnResourceType, theMethod, theContext, theProvider);
		Search search = theMethod.getAnnotation(Search.class);
		this.myQueryName = StringUtils.defaultIfBlank(search.queryName(), null);
		this.myCompartmentName = StringUtils.defaultIfBlank(search.compartmentName(), null);
		this.myDeclaredResourceType = (Class<? extends IResource>) theMethod.getReturnType();
		this.myIdParamIndex = MethodUtil.findIdParameterIndex(theMethod);

		Description desc = theMethod.getAnnotation(Description.class);
		if (desc != null) {
			if (isNotBlank(desc.formalDefinition())) {
				myDescription = StringUtils.defaultIfBlank(desc.formalDefinition(), null);
			} else {
				myDescription = StringUtils.defaultIfBlank(desc.shortDefinition(), null);
			}
		}

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
					throw new ConfigurationException(msg);
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
			throw new ConfigurationException(msg);
		}

	}

	public static QualifierDetails extractQualifiersFromParameterName(String theParamName) {
		QualifierDetails retVal = new QualifierDetails();
		if (theParamName == null || theParamName.length() == 0) {
			return retVal;
		}

		int dotIdx = -1;
		int colonIdx = -1;
		for (int idx = 0; idx < theParamName.length(); idx++) {
			char nextChar = theParamName.charAt(idx);
			if (nextChar == '.' && dotIdx == -1) {
				dotIdx = idx;
			} else if (nextChar == ':' && colonIdx == -1) {
				colonIdx = idx;
			}
		}

		if (dotIdx != -1 && colonIdx != -1) {
			if (dotIdx < colonIdx) {
				retVal.setDotQualifier(theParamName.substring(dotIdx, colonIdx));
				retVal.setColonQualifier(theParamName.substring(colonIdx));
			} else {
				retVal.setColonQualifier(theParamName.substring(colonIdx, dotIdx));
				retVal.setDotQualifier(theParamName.substring(dotIdx));
			}
		} else if (dotIdx != -1) {
			retVal.setDotQualifier(theParamName.substring(dotIdx));
		} else if (colonIdx != -1) {
			retVal.setColonQualifier(theParamName.substring(colonIdx));
		}

		return retVal;
	}

	public Class<? extends IResource> getDeclaredResourceType() {
		return myDeclaredResourceType;
	}

	public String getDescription() {
		return myDescription;
	}

	@Override
	public RestfulOperationTypeEnum getResourceOperationType() {
		return RestfulOperationTypeEnum.SEARCH_TYPE;
	}

	@Override
	public ReturnTypeEnum getReturnType() {
		return ReturnTypeEnum.BUNDLE;
	}

	@Override
	public RestfulOperationSystemEnum getSystemOperationType() {
		return null;
	}

	@Override
	public boolean incomingServerRequestMatchesMethod(Request theRequest) {
		if (!theRequest.getResourceName().equals(getResourceName())) {
			ourLog.trace("Method {} doesn't match because resource name {} != {}", getMethod().getName(), theRequest.getResourceName(), getResourceName());
			return false;
		}
		if (theRequest.getId() != null && myIdParamIndex == null) {
			ourLog.trace("Method {} doesn't match because ID is not null: {}", theRequest.getId());
			return false;
		}
		if (theRequest.getRequestType() == RequestType.GET && theRequest.getOperation() != null && !Constants.PARAM_SEARCH.equals(theRequest.getOperation())) {
			ourLog.trace("Method {} doesn't match because request type is GET but operation is not null: {}", theRequest.getId(), theRequest.getOperation());
			return false;
		}
		if (theRequest.getRequestType() == RequestType.POST && !Constants.PARAM_SEARCH.equals(theRequest.getOperation())) {
			ourLog.trace("Method {} doesn't match because request type is POST but operation is not _search: {}", theRequest.getId(), theRequest.getOperation());
			return false;
		}
		if (theRequest.getRequestType() != RequestType.GET && theRequest.getRequestType() != RequestType.POST) {
			ourLog.trace("Method {} doesn't match because request type is {}", getMethod());
			return false;
		}
		if (!StringUtils.equals(myCompartmentName, theRequest.getCompartmentName())) {
			ourLog.trace("Method {} doesn't match because it is for compartment {} but request is compartment {}", new Object[] { getMethod(), myCompartmentName, theRequest.getCompartmentName() });
			return false;
		}
		// This is used to track all the parameters so we can reject queries that
		// have additional params we don't understand
		Set<String> methodParamsTemp = new HashSet<String>();

		Set<String> unqualifiedNames = theRequest.getUnqualifiedToQualifiedNames().keySet();
		Set<String> qualifiedParamNames = theRequest.getParameters().keySet();
		for (int i = 0; i < this.getParameters().size(); i++) {
			if (!(getParameters().get(i) instanceof BaseQueryParameter)) {
				continue;
			}
			BaseQueryParameter temp = (BaseQueryParameter) getParameters().get(i);
			String name = temp.getName();
			if (temp.isRequired()) {

				if (qualifiedParamNames.contains(name)) {
					QualifierDetails qualifiers = extractQualifiersFromParameterName(name);
					if (qualifiers.passes(temp.getQualifierWhitelist(), temp.getQualifierBlacklist())) {
						methodParamsTemp.add(name);
					}
				} else if (unqualifiedNames.contains(name)) {
					List<String> qualifiedNames = theRequest.getUnqualifiedToQualifiedNames().get(name);
					qualifiedNames = processWhitelistAndBlacklist(qualifiedNames, temp.getQualifierWhitelist(), temp.getQualifierBlacklist());
					methodParamsTemp.addAll(qualifiedNames);
				} else {
					ourLog.trace("Method {} doesn't match param '{}' is not present", getMethod().getName(), name);
					return false;
				}

			} else {
				if (qualifiedParamNames.contains(name)) {
					QualifierDetails qualifiers = extractQualifiersFromParameterName(name);
					if (qualifiers.passes(temp.getQualifierWhitelist(), temp.getQualifierBlacklist())) {
						methodParamsTemp.add(name);
					}
				} else if (unqualifiedNames.contains(name)) {
					List<String> qualifiedNames = theRequest.getUnqualifiedToQualifiedNames().get(name);
					qualifiedNames = processWhitelistAndBlacklist(qualifiedNames, temp.getQualifierWhitelist(), temp.getQualifierBlacklist());
					methodParamsTemp.addAll(qualifiedNames);
				} else {
					methodParamsTemp.add(name);
				}
			}
		}
		if (myQueryName != null) {
			String[] queryNameValues = theRequest.getParameters().get(Constants.PARAM_QUERY);
			if (queryNameValues != null && StringUtils.isNotBlank(queryNameValues[0])) {
				String queryName = queryNameValues[0];
				if (!myQueryName.equals(queryName)) {
					ourLog.trace("Query name does not match {}", myQueryName);
					return false;
				} else {
					methodParamsTemp.add(Constants.PARAM_QUERY);
				}
			} else {
				ourLog.trace("Query name does not match {}", myQueryName);
				return false;
			}
		} else {
			String[] queryNameValues = theRequest.getParameters().get(Constants.PARAM_QUERY);
			if (queryNameValues != null && StringUtils.isNotBlank(queryNameValues[0])) {
				ourLog.trace("Query has name");
				return false;
			}
		}
		for (String next : theRequest.getParameters().keySet()) {
			if (ALLOWED_PARAMS.contains(next)) {
				methodParamsTemp.add(next);
			}
		}
		Set<String> keySet = theRequest.getParameters().keySet();
		for (String next : keySet) {
			// if (next.startsWith("_")) {
			// if (!SPECIAL_PARAM_NAMES.contains(next)) {
			// continue;
			// }
			// }
			if (!methodParamsTemp.contains(next)) {
				return false;
			}
		}

		return true;
	}

	@Override
	public BaseHttpClientInvocation invokeClient(Object[] theArgs) throws InternalErrorException {
		assert (myQueryName == null || ((theArgs != null ? theArgs.length : 0) == getParameters().size())) : "Wrong number of arguments: " + (theArgs != null ? theArgs.length : "null");

		Map<String, List<String>> queryStringArgs = new LinkedHashMap<String, List<String>>();

		if (myQueryName != null) {
			queryStringArgs.put(Constants.PARAM_QUERY, Collections.singletonList(myQueryName));
		}

		IdDt id = (IdDt) (myIdParamIndex != null ? theArgs[myIdParamIndex] : null);

		String resourceName = getResourceName();
		BaseHttpClientInvocation retVal = createSearchInvocation(getContext(), resourceName, queryStringArgs, id, myCompartmentName, null);

		if (theArgs != null) {
			for (int idx = 0; idx < theArgs.length; idx++) {
				IParameter nextParam = getParameters().get(idx);
				nextParam.translateClientArgumentIntoQueryArgument(getContext(), theArgs[idx], queryStringArgs, retVal);
			}
		}

		return retVal;
	}

	@Override
	public IBundleProvider invokeServer(RequestDetails theRequest, Object[] theMethodParams) throws InvalidRequestException, InternalErrorException {
		if (myIdParamIndex != null) {
			theMethodParams[myIdParamIndex] = theRequest.getId();
		}

		Object response = invokeServerMethod(theMethodParams);

		return toResourceList(response);

	}

	private List<String> processWhitelistAndBlacklist(List<String> theQualifiedNames, Set<String> theQualifierWhitelist, Set<String> theQualifierBlacklist) {
		if (theQualifierWhitelist == null && theQualifierBlacklist == null) {
			return theQualifiedNames;
		}
		ArrayList<String> retVal = new ArrayList<String>(theQualifiedNames.size());
		for (String next : theQualifiedNames) {
			QualifierDetails qualifiers = extractQualifiersFromParameterName(next);
			if (!qualifiers.passes(theQualifierWhitelist, theQualifierBlacklist)) {
				continue;
			}
			retVal.add(next);
		}
		return retVal;
	}

	public void setResourceType(Class<? extends IResource> resourceType) {
		this.myDeclaredResourceType = resourceType;
	}

	public static BaseHttpClientInvocation createSearchInvocation(FhirContext theContext, String theResourceName, Map<String, List<String>> theParameters, IdDt theId, String theCompartmentName,
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
				throw new InvalidRequestException(msg);
			} else {
				compartmentSearch = true;
			}
		}

		/*
		 * Are we doing a get (GET [base]/Patient?name=foo) or a get with search (GET [base]/Patient/_search?name=foo) or a post (POST [base]/Patient with parameters in the POST body)
		 */
		switch (searchStyle) {
		case GET:
		default:
			if (compartmentSearch) {
				invocation = new HttpGetClientInvocation(theParameters, theResourceName, theId.getIdPart(), theCompartmentName);
			} else {
				invocation = new HttpGetClientInvocation(theParameters, theResourceName);
			}
			break;
		case GET_WITH_SEARCH:
			if (compartmentSearch) {
				invocation = new HttpGetClientInvocation(theParameters, theResourceName, theId.getIdPart(), theCompartmentName, Constants.PARAM_SEARCH);
			} else {
				invocation = new HttpGetClientInvocation(theParameters, theResourceName, Constants.PARAM_SEARCH);
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

	public static class QualifierDetails {

		private String myColonQualifier;

		private String myDotQualifier;

		public String getColonQualifier() {
			return myColonQualifier;
		}

		public String getDotQualifier() {
			return myDotQualifier;
		}

		public boolean passes(Set<String> theQualifierWhitelist, Set<String> theQualifierBlacklist) {
			if (theQualifierWhitelist != null) {
				if (!theQualifierWhitelist.contains(".*")) {
					if (myDotQualifier != null) {
						if (!theQualifierWhitelist.contains(myDotQualifier)) {
							return false;
						}
					} else {
						if (!theQualifierWhitelist.contains(".")) {
							return false;
						}
					}
				}
				if (!theQualifierWhitelist.contains(":*")) {
					if (myColonQualifier != null) {
						if (!theQualifierWhitelist.contains(myColonQualifier)) {
							return false;
						}
					} else {
						if (!theQualifierWhitelist.contains(":")) {
							return false;
						}
					}
				}
			}
			if (theQualifierBlacklist != null) {
				if (myDotQualifier != null) {
					if (theQualifierBlacklist.contains(myDotQualifier)) {
						return false;
					}
				}
				if (myColonQualifier != null) {
					if (theQualifierBlacklist.contains(myColonQualifier)) {
						return false;
					}
				}
			}

			return true;
		}

		public void setColonQualifier(String theColonQualifier) {
			myColonQualifier = theColonQualifier;
		}

		public void setDotQualifier(String theDotQualifier) {
			myDotQualifier = theDotQualifier;
		}

	}

	@Override
	public String toString() {
		return getMethod().toString();
	}

	public static enum RequestType {
		DELETE, GET, OPTIONS, POST, PUT
	}

}
