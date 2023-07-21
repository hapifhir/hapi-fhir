package ca.uhn.fhir.mdm.model.mdmevents;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MdmEventResource implements IModelJson {
	/**
	 * The id of the resource that's part of this event.
	 */
	@JsonProperty("id")
	private String myId;

	/**
	 * The resource type.
	 */
	@JsonProperty("resourceType")
	private String myResourceType;

	/**
	 * True if this is a golden resource; false otherwise.
	 */
	@JsonProperty("isGoldenResource")
	private boolean myIsGoldenResource;

	public String getId() {
		return myId;
	}

	public void setId(String theId) {
		myId = theId;
	}

	public String getResourceType() {
		return myResourceType;
	}

	public void setResourceType(String theResourceType) {
		myResourceType = theResourceType;
	}

	public boolean isGoldenResource() {
		return myIsGoldenResource;
	}

	public void setIsGoldenResource(boolean theGoldenResource) {
		myIsGoldenResource = theGoldenResource;
	}
}
