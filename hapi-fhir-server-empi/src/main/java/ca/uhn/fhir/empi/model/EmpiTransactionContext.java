package ca.uhn.fhir.empi.model;

import ca.uhn.fhir.rest.server.TransactionLogMessages;
import com.google.common.annotations.VisibleForTesting;
import org.hl7.fhir.instance.model.api.IBaseResource;

public class EmpiTransactionContext {

	/**
	 * Any EMPI methods may add transaction log messages.
	 */
	private TransactionLogMessages myTransactionLogMessages;

	/**
	 * The payload of the original message that came in causing empi subscriber to start processing.
	 */
	private IBaseResource myPayload;
	private OperationType myRestOperation;


	public enum OperationType {
		CREATE,
		UPDATE,
		BATCH
	}
	public TransactionLogMessages getTransactionLogMessages() {
		return myTransactionLogMessages;
	}

	public EmpiTransactionContext() {
	}

	public EmpiTransactionContext(TransactionLogMessages theTransactionLogMessages, IBaseResource thePayload, OperationType theRestOperation) {
		myTransactionLogMessages = theTransactionLogMessages;
		myPayload = thePayload;
		myRestOperation = theRestOperation;
	}

	public void addTransactionLogMessage(String theMessage) {
		if (myTransactionLogMessages == null) {
			return;
		}
		this.myTransactionLogMessages.addMessage(myTransactionLogMessages, theMessage);
	}

	public IBaseResource getPayload() {
		return myPayload;
	}

	public OperationType getRestOperation() {
		return myRestOperation;
	}

	public void setTransactionLogMessages(TransactionLogMessages theTransactionLogMessages) {
		myTransactionLogMessages = theTransactionLogMessages;
	}

	public void setPayload(IBaseResource thePayload) {
		myPayload = thePayload;
	}

	public void setRestOperation(OperationType theRestOperation) {
		myRestOperation = theRestOperation;
	}
}
