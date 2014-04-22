package ca.uhn.fhir.rest.server;

import java.io.IOException;
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
import org.codehaus.stax2.ri.evt.ProcInstrEventImpl;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.method.BaseMethodBinding;
import ca.uhn.fhir.rest.method.ConformanceMethodBinding;
import ca.uhn.fhir.rest.method.Request;
import ca.uhn.fhir.rest.method.SearchMethodBinding;
import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.ConfigurationException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.MethodNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.provider.ServerConformanceProvider;
import ca.uhn.fhir.rest.server.provider.ServerProfileProvider;
import ca.uhn.fhir.util.ReflectionUtil;
import ca.uhn.fhir.util.VersionUtil;

public class RestfulServer extends HttpServlet {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(RestfulServer.class);

	private static final long serialVersionUID = 1L;

	private FhirContext myFhirContext;
	private Collection<Object> myProviders;
	private Map<String, ResourceBinding> myResourceNameToProvider = new HashMap<String, ResourceBinding>();
	private Collection<IResourceProvider> myResourceProviders;
	private ISecurityManager mySecurityManager;
	private BaseMethodBinding myServerConformanceMethod;
	private Object myServerConformanceProvider;
	private String myServerName = "HAPI FHIR Server";
	private String myServerVersion = VersionUtil.getVersion(); // defaults to
																// HAPI version
	private boolean myUseBrowserFriendlyContentTypes;

	public RestfulServer() {
		myFhirContext = new FhirContext();
		myServerConformanceProvider = new ServerConformanceProvider(this);
	}

	public void addHapiHeader(HttpServletResponse theHttpResponse) {
		theHttpResponse.addHeader("X-CatchingFhir", "Powered by HAPI FHIR " + VersionUtil.getVersion());
	}

	public FhirContext getFhirContext() {
		return myFhirContext;
	}

	/**
	 * Provides the non-resource specific providers which implement method calls
	 * on this server
	 * 
	 * @see #getResourceProviders()
	 */
	public Collection<Object> getProviders() {
		return myProviders;
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
	 * Returns the server conformance provider, which is the provider that is
	 * used to generate the server's conformance (metadata) statement.
	 * <p>
	 * By default, the {@link ServerConformanceProvider} is used, but this can
	 * be changed, or set to <code>null</code> if you do not wish to export a
	 * conformance statement.
	 * </p>
	 */
	public Object getServerConformanceProvider() {
		return myServerConformanceProvider;
	}

	/**
	 * Gets the server's name, as exported in conformance profiles exported by
	 * the server. This is informational only, but can be helpful to set with
	 * something appropriate.
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
	 * Gets the server's version, as exported in conformance profiles exported
	 * by the server. This is informational only, but can be helpful to set with
	 * something appropriate.
	 */
	public String getServerVersion() {
		return myServerVersion;
	}

	@Override
	public final void init() throws ServletException {
		initialize();
		try {
			ourLog.info("Initializing HAPI FHIR restful server");

			mySecurityManager = getSecurityManager();
			if (null == mySecurityManager) {
				ourLog.warn("No security manager has been provided, requests will not be authenticated!");
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
					findResourceMethods(provider);
				}
			}

			Collection<Object> providers = getProviders();
			if (providers != null) {
				for (Object next : providers) {
					findResourceMethods(next);
				}
			}

			findResourceMethods(getServerProfilesProvider());
			findSystemMethods(getServerConformanceProvider());

		} catch (Exception ex) {
			ourLog.error("An error occurred while loading request handlers!", ex);
			throw new ServletException("Failed to initialize FHIR Restful server", ex);
		}

		ourLog.info("A FHIR has been lit on this server");
	}

	public boolean isUseBrowserFriendlyContentTypes() {
		return myUseBrowserFriendlyContentTypes;
	}

	/**
	 * Sets the non-resource specific providers which implement method calls on
	 * this server
	 * 
	 * @see #setResourceProviders(Collection)
	 */
	public void setProviders(Collection<Object> theProviders) {
		myProviders = theProviders;
	}

	/**
	 * Sets the non-resource specific providers which implement method calls on
	 * this server
	 * 
	 * @see #setResourceProviders(Collection)
	 */
	public void setProviders(Object... theProviders) {
		myProviders = Arrays.asList(theProviders);
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
	 * Returns the server conformance provider, which is the provider that is
	 * used to generate the server's conformance (metadata) statement.
	 * <p>
	 * By default, the {@link ServerConformanceProvider} is used, but this can
	 * be changed, or set to <code>null</code> if you do not wish to export a
	 * conformance statement.
	 * </p>
	 * Note that this method can only be called before the server is
	 * initialized.
	 * 
	 * @throws IllegalStateException
	 *             Note that this method can only be called prior to
	 *             {@link #init() initialization} and will throw an
	 *             {@link IllegalStateException} if called after that.
	 */
	public void setServerConformanceProvider(Object theServerConformanceProvider) {
		if (myFhirContext != null) {
			throw new IllegalStateException("Server is already started");
		}
		myServerConformanceProvider = theServerConformanceProvider;
	}

	/**
	 * Gets the server's name, as exported in conformance profiles exported by
	 * the server. This is informational only, but can be helpful to set with
	 * something appropriate.
	 * 
	 * @see RestfulServer#setServerName(StringDt)
	 */
	public void setServerName(String theServerName) {
		myServerName = theServerName;
	}

	/**
	 * Gets the server's version, as exported in conformance profiles exported
	 * by the server. This is informational only, but can be helpful to set with
	 * something appropriate.
	 */
	public void setServerVersion(String theServerVersion) {
		myServerVersion = theServerVersion;
	}

	/**
	 * If set to <code>true</code> (default is false), the server will use
	 * browser friendly content-types (instead of standard FHIR ones) when it
	 * detects that the request is coming from a browser instead of a FHIR
	 */
	public void setUseBrowserFriendlyContentTypes(boolean theUseBrowserFriendlyContentTypes) {
		myUseBrowserFriendlyContentTypes = theUseBrowserFriendlyContentTypes;
	}

	private void findResourceMethods(Object theProvider) throws Exception {

		ourLog.info("Scanning type for RESTful methods: {}", theProvider.getClass());

		Class<?> clazz = theProvider.getClass();
		for (Method m : clazz.getDeclaredMethods()) {
			if (Modifier.isPublic(m.getModifiers()) && !Modifier.isStatic(m.getModifiers())) {
				ourLog.debug("Scanning public method: {}#{}", theProvider.getClass(), m.getName());

				BaseMethodBinding foundMethodBinding = BaseMethodBinding.bindMethod(m, myFhirContext, theProvider);
				if (foundMethodBinding != null) {
					
					RuntimeResourceDefinition definition = myFhirContext.getResourceDefinition(retType);
					ResourceBinding resourceBinding;
					if (myResourceNameToProvider.containsKey(definition.getName())) {
						resourceBinding = myResourceNameToProvider.get(definition.getName());
					} else {
						resourceBinding = new ResourceBinding();
						String resourceName = definition.getName();
						resourceBinding.setResourceName(resourceName);
						myResourceNameToProvider.put(resourceName, resourceBinding);
					}

					resourceBinding.addMethod(foundMethodBinding);
					ourLog.info(" * Method: {}#{} is a handler", theProvider.getClass(), m.getName());
				} else {
					ourLog.debug(" * Method: {}#{} is not a handler", theProvider.getClass(), m.getName());
				}
			}
		}
	}

	private void findSystemMethods(Object theSystemProvider) {
		Class<?> clazz = theSystemProvider.getClass();
		for (Method m : clazz.getDeclaredMethods()) {
			if (Modifier.isPublic(m.getModifiers())) {
				ourLog.debug("Scanning public method: {}#{}", theSystemProvider.getClass(), m.getName());

				BaseMethodBinding foundMethodBinding = BaseMethodBinding.bindSystemMethod(m, myFhirContext);
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

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		handleRequest(SearchMethodBinding.RequestType.DELETE, request, response);
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

	protected void handleRequest(SearchMethodBinding.RequestType requestType, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {

			if (null != mySecurityManager) {
				mySecurityManager.authenticate(request);
			}

			String resourceName = null;
			String requestFullPath = StringUtils.defaultString(request.getRequestURI());
			// String contextPath =
			// StringUtils.defaultString(request.getContextPath());
			String servletPath = StringUtils.defaultString(request.getServletPath());
			StringBuffer requestUrl = request.getRequestURL();
			String servletContextPath = "";
			if (request.getServletContext() != null) {
				servletContextPath = StringUtils.defaultIfBlank(request.getServletContext().getContextPath(), servletPath);
			} else {
				servletContextPath = servletPath;
			}

			ourLog.info("Request FullPath: {}", requestFullPath);
			ourLog.info("Servlet Path: {}", servletPath);
			ourLog.info("Request Url: {}", requestUrl);
			ourLog.info("Context Path: {}", servletContextPath);

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

			String completeUrl = StringUtils.isNotBlank(request.getQueryString()) ? requestUrl + "?" + request.getQueryString() : requestUrl.toString();

			Map<String, String[]> params = new HashMap<String, String[]>(request.getParameterMap());

			StringTokenizer tok = new StringTokenizer(requestPath, "/");
			if (!tok.hasMoreTokens()) {
				throw new MethodNotFoundException("No resource name specified");
			}
			resourceName = tok.nextToken();

			ResourceBinding resourceBinding = null;
			BaseMethodBinding resourceMethod = null;
			if ("metadata".equals(resourceName)) {
				resourceMethod = myServerConformanceMethod;
			} else {
				resourceBinding = myResourceNameToProvider.get(resourceName);
				if (resourceBinding == null) {
					throw new MethodNotFoundException("Unknown resource type '" + resourceName + "' - Server knows how to handle: " + myResourceNameToProvider.keySet());
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
				if (nextString.startsWith("_")) {
					if (operation != null) {
						throw new InvalidRequestException("URL Path contains two operations (part beginning with _): " + requestPath);
					}
					operation = nextString;
				}
			}

			if (tok.hasMoreTokens()) {
				String nextString = tok.nextToken();
				versionId = new IdDt(nextString);
			}

			// TODO: look for more tokens for version, compartments, etc...

			Request r = new Request();
			r.setResourceName(resourceName);
			r.setId(id);
			r.setVersion(versionId);
			r.setOperation(operation);
			r.setParameters(params);
			r.setRequestType(requestType);
			r.setInputReader(request.getReader());
			r.setFhirServerBase(fhirServerBase);
			r.setCompleteUrl(completeUrl);
			r.setServletRequest(request);

			if (resourceMethod == null && resourceBinding != null) {
				resourceMethod = resourceBinding.getMethod(r);
			}
			if (null == resourceMethod) {
				throw new MethodNotFoundException("No resource method available for the supplied parameters " + params);
			}

			resourceMethod.invokeServer(this, r, response);

		} catch (AuthenticationException e) {
			response.setStatus(e.getStatusCode());
			addHapiHeader(response);
			response.setContentType("text/plain");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(e.getMessage());
		} catch (BaseServerResponseException e) {

			if (e instanceof InternalErrorException) {
				ourLog.error("Failure during REST processing", e);
			} else {
				ourLog.warn("Failure during REST processing: {}", e.toString());
			}

			response.setStatus(e.getStatusCode());
			addHapiHeader(response);
			response.setContentType("text/plain");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().append(e.getMessage());
			response.getWriter().close();

		} catch (Throwable t) {
			// TODO: handle this better
			ourLog.error("Failed to process invocation", t);
			throw new ServletException(t);
		}

	}

	/**
	 * This method may be overridden by subclasses to do perform initialization
	 * that needs to be performed prior to the server being used.
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
