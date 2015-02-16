package org.hl7.fhir.instance.model.api;

import org.hl7.fhir.instance.model.IBase;

public interface IReference extends IBase {

	IAnyResource getResource();

	void setResource(IAnyResource theResource);

	String getReference();

	IReference setReference(String theReference);

	IBase setDisplay(String theValue);
}
