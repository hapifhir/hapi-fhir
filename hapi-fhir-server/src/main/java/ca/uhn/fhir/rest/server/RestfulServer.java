package ca.uhn.fhir.rest.server;

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
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.api.AddProfileTagEnum;
import ca.uhn.fhir.context.api.BundleInclusionRule;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.executor.InterceptorService;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.annotation.Destroy;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Initialize;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.PreferReturnEnum;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.IFhirVersionServer;
import ca.uhn.fhir.rest.api.server.IRestfulServer;
import ca.uhn.fhir.rest.api.server.ParseAction;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.RestfulServerUtils.ResponseEncoding;
import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.NotModifiedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.interceptor.ExceptionHandlingInterceptor;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;
import ca.uhn.fhir.rest.server.method.BaseMethodBinding;
import ca.uhn.fhir.rest.server.method.ConformanceMethodBinding;
import ca.uhn.fhir.rest.server.method.MethodMatchEnum;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.tenant.ITenantIdentificationStrategy;
import ca.uhn.fhir.util.CoverageIgnore;
import ca.uhn.fhir.util.OperationOutcomeUtil;
import ca.uhn.fhir.util.ReflectionUtil;
import ca.uhn.fhir.util.UrlPathTokenizer;
import ca.uhn.fhir.util.UrlUtil;
import ca.uhn.fhir.util.VersionUtil;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseConformance;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.jar.Manifest;
import java.util.stream.Collectors;

import static ca.uhn.fhir.util.StringUtil.toUtf8String;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * This class is the central class for the HAPI FHIR Plain Server framework.
 * <p>
 * See <a href="https://hapifhir.io/hapi-fhir/docs/server_plain/">HAPI FHIR Plain Server</a>
 * for information on how to use this framework.
 */
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
	/**
	 * Default value for {@link #setDefaultPreferReturn(PreferReturnEnum)}
	 */
	public static final PreferReturnEnum DEFAULT_PREFER_RETURN = PreferReturnEnum.REPRESENTATION;
	private static final ExceptionHandlingInterceptor DEFAULT_EXCEPTION_HANDLER = new ExceptionHandlingInterceptor();
	private static final Logger ourLog = LoggerFactory.getLogger(RestfulServer.class);
	private static final long serialVersionUID = 1L;
	private final List<Object> myPlainProviders = new ArrayList<>();
	private final List<IResourceProvider> myResourceProviders = new ArrayList<>();
	private IInterceptorService myInterceptorService;
	private BundleInclusionRule myBundleInclusionRule = BundleInclusionRule.BASED_ON_INCLUDES;
	private boolean myDefaultPrettyPrint = false;
	private EncodingEnum myDefaultResponseEncoding = EncodingEnum.JSON;
	private ETagSupportEnum myETagSupport = DEFAULT_ETAG_SUPPORT;
	private FhirContext myFhirContext;
	private boolean myIgnoreServerParsedRequestParameters = true;
	private String myImplementationDescription;
	private String myCopyright;
	private IPagingProvider myPagingProvider;
	private Integer myDefaultPageSize;
	private Integer myMaximumPageSize;
	private boolean myStatelessPagingDefault = false;
	private Lock myProviderRegistrationMutex = new ReentrantLock();
	private Map<String, ResourceBinding> myResourceNameToBinding = new HashMap<>();
	private IServerAddressStrategy myServerAddressStrategy = new IncomingRequestAddressStrategy();
	private ResourceBinding myServerBinding = new ResourceBinding();
	private ResourceBinding myGlobalBinding = new ResourceBinding();
	private ConformanceMethodBinding myServerConformanceMethod;
	private Object myServerConformanceProvider;
	private String myServerName = "HAPI FHIR Server";
	/**
	 * This is configurable but by default we just use HAPI version
	 */
	private String myServerVersion = createPoweredByHeaderProductVersion();
	private boolean myStarted;
	private boolean myUncompressIncomingContents = true;
	private ITenantIdentificationStrategy myTenantIdentificationStrategy;
	private PreferReturnEnum myDefaultPreferReturn = DEFAULT_PREFER_RETURN;
	private ElementsSupportEnum myElementsSupport = ElementsSupportEnum.EXTENDED;

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
		this(theCtx, new InterceptorService("RestfulServer"));
	}

	public RestfulServer(FhirContext theCtx, IInterceptorService theInterceptorService) {
		myFhirContext = theCtx;
		setInterceptorService(theInterceptorService);
	}

	/**
	 * @since 5.5.0
	 */
	protected ConformanceMethodBinding getServerConformanceMethod() {
		return myServerConformanceMethod;
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
		String poweredByHeader = createPoweredByHeader();
		if (isNotBlank(poweredByHeader)) {
			theHttpResponse.addHeader(Constants.POWERED_BY_HEADER, poweredByHeader);
		}


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

	public RestfulServerConfiguration createConfiguration() {
		RestfulServerConfiguration result = new RestfulServerConfiguration();
		result.setResourceBindings(getResourceBindings());
		result.setServerBindings(getServerBindings());
		result.setGlobalBindings(getGlobalBindings());
		result.setImplementationDescription(getImplementationDescription());
		result.setServerVersion(getServerVersion());
		result.setServerName(getServerName());
		result.setFhirContext(getFhirContext());
		result.setServerAddressStrategy(myServerAddressStrategy);
		try (InputStream inputStream = getClass().getResourceAsStream("/META-INF/MANIFEST.MF")) {
			if (inputStream != null) {
				Manifest manifest = new Manifest(inputStream);
				String value = manifest.getMainAttributes().getValue("Build-Time");
				result.setConformanceDate(new InstantDt(value));
			}
		} catch (Exception e) {
			// fall through
		}
		result.computeSharedSupertypeForResourcePerName(getResourceProviders());
		return result;
	}

	private List<BaseMethodBinding<?>> getGlobalBindings() {
		return myGlobalBinding.getMethodBindings();
	}

	protected List<String> createPoweredByAttributes() {
		return Lists.newArrayList("FHIR Server", "FHIR " + myFhirContext.getVersion().getVersion().getFhirVersionString() + "/" + myFhirContext.getVersion().getVersion().name());
	}

	/**
	 * Subclasses may override to provide their own powered by
	 * header. Note that if you want to be nice and still credit HAPI
	 * FHIR you could consider overriding
	 * {@link #createPoweredByAttributes()} instead and adding your own
	 * fragments to the list.
	 */
	protected String createPoweredByHeader() {
		StringBuilder b = new StringBuilder();
		b.append(createPoweredByHeaderProductName());
		b.append(" ");
		b.append(createPoweredByHeaderProductVersion());
		b.append(" ");
		b.append(createPoweredByHeaderComponentName());
		b.append(" (");

		List<String> poweredByAttributes = createPoweredByAttributes();
		for (ListIterator<String> iter = poweredByAttributes.listIterator(); iter.hasNext(); ) {
			if (iter.nextIndex() > 0) {
				b.append("; ");
			}
			b.append(iter.next());
		}

		b.append(")");
		return b.toString();
	}

	/**
	 * Subclasses my override
	 *
	 * @see #createPoweredByHeader()
	 */
	protected String createPoweredByHeaderComponentName() {
		return "REST Server";
	}

	/**
	 * Subclasses my override
	 *
	 * @see #createPoweredByHeader()
	 */
	protected String createPoweredByHeaderProductName() {
		return "HAPI FHIR";
	}

	/**
	 * Subclasses my override
	 *
	 * @see #createPoweredByHeader()
	 */
	protected String createPoweredByHeaderProductVersion() {
		String version = VersionUtil.getVersion();
		if (VersionUtil.isSnapshot()) {
			version = version + "/" + VersionUtil.getBuildNumber() + "/" + VersionUtil.getBuildDate();
		}
		return version;
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

		if (myServerConformanceMethod != null) {
			myServerConformanceMethod.close();
		}
		myResourceNameToBinding
			.values()
			.stream()
			.flatMap(t -> t.getMethodBindings().stream())
			.forEach(t -> t.close());
		myGlobalBinding
			.getMethodBindings()
			.forEach(t -> t.close());
		myServerBinding
			.getMethodBindings()
			.forEach(t -> t.close());

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
		if (myServerConformanceMethod.incomingServerRequestMatchesMethod(requestDetails) != MethodMatchEnum.NONE) {
			resourceMethod = myServerConformanceMethod;
		} else if (resourceName == null) {
			resourceBinding = myServerBinding;
		} else {
			resourceBinding = myResourceNameToBinding.get(resourceName);
			if (resourceBinding == null) {
				throwUnknownResourceTypeException(resourceName);
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
				throw new InvalidRequestException(Msg.code(287) + myFhirContext.getLocalizer().getMessage(RestfulServer.class, "rootRequest"));
			}
			throwUnknownFhirOperationException(requestDetails, requestPath, requestType);
		}
		return resourceMethod;
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

	private void findResourceMethods(Object theProvider) {

		ourLog.debug("Scanning type for RESTful methods: {}", theProvider.getClass());
		int count = 0;

		Class<?> clazz = theProvider.getClass();
		Class<?> supertype = clazz.getSuperclass();
		while (!Object.class.equals(supertype)) {
			count += findResourceMethodsOnInterfaces(theProvider, supertype.getInterfaces());
			count += findResourceMethods(theProvider, supertype);
			supertype = supertype.getSuperclass();
		}

		try {
			count += findResourceMethodsOnInterfaces(theProvider, clazz.getInterfaces());
			count += findResourceMethods(theProvider, clazz);
		} catch (ConfigurationException e) {
			throw new ConfigurationException(Msg.code(288) + "Failure scanning class " + clazz.getSimpleName() + ": " + e.getMessage(), e);
		}
		if (count == 0) {
			throw new ConfigurationException(Msg.code(289) + "Did not find any annotated RESTful methods on provider class " + theProvider.getClass().getName());
		}
	}

	private int findResourceMethodsOnInterfaces(Object theProvider, Class<?>[] interfaces) {
		int count = 0;
		for (Class<?> anInterface : interfaces) {
			count += findResourceMethodsOnInterfaces(theProvider, anInterface.getInterfaces());
			count += findResourceMethods(theProvider, anInterface);
		}
		return count;
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
				myServerConformanceMethod = (ConformanceMethodBinding) foundMethodBinding;
				if (myServerConformanceProvider == null) {
					myServerConformanceProvider = theProvider;
				}
				continue;
			}

			if (!Modifier.isPublic(m.getModifiers())) {
				throw new ConfigurationException(Msg.code(290) + "Method '" + m.getName() + "' is not public, FHIR RESTful methods must be public");
			}
			if (Modifier.isStatic(m.getModifiers())) {
				throw new ConfigurationException(Msg.code(291) + "Method '" + m.getName() + "' is static, FHIR RESTful methods must not be static");
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
								throw new ConfigurationException(Msg.code(292) + "Method[" + m.toString() + "] is not allowed to have a parameter annotated with " + annotation);
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
	 * {@link FhirContext#setAddProfileTagWhenEncoding(AddProfileTagEnum)}
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
	 * @param theAddProfileTag The behaviour enum (must not be null)
	 * @deprecated As of HAPI FHIR 1.5, this property has been moved to
	 * {@link FhirContext#setAddProfileTagWhenEncoding(AddProfileTagEnum)}
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
	 * @param theBundleInclusionRule - inclusion rule (@see BundleInclusionRule for behaviors)
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
	 * @param theETagSupport The ETag support mode
	 */
	public void setETagSupport(ETagSupportEnum theETagSupport) {
		if (theETagSupport == null) {
			throw new NullPointerException(Msg.code(293) + "theETagSupport can not be null");
		}
		myETagSupport = theETagSupport;
	}

	@Override
	public ElementsSupportEnum getElementsSupport() {
		return myElementsSupport;
	}

	/**
	 * Sets the elements support mode.
	 *
	 * @see <a href="http://hapifhir.io/doc_rest_server.html#extended_elements_support">Extended Elements Support</a>
	 */
	public void setElementsSupport(ElementsSupportEnum theElementsSupport) {
		Validate.notNull(theElementsSupport, "theElementsSupport must not be null");
		myElementsSupport = theElementsSupport;
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
	 * Returns the server copyright (will be added to the CapabilityStatement). Note that FHIR allows Markdown in this string.
	 */
	public String getCopyright() {
		return myCopyright;
	}

	/**
	 * Sets the server copyright (will be added to the CapabilityStatement). Note that FHIR allows Markdown in this string.
	 */
	public void setCopyright(String theCopyright) {
		myCopyright = theCopyright;
	}

	/**
	 * Returns a list of all registered server interceptors
	 *
	 * @deprecated As of HAPI FHIR 3.8.0, use {@link #getInterceptorService()} to access the interceptor service. You can register and unregister interceptors using this service.
	 */
	@Deprecated
	@Override
	public List<IServerInterceptor> getInterceptors_() {
		List<IServerInterceptor> retVal = getInterceptorService()
			.getAllRegisteredInterceptors()
			.stream()
			.filter(t -> t instanceof IServerInterceptor)
			.map(t -> (IServerInterceptor) t)
			.collect(Collectors.toList());
		return Collections.unmodifiableList(retVal);
	}

	/**
	 * Returns the interceptor registry for this service. Use this registry to register and unregister
	 *
	 * @since 3.8.0
	 */
	@Override
	public IInterceptorService getInterceptorService() {
		return myInterceptorService;
	}

	/**
	 * Sets the interceptor registry for this service. Use this registry to register and unregister
	 *
	 * @since 3.8.0
	 */
	public void setInterceptorService(@Nonnull IInterceptorService theInterceptorService) {
		Validate.notNull(theInterceptorService, "theInterceptorService must not be null");
		myInterceptorService = theInterceptorService;
	}

	/**
	 * Sets (or clears) the list of interceptors
	 *
	 * @param theList The list of interceptors (may be null)
	 * @deprecated As of HAPI FHIR 3.8.0, use {@link #getInterceptorService()} to access the interceptor service. You can register and unregister interceptors using this service.
	 */
	@Deprecated
	public void setInterceptors(@Nonnull List<?> theList) {
		myInterceptorService.unregisterAllInterceptors();
		myInterceptorService.registerInterceptors(theList);
	}

	/**
	 * Sets (or clears) the list of interceptors
	 *
	 * @param theInterceptors The list of interceptors (may be null)
	 * @deprecated As of HAPI FHIR 3.8.0, use {@link #getInterceptorService()} to access the interceptor service. You can register and unregister interceptors using this service.
	 */
	@Deprecated
	public void setInterceptors(IServerInterceptor... theInterceptors) {
		Validate.noNullElements(theInterceptors, "theInterceptors must not contain any null elements");
		setInterceptors(Arrays.asList(theInterceptors));
	}

	@Override
	public IPagingProvider getPagingProvider() {
		return myPagingProvider;
	}

	/**
	 * Sets the paging provider to use, or <code>null</code> to use no paging (which is the default).
	 * This will set defaultPageSize and maximumPageSize from the paging provider.
	 */
	public void setPagingProvider(IPagingProvider thePagingProvider) {
		myPagingProvider = thePagingProvider;
		if (myPagingProvider != null) {
			setDefaultPageSize(myPagingProvider.getDefaultPageSize());
			setMaximumPageSize(myPagingProvider.getMaximumPageSize());
		}

	}

	@Override
	public Integer getDefaultPageSize() {
		return myDefaultPageSize;
	}

	/**
	 * Sets the default page size to use, or <code>null</code> if no default page size
	 */
	public void setDefaultPageSize(Integer thePageSize) {
		myDefaultPageSize = thePageSize;
	}

	@Override
	public Integer getMaximumPageSize() {
		return myMaximumPageSize;
	}

	/**
	 * Sets the maximum page size to use, or <code>null</code> if no maximum page size
	 */
	public void setMaximumPageSize(Integer theMaximumPageSize) {
		myMaximumPageSize = theMaximumPageSize;
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
	 * @deprecated This method causes inconsistent behaviour depending on the order it is called in. Use {@link #registerProviders(Object...)} instead.
	 */
	@Deprecated
	public void setPlainProviders(Object... theProv) {
		setPlainProviders(Arrays.asList(theProv));
	}

	/**
	 * Sets the non-resource specific providers which implement method calls on this server.
	 *
	 * @see #setResourceProviders(Collection)
	 * @deprecated This method causes inconsistent behaviour depending on the order it is called in. Use {@link #registerProviders(Object...)} instead.
	 */
	@Deprecated
	public void setPlainProviders(Collection<Object> theProviders) {
		Validate.noNullElements(theProviders, "theProviders must not contain any null elements");

		myPlainProviders.clear();
		myPlainProviders.addAll(theProviders);
	}

	/**
	 * Allows users of RestfulServer to override the getRequestPath method to let them build their custom request path
	 * implementation
	 *
	 * @param requestFullPath    the full request path
	 * @param servletContextPath the servelet context path
	 * @param servletPath        the servelet path
	 * @return created resource path
	 */
	// NOTE: Don't make this a static method!! People want to override it
	protected String getRequestPath(String requestFullPath, String servletContextPath, String servletPath) {
		return requestFullPath.substring(escapedLength(servletContextPath) + escapedLength(servletPath));
	}

	public Collection<ResourceBinding> getResourceBindings() {
		return myResourceNameToBinding.values();
	}

	public Collection<BaseMethodBinding<?>> getProviderMethodBindings(Object theProvider) {
		Set<BaseMethodBinding<?>> retVal = new HashSet<>();
		for (ResourceBinding resourceBinding : getResourceBindings()) {
			for (BaseMethodBinding<?> methodBinding : resourceBinding.getMethodBindings()) {
				if (theProvider.equals(methodBinding.getProvider())) {
					retVal.add(methodBinding);
				}
			}
		}

		return retVal;
	}

	/**
	 * Provides the resource providers for this server
	 */
	public List<IResourceProvider> getResourceProviders() {
		return Collections.unmodifiableList(myResourceProviders);
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
	 * Sets the resource providers for this server
	 */
	public void setResourceProviders(Collection<IResourceProvider> theProviders) {
		Validate.noNullElements(theProviders, "theProviders must not contain any null elements");

		myResourceProviders.clear();
		myResourceProviders.addAll(theProviders);
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
	 */
	public String getServerBaseForRequest(ServletRequestDetails theRequest) {
		String fhirServerBase;
		fhirServerBase = myServerAddressStrategy.determineServerBase(getServletContext(), theRequest.getServletRequest());
		assert isNotBlank(fhirServerBase) : "Server Address Strategy did not return a value";

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
	 * @throws IllegalStateException Note that this method can only be called prior to {@link #init() initialization} and will throw an
	 *                               {@link IllegalStateException} if called after that.
	 */
	public void setServerConformanceProvider(Object theServerConformanceProvider) {
		if (myStarted) {
			throw new IllegalStateException(Msg.code(294) + "Server is already started");
		}

		// call the setRestfulServer() method to point the Conformance
		// Provider to this server instance. This is done to avoid
		// passing the server into the constructor. Having that sort
		// of cross linkage causes reference cycles in Spring wiring
		try {
			Method setRestfulServer = theServerConformanceProvider.getClass().getMethod("setRestfulServer", RestfulServer.class);
			if (setRestfulServer != null) {
				setRestfulServer.invoke(theServerConformanceProvider, this);
			}
		} catch (Exception e) {
			ourLog.warn("Error calling IServerConformanceProvider.setRestfulServer", e);
		}
		myServerConformanceProvider = theServerConformanceProvider;
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
		ServletRequestDetails requestDetails = newRequestDetails(theRequestType, theRequest, theResponse);

		String requestId = getOrCreateRequestId(theRequest);
		requestDetails.setRequestId(requestId);
		addRequestIdToResponse(requestDetails, requestId);

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
			if (isNotBlank(theRequest.getQueryString())) {
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
						String requestBody = toUtf8String(requestDetails.loadRequestContents());
						params = UrlUtil.parseQueryStrings(theRequest.getQueryString(), requestBody);
					} else if (theRequestType == RequestTypeEnum.GET) {
						params = UrlUtil.parseQueryString(theRequest.getQueryString());
					}
				}
			} else {
				completeUrl = requestUrl.toString();
			}

			if (params == null) {

				// If the request is coming in with a content-encoding, don't try to
				// load the params from the content.
				if (isNotBlank(theRequest.getHeader(Constants.HEADER_CONTENT_ENCODING))) {
					if (isNotBlank(theRequest.getQueryString())) {
						params = UrlUtil.parseQueryString(theRequest.getQueryString());
					} else {
						params = Collections.emptyMap();
					}
				}

				if (params == null) {
					params = new HashMap<>(theRequest.getParameterMap());
				}
			}

			requestDetails.setParameters(params);

			/* *************************
			 * Notify interceptors about the incoming request
			 * *************************/

			// Interceptor: SERVER_INCOMING_REQUEST_PRE_PROCESSED
			if (myInterceptorService.hasHooks(Pointcut.SERVER_INCOMING_REQUEST_PRE_PROCESSED)) {
				HookParams preProcessedParams = new HookParams();
				preProcessedParams.add(HttpServletRequest.class, theRequest);
				preProcessedParams.add(HttpServletResponse.class, theResponse);
				if (!myInterceptorService.callHooks(Pointcut.SERVER_INCOMING_REQUEST_PRE_PROCESSED, preProcessedParams)) {
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
						break;
					}
				}
			}
			requestDetails.setRespondGzip(respondGzip);
			requestDetails.setRequestPath(requestPath);
			requestDetails.setFhirServerBase(fhirServerBase);
			requestDetails.setCompleteUrl(completeUrl);

			// Interceptor: SERVER_INCOMING_REQUEST_PRE_HANDLER_SELECTED
			if (myInterceptorService.hasHooks(Pointcut.SERVER_INCOMING_REQUEST_PRE_HANDLER_SELECTED)) {
				HookParams preProcessedParams = new HookParams();
				preProcessedParams.add(HttpServletRequest.class, theRequest);
				preProcessedParams.add(HttpServletResponse.class, theResponse);
				preProcessedParams.add(RequestDetails.class, requestDetails);
				preProcessedParams.add(ServletRequestDetails.class, requestDetails);
				if (!myInterceptorService.callHooks(Pointcut.SERVER_INCOMING_REQUEST_PRE_HANDLER_SELECTED, preProcessedParams)) {
					return;
				}
			}

			validateRequest(requestDetails);

			BaseMethodBinding<?> resourceMethod = determineResourceMethod(requestDetails, requestPath);

			RestOperationTypeEnum operation = resourceMethod.getRestOperationType(requestDetails);
			requestDetails.setRestOperationType(operation);

			// Interceptor: SERVER_INCOMING_REQUEST_POST_PROCESSED
			if (myInterceptorService.hasHooks(Pointcut.SERVER_INCOMING_REQUEST_POST_PROCESSED)) {
				HookParams postProcessedParams = new HookParams();
				postProcessedParams.add(RequestDetails.class, requestDetails);
				postProcessedParams.add(ServletRequestDetails.class, requestDetails);
				postProcessedParams.add(HttpServletRequest.class, theRequest);
				postProcessedParams.add(HttpServletResponse.class, theResponse);
				if (!myInterceptorService.callHooks(Pointcut.SERVER_INCOMING_REQUEST_POST_PROCESSED, postProcessedParams)) {
					return;
				}
			}

			/*
			 * Actually invoke the server method. This call is to a HAPI method binding, which
			 * is an object that wraps a specific implementing (user-supplied) method, but
			 * handles its input and provides its output back to the client.
			 *
			 * This is basically the end of processing for a successful request, since the
			 * method binding replies to the client and closes the response.
			 */
			try (Closeable outputStreamOrWriter = (Closeable) resourceMethod.invokeServer(this, requestDetails)) {

				// Invoke interceptors
				HookParams hookParams = new HookParams();
				hookParams.add(RequestDetails.class, requestDetails);
				hookParams.add(ServletRequestDetails.class, requestDetails);
				myInterceptorService.callHooks(Pointcut.SERVER_PROCESSING_COMPLETED_NORMALLY, hookParams);

				ourLog.trace("Done writing to stream: {}", outputStreamOrWriter);
			}

		} catch (NotModifiedException | AuthenticationException e) {

			HookParams handleExceptionParams = new HookParams();
			handleExceptionParams.add(RequestDetails.class, requestDetails);
			handleExceptionParams.add(ServletRequestDetails.class, requestDetails);
			handleExceptionParams.add(HttpServletRequest.class, theRequest);
			handleExceptionParams.add(HttpServletResponse.class, theResponse);
			handleExceptionParams.add(BaseServerResponseException.class, e);
			if (!myInterceptorService.callHooks(Pointcut.SERVER_HANDLE_EXCEPTION, handleExceptionParams)) {
				return;
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
			HookParams preProcessParams = new HookParams();
			preProcessParams.add(RequestDetails.class, requestDetails);
			preProcessParams.add(ServletRequestDetails.class, requestDetails);
			preProcessParams.add(HttpServletRequest.class, theRequest);
			preProcessParams.add(HttpServletResponse.class, theResponse);
			preProcessParams.add(Throwable.class, e);
			BaseServerResponseException exception = (BaseServerResponseException) myInterceptorService.callHooksAndReturnObject(Pointcut.SERVER_PRE_PROCESS_OUTGOING_EXCEPTION, preProcessParams);

			/*
			 * If none of the interceptors converted the exception, default behaviour is to keep the exception as-is if it
			 * extends BaseServerResponseException, otherwise wrap it in an
			 * InternalErrorException.
			 */
			if (exception == null) {
				exception = DEFAULT_EXCEPTION_HANDLER.preProcessOutgoingException(requestDetails, e, theRequest);
			}

			/*
			 * If it's a 410 Gone, we want to include a location header in the response
			 * if we can, since that can include the resource version which is nice
			 * for the user.
			 */
			if (exception instanceof ResourceGoneException) {
				IIdType resourceId = ((ResourceGoneException) exception).getResourceId();
				if (resourceId != null && resourceId.hasResourceType() && resourceId.hasIdPart()) {
					String baseUrl = myServerAddressStrategy.determineServerBase(theRequest.getServletContext(), theRequest);
					resourceId = resourceId.withServerBase(baseUrl, resourceId.getResourceType());
					requestDetails.getResponse().addHeader(Constants.HEADER_LOCATION, resourceId.getValue());
				}
			}

			/*
			 * Next, interceptors get a shot at handling the exception
			 */
			HookParams handleExceptionParams = new HookParams();
			handleExceptionParams.add(RequestDetails.class, requestDetails);
			handleExceptionParams.add(ServletRequestDetails.class, requestDetails);
			handleExceptionParams.add(HttpServletRequest.class, theRequest);
			handleExceptionParams.add(HttpServletResponse.class, theResponse);
			handleExceptionParams.add(BaseServerResponseException.class, exception);
			if (!myInterceptorService.callHooks(Pointcut.SERVER_HANDLE_EXCEPTION, handleExceptionParams)) {
				return;
			}

			/*
			 * If we're handling an exception, no summary mode should be applied
			 */
			requestDetails.removeParameter(Constants.PARAM_SUMMARY);
			requestDetails.removeParameter(Constants.PARAM_ELEMENTS);
			requestDetails.removeParameter(Constants.PARAM_ELEMENTS + Constants.PARAM_ELEMENTS_EXCLUDE_MODIFIER);

			/*
			 * If nobody handles it, default behaviour is to stream back the OperationOutcome to the client.
			 */
			DEFAULT_EXCEPTION_HANDLER.handleException(requestDetails, exception, theRequest, theResponse);

		} finally {

			HookParams params = new HookParams();
			params.add(RequestDetails.class, requestDetails);
			params.addIfMatchesType(ServletRequestDetails.class, requestDetails);
			myInterceptorService.callHooks(Pointcut.SERVER_PROCESSING_COMPLETED, params);

		}
	}

	/**
	 * Subclasses may override this to customize the way that the RequestDetails object is created. Generally speaking, the
	 * right way to do this is to override this method, but call the super-implementation (<code>super.newRequestDetails</code>)
	 * and then customize the returned object before returning it.
	 *
	 * @param theRequestType The HTTP request verb
	 * @param theRequest     The servlet request
	 * @param theResponse    The servlet response
	 * @return A ServletRequestDetails instance to be passed to any resource providers, interceptors, etc. that are invoked as a part of serving this request.
	 */
	@Nonnull
	protected ServletRequestDetails newRequestDetails(RequestTypeEnum theRequestType, HttpServletRequest theRequest, HttpServletResponse theResponse) {
		ServletRequestDetails requestDetails = newRequestDetails();
		requestDetails.setServer(this);
		requestDetails.setRequestType(theRequestType);
		requestDetails.setServletRequest(theRequest);
		requestDetails.setServletResponse(theResponse);
		return requestDetails;
	}

	/**
	 * @deprecated Deprecated in HAPI FHIR 4.1.0 - Users wishing to override this method should override {@link #newRequestDetails(RequestTypeEnum, HttpServletRequest, HttpServletResponse)} instead
	 */
	@Deprecated
	protected ServletRequestDetails newRequestDetails() {
		return new ServletRequestDetails(getInterceptorService());
	}

	protected void addRequestIdToResponse(ServletRequestDetails theRequestDetails, String theRequestId) {
		theRequestDetails.getResponse().addHeader(Constants.HEADER_REQUEST_ID, theRequestId);
	}

	/**
	 * Reads a request ID from the request headers via the {@link Constants#HEADER_REQUEST_ID}
	 * header, or generates one if none is supplied.
	 * <p>
	 * Note that the generated request ID is a random 64-bit long integer encoded as
	 * hexadecimal. It is not generated using any cryptographic algorithms or a secure
	 * PRNG, so it should not be used for anything other than troubleshooting purposes.
	 * </p>
	 */
	protected String getOrCreateRequestId(HttpServletRequest theRequest) {
		String requestId = ServletRequestTracing.maybeGetRequestId(theRequest);

		// TODO can we delete this and newRequestId()
		//  and use ServletRequestTracing.getOrGenerateRequestId() instead?
		//  newRequestId() is protected.  Do you think anyone actually overrode it?
		if (isBlank(requestId)) {
			int requestIdLength = Constants.REQUEST_ID_LENGTH;
			requestId = newRequestId(requestIdLength);
		}

		return requestId;
	}

	/**
	 * Generate a new request ID string. Subclasses may ovrride.
	 */
	protected String newRequestId(int theRequestIdLength) {
		String requestId;
		requestId = RandomStringUtils.randomAlphanumeric(theRequestIdLength);
		return requestId;
	}

	protected void validateRequest(ServletRequestDetails theRequestDetails) {
		String[] elements = theRequestDetails.getParameters().get(Constants.PARAM_ELEMENTS);
		if (elements != null) {
			for (String next : elements) {
				if (next.indexOf(':') != -1) {
					throw new InvalidRequestException(Msg.code(295) + "Invalid _elements value: \"" + next + "\"");
				}
			}
		}

		elements = theRequestDetails.getParameters().get(Constants.PARAM_ELEMENTS + Constants.PARAM_ELEMENTS_EXCLUDE_MODIFIER);
		if (elements != null) {
			for (String next : elements) {
				if (next.indexOf(':') != -1) {
					throw new InvalidRequestException(Msg.code(296) + "Invalid _elements value: \"" + next + "\"");
				}
			}
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

				Collection<IResourceProvider> resourceProvider = getResourceProviders();
				// 'true' tells registerProviders() that
				// this call is part of initialization
				registerProviders(resourceProvider, true);

				Collection<Object> providers = getPlainProviders();
				// 'true' tells registerProviders() that
				// this call is part of initialization
				registerProviders(providers, true);

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

				invokeInitialize(confProvider);
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

			} catch (Exception e) {
				ourLog.error("An error occurred while loading request handlers!", e);
				throw new ServletException(Msg.code(297) + "Failed to initialize FHIR Restful server: " + e.getMessage(), e);
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
	 * @throws ServletException If the initialization failed. Note that you should consider throwing {@link UnavailableException}
	 *                          (which extends {@link ServletException}), as this is a flag to the servlet container
	 *                          that the servlet is not usable.
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

	/**
	 * Should the server "pretty print" responses by default (requesting clients can always override this default by
	 * supplying an <code>Accept</code> header in the request, or a <code>_pretty</code>
	 * parameter in the request URL.
	 * <p>
	 * The default is <code>false</code>
	 * </p>
	 * <p>
	 * Note that this setting is ignored by {@link ca.uhn.fhir.rest.server.interceptor.ResponseHighlighterInterceptor}
	 * when streaming HTML, although even when that interceptor it used this setting will
	 * still be honoured when streaming raw FHIR.
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
	 * <p>
	 * Note that this setting is ignored by {@link ca.uhn.fhir.rest.server.interceptor.ResponseHighlighterInterceptor}
	 * when streaming HTML, although even when that interceptor it used this setting will
	 * still be honoured when streaming raw FHIR.
	 * </p>
	 *
	 * @param theDefaultPrettyPrint The default pretty print setting
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
			resourceName = tok.nextTokenUnescapedAndSanitized();
			if (partIsOperation(resourceName)) {
				operation = resourceName;
				resourceName = null;
			}
		}
		theRequestDetails.setResourceName(resourceName);

		if (tok.hasMoreTokens()) {
			String nextString = tok.nextTokenUnescapedAndSanitized();
			if (partIsOperation(nextString)) {
				operation = nextString;
			} else {
				id = myFhirContext.getVersion().newIdType();
				id.setParts(null, resourceName, UrlUtil.unescape(nextString), null);
			}
		}

		if (tok.hasMoreTokens()) {
			String nextString = tok.nextTokenUnescapedAndSanitized();
			if (nextString.equals(Constants.PARAM_HISTORY)) {
				if (tok.hasMoreTokens()) {
					String versionString = tok.nextTokenUnescapedAndSanitized();
					if (id == null) {
						throw new InvalidRequestException(Msg.code(298) + "Don't know how to handle request path: " + theRequestPath);
					}
					id.setParts(null, resourceName, id.getIdPart(), UrlUtil.unescape(versionString));
				} else {
					operation = Constants.PARAM_HISTORY;
				}
			} else if (partIsOperation(nextString)) {
				if (operation != null) {
					throw new InvalidRequestException(Msg.code(299) + "URL Path contains two operations: " + theRequestPath);
				}
				operation = nextString;
			} else {
				compartment = nextString;
			}
		}

		// Secondary is for things like ..../_tags/_delete
		String secondaryOperation = null;

		while (tok.hasMoreTokens()) {
			String nextString = tok.nextTokenUnescapedAndSanitized();
			if (operation == null) {
				operation = nextString;
			} else if (secondaryOperation == null) {
				secondaryOperation = nextString;
			} else {
				throw new InvalidRequestException(Msg.code(300) + "URL path has unexpected token '" + nextString + "' at the end: " + theRequestPath);
			}
		}

		theRequestDetails.setId(id);
		theRequestDetails.setOperation(operation);
		theRequestDetails.setSecondaryOperation(secondaryOperation);
		theRequestDetails.setCompartmentName(compartment);
	}

	/**
	 * Registers an interceptor. This method is a convenience method which calls
	 * <code>getInterceptorService().registerInterceptor(theInterceptor);</code>
	 *
	 * @param theInterceptor The interceptor, must not be null
	 */
	public void registerInterceptor(Object theInterceptor) {
		Validate.notNull(theInterceptor, "Interceptor can not be null");
		getInterceptorService().registerInterceptor(theInterceptor);
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
	 * Register a group of providers. These could be Resource Providers (classes implementing {@link IResourceProvider}) or "plain" providers, or a mixture of the two.
	 *
	 * @param theProviders a {@code Collection} of theProviders. The parameter could be null or an empty {@code Collection}
	 */
	public void registerProviders(Object... theProviders) {
		Validate.noNullElements(theProviders);
		registerProviders(Arrays.asList(theProviders));
	}

	/**
	 * Register a group of theProviders. These could be Resource Providers, "plain" theProviders or a mixture of the two.
	 *
	 * @param theProviders a {@code Collection} of theProviders. The parameter could be null or an empty {@code Collection}
	 */
	public void registerProviders(Collection<?> theProviders) {
		Validate.noNullElements(theProviders, "theProviders must not contain any null elements");

		myProviderRegistrationMutex.lock();
		try {
			if (!myStarted) {
				for (Object provider : theProviders) {
					ourLog.debug("Registration of provider [" + provider.getClass().getName() + "] will be delayed until FHIR server startup");
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
		registerProviders(theProviders, false);
	}

	/*
	 * Inner method to actually register theProviders
	 */
	protected void registerProviders(Collection<?> theProviders, boolean inInit) {
		Validate.noNullElements(theProviders, "theProviders must not contain any null elements");

		List<IResourceProvider> newResourceProviders = new ArrayList<>();
		List<Object> newPlainProviders = new ArrayList<>();

		if (theProviders != null) {
			for (Object provider : theProviders) {
				if (provider instanceof IResourceProvider) {
					IResourceProvider rsrcProvider = (IResourceProvider) provider;
					Class<? extends IBaseResource> resourceType = rsrcProvider.getResourceType();
					if (resourceType == null) {
						throw new NullPointerException(Msg.code(301) + "getResourceType() on class '" + rsrcProvider.getClass().getCanonicalName() + "' returned null");
					}
					if (!inInit) {
						myResourceProviders.add(rsrcProvider);
					}
					newResourceProviders.add(rsrcProvider);
				} else {
					if (!inInit) {
						myPlainProviders.add(provider);
					}
					newPlainProviders.add(provider);
				}

			}
			if (!newResourceProviders.isEmpty()) {
				ourLog.info("Added {} resource provider(s). Total {}", newResourceProviders.size(), myResourceProviders.size());
				for (IResourceProvider provider : newResourceProviders) {
					findResourceMethods(provider);
				}
			}
			if (!newPlainProviders.isEmpty()) {
				ourLog.info("Added {} plain provider(s). Total {}", newPlainProviders.size(), myPlainProviders.size());
				for (Object provider : newPlainProviders) {
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
	private void removeResourceMethods(Object theProvider) {
		ourLog.info("Removing RESTful methods for: {}", theProvider.getClass());
		Class<?> clazz = theProvider.getClass();
		Class<?> supertype = clazz.getSuperclass();
		Collection<String> resourceNames = new ArrayList<>();
		while (!Object.class.equals(supertype)) {
			removeResourceMethods(theProvider, supertype, resourceNames);
			removeResourceMethodsOnInterfaces(theProvider, supertype.getInterfaces(), resourceNames);
			supertype = supertype.getSuperclass();
		}
		removeResourceMethods(theProvider, clazz, resourceNames);
		removeResourceMethodsOnInterfaces(theProvider, clazz.getInterfaces(), resourceNames);
		removeResourceNameBindings(resourceNames, theProvider);
	}

	private void removeResourceNameBindings(Collection<String> resourceNames, Object theProvider) {
		for (String resourceName : resourceNames) {
			ResourceBinding resourceBinding = myResourceNameToBinding.get(resourceName);
			if (resourceBinding == null) {
				continue;
			}

			for (Iterator<BaseMethodBinding<?>> it = resourceBinding.getMethodBindings().iterator(); it.hasNext(); ) {
				BaseMethodBinding<?> binding = it.next();
				if (theProvider.equals(binding.getProvider())) {
					it.remove();
					ourLog.info("{} binding of {} was removed", resourceName, binding);
				}
			}

			if (resourceBinding.getMethodBindings().isEmpty()) {
				myResourceNameToBinding.remove(resourceName);
			}
		}
	}

	private void removeResourceMethodsOnInterfaces(Object theProvider, Class<?>[] interfaces, Collection<String> resourceNames) {
		for (Class<?> anInterface : interfaces) {
			removeResourceMethods(theProvider, anInterface, resourceNames);
			removeResourceMethodsOnInterfaces(theProvider, anInterface.getInterfaces(), resourceNames);
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
			case PATCH:
			case TRACE:
			case TRACK:
			case HEAD:
			case CONNECT:
			default:
				handleRequest(method, theReq, theResp);
				break;
		}
	}

	/**
	 * Sets the non-resource specific providers which implement method calls on this server
	 *
	 * @see #setResourceProviders(Collection)
	 */
	public void setProviders(Object... theProviders) {
		Validate.noNullElements(theProviders, "theProviders must not contain any null elements");

		myPlainProviders.clear();
		if (theProviders != null) {
			myPlainProviders.addAll(Arrays.asList(theProviders));
		}
	}

	/**
	 * If provided (default is <code>null</code>), the tenant identification
	 * strategy provides a mechanism for a multitenant server to identify which tenant
	 * a given request corresponds to.
	 */
	public void setTenantIdentificationStrategy(ITenantIdentificationStrategy theTenantIdentificationStrategy) {
		myTenantIdentificationStrategy = theTenantIdentificationStrategy;
	}

	protected void throwUnknownFhirOperationException(RequestDetails requestDetails, String requestPath, RequestTypeEnum theRequestType) {
		FhirContext fhirContext = myFhirContext;
		throwUnknownFhirOperationException(requestDetails, requestPath, theRequestType, fhirContext);
	}

	protected void throwUnknownResourceTypeException(String theResourceName) {
		throw new ResourceNotFoundException(Msg.code(302) + "Unknown resource type '" + theResourceName + "' - Server knows how to handle: " + myResourceNameToBinding.keySet());
	}

	/**
	 * Unregisters an interceptor. This method is a convenience method which calls
	 * <code>getInterceptorService().unregisterInterceptor(theInterceptor);</code>
	 *
	 * @param theInterceptor The interceptor, must not be null
	 */
	public void unregisterInterceptor(Object theInterceptor) {
		Validate.notNull(theInterceptor, "Interceptor can not be null");
		getInterceptorService().unregisterInterceptor(theInterceptor);
	}

	/**
	 * Unregister one provider (either a Resource provider or a plain provider)
	 */
	public void unregisterProvider(Object provider) {
		if (provider != null) {
			Collection<Object> providerList = new ArrayList<>(1);
			providerList.add(provider);
			unregisterProviders(providerList);
		}
	}

	/**
	 * Unregister a {@code Collection} of providers
	 */
	public void unregisterProviders(Collection<?> providers) {
		if (providers != null) {
			for (Object provider : providers) {
				removeResourceMethods(provider);
				if (provider instanceof IResourceProvider) {
					myResourceProviders.remove(provider);
				} else {
					myPlainProviders.remove(provider);
				}
				invokeDestroy(provider);
			}
		}
	}

	/**
	 * Unregisters all plain and resource providers (but not the conformance provider).
	 */
	public void unregisterAllProviders() {
		unregisterAllProviders(myPlainProviders);
		unregisterAllProviders(myResourceProviders);
	}

	private void unregisterAllProviders(List<?> theProviders) {
		while (theProviders.size() > 0) {
			unregisterProvider(theProviders.get(0));
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
		String message = UrlUtil.sanitizeUrlPart(theException.getMessage());
		theResponse.getWriter().write(message);
	}

	/**
	 * By default, server create/update/patch/transaction methods return a copy of the resource
	 * as it was stored. This may be overridden by the client using the
	 * <code>Prefer</code> header.
	 * <p>
	 * This setting changes the default behaviour if no Prefer header is supplied by the client.
	 * The default is {@link PreferReturnEnum#REPRESENTATION}
	 * </p>
	 *
	 * @see <a href="http://hl7.org/fhir/http.html#ops">HL7 FHIR Specification</a> section on the Prefer header
	 */
	@Override
	public PreferReturnEnum getDefaultPreferReturn() {
		return myDefaultPreferReturn;
	}

	/**
	 * By default, server create/update/patch/transaction methods return a copy of the resource
	 * as it was stored. This may be overridden by the client using the
	 * <code>Prefer</code> header.
	 * <p>
	 * This setting changes the default behaviour if no Prefer header is supplied by the client.
	 * The default is {@link PreferReturnEnum#REPRESENTATION}
	 * </p>
	 *
	 * @see <a href="http://hl7.org/fhir/http.html#ops">HL7 FHIR Specification</a> section on the Prefer header
	 */
	public void setDefaultPreferReturn(PreferReturnEnum theDefaultPreferReturn) {
		Validate.notNull(theDefaultPreferReturn, "theDefaultPreferReturn must not be null");
		myDefaultPreferReturn = theDefaultPreferReturn;
	}

	/**
	 * Create a CapabilityStatement based on the given request
	 */
	public IBaseConformance getCapabilityStatement(ServletRequestDetails theRequestDetails) {
		// Create a cloned request details so we can make it indicate that this is a capabilities request
		ServletRequestDetails requestDetails = new ServletRequestDetails(theRequestDetails);
		requestDetails.setRestOperationType(RestOperationTypeEnum.METADATA);

		return myServerConformanceMethod.provideCapabilityStatement(this, requestDetails);
	}

	/**
	 * Count length of URL string, but treating unescaped sequences (e.g. ' ') as their unescaped equivalent (%20)
	 */
	protected static int escapedLength(String theServletPath) {
		int delta = 0;
		for (int i = 0; i < theServletPath.length(); i++) {
			char next = theServletPath.charAt(i);
			if (next == ' ') {
				delta = delta + 2;
			}
		}
		return theServletPath.length() + delta;
	}

	public static void throwUnknownFhirOperationException(RequestDetails requestDetails, String requestPath, RequestTypeEnum theRequestType, FhirContext theFhirContext) {
		String message = theFhirContext.getLocalizer().getMessage(RestfulServer.class, "unknownMethod", theRequestType.name(), requestPath, requestDetails.getParameters().keySet());

		IBaseOperationOutcome oo = OperationOutcomeUtil.newInstance(theFhirContext);
		OperationOutcomeUtil.addIssue(theFhirContext, oo, "error", message, null, "not-supported");

		throw new InvalidRequestException(Msg.code(303) + message, oo);
	}

	private static boolean partIsOperation(String nextString) {
		return nextString.length() > 0 && (nextString.charAt(0) == '_' || nextString.charAt(0) == '$' || nextString.equals(Constants.URL_TOKEN_METADATA));
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
