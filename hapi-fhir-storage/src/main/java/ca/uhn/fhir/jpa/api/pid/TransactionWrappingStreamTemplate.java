package ca.uhn.fhir.jpa.api.pid;

import org.springframework.transaction.support.TransactionOperations;

import java.util.function.Function;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
