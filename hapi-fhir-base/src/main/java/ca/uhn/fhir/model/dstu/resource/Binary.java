package ca.uhn.fhir.model.dstu.resource;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 University Health Network
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

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import ca.uhn.fhir.model.api.BaseResource;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.primitive.Base64BinaryDt;

@ResourceDef(name = "Binary", profile = "http://hl7.org/fhir/profiles/Binary", id = "binary")
public class Binary extends BaseResource implements IResource {

	private Base64BinaryDt myContent = new Base64BinaryDt();
	private String myContentType;

	/**
	 * Constructor
	 */
	public Binary() {
		// nothing
	}

	/**
	 * Constructor
	 * 
	 * @param theContentType
	 *            The content type
	 * @param theContent
	 *            The binary contents
	 */
	public Binary(String theContentType, byte[] theContent) {
		setContentType(theContentType);
		setContent(theContent);
	}

	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return Collections.emptyList();
	}

	public byte[] getContent() {
		return myContent.getValue();
	}

	public String getContentAsBase64() {
		return myContent.getValueAsString();
	}

	public String getContentType() {
		return myContentType;
	}

	@Override
	public boolean isEmpty() {
		return (myContent.isEmpty()) && StringUtils.isBlank(myContentType);
	}

	public void setContent(byte[] theContent) {
		myContent.setValue(theContent);
	}

	public void setContentAsBase64(String theContent) {
		myContent.setValueAsString(theContent);
	}

	public void setContentType(String theContentType) {
		myContentType = theContentType;
	}

	@Override
	public String getResourceName() {
		return Binary.class.getName();
	}

}
