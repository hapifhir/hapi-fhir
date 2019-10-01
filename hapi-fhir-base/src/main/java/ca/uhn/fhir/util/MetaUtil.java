package ca.uhn.fhir.util;

/*-
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseMetaType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.util.List;

public class MetaUtil {

	private MetaUtil() {
		// non-instantiable
	}

	public static String getSource(FhirContext theContext, IBaseMetaType theMeta) {
		BaseRuntimeElementCompositeDefinition<?> elementDef = (BaseRuntimeElementCompositeDefinition<?>) theContext.getElementDefinition(theMeta.getClass());
		BaseRuntimeChildDefinition sourceChild = elementDef.getChildByName("source");
		List<IBase> sourceValues = sourceChild.getAccessor().getValues(theMeta);
		String retVal = null;
		if (sourceValues.size() > 0) {
			retVal = ((IPrimitiveType<?>) sourceValues.get(0)).getValueAsString();
		}
		return retVal;
	}

	public static void setSource(FhirContext theContext, IBaseMetaType theMeta, String theValue) {
		BaseRuntimeElementCompositeDefinition<?> elementDef = (BaseRuntimeElementCompositeDefinition<?>) theContext.getElementDefinition(theMeta.getClass());
		BaseRuntimeChildDefinition sourceChild = elementDef.getChildByName("source");
		List<IBase> sourceValues = sourceChild.getAccessor().getValues(theMeta);
		IPrimitiveType<?> sourceElement;
		if (sourceValues.size() > 0) {
			sourceElement = ((IPrimitiveType<?>) sourceValues.get(0));
		} else {
			sourceElement = (IPrimitiveType<?>) theContext.getElementDefinition("uri").newInstance();
			sourceChild.getMutator().setValue(theMeta, sourceElement);
		}
		sourceElement.setValueAsString(theValue);
	}

}
