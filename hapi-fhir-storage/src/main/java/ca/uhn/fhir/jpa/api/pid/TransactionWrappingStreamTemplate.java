package ca.uhn.fhir.jpa.api.pid;

import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService.IExecutionBuilder;

import java.util.function.Function;
import java.util.stream.Stream;
import javax.annotation.Nonnull;

/**
 * Wrap a StreamTemplate with transaction advice.
 */
class TransactionWrappingStreamTemplate<T> implements StreamTemplate<T> {
	@Nonnull
	final IExecutionBuilder theTxBuilder;

	@Nonnull
	final StreamTemplate<T> theWrappedStreamTemplate;

	TransactionWrappingStreamTemplate(
			@Nonnull IExecutionBuilder theTheTxBuilder, @Nonnull StreamTemplate<T> theTheWrappedStreamTemplate) {
		theTxBuilder = theTheTxBuilder;
		theWrappedStreamTemplate = theTheWrappedStreamTemplate;
	}

	@Nonnull
	@Override
	public <R> R call(Function<Stream<T>, R> theCallback) {
		return theTxBuilder.execute(() -> theWrappedStreamTemplate.call(theCallback));
	}
}
