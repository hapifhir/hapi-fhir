package ca.uhn.fhir.rest.server;

/*
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
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

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.jar.Manifest;

import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.*;

import ca.uhn.fhir.rest.server.tenant.ITenantIdentificationStrategy;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import ca.uhn.fhir.context.*;
import ca.uhn.fhir.context.api.AddProfileTagEnum;
import ca.uhn.fhir.context.api.BundleInclusionRule;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.*;
import ca.uhn.fhir.rest.api.server.*;
import ca.uhn.fhir.rest.server.RestfulServerUtils.ResponseEncoding;
import ca.uhn.fhir.rest.server.exceptions.*;
import ca.uhn.fhir.rest.server.interceptor.*;
import ca.uhn.fhir.rest.server.method.*;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.*;

@SuppressWarnings("WeakerAccess")
public class RestfulServer extends HttpServlet implements IRestfulServer<ServletRequestDetails> {

	/**
	 * All incoming requests will have an attribute added to {@link HttpServletRequest#getAttribute(String)}
	 * with this key. The value will be a Java {@link Date} with the time that request processing began.
	 */
	public static final String REQUEST_START_TIME = RestfulServer.class.getName() + "REQUEST_START_TIME";

	/**
	 * Default setting for {@link #setETagSupport(ETagSupportEnum) ETag Support}: {@link ETagSupportEnum#ENABLED}
	 */
	public static final ETagSupportEnum DEFAULT_ETAG_SUPPORT = ETagSupportEnum.ENABLED;
	/**
	 * Requests will have an HttpServletRequest attribute set with this name, containing the servlet
	 * context, in order to avoid a dependency on Servlet-API 3.0+
	 */
	public static final String SERVLET_CONTEXT_ATTRIBUTE = "ca.uhn.fhir.rest.server.RestfulServer.servlet_context";
	private static final ExceptionHandlingInterceptor DEFAULT_EXCEPTION_HANDLER = new ExceptionHandlingInterceptor();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(RestfulServer.class);
	private static final long serialVersionUID = 1L;
	private final List<IServerInterceptor> myInterceptors = new ArrayList<>();
	private final List<Object> myPlainProviders = new ArrayList<>();
	private final List<IResourceProvider> myResourceProviders = new ArrayList<>();
	private BundleInclusionRule myBundleInclusionRule = BundleInclusionRule.BASED_ON_INCLUDES;
	private boolean myDefaultPrettyPrint = false;
	private EncodingEnum myDefaultResponseEncoding = EncodingEnum.XML;
	private ETagSupportEnum myETagSupport = DEFAULT_ETAG_SUPPORT;
	private FhirContext myFhirContext;
	private boolean myIgnoreServerParsedRequestParameters = true;
	private String myImplementationDescription;
	private IPagingProvider myPagingProvider;
	private Lock myProviderRegistrationMutex = new ReentrantLock();
	private Map<String, ResourceBinding> myResourceNameToBinding = new HashMap<>();
	private IServerAddressStrategy myServerAddressStrategy = new IncomingRequestAddressStrategy();
	private ResourceBinding myServerBinding = new ResourceBinding();
	private ResourceBinding myGlobalBinding = new ResourceBinding();
	private BaseMethodBinding<?> myServerConformanceMethod;
	private Object myServerConformanceProvider;
	private String myServerName = "HAPI FHIR Server";
	/** This is configurable but by default we just use HAPI version */
	private String myServerVersion = VersionUtil.getVersion();
	private boolean myStarted;
	private Map<String, IResourceProvider> myTypeToProvider = new HashMap<>();
	private boolean myUncompressIncomingContents = true;
	private boolean myUseBrowserFriendlyContentTypes;
	private ITenantIdentificationStrategy myTenantIdentificationStrategy;

	/**
	 * Constructor. Note that if no {@link FhirContext} is passed in to the server (either through the constructor, or
	 * through {@link #setFhirContext(FhirContext)}) the server will determine which
	 * version of FHIR to support through classpath scanning. This is brittle, and it is highly recommended to explicitly
	 * specify a FHIR version.
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

	private static boolean partIsOperation(String nextString) {
		return nextString.length() > 0 && (nextString.charAt(0) == '_' || nextString.charAt(0) == '$' || nextString.equals(Constants.URL_TOKEN_METADATA));
	}

	private void addContentLocationHeaders(RequestDetails theRequest, HttpServletResponse servletResponse, MethodOutcome response, String resourceName) {
		if (response != null && response.getId() != null) {
			addLocationHeader(theRequest, servletResponse, response, Constants.HEADER_LOCATION, resourceName);
			addLocationHeader(theRequest, servletResponse, response, Constants.HEADER_CONTENT_LOCATION, resourceName);
		}
	}

	/**
	 * This method is called prior to sending a response to incoming requests. It is used to add custom headers.
	 * <p>
	 * Use caution if overriding this method: it is recommended to call <code>super.addHeadersToResponse</code> to avoid
	 * inadvertently disabling functionality.
	 * </p>
	 */
	public void addHeadersToResponse(HttpServletResponse theHttpResponse) {
		StringBuilder b = new StringBuilder();
		b.append("HAPI FHIR ");
		b.append(VersionUtil.getVersion());
		b.append(" REST Server (FHIR Server; FHIR ");
		b.append(myFhirContext.getVersion().getVersion().getFhirVersionString());
		b.append('/');
		b.append(myFhirContext.getVersion().getVersion().name());
		b.append(")");
		theHttpResponse.addHeader("X-Powered-By", b.toString());
	}

	private void addLocationHeader(RequestDetails theRequest, HttpServletResponse theResponse, MethodOutcome response, String headerLocation, String resourceName) {
		StringBuilder b = new StringBuilder();
		b.append(theRequest.getFhirServerBase());
		b.append('/');
		b.append(resourceName);
		b.append('/');
		b.append(response.getId().getIdPart());
		if (response.getId().hasVersionIdPart()) {
			b.append("/" + Constants.PARAM_HISTORY + "/");
			b.append(response.getId().getVersionIdPart());
		}
		theResponse.addHeader(headerLocation, b.toString());

	}

	private void assertProviderIsValid(Object theNext) throws ConfigurationException {
		if (Modifier.isPublic(theNext.getClass().getModifiers()) == false) {
			throw new ConfigurationException("Can not use provider '" + theNext.getClass() + "' - Class must be public");
		}
	}

	public RestulfulServerConfiguration createConfiguration() {
		RestulfulServerConfiguration result = new RestulfulServerConfiguration();
		result.setResourceBindings(getResourceBindings());
		result.setServerBindings(getServerBindings());
		result.setImplementationDescription(getImplementationDescription());
		result.setServerVersion(getServerVersion());
		result.setServerName(getServerName());
		result.setFhirContext(getFhirContext());
		result.setServerAddressStrategy(myServerAddressStrategy);
		InputStream inputStream = null;
		try {
			inputStream = getClass().getResourceAsStream("/META-INF/MANIFEST.MF");
			if (inputStream != null) {
				Manifest manifest = new Manifest(inputStream);
				result.setConformanceDate(manifest.getMainAttributes().getValue("Build-Time"));
			}
		} catch (IOException e) {
			// fall through
		} finally {
			if (inputStream != null) {
				IOUtils.closeQuietly(inputStream);
			}
		}
		return result;
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

	/**
	 * Figure out and return whichever method binding is appropriate for
	 * the given request
	 */
	public BaseMethodBinding<?> determineResourceMethod(RequestDetails requestDetails, String requestPath) {
		RequestTypeEnum requestType = requestDetails.getRequestType();

		ResourceBinding resourceBinding = null;
		BaseMethodBinding<?> resourceMethod = null;
		String resourceName = requestDetails.getResourceName();
		if (myServerConformanceMethod.incomingServerRequestMatchesMethod(requestDetails)) {
			resourceMethod = myServerConformanceMethod;
		} else if (resourceName == null) {
			resourceBinding = myServerBinding;
		} else {
			resourceBinding = myResourceNameToBinding.get(resourceName);
			if (resourceBinding == null) {
				throw new ResourceNotFoundException("Unknown resource type '" + resourceName + "' - Server knows how to handle: " + myResourceNameToBinding.keySet());
			}
		}

		if (resourceMethod == null) {
			if (resourceBinding != null) {
				resourceMethod = resourceBinding.getMethod(requestDetails);
			}
			if (resourceMethod == null) {
				resourceMethod = myGlobalBinding.getMethod(requestDetails);
			}
		}
		if (resourceMethod == null) {
			if (isBlank(requestPath)) {
				throw new InvalidRequestException(myFhirContext.getLocalizer().getMessage(RestfulServer.class, "rootRequest"));
			}
			throw new InvalidRequestException(myFhirContext.getLocalizer().getMessage(RestfulServer.class, "unknownMethod", requestType.name(), requestPath, requestDetails.getParameters().keySet()));
		}
		return resourceMethod;
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

	private void findResourceMethods(Object theProvider) {

		ourLog.info("Scanning type for RESTful methods: {}", theProvider.getClass());
		int count = 0;

		Class<?> clazz = theProvider.getClass();
		Class<?> supertype = clazz.getSuperclass();
		while (!Object.class.equals(supertype)) {
			count += findResourceMethods(theProvider, supertype);
			supertype = supertype.getSuperclass();
		}

		try {
			count += findResourceMethods(theProvider, clazz);
		} catch (ConfigurationException e) {
			throw new ConfigurationException("Failure scanning class " + clazz.getSimpleName() + ": " + e.getMessage());
		}
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
			}
			if (Modifier.isStatic(m.getModifiers())) {
				throw new ConfigurationException("Method '" + m.getName() + "' is static, FHIR RESTful methods must not be static");
			}
			ourLog.debug("Scanning public method: {}#{}", theProvider.getClass(), m.getName());

			String resourceName = foundMethodBinding.getResourceName();
			ResourceBinding resourceBinding;
			if (resourceName == null) {
				if (foundMethodBinding.isGlobalMethod()) {
					resourceBinding = myGlobalBinding;
				} else {
					resourceBinding = myServerBinding;
				}
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

		return count;
	}

	/**
	 * @deprecated As of HAPI FHIR 1.5, this property has been moved to
	 *             {@link FhirContext#setAddProfileTagWhenEncoding(AddProfileTagEnum)}
	 */
	@Override
	@Deprecated
	public AddProfileTagEnum getAddProfileTag() {
		return myFhirContext.getAddProfileTagWhenEncoding();
	}

	/**
	 * Sets the profile tagging behaviour for the server. When set to a value other than {@link AddProfileTagEnum#NEVER}
	 * (which is the default), the server will automatically add a profile tag based on
	 * the class of the resource(s) being returned.
	 *
	 * @param theAddProfileTag
	 *           The behaviour enum (must not be null)
	 * @deprecated As of HAPI FHIR 1.5, this property has been moved to
	 *             {@link FhirContext#setAddProfileTagWhenEncoding(AddProfileTagEnum)}
	 */
	@Deprecated
	@CoverageIgnore
	public void setAddProfileTag(AddProfileTagEnum theAddProfileTag) {
		Validate.notNull(theAddProfileTag, "theAddProfileTag must not be null");
		myFhirContext.setAddProfileTagWhenEncoding(theAddProfileTag);
	}

	@Override
	public BundleInclusionRule getBundleInclusionRule() {
		return myBundleInclusionRule;
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
	 * Returns the default encoding to return (XML/JSON) if an incoming request does not specify a preference (either
	 * with the <code>_format</code> URL parameter, or with an <code>Accept</code> header
	 * in the request. The default is {@link EncodingEnum#XML}. Will not return null.
	 */
	@Override
	public EncodingEnum getDefaultResponseEncoding() {
		return myDefaultResponseEncoding;
	}

	/**
	 * Sets the default encoding to return (XML/JSON) if an incoming request does not specify a preference (either with
	 * the <code>_format</code> URL parameter, or with an <code>Accept</code> header in
	 * the request. The default is {@link EncodingEnum#XML}.
	 * <p>
	 * Note when testing this feature: Some browsers will include "application/xml" in their Accept header, which means
	 * that the
	 * </p>
	 */
	public void setDefaultResponseEncoding(EncodingEnum theDefaultResponseEncoding) {
		Validate.notNull(theDefaultResponseEncoding, "theDefaultResponseEncoding can not be null");
		myDefaultResponseEncoding = theDefaultResponseEncoding;
	}

	@Override
	public ETagSupportEnum getETagSupport() {
		return myETagSupport;
	}

	/**
	 * Sets (enables/disables) the server support for ETags. Must not be <code>null</code>. Default is
	 * {@link #DEFAULT_ETAG_SUPPORT}
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

	/**
	 * Gets the {@link FhirContext} associated with this server. For efficient processing, resource providers and plain
	 * providers should generally use this context if one is needed, as opposed to
	 * creating their own.
	 */
	@Override
	public FhirContext getFhirContext() {
		if (myFhirContext == null) {
			//TODO: Use of a deprecated method should be resolved.
			myFhirContext = new FhirContext();
		}
		return myFhirContext;
	}

	public void setFhirContext(FhirContext theFhirContext) {
		Validate.notNull(theFhirContext, "FhirContext must not be null");
		myFhirContext = theFhirContext;
	}

	public String getImplementationDescription() {
		return myImplementationDescription;
	}

	public void setImplementationDescription(String theImplementationDescription) {
		myImplementationDescription = theImplementationDescription;
	}

	/**
	 * Returns a list of all registered server interceptors
	 */
	@Override
	public List<IServerInterceptor> getInterceptors() {
		return Collections.unmodifiableList(myInterceptors);
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

	@Override
	public IPagingProvider getPagingProvider() {
		return myPagingProvider;
	}

	/**
	 * Sets the paging provider to use, or <code>null</code> to use no paging (which is the default)
	 */
	public void setPagingProvider(IPagingProvider thePagingProvider) {
		myPagingProvider = thePagingProvider;
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
	 * Sets the non-resource specific providers which implement method calls on this server.
	 *
	 * @see #setResourceProviders(Collection)
	 */
	public void setPlainProviders(Collection<Object> theProviders) {
		myPlainProviders.clear();
		if (theProviders != null) {
			myPlainProviders.addAll(theProviders);
		}
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
	 * Allows users of RestfulServer to override the getRequestPath method to let them build their custom request path
	 * implementation
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
	 * Sets the resource providers for this server
	 */
	public void setResourceProviders(Collection<IResourceProvider> theResourceProviders) {
		myResourceProviders.clear();
		if (theResourceProviders != null) {
			myResourceProviders.addAll(theResourceProviders);
		}
	}

	/**
	 * Sets the resource providers for this server
	 */
	public void setResourceProviders(IResourceProvider... theResourceProviders) {
		myResourceProviders.clear();
		if (theResourceProviders != null) {
			myResourceProviders.addAll(Arrays.asList(theResourceProviders));
		}
	}

	/**
	 * Get the server address strategy, which is used to determine what base URL to provide clients to refer to this
	 * server. Defaults to an instance of {@link IncomingRequestAddressStrategy}
	 */
	public IServerAddressStrategy getServerAddressStrategy() {
		return myServerAddressStrategy;
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
	 * Returns the server base URL (with no trailing '/') for a given request
	 * @param theRequest
	 */
	public String getServerBaseForRequest(ServletRequestDetails theRequest) {
		String fhirServerBase;
		fhirServerBase = myServerAddressStrategy.determineServerBase(getServletContext(), theRequest.getServletRequest());

		if (fhirServerBase.endsWith("/")) {
			fhirServerBase = fhirServerBase.substring(0, fhirServerBase.length() - 1);
		}

		if (myTenantIdentificationStrategy != null) {
			fhirServerBase = myTenantIdentificationStrategy.massageServerBaseUrl(fhirServerBase, theRequest);
		}

		return fhirServerBase;
	}

	/**
	 * Returns the method bindings for this server which are not specific to any particular resource type. This method is
	 * internal to HAPI and developers generally do not need to interact with it. Use
	 * with caution, as it may change.
	 */
	public List<BaseMethodBinding<?>> getServerBindings() {
		return myServerBinding.getMethodBindings();
	}

	/**
	 * Returns the server conformance provider, which is the provider that is used to generate the server's conformance
	 * (metadata) statement if one has been explicitly defined.
	 * <p>
	 * By default, the ServerConformanceProvider for the declared version of FHIR is used, but this can be changed, or
	 * set to <code>null</code> to use the appropriate one for the given FHIR version.
	 * </p>
	 */
	public Object getServerConformanceProvider() {
		return myServerConformanceProvider;
	}

	/**
	 * Returns the server conformance provider, which is the provider that is used to generate the server's conformance
	 * (metadata) statement.
	 * <p>
	 * By default, the ServerConformanceProvider implementation for the declared version of FHIR is used, but this can be
	 * changed, or set to <code>null</code> if you do not wish to export a conformance
	 * statement.
	 * </p>
	 * Note that this method can only be called before the server is initialized.
	 *
	 * @throws IllegalStateException
	 *            Note that this method can only be called prior to {@link #init() initialization} and will throw an
	 *            {@link IllegalStateException} if called after that.
	 */
	public void setServerConformanceProvider(Object theServerConformanceProvider) {
		if (myStarted) {
			throw new IllegalStateException("Server is already started");
		}

		// call the setRestfulServer() method to point the Conformance
		// Provider to this server instance. This is done to avoid
		// passing the server into the constructor. Having that sort
		// of cross linkage causes reference cycles in Spring wiring
		try {
			Method setRestfulServer = theServerConformanceProvider.getClass().getMethod("setRestfulServer", new Class[] { RestfulServer.class });
			if (setRestfulServer != null) {
				setRestfulServer.invoke(theServerConformanceProvider, new Object[] { this });
			}
		} catch (Exception e) {
			ourLog.warn("Error calling IServerConformanceProvider.setRestfulServer", e);
		}
		myServerConformanceProvider = theServerConformanceProvider;
	}

	/**
	 * If provided (default is <code>null</code>), the tenant identification
	 * strategy provides a mechanism for a multitenant server to identify which tenant
	 * a given request corresponds to.
	 */
	public void setTenantIdentificationStrategy(ITenantIdentificationStrategy theTenantIdentificationStrategy) {
		myTenantIdentificationStrategy = theTenantIdentificationStrategy;
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

	/**
	 * Sets the server's name, as exported in conformance profiles exported by the server. This is informational only,
	 * but can be helpful to set with something appropriate.
	 */
	public void setServerName(String theServerName) {
		myServerName = theServerName;
	}

	public IResourceProvider getServerProfilesProvider() {
		IFhirVersionServer versionServer = (IFhirVersionServer) getFhirContext().getVersion().getServerVersion();
		return versionServer.createServerProfilesProvider(this);
	}

	/**
	 * Gets the server's version, as exported in conformance profiles exported by the server. This is informational only,
	 * but can be helpful to set with something appropriate.
	 */
	public String getServerVersion() {
		return myServerVersion;
	}

	/**
	 * Gets the server's version, as exported in conformance profiles exported by the server. This is informational only,
	 * but can be helpful to set with something appropriate.
	 */
	public void setServerVersion(String theServerVersion) {
		myServerVersion = theServerVersion;
	}

	@SuppressWarnings("WeakerAccess")
	protected void handleRequest(RequestTypeEnum theRequestType, HttpServletRequest theRequest, HttpServletResponse theResponse) throws ServletException, IOException {
		String fhirServerBase;
		ServletRequestDetails requestDetails = new ServletRequestDetails();
		requestDetails.setServer(this);
		requestDetails.setRequestType(theRequestType);
		requestDetails.setServletRequest(theRequest);
		requestDetails.setServletResponse(theResponse);

		theRequest.setAttribute(SERVLET_CONTEXT_ATTRIBUTE, getServletContext());

		try {

			/* ***********************************
			 * Parse out the request parameters
			 * ***********************************/

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

			String completeUrl;
			Map<String, String[]> params = null;
			if (StringUtils.isNotBlank(theRequest.getQueryString())) {
				completeUrl = requestUrl + "?" + theRequest.getQueryString();
				/*
				 * By default, we manually parse the request params (the URL params, or the body for
				 * POST form queries) since Java containers can't be trusted to use UTF-8 encoding
				 * when parsing. Specifically Tomcat 7 and Glassfish 4.0 use 8859-1 for some dumb
				 * reason.... grr.....
				 */
				if (isIgnoreServerParsedRequestParameters()) {
					String contentType = theRequest.getHeader(Constants.HEADER_CONTENT_TYPE);
					if (theRequestType == RequestTypeEnum.POST && isNotBlank(contentType) && contentType.startsWith(Constants.CT_X_FORM_URLENCODED)) {
						String requestBody = new String(requestDetails.loadRequestContents(), Constants.CHARSET_UTF8);
						params = UrlUtil.parseQueryStrings(theRequest.getQueryString(), requestBody);
					} else if (theRequestType == RequestTypeEnum.GET) {
						params = UrlUtil.parseQueryString(theRequest.getQueryString());
					}
				}
			} else {
				completeUrl = requestUrl.toString();
			}

			if (params == null) {
				params = new HashMap<>(theRequest.getParameterMap());
			}

			requestDetails.setParameters(params);

			/* *************************
			 * Notify interceptors about the incoming request
			 * *************************/

			for (IServerInterceptor next : myInterceptors) {
				boolean continueProcessing = next.incomingRequestPreProcessed(theRequest, theResponse);
				if (!continueProcessing) {
					ourLog.debug("Interceptor {} returned false, not continuing processing");
					return;
				}
			}


			String requestPath = getRequestPath(requestFullPath, servletContextPath, servletPath);

			if (requestPath.length() > 0 && requestPath.charAt(0) == '/') {
				requestPath = requestPath.substring(1);
			}

			IIdType id;
			populateRequestDetailsFromRequestPath(requestDetails, requestPath);

			fhirServerBase = getServerBaseForRequest(requestDetails);

			if (theRequestType == RequestTypeEnum.PUT) {
				String contentLocation = theRequest.getHeader(Constants.HEADER_CONTENT_LOCATION);
				if (contentLocation != null) {
					id = myFhirContext.getVersion().newIdType();
					id.setValue(contentLocation);
					requestDetails.setId(id);
				}
			}

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

			// String pagingAction = theRequest.getParameter(Constants.PARAM_PAGINGACTION);
			// if (getPagingProvider() != null && isNotBlank(pagingAction)) {
			// requestDetails.setRestOperationType(RestOperationTypeEnum.GET_PAGE);
			// if (theRequestType != RequestTypeEnum.GET) {
			// /*
			// * We reconstruct the link-self URL using the request parameters, and this would break if the parameters came
			// in using a POST. We could probably work around that but why bother unless
			// * someone comes up with a reason for needing it.
			// */
			// throw new InvalidRequestException(getFhirContext().getLocalizer().getMessage(RestfulServer.class,
			// "getPagesNonHttpGet"));
			// }
			// handlePagingRequest(requestDetails, theResponse, pagingAction);
			// return;
			// }

			BaseMethodBinding<?> resourceMethod = determineResourceMethod(requestDetails, requestPath);

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
			 * Actualy invoke the server method. This call is to a HAPI method binding, which
			 * is an object that wraps a specific implementing (user-supplied) method, but
			 * handles its input and provides its output back to the client.
			 *
			 * This is basically the end of processing for a successful request, since the
			 * method binding replies to the client and closes the response.
			 */
			Closeable outputStreamOrWriter = (Closeable) resourceMethod.invokeServer(this, requestDetails);

			for (int i = getInterceptors().size() - 1; i >= 0; i--) {
				IServerInterceptor next = getInterceptors().get(i);
				try {
					next.processingCompletedNormally(requestDetails);
				} catch (Throwable t) {
					ourLog.error("Failure in interceptor method", t);
				}
			}

			IOUtils.closeQuietly(outputStreamOrWriter);

		} catch (NotModifiedException | AuthenticationException e) {

			for (int i = getInterceptors().size() - 1; i >= 0; i--) {
				IServerInterceptor next = getInterceptors().get(i);
				if (!next.handleException(requestDetails, e, theRequest, theResponse)) {
					ourLog.debug("Interceptor {} returned false, not continuing processing");
					return;
				}
			}
			writeExceptionToResponse(theResponse, e);

		} catch (Throwable e) {

			/*
			 * We have caught an exception during request processing. This might be because a handling method threw
			 * something they wanted to throw (e.g. UnprocessableEntityException because the request
			 * had business requirement problems) or it could be due to bugs (e.g. NullPointerException).
			 *
			 * First we let the interceptors have a crack at converting the exception into something HAPI can use
			 * (BaseServerResponseException)
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
			 * If none of the interceptors converted the exception, default behaviour is to keep the exception as-is if it
			 * extends BaseServerResponseException, otherwise wrap it in an
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
			requestDetails.removeParameter(Constants.PARAM_SUMMARY);
			requestDetails.removeParameter(Constants.PARAM_ELEMENTS);

			/*
			 * If nobody handles it, default behaviour is to stream back the OperationOutcome to the client.
			 */
			DEFAULT_EXCEPTION_HANDLER.handleException(requestDetails, exception, theRequest, theResponse);

		}
	}

	/**
	 * Initializes the server. Note that this method is final to avoid accidentally introducing bugs in implementations,
	 * but subclasses may put initialization code in {@link #initialize()}, which is
	 * called immediately before beginning initialization of the restful server's internal init.
	 */
	@Override
	public final void init() throws ServletException {
		myProviderRegistrationMutex.lock();
		try {
			initialize();

			Object confProvider;
			try {
				ourLog.info("Initializing HAPI FHIR restful server running in " + getFhirContext().getVersion().getVersion().name() + " mode");

				ProvidedResourceScanner providedResourceScanner = new ProvidedResourceScanner(getFhirContext());
				providedResourceScanner.scanForProvidedResources(this);

				Collection<IResourceProvider> resourceProvider = getResourceProviders();
				// 'true' tells registerProviders() that
				// this call is part of initialization
				registerProviders(resourceProvider, true);

				Collection<Object> providers = getPlainProviders();
				// 'true' tells registerProviders() that
				// this call is part of initialization
				registerProviders(providers, true);

				findResourceMethods(getServerProfilesProvider());

				confProvider = getServerConformanceProvider();
				if (confProvider == null) {
					IFhirVersionServer versionServer = (IFhirVersionServer) getFhirContext().getVersion().getServerVersion();
					confProvider = versionServer.createServerConformanceProvider(this);
				}
				// findSystemMethods(confProvider);
				findResourceMethods(confProvider);

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

				/*
				 * This is a bit odd, but we have a placeholder @GetPage method for now
				 * that gets the server to bind for the paging request. At some point
				 * it would be nice to set things up so that client code could provide
				 * an alternate implementation, but this isn't currently possible..
				 */
				findResourceMethods(new PageProvider());

			} catch (Exception ex) {
				ourLog.error("An error occurred while loading request handlers!", ex);
				throw new ServletException("Failed to initialize FHIR Restful server", ex);
			}

			myStarted = true;
			ourLog.info("A FHIR has been lit on this server");
		} finally {
			myProviderRegistrationMutex.unlock();
		}
	}

	/**
	 * This method may be overridden by subclasses to do perform initialization that needs to be performed prior to the
	 * server being used.
	 *
	 * @throws ServletException
	 *            If the initialization failed. Note that you should consider throwing {@link UnavailableException}
	 *            (which extends {@link ServletException}), as this is a flag to the servlet container
	 *            that the servlet is not usable.
	 */
	protected void initialize() throws ServletException {
		// nothing by default
	}

	private void invokeDestroy(Object theProvider) {
		invokeDestroy(theProvider, theProvider.getClass());
	}

	private void invokeDestroy(Object theProvider, Class<?> clazz) {
		for (Method m : ReflectionUtil.getDeclaredMethods(clazz)) {
			Destroy destroy = m.getAnnotation(Destroy.class);
			if (destroy != null) {
				invokeInitializeOrDestroyMethod(theProvider, m, "destroy");
			}
		}

		Class<?> supertype = clazz.getSuperclass();
		if (!Object.class.equals(supertype)) {
			invokeDestroy(theProvider, supertype);
		}
	}

	private void invokeInitializeOrDestroyMethod(Object theProvider, Method m, String theMethodDescription) {

		Class<?>[] paramTypes = m.getParameterTypes();
		Object[] params = new Object[paramTypes.length];

		int index = 0;
		for (Class<?> nextParamType : paramTypes) {

			if (RestfulServer.class.equals(nextParamType) || IRestfulServerDefaults.class.equals(nextParamType)) {
				params[index] = this;
			}

			index++;
		}

		try {
			m.invoke(theProvider, params);
		} catch (Exception e) {
			ourLog.error("Exception occurred in " + theMethodDescription + " method '" + m.getName() + "'", e);
		}
	}

	private void invokeInitialize(Object theProvider) {
		invokeInitialize(theProvider, theProvider.getClass());
	}

	private void invokeInitialize(Object theProvider, Class<?> clazz) {
		for (Method m : ReflectionUtil.getDeclaredMethods(clazz)) {
			Initialize initialize = m.getAnnotation(Initialize.class);
			if (initialize != null) {
				invokeInitializeOrDestroyMethod(theProvider, m, "initialize");
			}
		}

		Class<?> supertype = clazz.getSuperclass();
		if (!Object.class.equals(supertype)) {
			invokeInitialize(theProvider, supertype);
		}
	}

	/**
	 * Should the server "pretty print" responses by default (requesting clients can always override this default by
	 * supplying an <code>Accept</code> header in the request, or a <code>_pretty</code>
	 * parameter in the request URL.
	 * <p>
	 * The default is <code>false</code>
	 * </p>
	 *
	 * @return Returns the default pretty print setting
	 */
	@Override
	public boolean isDefaultPrettyPrint() {
		return myDefaultPrettyPrint;
	}

	/**
	 * Should the server "pretty print" responses by default (requesting clients can always override this default by
	 * supplying an <code>Accept</code> header in the request, or a <code>_pretty</code>
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
	 * If set to <code>true</code> (the default is <code>true</code>) this server will not
	 * use the parsed request parameters (URL parameters and HTTP POST form contents) but
	 * will instead parse these values manually from the request URL and request body.
	 * <p>
	 * This is useful because many servlet containers (e.g. Tomcat, Glassfish) will use
	 * ISO-8859-1 encoding to parse escaped URL characters instead of using UTF-8
	 * as is specified by FHIR.
	 * </p>
	 */
	public boolean isIgnoreServerParsedRequestParameters() {
		return myIgnoreServerParsedRequestParameters;
	}

	/**
	 * If set to <code>true</code> (the default is <code>true</code>) this server will not
	 * use the parsed request parameters (URL parameters and HTTP POST form contents) but
	 * will instead parse these values manually from the request URL and request body.
	 * <p>
	 * This is useful because many servlet containers (e.g. Tomcat, Glassfish) will use
	 * ISO-8859-1 encoding to parse escaped URL characters instead of using UTF-8
	 * as is specified by FHIR.
	 * </p>
	 */
	public void setIgnoreServerParsedRequestParameters(boolean theIgnoreServerParsedRequestParameters) {
		myIgnoreServerParsedRequestParameters = theIgnoreServerParsedRequestParameters;
	}

	/**
	 * Should the server attempt to decompress incoming request contents (default is <code>true</code>). Typically this
	 * should be set to <code>true</code> unless the server has other configuration to
	 * deal with decompressing request bodies (e.g. a filter applied to the whole server).
	 */
	public boolean isUncompressIncomingContents() {
		return myUncompressIncomingContents;
	}

	/**
	 * Should the server attempt to decompress incoming request contents (default is <code>true</code>). Typically this
	 * should be set to <code>true</code> unless the server has other configuration to
	 * deal with decompressing request bodies (e.g. a filter applied to the whole server).
	 */
	public void setUncompressIncomingContents(boolean theUncompressIncomingContents) {
		myUncompressIncomingContents = theUncompressIncomingContents;
	}

	/**
	 * @deprecated This feature did not work well, and will be removed. Use {@link ResponseHighlighterInterceptor}
	 *             instead as an interceptor on your server and it will provide more useful syntax
	 *             highlighting. Deprocated in 1.4
	 */
	@Deprecated
	@Override
	public boolean isUseBrowserFriendlyContentTypes() {
		return myUseBrowserFriendlyContentTypes;
	}

	/**
	 * @deprecated This feature did not work well, and will be removed. Use {@link ResponseHighlighterInterceptor}
	 *             instead as an interceptor on your server and it will provide more useful syntax
	 *             highlighting. Deprocated in 1.4
	 */
	@Deprecated
	public void setUseBrowserFriendlyContentTypes(boolean theUseBrowserFriendlyContentTypes) {
		myUseBrowserFriendlyContentTypes = theUseBrowserFriendlyContentTypes;
	}

	public void populateRequestDetailsFromRequestPath(RequestDetails theRequestDetails, String theRequestPath) {
		UrlPathTokenizer tok = new UrlPathTokenizer(theRequestPath);
		String resourceName = null;

		if (myTenantIdentificationStrategy != null) {
			myTenantIdentificationStrategy.extractTenant(tok, theRequestDetails);
		}

		IIdType id = null;
		String operation = null;
		String compartment = null;
		if (tok.hasMoreTokens()) {
			resourceName = tok.nextToken();
			if (partIsOperation(resourceName)) {
				operation = resourceName;
				resourceName = null;
			}
		}
		theRequestDetails.setResourceName(resourceName);

		if (tok.hasMoreTokens()) {
			String nextString = tok.nextToken();
			if (partIsOperation(nextString)) {
				operation = nextString;
			} else {
				id = myFhirContext.getVersion().newIdType();
				id.setParts(null, resourceName, UrlUtil.unescape(nextString), null);
			}
		}

		if (tok.hasMoreTokens()) {
			String nextString = tok.nextToken();
			if (nextString.equals(Constants.PARAM_HISTORY)) {
				if (tok.hasMoreTokens()) {
					String versionString = tok.nextToken();
					if (id == null) {
						throw new InvalidRequestException("Don't know how to handle request path: " + theRequestPath);
					}
					id.setParts(null, resourceName, id.getIdPart(), UrlUtil.unescape(versionString));
				} else {
					operation = Constants.PARAM_HISTORY;
				}
			} else if (partIsOperation(nextString)) {
				if (operation != null) {
					throw new InvalidRequestException("URL Path contains two operations: " + theRequestPath);
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
				throw new InvalidRequestException("URL path has unexpected token '" + nextString + "' at the end: " + theRequestPath);
			}
		}

		theRequestDetails.setId(id);
		theRequestDetails.setOperation(operation);
		theRequestDetails.setSecondaryOperation(secondaryOperation);
		theRequestDetails.setCompartmentName(compartment);
	}

	public void registerInterceptor(IServerInterceptor theInterceptor) {
		Validate.notNull(theInterceptor, "Interceptor can not be null");
		myInterceptors.add(theInterceptor);
	}

	/**
	 * Register a single provider. This could be a Resource Provider or a "plain" provider not associated with any
	 * resource.
	 */
	public void registerProvider(Object provider) {
		if (provider != null) {
			Collection<Object> providerList = new ArrayList<>(1);
			providerList.add(provider);
			registerProviders(providerList);
		}
	}

	/**
	 * Register a group of providers. These could be Resource Providers, "plain" providers or a mixture of the two.
	 *
	 * @param providers
	 *           a {@code Collection} of providers. The parameter could be null or an empty {@code Collection}
	 */
	public void registerProviders(Collection<? extends Object> providers) {
		myProviderRegistrationMutex.lock();
		try {
			if (!myStarted) {
				for (Object provider : providers) {
					ourLog.info("Registration of provider [" + provider.getClass().getName() + "] will be delayed until FHIR server startup");
					if (provider instanceof IResourceProvider) {
						myResourceProviders.add((IResourceProvider) provider);
					} else {
						myPlainProviders.add(provider);
					}
				}
				return;
			}
		} finally {
			myProviderRegistrationMutex.unlock();
		}
		registerProviders(providers, false);
	}

	/*
	 * Inner method to actually register providers
	 */
	protected void registerProviders(Collection<? extends Object> providers, boolean inInit) {
		List<IResourceProvider> newResourceProviders = new ArrayList<IResourceProvider>();
		List<Object> newPlainProviders = new ArrayList<Object>();
		ProvidedResourceScanner providedResourceScanner = new ProvidedResourceScanner(getFhirContext());

		if (providers != null) {
			for (Object provider : providers) {
				if (provider instanceof IResourceProvider) {
					IResourceProvider rsrcProvider = (IResourceProvider) provider;
					Class<? extends IBaseResource> resourceType = rsrcProvider.getResourceType();
					if (resourceType == null) {
						throw new NullPointerException("getResourceType() on class '" + rsrcProvider.getClass().getCanonicalName() + "' returned null");
					}
					String resourceName = getFhirContext().getResourceDefinition(resourceType).getName();
					if (myTypeToProvider.containsKey(resourceName)) {
						throw new ConfigurationException("Multiple resource providers return resource type[" + resourceName + "]: First[" + myTypeToProvider.get(resourceName).getClass().getCanonicalName()
								+ "] and Second[" + rsrcProvider.getClass().getCanonicalName() + "]");
					}
					if (!inInit) {
						myResourceProviders.add(rsrcProvider);
					}
					myTypeToProvider.put(resourceName, rsrcProvider);
					providedResourceScanner.scanForProvidedResources(rsrcProvider);
					newResourceProviders.add(rsrcProvider);
				} else {
					if (!inInit) {
						myPlainProviders.add(provider);
					}
					newPlainProviders.add(provider);
				}

			}
			if (!newResourceProviders.isEmpty()) {
				ourLog.info("Added {} resource provider(s). Total {}", newResourceProviders.size(), myTypeToProvider.size());
				for (IResourceProvider provider : newResourceProviders) {
					assertProviderIsValid(provider);
					findResourceMethods(provider);
				}
			}
			if (!newPlainProviders.isEmpty()) {
				ourLog.info("Added {} plain provider(s). Total {}", newPlainProviders.size(), myPlainProviders.size());
				for (Object provider : newPlainProviders) {
					assertProviderIsValid(provider);
					findResourceMethods(provider);
				}
			}
			if (!inInit) {
				ourLog.trace("Invoking provider initialize methods");
				if (!newResourceProviders.isEmpty()) {
					for (IResourceProvider provider : newResourceProviders) {
						invokeInitialize(provider);
					}
				}
				if (!newPlainProviders.isEmpty()) {
					for (Object provider : newPlainProviders) {
						invokeInitialize(provider);
					}
				}
			}
		}
	}

	/*
	 * Remove registered RESTful methods for a Provider (and all superclasses) when it is being unregistered
	 */
	private void removeResourceMethods(Object theProvider) throws Exception {
		ourLog.info("Removing RESTful methods for: {}", theProvider.getClass());
		Class<?> clazz = theProvider.getClass();
		Class<?> supertype = clazz.getSuperclass();
		Collection<String> resourceNames = new ArrayList<String>();
		while (!Object.class.equals(supertype)) {
			removeResourceMethods(theProvider, supertype, resourceNames);
			supertype = supertype.getSuperclass();
		}
		removeResourceMethods(theProvider, clazz, resourceNames);
		for (String resourceName : resourceNames) {
			myResourceNameToBinding.remove(resourceName);
		}
	}

	/*
	 * Collect the set of RESTful methods for a single class when it is being unregistered
	 */
	private void removeResourceMethods(Object theProvider, Class<?> clazz, Collection<String> resourceNames) throws ConfigurationException {
		for (Method m : ReflectionUtil.getDeclaredMethods(clazz)) {
			BaseMethodBinding<?> foundMethodBinding = BaseMethodBinding.bindMethod(m, getFhirContext(), theProvider);
			if (foundMethodBinding == null) {
				continue; // not a bound method
			}
			if (foundMethodBinding instanceof ConformanceMethodBinding) {
				myServerConformanceMethod = null;
				continue;
			}
			String resourceName = foundMethodBinding.getResourceName();
			if (!resourceNames.contains(resourceName)) {
				resourceNames.add(resourceName);
			}
		}
	}

	public Object returnResponse(ServletRequestDetails theRequest, ParseAction<?> outcome, int operationStatus, boolean allowPrefer, MethodOutcome response, String resourceName) throws IOException {
		HttpServletResponse servletResponse = theRequest.getServletResponse();
		servletResponse.setStatus(operationStatus);
		servletResponse.setCharacterEncoding(Constants.CHARSET_NAME_UTF8);
		addHeadersToResponse(servletResponse);
		if (allowPrefer) {
			addContentLocationHeaders(theRequest, servletResponse, response, resourceName);
		}
		Writer writer;
		if (outcome != null) {
			ResponseEncoding encoding = RestfulServerUtils.determineResponseEncodingWithDefault(theRequest);
			servletResponse.setContentType(encoding.getResourceContentType());
			writer = servletResponse.getWriter();
			IParser parser = encoding.getEncoding().newParser(getFhirContext());
			parser.setPrettyPrint(RestfulServerUtils.prettyPrintResponse(this, theRequest));
			outcome.execute(parser, writer);
		} else {
			servletResponse.setContentType(Constants.CT_TEXT_WITH_UTF8);
			writer = servletResponse.getWriter();
		}
		return writer;
	}

	@Override
	protected void service(HttpServletRequest theReq, HttpServletResponse theResp) throws ServletException, IOException {
		theReq.setAttribute(REQUEST_START_TIME, new Date());

		RequestTypeEnum method;
		try {
			method = RequestTypeEnum.valueOf(theReq.getMethod());
		} catch (IllegalArgumentException e) {
			super.service(theReq, theResp);
			return;
		}

		switch (method) {
		case DELETE:
			doDelete(theReq, theResp);
			break;
		case GET:
			doGet(theReq, theResp);
			break;
		case OPTIONS:
			doOptions(theReq, theResp);
			break;
		case POST:
			doPost(theReq, theResp);
			break;
		case PUT:
			doPut(theReq, theResp);
			break;
		default:
			handleRequest(method, theReq, theResp);
			break;
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
	 * Sets the non-resource specific providers which implement method calls on this server
	 *
	 * @see #setResourceProviders(Collection)
	 */
	public void setProviders(Object... theProviders) {
		myPlainProviders.clear();
		if (theProviders != null) {
			myPlainProviders.addAll(Arrays.asList(theProviders));
		}
	}

	public void unregisterInterceptor(IServerInterceptor theInterceptor) {
		Validate.notNull(theInterceptor, "Interceptor can not be null");
		myInterceptors.remove(theInterceptor);
	}

	/**
	 * Unregister one provider (either a Resource provider or a plain provider)
	 *
	 * @param provider
	 * @throws Exception
	 */
	public void unregisterProvider(Object provider) throws Exception {
		if (provider != null) {
			Collection<Object> providerList = new ArrayList<Object>(1);
			providerList.add(provider);
			unregisterProviders(providerList);
		}
	}

	/**
	 * Unregister a {@code Collection} of providers
	 *
	 * @param providers
	 * @throws Exception
	 */
	public void unregisterProviders(Collection<? extends Object> providers) throws Exception {
		ProvidedResourceScanner providedResourceScanner = new ProvidedResourceScanner(getFhirContext());
		if (providers != null) {
			for (Object provider : providers) {
				removeResourceMethods(provider);
				if (provider instanceof IResourceProvider) {
					myResourceProviders.remove(provider);
					IResourceProvider rsrcProvider = (IResourceProvider) provider;
					Class<? extends IBaseResource> resourceType = rsrcProvider.getResourceType();
					String resourceName = getFhirContext().getResourceDefinition(resourceType).getName();
					myTypeToProvider.remove(resourceName);
					providedResourceScanner.removeProvidedResources(rsrcProvider);
				} else {
					myPlainProviders.remove(provider);
				}
				invokeDestroy(provider);
			}
		}
	}

	private void writeExceptionToResponse(HttpServletResponse theResponse, BaseServerResponseException theException) throws IOException {
		theResponse.setStatus(theException.getStatusCode());
		addHeadersToResponse(theResponse);
		if (theException.hasResponseHeaders()) {
			for (Entry<String, List<String>> nextEntry : theException.getResponseHeaders().entrySet()) {
				for (String nextValue : nextEntry.getValue()) {
					if (isNotBlank(nextValue)) {
						theResponse.addHeader(nextEntry.getKey(), nextValue);
					}
				}
			}
		}
		theResponse.setContentType("text/plain");
		theResponse.setCharacterEncoding("UTF-8");
		theResponse.getWriter().write(theException.getMessage());
	}

//	/**
//	 * Returns the read method binding for the given resource type, or
//	 * returns <code>null</code> if not
//	 * @param theResourceType The resource type, e.g. "Patient"
//	 * @return The read method binding, or null
//	 */
//	public ReadMethodBinding findReadMethodBinding(String theResourceType) {
//		ReadMethodBinding retVal = null;
//
//		ResourceBinding type = myResourceNameToBinding.get(theResourceType);
//		if (type != null) {
//			for (BaseMethodBinding<?> next : type.getMethodBindings()) {
//				if (next instanceof ReadMethodBinding) {
//					retVal = (ReadMethodBinding) next;
//				}
//			}
//		}
//
//		return retVal;
//	}
}
