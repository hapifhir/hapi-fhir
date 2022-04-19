package ca.uhn.fhir.batch2.jobs.models;

import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hl7.fhir.r4.model.IdType;

public class Id implements IModelJson {

	@JsonProperty("type")
	private String myResourceType;
	@JsonProperty("id")
	private String myId;

	@Override
	public String toString() {
		// We put a space in here and not a "/" since this is a PID, not
		// a resource ID
		return "[" + myResourceType + " " + myId + "]";
	}

	public String getResourceType() {
		return myResourceType;
	}

	public Id setResourceType(String theResourceType) {
		myResourceType = theResourceType;
		return this;
	}

	public String getId() {
		return myId;
	}

	public Id setId(String theId) {
		myId = theId;
		return this;
	}

	@Override
	public boolean equals(Object theO) {
		if (this == theO) return true;

		if (theO == null || getClass() != theO.getClass()) return false;

		Id id = (Id) theO;

		return new EqualsBuilder().append(myResourceType, id.myResourceType).append(myId, id.myId).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(myResourceType).append(myId).toHashCode();
	}

	public static Id getIdFromPID(ResourcePersistentId thePID, String theResourceType) {
		Id id = new Id();
		id.setId(thePID.getId().toString());
		id.setResourceType(theResourceType);
		return id;
	}

	public ResourcePersistentId toPID() {
		ResourcePersistentId pid = new ResourcePersistentId(myId);
		pid.setAssociatedResourceId(new IdType(myId));
		return pid;
	}
}
