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
package ca.uhn.hapi.fhir.cdshooks.api;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.cr.common.IRepositoryFactory;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRestfulResponse;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.hapi.fhir.cdshooks.svc.cr.CdsCrSettings;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.opencds.cqf.fhir.utility.Ids;

public interface ICdsConfigService {
	@Nonnull
	FhirContext getFhirContext();

	@Nonnull
	ObjectMapper getObjectMapper();

	@Nonnull
	CdsCrSettings getCdsCrSettings();

	@Nullable
	default DaoRegistry getDaoRegistry() {
		return null;
	}

	@Nullable
	default IRepositoryFactory getRepositoryFactory() {
		return null;
	}

	@Nullable
	default RestfulServer getRestfulServer() {
		return null;
	}

	default RequestDetails createRequestDetails(FhirContext theFhirContext, String theId, String theResourceType) {
		SystemRequestDetails rd = new SystemRequestDetails();
		rd.setServer(getRestfulServer());
		rd.setResponse(new SystemRestfulResponse(rd));
		rd.setId(Ids.newId(theFhirContext.getVersion().getVersion(), theResourceType, theId));
		return rd;
	}
}
