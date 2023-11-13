package ca.uhn.fhir.jpa.api.pid;

import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService.IExecutionBuilder;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import javax.annotation.Nonnull;

/**
 * A template for stream queries, like JDBCTemplate and friends.
 *
 * We need to wrap access to the stream with a tx-span, a try-with-resources block, and RequestDetails.
 * @param <T> The stream content type
 */
public interface StreamTemplate<T> {
	@Nonnull
	<R> R call(Function<Stream<T>, R> theCallback);

	/**
	 * Wrap this template with a transaction boundary.
	 * Our dao Stream methods require an active Hibernate session for the duration of the Stream.
	 * This advice uses a tx boundary to ensure that active session.
	 *
	 * @param theTxBuilder the transaction and partition settings
	 * @return the wrapped template
	 */
	default StreamTemplate<T> withTransactionAdvice(IExecutionBuilder theTxBuilder) {
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
