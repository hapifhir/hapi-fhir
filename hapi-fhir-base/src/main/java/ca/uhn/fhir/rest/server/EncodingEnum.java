package ca.uhn.fhir.rest.server;

/*
 * #%L
 * HAPI FHIR Library
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

import java.util.HashMap;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;

public enum EncodingEnum {

	XML(Constants.CT_FHIR_XML, Constants.CT_ATOM_XML, Constants.CT_XML) {
		@Override
		public IParser newParser(FhirContext theContext) {
			return theContext.newXmlParser();
		}
	},

	JSON(Constants.CT_FHIR_JSON, Constants.CT_FHIR_JSON, Constants.CT_JSON) {
		@Override
		public IParser newParser(FhirContext theContext) {
			return theContext.newJsonParser();
		}
	}
	
	;

	private static HashMap<String, EncodingEnum> ourContentTypeToEncoding;

	static {
		ourContentTypeToEncoding = new HashMap<String, EncodingEnum>();
		for (EncodingEnum next: values()) {
			ourContentTypeToEncoding.put(next.getBundleContentType(), next);
			ourContentTypeToEncoding.put(next.getResourceContentType(), next);
			ourContentTypeToEncoding.put(next.getBrowserFriendlyBundleContentType(), next);
		}
	}
	
	private String myResourceContentType;
	private String myBundleContentType;
	private String myBrowserFriendlyContentType;
	
	EncodingEnum(String theResourceContentType, String theBundleContentType, String theBrowserFriendlyContentType) {
		myResourceContentType = theResourceContentType;
		myBundleContentType = theBundleContentType;
		myBrowserFriendlyContentType = theBrowserFriendlyContentType;
	}

	public abstract IParser newParser(FhirContext theContext);
	
	public String getBundleContentType() {
		return myBundleContentType;
	}

	public String getResourceContentType() {
		return myResourceContentType;
	}

	public String getBrowserFriendlyBundleContentType() {
		return myBrowserFriendlyContentType;
	}

	public static EncodingEnum forContentType(String theContentType) {
		return ourContentTypeToEncoding.get(theContentType);
	}


}
