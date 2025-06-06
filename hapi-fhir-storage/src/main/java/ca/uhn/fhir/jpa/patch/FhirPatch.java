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
import ca.uhn.fhir.jpa.util.RandomTextUtils;
import ca.uhn.fhir.parser.path.EncodeContextPath;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.IModelVisitor2;
import ca.uhn.fhir.util.ParametersUtil;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseEnumeration;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.utilities.xhtml.XhtmlNode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * FhirPatch handler.
 * Patch is defined by the spec: https://www.hl7.org/fhir/R4/fhirpatch.html
 */
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

			GetNewValueParameters params = new GetNewValueParameters();
			params.ChildDefinition = childDefinition;
			params.Parameters = theParameters;
			IBase newValue = getNewValue(params);

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

			GetNewValueParameters params = new GetNewValueParameters();
			params.Parameters = theParameters;
			params.ChildDefinition = childDefinition;
			IBase newValue = getNewValue(params);

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

		ParsedPath parsedPath = ParsedPath.parse(path);
		List<IBase> containingElements = myContext
				.newFhirPath()
				.evaluate(
						theResource,
						parsedPath.getEndsWithAFilterOrIndex() ? parsedPath.getContainingPath() : path,
						IBase.class);

		for (IBase nextElement : containingElements) {
			if (parsedPath.getEndsWithAFilterOrIndex()) {
				// if the path ends with a filter or index, we must be dealing with a list
				deleteFromList(theResource, nextElement, parsedPath.getLastElementName(), path);
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

		FhirPathChildDefinition parentDef = new FhirPathChildDefinition();

		List<ParsedFhirPath.FhirPathNode> pathNodes = new ArrayList<>();
		parsedFhirPath.getAllNodesWithPred(pathNodes, ParsedFhirPath.FhirPathNode::isNormalPathNode);
		List<String> parts = new ArrayList<>();
		for (ParsedFhirPath.FhirPathNode node : pathNodes) {
			parts.add(node.getValue());
		}

		FhirPathChildDefinition cd = childDefinition(parentDef, parts, theResource, fhirPath, parsedFhirPath);

		replaceValuesByPath(cd, theParameters, fhirPath, parsedFhirPath);
	}

	private void replaceValuesByPath(
		FhirPathChildDefinition theChildDefinition,
		IBase theParameters,
		IFhirPath theFhirPath,
		ParsedFhirPath theParsedFhirPath
	) {
		Optional<IBase> singleValuePart =
			ParametersUtil.getParameterPartValue(myContext, theParameters, PARAMETER_VALUE);
		if (singleValuePart.isPresent()) {
			IBase replacementValue = singleValuePart.get();

			FhirPathChildDefinition childDefinitionToUse = findChildDefinitionByReplacementType(theChildDefinition, replacementValue);

			// only a single replacement value (ie, not a replacement CompositeValue or anything)
			replaceSingleValue(theFhirPath, theParsedFhirPath, childDefinitionToUse, replacementValue);
			return; // guard
		}

		Optional<IBase> valueParts = ParametersUtil.getParameterPart(myContext, theParameters, PARAMETER_VALUE);
		if (valueParts.isPresent()) {
			// multiple replacement values provided via parts
			List<IBase> partParts = valueParts.map(this::extractPartsFromPart).orElse(Collections.emptyList());

			for (IBase nextValuePartPart : partParts) {
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
					FhirPathChildDefinition childDefinitionToUse = findChildDefinitionToUse(theChildDefinition, nextValuePartPart);

					BaseRuntimeChildDefinition subChild = childDefinitionToUse.getElementDefinition()
						.getChildByName(name);

					subChild.getMutator()
						.setValue(childDefinitionToUse.getBase(), optionalValue.get());
				}
			}

			return; // guard
		}

		// fall through to error state
		throw new RuntimeException("NO VALUES?");
	}

	private FhirPathChildDefinition findChildDefinitionByReplacementType(FhirPathChildDefinition theChildDefinition, IBase replacementValue) {
		boolean isPrimitive = replacementValue instanceof IPrimitiveType<?>;
		Predicate<FhirPathChildDefinition> predicate = def -> {
			if (isPrimitive) {
				// primitives will be at the very bottom (ie, no children underneath)
				return def.getBase() instanceof IPrimitiveType<?>;
			} else if (def.getBase().fhirType().equalsIgnoreCase(replacementValue.fhirType())) {
				return true;
			}
			return false;
		};

		FhirPathChildDefinition childDefinitionToUse = theChildDefinition;
		while (childDefinitionToUse != null) {
			if (predicate.test(childDefinitionToUse)) {
				break;
			}
			childDefinitionToUse = childDefinitionToUse.getChild();
		}

		if (childDefinitionToUse == null || childDefinitionToUse.getParent() == null) {
			throw new RuntimeException("REPLACE ME");
		}
		return childDefinitionToUse;
	}

	private FhirPathChildDefinition findChildDefinitionToUse(FhirPathChildDefinition theChildDefinition, IBase replacementValue) {
		FhirPathChildDefinition childDefinitionToUse = theChildDefinition;
		while (childDefinitionToUse != null && childDefinitionToUse.getChild() != null) {
			childDefinitionToUse = childDefinitionToUse.getChild();
		}
		if (childDefinitionToUse == null || childDefinitionToUse.getParent() == null) {
			throw new RuntimeException("REPLACE ME");
		}
		return childDefinitionToUse;
	}

	private void replaceSingleValue(
		IFhirPath theFhirPath,
		ParsedFhirPath theParsedFhirPath,
		FhirPathChildDefinition theTargetChildDefinition,
		IBase theReplacementValue
	) {
		if (theTargetChildDefinition.getElementDefinition().getChildType() == BaseRuntimeElementDefinition.ChildTypeEnum.PRIMITIVE_DATATYPE) {
			if (theTargetChildDefinition.getBase() instanceof IPrimitiveType<?> target
				&& theReplacementValue instanceof IPrimitiveType<?> source) {
				target.setValueAsString(source.getValueAsString());
			}
			return;
		}

		IBase containingElement = theTargetChildDefinition.getParent().getBase();
		BaseRuntimeChildDefinition runtimeDef = theTargetChildDefinition.getBaseRuntimeDefinition();
		if (runtimeDef == null) {
			runtimeDef = theTargetChildDefinition.getParent().getBaseRuntimeDefinition();
		}

		if (runtimeDef.isMultipleCardinality()) {
			// a list
			List<IBase> existing = new ArrayList<>(
				runtimeDef.getAccessor().getValues(
					containingElement
				)
			);
			if (existing.isEmpty()) {
				// no elements to replace - we shouldn't see this here though
				String msg = myContext
					.getLocalizer()
					.getMessage(FhirPatch.class, "noMatchingElementForPath", theParsedFhirPath.getRawPath());
				throw new InvalidRequestException(Msg.code(2617) + msg);
			}

			List<IBase> replaceables;
			if (isSubsettingNode(theParsedFhirPath.getTail())) {
				replaceables = applySubsettingFilter(theParsedFhirPath, theParsedFhirPath.getTail(), existing);
			} else if (existing.size() == 1) {
				replaceables = existing;
			} else {
				String raw = theParsedFhirPath.getRawPath();
				String finalNode = theParsedFhirPath.getLastElementName();
				String subpath = raw.substring(raw.indexOf(finalNode));
				if (subpath.startsWith(finalNode) && subpath.length() > finalNode.length()) {
					subpath = subpath.substring(finalNode.length() + 1); // + 1 for the "."
				}

				AtomicReference<String> subpathRef = new AtomicReference<>();
				subpathRef.set(subpath);
				replaceables = existing.stream()
					.filter(item -> {
						Optional<IBase> matched = theFhirPath.evaluateFirst(item, subpathRef.get(), IBase.class);
						return matched.isPresent();
					}).toList();
			}

			if (replaceables.size() != 1) {
				throw new RuntimeException("Somehow got wrong size");
			}
			IBase valueToReplace = replaceables.get(0);

			BaseRuntimeChildDefinition.IMutator listMutator =
				runtimeDef.getMutator();
			// clear the whole list first, then reconstruct it in the loop below replacing the values that need to be
			// replaced
			listMutator.setValue(containingElement, null);
			for (IBase existingValue : existing) {
				if (valueToReplace.equals(existingValue)) {
					listMutator.addValue(containingElement, theReplacementValue);
				} else {
					listMutator.addValue(containingElement, existingValue);
				}
			}
		} else {
			// a single element
			runtimeDef.getMutator().setValue(
				containingElement,
				theReplacementValue
			);
		}
	}

	private void handleReplaceOperation2(IBaseResource theResource, IBase theParameters) {
		String path = ParametersUtil.getParameterPartValueAsString(myContext, theParameters, PARAMETER_PATH);
		path = defaultString(path);

		ParsedFhirPath parsedFhirPath = ParsedFhirPath.parse(path);

		IFhirPath fhirPath = myContext.newFhirPath();

		List<IBase> containingElements = filterDown(List.of(theResource), fhirPath, parsedFhirPath);

		/*
		 * PATCH operations must return a single resource (as per https://www.hl7.org/fhir/R4/fhirpatch.html):
		 * * "The FHIRPath statement must return a single element"
		 * * "Except for the delete operation, it is an error if no element matches the specified path"
		 */
		if (containingElements.size() != 1) {
			if (containingElements.isEmpty()) {
				// to preserve existing behaviour
				throwNoElementsError(parsedFhirPath.getRawPath());
			} else {
				throw new InvalidRequestException(Msg.code(2715) + " Fhir paths must only return a single element.");
			}
		}
		IBase elementToUse = containingElements.get(0);

		FhirPathChildDefinition parentDef = new FhirPathChildDefinition();
//		parentDef.setElementDefinition(myContext.getResourceDefinition(theResource));

		List<ParsedFhirPath.FhirPathNode> pathNodes = new ArrayList<>();
		parsedFhirPath.getAllNodesWithPred(pathNodes, ParsedFhirPath.FhirPathNode::isNormalPathNode);
		List<String> parts = new ArrayList<>();
		for (ParsedFhirPath.FhirPathNode node : pathNodes) {
			parts.add(node.getValue());
		}

		FhirPathChildDefinition cd = childDefinition(parentDef, parts, theResource, fhirPath, parsedFhirPath);

		ChildDefinition childDefinition = findChildDefinition(theResource, elementToUse, parsedFhirPath, fhirPath);

		GetNewValueParameters params = new GetNewValueParameters();
		params.ChildDefinition = childDefinition;
		params.Parameters = theParameters;
		params.ParsedFhirPath = parsedFhirPath;
		params.ParentResource = theResource;
		params.FhirPath = fhirPath;
		IBase newValue = getNewValue(params);

//		if (childDefinition.getUsableChildElement().getChildType() == BaseRuntimeElementDefinition.ChildTypeEnum.COMPOSITE_DATATYPE) {
//
//			switch (elementToUse.fhirType()) {
//				case "reference" -> {
//				}
//				case "xhtml" -> {
//
//				}
//			}
//
//		} else {
			if (parsedFhirPath.endsWithFilterOrIndex()) {
				// if the path ends with a filter or index, we must be dealing with a list
				replaceInList(newValue, theResource, childDefinition, parsedFhirPath);
			} else {
				IBase el = childDefinition.getContainingElement();
				childDefinition.getUseableChildDef().getMutator().setValue(el, newValue);
			}
//		}
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
			// no filter, no index; this is just a raw path
			List<IBase> all = new ArrayList<>();
			for (IBase item : theList) {
				all.addAll(
					theFhirPath.evaluate(item, theParsed.getRawPath(), IBase.class)
				);
			}
			return all;
		} else if (!isSubsettingNode(theParsed.getTail())) {
			/*
			 * Not a subsetting node, so we can just apply the filter on
			 * the list we currently have and return that
			 */
			// todo - remove final node and filter
			String containedPath = theParsed.getContainingPath();

			ParsedFhirPath containedParsed = ParsedFhirPath.parse(containedPath);
			List<IBase> list = filterDown(theList, theFhirPath, containedParsed);

			String rawPath = theParsed.getRawPath();
			// the rest of the path
			String lastEl = containedParsed.getLastElementName();
			String filteredPath = rawPath.substring(rawPath.indexOf(lastEl) + lastEl.length() + 1);
			return list.stream().filter(item -> {
				Optional<IBase> matchOp = theFhirPath.evaluateFirst(item, filteredPath, IBase.class);
				return matchOp.isPresent();
			}).toList();
		} else {
			// A subsetting function or index must be evaluated on a list of
			// elements
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
			String newPath = truncatePath(theParsed, tail);
			List<IBase> filtered = filterDown(theList, theFhirPath, ParsedFhirPath.parse(newPath));

			return applySubsettingFilter(theParsed, tail, filtered);
		}
	}

	private List<IBase> applySubsettingFilter(ParsedFhirPath theParsed, ParsedFhirPath.FhirPathNode tail, List<IBase> filtered) {
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

	private String truncatePath(ParsedFhirPath theParsed, ParsedFhirPath.FhirPathNode tail) {
		String rawPath = theParsed.getRawPath();
		String tailValue = tail.getValue();
		int endIndex = rawPath.indexOf(tailValue);
		String newPath = rawPath.substring(0, endIndex);
		if (newPath.endsWith(".")) {
			newPath = newPath.substring(0, newPath.length() - 1);
		}
		return newPath;
	}

	private void replaceInList(
			IBase theNewValue,
			IBaseResource theResource,
			ChildDefinition theChildDefinitionForTheList,
			ParsedFhirPath thePath) {

		String theFullReplacePath = thePath.getRawPath();

		List<IBase> existingValues = new ArrayList<>();

		/*
		 * If there's a parent def, that means
		 * we're dealing with a primitive type.
		 */
		IBase elToUse = theChildDefinitionForTheList.getContainingElement();
		if (theChildDefinitionForTheList.hasParentDefinition()) {
//			elToUse = theChildDefinitionForTheList.getParentBase();
			// primitive
			existingValues.addAll(theChildDefinitionForTheList.getUseableChildDef()
				.getAccessor().getValues(elToUse));
		} else {
//			elToUse = theContainingElement;
			// non-primitive
			existingValues.addAll(
				theChildDefinitionForTheList.getUseableChildDef().getAccessor().getValues(elToUse));
		}

		List<IBase> valuesToReplace = myContext.newFhirPath().evaluate(theResource, theFullReplacePath, IBase.class);
		if (valuesToReplace.isEmpty()) {
			throwNoElementsError(theFullReplacePath);
		}

		BaseRuntimeChildDefinition.IMutator listMutator =
				theChildDefinitionForTheList.getUseableChildDef().getMutator();
		// clear the whole list first, then reconstruct it in the loop below replacing the values that need to be
		// replaced
		listMutator.setValue(elToUse, null);
		for (IBase existingValue : existingValues) {
			if (valuesToReplace.contains(existingValue)) {
				listMutator.addValue(elToUse, theNewValue);
			} else {
				listMutator.addValue(elToUse, existingValue);
			}
		}
	}

	private void throwNoElementsError(String theFullReplacePath) {
		String msg = myContext
			.getLocalizer()
			.getMessage(FhirPatch.class, "noMatchingElementForPath", theFullReplacePath);
		throw new InvalidRequestException(Msg.code(2617) + msg);
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

	/**
	 * Turns an invalid FhirPath into a valid one
	 *
	 * Do not use this for user input; but it can be used for internally parsed paths
	 */
	private String cleansePath(String thePath) {
		String path = thePath;

		// remove trailing .
		while (path.endsWith(".")) {
			path = path.substring(0, path.length() - 1);
		}

		// remove preceding .
		while (path.startsWith(".")) {
			path = path.substring(1);
		}

		// balance brackets
		int openBrace = path.indexOf("(");
		String remainder = path;
		int endingIndex = openBrace == -1 ? path.length() : 0;
		while (openBrace != -1) {
			int closing = RandomTextUtils.findMatchingClosingBrace(openBrace, remainder);
			endingIndex += closing + 1; // +1 because substring ending is exclusive
			remainder = remainder.substring(closing + 1);
			openBrace = remainder.indexOf("(");
		}
		path = path.substring(0, endingIndex);

		return path;
	}

//	private ChildDefinition.ParentDefinition getParentDefinition(IBase theParentResource, ParsedFhirPath theParsedPath, IFhirPath theFhirPath, String theChildName) {
//		// the type we have is not an
//		String lastNode = theParsedPath.getLastElementName();
//		List<ParsedFhirPath.FhirPathNode> allPathNodes = new ArrayList<>();
//		theParsedPath.getAllNodesWithPred(allPathNodes, ParsedFhirPath.FhirPathNode::isNormalPathNode);
//		StringBuilder sb = new StringBuilder();
//		ParsedFhirPath.FhirPathNode lastChild = theParsedPath.getHead();
//		for (ParsedFhirPath.FhirPathNode node : allPathNodes) {
//			if (node.getValue().equals(lastNode)) {
//				break;
//			}
//			if (!sb.isEmpty()) {
//				sb.append(".");
//			}
//			lastChild = node;
//			sb.append(node.getValue());
//		}
//		String parentPath = sb.toString();
//
//		List<IBase> elements = filterDown(List.of(theParentResource), theFhirPath, ParsedFhirPath.parse(parentPath));
//
//		String subPath = theParsedPath.getRawPath().substring(theParsedPath.getRawPath().indexOf(theChildName));
//
//		ParsedFhirPath parsedSubpath = ParsedFhirPath.parse(subPath);
//		// 2 ways we can have a filter
//		// either the parent of this child is filtered;
//		// or the end child of this subpath is a filter
//		ParsedFhirPath.FhirPathNode filterNode = null;
//		if (isSubsettingNode(lastChild.getNext())) {
//			filterNode = lastChild.getNext();
//		} else if (isSubsettingNode(parsedSubpath.getTail())) {
//			filterNode = parsedSubpath.getTail();
//			// subpath from childName to filterNode
//			subPath = subPath.substring(0, subPath.indexOf(filterNode.getValue()));
//			subPath = cleansePath(subPath);
//
//			/*
//			 * if the filter is on the first child of the top level resource
//			 * (ie, Patient.name[1])
//			 * we do not need to filter, because there can only be 1 parent.
//			 * The filter is for the child only
//			 */
//			if (filterNode.getPrevious().getValue().equals(theParsedPath.getHead().getNext().getValue())) {
//				filterNode = null;
//			}
//		}
//
//		AtomicReference<String> subpathref = new AtomicReference<>();
//		subpathref.set(subPath);
//		elements = elements.stream()
//			.filter(n -> {
//				Optional<IBase> matchedOp = theFhirPath.evaluateFirst(n, subpathref.get(), IBase.class);
//				return matchedOp.isPresent();
//			}).toList();
//
//		if (filterNode != null) {
//			elements = applySubsettingFilter(parsedSubpath, filterNode, elements);
//		}
//		if (elements.isEmpty()) {
//			// todo - handle
//			throw new InvalidRequestException("Should not get here");
//		}
//
//		IBase parentEl = elements.get(0);
//		BaseRuntimeElementDefinition<?> parentElement = myContext.getElementDefinition(parentEl.getClass());
//		BaseRuntimeChildDefinition parentDef = parentElement.getChildByName(theChildName);
//		return new ChildDefinition.ParentDefinition(parentEl, parentElement, parentDef, parentPath);
//	}

//	private ChildDefinition findChildDefinition(IBaseResource theParentResource, IBase theChildElement, ParsedFhirPath theParsedPath, IFhirPath theFhirPath) {
//		final String childName = theParsedPath.getLastElementName();
//		BaseRuntimeElementDefinition<?> elementDef = myContext.getElementDefinition(theChildElement.getClass());
//
//
//		// todo - explain
//		ChildDefinition childDefinition = null;
////		switch (elementDef.getChildType()) {
////			case PRIMITIVE_DATATYPE, COMPOSITE_DATATYPE -> {
////
////				// if it's a primitive, we should get the parent as the 'child' and set the parent as the parent of the parent
////				BaseRuntimeChildDefinition childDef = elementDef.getChildByName(childName);
////
////				if (childDef == null) {
////					childDefinition = new ChildDefinition(null, elementDef);
////					ChildDefinition.ParentDefinition parentDefinition = getParentDefinition(theParentResource, theParsedPath, theFhirPath, childName);
////					childDefinition.setParentDefinition(parentDefinition);
////					childDefinition.setContainingElement(theChildElement);
////				} else {
////					childDefinition = generateChildDefinition(childName, elementDef);
////
////					ChildDefinition.ParentDefinition parentDefinition = getParentDefinition(theParentResource, theParsedPath, theFhirPath, childName);
////					childDefinition.setParentDefinition(parentDefinition);
////
////					childDefinition.setContainingElement(theChildElement);
////				}
////			}
////			case RESOURCE_BLOCK -> {
////				ChildDefinition.ParentDefinition parentDefinition = getParentDefinition(theParentResource, theParsedPath, theFhirPath, childName);
////
////				childDefinition = generateChildDefinition(childName, parentDefinition.getParentElement());
////				childDefinition.setContainingElement(parentDefinition.getParentField());
////			}
////		}
//
//		if (childDefinition == null) {
//			childDefinition = generateChildDefinition(childName, elementDef);
//			childDefinition.setContainingElement(theChildElement);
//		}
//
//		return childDefinition;
//	}

	private ChildDefinition findChildDefinition(
		IBaseResource theParentResource,
		IBase theChildElement,
		ParsedFhirPath theParsedPath,
		IFhirPath theFhirPath) {
		List<ParsedFhirPath.FhirPathNode> nodes = new ArrayList<>();
		theParsedPath.getAllNodesWithPred(nodes, ParsedFhirPath.FhirPathNode::isNormalPathNode);

		BaseRuntimeElementDefinition<?> elementDef = myContext.getElementDefinition(theChildElement.getClass());

		return null;
	}

	private FhirPathChildDefinition childDefinition(
		FhirPathChildDefinition theParent,
		List<String> theFhirPathParts,
		IBase theBase,
		IFhirPath theFhirPath,
		ParsedFhirPath theParsedFhirPath
	) {
		FhirPathChildDefinition definition = new FhirPathChildDefinition();
		definition.setBase(theBase); // set this IBase value
		BaseRuntimeElementDefinition<?> parentElementDefinition = myContext.getElementDefinition(theBase.getClass());
		definition.setElementDefinition(parentElementDefinition); // set this element

		String head = theParsedFhirPath.getHead().getValue();
		definition.setFhirPath(head);

		if (theParent.getElementDefinition() != null) {
			definition.setBaseRuntimeDefinition(
				theParent.getElementDefinition().getChildByName(head)
			);
		}

		String rawPath = theParsedFhirPath.getRawPath();

		if (rawPath.equalsIgnoreCase(head)) {
			// we're at the bottom
			// return
			return definition;
		}

		// detach the head
		String headVal = theFhirPathParts.remove(0);
		String pathBeneathParent = rawPath.substring(headVal.length());
		pathBeneathParent = cleansePath(pathBeneathParent);

		if (isNotBlank(pathBeneathParent) && !theFhirPathParts.isEmpty()) {
			Stack<ParsedFhirPath.FhirPathNode> filteringNodes = new Stack<>();

			String childFilteringPath = pathBeneathParent;
			String nextPath = pathBeneathParent;

			if (isSubsettingNode(theParsedFhirPath.getTail())) {
				// the final node in this path is .first() or [0]... etc
				ParsedFhirPath.FhirPathNode filteringNode = theParsedFhirPath.getTail();
				filteringNodes.push(filteringNode);
				/*
				 * the fild filtering path will be the path - tail value.
				 * This will also be nextPath (the one we recurse on)
				 */
				childFilteringPath = pathBeneathParent.substring(0, pathBeneathParent.indexOf(filteringNode.getValue()));
				childFilteringPath = cleansePath(childFilteringPath);
				nextPath = childFilteringPath;
			}

			String directChildName = theFhirPathParts.get(0);

			ParsedFhirPath newPath = ParsedFhirPath.parse(nextPath);

			// yet another filtering node
			// this is if the head node has a filter applied immediately upon it
			// we need to remove it because it'll artificially filter down our children if we don't
//			if (newPath.getHead().getNext() != null && isSubsettingNode(newPath.getHead().getNext())) {
//				ParsedFhirPath.FhirPathNode filteringNode = newPath.getHead().getNext();
//
//				filteringNodes.push(filteringNode);
//
//				// we have to remove it from our path because we'll be handling it here
//				String omission = directChildName;
//				if (filteringNode.hasListIndex()) {
//					omission += filteringNode.getValue();
//				} else if (filteringNode instanceof ParsedFhirPath.FhirPathFunction fn) {
//					if (!fn.hasContainedExp()) {
//						omission += "." + filteringNode.getValue() + "()";
//					} else {
//						throw new RuntimeException("HAS AN EXPRESION");
//					}
//				}
//				nextPath = nextPath.substring(omission.length());
//				nextPath = cleansePath(nextPath);
//				newPath = ParsedFhirPath.parse(nextPath);
//				childFilteringPath = directChildName;
//			}

			if (newPath.getHead() instanceof ParsedFhirPath.FhirPathFunction fn && fn.hasContainedExp()) {
				newPath = fn.getContainedExp();
			}

			// get all direct children
			ParsedFhirPath.FhirPathNode newHead = newPath.getHead();
			List<IBase> allChildren = theFhirPath.evaluate(theBase, directChildName, IBase.class);

			// go through the children and take only the ones that match the path we have
			String filterPath = childFilteringPath;

			List<IBase> childs;
			if (filterPath.startsWith(newHead.getValue()) && !filterPath.equalsIgnoreCase(newHead.getValue())) {
				filterPath = filterPath.substring(newHead.getValue().length());
				filterPath = cleansePath(filterPath);
			}

			if (isNotBlank(filterPath)) {
				if (theFhirPathParts.contains(filterPath)) {
					/*
					 * We're filtering on just a fhirpath node for some reason (ie, "identifier" or "reference" or "string").
					 *
					 * We don't need to apply the filter;
					 * all children should be the same as this filtered type
					 * (likely we have a subsetting filter to apply)
					 */
					childs = allChildren;
				} else {
					AtomicReference<String> ref = new AtomicReference<>();
					ref.set(filterPath);
					if (allChildren.size() > 1) {
						childs = allChildren.stream()
							.filter(el -> {
								Optional<IBase> match = theFhirPath.evaluateFirst(el, ref.get(), IBase.class);
								return match.isPresent();
							}).toList();
					} else {
						// there is only 1 child (probably a top level element)
						childs = allChildren;
					}
				}
			} else {
				childs = allChildren;
			}

//			String newRaw = newPath.getRawPath();
			while (!filteringNodes.empty()) {
				ParsedFhirPath.FhirPathNode filteringNode = filteringNodes.pop();
				childs = applySubsettingFilter(newPath, filteringNode, childs);
			}

			// should only be one
			if (childs.size() != 1) {
//				ourLog.error("invalid");
				throw new RuntimeException("THIS ISN'T ALLOWED");
			}
			IBase child = childs.get(0);

			definition.setChild(childDefinition(
				definition,
				theFhirPathParts,
				child,
				theFhirPath,
				newPath
			));
		}

		return definition;
	}

	private ChildDefinition generateChildDefinition(String childName, BaseRuntimeElementDefinition<?> elementDef) {
		BaseRuntimeChildDefinition childDef = elementDef.getChildByName(childName);
		BaseRuntimeElementDefinition<?> childElement;
		if (childDef == null) {
			childDef = elementDef.getChildByName(childName + "[x]");
			childElement = childDef.getChildByName(
				childDef.getValidChildNames().iterator().next());
		} else {
			childElement = childDef.getChildByName(childName);
		}

		return new ChildDefinition(childDef, childElement);
	}

	private ChildDefinition findChildDefinition(IBase theContainingElement, String theElementName) {
		BaseRuntimeElementDefinition<?> elementDef = myContext.getElementDefinition(theContainingElement.getClass());

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

	private class GetNewValueParameters {
		ChildDefinition ChildDefinition;

		IBase Parameters;

		IBaseResource ParentResource;

		IFhirPath FhirPath;

		ParsedFhirPath ParsedFhirPath;

		public boolean isLegacyPath() {
			return ParsedFhirPath == null && FhirPath == null;
		}
	}

	private IBase getNewValue(GetNewValueParameters theParams) {
		IBase theParameters = theParams.Parameters;
		ChildDefinition theChildDefinition = theParams.ChildDefinition;

		Optional<IBase> valuePart = ParametersUtil.getParameterPart(myContext, theParameters, PARAMETER_VALUE);
		Optional<IBase> valuePartValue =
				ParametersUtil.getParameterPartValue(myContext, theParameters, PARAMETER_VALUE);

		IBase newValue;
		if (valuePartValue.isPresent()) {
			newValue = maybeMassageToEnumeration(valuePartValue.get(), theChildDefinition);

		} else {
			List<IBase> partParts = valuePart.map(this::extractPartsFromPart).orElse(Collections.emptyList());

			newValue = createAndPopulateNewElement(theParams, theChildDefinition, partParts);
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
	private IBase createAndPopulateNewElement(GetNewValueParameters theParams, ChildDefinition theDefinition, List<IBase> thePartParts) {
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
				ChildDefinition childDefinition;
				if (theParams.isLegacyPath()) {
					childDefinition = findChildDefinition(newElement, name);
				} else {
					childDefinition = findChildDefinition(
						theParams.ParentResource,
						newElement,
						theParams.ParsedFhirPath,
						theParams.FhirPath
					);
				}
				IBase newValue = maybeMassageToEnumeration(optionalValue.get(), childDefinition);

				BaseRuntimeChildDefinition partChildDef =
						theDefinition.getUsableChildElement().getChildByName(name);

				if (isNull(partChildDef)) {
					name = name + "[x]";
					partChildDef = theDefinition.getUsableChildElement().getChildByName(name);
				}

				partChildDef.getMutator().setValue(newElement, newValue);

				// a part represent a datatype or a complexType but not both at the same time.
				continue;
			}

			List<IBase> part = extractPartsFromPart(nextValuePartPart);

			if (!part.isEmpty()) {
				// we have a complexType.  let's find its definition and recursively process
				// them till all complexTypes are processed.
				// todo
				ChildDefinition childDefinition;
				if (theParams.isLegacyPath()) {
					// TODO - we should update all patch operations eventually
					childDefinition = findChildDefinition(newElement, name);
				} else {
					childDefinition = findChildDefinition(
						theParams.ParentResource,
						newElement,
						theParams.ParsedFhirPath,
						theParams.FhirPath
					);
				}

				IBase childNewValue = createAndPopulateNewElement(theParams, childDefinition, part);

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
