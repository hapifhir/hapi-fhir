package ca.uhn.fhir.util;

/*-
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.util.ArrayList;
import java.util.List;

public class SearchParameterUtil {

	public static List<String> getBaseAsStrings(FhirContext theContext, IBaseResource theResource) {
		Validate.notNull(theContext, "theContext must not be null");
		Validate.notNull(theResource, "theResource must not be null");
		RuntimeResourceDefinition def = theContext.getResourceDefinition(theResource);

		BaseRuntimeChildDefinition base = def.getChildByName("base");
		List<IBase> baseValues = base.getAccessor().getValues(theResource);
		List<String> retVal = new ArrayList<>();
		for (IBase next : baseValues) {
			IPrimitiveType<?> nextPrimitive = (IPrimitiveType<?>) next;
			retVal.add(nextPrimitive.getValueAsString());
		}

		return retVal;
	}

}
