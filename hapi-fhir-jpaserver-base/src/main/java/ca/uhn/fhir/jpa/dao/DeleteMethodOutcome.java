package ca.uhn.fhir.jpa.dao;

import java.util.List;

import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.rest.api.MethodOutcome;

/**
 * This class is a replacement for {@link DaoMethodOutcome} for delete operations,
 * as they can perform their operation over multiple resources
 */
public class DeleteMethodOutcome extends MethodOutcome {

	private List<ResourceTable> myDeletedEntities;

	public List<ResourceTable> getDeletedEntities() {
		return myDeletedEntities;
	}

	public DeleteMethodOutcome setDeletedEntities(List<ResourceTable> theDeletedEntities) {
		myDeletedEntities = theDeletedEntities;
		return this;
	}

}
