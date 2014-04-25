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

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Constants {

	public static final String CT_FHIR_JSON = "application/json+fhir";
	public static final String CT_FHIR_XML = "application/xml+fhir";
	public static final String PARAM_FORMAT = "_format";
	public static final String URL_TOKEN_HISTORY = "_history";
	public static final String CT_ATOM_XML = "application/atom+xml";
	public static final Set<String> FORMAT_VAL_XML;
	public static final Set<String> FORMAT_VAL_JSON;
	public static final Map<String, EncodingUtil> FORMAT_VAL_TO_ENCODING;
	public static final String CT_XML = "application/xml";
	public static final String CT_JSON = "application/json";
	public static final String CT_HTML = "text/html";
	public static final String PARAM_NARRATIVE = "_narrative";
	public  static final String PARAM_HISTORY = "_history";
	public static final String PARAM_PRETTY = "_pretty";
	public static final String PARAM_QUERY = "_query";
	public static final int STATUS_HTTP_201_CREATED = 201;
	public static final String CT_TEXT = "text/plain";
	public static final int STATUS_HTTP_200_OK = 200;
	public static final int STATUS_HTTP_422_UNPROCESSABLE_ENTITY = 422;
	public static final int STATUS_HTTP_404_NOT_FOUND = 404;
	public static final int STATUS_HTTP_400_BAD_REQUEST = 400;
	public static final int STATUS_HTTP_405_METHOD_NOT_ALLOWED = 405;
	public static final int STATUS_HTTP_409_CONFLICT = 409;
	public static final int STATUS_HTTP_412_PRECONDITION_FAILED = 412;
	public static final String HEADER_CONTENT_LOCATION = "Content-Location";
	public static final int STATUS_HTTP_204_NO_CONTENT = 204;
	public static final String PARAM_COUNT = "_count";
	public static final String PARAM_SINCE = "_since";
	public static final String PARAM_SEARCH = "_search";
	public static final String HEADER_LAST_MODIFIED = "Last-Modified";
	public static final String HEADER_LAST_MODIFIED_LOWERCASE = HEADER_LAST_MODIFIED.toLowerCase();
	public static final String PARAM_VALIDATE = "_validate";

	static {
		Map<String, EncodingUtil> valToEncoding = new HashMap<String, EncodingUtil>();
		
		HashSet<String> valXml = new HashSet<String>();
		valXml.add(CT_FHIR_XML);
		valXml.add("application/xml");
		valXml.add("xml");
		FORMAT_VAL_XML = Collections.unmodifiableSet(valXml);
		for (String string : valXml) {
			valToEncoding.put(string, EncodingUtil.XML);
		}

		HashSet<String> valJson = new HashSet<String>();
		valJson.add(CT_FHIR_JSON);
		valJson.add("application/json");
		valJson.add("json");
		FORMAT_VAL_JSON = Collections.unmodifiableSet(valJson);
		for (String string : valJson) {
			valToEncoding.put(string, EncodingUtil.JSON);
		}

		FORMAT_VAL_TO_ENCODING=Collections.unmodifiableMap(valToEncoding);
	}

}
