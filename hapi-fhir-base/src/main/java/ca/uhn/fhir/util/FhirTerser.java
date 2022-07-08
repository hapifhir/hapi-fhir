package ca.uhn.fhir.util;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition.ChildTypeEnum;
import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeChildChoiceDefinition;
import ca.uhn.fhir.context.RuntimeChildDirectResource;
import ca.uhn.fhir.context.RuntimeExtensionDtDefinition;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.api.IIdentifiableElement;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ISupportsUndeclaredExtensions;
import ca.uhn.fhir.model.base.composite.BaseContainedDt;
import ca.uhn.fhir.model.base.composite.BaseResourceReferenceDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.parser.DataFormatException;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseElement;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseHasExtensions;
import org.hl7.fhir.instance.model.api.IBaseHasModifierExtensions;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IDomainResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.substring;

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

public class FhirTerser {

	private static final Pattern COMPARTMENT_MATCHER_PATH = Pattern.compile("([a-zA-Z.]+)\\.where\\(resolve\\(\\) is ([a-zA-Z]+)\\)");
	private static final String USER_DATA_KEY_CONTAIN_RESOURCES_COMPLETED = FhirTerser.class.getName() + "_CONTAIN_RESOURCES_COMPLETED";
	private final FhirContext myContext;

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

		// DSTU3+
		if (theSource instanceof IBaseElement) {
			IBaseElement source = (IBaseElement) theSource;
			IBaseElement target = (IBaseElement) theTarget;
			target.setId(source.getId());
		}

		// DSTU2 only
		if (theSource instanceof IIdentifiableElement) {
			IIdentifiableElement source = (IIdentifiableElement) theSource;
			IIdentifiableElement target = (IIdentifiableElement) theTarget;
			target.setElementSpecificId(source.getElementSpecificId());
		}

		// DSTU2 only
		if (theSource instanceof IResource) {
			IResource source = (IResource) theSource;
			IResource target = (IResource) theTarget;
			target.setId(source.getId());
			target.getResourceMetadata().putAll(source.getResourceMetadata());
		}

		if (theSource instanceof IPrimitiveType<?>) {
			if (theTarget instanceof IPrimitiveType<?>) {
				String valueAsString = ((IPrimitiveType<?>) theSource).getValueAsString();
				if (isNotBlank(valueAsString)) {
					((IPrimitiveType<?>) theTarget).setValueAsString(valueAsString);
				}
				if (theSource instanceof IBaseHasExtensions && theTarget instanceof IBaseHasExtensions) {
					List<? extends IBaseExtension<?, ?>> extensions = ((IBaseHasExtensions) theSource).getExtension();
					for (IBaseExtension<?, ?> nextSource : extensions) {
						IBaseExtension<?, ?> nextTarget = ((IBaseHasExtensions) theTarget).addExtension();
						cloneInto(nextSource, nextTarget, theIgnoreMissingFields);
					}
				}
				return theSource;
			}
			if (theIgnoreMissingFields) {
				return theSource;
			}
			throw new DataFormatException(Msg.code(1788) + "Can not copy value from primitive of type " + theSource.getClass().getName() + " into type " + theTarget.getClass().getName());
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
					throw new DataFormatException(Msg.code(1789) + "Type " + theTarget.getClass().getName() + " does not have a child with name " + elementName);
				}

				BaseRuntimeElementDefinition<?> element = myContext.getElementDefinition(nextValue.getClass());
				Object instanceConstructorArg = targetChild.getInstanceConstructorArguments();
				IBase target;
				if (instanceConstructorArg != null) {
					target = element.newInstance(instanceConstructorArg);
				} else {
					target = element.newInstance();
				}

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
			throw new ConfigurationException(Msg.code(1790) + "Invalid path: " + thePath);
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
			throw new IllegalArgumentException(Msg.code(1791) + "Target is not a composite type: " + theTarget.getClass().getName());
		}

		BaseRuntimeElementCompositeDefinition<?> currentDef = (BaseRuntimeElementCompositeDefinition<?>) def;

		List<String> parts = parsePath(currentDef, thePath);

		List<T> retVal = getValues(currentDef, theTarget, parts, theWantedType);
		if (retVal.isEmpty()) {
			return null;
		}
		return retVal.get(0);
	}

	public Optional<String> getSinglePrimitiveValue(IBase theTarget, String thePath) {
		return getSingleValue(theTarget, thePath, IPrimitiveType.class).map(t -> t.getValueAsString());
	}

	public String getSinglePrimitiveValueOrNull(IBase theTarget, String thePath) {
		return getSingleValue(theTarget, thePath, IPrimitiveType.class).map(t -> t.getValueAsString()).orElse(null);
	}

	public <T extends IBase> Optional<T> getSingleValue(IBase theTarget, String thePath, Class<T> theWantedType) {
		return Optional.ofNullable(getSingleValueOrNull(theTarget, thePath, theWantedType));
	}

	private <T extends IBase> List<T> getValues(BaseRuntimeElementCompositeDefinition<?> theCurrentDef, IBase theCurrentObj, List<String> theSubList, Class<T> theWantedClass) {
		return getValues(theCurrentDef, theCurrentObj, theSubList, theWantedClass, false, false);
	}

	@SuppressWarnings("unchecked")
	private <T extends IBase> List<T> getValues(BaseRuntimeElementCompositeDefinition<?> theCurrentDef, IBase theCurrentObj, List<String> theSubList, Class<T> theWantedClass, boolean theCreate, boolean theAddExtension) {
		if (theSubList.isEmpty()) {
			return Collections.emptyList();
		}

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
			BaseRuntimeElementDefinition<?> childByName = nextDef.getChildByName(name);
			Object arg = nextDef.getInstanceConstructorArguments();
			IBase value;
			if (arg != null) {
				value = childByName.newInstance(arg);
			} else {
				value = childByName.newInstance();
			}
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
	 * @param theElement The element to be accessed. Must not be null.
	 * @param thePath    The path for the element to be accessed.@param theElement The resource instance to be accessed. Must not be null.
	 * @return A list of values of type {@link Object}.
	 */
	public List<IBase> getValues(IBase theElement, String thePath) {
		Class<IBase> wantedClass = IBase.class;

		return getValues(theElement, thePath, wantedClass);
	}

	/**
	 * Returns values stored in an element identified by its path. The list of values is of
	 * type {@link Object}.
	 *
	 * @param theElement The element to be accessed. Must not be null.
	 * @param thePath    The path for the element to be accessed.
	 * @param theCreate  When set to <code>true</code>, the terser will create a null-valued element where none exists.
	 * @return A list of values of type {@link Object}.
	 */
	public List<IBase> getValues(IBase theElement, String thePath, boolean theCreate) {
		Class<IBase> wantedClass = IBase.class;

		return getValues(theElement, thePath, wantedClass, theCreate);
	}

	/**
	 * Returns values stored in an element identified by its path. The list of values is of
	 * type {@link Object}.
	 *
	 * @param theElement      The element to be accessed. Must not be null.
	 * @param thePath         The path for the element to be accessed.
	 * @param theCreate       When set to <code>true</code>, the terser will create a null-valued element where none exists.
	 * @param theAddExtension When set to <code>true</code>, the terser will add a null-valued extension where one or more such extensions already exist.
	 * @return A list of values of type {@link Object}.
	 */
	public List<IBase> getValues(IBase theElement, String thePath, boolean theCreate, boolean theAddExtension) {
		Class<IBase> wantedClass = IBase.class;

		return getValues(theElement, thePath, wantedClass, theCreate, theAddExtension);
	}

	/**
	 * Returns values stored in an element identified by its path. The list of values is of
	 * type <code>theWantedClass</code>.
	 *
	 * @param theElement     The element to be accessed. Must not be null.
	 * @param thePath        The path for the element to be accessed.
	 * @param theWantedClass The desired class to be returned in a list.
	 * @param <T>            Type declared by <code>theWantedClass</code>
	 * @return A list of values of type <code>theWantedClass</code>.
	 */
	public <T extends IBase> List<T> getValues(IBase theElement, String thePath, Class<T> theWantedClass) {
		BaseRuntimeElementCompositeDefinition<?> def = (BaseRuntimeElementCompositeDefinition<?>) myContext.getElementDefinition(theElement.getClass());
		List<String> parts = parsePath(def, thePath);
		return getValues(def, theElement, parts, theWantedClass);
	}

	/**
	 * Returns values stored in an element identified by its path. The list of values is of
	 * type <code>theWantedClass</code>.
	 *
	 * @param theElement     The element to be accessed. Must not be null.
	 * @param thePath        The path for the element to be accessed.
	 * @param theWantedClass The desired class to be returned in a list.
	 * @param theCreate      When set to <code>true</code>, the terser will create a null-valued element where none exists.
	 * @param <T>            Type declared by <code>theWantedClass</code>
	 * @return A list of values of type <code>theWantedClass</code>.
	 */
	public <T extends IBase> List<T> getValues(IBase theElement, String thePath, Class<T> theWantedClass, boolean theCreate) {
		BaseRuntimeElementCompositeDefinition<?> def = (BaseRuntimeElementCompositeDefinition<?>) myContext.getElementDefinition(theElement.getClass());
		List<String> parts = parsePath(def, thePath);
		return getValues(def, theElement, parts, theWantedClass, theCreate, false);
	}

	/**
	 * Returns values stored in an element identified by its path. The list of values is of
	 * type <code>theWantedClass</code>.
	 *
	 * @param theElement      The element to be accessed. Must not be null.
	 * @param thePath         The path for the element to be accessed.
	 * @param theWantedClass  The desired class to be returned in a list.
	 * @param theCreate       When set to <code>true</code>, the terser will create a null-valued element where none exists.
	 * @param theAddExtension When set to <code>true</code>, the terser will add a null-valued extension where one or more such extensions already exist.
	 * @param <T>             Type declared by <code>theWantedClass</code>
	 * @return A list of values of type <code>theWantedClass</code>.
	 */
	public <T extends IBase> List<T> getValues(IBase theElement, String thePath, Class<T> theWantedClass, boolean theCreate, boolean theAddExtension) {
		BaseRuntimeElementCompositeDefinition<?> def = (BaseRuntimeElementCompositeDefinition<?>) myContext.getElementDefinition(theElement.getClass());
		List<String> parts = parsePath(def, thePath);
		return getValues(def, theElement, parts, theWantedClass, theCreate, theAddExtension);
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

		String firstPart = parts.get(0);
		if (Character.isUpperCase(firstPart.charAt(0)) && theElementDef instanceof RuntimeResourceDefinition) {
			if (firstPart.equals(theElementDef.getName())) {
				parts = parts.subList(1, parts.size());
			} else {
				parts = Collections.emptyList();
				return parts;
			}
		} else if (firstPart.equals(theElementDef.getName())) {
			parts = parts.subList(1, parts.size());
		}

		if (parts.size() < 1) {
			throw new ConfigurationException(Msg.code(1792) + "Invalid path: " + thePath);
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
		return isSourceInCompartmentForTarget(theCompartmentName, theSource, theTarget, null);
	}

	/**
	 * Returns <code>true</code> if <code>theSource</code> is in the compartment named <code>theCompartmentName</code>
	 * belonging to resource <code>theTarget</code>
	 *
	 * @param theCompartmentName                 The name of the compartment
	 * @param theSource                          The potential member of the compartment
	 * @param theTarget                          The owner of the compartment. Note that both the resource type and ID must be filled in on this IIdType or the method will throw an {@link IllegalArgumentException}
	 * @param theAdditionalCompartmentParamNames If provided, search param names provided here will be considered as included in the given compartment for this comparison.
	 * @return <code>true</code> if <code>theSource</code> is in the compartment or one of the additional parameters matched.
	 * @throws IllegalArgumentException If theTarget does not contain both a resource type and ID
	 */
	public boolean isSourceInCompartmentForTarget(String theCompartmentName, IBaseResource theSource, IIdType theTarget, Set<String> theAdditionalCompartmentParamNames) {
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

		// If passed an additional set of searchparameter names, add them for comparison purposes.
		if (theAdditionalCompartmentParamNames != null) {
			List<RuntimeSearchParam> additionalParams = theAdditionalCompartmentParamNames.stream().map(sourceDef::getSearchParam)
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
			if (params == null || params.isEmpty()) {
				params = additionalParams;
			} else {
				List<RuntimeSearchParam> existingParams = params;
				params = new ArrayList<>(existingParams.size() + additionalParams.size());
				params.addAll(existingParams);
				params.addAll(additionalParams);
			}
		}


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
							String resourceType = myContext.getResourceType(nextTarget);
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
					throw new IllegalStateException(Msg.code(1793) + "state should not happen: " + theDefinition.getChildType());
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
	 * Visit all elements in a given resource or element
	 * <p>
	 * <b>THIS ALTERNATE METHOD IS STILL EXPERIMENTAL! USE WITH CAUTION</b>
	 * </p>
	 * <p>
	 * Note on scope: This method will descend into any contained resources ({@link IResource#getContained()}) as well, but will not descend into linked resources (e.g.
	 * {@link BaseResourceReferenceDt#getResource()}) or embedded resources (e.g. Bundle.entry.resource)
	 * </p>
	 *
	 * @param theElement The element to visit
	 * @param theVisitor The visitor
	 */
	public void visit(IBase theElement, IModelVisitor2 theVisitor) {
		BaseRuntimeElementDefinition<?> def = myContext.getElementDefinition(theElement.getClass());
		if (def instanceof BaseRuntimeElementCompositeDefinition) {
			BaseRuntimeElementCompositeDefinition<?> defComposite = (BaseRuntimeElementCompositeDefinition<?>) def;
			visit(theElement, null, def, theVisitor, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
		} else if (theElement instanceof IBaseExtension) {
			theVisitor.acceptUndeclaredExtension((IBaseExtension<?, ?>) theElement, Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
		} else {
			theVisitor.acceptElement(theElement, Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
		}
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
			Class<? extends IBase> clazz = theElement.getClass();
			def = myContext.getElementDefinition(clazz);
			Validate.notNull(def, "Unable to find element definition for class: %s", clazz);
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
				List<BaseRuntimeChildDefinition> childrenAndExtensionDefs = childDef.getChildrenAndExtension();
				for (BaseRuntimeChildDefinition nextChild : childrenAndExtensionDefs) {

					List<?> values = nextChild.getAccessor().getValues(theElement);

					if (values != null) {
						for (Object nextValueObject : values) {
							IBase nextValue;
							try {
								nextValue = (IBase) nextValueObject;
							} catch (ClassCastException e) {
								String s = "Found instance of " + nextValueObject.getClass() + " - Did you set a field value to the incorrect type? Expected " + IBase.class.getName();
								throw new ClassCastException(Msg.code(1794) + s);
							}
							if (nextValue == null) {
								continue;
							}
							if (nextValue.isEmpty()) {
								continue;
							}
							BaseRuntimeElementDefinition<?> childElementDef;
							Class<? extends IBase> clazz = nextValue.getClass();
							childElementDef = nextChild.getChildElementDefinitionByDatatype(clazz);

							if (childElementDef == null) {
								childElementDef = myContext.getElementDefinition(clazz);
								Validate.notNull(childElementDef, "Unable to find element definition for class: %s", clazz);
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
				throw new IllegalStateException(Msg.code(1795) + "state should not happen: " + def.getChildType());
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

	private void containResourcesForEncoding(ContainedResources theContained, IBaseResource theResource, boolean theModifyResource) {
		List<IBaseReference> allReferences = getAllPopulatedChildElementsOfType(theResource, IBaseReference.class);
		for (IBaseReference next : allReferences) {
			IBaseResource resource = next.getResource();
			if (resource == null && next.getReferenceElement().isLocal()) {
				if (theContained.hasExistingIdToContainedResource()) {
					IBaseResource potentialTarget = theContained.getExistingIdToContainedResource().remove(next.getReferenceElement().getValue());
					if (potentialTarget != null) {
						theContained.addContained(next.getReferenceElement(), potentialTarget);
						containResourcesForEncoding(theContained, potentialTarget, theModifyResource);
					}
				}
			}
		}

		for (IBaseReference next : allReferences) {
			IBaseResource resource = next.getResource();
			if (resource != null) {
				if (resource.getIdElement().isEmpty() || resource.getIdElement().isLocal()) {
					if (theContained.getResourceId(resource) != null) {
						// Prevent infinite recursion if there are circular loops in the contained resources
						continue;
					}
					IIdType id = theContained.addContained(resource);
					if (theModifyResource) {
						getContainedResourceList(theResource).add(resource);
						next.setReference(id.getValue());
					}
					if (resource.getIdElement().isLocal() && theContained.hasExistingIdToContainedResource()) {
						theContained.getExistingIdToContainedResource().remove(resource.getIdElement().getValue());
					}
				}

			}

		}

	}

	/**
	 * Iterate through the whole resource and identify any contained resources. Optionally this method
	 * can also assign IDs and modify references where the resource link has been specified but not the
	 * reference text.
	 *
	 * @since 5.4.0
	 */
	public ContainedResources containResources(IBaseResource theResource, OptionsEnum... theOptions) {
		boolean storeAndReuse = false;
		boolean modifyResource = false;
		for (OptionsEnum next : theOptions) {
			switch (next) {
				case MODIFY_RESOURCE:
					modifyResource = true;
					break;
				case STORE_AND_REUSE_RESULTS:
					storeAndReuse = true;
					break;
			}
		}

		if (storeAndReuse) {
			Object cachedValue = theResource.getUserData(USER_DATA_KEY_CONTAIN_RESOURCES_COMPLETED);
			if (cachedValue != null) {
				return (ContainedResources) cachedValue;
			}
		}

		ContainedResources contained = new ContainedResources();

		List<? extends IBaseResource> containedResources = getContainedResourceList(theResource);
		for (IBaseResource next : containedResources) {
			String nextId = next.getIdElement().getValue();
			if (StringUtils.isNotBlank(nextId)) {
				if (!nextId.startsWith("#")) {
					nextId = '#' + nextId;
				}
				next.getIdElement().setValue(nextId);
			}
			contained.addContained(next);
		}

		if (myContext.getParserOptions().isAutoContainReferenceTargetsWithNoId()) {
			containResourcesForEncoding(contained, theResource, modifyResource);
		}

		if (storeAndReuse) {
			theResource.setUserData(USER_DATA_KEY_CONTAIN_RESOURCES_COMPLETED, contained);
		}

		return contained;
	}

	@SuppressWarnings("unchecked")
	private <T extends IBaseResource> List<T> getContainedResourceList(T theResource) {
		List<T> containedResources = Collections.emptyList();
		if (theResource instanceof IResource) {
			containedResources = (List<T>) ((IResource) theResource).getContained().getContainedResources();
		} else if (theResource instanceof IDomainResource) {
			containedResources = (List<T>) ((IDomainResource) theResource).getContained();
		}
		return containedResources;
	}

	/**
	 * Adds and returns a new element at the given path within the given structure. The paths used here
	 * are <b>not FHIRPath expressions</b> but instead just simple dot-separated path expressions.
	 * <p>
	 * Only the last entry in the path is always created, existing repetitions of elements before
	 * the final dot are returned if they exists (although they are created if they do not). For example,
	 * given the path <code>Patient.name.given</code>, a new repetition of <code>given</code> is always
	 * added to the first (index 0) repetition of the name. If an index-0 repetition of <code>name</code>
	 * already exists, it is added to. If one does not exist, it if created and then added to.
	 * </p>
	 * <p>
	 * If the last element in the path refers to a non-repeatable element that is already present and
	 * is not empty, a {@link DataFormatException} error will be thrown.
	 * </p>
	 *
	 * @param theTarget The element to add to. This will often be a {@link IBaseResource resource}
	 *                  instance, but does not need to be.
	 * @param thePath   The path.
	 * @return The newly added element
	 * @throws DataFormatException If the path is invalid or does not end with either a repeatable element, or
	 *                             an element that is non-repeatable but not already populated.
	 */
	@SuppressWarnings("unchecked")
	@Nonnull
	public <T extends IBase> T addElement(@Nonnull IBase theTarget, @Nonnull String thePath) {
		return (T) doAddElement(theTarget, thePath, 1).get(0);
	}

	@SuppressWarnings("unchecked")
	private <T extends IBase> List<T> doAddElement(IBase theTarget, String thePath, int theElementsToAdd) {
		if (theElementsToAdd == 0) {
			return Collections.emptyList();
		}

		IBase target = theTarget;
		BaseRuntimeElementCompositeDefinition<?> def = (BaseRuntimeElementCompositeDefinition<?>) myContext.getElementDefinition(target.getClass());
		List<String> parts = parsePath(def, thePath);

		for (int i = 0, partsSize = parts.size(); ; i++) {
			String nextPart = parts.get(i);
			boolean lastPart = i == partsSize - 1;

			BaseRuntimeChildDefinition nextChild = def.getChildByName(nextPart);
			if (nextChild == null) {
				throw new DataFormatException(Msg.code(1796) + "Invalid path " + thePath + ": Element of type " + def.getName() + " has no child named " + nextPart + ". Valid names: " + def.getChildrenAndExtension().stream().map(t -> t.getElementName()).sorted().collect(Collectors.joining(", ")));
			}

			List<IBase> childValues = nextChild.getAccessor().getValues(target);
			IBase childValue;
			if (childValues.size() > 0 && !lastPart) {
				childValue = childValues.get(0);
			} else {

				if (lastPart) {
					if (!childValues.isEmpty()) {
						if (theElementsToAdd == -1) {
							return (List<T>) Collections.singletonList(childValues.get(0));
						} else if (nextChild.getMax() == 1 && !childValues.get(0).isEmpty()) {
							throw new DataFormatException(Msg.code(1797) + "Element at path " + thePath + " is not repeatable and not empty");
						} else if (nextChild.getMax() == 1 && childValues.get(0).isEmpty()) {
							return (List<T>) Collections.singletonList(childValues.get(0));
						}
					}
				}

				BaseRuntimeElementDefinition<?> elementDef = nextChild.getChildByName(nextPart);
				childValue = elementDef.newInstance(nextChild.getInstanceConstructorArguments());
				nextChild.getMutator().addValue(target, childValue);

				if (lastPart) {
					if (theElementsToAdd == 1 || theElementsToAdd == -1) {
						return (List<T>) Collections.singletonList(childValue);
					} else {
						if (nextChild.getMax() == 1) {
							throw new DataFormatException(Msg.code(1798) + "Can not add multiple values at path " + thePath + ": Element does not repeat");
						}

						List<T> values = (List<T>) Lists.newArrayList(childValue);
						for (int j = 1; j < theElementsToAdd; j++) {
							childValue = elementDef.newInstance(nextChild.getInstanceConstructorArguments());
							nextChild.getMutator().addValue(target, childValue);
							values.add((T) childValue);
						}

						return values;
					}
				}

			}

			target = childValue;

			if (!lastPart) {
				BaseRuntimeElementDefinition<?> nextDef = myContext.getElementDefinition(target.getClass());
				if (!(nextDef instanceof BaseRuntimeElementCompositeDefinition)) {
					throw new DataFormatException(Msg.code(1799) + "Invalid path " + thePath + ": Element of type " + def.getName() + " has no child named " + nextPart + " (this is a primitive type)");
				}
				def = (BaseRuntimeElementCompositeDefinition<?>) nextDef;
			}
		}

	}

	/**
	 * Adds and returns a new element at the given path within the given structure. The paths used here
	 * are <b>not FHIRPath expressions</b> but instead just simple dot-separated path expressions.
	 * <p>
	 * This method follows all of the same semantics as {@link #addElement(IBase, String)} but it
	 * requires the path to point to an element with a primitive datatype and set the value of
	 * the datatype to the given value.
	 * </p>
	 *
	 * @param theTarget The element to add to. This will often be a {@link IBaseResource resource}
	 *                  instance, but does not need to be.
	 * @param thePath   The path.
	 * @param theValue  The value to set, or <code>null</code>.
	 * @return The newly added element
	 * @throws DataFormatException If the path is invalid or does not end with either a repeatable element, or
	 *                             an element that is non-repeatable but not already populated.
	 */
	@SuppressWarnings("unchecked")
	@Nonnull
	public <T extends IBase> T addElement(@Nonnull IBase theTarget, @Nonnull String thePath, @Nullable String theValue) {
		T value = (T) doAddElement(theTarget, thePath, 1).get(0);
		if (!(value instanceof IPrimitiveType)) {
			throw new DataFormatException(Msg.code(1800) + "Element at path " + thePath + " is not a primitive datatype. Found: " + myContext.getElementDefinition(value.getClass()).getName());
		}

		((IPrimitiveType<?>) value).setValueAsString(theValue);

		return value;
	}


	/**
	 * Adds and returns a new element at the given path within the given structure. The paths used here
	 * are <b>not FHIRPath expressions</b> but instead just simple dot-separated path expressions.
	 * <p>
	 * This method follows all of the same semantics as {@link #addElement(IBase, String)} but it
	 * requires the path to point to an element with a primitive datatype and set the value of
	 * the datatype to the given value.
	 * </p>
	 *
	 * @param theTarget The element to add to. This will often be a {@link IBaseResource resource}
	 *                  instance, but does not need to be.
	 * @param thePath   The path.
	 * @param theValue  The value to set, or <code>null</code>.
	 * @return The newly added element
	 * @throws DataFormatException If the path is invalid or does not end with either a repeatable element, or
	 *                             an element that is non-repeatable but not already populated.
	 */
	@SuppressWarnings("unchecked")
	@Nonnull
	public <T extends IBase> T setElement(@Nonnull IBase theTarget, @Nonnull String thePath, @Nullable String theValue) {
		T value = (T) doAddElement(theTarget, thePath, -1).get(0);
		if (!(value instanceof IPrimitiveType)) {
			throw new DataFormatException(Msg.code(1801) + "Element at path " + thePath + " is not a primitive datatype. Found: " + myContext.getElementDefinition(value.getClass()).getName());
		}

		((IPrimitiveType<?>) value).setValueAsString(theValue);

		return value;
	}


	/**
	 * This method has the same semantics as {@link #addElement(IBase, String, String)} but adds
	 * a collection of primitives instead of a single one.
	 *
	 * @param theTarget The element to add to. This will often be a {@link IBaseResource resource}
	 *                  instance, but does not need to be.
	 * @param thePath   The path.
	 * @param theValues The values to set, or <code>null</code>.
	 */
	public void addElements(IBase theTarget, String thePath, Collection<String> theValues) {
		List<IBase> targets = doAddElement(theTarget, thePath, theValues.size());
		Iterator<String> valuesIter = theValues.iterator();
		for (IBase target : targets) {

			if (!(target instanceof IPrimitiveType)) {
				throw new DataFormatException(Msg.code(1802) + "Element at path " + thePath + " is not a primitive datatype. Found: " + myContext.getElementDefinition(target.getClass()).getName());
			}

			((IPrimitiveType<?>) target).setValueAsString(valuesIter.next());
		}

	}

	/**
	 * Clones a resource object, copying all data elements from theSource into a new copy of the same type.
	 * <p>
	 * Note that:
	 * <ul>
	 *    <li>Only FHIR data elements are copied (i.e. user data maps are not copied)</li>
	 *    <li>If a class extending a HAPI FHIR type (e.g. an instance of a class extending the Patient class) is supplied, an instance of the base type will be returned.</li>
	 * </ul>
	 *
	 * @param theSource The source resource
	 * @return A copy of the source resource
	 * @since 5.6.0
	 */
	@SuppressWarnings("unchecked")
	public <T extends IBaseResource> T clone(T theSource) {
		Validate.notNull(theSource, "theSource must not be null");
		T target = (T) myContext.getResourceDefinition(theSource).newInstance();
		cloneInto(theSource, target, false);
		return target;
	}


	public enum OptionsEnum {

		/**
		 * Should we modify the resource in the case that contained resource IDs are assigned
		 * during a {@link #containResources(IBaseResource, OptionsEnum...)} pass.
		 */
		MODIFY_RESOURCE,

		/**
		 * Store the results of the operation in the resource metadata and reuse them if
		 * subsequent calls are made.
		 */
		STORE_AND_REUSE_RESULTS
	}

	public static class ContainedResources {
		private long myNextContainedId = 1;

		private List<IBaseResource> myResourceList;
		private IdentityHashMap<IBaseResource, IIdType> myResourceToIdMap;
		private Map<String, IBaseResource> myExistingIdToContainedResourceMap;

		public Map<String, IBaseResource> getExistingIdToContainedResource() {
			if (myExistingIdToContainedResourceMap == null) {
				myExistingIdToContainedResourceMap = new HashMap<>();
			}
			return myExistingIdToContainedResourceMap;
		}

		public IIdType addContained(IBaseResource theResource) {
			IIdType existing = getResourceToIdMap().get(theResource);
			if (existing != null) {
				return existing;
			}

			IIdType newId = theResource.getIdElement();
			if (isBlank(newId.getValue())) {
				newId.setValue("#" + myNextContainedId++);
			} else {
				// Avoid auto-assigned contained IDs colliding with pre-existing ones
				String idPart = newId.getValue();
				if (substring(idPart, 0, 1).equals("#")) {
					idPart = idPart.substring(1);
					if (StringUtils.isNumeric(idPart)) {
						myNextContainedId = Long.parseLong(idPart) + 1;
					}
				}
			}

			getResourceToIdMap().put(theResource, newId);
			getOrCreateResourceList().add(theResource);
			return newId;
		}

		public void addContained(IIdType theId, IBaseResource theResource) {
			if (!getResourceToIdMap().containsKey(theResource)) {
				getResourceToIdMap().put(theResource, theId);
				getOrCreateResourceList().add(theResource);
			}
		}

		public List<IBaseResource> getContainedResources() {
			if (getResourceToIdMap() == null) {
				return Collections.emptyList();
			}
			return getOrCreateResourceList();
		}

		public IIdType getResourceId(IBaseResource theNext) {
			if (getResourceToIdMap() == null) {
				return null;
			}
			return getResourceToIdMap().get(theNext);
		}

		private List<IBaseResource> getOrCreateResourceList() {
			if (myResourceList == null) {
				myResourceList = new ArrayList<>();
			}
			return myResourceList;
		}

		private IdentityHashMap<IBaseResource, IIdType> getResourceToIdMap() {
			if (myResourceToIdMap == null) {
				myResourceToIdMap = new IdentityHashMap<>();
			}
			return myResourceToIdMap;
		}

		public boolean isEmpty() {
			if (myResourceToIdMap == null) {
				return true;
			}
			return myResourceToIdMap.isEmpty();
		}

		public boolean hasExistingIdToContainedResource() {
			return myExistingIdToContainedResourceMap != null;
		}

		public void assignIdsToContainedResources() {

			if (!getContainedResources().isEmpty()) {

				/*
				 * The idea with the code block below:
				 *
				 * We want to preserve any IDs that were user-assigned, so that if it's really
				 * important to someone that their contained resource have the ID of #FOO
				 * or #1 we will keep that.
				 *
				 * For any contained resources where no ID was assigned by the user, we
				 * want to manually create an ID but make sure we don't reuse an existing ID.
				 */

				Set<String> ids = new HashSet<>();

				// Gather any user assigned IDs
				for (IBaseResource nextResource : getContainedResources()) {
					if (getResourceToIdMap().get(nextResource) != null) {
						ids.add(getResourceToIdMap().get(nextResource).getValue());
					}
				}

				// Automatically assign IDs to the rest
				for (IBaseResource nextResource : getContainedResources()) {

					while (getResourceToIdMap().get(nextResource) == null) {
						String nextCandidate = "#" + myNextContainedId;
						myNextContainedId++;
						if (!ids.add(nextCandidate)) {
							continue;
						}

						getResourceToIdMap().put(nextResource, new IdDt(nextCandidate));
					}

				}

			}

		}
	}

}
