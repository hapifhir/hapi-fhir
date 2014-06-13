package ca.uhn.fhir.context;

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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.Validate;

import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.narrative.INarrativeGenerator;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.parser.JsonParser;
import ca.uhn.fhir.parser.XmlParser;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.client.IRestfulClientFactory;
import ca.uhn.fhir.rest.client.RestfulClientFactory;
import ca.uhn.fhir.rest.client.api.IBasicClient;
import ca.uhn.fhir.rest.client.api.IRestfulClient;
import ca.uhn.fhir.util.FhirTerser;

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

	private volatile Map<Class<? extends IElement>, BaseRuntimeElementDefinition<?>> myClassToElementDefinition = Collections.emptyMap();
	private volatile Map<String, RuntimeResourceDefinition> myIdToResourceDefinition = Collections.emptyMap();
	private volatile Map<String, RuntimeResourceDefinition> myNameToElementDefinition = Collections.emptyMap();
	private volatile INarrativeGenerator myNarrativeGenerator;
	private volatile IRestfulClientFactory myRestfulClientFactory;
	private volatile RuntimeChildUndeclaredExtensionDefinition myRuntimeChildUndeclaredExtensionDefinition;

	/**
	 * Default constructor. In most cases this is the right constructor to use.
	 */
	public FhirContext() {
		super();
	}

	public FhirContext(Class<? extends IResource> theResourceType) {
		this(toCollection(theResourceType));
	}

	public FhirContext(Class<?>... theResourceTypes) {
		this(toCollection(theResourceTypes));
	}

	public FhirContext(Collection<Class<? extends IResource>> theResourceTypes) {
		scanResourceTypes(theResourceTypes);
	}

	/**
	 * Returns the scanned runtime model for the given type. This is an advanced feature which is generally only needed for extending the core library.
	 */
	public BaseRuntimeElementDefinition<?> getElementDefinition(Class<? extends IElement> theElementType) {
		return myClassToElementDefinition.get(theElementType);
	}
	
	public FhirTerser newTerser() {
		return new FhirTerser(this);
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
		Validate.notBlank(theResourceName, "Resource name must not be blank");

		RuntimeResourceDefinition retVal = myNameToElementDefinition.get(theResourceName);

		if (retVal == null) {
			try {
				String candidateName = Patient.class.getPackage().getName() + "." + theResourceName;
				Class<?> clazz = Class.forName(candidateName);
				if (IResource.class.isAssignableFrom(clazz)) {
					retVal = scanResourceType((Class<? extends IResource>) clazz);
				}
			} catch (ClassNotFoundException e) {
				return null;
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

	/**
	 * Create and return a new JSON parser.
	 * 
	 * <p>
	 * Performance Note: <b>This class is cheap</b> to create, and may be called once for every message being processed without incurring any performance penalty
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
	 * Performance Note: <b>This class is cheap</b> to create, and may be called once for every message being processed without incurring any performance penalty
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
	 * <p>
	 * In most cases it is preferable to use the non-generic clients instead of this mechanism, but not always.
	 * </p>
	 * 
	 * @param theServerBase
	 *            The URL of the base for the restful FHIR server to connect to
	 * @return 
	 */
	public IGenericClient newRestfulGenericClient(String theServerBase) {
		return getRestfulClientFactory().newGenericClient(theServerBase);
	}

	/**
	 * Create and return a new JSON parser.
	 * 
	 * <p>
	 * Performance Note: <b>This class is cheap</b> to create, and may be called once for every message being processed without incurring any performance penalty
	 * </p>
	 */
	public IParser newXmlParser() {
		return new XmlParser(this);
	}

	public void setNarrativeGenerator(INarrativeGenerator theNarrativeGenerator) {
		myNarrativeGenerator = theNarrativeGenerator;
	}

	private RuntimeResourceDefinition scanResourceType(Class<? extends IResource> theResourceType) {
		ArrayList<Class<? extends IResource>> resourceTypes = new ArrayList<Class<? extends IResource>>();
		resourceTypes.add(theResourceType);
		Map<Class<? extends IElement>, BaseRuntimeElementDefinition<?>> defs = scanResourceTypes(resourceTypes);
		return (RuntimeResourceDefinition) defs.get(theResourceType);
	}

	private Map<Class<? extends IElement>, BaseRuntimeElementDefinition<?>> scanResourceTypes(Collection<Class<? extends IResource>> theResourceTypes) {
		ModelScanner scanner = new ModelScanner(theResourceTypes);
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

		return classToElementDefinition;
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
