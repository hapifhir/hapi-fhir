package ca.uhn.fhir.jpa.interceptor.ex;

import ca.uhn.fhir.model.api.IProvenanceAgent;

public class ProvenanceAgentTestInterceptor {
	private final IProvenanceAgent myAgent;

	public ProvenanceAgentTestInterceptor(IProvenanceAgent theAgent) {
		myAgent = theAgent;
	}

	@ca.uhn.fhir.interceptor.api.Hook(ca.uhn.fhir.interceptor.api.Pointcut.PROVENANCE_AGENT)
	public IProvenanceAgent getProvenanceAgent() {
		return myAgent;
	}
}
