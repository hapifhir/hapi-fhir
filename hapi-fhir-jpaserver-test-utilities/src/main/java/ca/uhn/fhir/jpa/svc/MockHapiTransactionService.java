package ca.uhn.fhir.jpa.svc;

import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.SimpleTransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import javax.annotation.Nullable;

public class MockHapiTransactionService extends HapiTransactionService {

	private TransactionStatus myTransactionStatus;

	public MockHapiTransactionService() {
		this(new SimpleTransactionStatus());
	}

	public MockHapiTransactionService(TransactionStatus theTransactionStatus) {
		myTransactionStatus = theTransactionStatus;
	}

	@Nullable
	@Override
	protected <T> T doExecute(ExecutionBuilder theExecutionBuilder, TransactionCallback<T> theCallback) {
		return theCallback.doInTransaction(myTransactionStatus);
	}

}
