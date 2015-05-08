package ca.uhn.fhir.narrative;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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

import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.base.composite.BaseNarrativeDt;
import ca.uhn.fhir.parser.DataFormatException;

public interface INarrativeGenerator {

	void generateNarrative(String theProfile, IBaseResource theResource, BaseNarrativeDt<?> theNarrative) throws DataFormatException;

	void generateNarrative(IBaseResource theResource, BaseNarrativeDt<?> theNarrative);

	String generateTitle(IBaseResource theResource);

	String generateTitle(String theProfile, IBaseResource theResource);

	/**
	 * This method is called automatically by the framework, you do not need to interact with this method.
	 */
	void setFhirContext(FhirContext theFhirContext);
	
}
