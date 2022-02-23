package ca.uhn.fhir.util;

/*-
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
import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;

public class ResourceUtil {

	/**
	 * This method removes the narrative from the resource, or if the resource is a bundle, removes the narrative from
	 * all of the resources in the bundle
	 *
	 * @param theContext The fhir context
	 * @param theInput   The resource to remove the narrative from
	 */
	public static void removeNarrative(FhirContext theContext, IBaseResource theInput) {
		if (theInput instanceof IBaseBundle) {
			for (IBaseResource next : BundleUtil.toListOfResources(theContext, (IBaseBundle) theInput)) {
				removeNarrative(theContext, next);
			}
		}

		BaseRuntimeElementCompositeDefinition<?> element = theContext.getResourceDefinition(theInput.getClass());
		BaseRuntimeChildDefinition textElement = element.getChildByName("text");
		if (textElement != null) {
			textElement.getMutator().setValue(theInput, null);
		}
	}
}
