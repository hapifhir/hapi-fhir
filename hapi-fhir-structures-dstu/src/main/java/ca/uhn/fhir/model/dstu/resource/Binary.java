package ca.uhn.fhir.model.dstu.resource;

/*
 * #%L
 * HAPI FHIR Structures - DSTU1 (FHIR v0.80)
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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

import org.hl7.fhir.instance.model.api.IBaseBinary;

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.primitive.Base64BinaryDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.util.ElementUtil;

@ResourceDef(name = "Binary", profile = "http://hl7.org/fhir/profiles/Binary", id = "binary")
public class Binary extends BaseResource implements IBaseBinary {

	@Child(name = "content", order = 1)
	private Base64BinaryDt myContent = new Base64BinaryDt();

	@Child(name = "contentType", order = 0)
	private StringDt myContentType;

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
		if (myContentType == null) {
			return null;
		}
		return myContentType.getValue();
	}

	@Override
	public boolean isEmpty() {
		return (myContent.isEmpty()) && ElementUtil.isEmpty(myContentType);
	}

	public Binary setContent(byte[] theContent) {
		myContent.setValue(theContent);
		return this;
	}

	public Binary setContentAsBase64(String theContent) {
		myContent.setValueAsString(theContent);
		return this;
	}

	public Binary setContentType(String theContentType) {
		myContentType = new StringDt(theContentType);
		return this;
	}

	@Override
	public String getResourceName() {
		return Binary.class.getName();
	}

	@Override
	public FhirVersionEnum getStructureFhirVersionEnum() {
		return FhirVersionEnum.DSTU1;
	}

}
