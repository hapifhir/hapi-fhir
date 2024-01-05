/*-
 * #%L
 * HAPI FHIR - Clinical Reasoning
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.cr.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;

import java.util.List;
import java.util.Map;

public class ProviderSelector {
	private final FhirContext myFhirContext;
	private final Map<FhirVersionEnum, List<Class<?>>> myProviderMap;

	public ProviderSelector(FhirContext theFhirContext, Map<FhirVersionEnum, List<Class<?>>> theProviderMap) {
		myFhirContext = theFhirContext;
		myProviderMap = theProviderMap;
	}

	public List<Class<?>> getProviderType() {
		return myProviderMap.get(myFhirContext.getVersion().getVersion());
	}
}
