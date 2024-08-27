/*-
 * #%L
 * HAPI FHIR - CDS Hooks
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
package ca.uhn.hapi.fhir.cdshooks.svc;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.cr.common.IRepositoryFactory;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.hapi.fhir.cdshooks.api.ICdsConfigService;
import ca.uhn.hapi.fhir.cdshooks.svc.cr.CdsCrSettings;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class CdsConfigServiceImpl implements ICdsConfigService {
	private final FhirContext myFhirContext;
	private final ObjectMapper myObjectMapper;
	private final CdsCrSettings myCdsCrSettings;
	private final DaoRegistry myDaoRegistry;
	private final IRepositoryFactory myRepositoryFactory;
	private final RestfulServer myRestfulServer;

	public CdsConfigServiceImpl(
			@Nonnull FhirContext theFhirContext,
			@Nonnull ObjectMapper theObjectMapper,
			@Nonnull CdsCrSettings theCdsCrSettings,
			@Nullable DaoRegistry theDaoRegistry,
			@Nullable IRepositoryFactory theRepositoryFactory,
			@Nullable RestfulServer theRestfulServer) {
		myFhirContext = theFhirContext;
		myObjectMapper = theObjectMapper;
		myCdsCrSettings = theCdsCrSettings;
		myDaoRegistry = theDaoRegistry;
		myRepositoryFactory = theRepositoryFactory;
		myRestfulServer = theRestfulServer;
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

	@Nonnull
	@Override
	public CdsCrSettings getCdsCrSettings() {
		return myCdsCrSettings;
	}

	@Nullable
	@Override
	public DaoRegistry getDaoRegistry() {
		return myDaoRegistry;
	}

	@Nullable
	@Override
	public IRepositoryFactory getRepositoryFactory() {
		return myRepositoryFactory;
	}

	@Nullable
	@Override
	public RestfulServer getRestfulServer() {
		return myRestfulServer;
	}
}
