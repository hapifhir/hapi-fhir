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

	/**
	 * Fluent builder for creating a transactional callback
	 * <p>
	 * Method chain must end with a call to {@link IExecutionBuilder#task(Runnable)} or one of the other
	 * overloads of <code>task(...)</code>
	 * </p>
	 */
	IExecutionBuilder execute(@Nullable RequestDetails theRequestDetails);

	/**
	 * @deprecated It is highly recommended to use {@link #execute(RequestDetails)} instead of this method, for increased visibility.
	 */
	@Deprecated
	<T> T execute(@Nullable RequestDetails theRequestDetails, @Nullable TransactionDetails theTransactionDetails, @Nonnull Propagation thePropagation, @Nonnull Isolation theIsolation, @Nonnull ICallable<T> theCallback);

	interface IExecutionBuilder {

		IExecutionBuilder withIsolation(Isolation theIsolation);

		IExecutionBuilder withTransactionDetails(TransactionDetails theTransactionDetails);

		IExecutionBuilder withPropagation(Propagation thePropagation);

		IExecutionBuilder withRequestPartitionId(RequestPartitionId theRequestPartitionId);

		IExecutionBuilder readOnly();

		IExecutionBuilder onRollback(Runnable theOnRollback);

		void task(Runnable theTask);

		<T> T task(ICallable<T> theTask);

		<T> T task(TransactionCallback<T> callback);
	}
}
