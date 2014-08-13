package ca.uhn.fhir.model.view;

import java.util.List;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeChildDeclaredExtensionDefinition;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.BaseElement;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IResource;

public class ViewGenerator {

	public <T extends IResource> T newView(FhirContext theContext, IResource theResource, Class<T> theTargetType) {
		Class<? extends IResource> sourceType = theResource.getClass();
		RuntimeResourceDefinition sourceDef = theContext.getResourceDefinition(theResource);
		RuntimeResourceDefinition targetDef = theContext.getResourceDefinition(theTargetType);

		if (sourceType.equals(theTargetType)) {
			@SuppressWarnings("unchecked")
			T resource = (T) theResource;
			return resource;
		}

		T retVal;
		try {
			retVal = theTargetType.newInstance();
		} catch (InstantiationException e) {
			throw new ConfigurationException("Failed to instantiate " + theTargetType, e);
		} catch (IllegalAccessException e) {
			throw new ConfigurationException("Failed to instantiate " + theTargetType, e);
		}

		copyChildren(sourceDef, (BaseElement) theResource, targetDef, (BaseElement) retVal);

		return retVal;
	}

	private void copyChildren(BaseRuntimeElementCompositeDefinition<?> theSourceDef, BaseElement theSource, BaseRuntimeElementCompositeDefinition<?> theTargetDef, BaseElement theTarget) {
		if (!theSource.isEmpty()) {
			List<BaseRuntimeChildDefinition> targetChildren = theTargetDef.getChildren();
			for (BaseRuntimeChildDefinition nextChild : targetChildren) {

				BaseRuntimeChildDefinition sourceChildEquivalent = theSourceDef.getChildByNameOrThrowDataFormatException(nextChild.getElementName());
				if (sourceChildEquivalent == null) {
					continue;
				}

				List<? extends IElement> sourceValues = sourceChildEquivalent.getAccessor().getValues(theTarget);
				for (IElement nextElement : sourceValues) {
					nextChild.getMutator().addValue(theTargetDef, nextElement);
				}
			}
			
			List<RuntimeChildDeclaredExtensionDefinition> targetExts = theTargetDef.getExtensions();
			for (RuntimeChildDeclaredExtensionDefinition nextExt : targetExts) {
				String url = nextExt.getExtensionUrl();
				
				RuntimeChildDeclaredExtensionDefinition sourceDeclaredExt = theSourceDef.getDeclaredExtension(url);
				if (sourceDeclaredExt == null) {
					
					for (ExtensionDt next : theSource.getAllUndeclaredExtensions()) {
						if (next.getUrlAsString().equals(url)) {
							nextExt.getMutator().addValue(theTarget, next.getValue());
						}
					}
					
				} else {
					
					List<? extends IElement> values = sourceDeclaredExt.getAccessor().getValues(theSource);
					for (IElement nextElement : values) {
						nextExt.getMutator().addValue(theTarget, nextElement);
					}
					
				}
				
			}
			
			
		}
	}
}
