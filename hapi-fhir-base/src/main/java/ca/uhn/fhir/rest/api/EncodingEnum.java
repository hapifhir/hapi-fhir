package ca.uhn.fhir.rest.api;

/*
 * #%L
 * HAPI FHIR - Core Library
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isBlank;

public enum EncodingEnum {

	JSON(Constants.CT_FHIR_JSON, Constants.CT_FHIR_JSON_NEW, Constants.FORMAT_JSON) {
		@Override
		public IParser newParser(FhirContext theContext) {
			return theContext.newJsonParser();
		}
	},

	XML(Constants.CT_FHIR_XML, Constants.CT_FHIR_XML_NEW, Constants.FORMAT_XML) {
		@Override
		public IParser newParser(FhirContext theContext) {
			return theContext.newXmlParser();
		}
	},

	RDF(Constants.CT_RDF_TURTLE_LEGACY, Constants.CT_RDF_TURTLE, Constants.FORMAT_TURTLE) {
		@Override
		public IParser newParser(FhirContext theContext) {
			return theContext.newRDFParser();
		}
	},

        NDJSON(Constants.CT_FHIR_NDJSON, Constants.CT_FHIR_NDJSON, Constants.FORMAT_NDJSON) {
		@Override
		public IParser newParser(FhirContext theContext) {
			return theContext.newNDJsonParser();
		}
	};

	/**
	 * "json"
	 */
	public static final String JSON_PLAIN_STRING = "json";

	/**
	 * "rdf"
	 */
	public static final String RDF_PLAIN_STRING = "rdf";


	/**
	 * "xml"
	 */
	public static final String XML_PLAIN_STRING = "xml";

        /**
         * "ndjson"
         */
        public static final String NDJSON_PLAIN_STRING = "ndjson";
	
	private static Map<String, EncodingEnum> ourContentTypeToEncoding;
	private static Map<String, EncodingEnum> ourContentTypeToEncodingLegacy;
	private static Map<String, EncodingEnum> ourContentTypeToEncodingStrict;

	static {
		ourContentTypeToEncoding = new HashMap<>();
		ourContentTypeToEncodingLegacy = new HashMap<>();

		for (EncodingEnum next : values()) {
			ourContentTypeToEncoding.put(next.myResourceContentTypeNonLegacy, next);
			ourContentTypeToEncoding.put(next.myResourceContentTypeLegacy, next);
			ourContentTypeToEncodingLegacy.put(next.myResourceContentTypeLegacy, next);

			/*
			 * See #346
			 */
			ourContentTypeToEncoding.put(next.myResourceContentTypeNonLegacy.replace('+', ' '), next);
			ourContentTypeToEncoding.put(next.myResourceContentTypeLegacy.replace('+', ' '), next);
			ourContentTypeToEncodingLegacy.put(next.myResourceContentTypeLegacy.replace('+', ' '), next);

		}

		// Add before we add the lenient ones
		ourContentTypeToEncodingStrict = Collections.unmodifiableMap(new HashMap<>(ourContentTypeToEncoding));

		/*
		 * These are wrong, but we add them just to be tolerant of other
		 * people's mistakes
		 */
		ourContentTypeToEncoding.put("application/json", JSON);
		ourContentTypeToEncoding.put("application/xml", XML);
		ourContentTypeToEncoding.put("application/fhir+turtle", RDF);
		ourContentTypeToEncoding.put("application/x-turtle", RDF);
                ourContentTypeToEncoding.put("application/ndjson", NDJSON);
		ourContentTypeToEncoding.put("text/json", JSON);
		ourContentTypeToEncoding.put("text/ndjson", NDJSON);
		ourContentTypeToEncoding.put("text/xml", XML);
		ourContentTypeToEncoding.put("text/turtle", RDF);

		/*
		 * Plain values, used for parameter values
		 */
		ourContentTypeToEncoding.put(JSON_PLAIN_STRING, JSON);
		ourContentTypeToEncoding.put(XML_PLAIN_STRING, XML);
		ourContentTypeToEncoding.put(RDF_PLAIN_STRING, RDF);
		ourContentTypeToEncoding.put(NDJSON_PLAIN_STRING, NDJSON);
		ourContentTypeToEncoding.put(Constants.FORMAT_TURTLE, RDF);

		ourContentTypeToEncodingLegacy = Collections.unmodifiableMap(ourContentTypeToEncodingLegacy);

	}

	private String myFormatContentType;
	private String myResourceContentTypeLegacy;
	private String myResourceContentTypeNonLegacy;

	EncodingEnum(String theResourceContentTypeLegacy, String theResourceContentType, String theFormatContentType) {
		myResourceContentTypeLegacy = theResourceContentTypeLegacy;
		myResourceContentTypeNonLegacy = theResourceContentType;
		myFormatContentType = theFormatContentType;
	}

	/**
	 * Returns <code>xml</code> or <code>json</code> as used on the <code>_format</code> search parameter
	 */
	public String getFormatContentType() {
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

	public abstract IParser newParser(final FhirContext theContext);

	public static EncodingEnum detectEncoding(final String theBody) {
		EncodingEnum retVal = detectEncodingNoDefault(theBody);
		retVal = ObjectUtils.defaultIfNull(retVal, EncodingEnum.XML);
		return retVal;
	}

	public static EncodingEnum detectEncodingNoDefault(String theBody) {
		EncodingEnum retVal = null;
		for (int i = 0; i < theBody.length() && retVal == null; i++) {
			switch (theBody.charAt(i)) {
				case '<':
					retVal = EncodingEnum.XML;
					break;
				case '{':
					retVal = EncodingEnum.JSON;
					break;
			}
		}
		return retVal;
	}

	/**
	 * Returns the encoding for a given content type, or <code>null</code> if no encoding
	 * is found.
	 * <p>
	 * <b>This method is lenient!</b> Things like "application/xml" will return {@link EncodingEnum#XML}
	 * even if the "+fhir" part is missing from the expected content type. Also,
	 * spaces are treated as a plus (i.e. "application/fhir json" will be treated as
	 * "application/fhir+json" in order to account for unescaped spaces in URL
	 * parameters)
	 * </p>
	 */
	public static EncodingEnum forContentType(final String theContentType) {
		String contentTypeSplitted = getTypeWithoutCharset(theContentType);
		if (contentTypeSplitted == null) {
			return null;
		} else {
			return ourContentTypeToEncoding.get(contentTypeSplitted );
		}
	}


	/**
	 * Returns the encoding for a given content type, or <code>null</code> if no encoding
	 * is found.
	 * <p>
	 * <b>This method is NOT lenient!</b> Things like "application/xml" will return <code>null</code>
	 * </p>
	 *
	 * @see #forContentType(String)
	 */
	public static EncodingEnum forContentTypeStrict(final String theContentType) {
		String contentTypeSplitted = getTypeWithoutCharset(theContentType);
		if (contentTypeSplitted == null) {
			return null;
		} else {
			return ourContentTypeToEncodingStrict.get(contentTypeSplitted);
		}
	}

	static String getTypeWithoutCharset(final String theContentType) {
		if (isBlank(theContentType)) {
			return null;
		} else {

			int start = 0;
			for (; start < theContentType.length(); start++) {
				if (theContentType.charAt(start) != ' ') {
					break;
				}
			}
			int end = start;
			for (; end < theContentType.length(); end++) {
				if (theContentType.charAt(end) == ';') {
					break;
				}
			}
			for (; end > start; end--) {
				if (theContentType.charAt(end - 1) != ' ') {
					break;
				}
			}

			String retVal = theContentType.substring(start, end);

			if (retVal.contains(" ")) {
				retVal = retVal.replace(' ', '+');
			}
			return retVal;
		}
	}

	/**
	 * Is the given type a FHIR legacy (pre-DSTU3) content type?
	 */
	public static boolean isLegacy(final String theContentType) {
		String contentTypeSplitted = getTypeWithoutCharset(theContentType);
		if (contentTypeSplitted == null) {
			return false;
		} else {
			return ourContentTypeToEncodingLegacy.containsKey(contentTypeSplitted);
		}
	}


}
