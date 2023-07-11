/*-
 * #%L
 * HAPI FHIR - CDS Hooks
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
package ca.uhn.hapi.fhir.cdshooks.svc;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.hapi.fhir.cdshooks.api.ICdsConfigService;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.annotation.Nonnull;

public class CdsConfigServiceImpl implements ICdsConfigService {
	private final FhirContext myFhirContext;
	private final ObjectMapper myObjectMapper;

	public CdsConfigServiceImpl(@Nonnull FhirContext theFhirContext, @Nonnull ObjectMapper theObjectMapper) {
		myFhirContext = theFhirContext;
		myObjectMapper = theObjectMapper;
	}

	@Nonnull
	@Override
	public FhirContext getFhirContext() {
		return myFhirContext;
	}

	@Nonnull
	@Override
	public ObjectMapper getObjectMapper() {
		return myObjectMapper;
	}

}
