package ca.uhn.fhir.rest.client.method;

/*
 * #%L
 * HAPI FHIR - Client Framework
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

import org.hl7.fhir.instance.model.api.*;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.client.impl.BaseHttpClientInvocation;
import ca.uhn.fhir.rest.param.ParameterUtil;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.FhirTerser;

public class OperationMethodBinding extends BaseResourceReturningMethodBinding {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(OperationMethodBinding.class);
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

		myReturnType = ReturnTypeEnum.RESOURCE;

		if (getResourceName() == null) {
			myOtherOperatiopnType = RestOperationTypeEnum.EXTENDED_OPERATION_SERVER;
		} else if (myIdParamIndex == null) {
			myOtherOperatiopnType = RestOperationTypeEnum.EXTENDED_OPERATION_TYPE;
		} else {
			myOtherOperatiopnType = RestOperationTypeEnum.EXTENDED_OPERATION_INSTANCE;
		}

		myReturnParams = new ArrayList<OperationMethodBinding.ReturnType>();
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
	public BaseHttpClientInvocation invokeClient(Object[] theArgs) throws InternalErrorException {
		String id = null;
		if (myIdParamIndex != null) {
			IIdType idDt = (IIdType) theArgs[myIdParamIndex];
			id = idDt.getValue();
		}
		IBaseParameters parameters = (IBaseParameters) getContext().getResourceDefinition("Parameters").newInstance();

		if (theArgs != null) {
			for (int idx = 0; idx < theArgs.length; idx++) {
				IParameter nextParam = getParameters().get(idx);
				nextParam.translateClientArgumentIntoQueryArgument(getContext(), theArgs[idx], null, parameters);
			}
		}

		return createOperationInvocation(getContext(), getResourceName(), id, null, myName, parameters, false);
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

	public void setDescription(String theDescription) {
		myDescription = theDescription;
	}

	public static BaseHttpClientInvocation createOperationInvocation(FhirContext theContext, String theResourceName, String theId, String theVersion, String theOperationName, IBaseParameters theInput,
			boolean theUseHttpGet) {
		StringBuilder b = new StringBuilder();
		if (theResourceName != null) {
			b.append(theResourceName);
			if (isNotBlank(theId)) {
				b.append('/');
				b.append(theId);
				if (isNotBlank(theVersion)) {
					b.append("/_history/");
					b.append(theVersion);
				}
			}
		}
		if (b.length() > 0) {
			b.append('/');
		}
		if (!theOperationName.startsWith("$")) {
			b.append("$");
		}
		b.append(theOperationName);

		if (!theUseHttpGet) {
			return new HttpPostClientInvocation(theContext, theInput, b.toString());
		}
		FhirTerser t = theContext.newTerser();
		List<Object> parameters = t.getValues(theInput, "Parameters.parameter");

		Map<String, List<String>> params = new LinkedHashMap<String, List<String>>();
		for (Object nextParameter : parameters) {
			IPrimitiveType<?> nextNameDt = (IPrimitiveType<?>) t.getSingleValueOrNull((IBase) nextParameter, "name");
			if (nextNameDt == null || nextNameDt.isEmpty()) {
				ourLog.warn("Ignoring input parameter with no value in Parameters.parameter.name in operation client invocation");
				continue;
			}
			String nextName = nextNameDt.getValueAsString();
			if (!params.containsKey(nextName)) {
				params.put(nextName, new ArrayList<String>());
			}

			IBaseDatatype value = (IBaseDatatype) t.getSingleValueOrNull((IBase) nextParameter, "value[x]");
			if (value == null) {
				continue;
			}
			if (!(value instanceof IPrimitiveType)) {
				throw new IllegalArgumentException(
						"Can not invoke operation as HTTP GET when it has parameters with a composite (non priitive) datatype as the value. Found value: " + value.getClass().getName());
			}
			IPrimitiveType<?> primitive = (IPrimitiveType<?>) value;
			params.get(nextName).add(primitive.getValueAsString());
		}
		return new HttpGetClientInvocation(theContext, params, b.toString());
	}

	public static BaseHttpClientInvocation createProcessMsgInvocation(FhirContext theContext, String theOperationName, IBaseBundle theInput, Map<String, List<String>> urlParams) {
		StringBuilder b = new StringBuilder();

		if (b.length() > 0) {
			b.append('/');
		}
		if (!theOperationName.startsWith("$")) {
			b.append("$");
		}
		b.append(theOperationName);

		BaseHttpClientInvocation.appendExtraParamsWithQuestionMark(urlParams, b, b.indexOf("?") == -1);

		return new HttpPostClientInvocation(theContext, theInput, b.toString());

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
