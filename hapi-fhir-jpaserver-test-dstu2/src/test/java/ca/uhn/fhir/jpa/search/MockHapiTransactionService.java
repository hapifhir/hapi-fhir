package ca.uhn.fhir.jpa.search;

import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import org.springframework.transaction.support.SimpleTransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import javax.annotation.Nullable;

public class MockHapiTransactionService extends HapiTransactionService {

	@Nullable
	@Override
	protected <T> T doExecute(ExecutionBuilder theExecutionBuilder, TransactionCallback<T> theCallback) {
		return theCallback.doInTransaction(new SimpleTransactionStatus());
	}

}
