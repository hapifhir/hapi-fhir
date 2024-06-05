package ca.uhn.fhir.rest.api.server.storage;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hl7.fhir.instance.model.api.IIdType;

/**
 * A deserialized version of the PID
 * Must be converted to appropriate PID type to be used.
 */
public class SerializablePid implements IResourcePersistentId<String>, IModelJson {

	@JsonProperty("id")
	private String myId;

	@JsonProperty("resourceType")
	private String myResourceType;

	@JsonProperty("associatedResourceId")
	private String myAssociatedResourceId;

	@JsonProperty("version")
	private Long myVersion;

	SerializablePid() {}

	public SerializablePid(String theResourceType, String theId) {
		myId = theId;
		myResourceType = theResourceType;
	}

	@Override
	public IIdType getAssociatedResourceId() {
		throw new UnsupportedOperationException(
				Msg.code(2527)
						+ " This method is not implemented; call getAssociatedResourceIdStr() and use a FhirContext.getVersion().newId() to create.");
	}

	public String getAssociatedResourceIdStr() {
		return myAssociatedResourceId;
	}

	@JsonIgnore
	@Override
	public IResourcePersistentId<String> setAssociatedResourceId(IIdType theAssociatedResourceId) {
		myAssociatedResourceId = theAssociatedResourceId != null ? theAssociatedResourceId.getValueAsString() : null;
		return this;
	}

	/**
	 * If the id couldn't be parsed into a known object, it will be a map of fields to properties
	 */
	@Override
	public String getId() {
		return myId;
	}

	@Override
	public Long getVersion() {
		return myVersion;
	}

	@Override
	public void setVersion(Long theVersion) {
		myVersion = theVersion;
	}

	@Override
	public String getResourceType() {
		return myResourceType;
	}
}
