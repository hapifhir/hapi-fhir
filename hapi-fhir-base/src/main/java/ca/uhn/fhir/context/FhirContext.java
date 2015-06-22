package ca.uhn.fhir.context;

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

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.text.WordUtils;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.i18n.HapiLocalizer;
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
import ca.uhn.fhir.parser.XmlParser;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.client.IRestfulClientFactory;
import ca.uhn.fhir.rest.client.RestfulClientFactory;
import ca.uhn.fhir.rest.client.api.IBasicClient;
import ca.uhn.fhir.rest.client.api.IRestfulClient;
import ca.uhn.fhir.rest.server.IVersionSpecificBundleFactory;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.validation.FhirValidator;

/**
 * The FHIR context is the central starting point for the use of the HAPI FHIR API. It should be created once, and then
 * used as a factory for various other types of objects (parsers, clients, etc.).
 * 
 * <p>
 * Important usage notes:
 * </p>
 * <ul>
 * <li>Thread safety: <b>This class is thread safe</b> and may be shared between multiple processing threads.</li>
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
	private volatile Map<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> myClassToElementDefinition = Collections.emptyMap();
	private volatile Map<String, RuntimeResourceDefinition> myIdToResourceDefinition = Collections.emptyMap();
	private HapiLocalizer myLocalizer = new HapiLocalizer();
	private volatile Map<String, BaseRuntimeElementDefinition<?>> myNameToElementDefinition = Collections.emptyMap();
	private volatile Map<String, RuntimeResourceDefinition> myNameToResourceDefinition = Collections.emptyMap();
	private volatile Map<String, Class<? extends IBaseResource>> myNameToResourceType;
	private volatile INarrativeGenerator myNarrativeGenerator;
	private volatile IParserErrorHandler myParserErrorHandler = new LenientErrorHandler();
	private volatile IRestfulClientFactory myRestfulClientFactory;
	private volatile RuntimeChildUndeclaredExtensionDefinition myRuntimeChildUndeclaredExtensionDefinition;
	private final IFhirVersion myVersion;
	private Map<FhirVersionEnum, Map<String, Class<? extends IBaseResource>>> myVersionToNameToResourceType = Collections.emptyMap();

	/**
	 * Default constructor. In most cases this is the right constructor to use.
	 */
	public FhirContext() {
		this(EMPTY_LIST);
	}

	public FhirContext(Class<? extends IBaseResource> theResourceType) {
		this(toCollection(theResourceType));
	}

	public FhirContext(Class<?>... theResourceTypes) {
		this(toCollection(theResourceTypes));
	}

	public FhirContext(Collection<Class<? extends IBaseResource>> theResourceTypes) {
		this(null, theResourceTypes);
	}

	public FhirContext(FhirVersionEnum theVersion) {
		this(theVersion, null);
	}

	private FhirContext(FhirVersionEnum theVersion, Collection<Class<? extends IBaseResource>> theResourceTypes) {
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
		} else if (FhirVersionEnum.DEV.isPresentOnClasspath()) {
			myVersion = FhirVersionEnum.DEV.getVersionImplementation();
		} else {
			throw new IllegalStateException(getLocalizer().getMessage(FhirContext.class, "noStructures"));
		}

		if (theVersion == null) {
			ourLog.info("Creating new FhirContext with auto-detected version [{}]. It is recommended to explicitly select a version for future compatibility by invoking FhirContext.forDstuX()", myVersion.getVersion().name());
		} else {
			ourLog.info("Creating new FHIR context for FHIR version [{}]", myVersion.getVersion().name());
		}

		scanResourceTypes(toElementList(theResourceTypes));
	}

	private String createUnknownResourceNameError(String theResourceName, FhirVersionEnum theVersion) {
		return getLocalizer().getMessage(FhirContext.class, "unknownResourceName", theResourceName, theVersion);
	}

	/**
	 * Returns the scanned runtime model for the given type. This is an advanced feature which is generally only needed
	 * for extending the core library.
	 */
	@SuppressWarnings("unchecked")
	public BaseRuntimeElementDefinition<?> getElementDefinition(Class<? extends IBase> theElementType) {
		BaseRuntimeElementDefinition<?> retVal = myClassToElementDefinition.get(theElementType);
		if (retVal == null) {
			retVal = scanDatatype((Class<? extends IElement>) theElementType);
		}
		return retVal;
	}

	/**
	 * Returns the scanned runtime model for the given type. This is an advanced feature which is generally only needed
	 * for extending the core library.
	 */
	public BaseRuntimeElementDefinition<?> getElementDefinition(String theElementName) {
		return myNameToElementDefinition.get(theElementName);
	}
	
	/** For unit tests only */
	int getElementDefinitionCount() {
		return myClassToElementDefinition.size();
	}

	/**
	 * Returns all element definitions (resources, datatypes, etc.)
	 */
	public Collection<BaseRuntimeElementDefinition<?>> getElementDefinitions() {
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
	 * Returns the scanned runtime model for the given type. This is an advanced feature which is generally only needed
	 * for extending the core library.
	 */
	@SuppressWarnings("unchecked")
	public RuntimeResourceDefinition getResourceDefinition(Class<? extends IBaseResource> theResourceType) {
		if (theResourceType == null) {
			throw new NullPointerException("theResourceType can not be null");
		}
		if (Modifier.isAbstract(theResourceType.getModifiers())) {
			throw new IllegalArgumentException("Can not scan abstract or interface class (resource definitions must be concrete classes): " + theResourceType.getName());
		}

		RuntimeResourceDefinition retVal = (RuntimeResourceDefinition) myClassToElementDefinition.get(theResourceType);
		if (retVal == null) {
			retVal = scanResourceType((Class<? extends IResource>) theResourceType);
		}
		return retVal;
	}

	public RuntimeResourceDefinition getResourceDefinition(FhirVersionEnum theVersion, String theResourceName) {
		Validate.notNull(theVersion, "theVersion can not be null");

		if (theVersion.equals(myVersion.getVersion())) {
			return getResourceDefinition(theResourceName);
		}

		Map<String, Class<? extends IBaseResource>> nameToType = myVersionToNameToResourceType.get(theVersion);
		if (nameToType == null) {
			nameToType = new HashMap<String, Class<? extends IBaseResource>>();
			ModelScanner.scanVersionPropertyFile(null, nameToType, theVersion);

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
		return getResourceDefinition(theResource.getClass());
	}

	/**
	 * Returns the scanned runtime model for the given type. This is an advanced feature which is generally only needed
	 * for extending the core library.
	 */
	@SuppressWarnings("unchecked")
	public RuntimeResourceDefinition getResourceDefinition(String theResourceName) {
		Validate.notBlank(theResourceName, "theResourceName must not be blank");
		
		String resourceName = theResourceName;

		/*
		 * TODO: this is a bit of a hack, really we should have a translation table based on a property file or
		 * something so that we can detect names like diagnosticreport
		 */
		if (Character.isLowerCase(resourceName.charAt(0))) {
			resourceName = WordUtils.capitalize(resourceName);
		}

		Validate.notBlank(resourceName, "Resource name must not be blank");

		RuntimeResourceDefinition retVal = myNameToResourceDefinition.get(resourceName);

		if (retVal == null) {
			Class<? extends IBaseResource> clazz = myNameToResourceType.get(resourceName.toLowerCase());
			if (clazz == null) {
				throw new DataFormatException(createUnknownResourceNameError(resourceName, myVersion.getVersion()));
			}
			if (IBaseResource.class.isAssignableFrom(clazz)) {
				retVal = scanResourceType((Class<? extends IResource>) clazz);
			}
		}

		return retVal;
	}

	/**
	 * Returns the scanned runtime model for the given type. This is an advanced feature which is generally only needed
	 * for extending the core library.
	 */
	public RuntimeResourceDefinition getResourceDefinitionById(String theId) {
		return myIdToResourceDefinition.get(theId);
	}

	/**
	 * Returns the scanned runtime models. This is an advanced feature which is generally only needed for extending the
	 * core library.
	 */
	public Collection<RuntimeResourceDefinition> getResourceDefinitions() {
		return myIdToResourceDefinition.values();
	}

	public IRestfulClientFactory getRestfulClientFactory() {
		if (myRestfulClientFactory == null) {
			myRestfulClientFactory = new RestfulClientFactory(this);
		}
		return myRestfulClientFactory;
	}

	public RuntimeChildUndeclaredExtensionDefinition getRuntimeChildUndeclaredExtensionDefinition() {
		return myRuntimeChildUndeclaredExtensionDefinition;
	}

	public IFhirVersion getVersion() {
		return myVersion;
	}

	/**
	 * This method should be considered experimental and will likely change in future releases
	 * of HAPI. Use with caution!
	 */
	public IVersionSpecificBundleFactory newBundleFactory() {
		return myVersion.newBundleFactory(this);
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
	 * href="http://hl7api.sourceforge.net/hapi-fhir/doc_rest_client.html">RESTful Client</a> documentation for more
	 * information on how to define this interface.
	 * 
	 * <p>
	 * Performance Note: <b>This method is cheap</b> to call, and may be called once for every operation invocation
	 * without incurring any performance penalty
	 * </p>
	 * 
	 * @param theClientType
	 *            The client type, which is an interface type to be instantiated
	 * @param theServerBase
	 *            The URL of the base for the restful FHIR server to connect to
	 * @return A newly created client
	 * @throws ConfigurationException
	 *             If the interface type is not an interface
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
	 *            The URL of the base for the restful FHIR server to connect to
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

	private BaseRuntimeElementDefinition<?> scanDatatype(Class<? extends IElement> theResourceType) {
		ArrayList<Class<? extends IElement>> resourceTypes = new ArrayList<Class<? extends IElement>>();
		resourceTypes.add(theResourceType);
		Map<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> defs = scanResourceTypes(resourceTypes);
		return defs.get(theResourceType);
	}

	private RuntimeResourceDefinition scanResourceType(Class<? extends IResource> theResourceType) {
		ArrayList<Class<? extends IElement>> resourceTypes = new ArrayList<Class<? extends IElement>>();
		resourceTypes.add(theResourceType);
		Map<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> defs = scanResourceTypes(resourceTypes);
		return (RuntimeResourceDefinition) defs.get(theResourceType);
	}

	private Map<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> scanResourceTypes(Collection<Class<? extends IElement>> theResourceTypes) {
		ModelScanner scanner = new ModelScanner(this, myVersion.getVersion(), myClassToElementDefinition, theResourceTypes);
		if (myRuntimeChildUndeclaredExtensionDefinition == null) {
			myRuntimeChildUndeclaredExtensionDefinition = scanner.getRuntimeChildUndeclaredExtensionDefinition();
		}

		Map<String, BaseRuntimeElementDefinition<?>> nameToElementDefinition = new HashMap<String, BaseRuntimeElementDefinition<?>>();
		nameToElementDefinition.putAll(myNameToElementDefinition);
		nameToElementDefinition.putAll(scanner.getNameToElementDefinitions());

		Map<String, RuntimeResourceDefinition> nameToResourceDefinition = new HashMap<String, RuntimeResourceDefinition>();
		nameToResourceDefinition.putAll(myNameToResourceDefinition);
		nameToResourceDefinition.putAll(scanner.getNameToResourceDefinition());
		
		Map<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> classToElementDefinition = new HashMap<Class<? extends IBase>, BaseRuntimeElementDefinition<?>>();
		classToElementDefinition.putAll(myClassToElementDefinition);
		classToElementDefinition.putAll(scanner.getClassToElementDefinitions());

		Map<String, RuntimeResourceDefinition> idToElementDefinition = new HashMap<String, RuntimeResourceDefinition>();
		idToElementDefinition.putAll(myIdToResourceDefinition);
		idToElementDefinition.putAll(scanner.getIdToResourceDefinition());

		myNameToElementDefinition = nameToElementDefinition;
		myClassToElementDefinition = classToElementDefinition;
		myIdToResourceDefinition = idToElementDefinition;
		myNameToResourceDefinition = nameToResourceDefinition;

		myNameToResourceType = scanner.getNameToResourceType();

		return classToElementDefinition;
	}

	/**
	 * This feature is not yet in its final state and should be considered an internal part of HAPI for now - use with
	 * caution
	 */
	public void setLocalizer(HapiLocalizer theMessages) {
		myLocalizer = theMessages;
	}

	public void setNarrativeGenerator(INarrativeGenerator theNarrativeGenerator) {
		if (theNarrativeGenerator != null) {
			theNarrativeGenerator.setFhirContext(this);
		}
		myNarrativeGenerator = theNarrativeGenerator;
	}

	/**
	 * Sets a parser error handler to use by default on all parsers
	 * 
	 * @param theParserErrorHandler The error handler
	 */
	public void setParserErrorHandler(IParserErrorHandler theParserErrorHandler) {
		Validate.notNull(theParserErrorHandler, "theParserErrorHandler must not be null");
		myParserErrorHandler = theParserErrorHandler;
	}

	@SuppressWarnings("unchecked")
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

	/**
	 * Creates and returns a new FhirContext with version {@link FhirVersionEnum#DEV}
	 * 
	 * @deprecated Support for DEV resources will be removed, you should use DSTU2 resources instead 
	 */
	@Deprecated
	public static FhirContext forDev() {
		return new FhirContext(FhirVersionEnum.DEV);
	}

	/**
	 * Creates and returns a new FhirContext with version {@link FhirVersionEnum#DSTU1}
	 */
	public static FhirContext forDstu1() {
		return new FhirContext(FhirVersionEnum.DSTU1);
	}

	/**
	 * Creates and returns a new FhirContext with version {@link FhirVersionEnum#DSTU2}
	 */
	public static FhirContext forDstu2() {
		return new FhirContext(FhirVersionEnum.DSTU2);
	}

	public static FhirContext forDstu2Hl7Org() {
		return new FhirContext(FhirVersionEnum.DSTU2_HL7ORG);
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
