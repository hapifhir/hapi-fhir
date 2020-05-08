package ca.uhn.fhir.empi.model;

import ca.uhn.fhir.rest.server.TransactionLogMessages;

public class EmpiTransactionContext {

	/**
	 * Any EMPI methods may add transaction log messages.
	 */
	private TransactionLogMessages myTransactionLogMessages;

	private OperationType myRestOperation;


	public enum OperationType {
		CREATE,
		UPDATE,
		BATCH,
		MERGE_PERSONS
	}
	public TransactionLogMessages getTransactionLogMessages() {
		return myTransactionLogMessages;
	}

	public EmpiTransactionContext() {
	}

	public EmpiTransactionContext(TransactionLogMessages theTransactionLogMessages, OperationType theRestOperation) {
		myTransactionLogMessages = theTransactionLogMessages;
		myRestOperation = theRestOperation;
	}

	public void addTransactionLogMessage(String theMessage) {
		if (myTransactionLogMessages == null) {
			return;
		}
		this.myTransactionLogMessages.addMessage(myTransactionLogMessages, theMessage);
	}

	public OperationType getRestOperation() {
		return myRestOperation;
	}

	public void setTransactionLogMessages(TransactionLogMessages theTransactionLogMessages) {
		myTransactionLogMessages = theTransactionLogMessages;
	}

	public void setRestOperation(OperationType theRestOperation) {
		myRestOperation = theRestOperation;
	}
}
