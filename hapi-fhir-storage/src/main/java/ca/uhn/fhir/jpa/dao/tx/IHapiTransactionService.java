/*-
 * #%L
 * HAPI FHIR Storage api
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
package ca.uhn.fhir.jpa.dao.tx;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.util.ICallable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.support.TransactionCallback;

import java.util.concurrent.Callable;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * This class is used to execute code within the context of a database transaction,
 * just like Spring's {@link org.springframework.transaction.support.TransactionTemplate}
 * but with more functionality. It can auto-execute code upon rollback, it translates
 * specific exceptions, and it stores transaction context in a ThreadLocal.
 */
public interface IHapiTransactionService {

	/**
	 * Fluent builder for creating a transactional callback
	 * <p>
	 * Method chain must end with a call to {@link IExecutionBuilder#execute(Runnable)} or one of the other
	 * overloads of <code>task(...)</code>
	 * </p>
	 */
	IExecutionBuilder withRequest(@Nullable RequestDetails theRequestDetails);

	/**
	 * Fluent builder for internal system requests with no external
	 * requestdetails associated
	 */
	IExecutionBuilder withSystemRequest();

	/**
	 * Fluent builder for internal system requests with no external
	 * {@link RequestDetails} associated and a pre-specified partition ID.
	 * This method is sugar for
	 * <pre>
	 *    withSystemRequest()
	 * 			.withRequestPartitionId(thePartitionId);
	 * </pre>
	 *
	 * @since 6.6.0
	 */
	default IExecutionBuilder withSystemRequestOnPartition(RequestPartitionId theRequestPartitionId) {
		return withSystemRequest().withRequestPartitionId(theRequestPartitionId);
	}

	/**
	 * @deprecated It is highly recommended to use {@link #withRequest(RequestDetails)} instead of this method, for increased visibility.
	 */
	@Deprecated
	<T> T withRequest(
			@Nullable RequestDetails theRequestDetails,
			@Nullable TransactionDetails theTransactionDetails,
			@Nonnull Propagation thePropagation,
			@Nonnull Isolation theIsolation,
			@Nonnull ICallable<T> theCallback);

	interface IExecutionBuilder {

		IExecutionBuilder withIsolation(Isolation theIsolation);

		IExecutionBuilder withTransactionDetails(TransactionDetails theTransactionDetails);

		IExecutionBuilder withPropagation(Propagation thePropagation);

		IExecutionBuilder withRequestPartitionId(RequestPartitionId theRequestPartitionId);

		IExecutionBuilder readOnly();

		IExecutionBuilder onRollback(Runnable theOnRollback);

		void execute(Runnable theTask);

		<T> T execute(Callable<T> theTask);

		<T> T execute(TransactionCallback<T> callback);
	}
}
