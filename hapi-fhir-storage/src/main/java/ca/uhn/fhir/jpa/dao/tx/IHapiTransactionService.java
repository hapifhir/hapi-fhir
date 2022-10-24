package ca.uhn.fhir.jpa.dao.tx;

/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.util.ICallable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.support.TransactionCallback;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IHapiTransactionService {
	<T> T execute(@Nullable RequestDetails theRequestDetails, @Nullable TransactionDetails theTransactionDetails, @Nonnull Propagation thePropagation, @Nonnull Isolation theIsolation, @Nonnull ICallable<T> theCallback);

	IExecutionBuilder execute(@Nullable RequestDetails theRequestDetails);

	interface IExecutionBuilder {
		HapiTransactionService.ExecutionBuilder withIsolation(Isolation theIsolation);

		HapiTransactionService.ExecutionBuilder withTransactionDetails(TransactionDetails theTransactionDetails);

		HapiTransactionService.ExecutionBuilder withPropagation(Propagation thePropagation);

		HapiTransactionService.ExecutionBuilder withRequestPartitionId(RequestPartitionId theRequestPartitionId);

		HapiTransactionService.ExecutionBuilder readOnly();

		HapiTransactionService.ExecutionBuilder onRollback(Runnable theOnRollback);

		void task(Runnable theTask);

		<T> T task(ICallable<T> theTask);

		<T> T task(TransactionCallback<T> callback);
	}
}
