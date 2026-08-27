/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.api.dao;

import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.model.cross.IBasePersistedResource;
import ca.uhn.fhir.jpa.update.UpdateParameters;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.Date;

public interface IJpaDao<T extends IBaseResource> {
	@SuppressWarnings("unchecked")
	IBasePersistedResource updateEntity(
			RequestDetails theRequest,
			IBaseResource theResource,
			IBasePersistedResource theEntity,
			Date theDeletedTimestampOrNull,
			boolean thePerformIndexing,
			boolean theUpdateVersion,
			TransactionDetails theTransactionDetails,
			boolean theForceUpdate,
			boolean theCreateNewHistoryEntry);

	/**
	 * @deprecated Call {@link #updateInternal(UpdateParameters)} instead. Scheduled for removal in 8.16.0.
	 */
	@Deprecated(since = "8.14.0", forRemoval = true)
	DaoMethodOutcome updateInternal(
			RequestDetails theRequestDetails,
			T theResource,
			String theMatchUrl,
			boolean thePerformIndexing,
			boolean theForceUpdateVersion,
			IBasePersistedResource theEntity,
			IIdType theResourceId,
			IBaseResource theOldResource,
			RestOperationTypeEnum theOperationType,
			TransactionDetails theTransactionDetails);

	/**
	 * Performs the write of an update or a patch against storage, broadcasting the storage interceptor
	 * hooks that surround it.
	 * <p>
	 * This default implementation unpacks {@literal theParameters} onto the deprecated ten-argument
	 * variant above, which has no slot for {@link UpdateParameters#getExpectedVersion()}. The expected
	 * version is therefore discarded and no {@code If-Match} precondition is re-validated at the point
	 * of the write, which matches the behaviour of every implementor that has not overridden this
	 * method. Implementors wanting that re-validation - a FHIR transaction needs it, because there the
	 * precondition was first checked during a pass that stored nothing - must override this method
	 * rather than the ten-argument one.
	 * </p>
	 *
	 * @param theParameters the resource being written together with the entity it replaces and the
	 *                         metadata describing the write
	 * @return the outcome of the write
	 */
	default DaoMethodOutcome updateInternal(UpdateParameters<T> theParameters) {
		return updateInternal(
				theParameters.getRequest(),
				theParameters.getResource(),
				theParameters.getMatchUrl(),
				theParameters.shouldPerformIndexing(),
				theParameters.shouldForceUpdateVersion(),
				theParameters.getEntity(),
				theParameters.getResourceIdToUpdate(),
				theParameters.getOldResource(),
				theParameters.getOperationType(),
				theParameters.getTransactionDetails());
	}
}
