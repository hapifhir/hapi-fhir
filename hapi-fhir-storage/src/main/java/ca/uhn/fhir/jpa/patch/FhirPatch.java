/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
import ca.uhn.fhir.context.RuntimeChildChoiceDefinition;
import ca.uhn.fhir.context.RuntimeChildPrimitiveDatatypeDefinition;
import ca.uhn.fhir.fhirpath.IFhirPath;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.util.FhirPathUtils;
import ca.uhn.fhir.parser.path.EncodeContextPath;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.FhirPatchBuilder;
import ca.uhn.fhir.util.ParametersUtil;
import ca.uhn.fhir.util.UrlUtil;
import com.google.common.collect.Multimap;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseEnumeration;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.utilities.xhtml.XhtmlNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * FhirPatch handler.
 * Patch is defined by the spec: https://hl7.org/fhir/fhirpatch.html
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
	public static final String PARAMETER_ALLOW_MULTIPLE_MATCHES = FhirPatchBuilder.PARAMETER_ALLOW_MULTIPLE_MATCHES;

	private static final Pattern EXTENSION_FUNCTION_PATTERN =
			Pattern.compile("((?:modifier)?[Ee]xtension)\\('([^']+)'\\)");

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

	/**
	 * Apply the patch against the given resource.
	 * @param theResource The resourece to patch. This object will be modified in-place.
	 * @param thePatch The patch document.
	 */
	public void apply(IBaseResource theResource, IBaseResource thePatch) {
		PatchOutcome retVal = new PatchOutcome();
		doApply(theResource, thePatch, retVal);
		if (retVal.hasErrors()) {
			throw new InvalidRequestException(Msg.code(1267) + retVal.getErrors());
		}
	}

	/**
	 * @param theResource If this is <code>null</code>, the patch is validated but no work is done
	 */
	private void doApply(
			@Nullable IBaseResource theResource, @Nonnull IBaseResource thePatch, PatchOutcome theOutcome) {
		Multimap<String, IBase> namedParameters = ParametersUtil.getNamedParameters(myContext, thePatch);
		for (Map.Entry<String, IBase> namedParameterEntry : namedParameters.entries()) {
			if (namedParameterEntry.getKey().equals(PARAMETER_OPERATION)) {
				IBase nextOperation = namedParameterEntry.getValue();
				String type = ParametersUtil.getParameterPartValueAsString(myContext, nextOperation, PARAMETER_TYPE);
				type = defaultString(type);

				if (OPERATION_DELETE.equals(type)) {
					handleDeleteOperation(theResource, nextOperation, theOutcome);
				} else if (OPERATION_ADD.equals(type)) {
					handleAddOperation(theResource, nextOperation, theOutcome);
				} else if (OPERATION_REPLACE.equals(type)) {
					handleReplaceOperation(theResource, nextOperation, theOutcome);
				} else if (OPERATION_INSERT.equals(type)) {
					handleInsertOperation(theResource, nextOperation, theOutcome);
				} else if (OPERATION_MOVE.equals(type)) {
					handleMoveOperation(theResource, nextOperation);
				} else {
					theOutcome.addError("Unknown patch operation type: " + type);
				}

			} else {
				theOutcome.addError("Unknown patch parameter name: " + namedParameterEntry.getKey());
			}
		}
	}

	private void handleAddOperation(@Nullable IBaseResource theResource, IBase theParameters, PatchOutcome theOutcome) {

		String path = ParametersUtil.getParameterPartValueAsString(myContext, theParameters, PARAMETER_PATH);
		String elementName = ParametersUtil.getParameterPartValueAsString(myContext, theParameters, PARAMETER_NAME);

		String containingPath = defaultString(path);
		IFhirPath fhirPath = myContext.newFhirPath();
		IFhirPath.IParsedExpression parsedExpression = parseFhirPathExpression(fhirPath, containingPath);

		if (theResource == null) {
			return;
		}

		List<IBase> containingElements = fhirPath.evaluate(theResource, parsedExpression, IBase.class);
		for (IBase nextElement : containingElements) {
			ChildDefinition childDefinition = findChildDefinition(nextElement, elementName);

			IBase newValue = getNewValue(theParameters, childDefinition);

			childDefinition.getUseableChildDef().getMutator().addValue(nextElement, newValue);
		}

		if (containingElements.isEmpty()) {
			theOutcome.addError("No content found at " + containingPath + " when adding");
		}
	}

	private void handleInsertOperation(
			@Nullable IBaseResource theResource, IBase theParameters, PatchOutcome theOutcome) {

		String path = ParametersUtil.getParameterPartValueAsString(myContext, theParameters, PARAMETER_PATH);
		path = defaultString(path);

		int lastDot = path.lastIndexOf(".");
		if (lastDot == -1) {
			theOutcome.addError("Invalid path for insert operation (must point to a repeatable element): "
					+ UrlUtil.sanitizeUrlPart(path));
			return;
		}

		String containingPath = path.substring(0, lastDot);
		String elementName = path.substring(lastDot + 1);
		Integer insertIndex = ParametersUtil.getParameterPartValueAsInteger(myContext, theParameters, PARAMETER_INDEX)
				.orElseThrow(() -> new InvalidRequestException("No index supplied for insert operation"));

		/*
		 * If there is no resource, we're only validating the patch so we can bail now since validation
		 * is above
		 */
		if (theResource == null) {
			return;
		}

		List<IBase> containingElements = myContext.newFhirPath().evaluate(theResource, containingPath, IBase.class);
		for (IBase nextElement : containingElements) {

			ChildDefinition childDefinition = findChildDefinition(nextElement, elementName);

			IBase newValue = getNewValue(theParameters, childDefinition);

			List<IBase> existingValues = new ArrayList<>(
					childDefinition.getUseableChildDef().getAccessor().getValues(nextElement));
			if (insertIndex == null || insertIndex < 0 || insertIndex > existingValues.size()) {
				String msg = myContext
						.getLocalizer()
						.getMessage(FhirPatch.class, "invalidInsertIndex", insertIndex, path, existingValues.size());
				theOutcome.addError(msg);
				return;
			}
			existingValues.add(insertIndex, newValue);

			childDefinition.getUseableChildDef().getMutator().setValue(nextElement, null);
			for (IBase nextNewValue : existingValues) {
				childDefinition.getUseableChildDef().getMutator().addValue(nextElement, nextNewValue);
			}
		}
	}

	private void handleDeleteOperation(
			@Nullable IBaseResource theResource, IBase theParameters, PatchOutcome theOutcome) {
		String path = ParametersUtil.getParameterPartValueAsString(myContext, theParameters, PARAMETER_PATH);
		path = defaultString(path);

		boolean allowMultiDelete = ParametersUtil.getParameterPartValueAsBoolean(
						myContext, theParameters, PARAMETER_ALLOW_MULTIPLE_MATCHES)
				.orElse(Boolean.FALSE);

		ParsedFhirPath parsedPath = ParsedFhirPath.parse(path);

		if (theResource == null) {
			return;
		}

		String pathToSelect;
		if (parsedPath.endsWithFilterOrIndex()) {
			pathToSelect = parsedPath.getContainingPath();
		} else {
			pathToSelect = path;
		}
		List<IBase> containingElements = myContext.newFhirPath().evaluate(theResource, pathToSelect, IBase.class);

		int count = 0;
		for (IBase nextElement : containingElements) {
			if (parsedPath.endsWithFilterOrIndex()) {
				// if the path ends with a filter or index, we must be dealing with a list
				count += deleteFromList(theResource, nextElement, parsedPath.getLastElementName(), path);
			} else {
				count += deleteSingleElement(nextElement);
			}
		}

		if (count > 1 && !allowMultiDelete) {
			theOutcome.addError("Multiple elements found at " + path + " when deleting");
			return;
		}
	}

	private int deleteFromList(
			IBaseResource theResource,
			IBase theContainingElement,
			String theListElementName,
			String theElementToDeletePath) {
		ChildDefinition childDefinition = findChildDefinition(theContainingElement, theListElementName);

		List<IBase> existingValues = new ArrayList<>(
				childDefinition.getUseableChildDef().getAccessor().getValues(theContainingElement));
		List<IBase> elementsToRemove =
				myContext.newFhirPath().evaluate(theResource, theElementToDeletePath, IBase.class);

		int initialSize = existingValues.size();
		existingValues.removeAll(elementsToRemove);
		int delta = initialSize - existingValues.size();

		childDefinition.getUseableChildDef().getMutator().setValue(theContainingElement, null);
		for (IBase nextNewValue : existingValues) {
			childDefinition.getUseableChildDef().getMutator().addValue(theContainingElement, nextNewValue);
		}

		return delta;
	}

	private void handleReplaceOperation(
			@Nullable IBaseResource theResource, @Nullable IBase theParameters, PatchOutcome theOutcome) {
		String path = ParametersUtil.getParameterPartValueAsString(myContext, theParameters, PARAMETER_PATH);
		path = defaultString(path);
		path = normalizeExtensionFunctionSyntax(path);

		// TODO
		/*
		 * We should replace this with
		 * IParsedExpression to expose the parsed parts of the
		 * path (including functional nodes).
		 *
		 * Alternatively, could make an Antir parser using
		 * the exposed grammar (http://hl7.org/fhirpath/N1/grammar.html)
		 *
		 * Might be required for more complex handling.
		 */
		IFhirPath fhirPath = myContext.newFhirPath();
		parseFhirPathExpression(fhirPath, path);
		ParsedFhirPath parsedFhirPath = ParsedFhirPath.parse(path);

		if (theResource == null) {
			return;
		}

		FhirPathChildDefinition parentDef = new FhirPathChildDefinition();

		List<ParsedFhirPath.FhirPathNode> pathNodes = new ArrayList<>();
		parsedFhirPath.getAllNodesWithPred(pathNodes, ParsedFhirPath.FhirPathNode::isNormalPathNode);
		List<String> parts = new ArrayList<>();
		for (ParsedFhirPath.FhirPathNode node : pathNodes) {
			parts.add(node.getValue());
		}

		// fetch all runtime definitions along fhirpath
		Optional<FhirPathChildDefinition> cdOpt =
				childDefinition(parentDef, parts, theResource, fhirPath, parsedFhirPath, path, theOutcome);
		if (cdOpt.isEmpty()) {
			return;
		}
		FhirPathChildDefinition cd = cdOpt.get();

		// replace the value
		replaceValuesByPath(cd, theParameters, fhirPath, parsedFhirPath, theOutcome);
	}

	private void replaceValuesByPath(
			FhirPathChildDefinition theChildDefinition,
			IBase theParameters,
			IFhirPath theFhirPath,
			ParsedFhirPath theParsedFhirPath,
			PatchOutcome theOutcome) {
		Optional<IBase> singleValuePart =
				ParametersUtil.getParameterPartValue(myContext, theParameters, PARAMETER_VALUE);
		if (singleValuePart.isPresent()) {
			IBase replacementValue = singleValuePart.get();

			FhirPathChildDefinition childDefinitionToUse =
					findChildDefinitionByReplacementType(theChildDefinition, replacementValue);

			// only a single replacement value (ie, not a replacement CompositeValue or anything)
			replaceSingleValue(theFhirPath, theParsedFhirPath, childDefinitionToUse, replacementValue, theOutcome);
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
					FhirPathChildDefinition childDefinitionToUse = findChildDefinitionAtEndOfPath(theChildDefinition);

					BaseRuntimeChildDefinition subChild =
							childDefinitionToUse.getElementDefinition().getChildByName(name);

					subChild.getMutator().setValue(childDefinitionToUse.getBase(), optionalValue.get());
				}
			}

			return; // guard
		}

		// fall through to error state
		theOutcome.addError(" No valid replacement value for patch operation.");
	}

	private FhirPathChildDefinition findChildDefinitionByReplacementType(
			FhirPathChildDefinition theChildDefinition, IBase replacementValue) {
		boolean isPrimitive = replacementValue instanceof IPrimitiveType<?>;
		Predicate<FhirPathChildDefinition> predicate = def -> {
			if (isPrimitive) {
				// primitives will be at the very bottom (ie, no children underneath)
				return def.getBase() instanceof IPrimitiveType<?>;
			} else {
				return def.getBase().fhirType().equalsIgnoreCase(replacementValue.fhirType());
			}
		};

		return findChildDefinition(theChildDefinition, predicate);
	}

	private FhirPathChildDefinition findChildDefinitionAtEndOfPath(FhirPathChildDefinition theChildDefinition) {
		return findChildDefinition(theChildDefinition, childDefinition -> childDefinition.getChild() == null);
	}

	private FhirPathChildDefinition findChildDefinition(
			FhirPathChildDefinition theChildDefinition, Predicate<FhirPathChildDefinition> thePredicate) {
		FhirPathChildDefinition childDefinitionToUse = theChildDefinition;
		while (childDefinitionToUse != null) {
			if (thePredicate.test(childDefinitionToUse)) {
				return childDefinitionToUse;
			}
			childDefinitionToUse = childDefinitionToUse.getChild();
		}

		throw new InvalidRequestException(Msg.code(2719) + " No runtime definition found for patch operation.");
	}

	private void replaceSingleValue(
			IFhirPath theFhirPath,
			ParsedFhirPath theParsedFhirPath,
			FhirPathChildDefinition theTargetChildDefinition,
			IBase theReplacementValue,
			PatchOutcome theOutcome) {

		/*
		 * We handle XHTML a bit differently, since it isn't like any of the other FHIR types
		 */
		if (theTargetChildDefinition.getBaseRuntimeDefinition()
				instanceof RuntimeChildPrimitiveDatatypeDefinition child) {
			if (child.getDatatype().equals(XhtmlNode.class)
					&& child.getElementName().equals("div")) {
				IPrimitiveType<?> target = (IPrimitiveType<?>) theTargetChildDefinition.getBase();
				if (theReplacementValue instanceof IPrimitiveType<?> replacementValue) {
					target.setValueAsString(replacementValue.getValueAsString());
					return;
				}
			}
		}

		if (theTargetChildDefinition.getElementDefinition().getChildType()
				== BaseRuntimeElementDefinition.ChildTypeEnum.PRIMITIVE_DATATYPE) {
			if (theTargetChildDefinition.getBase() instanceof IPrimitiveType<?> target
					&& theReplacementValue instanceof IPrimitiveType<?> source) {
				if (target.fhirType().equalsIgnoreCase(source.fhirType())) {
					if (theTargetChildDefinition
									.getParent()
									.getBase()
									.fhirType()
									.equalsIgnoreCase("narrative")
							&& theTargetChildDefinition.getFhirPath().equalsIgnoreCase("div")) {
						/*
						 * Special case handling for Narrative elements
						 * because xhtml is a primitive type, but it's fhirtype is recorded as "string"
						 * (which means we cannot actually assign it as a primitive type).
						 *
						 * Instead, we have to get the parent's type and set it's child as a new
						 * XHTML child.
						 */
						FhirPathChildDefinition narrativeDefinition = theTargetChildDefinition.getParent();
						BaseRuntimeElementDefinition<?> narrativeElement = narrativeDefinition.getElementDefinition();

						BaseRuntimeElementDefinition<?> newXhtmlEl = myContext.getElementDefinition("xhtml");

						IPrimitiveType<?> xhtmlType;
						if (theTargetChildDefinition.getBaseRuntimeDefinition().getInstanceConstructorArguments()
								!= null) {
							xhtmlType = (IPrimitiveType<?>) newXhtmlEl.newInstance(theTargetChildDefinition
									.getBaseRuntimeDefinition()
									.getInstanceConstructorArguments());
						} else {
							xhtmlType = (IPrimitiveType<?>) newXhtmlEl.newInstance();
						}

						xhtmlType.setValueAsString(source.getValueAsString());
						narrativeElement
								.getChildByName(theTargetChildDefinition.getFhirPath())
								.getMutator()
								.setValue(narrativeDefinition.getBase(), xhtmlType);
					} else {
						target.setValueAsString(source.getValueAsString());
					}
				} else if (theTargetChildDefinition.getChild() != null) {
					// there's subchildren (possibly we're setting an 'extension' value
					FhirPathChildDefinition ct = findChildDefinitionAtEndOfPath(theTargetChildDefinition);
					replaceSingleValue(theFhirPath, theParsedFhirPath, ct, theReplacementValue, theOutcome);
				} else {
					BaseRuntimeChildDefinition runtimeDef = theTargetChildDefinition.getBaseRuntimeDefinition();
					if (runtimeDef != null
							&& !runtimeDef.isMultipleCardinality()
							&& !(runtimeDef instanceof RuntimeChildChoiceDefinition)) {
						// non-choice, single-cardinality: lenient string assignment
						target.setValueAsString(source.getValueAsString());
						return;
					}

					// choice type - the primitive can have multiple value types
					BaseRuntimeElementDefinition<?> parentEl =
							theTargetChildDefinition.getParent().getElementDefinition();
					String childFhirPath = theTargetChildDefinition.getFhirPath();

					BaseRuntimeChildDefinition choiceTarget = parentEl.getChildByName(childFhirPath);
					if (choiceTarget == null) {
						// possibly a choice type
						choiceTarget = parentEl.getChildByName(childFhirPath + "[x]");
					}
					choiceTarget
							.getMutator()
							.setValue(theTargetChildDefinition.getParent().getBase(), theReplacementValue);
				}
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
			List<IBase> existing = new ArrayList<>(runtimeDef.getAccessor().getValues(containingElement));
			if (existing.isEmpty()) {
				// no elements to replace - we shouldn't see this here though
				String msg = myContext
						.getLocalizer()
						.getMessage(FhirPatch.class, "noMatchingElementForPath", theParsedFhirPath.getRawPath());
				theOutcome.addError(msg);
				return;
			}

			List<IBase> replaceables;
			if (FhirPathUtils.isSubsettingNode(theParsedFhirPath.getTail())) {
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
						})
						.toList();
			}

			if (replaceables.size() != 1) {
				throw new InvalidRequestException(
						Msg.code(2715) + " Expected to find a single element, but provided FhirPath returned "
								+ replaceables.size() + " elements.");
			}
			IBase valueToReplace = replaceables.get(0);

			BaseRuntimeChildDefinition.IMutator listMutator = runtimeDef.getMutator();
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
			runtimeDef.getMutator().setValue(containingElement, theReplacementValue);
		}
	}

	private List<IBase> applySubsettingFilter(
			ParsedFhirPath theParsed, ParsedFhirPath.FhirPathNode tail, List<IBase> filtered) {
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
						throw new InvalidRequestException(
								Msg.code(2710) + " List contains more than a single element.");
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
					throw new InvalidRequestException(
							Msg.code(2712) + " Invalid fhir path element encountered: " + theParsed.getRawPath());
				}
				default -> {
					// we shouldn't see this; it means we have not handled a filtering case
					throw new InvalidRequestException(
							Msg.code(2711) + " Unrecognized filter of type " + tail.getValue());
				}
			}
		}
	}

	private static String normalizeExtensionFunctionSyntax(String thePath) {
		return EXTENSION_FUNCTION_PATTERN.matcher(thePath).replaceAll("$1.where(url='$2')");
	}

	private void throwNoElementsError(String theFullReplacePath) {
		String msg =
				myContext.getLocalizer().getMessage(FhirPatch.class, "noMatchingElementForPath", theFullReplacePath);
		throw new InvalidRequestException(Msg.code(2761) + msg);
	}

	private void handleMoveOperation(@Nullable IBaseResource theResource, IBase theParameters) {
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

		if (theResource == null) {
			return;
		}

		List<IBase> containingElements = myContext.newFhirPath().evaluate(theResource, containingPath, IBase.class);
		for (IBase nextElement : containingElements) {

			ChildDefinition childDefinition = findChildDefinition(nextElement, elementName);

			List<IBase> existingValues = new ArrayList<>(
					childDefinition.getUseableChildDef().getAccessor().getValues(nextElement));
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
	 * Returns {@link Optional#empty()} if the child could not be found, which is an error
	 * and should result in aborting the operation.
	 */
	private Optional<FhirPathChildDefinition> childDefinition(
			FhirPathChildDefinition theParent,
			List<String> theFhirPathParts,
			@Nonnull IBase theBase,
			IFhirPath theFhirPath,
			ParsedFhirPath theParsedFhirPath,
			String theOriginalPath,
			PatchOutcome theOutcome) {
		FhirPathChildDefinition definition = new FhirPathChildDefinition();
		definition.setBase(theBase); // set this IBase value
		BaseRuntimeElementDefinition<?> parentElementDefinition = myContext.getElementDefinition(theBase.getClass());
		definition.setElementDefinition(parentElementDefinition); // set this element

		String head = theParsedFhirPath.getHead().getValue();
		definition.setFhirPath(head);

		if (theParent.getElementDefinition() != null) {
			BaseRuntimeChildDefinition runtimeChild =
					theParent.getElementDefinition().getChildByName(head);
			if (runtimeChild == null) {
				// possibly a choice type (e.g. value[x] on Extension)
				runtimeChild = theParent.getElementDefinition().getChildByName(head + "[x]");
			}
			definition.setBaseRuntimeDefinition(runtimeChild);
		}

		String rawPath = theParsedFhirPath.getRawPath();

		if (rawPath.equalsIgnoreCase(head)) {
			// we're at the bottom
			// return
			return Optional.of(definition);
		}

		// detach the head
		String headVal = theFhirPathParts.remove(0);
		String pathBeneathParent = rawPath.substring(headVal.length());
		pathBeneathParent = FhirPathUtils.cleansePath(pathBeneathParent);

		if (isNotBlank(pathBeneathParent) && !theFhirPathParts.isEmpty()) {
			Stack<ParsedFhirPath.FhirPathNode> filteringNodes = new Stack<>();

			String childFilteringPath = pathBeneathParent;
			String nextPath = pathBeneathParent;

			if (FhirPathUtils.isSubsettingNode(theParsedFhirPath.getTail())) {
				// the final node in this path is .first() or [0]... etc
				ParsedFhirPath.FhirPathNode filteringNode = theParsedFhirPath.getTail();
				filteringNodes.push(filteringNode);
				/*
				 * the field filtering path will be the path - tail value.
				 * This will also be nextPath (the one we recurse on)
				 */
				int endInd = pathBeneathParent.indexOf(filteringNode.getValue());
				if (endInd == -1) {
					endInd = pathBeneathParent.length();
				}
				childFilteringPath = pathBeneathParent.substring(0, endInd);
				childFilteringPath = FhirPathUtils.cleansePath(childFilteringPath);
				nextPath = childFilteringPath;
			}

			String directChildName = theFhirPathParts.get(0);

			ParsedFhirPath newPath = ParsedFhirPath.parse(nextPath);

			if (newPath.getHead() instanceof ParsedFhirPath.FhirPathFunction fn && fn.hasContainedExp()) {
				if (fn.isFilter()) {
					// Filter function (e.g. where()) already applied in parent recursion - skip it
					if (fn.hasNext()) {
						String remainingPath =
								newPath.getTopLevelPathFromTo(node -> node == fn.getNext(), node -> false);
						newPath = ParsedFhirPath.parse(remainingPath);
						childFilteringPath = remainingPath;
					} else {
						childFilteringPath = "";
					}
				} else {
					newPath = fn.getContainedExp();
					childFilteringPath = newPath.getRawPath();
				}
			}

			// get all direct children
			ParsedFhirPath.FhirPathNode newHead = newPath.getHead();
			List<IBase> allChildren;
			if (directChildName.equals("div")) {
				/*
				 * We handle XHTML a bit differently, since it isn't like any of the other FHIR types
				 */
				allChildren = myContext.newTerser().getValues(theBase, directChildName);
			} else {
				allChildren = theFhirPath.evaluate(theBase, directChildName, IBase.class);
			}

			// go through the children and take only the ones that match the path we have
			String filterPath = childFilteringPath;

			List<IBase> childs;
			if (filterPath.startsWith(newHead.getValue()) && !filterPath.equalsIgnoreCase(newHead.getValue())) {
				filterPath = filterPath.substring(newHead.getValue().length());
				filterPath = FhirPathUtils.cleansePath(filterPath);

				if (newPath.getHead().getNext() != null
						&& FhirPathUtils.isSubsettingNode(newPath.getHead().getNext())) {
					// yet another filter node
					ParsedFhirPath.FhirPathNode filterNode = newPath.getHead().getNext();
					filteringNodes.push(filterNode);

					String newRaw = newPath.getRawPath();
					String updated = "";
					if (filterNode.hasNext()) {
						updated = newRaw.substring(
								newRaw.indexOf(filterNode.getNext().getValue()));
						updated = FhirPathUtils.cleansePath(updated);
					}
					filterPath = updated;
					updated = newPath.getHead().getValue() + "." + updated;
					newPath = ParsedFhirPath.parse(updated);
				}
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
								})
								.toList();
					} else {
						// there is only 1 child (probably a top level element)
						// we still filter own because child elements can have different child types (that might match
						// multiple childs)
						// eg: everything has "extension" on it
						childs = allChildren.stream()
								.filter(el -> {
									Optional<IBase> match = theFhirPath.evaluateFirst(el, ref.get(), IBase.class);
									return match.isPresent();
								})
								.findFirst()
								.stream()
								.toList();
					}
				}
			} else {
				childs = allChildren;
			}

			while (!filteringNodes.empty()) {
				ParsedFhirPath.FhirPathNode filteringNode = filteringNodes.pop();
				childs = applySubsettingFilter(newPath, filteringNode, childs);
			}

			// should only be one
			if (childs.size() != 1) {
				if (childs.isEmpty()) {
					throwNoElementsError(theOriginalPath);
				}
				throw new InvalidRequestException(
						Msg.code(2704) + " FhirPath returns more than 1 element: " + theOriginalPath);
			}
			IBase child = childs.get(0);

			Optional<FhirPathChildDefinition> fhirPathChildDefinition = childDefinition(
					definition, theFhirPathParts, child, theFhirPath, newPath, theOriginalPath, theOutcome);
			if (fhirPathChildDefinition.isEmpty()) {
				return Optional.empty();
			}
			definition.setChild(fhirPathChildDefinition.get());
		}

		return Optional.of(definition);
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

				// Special case for Extension.url because it isn't a normal model element
				if ("url".equals(name)) {
					if (theDefinition.getChildElement().getName().equals("Extension")) {
						if (optionalValue.get() instanceof IPrimitiveType<?> primitive) {
							((IBaseExtension<?, ?>) newElement).setUrl(primitive.getValueAsString());
							continue;
						}
					}
				}

				// we have a dataType. let's extract its value and assign it.
				ChildDefinition childDefinition;
				childDefinition = findChildDefinition(newElement, name);

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
				ChildDefinition childDefinition = findChildDefinition(newElement, name);

				IBase childNewValue = createAndPopulateNewElement(childDefinition, part);

				childDefinition.getUseableChildDef().getMutator().setValue(newElement, childNewValue);
			}
		}

		return newElement;
	}

	private int deleteSingleElement(IBase theElementToDelete) {
		if (myContext.newTerser().clear(theElementToDelete)) {
			return 1;
		} else {
			return 0;
		}
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

	/**
	 * Validates a FHIRPatch Parameters document and throws an {@link InvalidRequestException}
	 * if it is not valid. Note, in previous versions of HAPI FHIR this method threw an exception
	 * but the full error list is now returned instead.
	 *
	 * @param thePatch The Parameters resource to validate as a FHIRPatch document
	 * @return Returns a {@link PatchOutcome} containing any errors
	 * @since 8.4.0
	 */
	public PatchOutcome validate(IBaseResource thePatch) {
		PatchOutcome retVal = new PatchOutcome();
		doApply(null, thePatch, retVal);
		return retVal;
	}

	private static IFhirPath.IParsedExpression parseFhirPathExpression(IFhirPath fhirPath, String containingPath) {
		IFhirPath.IParsedExpression retVal;
		try {
			retVal = fhirPath.parse(containingPath);
		} catch (Exception theE) {
			throw new InvalidRequestException(
					Msg.code(2726) + String.format(" %s is not a valid fhir path", containingPath), theE);
		}
		return retVal;
	}

	public static class PatchOutcome {

		private List<String> myErrors = new ArrayList<>(1);

		public void addError(String theError) {
			myErrors.add(theError);
		}

		public List<String> getErrors() {
			return myErrors;
		}

		public boolean hasErrors() {
			return !myErrors.isEmpty();
		}
	}
}
