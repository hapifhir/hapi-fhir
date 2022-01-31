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
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IDatatype;
import ca.uhn.fhir.model.api.IQueryParameterAnd;
import ca.uhn.fhir.model.api.IQueryParameterOr;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.QualifiedParamList;
import ca.uhn.fhir.rest.api.ValidationModeEnum;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.ParametersUtil;

public class OperationParameter implements IParameter {

	@SuppressWarnings("unchecked")
	private static final Class<? extends IQueryParameterType>[] COMPOSITE_TYPES = new Class[0];

	static final String REQUEST_CONTENTS_USERDATA_KEY = OperationParam.class.getName() + "_PARSED_RESOURCE";

	private final FhirContext myContext;
	private IOperationParamConverter myConverter;
	@SuppressWarnings("rawtypes")
	private int myMax;
	private int myMin;
	private final String myName;
	private Class<?> myParameterType;
	private String myParamType;
	private SearchParameter mySearchParameterBinding;

	public OperationParameter(FhirContext theCtx, String theOperationName, OperationParam theOperationParam) {
		this(theCtx, theOperationName, theOperationParam.name(), theOperationParam.min(), theOperationParam.max());
	}

	OperationParameter(FhirContext theCtx, String theOperationName, String theParameterName, int theMin, int theMax) {
		myName = theParameterName;
		myMin = theMin;
		myMax = theMax;
		myContext = theCtx;
	}

	protected FhirContext getContext() {
		return myContext;
	}


	public String getName() {
		return myName;
	}

	public String getParamType() {
		return myParamType;
	}

	public String getSearchParamType() {
		if (mySearchParameterBinding != null) {
			return mySearchParameterBinding.getParamType().getCode();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initializeTypes(Method theMethod, Class<? extends Collection<?>> theOuterCollectionType, Class<? extends Collection<?>> theInnerCollectionType, Class<?> theParameterType) {
		if (getContext().getVersion().getVersion().isRi()) {
			if (IDatatype.class.isAssignableFrom(theParameterType)) {
				throw new ConfigurationException(Msg.code(1408) + "Incorrect use of type " + theParameterType.getSimpleName() + " as parameter type for method when context is for version " + getContext().getVersion().getVersion().name() + " in method: " + theMethod.toString());
			}
		}

		myParameterType = theParameterType;
		if (theInnerCollectionType != null) {
			if (myMax == OperationParam.MAX_DEFAULT) {
				myMax = OperationParam.MAX_UNLIMITED;
			}
		} else if (IQueryParameterAnd.class.isAssignableFrom(myParameterType)) {
			if (myMax == OperationParam.MAX_DEFAULT) {
				myMax = OperationParam.MAX_UNLIMITED;
			}
		} else {
			if (myMax == OperationParam.MAX_DEFAULT) {
				myMax = 1;
			}
		}

		boolean typeIsConcrete = !myParameterType.isInterface() && !Modifier.isAbstract(myParameterType.getModifiers());

		//@formatter:off
		boolean isSearchParam = 
			IQueryParameterType.class.isAssignableFrom(myParameterType) || 
			IQueryParameterOr.class.isAssignableFrom(myParameterType) ||
			IQueryParameterAnd.class.isAssignableFrom(myParameterType); 
		//@formatter:off

		/*
		 * Note: We say here !IBase.class.isAssignableFrom because a bunch of DSTU1/2 datatypes also
		 * extend this interface. I'm not sure if they should in the end.. but they do, so we
		 * exclude them.
		 */
		isSearchParam &= typeIsConcrete && !IBase.class.isAssignableFrom(myParameterType);

		/*
		 * The parameter can be of type string for validation methods - This is a bit weird. See ValidateDstu2Test. We
		 * should probably clean this up..
		 */
		if (!myParameterType.equals(IBase.class) && !myParameterType.equals(String.class)) {
			if (IBaseResource.class.isAssignableFrom(myParameterType) && myParameterType.isInterface()) {
				myParamType = "Resource";
			} else if (DateRangeParam.class.isAssignableFrom(myParameterType)) {
				myParamType = "date";
				myMax = 2;
			} else if (myParameterType.equals(ValidationModeEnum.class)) {
				myParamType = "code";
			} else if (IBase.class.isAssignableFrom(myParameterType) && typeIsConcrete) {
				myParamType = myContext.getElementDefinition((Class<? extends IBase>) myParameterType).getName();
			} else if (isSearchParam) {
				myParamType = "string";
				mySearchParameterBinding = new SearchParameter(myName, myMin > 0);
				mySearchParameterBinding.setCompositeTypes(COMPOSITE_TYPES);
				mySearchParameterBinding.setType(myContext, theParameterType, theInnerCollectionType, theOuterCollectionType);
				myConverter = new OperationParamConverter();
			} else {
				throw new ConfigurationException(Msg.code(1409) + "Invalid type for @OperationParam: " + myParameterType.getName());
			}

		}

	}

	public OperationParameter setConverter(IOperationParamConverter theConverter) {
		myConverter = theConverter;
		return this;
	}

	@Override
	public void translateClientArgumentIntoQueryArgument(FhirContext theContext, Object theSourceClientArgument, Map<String, List<String>> theTargetQueryArguments, IBaseResource theTargetResource) throws InternalErrorException {
		assert theTargetResource != null;
		Object sourceClientArgument = theSourceClientArgument;
		if (sourceClientArgument == null) {
			return;
		}

		if (myConverter != null) {
			sourceClientArgument = myConverter.outgoingClient(sourceClientArgument);
		}

		ParametersUtil.addParameterToParameters(theContext, (IBaseParameters) theTargetResource, myName, sourceClientArgument);
	}



	public static void throwInvalidMode(String paramValues) {
		throw new InvalidRequestException(Msg.code(1410) + "Invalid mode value: \"" + paramValues + "\"");
	}

	interface IOperationParamConverter {

		Object outgoingClient(Object theObject);

	}

	class OperationParamConverter implements IOperationParamConverter {

		public OperationParamConverter() {
			Validate.isTrue(mySearchParameterBinding != null);
		}

		@Override
		public Object outgoingClient(Object theObject) {
			IQueryParameterType obj = (IQueryParameterType) theObject;
			IPrimitiveType<?> retVal = (IPrimitiveType<?>) myContext.getElementDefinition("string").newInstance();
			retVal.setValueAsString(obj.getValueAsQueryToken(myContext));
			return retVal;
		}

	}


}
