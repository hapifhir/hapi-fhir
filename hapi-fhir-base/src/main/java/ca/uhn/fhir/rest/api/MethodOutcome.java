package ca.uhn.fhir.rest.api;

import ca.uhn.fhir.model.primitive.IdDt;

public class MethodOutcome {

	private IdDt myId;
	private IdDt myVersionId;
	private boolean myCreated;

	public MethodOutcome() {
	}

	public MethodOutcome(boolean theCreated, IdDt theId, IdDt theVersionId) {
		super();
		myId = theId;
		myVersionId = theVersionId;
		myCreated=theCreated;
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

	/**
	 * Set to <code>true</code> if the method resulted in the creation of a new resource. Set to 
	 * <code>false</code> if the method resulted in an update/modification/removal to an existing resource.
	 */
	public boolean isCreated() {
		return myCreated;
	}

	/**
	 * Set to <code>true</code> if the method resulted in the creation of a new resource. Set to 
	 * <code>false</code> if the method resulted in an update/modification/removal to an existing resource.
	 */
	public void setCreated(boolean theCreated) {
		myCreated=theCreated;
	}

}
