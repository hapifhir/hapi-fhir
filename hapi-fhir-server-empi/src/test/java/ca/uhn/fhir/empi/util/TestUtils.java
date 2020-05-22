package ca.uhn.fhir.empi.util;

import ca.uhn.fhir.empi.model.EmpiTransactionContext;

public class TestUtils {
	public static EmpiTransactionContext createDummyContext() {
			EmpiTransactionContext context = new EmpiTransactionContext();
			return context;
	}
}
