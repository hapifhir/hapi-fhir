package ca.uhn.fhir.rest.gclient;

import java.io.IOException;
import java.io.OutputStream;

public class RawRequestEntity {
	private final String myContentType;
	private final byte[] myBytes;

	/**
	 * Constructor
	 *
	 * @param theContentType The "Content-Type" header value of the request (e.g. "application/json")
	 * @param theBytes       The bytes to use as the request body payload
	 * @since 8.12.0
	 */
	public RawRequestEntity(String theContentType, byte[] theBytes) {
		myContentType = theContentType;
		myBytes = theBytes;
	}

	public String getContentType() {
		return myContentType;
	}

	public byte[] getBytes() {
		return myBytes;
	}
}
