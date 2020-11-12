package ca.uhn.fhir.empi.util;

import ca.uhn.fhir.empi.model.MdmTransactionContext;

public class TestUtils {
	public static MdmTransactionContext createDummyContext() {
			MdmTransactionContext context = new MdmTransactionContext();
			return context;
	}
}
