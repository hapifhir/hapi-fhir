package ca.uhn.fhir.batch2.jobs.installpackage.model;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PackageContentsJson implements IModelJson {

	@JsonProperty("contents")
	private byte[] myContents;

	public byte[] getContents() {
		return myContents;
	}
	public void setContents(byte[] theContents) {
		myContents = theContents;
	}
}
