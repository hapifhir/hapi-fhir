package ca.uhn.fhir.rest.method;

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
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.io.IOUtils;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.base.resource.BaseOperationOutcome;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.client.BaseHttpClientInvocation;
import ca.uhn.fhir.rest.client.exceptions.NonFhirResponseException;
import ca.uhn.fhir.rest.server.BundleProviders;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.IBundleProvider;
import ca.uhn.fhir.rest.server.IDynamicSearchResourceProvider;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.IRestfulServer;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.MethodNotAllowedException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.rest.server.exceptions.UnclassifiedServerFailureException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor.ActionRequestDetails;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.ReflectionUtil;

public abstract class BaseMethodBinding<T> implements IClientResponseHandler<T> {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseMethodBinding.class);
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

		for (IParameter next : myParameters) {
			if (next instanceof ConditionalParamBinder) {
				mySupportsConditional = true;
				if (((ConditionalParamBinder) next).isSupportsMultiple()) {
					mySupportsConditionalMultiple = true;
				}
				break;
			}
		}

	}

	protected IParser createAppropriateParserForParsingResponse(String theResponseMimeType, Reader theResponseReader, int theResponseStatusCode, List<Class<? extends IBaseResource>> thePreferTypes) {
		EncodingEnum encoding = EncodingEnum.forContentType(theResponseMimeType);
		if (encoding == null) {
			NonFhirResponseException ex = NonFhirResponseException.newInstance(theResponseStatusCode, theResponseMimeType, theResponseReader);
			populateException(ex, theResponseReader);
			throw ex;
		}

		IParser parser = encoding.newParser(getContext());
		
		parser.setPreferTypes(thePreferTypes);
		
		return parser;
	}

	protected IParser createAppropriateParserForParsingServerRequest(RequestDetails theRequest) {
		String contentTypeHeader = theRequest.getHeader(Constants.HEADER_CONTENT_TYPE);
		EncodingEnum encoding;
		if (isBlank(contentTypeHeader)) {
			encoding = EncodingEnum.XML;
		} else {
			int semicolon = contentTypeHeader.indexOf(';');
			if (semicolon != -1) {
				contentTypeHeader = contentTypeHeader.substring(0, semicolon);
			}
			encoding = EncodingEnum.forContentType(contentTypeHeader);
		}

		if (encoding == null) {
			throw new InvalidRequestException("Request contins non-FHIR conent-type header value: " + contentTypeHeader);
		}

		IParser parser = encoding.newParser(getContext());
		return parser;
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

	public List<Class<?>> getAllowableParamAnnotations() {
		return null;
	}

	public FhirContext getContext() {
		return myContext;
	}

	public Set<String> getIncludes() {
		Set<String> retVal = new TreeSet<String>();
		for (IParameter next : myParameters) {
			if (next instanceof IncludeParameter) {
				retVal.addAll(((IncludeParameter) next).getAllow());
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

	public Object getProvider() {
		return myProvider;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
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

	public abstract RestOperationTypeEnum getRestOperationType();

	/**
	 * Determine which operation is being fired for a specific request
	 * 
	 * @param theRequestDetails
	 *           The request
	 */
	public RestOperationTypeEnum getRestOperationType(RequestDetails theRequestDetails) {
		return getRestOperationType();
	}

	public abstract boolean incomingServerRequestMatchesMethod(RequestDetails theRequest);

	public abstract BaseHttpClientInvocation invokeClient(Object[] theArgs) throws InternalErrorException;

	public abstract Object invokeServer(IRestfulServer<?> theServer, RequestDetails theRequest) throws BaseServerResponseException, IOException;

	protected final Object invokeServerMethod(IRestfulServer<?> theServer, RequestDetails theRequest, Object[] theMethodParams) {
		// Handle server action interceptors
		RestOperationTypeEnum operationType = getRestOperationType(theRequest);
		if (operationType != null) {
			for (IServerInterceptor next : theServer.getInterceptors()) {
				ActionRequestDetails details = new ActionRequestDetails(theRequest);
				populateActionRequestDetailsForInterceptor(theRequest, details, theMethodParams);
				next.incomingRequestPreHandled(operationType, details);
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
			throw new InternalErrorException("Failed to call access method", e);
		} catch (Exception e) {
			throw new InternalErrorException("Failed to call access method", e);
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
	 * Subclasses may override this method (but should also call super.{@link #populateActionRequestDetailsForInterceptor(RequestDetails, ActionRequestDetails, Object[])} to provide method specifics to the
	 * interceptors.
	 * 
	 * @param theRequestDetails
	 *           The server request details
	 * @param theDetails
	 *           The details object to populate
	 * @param theMethodParams
	 *           The method params as generated by the specific method binding
	 */
	protected void populateActionRequestDetailsForInterceptor(RequestDetails theRequestDetails, ActionRequestDetails theDetails, Object[] theMethodParams) {
		// nothing by default
	}

	protected BaseServerResponseException processNon2xxResponseAndReturnExceptionToThrow(int theStatusCode, String theResponseMimeType, Reader theResponseReader) {
		BaseServerResponseException ex;
		switch (theStatusCode) {
		case Constants.STATUS_HTTP_400_BAD_REQUEST:
			ex = new InvalidRequestException("Server responded with HTTP 400");
			break;
		case Constants.STATUS_HTTP_404_NOT_FOUND:
			ex = new ResourceNotFoundException("Server responded with HTTP 404");
			break;
		case Constants.STATUS_HTTP_405_METHOD_NOT_ALLOWED:
			ex = new MethodNotAllowedException("Server responded with HTTP 405");
			break;
		case Constants.STATUS_HTTP_409_CONFLICT:
			ex = new ResourceVersionConflictException("Server responded with HTTP 409");
			break;
		case Constants.STATUS_HTTP_412_PRECONDITION_FAILED:
			ex = new PreconditionFailedException("Server responded with HTTP 412");
			break;
		case Constants.STATUS_HTTP_422_UNPROCESSABLE_ENTITY:
			IParser parser = createAppropriateParserForParsingResponse(theResponseMimeType, theResponseReader, theStatusCode, null);
			// TODO: handle if something other than OO comes back
			BaseOperationOutcome operationOutcome = (BaseOperationOutcome) parser.parseResource(theResponseReader);
			ex = new UnprocessableEntityException(myContext, operationOutcome);
			break;
		default:
			ex = new UnclassifiedServerFailureException(theStatusCode, "Server responded with HTTP " + theStatusCode);
			break;
		}

		populateException(ex, theResponseReader);
		return ex;
	}

	/** For unit tests only */
	public void setParameters(List<IParameter> theParameters) {
		myParameters = theParameters;
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
			throw new InternalErrorException("Unexpected return type: " + response.getClass().getCanonicalName());
		}
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
		GetTags getTags = theMethod.getAnnotation(GetTags.class);
		AddTags addTags = theMethod.getAnnotation(AddTags.class);
		DeleteTags deleteTags = theMethod.getAnnotation(DeleteTags.class);
		Transaction transaction = theMethod.getAnnotation(Transaction.class);
		Operation operation = theMethod.getAnnotation(Operation.class);
		GetPage getPage = theMethod.getAnnotation(GetPage.class);
		Patch patch = theMethod.getAnnotation(Patch.class);

		// ** if you add another annotation above, also add it to the next line:
		if (!verifyMethodHasZeroOrOneOperationAnnotation(theMethod, read, search, conformance, create, update, delete, history, validate, getTags, addTags, deleteTags, transaction, operation, getPage, patch)) {
			return null;
		}

		if (getPage != null) {
			return new PageMethodBinding(theContext, theMethod);
		}
		
		Class<? extends IBaseResource> returnType;

		Class<? extends IBaseResource> returnTypeFromRp = null;
		if (theProvider instanceof IResourceProvider) {
			returnTypeFromRp = ((IResourceProvider) theProvider).getResourceType();
			if (!verifyIsValidResourceReturnType(returnTypeFromRp)) {
				throw new ConfigurationException("getResourceType() from " + IResourceProvider.class.getSimpleName() + " type " + theMethod.getDeclaringClass().getCanonicalName() + " returned "
						+ toLogString(returnTypeFromRp) + " - Must return a resource type");
			}
		}

		Class<?> returnTypeFromMethod = theMethod.getReturnType();
		if (getTags != null) {
			if (!TagList.class.equals(returnTypeFromMethod)) {
				throw new ConfigurationException("Method '" + theMethod.getName() + "' from type " + theMethod.getDeclaringClass().getCanonicalName() + " is annotated with @"
						+ GetTags.class.getSimpleName() + " but does not return type " + TagList.class.getName());
			}
		} else if (MethodOutcome.class.isAssignableFrom(returnTypeFromMethod)) {
			// returns a method outcome
		} else if (IBundleProvider.class.equals(returnTypeFromMethod)) {
			// returns a bundle provider
		} else if (Bundle.class.equals(returnTypeFromMethod)) {
			// returns a bundle
		} else if (void.class.equals(returnTypeFromMethod)) {
			// returns a bundle
		} else if (Collection.class.isAssignableFrom(returnTypeFromMethod)) {
			returnTypeFromMethod = ReflectionUtil.getGenericCollectionTypeOfMethodReturnType(theMethod);
			if (returnTypeFromMethod == null) {
				ourLog.trace("Method {} returns a non-typed list, can't verify return type", theMethod);
			} else if (!verifyIsValidResourceReturnType(returnTypeFromMethod) && !isResourceInterface(returnTypeFromMethod)) {
				throw new ConfigurationException("Method '" + theMethod.getName() + "' from " + IResourceProvider.class.getSimpleName() + " type " + theMethod.getDeclaringClass().getCanonicalName()
						+ " returns a collection with generic type " + toLogString(returnTypeFromMethod)
						+ " - Must return a resource type or a collection (List, Set) with a resource type parameter (e.g. List<Patient> or List<IBaseResource> )");
			}
		} else {
			if (!isResourceInterface(returnTypeFromMethod) && !verifyIsValidResourceReturnType(returnTypeFromMethod)) {
				throw new ConfigurationException("Method '" + theMethod.getName() + "' from " + IResourceProvider.class.getSimpleName() + " type " + theMethod.getDeclaringClass().getCanonicalName()
						+ " returns " + toLogString(returnTypeFromMethod) + " - Must return a resource type (eg Patient, " + Bundle.class.getSimpleName() + ", " + IBundleProvider.class.getSimpleName()
						+ ", etc., see the documentation for more details)");
			}
		}

		Class<? extends IBaseResource> returnTypeFromAnnotation = IBaseResource.class;
		if (read != null) {
			returnTypeFromAnnotation = read.type();
		} else if (search != null) {
			returnTypeFromAnnotation = search.type();
		} else if (history != null) {
			returnTypeFromAnnotation = history.type();
		} else if (delete != null) {
			returnTypeFromAnnotation = delete.type();
		} else if (patch != null) {
			returnTypeFromAnnotation = patch.type();
		} else if (create != null) {
			returnTypeFromAnnotation = create.type();
		} else if (update != null) {
			returnTypeFromAnnotation = update.type();
		} else if (validate != null) {
			returnTypeFromAnnotation = validate.type();
		} else if (getTags != null) {
			returnTypeFromAnnotation = getTags.type();
		} else if (addTags != null) {
			returnTypeFromAnnotation = addTags.type();
		} else if (deleteTags != null) {
			returnTypeFromAnnotation = deleteTags.type();
		}

		if (returnTypeFromRp != null) {
			if (returnTypeFromAnnotation != null && !isResourceInterface(returnTypeFromAnnotation)) {
				if (!returnTypeFromRp.isAssignableFrom(returnTypeFromAnnotation)) {
					//FIXME potential null access on retunrTypeFromMethod
					throw new ConfigurationException("Method '" + theMethod.getName() + "' in type " + theMethod.getDeclaringClass().getCanonicalName() + " returns type "
							+ returnTypeFromMethod.getCanonicalName() + " - Must return " + returnTypeFromRp.getCanonicalName() + " (or a subclass of it) per IResourceProvider contract");
				}
				if (!returnTypeFromRp.isAssignableFrom(returnTypeFromAnnotation)) {
					throw new ConfigurationException(
							"Method '" + theMethod.getName() + "' in type " + theMethod.getDeclaringClass().getCanonicalName() + " claims to return type " + returnTypeFromAnnotation.getCanonicalName()
									+ " per method annotation - Must return " + returnTypeFromRp.getCanonicalName() + " (or a subclass of it) per IResourceProvider contract");
				}
				returnType = returnTypeFromAnnotation;
			} else {
				returnType = returnTypeFromRp;
			}
		} else {
			if (!isResourceInterface(returnTypeFromAnnotation)) {
				if (!verifyIsValidResourceReturnType(returnTypeFromAnnotation)) {
					throw new ConfigurationException("Method '" + theMethod.getName() + "' from " + IResourceProvider.class.getSimpleName() + " type " + theMethod.getDeclaringClass().getCanonicalName()
							+ " returns " + toLogString(returnTypeFromAnnotation) + " according to annotation - Must return a resource type");
				}
				returnType = returnTypeFromAnnotation;
			} else {
				// if (IRestfulClient.class.isAssignableFrom(theMethod.getDeclaringClass())) {
				// Clients don't define their methods in resource specific types, so they can
				// infer their resource type from the method return type.
				returnType = (Class<? extends IBaseResource>) returnTypeFromMethod;
				// } else {
				// This is a plain provider method returning a resource, so it should be
				// an operation or global search presumably
				// returnType = null;
			}
		}

		if (read != null) {
			return new ReadMethodBinding(returnType, theMethod, theContext, theProvider);
		} else if (search != null) {
			if (search.dynamic()) {
				IDynamicSearchResourceProvider provider = (IDynamicSearchResourceProvider) theProvider;
				return new DynamicSearchMethodBinding(returnType, theMethod, theContext, provider);
			}
			return new SearchMethodBinding(returnType, theMethod, theContext, theProvider);
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
			if (theContext.getVersion().getVersion() == FhirVersionEnum.DSTU1) {
				return new ValidateMethodBindingDstu1(theMethod, theContext, theProvider);
			}
			return new ValidateMethodBindingDstu2Plus(returnType, returnTypeFromRp, theMethod, theContext, theProvider, validate);
		} else if (getTags != null) {
			return new GetTagsMethodBinding(theMethod, theContext, theProvider, getTags);
		} else if (addTags != null) {
			return new AddTagsMethodBinding(theMethod, theContext, theProvider, addTags);
		} else if (deleteTags != null) {
			return new DeleteTagsMethodBinding(theMethod, theContext, theProvider, deleteTags);
		} else if (transaction != null) {
			return new TransactionMethodBinding(theMethod, theContext, theProvider);
		} else if (operation != null) {
			return new OperationMethodBinding(returnType, returnTypeFromRp, theMethod, theContext, theProvider, operation);
		} else {
			throw new ConfigurationException("Did not detect any FHIR annotations on method '" + theMethod.getName() + "' on type: " + theMethod.getDeclaringClass().getCanonicalName());
		}

		// // each operation name must have a request type annotation and be
		// unique
		// if (null != read) {
		// return rm;
		// }
		//
		// SearchMethodBinding sm = new SearchMethodBinding();
		// if (null != search) {
		// sm.setRequestType(SearchMethodBinding.RequestType.GET);
		// } else if (null != theMethod.getAnnotation(PUT.class)) {
		// sm.setRequestType(SearchMethodBinding.RequestType.PUT);
		// } else if (null != theMethod.getAnnotation(POST.class)) {
		// sm.setRequestType(SearchMethodBinding.RequestType.POST);
		// } else if (null != theMethod.getAnnotation(DELETE.class)) {
		// sm.setRequestType(SearchMethodBinding.RequestType.DELETE);
		// } else {
		// return null;
		// }
		//
		// return sm;
	}

	private static boolean isResourceInterface(Class<?> theReturnTypeFromMethod) {
		return theReturnTypeFromMethod.equals(IBaseResource.class) || theReturnTypeFromMethod.equals(IResource.class) || theReturnTypeFromMethod.equals(IAnyResource.class);
	}

	private static void populateException(BaseServerResponseException theEx, Reader theResponseReader) {
		try {
			String responseText = IOUtils.toString(theResponseReader);
			theEx.setResponseBody(responseText);
		} catch (IOException e) {
			ourLog.debug("Failed to read response", e);
		}
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
		// boolean retVal = Modifier.isAbstract(theReturnType.getModifiers()) == false;
		// return retVal;
	}

	public static boolean verifyMethodHasZeroOrOneOperationAnnotation(Method theNextMethod, Object... theAnnotations) {
		Object obj1 = null;
		for (Object object : theAnnotations) {
			if (object != null) {
				if (obj1 == null) {
					obj1 = object;
				} else {
					throw new ConfigurationException("Method " + theNextMethod.getName() + " on type '" + theNextMethod.getDeclaringClass().getSimpleName() + " has annotations @"
							+ obj1.getClass().getSimpleName() + " and @" + object.getClass().getSimpleName() + ". Can not have both.");
				}

			}
		}
		if (obj1 == null) {
			return false;
			// throw new ConfigurationException("Method '" +
			// theNextMethod.getName() + "' on type '" +
			// theNextMethod.getDeclaringClass().getSimpleName() +
			// " has no FHIR method annotations.");
		}
		return true;
	}

	/**
	 * @see ServletRequestDetails#getByteStreamRequestContents()
	 */
	public static class ActiveRequestReader implements IRequestReader {
		@Override
		public InputStream getInputStream(RequestDetails theRequestDetails) throws IOException {
			return theRequestDetails.getInputStream();
		}
	}

	/**
	 * @see ServletRequestDetails#getByteStreamRequestContents()
	 */
	public static class InactiveRequestReader implements IRequestReader {
		@Override
		public InputStream getInputStream(RequestDetails theRequestDetails) {
			throw new IllegalStateException("The servlet-api JAR is not found on the classpath. Please check that this library is available.");
		}
	}

	/**
	 * @see ServletRequestDetails#getByteStreamRequestContents()
	 */
	public static interface IRequestReader {
		InputStream getInputStream(RequestDetails theRequestDetails) throws IOException;
	}

}
