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
package ca.uhn.fhir.jpa.dao.expunge;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.Validate;

import java.util.function.Supplier;

/**
 * Utility class wrapping a supplier in a transaction with the purpose of performing the supply operation with a
 * partitioned aware context.
 */
public class PartitionAwareSupplier {
	private final HapiTransactionService myTransactionService;
	private final RequestDetails myRequestDetails;
	private final RequestPartitionId myRequestPartitionId;
	private final IRequestPartitionHelperSvc myRequestPartitionHelperSvc;

	/**
	 * @param theTxService The transaction service for transaction management
	 * @param theRequestPartitionHelperSvc The partition helper service used to determine partition from request details
	 *                                     when theRequestPartitionId is null.
	 * @param theRequestDetails The request details to use for transaction context
	 * @param theRequestPartitionId The partition ID to use for all operations. When provided, this
	 *                              overrides any partition that would be determined from theRequestDetails.
	 *                              May be null to use the partition from theRequestDetails.
	 */
	public PartitionAwareSupplier(
			HapiTransactionService theTxService,
			IRequestPartitionHelperSvc theRequestPartitionHelperSvc,
			RequestDetails theRequestDetails,
			@Nullable RequestPartitionId theRequestPartitionId) {
		myTransactionService = theTxService;
		myRequestPartitionHelperSvc = theRequestPartitionHelperSvc;
		myRequestDetails = theRequestDetails;
		myRequestPartitionId = theRequestPartitionId;
	}

	@Nonnull
	public <T> T supplyInPartitionedContext(Supplier<T> theResourcePersistentIdSupplier) {
		IHapiTransactionService.IExecutionBuilder executionBuilder = myTransactionService.withRequest(myRequestDetails);
		// use explicit partition ID if provided
		if (myRequestPartitionId != null) {
			executionBuilder.withRequestPartitionId(myRequestPartitionId);
		} else if (myRequestPartitionHelperSvc != null && myRequestDetails != null) {
			executionBuilder.withRequestPartitionId(
					myRequestPartitionHelperSvc.determineGenericPartitionForRequest(myRequestDetails));
		}
		T retVal = executionBuilder.execute(tx -> theResourcePersistentIdSupplier.get());

		Validate.notNull(retVal, "No resource persistent id supplied by supplier %s", theResourcePersistentIdSupplier);
		return retVal;
	}
}
