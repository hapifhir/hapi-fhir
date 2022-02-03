package ca.uhn.fhir.jpa.patch;

/*-
 * #%L
 * HAPI FHIR JPA Server
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
import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.path.EncodeContextPath;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.IModelVisitor2;
import ca.uhn.fhir.util.ParametersUtil;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseEnumeration;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.utilities.xhtml.XhtmlNode;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class FhirPatch {

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

		List<IBase> opParameters = ParametersUtil.getNamedParameters(myContext, thePatch, "operation");
		for (IBase nextOp : opParameters) {
			String type = ParametersUtil.getParameterPartValueAsString(myContext, nextOp, "type");
			String path = ParametersUtil.getParameterPartValueAsString(myContext, nextOp, "path");
			Optional<IBase> valuePart = ParametersUtil.getParameterPart(myContext, nextOp, "value");
			Optional<IBase> valuePartValue = ParametersUtil.getParameterPartValue(myContext, nextOp, "value");

			type = defaultString(type);
			path = defaultString(path);

			String containingPath;
			String elementName;
			Integer removeIndex = null;
			Integer insertIndex = null;
			if ("delete".equals(type)) {

				doDelete(theResource, path);
				return;

			} else if ("add".equals(type)) {

				containingPath = path;
				elementName = ParametersUtil.getParameterPartValueAsString(myContext, nextOp, "name");

			} else if ("replace".equals(type)) {

				int lastDot = path.lastIndexOf(".");
				containingPath = path.substring(0, lastDot);
				elementName = path.substring(lastDot + 1);

			} else if ("insert".equals(type)) {

				int lastDot = path.lastIndexOf(".");
				containingPath = path.substring(0, lastDot);
				elementName = path.substring(lastDot + 1);
				insertIndex = ParametersUtil
					.getParameterPartValue(myContext, nextOp, "index")
					.map(t -> (IPrimitiveType<Integer>) t)
					.map(t -> t.getValue())
					.orElseThrow(() -> new InvalidRequestException("No index supplied for insert operation"));

			} else if ("move".equals(type)) {

				int lastDot = path.lastIndexOf(".");
				containingPath = path.substring(0, lastDot);
				elementName = path.substring(lastDot + 1);
				insertIndex = ParametersUtil
					.getParameterPartValue(myContext, nextOp, "destination")
					.map(t -> (IPrimitiveType<Integer>) t)
					.map(t -> t.getValue())
					.orElseThrow(() -> new InvalidRequestException("No index supplied for insert operation"));
				removeIndex = ParametersUtil
					.getParameterPartValue(myContext, nextOp, "source")
					.map(t -> (IPrimitiveType<Integer>) t)
					.map(t -> t.getValue())
					.orElseThrow(() -> new InvalidRequestException("No index supplied for insert operation"));

			} else {

				throw new InvalidRequestException(Msg.code(1267) + "Unknown patch operation type: " + type);

			}

			List<IBase> paths = myContext.newFhirPath().evaluate(theResource, containingPath, IBase.class);
			for (IBase next : paths) {

				BaseRuntimeElementDefinition<?> elementDef = myContext.getElementDefinition(next.getClass());

				String childName = elementName;
				BaseRuntimeChildDefinition childDef = elementDef.getChildByName(childName);
				BaseRuntimeElementDefinition<?> childElement;
				if (childDef == null) {
					childName = elementName + "[x]";
					childDef = elementDef.getChildByName(childName);
					childElement = childDef.getChildByName(childDef.getValidChildNames().iterator().next());
				} else {
					childElement = childDef.getChildByName(childName);
				}

				if ("move".equals(type)) {

					List<IBase> existingValues = new ArrayList<>(childDef.getAccessor().getValues(next));
					if (removeIndex == null || removeIndex >= existingValues.size()) {
						String msg = myContext.getLocalizer().getMessage(FhirPatch.class, "invalidMoveSourceIndex", removeIndex, path, existingValues.size());
						throw new InvalidRequestException(Msg.code(1268) + msg);
					}
					IBase newValue = existingValues.remove(removeIndex.intValue());

					if (insertIndex == null || insertIndex > existingValues.size()) {
						String msg = myContext.getLocalizer().getMessage(FhirPatch.class, "invalidMoveDestinationIndex", insertIndex, path, existingValues.size());
						throw new InvalidRequestException(Msg.code(1269) + msg);
					}
					existingValues.add(insertIndex, newValue);

					childDef.getMutator().setValue(next, null);
					for (IBase nextNewValue : existingValues) {
						childDef.getMutator().addValue(next, nextNewValue);
					}

					continue;
				}

				IBase newValue;
				if (valuePartValue.isPresent()) {
					newValue = valuePartValue.get();
				} else {
					newValue = childElement.newInstance();

					if (valuePart.isPresent()) {
						List<IBase> valuePartParts = myContext.newTerser().getValues(valuePart.get(), "part");
						for (IBase nextValuePartPart : valuePartParts) {

							String name = myContext.newTerser().getSingleValue(nextValuePartPart, "name", IPrimitiveType.class).map(t -> t.getValueAsString()).orElse(null);
							if (isNotBlank(name)) {

								Optional<IBase> value = myContext.newTerser().getSingleValue(nextValuePartPart, "value[x]", IBase.class);
								if (value.isPresent()) {

									BaseRuntimeChildDefinition partChildDef = childElement.getChildByName(name);
									partChildDef.getMutator().addValue(newValue, value.get());

								}

							}

						}
					}

				}

				if (IBaseEnumeration.class.isAssignableFrom(childElement.getImplementingClass()) || XhtmlNode.class.isAssignableFrom(childElement.getImplementingClass())) {
					// If the compositeElementDef is an IBaseEnumeration, we will use the actual compositeElementDef definition to build one, since
					// it needs the right factory object passed to its constructor
					IPrimitiveType<?> newValueInstance = (IPrimitiveType<?>) childElement.newInstance();
					newValueInstance.setValueAsString(((IPrimitiveType<?>) newValue).getValueAsString());
					childDef.getMutator().setValue(next, newValueInstance);
					newValue = newValueInstance;
				}

				if ("insert".equals(type)) {

					List<IBase> existingValues = new ArrayList<>(childDef.getAccessor().getValues(next));
					if (insertIndex == null || insertIndex > existingValues.size()) {
						String msg = myContext.getLocalizer().getMessage(FhirPatch.class, "invalidInsertIndex", insertIndex, path, existingValues.size());
						throw new InvalidRequestException(Msg.code(1270) + msg);
					}
					existingValues.add(insertIndex, newValue);

					childDef.getMutator().setValue(next, null);
					for (IBase nextNewValue : existingValues) {
						childDef.getMutator().addValue(next, nextNewValue);
					}

				} else {
					childDef.getMutator().setValue(next, newValue);
				}

			}


		}

	}

	private void doDelete(IBaseResource theResource, String thePath) {
		List<IBase> paths = myContext.newFhirPath().evaluate(theResource, thePath, IBase.class);
		for (IBase next : paths) {
			myContext.newTerser().visit(next, new IModelVisitor2() {
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
	}

	public IBaseParameters diff(@Nullable IBaseResource theOldValue, @Nonnull IBaseResource theNewValue) {
		IBaseParameters retVal = ParametersUtil.newInstance(myContext);
		String newValueTypeName = myContext.getResourceDefinition(theNewValue).getName();

		if (theOldValue == null) {

			IBase operation = ParametersUtil.addParameterToParameters(myContext, retVal, "operation");
			ParametersUtil.addPartCode(myContext, operation, "type", "insert");
			ParametersUtil.addPartString(myContext, operation, "path", newValueTypeName);
			ParametersUtil.addPart(myContext, operation, "value", theNewValue);

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
			IBase operation = ParametersUtil.addParameterToParameters(myContext, theDiff, "operation");
			ParametersUtil.addPartCode(myContext, operation, "type", "replace");
			ParametersUtil.addPartString(myContext, operation, "path", theTargetPath);
			addValueToDiff(operation, theOldField, theNewField);
		} else {
			if (theOldField instanceof IPrimitiveType) {
				IPrimitiveType<?> oldPrimitive = (IPrimitiveType<?>) theOldField;
				IPrimitiveType<?> newPrimitive = (IPrimitiveType<?>) theNewField;
				String oldValueAsString = toValue(oldPrimitive);
				String newValueAsString = toValue(newPrimitive);
				if (!Objects.equals(oldValueAsString, newValueAsString)) {
					IBase operation = ParametersUtil.addParameterToParameters(myContext, theDiff, "operation");
					ParametersUtil.addPartCode(myContext, operation, "type", "replace");
					ParametersUtil.addPartString(myContext, operation, "path", theTargetPath);
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
			IBase operation = ParametersUtil.addParameterToParameters(myContext, theDiff, "operation");
			ParametersUtil.addPartCode(myContext, operation, "type", "delete");
			ParametersUtil.addPartString(myContext, operation, "path", theTargetPath + "." + elementName + (repeatable ? "[" + targetIndex + "]" : ""));

			sourceIndex++;
			targetIndex++;
		}

		theSourceEncodePath.popPath();
	}

	private void addInsertItems(IBaseParameters theDiff, List<? extends IBase> theTargetValues, int theTargetIndex, String thePath, BaseRuntimeChildDefinition theChildDefinition) {
		IBase operation = ParametersUtil.addParameterToParameters(myContext, theDiff, "operation");
		ParametersUtil.addPartCode(myContext, operation, "type", "insert");
		ParametersUtil.addPartString(myContext, operation, "path", thePath);
		ParametersUtil.addPartInteger(myContext, operation, "index", theTargetIndex);

		IBase value = theTargetValues.get(theTargetIndex);
		BaseRuntimeElementDefinition<?> valueDef = myContext.getElementDefinition(value.getClass());

		/*
		 * If the value is a Resource or a datatype, we can put it into the part.value and that will cover
		 * all of its children. If it's an infrastructure element though, such as Patient.contact we can't
		 * just put it into part.value because it isn't an actual type. So we have to put all of its
		 * childen in instead.
		 */
		if (valueDef.isStandardType()) {
			ParametersUtil.addPart(myContext, operation, "value", value);
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
		ParametersUtil.addPart(myContext, theOperationPart, "value", newValue);
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
		// XHTML content is dealt with by putting it in a string
		if (theNewValue instanceof XhtmlNode) {
			String xhtmlString = ((XhtmlNode) theNewValue).getValueAsString();
			theNewValue = myContext.getElementDefinition("string").newInstance(xhtmlString);
		}

		// IIdType can hold a fully qualified ID, but we just want the ID part to show up in diffs
		if (theNewValue instanceof IIdType) {
			String idPart = ((IIdType) theNewValue).getIdPart();
			theNewValue = myContext.getElementDefinition("id").newInstance(idPart);
		}

		return theNewValue;
	}

	private String toValue(IPrimitiveType<?> theOldPrimitive) {
		if (theOldPrimitive instanceof IIdType) {
			return ((IIdType) theOldPrimitive).getIdPart();
		}
		return theOldPrimitive.getValueAsString();
	}
}
