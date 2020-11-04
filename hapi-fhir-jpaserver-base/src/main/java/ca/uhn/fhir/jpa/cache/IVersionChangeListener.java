package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.model.primitive.IdDt;

public interface IVersionChangeListener {
	void handleCreate(IdDt theResourceId);
	void handleUpdate(IdDt theResourceId);
	void handleDelete(IdDt theResourceId);
}
