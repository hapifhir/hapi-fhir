package ca.uhn.fhir.empi.model;

import java.util.ArrayList;
import java.util.List;

public class EmpiMessages {
	private List<String> myMessages;
	public void addMessage(String theMessage) {
		if (myMessages == null) {
			myMessages = new ArrayList<>();
		}
		myMessages.add(theMessage);
	}

	public List<String> getValues() {
		return myMessages;
	}

	public static void addMessage(EmpiMessages theEmpiMessages, String theMessage) {
		if (theEmpiMessages == null) {
			return;
		}
		theEmpiMessages.addMessage(theMessage);
	}

}
