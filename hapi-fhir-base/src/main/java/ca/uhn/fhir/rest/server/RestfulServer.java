package ca.uhn.fhir.rest.server;

/*
 * #%L
 * HAPI FHIR Library
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

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.method.BaseMethodBinding;
import ca.uhn.fhir.rest.method.ConformanceMethodBinding;
import ca.uhn.fhir.rest.method.Request;
import ca.uhn.fhir.rest.method.SearchMethodBinding;
import ca.uhn.fhir.rest.method.SearchMethodBinding.RequestType;
import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.provider.ServerConformanceProvider;
import ca.uhn.fhir.rest.server.provider.ServerProfileProvider;
import ca.uhn.fhir.util.VersionUtil;

public class RestfulServer extends HttpServlet {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(RestfulServer.class);

	private static final long serialVersionUID = 1L;

	private FhirContext myFhirContext;
	private ResourceBinding myNullResourceBinding = new ResourceBinding();
	private Collection<Object> myPlainProviders;
	private Map<String, ResourceBinding> myResourceNameToProvider = new HashMap<String, ResourceBinding>();
	private Collection<IResourceProvider> myResourceProviders;
	private ISecurityManager mySecurityManager;
	private BaseMethodBinding<?> myServerConformanceMethod;
	private Object myServerConformanceProvider;
	private String myServerName = "HAPI FHIR Server";
	/** This is configurable but by default we jsut use HAPI version */
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
		theHttpResponse.addHeader("X-PoweredBy", "HAPI FHIR " + VersionUtil.getVersion() + " RESTful Server");
	}

	/**
	 * Gets the {@link FhirContext} associated with this server. For efficient processing, resource providers and plain
	 * providers should generally use this context if one is needed, as opposed to creating their own.
	 */
	public FhirContext getFhirContext() {
		return myFhirContext;
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
	 * @see RestfulServer#setServerName(StringDt)
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

	public boolean isUseBrowserFriendlyContentTypes() {
		return myUseBrowserFriendlyContentTypes;
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
	 * Gets the server's name, as exported in conformance profiles exported by the server. This is informational only,
	 * but can be helpful to set with something appropriate.
	 * 
	 * @see RestfulServer#setServerName(StringDt)
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

	private void assertProviderIsValid(Object theNext) throws ConfigurationException {
		if (Modifier.isPublic(theNext.getClass().getModifiers()) == false) {
			throw new ConfigurationException("Can not use provider '" + theNext.getClass() + "' - Must be public");
		}
	}

	private void findResourceMethods(Object theProvider) throws Exception {

		ourLog.info("Scanning type for RESTful methods: {}", theProvider.getClass());

		Class<?> clazz = theProvider.getClass();
		Class<?> supertype = clazz.getSuperclass();
		if (!Object.class.equals(supertype)) {
			findResourceMethods(theProvider, supertype);
		}

		findResourceMethods(theProvider, clazz);
	}

	private void findResourceMethods(Object theProvider, Class<?> clazz) {
		for (Method m : clazz.getDeclaredMethods()) {
			if (!Modifier.isPublic(m.getModifiers())) {
				ourLog.debug("Ignoring non-public method: {}", m);
			} else {
				if (!Modifier.isStatic(m.getModifiers())) {
					ourLog.debug("Scanning public method: {}#{}", theProvider.getClass(), m.getName());

					BaseMethodBinding<?> foundMethodBinding = BaseMethodBinding.bindMethod(m, myFhirContext, theProvider);
					if (foundMethodBinding != null) {

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

						resourceBinding.addMethod(foundMethodBinding);
						ourLog.debug(" * Method: {}#{} is a handler", theProvider.getClass(), m.getName());
					} else {
						ourLog.debug(" * Method: {}#{} is not a handler", theProvider.getClass(), m.getName());
					}
				}
			}
		}
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

	// /**
	// * Sets the {@link INarrativeGenerator Narrative Generator} to use when
	// serializing responses from this server, or <code>null</code> (which is
	// the default) to disable narrative generation.
	// * Note that this method can only be called before the server is
	// initialized.
	// *
	// * @throws IllegalStateException
	// * Note that this method can only be called prior to {@link #init()
	// initialization} and will throw an {@link IllegalStateException} if called
	// after that.
	// */
	// public void setNarrativeGenerator(INarrativeGenerator
	// theNarrativeGenerator) {
	// myNarrativeGenerator = theNarrativeGenerator;
	// }

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

	protected void handleRequest(SearchMethodBinding.RequestType theRequestType, HttpServletRequest theRequest, HttpServletResponse theResponse) throws ServletException, IOException {
		try {

			if (null != mySecurityManager) {
				mySecurityManager.authenticate(theRequest);
			}

			String resourceName = null;
			String requestFullPath = StringUtils.defaultString(theRequest.getRequestURI());
			// String contextPath =
			// StringUtils.defaultString(request.getContextPath());
			String servletPath = StringUtils.defaultString(theRequest.getServletPath());
			StringBuffer requestUrl = theRequest.getRequestURL();
			String servletContextPath = "";
			if (theRequest.getServletContext() != null) {
				servletContextPath = StringUtils.defaultIfBlank(theRequest.getServletContext().getContextPath(), servletPath);
			} else {
				servletContextPath = servletPath;
			}

			if (ourLog.isTraceEnabled()) {
				ourLog.trace("Request FullPath: {}", requestFullPath);
				ourLog.trace("Servlet Path: {}", servletPath);
				ourLog.trace("Request Url: {}", requestUrl);
				ourLog.trace("Context Path: {}", servletContextPath);
			}

			servletPath = servletContextPath;

			IdDt id = null;
			IdDt versionId = null;
			String operation = null;

			String requestPath = requestFullPath.substring(servletPath.length());
			if (requestPath.length() > 0 && requestPath.charAt(0) == '/') {
				requestPath = requestPath.substring(1);
			}

			int contextIndex;
			if (servletPath.length() == 0) {
				contextIndex = requestUrl.indexOf(requestPath);
			} else {
				contextIndex = requestUrl.indexOf(servletPath);
			}

			String fhirServerBase = requestUrl.substring(0, contextIndex + servletPath.length());
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
			if ("metadata".equals(resourceName)) {
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
					id = new IdDt(nextString);
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
						versionId = new IdDt(resourceName + "/" + id.getUnqualifiedId() + "/_history/" + versionString);
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

			if (theRequestType == RequestType.PUT && versionId == null) {
				String contentLocation = theRequest.getHeader("Content-Location");
				if (contentLocation != null) {
					versionId = new IdDt(contentLocation);
				}
			}

			// TODO: look for more tokens for version, compartments, etc...

			Request r = new Request();
			r.setResourceName(resourceName);
			r.setId(id);
			r.setVersion(versionId);
			r.setOperation(operation);
			r.setSecondaryOperation(secondaryOperation);
			r.setParameters(params);
			r.setRequestType(theRequestType);
			if ("application/x-www-form-urlencoded".equals(theRequest.getContentType())) {
				r.setInputReader(new StringReader(""));
			} else {
				r.setInputReader(theRequest.getReader());
			}
			r.setFhirServerBase(fhirServerBase);
			r.setCompleteUrl(completeUrl);
			r.setServletRequest(theRequest);
			r.setServletResponse(theResponse);

			if (resourceMethod == null && resourceBinding != null) {
				resourceMethod = resourceBinding.getMethod(r);
			}
			if (null == resourceMethod) {
				throw new InvalidRequestException("No resource method available for the supplied parameters " + params);
			}

			resourceMethod.invokeServer(this, r, theResponse);

		} catch (AuthenticationException e) {
			theResponse.setStatus(e.getStatusCode());
			addHeadersToResponse(theResponse);
			theResponse.setContentType("text/plain");
			theResponse.setCharacterEncoding("UTF-8");
			theResponse.getWriter().write(e.getMessage());
		} catch (BaseServerResponseException e) {

			if (e instanceof InternalErrorException) {
				ourLog.error("Failure during REST processing", e);
			} else {
				ourLog.warn("Failure during REST processing: {}", e.toString());
			}

			theResponse.setStatus(e.getStatusCode());
			addHeadersToResponse(theResponse);
			theResponse.setContentType("text/plain");
			theResponse.setCharacterEncoding("UTF-8");
			theResponse.getWriter().append(e.getMessage());
			theResponse.getWriter().close();

		} catch (Throwable t) {
			// TODO: handle this better
			ourLog.error("Failed to process invocation", t);
			throw new ServletException(t);
		}

	}

	/**
	 * This method may be overridden by subclasses to do perform initialization that needs to be performed prior to the
	 * server being used.
	 */
	protected void initialize() {
		// nothing by default
	}

	public enum NarrativeModeEnum {
		NORMAL, ONLY, SUPPRESS;

		public static NarrativeModeEnum valueOfCaseInsensitive(String theCode) {
			return valueOf(NarrativeModeEnum.class, theCode.toUpperCase());
		}
	}

}
