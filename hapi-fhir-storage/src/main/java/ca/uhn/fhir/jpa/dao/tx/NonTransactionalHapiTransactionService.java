package ca.uhn.fhir.jpa.dao.tx;

import org.springframework.transaction.support.TransactionCallback;

import javax.annotation.Nullable;

/**
 * A transaction service implementation that does not actually
 * wrap any transactions. This is mostly intended for tests but
 * could be used in non-transactional systems too.
 */
public class NonTransactionalHapiTransactionService extends HapiTransactionService {

	@Nullable
	@Override
	protected <T> T doExecute(ExecutionBuilder theExecutionBuilder, TransactionCallback<T> theCallback) {
		return theCallback.doInTransaction(null);
	}
}
