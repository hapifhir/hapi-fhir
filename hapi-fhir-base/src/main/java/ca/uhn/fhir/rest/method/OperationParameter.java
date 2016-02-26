package ca.uhn.fhir.rest.method;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
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
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeChildDefinition.IAccessor;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.IRuntimeDatatypeDefinition;
import ca.uhn.fhir.context.RuntimeChildPrimitiveDatatypeDefinition;
import ca.uhn.fhir.context.RuntimePrimitiveDatatypeDefinition;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.i18n.HapiLocalizer;
import ca.uhn.fhir.model.api.IDatatype;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.ValidationModeEnum;
import ca.uhn.fhir.rest.param.CollectionBinder;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.MethodNotAllowedException;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.ParametersUtil;

public class OperationParameter implements IParameter {

	static final String REQUEST_CONTENTS_USERDATA_KEY = OperationParam.class.getName() + "_PARSED_RESOURCE";
	
	private IConverter myConverter;
	@SuppressWarnings("rawtypes")
	private Class<? extends Collection> myInnerCollectionType;
	private int myMax;
	private int myMin;
	private final String myName;
	private final String myOperationName;
	private Class<?> myParameterType;
	private String myParamType;
	private final FhirContext myContext;
	private boolean myAllowGet;

	public OperationParameter(FhirContext theCtx, String theOperationName, OperationParam theOperationParam) {
		this(theCtx, theOperationName, theOperationParam.name(), theOperationParam.min(), theOperationParam.max());
	}

	OperationParameter(FhirContext theCtx, String theOperationName, String theParameterName, int theMin, int theMax) {
		myOperationName = theOperationName;
		myName = theParameterName;
		myMin = theMin;
		myMax = theMax;
		myContext = theCtx;
	}

	protected FhirContext getContext() {
		return myContext;
	}

	public int getMax() {
		return myMax;
	}

	public int getMin() {
		return myMin;
	}

	public String getName() {
		return myName;
	}

	public String getParamType() {
		return myParamType;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initializeTypes(Method theMethod, Class<? extends Collection<?>> theOuterCollectionType, Class<? extends Collection<?>> theInnerCollectionType, Class<?> theParameterType) {
		if (getContext().getVersion().getVersion().isRi()) {
			if (IDatatype.class.isAssignableFrom(theParameterType)) {
				throw new ConfigurationException("Incorrect use of type " + theParameterType.getSimpleName() + " as parameter type for method when context is for version "
						+ getContext().getVersion().getVersion().name() + " in method: " + theMethod.toString());
			}
		}

		myParameterType = theParameterType;
		if (theInnerCollectionType != null) {
			myInnerCollectionType = CollectionBinder.getInstantiableCollectionType(theInnerCollectionType, myName);
		} else {
			myMax = 1;
		}

		myAllowGet = IPrimitiveType.class.isAssignableFrom(myParameterType) || String.class.equals(myParameterType);

		/*
		 * The parameter can be of type string for validation methods - This is a bit weird. See ValidateDstu2Test. We should probably clean this up..
		 */
		if (!myParameterType.equals(IBase.class) && !myParameterType.equals(String.class)) {
			if (IBaseResource.class.isAssignableFrom(myParameterType) && myParameterType.isInterface()) {
				myParamType = "Resource";
			} else if (DateRangeParam.class.isAssignableFrom(myParameterType)) {
				myParamType = "date";
				myMax = 2;
				myAllowGet = true;
			} else if (myParameterType.equals(ValidationModeEnum.class)) {
				// this is ok
			} else if (!IBase.class.isAssignableFrom(myParameterType) || myParameterType.isInterface() || Modifier.isAbstract(myParameterType.getModifiers())) {
				throw new ConfigurationException("Invalid type for @OperationParam: " + myParameterType.getName());
			} else if (myParameterType.equals(ValidationModeEnum.class)) {
				myParamType = "code";
			} else {
				myParamType = myContext.getElementDefinition((Class<? extends IBase>) myParameterType).getName();
			}
		}

	}

	public OperationParameter setConverter(IConverter theConverter) {
		myConverter = theConverter;
		return this;
	}

	@Override
	public void translateClientArgumentIntoQueryArgument(FhirContext theContext, Object theSourceClientArgument, Map<String, List<String>> theTargetQueryArguments, IBaseResource theTargetResource)
			throws InternalErrorException {
		assert theTargetResource != null;
		Object sourceClientArgument = theSourceClientArgument;
		if (sourceClientArgument == null) {
			return;
		}

		if (myConverter != null) {
			sourceClientArgument = myConverter.outgoingClient(sourceClientArgument);
		}

		ParametersUtil.addParameterToParameters(theContext, theTargetResource, sourceClientArgument, myName);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object translateQueryParametersIntoServerArgument(RequestDetails theRequest, BaseMethodBinding<?> theMethodBinding) throws InternalErrorException, InvalidRequestException {
		List<Object> matchingParamValues = new ArrayList<Object>();

		if (theRequest.getRequestType() == RequestTypeEnum.GET) {
			String[] paramValues = theRequest.getParameters().get(myName);
			if (paramValues != null && paramValues.length > 0) {
				if (myAllowGet) {

					if (DateRangeParam.class.isAssignableFrom(myParameterType)) {
						List<QualifiedParamList> parameters = new ArrayList<QualifiedParamList>();
						parameters.add(QualifiedParamList.singleton(paramValues[0]));
						if (paramValues.length > 1) {
							parameters.add(QualifiedParamList.singleton(paramValues[1]));
						}
						DateRangeParam dateRangeParam = new DateRangeParam();
						dateRangeParam.setValuesAsQueryTokens(parameters);
						matchingParamValues.add(dateRangeParam);
					} else if (String.class.isAssignableFrom(myParameterType)) {

						for (String next : paramValues) {
							matchingParamValues.add(next);
						}

					} else {
						for (String nextValue : paramValues) {
							FhirContext ctx = theRequest.getServer().getFhirContext();
							RuntimePrimitiveDatatypeDefinition def = (RuntimePrimitiveDatatypeDefinition) ctx.getElementDefinition((Class<? extends IBase>) myParameterType);
							IPrimitiveType<?> instance = def.newInstance();
							instance.setValueAsString(nextValue);
							matchingParamValues.add(instance);
						}
					}
				} else {
					HapiLocalizer localizer = theRequest.getServer().getFhirContext().getLocalizer();
					String msg = localizer.getMessage(OperationParameter.class, "urlParamNotPrimitive", myOperationName, myName);
					throw new MethodNotAllowedException(msg, RequestTypeEnum.POST);
				}
			}
		} else {

			FhirContext ctx = theRequest.getServer().getFhirContext();

			IBaseResource requestContents = (IBaseResource) theRequest.getUserData().get(REQUEST_CONTENTS_USERDATA_KEY);
			RuntimeResourceDefinition def = ctx.getResourceDefinition(requestContents);
			if (def.getName().equals("Parameters")) {

				BaseRuntimeChildDefinition paramChild = def.getChildByName("parameter");
				BaseRuntimeElementCompositeDefinition<?> paramChildElem = (BaseRuntimeElementCompositeDefinition<?>) paramChild.getChildByName("parameter");

				RuntimeChildPrimitiveDatatypeDefinition nameChild = (RuntimeChildPrimitiveDatatypeDefinition) paramChildElem.getChildByName("name");
				BaseRuntimeChildDefinition valueChild = paramChildElem.getChildByName("value[x]");
				BaseRuntimeChildDefinition resourceChild = paramChildElem.getChildByName("resource");

				IAccessor paramChildAccessor = paramChild.getAccessor();
				List<IBase> values = paramChildAccessor.getValues(requestContents);
				for (IBase nextParameter : values) {
					List<IBase> nextNames = nameChild.getAccessor().getValues(nextParameter);
					if (nextNames != null && nextNames.size() > 0) {
						IPrimitiveType<?> nextName = (IPrimitiveType<?>) nextNames.get(0);
						if (myName.equals(nextName.getValueAsString())) {

							if (myParameterType.isAssignableFrom(nextParameter.getClass())) {
								matchingParamValues.add(nextParameter);
							} else {
								List<IBase> paramValues = valueChild.getAccessor().getValues(nextParameter);
								List<IBase> paramResources = resourceChild.getAccessor().getValues(nextParameter);
								if (paramValues != null && paramValues.size() > 0) {
									tryToAddValues(paramValues, matchingParamValues);
								} else if (paramResources != null && paramResources.size() > 0) {
									tryToAddValues(paramResources, matchingParamValues);
								}
							}

						}
					}
				}

			} else {
				
				if (myParameterType.isAssignableFrom(requestContents.getClass())) {
					tryToAddValues(Arrays.asList((IBase) requestContents), matchingParamValues);
				}

			}
		}

		if (matchingParamValues.isEmpty()) {
			return null;
		}

		if (myInnerCollectionType == null) {
			return matchingParamValues.get(0);
		}

		try {
			@SuppressWarnings("rawtypes")
			Collection retVal = myInnerCollectionType.newInstance();
			retVal.addAll(matchingParamValues);
			return retVal;
		} catch (InstantiationException e) {
			throw new InternalErrorException("Failed to instantiate " + myInnerCollectionType, e);
		} catch (IllegalAccessException e) {
			throw new InternalErrorException("Failed to instantiate " + myInnerCollectionType, e);
		}
	}

	@SuppressWarnings("unchecked")
	private void tryToAddValues(List<IBase> theParamValues, List<Object> theMatchingParamValues) {
		for (Object nextValue : theParamValues) {
			if (nextValue == null) {
				continue;
			}
			if (myConverter != null) {
				nextValue = myConverter.incomingServer(nextValue);
			}
			if (!myParameterType.isAssignableFrom(nextValue.getClass())) {
				Class<? extends IBaseDatatype> sourceType = (Class<? extends IBaseDatatype>) nextValue.getClass();
				Class<? extends IBaseDatatype> targetType = (Class<? extends IBaseDatatype>) myParameterType;
				BaseRuntimeElementDefinition<?> sourceTypeDef = myContext.getElementDefinition(sourceType);
				BaseRuntimeElementDefinition<?> targetTypeDef = myContext.getElementDefinition(targetType);
				if (targetTypeDef instanceof IRuntimeDatatypeDefinition && sourceTypeDef instanceof IRuntimeDatatypeDefinition) {
					IRuntimeDatatypeDefinition targetTypeDtDef = (IRuntimeDatatypeDefinition) targetTypeDef;
					if (targetTypeDtDef.isProfileOf(sourceType)) {
						FhirTerser terser = myContext.newTerser();
						IBase newTarget = targetTypeDef.newInstance();
						terser.cloneInto((IBase) nextValue, newTarget, true);
						theMatchingParamValues.add(newTarget);
						continue;
					}
				}
				throwWrongParamType(nextValue);
			}
			theMatchingParamValues.add(nextValue);
		}
	}

	private void throwWrongParamType(Object nextValue) {
		throw new InvalidRequestException("Request has parameter " + myName + " of type " + nextValue.getClass().getSimpleName() + " but method expects type " + myParameterType.getSimpleName());
	}

	public interface IConverter {

		Object incomingServer(Object theObject);

		Object outgoingClient(Object theObject);

	}

}
