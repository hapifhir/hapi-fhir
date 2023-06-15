package ca.uhn.fhir.jpa.search;

import javax.annotation.Nullable;

import org.springframework.transaction.support.SimpleTransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;

public class MockHapiTransactionService extends HapiTransactionService {

    @Nullable
    @Override
    protected <T> T doExecute(
            ExecutionBuilder theExecutionBuilder, TransactionCallback<T> theCallback) {
        return theCallback.doInTransaction(new SimpleTransactionStatus());
    }
}
