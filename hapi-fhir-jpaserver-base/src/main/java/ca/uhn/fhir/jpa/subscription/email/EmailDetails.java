package ca.uhn.fhir.jpa.subscription.email;

import java.util.List;

public class EmailDetails {
	private String mySubjectTemplate;
	private String myBodyTemplate;
	private List<String> myTo;
	private String myFrom;

	public String getBodyTemplate() {
		return myBodyTemplate;
	}

	public void setBodyTemplate(String theBodyTemplate) {
		myBodyTemplate = theBodyTemplate;
	}

	public String getFrom() {
		return myFrom;
	}

	public void setFrom(String theFrom) {
		myFrom = theFrom;
	}

	public String getSubjectTemplate() {
		return mySubjectTemplate;
	}

	public void setSubjectTemplate(String theSubjectTemplate) {
		mySubjectTemplate = theSubjectTemplate;
	}

	public List<String> getTo() {
		return myTo;
	}

	public void setTo(List<String> theTo) {
		myTo = theTo;
	}

}
