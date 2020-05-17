package ca.uhn.fhir.util;

import ca.uhn.fhir.context.*;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition.ChildTypeEnum;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ISupportsUndeclaredExtensions;
import ca.uhn.fhir.model.base.composite.BaseContainedDt;
import ca.uhn.fhir.model.base.composite.BaseResourceReferenceDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.parser.DataFormatException;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.*;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

public class FhirTerser {

	private static final Pattern COMPARTMENT_MATCHER_PATH = Pattern.compile("([a-zA-Z.]+)\\.where\\(resolve\\(\\) is ([a-zA-Z]+)\\)");
	private FhirContext myContext;

	public FhirTerser(FhirContext theContext) {
		super();
		myContext = theContext;
	}

	private List<String> addNameToList(List<String> theCurrentList, BaseRuntimeChildDefinition theChildDefinition) {
		if (theChildDefinition == null)
			return null;
		if (theCurrentList == null || theCurrentList.isEmpty())
			return new ArrayList<>(Collections.singletonList(theChildDefinition.getElementName()));
		List<String> newList = new ArrayList<>(theCurrentList);
		newList.add(theChildDefinition.getElementName());
		return newList;
	}

	private ExtensionDt createEmptyExtensionDt(IBaseExtension theBaseExtension, String theUrl) {
		return createEmptyExtensionDt(theBaseExtension, false, theUrl);
	}

	@SuppressWarnings("unchecked")
	private ExtensionDt createEmptyExtensionDt(IBaseExtension theBaseExtension, boolean theIsModifier, String theUrl) {
		ExtensionDt retVal = new ExtensionDt(theIsModifier, theUrl);
		theBaseExtension.getExtension().add(retVal);
		return retVal;
	}

	private ExtensionDt createEmptyExtensionDt(ISupportsUndeclaredExtensions theSupportsUndeclaredExtensions, String theUrl) {
		return createEmptyExtensionDt(theSupportsUndeclaredExtensions, false, theUrl);
	}

	private ExtensionDt createEmptyExtensionDt(ISupportsUndeclaredExtensions theSupportsUndeclaredExtensions, boolean theIsModifier, String theUrl) {
		return theSupportsUndeclaredExtensions.addUndeclaredExtension(theIsModifier, theUrl);
	}

	private IBaseExtension createEmptyExtension(IBaseHasExtensions theBaseHasExtensions, String theUrl) {
		return (IBaseExtension) theBaseHasExtensions.addExtension().setUrl(theUrl);
	}

	private IBaseExtension createEmptyModifierExtension(IBaseHasModifierExtensions theBaseHasModifierExtensions, String theUrl) {
		return (IBaseExtension) theBaseHasModifierExtensions.addModifierExtension().setUrl(theUrl);
	}

	private ExtensionDt createEmptyModifierExtensionDt(ISupportsUndeclaredExtensions theSupportsUndeclaredExtensions, String theUrl) {
		return createEmptyExtensionDt(theSupportsUndeclaredExtensions, true, theUrl);
	}

	/**
	 * Clones all values from a source object into the equivalent fields in a target object
	 *
	 * @param theSource              The source object (must not be null)
	 * @param theTarget              The target object to copy values into (must not be null)
	 * @param theIgnoreMissingFields The ignore fields in the target which do not exist (if false, an exception will be thrown if the target is unable to accept a value from the source)
	 * @return Returns the target (which will be the same object that was passed into theTarget) for easy chaining
	 */
	public IBase cloneInto(IBase theSource, IBase theTarget, boolean theIgnoreMissingFields) {
		Validate.notNull(theSource, "theSource must not be null");
		Validate.notNull(theTarget, "theTarget must not be null");

		if (theSource instanceof IPrimitiveType<?>) {
			if (theTarget instanceof IPrimitiveType<?>) {
				((IPrimitiveType<?>) theTarget).setValueAsString(((IPrimitiveType<?>) theSource).getValueAsString());
				return theSource;
			}
			if (theIgnoreMissingFields) {
				return theSource;
			}
			throw new DataFormatException("Can not copy value from primitive of type " + theSource.getClass().getName() + " into type " + theTarget.getClass().getName());
		}

		BaseRuntimeElementCompositeDefinition<?> sourceDef = (BaseRuntimeElementCompositeDefinition<?>) myContext.getElementDefinition(theSource.getClass());
		BaseRuntimeElementCompositeDefinition<?> targetDef = (BaseRuntimeElementCompositeDefinition<?>) myContext.getElementDefinition(theTarget.getClass());

		List<BaseRuntimeChildDefinition> children = sourceDef.getChildren();
		if (sourceDef instanceof RuntimeExtensionDtDefinition) {
			children = ((RuntimeExtensionDtDefinition) sourceDef).getChildrenIncludingUrl();
		}

		for (BaseRuntimeChildDefinition nextChild : children)
			for (IBase nextValue : nextChild.getAccessor().getValues(theSource)) {
				String elementName = nextChild.getChildNameByDatatype(nextValue.getClass());
				BaseRuntimeChildDefinition targetChild = targetDef.getChildByName(elementName);
				if (targetChild == null) {
					if (theIgnoreMissingFields) {
						continue;
					}
					throw new DataFormatException("Type " + theTarget.getClass().getName() + " does not have a child with name " + elementName);
				}

				BaseRuntimeElementDefinition<?> element = myContext.getElementDefinition(nextValue.getClass());
				IBase target = element.newInstance();

				targetChild.getMutator().addValue(theTarget, target);
				cloneInto(nextValue, target, theIgnoreMissingFields);
			}

		return theTarget;
	}

	/**
	 * Returns a list containing all child elements (including the resource itself) which are <b>non-empty</b> and are either of the exact type specified, or are a subclass of that type.
	 * <p>
	 * For example, specifying a type of {@link StringDt} would return all non-empty string instances within the message. Specifying a type of {@link IResource} would return the resource itself, as
	 * well as any contained resources.
	 * </p>
	 * <p>
	 * Note on scope: This method will descend into any contained resources ({@link IResource#getContained()}) as well, but will not descend into linked resources (e.g.
	 * {@link BaseResourceReferenceDt#getResource()}) or embedded resources (e.g. Bundle.entry.resource)
	 * </p>
	 *
	 * @param theResource The resource instance to search. Must not be null.
	 * @param theType     The type to search for. Must not be null.
	 * @return Returns a list of all matching elements
	 */
	public <T extends IBase> List<T> getAllPopulatedChildElementsOfType(IBaseResource theResource, final Class<T> theType) {
		final ArrayList<T> retVal = new ArrayList<>();
		BaseRuntimeElementCompositeDefinition<?> def = myContext.getResourceDefinition(theResource);
		visit(newMap(), theResource, theResource, null, null, def, new IModelVisitor() {
			@SuppressWarnings("unchecked")
			@Override
			public void acceptElement(IBaseResource theOuterResource, IBase theElement, List<String> thePathToElement, BaseRuntimeChildDefinition theChildDefinition, BaseRuntimeElementDefinition<?> theDefinition) {
				if (theElement == null || theElement.isEmpty()) {
					return;
				}

				if (theType.isAssignableFrom(theElement.getClass())) {
					retVal.add((T) theElement);
				}
			}
		});
		return retVal;
	}

	public List<ResourceReferenceInfo> getAllResourceReferences(final IBaseResource theResource) {
		final ArrayList<ResourceReferenceInfo> retVal = new ArrayList<>();
		BaseRuntimeElementCompositeDefinition<?> def = myContext.getResourceDefinition(theResource);
		visit(newMap(), theResource, theResource, null, null, def, new IModelVisitor() {
			@Override
			public void acceptElement(IBaseResource theOuterResource, IBase theElement, List<String> thePathToElement, BaseRuntimeChildDefinition theChildDefinition, BaseRuntimeElementDefinition<?> theDefinition) {
				if (theElement == null || theElement.isEmpty()) {
					return;
				}
				if (IBaseReference.class.isAssignableFrom(theElement.getClass())) {
					retVal.add(new ResourceReferenceInfo(myContext, theOuterResource, thePathToElement, (IBaseReference) theElement));
				}
			}
		});
		return retVal;
	}

	private BaseRuntimeChildDefinition getDefinition(BaseRuntimeElementCompositeDefinition<?> theCurrentDef, List<String> theSubList) {
		BaseRuntimeChildDefinition nextDef = theCurrentDef.getChildByNameOrThrowDataFormatException(theSubList.get(0));

		if (theSubList.size() == 1) {
			return nextDef;
		}
		BaseRuntimeElementCompositeDefinition<?> cmp = (BaseRuntimeElementCompositeDefinition<?>) nextDef.getChildByName(theSubList.get(0));
		return getDefinition(cmp, theSubList.subList(1, theSubList.size()));
	}

	public BaseRuntimeChildDefinition getDefinition(Class<? extends IBaseResource> theResourceType, String thePath) {
		RuntimeResourceDefinition def = myContext.getResourceDefinition(theResourceType);

		List<String> parts = Arrays.asList(thePath.split("\\."));
		List<String> subList = parts.subList(1, parts.size());
		if (subList.size() < 1) {
			throw new ConfigurationException("Invalid path: " + thePath);
		}
		return getDefinition(def, subList);

	}

	public Object getSingleValueOrNull(IBase theTarget, String thePath) {
		Class<IBase> wantedType = IBase.class;

		return getSingleValueOrNull(theTarget, thePath, wantedType);
	}

	public <T extends IBase> T getSingleValueOrNull(IBase theTarget, String thePath, Class<T> theWantedType) {
		Validate.notNull(theTarget, "theTarget must not be null");
		Validate.notBlank(thePath, "thePath must not be empty");

		BaseRuntimeElementDefinition<?> def = myContext.getElementDefinition(theTarget.getClass());
		if (!(def instanceof BaseRuntimeElementCompositeDefinition)) {
			throw new IllegalArgumentException("Target is not a composite type: " + theTarget.getClass().getName());
		}

		BaseRuntimeElementCompositeDefinition<?> currentDef = (BaseRuntimeElementCompositeDefinition<?>) def;

		List<String> parts = parsePath(currentDef, thePath);

		List<T> retVal = getValues(currentDef, theTarget, parts, theWantedType);
		if (retVal.isEmpty()) {
			return null;
		}
		return retVal.get(0);
	}

	private <T extends IBase> List<T> getValues(BaseRuntimeElementCompositeDefinition<?> theCurrentDef, IBase theCurrentObj, List<String> theSubList, Class<T> theWantedClass) {
		return getValues(theCurrentDef, theCurrentObj, theSubList, theWantedClass, false, false);
	}

	@SuppressWarnings("unchecked")
	private <T extends IBase> List<T> getValues(BaseRuntimeElementCompositeDefinition<?> theCurrentDef, IBase theCurrentObj, List<String> theSubList, Class<T> theWantedClass, boolean theCreate, boolean theAddExtension) {
		String name = theSubList.get(0);
		List<T> retVal = new ArrayList<>();

		if (name.startsWith("extension('")) {
			String extensionUrl = name.substring("extension('".length());
			int endIndex = extensionUrl.indexOf('\'');
			if (endIndex != -1) {
				extensionUrl = extensionUrl.substring(0, endIndex);
			}

			if (myContext.getVersion().getVersion().isOlderThan(FhirVersionEnum.DSTU3)) {
				// DTSU2
				final String extensionDtUrlForLambda = extensionUrl;
				List<ExtensionDt> extensionDts = Collections.emptyList();
				if (theCurrentObj instanceof ISupportsUndeclaredExtensions) {
					extensionDts = ((ISupportsUndeclaredExtensions) theCurrentObj).getUndeclaredExtensions()
						.stream()
						.filter(t -> t.getUrl().equals(extensionDtUrlForLambda))
						.collect(Collectors.toList());

					if (theAddExtension
						&& (!(theCurrentObj instanceof IBaseExtension) || (extensionDts.isEmpty() && theSubList.size() == 1))) {
						extensionDts.add(createEmptyExtensionDt((ISupportsUndeclaredExtensions) theCurrentObj, extensionUrl));
					}

					if (extensionDts.isEmpty() && theCreate) {
						extensionDts.add(createEmptyExtensionDt((ISupportsUndeclaredExtensions) theCurrentObj, extensionUrl));
					}

				} else if (theCurrentObj instanceof IBaseExtension) {
					extensionDts = ((IBaseExtension) theCurrentObj).getExtension();

					if (theAddExtension
						&& (extensionDts.isEmpty() && theSubList.size() == 1)) {
						extensionDts.add(createEmptyExtensionDt((IBaseExtension) theCurrentObj, extensionUrl));
					}

					if (extensionDts.isEmpty() && theCreate) {
						extensionDts.add(createEmptyExtensionDt((IBaseExtension) theCurrentObj, extensionUrl));
					}
				}

				for (ExtensionDt next : extensionDts) {
					if (theWantedClass.isAssignableFrom(next.getClass())) {
						retVal.add((T) next);
					}
				}
			} else {
				// DSTU3+
				final String extensionUrlForLambda = extensionUrl;
				List<IBaseExtension> extensions = Collections.emptyList();
				if (theCurrentObj instanceof IBaseHasExtensions) {
					extensions = ((IBaseHasExtensions) theCurrentObj).getExtension()
						.stream()
						.filter(t -> t.getUrl().equals(extensionUrlForLambda))
						.collect(Collectors.toList());

					if (theAddExtension
						&& (!(theCurrentObj instanceof IBaseExtension) || (extensions.isEmpty() && theSubList.size() == 1))) {
						extensions.add(createEmptyExtension((IBaseHasExtensions) theCurrentObj, extensionUrl));
					}

					if (extensions.isEmpty() && theCreate) {
						extensions.add(createEmptyExtension((IBaseHasExtensions) theCurrentObj, extensionUrl));
					}
				}

				for (IBaseExtension next : extensions) {
					if (theWantedClass.isAssignableFrom(next.getClass())) {
						retVal.add((T) next);
					}
				}
			}

			if (theSubList.size() > 1) {
				List<T> values = retVal;
				retVal = new ArrayList<>();
				for (T nextElement : values) {
					BaseRuntimeElementCompositeDefinition<?> nextChildDef = (BaseRuntimeElementCompositeDefinition<?>) myContext.getElementDefinition(nextElement.getClass());
					List<T> foundValues = getValues(nextChildDef, nextElement, theSubList.subList(1, theSubList.size()), theWantedClass, theCreate, theAddExtension);
					retVal.addAll(foundValues);
				}
			}

			return retVal;
		}

		if (name.startsWith("modifierExtension('")) {
			String extensionUrl = name.substring("modifierExtension('".length());
			int endIndex = extensionUrl.indexOf('\'');
			if (endIndex != -1) {
				extensionUrl = extensionUrl.substring(0, endIndex);
			}

			if (myContext.getVersion().getVersion().isOlderThan(FhirVersionEnum.DSTU3)) {
				// DSTU2
				final String extensionDtUrlForLambda = extensionUrl;
				List<ExtensionDt> extensionDts = Collections.emptyList();
				if (theCurrentObj instanceof ISupportsUndeclaredExtensions) {
					extensionDts = ((ISupportsUndeclaredExtensions) theCurrentObj).getUndeclaredModifierExtensions()
						.stream()
						.filter(t -> t.getUrl().equals(extensionDtUrlForLambda))
						.collect(Collectors.toList());

					if (theAddExtension
						&& (!(theCurrentObj instanceof IBaseExtension) || (extensionDts.isEmpty() && theSubList.size() == 1))) {
						extensionDts.add(createEmptyModifierExtensionDt((ISupportsUndeclaredExtensions) theCurrentObj, extensionUrl));
					}

					if (extensionDts.isEmpty() && theCreate) {
						extensionDts.add(createEmptyModifierExtensionDt((ISupportsUndeclaredExtensions) theCurrentObj, extensionUrl));
					}

				} else if (theCurrentObj instanceof IBaseExtension) {
					extensionDts = ((IBaseExtension) theCurrentObj).getExtension();

					if (theAddExtension
						&& (extensionDts.isEmpty() && theSubList.size() == 1)) {
						extensionDts.add(createEmptyExtensionDt((IBaseExtension) theCurrentObj, extensionUrl));
					}

					if (extensionDts.isEmpty() && theCreate) {
						extensionDts.add(createEmptyExtensionDt((IBaseExtension) theCurrentObj, extensionUrl));
					}
				}

				for (ExtensionDt next : extensionDts) {
					if (theWantedClass.isAssignableFrom(next.getClass())) {
						retVal.add((T) next);
					}
				}
			} else {
				// DSTU3+
				final String extensionUrlForLambda = extensionUrl;
				List<IBaseExtension> extensions = Collections.emptyList();

				if (theCurrentObj instanceof IBaseHasModifierExtensions) {
					extensions = ((IBaseHasModifierExtensions) theCurrentObj).getModifierExtension()
						.stream()
						.filter(t -> t.getUrl().equals(extensionUrlForLambda))
						.collect(Collectors.toList());

					if (theAddExtension
						&& (!(theCurrentObj instanceof IBaseExtension) || (extensions.isEmpty() && theSubList.size() == 1))) {
						extensions.add(createEmptyModifierExtension((IBaseHasModifierExtensions) theCurrentObj, extensionUrl));
					}

					if (extensions.isEmpty() && theCreate) {
						extensions.add(createEmptyModifierExtension((IBaseHasModifierExtensions) theCurrentObj, extensionUrl));
					}
				}

				for (IBaseExtension next : extensions) {
					if (theWantedClass.isAssignableFrom(next.getClass())) {
						retVal.add((T) next);
					}
				}
			}

			if (theSubList.size() > 1) {
				List<T> values = retVal;
				retVal = new ArrayList<>();
				for (T nextElement : values) {
					BaseRuntimeElementCompositeDefinition<?> nextChildDef = (BaseRuntimeElementCompositeDefinition<?>) myContext.getElementDefinition(nextElement.getClass());
					List<T> foundValues = getValues(nextChildDef, nextElement, theSubList.subList(1, theSubList.size()), theWantedClass, theCreate, theAddExtension);
					retVal.addAll(foundValues);
				}
			}

			return retVal;
		}

		BaseRuntimeChildDefinition nextDef = theCurrentDef.getChildByNameOrThrowDataFormatException(name);
		List<? extends IBase> values = nextDef.getAccessor().getValues(theCurrentObj);

		if (values.isEmpty() && theCreate) {
			IBase value = nextDef.getChildByName(name).newInstance();
			nextDef.getMutator().addValue(theCurrentObj, value);
			List<IBase> list = new ArrayList<>();
			list.add(value);
			values = list;
		}

		if (theSubList.size() == 1) {
			if (nextDef instanceof RuntimeChildChoiceDefinition) {
				for (IBase next : values) {
					if (next != null) {
						if (name.endsWith("[x]")) {
							if (theWantedClass == null || theWantedClass.isAssignableFrom(next.getClass())) {
								retVal.add((T) next);
							}
						} else {
							String childName = nextDef.getChildNameByDatatype(next.getClass());
							if (theSubList.get(0).equals(childName)) {
								if (theWantedClass == null || theWantedClass.isAssignableFrom(next.getClass())) {
									retVal.add((T) next);
								}
							}
						}
					}
				}
			} else {
				for (IBase next : values) {
					if (next != null) {
						if (theWantedClass == null || theWantedClass.isAssignableFrom(next.getClass())) {
							retVal.add((T) next);
						}
					}
				}
			}
		} else {
			for (IBase nextElement : values) {
				BaseRuntimeElementCompositeDefinition<?> nextChildDef = (BaseRuntimeElementCompositeDefinition<?>) myContext.getElementDefinition(nextElement.getClass());
				List<T> foundValues = getValues(nextChildDef, nextElement, theSubList.subList(1, theSubList.size()), theWantedClass, theCreate, theAddExtension);
				retVal.addAll(foundValues);
			}
		}
		return retVal;
	}

	/**
	 * Returns values stored in an element identified by its path. The list of values is of
	 * type {@link Object}.
	 *
	 * @param theResource The resource instance to be accessed. Must not be null.
	 * @param thePath     The path for the element to be accessed.
	 * @return A list of values of type {@link Object}.
	 */
	public List<IBase> getValues(IBaseResource theResource, String thePath) {
		Class<IBase> wantedClass = IBase.class;

		return getValues(theResource, thePath, wantedClass);
	}

	/**
	 * Returns values stored in an element identified by its path. The list of values is of
	 * type {@link Object}.
	 *
	 * @param theResource The resource instance to be accessed. Must not be null.
	 * @param thePath     The path for the element to be accessed.
	 * @param theCreate   When set to <code>true</code>, the terser will create a null-valued element where none exists.
	 * @return A list of values of type {@link Object}.
	 */
	public List<IBase> getValues(IBaseResource theResource, String thePath, boolean theCreate) {
		Class<IBase> wantedClass = IBase.class;

		return getValues(theResource, thePath, wantedClass, theCreate);
	}

	/**
	 * Returns values stored in an element identified by its path. The list of values is of
	 * type {@link Object}.
	 *
	 * @param theResource     The resource instance to be accessed. Must not be null.
	 * @param thePath         The path for the element to be accessed.
	 * @param theCreate       When set to <code>true</code>, the terser will create a null-valued element where none exists.
	 * @param theAddExtension When set to <code>true</code>, the terser will add a null-valued extension where one or more such extensions already exist.
	 * @return A list of values of type {@link Object}.
	 */
	public List<IBase> getValues(IBaseResource theResource, String thePath, boolean theCreate, boolean theAddExtension) {
		Class<IBase> wantedClass = IBase.class;

		return getValues(theResource, thePath, wantedClass, theCreate, theAddExtension);
	}

	/**
	 * Returns values stored in an element identified by its path. The list of values is of
	 * type <code>theWantedClass</code>.
	 *
	 * @param theResource    The resource instance to be accessed. Must not be null.
	 * @param thePath        The path for the element to be accessed.
	 * @param theWantedClass The desired class to be returned in a list.
	 * @param <T>            Type declared by <code>theWantedClass</code>
	 * @return A list of values of type <code>theWantedClass</code>.
	 */
	public <T extends IBase> List<T> getValues(IBaseResource theResource, String thePath, Class<T> theWantedClass) {
		RuntimeResourceDefinition def = myContext.getResourceDefinition(theResource);
		List<String> parts = parsePath(def, thePath);
		return getValues(def, theResource, parts, theWantedClass);
	}

	/**
	 * Returns values stored in an element identified by its path. The list of values is of
	 * type <code>theWantedClass</code>.
	 *
	 * @param theResource    The resource instance to be accessed. Must not be null.
	 * @param thePath        The path for the element to be accessed.
	 * @param theWantedClass The desired class to be returned in a list.
	 * @param theCreate      When set to <code>true</code>, the terser will create a null-valued element where none exists.
	 * @param <T>            Type declared by <code>theWantedClass</code>
	 * @return A list of values of type <code>theWantedClass</code>.
	 */
	public <T extends IBase> List<T> getValues(IBaseResource theResource, String thePath, Class<T> theWantedClass, boolean theCreate) {
		RuntimeResourceDefinition def = myContext.getResourceDefinition(theResource);
		List<String> parts = parsePath(def, thePath);
		return getValues(def, theResource, parts, theWantedClass, theCreate, false);
	}

	/**
	 * Returns values stored in an element identified by its path. The list of values is of
	 * type <code>theWantedClass</code>.
	 *
	 * @param theResource     The resource instance to be accessed. Must not be null.
	 * @param thePath         The path for the element to be accessed.
	 * @param theWantedClass  The desired class to be returned in a list.
	 * @param theCreate       When set to <code>true</code>, the terser will create a null-valued element where none exists.
	 * @param theAddExtension When set to <code>true</code>, the terser will add a null-valued extension where one or more such extensions already exist.
	 * @param <T>             Type declared by <code>theWantedClass</code>
	 * @return A list of values of type <code>theWantedClass</code>.
	 */
	public <T extends IBase> List<T> getValues(IBaseResource theResource, String thePath, Class<T> theWantedClass, boolean theCreate, boolean theAddExtension) {
		RuntimeResourceDefinition def = myContext.getResourceDefinition(theResource);
		List<String> parts = parsePath(def, thePath);
		return getValues(def, theResource, parts, theWantedClass, theCreate, theAddExtension);
	}

	private List<String> parsePath(BaseRuntimeElementCompositeDefinition<?> theElementDef, String thePath) {
		List<String> parts = new ArrayList<>();

		int currentStart = 0;
		boolean inSingleQuote = false;
		for (int i = 0; i < thePath.length(); i++) {
			switch (thePath.charAt(i)) {
				case '\'':
					inSingleQuote = !inSingleQuote;
					break;
				case '.':
					if (!inSingleQuote) {
						parts.add(thePath.substring(currentStart, i));
						currentStart = i + 1;
					}
					break;
			}
		}

		parts.add(thePath.substring(currentStart));

		if (theElementDef instanceof RuntimeResourceDefinition) {
			if (parts.size() > 0 && parts.get(0).equals(theElementDef.getName())) {
				parts = parts.subList(1, parts.size());
			}
		}

		if (parts.size() < 1) {
			throw new ConfigurationException("Invalid path: " + thePath);
		}
		return parts;
	}

	/**
	 * Returns <code>true</code> if <code>theSource</code> is in the compartment named <code>theCompartmentName</code>
	 * belonging to resource <code>theTarget</code>
	 *
	 * @param theCompartmentName The name of the compartment
	 * @param theSource          The potential member of the compartment
	 * @param theTarget          The owner of the compartment. Note that both the resource type and ID must be filled in on this IIdType or the method will throw an {@link IllegalArgumentException}
	 * @return <code>true</code> if <code>theSource</code> is in the compartment
	 * @throws IllegalArgumentException If theTarget does not contain both a resource type and ID
	 */
	public boolean isSourceInCompartmentForTarget(String theCompartmentName, IBaseResource theSource, IIdType theTarget) {
		Validate.notBlank(theCompartmentName, "theCompartmentName must not be null or blank");
		Validate.notNull(theSource, "theSource must not be null");
		Validate.notNull(theTarget, "theTarget must not be null");
		Validate.notBlank(defaultString(theTarget.getResourceType()), "theTarget must have a populated resource type (theTarget.getResourceType() does not return a value)");
		Validate.notBlank(defaultString(theTarget.getIdPart()), "theTarget must have a populated ID (theTarget.getIdPart() does not return a value)");

		String wantRef = theTarget.toUnqualifiedVersionless().getValue();

		RuntimeResourceDefinition sourceDef = myContext.getResourceDefinition(theSource);
		if (theSource.getIdElement().hasIdPart()) {
			if (wantRef.equals(sourceDef.getName() + '/' + theSource.getIdElement().getIdPart())) {
				return true;
			}
		}

		List<RuntimeSearchParam> params = sourceDef.getSearchParamsForCompartmentName(theCompartmentName);
		for (RuntimeSearchParam nextParam : params) {
			for (String nextPath : nextParam.getPathsSplit()) {

				/*
				 * DSTU3 and before just defined compartments as being (e.g.) named
				 * Patient with a path like CarePlan.subject
				 *
				 * R4 uses a fancier format like CarePlan.subject.where(resolve() is Patient)
				 *
				 * The following Regex is a hack to make that efficient at runtime.
				 */
				String wantType = null;
				Pattern pattern = COMPARTMENT_MATCHER_PATH;
				Matcher matcher = pattern.matcher(nextPath);
				if (matcher.matches()) {
					nextPath = matcher.group(1);
					wantType = matcher.group(2);
				}

				List<IBaseReference> values = getValues(theSource, nextPath, IBaseReference.class);
				for (IBaseReference nextValue : values) {
					IIdType nextTargetId = nextValue.getReferenceElement();
					String nextRef = nextTargetId.toUnqualifiedVersionless().getValue();

					/*
					 * If the reference isn't an explicit resource ID, but instead is just
					 * a resource object, we'll calculate its ID and treat the target
					 * as that.
					 */
					if (isBlank(nextRef) && nextValue.getResource() != null) {
						IBaseResource nextTarget = nextValue.getResource();
						nextTargetId = nextTarget.getIdElement().toUnqualifiedVersionless();
						if (!nextTargetId.hasResourceType()) {
							String resourceType = myContext.getResourceDefinition(nextTarget).getName();
							nextTargetId.setParts(null, resourceType, nextTargetId.getIdPart(), null);
						}
						nextRef = nextTargetId.getValue();
					}

					if (isNotBlank(wantType)) {
						String nextTargetIdResourceType = nextTargetId.getResourceType();
						if (nextTargetIdResourceType == null || !nextTargetIdResourceType.equals(wantType)) {
							continue;
						}
					}

					if (wantRef.equals(nextRef)) {
						return true;
					}
				}
			}
		}

		return false;
	}

	private void visit(IBase theElement, BaseRuntimeChildDefinition theChildDefinition, BaseRuntimeElementDefinition<?> theDefinition, IModelVisitor2 theCallback, List<IBase> theContainingElementPath,
							 List<BaseRuntimeChildDefinition> theChildDefinitionPath, List<BaseRuntimeElementDefinition<?>> theElementDefinitionPath) {
		if (theChildDefinition != null) {
			theChildDefinitionPath.add(theChildDefinition);
		}
		theContainingElementPath.add(theElement);
		theElementDefinitionPath.add(theDefinition);

		boolean recurse = theCallback.acceptElement(theElement, Collections.unmodifiableList(theContainingElementPath), Collections.unmodifiableList(theChildDefinitionPath),
			Collections.unmodifiableList(theElementDefinitionPath));
		if (recurse) {

			/*
			 * Visit undeclared extensions
			 */
			if (theElement instanceof ISupportsUndeclaredExtensions) {
				ISupportsUndeclaredExtensions containingElement = (ISupportsUndeclaredExtensions) theElement;
				for (ExtensionDt nextExt : containingElement.getUndeclaredExtensions()) {
					theContainingElementPath.add(nextExt);
					theCallback.acceptUndeclaredExtension(nextExt, theContainingElementPath, theChildDefinitionPath, theElementDefinitionPath);
					theContainingElementPath.remove(theContainingElementPath.size() - 1);
				}
			}

			/*
			 * Now visit the children of the given element
			 */
			switch (theDefinition.getChildType()) {
				case ID_DATATYPE:
				case PRIMITIVE_XHTML_HL7ORG:
				case PRIMITIVE_XHTML:
				case PRIMITIVE_DATATYPE:
					// These are primitive types, so we don't need to visit their children
					break;
				case RESOURCE:
				case RESOURCE_BLOCK:
				case COMPOSITE_DATATYPE: {
					BaseRuntimeElementCompositeDefinition<?> childDef = (BaseRuntimeElementCompositeDefinition<?>) theDefinition;
					for (BaseRuntimeChildDefinition nextChild : childDef.getChildrenAndExtension()) {
						List<? extends IBase> values = nextChild.getAccessor().getValues(theElement);
						if (values != null) {
							for (IBase nextValue : values) {
								if (nextValue == null) {
									continue;
								}
								if (nextValue.isEmpty()) {
									continue;
								}
								BaseRuntimeElementDefinition<?> childElementDef;
								Class<? extends IBase> valueType = nextValue.getClass();
								childElementDef = nextChild.getChildElementDefinitionByDatatype(valueType);
								while (childElementDef == null && IBase.class.isAssignableFrom(valueType)) {
									childElementDef = nextChild.getChildElementDefinitionByDatatype(valueType);
									valueType = (Class<? extends IBase>) valueType.getSuperclass();
								}

								Class<? extends IBase> typeClass = nextValue.getClass();
								while (childElementDef == null && IBase.class.isAssignableFrom(typeClass)) {
									//noinspection unchecked
									typeClass = (Class<? extends IBase>) typeClass.getSuperclass();
									childElementDef = nextChild.getChildElementDefinitionByDatatype(typeClass);
								}

								Validate.notNull(childElementDef, "Found value of type[%s] which is not valid for field[%s] in %s", nextValue.getClass(), nextChild.getElementName(), childDef.getName());

								visit(nextValue, nextChild, childElementDef, theCallback, theContainingElementPath, theChildDefinitionPath, theElementDefinitionPath);
							}
						}
					}
					break;
				}
				case CONTAINED_RESOURCES: {
					BaseContainedDt value = (BaseContainedDt) theElement;
					for (IResource next : value.getContainedResources()) {
						BaseRuntimeElementCompositeDefinition<?> def = myContext.getResourceDefinition(next);
						visit(next, null, def, theCallback, theContainingElementPath, theChildDefinitionPath, theElementDefinitionPath);
					}
					break;
				}
				case EXTENSION_DECLARED:
				case UNDECL_EXT: {
					throw new IllegalStateException("state should not happen: " + theDefinition.getChildType());
				}
				case CONTAINED_RESOURCE_LIST: {
					if (theElement != null) {
						BaseRuntimeElementDefinition<?> def = myContext.getElementDefinition(theElement.getClass());
						visit(theElement, null, def, theCallback, theContainingElementPath, theChildDefinitionPath, theElementDefinitionPath);
					}
					break;
				}
			}

		}

		if (theChildDefinition != null) {
			theChildDefinitionPath.remove(theChildDefinitionPath.size() - 1);
		}
		theContainingElementPath.remove(theContainingElementPath.size() - 1);
		theElementDefinitionPath.remove(theElementDefinitionPath.size() - 1);
	}

	/**
	 * Visit all elements in a given resource
	 *
	 * <p>
	 * Note on scope: This method will descend into any contained resources ({@link IResource#getContained()}) as well, but will not descend into linked resources (e.g.
	 * {@link BaseResourceReferenceDt#getResource()}) or embedded resources (e.g. Bundle.entry.resource)
	 * </p>
	 *
	 * @param theResource The resource to visit
	 * @param theVisitor  The visitor
	 */
	public void visit(IBaseResource theResource, IModelVisitor theVisitor) {
		BaseRuntimeElementCompositeDefinition<?> def = myContext.getResourceDefinition(theResource);
		visit(newMap(), theResource, theResource, null, null, def, theVisitor);
	}

	public Map<Object, Object> newMap() {
		return new IdentityHashMap<>();
	}

	/**
	 * Visit all elements in a given resource
	 * <p>
	 * <b>THIS ALTERNATE METHOD IS STILL EXPERIMENTAL! USE WITH CAUTION</b>
	 * </p>
	 * <p>
	 * Note on scope: This method will descend into any contained resources ({@link IResource#getContained()}) as well, but will not descend into linked resources (e.g.
	 * {@link BaseResourceReferenceDt#getResource()}) or embedded resources (e.g. Bundle.entry.resource)
	 * </p>
	 *
	 * @param theResource The resource to visit
	 * @param theVisitor  The visitor
	 */
	public void visit(IBaseResource theResource, IModelVisitor2 theVisitor) {
		BaseRuntimeElementCompositeDefinition<?> def = myContext.getResourceDefinition(theResource);
		visit(theResource, null, def, theVisitor, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
	}

	private void visit(Map<Object, Object> theStack, IBaseResource theResource, IBase theElement, List<String> thePathToElement, BaseRuntimeChildDefinition theChildDefinition,
							 BaseRuntimeElementDefinition<?> theDefinition, IModelVisitor theCallback) {
		List<String> pathToElement = addNameToList(thePathToElement, theChildDefinition);

		if (theStack.put(theElement, theElement) != null) {
			return;
		}

		theCallback.acceptElement(theResource, theElement, pathToElement, theChildDefinition, theDefinition);

		BaseRuntimeElementDefinition<?> def = theDefinition;
		if (def.getChildType() == ChildTypeEnum.CONTAINED_RESOURCE_LIST) {
			def = myContext.getElementDefinition(theElement.getClass());
		}

		if (theElement instanceof IBaseReference) {
			IBaseResource target = ((IBaseReference) theElement).getResource();
			if (target != null) {
				if (target.getIdElement().hasIdPart() == false || target.getIdElement().isLocal()) {
					RuntimeResourceDefinition targetDef = myContext.getResourceDefinition(target);
					visit(theStack, target, target, pathToElement, null, targetDef, theCallback);
				}
			}
		}

		switch (def.getChildType()) {
			case ID_DATATYPE:
			case PRIMITIVE_XHTML_HL7ORG:
			case PRIMITIVE_XHTML:
			case PRIMITIVE_DATATYPE:
				// These are primitive types
				break;
			case RESOURCE:
			case RESOURCE_BLOCK:
			case COMPOSITE_DATATYPE: {
				BaseRuntimeElementCompositeDefinition<?> childDef = (BaseRuntimeElementCompositeDefinition<?>) def;
				for (BaseRuntimeChildDefinition nextChild : childDef.getChildrenAndExtension()) {

					List<?> values = nextChild.getAccessor().getValues(theElement);
					if (values != null) {
						for (Object nextValueObject : values) {
							IBase nextValue;
							try {
								nextValue = (IBase) nextValueObject;
							} catch (ClassCastException e) {
								String s = "Found instance of " + nextValueObject.getClass() + " - Did you set a field value to the incorrect type? Expected " + IBase.class.getName();
								throw new ClassCastException(s);
							}
							if (nextValue == null) {
								continue;
							}
							if (nextValue.isEmpty()) {
								continue;
							}
							BaseRuntimeElementDefinition<?> childElementDef;
							childElementDef = nextChild.getChildElementDefinitionByDatatype(nextValue.getClass());

							if (childElementDef == null) {
								childElementDef = myContext.getElementDefinition(nextValue.getClass());
							}

							if (nextChild instanceof RuntimeChildDirectResource) {
								// Don't descend into embedded resources
								theCallback.acceptElement(theResource, nextValue, null, nextChild, childElementDef);
							} else {
								visit(theStack, theResource, nextValue, pathToElement, nextChild, childElementDef, theCallback);
							}
						}
					}
				}
				break;
			}
			case CONTAINED_RESOURCES: {
				BaseContainedDt value = (BaseContainedDt) theElement;
				for (IResource next : value.getContainedResources()) {
					def = myContext.getResourceDefinition(next);
					visit(theStack, next, next, pathToElement, null, def, theCallback);
				}
				break;
			}
			case CONTAINED_RESOURCE_LIST:
			case EXTENSION_DECLARED:
			case UNDECL_EXT: {
				throw new IllegalStateException("state should not happen: " + def.getChildType());
			}
		}

		theStack.remove(theElement);

	}

	/**
	 * Returns all embedded resources that are found embedded within <code>theResource</code>.
	 * An embedded resource is a resource that can be found as a direct child within a resource,
	 * as opposed to being referenced by the resource.
	 * <p>
	 * Examples include resources found within <code>Bundle.entry.resource</code>
	 * and <code>Parameters.parameter.resource</code>, as well as contained resources
	 * found within <code>Resource.contained</code>
	 * </p>
	 *
	 * @param theRecurse Should embedded resources be recursively scanned for further embedded
	 *                   resources
	 * @return A collection containing the embedded resources. Order is arbitrary.
	 */
	public Collection<IBaseResource> getAllEmbeddedResources(IBaseResource theResource, boolean theRecurse) {
		Validate.notNull(theResource, "theResource must not be null");
		ArrayList<IBaseResource> retVal = new ArrayList<>();

		visit(theResource, new IModelVisitor2() {
			@Override
			public boolean acceptElement(IBase theElement, List<IBase> theContainingElementPath, List<BaseRuntimeChildDefinition> theChildDefinitionPath, List<BaseRuntimeElementDefinition<?>> theElementDefinitionPath) {
				if (theElement == theResource) {
					return true;
				}
				if (theElement instanceof IBaseResource) {
					retVal.add((IBaseResource) theElement);
					return theRecurse;
				}
				return true;
			}

			@Override
			public boolean acceptUndeclaredExtension(IBaseExtension<?, ?> theNextExt, List<IBase> theContainingElementPath, List<BaseRuntimeChildDefinition> theChildDefinitionPath, List<BaseRuntimeElementDefinition<?>> theElementDefinitionPath) {
				return true;
			}
		});

		return retVal;
	}

	/**
	 * Clear all content on a resource
	 */
	public void clear(IBaseResource theInput) {
		visit(theInput, new IModelVisitor2() {
			@Override
			public boolean acceptElement(IBase theElement, List<IBase> theContainingElementPath, List<BaseRuntimeChildDefinition> theChildDefinitionPath, List<BaseRuntimeElementDefinition<?>> theElementDefinitionPath) {
				if (theElement instanceof IPrimitiveType) {
					((IPrimitiveType) theElement).setValueAsString(null);
				}
				return true;
			}

			@Override
			public boolean acceptUndeclaredExtension(IBaseExtension<?, ?> theNextExt, List<IBase> theContainingElementPath, List<BaseRuntimeChildDefinition> theChildDefinitionPath, List<BaseRuntimeElementDefinition<?>> theElementDefinitionPath) {
				theNextExt.setUrl(null);
				theNextExt.setValue(null);
				return true;
			}

		});
	}
}
