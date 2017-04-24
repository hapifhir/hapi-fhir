package ca.uhn.fhir.rest.method;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.Validate;
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
import ca.uhn.fhir.model.api.IQueryParameterAnd;
import ca.uhn.fhir.model.api.IQueryParameterOr;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.ValidationModeEnum;
import ca.uhn.fhir.rest.param.BaseAndListParam;
import ca.uhn.fhir.rest.param.CollectionBinder;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.MethodNotAllowedException;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.ParametersUtil;
import ca.uhn.fhir.util.ReflectionUtil;

public class OperationParameter implements IParameter {

	@SuppressWarnings("unchecked")
	private static final Class<? extends IQueryParameterType>[] COMPOSITE_TYPES = new Class[0];

	static final String REQUEST_CONTENTS_USERDATA_KEY = OperationParam.class.getName() + "_PARSED_RESOURCE";

	private boolean myAllowGet;

	private final FhirContext myContext;
	private IOperationParamConverter myConverter;
	@SuppressWarnings("rawtypes")
	private Class<? extends Collection> myInnerCollectionType;
	private int myMax;
	private int myMin;
	private final String myName;
	private final String myOperationName;
	private Class<?> myParameterType;
	private String myParamType;
	private SearchParameter mySearchParameterBinding;

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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addValueToList(List<Object> matchingParamValues, Object values) {
		if (values != null) {
			if (BaseAndListParam.class.isAssignableFrom(myParameterType) && matchingParamValues.size() > 0) {
				BaseAndListParam existing = (BaseAndListParam<?>) matchingParamValues.get(0);
				BaseAndListParam<?> newAndList = (BaseAndListParam<?>) values;
				for (IQueryParameterOr nextAnd : newAndList.getValuesAsQueryTokens()) {
					existing.addAnd(nextAnd);
				}
			} else {
				matchingParamValues.add(values);
			}
		}
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
				throw new ConfigurationException("Incorrect use of type " + theParameterType.getSimpleName() + " as parameter type for method when context is for version " + getContext().getVersion().getVersion().name() + " in method: " + theMethod.toString());
			}
		}

		myParameterType = theParameterType;
		if (theInnerCollectionType != null) {
			myInnerCollectionType = CollectionBinder.getInstantiableCollectionType(theInnerCollectionType, myName);
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

		myAllowGet = IPrimitiveType.class.isAssignableFrom(myParameterType) || String.class.equals(myParameterType) || isSearchParam || ValidationModeEnum.class.equals(myParameterType);

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
				myAllowGet = true;
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
				throw new ConfigurationException("Invalid type for @OperationParam: " + myParameterType.getName());
			}

		}

	}

	public OperationParameter setConverter(IOperationParamConverter theConverter) {
		myConverter = theConverter;
		return this;
	}

	private void throwWrongParamType(Object nextValue) {
		throw new InvalidRequestException("Request has parameter " + myName + " of type " + nextValue.getClass().getSimpleName() + " but method expects type " + myParameterType.getSimpleName());
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

		ParametersUtil.addParameterToParameters(theContext, theTargetResource, sourceClientArgument, myName);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object translateQueryParametersIntoServerArgument(RequestDetails theRequest, BaseMethodBinding<?> theMethodBinding) throws InternalErrorException, InvalidRequestException {
		List<Object> matchingParamValues = new ArrayList<Object>();

		if (theRequest.getRequestType() == RequestTypeEnum.GET) {
			translateQueryParametersIntoServerArgumentForGet(theRequest, matchingParamValues);
		} else {
			translateQueryParametersIntoServerArgumentForPost(theRequest, matchingParamValues);
		}

		if (matchingParamValues.isEmpty()) {
			return null;
		}

		if (myInnerCollectionType == null) {
			return matchingParamValues.get(0);
		}

		Collection<Object> retVal = ReflectionUtil.newInstance(myInnerCollectionType);
		retVal.addAll(matchingParamValues);
		return retVal;
	}

	private void translateQueryParametersIntoServerArgumentForGet(RequestDetails theRequest, List<Object> matchingParamValues) {
		if (mySearchParameterBinding != null) {

			List<QualifiedParamList> params = new ArrayList<QualifiedParamList>();
			String nameWithQualifierColon = myName + ":";

			for (String nextParamName : theRequest.getParameters().keySet()) {
				String qualifier;
				if (nextParamName.equals(myName)) {
					qualifier = null;
				} else if (nextParamName.startsWith(nameWithQualifierColon)) {
					qualifier = nextParamName.substring(nextParamName.indexOf(':'));
				} else {
					// This is some other parameter, not the one bound by this instance
					continue;
				}
				String[] values = theRequest.getParameters().get(nextParamName);
				if (values != null) {
					for (String nextValue : values) {
						params.add(QualifiedParamList.splitQueryStringByCommasIgnoreEscape(qualifier, nextValue));
					}
				}
			}
			if (!params.isEmpty()) {
				for (QualifiedParamList next : params) {
					Object values = mySearchParameterBinding.parse(myContext, Collections.singletonList(next));
					addValueToList(matchingParamValues, values);
				}
				
			}

		} else {
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
						FhirContext ctx = theRequest.getServer().getFhirContext();
						dateRangeParam.setValuesAsQueryTokens(ctx, myName, parameters);
						matchingParamValues.add(dateRangeParam);
					} else if (String.class.isAssignableFrom(myParameterType)) {

						for (String next : paramValues) {
							matchingParamValues.add(next);
						}
					} else if (ValidationModeEnum.class.equals(myParameterType)) {
						
						if (isNotBlank(paramValues[0])) {
							ValidationModeEnum validationMode = ValidationModeEnum.forCode(paramValues[0]);
							if (validationMode != null) {
								matchingParamValues.add(validationMode);
							} else {
								throwInvalidMode(paramValues[0]);
							}
						}
						
					} else {
						for (String nextValue : paramValues) {
							FhirContext ctx = theRequest.getServer().getFhirContext();
							RuntimePrimitiveDatatypeDefinition def = (RuntimePrimitiveDatatypeDefinition) ctx.getElementDefinition(myParameterType.asSubclass(IBase.class));
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
		}
	}

	private void translateQueryParametersIntoServerArgumentForPost(RequestDetails theRequest, List<Object> matchingParamValues) {
		IBaseResource requestContents = (IBaseResource) theRequest.getUserData().get(REQUEST_CONTENTS_USERDATA_KEY);
		RuntimeResourceDefinition def = myContext.getResourceDefinition(requestContents);
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
			
			addValueToList(theMatchingParamValues, nextValue);
		}
	}

	public static void throwInvalidMode(String paramValues) {
		throw new InvalidRequestException("Invalid mode value: \"" + paramValues + "\"");
	}

	interface IOperationParamConverter {

		Object incomingServer(Object theObject);

		Object outgoingClient(Object theObject);

	}

	class OperationParamConverter implements IOperationParamConverter {

		public OperationParamConverter() {
			Validate.isTrue(mySearchParameterBinding != null);
		}

		@Override
		public Object incomingServer(Object theObject) {
			IPrimitiveType<?> obj = (IPrimitiveType<?>) theObject;
			List<QualifiedParamList> paramList = Collections.singletonList(QualifiedParamList.splitQueryStringByCommasIgnoreEscape(null, obj.getValueAsString()));
			return mySearchParameterBinding.parse(myContext, paramList);
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
