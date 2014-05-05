package ca.uhn.example.rest;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.dstu.resource.DiagnosticReport;
import ca.uhn.fhir.model.primitive.StringDt;

@ResourceDef(name = "DiagnosticReport")
public class MyDiagnosticReport extends DiagnosticReport {

	/*
	 * Fields
	 */

	@Extension(url = "http://foo#additional", isModifier = false, definedLocally = true)
	@Child(name = "additionalInformation")
	private StringDt myAdditionalInformation;

	/*
	 * Getters and Setters
	 */
	
	public StringDt getAdditionalInformation() {
		if (myAdditionalInformation==null) {
			myAdditionalInformation=new StringDt();
		}
		return myAdditionalInformation;
	}

	public void setAdditionalInformation(StringDt theAdditionalInformation) {
		myAdditionalInformation = theAdditionalInformation;
	}

}
