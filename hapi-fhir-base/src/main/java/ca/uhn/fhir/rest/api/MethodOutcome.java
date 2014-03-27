package ca.uhn.fhir.rest.api;

import ca.uhn.fhir.model.primitive.IdDt;

public class MethodOutcome {

	private IdDt myId;
	private IdDt myVersionId;

	public MethodOutcome() {
	}

	public MethodOutcome(IdDt theId, IdDt theVersionId) {
		super();
		myId = theId;
		myVersionId = theVersionId;
	}

	public IdDt getId() {
		return myId;
	}

	public IdDt getVersionId() {
		return myVersionId;
	}

	public void setId(IdDt theId) {
		myId = theId;
	}

	public void setVersionId(IdDt theVersionId) {
		myVersionId = theVersionId;
	}

}
