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
 * http://www.apache.org/licenses/LICENSE-2.0
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

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.*;
import ca.uhn.fhir.rest.param.ParameterUtil;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.MethodNotAllowedException;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor.ActionRequestDetails;

public class OperationMethodBinding extends BaseResourceReturningMethodBinding {

	private BundleTypeEnum myBundleType;
	private boolean myCanOperateAtInstanceLevel;
	private boolean myCanOperateAtServerLevel;
	private boolean myCanOperateAtTypeLevel;
	private String myDescription;
	private final boolean myIdempotent;
	private final Integer myIdParamIndex;
	private final String myName;
	private final RestOperationTypeEnum myOtherOperatiopnType;
	private List<ReturnType> myReturnParams;
	private final ReturnTypeEnum myReturnType;

	protected OperationMethodBinding(Class<?> theReturnResourceType, Class<? extends IBaseResource> theReturnTypeFromRp, Method theMethod, FhirContext theContext, Object theProvider,
			boolean theIdempotent, String theOperationName, Class<? extends IBaseResource> theOperationType,
			OperationParam[] theReturnParams, BundleTypeEnum theBundleType) {
		super(theReturnResourceType, theMethod, theContext, theProvider);

		myBundleType = theBundleType;
		myIdempotent = theIdempotent;
		myIdParamIndex = ParameterUtil.findIdParameterIndex(theMethod, getContext());
		if (myIdParamIndex != null) {
			for (Annotation next : theMethod.getParameterAnnotations()[myIdParamIndex]) {
				if (next instanceof IdParam) {
					myCanOperateAtTypeLevel = ((IdParam) next).optional() == true;
				}
			}
		} else {
			myCanOperateAtTypeLevel = true;
		}

		Description description = theMethod.getAnnotation(Description.class);
		if (description != null) {
			myDescription = description.formalDefinition();
			if (isBlank(myDescription)) {
				myDescription = description.shortDefinition();
			}
		}
		if (isBlank(myDescription)) {
			myDescription = null;
		}

		if (isBlank(theOperationName)) {
			throw new ConfigurationException("Method '" + theMethod.getName() + "' on type " + theMethod.getDeclaringClass().getName() + " is annotated with @" + Operation.class.getSimpleName()
					+ " but this annotation has no name defined");
		}
		if (theOperationName.startsWith("$") == false) {
			theOperationName = "$" + theOperationName;
		}
		myName = theOperationName;

		if (theReturnTypeFromRp != null) {
			setResourceName(theContext.getResourceDefinition(theReturnTypeFromRp).getName());
		} else {
			if (Modifier.isAbstract(theOperationType.getModifiers()) == false) {
				setResourceName(theContext.getResourceDefinition(theOperationType).getName());
			} else {
				setResourceName(null);
			}
		}

		if (theMethod.getReturnType().equals(IBundleProvider.class)) {
			myReturnType = ReturnTypeEnum.BUNDLE;
		} else {
			myReturnType = ReturnTypeEnum.RESOURCE;
		}

		if (getResourceName() == null) {
			myOtherOperatiopnType = RestOperationTypeEnum.EXTENDED_OPERATION_SERVER;
		} else if (myIdParamIndex == null) {
			myOtherOperatiopnType = RestOperationTypeEnum.EXTENDED_OPERATION_TYPE;
		} else {
			myOtherOperatiopnType = RestOperationTypeEnum.EXTENDED_OPERATION_INSTANCE;
		}

		myReturnParams = new ArrayList<>();
		if (theReturnParams != null) {
			for (OperationParam next : theReturnParams) {
				ReturnType type = new ReturnType();
				type.setName(next.name());
				type.setMin(next.min());
				type.setMax(next.max());
				if (type.getMax() == OperationParam.MAX_DEFAULT) {
					type.setMax(1);
				}
				if (!next.type().equals(IBase.class)) {
					if (next.type().isInterface() || Modifier.isAbstract(next.type().getModifiers())) {
						throw new ConfigurationException("Invalid value for @OperationParam.type(): " + next.type().getName());
					}
					type.setType(theContext.getElementDefinition(next.type()).getName());
				}
				myReturnParams.add(type);
			}
		}

		if (myIdParamIndex != null) {
			myCanOperateAtInstanceLevel = true;
		}
		if (getResourceName() == null) {
			myCanOperateAtServerLevel = true;
		}

	}

	public OperationMethodBinding(Class<?> theReturnResourceType, Class<? extends IBaseResource> theReturnTypeFromRp, Method theMethod, FhirContext theContext, Object theProvider,
			Operation theAnnotation) {
		this(theReturnResourceType, theReturnTypeFromRp, theMethod, theContext, theProvider, theAnnotation.idempotent(), theAnnotation.name(), theAnnotation.type(), theAnnotation.returnParameters(),
				theAnnotation.bundleType());
	}

	public String getDescription() {
		return myDescription;
	}

	/**
	 * Returns the name of the operation, starting with "$"
	 */
	public String getName() {
		return myName;
	}

	@Override
	protected BundleTypeEnum getResponseBundleType() {
		return myBundleType;
	}

	@Override
	public RestOperationTypeEnum getRestOperationType() {
		return myOtherOperatiopnType;
	}

	public List<ReturnType> getReturnParams() {
		return Collections.unmodifiableList(myReturnParams);
	}

	@Override
	public ReturnTypeEnum getReturnType() {
		return myReturnType;
	}

	@Override
	public boolean incomingServerRequestMatchesMethod(RequestDetails theRequest) {
		if (getResourceName() == null) {
			if (isNotBlank(theRequest.getResourceName())) {
				return false;
			}
		} else if (!getResourceName().equals(theRequest.getResourceName())) {
			return false;
		}

		if (!myName.equals(theRequest.getOperation())) {
			return false;
		}

		RequestTypeEnum requestType = theRequest.getRequestType();
		if (requestType != RequestTypeEnum.GET && requestType != RequestTypeEnum.POST) {
			// Operations can only be invoked with GET and POST
			return false;
		}

		boolean requestHasId = theRequest.getId() != null;
		if (requestHasId) {
			if (isCanOperateAtInstanceLevel() == false) {
				return false;
			}
		} else {
			if (myCanOperateAtTypeLevel == false) {
				return false;
			}
		}

		return true;
	}


	@Override
	public RestOperationTypeEnum getRestOperationType(RequestDetails theRequestDetails) {
		RestOperationTypeEnum retVal = super.getRestOperationType(theRequestDetails);

		if (retVal == RestOperationTypeEnum.EXTENDED_OPERATION_INSTANCE) {
			if (theRequestDetails.getId() == null) {
				retVal = RestOperationTypeEnum.EXTENDED_OPERATION_TYPE;
			}
		}

		return retVal;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
			.append("name", myName)
			.append("methodName", getMethod().getDeclaringClass().getSimpleName() + "." + getMethod().getName())
			.append("serverLevel", myCanOperateAtServerLevel)
			.append("typeLevel", myCanOperateAtTypeLevel)
			.append("instanceLevel", myCanOperateAtInstanceLevel)
			.toString();
	}

	@Override
	public Object invokeServer(IRestfulServer<?> theServer, RequestDetails theRequest) throws BaseServerResponseException, IOException {
		if (theRequest.getRequestType() == RequestTypeEnum.POST) {
			IBaseResource requestContents = ResourceParameter.loadResourceFromRequest(theRequest, this, null);
			theRequest.getUserData().put(OperationParameter.REQUEST_CONTENTS_USERDATA_KEY, requestContents);
		}
		return super.invokeServer(theServer, theRequest);
	}

	@Override
	public Object invokeServer(IRestfulServer<?> theServer, RequestDetails theRequest, Object[] theMethodParams) throws BaseServerResponseException {
		if (theRequest.getRequestType() == RequestTypeEnum.POST) {
			// all good
		} else if (theRequest.getRequestType() == RequestTypeEnum.GET) {
			if (!myIdempotent) {
				String message = getContext().getLocalizer().getMessage(OperationMethodBinding.class, "methodNotSupported", theRequest.getRequestType(), RequestTypeEnum.POST.name());
				throw new MethodNotAllowedException(message, RequestTypeEnum.POST);
			}
		} else {
			if (!myIdempotent) {
				String message = getContext().getLocalizer().getMessage(OperationMethodBinding.class, "methodNotSupported", theRequest.getRequestType(), RequestTypeEnum.POST.name());
				throw new MethodNotAllowedException(message, RequestTypeEnum.POST);
			}
			String message = getContext().getLocalizer().getMessage(OperationMethodBinding.class, "methodNotSupported", theRequest.getRequestType(), RequestTypeEnum.GET.name(), RequestTypeEnum.POST.name());
			throw new MethodNotAllowedException(message, RequestTypeEnum.GET, RequestTypeEnum.POST);
		}

		if (myIdParamIndex != null) {
			theMethodParams[myIdParamIndex] = theRequest.getId();
		}

		Object response = invokeServerMethod(theServer, theRequest, theMethodParams);
		IBundleProvider retVal = toResourceList(response);
		return retVal;
	}

	public boolean isCanOperateAtInstanceLevel() {
		return this.myCanOperateAtInstanceLevel;
	}

	public boolean isCanOperateAtServerLevel() {
		return this.myCanOperateAtServerLevel;
	}

	public boolean isCanOperateAtTypeLevel() {
		return myCanOperateAtTypeLevel;
	}

	public boolean isIdempotent() {
		return myIdempotent;
	}

	@Override
	protected void populateActionRequestDetailsForInterceptor(RequestDetails theRequestDetails, ActionRequestDetails theDetails, Object[] theMethodParams) {
		super.populateActionRequestDetailsForInterceptor(theRequestDetails, theDetails, theMethodParams);
		theDetails.setResource((IBaseResource) theRequestDetails.getUserData().get(OperationParameter.REQUEST_CONTENTS_USERDATA_KEY));
	}

	public void setDescription(String theDescription) {
		myDescription = theDescription;
	}


	public static class ReturnType {
		private int myMax;
		private int myMin;
		private String myName;
		/**
		 * http://hl7-fhir.github.io/valueset-operation-parameter-type.html
		 */
		private String myType;

		public int getMax() {
			return myMax;
		}

		public int getMin() {
			return myMin;
		}

		public String getName() {
			return myName;
		}

		public String getType() {
			return myType;
		}

		public void setMax(int theMax) {
			myMax = theMax;
		}

		public void setMin(int theMin) {
			myMin = theMin;
		}

		public void setName(String theName) {
			myName = theName;
		}

		public void setType(String theType) {
			myType = theType;
		}
	}

}
