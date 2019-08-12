package ca.uhn.fhir.narrative;

/*
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

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.instance.model.api.IBaseResource;

public interface INarrativeGenerator {

	/**
	 * Generate any narratives for the given resource that have applicable
	 * templates, and populates the appropriate field(s). This almost always means
	 * the <code>Resource.text.narrative</code> field, but for some resource types
	 * it can mean other fields (e.g. <code>Composition.</code>
	 *
	 * @return Returns <code>true</code> if a narrative was actually generated
	 */
	boolean populateResourceNarrative(FhirContext theFhirContext, IBaseResource theResource);

}
