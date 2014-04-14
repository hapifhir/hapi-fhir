package ca.uhn.fhir.rest.server;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
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
import ca.uhn.fhir.narrative.INarrativeGenerator;
import ca.uhn.fhir.rest.method.BaseMethodBinding;
import ca.uhn.fhir.rest.method.ConformanceMethodBinding;
import ca.uhn.fhir.rest.method.Request;
import ca.uhn.fhir.rest.method.SearchMethodBinding;
import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.MethodNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.provider.ServerConformanceProvider;
import ca.uhn.fhir.rest.server.provider.ServerProfileProvider;
import ca.uhn.fhir.util.VersionUtil;

public abstract class RestfulServer extends HttpServlet {


	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(RestfulServer.class);

	private static final long serialVersionUID = 1L;

	private FhirContext myFhirContext;
	private INarrativeGenerator myNarrativeGenerator;
	private Map<String, ResourceBinding> myResourceNameToProvider = new HashMap<String, ResourceBinding>();
	private Object myServerConformanceProvider;
	private Map<Class<? extends IResource>, IResourceProvider> myTypeToProvider = new HashMap<Class<? extends IResource>, IResourceProvider>();
	private boolean myUseBrowserFriendlyContentTypes;
	private ISecurityManager securityManager;
	private BaseMethodBinding myServerConformanceMethod;
	private String myServerName = "HAPI FHIR Server";
	private String myServerVersion = VersionUtil.getVersion();

	public RestfulServer() {
		myFhirContext = new FhirContext();
		myServerConformanceProvider=new ServerConformanceProvider(this);
	}

	public void addHapiHeader(HttpServletResponse theHttpResponse) {
		theHttpResponse.addHeader("X-CatchingFhir", "Powered by HAPI FHIR " + VersionUtil.getVersion());
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

	private void findResourceMethods(IResourceProvider theProvider) throws Exception {

		Class<? extends IResource> resourceType = theProvider.getResourceType();
		RuntimeResourceDefinition definition = myFhirContext.getResourceDefinition(resourceType);

		ResourceBinding r = new ResourceBinding();
		r.setResourceProvider(theProvider);
		r.setResourceName(definition.getName());
		myResourceNameToProvider.put(definition.getName(), r);

		ourLog.info("Scanning type for RESTful methods: {}", theProvider.getClass());

		Class<?> clazz = theProvider.getClass();
		for (Method m : clazz.getDeclaredMethods()) {
			if (Modifier.isPublic(m.getModifiers())) {
				ourLog.debug("Scanning public method: {}#{}", theProvider.getClass(), m.getName());

				BaseMethodBinding foundMethodBinding = BaseMethodBinding.bindMethod(theProvider.getResourceType(), m, myFhirContext, theProvider);
				if (foundMethodBinding != null) {
					r.addMethod(foundMethodBinding);
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

	public FhirContext getFhirContext() {
		return myFhirContext;
	}

	public INarrativeGenerator getNarrativeGenerator() {
		return myNarrativeGenerator;
	}

	public Collection<ResourceBinding> getResourceBindings() {
		return myResourceNameToProvider.values();
	}

	/**
	 * This method must be overridden to provide one or more resource providers
	 */
	public abstract Collection<IResourceProvider> getResourceProviders();


	/**
	 * This method should be overridden to provide a security manager instance. By default, returns null.
	 */
	public ISecurityManager getSecurityManager() {
		return null;
	}

	/**
	 * Returns the server conformance provider, which is the provider that
	 * is used to generate the server's conformance (metadata) statement.
	 * <p>
	 * By default, the {@link ServerConformanceProvider} is used, but
	 * this can be changed, or set to <code>null</code> if you do not wish
	 * to export a conformance statement.
	 * </p>
	 */
	public Object getServerConformanceProvider() {
		return myServerConformanceProvider;
	}

	public IResourceProvider getServerProfilesProvider() {
		return new ServerProfileProvider(getFhirContext());
	}

	protected void handleRequest(SearchMethodBinding.RequestType requestType, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {

			if (null != securityManager) {
				securityManager.authenticate(request);
			}

			String resourceName = null;
			String requestFullPath = StringUtils.defaultString(request.getRequestURI());
			// String contextPath = StringUtils.defaultString(request.getContextPath());
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

			Object provider=null;
			ResourceBinding resourceBinding=null;
			BaseMethodBinding resourceMethod=null;
			if ("metadata".equals(resourceName)) {
				provider = myServerConformanceProvider;
				if (provider==null) {
					throw new ResourceNotFoundException("This server does not support 'metadata' query");
				}
				resourceMethod = myServerConformanceMethod;
			} else {
				resourceBinding = myResourceNameToProvider.get(resourceName);
				if (resourceBinding == null) {
					throw new MethodNotFoundException("Unknown resource type '" + resourceName+"' - Server knows how to handle: "+myResourceNameToProvider.keySet());
				}
				provider = resourceBinding.getResourceProvider();
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
					if (operation !=null) {
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
			r.setResourceProvider(provider);
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

	@Override
	public final void init() throws ServletException {
		initialize();
		try {
			ourLog.info("Initializing HAPI FHIR restful server");

			securityManager = getSecurityManager();
			if (null == securityManager) {
				ourLog.warn("No security manager has been provided, requests will not be authenticated!");
			}

			Collection<IResourceProvider> resourceProvider = getResourceProviders();
			for (IResourceProvider nextProvider : resourceProvider) {
				Class<? extends IResource> resourceType = nextProvider.getResourceType();
				if (resourceType==null) {
					throw new NullPointerException("getResourceType() on class '" + nextProvider.getClass().getCanonicalName() + "' returned null");
				}
				if (myTypeToProvider.containsKey(resourceType)) {
					throw new ServletException("Multiple providers for type: " + resourceType.getCanonicalName());
				}
				myTypeToProvider.put(resourceType, nextProvider);
			}

			ourLog.info("Got {} resource providers", myTypeToProvider.size());

			myFhirContext.setNarrativeGenerator(myNarrativeGenerator);

			for (IResourceProvider provider : myTypeToProvider.values()) {
				findResourceMethods(provider);
			}

			findResourceMethods(getServerProfilesProvider());
			findSystemMethods(getServerConformanceProvider());

		} catch (Exception ex) {
			ourLog.error("An error occurred while loading request handlers!", ex);
			throw new ServletException("Failed to initialize FHIR Restful server", ex);
		}

		ourLog.info("A FHIR has been lit on this server");
	}

	/**
	 * This method may be overridden by subclasses to do perform initialization that needs to be performed prior to the server being used.
	 */
	protected void initialize() {
		// nothing by default
	}

	public boolean isUseBrowserFriendlyContentTypes() {
		return myUseBrowserFriendlyContentTypes;
	}
	
	/**
	 * Sets the {@link INarrativeGenerator Narrative Generator} to use when serializing responses from this server, or <code>null</code> (which is the default) to disable narrative generation.
	 * Note that this method can only be called before the server is initialized. 
	 * 
	 * @throws IllegalStateException
	 *             Note that this method can only be called prior to {@link #init() initialization} and will throw an {@link IllegalStateException} if called after that.
	 */
	public void setNarrativeGenerator(INarrativeGenerator theNarrativeGenerator) {
		if (myFhirContext != null) {
			throw new IllegalStateException("Server has already been initialized, can not change this property");
		}
		myNarrativeGenerator = theNarrativeGenerator;
	}

	/**
	 * Returns the server conformance provider, which is the provider that
	 * is used to generate the server's conformance (metadata) statement.
	 * <p>
	 * By default, the {@link ServerConformanceProvider} is used, but
	 * this can be changed, or set to <code>null</code> if you do not wish
	 * to export a conformance statement.
	 * </p>
	 * Note that this method can only be called before the server is initialized. 
	 * 
	 * @throws IllegalStateException
	 *             Note that this method can only be called prior to {@link #init() initialization} and will throw an {@link IllegalStateException} if called after that.
	 */
	public void setServerConformanceProvider(Object theServerConformanceProvider) {
		if (myFhirContext!=null) {
			throw new IllegalStateException("Server is already started");
		}
		myServerConformanceProvider = theServerConformanceProvider;
	}

	/**
	 * If set to <code>true</code> (default is false), the server will use browser friendly content-types (instead of standard FHIR ones) when it detects that the request is coming from a browser
	 * instead of a FHIR
	 */
	public void setUseBrowserFriendlyContentTypes(boolean theUseBrowserFriendlyContentTypes) {
		myUseBrowserFriendlyContentTypes = theUseBrowserFriendlyContentTypes;
	}

	public enum NarrativeModeEnum {
		NORMAL, 
		ONLY, 
		SUPPRESS;
		
		public static NarrativeModeEnum valueOfCaseInsensitive(String theCode) {
			return valueOf(NarrativeModeEnum.class, theCode.toUpperCase());
		}
	}

	/**
	 * Gets the server's name, as exported in conformance profiles exported by the server. This
	 * is informational only, but can be helpful to set with something appropriate.
	 * 
	 * @see RestfulServer#setServerName(StringDt)
	 */
	public String getServerName() {
		return myServerName;
	}

	/**
	 * Gets the server's name, as exported in conformance profiles exported by the server. This
	 * is informational only, but can be helpful to set with something appropriate.
	 * 
	 * @see RestfulServer#setServerName(StringDt)
	 */
	public void setServerName(String theServerName) {
		myServerName = theServerName;
	}

	/**
	 * Gets the server's version, as exported in conformance profiles exported by the server. This
	 * is informational only, but can be helpful to set with something appropriate.
	 */
	public void setServerVersion(String theServerVersion) {
		myServerVersion = theServerVersion;
	}

	/**
	 * Gets the server's version, as exported in conformance profiles exported by the server. This
	 * is informational only, but can be helpful to set with something appropriate.
	 */
	public String getServerVersion() {
		return myServerVersion;
	}

}
