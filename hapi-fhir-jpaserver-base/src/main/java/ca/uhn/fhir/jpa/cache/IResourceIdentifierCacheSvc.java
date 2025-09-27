/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import jakarta.annotation.Nonnull;

import java.util.function.Supplier;

public interface IResourceIdentifierCacheSvc {

	/**
	 * Retrieves (and creates if necessary) the PID associated with the
	 * given identifier system URL.
	 *
	 * @param theSystem The <code>Identifier.system</code> value
	 */
	long getOrCreateResourceIdentifierSystem(
			RequestDetails theRequestDetails, RequestPartitionId theRequestPartitionId, String theSystem);

	/**
	 * Retrieves the FHIR ID assimilated with the given Patient identifier, creating a new
	 * record using the given Supplier if no existing record is found. This method enforces
	 * uniqueness on the identifier using a database constraint, and will therefore only allow
	 * one FHIR ID to be associated with one Identifier. No uniqueness is enforced on the FHIR
	 * ID.
	 *
	 * @param theSystem        The <code>Identifier.system</code> value
	 * @param theValue         The <code>Identifier.value</code> value
	 * @param theNewIdSupplier If no existing FHIR ID is found, a new entry will be created using this ID supplier
	 * @return The FHIR ID associated with this identifier
	 */
	@Nonnull
	String getFhirIdAssociatedWithUniquePatientIdentifier(
			RequestDetails theRequestDetails,
			RequestPartitionId theRequestPartitionId,
			String theSystem,
			String theValue,
			Supplier<String> theNewIdSupplier);
}
