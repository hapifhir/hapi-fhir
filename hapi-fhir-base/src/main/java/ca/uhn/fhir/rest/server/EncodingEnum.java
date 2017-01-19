package ca.uhn.fhir.rest.server;

import java.util.Collections;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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
import java.util.Map;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;

public enum EncodingEnum {

	JSON(Constants.CT_FHIR_JSON, Constants.CT_FHIR_JSON_NEW, Constants.CT_FHIR_JSON, Constants.FORMAT_JSON) {
		@Override
		public IParser newParser(FhirContext theContext) {
			return theContext.newJsonParser();
		}
	},

	XML(Constants.CT_FHIR_XML, Constants.CT_FHIR_XML_NEW, Constants.CT_ATOM_XML, Constants.FORMAT_XML) {
		@Override
		public IParser newParser(FhirContext theContext) {
			return theContext.newXmlParser();
		}
	}

	;

	/** "xml" */
	public static final String XML_PLAIN_STRING = "xml";
	/** "json" */
	public  static final String JSON_PLAIN_STRING = "json";
	
	private static Map<String, EncodingEnum> ourContentTypeToEncoding;
	private static Map<String, EncodingEnum> ourContentTypeToEncodingNonLegacy;
	private static Map<String, EncodingEnum> ourContentTypeToEncodingStrict;

	static {
		ourContentTypeToEncoding = new HashMap<String, EncodingEnum>();
		ourContentTypeToEncodingNonLegacy = new HashMap<String, EncodingEnum>();
		
		for (EncodingEnum next : values()) {
			ourContentTypeToEncoding.put(next.getBundleContentType(), next);
			ourContentTypeToEncoding.put(next.myResourceContentTypeNonLegacy, next);
			ourContentTypeToEncoding.put(next.myResourceContentTypeLegacy, next);
			ourContentTypeToEncodingNonLegacy.put(next.myResourceContentTypeNonLegacy, next);

			/*
			 * See #346
			 */
			ourContentTypeToEncoding.put(next.myResourceContentTypeNonLegacy.replace('+', ' '), next);
			ourContentTypeToEncoding.put(next.myResourceContentTypeLegacy.replace('+', ' '), next);
			ourContentTypeToEncodingNonLegacy.put(next.myResourceContentTypeNonLegacy.replace('+', ' '), next);

		}

		// Add before we add the lenient ones
		ourContentTypeToEncodingStrict = Collections.unmodifiableMap(new HashMap<String, EncodingEnum>(ourContentTypeToEncoding));

		/*
		 * These are wrong, but we add them just to be tolerant of other
		 * people's mistakes
		 */
		ourContentTypeToEncoding.put("application/json", JSON);
		ourContentTypeToEncoding.put("application/xml", XML);
		ourContentTypeToEncoding.put("text/json", JSON);
		ourContentTypeToEncoding.put("text/xml", XML);

		/*
		 * Plain values, used for parameter values
		 */
		ourContentTypeToEncoding.put(JSON_PLAIN_STRING, JSON);
		ourContentTypeToEncoding.put(XML_PLAIN_STRING, XML);

		ourContentTypeToEncodingNonLegacy = Collections.unmodifiableMap(ourContentTypeToEncodingNonLegacy);

	}

	private String myBundleContentType;
	private String myFormatContentType;
	private String myResourceContentTypeNonLegacy;
	private String myResourceContentTypeLegacy;

	EncodingEnum(String theResourceContentTypeLegacy, String theResourceContentType, String theBundleContentType, String theFormatContentType) {
		myResourceContentTypeLegacy = theResourceContentTypeLegacy;
		myResourceContentTypeNonLegacy = theResourceContentType;
		myBundleContentType = theBundleContentType;
		myFormatContentType = theFormatContentType;
	}

	public String getBundleContentType() {
		return myBundleContentType;
	}

	public String getFormatContentType() {
		return myFormatContentType;
	}

	public String getRequestContentType() {
		return myFormatContentType;
	}

	/**
	 * Will return application/xml+fhir style
	 */
	public String getResourceContentType() {
		return myResourceContentTypeLegacy;
	}

	/**
	 * Will return application/fhir+xml style
	 */
	public String getResourceContentTypeNonLegacy() {
		return myResourceContentTypeNonLegacy;
	}

	public abstract IParser newParser(FhirContext theContext);

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

	public static boolean isNonLegacy(String theFormat) {
		return ourContentTypeToEncodingNonLegacy.containsKey(theFormat);
	}


}
