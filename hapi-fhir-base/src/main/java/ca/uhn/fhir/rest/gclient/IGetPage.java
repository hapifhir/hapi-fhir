package ca.uhn.fhir.rest.gclient;

import ca.uhn.fhir.model.api.Bundle;

public interface IGetPage {

	IGetPageTyped previous(Bundle theBundle);
	
	IGetPageTyped next(Bundle theBundle);
	
	IGetPageTyped url(String thePageUrl);
	
}
