package ca.uhn.fhir.parser;

import org.hl7.fhir.dstu2016may.model.Observation;

import ca.uhn.fhir.model.api.annotation.ResourceDef;

@ResourceDef(name = "Observation", profile = CustomObservation.PROFILE)
public class CustomObservation extends Observation {

	public static final String PROFILE = "http://custom_Observation";
	
	private static final long serialVersionUID = 1L;

}