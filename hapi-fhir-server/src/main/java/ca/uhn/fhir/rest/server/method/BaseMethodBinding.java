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
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.IRestfulServer;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.BundleProviders;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor.ActionRequestDetails;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.ReflectionUtil;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public abstract class BaseMethodBinding<T> {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseMethodBinding.class);
	private final List<BaseQueryParameter> myQueryParameters;
	private FhirContext myContext;
	private Method myMethod;
	private List<IParameter> myParameters;
	private Object myProvider;
	private boolean mySupportsConditional;
	private boolean mySupportsConditionalMultiple;
	public BaseMethodBinding(Method theMethod, FhirContext theContext, Object theProvider) {
		assert theMethod != null;
		assert theContext != null;

		myMethod = theMethod;
		myContext = theContext;
		myProvider = theProvider;
		myParameters = MethodUtil.getResourceParameters(theContext, theMethod, theProvider, getRestOperationType());
		myQueryParameters = myParameters
			.stream()
			.filter(t -> t instanceof BaseQueryParameter)
			.map(t -> (BaseQueryParameter) t)
			.collect(Collectors.toList());

		for (IParameter next : myParameters) {
			if (next instanceof ConditionalParamBinder) {
				mySupportsConditional = true;
				if (((ConditionalParamBinder) next).isSupportsMultiple()) {
					mySupportsConditionalMultiple = true;
				}
				break;
			}
		}

		// This allows us to invoke methods on private classes
		myMethod.setAccessible(true);
	}

	protected List<BaseQueryParameter> getQueryParameters() {
		return myQueryParameters;
	}

	protected Object[] createMethodParams(RequestDetails theRequest) {
		Object[] params = new Object[getParameters().size()];
		for (int i = 0; i < getParameters().size(); i++) {
			IParameter param = getParameters().get(i);
			if (param != null) {
				params[i] = param.translateQueryParametersIntoServerArgument(theRequest, this);
			}
		}
		return params;
	}

	protected Object[] createParametersForServerRequest(RequestDetails theRequest) {
		Object[] params = new Object[getParameters().size()];
		for (int i = 0; i < getParameters().size(); i++) {
			IParameter param = getParameters().get(i);
			if (param == null) {
				continue;
			}
			params[i] = param.translateQueryParametersIntoServerArgument(theRequest, this);
		}
		return params;
	}

	/**
	 * Subclasses may override to declare that they apply to all resource types
	 */
	public boolean isGlobalMethod() {
		return false;
	}

	public List<Class<?>> getAllowableParamAnnotations() {
		return null;
	}

	public FhirContext getContext() {
		return myContext;
	}

	public Set<String> getIncludes() {
		return doGetIncludesOrRevIncludes(false);
	}

	public Set<String> getRevIncludes() {
		return doGetIncludesOrRevIncludes(true);
	}

	private Set<String> doGetIncludesOrRevIncludes(boolean reverse) {
		Set<String> retVal = new TreeSet<>();
		for (IParameter next : myParameters) {
			if (next instanceof IncludeParameter) {
				IncludeParameter includeParameter = (IncludeParameter) next;
				if (includeParameter.isReverse() == reverse) {
					retVal.addAll(includeParameter.getAllow());
				}
			}
		}
		return retVal;
	}

	public Method getMethod() {
		return myMethod;
	}

	public List<IParameter> getParameters() {
		return myParameters;
	}

	/**
	 * For unit tests only
	 */
	public void setParameters(List<IParameter> theParameters) {
		myParameters = theParameters;
	}

	public Object getProvider() {
		return myProvider;
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	public Set<Include> getRequestIncludesFromParams(Object[] params) {
		if (params == null || params.length == 0) {
			return null;
		}
		int index = 0;
		boolean match = false;
		for (IParameter parameter : myParameters) {
			if (parameter instanceof IncludeParameter) {
				match = true;
				break;
			}
			index++;
		}
		if (!match) {
			return null;
		}
		if (index >= params.length) {
			ourLog.warn("index out of parameter range (should never happen");
			return null;
		}
		if (params[index] instanceof Set) {
			return (Set<Include>) params[index];
		}
		if (params[index] instanceof Iterable) {
			Set includes = new HashSet<Include>();
			for (Object o : (Iterable) params[index]) {
				if (o instanceof Include) {
					includes.add(o);
				}
			}
			return includes;
		}
		ourLog.warn("include params wasn't Set or Iterable, it was {}", params[index].getClass());
		return null;
	}

	/**
	 * Returns the name of the resource this method handles, or <code>null</code> if this method is not resource specific
	 */
	public abstract String getResourceName();

	@Nonnull
	public abstract RestOperationTypeEnum getRestOperationType();

	/**
	 * Determine which operation is being fired for a specific request
	 *
	 * @param theRequestDetails The request
	 */
	public RestOperationTypeEnum getRestOperationType(RequestDetails theRequestDetails) {
		return getRestOperationType();
	}

	public abstract MethodMatchEnum incomingServerRequestMatchesMethod(RequestDetails theRequest);

	public abstract Object invokeServer(IRestfulServer<?> theServer, RequestDetails theRequest) throws BaseServerResponseException, IOException;

	protected final Object invokeServerMethod(RequestDetails theRequest, Object[] theMethodParams) {
		// Handle server action interceptors
		RestOperationTypeEnum operationType = getRestOperationType(theRequest);
		if (operationType != null) {

			ActionRequestDetails details = new ActionRequestDetails(theRequest);
			populateActionRequestDetailsForInterceptor(theRequest, details, theMethodParams);

			// Interceptor invoke: SERVER_INCOMING_REQUEST_PRE_HANDLED
			HookParams preHandledParams = new HookParams();
			preHandledParams.add(RestOperationTypeEnum.class, theRequest.getRestOperationType());
			preHandledParams.add(RequestDetails.class, theRequest);
			preHandledParams.addIfMatchesType(ServletRequestDetails.class, theRequest);
			preHandledParams.add(ActionRequestDetails.class, details);
			if (theRequest.getInterceptorBroadcaster() != null) {
				theRequest
					.getInterceptorBroadcaster()
					.callHooks(Pointcut.SERVER_INCOMING_REQUEST_PRE_HANDLED, preHandledParams);
			}

		}

		// Actually invoke the method
		try {
			Method method = getMethod();
			return method.invoke(getProvider(), theMethodParams);
		} catch (InvocationTargetException e) {
			if (e.getCause() instanceof BaseServerResponseException) {
				throw (BaseServerResponseException) e.getCause();
			}
			if (e.getTargetException() instanceof DataFormatException) {
				throw (DataFormatException)e.getTargetException();
			}
			throw new InternalErrorException(Msg.code(389) + "Failed to call access method: " + e.getCause(), e);
		} catch (Exception e) {
			throw new InternalErrorException(Msg.code(390) + "Failed to call access method: " + e.getCause(), e);
		}
	}

	/**
	 * Does this method have a parameter annotated with {@link ConditionalParamBinder}. Note that many operations don't actually support this paramter, so this will only return true occasionally.
	 */
	public boolean isSupportsConditional() {
		return mySupportsConditional;
	}

	/**
	 * Does this method support conditional operations over multiple objects (basically for conditional delete)
	 */
	public boolean isSupportsConditionalMultiple() {
		return mySupportsConditionalMultiple;
	}

	/**
	 * Subclasses may override this method (but should also call super) to provide method specifics to the
	 * interceptors.
	 *
	 * @param theRequestDetails The server request details
	 * @param theDetails        The details object to populate
	 * @param theMethodParams   The method params as generated by the specific method binding
	 */
	protected void populateActionRequestDetailsForInterceptor(RequestDetails theRequestDetails, ActionRequestDetails theDetails, Object[] theMethodParams) {
		// nothing by default
	}

	protected IBundleProvider toResourceList(Object response) throws InternalErrorException {
		if (response == null) {
			return BundleProviders.newEmptyList();
		} else if (response instanceof IBundleProvider) {
			return (IBundleProvider) response;
		} else if (response instanceof IBaseResource) {
			return BundleProviders.newList((IBaseResource) response);
		} else if (response instanceof Collection) {
			List<IBaseResource> retVal = new ArrayList<IBaseResource>();
			for (Object next : ((Collection<?>) response)) {
				retVal.add((IBaseResource) next);
			}
			return BundleProviders.newList(retVal);
		} else if (response instanceof MethodOutcome) {
			IBaseResource retVal = ((MethodOutcome) response).getOperationOutcome();
			if (retVal == null) {
				retVal = getContext().getResourceDefinition("OperationOutcome").newInstance();
			}
			return BundleProviders.newList(retVal);
		} else {
			throw new InternalErrorException(Msg.code(391) + "Unexpected return type: " + response.getClass().getCanonicalName());
		}
	}

	public void close() {
		// subclasses may override
	}

	@SuppressWarnings("unchecked")
	public static BaseMethodBinding<?> bindMethod(Method theMethod, FhirContext theContext, Object theProvider) {
		Read read = theMethod.getAnnotation(Read.class);
		Search search = theMethod.getAnnotation(Search.class);
		Metadata conformance = theMethod.getAnnotation(Metadata.class);
		Create create = theMethod.getAnnotation(Create.class);
		Update update = theMethod.getAnnotation(Update.class);
		Delete delete = theMethod.getAnnotation(Delete.class);
		History history = theMethod.getAnnotation(History.class);
		Validate validate = theMethod.getAnnotation(Validate.class);
		AddTags addTags = theMethod.getAnnotation(AddTags.class);
		DeleteTags deleteTags = theMethod.getAnnotation(DeleteTags.class);
		Transaction transaction = theMethod.getAnnotation(Transaction.class);
		Operation operation = theMethod.getAnnotation(Operation.class);
		GetPage getPage = theMethod.getAnnotation(GetPage.class);
		Patch patch = theMethod.getAnnotation(Patch.class);
		GraphQL graphQL = theMethod.getAnnotation(GraphQL.class);

		// ** if you add another annotation above, also add it to the next line:
		if (!verifyMethodHasZeroOrOneOperationAnnotation(theMethod, read, search, conformance, create, update, delete, history, validate, addTags, deleteTags, transaction, operation, getPage, patch, graphQL)) {
			return null;
		}

		if (getPage != null) {
			return new PageMethodBinding(theContext, theMethod);
		}

		if (graphQL != null) {
			return new GraphQLMethodBinding(theMethod, graphQL.type(), theContext, theProvider);
		}

		Class<? extends IBaseResource> returnType;

		Class<? extends IBaseResource> returnTypeFromRp = null;
		if (theProvider instanceof IResourceProvider) {
			returnTypeFromRp = ((IResourceProvider) theProvider).getResourceType();
			if (!verifyIsValidResourceReturnType(returnTypeFromRp)) {
				throw new ConfigurationException(Msg.code(392) + "getResourceType() from " + IResourceProvider.class.getSimpleName() + " type " + theMethod.getDeclaringClass().getCanonicalName() + " returned "
					+ toLogString(returnTypeFromRp) + " - Must return a resource type");
			}
		}

		Class<?> returnTypeFromMethod = theMethod.getReturnType();
		if (MethodOutcome.class.isAssignableFrom(returnTypeFromMethod)) {
			// returns a method outcome
		} else if (IBundleProvider.class.equals(returnTypeFromMethod)) {
			// returns a bundle provider
		} else if (void.class.equals(returnTypeFromMethod)) {
			// returns a bundle
		} else if (Collection.class.isAssignableFrom(returnTypeFromMethod)) {
			returnTypeFromMethod = ReflectionUtil.getGenericCollectionTypeOfMethodReturnType(theMethod);
			if (returnTypeFromMethod == null) {
				ourLog.trace("Method {} returns a non-typed list, can't verify return type", theMethod);
			} else if (!verifyIsValidResourceReturnType(returnTypeFromMethod) && !isResourceInterface(returnTypeFromMethod)) {
				throw new ConfigurationException(Msg.code(393) + "Method '" + theMethod.getName() + "' from " + IResourceProvider.class.getSimpleName() + " type " + theMethod.getDeclaringClass().getCanonicalName()
					+ " returns a collection with generic type " + toLogString(returnTypeFromMethod)
					+ " - Must return a resource type or a collection (List, Set) with a resource type parameter (e.g. List<Patient> or List<IBaseResource> )");
			}
		} else if (IBaseBundle.class.isAssignableFrom(returnTypeFromMethod) && returnTypeFromRp == null) {
			// If a plain provider method returns a Bundle, we'll assume it to be a system
			// level operation and not a type/instance level operation on the Bundle type.
			returnTypeFromMethod = null;
		} else {
			if (!isResourceInterface(returnTypeFromMethod) && !verifyIsValidResourceReturnType(returnTypeFromMethod)) {
				throw new ConfigurationException(Msg.code(394) + "Method '" + theMethod.getName() + "' from " + IResourceProvider.class.getSimpleName() + " type " + theMethod.getDeclaringClass().getCanonicalName()
					+ " returns " + toLogString(returnTypeFromMethod) + " - Must return a resource type (eg Patient, Bundle, " + IBundleProvider.class.getSimpleName()
					+ ", etc., see the documentation for more details)");
			}
		}

		Class<? extends IBaseResource> returnTypeFromAnnotation = IBaseResource.class;
		String returnTypeNameFromAnnotation = null;
		if (read != null) {
			returnTypeFromAnnotation = read.type();
			returnTypeNameFromAnnotation = read.typeName();
		} else if (search != null) {
			returnTypeFromAnnotation = search.type();
			returnTypeNameFromAnnotation = search.typeName();
		} else if (history != null) {
			returnTypeFromAnnotation = history.type();
			returnTypeNameFromAnnotation = history.typeName();
		} else if (delete != null) {
			returnTypeFromAnnotation = delete.type();
			returnTypeNameFromAnnotation = delete.typeName();
		} else if (patch != null) {
			returnTypeFromAnnotation = patch.type();
			returnTypeNameFromAnnotation = patch.typeName();
		} else if (create != null) {
			returnTypeFromAnnotation = create.type();
			returnTypeNameFromAnnotation = create.typeName();
		} else if (update != null) {
			returnTypeFromAnnotation = update.type();
			returnTypeNameFromAnnotation = update.typeName();
		} else if (validate != null) {
			returnTypeFromAnnotation = validate.type();
			returnTypeNameFromAnnotation = validate.typeName();
		} else if (addTags != null) {
			returnTypeFromAnnotation = addTags.type();
			returnTypeNameFromAnnotation = addTags.typeName();
		} else if (deleteTags != null) {
			returnTypeFromAnnotation = deleteTags.type();
			returnTypeNameFromAnnotation = deleteTags.typeName();
		}

		if (isNotBlank(returnTypeNameFromAnnotation)) {
			returnTypeFromAnnotation = theContext.getResourceDefinition(returnTypeNameFromAnnotation).getImplementingClass();
		}

		if (returnTypeFromRp != null) {
			if (returnTypeFromAnnotation != null && !isResourceInterface(returnTypeFromAnnotation)) {
				if (returnTypeFromMethod != null && !returnTypeFromRp.isAssignableFrom(returnTypeFromMethod)) {
					throw new ConfigurationException(Msg.code(395) + "Method '" + theMethod.getName() + "' in type " + theMethod.getDeclaringClass().getCanonicalName() + " returns type "
						+ returnTypeFromMethod.getCanonicalName() + " - Must return " + returnTypeFromRp.getCanonicalName() + " (or a subclass of it) per IResourceProvider contract");
				}
				if (!returnTypeFromRp.isAssignableFrom(returnTypeFromAnnotation)) {
					throw new ConfigurationException(Msg.code(396) + "Method '" + theMethod.getName() + "' in type " + theMethod.getDeclaringClass().getCanonicalName() + " claims to return type " + returnTypeFromAnnotation.getCanonicalName()
							+ " per method annotation - Must return " + returnTypeFromRp.getCanonicalName() + " (or a subclass of it) per IResourceProvider contract");
				}
				returnType = returnTypeFromAnnotation;
			} else {
				returnType = returnTypeFromRp;
			}
		} else {
			if (!isResourceInterface(returnTypeFromAnnotation)) {
				if (!verifyIsValidResourceReturnType(returnTypeFromAnnotation)) {
					throw new ConfigurationException(Msg.code(397) + "Method '" + theMethod.getName() + "' from " + IResourceProvider.class.getSimpleName() + " type " + theMethod.getDeclaringClass().getCanonicalName()
						+ " returns " + toLogString(returnTypeFromAnnotation) + " according to annotation - Must return a resource type");
				}
				returnType = returnTypeFromAnnotation;
			} else {
				returnType = (Class<? extends IBaseResource>) returnTypeFromMethod;
			}
		}

		if (read != null) {
			return new ReadMethodBinding(returnType, theMethod, theContext, theProvider);
		} else if (search != null) {
			return new SearchMethodBinding(returnType, returnTypeFromRp, theMethod, theContext, theProvider);
		} else if (conformance != null) {
			return new ConformanceMethodBinding(theMethod, theContext, theProvider);
		} else if (create != null) {
			return new CreateMethodBinding(theMethod, theContext, theProvider);
		} else if (update != null) {
			return new UpdateMethodBinding(theMethod, theContext, theProvider);
		} else if (delete != null) {
			return new DeleteMethodBinding(theMethod, theContext, theProvider);
		} else if (patch != null) {
			return new PatchMethodBinding(theMethod, theContext, theProvider);
		} else if (history != null) {
			return new HistoryMethodBinding(theMethod, theContext, theProvider);
		} else if (validate != null) {
			return new ValidateMethodBindingDstu2Plus(returnType, returnTypeFromRp, theMethod, theContext, theProvider, validate);
		} else if (transaction != null) {
			return new TransactionMethodBinding(theMethod, theContext, theProvider);
		} else if (operation != null) {
			return new OperationMethodBinding(returnType, returnTypeFromRp, theMethod, theContext, theProvider, operation);
		} else {
			throw new ConfigurationException(Msg.code(398) + "Did not detect any FHIR annotations on method '" + theMethod.getName() + "' on type: " + theMethod.getDeclaringClass().getCanonicalName());
		}

	}

	private static boolean isResourceInterface(Class<?> theReturnTypeFromMethod) {
		return theReturnTypeFromMethod != null && (theReturnTypeFromMethod.equals(IBaseResource.class) || theReturnTypeFromMethod.equals(IResource.class) || theReturnTypeFromMethod.equals(IAnyResource.class));
	}

	private static String toLogString(Class<?> theType) {
		if (theType == null) {
			return null;
		}
		return theType.getCanonicalName();
	}

	private static boolean verifyIsValidResourceReturnType(Class<?> theReturnType) {
		if (theReturnType == null) {
			return false;
		}
		if (!IBaseResource.class.isAssignableFrom(theReturnType)) {
			return false;
		}
		return true;
	}

	public static boolean verifyMethodHasZeroOrOneOperationAnnotation(Method theNextMethod, Object... theAnnotations) {
		Object obj1 = null;
		for (Object object : theAnnotations) {
			if (object != null) {
				if (obj1 == null) {
					obj1 = object;
				} else {
					throw new ConfigurationException(Msg.code(399) + "Method " + theNextMethod.getName() + " on type '" + theNextMethod.getDeclaringClass().getSimpleName() + " has annotations @"
						+ obj1.getClass().getSimpleName() + " and @" + object.getClass().getSimpleName() + ". Can not have both.");
				}

			}
		}
		if (obj1 == null) {
			return false;
		}
		return true;
	}

}
