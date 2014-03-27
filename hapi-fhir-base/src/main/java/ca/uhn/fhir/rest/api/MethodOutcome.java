package ca.uhn.fhir.rest.api;

import ca.uhn.fhir.model.primitive.IdDt;

public class MethodOutcome {

	private IdDt myId;
	private IdDt myVersionId;

	public IdDt getId() {
		return myId;
	}

	public MethodOutcome() {
	}

	public MethodOutcome(IdDt theId, IdDt theVersionId) {
		super();
		myId = theId;
		myVersionId = theVersionId;
	}

	public void setId(IdDt theId) {
		myId = theId;
	}

	public IdDt getVersionId() {
		return myVersionId;
	}

	public void setVersionId(IdDt theVersionId) {
		myVersionId = theVersionId;
	}

}
