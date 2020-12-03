package ca.uhn.fhir.mdm.model;

import ca.uhn.fhir.rest.server.TransactionLogMessages;

public class MdmTransactionContext {

	public enum OperationType {
		CREATE_RESOURCE,
		UPDATE_RESOURCE,
		SUBMIT_RESOURCE_TO_MDM,
		QUERY_LINKS,
		UPDATE_LINK,
		DUPLICATE_GOLDEN_RESOURCES,
		NOT_DUPLICATE,
		MERGE_GOLDEN_RESOURCES
	}

	/**
	 * Any MDM methods may add transaction log messages.
	 */
	private TransactionLogMessages myTransactionLogMessages;

	private OperationType myRestOperation;

	private String myResourceType;

	public TransactionLogMessages getTransactionLogMessages() {
		return myTransactionLogMessages;
	}

	public MdmTransactionContext() {
	}

	public MdmTransactionContext(TransactionLogMessages theTransactionLogMessages, OperationType theRestOperation) {
		myTransactionLogMessages = theTransactionLogMessages;
		myRestOperation = theRestOperation;
	}

	public MdmTransactionContext(TransactionLogMessages theTransactionLogMessages, OperationType theRestOperation, String theResourceType) {
		this(theTransactionLogMessages, theRestOperation);
		setResourceType(theResourceType);
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

	public String getResourceType() {
		return myResourceType;
	}

	public void setResourceType(String myResourceType) {
		this.myResourceType = myResourceType;
	}

}
