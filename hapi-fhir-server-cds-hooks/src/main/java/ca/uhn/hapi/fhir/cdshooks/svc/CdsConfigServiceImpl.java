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
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.hapi.fhir.cdshooks.api.ICdsConfigService;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CdsConfigServiceImpl implements ICdsConfigService {
	private final FhirContext myFhirContext;
	private final ObjectMapper myObjectMapper;
	private final DaoRegistry myDaoRegistry;

	public CdsConfigServiceImpl(
			@Nonnull FhirContext theFhirContext,
			@Nonnull ObjectMapper theObjectMapper,
			@Nullable DaoRegistry theDaoRegistry) {
		myFhirContext = theFhirContext;
		myObjectMapper = theObjectMapper;
		myDaoRegistry = theDaoRegistry;
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

	@Nullable
	@Override
	public DaoRegistry getDaoRegistry() {
		return myDaoRegistry;
	}
}
