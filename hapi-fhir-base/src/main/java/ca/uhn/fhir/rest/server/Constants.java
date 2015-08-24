package ca.uhn.fhir.rest.server;

/*
 * #%L
 * HAPI FHIR - Core Library
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

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Constants {

	public static final String CHARSET_NAME_UTF8 = "UTF-8";
	public static final Charset CHARSET_UTF8;
	public static final String CHARSET_UTF8_CTSUFFIX = "; charset=" + CHARSET_NAME_UTF8;
	public static final String CT_ATOM_XML = "application/atom+xml";
	public static final String CT_FHIR_JSON = "application/json+fhir";
	public static final String CT_FHIR_XML = "application/xml+fhir";
	public static final String CT_HTML = "text/html";
	public static final String CT_HTML_WITH_UTF8 = "text/html" + CHARSET_UTF8_CTSUFFIX;
	public static final String CT_JSON = "application/json";
	public static final String CT_OCTET_STREAM = "application/octet-stream";
	public static final String CT_TEXT = "text/plain";
	public static final String CT_TEXT_WITH_UTF8 = CT_TEXT + CHARSET_UTF8_CTSUFFIX;
	public static final String CT_XML = "application/xml";
	public static final String ENCODING_GZIP = "gzip";
	public static final String EXTOP_VALIDATE = "$validate";
	public static final String EXTOP_VALIDATE_MODE = "mode";
	public static final String EXTOP_VALIDATE_PROFILE = "profile";
	public static final String EXTOP_VALIDATE_RESOURCE = "resource";
	public static final String FORMAT_JSON = "json";
	public static final Set<String> FORMAT_VAL_JSON;
	public static final Map<String, EncodingEnum> FORMAT_VAL_TO_ENCODING;
	public static final Set<String> FORMAT_VAL_XML;
	public static final String FORMAT_XML = "xml";
	public static final String HEADER_ACCEPT = "Accept";
	public static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
	public static final String HEADER_ALLOW = "Allow";
	public static final String HEADER_AUTHORIZATION = "Authorization";
	public static final String HEADER_AUTHORIZATION_VALPREFIX_BASIC = "Basic ";
	public static final String HEADER_AUTHORIZATION_VALPREFIX_BEARER = "Bearer ";
	public static final String HEADER_CATEGORY = "Category";
	public static final String HEADER_CATEGORY_LC = HEADER_CATEGORY.toLowerCase();
	public static final String HEADER_CONTENT_DISPOSITION = "Content-Disposition";
	public static final String HEADER_CONTENT_ENCODING = "Content-Encoding";
	public static final String HEADER_CONTENT_LOCATION = "Content-Location";
	public static final String HEADER_CONTENT_LOCATION_LC = HEADER_CONTENT_LOCATION.toLowerCase();
	public static final String HEADER_CONTENT_TYPE = "Content-Type";
	public static final String HEADER_COOKIE = "Cookie";
	public static final String HEADER_CORS_ALLOW_METHODS = "Access-Control-Allow-Methods";
	public static final String HEADER_CORS_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
	public static final String HEADER_CORS_EXPOSE_HEADERS = "Access-Control-Expose-Headers";
	public static final String HEADER_ETAG = "ETag";
	public static final String HEADER_ETAG_LC = HEADER_ETAG.toLowerCase();
	public static final String HEADER_IF_MATCH = "If-Match";
	public static final String HEADER_IF_MATCH_LC = HEADER_IF_MATCH.toLowerCase();
	public static final String HEADER_IF_NONE_EXIST = "If-None-Exist";
	public static final String HEADER_IF_NONE_EXIST_LC = HEADER_IF_NONE_EXIST.toLowerCase();
	public static final String HEADER_IF_NONE_MATCH = "If-None-Match";
	public static final String HEADER_IF_NONE_MATCH_LC = HEADER_IF_NONE_MATCH.toLowerCase();
	public static final String HEADER_LAST_MODIFIED = "Last-Modified";
	public static final String HEADER_LAST_MODIFIED_LOWERCASE = HEADER_LAST_MODIFIED.toLowerCase();
	public static final String HEADER_LOCATION = "Location";
	public static final String HEADER_LOCATION_LC = HEADER_LOCATION.toLowerCase();
	public static final String HEADER_PREFER = "Prefer";
	public static final String HEADER_PREFER_RETURN = "return";
	public static final String HEADER_PREFER_RETURN_MINIMAL = "minimal";
	public static final String HEADER_PREFER_RETURN_REPRESENTATION = "representation";
	public static final String HEADER_SUFFIX_CT_UTF_8 = "; charset=UTF-8";
	public static final String HEADERVALUE_CORS_ALLOW_METHODS_ALL = "GET, POST, PUT, DELETE, OPTIONS";
	public static final Map<Integer, String> HTTP_STATUS_NAMES;
	public static final String LINK_FHIR_BASE = "fhir-base";
	public static final String LINK_FIRST = "first";
	public static final String LINK_LAST = "last";
	public static final String LINK_NEXT = "next";
	public static final String LINK_PREVIOUS = "previous";
	public static final String LINK_SELF = "self";
	public static final String OPENSEARCH_NS_OLDER = "http://purl.org/atompub/tombstones/1.0";
	public static final String PARAM_COUNT = "_count";
	public static final String PARAM_DELETE = "_delete";
	public static final String PARAM_ELEMENTS = "_elements";
	public static final String PARAM_FORMAT = "_format";
	public static final String PARAM_HISTORY = "_history";
	public static final String PARAM_INCLUDE = "_include";
	public static final String PARAM_LASTUPDATED = "_lastUpdated";
	public static final String PARAM_NARRATIVE = "_narrative";
	public static final String PARAM_PAGINGACTION = "_getpages";
	public static final String PARAM_PAGINGOFFSET = "_getpagesoffset";
	public static final String PARAM_PRETTY = "_pretty";
	public static final String PARAM_PRETTY_VALUE_TRUE = "true";
	public static final String PARAM_PROFILE = "_profile";
	public static final String PARAM_QUERY = "_query";
	public static final String PARAM_REVINCLUDE = "_revinclude";
	public static final String PARAM_SEARCH = "_search";
	public static final String PARAM_SECURITY = "_security";
	public static final String PARAM_SINCE = "_since";
	public static final String PARAM_SORT = "_sort";
	public static final String PARAM_SORT_ASC = "_sort:asc";
	public static final String PARAM_SORT_DESC = "_sort:desc";
	public static final String PARAM_SUMMARY = "_summary";
	public static final String PARAM_TAG = "_tag";
	public static final String PARAM_TAGS = "_tags";
	public static final String PARAM_VALIDATE = "_validate";
	public static final String PARAMQUALIFIER_MISSING = ":missing";
	public static final String PARAMQUALIFIER_MISSING_FALSE = "false";
	public static final String PARAMQUALIFIER_MISSING_TRUE = "true";
	public static final String PARAMQUALIFIER_STRING_EXACT = ":exact";
	public static final String PARAMQUALIFIER_TOKEN_TEXT = ":text";
	public static final int STATUS_HTTP_200_OK = 200;
	public static final int STATUS_HTTP_201_CREATED = 201;
	public static final int STATUS_HTTP_204_NO_CONTENT = 204;
	public static final int STATUS_HTTP_304_NOT_MODIFIED = 304;
	public static final int STATUS_HTTP_400_BAD_REQUEST = 400;
	public static final int STATUS_HTTP_401_CLIENT_UNAUTHORIZED = 401;
	public static final int STATUS_HTTP_403_FORBIDDEN = 403;
	public static final int STATUS_HTTP_404_NOT_FOUND = 404;
	public static final int STATUS_HTTP_405_METHOD_NOT_ALLOWED = 405;
	public static final int STATUS_HTTP_409_CONFLICT = 409;
	public static final int STATUS_HTTP_410_GONE = 410;
	public static final int STATUS_HTTP_412_PRECONDITION_FAILED = 412;
	public static final int STATUS_HTTP_422_UNPROCESSABLE_ENTITY = 422;
	public static final int STATUS_HTTP_500_INTERNAL_ERROR = 500;
	public static final int STATUS_HTTP_501_NOT_IMPLEMENTED = 501;
	public static final String TAG_SUBSETTED_CODE = "SUBSETTED";
	public static final String TAG_SUBSETTED_SYSTEM = "http://hl7.org/fhir/v3/ObservationValue";
	public static final String URL_TOKEN_HISTORY = "_history";
	public static final String URL_TOKEN_METADATA = "metadata";

	static {
		Map<String, EncodingEnum> valToEncoding = new HashMap<String, EncodingEnum>();

		HashSet<String> valXml = new HashSet<String>();
		valXml.add(CT_FHIR_XML);
		valXml.add("application/xml");
		valXml.add("xml");
		FORMAT_VAL_XML = Collections.unmodifiableSet(valXml);
		for (String string : valXml) {
			valToEncoding.put(string, EncodingEnum.XML);
		}

		HashSet<String> valJson = new HashSet<String>();
		valJson.add(CT_FHIR_JSON);
		valJson.add("application/json");
		valJson.add("json");
		FORMAT_VAL_JSON = Collections.unmodifiableSet(valJson);
		for (String string : valJson) {
			valToEncoding.put(string, EncodingEnum.JSON);
		}

		FORMAT_VAL_TO_ENCODING = Collections.unmodifiableMap(valToEncoding);

		CHARSET_UTF8 = Charset.forName(CHARSET_NAME_UTF8);

		HashMap<Integer, String> statusNames = new HashMap<Integer, String>();

		statusNames.put(200, "OK");
		statusNames.put(201, "Created");
		statusNames.put(202, "Accepted");
		statusNames.put(203, "Non-Authoritative Information");
		statusNames.put(204, "No Content");
		statusNames.put(205, "Reset Content");
		statusNames.put(206, "Partial Content");
		statusNames.put(207, "Multi-Status");
		statusNames.put(208, "Already Reported");
		statusNames.put(226, "IM Used");
		statusNames.put(300, "Multiple Choices");
		statusNames.put(301, "Moved Permanently");
		statusNames.put(302, "Found");
		statusNames.put(302, "Moved Temporarily");
		statusNames.put(303, "See Other");
		statusNames.put(304, "Not Modified");
		statusNames.put(305, "Use Proxy");
		statusNames.put(307, "Temporary Redirect");
		statusNames.put(308, "Permanent Redirect");
		statusNames.put(400, "Bad Request");
		statusNames.put(401, "Unauthorized");
		statusNames.put(402, "Payment Required");
		statusNames.put(403, "Forbidden");
		statusNames.put(404, "Not Found");
		statusNames.put(405, "Method Not Allowed");
		statusNames.put(406, "Not Acceptable");
		statusNames.put(407, "Proxy Authentication Required");
		statusNames.put(408, "Request Timeout");
		statusNames.put(409, "Conflict");
		statusNames.put(410, "Gone");
		statusNames.put(411, "Length Required");
		statusNames.put(412, "Precondition Failed");
		statusNames.put(413, "Payload Too Large");
		statusNames.put(413, "Request Entity Too Large");
		statusNames.put(414, "URI Too Long");
		statusNames.put(414, "Request-URI Too Long");
		statusNames.put(415, "Unsupported Media Type");
		statusNames.put(416, "Requested range not satisfiable");
		statusNames.put(417, "Expectation Failed");
		statusNames.put(418, "I'm a teapot");
		statusNames.put(419, "Insufficient Space On Resource");
		statusNames.put(420, "Method Failure");
		statusNames.put(421, "Destination Locked");
		statusNames.put(422, "Unprocessable Entity");
		statusNames.put(423, "Locked");
		statusNames.put(424, "Failed Dependency");
		statusNames.put(426, "Upgrade Required");
		statusNames.put(428, "Precondition Required");
		statusNames.put(429, "Too Many Requests");
		statusNames.put(431, "Request Header Fields Too Large");
		statusNames.put(500, "Internal Server Error");
		statusNames.put(501, "Not Implemented");
		statusNames.put(502, "Bad Gateway");
		statusNames.put(503, "Service Unavailable");
		statusNames.put(504, "Gateway Timeout");
		statusNames.put(505, "HTTP Version not supported");
		statusNames.put(506, "Variant Also Negotiates");
		statusNames.put(507, "Insufficient Storage");
		statusNames.put(508, "Loop Detected");
		statusNames.put(509, "Bandwidth Limit Exceeded");
		statusNames.put(510, "Not Extended");
		statusNames.put(511, "Network Authentication Required");
		HTTP_STATUS_NAMES = Collections.unmodifiableMap(statusNames);
	}

}
