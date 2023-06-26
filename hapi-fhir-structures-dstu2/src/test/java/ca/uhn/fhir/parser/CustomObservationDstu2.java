package ca.uhn.fhir.parser;

import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.dstu2.resource.Observation;

@ResourceDef(name = "Observation", profile = CustomObservationDstu2.PROFILE)
public class CustomObservationDstu2 extends Observation {

	public static final String PROFILE = "http://custom_Observation";
	
	private static final long serialVersionUID = 1L;

}