package ca.uhn.fhir.model.view;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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

import java.util.List;

import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseExtension;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeChildDeclaredExtensionDefinition;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.BaseElement;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.api.IResource;

public class ViewGenerator {

	private FhirContext myCtx;

	public ViewGenerator(FhirContext theFhirContext) {
		myCtx=theFhirContext;
	}

	public <T extends IResource> T newView(IResource theResource, Class<T> theTargetType) {
		Class<? extends IResource> sourceType = theResource.getClass();
		RuntimeResourceDefinition sourceDef = myCtx.getResourceDefinition(theResource);
		RuntimeResourceDefinition targetDef = myCtx.getResourceDefinition(theTargetType);

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
			List<RuntimeChildDeclaredExtensionDefinition> targetExts = theTargetDef.getExtensions();

			for (BaseRuntimeChildDefinition nextChild : targetChildren) {

				String elementName = nextChild.getElementName();
				if (nextChild.getValidChildNames().size() > 1) {
					elementName = nextChild.getValidChildNames().iterator().next();
				}
				
				BaseRuntimeChildDefinition sourceChildEquivalent = theSourceDef.getChildByNameOrThrowDataFormatException(elementName);
				if (sourceChildEquivalent == null) {
					continue;
				}

				List<? extends IBase> sourceValues = sourceChildEquivalent.getAccessor().getValues(theSource);
				for (IBase nextElement : sourceValues) {
					boolean handled = false;
					if (nextElement instanceof IBaseExtension) {
						String url = ((IBaseExtension<?,?>) nextElement).getUrl();
						for (RuntimeChildDeclaredExtensionDefinition nextExt : targetExts) {
							String nextTargetUrl = nextExt.getExtensionUrl();
							if (!nextTargetUrl.equals(url)) {
								continue;
							}
							addExtension(theSourceDef, theSource, theTarget, nextExt, url);
							handled = true;
						}						
					} 
					if (!handled) {
						nextChild.getMutator().addValue(theTarget, nextElement);
					}
				}
			}
			
			for (RuntimeChildDeclaredExtensionDefinition nextExt : targetExts) {
				String url = nextExt.getExtensionUrl();
				addExtension(theSourceDef, theSource, theTarget, nextExt, url);
			}
			
			
		}
	}

	private void addExtension(BaseRuntimeElementCompositeDefinition<?> theSourceDef, BaseElement theSource, BaseElement theTarget, RuntimeChildDeclaredExtensionDefinition nextExt, String url) {
		RuntimeChildDeclaredExtensionDefinition sourceDeclaredExt = theSourceDef.getDeclaredExtension(url, "");
		if (sourceDeclaredExt == null) {
			
			for (ExtensionDt next : theSource.getAllUndeclaredExtensions()) {
				if (next.getUrlAsString().equals(url)) {
					nextExt.getMutator().addValue(theTarget, next.getValue());
				}
			}
			
		} else {
			
			List<? extends IBase> values = sourceDeclaredExt.getAccessor().getValues(theSource);
			for (IBase nextElement : values) {
				nextExt.getMutator().addValue(theTarget, nextElement);
			}
			
		}
	}
}
