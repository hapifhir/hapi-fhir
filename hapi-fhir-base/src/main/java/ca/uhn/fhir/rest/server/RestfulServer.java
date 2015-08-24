package ca.uhn.fhir.rest.server;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.ProvidedResourceScanner;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.Destroy;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Initialize;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.SummaryEnum;
import ca.uhn.fhir.rest.method.BaseMethodBinding;
import ca.uhn.fhir.rest.method.ConformanceMethodBinding;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.NotModifiedException;
import ca.uhn.fhir.rest.server.interceptor.ExceptionHandlingInterceptor;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;
import ca.uhn.fhir.util.ReflectionUtil;
import ca.uhn.fhir.util.UrlUtil;
import ca.uhn.fhir.util.VersionUtil;

public class RestfulServer extends HttpServlet {

	private static final ExceptionHandlingInterceptor DEFAULT_EXCEPTION_HANDLER = new ExceptionHandlingInterceptor();
	private static final long serialVersionUID = 1L;
	/**
	 * Default setting for {@link #setETagSupport(ETagSupportEnum) ETag Support}: {@link ETagSupportEnum#ENABLED}
	 */
	public static final ETagSupportEnum DEFAULT_ETAG_SUPPORT = ETagSupportEnum.ENABLED;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(RestfulServer.class);
	private AddProfileTagEnum myAddProfileTag;
	private BundleInclusionRule myBundleInclusionRule = BundleInclusionRule.BASED_ON_INCLUDES;
	private boolean myDefaultPrettyPrint = false;
	private EncodingEnum myDefaultResponseEncoding = EncodingEnum.XML;
	private ETagSupportEnum myETagSupport = DEFAULT_ETAG_SUPPORT;
	private FhirContext myFhirContext;
	private String myImplementationDescription;
	private final List<IServerInterceptor> myInterceptors = new ArrayList<IServerInterceptor>();
	private IPagingProvider myPagingProvider;
	private Collection<Object> myPlainProviders;
	private Map<String, ResourceBinding> myResourceNameToBinding = new HashMap<String, ResourceBinding>();
	private Collection<IResourceProvider> myResourceProviders;
	private IServerAddressStrategy myServerAddressStrategy = new IncomingRequestAddressStrategy();
	private ResourceBinding myServerBinding = new ResourceBinding();
	private BaseMethodBinding<?> myServerConformanceMethod;
	private Object myServerConformanceProvider;
	private String myServerName = "HAPI FHIR Server";
	/** This is configurable but by default we just use HAPI version */
	private String myServerVersion = VersionUtil.getVersion();
	private boolean myStarted;
	private boolean myUseBrowserFriendlyContentTypes;

	/**
	 * Constructor. Note that if no {@link FhirContext} is passed in to the server (either through the constructor, or through {@link #setFhirContext(FhirContext)}) the server will determine which
	 * version of FHIR to support through classpath scanning. This is brittle, and it is highly recommended to explicitly specify a FHIR version.
	 */
	public RestfulServer() {
		this(null);
	}

	/**
	 * Constructor
	 */
	public RestfulServer(FhirContext theCtx) {
		myFhirContext = theCtx;
	}

	/**
	 * This method is called prior to sending a response to incoming requests. It is used to add custom headers.
	 * <p>
	 * Use caution if overriding this method: it is recommended to call <code>super.addHeadersToResponse</code> to avoid inadvertantly disabling functionality.
	 * </p>
	 */
	public void addHeadersToResponse(HttpServletResponse theHttpResponse) {
		theHttpResponse.addHeader("X-Powered-By", "HAPI FHIR " + VersionUtil.getVersion() + " RESTful Server");
	}

	private void assertProviderIsValid(Object theNext) throws ConfigurationException {
		if (Modifier.isPublic(theNext.getClass().getModifiers()) == false) {
			throw new ConfigurationException("Can not use provider '" + theNext.getClass() + "' - Class must be public");
		}
	}

	@Override
	public void destroy() {
		if (getResourceProviders() != null) {
			for (IResourceProvider iResourceProvider : getResourceProviders()) {
				invokeDestroy(iResourceProvider);
			}
		}
		if (myServerConformanceProvider != null) {
			invokeDestroy(myServerConformanceProvider);
		}
		if (getPlainProviders() != null) {
			for (Object next : getPlainProviders()) {
				invokeDestroy(next);
			}
		}
	}

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		handleRequest(RequestTypeEnum.DELETE, request, response);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		handleRequest(RequestTypeEnum.GET, request, response);
	}

	@Override
	protected void doOptions(HttpServletRequest theReq, HttpServletResponse theResp) throws ServletException, IOException {
		handleRequest(RequestTypeEnum.OPTIONS, theReq, theResp);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		handleRequest(RequestTypeEnum.POST, request, response);
	}

	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		handleRequest(RequestTypeEnum.PUT, request, response);
	}

	/**
	 * Count length of URL string, but treating unescaped sequences (e.g. ' ') as their unescaped equivalent (%20)
	 */
	protected int escapedLength(String theServletPath) {
		int delta = 0;
		for (int i = 0; i < theServletPath.length(); i++) {
			char next = theServletPath.charAt(i);
			if (next == ' ') {
				delta = delta + 2;
			}
		}
		return theServletPath.length() + delta;
	}

	private void findResourceMethods(Object theProvider) throws Exception {

		ourLog.info("Scanning type for RESTful methods: {}", theProvider.getClass());
		int count = 0;

		Class<?> clazz = theProvider.getClass();
		Class<?> supertype = clazz.getSuperclass();
		while (!Object.class.equals(supertype)) {
			count += findResourceMethods(theProvider, supertype);
			supertype = supertype.getSuperclass();
		}

		count += findResourceMethods(theProvider, clazz);

		if (count == 0) {
			throw new ConfigurationException("Did not find any annotated RESTful methods on provider class " + theProvider.getClass().getCanonicalName());
		}
	}

	private int findResourceMethods(Object theProvider, Class<?> clazz) throws ConfigurationException {
		int count = 0;

		for (Method m : ReflectionUtil.getDeclaredMethods(clazz)) {
			BaseMethodBinding<?> foundMethodBinding = BaseMethodBinding.bindMethod(m, getFhirContext(), theProvider);
			if (foundMethodBinding == null) {
				continue;
			}

			count++;

			if (foundMethodBinding instanceof ConformanceMethodBinding) {
				myServerConformanceMethod = foundMethodBinding;
				continue;
			}

			if (!Modifier.isPublic(m.getModifiers())) {
				throw new ConfigurationException("Method '" + m.getName() + "' is not public, FHIR RESTful methods must be public");
			} else {
				if (Modifier.isStatic(m.getModifiers())) {
					throw new ConfigurationException("Method '" + m.getName() + "' is static, FHIR RESTful methods must not be static");
				} else {
					ourLog.debug("Scanning public method: {}#{}", theProvider.getClass(), m.getName());

					String resourceName = foundMethodBinding.getResourceName();
					ResourceBinding resourceBinding;
					if (resourceName == null) {
						resourceBinding = myServerBinding;
					} else {
						RuntimeResourceDefinition definition = getFhirContext().getResourceDefinition(resourceName);
						if (myResourceNameToBinding.containsKey(definition.getName())) {
							resourceBinding = myResourceNameToBinding.get(definition.getName());
						} else {
							resourceBinding = new ResourceBinding();
							resourceBinding.setResourceName(resourceName);
							myResourceNameToBinding.put(resourceName, resourceBinding);
						}
					}

					List<Class<?>> allowableParams = foundMethodBinding.getAllowableParamAnnotations();
					if (allowableParams != null) {
						for (Annotation[] nextParamAnnotations : m.getParameterAnnotations()) {
							for (Annotation annotation : nextParamAnnotations) {
								Package pack = annotation.annotationType().getPackage();
								if (pack.equals(IdParam.class.getPackage())) {
									if (!allowableParams.contains(annotation.annotationType())) {
										throw new ConfigurationException("Method[" + m.toString() + "] is not allowed to have a parameter annotated with " + annotation);
									}
								}
							}
						}
					}

					resourceBinding.addMethod(foundMethodBinding);
					ourLog.debug(" * Method: {}#{} is a handler", theProvider.getClass(), m.getName());
				}
			}
		}

		return count;
	}

	/**
	 * Returns the setting for automatically adding profile tags
	 *
	 * @see #setAddProfileTag(AddProfileTagEnum)
	 */
	public AddProfileTagEnum getAddProfileTag() {
		return myAddProfileTag;
	}

	public BundleInclusionRule getBundleInclusionRule() {
		return myBundleInclusionRule;
	}

	/**
	 * Returns the default encoding to return (XML/JSON) if an incoming request does not specify a preference (either with the <code>_format</code> URL parameter, or with an <code>Accept</code> header
	 * in the request. The default is {@link EncodingEnum#XML}.
	 */
	public EncodingEnum getDefaultResponseEncoding() {
		return myDefaultResponseEncoding;
	}

	/**
	 * Returns the server support for ETags (will not be <code>null</code>). Default is {@link #DEFAULT_ETAG_SUPPORT}
	 */
	public ETagSupportEnum getETagSupport() {
		return myETagSupport;
	}

	/**
	 * Gets the {@link FhirContext} associated with this server. For efficient processing, resource providers and plain providers should generally use this context if one is needed, as opposed to
	 * creating their own.
	 */
	public FhirContext getFhirContext() {
		if (myFhirContext == null) {
			myFhirContext = new FhirContext();
		}
		return myFhirContext;
	}

	public String getImplementationDescription() {
		return myImplementationDescription;
	}

	/**
	 * Returns a ist of all registered server interceptors
	 */
	public List<IServerInterceptor> getInterceptors() {
		return Collections.unmodifiableList(myInterceptors);
	}

	public IPagingProvider getPagingProvider() {
		return myPagingProvider;
	}

	/**
	 * Provides the non-resource specific providers which implement method calls on this server
	 *
	 * @see #getResourceProviders()
	 */
	public Collection<Object> getPlainProviders() {
		return myPlainProviders;
	}

	/**
	 * Allows users of RestfulServer to override the getRequestPath method to let them build their custom request path implementation
	 *
	 * @param requestFullPath
	 *           the full request path
	 * @param servletContextPath
	 *           the servelet context path
	 * @param servletPath
	 *           the servelet path
	 * @return created resource path
	 */
	protected String getRequestPath(String requestFullPath, String servletContextPath, String servletPath) {
		return requestFullPath.substring(escapedLength(servletContextPath) + escapedLength(servletPath));
	}

	public Collection<ResourceBinding> getResourceBindings() {
		return myResourceNameToBinding.values();
	}

	/**
	 * Provides the resource providers for this server
	 */
	public Collection<IResourceProvider> getResourceProviders() {
		return myResourceProviders;
	}

	/**
	 * Get the server address strategy, which is used to determine what base URL to provide clients to refer to this server. Defaults to an instance of {@link IncomingRequestAddressStrategy}
	 */
	public IServerAddressStrategy getServerAddressStrategy() {
		return myServerAddressStrategy;
	}

	/**
	 * Returns the server base URL (with no trailing '/') for a given request
	 */
	public String getServerBaseForRequest(HttpServletRequest theRequest) {
		String fhirServerBase;
		fhirServerBase = myServerAddressStrategy.determineServerBase(getServletContext(), theRequest);

		if (fhirServerBase.endsWith("/")) {
			fhirServerBase = fhirServerBase.substring(0, fhirServerBase.length() - 1);
		}
		return fhirServerBase;
	}

	/**
	 * Returns the method bindings for this server which are not specific to any particular resource type. This method is internal to HAPI and developers generally do not need to interact with it. Use
	 * with caution, as it may change.
	 */
	public List<BaseMethodBinding<?>> getServerBindings() {
		return myServerBinding.getMethodBindings();
	}

	/**
	 * Returns the server conformance provider, which is the provider that is used to generate the server's conformance (metadata) statement if one has been explicitly defined.
	 * <p>
	 * By default, the ServerConformanceProvider for the declared version of FHIR is used, but this can be changed, or set to <code>null</code> to use the appropriate one for the given FHIR version.
	 * </p>
	 */
	public Object getServerConformanceProvider() {
		return myServerConformanceProvider;
	}

	/**
	 * Gets the server's name, as exported in conformance profiles exported by the server. This is informational only, but can be helpful to set with something appropriate.
	 *
	 * @see RestfulServer#setServerName(String)
	 */
	public String getServerName() {
		return myServerName;
	}

	public IResourceProvider getServerProfilesProvider() {
		return getFhirContext().getVersion().createServerProfilesProvider(this);
	}

	/**
	 * Gets the server's version, as exported in conformance profiles exported by the server. This is informational only, but can be helpful to set with something appropriate.
	 */
	public String getServerVersion() {
		return myServerVersion;
	}

	private void handlePagingRequest(RequestDetails theRequest, HttpServletResponse theResponse, String thePagingAction) throws IOException {
		IBundleProvider resultList = getPagingProvider().retrieveResultList(thePagingAction);
		if (resultList == null) {
			ourLog.info("Client requested unknown paging ID[{}]", thePagingAction);
			theResponse.setStatus(Constants.STATUS_HTTP_410_GONE);
			addHeadersToResponse(theResponse);
			theResponse.setContentType("text/plain");
			theResponse.setCharacterEncoding("UTF-8");
			theResponse.getWriter().append("Search ID[" + thePagingAction + "] does not exist and may have expired.");
			theResponse.getWriter().close();
			return;
		}

		Integer count = RestfulServerUtils.extractCountParameter(theRequest.getServletRequest());
		if (count == null) {
			count = getPagingProvider().getDefaultPageSize();
		} else if (count > getPagingProvider().getMaximumPageSize()) {
			count = getPagingProvider().getMaximumPageSize();
		}

		Integer offsetI = RestfulServerUtils.tryToExtractNamedParameter(theRequest.getServletRequest(), Constants.PARAM_PAGINGOFFSET);
		if (offsetI == null || offsetI < 0) {
			offsetI = 0;
		}

		int start = Math.min(offsetI, resultList.size() - 1);

		EncodingEnum responseEncoding = RestfulServerUtils.determineResponseEncodingNoDefault(theRequest.getServletRequest());
		boolean prettyPrint = RestfulServerUtils.prettyPrintResponse(this, theRequest);
		boolean requestIsBrowser = requestIsBrowser(theRequest.getServletRequest());
		Set<SummaryEnum> summaryMode = RestfulServerUtils.determineSummaryMode(theRequest);
		boolean respondGzip = theRequest.isRespondGzip();

		IVersionSpecificBundleFactory bundleFactory = getFhirContext().newBundleFactory();

		Set<Include> includes = new HashSet<Include>();
		String[] reqIncludes = theRequest.getServletRequest().getParameterValues(Constants.PARAM_INCLUDE);
		if (reqIncludes != null) {
			for (String nextInclude : reqIncludes) {
				includes.add(new Include(nextInclude));
			}
		}

		String linkSelfBase = getServerAddressStrategy().determineServerBase(getServletContext(), theRequest.getServletRequest());
		String linkSelf = linkSelfBase + theRequest.getCompleteUrl().substring(theRequest.getCompleteUrl().indexOf('?'));

		bundleFactory.initializeBundleFromBundleProvider(this, resultList, responseEncoding, theRequest.getFhirServerBase(), linkSelf, prettyPrint, start, count, thePagingAction, null, includes);

		Bundle bundle = bundleFactory.getDstu1Bundle();
		if (bundle != null) {
			for (int i = getInterceptors().size() - 1; i >= 0; i--) {
				IServerInterceptor next = getInterceptors().get(i);
				boolean continueProcessing = next.outgoingResponse(theRequest, bundle, theRequest.getServletRequest(), theRequest.getServletResponse());
				if (!continueProcessing) {
					ourLog.debug("Interceptor {} returned false, not continuing processing");
					return;
				}
			}
			RestfulServerUtils.streamResponseAsBundle(this, theResponse, bundle, theRequest.getFhirServerBase(), summaryMode, respondGzip, requestIsBrowser, theRequest);
		} else {
			IBaseResource resBundle = bundleFactory.getResourceBundle();
			for (int i = getInterceptors().size() - 1; i >= 0; i--) {
				IServerInterceptor next = getInterceptors().get(i);
				boolean continueProcessing = next.outgoingResponse(theRequest, resBundle, theRequest.getServletRequest(), theRequest.getServletResponse());
				if (!continueProcessing) {
					ourLog.debug("Interceptor {} returned false, not continuing processing");
					return;
				}
			}
			RestfulServerUtils.streamResponseAsResource(this, theResponse, resBundle, prettyPrint, summaryMode, Constants.STATUS_HTTP_200_OK, theRequest.isRespondGzip(), false, theRequest);
		}
	}

	protected void handleRequest(RequestTypeEnum theRequestType, HttpServletRequest theRequest, HttpServletResponse theResponse) throws ServletException, IOException {
		String fhirServerBase = null;
		boolean requestIsBrowser = requestIsBrowser(theRequest);
		RequestDetails requestDetails = new RequestDetails();
		requestDetails.setServer(this);
		requestDetails.setRequestType(theRequestType);
		requestDetails.setServletRequest(theRequest);
		requestDetails.setServletResponse(theResponse);

		try {

			for (IServerInterceptor next : myInterceptors) {
				boolean continueProcessing = next.incomingRequestPreProcessed(theRequest, theResponse);
				if (!continueProcessing) {
					ourLog.debug("Interceptor {} returned false, not continuing processing");
					return;
				}
			}

			String resourceName = null;
			String requestFullPath = StringUtils.defaultString(theRequest.getRequestURI());
			String servletPath = StringUtils.defaultString(theRequest.getServletPath());
			StringBuffer requestUrl = theRequest.getRequestURL();
			String servletContextPath = IncomingRequestAddressStrategy.determineServletContextPath(theRequest, this);

			/*
			 * Just for debugging..
			 */
			if (ourLog.isTraceEnabled()) {
				ourLog.trace("Request FullPath: {}", requestFullPath);
				ourLog.trace("Servlet Path: {}", servletPath);
				ourLog.trace("Request Url: {}", requestUrl);
				ourLog.trace("Context Path: {}", servletContextPath);
			}

			IdDt id = null;
			String operation = null;
			String compartment = null;

			String requestPath = getRequestPath(requestFullPath, servletContextPath, servletPath);
			if (requestPath.length() > 0 && requestPath.charAt(0) == '/') {
				requestPath = requestPath.substring(1);
			}

			fhirServerBase = getServerBaseForRequest(theRequest);

			String completeUrl = StringUtils.isNotBlank(theRequest.getQueryString()) ? requestUrl + "?" + theRequest.getQueryString() : requestUrl.toString();

			Map<String, String[]> params = new HashMap<String, String[]>(theRequest.getParameterMap());
			requestDetails.setParameters(params);

			StringTokenizer tok = new StringTokenizer(requestPath, "/");
			if (tok.hasMoreTokens()) {
				resourceName = tok.nextToken();
				if (partIsOperation(resourceName)) {
					operation = resourceName;
					resourceName = null;
				}
			}
			requestDetails.setResourceName(resourceName);

			ResourceBinding resourceBinding = null;
			BaseMethodBinding<?> resourceMethod = null;
			if (Constants.URL_TOKEN_METADATA.equals(resourceName) || theRequestType == RequestTypeEnum.OPTIONS) {
				resourceMethod = myServerConformanceMethod;
			} else if (resourceName == null) {
				resourceBinding = myServerBinding;
			} else {
				resourceBinding = myResourceNameToBinding.get(resourceName);
				if (resourceBinding == null) {
					throw new InvalidRequestException("Unknown resource type '" + resourceName + "' - Server knows how to handle: " + myResourceNameToBinding.keySet());
				}
			}

			if (tok.hasMoreTokens()) {
				String nextString = tok.nextToken();
				if (partIsOperation(nextString)) {
					operation = nextString;
				} else {
					id = new IdDt(resourceName, UrlUtil.unescape(nextString));
				}
			}

			if (tok.hasMoreTokens()) {
				String nextString = tok.nextToken();
				if (nextString.equals(Constants.PARAM_HISTORY)) {
					if (tok.hasMoreTokens()) {
						String versionString = tok.nextToken();
						if (id == null) {
							throw new InvalidRequestException("Don't know how to handle request path: " + requestPath);
						}
						id = new IdDt(resourceName, id.getIdPart(), UrlUtil.unescape(versionString));
					} else {
						operation = Constants.PARAM_HISTORY;
					}
				} else if (partIsOperation(nextString)) {
					if (operation != null) {
						throw new InvalidRequestException("URL Path contains two operations: " + requestPath);
					}
					operation = nextString;
				} else {
					compartment = nextString;
				}
			}

			// Secondary is for things like ..../_tags/_delete
			String secondaryOperation = null;

			while (tok.hasMoreTokens()) {
				String nextString = tok.nextToken();
				if (operation == null) {
					operation = nextString;
				} else if (secondaryOperation == null) {
					secondaryOperation = nextString;
				} else {
					throw new InvalidRequestException("URL path has unexpected token '" + nextString + "' at the end: " + requestPath);
				}
			}

			if (theRequestType == RequestTypeEnum.PUT) {
				String contentLocation = theRequest.getHeader(Constants.HEADER_CONTENT_LOCATION);
				if (contentLocation != null) {
					id = new IdDt(contentLocation);
				}
			}
			requestDetails.setId(id);
			requestDetails.setOperation(operation);
			requestDetails.setSecondaryOperation(secondaryOperation);
			requestDetails.setCompartmentName(compartment);

			String acceptEncoding = theRequest.getHeader(Constants.HEADER_ACCEPT_ENCODING);
			boolean respondGzip = false;
			if (acceptEncoding != null) {
				String[] parts = acceptEncoding.trim().split("\\s*,\\s*");
				for (String string : parts) {
					if (string.equals("gzip")) {
						respondGzip = true;
					}
				}
			}
			requestDetails.setRespondGzip(respondGzip);
			requestDetails.setRequestPath(requestPath);
			requestDetails.setFhirServerBase(fhirServerBase);
			requestDetails.setCompleteUrl(completeUrl);

			String pagingAction = theRequest.getParameter(Constants.PARAM_PAGINGACTION);
			if (getPagingProvider() != null && isNotBlank(pagingAction)) {
				requestDetails.setRestOperationType(RestOperationTypeEnum.GET_PAGE);
				if (theRequestType != RequestTypeEnum.GET) {
					/*
					 * We reconstruct the link-self URL using the request parameters, and this would break if the parameters came in using a POST. We could probably work around that but why bother unless
					 * someone comes up with a reason for needing it.
					 */
					throw new InvalidRequestException(getFhirContext().getLocalizer().getMessage(RestfulServer.class, "getPagesNonHttpGet"));
				}
				handlePagingRequest(requestDetails, theResponse, pagingAction);
				return;
			}

			if (resourceMethod == null) {
				if (resourceBinding != null) {
					resourceMethod = resourceBinding.getMethod(requestDetails);
				}
			}
			if (resourceMethod == null) {
				if (isBlank(requestPath)) {
					throw new InvalidRequestException(myFhirContext.getLocalizer().getMessage(RestfulServer.class, "rootRequest"));
				} else {
					throw new InvalidRequestException(myFhirContext.getLocalizer().getMessage(RestfulServer.class, "unknownMethod", theRequestType.name(), requestPath, params.keySet()));
				}
			}

			requestDetails.setRestOperationType(resourceMethod.getRestOperationType());

			// Handle server interceptors
			for (IServerInterceptor next : myInterceptors) {
				boolean continueProcessing = next.incomingRequestPostProcessed(requestDetails, theRequest, theResponse);
				if (!continueProcessing) {
					ourLog.debug("Interceptor {} returned false, not continuing processing");
					return;
				}
			}

			/*
			 * Actualy invoke the server method. This call is to a HAPI method binding, which is an object that wraps a specific implementing (user-supplied) method, but handles its input and provides
			 * its output back to the client.
			 * 
			 * This is basically the end of processing for a successful request, since the method binding replies to the client and closes the response.
			 */
			resourceMethod.invokeServer(this, requestDetails);

		} catch (NotModifiedException e) {

			for (int i = getInterceptors().size() - 1; i >= 0; i--) {
				IServerInterceptor next = getInterceptors().get(i);
				if (!next.handleException(requestDetails, e, theRequest, theResponse)) {
					ourLog.debug("Interceptor {} returned false, not continuing processing");
					return;
				}
			}
			writeExceptionToResponse(theResponse, e);

		} catch (AuthenticationException e) {

			for (int i = getInterceptors().size() - 1; i >= 0; i--) {
				IServerInterceptor next = getInterceptors().get(i);
				if (!next.handleException(requestDetails, e, theRequest, theResponse)) {
					ourLog.debug("Interceptor {} returned false, not continuing processing");
					return;
				}
			}

			if (requestIsBrowser) {
				// if request is coming from a browser, prompt the user to enter login credentials
				theResponse.setHeader("WWW-Authenticate", "BASIC realm=\"FHIR\"");
			}
			writeExceptionToResponse(theResponse, e);

		} catch (Throwable e) {

			/*
			 * We have caught an exception during request processing. This might be because a handling method threw something they wanted to throw (e.g. UnprocessableEntityException because the request
			 * had business requirement problems) or it could be due to bugs (e.g. NullPointerException).
			 * 
			 * First we let the interceptors have a crack at converting the exception into something HAPI can use (BaseServerResponseException)
			 */
			BaseServerResponseException exception = null;
			for (int i = getInterceptors().size() - 1; i >= 0; i--) {
				IServerInterceptor next = getInterceptors().get(i);
				exception = next.preProcessOutgoingException(requestDetails, e, theRequest);
				if (exception != null) {
					ourLog.debug("Interceptor {} returned false, not continuing processing");
					break;
				}
			}

			/*
			 * If none of the interceptors converted the exception, default behaviour is to keep the exception as-is if it extends BaseServerResponseException, otherwise wrap it in an
			 * InternalErrorException.
			 */
			if (exception == null) {
				exception = DEFAULT_EXCEPTION_HANDLER.preProcessOutgoingException(requestDetails, e, theRequest);
			}

			/*
			 * Next, interceptors get a shot at handling the exception
			 */
			for (int i = getInterceptors().size() - 1; i >= 0; i--) {
				IServerInterceptor next = getInterceptors().get(i);
				if (!next.handleException(requestDetails, exception, theRequest, theResponse)) {
					ourLog.debug("Interceptor {} returned false, not continuing processing");
					return;
				}
			}

			/*
			 * If we're handling an exception, no summary mode should be applied
			 */
			requestDetails.getParameters().remove(Constants.PARAM_SUMMARY);
			requestDetails.getParameters().remove(Constants.PARAM_ELEMENTS);
			
			/*
			 * If nobody handles it, default behaviour is to stream back the OperationOutcome to the client.
			 */
			DEFAULT_EXCEPTION_HANDLER.handleException(requestDetails, exception, theRequest, theResponse);

		}
	}

	/**
	 * Initializes the server. Note that this method is final to avoid accidentally introducing bugs in implementations, but subclasses may put initialization code in {@link #initialize()}, which is
	 * called immediately before beginning initialization of the restful server's internal init.
	 */
	@Override
	public final void init() throws ServletException {
		initialize();

		Object confProvider;
		try {
			ourLog.info("Initializing HAPI FHIR restful server running in " + getFhirContext().getVersion().getVersion().name() + " mode");

			ProvidedResourceScanner providedResourceScanner = new ProvidedResourceScanner(getFhirContext());
			providedResourceScanner.scanForProvidedResources(this);

			Collection<IResourceProvider> resourceProvider = getResourceProviders();
			if (resourceProvider != null) {
				Map<String, IResourceProvider> typeToProvider = new HashMap<String, IResourceProvider>();
				for (IResourceProvider nextProvider : resourceProvider) {

					Class<? extends IBaseResource> resourceType = nextProvider.getResourceType();
					if (resourceType == null) {
						throw new NullPointerException("getResourceType() on class '" + nextProvider.getClass().getCanonicalName() + "' returned null");
					}

					String resourceName = getFhirContext().getResourceDefinition(resourceType).getName();
					if (typeToProvider.containsKey(resourceName)) {
						throw new ServletException("Multiple resource providers return resource type[" + resourceName + "]: First[" + typeToProvider.get(resourceName).getClass().getCanonicalName()
								+ "] and Second[" + nextProvider.getClass().getCanonicalName() + "]");
					}
					typeToProvider.put(resourceName, nextProvider);
					providedResourceScanner.scanForProvidedResources(nextProvider);
				}
				ourLog.info("Got {} resource providers", typeToProvider.size());
				for (IResourceProvider provider : typeToProvider.values()) {
					assertProviderIsValid(provider);
					findResourceMethods(provider);
				}
			}

			Collection<Object> providers = getPlainProviders();
			if (providers != null) {
				for (Object next : providers) {
					assertProviderIsValid(next);
					findResourceMethods(next);
				}
			}

			findResourceMethods(getServerProfilesProvider());

			confProvider = getServerConformanceProvider();
			if (confProvider == null) {
				confProvider = getFhirContext().getVersion().createServerConformanceProvider(this);
			}
			// findSystemMethods(confProvider);
			findResourceMethods(confProvider);

		} catch (Exception ex) {
			ourLog.error("An error occurred while loading request handlers!", ex);
			throw new ServletException("Failed to initialize FHIR Restful server", ex);
		}

		ourLog.trace("Invoking provider initialize methods");
		if (getResourceProviders() != null) {
			for (IResourceProvider iResourceProvider : getResourceProviders()) {
				invokeInitialize(iResourceProvider);
			}
		}
		if (confProvider != null) {
			invokeInitialize(confProvider);
		}
		if (getPlainProviders() != null) {
			for (Object next : getPlainProviders()) {
				invokeInitialize(next);
			}
		}

		myStarted = true;
		ourLog.info("A FHIR has been lit on this server");
	}

	/**
	 * This method may be overridden by subclasses to do perform initialization that needs to be performed prior to the server being used.
	 * 
	 * @throws ServletException
	 *            If the initialization failed. Note that you should consider throwing {@link UnavailableException} (which extends {@link ServletException}), as this is a flag to the servlet container
	 *            that the servlet is not usable.
	 */
	protected void initialize() throws ServletException {
		// nothing by default
	}

	private void invokeDestroy(Object theProvider) {
		invokeDestroy(theProvider, theProvider.getClass());
	}

	private void invokeInitialize(Object theProvider) {
		invokeInitialize(theProvider, theProvider.getClass());
	}

	private void invokeDestroy(Object theProvider, Class<?> clazz) {
		for (Method m : ReflectionUtil.getDeclaredMethods(clazz)) {
			Destroy destroy = m.getAnnotation(Destroy.class);
			if (destroy != null) {
				try {
					m.invoke(theProvider);
				} catch (IllegalAccessException e) {
					ourLog.error("Exception occurred in destroy ", e);
				} catch (InvocationTargetException e) {
					ourLog.error("Exception occurred in destroy ", e);
				}
				return;
			}
		}

		Class<?> supertype = clazz.getSuperclass();
		if (!Object.class.equals(supertype)) {
			invokeDestroy(theProvider, supertype);
		}
	}

	private void invokeInitialize(Object theProvider, Class<?> clazz) {
		for (Method m : ReflectionUtil.getDeclaredMethods(clazz)) {
			Initialize initialize = m.getAnnotation(Initialize.class);
			if (initialize != null) {
				try {
					m.invoke(theProvider);
				} catch (IllegalAccessException e) {
					ourLog.error("Exception occurred in destroy ", e);
				} catch (InvocationTargetException e) {
					ourLog.error("Exception occurred in destroy ", e);
				}
				return;
			}
		}

		Class<?> supertype = clazz.getSuperclass();
		if (!Object.class.equals(supertype)) {
			invokeInitialize(theProvider, supertype);
		}
	}

	/**
	 * Should the server "pretty print" responses by default (requesting clients can always override this default by supplying an <code>Accept</code> header in the request, or a <code>_pretty</code>
	 * parameter in the request URL.
	 * <p>
	 * The default is <code>false</code>
	 * </p>
	 * 
	 * @return Returns the default pretty print setting
	 */
	public boolean isDefaultPrettyPrint() {
		return myDefaultPrettyPrint;
	}

	public boolean isUseBrowserFriendlyContentTypes() {
		return myUseBrowserFriendlyContentTypes;
	}

	public void registerInterceptor(IServerInterceptor theInterceptor) {
		Validate.notNull(theInterceptor, "Interceptor can not be null");
		myInterceptors.add(theInterceptor);
	}

	/**
	 * Sets the profile tagging behaviour for the server. When set to a value other than {@link AddProfileTagEnum#NEVER} (which is the default), the server will automatically add a profile tag based on
	 * the class of the resource(s) being returned.
	 *
	 * @param theAddProfileTag
	 *           The behaviour enum (must not be null)
	 */
	public void setAddProfileTag(AddProfileTagEnum theAddProfileTag) {
		Validate.notNull(theAddProfileTag, "theAddProfileTag must not be null");
		myAddProfileTag = theAddProfileTag;
	}

	/**
	 * Set how bundle factory should decide whether referenced resources should be included in bundles
	 *
	 * @param theBundleInclusionRule
	 *           - inclusion rule (@see BundleInclusionRule for behaviors)
	 */
	public void setBundleInclusionRule(BundleInclusionRule theBundleInclusionRule) {
		myBundleInclusionRule = theBundleInclusionRule;
	}

	/**
	 * Should the server "pretty print" responses by default (requesting clients can always override this default by supplying an <code>Accept</code> header in the request, or a <code>_pretty</code>
	 * parameter in the request URL.
	 * <p>
	 * The default is <code>false</code>
	 * </p>
	 * 
	 * @param theDefaultPrettyPrint
	 *           The default pretty print setting
	 */
	public void setDefaultPrettyPrint(boolean theDefaultPrettyPrint) {
		myDefaultPrettyPrint = theDefaultPrettyPrint;
	}

	/**
	 * Sets the default encoding to return (XML/JSON) if an incoming request does not specify a preference (either with the <code>_format</code> URL parameter, or with an <code>Accept</code> header in
	 * the request. The default is {@link EncodingEnum#XML}.
	 * <p>
	 * Note when testing this feature: Some browsers will include "application/xml" in their Accept header, which means that the
	 * </p>
	 */
	public void setDefaultResponseEncoding(EncodingEnum theDefaultResponseEncoding) {
		Validate.notNull(theDefaultResponseEncoding, "theDefaultResponseEncoding can not be null");
		myDefaultResponseEncoding = theDefaultResponseEncoding;
	}

	/**
	 * Sets (enables/disables) the server support for ETags. Must not be <code>null</code>. Default is {@link #DEFAULT_ETAG_SUPPORT}
	 * 
	 * @param theETagSupport
	 *           The ETag support mode
	 */
	public void setETagSupport(ETagSupportEnum theETagSupport) {
		if (theETagSupport == null) {
			throw new NullPointerException("theETagSupport can not be null");
		}
		myETagSupport = theETagSupport;
	}

	public void setFhirContext(FhirContext theFhirContext) {
		Validate.notNull(theFhirContext, "FhirContext must not be null");
		myFhirContext = theFhirContext;
	}

	public void setImplementationDescription(String theImplementationDescription) {
		myImplementationDescription = theImplementationDescription;
	}

	/**
	 * Sets (or clears) the list of interceptors
	 *
	 * @param theList
	 *           The list of interceptors (may be null)
	 */
	public void setInterceptors(IServerInterceptor... theList) {
		myInterceptors.clear();
		if (theList != null) {
			myInterceptors.addAll(Arrays.asList(theList));
		}
	}

	/**
	 * Sets (or clears) the list of interceptors
	 *
	 * @param theList
	 *           The list of interceptors (may be null)
	 */
	public void setInterceptors(List<IServerInterceptor> theList) {
		myInterceptors.clear();
		if (theList != null) {
			myInterceptors.addAll(theList);
		}
	}

	/**
	 * Sets the paging provider to use, or <code>null</code> to use no paging (which is the default)
	 */
	public void setPagingProvider(IPagingProvider thePagingProvider) {
		myPagingProvider = thePagingProvider;
	}

	/**
	 * Sets the non-resource specific providers which implement method calls on this server.
	 *
	 * @see #setResourceProviders(Collection)
	 */
	public void setPlainProviders(Collection<Object> theProviders) {
		myPlainProviders = theProviders;
	}

	/**
	 * Sets the non-resource specific providers which implement method calls on this server.
	 *
	 * @see #setResourceProviders(Collection)
	 */
	public void setPlainProviders(Object... theProv) {
		setPlainProviders(Arrays.asList(theProv));
	}

	/**
	 * Sets the non-resource specific providers which implement method calls on this server
	 *
	 * @see #setResourceProviders(Collection)
	 */
	public void setProviders(Object... theProviders) {
		myPlainProviders = Arrays.asList(theProviders);
	}

	/**
	 * Sets the resource providers for this server
	 */
	public void setResourceProviders(Collection<IResourceProvider> theResourceProviders) {
		myResourceProviders = theResourceProviders;
	}

	/**
	 * Sets the resource providers for this server
	 */
	public void setResourceProviders(IResourceProvider... theResourceProviders) {
		myResourceProviders = Arrays.asList(theResourceProviders);
	}

	/**
	 * Provide a server address strategy, which is used to determine what base URL to provide clients to refer to this server. Defaults to an instance of {@link IncomingRequestAddressStrategy}
	 */
	public void setServerAddressStrategy(IServerAddressStrategy theServerAddressStrategy) {
		Validate.notNull(theServerAddressStrategy, "Server address strategy can not be null");
		myServerAddressStrategy = theServerAddressStrategy;
	}

	/**
	 * Returns the server conformance provider, which is the provider that is used to generate the server's conformance (metadata) statement.
	 * <p>
	 * By default, the ServerConformanceProvider implementation for the declared version of FHIR is used, but this can be changed, or set to <code>null</code> if you do not wish to export a conformance
	 * statement.
	 * </p>
	 * Note that this method can only be called before the server is initialized.
	 *
	 * @throws IllegalStateException
	 *            Note that this method can only be called prior to {@link #init() initialization} and will throw an {@link IllegalStateException} if called after that.
	 */
	public void setServerConformanceProvider(Object theServerConformanceProvider) {
		if (myStarted) {
			throw new IllegalStateException("Server is already started");
		}
		myServerConformanceProvider = theServerConformanceProvider;
	}

	/**
	 * Sets the server's name, as exported in conformance profiles exported by the server. This is informational only, but can be helpful to set with something appropriate.
	 */
	public void setServerName(String theServerName) {
		myServerName = theServerName;
	}

	/**
	 * Gets the server's version, as exported in conformance profiles exported by the server. This is informational only, but can be helpful to set with something appropriate.
	 */
	public void setServerVersion(String theServerVersion) {
		myServerVersion = theServerVersion;
	}

	/**
	 * If set to <code>true</code> (default is false), the server will use browser friendly content-types (instead of standard FHIR ones) when it detects that the request is coming from a browser
	 * instead of a FHIR
	 */
	public void setUseBrowserFriendlyContentTypes(boolean theUseBrowserFriendlyContentTypes) {
		myUseBrowserFriendlyContentTypes = theUseBrowserFriendlyContentTypes;
	}

	public void unregisterInterceptor(IServerInterceptor theInterceptor) {
		Validate.notNull(theInterceptor, "Interceptor can not be null");
		myInterceptors.remove(theInterceptor);
	}

	private void writeExceptionToResponse(HttpServletResponse theResponse, BaseServerResponseException theException) throws IOException {
		theResponse.setStatus(theException.getStatusCode());
		addHeadersToResponse(theResponse);
		theResponse.setContentType("text/plain");
		theResponse.setCharacterEncoding("UTF-8");
		theResponse.getWriter().write(theException.getMessage());
	}

	private static boolean partIsOperation(String nextString) {
		return nextString.length() > 0 && (nextString.charAt(0) == '_' || nextString.charAt(0) == '$');
	}

	public static boolean requestIsBrowser(HttpServletRequest theRequest) {
		String userAgent = theRequest.getHeader("User-Agent");
		return userAgent != null && userAgent.contains("Mozilla");
	}

}
