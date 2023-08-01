package ca.uhn.fhir.jpa.mdm.models;

import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import org.hl7.fhir.instance.model.api.IAnyResource;

public class FindGoldenResourceCandidatesParams {

	/**
	 * The resource to find matches for
	 */
	private final IAnyResource myResource;

	/**
	 * The mdm context
	 */
	private final MdmTransactionContext myContext;

	public FindGoldenResourceCandidatesParams(IAnyResource theResource, MdmTransactionContext theContext) {
		myResource = theResource;
		myContext = theContext;
	}

	public IAnyResource getResource() {
		return myResource;
	}

	public MdmTransactionContext getContext() {
		return myContext;
	}
}
