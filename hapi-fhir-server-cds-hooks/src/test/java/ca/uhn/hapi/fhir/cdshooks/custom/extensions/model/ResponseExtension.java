package ca.uhn.hapi.fhir.cdshooks.custom.extensions.model;

import ca.uhn.hapi.fhir.cdshooks.api.json.CdsHooksExtension;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class ResponseExtension extends CdsHooksExtension {
	@JsonProperty(value = "timestamp", required = true)
	private Date myDate;
	@JsonProperty(value = "myextension-practitionerspecialty", required = true)
	private String myPractitionerSpecialty;

	public void setTimestamp(Date theDate) {
		myDate = theDate;
	}

	public void setPractitionerSpecialty(String thePractitionerSpecialty) {
		myPractitionerSpecialty = thePractitionerSpecialty;
	}

}
