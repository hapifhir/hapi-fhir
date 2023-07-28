package ca.uhn.fhir.jpa.mdm.models;

import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import org.hl7.fhir.instance.model.api.IAnyResource;

public class FindGoldenResourceCandidatesParams {

	/**
	 * The resource to find matches for
	 */
	private IAnyResource myResource;

	/**
	 * The mdm context
	 */
	private MdmTransactionContext myContext;

	public IAnyResource getResource() {
		return myResource;
	}

	public void setResource(IAnyResource theResource) {
		myResource = theResource;
	}

	public MdmTransactionContext getContext() {
		return myContext;
	}

	public void setContext(MdmTransactionContext theContext) {
		myContext = theContext;
	}
}
