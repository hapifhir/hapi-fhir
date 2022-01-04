package ca.uhn.fhir.model.view;

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

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeChildDeclaredExtensionDefinition;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.i18n.Msg;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseHasExtensions;
import org.hl7.fhir.instance.model.api.IBaseHasModifierExtensions;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.List;

public class ViewGenerator {

	private FhirContext myCtx;

	public ViewGenerator(FhirContext theFhirContext) {
		myCtx = theFhirContext;
	}

	public <T extends IBaseResource> T newView(IBaseResource theResource, Class<T> theTargetType) {
		Class<? extends IBaseResource> sourceType = theResource.getClass();
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
		} catch (Exception e) {
			throw new ConfigurationException(Msg.code(1886) + "Failed to instantiate " + theTargetType, e);
		}

		copyChildren(sourceDef, (IBase) theResource, targetDef, (IBase) retVal);

		return retVal;
	}

	private void copyChildren(BaseRuntimeElementCompositeDefinition<?> theSourceDef, IBase theSource, BaseRuntimeElementCompositeDefinition<?> theTargetDef, IBase theTarget) {
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
						String url = ((IBaseExtension<?, ?>) nextElement).getUrl();
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

	private void addExtension(BaseRuntimeElementCompositeDefinition<?> theSourceDef, IBase theSource, IBase theTarget, RuntimeChildDeclaredExtensionDefinition nextExt, String url) {
		RuntimeChildDeclaredExtensionDefinition sourceDeclaredExt = theSourceDef.getDeclaredExtension(url, "");
		if (sourceDeclaredExt == null) {

			if (theSource instanceof IBaseHasExtensions) {
				for (IBaseExtension<?, ?> next : ((IBaseHasExtensions) theSource).getExtension()) {
					if (next.getUrl().equals(url)) {
						nextExt.getMutator().addValue(theTarget, next.getValue());
					}
				}
			}
			if (theSource instanceof IBaseHasModifierExtensions) {
				for (IBaseExtension<?, ?> next : ((IBaseHasModifierExtensions) theSource).getModifierExtension()) {
					if (next.getUrl().equals(url)) {
						nextExt.getMutator().addValue(theTarget, next.getValue());
					}
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
