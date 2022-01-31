package ca.uhn.fhir.context;

import ca.uhn.fhir.context.api.AddProfileTagEnum;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.fhirpath.IFhirPath;
import ca.uhn.fhir.i18n.HapiLocalizer;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IFhirVersion;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.view.ViewGenerator;
import ca.uhn.fhir.narrative.INarrativeGenerator;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.parser.IParserErrorHandler;
import ca.uhn.fhir.parser.JsonParser;
import ca.uhn.fhir.parser.LenientErrorHandler;
import ca.uhn.fhir.parser.NDJsonParser;
import ca.uhn.fhir.parser.RDFParser;
import ca.uhn.fhir.parser.XmlParser;
import ca.uhn.fhir.rest.api.IVersionSpecificBundleFactory;
import ca.uhn.fhir.rest.client.api.IBasicClient;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.IRestfulClient;
import ca.uhn.fhir.rest.client.api.IRestfulClientFactory;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.ReflectionUtil;
import ca.uhn.fhir.util.VersionUtil;
import ca.uhn.fhir.validation.FhirValidator;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.jena.riot.Lang;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

/*
 * #%L
 * HAPI FHIR - Core Library
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
	private static final Map<FhirVersionEnum, FhirContext> ourStaticContexts = Collections.synchronizedMap(new EnumMap<>(FhirVersionEnum.class));
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirContext.class);
	private final IFhirVersion myVersion;
	private final Map<String, Class<? extends IBaseResource>> myDefaultTypeForProfile = new HashMap<>();
	private final Set<PerformanceOptionsEnum> myPerformanceOptions = new HashSet<>();
	private final Collection<Class<? extends IBaseResource>> myResourceTypesToScan;
	private AddProfileTagEnum myAddProfileTagWhenEncoding = AddProfileTagEnum.ONLY_FOR_CUSTOM;
	private volatile Map<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> myClassToElementDefinition = Collections.emptyMap();
	private ArrayList<Class<? extends IBase>> myCustomTypes;
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
	private volatile IRestfulClientFactory myRestfulClientFactory;
	private volatile RuntimeChildUndeclaredExtensionDefinition myRuntimeChildUndeclaredExtensionDefinition;
	private IValidationSupport myValidationSupport;
	private Map<FhirVersionEnum, Map<String, Class<? extends IBaseResource>>> myVersionToNameToResourceType = Collections.emptyMap();
	private volatile Set<String> myResourceNames;
	private volatile Boolean myFormatXmlSupported;
	private volatile Boolean myFormatJsonSupported;
	private volatile Boolean myFormatNDJsonSupported;
	private volatile Boolean myFormatRdfSupported;
	private IFhirValidatorFactory myFhirValidatorFactory = fhirContext -> new FhirValidator(fhirContext);

	/**
	 * @deprecated It is recommended that you use one of the static initializer methods instead
	 * of this method, e.g. {@link #forDstu2()} or {@link #forDstu3()} or {@link #forR4()}
	 */
	@Deprecated
	public FhirContext() {
		this(EMPTY_LIST);
	}

	/**
	 * @deprecated It is recommended that you use one of the static initializer methods instead
	 * of this method, e.g. {@link #forDstu2()} or {@link #forDstu3()} or {@link #forR4()}
	 */
	@Deprecated
	public FhirContext(final Class<? extends IBaseResource> theResourceType) {
		this(toCollection(theResourceType));
	}

	/**
	 * @deprecated It is recommended that you use one of the static initializer methods instead
	 * of this method, e.g. {@link #forDstu2()} or {@link #forDstu3()} or {@link #forR4()}
	 */
	@Deprecated
	public FhirContext(final Class<?>... theResourceTypes) {
		this(toCollection(theResourceTypes));
	}

	/**
	 * @deprecated It is recommended that you use one of the static initializer methods instead
	 * of this method, e.g. {@link #forDstu2()} or {@link #forDstu3()} or {@link #forR4()}
	 */
	@Deprecated
	public FhirContext(final Collection<Class<? extends IBaseResource>> theResourceTypes) {
		this(null, theResourceTypes);
	}

	/**
	 * In most cases it is recommended that you use one of the static initializer methods instead
	 * of this method, e.g. {@link #forDstu2()} or {@link #forDstu3()} or {@link #forR4()}, but
	 * this method can also be used if you wish to supply the version programmatically.
	 */
	public FhirContext(final FhirVersionEnum theVersion) {
		this(theVersion, null);
	}

	private FhirContext(final FhirVersionEnum theVersion, final Collection<Class<? extends IBaseResource>> theResourceTypes) {
		VersionUtil.getVersion();

		if (theVersion != null) {
			if (!theVersion.isPresentOnClasspath()) {
				throw new IllegalStateException(Msg.code(1680) + getLocalizer().getMessage(FhirContext.class, "noStructuresForSpecifiedVersion", theVersion.name()));
			}
			myVersion = theVersion.getVersionImplementation();
		} else if (FhirVersionEnum.DSTU2.isPresentOnClasspath()) {
			myVersion = FhirVersionEnum.DSTU2.getVersionImplementation();
		} else if (FhirVersionEnum.DSTU2_HL7ORG.isPresentOnClasspath()) {
			myVersion = FhirVersionEnum.DSTU2_HL7ORG.getVersionImplementation();
		} else if (FhirVersionEnum.DSTU2_1.isPresentOnClasspath()) {
			myVersion = FhirVersionEnum.DSTU2_1.getVersionImplementation();
		} else if (FhirVersionEnum.DSTU3.isPresentOnClasspath()) {
			myVersion = FhirVersionEnum.DSTU3.getVersionImplementation();
		} else if (FhirVersionEnum.R4.isPresentOnClasspath()) {
			myVersion = FhirVersionEnum.R4.getVersionImplementation();
		} else {
			throw new IllegalStateException(Msg.code(1681) + getLocalizer().getMessage(FhirContext.class, "noStructures"));
		}

		if (theVersion == null) {
			ourLog.info("Creating new FhirContext with auto-detected version [{}]. It is recommended to explicitly select a version for future compatibility by invoking FhirContext.forDstuX()",
				myVersion.getVersion().name());
		} else {
			if ("true".equals(System.getProperty("unit_test_mode"))) {
				String calledAt = ExceptionUtils.getStackFrames(new Throwable())[4];
				ourLog.info("Creating new FHIR context for FHIR version [{}]{}", myVersion.getVersion().name(), calledAt);
			} else {
				ourLog.info("Creating new FHIR context for FHIR version [{}]", myVersion.getVersion().name());
			}
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


	/**
	 * @since 5.6.0
	 */
	public static FhirContext forDstu2Cached() {
		return forCached(FhirVersionEnum.DSTU2);
	}

	/**
	 * @since 5.5.0
	 */
	public static FhirContext forDstu3Cached() {
		return forCached(FhirVersionEnum.DSTU3);
	}

	/**
	 * @since 5.5.0
	 */
	public static FhirContext forR4Cached() {
		return forCached(FhirVersionEnum.R4);
	}

	/**
	 * @since 5.5.0
	 */
	public static FhirContext forR5Cached() {
		return forCached(FhirVersionEnum.R5);
	}

	private String createUnknownResourceNameError(final String theResourceName, final FhirVersionEnum theVersion) {
		return getLocalizer().getMessage(FhirContext.class, "unknownResourceName", theResourceName, theVersion);
	}

	private void ensureCustomTypeList() {
		myClassToElementDefinition.clear();
		if (myCustomTypes == null) {
			myCustomTypes = new ArrayList<>();
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
	 * @param theAddProfileTagWhenEncoding The add profile mode (must not be <code>null</code>)
	 */
	public void setAddProfileTagWhenEncoding(final AddProfileTagEnum theAddProfileTagWhenEncoding) {
		Validate.notNull(theAddProfileTagWhenEncoding, "theAddProfileTagWhenEncoding must not be null");
		myAddProfileTagWhenEncoding = theAddProfileTagWhenEncoding;
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
	public Class<? extends IBaseResource> getDefaultTypeForProfile(final String theProfile) {
		validateInitialized();
		return myDefaultTypeForProfile.get(theProfile);
	}

	/**
	 * Returns the scanned runtime model for the given type. This is an advanced feature which is generally only needed
	 * for extending the core library.
	 */
	@SuppressWarnings("unchecked")
	public BaseRuntimeElementDefinition<?> getElementDefinition(final Class<? extends IBase> theElementType) {
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
	@Nullable
	public BaseRuntimeElementDefinition<?> getElementDefinition(final String theElementName) {
		validateInitialized();
		return myNameToElementDefinition.get(theElementName.toLowerCase());
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

	/**
	 * This feature is not yet in its final state and should be considered an internal part of HAPI for now - use with
	 * caution
	 */
	public void setLocalizer(final HapiLocalizer theMessages) {
		myLocalizer = theMessages;
	}

	public INarrativeGenerator getNarrativeGenerator() {
		return myNarrativeGenerator;
	}

	public void setNarrativeGenerator(final INarrativeGenerator theNarrativeGenerator) {
		myNarrativeGenerator = theNarrativeGenerator;
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
	 * Sets the parser options object which will be used to supply default
	 * options to newly created parsers
	 *
	 * @param theParserOptions The parser options object - Must not be <code>null</code>
	 */
	public void setParserOptions(final ParserOptions theParserOptions) {
		Validate.notNull(theParserOptions, "theParserOptions must not be null");
		myParserOptions = theParserOptions;
	}

	/**
	 * Get the configured performance options
	 */
	public Set<PerformanceOptionsEnum> getPerformanceOptions() {
		return myPerformanceOptions;
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
	 * Sets the configured performance options
	 *
	 * @see PerformanceOptionsEnum for a list of available options
	 */
	public void setPerformanceOptions(final Collection<PerformanceOptionsEnum> theOptions) {
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
	public void setPerformanceOptions(final PerformanceOptionsEnum... thePerformanceOptions) {
		Collection<PerformanceOptionsEnum> asList = null;
		if (thePerformanceOptions != null) {
			asList = Arrays.asList(thePerformanceOptions);
		}
		setPerformanceOptions(asList);
	}

	/**
	 * Returns the scanned runtime model for the given type. This is an advanced feature which is generally only needed
	 * for extending the core library.
	 */
	public RuntimeResourceDefinition getResourceDefinition(final Class<? extends IBaseResource> theResourceType) {
		validateInitialized();
		Validate.notNull(theResourceType, "theResourceType can not be null");

		if (Modifier.isAbstract(theResourceType.getModifiers())) {
			throw new IllegalArgumentException(Msg.code(1682) + "Can not scan abstract or interface class (resource definitions must be concrete classes): " + theResourceType.getName());
		}

		RuntimeResourceDefinition retVal = (RuntimeResourceDefinition) myClassToElementDefinition.get(theResourceType);
		if (retVal == null) {
			retVal = scanResourceType(theResourceType);
		}

		return retVal;
	}

	public RuntimeResourceDefinition getResourceDefinition(final FhirVersionEnum theVersion, final String theResourceName) {
		Validate.notNull(theVersion, "theVersion can not be null");
		validateInitialized();

		if (theVersion.equals(myVersion.getVersion())) {
			return getResourceDefinition(theResourceName);
		}

		Map<String, Class<? extends IBaseResource>> nameToType = myVersionToNameToResourceType.get(theVersion);
		if (nameToType == null) {
			nameToType = new HashMap<>();
			Map<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> existing = new HashMap<>();
			ModelScanner.scanVersionPropertyFile(null, nameToType, theVersion, existing);

			Map<FhirVersionEnum, Map<String, Class<? extends IBaseResource>>> newVersionToNameToResourceType = new HashMap<>();
			newVersionToNameToResourceType.putAll(myVersionToNameToResourceType);
			newVersionToNameToResourceType.put(theVersion, nameToType);
			myVersionToNameToResourceType = newVersionToNameToResourceType;
		}

		Class<? extends IBaseResource> resourceType = nameToType.get(theResourceName.toLowerCase());
		if (resourceType == null) {
			throw new DataFormatException(Msg.code(1683) + createUnknownResourceNameError(theResourceName, theVersion));
		}

		return getResourceDefinition(resourceType);
	}

	/**
	 * Returns the scanned runtime model for the given type. This is an advanced feature which is generally only needed
	 * for extending the core library.
	 */
	public RuntimeResourceDefinition getResourceDefinition(final IBaseResource theResource) {
		validateInitialized();
		Validate.notNull(theResource, "theResource must not be null");
		return getResourceDefinition(theResource.getClass());
	}

	/**
	 * Returns the name of a given resource class.
	 */
	public String getResourceType(final Class<? extends IBaseResource> theResourceType) {
		return getResourceDefinition(theResourceType).getName();
	}

	/**
	 * Returns the name of the scanned runtime model for the given type. This is an advanced feature which is generally only needed
	 * for extending the core library.
	 */
	public String getResourceType(final IBaseResource theResource) {
		return getResourceDefinition(theResource).getName();
	}

	/*
	 * Returns the type of the scanned runtime model for the given type. This is an advanced feature which is generally only needed
	 * for extending the core library.
	 * <p>
	 * Note that this method is case insensitive!
	 * </p>
	 *
	 * @throws DataFormatException If the resource name is not known
	 */
	public String getResourceType(final String theResourceName) throws DataFormatException {
		return getResourceDefinition(theResourceName).getName();
	}

	/*
	 * Returns the scanned runtime model for the given type. This is an advanced feature which is generally only needed
	 * for extending the core library.
	 * <p>
	 * Note that this method is case insensitive!
	 * </p>
	 *
	 * @throws DataFormatException If the resource name is not known
	 */
	public RuntimeResourceDefinition getResourceDefinition(final String theResourceName) throws DataFormatException {
		validateInitialized();
		Validate.notBlank(theResourceName, "theResourceName must not be blank");

		String resourceName = theResourceName.toLowerCase();
		RuntimeResourceDefinition retVal = myNameToResourceDefinition.get(resourceName);

		if (retVal == null) {
			Class<? extends IBaseResource> clazz = myNameToResourceType.get(resourceName.toLowerCase());
			if (clazz == null) {
				// ***********************************************************************
				// Multiple spots in HAPI FHIR and Smile CDR depend on DataFormatException
				// being thrown by this method, don't change that.
				// ***********************************************************************
				throw new DataFormatException(Msg.code(1684) + createUnknownResourceNameError(theResourceName, myVersion.getVersion()));
			}
			if (IBaseResource.class.isAssignableFrom(clazz)) {
				retVal = scanResourceType(clazz);
			}
		}
		return retVal;
	}

	/**
	 * Returns the scanned runtime model for the given type. This is an advanced feature which is generally only needed
	 * for extending the core library.
	 */
	public RuntimeResourceDefinition getResourceDefinitionById(final String theId) {
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
	 * Returns an unmodifiable set containing all resource names known to this
	 * context
	 *
	 * @since 5.1.0
	 */
	public Set<String> getResourceTypes() {
		Set<String> resourceNames = myResourceNames;
		if (resourceNames == null) {
			resourceNames = buildResourceNames();
			myResourceNames = resourceNames;
		}
		return resourceNames;
	}

	@Nonnull
	private Set<String> buildResourceNames() {
		Set<String> retVal = new HashSet<>();
		Properties props = new Properties();
		try (InputStream propFile = myVersion.getFhirVersionPropertiesFile()) {
			props.load(propFile);
		} catch (IOException e) {
			throw new ConfigurationException(Msg.code(1685) + "Failed to load version properties file", e);
		}
		Enumeration<?> propNames = props.propertyNames();
		while (propNames.hasMoreElements()) {
			String next = (String) propNames.nextElement();
			if (next.startsWith("resource.")) {
				retVal.add(next.substring("resource.".length()).trim());
			}
		}
		return retVal;
	}

	/**
	 * Get the restful client factory. If no factory has been set, this will be initialized with
	 * a new ApacheRestfulClientFactory.
	 *
	 * @return the factory used to create the restful clients
	 */
	public IRestfulClientFactory getRestfulClientFactory() {
		if (myRestfulClientFactory == null) {
			try {
				myRestfulClientFactory = (IRestfulClientFactory) ReflectionUtil.newInstance(Class.forName("ca.uhn.fhir.rest.client.apache.ApacheRestfulClientFactory"), FhirContext.class, this);
			} catch (ClassNotFoundException e) {
				throw new ConfigurationException(Msg.code(1686) + "hapi-fhir-client does not appear to be on the classpath");
			}
		}
		return myRestfulClientFactory;
	}

	/**
	 * Set the restful client factory
	 *
	 * @param theRestfulClientFactory The new client factory (must not be null)
	 */
	public void setRestfulClientFactory(final IRestfulClientFactory theRestfulClientFactory) {
		Validate.notNull(theRestfulClientFactory, "theRestfulClientFactory must not be null");
		this.myRestfulClientFactory = theRestfulClientFactory;
	}

	public RuntimeChildUndeclaredExtensionDefinition getRuntimeChildUndeclaredExtensionDefinition() {
		validateInitialized();
		return myRuntimeChildUndeclaredExtensionDefinition;
	}

	/**
	 * Returns the validation support module configured for this context, creating a default
	 * implementation if no module has been passed in via the {@link #setValidationSupport(IValidationSupport)}
	 * method
	 *
	 * @see #setValidationSupport(IValidationSupport)
	 */
	public IValidationSupport getValidationSupport() {
		IValidationSupport retVal = myValidationSupport;
		if (retVal == null) {
			retVal = new DefaultProfileValidationSupport(this);

			/*
			 * If hapi-fhir-validation is on the classpath, we can create a much more robust
			 * validation chain using the classes found in that package
			 */
			String inMemoryTermSvcType = "org.hl7.fhir.common.hapi.validation.support.InMemoryTerminologyServerValidationSupport";
			String commonCodeSystemsSupportType = "org.hl7.fhir.common.hapi.validation.support.CommonCodeSystemsTerminologyService";
			if (ReflectionUtil.typeExists(inMemoryTermSvcType)) {
				IValidationSupport inMemoryTermSvc = ReflectionUtil.newInstanceOrReturnNull(inMemoryTermSvcType, IValidationSupport.class, new Class<?>[]{FhirContext.class}, new Object[]{this});
				IValidationSupport commonCodeSystemsSupport = ReflectionUtil.newInstanceOrReturnNull(commonCodeSystemsSupportType, IValidationSupport.class, new Class<?>[]{FhirContext.class}, new Object[]{this});
				retVal = ReflectionUtil.newInstanceOrReturnNull("org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain", IValidationSupport.class, new Class<?>[]{IValidationSupport[].class}, new Object[]{new IValidationSupport[]{
					retVal,
					inMemoryTermSvc,
					commonCodeSystemsSupport
				}});
				assert retVal != null : "Failed to instantiate " + "org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain";
			}


			myValidationSupport = retVal;
		}
		return retVal;
	}

	/**
	 * Sets the validation support module to use for this context. The validation support module
	 * is used to supply underlying infrastructure such as conformance resources (StructureDefinition, ValueSet, etc)
	 * as well as to provide terminology services to modules such as the validator and FluentPath executor
	 */
	public void setValidationSupport(IValidationSupport theValidationSupport) {
		myValidationSupport = theValidationSupport;
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
	 * @return Returns <code>true</code> if the XML serialization format is supported, based on the
	 * available libraries on the classpath.
	 *
	 * @since 5.4.0
	 */
	public boolean isFormatXmlSupported() {
		Boolean retVal = myFormatXmlSupported;
		if (retVal == null) {
			retVal = tryToInitParser(() -> newXmlParser());
			myFormatXmlSupported = retVal;
		}
		return retVal;
	}

	/**
	 * @return Returns <code>true</code> if the JSON serialization format is supported, based on the
	 * available libraries on the classpath.
	 *
	 * @since 5.4.0
	 */
	public boolean isFormatJsonSupported() {
		Boolean retVal = myFormatJsonSupported;
		if (retVal == null) {
			retVal = tryToInitParser(() -> newJsonParser());
			myFormatJsonSupported = retVal;
		}
		return retVal;
	}

        /**
         * @return Returns <code>true</code> if the NDJSON serialization format is supported, based on the
         * available libraries on the classpath.
         *
         * @since 5.6.0
         */
        public boolean isFormatNDJsonSupported() {
                Boolean retVal = myFormatNDJsonSupported;
                if (retVal == null) {
                        retVal = tryToInitParser(() -> newNDJsonParser());
                        myFormatNDJsonSupported = retVal;
                }
                return retVal;
        }

	/**
	 * @return Returns <code>true</code> if the RDF serialization format is supported, based on the
	 * available libraries on the classpath.
	 *
	 * @since 5.4.0
	 */
	public boolean isFormatRdfSupported() {
		Boolean retVal = myFormatRdfSupported;
		if (retVal == null) {
			retVal = tryToInitParser(() -> newRDFParser());
			myFormatRdfSupported = retVal;
		}
		return retVal;
	}

	public IVersionSpecificBundleFactory newBundleFactory() {
		return myVersion.newBundleFactory(this);
	}

	/**
	 * @since 2.2
	 * @deprecated Deprecated in HAPI FHIR 5.0.0. Use {@link #newFhirPath()} instead.
	 */
	@Deprecated
	public IFhirPath newFluentPath() {
		return newFhirPath();
	}

	/**
	 * Creates a new FhirPath engine which can be used to evaluate
	 * path expressions over FHIR resources. Note that this engine will use the
	 * {@link IValidationSupport context validation support} module which is
	 * configured on the context at the time this method is called.
	 * <p>
	 * In other words, you may wish to call {@link #setValidationSupport(IValidationSupport)} before
	 * calling {@link #newFluentPath()}
	 * </p>
	 * <p>
	 * Note that this feature was added for FHIR DSTU3 and is not available
	 * for contexts configured to use an older version of FHIR. Calling this method
	 * on a context for a previous version of fhir will result in an
	 * {@link UnsupportedOperationException}
	 * </p>
	 *
	 * @since 5.0.0
	 */
	public IFhirPath newFhirPath() {
		return myVersion.createFhirPathExecutor(this);
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
         * Create and return a new NDJSON parser.
         *
         * <p>
         * Thread safety: <b>Parsers are not guaranteed to be thread safe</b>. Create a new parser instance for every thread
         * or every message being parsed/encoded.
         * </p>
         * <p>
         * Performance Note: <b>This method is cheap</b> to call, and may be called once for every message being processed
         * without incurring any performance penalty
         * </p>
         * <p>
         * The NDJsonParser provided here is expected to translate between legal NDJson and FHIR Bundles.
         * In particular, it is able to encode the resources in a FHIR Bundle to NDJson, as well as decode
         * NDJson into a FHIR "collection"-type Bundle populated with the resources described in the NDJson.
         * It will throw an exception in the event where it is asked to encode to anything other than a FHIR Bundle
         * or where it is asked to decode into anything other than a FHIR Bundle.
         * </p>
         */
        public IParser newNDJsonParser() {
                return new NDJsonParser(this, myParserErrorHandler);
        }

	/**
	 * Create and return a new RDF parser.
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
	public IParser newRDFParser() {
		return new RDFParser(this, myParserErrorHandler, Lang.TURTLE);
	}

	/**
	 * Instantiates a new client instance. This method requires an interface which is defined specifically for your use
	 * cases to contain methods for each of the RESTful operations you wish to implement (e.g. "read ImagingStudy",
	 * "search Patient by identifier", etc.). This interface must extend {@link IRestfulClient} (or commonly its
	 * sub-interface {@link IBasicClient}). See the <a
	 * href="https://hapifhir.io/hapi-fhir/docs/client/introduction.html">RESTful Client</a> documentation for more
	 * information on how to define this interface.
	 *
	 * <p>
	 * Performance Note: <b>This method is cheap</b> to call, and may be called once for every operation invocation
	 * without incurring any performance penalty
	 * </p>
	 *
	 * @param theClientType The client type, which is an interface type to be instantiated
	 * @param theServerBase The URL of the base for the restful FHIR server to connect to
	 * @return A newly created client
	 * @throws ConfigurationException If the interface type is not an interface
	 */
	public <T extends IRestfulClient> T newRestfulClient(final Class<T> theClientType, final String theServerBase) {
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
	 * @param theServerBase The URL of the base for the restful FHIR server to connect to
	 */
	public IGenericClient newRestfulGenericClient(final String theServerBase) {
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
		return myFhirValidatorFactory.newFhirValidator(this);
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
	 * @param theType The custom type to add (must not be <code>null</code>)
	 */
	public void registerCustomType(final Class<? extends IBase> theType) {
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
	 * @param theTypes The custom types to add (must not be <code>null</code> or contain null elements in the collection)
	 */
	public void registerCustomTypes(final Collection<Class<? extends IBase>> theTypes) {
		Validate.notNull(theTypes, "theTypes must not be null");
		Validate.noNullElements(theTypes.toArray(), "theTypes must not contain any null elements");

		ensureCustomTypeList();

		myCustomTypes.addAll(theTypes);
	}

	private BaseRuntimeElementDefinition<?> scanDatatype(final Class<? extends IElement> theResourceType) {
		ArrayList<Class<? extends IElement>> resourceTypes = new ArrayList<>();
		resourceTypes.add(theResourceType);
		Map<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> defs = scanResourceTypes(resourceTypes);
		return defs.get(theResourceType);
	}

	private RuntimeResourceDefinition scanResourceType(final Class<? extends IBaseResource> theResourceType) {
		ArrayList<Class<? extends IElement>> resourceTypes = new ArrayList<>();
		resourceTypes.add(theResourceType);
		Map<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> defs = scanResourceTypes(resourceTypes);
		return (RuntimeResourceDefinition) defs.get(theResourceType);
	}

	private synchronized Map<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> scanResourceTypes(final Collection<Class<? extends IElement>> theResourceTypes) {
		List<Class<? extends IBase>> typesToScan = new ArrayList<>();
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

		Map<String, BaseRuntimeElementDefinition<?>> nameToElementDefinition = new HashMap<>();
		nameToElementDefinition.putAll(myNameToElementDefinition);
		for (Entry<String, BaseRuntimeElementDefinition<?>> next : scanner.getNameToElementDefinitions().entrySet()) {
			if (!nameToElementDefinition.containsKey(next.getKey())) {
				nameToElementDefinition.put(next.getKey().toLowerCase(), next.getValue());
			}
		}

		Map<String, RuntimeResourceDefinition> nameToResourceDefinition = new HashMap<>();
		nameToResourceDefinition.putAll(myNameToResourceDefinition);
		for (Entry<String, RuntimeResourceDefinition> next : scanner.getNameToResourceDefinition().entrySet()) {
			if (!nameToResourceDefinition.containsKey(next.getKey())) {
				nameToResourceDefinition.put(next.getKey(), next.getValue());
			}
		}

		Map<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> classToElementDefinition = new HashMap<>();
		classToElementDefinition.putAll(myClassToElementDefinition);
		classToElementDefinition.putAll(scanner.getClassToElementDefinitions());
		for (BaseRuntimeElementDefinition<?> next : classToElementDefinition.values()) {
			if (next instanceof RuntimeResourceDefinition) {
				if ("Bundle".equals(next.getName())) {
					if (!IBaseBundle.class.isAssignableFrom(next.getImplementingClass())) {
						throw new ConfigurationException(Msg.code(1687) + "Resource type declares resource name Bundle but does not implement IBaseBundle");
					}
				}
			}
		}

		Map<String, RuntimeResourceDefinition> idToElementDefinition = new HashMap<>();
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
	 * Sets the default type which will be used when parsing a resource that is found to be
	 * of the given profile.
	 * <p>
	 * For example, this method is invoked with the profile string of
	 * <code>"http://example.com/some_patient_profile"</code> and the type of <code>MyPatient.class</code>,
	 * if the parser is parsing a resource and finds that it declares that it conforms to that profile,
	 * the <code>MyPatient</code> type will be used unless otherwise specified.
	 * </p>
	 *
	 * @param theProfile The profile string, e.g. <code>"http://example.com/some_patient_profile"</code>. Must not be
	 *                   <code>null</code> or empty.
	 * @param theClass   The resource type, or <code>null</code> to clear any existing type
	 */
	public void setDefaultTypeForProfile(final String theProfile, final Class<? extends IBaseResource> theClass) {
		Validate.notBlank(theProfile, "theProfile must not be null or empty");
		if (theClass == null) {
			myDefaultTypeForProfile.remove(theProfile);
		} else {
			myDefaultTypeForProfile.put(theProfile, theClass);
		}
	}

	/**
	 * Sets a parser error handler to use by default on all parsers
	 *
	 * @param theParserErrorHandler The error handler
	 */
	public FhirContext setParserErrorHandler(final IParserErrorHandler theParserErrorHandler) {
		Validate.notNull(theParserErrorHandler, "theParserErrorHandler must not be null");
		myParserErrorHandler = theParserErrorHandler;
		return this;
	}

	/**
	 * Set the factory method used to create FhirValidator instances
	 *
	 * @param theFhirValidatorFactory
	 * @return this
	 * @since 5.6.0
	 */
	public FhirContext setFhirValidatorFactory(IFhirValidatorFactory theFhirValidatorFactory) {
		myFhirValidatorFactory = theFhirValidatorFactory;
		return this;
	}

	@SuppressWarnings({"cast"})
	private List<Class<? extends IElement>> toElementList(final Collection<Class<? extends IBaseResource>> theResourceTypes) {
		if (theResourceTypes == null) {
			return null;
		}
		List<Class<? extends IElement>> resTypes = new ArrayList<>();
		for (Class<? extends IBaseResource> next : theResourceTypes) {
			resTypes.add(next);
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

	@Override
	public String toString() {
		return "FhirContext[" + myVersion.getVersion().name() + "]";
	}

	// TODO KHS add the other primitive types
	public IPrimitiveType<Boolean> getPrimitiveBoolean(Boolean theValue) {
		IPrimitiveType<Boolean> retval = (IPrimitiveType<Boolean>) getElementDefinition("boolean").newInstance();
		retval.setValue(theValue);
		return retval;
	}

	private static boolean tryToInitParser(Runnable run) {
		boolean retVal;
		try {
			run.run();
			retVal = true;
		} catch (UnsupportedClassVersionError | Exception | NoClassDefFoundError e) {
			retVal = false;
		}
		return retVal;
	}

	/**
	 * Creates and returns a new FhirContext with version {@link FhirVersionEnum#DSTU2 DSTU2}
	 */
	public static FhirContext forDstu2() {
		return new FhirContext(FhirVersionEnum.DSTU2);
	}

	/**
	 * Creates and returns a new FhirContext with version {@link FhirVersionEnum#DSTU2_HL7ORG DSTU2} (using the Reference
	 * Implementation Structures)
	 */
	public static FhirContext forDstu2Hl7Org() {
		return new FhirContext(FhirVersionEnum.DSTU2_HL7ORG);
	}

	/**
	 * Creates and returns a new FhirContext with version {@link FhirVersionEnum#DSTU2 DSTU2} (2016 May DSTU3 Snapshot)
	 */
	public static FhirContext forDstu2_1() {
		return new FhirContext(FhirVersionEnum.DSTU2_1);
	}

	/**
	 * Creates and returns a new FhirContext with version {@link FhirVersionEnum#DSTU3 DSTU3}
	 *
	 * @since 1.4
	 */
	public static FhirContext forDstu3() {
		return new FhirContext(FhirVersionEnum.DSTU3);
	}

	/**
	 * Creates and returns a new FhirContext with version {@link FhirVersionEnum#R4 R4}
	 *
	 * @since 3.0.0
	 */
	public static FhirContext forR4() {
		return new FhirContext(FhirVersionEnum.R4);
	}

	/**
	 * Creates and returns a new FhirContext with version {@link FhirVersionEnum#R5 R5}
	 *
	 * @since 4.0.0
	 */
	public static FhirContext forR5() {
		return new FhirContext(FhirVersionEnum.R5);
	}

	/**
	 * Returns a statically cached {@literal FhirContext} instance for the given version, creating one if none exists in the
	 * cache. One FhirContext will be kept in the cache for each FHIR version that is requested (by calling
	 * this method for that version), and the cache will never be expired.
	 *
	 * @since 5.1.0
	 */
	public static FhirContext forCached(FhirVersionEnum theFhirVersionEnum) {
		return ourStaticContexts.computeIfAbsent(theFhirVersionEnum, v -> new FhirContext(v));
	}

	private static Collection<Class<? extends IBaseResource>> toCollection(Class<? extends IBaseResource> theResourceType) {
		ArrayList<Class<? extends IBaseResource>> retVal = new ArrayList<>(1);
		retVal.add(theResourceType);
		return retVal;
	}

	@SuppressWarnings("unchecked")
	private static List<Class<? extends IBaseResource>> toCollection(final Class<?>[] theResourceTypes) {
		ArrayList<Class<? extends IBaseResource>> retVal = new ArrayList<Class<? extends IBaseResource>>(1);
		for (Class<?> clazz : theResourceTypes) {
			if (!IResource.class.isAssignableFrom(clazz)) {
				throw new IllegalArgumentException(Msg.code(1688) + clazz.getCanonicalName() + " is not an instance of " + IResource.class.getSimpleName());
			}
			retVal.add((Class<? extends IResource>) clazz);
		}
		return retVal;
	}
}
