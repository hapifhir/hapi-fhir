package ca.uhn.fhir.model.api;

import org.hl7.fhir.instance.model.api.IBaseReference;

import java.io.Serializable;

public interface IProvenanceAgent extends Serializable {
	IBaseReference getWho();
	IBaseReference getOnBehalfOf();
}
