package org.hl7.fhir.instance.model.api;

public interface ICoding {

	ICoding setSystem(String theScheme);

	ICoding setCode(String theTerm);

	ICoding setDisplay(String theLabel);

}
