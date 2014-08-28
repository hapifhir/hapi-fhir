package ca.uhn.fhir.rest.server;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 University Health Network
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

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.exception.ExceptionUtils;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.Tag;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.Binary;
import ca.uhn.fhir.model.dstu.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu.resource.OperationOutcome.Issue;
import ca.uhn.fhir.model.dstu.valueset.IssueSeverityEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.method.BaseMethodBinding;
import ca.uhn.fhir.rest.method.ConformanceMethodBinding;
import ca.uhn.fhir.rest.method.OtherOperationTypeEnum;
import ca.uhn.fhir.rest.method.Request;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.method.SearchMethodBinding;
import ca.uhn.fhir.rest.method.SearchMethodBinding.RequestType;
import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;
import ca.uhn.fhir.rest.server.provider.ServerConformanceProvider;
import ca.uhn.fhir.rest.server.provider.ServerProfileProvider;
import ca.uhn.fhir.util.VersionUtil;

public class RestfulServer extends HttpServlet {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(RestfulServer.class);
	private static final long serialVersionUID = 1L;

	private AddProfileTagEnum myAddProfileTag;
	private FhirContext myFhirContext;
	private String myImplementationDescription;
	private List<IServerInterceptor> myInterceptors = new ArrayList<IServerInterceptor>();
	private ResourceBinding myNullResourceBinding = new ResourceBinding();
	private IPagingProvider myPagingProvider;
	private Collection<Object> myPlainProviders;
	private Map<String, ResourceBinding> myResourceNameToProvider = new HashMap<String, ResourceBinding>();
	private Collection<IResourceProvider> myResourceProviders;
	private ISecurityManager mySecurityManager;
	private IServerAddressStrategy myServerAddressStrategy = new IncomingRequestAddressStrategy();
	private BaseMethodBinding<?> myServerConformanceMethod;
	private Object myServerConformanceProvider;
	private String myServerName = "HAPI FHIR Server";
	/** This is configurable but by default we just use HAPI version */
	private String myServerVersion = VersionUtil.getVersion();
	private boolean myStarted;
	private boolean myUseBrowserFriendlyContentTypes;

	/**
	 * Constructor
	 */
	public RestfulServer() {
		this(new FhirContext());
	}

	public RestfulServer(FhirContext theCtx) {
		myFhirContext = theCtx;
		myServerConformanceProvider = new ServerConformanceProvider(this);
	}

	/**
	 * This method is called prior to sending a response to incoming requests. It is used to add custom headers.
	 * <p>
	 * Use caution if overriding this method: it is recommended to call <code>super.addHeadersToResponse</code> to avoid
	 * inadvertantly disabling functionality.
	 * </p>
	 */
	public void addHeadersToResponse(HttpServletResponse theHttpResponse) {
		theHttpResponse.addHeader("X-Powered-By", "HAPI FHIR " + VersionUtil.getVersion() + " RESTful Server");
	}

	private void assertProviderIsValid(Object theNext) throws ConfigurationException {
		if (Modifier.isPublic(theNext.getClass().getModifiers()) == false) {
			throw new ConfigurationException("Can not use provider '" + theNext.getClass() + "' - Class ust be public");
		}
	}

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		handleRequest(SearchMethodBinding.RequestType.DELETE, request, response);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		handleRequest(SearchMethodBinding.RequestType.GET, request, response);
	}

	@Override
	protected void doOptions(HttpServletRequest theReq, HttpServletResponse theResp) throws ServletException, IOException {
		handleRequest(SearchMethodBinding.RequestType.OPTIONS, theReq, theResp);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		handleRequest(SearchMethodBinding.RequestType.POST, request, response);
	}

	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		handleRequest(SearchMethodBinding.RequestType.PUT, request, response);
	}

	/**
	 * Count length of URL string, but treating unescaped sequences (e.g. ' ') as their unescaped equivalent (%20)
	 */
	private int escapedLength(String theServletPath) {
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

		for (Method m : clazz.getDeclaredMethods()) {
			BaseMethodBinding<?> foundMethodBinding = BaseMethodBinding.bindMethod(m, myFhirContext, theProvider);
			if (foundMethodBinding == null) {
				continue;
			}

			count++;

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
						resourceBinding = myNullResourceBinding;
					} else {
						RuntimeResourceDefinition definition = myFhirContext.getResourceDefinition(resourceName);
						if (myResourceNameToProvider.containsKey(definition.getName())) {
							resourceBinding = myResourceNameToProvider.get(definition.getName());
						} else {
							resourceBinding = new ResourceBinding();
							resourceBinding.setResourceName(resourceName);
							myResourceNameToProvider.put(resourceName, resourceBinding);
						}
					}

					List<Class<?>> allowableParams = foundMethodBinding.getAllowableParamAnnotations();
					if (allowableParams != null) {
						for (Annotation[] nextParamAnnotations : m.getParameterAnnotations()) {
							for (Annotation annotation : nextParamAnnotations) {
								Package pack = annotation.annotationType().getPackage();
								if (pack.equals(IdParam.class.getPackage())) {
									if (!allowableParams.contains(annotation.annotationType())) {
										throw new ConfigurationException("Method[" + m.toString() + "] is not allowed to have a parameter annotated with "+ annotation);
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

	private void findSystemMethods(Object theSystemProvider) {
		Class<?> clazz = theSystemProvider.getClass();

		findSystemMethods(theSystemProvider, clazz);

	}

	private void findSystemMethods(Object theSystemProvider, Class<?> clazz) {
		Class<?> supertype = clazz.getSuperclass();
		if (!Object.class.equals(supertype)) {
			findSystemMethods(theSystemProvider, supertype);
		}

		for (Method m : clazz.getDeclaredMethods()) {
			if (Modifier.isPublic(m.getModifiers())) {
				ourLog.debug("Scanning public method: {}#{}", theSystemProvider.getClass(), m.getName());

				BaseMethodBinding<?> foundMethodBinding = BaseMethodBinding.bindMethod(m, myFhirContext, theSystemProvider);
				if (foundMethodBinding != null) {
					if (foundMethodBinding instanceof ConformanceMethodBinding) {
						myServerConformanceMethod = foundMethodBinding;
					}
					ourLog.info(" * Method: {}#{} is a handler", theSystemProvider.getClass(), m.getName());
				} else {
					ourLog.debug(" * Method: {}#{} is not a handler", theSystemProvider.getClass(), m.getName());
				}
			}
		}
	}

	/**
	 * Returns the setting for automatically adding profile tags
	 * 
	 * @see #setAddProfileTag(AddProfileTagEnum)
	 */
	public AddProfileTagEnum getAddProfileTag() {
		return myAddProfileTag;
	}

	/**
	 * Gets the {@link FhirContext} associated with this server. For efficient processing, resource providers and plain
	 * providers should generally use this context if one is needed, as opposed to creating their own.
	 */
	public FhirContext getFhirContext() {
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

	public Collection<ResourceBinding> getResourceBindings() {
		return myResourceNameToProvider.values();
	}

	/**
	 * Provides the resource providers for this server
	 */
	public Collection<IResourceProvider> getResourceProviders() {
		return myResourceProviders;
	}

	/**
	 * Provides the security manager, or <code>null</code> if none
	 */
	public ISecurityManager getSecurityManager() {
		return mySecurityManager;
	}

	/**
	 * Get the server address strategy, which is used to determine what base URL to provide clients to refer to this
	 * server. Defaults to an instance of {@link IncomingRequestAddressStrategy}
	 */
	public IServerAddressStrategy getServerAddressStrategy() {
		return myServerAddressStrategy;
	}

	/**
	 * Returns the server conformance provider, which is the provider that is used to generate the server's conformance
	 * (metadata) statement.
	 * <p>
	 * By default, the {@link ServerConformanceProvider} is used, but this can be changed, or set to <code>null</code>
	 * if you do not wish to export a conformance statement.
	 * </p>
	 */
	public Object getServerConformanceProvider() {
		return myServerConformanceProvider;
	}

	/**
	 * Gets the server's name, as exported in conformance profiles exported by the server. This is informational only,
	 * but can be helpful to set with something appropriate.
	 * 
	 * @see RestfulServer#setServerName(String)
	 */
	public String getServerName() {
		return myServerName;
	}

	public IResourceProvider getServerProfilesProvider() {
		return new ServerProfileProvider(getFhirContext());
	}

	/**
	 * Gets the server's version, as exported in conformance profiles exported by the server. This is informational
	 * only, but can be helpful to set with something appropriate.
	 */
	public String getServerVersion() {
		return myServerVersion;
	}

	private void handlePagingRequest(Request theRequest, HttpServletResponse theResponse, String thePagingAction) throws IOException {
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

		Integer count = extractCountParameter(theRequest.getServletRequest());
		if (count == null) {
			count = getPagingProvider().getDefaultPageSize();
		} else if (count > getPagingProvider().getMaximumPageSize()) {
			count = getPagingProvider().getMaximumPageSize();
		}

		Integer offsetI = tryToExtractNamedParameter(theRequest.getServletRequest(), Constants.PARAM_PAGINGOFFSET);
		if (offsetI == null || offsetI < 0) {
			offsetI = 0;
		}

		int start = Math.min(offsetI, resultList.size() - 1);

		EncodingEnum responseEncoding = determineResponseEncoding(theRequest.getServletRequest());
		boolean prettyPrint = prettyPrintResponse(theRequest);
		boolean requestIsBrowser = requestIsBrowser(theRequest.getServletRequest());
		NarrativeModeEnum narrativeMode = determineNarrativeMode(theRequest);
		boolean respondGzip = theRequest.isRespondGzip();

		Bundle bundle = createBundleFromBundleProvider(this, theResponse, resultList, responseEncoding, theRequest.getFhirServerBase(), theRequest.getCompleteUrl(), prettyPrint, requestIsBrowser, narrativeMode, start, count, thePagingAction);

		for (int i = getInterceptors().size() - 1; i >= 0; i--) {
			IServerInterceptor next = getInterceptors().get(i);
			boolean continueProcessing = next.outgoingResponse(theRequest, bundle, theRequest.getServletRequest(), theRequest.getServletResponse());
			if (!continueProcessing) {
				return;
			}
		}

		streamResponseAsBundle(this, theResponse, bundle, responseEncoding, theRequest.getFhirServerBase(), prettyPrint, narrativeMode, respondGzip);

	}

	protected void handleRequest(SearchMethodBinding.RequestType theRequestType, HttpServletRequest theRequest, HttpServletResponse theResponse) throws ServletException, IOException {
		for (IServerInterceptor next : myInterceptors) {
			boolean continueProcessing = next.incomingRequestPreProcessed(theRequest, theResponse);
			if (!continueProcessing) {
				return;
			}
		}

		String fhirServerBase = null;
		boolean requestIsBrowser = requestIsBrowser(theRequest);
		try {

			if (null != mySecurityManager) {
				mySecurityManager.authenticate(theRequest);
			}

			String resourceName = null;
			String requestFullPath = StringUtils.defaultString(theRequest.getRequestURI());
			String servletPath = StringUtils.defaultString(theRequest.getServletPath());
			StringBuffer requestUrl = theRequest.getRequestURL();
			String servletContextPath = "";
			if (theRequest.getServletContext() != null) {
				servletContextPath = StringUtils.defaultString(theRequest.getServletContext().getContextPath());
				// } else {
				// servletContextPath = servletPath;
			}

			if (ourLog.isTraceEnabled()) {
				ourLog.trace("Request FullPath: {}", requestFullPath);
				ourLog.trace("Servlet Path: {}", servletPath);
				ourLog.trace("Request Url: {}", requestUrl);
				ourLog.trace("Context Path: {}", servletContextPath);
			}

			IdDt id = null;
			String operation = null;

			String requestPath = requestFullPath.substring(escapedLength(servletContextPath) + escapedLength(servletPath));
			if (requestPath.length() > 0 && requestPath.charAt(0) == '/') {
				requestPath = requestPath.substring(1);
			}

			fhirServerBase = myServerAddressStrategy.determineServerBase(theRequest);

			if (fhirServerBase.endsWith("/")) {
				fhirServerBase = fhirServerBase.substring(0, fhirServerBase.length() - 1);
			}

			String completeUrl = StringUtils.isNotBlank(theRequest.getQueryString()) ? requestUrl + "?" + theRequest.getQueryString() : requestUrl.toString();

			Map<String, String[]> params = new HashMap<String, String[]>(theRequest.getParameterMap());

			StringTokenizer tok = new StringTokenizer(requestPath, "/");
			if (tok.hasMoreTokens()) {
				resourceName = tok.nextToken();
				if (resourceName.startsWith("_")) {
					operation = resourceName;
					resourceName = null;
				}
			}

			ResourceBinding resourceBinding = null;
			BaseMethodBinding<?> resourceMethod = null;
			if ("metadata".equals(resourceName) || theRequestType == RequestType.OPTIONS) {
				resourceMethod = myServerConformanceMethod;
			} else if (resourceName == null) {
				resourceBinding = myNullResourceBinding;
			} else {
				resourceBinding = myResourceNameToProvider.get(resourceName);
				if (resourceBinding == null) {
					throw new InvalidRequestException("Unknown resource type '" + resourceName + "' - Server knows how to handle: " + myResourceNameToProvider.keySet());
				}
			}

			if (tok.hasMoreTokens()) {
				String nextString = tok.nextToken();
				if (nextString.startsWith("_")) {
					operation = nextString;
				} else {
					id = new IdDt(resourceName, nextString);
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
						id = new IdDt(resourceName + "/" + id.getIdPart() + "/_history/" + versionString);
					} else {
						operation = Constants.PARAM_HISTORY;
					}
				} else if (nextString.startsWith("_")) {
					if (operation != null) {
						throw new InvalidRequestException("URL Path contains two operations (part beginning with _): " + requestPath);
					}
					operation = nextString;
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

			if (theRequestType == RequestType.PUT) {
				String contentLocation = theRequest.getHeader(Constants.HEADER_CONTENT_LOCATION);
				if (contentLocation != null) {
					id = new IdDt(contentLocation);
				}
			}

			// TODO: look for more tokens for version, compartments, etc...

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

			Request r = new Request();
			r.setResourceName(resourceName);
			r.setId(id);
			r.setOperation(operation);
			r.setSecondaryOperation(secondaryOperation);
			r.setParameters(params);
			r.setRequestType(theRequestType);
			r.setFhirServerBase(fhirServerBase);
			r.setCompleteUrl(completeUrl);
			r.setServletRequest(theRequest);
			r.setServletResponse(theResponse);
			r.setRespondGzip(respondGzip);

			String pagingAction = theRequest.getParameter(Constants.PARAM_PAGINGACTION);
			if (getPagingProvider() != null && isNotBlank(pagingAction)) {
				r.setOtherOperationType(OtherOperationTypeEnum.GET_PAGE);
				handlePagingRequest(r, theResponse, pagingAction);
				return;
			}

			if (resourceMethod == null && resourceBinding != null) {
				resourceMethod = resourceBinding.getMethod(r);
			}
			if (resourceMethod == null) {
				StringBuilder b = new StringBuilder();
				b.append("No resource method available for ");
				b.append(theRequestType.name());
				b.append(" operation[");
				b.append(requestPath);
				b.append("]");
				b.append(" with parameters ");
				b.append(params.keySet());
				throw new InvalidRequestException(b.toString());
			}

			RequestDetails requestDetails = r;
			requestDetails.setResourceOperationType(resourceMethod.getResourceOperationType());
			requestDetails.setSystemOperationType(resourceMethod.getSystemOperationType());
			requestDetails.setOtherOperationType(resourceMethod.getOtherOperationType());

			for (IServerInterceptor next : myInterceptors) {
				boolean continueProcessing = next.incomingRequestPostProcessed(requestDetails, theRequest, theResponse);
				if (!continueProcessing) {
					return;
				}
			}

			resourceMethod.invokeServer(this, r);

		} catch (AuthenticationException e) {
			if (requestIsBrowser) {
				// if request is coming from a browser, prompt the user to enter login credentials
				theResponse.setHeader("WWW-Authenticate", "BASIC realm=\"FHIR\"");
			}
			theResponse.setStatus(e.getStatusCode());
			addHeadersToResponse(theResponse);
			theResponse.setContentType("text/plain");
			theResponse.setCharacterEncoding("UTF-8");
			theResponse.getWriter().write(e.getMessage());

		} catch (Throwable e) {

			OperationOutcome oo = null;
			int statusCode = 500;

			if (e instanceof BaseServerResponseException) {
				oo = ((BaseServerResponseException) e).getOperationOutcome();
				statusCode = ((BaseServerResponseException) e).getStatusCode();
			}

			if (oo == null) {
				oo = new OperationOutcome();
				Issue issue = oo.addIssue();
				issue.getSeverity().setValueAsEnum(IssueSeverityEnum.ERROR);
				if (e instanceof InternalErrorException) {
					ourLog.error("Failure during REST processing", e);
					issue.getDetails().setValue(e.toString() + "\n\n" + ExceptionUtils.getStackTrace(e));
				} else if (e instanceof BaseServerResponseException) {
					ourLog.warn("Failure during REST processing: {}", e.toString());
					statusCode = ((BaseServerResponseException) e).getStatusCode();
					issue.getDetails().setValue(e.getMessage());
				} else {
					ourLog.error("Failure during REST processing", e);
					issue.getDetails().setValue(e.toString() + "\n\n" + ExceptionUtils.getStackTrace(e));
				}
			}

			streamResponseAsResource(this, theResponse, oo, determineResponseEncoding(theRequest), true, requestIsBrowser, NarrativeModeEnum.NORMAL, statusCode, false, fhirServerBase);

			theResponse.setStatus(statusCode);
			addHeadersToResponse(theResponse);
			theResponse.setContentType("text/plain");
			theResponse.setCharacterEncoding("UTF-8");
			theResponse.getWriter().append(e.getMessage());
			theResponse.getWriter().close();

		}

	}

	/**
	 * Initializes the server. Note that this method is final to avoid accidentally introducing bugs in implementations,
	 * but subclasses may put initialization code in {@link #initialize()}, which is called immediately before beginning
	 * initialization of the restful server's internal init.
	 */
	@Override
	public final void init() throws ServletException {
		initialize();
		try {
			ourLog.info("Initializing HAPI FHIR restful server");

			mySecurityManager = getSecurityManager();
			if (null == mySecurityManager) {
				ourLog.trace("No security manager has been provided");
			}

			Collection<IResourceProvider> resourceProvider = getResourceProviders();
			if (resourceProvider != null) {
				Map<Class<? extends IResource>, IResourceProvider> typeToProvider = new HashMap<Class<? extends IResource>, IResourceProvider>();
				for (IResourceProvider nextProvider : resourceProvider) {
					Class<? extends IResource> resourceType = nextProvider.getResourceType();
					if (resourceType == null) {
						throw new NullPointerException("getResourceType() on class '" + nextProvider.getClass().getCanonicalName() + "' returned null");
					}
					if (typeToProvider.containsKey(resourceType)) {
						throw new ServletException("Multiple providers for type: " + resourceType.getCanonicalName());
					}
					typeToProvider.put(resourceType, nextProvider);
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
			findSystemMethods(getServerConformanceProvider());

		} catch (Exception ex) {
			ourLog.error("An error occurred while loading request handlers!", ex);
			throw new ServletException("Failed to initialize FHIR Restful server", ex);
		}
		
		myStarted = true;
		ourLog.info("A FHIR has been lit on this server");
	}

	/**
	 * This method may be overridden by subclasses to do perform initialization that needs to be performed prior to the
	 * server being used.
	 */
	protected void initialize() throws ServletException {
		// nothing by default
	}

	public boolean isUseBrowserFriendlyContentTypes() {
		return myUseBrowserFriendlyContentTypes;
	}

	public void registerInterceptor(IServerInterceptor theInterceptor) {
		Validate.notNull(theInterceptor, "Interceptor can not be null");
		myInterceptors.add(theInterceptor);
	}

	private boolean requestIsBrowser(HttpServletRequest theRequest) {
		String userAgent = theRequest.getHeader("User-Agent");
		return userAgent != null && userAgent.contains("Mozilla");
	}

	/**
	 * Sets the profile tagging behaviour for the server. When set to a value other than {@link AddProfileTagEnum#NEVER}
	 * (which is the default), the server will automatically add a profile tag based on the class of the resource(s)
	 * being returned.
	 * 
	 * @param theAddProfileTag
	 *            The behaviour enum (must not be null)
	 */
	public void setAddProfileTag(AddProfileTagEnum theAddProfileTag) {
		Validate.notNull(theAddProfileTag, "theAddProfileTag must not be null");
		myAddProfileTag = theAddProfileTag;
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
	 *            The list of interceptors (may be null)
	 */
	public void setInterceptors(List<IServerInterceptor> theList) {
		myInterceptors.clear();
		if (theList != null) {
			myInterceptors.addAll(theList);
		}
	}

	/**
	 * Sets (or clears) the list of interceptors
	 * 
	 * @param theList
	 *            The list of interceptors (may be null)
	 */
	public void setInterceptors(IServerInterceptor... theList) {
		myInterceptors.clear();
		if (theList != null) {
			myInterceptors.addAll(Arrays.asList(theList));
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
	 * Sets the security manager, or <code>null</code> if none
	 */
	public void setSecurityManager(ISecurityManager theSecurityManager) {
		mySecurityManager = theSecurityManager;
	}

	/**
	 * Provide a server address strategy, which is used to determine what base URL to provide clients to refer to this
	 * server. Defaults to an instance of {@link IncomingRequestAddressStrategy}
	 */
	public void setServerAddressStrategy(IServerAddressStrategy theServerAddressStrategy) {
		Validate.notNull(theServerAddressStrategy, "Server address strategy can not be null");
		myServerAddressStrategy = theServerAddressStrategy;
	}

	/**
	 * Returns the server conformance provider, which is the provider that is used to generate the server's conformance
	 * (metadata) statement.
	 * <p>
	 * By default, the {@link ServerConformanceProvider} is used, but this can be changed, or set to <code>null</code>
	 * if you do not wish to export a conformance statement.
	 * </p>
	 * Note that this method can only be called before the server is initialized.
	 * 
	 * @throws IllegalStateException
	 *             Note that this method can only be called prior to {@link #init() initialization} and will throw an
	 *             {@link IllegalStateException} if called after that.
	 */
	public void setServerConformanceProvider(Object theServerConformanceProvider) {
		if (myStarted) {
			throw new IllegalStateException("Server is already started");
		}
		myServerConformanceProvider = theServerConformanceProvider;
	}

	/**
	 * Sets the server's name, as exported in conformance profiles exported by the server. This is informational only,
	 * but can be helpful to set with something appropriate.
	 */
	public void setServerName(String theServerName) {
		myServerName = theServerName;
	}

	/**
	 * Gets the server's version, as exported in conformance profiles exported by the server. This is informational
	 * only, but can be helpful to set with something appropriate.
	 */
	public void setServerVersion(String theServerVersion) {
		myServerVersion = theServerVersion;
	}

	/**
	 * If set to <code>true</code> (default is false), the server will use browser friendly content-types (instead of
	 * standard FHIR ones) when it detects that the request is coming from a browser instead of a FHIR
	 */
	public void setUseBrowserFriendlyContentTypes(boolean theUseBrowserFriendlyContentTypes) {
		myUseBrowserFriendlyContentTypes = theUseBrowserFriendlyContentTypes;
	}

	public void unregisterInterceptor(IServerInterceptor theInterceptor) {
		Validate.notNull(theInterceptor, "Interceptor can not be null");
		myInterceptors.remove(theInterceptor);
	}

	private static void addProfileToBundleEntry(FhirContext theContext, IResource theResource) {

		TagList tl = ResourceMetadataKeyEnum.TAG_LIST.get(theResource);
		if (tl == null) {
			tl = new TagList();
			ResourceMetadataKeyEnum.TAG_LIST.put(theResource, tl);
		}

		RuntimeResourceDefinition nextDef = theContext.getResourceDefinition(theResource);
		String profile = nextDef.getResourceProfile();
		if (isNotBlank(profile)) {
			tl.add(new Tag(Tag.HL7_ORG_PROFILE_TAG, profile, null));
		}
	}

	public static Bundle createBundleFromBundleProvider(RestfulServer theServer, HttpServletResponse theHttpResponse, IBundleProvider theResult, EncodingEnum theResponseEncoding, String theServerBase, String theCompleteUrl, boolean thePrettyPrint, boolean theRequestIsBrowser,
			NarrativeModeEnum theNarrativeMode, int theOffset, Integer theLimit, String theSearchId) {
		theHttpResponse.setStatus(200);

		if (theRequestIsBrowser && theServer.isUseBrowserFriendlyContentTypes()) {
			theHttpResponse.setContentType(theResponseEncoding.getBrowserFriendlyBundleContentType());
		} else if (theNarrativeMode == NarrativeModeEnum.ONLY) {
			theHttpResponse.setContentType(Constants.CT_HTML);
		} else {
			theHttpResponse.setContentType(theResponseEncoding.getBundleContentType());
		}

		theHttpResponse.setCharacterEncoding(Constants.CHARSET_UTF_8);

		theServer.addHeadersToResponse(theHttpResponse);

		int numToReturn;
		String searchId = null;
		List<IResource> resourceList;
		if (theServer.getPagingProvider() == null) {
			numToReturn = theResult.size();
			resourceList = theResult.getResources(0, numToReturn);
		} else {
			IPagingProvider pagingProvider = theServer.getPagingProvider();
			if (theLimit == null) {
				numToReturn = pagingProvider.getDefaultPageSize();
			} else {
				numToReturn = Math.min(pagingProvider.getMaximumPageSize(), theLimit);
			}

			numToReturn = Math.min(numToReturn, theResult.size() - theOffset);
			resourceList = theResult.getResources(theOffset, numToReturn + theOffset);

			if (theSearchId != null) {
				searchId = theSearchId;
			} else {
				if (theResult.size() > numToReturn) {
					searchId = pagingProvider.storeResultList(theResult);
					Validate.notNull(searchId, "Paging provider returned null searchId");
				}
			}
		}

		for (IResource next : resourceList) {
			if (next.getId() == null || next.getId().isEmpty()) {
				if (!(next instanceof OperationOutcome)) {
					throw new InternalErrorException("Server method returned resource of type[" + next.getClass().getSimpleName() + "] with no ID specified (IResource#setId(IdDt) must be called)");
				}
			}
		}

		if (theServer.getAddProfileTag() != AddProfileTagEnum.NEVER) {
			for (int i = 0; i < resourceList.size(); i++) {
				IResource nextRes = resourceList.get(i);
				RuntimeResourceDefinition def = theServer.getFhirContext().getResourceDefinition(nextRes);
				if (theServer.getAddProfileTag() == AddProfileTagEnum.ALWAYS || !def.isStandardProfile()) {
					addProfileToBundleEntry(theServer.getFhirContext(), nextRes);
				}
			}
		}

		Bundle bundle = createBundleFromResourceList(theServer.getFhirContext(), theServer.getServerName(), resourceList, theServerBase, theCompleteUrl, theResult.size());

		bundle.setPublished(theResult.getPublished());

		if (theServer.getPagingProvider() != null) {
			int limit;
			limit = theLimit != null ? theLimit : theServer.getPagingProvider().getDefaultPageSize();
			limit = Math.min(limit, theServer.getPagingProvider().getMaximumPageSize());

			if (searchId != null) {
				if (theOffset + numToReturn < theResult.size()) {
					bundle.getLinkNext().setValue(createPagingLink(theServerBase, searchId, theOffset + numToReturn, numToReturn, theResponseEncoding, thePrettyPrint));
				}
				if (theOffset > 0) {
					int start = Math.max(0, theOffset - limit);
					bundle.getLinkPrevious().setValue(createPagingLink(theServerBase, searchId, start, limit, theResponseEncoding, thePrettyPrint));
				}
			}
		}
		return bundle;
	}

	public static Bundle createBundleFromResourceList(FhirContext theContext, String theAuthor, List<IResource> theResult, String theServerBase, String theCompleteUrl, int theTotalResults) {
		Bundle bundle = new Bundle();
		bundle.getAuthorName().setValue(theAuthor);
		bundle.getBundleId().setValue(UUID.randomUUID().toString());
		bundle.getPublished().setToCurrentTimeInLocalTimeZone();
		bundle.getLinkBase().setValue(theServerBase);
		bundle.getLinkSelf().setValue(theCompleteUrl);

		List<IResource> addedResources = new ArrayList<IResource>();
		Set<IdDt> addedResourceIds = new HashSet<IdDt>();
		for (IResource next : theResult) {

			if (theContext.getNarrativeGenerator() != null) {
				String title = theContext.getNarrativeGenerator().generateTitle(next);
				ourLog.trace("Narrative generator created title: {}", title);
				if (StringUtils.isNotBlank(title)) {
					ResourceMetadataKeyEnum.TITLE.put(next, title);
				}
			} else {
				ourLog.trace("No narrative generator specified");
			}

			List<ResourceReferenceDt> references = theContext.newTerser().getAllPopulatedChildElementsOfType(next, ResourceReferenceDt.class);
			do {
				List<IResource> addedResourcesThisPass = new ArrayList<IResource>();

				for (ResourceReferenceDt nextRef : references) {
					IResource nextRes = nextRef.getResource();
					if (nextRes != null) {
						if (nextRes.getId().hasIdPart()) {
							IdDt id = nextRes.getId().toVersionless();
							if (id.hasResourceType() == false) {
								String resName = theContext.getResourceDefinition(nextRes).getName();
								id = id.withResourceType(resName);
							}

							if (!addedResourceIds.contains(id)) {
								addedResourceIds.add(id);
								addedResourcesThisPass.add(nextRes);
							}

							nextRef.setResource(null);
							nextRef.setReference(id);
						}
					}
				}

				// Linked resources may themselves have linked resources
				references = new ArrayList<ResourceReferenceDt>();
				for (IResource iResource : addedResourcesThisPass) {
					List<ResourceReferenceDt> newReferences = theContext.newTerser().getAllPopulatedChildElementsOfType(iResource, ResourceReferenceDt.class);
					references.addAll(newReferences);
				}

				addedResources.addAll(addedResourcesThisPass);

			} while (references.isEmpty() == false);

			bundle.addResource(next, theContext, theServerBase);

		}

		/*
		 * Actually add the resources to the bundle
		 */
		for (IResource next : addedResources) {
			bundle.addResource(next, theContext, theServerBase);
		}

		bundle.getTotalResults().setValue(theTotalResults);
		return bundle;
	}

	public static String createPagingLink(String theServerBase, String theSearchId, int theOffset, int theCount, EncodingEnum theResponseEncoding, boolean thePrettyPrint) {
		StringBuilder b = new StringBuilder();
		b.append(theServerBase);
		b.append('?');
		b.append(Constants.PARAM_PAGINGACTION);
		b.append('=');
		try {
			b.append(URLEncoder.encode(theSearchId, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new Error("UTF-8 not supported", e);// should not happen
		}
		b.append('&');
		b.append(Constants.PARAM_PAGINGOFFSET);
		b.append('=');
		b.append(theOffset);
		b.append('&');
		b.append(Constants.PARAM_COUNT);
		b.append('=');
		b.append(theCount);
		b.append('&');
		b.append(Constants.PARAM_FORMAT);
		b.append('=');
		b.append(theResponseEncoding.getRequestContentType());
		if (thePrettyPrint) {
			b.append('&');
			b.append(Constants.PARAM_PRETTY);
			b.append('=');
			b.append(Constants.PARAM_PRETTY_VALUE_TRUE);
		}
		return b.toString();
	}

	public static NarrativeModeEnum determineNarrativeMode(RequestDetails theRequest) {
		Map<String, String[]> requestParams = theRequest.getParameters();
		String[] narrative = requestParams.remove(Constants.PARAM_NARRATIVE);
		NarrativeModeEnum narrativeMode = null;
		if (narrative != null && narrative.length > 0) {
			narrativeMode = NarrativeModeEnum.valueOfCaseInsensitive(narrative[0]);
		}
		if (narrativeMode == null) {
			narrativeMode = NarrativeModeEnum.NORMAL;
		}
		return narrativeMode;
	}

	public static EncodingEnum determineRequestEncoding(Request theReq) {
		Enumeration<String> acceptValues = theReq.getServletRequest().getHeaders(Constants.HEADER_CONTENT_TYPE);
		if (acceptValues != null) {
			while (acceptValues.hasMoreElements()) {
				String nextAcceptHeaderValue = acceptValues.nextElement();
				if (nextAcceptHeaderValue != null && isNotBlank(nextAcceptHeaderValue)) {
					for (String nextPart : nextAcceptHeaderValue.split(",")) {
						int scIdx = nextPart.indexOf(';');
						if (scIdx == 0) {
							continue;
						}
						if (scIdx != -1) {
							nextPart = nextPart.substring(0, scIdx);
						}
						nextPart = nextPart.trim();
						EncodingEnum retVal = Constants.FORMAT_VAL_TO_ENCODING.get(nextPart);
						if (retVal != null) {
							return retVal;
						}
					}
				}
			}
		}
		return EncodingEnum.XML;
	}

	public static EncodingEnum determineResponseEncoding(HttpServletRequest theReq) {
		String[] format = theReq.getParameterValues(Constants.PARAM_FORMAT);
		if (format != null) {
			for (String nextFormat : format) {
				EncodingEnum retVal = Constants.FORMAT_VAL_TO_ENCODING.get(nextFormat);
				if (retVal != null) {
					return retVal;
				}
			}
		}

		Enumeration<String> acceptValues = theReq.getHeaders(Constants.HEADER_ACCEPT);
		if (acceptValues != null) {
			while (acceptValues.hasMoreElements()) {
				String nextAcceptHeaderValue = acceptValues.nextElement();
				if (nextAcceptHeaderValue != null && isNotBlank(nextAcceptHeaderValue)) {
					for (String nextPart : nextAcceptHeaderValue.split(",")) {
						int scIdx = nextPart.indexOf(';');
						if (scIdx == 0) {
							continue;
						}
						if (scIdx != -1) {
							nextPart = nextPart.substring(0, scIdx);
						}
						nextPart = nextPart.trim();
						EncodingEnum retVal = Constants.FORMAT_VAL_TO_ENCODING.get(nextPart);
						if (retVal != null) {
							return retVal;
						}
					}
				}
			}
		}
		return EncodingEnum.XML;
	}

	public static Integer extractCountParameter(HttpServletRequest theRequest) {
		String name = Constants.PARAM_COUNT;
		return tryToExtractNamedParameter(theRequest, name);
	}

	public static IParser getNewParser(FhirContext theContext, EncodingEnum theResponseEncoding, boolean thePrettyPrint, NarrativeModeEnum theNarrativeMode) {
		IParser parser;
		switch (theResponseEncoding) {
		case JSON:
			parser = theContext.newJsonParser();
			break;
		case XML:
		default:
			parser = theContext.newXmlParser();
			break;
		}
		return parser.setPrettyPrint(thePrettyPrint).setSuppressNarratives(theNarrativeMode == NarrativeModeEnum.SUPPRESS);
	}

	private static Writer getWriter(HttpServletResponse theHttpResponse, boolean theRespondGzip) throws UnsupportedEncodingException, IOException {
		Writer writer;
		if (theRespondGzip) {
			theHttpResponse.addHeader(Constants.HEADER_CONTENT_ENCODING, Constants.ENCODING_GZIP);
			writer = new OutputStreamWriter(new GZIPOutputStream(theHttpResponse.getOutputStream()), "UTF-8");
		} else {
			writer = theHttpResponse.getWriter();
		}
		return writer;
	}

	public static boolean prettyPrintResponse(Request theRequest) {
		Map<String, String[]> requestParams = theRequest.getParameters();
		String[] pretty = requestParams.remove(Constants.PARAM_PRETTY);
		boolean prettyPrint;
		if (pretty != null && pretty.length > 0) {
			if (Constants.PARAM_PRETTY_VALUE_TRUE.equals(pretty[0])) {
				prettyPrint = true;
			} else {
				prettyPrint = false;
			}
		} else {
			prettyPrint = false;
			Enumeration<String> acceptValues = theRequest.getServletRequest().getHeaders(Constants.HEADER_ACCEPT);
			if (acceptValues != null) {
				while (acceptValues.hasMoreElements()) {
					String nextAcceptHeaderValue = acceptValues.nextElement();
					if (nextAcceptHeaderValue.contains("pretty=true")) {
						prettyPrint = true;
					}
				}
			}
		}
		return prettyPrint;
	}

	public static void streamResponseAsBundle(RestfulServer theServer, HttpServletResponse theHttpResponse, Bundle bundle, EncodingEnum theResponseEncoding, String theServerBase, boolean thePrettyPrint, NarrativeModeEnum theNarrativeMode, boolean theRespondGzip)
			throws IOException {
		assert !theServerBase.endsWith("/");

		Writer writer = getWriter(theHttpResponse, theRespondGzip);
		try {
			if (theNarrativeMode == NarrativeModeEnum.ONLY) {
				for (IResource next : bundle.toListOfResources()) {
					writer.append(next.getText().getDiv().getValueAsString());
					writer.append("<hr/>");
				}
			} else {
				RestfulServer.getNewParser(theServer.getFhirContext(), theResponseEncoding, thePrettyPrint, theNarrativeMode).encodeBundleToWriter(bundle, writer);
			}
		} finally {
			writer.close();
		}
	}

	public static void streamResponseAsResource(RestfulServer theServer, HttpServletResponse theHttpResponse, IResource theResource, EncodingEnum theResponseEncoding, boolean thePrettyPrint, boolean theRequestIsBrowser, NarrativeModeEnum theNarrativeMode, boolean theRespondGzip,
			String theServerBase) throws IOException {
		int stausCode = 200;
		streamResponseAsResource(theServer, theHttpResponse, theResource, theResponseEncoding, thePrettyPrint, theRequestIsBrowser, theNarrativeMode, stausCode, theRespondGzip, theServerBase);
	}

	private static void streamResponseAsResource(RestfulServer theServer, HttpServletResponse theHttpResponse, IResource theResource, EncodingEnum theResponseEncoding, boolean thePrettyPrint, boolean theRequestIsBrowser, NarrativeModeEnum theNarrativeMode, int stausCode,
			boolean theRespondGzip, String theServerBase) throws IOException {
		theHttpResponse.setStatus(stausCode);

		if (theResource.getId() != null && theResource.getId().hasIdPart() && isNotBlank(theServerBase)) {
			String resName = theServer.getFhirContext().getResourceDefinition(theResource).getName();
			String fullId = theResource.getId().withServerBase(theServerBase, resName);
			theHttpResponse.addHeader(Constants.HEADER_CONTENT_LOCATION, fullId);
		}

		if (theServer.getAddProfileTag() != AddProfileTagEnum.NEVER) {
			RuntimeResourceDefinition def = theServer.getFhirContext().getResourceDefinition(theResource);
			if (theServer.getAddProfileTag() == AddProfileTagEnum.ALWAYS || !def.isStandardProfile()) {
				addProfileToBundleEntry(theServer.getFhirContext(), theResource);
			}
		}

		if (theResource instanceof Binary) {
			Binary bin = (Binary) theResource;
			if (isNotBlank(bin.getContentType())) {
				theHttpResponse.setContentType(bin.getContentType());
			} else {
				theHttpResponse.setContentType(Constants.CT_OCTET_STREAM);
			}
			if (bin.getContent() == null || bin.getContent().length == 0) {
				return;
			}

			theHttpResponse.addHeader(Constants.HEADER_CONTENT_DISPOSITION, "Attachment;");

			theHttpResponse.setContentLength(bin.getContent().length);
			ServletOutputStream oos = theHttpResponse.getOutputStream();
			oos.write(bin.getContent());
			oos.close();
			return;
		}

		if (theRequestIsBrowser && theServer.isUseBrowserFriendlyContentTypes()) {
			theHttpResponse.setContentType(theResponseEncoding.getBrowserFriendlyBundleContentType());
		} else if (theNarrativeMode == NarrativeModeEnum.ONLY) {
			theHttpResponse.setContentType(Constants.CT_HTML);
		} else {
			theHttpResponse.setContentType(theResponseEncoding.getResourceContentType());
		}
		theHttpResponse.setCharacterEncoding(Constants.CHARSET_UTF_8);

		theServer.addHeadersToResponse(theHttpResponse);

		InstantDt lastUpdated = ResourceMetadataKeyEnum.UPDATED.get(theResource);
		if (lastUpdated != null) {
			theHttpResponse.addHeader(Constants.HEADER_LAST_MODIFIED, lastUpdated.getValueAsString());
		}

		TagList list = (TagList) theResource.getResourceMetadata().get(ResourceMetadataKeyEnum.TAG_LIST);
		if (list != null) {
			for (Tag tag : list) {
				if (StringUtils.isNotBlank(tag.getTerm())) {
					theHttpResponse.addHeader(Constants.HEADER_CATEGORY, tag.toHeaderValue());
				}
			}
		}

		Writer writer = getWriter(theHttpResponse, theRespondGzip);
		try {
			if (theNarrativeMode == NarrativeModeEnum.ONLY) {
				writer.append(theResource.getText().getDiv().getValueAsString());
			} else {
				RestfulServer.getNewParser(theServer.getFhirContext(), theResponseEncoding, thePrettyPrint, theNarrativeMode).encodeResourceToWriter(theResource, writer);
			}
		} finally {
			writer.close();
		}
	}

	private static Integer tryToExtractNamedParameter(HttpServletRequest theRequest, String name) {
		String countString = theRequest.getParameter(name);
		Integer count = null;
		if (isNotBlank(countString)) {
			try {
				count = Integer.parseInt(countString);
			} catch (NumberFormatException e) {
				ourLog.debug("Failed to parse _count value '{}': {}", countString, e);
			}
		}
		return count;
	}

	public enum NarrativeModeEnum {
		NORMAL, ONLY, SUPPRESS;

		public static NarrativeModeEnum valueOfCaseInsensitive(String theCode) {
			return valueOf(NarrativeModeEnum.class, theCode.toUpperCase());
		}
	}

}
