package ca.uhn.fhir.jpa.dao;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
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
import java.util.List;

import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.util.FhirTerser;

public class BaseSearchParamExtractor {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseSearchParamExtractor.class);
	private FhirContext myContext;
	
	public BaseSearchParamExtractor(FhirContext theContext) {
		myContext = theContext;
	}

	protected FhirContext getContext() {
		return myContext;
	}

	protected List<Object> extractValues(String thePaths, IBaseResource theResource) {
		List<Object> values = new ArrayList<Object>();
		String[] nextPathsSplit = thePaths.split("\\|");
		FhirTerser t = myContext.newTerser();
		for (String nextPath : nextPathsSplit) {
			String nextPathTrimmed = nextPath.trim();
			try {
				values.addAll(t.getValues(theResource, nextPathTrimmed));
			} catch (Exception e) {
				RuntimeResourceDefinition def = myContext.getResourceDefinition(theResource);
				ourLog.warn("Failed to index values from path[{}] in resource type[{}]: {}", new Object[] { nextPathTrimmed, def.getName(), e.toString(), e } );
			}
		}
		return values;
	}

	
}
