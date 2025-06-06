package ca.uhn.fhir.model.api;

import org.hl7.fhir.instance.model.api.IBaseReference;

public class ProvenanceAgent implements IProvenanceAgent {

	private static final long serialVersionUID = 1L;

	IBaseReference myWho;
	IBaseReference myOnBehalfOf;

	@Override
	public IBaseReference getWho() {
		return myWho;
	}

	public IProvenanceAgent setWho(IBaseReference theWho) {
		myWho = theWho;
		return this;
	}

	@Override
	public IBaseReference getOnBehalfOf() {
		return myOnBehalfOf;
	}

	public IProvenanceAgent setOnBehalfOf(IBaseReference theOnBehalfOf) {
		myOnBehalfOf = theOnBehalfOf;
		return this;
	}
}
