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
 * http://www.apache.org/licenses/LICENSE-2.0
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
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.IRestfulServer;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.ParameterUtil;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.MethodNotAllowedException;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor.ActionRequestDetails;
import ca.uhn.fhir.util.ParametersUtil;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class OperationMethodBinding extends BaseResourceReturningMethodBinding {

	public static final String WILDCARD_NAME = "$" + Operation.NAME_MATCH_ALL;
	private final boolean myIdempotent;
	private final Integer myIdParamIndex;
	private final String myName;
	private final RestOperationTypeEnum myOtherOperationType;
	private final ReturnTypeEnum myReturnType;
	private final String myShortDescription;
	private boolean myGlobal;
	private BundleTypeEnum myBundleType;
	private boolean myCanOperateAtInstanceLevel;
	private boolean myCanOperateAtServerLevel;
	private boolean myCanOperateAtTypeLevel;
	private String myDescription;
	private List<ReturnType> myReturnParams;
	private boolean myManualRequestMode;
	private boolean myManualResponseMode;

	/**
	 * Constructor - This is the constructor that is called when binding a
	 * standard @Operation method.
	 */
	public OperationMethodBinding(Class<?> theReturnResourceType, Class<? extends IBaseResource> theReturnTypeFromRp, Method theMethod, FhirContext theContext, Object theProvider,
											Operation theAnnotation) {
		this(theReturnResourceType, theReturnTypeFromRp, theMethod, theContext, theProvider, theAnnotation.idempotent(), theAnnotation.name(), theAnnotation.type(), theAnnotation.typeName(), theAnnotation.returnParameters(),
			theAnnotation.bundleType(), theAnnotation.global());

		myManualRequestMode = theAnnotation.manualRequest();
		myManualResponseMode = theAnnotation.manualResponse();
	}

	protected OperationMethodBinding(Class<?> theReturnResourceType, Class<? extends IBaseResource> theReturnTypeFromRp, Method theMethod, FhirContext theContext, Object theProvider,
												boolean theIdempotent, String theOperationName, Class<? extends IBaseResource> theOperationType, String theOperationTypeName,
												OperationParam[] theReturnParams, BundleTypeEnum theBundleType, boolean theGlobal) {
		super(theReturnResourceType, theMethod, theContext, theProvider);

		myBundleType = theBundleType;
		myIdempotent = theIdempotent;
		myDescription = ParametersUtil.extractDescription(theMethod);
		myShortDescription = ParametersUtil.extractShortDefinition(theMethod);
		myGlobal = theGlobal;

		for (Annotation[] nextParamAnnotations : theMethod.getParameterAnnotations()) {
			for (Annotation nextParam : nextParamAnnotations) {
				if (nextParam instanceof OptionalParam || nextParam instanceof RequiredParam) {
					throw new ConfigurationException(Msg.code(421) + "Illegal method parameter annotation @" + nextParam.annotationType().getSimpleName() + " on method: " + theMethod.toString());
				}
			}
		}

		if (isBlank(theOperationName)) {
			throw new ConfigurationException(Msg.code(422) + "Method '" + theMethod.getName() + "' on type " + theMethod.getDeclaringClass().getName() + " is annotated with @" + Operation.class.getSimpleName()
				+ " but this annotation has no name defined");
		}
		if (theOperationName.startsWith("$") == false) {
			theOperationName = "$" + theOperationName;
		}
		myName = theOperationName;

		try {
			if (theReturnTypeFromRp != null) {
				setResourceName(theContext.getResourceType(theReturnTypeFromRp));
			} else if (theOperationType != null && Modifier.isAbstract(theOperationType.getModifiers()) == false) {
				setResourceName(theContext.getResourceType(theOperationType));
			} else if (isNotBlank(theOperationTypeName)) {
				setResourceName(theContext.getResourceType(theOperationTypeName));
			} else {
				setResourceName(null);
			}
		} catch (DataFormatException e) {
			throw new ConfigurationException(Msg.code(423) + "Failed to bind method " + theMethod + " - " + e.getMessage(), e);
		}

		if (theMethod.getReturnType().equals(IBundleProvider.class)) {
			myReturnType = ReturnTypeEnum.BUNDLE;
		} else {
			myReturnType = ReturnTypeEnum.RESOURCE;
		}

		myIdParamIndex = ParameterUtil.findIdParameterIndex(theMethod, getContext());
		if (getResourceName() == null) {
			myOtherOperationType = RestOperationTypeEnum.EXTENDED_OPERATION_SERVER;
			if (myIdParamIndex != null) {
				myCanOperateAtInstanceLevel = true;
			} else {
				myCanOperateAtServerLevel = true;
			}
		} else if (myIdParamIndex == null) {
			myOtherOperationType = RestOperationTypeEnum.EXTENDED_OPERATION_TYPE;
			myCanOperateAtTypeLevel = true;
		} else {
			myOtherOperationType = RestOperationTypeEnum.EXTENDED_OPERATION_INSTANCE;
			myCanOperateAtInstanceLevel = true;
			for (Annotation next : theMethod.getParameterAnnotations()[myIdParamIndex]) {
				if (next instanceof IdParam) {
					myCanOperateAtTypeLevel = ((IdParam) next).optional() == true;
				}
			}
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
				Class<? extends IBase> returnType = next.type();
				if (!returnType.equals(IBase.class)) {
					if (returnType.isInterface() || Modifier.isAbstract(returnType.getModifiers())) {
						throw new ConfigurationException(Msg.code(424) + "Invalid value for @OperationParam.type(): " + returnType.getName());
					}
					OperationParameter.validateTypeIsAppropriateVersionForContext(theMethod, returnType, theContext, "return");
					type.setType(theContext.getElementDefinition(returnType).getName());
				}
				myReturnParams.add(type);
			}
		}

		// Parameter Validation
		if (myCanOperateAtInstanceLevel && !isGlobalMethod() && getResourceName() == null) {
			throw new ConfigurationException(Msg.code(425) + "@" + Operation.class.getSimpleName() + " method is an instance level method (it has an @" + IdParam.class.getSimpleName() + " parameter) but is not marked as global() and is not declared in a resource provider: " + theMethod.getName());
		}

	}

	public String getShortDescription() {
		return myShortDescription;
	}

	@Override
	public boolean isGlobalMethod() {
		return myGlobal;
	}

	public String getDescription() {
		return myDescription;
	}

	public void setDescription(String theDescription) {
		myDescription = theDescription;
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

	@Nonnull
	@Override
	public RestOperationTypeEnum getRestOperationType() {
		return myOtherOperationType;
	}

	public List<ReturnType> getReturnParams() {
		return Collections.unmodifiableList(myReturnParams);
	}

	@Override
	public ReturnTypeEnum getReturnType() {
		return myReturnType;
	}

	@Override
	public MethodMatchEnum incomingServerRequestMatchesMethod(RequestDetails theRequest) {
		if (isBlank(theRequest.getOperation())) {
			return MethodMatchEnum.NONE;
		}

		if (!myName.equals(theRequest.getOperation())) {
			if (!myName.equals(WILDCARD_NAME)) {
				return MethodMatchEnum.NONE;
			}
		}

		if (getResourceName() == null) {
			if (isNotBlank(theRequest.getResourceName())) {
				if (!isGlobalMethod()) {
					return MethodMatchEnum.NONE;
				}
			}
		}

		if (getResourceName() != null && !getResourceName().equals(theRequest.getResourceName())) {
			return MethodMatchEnum.NONE;
		}

		RequestTypeEnum requestType = theRequest.getRequestType();
		if (requestType != RequestTypeEnum.GET && requestType != RequestTypeEnum.POST) {
			// Operations can only be invoked with GET and POST
			return MethodMatchEnum.NONE;
		}

		boolean requestHasId = theRequest.getId() != null;
		if (requestHasId) {
			return myCanOperateAtInstanceLevel ? MethodMatchEnum.EXACT : MethodMatchEnum.NONE;
		}
		if (isNotBlank(theRequest.getResourceName())) {
			return myCanOperateAtTypeLevel ? MethodMatchEnum.EXACT : MethodMatchEnum.NONE;
		}
		return myCanOperateAtServerLevel ? MethodMatchEnum.EXACT : MethodMatchEnum.NONE;
	}

	@Override
	public RestOperationTypeEnum getRestOperationType(RequestDetails theRequestDetails) {
		RestOperationTypeEnum retVal = super.getRestOperationType(theRequestDetails);

		if (retVal == RestOperationTypeEnum.EXTENDED_OPERATION_INSTANCE) {
			if (theRequestDetails.getId() == null) {
				retVal = RestOperationTypeEnum.EXTENDED_OPERATION_TYPE;
			}
		}

		if (myGlobal && theRequestDetails.getId() != null && theRequestDetails.getId().hasIdPart()) {
			retVal = RestOperationTypeEnum.EXTENDED_OPERATION_INSTANCE;
		} else if (myGlobal && isNotBlank(theRequestDetails.getResourceName())) {
			retVal = RestOperationTypeEnum.EXTENDED_OPERATION_TYPE;
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
		if (theRequest.getRequestType() == RequestTypeEnum.POST && !myManualRequestMode) {
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
				throw new MethodNotAllowedException(Msg.code(426) + message, RequestTypeEnum.POST);
			}
		} else {
			if (!myIdempotent) {
				String message = getContext().getLocalizer().getMessage(OperationMethodBinding.class, "methodNotSupported", theRequest.getRequestType(), RequestTypeEnum.POST.name());
				throw new MethodNotAllowedException(Msg.code(427) + message, RequestTypeEnum.POST);
			}
			String message = getContext().getLocalizer().getMessage(OperationMethodBinding.class, "methodNotSupported", theRequest.getRequestType(), RequestTypeEnum.GET.name(), RequestTypeEnum.POST.name());
			throw new MethodNotAllowedException(Msg.code(428) + message, RequestTypeEnum.GET, RequestTypeEnum.POST);
		}

		if (myIdParamIndex != null) {
			theMethodParams[myIdParamIndex] = theRequest.getId();
		}

		Object response = invokeServerMethod(theRequest, theMethodParams);
		if (myManualResponseMode) {
			return null;
		}

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
		IBaseResource resource = (IBaseResource) theRequestDetails.getUserData().get(OperationParameter.REQUEST_CONTENTS_USERDATA_KEY);
		theRequestDetails.setResource(resource);
		if (theDetails != null) {
			theDetails.setResource(resource);
		}
	}

	public boolean isManualRequestMode() {
		return myManualRequestMode;
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

		public void setMax(int theMax) {
			myMax = theMax;
		}

		public int getMin() {
			return myMin;
		}

		public void setMin(int theMin) {
			myMin = theMin;
		}

		public String getName() {
			return myName;
		}

		public void setName(String theName) {
			myName = theName;
		}

		public String getType() {
			return myType;
		}

		public void setType(String theType) {
			myType = theType;
		}
	}

}
