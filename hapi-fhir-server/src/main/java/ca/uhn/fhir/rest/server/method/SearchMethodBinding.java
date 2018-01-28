package ca.uhn.fhir.rest.server.method;

/*
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;

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

public class SearchMethodBinding extends BaseResourceReturningMethodBinding {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchMethodBinding.class);

	private String myCompartmentName;
	private String myDescription;
	private Integer myIdParamIndex;
	private String myQueryName;
	private boolean myAllowUnknownParams;

	public SearchMethodBinding(Class<? extends IBaseResource> theReturnResourceType, Method theMethod, FhirContext theContext, Object theProvider) {
		super(theReturnResourceType, theMethod, theContext, theProvider);
		Search search = theMethod.getAnnotation(Search.class);
		this.myQueryName = StringUtils.defaultIfBlank(search.queryName(), null);
		this.myCompartmentName = StringUtils.defaultIfBlank(search.compartmentName(), null);
		this.myIdParamIndex = ParameterUtil.findIdParameterIndex(theMethod, getContext());
		this.myAllowUnknownParams = search.allowUnknownParams();

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

		}

		/*
		 * Only compartment searching methods may have an ID parameter
		 */
		if (isBlank(myCompartmentName) && myIdParamIndex != null) {
			String msg = theContext.getLocalizer().getMessage(getClass().getName() + ".idWithoutCompartment", theMethod.getName(), theMethod.getDeclaringClass());
			throw new ConfigurationException(msg);
		}

	}

	public String getDescription() {
		return myDescription;
	}

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
	public boolean incomingServerRequestMatchesMethod(RequestDetails theRequest) {
		
		String clientPreference = theRequest.getHeader(Constants.HEADER_PREFER);
		boolean lenientHandling = false;
		if(clientPreference != null)
		{
			String[] preferences = clientPreference.split(";");
			for( String p : preferences){
				if("handling:lenient".equalsIgnoreCase(p))
				{
					lenientHandling = true;
					break;
				}
			}
		}
		
		if (theRequest.getId() != null && myIdParamIndex == null) {
			ourLog.trace("Method {} doesn't match because ID is not null: {}", theRequest.getId());
			return false;
		}
		if (theRequest.getRequestType() == RequestTypeEnum.GET && theRequest.getOperation() != null && !Constants.PARAM_SEARCH.equals(theRequest.getOperation())) {
			ourLog.trace("Method {} doesn't match because request type is GET but operation is not null: {}", theRequest.getId(), theRequest.getOperation());
			return false;
		}
		if (theRequest.getRequestType() == RequestTypeEnum.POST && !Constants.PARAM_SEARCH.equals(theRequest.getOperation())) {
			ourLog.trace("Method {} doesn't match because request type is POST but operation is not _search: {}", theRequest.getId(), theRequest.getOperation());
			return false;
		}
		if (theRequest.getRequestType() != RequestTypeEnum.GET && theRequest.getRequestType() != RequestTypeEnum.POST) {
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
				}
				if (unqualifiedNames.contains(name)) {
					List<String> qualifiedNames = theRequest.getUnqualifiedToQualifiedNames().get(name);
					qualifiedNames = processWhitelistAndBlacklist(qualifiedNames, temp.getQualifierWhitelist(), temp.getQualifierBlacklist());
					methodParamsTemp.addAll(qualifiedNames);
				}
				if (!qualifiedParamNames.contains(name) && !unqualifiedNames.contains(name))
				{
					ourLog.trace("Method {} doesn't match param '{}' is not present", getMethod().getName(), name);
					return false;
				}

			} else {
				if (qualifiedParamNames.contains(name)) {
					QualifierDetails qualifiers = extractQualifiersFromParameterName(name);
					if (qualifiers.passes(temp.getQualifierWhitelist(), temp.getQualifierBlacklist())) {
						methodParamsTemp.add(name);
					}
				} 
				if (unqualifiedNames.contains(name)) {
					List<String> qualifiedNames = theRequest.getUnqualifiedToQualifiedNames().get(name);
					qualifiedNames = processWhitelistAndBlacklist(qualifiedNames, temp.getQualifierWhitelist(), temp.getQualifierBlacklist());
					methodParamsTemp.addAll(qualifiedNames);
				}
				if (!qualifiedParamNames.contains(name)) { 
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
				}
				methodParamsTemp.add(Constants.PARAM_QUERY);
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
		if(lenientHandling == true)
			return true;

		if (myAllowUnknownParams == false) {
			for (String next : keySet) {
				if (!methodParamsTemp.contains(next)) {
					return false;
				}
			}
		}
		return true;
	}


	@Override
	public IBundleProvider invokeServer(IRestfulServer<?> theServer, RequestDetails theRequest, Object[] theMethodParams) throws InvalidRequestException, InternalErrorException {
		if (myIdParamIndex != null) {
			theMethodParams[myIdParamIndex] = theRequest.getId();
		}

		Object response = invokeServerMethod(theServer, theRequest, theMethodParams);

		return toResourceList(response);

	}

	@Override
	protected boolean isAddContentLocationHeader() {
		return false;
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

	@Override
	public String toString() {
		return getMethod().toString();
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
				retVal.setParamName(theParamName.substring(0, dotIdx));
				retVal.setWholeQualifier(theParamName.substring(dotIdx));
			} else {
				retVal.setColonQualifier(theParamName.substring(colonIdx, dotIdx));
				retVal.setDotQualifier(theParamName.substring(dotIdx));
				retVal.setParamName(theParamName.substring(0, colonIdx));
				retVal.setWholeQualifier(theParamName.substring(colonIdx));
			}
		} else if (dotIdx != -1) {
			retVal.setDotQualifier(theParamName.substring(dotIdx));
			retVal.setParamName(theParamName.substring(0, dotIdx));
			retVal.setWholeQualifier(theParamName.substring(dotIdx));
		} else if (colonIdx != -1) {
			retVal.setColonQualifier(theParamName.substring(colonIdx));
			retVal.setParamName(theParamName.substring(0, colonIdx));
			retVal.setWholeQualifier(theParamName.substring(colonIdx));
		} else {
			retVal.setParamName(theParamName);
			retVal.setColonQualifier(null);
			retVal.setDotQualifier(null);
			retVal.setWholeQualifier(null);
		}

		return retVal;
	}


}
