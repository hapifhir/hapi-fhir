package ca.uhn.fhir.context;

/*-
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.IBoundCodeableConcept;
import ca.uhn.fhir.model.api.IDatatype;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.IResourceBlock;
import ca.uhn.fhir.model.api.IValueSetEnumBinder;
import ca.uhn.fhir.model.api.annotation.Binding;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.base.composite.BaseContainedDt;
import ca.uhn.fhir.model.base.composite.BaseNarrativeDt;
import ca.uhn.fhir.model.base.composite.BaseResourceReferenceDt;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.util.ReflectionUtil;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBackboneElement;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseDatatypeElement;
import org.hl7.fhir.instance.model.api.IBaseEnumeration;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.ICompositeType;
import org.hl7.fhir.instance.model.api.INarrative;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public abstract class BaseRuntimeElementCompositeDefinition<T extends IBase> extends BaseRuntimeElementDefinition<T> {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseRuntimeElementCompositeDefinition.class);
	private final FhirContext myContext;
	private Map<String, Integer> forcedOrder = null;
	private List<BaseRuntimeChildDefinition> myChildren = new ArrayList<>();
	private List<BaseRuntimeChildDefinition> myChildrenAndExtensions;
	private Map<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> myClassToElementDefinitions;
	private Map<String, BaseRuntimeChildDefinition> myNameToChild = new HashMap<>();
	private List<ScannedField> myScannedFields = new ArrayList<>();
	private volatile SealingStateEnum mySealed = SealingStateEnum.NOT_SEALED;

	@SuppressWarnings("unchecked")
	public BaseRuntimeElementCompositeDefinition(String theName, Class<? extends T> theImplementingClass, boolean theStandardType, FhirContext theContext, Map<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> theClassToElementDefinitions) {
		super(theName, theImplementingClass, theStandardType);

		myContext = theContext;
		myClassToElementDefinitions = theClassToElementDefinitions;

		/*
		 * We scan classes for annotated fields in the class but also all of its superclasses
		 */
		Class<? extends IBase> current = theImplementingClass;
		LinkedList<Class<? extends IBase>> classes = new LinkedList<>();
		do {
			if (forcedOrder == null) {
				ChildOrder childOrder = current.getAnnotation(ChildOrder.class);
				if (childOrder != null) {
					forcedOrder = new HashMap<>();
					for (int i = 0; i < childOrder.names().length; i++) {
						String nextName = childOrder.names()[i];
						if (nextName.endsWith("[x]")) {
							nextName = nextName.substring(0, nextName.length() - 3);
						}
						forcedOrder.put(nextName, i);
					}
				}
			}
			classes.push(current);
			if (IBase.class.isAssignableFrom(current.getSuperclass())) {
				current = (Class<? extends IBase>) current.getSuperclass();
			} else {
				current = null;
			}
		} while (current != null);

		Set<Field> fields = new HashSet<>();
		for (Class<? extends IBase> nextClass : classes) {
			int fieldIndexInClass = 0;
			for (Field next : nextClass.getDeclaredFields()) {
				if (fields.add(next)) {
					ScannedField scannedField = new ScannedField(next, theImplementingClass, fieldIndexInClass == 0);
					if (scannedField.getChildAnnotation() != null) {
						myScannedFields.add(scannedField);
						fieldIndexInClass++;
					}
				}
			}
		}

	}

	void addChild(BaseRuntimeChildDefinition theNext) {
		if (theNext == null) {
			throw new NullPointerException(Msg.code(1698));
		}
		if (theNext.getExtensionUrl() != null) {
			throw new IllegalArgumentException(Msg.code(1699) + "Shouldn't haven an extension URL, use addExtension instead");
		}
		myChildren.add(theNext);
	}

	@Override
	public BaseRuntimeChildDefinition getChildByName(String theName) {
		validateSealed();
		return myNameToChild.get(theName);
	}

	public BaseRuntimeChildDefinition getChildByNameOrThrowDataFormatException(String theName) throws DataFormatException {
		validateSealed();
		BaseRuntimeChildDefinition retVal = myNameToChild.get(theName);
		if (retVal == null) {
			throw new DataFormatException(Msg.code(1700) + "Unknown child name '" + theName + "' in element " + getName() + " - Valid names are: " + new TreeSet<String>(myNameToChild.keySet()));
		}
		return retVal;
	}

	@Override
	public List<BaseRuntimeChildDefinition> getChildren() {
		validateSealed();
		return myChildren;
	}


	public List<BaseRuntimeChildDefinition> getChildrenAndExtension() {
		validateSealed();
		return myChildrenAndExtensions;
	}


	/**
	 * Has this class been sealed
	 */
	public boolean isSealed() {
		return mySealed == SealingStateEnum.SEALED;
	}

	@SuppressWarnings("unchecked")
	void populateScanAlso(Set<Class<? extends IBase>> theScanAlso) {
		for (ScannedField next : myScannedFields) {
			if (IBase.class.isAssignableFrom(next.getElementType())) {
				if (next.getElementType().isInterface() == false && Modifier.isAbstract(next.getElementType().getModifiers()) == false) {
					theScanAlso.add((Class<? extends IBase>) next.getElementType());
				}
			}
			for (Class<? extends IBase> nextChildType : next.getChoiceTypes()) {
				if (nextChildType.isInterface() == false && Modifier.isAbstract(nextChildType.getModifiers()) == false) {
					theScanAlso.add(nextChildType);
				}
			}
		}
	}

	private void scanCompositeElementForChildren() {
		Set<String> elementNames = new HashSet<>();
		TreeMap<Integer, BaseRuntimeDeclaredChildDefinition> orderToElementDef = new TreeMap<>();
		TreeMap<Integer, BaseRuntimeDeclaredChildDefinition> orderToExtensionDef = new TreeMap<>();

		scanCompositeElementForChildren(elementNames, orderToElementDef, orderToExtensionDef);

		if (forcedOrder != null) {
			/*
			 * Find out how many elements don't match any entry in the list
			 * for forced order. Those elements come first.
			 */
			TreeMap<Integer, BaseRuntimeDeclaredChildDefinition> newOrderToExtensionDef = new TreeMap<>();
			int unknownCount = 0;
			for (BaseRuntimeDeclaredChildDefinition nextEntry : orderToElementDef.values()) {
				if (!forcedOrder.containsKey(nextEntry.getElementName())) {
					newOrderToExtensionDef.put(unknownCount, nextEntry);
					unknownCount++;
				}
			}
			for (BaseRuntimeDeclaredChildDefinition nextEntry : orderToElementDef.values()) {
				if (forcedOrder.containsKey(nextEntry.getElementName())) {
					Integer newOrder = forcedOrder.get(nextEntry.getElementName());
					newOrderToExtensionDef.put(newOrder + unknownCount, nextEntry);
				}
			}
			orderToElementDef = newOrderToExtensionDef;
		}

		TreeSet<Integer> orders = new TreeSet<>();
		orders.addAll(orderToElementDef.keySet());
		orders.addAll(orderToExtensionDef.keySet());

		for (Integer i : orders) {
			BaseRuntimeChildDefinition nextChild = orderToElementDef.get(i);
			if (nextChild != null) {
				this.addChild(nextChild);
			}
			BaseRuntimeDeclaredChildDefinition nextExt = orderToExtensionDef.get(i);
			if (nextExt != null) {
				this.addExtension((RuntimeChildDeclaredExtensionDefinition) nextExt);
			}
		}

	}

	@SuppressWarnings("unchecked")
	private void scanCompositeElementForChildren(Set<String> elementNames, TreeMap<Integer, BaseRuntimeDeclaredChildDefinition> theOrderToElementDef,
																TreeMap<Integer, BaseRuntimeDeclaredChildDefinition> theOrderToExtensionDef) {
		int baseElementOrder = 0;

		for (ScannedField next : myScannedFields) {
			if (next.isFirstFieldInNewClass()) {
				baseElementOrder = theOrderToElementDef.isEmpty() ? 0 : theOrderToElementDef.lastEntry().getKey() + 1;
			}

			Class<?> declaringClass = next.getField().getDeclaringClass();

			Description descriptionAnnotation = ModelScanner.pullAnnotation(next.getField(), Description.class);

			TreeMap<Integer, BaseRuntimeDeclaredChildDefinition> orderMap = theOrderToElementDef;
			Extension extensionAttr = ModelScanner.pullAnnotation(next.getField(), Extension.class);
			if (extensionAttr != null) {
				orderMap = theOrderToExtensionDef;
			}

			Child childAnnotation = next.getChildAnnotation();
			Field nextField = next.getField();
			String elementName = childAnnotation.name();
			int order = childAnnotation.order();
			boolean childIsChoiceType = false;
			boolean orderIsReplaceParent = false;
			BaseRuntimeChildDefinition replacedParent = null;

			if (order == Child.REPLACE_PARENT) {

				if (extensionAttr != null) {

					for (Entry<Integer, BaseRuntimeDeclaredChildDefinition> nextEntry : orderMap.entrySet()) {
						BaseRuntimeDeclaredChildDefinition nextDef = nextEntry.getValue();
						if (nextDef instanceof RuntimeChildDeclaredExtensionDefinition) {
							if (nextDef.getExtensionUrl().equals(extensionAttr.url())) {
								orderIsReplaceParent = true;
								order = nextEntry.getKey();
								replacedParent = orderMap.remove(nextEntry.getKey());
								elementNames.remove(elementName);
								break;
							}
						}
					}
					if (order == Child.REPLACE_PARENT) {
						throw new ConfigurationException(Msg.code(1701) + "Field " + nextField.getName() + "' on target type " + declaringClass.getSimpleName() + " has order() of REPLACE_PARENT (" + Child.REPLACE_PARENT
							+ ") but no parent element with extension URL " + extensionAttr.url() + " could be found on type " + nextField.getDeclaringClass().getSimpleName());
					}

				} else {

					for (Entry<Integer, BaseRuntimeDeclaredChildDefinition> nextEntry : orderMap.entrySet()) {
						BaseRuntimeDeclaredChildDefinition nextDef = nextEntry.getValue();
						if (elementName.equals(nextDef.getElementName())) {
							orderIsReplaceParent = true;
							order = nextEntry.getKey();
							BaseRuntimeDeclaredChildDefinition existing = orderMap.remove(nextEntry.getKey());
							replacedParent = existing;
							elementNames.remove(elementName);

							/*
							 * See #350 - If the original field (in the superclass) with the given name is a choice, then we need to make sure
							 * that the field which replaces is a choice even if it's only a choice of one type - this is because the
							 * element name when serialized still needs to reflect the datatype
							 */
							if (existing instanceof RuntimeChildChoiceDefinition) {
								childIsChoiceType = true;
							}
							break;
						}
					}
					if (order == Child.REPLACE_PARENT) {
						throw new ConfigurationException(Msg.code(1702) + "Field " + nextField.getName() + "' on target type " + declaringClass.getSimpleName() + " has order() of REPLACE_PARENT (" + Child.REPLACE_PARENT
							+ ") but no parent element with name " + elementName + " could be found on type " + nextField.getDeclaringClass().getSimpleName());
					}

				}

			}

			if (order < 0 && order != Child.ORDER_UNKNOWN) {
				throw new ConfigurationException(Msg.code(1703) + "Invalid order '" + order + "' on @Child for field '" + nextField.getName() + "' on target type: " + declaringClass);
			}

			if (order != Child.ORDER_UNKNOWN && !orderIsReplaceParent) {
				order = order + baseElementOrder;
			}
			// int min = childAnnotation.min();
			// int max = childAnnotation.max();

			/*
			 * Anything that's marked as unknown is given a new ID that is <0 so that it doesn't conflict with any given IDs and can be figured out later
			 */
			if (order == Child.ORDER_UNKNOWN) {
				order = 0;
				while (orderMap.containsKey(order)) {
					order++;
				}
			}

			List<Class<? extends IBase>> choiceTypes = next.getChoiceTypes();

			if (orderMap.containsKey(order)) {
				throw new ConfigurationException(Msg.code(1704) + "Detected duplicate field order '" + childAnnotation.order() + "' for element named '" + elementName + "' in type '" + declaringClass.getCanonicalName() + "' - Already had: " + orderMap.get(order).getElementName());
			}

			if (elementNames.contains(elementName)) {
				throw new ConfigurationException(Msg.code(1705) + "Detected duplicate field name '" + elementName + "' in type '" + declaringClass.getCanonicalName() + "'");
			}

			Class<?> nextElementType = next.getElementType();

			BaseRuntimeDeclaredChildDefinition def;
			if (childAnnotation.name().equals("extension") && IBaseExtension.class.isAssignableFrom(nextElementType)) {
				def = new RuntimeChildExtension(nextField, childAnnotation.name(), childAnnotation, descriptionAnnotation);
			} else if (childAnnotation.name().equals("modifierExtension") && IBaseExtension.class.isAssignableFrom(nextElementType)) {
				def = new RuntimeChildExtension(nextField, childAnnotation.name(), childAnnotation, descriptionAnnotation);
			} else if (BaseContainedDt.class.isAssignableFrom(nextElementType) || (childAnnotation.name().equals("contained") && IBaseResource.class.isAssignableFrom(nextElementType))) {
				/*
				 * Child is contained resources
				 */
				def = new RuntimeChildContainedResources(nextField, childAnnotation, descriptionAnnotation, elementName);
			} else if (IAnyResource.class.isAssignableFrom(nextElementType) || IResource.class.equals(nextElementType)) {
				/*
				 * Child is a resource as a direct child, as in Bundle.entry.resource
				 */
				def = new RuntimeChildDirectResource(nextField, childAnnotation, descriptionAnnotation, elementName);
			} else {
				childIsChoiceType |= choiceTypes.size() > 1;
				if (extensionAttr == null && childIsChoiceType && !BaseResourceReferenceDt.class.isAssignableFrom(nextElementType) && !IBaseReference.class.isAssignableFrom(nextElementType)) {
					def = new RuntimeChildChoiceDefinition(nextField, elementName, childAnnotation, descriptionAnnotation, choiceTypes);
				} else if (extensionAttr != null) {
					/*
					 * Child is an extension
					 */
					Class<? extends IBase> et = (Class<? extends IBase>) nextElementType;

					Object binder = null;
					if (BoundCodeDt.class.isAssignableFrom(nextElementType) || IBoundCodeableConcept.class.isAssignableFrom(nextElementType)) {
						binder = ModelScanner.getBoundCodeBinder(nextField);
					}

					def = new RuntimeChildDeclaredExtensionDefinition(nextField, childAnnotation, descriptionAnnotation, extensionAttr, elementName, extensionAttr.url(), et, binder);

					if (IBaseEnumeration.class.isAssignableFrom(nextElementType)) {
						((RuntimeChildDeclaredExtensionDefinition) def).setEnumerationType(ReflectionUtil.getGenericCollectionTypeOfFieldWithSecondOrderForList(nextField));
					}
				} else if (BaseResourceReferenceDt.class.isAssignableFrom(nextElementType) || IBaseReference.class.isAssignableFrom(nextElementType)) {
					/*
					 * Child is a resource reference
					 */
					List<Class<? extends IBaseResource>> refTypesList = new ArrayList<>();
					for (Class<? extends IElement> nextType : childAnnotation.type()) {
						if (IBaseReference.class.isAssignableFrom(nextType)) {
							refTypesList.add(myContext.getVersion().getVersion().isRi() ? IAnyResource.class : IResource.class);
							continue;
						} else if (IBaseResource.class.isAssignableFrom(nextType) == false) {
							throw new ConfigurationException(Msg.code(1706) + "Field '" + nextField.getName() + "' in class '" + nextField.getDeclaringClass().getCanonicalName() + "' is of type " + BaseResourceReferenceDt.class + " but contains a non-resource type: " + nextType.getCanonicalName());
						}
						refTypesList.add((Class<? extends IBaseResource>) nextType);
					}
					def = new RuntimeChildResourceDefinition(nextField, elementName, childAnnotation, descriptionAnnotation, refTypesList);

				} else if (IResourceBlock.class.isAssignableFrom(nextElementType) || IBaseBackboneElement.class.isAssignableFrom(nextElementType)
					|| IBaseDatatypeElement.class.isAssignableFrom(nextElementType)) {
					/*
					 * Child is a resource block (i.e. a sub-tag within a resource) TODO: do these have a better name according to HL7?
					 */

					Class<? extends IBase> blockDef = (Class<? extends IBase>) nextElementType;
					def = new RuntimeChildResourceBlockDefinition(myContext, nextField, childAnnotation, descriptionAnnotation, elementName, blockDef);
				} else if (IDatatype.class.equals(nextElementType) || IElement.class.equals(nextElementType) || "Type".equals(nextElementType.getSimpleName())
					|| IBaseDatatype.class.equals(nextElementType)) {

					def = new RuntimeChildAny(nextField, elementName, childAnnotation, descriptionAnnotation);
				} else if (IDatatype.class.isAssignableFrom(nextElementType) || IPrimitiveType.class.isAssignableFrom(nextElementType) || ICompositeType.class.isAssignableFrom(nextElementType)
					|| IBaseDatatype.class.isAssignableFrom(nextElementType) || IBaseExtension.class.isAssignableFrom(nextElementType)) {
					Class<? extends IBase> nextDatatype = (Class<? extends IBase>) nextElementType;

					if (IPrimitiveType.class.isAssignableFrom(nextElementType)) {
						if (nextElementType.equals(BoundCodeDt.class)) {
							IValueSetEnumBinder<Enum<?>> binder = ModelScanner.getBoundCodeBinder(nextField);
							Class<? extends Enum<?>> enumType = ModelScanner.determineEnumTypeForBoundField(nextField);
							def = new RuntimeChildPrimitiveBoundCodeDatatypeDefinition(nextField, elementName, childAnnotation, descriptionAnnotation, nextDatatype, binder, enumType);
						} else if (IBaseEnumeration.class.isAssignableFrom(nextElementType)) {
							Class<? extends Enum<?>> binderType = ModelScanner.determineEnumTypeForBoundField(nextField);
							def = new RuntimeChildPrimitiveEnumerationDatatypeDefinition(nextField, elementName, childAnnotation, descriptionAnnotation, nextDatatype, binderType);
						} else {
							def = new RuntimeChildPrimitiveDatatypeDefinition(nextField, elementName, descriptionAnnotation, childAnnotation, nextDatatype);
						}
					} else {
						if (IBoundCodeableConcept.class.isAssignableFrom(nextElementType)) {
							IValueSetEnumBinder<Enum<?>> binder = ModelScanner.getBoundCodeBinder(nextField);
							Class<? extends Enum<?>> enumType = ModelScanner.determineEnumTypeForBoundField(nextField);
							def = new RuntimeChildCompositeBoundDatatypeDefinition(nextField, elementName, childAnnotation, descriptionAnnotation, nextDatatype, binder, enumType);
						} else if (BaseNarrativeDt.class.isAssignableFrom(nextElementType) || INarrative.class.isAssignableFrom(nextElementType)) {
							def = new RuntimeChildNarrativeDefinition(nextField, elementName, childAnnotation, descriptionAnnotation, nextDatatype);
						} else {
							def = new RuntimeChildCompositeDatatypeDefinition(nextField, elementName, childAnnotation, descriptionAnnotation, nextDatatype);
						}
					}

				} else {
					throw new ConfigurationException(Msg.code(1707) + "Field '" + elementName + "' in type '" + declaringClass.getCanonicalName() + "' is not a valid child type: " + nextElementType);
				}

				Binding bindingAnnotation = ModelScanner.pullAnnotation(nextField, Binding.class);
				if (bindingAnnotation != null) {
					if (isNotBlank(bindingAnnotation.valueSet())) {
						def.setBindingValueSet(bindingAnnotation.valueSet());
					}
				}

			}

			def.setReplacedParentDefinition(replacedParent);
			orderMap.put(order, def);
			elementNames.add(elementName);
		}
	}

	@Override
	void sealAndInitialize(FhirContext theContext, Map<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> theClassToElementDefinitions) {
		if (mySealed == SealingStateEnum.SEALED) {
			return;
		}

		synchronized (myContext) {
			if (mySealed == SealingStateEnum.SEALED || mySealed == SealingStateEnum.SEALING) {
				return;
			}
			mySealed = SealingStateEnum.SEALING;

			scanCompositeElementForChildren();

			super.sealAndInitialize(theContext, theClassToElementDefinitions);

			for (BaseRuntimeChildDefinition next : myChildren) {
				next.sealAndInitialize(theContext, theClassToElementDefinitions);
			}

			myNameToChild = new HashMap<>();
			for (BaseRuntimeChildDefinition next : myChildren) {
				if (next instanceof RuntimeChildChoiceDefinition) {
					String key = next.getElementName() + "[x]";
					myNameToChild.put(key, next);
				}
				for (String nextName : next.getValidChildNames()) {
					if (myNameToChild.containsKey(nextName)) {
						throw new ConfigurationException(Msg.code(1708) + "Duplicate child name[" + nextName + "] in Element[" + getName() + "]");
					}
					myNameToChild.put(nextName, next);
				}
			}

			myChildren = Collections.unmodifiableList(myChildren);
			myNameToChild = Collections.unmodifiableMap(myNameToChild);

			List<BaseRuntimeChildDefinition> children = new ArrayList<>();
			children.addAll(myChildren);

			/*
			 * Because of the way the type hierarchy works for DSTU2 resources,
			 * things end up in the wrong order
			 */
			if (theContext.getVersion().getVersion() == FhirVersionEnum.DSTU2) {
				int extIndex = findIndex(children, "extension", false);
				int containedIndex = findIndex(children, "contained", false);
				if (containedIndex != -1 && extIndex != -1 && extIndex < containedIndex) {
					BaseRuntimeChildDefinition extension = children.remove(extIndex);
					if (containedIndex > children.size()) {
						children.add(extension);
					} else {
						children.add(containedIndex, extension);
					}
					int modIndex = findIndex(children, "modifierExtension", false);
					if (modIndex < containedIndex) {
						extension = children.remove(modIndex);
						if (containedIndex > children.size()) {
							children.add(extension);
						} else {
							children.add(containedIndex, extension);
						}
					}
				}
			}

			/*
			 * Add declared extensions alongside the undeclared ones
			 */
			if (getExtensionsNonModifier().isEmpty() == false) {
				children.addAll(findIndex(children, "extension", true), getExtensionsNonModifier());
			}
			if (getExtensionsModifier().isEmpty() == false) {
				children.addAll(findIndex(children, "modifierExtension", true), getExtensionsModifier());
			}

			myChildrenAndExtensions = Collections.unmodifiableList(children);
			mySealed = SealingStateEnum.SEALED;
		}
	}


	@Override
	protected void validateSealed() {
		if (mySealed != SealingStateEnum.SEALED) {
			synchronized (myContext) {
				if (mySealed == SealingStateEnum.NOT_SEALED) {
					sealAndInitialize(myContext, myClassToElementDefinitions);
				}
			}
		}
	}

	private enum SealingStateEnum {
		NOT_SEALED,
		SEALING,
		SEALED
	}

	private static class ScannedField {
		private Child myChildAnnotation;

		private List<Class<? extends IBase>> myChoiceTypes = new ArrayList<>();
		private Class<?> myElementType;
		private Field myField;
		private boolean myFirstFieldInNewClass;

		ScannedField(Field theField, Class<?> theClass, boolean theFirstFieldInNewClass) {
			myField = theField;
			myFirstFieldInNewClass = theFirstFieldInNewClass;

			Child childAnnotation = ModelScanner.pullAnnotation(theField, Child.class);
			if (childAnnotation == null) {
				ourLog.trace("Ignoring non @Child field {} on target type {}", theField.getName(), theClass);
				return;
			}
			if (Modifier.isFinal(theField.getModifiers())) {
				ourLog.trace("Ignoring constant {} on target type {}", theField.getName(), theClass);
				return;
			}

			myChildAnnotation = childAnnotation;
			myElementType = ModelScanner.determineElementType(theField);

			Collections.addAll(myChoiceTypes, childAnnotation.type());
		}

		public Child getChildAnnotation() {
			return myChildAnnotation;
		}

		public List<Class<? extends IBase>> getChoiceTypes() {
			return myChoiceTypes;
		}

		public Class<?> getElementType() {
			return myElementType;
		}

		public Field getField() {
			return myField;
		}

		public boolean isFirstFieldInNewClass() {
			return myFirstFieldInNewClass;
		}

		@Override
		public String toString() {
			return myField.getName();
		}
	}

	private static int findIndex(List<BaseRuntimeChildDefinition> theChildren, String theName, boolean theDefaultAtEnd) {
		int index = theDefaultAtEnd ? theChildren.size() : -1;
		for (ListIterator<BaseRuntimeChildDefinition> iter = theChildren.listIterator(); iter.hasNext(); ) {
			if (iter.next().getElementName().equals(theName)) {
				index = iter.previousIndex();
				break;
			}
		}
		return index;
	}

}
