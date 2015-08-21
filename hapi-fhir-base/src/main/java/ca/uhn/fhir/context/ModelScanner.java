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
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBackboneElement;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseDatatypeElement;
import org.hl7.fhir.instance.model.api.IBaseEnumFactory;
import org.hl7.fhir.instance.model.api.IBaseEnumeration;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IBaseXhtml;
import org.hl7.fhir.instance.model.api.ICompositeType;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.INarrative;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import ca.uhn.fhir.model.api.CodeableConceptElement;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.api.IBoundCodeableConcept;
import ca.uhn.fhir.model.api.ICodeEnum;
import ca.uhn.fhir.model.api.IDatatype;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.IResourceBlock;
import ca.uhn.fhir.model.api.IValueSetEnumBinder;
import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.base.composite.BaseContainedDt;
import ca.uhn.fhir.model.base.composite.BaseNarrativeDt;
import ca.uhn.fhir.model.base.composite.BaseResourceReferenceDt;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.ICodedDatatype;
import ca.uhn.fhir.model.primitive.XhtmlDt;
import ca.uhn.fhir.rest.method.RestSearchParameterTypeEnum;
import ca.uhn.fhir.util.ReflectionUtil;

class ModelScanner {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ModelScanner.class);

	private final Map<Class<? extends Annotation>, Class<? extends Annotation>> myAnnotationForwards = new HashMap<Class<? extends Annotation>, Class<? extends Annotation>>();
	private Map<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> myClassToElementDefinitions = new HashMap<Class<? extends IBase>, BaseRuntimeElementDefinition<?>>();
	private FhirContext myContext;
	private Map<String, RuntimeResourceDefinition> myIdToResourceDefinition = new HashMap<String, RuntimeResourceDefinition>();
	private Map<String, BaseRuntimeElementDefinition<?>> myNameToElementDefinitions = new HashMap<String, BaseRuntimeElementDefinition<?>>();
	private Map<String, RuntimeResourceDefinition> myNameToResourceDefinitions = new HashMap<String, RuntimeResourceDefinition>();
	private Map<String, Class<? extends IBaseResource>> myNameToResourceType = new HashMap<String, Class<? extends IBaseResource>>();
	private RuntimeChildUndeclaredExtensionDefinition myRuntimeChildUndeclaredExtensionDefinition;
	private Set<Class<? extends IBase>> myScanAlso = new HashSet<Class<? extends IBase>>();
	private Set<Class<? extends ICodeEnum>> myScanAlsoCodeTable = new HashSet<Class<? extends ICodeEnum>>();
	private FhirVersionEnum myVersion;

	private Set<Class<? extends IBase>> myVersionTypes;

	ModelScanner(FhirContext theContext, FhirVersionEnum theVersion, Map<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> theExistingDefinitions, Collection<Class<? extends IElement>> theResourceTypes) throws ConfigurationException {
		myContext = theContext;
		myVersion = theVersion;
		Set<Class<? extends IBase>> toScan;
		if (theResourceTypes != null) {
			toScan = new HashSet<Class<? extends IBase>>(theResourceTypes);
		} else {
			toScan = new HashSet<Class<? extends IBase>>();
		}
		init(theExistingDefinitions, toScan);
	}

	private void addScanAlso(Class<? extends IBase> theType) {
		if (theType.isInterface() || Modifier.isAbstract(theType.getModifiers())) {
			return;
		}
		myScanAlso.add(theType);
	}

	private Class<?> determineElementType(Field next) {
		Class<?> nextElementType = next.getType();
		if (List.class.equals(nextElementType)) {
			nextElementType = ReflectionUtil.getGenericCollectionTypeOfField(next);
		} else if (Collection.class.isAssignableFrom(nextElementType)) {
			throw new ConfigurationException("Field '" + next.getName() + "' in type '" + next.getClass().getCanonicalName() + "' is a Collection - Only java.util.List curently supported");
		}
		return nextElementType;
	}

	@SuppressWarnings("unchecked")
	private IValueSetEnumBinder<Enum<?>> getBoundCodeBinder(Field theNext) {
		Class<?> bound = getGenericCollectionTypeOfCodedField(theNext);
		if (bound == null) {
			throw new ConfigurationException("Field '" + theNext + "' has no parameter for " + BoundCodeDt.class.getSimpleName() + " to determine enum type");
		}

		try {
			Field bindingField = bound.getField("VALUESET_BINDER");
			return (IValueSetEnumBinder<Enum<?>>) bindingField.get(null);
		} catch (IllegalArgumentException e) {
			throw new ConfigurationException("Field '" + theNext + "' has type parameter " + bound.getCanonicalName() + " but this class has no valueset binding field", e);
		} catch (IllegalAccessException e) {
			throw new ConfigurationException("Field '" + theNext + "' has type parameter " + bound.getCanonicalName() + " but this class has no valueset binding field", e);
		} catch (NoSuchFieldException e) {
			throw new ConfigurationException("Field '" + theNext + "' has type parameter " + bound.getCanonicalName() + " but this class has no valueset binding field", e);
		} catch (SecurityException e) {
			throw new ConfigurationException("Field '" + theNext + "' has type parameter " + bound.getCanonicalName() + " but this class has no valueset binding field", e);
		}
	}

	public Map<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> getClassToElementDefinitions() {
		return myClassToElementDefinitions;
	}

	public Map<String, RuntimeResourceDefinition> getIdToResourceDefinition() {
		return myIdToResourceDefinition;
	}

	public Map<String, BaseRuntimeElementDefinition<?>> getNameToElementDefinitions() {
		return myNameToElementDefinitions;
	}

	public Map<String, RuntimeResourceDefinition> getNameToResourceDefinition() {
		return myNameToResourceDefinitions;
	}

	public Map<String, RuntimeResourceDefinition> getNameToResourceDefinitions() {
		return (myNameToResourceDefinitions);
	}

	public Map<String, Class<? extends IBaseResource>> getNameToResourceType() {
		return myNameToResourceType;
	}

	public RuntimeChildUndeclaredExtensionDefinition getRuntimeChildUndeclaredExtensionDefinition() {
		return myRuntimeChildUndeclaredExtensionDefinition;
	}

	private void init(Map<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> theExistingDefinitions, Set<Class<? extends IBase>> theDatatypes) {
		if (theExistingDefinitions != null) {
			myClassToElementDefinitions.putAll(theExistingDefinitions);
		}

		int startSize = myClassToElementDefinitions.size();
		long start = System.currentTimeMillis();
		Map<String, Class<? extends IBaseResource>> resourceTypes = myNameToResourceType;

		myVersionTypes = scanVersionPropertyFile(theDatatypes, resourceTypes, myVersion);

		// toScan.add(DateDt.class);
		// toScan.add(CodeDt.class);
		// toScan.add(DecimalDt.class);
		// toScan.add(AttachmentDt.class);
		// toScan.add(ResourceReferenceDt.class);
		// toScan.add(QuantityDt.class);

		do {
			for (Class<? extends IBase> nextClass : theDatatypes) {
				scan(nextClass);
			}
			for (Iterator<Class<? extends IBase>> iter = myScanAlso.iterator(); iter.hasNext();) {
				if (myClassToElementDefinitions.containsKey(iter.next())) {
					iter.remove();
				}
			}
			theDatatypes.clear();
			theDatatypes.addAll(myScanAlso);
			myScanAlso.clear();
		} while (!theDatatypes.isEmpty());

		for (Entry<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> nextEntry : myClassToElementDefinitions.entrySet()) {
			if (theExistingDefinitions != null && theExistingDefinitions.containsKey(nextEntry.getKey())) {
				continue;
			}
			BaseRuntimeElementDefinition<?> next = nextEntry.getValue();
			next.sealAndInitialize(myContext, myClassToElementDefinitions);
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

	/**
	 * There are two implementations of all of the annotations (e.g. {@link Child} and
	 * {@link org.hl7.fhir.instance.model.annotations.Child}) since the HL7.org ones will eventually replace the HAPI
	 * ones. Annotations can't extend each other or implement interfaces or anything like that, so rather than duplicate
	 * all of the annotation processing code this method just creates an interface Proxy to simulate the HAPI
	 * annotations if the HL7.org ones are found instead.
	 */
	@SuppressWarnings("unchecked")
	private <T extends Annotation> T pullAnnotation(Class<?> theContainer, AnnotatedElement theTarget, Class<T> theAnnotationType) {

		T retVal = theTarget.getAnnotation(theAnnotationType);
		if (myContext.getVersion().getVersion() != FhirVersionEnum.DSTU2_HL7ORG) {
			return retVal;
		}

		if (retVal == null) {
			final Class<? extends Annotation> altAnnotationClass;
			/*
			 * Use a cache to minimize Class.forName calls, since they are slow and expensive..
			 */
			if (myAnnotationForwards.containsKey(theAnnotationType) == false) {
				String sourceClassName = theAnnotationType.getName();
				String candidateAltClassName = sourceClassName.replace("ca.uhn.fhir.model.api.annotation", "org.hl7.fhir.instance.model.annotations");
				if (!sourceClassName.equals(candidateAltClassName)) {
					Class<?> forName;
					try {
						forName = Class.forName(candidateAltClassName);
						ourLog.debug("Forwarding annotation request for [{}] to class [{}]", theAnnotationType, forName);
					} catch (ClassNotFoundException e) {
						forName = null;
					}
					altAnnotationClass = (Class<? extends Annotation>) forName;
				} else {
					altAnnotationClass = null;
				}
				myAnnotationForwards.put(theAnnotationType, altAnnotationClass);
			} else {
				altAnnotationClass = myAnnotationForwards.get(theAnnotationType);
			}

			if (altAnnotationClass == null) {
				return null;
			}

			final Annotation altAnnotation;
			altAnnotation = theTarget.getAnnotation(altAnnotationClass);
			if (altAnnotation == null) {
				return null;
			}

			InvocationHandler h = new InvocationHandler() {

				@Override
				public Object invoke(Object theProxy, Method theMethod, Object[] theArgs) throws Throwable {
					Method altMethod = altAnnotationClass.getMethod(theMethod.getName(), theMethod.getParameterTypes());
					return altMethod.invoke(altAnnotation, theArgs);
				}
			};
			retVal = (T) Proxy.newProxyInstance(theAnnotationType.getClassLoader(), new Class<?>[] { theAnnotationType }, h);

		}

		return retVal;
	}

	private void scan(Class<? extends IBase> theClass) throws ConfigurationException {
		BaseRuntimeElementDefinition<?> existingDef = myClassToElementDefinitions.get(theClass);
		if (existingDef != null) {
			return;
		}

		ResourceDef resourceDefinition = pullAnnotation(theClass, theClass, ResourceDef.class);
		if (resourceDefinition != null) {
			if (!IBaseResource.class.isAssignableFrom(theClass)) {
				throw new ConfigurationException("Resource type contains a @" + ResourceDef.class.getSimpleName() + " annotation but does not implement " + IResource.class.getCanonicalName() + ": " + theClass.getCanonicalName());
			}
			@SuppressWarnings("unchecked")
			Class<? extends IBaseResource> resClass = (Class<? extends IBaseResource>) theClass;
			scanResource(resClass, resourceDefinition);
		}

		DatatypeDef datatypeDefinition = pullAnnotation(theClass, theClass, DatatypeDef.class);
		if (datatypeDefinition != null) {
			if (ICompositeType.class.isAssignableFrom(theClass)) {
				@SuppressWarnings("unchecked")
				Class<? extends ICompositeType> resClass = (Class<? extends ICompositeType>) theClass;
				scanCompositeDatatype(resClass, datatypeDefinition);
			} else if (IPrimitiveType.class.isAssignableFrom(theClass)) {
				@SuppressWarnings({ "unchecked" })
				Class<? extends IPrimitiveType<?>> resClass = (Class<? extends IPrimitiveType<?>>) theClass;
				scanPrimitiveDatatype(resClass, datatypeDefinition);
			} else {
				throw new ConfigurationException("Resource type contains a @" + DatatypeDef.class.getSimpleName() + " annotation but does not implement " + IDatatype.class.getCanonicalName() + ": " + theClass.getCanonicalName());
			}
		}

		Block blockDefinition = pullAnnotation(theClass, theClass, Block.class);

		if (blockDefinition != null) {
			if (IResourceBlock.class.isAssignableFrom(theClass) || IBaseBackboneElement.class.isAssignableFrom(theClass) || IBaseDatatypeElement.class.isAssignableFrom(theClass)) {
				scanBlock(theClass);
			} else {
				throw new ConfigurationException("Type contains a @" + Block.class.getSimpleName() + " annotation but does not implement " + IResourceBlock.class.getCanonicalName() + ": " + theClass.getCanonicalName());
			}
		}

		if (blockDefinition == null && datatypeDefinition == null && resourceDefinition == null) {
			throw new ConfigurationException("Resource class[" + theClass.getName() + "] does not contain any valid HAPI-FHIR annotations");
		}
	}

	private void scanBlock(Class<? extends IBase> theClass) {
		ourLog.debug("Scanning resource block class: {}", theClass.getName());

		String resourceName = theClass.getCanonicalName();
		if (isBlank(resourceName)) {
			throw new ConfigurationException("Block type @" + Block.class.getSimpleName() + " annotation contains no name: " + theClass.getCanonicalName());
		}

		RuntimeResourceBlockDefinition resourceDef = new RuntimeResourceBlockDefinition(resourceName, theClass, isStandardType(theClass));
		myClassToElementDefinitions.put(theClass, resourceDef);

		scanCompositeElementForChildren(theClass, resourceDef);
	}

	private void scanCompositeDatatype(Class<? extends ICompositeType> theClass, DatatypeDef theDatatypeDefinition) {
		ourLog.debug("Scanning datatype class: {}", theClass.getName());

		RuntimeCompositeDatatypeDefinition resourceDef;
		if (theClass.equals(ExtensionDt.class)) {
			resourceDef = new RuntimeExtensionDtDefinition(theDatatypeDefinition, theClass, true);
		} else {
			resourceDef = new RuntimeCompositeDatatypeDefinition(theDatatypeDefinition, theClass, isStandardType(theClass));
		}
		myClassToElementDefinitions.put(theClass, resourceDef);
		myNameToElementDefinitions.put(resourceDef.getName(), resourceDef);
		scanCompositeElementForChildren(theClass, resourceDef);
	}

	@SuppressWarnings("unchecked")
	private void scanCompositeElementForChildren(Class<? extends IBase> theClass, BaseRuntimeElementCompositeDefinition<?> theDefinition) {
		Set<String> elementNames = new HashSet<String>();
		TreeMap<Integer, BaseRuntimeDeclaredChildDefinition> orderToElementDef = new TreeMap<Integer, BaseRuntimeDeclaredChildDefinition>();
		TreeMap<Integer, BaseRuntimeDeclaredChildDefinition> orderToExtensionDef = new TreeMap<Integer, BaseRuntimeDeclaredChildDefinition>();

		LinkedList<Class<? extends IBase>> classes = new LinkedList<Class<? extends IBase>>();

		/*
		 * We scan classes for annotated fields in the class but also all of its superclasses
		 */
		Class<? extends IBase> current = theClass;
		do {
			classes.push(current);
			if (IBase.class.isAssignableFrom(current.getSuperclass())) {
				current = (Class<? extends IBase>) current.getSuperclass();
			} else {
				current = null;
			}
		} while (current != null);

		for (Class<? extends IBase> next : classes) {
			scanCompositeElementForChildren(next, elementNames, orderToElementDef, orderToExtensionDef);
		}

		// while (orderToElementDef.size() > 0 && orderToElementDef.firstKey() <
		// 0) {
		// BaseRuntimeDeclaredChildDefinition elementDef =
		// orderToElementDef.remove(orderToElementDef.firstKey());
		// if (elementDef.getElementName().equals("identifier")) {
		// orderToElementDef.put(theIdentifierOrder, elementDef);
		// } else {
		// throw new ConfigurationException("Don't know how to handle element: "
		// + elementDef.getElementName());
		// }
		// }

		TreeSet<Integer> orders = new TreeSet<Integer>();
		orders.addAll(orderToElementDef.keySet());
		orders.addAll(orderToExtensionDef.keySet());

		for (Integer i : orders) {
			BaseRuntimeChildDefinition nextChild = orderToElementDef.get(i);
			if (nextChild != null) {
				theDefinition.addChild(nextChild);
			}
			BaseRuntimeDeclaredChildDefinition nextExt = orderToExtensionDef.get(i);
			if (nextExt != null) {
				theDefinition.addExtension((RuntimeChildDeclaredExtensionDefinition) nextExt);
			}
		}

	}

	@SuppressWarnings("unchecked")
	private void scanCompositeElementForChildren(Class<? extends IBase> theClass, Set<String> elementNames, TreeMap<Integer, BaseRuntimeDeclaredChildDefinition> theOrderToElementDef, TreeMap<Integer, BaseRuntimeDeclaredChildDefinition> theOrderToExtensionDef) {
		int baseElementOrder = theOrderToElementDef.isEmpty() ? 0 : theOrderToElementDef.lastEntry().getKey() + 1;

		for (Field next : theClass.getDeclaredFields()) {

			if (Modifier.isFinal(next.getModifiers())) {
				ourLog.trace("Ignoring constant {} on target type {}", next.getName(), theClass);
				continue;
			}

			Child childAnnotation = pullAnnotation(theClass, next, Child.class);
			if (childAnnotation == null) {
				ourLog.trace("Ignoring non @Child field {} on target type {}", next.getName(), theClass);
				continue;
			}

			Description descriptionAnnotation = pullAnnotation(theClass, next, Description.class);

			TreeMap<Integer, BaseRuntimeDeclaredChildDefinition> orderMap = theOrderToElementDef;
			Extension extensionAttr = pullAnnotation(theClass, next, Extension.class);
			if (extensionAttr != null) {
				orderMap = theOrderToExtensionDef;
			}

			String elementName = childAnnotation.name();
			int order = childAnnotation.order();
			if (order == Child.REPLACE_PARENT) {

				if (extensionAttr != null) {

					for (Entry<Integer, BaseRuntimeDeclaredChildDefinition> nextEntry : orderMap.entrySet()) {
						BaseRuntimeDeclaredChildDefinition nextDef = nextEntry.getValue();
						if (nextDef instanceof RuntimeChildDeclaredExtensionDefinition) {
							if (nextDef.getExtensionUrl().equals(extensionAttr.url())) {
								order = nextEntry.getKey();
								orderMap.remove(nextEntry.getKey());
								elementNames.remove(elementName);
								break;
							}
						}
					}
					if (order == Child.REPLACE_PARENT) {
						throw new ConfigurationException("Field " + next.getName() + "' on target type " + theClass.getSimpleName() + " has order() of REPLACE_PARENT (" + Child.REPLACE_PARENT + ") but no parent element with extension URL " + extensionAttr.url() + " could be found on type "
								+ next.getDeclaringClass().getSimpleName());
					}

				} else {

					for (Entry<Integer, BaseRuntimeDeclaredChildDefinition> nextEntry : orderMap.entrySet()) {
						BaseRuntimeDeclaredChildDefinition nextDef = nextEntry.getValue();
						if (elementName.equals(nextDef.getElementName())) {
							order = nextEntry.getKey();
							orderMap.remove(nextEntry.getKey());
							elementNames.remove(elementName);
							break;
						}
					}
					if (order == Child.REPLACE_PARENT) {
						throw new ConfigurationException("Field " + next.getName() + "' on target type " + theClass.getSimpleName() + " has order() of REPLACE_PARENT (" + Child.REPLACE_PARENT + ") but no parent element with name " + elementName + " could be found on type "
								+ next.getDeclaringClass().getSimpleName());
					}

				}

			}
			if (order < 0 && order != Child.ORDER_UNKNOWN) {
				throw new ConfigurationException("Invalid order '" + order + "' on @Child for field '" + next.getName() + "' on target type: " + theClass);
			}
			if (order != Child.ORDER_UNKNOWN) {
				order = order + baseElementOrder;
			}
			// int min = childAnnotation.min();
			// int max = childAnnotation.max();

			/*
			 * Anything that's marked as unknown is given a new ID that is <0 so that it doesn't conflict with any given
			 * IDs and can be figured out later
			 */
			if (order == Child.ORDER_UNKNOWN) {
				order = Integer.MIN_VALUE;
				while (orderMap.containsKey(order)) {
					order++;
				}
			}

			List<Class<? extends IBase>> choiceTypes = new ArrayList<Class<? extends IBase>>();
			for (Class<? extends IBase> nextChoiceType : childAnnotation.type()) {
				choiceTypes.add(nextChoiceType);
			}

			if (orderMap.containsKey(order)) {
				throw new ConfigurationException("Detected duplicate field order '" + childAnnotation.order() + "' for element named '" + elementName + "' in type '" + theClass.getCanonicalName() + "'");
			}

			if (elementNames.contains(elementName)) {
				throw new ConfigurationException("Detected duplicate field name '" + elementName + "' in type '" + theClass.getCanonicalName() + "'");
			}

			Class<?> nextElementType = determineElementType(next);

			if (BaseContainedDt.class.isAssignableFrom(nextElementType) || (childAnnotation.name().equals("contained") && IBaseResource.class.isAssignableFrom(nextElementType))) {
				/*
				 * Child is contained resources
				 */
				RuntimeChildContainedResources def = new RuntimeChildContainedResources(next, childAnnotation, descriptionAnnotation, elementName);
				orderMap.put(order, def);

			} else if (IAnyResource.class.isAssignableFrom(nextElementType) || IResource.class.equals(nextElementType)) {
				/*
				 * Child is a resource as a direct child, as in Bundle.entry.resource
				 */
				RuntimeChildDirectResource def = new RuntimeChildDirectResource(next, childAnnotation, descriptionAnnotation, elementName);
				orderMap.put(order, def);

			} else if (choiceTypes.size() > 1 && !BaseResourceReferenceDt.class.isAssignableFrom(nextElementType) && !IBaseReference.class.isAssignableFrom(nextElementType)) {
				/*
				 * Child is a choice element
				 */
				for (Class<? extends IBase> nextType : choiceTypes) {
					addScanAlso(nextType);
				}
				RuntimeChildChoiceDefinition def = new RuntimeChildChoiceDefinition(next, elementName, childAnnotation, descriptionAnnotation, choiceTypes);
				orderMap.put(order, def);

			} else if (next.getType().equals(ExtensionDt.class)) {

				RuntimeChildExtensionDt def = new RuntimeChildExtensionDt(next, elementName, childAnnotation, descriptionAnnotation);
				orderMap.put(order, def);
				if (IElement.class.isAssignableFrom(nextElementType)) {
					addScanAlso((Class<? extends IElement>) nextElementType);
				}

			} else if (extensionAttr != null) {
				/*
				 * Child is an extension
				 */
				Class<? extends IBase> et = (Class<? extends IBase>) nextElementType;

				Object binder = null;
				if (BoundCodeDt.class.isAssignableFrom(nextElementType) || IBoundCodeableConcept.class.isAssignableFrom(nextElementType)) {
					binder = getBoundCodeBinder(next);
				}

				RuntimeChildDeclaredExtensionDefinition def = new RuntimeChildDeclaredExtensionDefinition(next, childAnnotation, descriptionAnnotation, extensionAttr, elementName, extensionAttr.url(), et, binder);

				if (IBaseEnumeration.class.isAssignableFrom(nextElementType)) {
					def.setEnumerationType(ReflectionUtil.getGenericCollectionTypeOfFieldWithSecondOrderForList(next));
				}

				orderMap.put(order, def);
				if (IBase.class.isAssignableFrom(nextElementType)) {
					addScanAlso((Class<? extends IBase>) nextElementType);
				}
			} else if (BaseResourceReferenceDt.class.isAssignableFrom(nextElementType) || IBaseReference.class.isAssignableFrom(nextElementType)) {
				/*
				 * Child is a resource reference
				 */
				List<Class<? extends IBaseResource>> refTypesList = new ArrayList<Class<? extends IBaseResource>>();
				for (Class<? extends IElement> nextType : childAnnotation.type()) {
					if (IBaseResource.class.isAssignableFrom(nextType) == false) {
						throw new ConfigurationException("Field '" + next.getName() + "' in class '" + next.getDeclaringClass().getCanonicalName() + "' is of type " + BaseResourceReferenceDt.class + " but contains a non-resource type: " + nextType.getCanonicalName());
					}
					refTypesList.add((Class<? extends IBaseResource>) nextType);
					addScanAlso(nextType);
				}
				RuntimeChildResourceDefinition def = new RuntimeChildResourceDefinition(next, elementName, childAnnotation, descriptionAnnotation, refTypesList);
				orderMap.put(order, def);

			} else if (IResourceBlock.class.isAssignableFrom(nextElementType) || IBaseBackboneElement.class.isAssignableFrom(nextElementType) || IBaseDatatypeElement.class.isAssignableFrom(nextElementType)) {
				/*
				 * Child is a resource block (i.e. a sub-tag within a resource) TODO: do these have a better name
				 * according to HL7?
				 */

				Class<? extends IBase> blockDef = (Class<? extends IBase>) nextElementType;
				addScanAlso(blockDef);
				RuntimeChildResourceBlockDefinition def = new RuntimeChildResourceBlockDefinition(next, childAnnotation, descriptionAnnotation, elementName, blockDef);
				orderMap.put(order, def);

			} else if (IDatatype.class.equals(nextElementType) || IElement.class.equals(nextElementType) || "org.hl7.fhir.instance.model.Type".equals(nextElementType.getName()) || IBaseDatatype.class.equals(nextElementType)) {

				RuntimeChildAny def = new RuntimeChildAny(next, elementName, childAnnotation, descriptionAnnotation);
				orderMap.put(order, def);

			} else if (IDatatype.class.isAssignableFrom(nextElementType) || IPrimitiveType.class.isAssignableFrom(nextElementType) || ICompositeType.class.isAssignableFrom(nextElementType) || IBaseDatatype.class.isAssignableFrom(nextElementType)
					|| IBaseExtension.class.isAssignableFrom(nextElementType)) {
				Class<? extends IBase> nextDatatype = (Class<? extends IBase>) nextElementType;

				addScanAlso(nextDatatype);
				BaseRuntimeChildDatatypeDefinition def;
				if (IPrimitiveType.class.isAssignableFrom(nextElementType)) {
					if (nextElementType.equals(BoundCodeDt.class)) {
						IValueSetEnumBinder<Enum<?>> binder = getBoundCodeBinder(next);
						def = new RuntimeChildPrimitiveBoundCodeDatatypeDefinition(next, elementName, childAnnotation, descriptionAnnotation, nextDatatype, binder);
					} else if (IBaseEnumeration.class.isAssignableFrom(nextElementType)) {
						Class<?> binderType = ReflectionUtil.getGenericCollectionTypeOfFieldWithSecondOrderForList(next);
						def = new RuntimeChildPrimitiveEnumerationDatatypeDefinition(next, elementName, childAnnotation, descriptionAnnotation, nextDatatype, binderType);
					} else if (childAnnotation.enumFactory().getSimpleName().equals("NoEnumFactory") == false) {
						Class<? extends IBaseEnumFactory<?>> enumFactory = childAnnotation.enumFactory();
						def = new RuntimeChildEnumerationDatatypeDefinition(next, elementName, childAnnotation, descriptionAnnotation, nextDatatype, enumFactory);
						// } else if ("id".equals(elementName) && IIdType.class.isAssignableFrom(nextDatatype)) {
						// def = new RuntimeChildIdDatatypeDefinition(next, elementName, descriptionAnnotation,
						// childAnnotation, nextDatatype);
					} else {
						def = new RuntimeChildPrimitiveDatatypeDefinition(next, elementName, descriptionAnnotation, childAnnotation, nextDatatype);
					}
				} else if (IBaseXhtml.class.isAssignableFrom(nextElementType)) {
					def = new RuntimeChildXhtmlDatatypeDefinition(next, elementName, descriptionAnnotation, childAnnotation, nextDatatype);
				} else {
					if (IBoundCodeableConcept.class.isAssignableFrom(nextElementType)) {
						IValueSetEnumBinder<Enum<?>> binder = getBoundCodeBinder(next);
						def = new RuntimeChildCompositeBoundDatatypeDefinition(next, elementName, childAnnotation, descriptionAnnotation, nextDatatype, binder);
					} else if (BaseNarrativeDt.class.isAssignableFrom(nextElementType) || INarrative.class.isAssignableFrom(nextElementType)) {
						def = new RuntimeChildNarrativeDefinition(next, elementName, childAnnotation, descriptionAnnotation, nextDatatype);
					} else {
						def = new RuntimeChildCompositeDatatypeDefinition(next, elementName, childAnnotation, descriptionAnnotation, nextDatatype);
					}
				}

				CodeableConceptElement concept = pullAnnotation(theClass, next, CodeableConceptElement.class);
				if (concept != null) {
					if (!ICodedDatatype.class.isAssignableFrom(nextDatatype)) {
						throw new ConfigurationException("Field '" + elementName + "' in type '" + theClass.getCanonicalName() + "' is marked as @" + CodeableConceptElement.class.getCanonicalName() + " but type is not a subtype of " + ICodedDatatype.class.getName());
					} else {
						Class<? extends ICodeEnum> type = concept.type();
						myScanAlsoCodeTable.add(type);
						def.setCodeType(type);
					}
				}

				orderMap.put(order, def);

			} else {
				throw new ConfigurationException("Field '" + elementName + "' in type '" + theClass.getCanonicalName() + "' is not a valid child type: " + nextElementType);
			}

			elementNames.add(elementName);
		}
	}

	private String scanPrimitiveDatatype(Class<? extends IPrimitiveType<?>> theClass, DatatypeDef theDatatypeDefinition) {
		ourLog.debug("Scanning resource class: {}", theClass.getName());

		String resourceName = theDatatypeDefinition.name();
		if (isBlank(resourceName)) {
			throw new ConfigurationException("Resource type @" + ResourceDef.class.getSimpleName() + " annotation contains no resource name: " + theClass.getCanonicalName());
		}

		BaseRuntimeElementDefinition<?> resourceDef;
		if (theClass.equals(XhtmlDt.class)) {
			@SuppressWarnings("unchecked")
			Class<XhtmlDt> clazz = (Class<XhtmlDt>) theClass;
			resourceDef = new RuntimePrimitiveDatatypeNarrativeDefinition(resourceName, clazz, isStandardType(clazz));
		} else if (IBaseXhtml.class.isAssignableFrom(theClass)) {
			@SuppressWarnings("unchecked")
			Class<? extends IBaseXhtml> clazz = (Class<? extends IBaseXhtml>) theClass;
			resourceDef = new RuntimePrimitiveDatatypeXhtmlHl7OrgDefinition(resourceName, clazz, isStandardType(clazz));
		} else if (IIdType.class.isAssignableFrom(theClass)) {
			resourceDef = new RuntimeIdDatatypeDefinition(theDatatypeDefinition, theClass, isStandardType(theClass));
		} else {
			resourceDef = new RuntimePrimitiveDatatypeDefinition(theDatatypeDefinition, theClass, isStandardType(theClass));
		}
		myClassToElementDefinitions.put(theClass, resourceDef);
		if (!theDatatypeDefinition.isSpecialization()) {
			myNameToElementDefinitions.put(resourceName, resourceDef);
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
				ResourceDef nextDef = pullAnnotation(theClass, parent, ResourceDef.class);
				if (nextDef != null) {
					resourceName = nextDef.name();
				}
				parent = parent.getSuperclass();
			}
			if (isBlank(resourceName)) {
				throw new ConfigurationException("Resource type @" + ResourceDef.class.getSimpleName() + " annotation contains no resource name(): " + theClass.getCanonicalName() + " - This is only allowed for types that extend other resource types ");
			}
		}

		String resourceId = resourceDefinition.id();
		if (!isBlank(resourceId)) {
			if (myIdToResourceDefinition.containsKey(resourceId)) {
				throw new ConfigurationException("The following resource types have the same ID of '" + resourceId + "' - " + theClass.getCanonicalName() + " and " + myIdToResourceDefinition.get(resourceId).getImplementingClass().getCanonicalName());
			}
		}

		RuntimeResourceDefinition resourceDef = new RuntimeResourceDefinition(myContext, resourceName, theClass, resourceDefinition, isStandardType(theClass));
		myClassToElementDefinitions.put(theClass, resourceDef);
		if (primaryNameProvider) {
			if (resourceDef.getStructureVersion() == myVersion) {
				myNameToResourceDefinitions.put(resourceName, resourceDef);
			}
		}
		scanCompositeElementForChildren(theClass, resourceDef);

		myIdToResourceDefinition.put(resourceId, resourceDef);

		scanResourceForSearchParams(theClass, resourceDef);

		return resourceName;
	}

	private void scanResourceForSearchParams(Class<? extends IBaseResource> theClass, RuntimeResourceDefinition theResourceDef) {

		Map<String, RuntimeSearchParam> nameToParam = new HashMap<String, RuntimeSearchParam>();
		Map<Field, SearchParamDefinition> compositeFields = new LinkedHashMap<Field, SearchParamDefinition>();

		for (Field nextField : theClass.getFields()) {
			SearchParamDefinition searchParam = pullAnnotation(theClass, nextField, SearchParamDefinition.class);
			if (searchParam != null) {
				RestSearchParameterTypeEnum paramType = RestSearchParameterTypeEnum.valueOf(searchParam.type().toUpperCase());
				if (paramType == null) {
					throw new ConfigurationException("Search param " + searchParam.name() + " has an invalid type: " + searchParam.type());
				}
				if (paramType == RestSearchParameterTypeEnum.COMPOSITE) {
					compositeFields.put(nextField, searchParam);
					continue;
				}
				RuntimeSearchParam param = new RuntimeSearchParam(searchParam.name(), searchParam.description(), searchParam.path(), paramType);
				theResourceDef.addSearchParam(param);
				nameToParam.put(param.getName(), param);
			}
		}

		for (Entry<Field, SearchParamDefinition> nextEntry : compositeFields.entrySet()) {
			SearchParamDefinition searchParam = nextEntry.getValue();

			List<RuntimeSearchParam> compositeOf = new ArrayList<RuntimeSearchParam>();
			for (String nextName : searchParam.compositeOf()) {
				RuntimeSearchParam param = nameToParam.get(nextName);
				if (param == null) {
					ourLog.warn("Search parameter {}.{} declares that it is a composite with compositeOf value '{}' but that is not a valid parametr name itself. Valid values are: {}", new Object[] { theResourceDef.getName(), searchParam.name(), nextName, nameToParam.keySet() });
					continue;
				}
				compositeOf.add(param);
			}

			RuntimeSearchParam param = new RuntimeSearchParam(searchParam.name(), searchParam.description(), searchParam.path(), RestSearchParameterTypeEnum.COMPOSITE, compositeOf);
			theResourceDef.addSearchParam(param);
		}
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

	static Set<Class<? extends IBase>> scanVersionPropertyFile(Set<Class<? extends IBase>> theDatatypes, Map<String, Class<? extends IBaseResource>> theResourceTypes, FhirVersionEnum version) {
		Set<Class<? extends IBase>> retVal = new HashSet<Class<? extends IBase>>();

		InputStream str = version.getVersionImplementation().getFhirVersionPropertiesFile();
		Properties prop = new Properties();
		try {
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
							throw new ConfigurationException("Unknown class[" + nextValue + "] for data type definition: " + nextKey.substring("datatype.".length()), e);
						}
					}
				} else if (nextKey.startsWith("resource.")) {
					// Resources
					String resName = nextKey.substring("resource.".length()).toLowerCase();
					try {
						@SuppressWarnings("unchecked")
						Class<? extends IBaseResource> nextClass = (Class<? extends IBaseResource>) Class.forName(nextValue);
						if (!IBaseResource.class.isAssignableFrom(nextClass)) {
							throw new ConfigurationException("Class is not assignable from " + IBaseResource.class.getSimpleName() + ": " + nextValue);
						}

						theResourceTypes.put(resName, nextClass);
					} catch (ClassNotFoundException e) {
						throw new ConfigurationException("Unknown class[" + nextValue + "] for resource definition: " + nextKey.substring("resource.".length()), e);
					}
				} else {
					throw new ConfigurationException("Unexpected property in version property file: " + nextKey + "=" + nextValue);
				}
			}
		} catch (IOException e) {
			throw new ConfigurationException("Failed to load model property file from classpath: " + "/ca/uhn/fhir/model/dstu/model.properties");
		}

		return retVal;
	}

}
