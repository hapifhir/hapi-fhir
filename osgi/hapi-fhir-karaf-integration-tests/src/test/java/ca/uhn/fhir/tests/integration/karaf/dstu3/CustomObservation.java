package ca.uhn.fhir.tests.integration.karaf.dstu3;

import ca.uhn.fhir.model.api.annotation.ResourceDef;
import org.hl7.fhir.dstu3.model.Observation;

@ResourceDef(name = "Observation", profile = CustomObservation.PROFILE)
public class CustomObservation extends Observation {

	public static final String PROFILE = "http://custom_Observation";
	
	private static final long serialVersionUID = 1L;

}
