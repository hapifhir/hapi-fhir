/*-
 * #%L
 * HAPI FHIR - CDS Hooks
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRestfulResponse;
import ca.uhn.fhir.rest.server.RestfulServer;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IIdType;

import static com.google.common.base.Preconditions.checkNotNull;

public interface ICdsConfigService {
	@Nonnull
	FhirContext getFhirContext();

	@Nonnull
	ObjectMapper getObjectMapper();

	@Nullable
	default DaoRegistry getDaoRegistry() {
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
		rd.setId(newId(theFhirContext.getVersion().getVersion(), theResourceType, theId));
		return rd;
	}

	private static <IdType extends IIdType> IdType newId(
			FhirVersionEnum fhirVersionEnum, String resourceType, String idPart) {
		checkNotNull(fhirVersionEnum);
		checkNotNull(resourceType);
		checkNotNull(idPart);

		return newId(fhirVersionEnum, resourceType + "/" + idPart);
	}

	private static <IdType extends IIdType> IdType newId(FhirVersionEnum fhirVersionEnum, String id) {
		checkNotNull(fhirVersionEnum);
		checkNotNull(id);

		switch (fhirVersionEnum) {
			case DSTU2:
				return (IdType) new ca.uhn.fhir.model.primitive.IdDt(id);
			case DSTU2_1:
				return (IdType) new org.hl7.fhir.dstu2016may.model.IdType(id);
			case DSTU2_HL7ORG:
				return (IdType) new org.hl7.fhir.dstu2.model.IdType(id);
			case DSTU3:
				return (IdType) new org.hl7.fhir.dstu3.model.IdType(id);
			case R4:
				return (IdType) new org.hl7.fhir.r4.model.IdType(id);
			case R4B:
				return (IdType) new org.hl7.fhir.r4b.model.IdType(id);
			case R5:
				return (IdType) new org.hl7.fhir.r5.model.IdType(id);
			default:
				throw new IllegalArgumentException(String.format(
						"newId does not support FHIR version %s", fhirVersionEnum.getFhirVersionString()));
		}
	}
}
