package ca.uhn.fhir.empi.model;

import ca.uhn.fhir.rest.server.TransactionLogMessages;
import org.hl7.fhir.instance.model.api.IAnyResource;

public class EmpiTransactionContext {

	/**
	 * Any EMPI methods may add transaction log messages.
	 */
	private TransactionLogMessages myTransactionLogMessages;

	/**
	 * The payload of the original message that came in causing empi subscriber to start processing.
	 */
	private IAnyResource[] myResources;
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

	public EmpiTransactionContext(TransactionLogMessages theTransactionLogMessages, OperationType theRestOperation, IAnyResource... theResources) {
		myTransactionLogMessages = theTransactionLogMessages;
		myResources = theResources;
		myRestOperation = theRestOperation;
	}

	public void addTransactionLogMessage(String theMessage) {
		if (myTransactionLogMessages == null) {
			return;
		}
		this.myTransactionLogMessages.addMessage(myTransactionLogMessages, theMessage);
	}

	public IAnyResource[] getResources() {
		return myResources;
	}

	public OperationType getRestOperation() {
		return myRestOperation;
	}

	public void setTransactionLogMessages(TransactionLogMessages theTransactionLogMessages) {
		myTransactionLogMessages = theTransactionLogMessages;
	}

	public void setResources(IAnyResource... theResources) {
		myResources = theResources;
	}

	public void setRestOperation(OperationType theRestOperation) {
		myRestOperation = theRestOperation;
	}
}
