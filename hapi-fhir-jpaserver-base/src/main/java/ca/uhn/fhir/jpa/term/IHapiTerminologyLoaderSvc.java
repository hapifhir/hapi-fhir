package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.rest.method.RequestDetails;

public interface IHapiTerminologyLoaderSvc {

	String SCT_URL = "http://snomed.info/sct";
	
	void loadSnomedCt(byte[] theZipBytes, RequestDetails theRequestDetails);

}
