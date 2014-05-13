package ca.uhn.fhir.util;

/*
 * #%L
 * HAPI FHIR Library
 * %%
 * Copyright (C) 2014 University Health Network
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IResource;

public class FhirTerser {

	public FhirTerser(FhirContext theContext) {
		super();
		myContext = theContext;
	}

	private FhirContext myContext;

	public List<Object> getValues(IResource theResource, String thePath) {
		RuntimeResourceDefinition def = myContext.getResourceDefinition(theResource);

		BaseRuntimeElementCompositeDefinition<?> currentDef = def;
		Object currentObj = theResource;

		List<String> parts = Arrays.asList(thePath.split("\\."));
		List<String> subList = parts.subList(1, parts.size() );
		if (subList.size()< 1) {
			throw new ConfigurationException("Invalid path: " + thePath);
		}
		return getValues(currentDef, currentObj, subList);

	}

	private List<Object> getValues(BaseRuntimeElementCompositeDefinition<?> theCurrentDef, Object theCurrentObj, List<String> theSubList) {
		BaseRuntimeChildDefinition nextDef = theCurrentDef.getChildByNameOrThrowDataFormatException(theSubList.get(0));
		List<? extends IElement> values = nextDef.getAccessor().getValues(theCurrentObj);
		List<Object> retVal = new ArrayList<Object>();

		if (theSubList.size() == 1) {
			retVal.addAll(values);
		} else {
			for (IElement nextElement : values) {
				BaseRuntimeElementCompositeDefinition<?> nextChildDef = (BaseRuntimeElementCompositeDefinition<?>) myContext.getElementDefinition(nextElement.getClass());
				List<?> foundValues = getValues(nextChildDef, nextElement, theSubList.subList(1, theSubList.size() ));
				retVal.addAll(foundValues);
			}
		}
		return retVal;
	}

}
