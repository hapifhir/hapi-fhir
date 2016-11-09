package ca.uhn.fhir.android;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.okhttp.client.OkHttpRestfulClientFactory;

/**
 * This class exists in order to ensure that 
 */
public class AndroidMarker {

	public static void configureContext(FhirContext theContext) {
		theContext.setRestfulClientFactory(new OkHttpRestfulClientFactory(theContext));
	}
	
	
}
