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
import java.util.stream.Stream;

/**
 * Wrap a StreamTemplate with transaction advice.
 * We can't cary open ResultSets past a transaction boundary.
 * This wraps a Stream producer with tx advice so the connection is still open.
 */
class TransactionWrappingStreamTemplate<T> implements StreamTemplate<T> {
	@Nonnull
	final TransactionOperations myTransaction;

	@Nonnull
	final StreamTemplate<T> myWrappedStreamTemplate;

	TransactionWrappingStreamTemplate(
			@Nonnull TransactionOperations theTransaction, @Nonnull StreamTemplate<T> theWrappedStreamTemplate) {
		myTransaction = theTransaction;
		this.myWrappedStreamTemplate = theWrappedStreamTemplate;
	}

	@Nullable
	@Override
	public <R> R call(@Nonnull Function<Stream<T>, R> theCallback) {
		return myTransaction.execute(unusedTxStatus -> myWrappedStreamTemplate.call(theCallback));
	}
}
