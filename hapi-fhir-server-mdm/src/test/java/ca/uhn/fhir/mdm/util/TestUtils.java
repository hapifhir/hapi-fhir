package ca.uhn.fhir.mdm.util;

import ca.uhn.fhir.mdm.model.MdmTransactionContext;

public class TestUtils {
	public static MdmTransactionContext createDummyContext() {
			MdmTransactionContext context = new MdmTransactionContext();
			return context;
	}
}
