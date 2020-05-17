package ca.uhn.fhir.jpa.patch;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.IModelVisitor2;
import ca.uhn.fhir.util.ParametersUtil;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseEnumeration;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.utilities.xhtml.XhtmlNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class FhirPatch {

	private final FhirContext myContext;

	public FhirPatch(FhirContext theContext) {
		myContext = theContext;
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

				throw new InvalidRequestException("Unknown patch operation type: " + type);

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
					if (removeIndex >= existingValues.size()) {
						String msg = myContext.getLocalizer().getMessage(FhirPatch.class, "invalidMoveSourceIndex", removeIndex, path, existingValues.size());
						throw new InvalidRequestException(msg);
					}
					IBase newValue = existingValues.remove(removeIndex.intValue());

					if (insertIndex > existingValues.size()) {
						String msg = myContext.getLocalizer().getMessage(FhirPatch.class, "invalidMoveDestinationIndex", insertIndex, path, existingValues.size());
						throw new InvalidRequestException(msg);
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

									BaseRuntimeChildDefinition partChildDef = ((BaseRuntimeElementCompositeDefinition<?>) childElement).getChildByName(name);
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
					if (insertIndex > existingValues.size()) {
						String msg = myContext.getLocalizer().getMessage(FhirPatch.class, "invalidInsertIndex", insertIndex, path, existingValues.size());
						throw new InvalidRequestException(msg);
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


	public void doDelete(IBaseResource theResource, String thePath) {
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

	public IBaseParameters diff(IBaseResource theOldValue, IBaseResource theNewValue) {
		String oldValueTypeName = myContext.getResourceDefinition(theOldValue).getName();
		String newValueTypeName = myContext.getResourceDefinition(theNewValue).getName();
		Validate.isTrue(oldValueTypeName.equalsIgnoreCase(newValueTypeName), "Resources must be of same type");

		IBaseParameters retVal = ParametersUtil.newInstance(myContext);

		BaseRuntimeElementCompositeDefinition<?> def = myContext.getResourceDefinition(theOldValue).getBaseDefinition();
		String path = def.getName();

		compare(retVal, def, path, path, theOldValue, theNewValue);

		return retVal;
	}

	public void compare(IBaseParameters theDiff, BaseRuntimeElementDefinition<?> theDef, String theSourcePath, String theTargetPath, IBase theOldField, IBase theNewField) {

		BaseRuntimeElementDefinition<?> sourceDef = myContext.getElementDefinition(theOldField.getClass());
		BaseRuntimeElementDefinition<?> targetDef = myContext.getElementDefinition(theNewField.getClass());
		if (!sourceDef.getName().equals(targetDef.getName())) {
			IBase operation = ParametersUtil.addParameterToParameters(myContext, theDiff, "operation");
			ParametersUtil.addPartCode(myContext, operation, "type", "replace");
			ParametersUtil.addPartString(myContext, operation, "path", theTargetPath);
			ParametersUtil.addPart(myContext, operation, "value", theNewField);
		} else {
			if (theOldField instanceof IPrimitiveType) {
				IPrimitiveType<?> oldPrimitive = (IPrimitiveType<?>) theOldField;
				IPrimitiveType<?> newPrimitive = (IPrimitiveType<?>) theNewField;
				if (!Objects.equals(oldPrimitive.getValueAsString(), newPrimitive.getValueAsString())) {
					IBase operation = ParametersUtil.addParameterToParameters(myContext, theDiff, "operation");
					ParametersUtil.addPartCode(myContext, operation, "type", "replace");
					ParametersUtil.addPartString(myContext, operation, "path", theTargetPath);
					ParametersUtil.addPart(myContext, operation, "value", newPrimitive);
				}
			}

			List<BaseRuntimeChildDefinition> children = theDef.getChildren();
			for (BaseRuntimeChildDefinition nextChild : children) {
				compareField(theDiff, theSourcePath, theTargetPath, theOldField, theNewField, nextChild);
			}

		}

	}

	public void compareField(IBaseParameters theDiff, String theSourcePath, String theTargetPath, IBase theOldField, IBase theNewField, BaseRuntimeChildDefinition theChildDef) {
		String elementName = theChildDef.getElementName();
		boolean repeatable = theChildDef.getMax() != 1;

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

			compare(theDiff, def, sourcePath, targetPath, sourceChildField, targetChildField);

			sourceIndex++;
			targetIndex++;
		}

		// Find newly inserted items
		while (targetIndex < targetValues.size()) {
			IBase operation = ParametersUtil.addParameterToParameters(myContext, theDiff, "operation");
			ParametersUtil.addPartCode(myContext, operation, "type", "insert");
			ParametersUtil.addPartString(myContext, operation, "path", theTargetPath + "." + elementName);
			ParametersUtil.addPartInteger(myContext, operation, "index", targetIndex);
			ParametersUtil.addPart(myContext, operation, "value", targetValues.get(targetIndex));

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
	}

}
