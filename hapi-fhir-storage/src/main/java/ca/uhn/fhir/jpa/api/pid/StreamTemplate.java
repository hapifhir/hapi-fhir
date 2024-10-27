/*-
 * #%L
 * HAPI FHIR Storage api
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
package ca.uhn.fhir.jpa.api.pid;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.transaction.support.TransactionOperations;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * A template for stream queries, like JDBCTemplate and friends.
 *
 * We need to wrap access to the stream with a tx-span, a try-with-resources block, and RequestDetails.
 * @param <T> The stream content type
 */
public interface StreamTemplate<T> {
	@Nullable
	<R> R call(@Nonnull Function<Stream<T>, R> theCallback);

	/**
	 * Wrap this template with a transaction boundary.
	 * Our dao Stream methods require an active Hibernate session for the duration of the Stream.
	 * This advice uses a tx boundary to ensure that active session.
	 *
	 * @param theTxBuilder the transaction and partition settings
	 * @return the wrapped template
	 */
	default StreamTemplate<T> withTransactionAdvice(TransactionOperations theTxBuilder) {
		return new TransactionWrappingStreamTemplate<>(theTxBuilder, this);
	}

	/**
	 * Wrap the supplied stream as a StreamTemplate in a try-with-resources block to ensure it is closed.
	 * @param theStreamQuery the query to run
	 * @return a template that will always close the Stream on exit.
	 */
	static <ST> StreamTemplate<ST> fromSupplier(Supplier<Stream<ST>> theStreamQuery) {
		return new AutoClosingStreamTemplate<>(theStreamQuery);
	}
}
