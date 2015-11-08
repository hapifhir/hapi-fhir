package ca.uhn.fhir.jpa.util;

import ca.uhn.fhir.model.primitive.IdDt;

public class DeleteConflict {

	private final IdDt mySourceId;
	private final String mySourcePath;
	private final IdDt myTargetId;

	public DeleteConflict(IdDt theSourceId, String theSourcePath, IdDt theTargetId) {
		mySourceId = theSourceId;
		mySourcePath = theSourcePath;
		myTargetId = theTargetId;
	}

	public IdDt getSourceId() {
		return mySourceId;
	}

	public String getSourcePath() {
		return mySourcePath;
	}

	public IdDt getTargetId() {
		return myTargetId;
	}

}
