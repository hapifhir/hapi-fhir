/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.parser.path.EncodeContextPath;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.IModelVisitor2;
import ca.uhn.fhir.util.ParametersUtil;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.utilities.xhtml.XhtmlNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class FhirPatch2 {
	private static final Logger ourLog = LoggerFactory.getLogger(FhirPatch2.class);

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

	public FhirPatch2(FhirContext theContext) {
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
//				handleAddOperation(theResource, nextOperation);
				handleAddOperation2(theResource, nextOperation);
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

	private void handleAddOperation2(IBaseResource theResource, IBase theParameters) {

		final FhirTerser fhirTerser = myContext.newTerser();

		String path = ParametersUtil.getParameterPartValueAsString(myContext, theParameters, PARAMETER_PATH);
		String elementName = ParametersUtil.getParameterPartValueAsString(myContext, theParameters, PARAMETER_NAME);
		final Optional<IBase> optValuePartValue = ParametersUtil.getParameterPartValue(myContext, theParameters, PARAMETER_VALUE);

		String containingPath = defaultString(path);

		final List<IBase> valuesFromTerser = fhirTerser.getValues(theResource, path, true);

		for (IBase valueFromTerser : valuesFromTerser) {
			boolean doAnyMatch = false;
			if (optValuePartValue.isPresent()) {
				final IBase valueFromParameter = optValuePartValue.get();
				for (IPrimitiveType<?> primitiveType : fhirTerser.getValues(valueFromTerser, elementName, IPrimitiveType.class)) {
					if (primitiveType.getValue().equals(valueFromParameter)) {
						doAnyMatch = true;
					}
				}
			}
		}

		List<IBase> containingElements = myContext.newFhirPath().evaluate(theResource, containingPath, IBase.class);
	}

	private void handleAddOperation(IBaseResource theResource, IBase theParameters) {

		final FhirTerser fhirTerser = myContext.newTerser();

		String path = ParametersUtil.getParameterPartValueAsString(myContext, theParameters, PARAMETER_PATH);
		String elementName = ParametersUtil.getParameterPartValueAsString(myContext, theParameters, PARAMETER_NAME);
		final Optional<IBase> optValuePartValue = ParametersUtil.getParameterPartValue(myContext, theParameters, PARAMETER_VALUE);

		String containingPath = defaultString(path);

		// TODO:  use case 1:  there is no tree at all (ie: Patient.link.other.reference where no reference exists)
		// TODO:  use case 2:  there is a tree  but we have a new value and we want to add a new element (ex:  a Patient.link.other.reference exists, but we want another one)
		// TODO:  use case 3:  there is a tree  but we have a duplicate value so we don't want to add a new element, nor edit the existing one (ex:  a Patient.link.other.reference exists, but we want another one)
		// TODO:  use case 4:  everything else

		List<IBase> containingElements = myContext.newFhirPath().evaluate(theResource, containingPath, IBase.class);
		for (IBase nextElement : containingElements) {
			ChildDefinition childDefinition = findChildDefinition(nextElement, elementName);

			IBase newValue = getNewValue(theParameters, nextElement, childDefinition);

			// TODO:  can't handle direct reference to a resource (ex: "Patient")
			// TODO:  can't handle indexed XPath (ex: "Patient.contact[]") Unknown child name 'contact[0]' in element Patient
			final boolean shouldProceed = isPathSupportedByTerser(containingPath) && optValuePartValue.isPresent();

			if (! shouldProceed) {
				// TODO:  this will effectively replace "Patient/123" with "Patient/456"
				childDefinition.getChildDef().getMutator().addValue(nextElement, newValue);
			} else {
					final String[] split = containingPath.split("\\.");
					final List<IBase> valuesForFullPath = fhirTerser.getValues(theResource, path, false);
					for (IBase value : valuesForFullPath) {
						ourLog.info("value.getClass(): {}", value.getClass());
						// TODO:  I want to match up the parameter value for the element in question with the existing value and ensure they're different
						// TODO:  how do I automagically do a value comparison for a leaf node?
						// TODO: handle multiple primitive value
//						final String singlePrimitiveValueOrNull = fhirTerser.getSinglePrimitiveValueOrNull(value, elementName);
						final List<IBase> valuesFromBase = fhirTerser.getValues(value, elementName);
						// TODO:  loop over them, check if it's a primitive, and then compare to the parameter value
//						if (value instanceof Reference && optValuePartValue.get() instanceof StringType) {

						for (IBase valueFromBase : valuesFromBase) {
							if( valueFromBase instanceof IPrimitiveType<?>) {
								final IPrimitiveType<?> castedValue = (IPrimitiveType<?>)valuesFromBase;
							}
						}

						if (optValuePartValue.get() instanceof IPrimitiveType<?>) {
							ourLog.info("value  instanceof IPrimitiveType: {}", value  instanceof IPrimitiveType);
							// TODO:  false
//							final Reference existingReference = (Reference) value;
							final StringType parameterStringType = (StringType) optValuePartValue.get();

//							if (parameterStringType.getValue().equals(singlePrimitiveValueOrNull)) {
								if (parameterStringType.getValue().equals("")) {
//							if (existingReference.getReference().equals(parameterStringType.getValue())) {
								ourLog.info("Trying to add duplicate reference");
							} else {
								ourLog.info("Trying to add new reference");

								if (split.length > 1) {
									final String firstChunkOfPath = split[0] + "." + split[1];
									IBase iBase = fhirTerser.addElement(theResource, firstChunkOfPath);
									ourLog.info("iBase: {}", iBase);

									if (split.length > 2) {
										for (int index = 2; index < split.length; index++) {
											ourLog.info("split[{}]: {}", index, split[index]);
											iBase = fhirTerser.addElement(iBase, split[index]);
										}
									}

									fhirTerser.addElement(iBase, elementName, parameterStringType.getValue());
								} else {
									// TODO:  what does it mean if I end up here?
									ourLog.info("split.length <= 1");
								}
							}
						} // else:  the reference is a duplicate:  so do nothing.... you may be able t o fall back on the childDefinition
					}
			}
		}

		// TODO:  we probably want to reuse this pattern with the above code
		if (containingElements.isEmpty()) {
			ourLog.info("containingElements is empty for path: {}", path);

			final String[] split = path.split("\\.");

			ourLog.info("split: {}", Arrays.toString(split));

			String previousSubPath = null;
			// TODO:  what if there is only one split?
			for (int index = 0; index < split.length; index++) {
				// TODO:  this works even though it's a little gross

				// Add new elements until we reach the end of the path and don't bust the array limit
				if (! path.equals(previousSubPath)) {
					final String subPath = subPath(split, index + 2, ".");
					final List<IBase> values = fhirTerser.getValues(theResource, subPath, true);
					ourLog.info("values: {}", values);
					for (IBase value : values) {
						if(value instanceof IPrimitiveType<?>) {
							final IPrimitiveType<?> castedValue = (IPrimitiveType<?>)value;
//							optValuePartValue.ifPresent(valuePart -> ((IBaseReference) iBase).setReference(valuePart.toString()));
						}
					}
//					// TODO:  what happens if we have zero elements?  is that even possible?
//					if (values.size() == 1) {
//						final IBase iBase = values.get(0);
//						// TODO: do we need a hornet's nest of ugly conditional logical or is there code that automatically handles different IBase values?
//						if (iBase instanceof IBaseReference) {
//							optValuePartValue.ifPresent(valuePart -> ((IBaseReference) iBase).setReference(valuePart.toString()));
//						}
//					}
					previousSubPath = subPath;
				} else {
					ourLog.info("we're done!");
					break;
				}
			}
		}
	}

	// TODO:  this needs to become much more sophisticated
	private boolean isPathSupportedByTerser(String path) {
		return path.contains(".") && ! path.contains("[") && ! path.contains("(");
	}

	private String subPath(String[] split, int numDelims, String delim) {
		return Arrays.stream(split, 0, numDelims)
			.collect(Collectors.joining(delim));
	}

	private void handleInsertOperation(IBaseResource theResource, IBase theParameters) {

		String path = ParametersUtil.getParameterPartValueAsString(myContext, theParameters, PARAMETER_PATH);
		path = defaultString(path);

		int lastDot = path.lastIndexOf(".");
		String containingPath = path.substring(0, lastDot);
		String elementName = path.substring(lastDot + 1);
		Integer insertIndex = ParametersUtil
			.getParameterPartValueAsInteger(myContext, theParameters, PARAMETER_INDEX)
			.orElseThrow(() -> new InvalidRequestException("No index supplied for insert operation"));

		List<IBase> containingElements = myContext.newFhirPath().evaluate(theResource, containingPath, IBase.class);
		for (IBase nextElement : containingElements) {

			ChildDefinition childDefinition = findChildDefinition(nextElement, elementName);

			IBase newValue = getNewValue(theParameters, nextElement, childDefinition);

			List<IBase> existingValues = new ArrayList<>(childDefinition.getChildDef().getAccessor().getValues(nextElement));
			if (insertIndex == null || insertIndex < 0 || insertIndex > existingValues.size()) {
				String msg = myContext.getLocalizer().getMessage(FhirPatch2.class, "invalidInsertIndex", insertIndex, path, existingValues.size());
				throw new InvalidRequestException(Msg.code(1270) + msg);
			}
			existingValues.add(insertIndex, newValue);

			childDefinition.getChildDef().getMutator().setValue(nextElement, null);
			for (IBase nextNewValue : existingValues) {
				childDefinition.getChildDef().getMutator().addValue(nextElement, nextNewValue);
			}
		}
	}

	private void handleDeleteOperation(IBaseResource theResource, IBase theParameters) {

		String path = ParametersUtil.getParameterPartValueAsString(myContext, theParameters, PARAMETER_PATH);
		path = defaultString(path);

		String containingPath;
		String elementName;

		if (path.endsWith(")")) {
			// This is probably a filter, so we're probably dealing with a list
			int filterArgsIndex = path.lastIndexOf('('); // Let's hope there aren't nested parentheses
			int lastDotIndex = path.lastIndexOf('.', filterArgsIndex); // There might be a dot inside the parentheses, so look to the left of that
			int secondLastDotIndex = path.lastIndexOf('.', lastDotIndex-1);
			containingPath = path.substring(0, secondLastDotIndex);
			elementName = path.substring(secondLastDotIndex + 1, lastDotIndex);
		} else if (path.endsWith("]")) {
			// This is almost definitely a list
			int openBracketIndex = path.lastIndexOf('[');
			int lastDotIndex = path.lastIndexOf('.', openBracketIndex);
			containingPath = path.substring(0, lastDotIndex);
			elementName = path.substring(lastDotIndex + 1, openBracketIndex);
		} else {
			containingPath = path;
			elementName = null;
		}

		List<IBase> containingElements = myContext.newFhirPath().evaluate(theResource, containingPath, IBase.class);
		for (IBase nextElement : containingElements) {
			if (elementName == null) {
				deleteSingleElement(nextElement);
			} else {
				deleteFromList(theResource, nextElement, elementName, path);
			}
		}
	}

	private void deleteFromList(IBaseResource theResource, IBase theContainingElement, String theListElementName, String theElementToDeletePath) {
		ChildDefinition childDefinition = findChildDefinition(theContainingElement, theListElementName);

		List<IBase> existingValues = new ArrayList<>(childDefinition.getChildDef().getAccessor().getValues(theContainingElement));
		List<IBase> elementsToRemove = myContext.newFhirPath().evaluate(theResource, theElementToDeletePath, IBase.class);
		existingValues.removeAll(elementsToRemove);

		childDefinition.getChildDef().getMutator().setValue(theContainingElement, null);
		for (IBase nextNewValue : existingValues) {
			childDefinition.getChildDef().getMutator().addValue(theContainingElement, nextNewValue);
		}
	}

	private void handleReplaceOperation(IBaseResource theResource, IBase theParameters) {
		String path = ParametersUtil.getParameterPartValueAsString(myContext, theParameters, PARAMETER_PATH);
		path = defaultString(path);

		int lastDot = path.lastIndexOf(".");
		String containingPath = path.substring(0, lastDot);
		String elementName = path.substring(lastDot + 1);

		List<IBase> containingElements = myContext.newFhirPath().evaluate(theResource, containingPath, IBase.class);
		for (IBase nextElement : containingElements) {

			ChildDefinition childDefinition = findChildDefinition(nextElement, elementName);

			IBase newValue = getNewValue(theParameters, nextElement, childDefinition);

			childDefinition.getChildDef().getMutator().setValue(nextElement, newValue);
		}
	}

	private void handleMoveOperation(IBaseResource theResource, IBase theParameters) {
		String path = ParametersUtil.getParameterPartValueAsString(myContext, theParameters, PARAMETER_PATH);
		path = defaultString(path);

		int lastDot = path.lastIndexOf(".");
		String containingPath = path.substring(0, lastDot);
		String elementName = path.substring(lastDot + 1);
		Integer insertIndex = ParametersUtil
			.getParameterPartValueAsInteger(myContext, theParameters, PARAMETER_DESTINATION)
			.orElseThrow(() -> new InvalidRequestException("No index supplied for move operation"));
		Integer removeIndex = ParametersUtil
			.getParameterPartValueAsInteger(myContext, theParameters, PARAMETER_SOURCE)
			.orElseThrow(() -> new InvalidRequestException("No index supplied for move operation"));

		List<IBase> containingElements = myContext.newFhirPath().evaluate(theResource, containingPath, IBase.class);
		for (IBase nextElement : containingElements) {

			ChildDefinition childDefinition = findChildDefinition(nextElement, elementName);

			List<IBase> existingValues = new ArrayList<>(childDefinition.getChildDef().getAccessor().getValues(nextElement));
			if (removeIndex == null || removeIndex < 0 || removeIndex >= existingValues.size()) {
				String msg = myContext.getLocalizer().getMessage(FhirPatch2.class, "invalidMoveSourceIndex", removeIndex, path, existingValues.size());
				throw new InvalidRequestException(Msg.code(1268) + msg);
			}
			IBase newValue = existingValues.remove(removeIndex.intValue());

			if (insertIndex == null || insertIndex < 0 || insertIndex > existingValues.size()) {
				String msg = myContext.getLocalizer().getMessage(FhirPatch2.class, "invalidMoveDestinationIndex", insertIndex, path, existingValues.size());
				throw new InvalidRequestException(Msg.code(1269) + msg);
			}
			existingValues.add(insertIndex, newValue);

			childDefinition.getChildDef().getMutator().setValue(nextElement, null);
			for (IBase nextNewValue : existingValues) {
				childDefinition.getChildDef().getMutator().addValue(nextElement, nextNewValue);
			}
		}
	}

	private ChildDefinition findChildDefinition(IBase theContainingElement, String theElementName) {
		BaseRuntimeElementDefinition<?> elementDef = myContext.getElementDefinition(theContainingElement.getClass());

		String childName = theElementName;
		BaseRuntimeChildDefinition childDef = elementDef.getChildByName(childName);
		BaseRuntimeElementDefinition<?> childElement;
		if (childDef == null) {
			childName = theElementName + "[x]";
			childDef = elementDef.getChildByName(childName);
			childElement = childDef.getChildByName(childDef.getValidChildNames().iterator().next());
		} else {
			childElement = childDef.getChildByName(childName);
		}

		return new ChildDefinition(childDef, childElement);
	}

	private IBase getNewValue(IBase theParameters, IBase theElement, ChildDefinition theChildDefinition) {
		Optional<IBase> valuePart = ParametersUtil.getParameterPart(myContext, theParameters, PARAMETER_VALUE);
		Optional<IBase> valuePartValue = ParametersUtil.getParameterPartValue(myContext, theParameters, PARAMETER_VALUE);

		IBase newValue;
		if (valuePartValue.isPresent()) {
			newValue = valuePartValue.get();
		} else {
			newValue = theChildDefinition.getChildElement().newInstance();

			if (valuePart.isPresent()) {
				IBase theValueElement = valuePart.get();
				populateNewValue(theChildDefinition, newValue, theValueElement);
			}

		}

		if (IBaseEnumeration.class.isAssignableFrom(theChildDefinition.getChildElement().getImplementingClass()) || XhtmlNode.class.isAssignableFrom(theChildDefinition.getChildElement().getImplementingClass())) {
			// If the compositeElementDef is an IBaseEnumeration, we will use the actual compositeElementDef definition to build one, since
			// it needs the right factory object passed to its constructor
			IPrimitiveType<?> newValueInstance;
			if (theChildDefinition.getChildDef().getInstanceConstructorArguments() != null) {
				newValueInstance = (IPrimitiveType<?>) theChildDefinition.getChildElement().newInstance(
					theChildDefinition.getChildDef().getInstanceConstructorArguments());
			} else {
				newValueInstance = (IPrimitiveType<?>) theChildDefinition.getChildElement().newInstance();
			}
			newValueInstance.setValueAsString(((IPrimitiveType<?>) newValue).getValueAsString());
			theChildDefinition.getChildDef().getMutator().setValue(theElement, newValueInstance);
			newValue = newValueInstance;
		}
		return newValue;
	}

	private void populateNewValue(ChildDefinition theChildDefinition, IBase theNewValue, IBase theValueElement) {
		List<IBase> valuePartParts = myContext.newTerser().getValues(theValueElement, "part");
		for (IBase nextValuePartPart : valuePartParts) {

			String name = myContext.newTerser().getSingleValue(nextValuePartPart, PARAMETER_NAME, IPrimitiveType.class).map(IPrimitiveType::getValueAsString).orElse(null);
			if (isNotBlank(name)) {

				Optional<IBase> value = myContext.newTerser().getSingleValue(nextValuePartPart, "value[x]", IBase.class);
				if (value.isPresent()) {

					BaseRuntimeChildDefinition partChildDef = theChildDefinition.getChildElement().getChildByName(name);
					if (partChildDef == null) {
						name = name + "[x]";
						partChildDef = theChildDefinition.getChildElement().getChildByName(name);
					}
					partChildDef.getMutator().addValue(theNewValue, value.get());

				}

			}

		}
	}

	private void deleteSingleElement(IBase theElementToDelete) {
		myContext.newTerser().visit(theElementToDelete, new IModelVisitor2() {
			@Override
			public boolean acceptElement(IBase theElement, List<IBase> theContainingElementPath, List<BaseRuntimeChildDefinition> theChildDefinitionPath, List<BaseRuntimeElementDefinition<?>> theElementDefinitionPath) {
				if (theElement instanceof IPrimitiveType) {
					((IPrimitiveType<?>) theElement).setValueAsString(null);
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

	public IBaseParameters diff(@Nullable IBaseResource theOldValue, @Nonnull IBaseResource theNewValue) {
		IBaseParameters retVal = ParametersUtil.newInstance(myContext);
		String newValueTypeName = myContext.getResourceDefinition(theNewValue).getName();

		if (theOldValue == null) {

			IBase operation = ParametersUtil.addParameterToParameters(myContext, retVal, PARAMETER_OPERATION);
			ParametersUtil.addPartCode(myContext, operation, PARAMETER_TYPE, OPERATION_INSERT);
			ParametersUtil.addPartString(myContext, operation, PARAMETER_PATH, newValueTypeName);
			ParametersUtil.addPart(myContext, operation, PARAMETER_VALUE, theNewValue);

		} else {

			String oldValueTypeName = myContext.getResourceDefinition(theOldValue).getName();
			Validate.isTrue(oldValueTypeName.equalsIgnoreCase(newValueTypeName), "Resources must be of same type");


			BaseRuntimeElementCompositeDefinition<?> def = myContext.getResourceDefinition(theOldValue).getBaseDefinition();
			String path = def.getName();

			EncodeContextPath contextPath = new EncodeContextPath();
			contextPath.pushPath(path, true);

			compare(retVal, contextPath, def, path, path, theOldValue, theNewValue);

			contextPath.popPath();
			assert contextPath.getPath().isEmpty();
		}

		return retVal;
	}

	private void compare(IBaseParameters theDiff, EncodeContextPath theSourceEncodeContext, BaseRuntimeElementDefinition<?> theDef, String theSourcePath, String theTargetPath, IBase theOldField, IBase theNewField) {

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
				compareField(theDiff, theSourceEncodeContext, theSourcePath, theTargetPath, theOldField, theNewField, nextChild);
			}

		}

	}

	private void compareField(IBaseParameters theDiff, EncodeContextPath theSourceEncodePath, String theSourcePath, String theTargetPath, IBase theOldField, IBase theNewField, BaseRuntimeChildDefinition theChildDef) {
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
			ParametersUtil.addPartString(myContext, operation, PARAMETER_PATH, theTargetPath + "." + elementName + (repeatable ? "[" + targetIndex + "]" : ""));

			sourceIndex++;
			targetIndex++;
		}

		theSourceEncodePath.popPath();
	}

	private void addInsertItems(IBaseParameters theDiff, List<? extends IBase> theTargetValues, int theTargetIndex, String thePath, BaseRuntimeChildDefinition theChildDefinition) {
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
		 * childen in instead.
		 */
		if (valueDef.isStandardType()) {
			ParametersUtil.addPart(myContext, operation, PARAMETER_VALUE, value);
		} else {
			for (BaseRuntimeChildDefinition nextChild : valueDef.getChildren()) {
				List<IBase> childValues = nextChild.getAccessor().getValues(value);
				for (int index = 0; index < childValues.size(); index++) {
					boolean childRepeatable = theChildDefinition.getMax() != 1;
					String elementName = nextChild.getChildNameByDatatype(childValues.get(index).getClass());
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

	private static class ChildDefinition {
		private final BaseRuntimeChildDefinition myChildDef;
		private final BaseRuntimeElementDefinition<?> myChildElement;

		public ChildDefinition(BaseRuntimeChildDefinition theChildDef, BaseRuntimeElementDefinition<?> theChildElement) {
			this.myChildDef = theChildDef;
			this.myChildElement = theChildElement;
		}

		public BaseRuntimeChildDefinition getChildDef() {
			return myChildDef;
		}

		public BaseRuntimeElementDefinition<?> getChildElement() {
			return myChildElement;
		}
	}
}
