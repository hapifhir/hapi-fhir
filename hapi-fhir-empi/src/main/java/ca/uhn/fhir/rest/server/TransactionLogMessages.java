package ca.uhn.fhir.rest.server;

import java.util.ArrayList;
import java.util.List;

public class TransactionLogMessages {
	private List<String> myMessages;
	private final String myParentRequestId;

	private TransactionLogMessages(String theParentRequestId) {
		myParentRequestId = theParentRequestId;
	}

	public static TransactionLogMessages createFromRequestId(String theParentRequestId) {
		return new TransactionLogMessages(theParentRequestId);
	}

	private void addMessage(String theMessage) {
		if (myMessages == null) {
			myMessages = new ArrayList<>();
		}
		myMessages.add(theMessage);
	}

	public List<String> getValues() {
		return myMessages;
	}

	public static void addMessage(TransactionLogMessages theTransactionLogMessages, String theMessage) {
		if (theTransactionLogMessages == null) {
			return;
		}
		theTransactionLogMessages.addMessage(theMessage);
	}

	public String getParentRequestId() {
		return myParentRequestId;
	}
}
