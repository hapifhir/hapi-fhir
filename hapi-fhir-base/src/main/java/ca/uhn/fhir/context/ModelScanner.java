package ca.uhn.fhir.context;

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
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.context.RuntimeSearchParam.RuntimeSearchParamStatusEnum;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.BaseIdentifiableElement;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.api.IDatatype;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.IResourceBlock;
import ca.uhn.fhir.model.api.IValueSetEnumBinder;
import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Compartment;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.XhtmlDt;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.util.ReflectionUtil;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBackboneElement;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseDatatypeElement;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IBaseXhtml;
import org.hl7.fhir.instance.model.api.ICompositeType;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isBlank;

class ModelScanner {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ModelScanner.class);
	private Map<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> myClassToElementDefinitions = new HashMap<>();
	private FhirContext myContext;
	private Map<String, RuntimeResourceDefinition> myIdToResourceDefinition = new HashMap<>();
	private Map<String, BaseRuntimeElementDefinition<?>> myNameToElementDefinitions = new HashMap<>();
	private Map<String, RuntimeResourceDefinition> myNameToResourceDefinitions = new HashMap<>();
	private Map<String, Class<? extends IBaseResource>> myNameToResourceType = new HashMap<>();
	private RuntimeChildUndeclaredExtensionDefinition myRuntimeChildUndeclaredExtensionDefinition;
	private Set<Class<? extends IBase>> myScanAlso = new HashSet<>();
	private FhirVersionEnum myVersion;

	private Set<Class<? extends IBase>> myVersionTypes;

	ModelScanner(FhirContext theContext, FhirVersionEnum theVersion, Map<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> theExistingDefinitions,
					 @Nonnull Collection<Class<? extends IBase>> theResourceTypes) throws ConfigurationException {
		myContext = theContext;
		myVersion = theVersion;

		Set<Class<? extends IBase>> toScan = new HashSet<>(theResourceTypes);
		init(theExistingDefinitions, toScan);
	}

	Map<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> getClassToElementDefinitions() {
		return myClassToElementDefinitions;
	}

	Map<String, RuntimeResourceDefinition> getIdToResourceDefinition() {
		return myIdToResourceDefinition;
	}

	Map<String, BaseRuntimeElementDefinition<?>> getNameToElementDefinitions() {
		return myNameToElementDefinitions;
	}

	Map<String, RuntimeResourceDefinition> getNameToResourceDefinition() {
		return myNameToResourceDefinitions;
	}

	Map<String, Class<? extends IBaseResource>> getNameToResourceType() {
		return myNameToResourceType;
	}

	RuntimeChildUndeclaredExtensionDefinition getRuntimeChildUndeclaredExtensionDefinition() {
		return myRuntimeChildUndeclaredExtensionDefinition;
	}

	private void init(Map<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> theExistingDefinitions, Set<Class<? extends IBase>> theTypesToScan) {
		if (theExistingDefinitions != null) {
			myClassToElementDefinitions.putAll(theExistingDefinitions);
		}

		int startSize = myClassToElementDefinitions.size();
		long start = System.currentTimeMillis();
		Map<String, Class<? extends IBaseResource>> resourceTypes = myNameToResourceType;

		Set<Class<? extends IBase>> typesToScan = theTypesToScan;
		myVersionTypes = scanVersionPropertyFile(typesToScan, resourceTypes, myVersion, myClassToElementDefinitions);

		do {
			for (Class<? extends IBase> nextClass : typesToScan) {
				scan(nextClass);
			}
			myScanAlso.removeIf(theClass -> myClassToElementDefinitions.containsKey(theClass));
			typesToScan.clear();
			typesToScan.addAll(myScanAlso);
			myScanAlso.clear();
		} while (!typesToScan.isEmpty());

		for (Entry<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> nextEntry : myClassToElementDefinitions.entrySet()) {
			if (theExistingDefinitions != null && theExistingDefinitions.containsKey(nextEntry.getKey())) {
				continue;
			}
			BaseRuntimeElementDefinition<?> next = nextEntry.getValue();

			boolean deferredSeal = false;
			if (myContext.getPerformanceOptions().contains(PerformanceOptionsEnum.DEFERRED_MODEL_SCANNING)) {
				if (next instanceof BaseRuntimeElementCompositeDefinition) {
					deferredSeal = true;
				}
			}
			if (!deferredSeal) {
				next.sealAndInitialize(myContext, myClassToElementDefinitions);
			}
		}

		myRuntimeChildUndeclaredExtensionDefinition = new RuntimeChildUndeclaredExtensionDefinition();
		myRuntimeChildUndeclaredExtensionDefinition.sealAndInitialize(myContext, myClassToElementDefinitions);

		long time = System.currentTimeMillis() - start;
		int size = myClassToElementDefinitions.size() - startSize;
		ourLog.debug("Done scanning FHIR library, found {} model entries in {}ms", size, time);
	}

	private boolean isStandardType(Class<? extends IBase> theClass) {
		return myVersionTypes.contains(theClass);
	}

	void scan(Class<? extends IBase> theClass) throws ConfigurationException {
		BaseRuntimeElementDefinition<?> existingDef = myClassToElementDefinitions.get(theClass);
		if (existingDef != null) {
			return;
		}

		ResourceDef resourceDefinition = pullAnnotation(theClass, ResourceDef.class);
		if (resourceDefinition != null) {
			if (!IBaseResource.class.isAssignableFrom(theClass)) {
				throw new ConfigurationException(Msg.code(1714) + "Resource type contains a @" + ResourceDef.class.getSimpleName() + " annotation but does not implement " + IResource.class.getCanonicalName() + ": " + theClass.getCanonicalName());
			}
			@SuppressWarnings("unchecked")
			Class<? extends IBaseResource> resClass = (Class<? extends IBaseResource>) theClass;
			scanResource(resClass, resourceDefinition);
			return;
		}

		DatatypeDef datatypeDefinition = pullAnnotation(theClass, DatatypeDef.class);
		if (datatypeDefinition != null) {
			if (ICompositeType.class.isAssignableFrom(theClass)) {
				@SuppressWarnings("unchecked")
				Class<? extends ICompositeType> resClass = (Class<? extends ICompositeType>) theClass;
				scanCompositeDatatype(resClass, datatypeDefinition);
			} else if (IPrimitiveType.class.isAssignableFrom(theClass)) {
				@SuppressWarnings({"unchecked"})
				Class<? extends IPrimitiveType<?>> resClass = (Class<? extends IPrimitiveType<?>>) theClass;
				scanPrimitiveDatatype(resClass, datatypeDefinition);
			}

			return;
		}

		Block blockDefinition = pullAnnotation(theClass, Block.class);

		if (blockDefinition != null) {
			if (IResourceBlock.class.isAssignableFrom(theClass) || IBaseBackboneElement.class.isAssignableFrom(theClass) || IBaseDatatypeElement.class.isAssignableFrom(theClass)) {
				scanBlock(theClass);
			} else {
				throw new ConfigurationException(Msg.code(1715) + "Type contains a @" + Block.class.getSimpleName() + " annotation but does not implement " + IResourceBlock.class.getCanonicalName() + ": " + theClass.getCanonicalName());
			}
		}

		if (blockDefinition == null) {
			if (theClass.getSimpleName().equals("XhtmlType")) {
				return;
			}

			throw new ConfigurationException(Msg.code(1716) + "Resource class[" + theClass.getName() + "] does not contain any valid HAPI-FHIR annotations");
		}
	}

	private void scanBlock(Class<? extends IBase> theClass) {
		ourLog.debug("Scanning resource block class: {}", theClass.getName());

		String resourceName = theClass.getCanonicalName();

		// Just in case someone messes up when upgrading from DSTU2
		if (myContext.getVersion().getVersion().isEqualOrNewerThan(FhirVersionEnum.DSTU3)) {
			if (BaseIdentifiableElement.class.isAssignableFrom(theClass)) {
				throw new ConfigurationException(Msg.code(1717) + "@Block class for version " + myContext.getVersion().getVersion().name() + " should not extend " + BaseIdentifiableElement.class.getSimpleName() + ": " + theClass.getName());
			}
		}

		RuntimeResourceBlockDefinition blockDef = new RuntimeResourceBlockDefinition(resourceName, theClass, isStandardType(theClass), myContext, myClassToElementDefinitions);
		blockDef.populateScanAlso(myScanAlso);

		myClassToElementDefinitions.put(theClass, blockDef);
	}

	private void scanCompositeDatatype(Class<? extends ICompositeType> theClass, DatatypeDef theDatatypeDefinition) {
		ourLog.debug("Scanning datatype class: {}", theClass.getName());

		RuntimeCompositeDatatypeDefinition elementDef;
		if (theClass.equals(ExtensionDt.class)) {
			elementDef = new RuntimeExtensionDtDefinition(theDatatypeDefinition, theClass, true, myContext, myClassToElementDefinitions);
			// } else if (IBaseMetaType.class.isAssignableFrom(theClass)) {
			// resourceDef = new RuntimeMetaDefinition(theDatatypeDefinition, theClass, isStandardType(theClass));
		} else {
			elementDef = new RuntimeCompositeDatatypeDefinition(theDatatypeDefinition, theClass, isStandardType(theClass), myContext, myClassToElementDefinitions);
		}
		myClassToElementDefinitions.put(theClass, elementDef);
		myNameToElementDefinitions.put(elementDef.getName().toLowerCase(), elementDef);

		/*
		 * See #423:
		 * If the type contains a field that has a custom type, we want to make
		 * sure that this type gets scanned as well
		 */
		elementDef.populateScanAlso(myScanAlso);
	}

	private String scanPrimitiveDatatype(Class<? extends IPrimitiveType<?>> theClass, DatatypeDef theDatatypeDefinition) {
		ourLog.debug("Scanning resource class: {}", theClass.getName());

		String resourceName = theDatatypeDefinition.name();
		if (isBlank(resourceName)) {
			throw new ConfigurationException(Msg.code(1718) + "Resource type @" + ResourceDef.class.getSimpleName() + " annotation contains no resource name: " + theClass.getCanonicalName());
		}

		BaseRuntimeElementDefinition<?> elementDef;
		if (theClass.equals(XhtmlDt.class)) {
			@SuppressWarnings("unchecked")
			Class<XhtmlDt> clazz = (Class<XhtmlDt>) theClass;
			elementDef = new RuntimePrimitiveDatatypeNarrativeDefinition(resourceName, clazz, isStandardType(clazz));
		} else if (IBaseXhtml.class.isAssignableFrom(theClass)) {
			@SuppressWarnings("unchecked")
			Class<? extends IBaseXhtml> clazz = (Class<? extends IBaseXhtml>) theClass;
			elementDef = new RuntimePrimitiveDatatypeXhtmlHl7OrgDefinition(resourceName, clazz, isStandardType(clazz));
		} else if (IIdType.class.isAssignableFrom(theClass)) {
			elementDef = new RuntimeIdDatatypeDefinition(theDatatypeDefinition, theClass, isStandardType(theClass));
		} else {
			elementDef = new RuntimePrimitiveDatatypeDefinition(theDatatypeDefinition, theClass, isStandardType(theClass));
		}
		myClassToElementDefinitions.put(theClass, elementDef);
		if (!theDatatypeDefinition.isSpecialization()) {
			if (myVersion.isRi() && IDatatype.class.isAssignableFrom(theClass)) {
				ourLog.debug("Not adding non RI type {} to RI context", theClass);
			} else if (!myVersion.isRi() && !IDatatype.class.isAssignableFrom(theClass)) {
				ourLog.debug("Not adding RI type {} to non RI context", theClass);
			} else {
				myNameToElementDefinitions.put(resourceName, elementDef);
			}
		}

		return resourceName;
	}

	private String scanResource(Class<? extends IBaseResource> theClass, ResourceDef resourceDefinition) {
		ourLog.debug("Scanning resource class: {}", theClass.getName());

		boolean primaryNameProvider = true;
		String resourceName = resourceDefinition.name();
		if (isBlank(resourceName)) {
			Class<?> parent = theClass.getSuperclass();
			primaryNameProvider = false;
			while (parent.equals(Object.class) == false && isBlank(resourceName)) {
				ResourceDef nextDef = pullAnnotation(parent, ResourceDef.class);
				if (nextDef != null) {
					resourceName = nextDef.name();
				}
				parent = parent.getSuperclass();
			}
			if (isBlank(resourceName)) {
				throw new ConfigurationException(Msg.code(1719) + "Resource type @" + ResourceDef.class.getSimpleName() + " annotation contains no resource name(): " + theClass.getCanonicalName()
					+ " - This is only allowed for types that extend other resource types ");
			}
		}

		String resourceNameLowerCase = resourceName.toLowerCase();
		Class<? extends IBaseResource> builtInType = myNameToResourceType.get(resourceNameLowerCase);
		boolean standardType = builtInType != null && builtInType.equals(theClass) == true;
		if (primaryNameProvider) {
			if (builtInType != null && builtInType.equals(theClass) == false) {
				primaryNameProvider = false;
			}
		}

		String resourceId = resourceDefinition.id();
		if (!isBlank(resourceId)) {
			if (myIdToResourceDefinition.containsKey(resourceId)) {
				throw new ConfigurationException(Msg.code(1720) + "The following resource types have the same ID of '" + resourceId + "' - " + theClass.getCanonicalName() + " and "
					+ myIdToResourceDefinition.get(resourceId).getImplementingClass().getCanonicalName());
			}
		}

		RuntimeResourceDefinition resourceDef = new RuntimeResourceDefinition(myContext, resourceName, theClass, resourceDefinition, standardType, myClassToElementDefinitions);
		myClassToElementDefinitions.put(theClass, resourceDef);
		if (primaryNameProvider) {
			if (resourceDef.getStructureVersion() == myVersion) {
				myNameToResourceDefinitions.put(resourceNameLowerCase, resourceDef);
			}
		}

		myIdToResourceDefinition.put(resourceId, resourceDef);

		scanResourceForSearchParams(theClass, resourceDef);

		/*
		 * See #423:
		 * If the type contains a field that has a custom type, we want to make
		 * sure that this type gets scanned as well
		 */
		resourceDef.populateScanAlso(myScanAlso);

		return resourceName;
	}

	private void scanResourceForSearchParams(Class<? extends IBaseResource> theClass, RuntimeResourceDefinition theResourceDef) {

		Map<String, RuntimeSearchParam> nameToParam = new HashMap<>();
		Map<Field, SearchParamDefinition> compositeFields = new LinkedHashMap<>();

		/*
		 * Make sure we pick up fields in interfaces too.. This ensures that we
		 * grab the _id field which generally gets picked up via interface
		 */
		Set<Field> fields = new HashSet<>(Arrays.asList(theClass.getFields()));
		Class<?> nextClass = theClass;
		do {
			for (Class<?> nextInterface : nextClass.getInterfaces()) {
				fields.addAll(Arrays.asList(nextInterface.getFields()));
			}
			nextClass = nextClass.getSuperclass();
		} while (nextClass.equals(Object.class) == false);

		/*
		 * Now scan the fields for search params
		 */
		for (Field nextField : fields) {
			SearchParamDefinition searchParam = pullAnnotation(nextField, SearchParamDefinition.class);
			if (searchParam != null) {
				RestSearchParameterTypeEnum paramType = RestSearchParameterTypeEnum.forCode(searchParam.type().toLowerCase());
				if (paramType == null) {
					throw new ConfigurationException(Msg.code(1721) + "Search param " + searchParam.name() + " has an invalid type: " + searchParam.type());
				}
				Set<String> providesMembershipInCompartments;
				providesMembershipInCompartments = new HashSet<>();
				for (Compartment next : searchParam.providesMembershipIn()) {
					if (paramType != RestSearchParameterTypeEnum.REFERENCE) {
						StringBuilder b = new StringBuilder();
						b.append("Search param ");
						b.append(searchParam.name());
						b.append(" on resource type ");
						b.append(theClass.getName());
						b.append(" provides compartment membership but is not of type 'reference'");
						ourLog.warn(b.toString());
						continue;
					}
					String name = next.name();

					// As of 2021-12-28 the R5 structures incorrectly have this prefix
					if (name.startsWith("Base FHIR compartment definition for ")) {
						name = name.substring("Base FHIR compartment definition for ".length());
					}
					providesMembershipInCompartments.add(name);
				}

				List<RuntimeSearchParam.Component> components = null;
				if (paramType == RestSearchParameterTypeEnum.COMPOSITE) {
					components = new ArrayList<>();
					for (String name : searchParam.compositeOf()) {
						String ref = toCanonicalSearchParameterUri(theResourceDef, name);
						components.add(new RuntimeSearchParam.Component(null, ref));
					}
				}

				Collection<String> base = Collections.singletonList(theResourceDef.getName());
				String url = null;
				if (theResourceDef.isStandardType()) {
					String name = searchParam.name();
					url = toCanonicalSearchParameterUri(theResourceDef, name);
				}
				RuntimeSearchParam param = new RuntimeSearchParam(null, url, searchParam.name(), searchParam.description(), searchParam.path(), paramType, providesMembershipInCompartments, toTargetList(searchParam.target()), RuntimeSearchParamStatusEnum.ACTIVE, null, components, base);
				theResourceDef.addSearchParam(param);
				nameToParam.put(param.getName(), param);
			}
		}

	}

	private String toCanonicalSearchParameterUri(RuntimeResourceDefinition theResourceDef, String theName) {
		return "http://hl7.org/fhir/SearchParameter/" + theResourceDef.getName() + "-" + theName;
	}

	private Set<String> toTargetList(Class<? extends IBaseResource>[] theTarget) {
		HashSet<String> retVal = new HashSet<>();

		for (Class<? extends IBaseResource> nextType : theTarget) {
			ResourceDef resourceDef = nextType.getAnnotation(ResourceDef.class);
			if (resourceDef != null) {
				retVal.add(resourceDef.name());
			}
		}

		return retVal;
	}

	static Class<?> determineElementType(Field next) {
		Class<?> nextElementType = next.getType();
		if (List.class.equals(nextElementType)) {
			nextElementType = ReflectionUtil.getGenericCollectionTypeOfField(next);
		} else if (Collection.class.isAssignableFrom(nextElementType)) {
			throw new ConfigurationException(Msg.code(1722) + "Field '" + next.getName() + "' in type '" + next.getClass().getCanonicalName() + "' is a Collection - Only java.util.List curently supported");
		}
		return nextElementType;
	}

	@SuppressWarnings("unchecked")
	static IValueSetEnumBinder<Enum<?>> getBoundCodeBinder(Field theNext) {
		Class<?> bound = getGenericCollectionTypeOfCodedField(theNext);
		if (bound == null) {
			throw new ConfigurationException(Msg.code(1723) + "Field '" + theNext + "' has no parameter for " + BoundCodeDt.class.getSimpleName() + " to determine enum type");
		}

		String fieldName = "VALUESET_BINDER";
		try {
			Field bindingField = bound.getField(fieldName);
			return (IValueSetEnumBinder<Enum<?>>) bindingField.get(null);
		} catch (Exception e) {
			throw new ConfigurationException(Msg.code(1724) + "Field '" + theNext + "' has type parameter " + bound.getCanonicalName() + " but this class has no valueset binding field (must have a field called " + fieldName + ")", e);
		}
	}

	/**
	 * There are two implementations of all of the annotations (e.g. {@link Child} since the HL7.org ones will eventually replace the HAPI
	 * ones. Annotations can't extend each other or implement interfaces or anything like that, so rather than duplicate all of the annotation processing code this method just creates an interface
	 * Proxy to simulate the HAPI annotations if the HL7.org ones are found instead.
	 */
	static <T extends Annotation> T pullAnnotation(AnnotatedElement theTarget, Class<T> theAnnotationType) {
		T retVal = theTarget.getAnnotation(theAnnotationType);
		return retVal;
	}

	static Class<? extends Enum<?>> determineEnumTypeForBoundField(Field next) {
		@SuppressWarnings("unchecked")
		Class<? extends Enum<?>> enumType = (Class<? extends Enum<?>>) ReflectionUtil.getGenericCollectionTypeOfFieldWithSecondOrderForList(next);
		return enumType;
	}

	private static Class<?> getGenericCollectionTypeOfCodedField(Field next) {
		Class<?> type;
		ParameterizedType collectionType = (ParameterizedType) next.getGenericType();
		Type firstArg = collectionType.getActualTypeArguments()[0];
		if (ParameterizedType.class.isAssignableFrom(firstArg.getClass())) {
			ParameterizedType pt = ((ParameterizedType) firstArg);
			firstArg = pt.getActualTypeArguments()[0];
			type = (Class<?>) firstArg;
		} else {
			type = (Class<?>) firstArg;
		}
		return type;
	}

	static Set<Class<? extends IBase>> scanVersionPropertyFile(Set<Class<? extends IBase>> theDatatypes, Map<String, Class<? extends IBaseResource>> theResourceTypes, FhirVersionEnum theVersion, Map<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> theExistingElementDefinitions) {
		Set<Class<? extends IBase>> retVal = new HashSet<>();

		try (InputStream str = theVersion.getVersionImplementation().getFhirVersionPropertiesFile()) {
			Properties prop = new Properties();
			prop.load(str);
			for (Entry<Object, Object> nextEntry : prop.entrySet()) {
				String nextKey = nextEntry.getKey().toString();
				String nextValue = nextEntry.getValue().toString();

				if (nextKey.startsWith("datatype.")) {
					if (theDatatypes != null) {
						try {
							// Datatypes

							@SuppressWarnings("unchecked")
							Class<? extends IBase> dtType = (Class<? extends IBase>) Class.forName(nextValue);
							if (theExistingElementDefinitions.containsKey(dtType)) {
								continue;
							}
							retVal.add(dtType);

							if (IElement.class.isAssignableFrom(dtType)) {
								@SuppressWarnings("unchecked")
								Class<? extends IElement> nextClass = (Class<? extends IElement>) dtType;
								theDatatypes.add(nextClass);
							} else if (IBaseDatatype.class.isAssignableFrom(dtType)) {
								@SuppressWarnings("unchecked")
								Class<? extends IBaseDatatype> nextClass = (Class<? extends IBaseDatatype>) dtType;
								theDatatypes.add(nextClass);
							} else {
								ourLog.warn("Class is not assignable from " + IElement.class.getSimpleName() + " or " + IBaseDatatype.class.getSimpleName() + ": " + nextValue);
								continue;
							}

						} catch (ClassNotFoundException e) {
							throw new ConfigurationException(Msg.code(1725) + "Unknown class[" + nextValue + "] for data type definition: " + nextKey.substring("datatype.".length()), e);
						}
					}
				} else if (nextKey.startsWith("resource.")) {
					// Resources
					String resName = nextKey.substring("resource.".length()).toLowerCase();
					try {
						@SuppressWarnings("unchecked")
						Class<? extends IBaseResource> nextClass = (Class<? extends IBaseResource>) Class.forName(nextValue);
						if (theExistingElementDefinitions.containsKey(nextClass)) {
							continue;
						}
						if (!IBaseResource.class.isAssignableFrom(nextClass)) {
							throw new ConfigurationException(Msg.code(1726) + "Class is not assignable from " + IBaseResource.class.getSimpleName() + ": " + nextValue);
						}

						theResourceTypes.put(resName, nextClass);
					} catch (ClassNotFoundException e) {
						throw new ConfigurationException(Msg.code(1727) + "Unknown class[" + nextValue + "] for resource definition: " + nextKey.substring("resource.".length()), e);
					}
				} else {
					throw new ConfigurationException(Msg.code(1728) + "Unexpected property in version property file: " + nextKey + "=" + nextValue);
				}
			}
		} catch (IOException e) {
			throw new ConfigurationException(Msg.code(1729) + "Failed to load model property file from classpath: " + "/ca/uhn/fhir/model/dstu/model.properties");
		}

		return retVal;
	}

}
