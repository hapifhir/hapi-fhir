package ca.uhn.fhir.batch2.jobs.export.models;

import ca.uhn.fhir.batch2.jobs.chunk.TypedPidJson;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hl7.fhir.instance.model.api.IIdType;

public class PatientIdAndPidJson extends TypedPidJson {

	/**
	 * This is the actual resource id (server or forced).
	 * Eg: Patient/123 or Patient/RED
	 */
	@JsonProperty("resourceId")
	private String myResourceId;

	/**
	 * Empty constructor for serialization
	 */
	public PatientIdAndPidJson() {}

	public PatientIdAndPidJson(IResourcePersistentId theResourcePersistentId) {
		super(
				theResourcePersistentId.getResourceType(),
				theResourcePersistentId.getPartitionId(),
				theResourcePersistentId.getId().toString());

		setResourceId(theResourcePersistentId.getAssociatedResourceId());
	}

	public String getResourceId() {
		return myResourceId;
	}

	public void setResourceId(IIdType theIdType) {
		this.setResourceId(theIdType.toUnqualifiedVersionless().getValue());
	}

	public void setResourceId(String theResourceId) {
		myResourceId = theResourceId;
	}

	public <T extends IResourcePersistentId<?>> T toPersistentId(
			IIdHelperService<T> theIdHelperService, FhirContext theFhirContext) {
		T pid = super.toPersistentId(theIdHelperService);

		// set the resource persistent id
		pid.setAssociatedResourceId(theFhirContext.getVersion().newIdType(getResourceId()));
		return pid;
	}
}
