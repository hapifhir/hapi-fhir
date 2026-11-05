/*-
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
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
