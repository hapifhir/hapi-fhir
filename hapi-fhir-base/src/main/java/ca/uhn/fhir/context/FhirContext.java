package ca.uhn.fhir.context;

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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.text.WordUtils;

import ca.uhn.fhir.i18n.HapiLocalizer;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IFhirVersion;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.resource.Binary;
import ca.uhn.fhir.model.view.ViewGenerator;
import ca.uhn.fhir.narrative.INarrativeGenerator;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.parser.JsonParser;
import ca.uhn.fhir.parser.XmlParser;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.client.IRestfulClientFactory;
import ca.uhn.fhir.rest.client.RestfulClientFactory;
import ca.uhn.fhir.rest.client.api.IBasicClient;
import ca.uhn.fhir.rest.client.api.IRestfulClient;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.validation.FhirValidator;

/**
 * The FHIR context is the central starting point for the use of the HAPI FHIR API. It should be created once, and then used as a factory for various other types of objects (parsers, clients, etc.).
 * 
 * <p>
 * Important usage notes:
 * <ul>
 * <li>Thread safety: <b>This class is thread safe</b> and may be shared between multiple processing threads.</li>
 * <li>
 * Performance: <b>This class is expensive</b> to create, as it scans every resource class it needs to parse or encode to build up an internal model of those classes. For that reason, you should try
 * to create one FhirContext instance which remains for the life of your application and reuse that instance. Note that it will not cause problems to create multiple instances (ie. resources
 * originating from one FhirContext may be passed to parsers originating from another) but you will incur a performance penalty if a new FhirContext is created for every message you parse/encode.</li>
 * </ul>
 * </p>
 */
public class FhirContext {

	private static final List<Class<? extends IResource>> EMPTY_LIST = Collections.emptyList();
	private volatile Map<Class<? extends IElement>, BaseRuntimeElementDefinition<?>> myClassToElementDefinition = Collections.emptyMap();
	private volatile Map<String, RuntimeResourceDefinition> myIdToResourceDefinition = Collections.emptyMap();
	private HapiLocalizer myLocalizer = new HapiLocalizer();
	private volatile Map<String, RuntimeResourceDefinition> myNameToElementDefinition = Collections.emptyMap();
	private Map<String, String> myNameToResourceType;
	private volatile INarrativeGenerator myNarrativeGenerator;
	private volatile IRestfulClientFactory myRestfulClientFactory;
	private volatile RuntimeChildUndeclaredExtensionDefinition myRuntimeChildUndeclaredExtensionDefinition;
	private IFhirVersion myVersion;

	/**
	 * Default constructor. In most cases this is the right constructor to use.
	 */
	public FhirContext() {
		this(EMPTY_LIST);
	}

	public FhirContext(Class<? extends IResource> theResourceType) {
		this(toCollection(theResourceType));
	}

	public FhirContext(Class<?>... theResourceTypes) {
		this(toCollection(theResourceTypes));
	}

	public FhirContext(Collection<Class<? extends IResource>> theResourceTypes) {
		scanResourceTypes(theResourceTypes);
		
		if (FhirVersionEnum.DSTU1.isPresentOnClasspath()) {
			myVersion = FhirVersionEnum.DSTU1.getVersionImplementation();
		} else {
			throw new IllegalStateException("Could not find any HAPI-FHIR structure JARs on the classpath. Note that as of HAPI-FHIR v0.8, a separate FHIR strcture JAR must be added to your classpath or project pom.xml");
		}
		
	}

	/**
	 * Returns the scanned runtime model for the given type. This is an advanced feature which is generally only needed for extending the core library.
	 */
	public BaseRuntimeElementDefinition<?> getElementDefinition(Class<? extends IElement> theElementType) {
		return myClassToElementDefinition.get(theElementType);
	}

	/** For unit tests only */
	int getElementDefinitionCount() {
		return myClassToElementDefinition.size();
	}

	/**
	 * This feature is not yet in its final state and should be considered an internal part of HAPI for now - use with caution
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
	 * Returns the scanned runtime model for the given type. This is an advanced feature which is generally only needed for extending the core library.
	 */
	public RuntimeResourceDefinition getResourceDefinition(Class<? extends IResource> theResourceType) {
		RuntimeResourceDefinition retVal = (RuntimeResourceDefinition) myClassToElementDefinition.get(theResourceType);
		if (retVal == null) {
			retVal = scanResourceType(theResourceType);
		}
		return retVal;
	}

	/**
	 * Returns the scanned runtime model for the given type. This is an advanced feature which is generally only needed for extending the core library.
	 */
	public RuntimeResourceDefinition getResourceDefinition(IResource theResource) {
		return getResourceDefinition(theResource.getClass());
	}

	/**
	 * Returns the scanned runtime model for the given type. This is an advanced feature which is generally only needed for extending the core library.
	 */
	@SuppressWarnings("unchecked")
	public RuntimeResourceDefinition getResourceDefinition(String theResourceName) {
		String resourceName = theResourceName;

		/*
		 * TODO: this is a bit of a hack, really we should have a translation table based on a property file or something so that we can detect names like diagnosticreport
		 */
		if (Character.isLowerCase(resourceName.charAt(0))) {
			resourceName = WordUtils.capitalize(resourceName);
		}

		Validate.notBlank(resourceName, "Resource name must not be blank");

		RuntimeResourceDefinition retVal = myNameToElementDefinition.get(resourceName);

		if (retVal == null) {
			try {
				String className = myNameToResourceType.get(resourceName.toLowerCase());
				if (className == null) {
					if ("binary".equals(resourceName.toLowerCase())) {
						// Binary is not generated so it's not in the list of potential resources
						className = Binary.class.getName();
					} else {
						throw new DataFormatException("Unknown resource name[" + resourceName + "]");
					}
				}
				Class<?> clazz = Class.forName(className);
				if (IResource.class.isAssignableFrom(clazz)) {
					retVal = scanResourceType((Class<? extends IResource>) clazz);
				}
			} catch (ClassNotFoundException e) {
				throw new DataFormatException("Unknown resource name[" + resourceName + "]");
			}
		}

		return retVal;
	}

	/**
	 * Returns the scanned runtime model for the given type. This is an advanced feature which is generally only needed for extending the core library.
	 */
	public RuntimeResourceDefinition getResourceDefinitionById(String theId) {
		return myIdToResourceDefinition.get(theId);
	}

	/**
	 * Returns the scanned runtime models. This is an advanced feature which is generally only needed for extending the core library.
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
	 * Create and return a new JSON parser.
	 * 
	 * <p>
	 * Thread safety: <b>Parsers are not guaranteed to be thread safe</b>. Create a new parser instance for every thread or every message being parsed/encoded.
	 * </p>
	 * <p>
	 * Performance Note: <b>This method is cheap</b> to call, and may be called once for every message being processed without incurring any performance penalty
	 * </p>
	 */
	public IParser newJsonParser() {
		return new JsonParser(this);
	}

	/**
	 * Instantiates a new client instance. This method requires an interface which is defined specifically for your use cases to contain methods for each of the RESTful operations you wish to
	 * implement (e.g. "read ImagingStudy", "search Patient by identifier", etc.). This interface must extend {@link IRestfulClient} (or commonly its sub-interface {@link IBasicClient}). See the <a
	 * href="http://hl7api.sourceforge.net/hapi-fhir/doc_rest_client.html">RESTful Client</a> documentation for more information on how to define this interface.
	 * 
	 * <p>
	 * Performance Note: <b>This method is cheap</b> to call, and may be called once for every operation invocation without incurring any performance penalty
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
	 * Instantiates a new generic client. A generic client is able to perform any of the FHIR RESTful operations against a compliant server, but does not have methods defining the specific
	 * functionality required (as is the case with {@link #newRestfulClient(Class, String) non-generic clients}).
	 * 
	 * <p>
	 * Performance Note: <b>This method is cheap</b> to call, and may be called once for every operation invocation without incurring any performance penalty
	 * </p>
	 * 
	 * @param theServerBase
	 *            The URL of the base for the restful FHIR server to connect to
	 * @return
	 */
	public IGenericClient newRestfulGenericClient(String theServerBase) {
		return getRestfulClientFactory().newGenericClient(theServerBase);
	}

	public FhirTerser newTerser() {
		return new FhirTerser(this);
	}

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
	 * Thread safety: <b>Parsers are not guaranteed to be thread safe</b>. Create a new parser instance for every thread or every message being parsed/encoded.
	 * </p>
	 * <p>
	 * Performance Note: <b>This method is cheap</b> to call, and may be called once for every message being processed without incurring any performance penalty
	 * </p>
	 */
	public IParser newXmlParser() {
		return new XmlParser(this);
	}

	private RuntimeResourceDefinition scanResourceType(Class<? extends IResource> theResourceType) {
		ArrayList<Class<? extends IResource>> resourceTypes = new ArrayList<Class<? extends IResource>>();
		resourceTypes.add(theResourceType);
		Map<Class<? extends IElement>, BaseRuntimeElementDefinition<?>> defs = scanResourceTypes(resourceTypes);
		return (RuntimeResourceDefinition) defs.get(theResourceType);
	}

	private Map<Class<? extends IElement>, BaseRuntimeElementDefinition<?>> scanResourceTypes(Collection<Class<? extends IResource>> theResourceTypes) {
		ModelScanner scanner = new ModelScanner(this, myClassToElementDefinition, theResourceTypes);
		if (myRuntimeChildUndeclaredExtensionDefinition == null) {
			myRuntimeChildUndeclaredExtensionDefinition = scanner.getRuntimeChildUndeclaredExtensionDefinition();
		}

		Map<String, RuntimeResourceDefinition> nameToElementDefinition = new HashMap<String, RuntimeResourceDefinition>();
		nameToElementDefinition.putAll(myNameToElementDefinition);
		nameToElementDefinition.putAll(scanner.getNameToResourceDefinitions());

		Map<Class<? extends IElement>, BaseRuntimeElementDefinition<?>> classToElementDefinition = new HashMap<Class<? extends IElement>, BaseRuntimeElementDefinition<?>>();
		classToElementDefinition.putAll(myClassToElementDefinition);
		classToElementDefinition.putAll(scanner.getClassToElementDefinitions());

		Map<String, RuntimeResourceDefinition> idToElementDefinition = new HashMap<String, RuntimeResourceDefinition>();
		idToElementDefinition.putAll(myIdToResourceDefinition);
		idToElementDefinition.putAll(scanner.getIdToResourceDefinition());

		myNameToElementDefinition = nameToElementDefinition;
		myClassToElementDefinition = classToElementDefinition;
		myIdToResourceDefinition = idToElementDefinition;

		myNameToResourceType = scanner.getNameToResourceType();

		return classToElementDefinition;
	}

	/**
	 * This feature is not yet in its final state and should be considered an internal part of HAPI for now - use with caution
	 */
	public void setLocalizer(HapiLocalizer theMessages) {
		myLocalizer = theMessages;
	}

	public void setNarrativeGenerator(INarrativeGenerator theNarrativeGenerator) {
		myNarrativeGenerator = theNarrativeGenerator;
	}

	private static Collection<Class<? extends IResource>> toCollection(Class<? extends IResource> theResourceType) {
		ArrayList<Class<? extends IResource>> retVal = new ArrayList<Class<? extends IResource>>(1);
		retVal.add(theResourceType);
		return retVal;
	}

	@SuppressWarnings("unchecked")
	private static List<Class<? extends IResource>> toCollection(Class<?>[] theResourceTypes) {
		ArrayList<Class<? extends IResource>> retVal = new ArrayList<Class<? extends IResource>>(1);
		for (Class<?> clazz : theResourceTypes) {
			if (!IResource.class.isAssignableFrom(clazz)) {
				throw new IllegalArgumentException(clazz.getCanonicalName() + " is not an instance of " + IResource.class.getSimpleName());
			}
			retVal.add((Class<? extends IResource>) clazz);
		}
		return retVal;
	}

}
