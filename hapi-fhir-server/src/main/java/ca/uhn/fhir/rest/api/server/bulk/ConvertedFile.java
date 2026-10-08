package ca.uhn.fhir.rest.api.server.bulk;

public class ConvertedFile {

	private byte[] myBytes;

	private String myMimeType;

	private String myResourceType;

	public byte[] getBytes() {
		return myBytes;
	}

	/**
	 * Sets the bytes that will be written into the binary.
	 */
	public void setBytes(byte[] theBytes) {
		myBytes = theBytes;
	}

	public String getMimeType() {
		return myMimeType;
	}

	/**
	 * Sets the mime type of the file to be written.
	 */
	public void setMimeType(String theMimeType) {
		myMimeType = theMimeType;
	}

	public String getResourceType() {
		return myResourceType;
	}

	/**
	 * The resource type of the resources contained in this file.
	 * Each file should only contain *one* type of resource,
	 * so this entire file should consist of only these resources.
	 */
	public void setResourceType(String theResourceType) {
		myResourceType = theResourceType;
	}
}
