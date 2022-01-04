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
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.client.impl.BaseHttpClientInvocation;
import ca.uhn.fhir.rest.param.ParameterUtil;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.ParametersUtil;
import org.hl7.fhir.instance.model.api.*;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class OperationMethodBinding extends BaseResourceReturningMethodBinding {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(OperationMethodBinding.class);
	private final boolean myIdempotent;
	private final Integer myIdParamIndex;
	private final String myName;
	private final RestOperationTypeEnum myOtherOperationType;
	private final ReturnTypeEnum myReturnType;
	private BundleTypeEnum myBundleType;
	private String myDescription;

	protected OperationMethodBinding(Class<?> theReturnResourceType, Class<? extends IBaseResource> theReturnTypeFromRp, Method theMethod, FhirContext theContext, Object theProvider,
												boolean theIdempotent, String theOperationName, Class<? extends IBaseResource> theOperationType,
												BundleTypeEnum theBundleType) {
		super(theReturnResourceType, theMethod, theContext, theProvider);

		myBundleType = theBundleType;
		myIdempotent = theIdempotent;
		myIdParamIndex = ParameterUtil.findIdParameterIndex(theMethod, getContext());

		Description description = theMethod.getAnnotation(Description.class);
		if (description != null) {
			myDescription = ParametersUtil.extractDescription(description);
		}
		if (isBlank(myDescription)) {
			myDescription = null;
		}

		if (isBlank(theOperationName)) {
			throw new ConfigurationException(Msg.code(1452) + "Method '" + theMethod.getName() + "' on type " + theMethod.getDeclaringClass().getName() + " is annotated with @" + Operation.class.getSimpleName()
				+ " but this annotation has no name defined");
		}
		if (theOperationName.startsWith("$") == false) {
			theOperationName = "$" + theOperationName;
		}
		myName = theOperationName;

		if (theReturnTypeFromRp != null) {
			setResourceName(theContext.getResourceType(theReturnTypeFromRp));
		} else {
			if (Modifier.isAbstract(theOperationType.getModifiers()) == false) {
				setResourceName(theContext.getResourceType(theOperationType));
			} else {
				setResourceName(null);
			}
		}

		myReturnType = ReturnTypeEnum.RESOURCE;

		if (getResourceName() == null) {
			myOtherOperationType = RestOperationTypeEnum.EXTENDED_OPERATION_SERVER;
		} else if (myIdParamIndex == null) {
			myOtherOperationType = RestOperationTypeEnum.EXTENDED_OPERATION_TYPE;
		} else {
			myOtherOperationType = RestOperationTypeEnum.EXTENDED_OPERATION_INSTANCE;
		}

	}

	public OperationMethodBinding(Class<?> theReturnResourceType, Class<? extends IBaseResource> theReturnTypeFromRp, Method theMethod, FhirContext theContext, Object theProvider,
											Operation theAnnotation) {
		this(theReturnResourceType, theReturnTypeFromRp, theMethod, theContext, theProvider, theAnnotation.idempotent(), theAnnotation.name(), theAnnotation.type(), theAnnotation.bundleType());
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

	@Override
	public RestOperationTypeEnum getRestOperationType() {
		return myOtherOperationType;
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

	public boolean isIdempotent() {
		return myIdempotent;
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
		List<IBase> parameters = t.getValues(theInput, "Parameters.parameter");

		Map<String, List<String>> params = new LinkedHashMap<>();
		for (Object nextParameter : parameters) {
			IPrimitiveType<?> nextNameDt = (IPrimitiveType<?>) t.getSingleValueOrNull((IBase) nextParameter, "name");
			if (nextNameDt == null || nextNameDt.isEmpty()) {
				ourLog.warn("Ignoring input parameter with no value in Parameters.parameter.name in operation client invocation");
				continue;
			}
			String nextName = nextNameDt.getValueAsString();
			if (!params.containsKey(nextName)) {
				params.put(nextName, new ArrayList<>());
			}

			IBaseDatatype value = (IBaseDatatype) t.getSingleValueOrNull((IBase) nextParameter, "value[x]");
			if (value == null) {
				continue;
			}
			if (!(value instanceof IPrimitiveType)) {
				throw new IllegalArgumentException(Msg.code(1453) + "Can not invoke operation as HTTP GET when it has parameters with a composite (non priitive) datatype as the value. Found value: " + value.getClass().getName());
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

}
