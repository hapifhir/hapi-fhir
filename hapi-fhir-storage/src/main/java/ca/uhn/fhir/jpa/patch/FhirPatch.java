/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.patch;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.fhirpath.IFhirPath;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.parser.path.EncodeContextPath;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.IModelVisitor2;
import ca.uhn.fhir.util.ParametersUtil;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBooleanDatatype;
import org.hl7.fhir.instance.model.api.IBaseEnumeration;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.utilities.xhtml.XhtmlNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.defaultString;

public class FhirPatch {
	org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirPatch.class);

	public static final String OPERATION_ADD = "add";
	public static final String OPERATION_DELETE = "delete";
	public static final String OPERATION_INSERT = "insert";
	public static final String OPERATION_MOVE = "move";
	public static final String OPERATION_REPLACE = "replace";
	public static final String PARAMETER_DESTINATION = "destination";
	public static final String PARAMETER_INDEX = "index";
	public static final String PARAMETER_NAME = "name";
	public static final String PARAMETER_OPERATION = "operation";
	public static final String PARAMETER_PATH = "path";
	public static final String PARAMETER_SOURCE = "source";
	public static final String PARAMETER_TYPE = "type";
	public static final String PARAMETER_VALUE = "value";

	private final FhirContext myContext;
	private boolean myIncludePreviousValueInDiff;
	private Set<EncodeContextPath> myIgnorePaths = Collections.emptySet();

	public FhirPatch(FhirContext theContext) {
		myContext = theContext;
	}

	/**
	 * Adds a path element that will not be included in generated diffs. Values can take the form
	 * <code>ResourceName.fieldName.fieldName</code> and wildcards are supported, such
	 * as <code>*.meta</code>.
	 */
	public void addIgnorePath(String theIgnorePath) {
		Validate.notBlank(theIgnorePath, "theIgnorePath must not be null or empty");

		if (myIgnorePaths.isEmpty()) {
			myIgnorePaths = new HashSet<>();
		}
		myIgnorePaths.add(new EncodeContextPath(theIgnorePath));
	}

	public void setIncludePreviousValueInDiff(boolean theIncludePreviousValueInDiff) {
		myIncludePreviousValueInDiff = theIncludePreviousValueInDiff;
	}

	public void apply(IBaseResource theResource, IBaseResource thePatch) {

		List<IBase> opParameters = ParametersUtil.getNamedParameters(myContext, thePatch, PARAMETER_OPERATION);
		for (IBase nextOperation : opParameters) {
			String type = ParametersUtil.getParameterPartValueAsString(myContext, nextOperation, PARAMETER_TYPE);
			type = defaultString(type);

			if (OPERATION_DELETE.equals(type)) {
				handleDeleteOperation(theResource, nextOperation);
			} else if (OPERATION_ADD.equals(type)) {
				handleAddOperation(theResource, nextOperation);
			} else if (OPERATION_REPLACE.equals(type)) {
				handleReplaceOperation(theResource, nextOperation);
			} else if (OPERATION_INSERT.equals(type)) {
				handleInsertOperation(theResource, nextOperation);
			} else if (OPERATION_MOVE.equals(type)) {
				handleMoveOperation(theResource, nextOperation);
			} else {
				throw new InvalidRequestException(Msg.code(1267) + "Unknown patch operation type: " + type);
			}
		}
	}

	private void handleAddOperation(IBaseResource theResource, IBase theParameters) {

		String path = ParametersUtil.getParameterPartValueAsString(myContext, theParameters, PARAMETER_PATH);
		String elementName = ParametersUtil.getParameterPartValueAsString(myContext, theParameters, PARAMETER_NAME);

		String containingPath = defaultString(path);

		List<IBase> containingElements = myContext.newFhirPath().evaluate(theResource, containingPath, IBase.class);
		for (IBase nextElement : containingElements) {
			ChildDefinition childDefinition = findChildDefinition(nextElement, elementName);

			IBase newValue = getNewValue(theParameters, childDefinition);

			childDefinition.getUseableChildDef().getMutator().addValue(nextElement, newValue);
		}
	}

	private void handleInsertOperation(IBaseResource theResource, IBase theParameters) {

		String path = ParametersUtil.getParameterPartValueAsString(myContext, theParameters, PARAMETER_PATH);
		path = defaultString(path);

		int lastDot = path.lastIndexOf(".");
		String containingPath = path.substring(0, lastDot);
		String elementName = path.substring(lastDot + 1);
		Integer insertIndex = ParametersUtil.getParameterPartValueAsInteger(myContext, theParameters, PARAMETER_INDEX)
				.orElseThrow(() -> new InvalidRequestException("No index supplied for insert operation"));

		List<IBase> containingElements = myContext.newFhirPath().evaluate(theResource, containingPath, IBase.class);
		for (IBase nextElement : containingElements) {

			ChildDefinition childDefinition = findChildDefinition(nextElement, elementName);

			IBase newValue = getNewValue(theParameters, childDefinition);

			List<IBase> existingValues =
					new ArrayList<>(childDefinition.getUseableChildDef().getAccessor().getValues(nextElement));
			if (insertIndex == null || insertIndex < 0 || insertIndex > existingValues.size()) {
				String msg = myContext
						.getLocalizer()
						.getMessage(FhirPatch.class, "invalidInsertIndex", insertIndex, path, existingValues.size());
				throw new InvalidRequestException(Msg.code(1270) + msg);
			}
			existingValues.add(insertIndex, newValue);

			childDefinition.getUseableChildDef().getMutator().setValue(nextElement, null);
			for (IBase nextNewValue : existingValues) {
				childDefinition.getUseableChildDef().getMutator().addValue(nextElement, nextNewValue);
			}
		}
	}

	private void handleDeleteOperation(IBaseResource theResource, IBase theParameters) {
		String path = ParametersUtil.getParameterPartValueAsString(myContext, theParameters, PARAMETER_PATH);
		path = defaultString(path);

		ParsedFhirPath parsedFhirPath = ParsedFhirPath.parse(path);
		List<IBase> containingElements = myContext
				.newFhirPath()
				.evaluate(
						theResource,
						parsedFhirPath.endsWithFilterOrIndex() ? parsedFhirPath.getContainingPath() : path,
						IBase.class);

		for (IBase nextElement : containingElements) {
			if (parsedFhirPath.endsWithFilterOrIndex()) {
				// if the path ends with a filter or index, we must be dealing with a list
				deleteFromList(theResource, nextElement, parsedFhirPath.getLastElementName(), path);
			} else {
				deleteSingleElement(nextElement);
			}
		}
	}

	private void deleteFromList(
			IBaseResource theResource,
			IBase theContainingElement,
			String theListElementName,
			String theElementToDeletePath) {
		ChildDefinition childDefinition = findChildDefinition(theContainingElement, theListElementName);

		List<IBase> existingValues =
				new ArrayList<>(childDefinition.getUseableChildDef().getAccessor().getValues(theContainingElement));
		List<IBase> elementsToRemove =
				myContext.newFhirPath().evaluate(theResource, theElementToDeletePath, IBase.class);
		existingValues.removeAll(elementsToRemove);

		childDefinition.getUseableChildDef().getMutator().setValue(theContainingElement, null);
		for (IBase nextNewValue : existingValues) {
			childDefinition.getUseableChildDef().getMutator().addValue(theContainingElement, nextNewValue);
		}
	}

	private void handleReplaceOperation(IBaseResource theResource, IBase theParameters) {
		String path = ParametersUtil.getParameterPartValueAsString(myContext, theParameters, PARAMETER_PATH);
		path = defaultString(path);

		ParsedFhirPath parsedFhirPath = ParsedFhirPath.parse(path);

		IFhirPath fhirPath = myContext.newFhirPath();
		String lastEl = parsedFhirPath.getLastElementName();

		List<IBase> containingElements =
			getElementsToUpdate(theResource, parsedFhirPath, fhirPath);

		for (IBase containingElement : containingElements) {
			IBase elementToUse = containingElement;

			// get current element's child definition so we can construct the new value
			ChildDefinition childDefinition = findChildDefinition(containingElement, lastEl);

			// if there's no child def, it's because we've hit a primitive node
			if (!childDefinition.hasChildDef()) {
				elementToUse = processPrimitiveNode(theResource, parsedFhirPath, fhirPath, childDefinition);
			}

			IBase newValue = getNewValue(theParameters, childDefinition);

			// this is assuming a complex non-primitive obj; need to handle primitive types too
			if (parsedFhirPath.endsWithFilterOrIndex()) {
				// if the path ends with a filter or index, we must be dealing with a list
				replaceInList(newValue, theResource, elementToUse, childDefinition, parsedFhirPath);
			} else {
				childDefinition.getUseableChildDef().getMutator().setValue(elementToUse, newValue);
			}
		}
	}

	/**
	 * Determines if the node is a subsetting node
	 * as described by http://hl7.org/fhirpath/N1/#subsetting
	 */
	private boolean isSubsettingNode(ParsedFhirPath.FhirPathNode theNode) {
		if (theNode.getListIndex() >= 0) {
			return true;
		}
		if (theNode.isFunction()) {
			String funName = theNode.getValue();
			switch (funName) {
				case "first", "last", "single", "tail", "skip", "take", "exclude", "intersect" -> {
					return true;
				}
			}
		}
		return false;
	}

	private List<IBase> filterDown(List<IBase> theList, IFhirPath theFhirPath, ParsedFhirPath theParsed) {
		if (!theParsed.endsWithFilterOrIndex()) {
			// no filter, no index - just return the list as is
			return theList;
		} if (!isSubsettingNode(theParsed.getTail())) {
			/*
			 * Not a subsetting node, so we can just apply the filter on
			 * the list we currently have and return that
			 */
			return applyFilter(theList, theFhirPath, theParsed);
		} else {
			// A subsetting node or index (so has to be evaluated on a list of elements)
			ParsedFhirPath.FhirPathNode tail = theParsed.getTail();

			if (tail.isFunction() && (tail.getValue().equals("intersect") || tail.getValue().equals("exclude"))) {
				// TODO - need to support this; but out of scope for now
				/*
				 * We would have to further filter down these specific functions since their arguments
				 * (the contained path of the final nodes) have to be evaluated first
				 */
				ourLog.error("The functions 'exclude' and 'intersect' are not supported for patching. No patching will be done");
				return List.of(); // empty list so nothing is patched
			}

			// apply the filter
			// to the path, minus the final node (which is used for filtering here)
			String rawPath = theParsed.getRawPath();
			String tailValue = tail.hasListIndex() ? "[" + tail.getListIndex() + "]" : tail.getValue();
			int endIndex = rawPath.indexOf(tailValue);
			String newPath = rawPath.substring(0, endIndex);
			if (newPath.endsWith(".")) {
				newPath = newPath.substring(0, newPath.length() - 1);
			}
			List<IBase> filtered = filterDown(theList, theFhirPath, ParsedFhirPath.parse(newPath));

			if (tail.getListIndex() >= 0) {
				// specific index
				if (tail.getListIndex() < filtered.size()) {
					return List.of(filtered.get(tail.getListIndex()));
				} else {
					ourLog.info("Nothing matching index {}; nothing patched.", tail.getListIndex());
					return List.of();
				}
			} else {
				if (filtered.isEmpty()) {
					// empty lists should match all filters, so we'll return it here
					ourLog.info("List contains no elements; no patching will occur");
					return List.of();
				}

				switch (tail.getValue()) {
					case "first" -> {
						return List.of(filtered.get(0));
					}
					case "last" -> {
						return List.of(filtered.get(filtered.size() - 1));
					}
					case "tail" -> {
						if (filtered.size() == 1) {
							ourLog.info("List contains only a single element - no patching will occur");
							return List.of();
						}
						return filtered.subList(1, filtered.size());
					}
					case "single" -> {
						if (filtered.size() != 1) {
							throw new InvalidRequestException(Msg.code(2710) + " List contains more than a single element.");
						}
						// only one element
						return filtered;
					}
					case "skip", "take" -> {
						if (tail instanceof ParsedFhirPath.FhirPathFunction fn) {
							String containedNum = fn.getContainedExp().getHead().getValue();
							try {
								int num = Integer.parseInt(containedNum);

								if (tail.getValue().equals("skip")) {
									if (num < filtered.size()) {
										return filtered.subList(num, filtered.size());
									}
								} else if (tail.getValue().equals("take")) {
									if (num < filtered.size()) {
										return filtered.subList(0, num);
									} else {
										// otherwise, return everything
										return filtered;
									}
								}

								return List.of();
							} catch (NumberFormatException ex) {
								ourLog.error("{} is not a number", containedNum, ex);
							}
						}
						throw new InvalidRequestException(Msg.code(2712) + " Invalid fhir path element encountered: " + theParsed.getRawPath());
					}
					default -> {
						// we shouldn't see this; it means we have not handled a filtering case
						throw new InvalidRequestException(Msg.code(2711) + " Unrecognized filter of type " + tail.getValue());
					}
				}
			}
		}
	}

	private List<IBase> applyFilter(List<IBase> theList, IFhirPath theFhirPath, ParsedFhirPath theParsed) {
		String lastEl = theParsed.getLastElementName();
		String path = theParsed.getRawPath().substring(theParsed.getRawPath().indexOf(lastEl));

		return theList.stream().filter(item -> {
			// apply the filter to determine if this item matches it
			Optional<IBase> matchedOp = theFhirPath.evaluateFirst(item, path, IBase.class);
			return matchedOp.isPresent();
		}).toList();
	}

	/**
	 * Returns a list of IBase elements to be updated (ie, the ones that match the FhirPath).
	 * @param theResource the parent resource
	 * @param theParsedFhirPath the parsed fhir path
	 * @param theFhirPath the fhirpath parser
	 * @return a list of elements to update
	 */
	private List<IBase> getElementsToUpdate(IBaseResource theResource, ParsedFhirPath theParsedFhirPath, IFhirPath theFhirPath) {
		String containedPath = theParsedFhirPath.getContainingPath();

		// these are the parent elements at the highest possible level in the fhirpath
		// ie, everything will be contained within these and we need to filter them down
		List<IBase> conatinedEls = theFhirPath.evaluate(theResource, containedPath, IBase.class);

		return filterDown(conatinedEls, theFhirPath, theParsedFhirPath);
	}

	/**
	 * We are at a primitive node (stringtype, inttype, etc).
	 * -
	 * In order to update the value, we must actually target the parent
	 * of this node, with this node being the child to target.
	 * -
	 * Eg:
	 * Given the path
	 * "Resource.path.to.node"
	 * -
	 * The parent path is "Resource.path.to"
	 * The child node to target is named "node"
	 * -
	 * The RuntimeChildDefinition at "Resource.path.to" will have
	 * the accessor we need to use to update the value.
	 *
	 * @param theResource - the Resource we're trying to alter
	 * @param parsedPath - a parsed path object defining the fhirpath on the Resource we are targetting
	 * @param fhirPath - A fhirpath object, used for parsing
	 * @param childDefinition - the child definition obtained by the fhirpath parsing.
	 *
	 * @return The target element; ie, the element that needs to have its value updated.
	 */
	private IBase processPrimitiveNode(IBaseResource theResource, ParsedFhirPath parsedPath, IFhirPath fhirPath, ChildDefinition childDefinition) {
		IBase elementToUse;

		String lastChild = parsedPath.getLastElementName();
		String parentPath = parsedPath.getPathUntilPreCondition(n -> n.getValue().equals(lastChild));

		// now we update the target element as the child element of the parent
		List<IBase> parentPathChildDef = fhirPath.evaluate(theResource, parentPath, IBase.class);
		if (parentPathChildDef == null || parentPathChildDef.isEmpty()) {
			// we should never see this
			throw new InvalidRequestException(Msg.code(2704) + " No elements found at path " + parentPath);
		}
		elementToUse = parentPathChildDef.get(0);

		BaseRuntimeElementDefinition<?> parentDef = myContext.getElementDefinition(elementToUse.getClass());

		// set the RuntimeChildDefinition to use
		// to be the child of the parent with the
		childDefinition.setParentDef(
			parentDef.getChildByName(lastChild)
		);
		return elementToUse;
	}

	private void replaceInList(
			IBase theNewValue,
			IBaseResource theResource,
			IBase theContainingElement,
			ChildDefinition theChildDefinitionForTheList,
			ParsedFhirPath thePath) {

		String theFullReplacePath = thePath.getContainingPath() + "." + thePath.getLastElementName();

		List<IBase> existingValues = new ArrayList<>();

		/*
		 * If there's a parent def, that means
		 * we're dealing with a primitive type.
		 */
		if (theChildDefinitionForTheList.hasParentDef()) {
			// primitive
			existingValues.addAll(theChildDefinitionForTheList.getUseableChildDef()
				.getAccessor().getValues(theContainingElement));
		} else {
			// non-primitive
			existingValues.addAll(
				theChildDefinitionForTheList.getUseableChildDef().getAccessor().getValues(theContainingElement));
		}

		List<IBase> valuesToReplace = myContext.newFhirPath().evaluate(theResource, theFullReplacePath, IBase.class);
		if (valuesToReplace.isEmpty()) {
			String msg = myContext
					.getLocalizer()
					.getMessage(FhirPatch.class, "noMatchingElementForPath", theFullReplacePath);
			throw new InvalidRequestException(Msg.code(2617) + msg);
		}

		BaseRuntimeChildDefinition.IMutator listMutator =
				theChildDefinitionForTheList.getUseableChildDef().getMutator();
		// clear the whole list first, then reconstruct it in the loop below replacing the values that need to be
		// replaced
		listMutator.setValue(theContainingElement, null);
		for (IBase existingValue : existingValues) {
			if (valuesToReplace.contains(existingValue)) {
				listMutator.addValue(theContainingElement, theNewValue);
			} else {
				listMutator.addValue(theContainingElement, existingValue);
			}
		}
	}

	private void handleMoveOperation(IBaseResource theResource, IBase theParameters) {
		String path = ParametersUtil.getParameterPartValueAsString(myContext, theParameters, PARAMETER_PATH);
		path = defaultString(path);

		int lastDot = path.lastIndexOf(".");
		String containingPath = path.substring(0, lastDot);
		String elementName = path.substring(lastDot + 1);
		Integer insertIndex = ParametersUtil.getParameterPartValueAsInteger(
						myContext, theParameters, PARAMETER_DESTINATION)
				.orElseThrow(() -> new InvalidRequestException("No index supplied for move operation"));
		Integer removeIndex = ParametersUtil.getParameterPartValueAsInteger(myContext, theParameters, PARAMETER_SOURCE)
				.orElseThrow(() -> new InvalidRequestException("No index supplied for move operation"));

		List<IBase> containingElements = myContext.newFhirPath().evaluate(theResource, containingPath, IBase.class);
		for (IBase nextElement : containingElements) {

			ChildDefinition childDefinition = findChildDefinition(nextElement, elementName);

			List<IBase> existingValues =
					new ArrayList<>(childDefinition.getUseableChildDef().getAccessor().getValues(nextElement));
			if (removeIndex == null || removeIndex < 0 || removeIndex >= existingValues.size()) {
				String msg = myContext
						.getLocalizer()
						.getMessage(
								FhirPatch.class, "invalidMoveSourceIndex", removeIndex, path, existingValues.size());
				throw new InvalidRequestException(Msg.code(1268) + msg);
			}
			IBase newValue = existingValues.remove(removeIndex.intValue());

			if (insertIndex == null || insertIndex < 0 || insertIndex > existingValues.size()) {
				String msg = myContext
						.getLocalizer()
						.getMessage(
								FhirPatch.class,
								"invalidMoveDestinationIndex",
								insertIndex,
								path,
								existingValues.size());
				throw new InvalidRequestException(Msg.code(1269) + msg);
			}
			existingValues.add(insertIndex, newValue);

			childDefinition.getUseableChildDef().getMutator().setValue(nextElement, null);
			for (IBase nextNewValue : existingValues) {
				childDefinition.getUseableChildDef().getMutator().addValue(nextElement, nextNewValue);
			}
		}
	}

	private ChildDefinition findChildDefinition(IBase theContainingElement, String theElementName) {
		BaseRuntimeElementDefinition<?> elementDef = myContext.getElementDefinition(theContainingElement.getClass());

		if (elementDef.getChildType() == BaseRuntimeElementDefinition.ChildTypeEnum.PRIMITIVE_DATATYPE) {
			// if it's a primitive, there is no child runtime definition to target
			return new ChildDefinition(null, elementDef);
		}

		String childName = theElementName;
		BaseRuntimeChildDefinition childDef = elementDef.getChildByName(childName);
		BaseRuntimeElementDefinition<?> childElement;
		if (childDef == null) {
			childName = theElementName + "[x]";
			childDef = elementDef.getChildByName(childName);
			childElement = childDef.getChildByName(
					childDef.getValidChildNames().iterator().next());
		} else {
			childElement = childDef.getChildByName(childName);
		}

		return new ChildDefinition(childDef, childElement);
	}

	private IBase getNewValue(IBase theParameters, ChildDefinition theChildDefinition) {
		Optional<IBase> valuePart = ParametersUtil.getParameterPart(myContext, theParameters, PARAMETER_VALUE);
		Optional<IBase> valuePartValue =
				ParametersUtil.getParameterPartValue(myContext, theParameters, PARAMETER_VALUE);

		IBase newValue;
		if (valuePartValue.isPresent()) {
			newValue = maybeMassageToEnumeration(valuePartValue.get(), theChildDefinition);

		} else {
			List<IBase> partParts = valuePart.map(this::extractPartsFromPart).orElse(Collections.emptyList());

			newValue = createAndPopulateNewElement(theChildDefinition, partParts);
		}

		return newValue;
	}

	private IBase maybeMassageToEnumeration(IBase theValue, ChildDefinition theChildDefinition) {
		IBase retVal = theValue;

		if (IBaseEnumeration.class.isAssignableFrom(
						theChildDefinition.getChildElement().getImplementingClass())
				|| XhtmlNode.class.isAssignableFrom(
						theChildDefinition.getChildElement().getImplementingClass())) {
			// If the compositeElementDef is an IBaseEnumeration, we will use the actual compositeElementDef definition
			// to build one, since
			// it needs the right factory object passed to its constructor
			IPrimitiveType<?> newValueInstance;
			if (theChildDefinition.getUseableChildDef().getInstanceConstructorArguments() != null) {
				newValueInstance = (IPrimitiveType<?>) theChildDefinition
						.getChildElement()
						.newInstance(theChildDefinition.getUseableChildDef().getInstanceConstructorArguments());
			} else {
				newValueInstance =
						(IPrimitiveType<?>) theChildDefinition.getChildElement().newInstance();
			}
			newValueInstance.setValueAsString(((IPrimitiveType<?>) theValue).getValueAsString());
			retVal = newValueInstance;
		}

		return retVal;
	}

	@Nonnull
	private List<IBase> extractPartsFromPart(IBase theParametersParameterComponent) {
		return myContext.newTerser().getValues(theParametersParameterComponent, "part");
	}

	/**
	 * this method will instantiate an element according to the provided Definition and initialize its fields
	 * from the values provided in thePartParts.  a part usually represent a datatype as a name/value[X] pair.
	 * it may also represent a complex type like an Extension.
	 *
	 * @param theDefinition wrapper around the runtime definition of the element to be populated
	 * @param thePartParts list of Part to populate the element that will be created from theDefinition
	 * @return an element that was created from theDefinition and populated with the parts
	 */
	private IBase createAndPopulateNewElement(ChildDefinition theDefinition, List<IBase> thePartParts) {
		IBase newElement = theDefinition.getChildElement().newInstance();

		for (IBase nextValuePartPart : thePartParts) {

			String name = myContext
					.newTerser()
					.getSingleValue(nextValuePartPart, PARAMETER_NAME, IPrimitiveType.class)
					.map(IPrimitiveType::getValueAsString)
					.orElse(null);

			if (StringUtils.isBlank(name)) {
				continue;
			}

			Optional<IBase> optionalValue =
					myContext.newTerser().getSingleValue(nextValuePartPart, "value[x]", IBase.class);

			if (optionalValue.isPresent()) {
				// we have a dataType. let's extract its value and assign it.

				ChildDefinition childDefinition = findChildDefinition(newElement, name);
				IBase newValue = maybeMassageToEnumeration(optionalValue.get(), childDefinition);

				BaseRuntimeChildDefinition partChildDef =
						theDefinition.getChildElement().getChildByName(name);

				if (isNull(partChildDef)) {
					name = name + "[x]";
					partChildDef = theDefinition.getChildElement().getChildByName(name);
				}

				partChildDef.getMutator().setValue(newElement, newValue);

				// a part represent a datatype or a complexType but not both at the same time.
				continue;
			}

			List<IBase> part = extractPartsFromPart(nextValuePartPart);

			if (!part.isEmpty()) {
				// we have a complexType.  let's find its definition and recursively process
				// them till all complexTypes are processed.
				ChildDefinition childDefinition = findChildDefinition(newElement, name);

				IBase childNewValue = createAndPopulateNewElement(childDefinition, part);

				childDefinition.getUseableChildDef().getMutator().setValue(newElement, childNewValue);
			}
		}

		return newElement;
	}

	private void deleteSingleElement(IBase theElementToDelete) {
		myContext.newTerser().visit(theElementToDelete, new IModelVisitor2() {
			@Override
			public boolean acceptElement(
					IBase theElement,
					List<IBase> theContainingElementPath,
					List<BaseRuntimeChildDefinition> theChildDefinitionPath,
					List<BaseRuntimeElementDefinition<?>> theElementDefinitionPath) {
				if (theElement instanceof IPrimitiveType) {
					((IPrimitiveType<?>) theElement).setValueAsString(null);
				}
				return true;
			}
		});
	}

	public IBaseParameters diff(@Nullable IBaseResource theOldValue, @Nonnull IBaseResource theNewValue) {
		IBaseParameters retVal = ParametersUtil.newInstance(myContext);
		String newValueTypeName = myContext.getResourceDefinition(theNewValue).getName();

		if (theOldValue == null) {
			IBase operation = ParametersUtil.addParameterToParameters(myContext, retVal, PARAMETER_OPERATION);
			ParametersUtil.addPartCode(myContext, operation, PARAMETER_TYPE, OPERATION_INSERT);
			ParametersUtil.addPartString(myContext, operation, PARAMETER_PATH, newValueTypeName);
			ParametersUtil.addPart(myContext, operation, PARAMETER_VALUE, theNewValue);
		} else {
			String oldValueTypeName =
					myContext.getResourceDefinition(theOldValue).getName();
			Validate.isTrue(oldValueTypeName.equalsIgnoreCase(newValueTypeName), "Resources must be of same type");

			BaseRuntimeElementCompositeDefinition<?> def =
					myContext.getResourceDefinition(theOldValue).getBaseDefinition();
			String path = def.getName();

			EncodeContextPath contextPath = new EncodeContextPath();
			contextPath.pushPath(path, true);

			compare(retVal, contextPath, def, path, path, theOldValue, theNewValue);

			contextPath.popPath();
			assert contextPath.getPath().isEmpty();
		}

		return retVal;
	}

	private void compare(
			IBaseParameters theDiff,
			EncodeContextPath theSourceEncodeContext,
			BaseRuntimeElementDefinition<?> theDef,
			String theSourcePath,
			String theTargetPath,
			IBase theOldField,
			IBase theNewField) {

		boolean pathIsIgnored = pathIsIgnored(theSourceEncodeContext);
		if (pathIsIgnored) {
			return;
		}

		BaseRuntimeElementDefinition<?> sourceDef = myContext.getElementDefinition(theOldField.getClass());
		BaseRuntimeElementDefinition<?> targetDef = myContext.getElementDefinition(theNewField.getClass());
		if (!sourceDef.getName().equals(targetDef.getName())) {
			IBase operation = ParametersUtil.addParameterToParameters(myContext, theDiff, PARAMETER_OPERATION);
			ParametersUtil.addPartCode(myContext, operation, PARAMETER_TYPE, OPERATION_REPLACE);
			ParametersUtil.addPartString(myContext, operation, PARAMETER_PATH, theTargetPath);
			addValueToDiff(operation, theOldField, theNewField);
		} else {
			if (theOldField instanceof IPrimitiveType) {
				IPrimitiveType<?> oldPrimitive = (IPrimitiveType<?>) theOldField;
				IPrimitiveType<?> newPrimitive = (IPrimitiveType<?>) theNewField;
				String oldValueAsString = toValue(oldPrimitive);
				String newValueAsString = toValue(newPrimitive);
				if (!Objects.equals(oldValueAsString, newValueAsString)) {
					IBase operation = ParametersUtil.addParameterToParameters(myContext, theDiff, PARAMETER_OPERATION);
					ParametersUtil.addPartCode(myContext, operation, PARAMETER_TYPE, OPERATION_REPLACE);
					ParametersUtil.addPartString(myContext, operation, PARAMETER_PATH, theTargetPath);
					addValueToDiff(operation, oldPrimitive, newPrimitive);
				}
			}

			List<BaseRuntimeChildDefinition> children = theDef.getChildren();
			for (BaseRuntimeChildDefinition nextChild : children) {
				compareField(
						theDiff,
						theSourceEncodeContext,
						theSourcePath,
						theTargetPath,
						theOldField,
						theNewField,
						nextChild);
			}
		}
	}

	private void compareField(
			IBaseParameters theDiff,
			EncodeContextPath theSourceEncodePath,
			String theSourcePath,
			String theTargetPath,
			IBase theOldField,
			IBase theNewField,
			BaseRuntimeChildDefinition theChildDef) {
		String elementName = theChildDef.getElementName();
		boolean repeatable = theChildDef.getMax() != 1;
		theSourceEncodePath.pushPath(elementName, false);
		if (pathIsIgnored(theSourceEncodePath)) {
			theSourceEncodePath.popPath();
			return;
		}

		List<? extends IBase> sourceValues = theChildDef.getAccessor().getValues(theOldField);
		List<? extends IBase> targetValues = theChildDef.getAccessor().getValues(theNewField);

		int sourceIndex = 0;
		int targetIndex = 0;
		while (sourceIndex < sourceValues.size() && targetIndex < targetValues.size()) {

			IBase sourceChildField = sourceValues.get(sourceIndex);
			Validate.notNull(sourceChildField); // not expected to happen, but just in case
			BaseRuntimeElementDefinition<?> def = myContext.getElementDefinition(sourceChildField.getClass());
			IBase targetChildField = targetValues.get(targetIndex);
			Validate.notNull(targetChildField); // not expected to happen, but just in case
			String sourcePath = theSourcePath + "." + elementName + (repeatable ? "[" + sourceIndex + "]" : "");
			String targetPath = theSourcePath + "." + elementName + (repeatable ? "[" + targetIndex + "]" : "");

			compare(theDiff, theSourceEncodePath, def, sourcePath, targetPath, sourceChildField, targetChildField);

			sourceIndex++;
			targetIndex++;
		}

		// Find newly inserted items
		while (targetIndex < targetValues.size()) {
			String path = theTargetPath + "." + elementName;
			addInsertItems(theDiff, targetValues, targetIndex, path, theChildDef);
			targetIndex++;
		}

		// Find deleted items
		while (sourceIndex < sourceValues.size()) {
			IBase operation = ParametersUtil.addParameterToParameters(myContext, theDiff, PARAMETER_OPERATION);
			ParametersUtil.addPartCode(myContext, operation, PARAMETER_TYPE, OPERATION_DELETE);
			ParametersUtil.addPartString(
					myContext,
					operation,
					PARAMETER_PATH,
					theTargetPath + "." + elementName + (repeatable ? "[" + targetIndex + "]" : ""));

			sourceIndex++;
			targetIndex++;
		}

		theSourceEncodePath.popPath();
	}

	private void addInsertItems(
			IBaseParameters theDiff,
			List<? extends IBase> theTargetValues,
			int theTargetIndex,
			String thePath,
			BaseRuntimeChildDefinition theChildDefinition) {
		IBase operation = ParametersUtil.addParameterToParameters(myContext, theDiff, PARAMETER_OPERATION);
		ParametersUtil.addPartCode(myContext, operation, PARAMETER_TYPE, OPERATION_INSERT);
		ParametersUtil.addPartString(myContext, operation, PARAMETER_PATH, thePath);
		ParametersUtil.addPartInteger(myContext, operation, PARAMETER_INDEX, theTargetIndex);

		IBase value = theTargetValues.get(theTargetIndex);
		BaseRuntimeElementDefinition<?> valueDef = myContext.getElementDefinition(value.getClass());

		/*
		 * If the value is a Resource or a datatype, we can put it into the part.value and that will cover
		 * all of its children. If it's an infrastructure element though, such as Patient.contact we can't
		 * just put it into part.value because it isn't an actual type. So we have to put all of its
		 * children in instead.
		 */
		if (valueDef.isStandardType()) {
			ParametersUtil.addPart(myContext, operation, PARAMETER_VALUE, value);
		} else {
			for (BaseRuntimeChildDefinition nextChild : valueDef.getChildren()) {
				List<IBase> childValues = nextChild.getAccessor().getValues(value);
				for (int index = 0; index < childValues.size(); index++) {
					boolean childRepeatable = theChildDefinition.getMax() != 1;
					String elementName = nextChild.getChildNameByDatatype(
							childValues.get(index).getClass());
					String targetPath = thePath + (childRepeatable ? "[" + index + "]" : "") + "." + elementName;
					addInsertItems(theDiff, childValues, index, targetPath, nextChild);
				}
			}
		}
	}

	private void addValueToDiff(IBase theOperationPart, IBase theOldValue, IBase theNewValue) {

		if (myIncludePreviousValueInDiff) {
			IBase oldValue = massageValueForDiff(theOldValue);
			ParametersUtil.addPart(myContext, theOperationPart, "previousValue", oldValue);
		}

		IBase newValue = massageValueForDiff(theNewValue);
		ParametersUtil.addPart(myContext, theOperationPart, PARAMETER_VALUE, newValue);
	}

	private boolean pathIsIgnored(EncodeContextPath theSourceEncodeContext) {
		boolean pathIsIgnored = false;
		for (EncodeContextPath next : myIgnorePaths) {
			if (theSourceEncodeContext.startsWith(next, false)) {
				pathIsIgnored = true;
				break;
			}
		}
		return pathIsIgnored;
	}

	private IBase massageValueForDiff(IBase theNewValue) {
		IBase massagedValue = theNewValue;

		// XHTML content is dealt with by putting it in a string
		if (theNewValue instanceof XhtmlNode) {
			String xhtmlString = ((XhtmlNode) theNewValue).getValueAsString();
			massagedValue = myContext.getElementDefinition("string").newInstance(xhtmlString);
		}

		// IIdType can hold a fully qualified ID, but we just want the ID part to show up in diffs
		if (theNewValue instanceof IIdType) {
			String idPart = ((IIdType) theNewValue).getIdPart();
			massagedValue = myContext.getElementDefinition("id").newInstance(idPart);
		}

		return massagedValue;
	}

	private String toValue(IPrimitiveType<?> theOldPrimitive) {
		if (theOldPrimitive instanceof IIdType) {
			return ((IIdType) theOldPrimitive).getIdPart();
		}
		return theOldPrimitive.getValueAsString();
	}
}
