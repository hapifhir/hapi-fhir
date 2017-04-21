package ca.uhn.fhir.context;

import java.lang.reflect.Method;

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
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.lang.reflect.Modifier;
import java.util.*;
import java.util.Map.Entry;

import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.support.IContextValidationSupport;
import ca.uhn.fhir.fluentpath.IFluentPath;
import ca.uhn.fhir.i18n.HapiLocalizer;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IFhirVersion;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.view.ViewGenerator;
import ca.uhn.fhir.narrative.INarrativeGenerator;
import ca.uhn.fhir.parser.*;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.client.IRestfulClientFactory;
import ca.uhn.fhir.rest.client.apache.ApacheRestfulClientFactory;
import ca.uhn.fhir.rest.client.api.IBasicClient;
import ca.uhn.fhir.rest.client.api.IRestfulClient;
import ca.uhn.fhir.rest.server.AddProfileTagEnum;
import ca.uhn.fhir.rest.server.IVersionSpecificBundleFactory;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.VersionUtil;
import ca.uhn.fhir.validation.FhirValidator;

/**
 * The FHIR context is the central starting point for the use of the HAPI FHIR API. It should be created once, and then
 * used as a factory for various other types of objects (parsers, clients, etc.).
 * 
 * <p>
 * Important usage notes:
 * </p>
 * <ul>
 * <li>
 * Thread safety: <b>This class is thread safe</b> and may be shared between multiple processing
 * threads, except for the {@link #registerCustomType} and {@link #registerCustomTypes} methods.
 * </li>
 * <li>
 * Performance: <b>This class is expensive</b> to create, as it scans every resource class it needs to parse or encode
 * to build up an internal model of those classes. For that reason, you should try to create one FhirContext instance
 * which remains for the life of your application and reuse that instance. Note that it will not cause problems to
 * create multiple instances (ie. resources originating from one FhirContext may be passed to parsers originating from
 * another) but you will incur a performance penalty if a new FhirContext is created for every message you parse/encode.
 * </li>
 * </ul>
 */
public class FhirContext {

	private static final List<Class<? extends IBaseResource>> EMPTY_LIST = Collections.emptyList();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirContext.class);
	private AddProfileTagEnum myAddProfileTagWhenEncoding = AddProfileTagEnum.ONLY_FOR_CUSTOM;
	private volatile Map<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> myClassToElementDefinition = Collections.emptyMap();
	private ArrayList<Class<? extends IBase>> myCustomTypes;
	private Map<String, Class<? extends IBaseResource>> myDefaultTypeForProfile = new HashMap<String, Class<? extends IBaseResource>>();
	private volatile Map<String, RuntimeResourceDefinition> myIdToResourceDefinition = Collections.emptyMap();
	private volatile boolean myInitialized;
	private volatile boolean myInitializing = false;
	private HapiLocalizer myLocalizer = new HapiLocalizer();
	private volatile Map<String, BaseRuntimeElementDefinition<?>> myNameToElementDefinition = Collections.emptyMap();
	private volatile Map<String, RuntimeResourceDefinition> myNameToResourceDefinition = Collections.emptyMap();
	private volatile Map<String, Class<? extends IBaseResource>> myNameToResourceType;
	private volatile INarrativeGenerator myNarrativeGenerator;
	private volatile IParserErrorHandler myParserErrorHandler = new LenientErrorHandler();
	private ParserOptions myParserOptions = new ParserOptions();
	private Set<PerformanceOptionsEnum> myPerformanceOptions = new HashSet<PerformanceOptionsEnum>();
	private Collection<Class<? extends IBaseResource>> myResourceTypesToScan;
	private volatile IRestfulClientFactory myRestfulClientFactory;
	private volatile RuntimeChildUndeclaredExtensionDefinition myRuntimeChildUndeclaredExtensionDefinition;
	private IContextValidationSupport<?, ?, ?, ?, ?, ?> myValidationSupport;

	private final IFhirVersion myVersion;

	private Map<FhirVersionEnum, Map<String, Class<? extends IBaseResource>>> myVersionToNameToResourceType = Collections.emptyMap();

	/**
	 * @deprecated It is recommended that you use one of the static initializer methods instead
	 *             of this method, e.g. {@link #forDstu2()} or {@link #forDstu3()}
	 */
	@Deprecated
	public FhirContext() {
		this(EMPTY_LIST);
	}

	/**
	 * @deprecated It is recommended that you use one of the static initializer methods instead
	 *             of this method, e.g. {@link #forDstu2()} or {@link #forDstu3()}
	 */
	@Deprecated
	public FhirContext(Class<? extends IBaseResource> theResourceType) {
		this(toCollection(theResourceType));
	}

	/**
	 * @deprecated It is recommended that you use one of the static initializer methods instead
	 *             of this method, e.g. {@link #forDstu2()} or {@link #forDstu3()}
	 */
	@Deprecated
	public FhirContext(Class<?>... theResourceTypes) {
		this(toCollection(theResourceTypes));
	}

	/**
	 * @deprecated It is recommended that you use one of the static initializer methods instead
	 *             of this method, e.g. {@link #forDstu2()} or {@link #forDstu3()}
	 */
	@Deprecated
	public FhirContext(Collection<Class<? extends IBaseResource>> theResourceTypes) {
		this(null, theResourceTypes);
	}

	/**
	 * In most cases it is recommended that you use one of the static initializer methods instead
	 * of this method, e.g. {@link #forDstu2()} or {@link #forDstu3()}, but
	 * this method can also be used if you wish to supply the version programmatically.
	 */
	public FhirContext(FhirVersionEnum theVersion) {
		this(theVersion, null);
	}

	private FhirContext(FhirVersionEnum theVersion, Collection<Class<? extends IBaseResource>> theResourceTypes) {
		VersionUtil.getVersion();

		if (theVersion != null) {
			if (!theVersion.isPresentOnClasspath()) {
				throw new IllegalStateException(getLocalizer().getMessage(FhirContext.class, "noStructuresForSpecifiedVersion", theVersion.name()));
			}
			myVersion = theVersion.getVersionImplementation();
		} else if (FhirVersionEnum.DSTU1.isPresentOnClasspath()) {
			myVersion = FhirVersionEnum.DSTU1.getVersionImplementation();
		} else if (FhirVersionEnum.DSTU2.isPresentOnClasspath()) {
			myVersion = FhirVersionEnum.DSTU2.getVersionImplementation();
		} else if (FhirVersionEnum.DSTU2_HL7ORG.isPresentOnClasspath()) {
			myVersion = FhirVersionEnum.DSTU2_HL7ORG.getVersionImplementation();
		} else if (FhirVersionEnum.DSTU3.isPresentOnClasspath()) {
			myVersion = FhirVersionEnum.DSTU3.getVersionImplementation();
		} else {
			throw new IllegalStateException(getLocalizer().getMessage(FhirContext.class, "noStructures"));
		}

		if (theVersion == null) {
			ourLog.info("Creating new FhirContext with auto-detected version [{}]. It is recommended to explicitly select a version for future compatibility by invoking FhirContext.forDstuX()",
					myVersion.getVersion().name());
		} else {
			ourLog.info("Creating new FHIR context for FHIR version [{}]", myVersion.getVersion().name());
		}

		myResourceTypesToScan = theResourceTypes;

		/*
		 * Check if we're running in Android mode and configure the context appropriately if so
		 */
		try {
			Class<?> clazz = Class.forName("ca.uhn.fhir.android.AndroidMarker");
			ourLog.info("Android mode detected, configuring FhirContext for Android operation");
			try {
				Method method = clazz.getMethod("configureContext", FhirContext.class);
				method.invoke(null, this);
			} catch (Throwable e) {
				ourLog.warn("Failed to configure context for Android operation", e);
			}
		} catch (ClassNotFoundException e) {
			ourLog.trace("Android mode not detected");
		}

	}

	private String createUnknownResourceNameError(String theResourceName, FhirVersionEnum theVersion) {
		return getLocalizer().getMessage(FhirContext.class, "unknownResourceName", theResourceName, theVersion);
	}

	private void ensureCustomTypeList() {
		myClassToElementDefinition.clear();
		if (myCustomTypes == null) {
			myCustomTypes = new ArrayList<Class<? extends IBase>>();
		}
	}

	/**
	 * When encoding resources, this setting configures the parser to include
	 * an entry in the resource's metadata section which indicates which profile(s) the
	 * resource claims to conform to. The default is {@link AddProfileTagEnum#ONLY_FOR_CUSTOM}.
	 * 
	 * @see #setAddProfileTagWhenEncoding(AddProfileTagEnum) for more information
	 */
	public AddProfileTagEnum getAddProfileTagWhenEncoding() {
		return myAddProfileTagWhenEncoding;
	}

	Collection<RuntimeResourceDefinition> getAllResourceDefinitions() {
		validateInitialized();
		return myNameToResourceDefinition.values();
	}

	/**
	 * Returns the default resource type for the given profile
	 * 
	 * @see #setDefaultTypeForProfile(String, Class)
	 */
	public Class<? extends IBaseResource> getDefaultTypeForProfile(String theProfile) {
		validateInitialized();
		return myDefaultTypeForProfile.get(theProfile);
	}

	/**
	 * Returns the scanned runtime model for the given type. This is an advanced feature which is generally only needed
	 * for extending the core library.
	 */
	@SuppressWarnings("unchecked")
	public BaseRuntimeElementDefinition<?> getElementDefinition(Class<? extends IBase> theElementType) {
		validateInitialized();
		BaseRuntimeElementDefinition<?> retVal = myClassToElementDefinition.get(theElementType);
		if (retVal == null) {
			retVal = scanDatatype((Class<? extends IElement>) theElementType);
		}
		return retVal;
	}

	/**
	 * Returns the scanned runtime model for the given type. This is an advanced feature which is generally only needed
	 * for extending the core library.
	 * <p>
	 * Note that this method is case insensitive!
	 * </p>
	 */
	public BaseRuntimeElementDefinition<?> getElementDefinition(String theElementName) {
		validateInitialized();
		return myNameToElementDefinition.get(theElementName.toLowerCase());
	}

	/** For unit tests only */
	int getElementDefinitionCount() {
		validateInitialized();
		return myClassToElementDefinition.size();
	}

	/**
	 * Returns all element definitions (resources, datatypes, etc.)
	 */
	public Collection<BaseRuntimeElementDefinition<?>> getElementDefinitions() {
		validateInitialized();
		return Collections.unmodifiableCollection(myClassToElementDefinition.values());
	}

	/**
	 * This feature is not yet in its final state and should be considered an internal part of HAPI for now - use with
	 * caution
	 */
	public HapiLocalizer getLocalizer() {
		if (myLocalizer == null) {
			myLocalizer = new HapiLocalizer();
		}
		return myLocalizer;
	}

	public INarrativeGenerator getNarrativeGenerator() {
		return myNarrativeGenerator;
	}

	/**
	 * Returns the parser options object which will be used to supply default
	 * options to newly created parsers
	 * 
	 * @return The parser options - Will not return <code>null</code>
	 */
	public ParserOptions getParserOptions() {
		return myParserOptions;
	}

	/**
	 * Get the configured performance options
	 */
	public Set<PerformanceOptionsEnum> getPerformanceOptions() {
		return myPerformanceOptions;
	}

	/**
	 * Returns the scanned runtime model for the given type. This is an advanced feature which is generally only needed
	 * for extending the core library.
	 */
	public RuntimeResourceDefinition getResourceDefinition(Class<? extends IBaseResource> theResourceType) {
		validateInitialized();
		if (theResourceType == null) {
			throw new NullPointerException("theResourceType can not be null");
		}
		if (Modifier.isAbstract(theResourceType.getModifiers())) {
			throw new IllegalArgumentException("Can not scan abstract or interface class (resource definitions must be concrete classes): " + theResourceType.getName());
		}

		RuntimeResourceDefinition retVal = (RuntimeResourceDefinition) myClassToElementDefinition.get(theResourceType);
		if (retVal == null) {
			retVal = scanResourceType(theResourceType);
		}
		return retVal;
	}

	public RuntimeResourceDefinition getResourceDefinition(FhirVersionEnum theVersion, String theResourceName) {
		Validate.notNull(theVersion, "theVersion can not be null");
		validateInitialized();

		if (theVersion.equals(myVersion.getVersion())) {
			return getResourceDefinition(theResourceName);
		}

		Map<String, Class<? extends IBaseResource>> nameToType = myVersionToNameToResourceType.get(theVersion);
		if (nameToType == null) {
			nameToType = new HashMap<String, Class<? extends IBaseResource>>();
			Map<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> existing = Collections.emptyMap();
			ModelScanner.scanVersionPropertyFile(null, nameToType, theVersion, existing);

			Map<FhirVersionEnum, Map<String, Class<? extends IBaseResource>>> newVersionToNameToResourceType = new HashMap<FhirVersionEnum, Map<String, Class<? extends IBaseResource>>>();
			newVersionToNameToResourceType.putAll(myVersionToNameToResourceType);
			newVersionToNameToResourceType.put(theVersion, nameToType);
			myVersionToNameToResourceType = newVersionToNameToResourceType;
		}

		Class<? extends IBaseResource> resourceType = nameToType.get(theResourceName.toLowerCase());
		if (resourceType == null) {
			throw new DataFormatException(createUnknownResourceNameError(theResourceName, theVersion));
		}

		return getResourceDefinition(resourceType);
	}

	/**
	 * Returns the scanned runtime model for the given type. This is an advanced feature which is generally only needed
	 * for extending the core library.
	 */
	public RuntimeResourceDefinition getResourceDefinition(IBaseResource theResource) {
		validateInitialized();
		Validate.notNull(theResource, "theResource must not be null");
		return getResourceDefinition(theResource.getClass());
	}

	/**
	 * Returns the scanned runtime model for the given type. This is an advanced feature which is generally only needed
	 * for extending the core library.
	 * <p>
	 * Note that this method is case insensitive!
	 * </p>
	 */
	public RuntimeResourceDefinition getResourceDefinition(String theResourceName) {
		validateInitialized();
		Validate.notBlank(theResourceName, "theResourceName must not be blank");

		String resourceName = theResourceName.toLowerCase();
		RuntimeResourceDefinition retVal = myNameToResourceDefinition.get(resourceName);

		if (retVal == null) {
			Class<? extends IBaseResource> clazz = myNameToResourceType.get(resourceName.toLowerCase());
			if (clazz == null) {
				throw new DataFormatException(createUnknownResourceNameError(theResourceName, myVersion.getVersion()));
			}
			if (IBaseResource.class.isAssignableFrom(clazz)) {
				retVal = scanResourceType(clazz);
			}
		}

		return retVal;
	}

	// /**
	// * Return an unmodifiable collection containing all known resource definitions
	// */
	// public Collection<RuntimeResourceDefinition> getResourceDefinitions() {
	//
	// Set<Class<? extends IBase>> datatypes = Collections.emptySet();
	// Map<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> existing = Collections.emptyMap();
	// HashMap<String, Class<? extends IBaseResource>> types = new HashMap<String, Class<? extends IBaseResource>>();
	// ModelScanner.scanVersionPropertyFile(datatypes, types, myVersion.getVersion(), existing);
	// for (int next : types.)
	//
	// return Collections.unmodifiableCollection(myIdToResourceDefinition.values());
	// }

	/**
	 * Returns the scanned runtime model for the given type. This is an advanced feature which is generally only needed
	 * for extending the core library.
	 */
	public RuntimeResourceDefinition getResourceDefinitionById(String theId) {
		validateInitialized();
		return myIdToResourceDefinition.get(theId);
	}

	/**
	 * Returns the scanned runtime models. This is an advanced feature which is generally only needed for extending the
	 * core library.
	 */
	public Collection<RuntimeResourceDefinition> getResourceDefinitionsWithExplicitId() {
		validateInitialized();
		return myIdToResourceDefinition.values();
	}

	/**
	 * Get the restful client factory. If no factory has been set, this will be initialized with
	 * a new ApacheRestfulClientFactory.
	 * 
	 * @return the factory used to create the restful clients
	 */
	public IRestfulClientFactory getRestfulClientFactory() {
		if (myRestfulClientFactory == null) {
			myRestfulClientFactory = new ApacheRestfulClientFactory(this);
		}
		return myRestfulClientFactory;
	}

	public RuntimeChildUndeclaredExtensionDefinition getRuntimeChildUndeclaredExtensionDefinition() {
		validateInitialized();
		return myRuntimeChildUndeclaredExtensionDefinition;
	}

	/**
	 * Returns the validation support module configured for this context, creating a default
	 * implementation if no module has been passed in via the {@link #setValidationSupport(IContextValidationSupport)}
	 * method
	 * 
	 * @see #setValidationSupport(IContextValidationSupport)
	 */
	public IContextValidationSupport<?, ?, ?, ?, ?, ?> getValidationSupport() {
		if (myValidationSupport == null) {
			myValidationSupport = myVersion.createValidationSupport();
		}
		return myValidationSupport;
	}

	public IFhirVersion getVersion() {
		return myVersion;
	}

	/**
	 * Returns <code>true</code> if any default types for specific profiles have been defined
	 * within this context.
	 * 
	 * @see #setDefaultTypeForProfile(String, Class)
	 * @see #getDefaultTypeForProfile(String)
	 */
	public boolean hasDefaultTypeForProfile() {
		validateInitialized();
		return !myDefaultTypeForProfile.isEmpty();
	}

	/**
	 * This method should be considered experimental and will likely change in future releases
	 * of HAPI. Use with caution!
	 */
	public IVersionSpecificBundleFactory newBundleFactory() {
		return myVersion.newBundleFactory(this);
	}

	/**
	 * Creates a new FluentPath engine which can be used to exvaluate
	 * path expressions over FHIR resources. Note that this engine will use the
	 * {@link IContextValidationSupport context validation support} module which is
	 * configured on the context at the time this method is called.
	 * <p>
	 * In other words, call {@link #setValidationSupport(IContextValidationSupport)} before
	 * calling {@link #newFluentPath()}
	 * </p>
	 * <p>
	 * Note that this feature was added for FHIR DSTU3 and is not available
	 * for contexts configured to use an older version of FHIR. Calling this method
	 * on a context for a previous version of fhir will result in an
	 * {@link UnsupportedOperationException}
	 * </p>
	 * 
	 * @since 2.2
	 */
	public IFluentPath newFluentPath() {
		return myVersion.createFluentPathExecutor(this);
	}

	/**
	 * Create and return a new JSON parser.
	 * 
	 * <p>
	 * Thread safety: <b>Parsers are not guaranteed to be thread safe</b>. Create a new parser instance for every thread
	 * or every message being parsed/encoded.
	 * </p>
	 * <p>
	 * Performance Note: <b>This method is cheap</b> to call, and may be called once for every message being processed
	 * without incurring any performance penalty
	 * </p>
	 */
	public IParser newJsonParser() {
		return new JsonParser(this, myParserErrorHandler);
	}

	/**
	 * Instantiates a new client instance. This method requires an interface which is defined specifically for your use
	 * cases to contain methods for each of the RESTful operations you wish to implement (e.g. "read ImagingStudy",
	 * "search Patient by identifier", etc.). This interface must extend {@link IRestfulClient} (or commonly its
	 * sub-interface {@link IBasicClient}). See the <a
	 * href="http://jamesagnew.github.io/hapi-fhir/doc_rest_client.html">RESTful Client</a> documentation for more
	 * information on how to define this interface.
	 * 
	 * <p>
	 * Performance Note: <b>This method is cheap</b> to call, and may be called once for every operation invocation
	 * without incurring any performance penalty
	 * </p>
	 * 
	 * @param theClientType
	 *          The client type, which is an interface type to be instantiated
	 * @param theServerBase
	 *          The URL of the base for the restful FHIR server to connect to
	 * @return A newly created client
	 * @throws ConfigurationException
	 *           If the interface type is not an interface
	 */
	public <T extends IRestfulClient> T newRestfulClient(Class<T> theClientType, String theServerBase) {
		return getRestfulClientFactory().newClient(theClientType, theServerBase);
	}

	/**
	 * Instantiates a new generic client. A generic client is able to perform any of the FHIR RESTful operations against
	 * a compliant server, but does not have methods defining the specific functionality required (as is the case with
	 * {@link #newRestfulClient(Class, String) non-generic clients}).
	 * 
	 * <p>
	 * Performance Note: <b>This method is cheap</b> to call, and may be called once for every operation invocation
	 * without incurring any performance penalty
	 * </p>
	 * 
	 * @param theServerBase
	 *          The URL of the base for the restful FHIR server to connect to
	 */
	public IGenericClient newRestfulGenericClient(String theServerBase) {
		return getRestfulClientFactory().newGenericClient(theServerBase);
	}

	public FhirTerser newTerser() {
		return new FhirTerser(this);
	}

	/**
	 * Create a new validator instance.
	 * <p>
	 * Note on thread safety: Validators are thread safe, you may use a single validator
	 * in multiple threads. (This is in contrast to parsers)
	 * </p>
	 */
	public FhirValidator newValidator() {
		return new FhirValidator(this);
	}

	public ViewGenerator newViewGenerator() {
		return new ViewGenerator(this);
	}

	/**
	 * Create and return a new XML parser.
	 * 
	 * <p>
	 * Thread safety: <b>Parsers are not guaranteed to be thread safe</b>. Create a new parser instance for every thread
	 * or every message being parsed/encoded.
	 * </p>
	 * <p>
	 * Performance Note: <b>This method is cheap</b> to call, and may be called once for every message being processed
	 * without incurring any performance penalty
	 * </p>
	 */
	public IParser newXmlParser() {
		return new XmlParser(this, myParserErrorHandler);
	}

	/**
	 * This method may be used to register a custom resource or datatype. Note that by using
	 * custom types, you are creating a system that will not interoperate with other systems that
	 * do not know about your custom type. There are valid reasons however for wanting to create
	 * custom types and this method can be used to enable them.
	 * <p>
	 * <b>THREAD SAFETY WARNING:</b> This method is not thread safe. It should be called before any
	 * threads are able to call any methods on this context.
	 * </p>
	 * 
	 * @param theType
	 *          The custom type to add (must not be <code>null</code>)
	 */
	public void registerCustomType(Class<? extends IBase> theType) {
		Validate.notNull(theType, "theType must not be null");

		ensureCustomTypeList();
		myCustomTypes.add(theType);
	}

	/**
	 * This method may be used to register a custom resource or datatype. Note that by using
	 * custom types, you are creating a system that will not interoperate with other systems that
	 * do not know about your custom type. There are valid reasons however for wanting to create
	 * custom types and this method can be used to enable them.
	 * <p>
	 * <b>THREAD SAFETY WARNING:</b> This method is not thread safe. It should be called before any
	 * threads are able to call any methods on this context.
	 * </p>
	 * 
	 * @param theTypes
	 *          The custom types to add (must not be <code>null</code> or contain null elements in the collection)
	 */
	public void registerCustomTypes(Collection<Class<? extends IBase>> theTypes) {
		Validate.notNull(theTypes, "theTypes must not be null");
		Validate.noNullElements(theTypes.toArray(), "theTypes must not contain any null elements");

		ensureCustomTypeList();

		myCustomTypes.addAll(theTypes);
	}

	private BaseRuntimeElementDefinition<?> scanDatatype(Class<? extends IElement> theResourceType) {
		ArrayList<Class<? extends IElement>> resourceTypes = new ArrayList<Class<? extends IElement>>();
		resourceTypes.add(theResourceType);
		Map<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> defs = scanResourceTypes(resourceTypes);
		return defs.get(theResourceType);
	}

	private RuntimeResourceDefinition scanResourceType(Class<? extends IBaseResource> theResourceType) {
		ArrayList<Class<? extends IElement>> resourceTypes = new ArrayList<Class<? extends IElement>>();
		resourceTypes.add(theResourceType);
		Map<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> defs = scanResourceTypes(resourceTypes);
		return (RuntimeResourceDefinition) defs.get(theResourceType);
	}

	private synchronized Map<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> scanResourceTypes(Collection<Class<? extends IElement>> theResourceTypes) {
		List<Class<? extends IBase>> typesToScan = new ArrayList<Class<? extends IBase>>();
		if (theResourceTypes != null) {
			typesToScan.addAll(theResourceTypes);
		}
		if (myCustomTypes != null) {
			typesToScan.addAll(myCustomTypes);
			myCustomTypes = null;
		}

		ModelScanner scanner = new ModelScanner(this, myVersion.getVersion(), myClassToElementDefinition, typesToScan);
		if (myRuntimeChildUndeclaredExtensionDefinition == null) {
			myRuntimeChildUndeclaredExtensionDefinition = scanner.getRuntimeChildUndeclaredExtensionDefinition();
		}

		Map<String, BaseRuntimeElementDefinition<?>> nameToElementDefinition = new HashMap<String, BaseRuntimeElementDefinition<?>>();
		nameToElementDefinition.putAll(myNameToElementDefinition);
		for (Entry<String, BaseRuntimeElementDefinition<?>> next : scanner.getNameToElementDefinitions().entrySet()) {
			if (!nameToElementDefinition.containsKey(next.getKey())) {
				nameToElementDefinition.put(next.getKey(), next.getValue());
			}
		}

		Map<String, RuntimeResourceDefinition> nameToResourceDefinition = new HashMap<String, RuntimeResourceDefinition>();
		nameToResourceDefinition.putAll(myNameToResourceDefinition);
		for (Entry<String, RuntimeResourceDefinition> next : scanner.getNameToResourceDefinition().entrySet()) {
			if (!nameToResourceDefinition.containsKey(next.getKey())) {
				nameToResourceDefinition.put(next.getKey(), next.getValue());
			}
		}

		Map<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> classToElementDefinition = new HashMap<Class<? extends IBase>, BaseRuntimeElementDefinition<?>>();
		classToElementDefinition.putAll(myClassToElementDefinition);
		classToElementDefinition.putAll(scanner.getClassToElementDefinitions());
		for (BaseRuntimeElementDefinition<?> next : classToElementDefinition.values()) {
			if (next instanceof RuntimeResourceDefinition) {
				if ("Bundle".equals(next.getName())) {
					if (!IBaseBundle.class.isAssignableFrom(next.getImplementingClass())) {
						throw new ConfigurationException("Resource type declares resource name Bundle but does not implement IBaseBundle");
					}
				}
			}
		}

		Map<String, RuntimeResourceDefinition> idToElementDefinition = new HashMap<String, RuntimeResourceDefinition>();
		idToElementDefinition.putAll(myIdToResourceDefinition);
		idToElementDefinition.putAll(scanner.getIdToResourceDefinition());

		myNameToElementDefinition = nameToElementDefinition;
		myClassToElementDefinition = classToElementDefinition;
		myIdToResourceDefinition = idToElementDefinition;
		myNameToResourceDefinition = nameToResourceDefinition;

		myNameToResourceType = scanner.getNameToResourceType();

		myInitialized = true;
		return classToElementDefinition;
	}

	/**
	 * When encoding resources, this setting configures the parser to include
	 * an entry in the resource's metadata section which indicates which profile(s) the
	 * resource claims to conform to. The default is {@link AddProfileTagEnum#ONLY_FOR_CUSTOM}.
	 * <p>
	 * This feature is intended for situations where custom resource types are being used,
	 * avoiding the need to manually add profile declarations for these custom types.
	 * </p>
	 * <p>
	 * See <a href="http://jamesagnew.gihhub.io/hapi-fhir/doc_extensions.html">Profiling and Extensions</a>
	 * for more information on using custom types.
	 * </p>
	 * <p>
	 * Note that this feature automatically adds the profile, but leaves any profile tags
	 * which have been manually added in place as well.
	 * </p>
	 * 
	 * @param theAddProfileTagWhenEncoding
	 *          The add profile mode (must not be <code>null</code>)
	 */
	public void setAddProfileTagWhenEncoding(AddProfileTagEnum theAddProfileTagWhenEncoding) {
		Validate.notNull(theAddProfileTagWhenEncoding, "theAddProfileTagWhenEncoding must not be null");
		myAddProfileTagWhenEncoding = theAddProfileTagWhenEncoding;
	}

	/**
	 * Sets the default type which will be used when parsing a resource that is found to be
	 * of the given profile.
	 * <p>
	 * For example, this method is invoked with the profile string of
	 * <code>"http://example.com/some_patient_profile"</code> and the type of <code>MyPatient.class</code>,
	 * if the parser is parsing a resource and finds that it declares that it conforms to that profile,
	 * the <code>MyPatient</code> type will be used unless otherwise specified.
	 * </p>
	 * 
	 * @param theProfile
	 *          The profile string, e.g. <code>"http://example.com/some_patient_profile"</code>. Must not be
	 *          <code>null</code> or empty.
	 * @param theClass
	 *          The resource type, or <code>null</code> to clear any existing type
	 */
	public void setDefaultTypeForProfile(String theProfile, Class<? extends IBaseResource> theClass) {
		Validate.notBlank(theProfile, "theProfile must not be null or empty");
		if (theClass == null) {
			myDefaultTypeForProfile.remove(theProfile);
		} else {
			myDefaultTypeForProfile.put(theProfile, theClass);
		}
	}

	/**
	 * This feature is not yet in its final state and should be considered an internal part of HAPI for now - use with
	 * caution
	 */
	public void setLocalizer(HapiLocalizer theMessages) {
		myLocalizer = theMessages;
	}

	public void setNarrativeGenerator(INarrativeGenerator theNarrativeGenerator) {
		myNarrativeGenerator = theNarrativeGenerator;
	}

	/**
	 * Sets a parser error handler to use by default on all parsers
	 * 
	 * @param theParserErrorHandler
	 *          The error handler
	 */
	public void setParserErrorHandler(IParserErrorHandler theParserErrorHandler) {
		Validate.notNull(theParserErrorHandler, "theParserErrorHandler must not be null");
		myParserErrorHandler = theParserErrorHandler;
	}

	/**
	 * Sets the parser options object which will be used to supply default
	 * options to newly created parsers
	 * 
	 * @param theParserOptions
	 *          The parser options object - Must not be <code>null</code>
	 */
	public void setParserOptions(ParserOptions theParserOptions) {
		Validate.notNull(theParserOptions, "theParserOptions must not be null");
		myParserOptions = theParserOptions;
	}

	/**
	 * Sets the configured performance options
	 * 
	 * @see PerformanceOptionsEnum for a list of available options
	 */
	public void setPerformanceOptions(Collection<PerformanceOptionsEnum> theOptions) {
		myPerformanceOptions.clear();
		if (theOptions != null) {
			myPerformanceOptions.addAll(theOptions);
		}
	}

	/**
	 * Sets the configured performance options
	 * 
	 * @see PerformanceOptionsEnum for a list of available options
	 */
	public void setPerformanceOptions(PerformanceOptionsEnum... thePerformanceOptions) {
		Collection<PerformanceOptionsEnum> asList = null;
		if (thePerformanceOptions != null) {
			asList = Arrays.asList(thePerformanceOptions);
		}
		setPerformanceOptions(asList);
	}

	/**
	 * Set the restful client factory
	 * 
	 * @param theRestfulClientFactory
	 */
	public void setRestfulClientFactory(IRestfulClientFactory theRestfulClientFactory) {
		Validate.notNull(theRestfulClientFactory, "theRestfulClientFactory must not be null");
		this.myRestfulClientFactory = theRestfulClientFactory;
	}

	/**
	 * Sets the validation support module to use for this context. The validation support module
	 * is used to supply underlying infrastructure such as conformance resources (StructureDefinition, ValueSet, etc)
	 * as well as to provide terminology services to modules such as the validator and FluentPath executor
	 */
	public void setValidationSupport(IContextValidationSupport<?, ?, ?, ?, ?, ?> theValidationSupport) {
		myValidationSupport = theValidationSupport;
	}

	@SuppressWarnings({ "cast" })
	private List<Class<? extends IElement>> toElementList(Collection<Class<? extends IBaseResource>> theResourceTypes) {
		if (theResourceTypes == null) {
			return null;
		}
		List<Class<? extends IElement>> resTypes = new ArrayList<Class<? extends IElement>>();
		for (Class<? extends IBaseResource> next : theResourceTypes) {
			resTypes.add((Class<? extends IElement>) next);
		}
		return resTypes;
	}
	
	private void validateInitialized() {
		// See #610
		if (!myInitialized) {
			synchronized (this) {
				if (!myInitialized && !myInitializing) {
					myInitializing = true;
					scanResourceTypes(toElementList(myResourceTypesToScan));
				}
			}
		}
	}

	/**
	 * Creates and returns a new FhirContext with version {@link FhirVersionEnum#DSTU1 DSTU1}
	 */
	public static FhirContext forDstu1() {
		return new FhirContext(FhirVersionEnum.DSTU1);
	}

	/**
	 * Creates and returns a new FhirContext with version {@link FhirVersionEnum#DSTU2 DSTU2}
	 */
	public static FhirContext forDstu2() {
		return new FhirContext(FhirVersionEnum.DSTU2);
	}

	/**
	 * Creates and returns a new FhirContext with version {@link FhirVersionEnum#DSTU2 DSTU2} (2016 May DSTU3 Snapshot)
	 */
	public static FhirContext forDstu2_1() {
		return new FhirContext(FhirVersionEnum.DSTU2_1);
	}

	/**
	 * Creates and returns a new FhirContext with version {@link FhirVersionEnum#DSTU2_HL7ORG DSTU2} (using the Reference
	 * Implementation Structures)
	 */
	public static FhirContext forDstu2Hl7Org() {
		return new FhirContext(FhirVersionEnum.DSTU2_HL7ORG);
	}

	/**
	 * Creates and returns a new FhirContext with version {@link FhirVersionEnum#DSTU3 DSTU3}
	 * 
	 * @since 1.4
	 */
	public static FhirContext forDstu3() {
		return new FhirContext(FhirVersionEnum.DSTU3);
	}

	private static Collection<Class<? extends IBaseResource>> toCollection(Class<? extends IBaseResource> theResourceType) {
		ArrayList<Class<? extends IBaseResource>> retVal = new ArrayList<Class<? extends IBaseResource>>(1);
		retVal.add(theResourceType);
		return retVal;
	}

	@SuppressWarnings("unchecked")
	private static List<Class<? extends IBaseResource>> toCollection(Class<?>[] theResourceTypes) {
		ArrayList<Class<? extends IBaseResource>> retVal = new ArrayList<Class<? extends IBaseResource>>(1);
		for (Class<?> clazz : theResourceTypes) {
			if (!IResource.class.isAssignableFrom(clazz)) {
				throw new IllegalArgumentException(clazz.getCanonicalName() + " is not an instance of " + IResource.class.getSimpleName());
			}
			retVal.add((Class<? extends IResource>) clazz);
		}
		return retVal;
	}

}
