package ca.uhn.fhir.jpa.dao;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.jpa.model.entity.ResourceEncodingEnum;

class EncodedResource {

	private boolean myChanged;
	private byte[] myResource;
	private ResourceEncodingEnum myEncoding;
	private String myResourceText;

	public ResourceEncodingEnum getEncoding() {
		return myEncoding;
	}

	public void setEncoding(ResourceEncodingEnum theEncoding) {
		myEncoding = theEncoding;
	}

	public byte[] getResourceBinary() {
		return myResource;
	}

	public void setResourceBinary(byte[] theResource) {
		myResource = theResource;
	}

	public boolean isChanged() {
		return myChanged;
	}

	public void setChanged(boolean theChanged) {
		myChanged = theChanged;
	}

	public String getResourceText() {
		return myResourceText;
	}

	public void setResourceText(String theResourceText) {
		myResourceText = theResourceText;
	}
}
