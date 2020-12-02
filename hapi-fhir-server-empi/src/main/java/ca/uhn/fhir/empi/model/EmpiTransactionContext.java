package ca.uhn.fhir.empi.model;

public class EmpiTransactionContext {

	/**
	 * Any EMPI methods may add transaction log messages.
	 */
	private TransactionLogMessages myTransactionLogMessages;

	private OperationType myRestOperation;


	public enum OperationType {
		CREATE_RESOURCE,
		UPDATE_RESOURCE,
		SUBMIT_RESOURCE_TO_EMPI,
		QUERY_LINKS,
		UPDATE_LINK,
		DUPLICATE_PERSONS,
		NOT_DUPLICATE,
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
