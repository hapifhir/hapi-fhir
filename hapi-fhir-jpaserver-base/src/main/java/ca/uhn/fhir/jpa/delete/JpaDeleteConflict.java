package ca.uhn.fhir.jpa.delete;

import ca.uhn.fhir.jpa.api.model.DeleteConflict;
import ca.uhn.fhir.model.primitive.IdDt;

public class JpaDeleteConflict extends DeleteConflict {

	// FIXME: delete
	public JpaDeleteConflict(IdDt theSourceId, String theSourcePath, IdDt theTargetId) {
		super(theSourceId, theSourcePath, theTargetId);
	}

}
