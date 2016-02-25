package ca.uhn.fhir.rest.server;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
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

import java.util.HashMap;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;

public enum EncodingEnum {

	XML(Constants.CT_FHIR_XML, Constants.CT_ATOM_XML, Constants.FORMAT_XML) {
		@Override
		public IParser newParser(FhirContext theContext) {
			return theContext.newXmlParser();
		}
	},

	JSON(Constants.CT_FHIR_JSON, Constants.CT_FHIR_JSON, Constants.FORMAT_JSON) {
		@Override
		public IParser newParser(FhirContext theContext) {
			return theContext.newJsonParser();
		}
	}

	;

	private static HashMap<String, EncodingEnum> ourContentTypeToEncoding;
	private static HashMap<String, EncodingEnum> ourContentTypeToEncodingStrict;

	static {
		ourContentTypeToEncoding = new HashMap<String, EncodingEnum>();
		for (EncodingEnum next : values()) {
			ourContentTypeToEncoding.put(next.getBundleContentType(), next);
			ourContentTypeToEncoding.put(next.getResourceContentType(), next);
		}
		
		// Add before we add the lenient ones
		ourContentTypeToEncodingStrict = new HashMap<String, EncodingEnum>(ourContentTypeToEncoding);

		/*
		 * These are wrong, but we add them just to be tolerant of other
		 * people's mistakes
		 */
		ourContentTypeToEncoding.put("application/json", JSON);
		ourContentTypeToEncoding.put("application/xml", XML);
		ourContentTypeToEncoding.put("application/fhir+xml", XML);
		ourContentTypeToEncoding.put("text/json", JSON);
		ourContentTypeToEncoding.put("text/xml", XML);

	}

	private String myResourceContentType;
	private String myBundleContentType;
	private String myFormatContentType;

	EncodingEnum(String theResourceContentType, String theBundleContentType, String theFormatContentType) {
		myResourceContentType = theResourceContentType;
		myBundleContentType = theBundleContentType;
		myFormatContentType = theFormatContentType;
	}

	public String getRequestContentType() {
		return myFormatContentType;
	}

	public abstract IParser newParser(FhirContext theContext);

	public String getBundleContentType() {
		return myBundleContentType;
	}

	public String getResourceContentType() {
		return myResourceContentType;
	}

	/**
	 * Returns the encoding for a given content type, or <code>null</code> if no encoding
	 * is found. 
	 * <p>
	 * <b>This method is lenient!</b> Things like "application/xml" will return {@link EncodingEnum#XML}
	 * even if the "+fhir" part is missing from the expected content type.
	 * </p>
	 */
	public static EncodingEnum forContentType(String theContentType) {
		return ourContentTypeToEncoding.get(theContentType);
	}

	/**
	 * Returns the encoding for a given content type, or <code>null</code> if no encoding
	 * is found. 
	 * <p>
	 * <b>This method is NOT lenient!</b> Things like "application/xml" will return <code>null</code>
	 * </p>
	 * @see #forContentType(String)
	 */
	public static EncodingEnum forContentTypeStrict(String theContentType) {
		return ourContentTypeToEncodingStrict.get(theContentType);
	}

	public String getFormatContentType() {
		return myFormatContentType;
	}

}
