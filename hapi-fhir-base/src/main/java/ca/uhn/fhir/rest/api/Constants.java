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

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.apache.commons.lang3.StringUtils.defaultIfBlank;

public class Constants {

	public static final String CT_TEXT_CSV = "text/csv";
	public static final String HEADER_REQUEST_ID = "X-Request-ID";
	public static final String HEADER_REQUEST_SOURCE = "X-Request-Source";
	public static final String CACHE_CONTROL_MAX_RESULTS = "max-results";
	public static final String CACHE_CONTROL_NO_CACHE = "no-cache";
	public static final String CACHE_CONTROL_NO_STORE = "no-store";
	public static final String CHARSET_NAME_UTF8 = "UTF-8";
	public static final Charset CHARSET_UTF8;
	public static final String CHARSET_UTF8_CTSUFFIX = "; charset=" + CHARSET_NAME_UTF8;
	/**
	 * Contains a standard set of headers which are used by FHIR / HAPI FHIR, and therefore
	 * would make a useful set for CORS AllowedHeader declarations
	 */
	public static final Set<String> CORS_ALLOWED_HEADERS;
	/**
	 * Contains a standard set of HTTP Methods which are used by FHIR / HAPI FHIR, and therefore
	 * would make a useful set for CORS AllowedMethod declarations
	 */
	public static final Set<String> CORS_ALLWED_METHODS;
	public static final String CT_FHIR_JSON = "application/json+fhir";
	public static final String CT_RDF_TURTLE = "application/x-turtle";
	/**
	 * The FHIR MimeType for JSON encoding in FHIR DSTU3+
	 */
	public static final String CT_FHIR_JSON_NEW = "application/fhir+json";
	public static final String CT_FHIR_XML = "application/xml+fhir";


	/**
	 * The FHIR MimeType for XML encoding in FHIR DSTU3+
	 */
	public static final String CT_FHIR_XML_NEW = "application/fhir+xml";
	public static final String CT_HTML = "text/html";
	public static final String CT_HTML_WITH_UTF8 = "text/html" + CHARSET_UTF8_CTSUFFIX;
	public static final String CT_JSON = "application/json";
	public static final String CT_GRAPHQL = "application/graphql";
	public static final String CT_JSON_PATCH = "application/json-patch+json";
	public static final String CT_OCTET_STREAM = "application/octet-stream";
	public static final String CT_TEXT = "text/plain";
	public static final String CT_TEXT_WITH_UTF8 = CT_TEXT + CHARSET_UTF8_CTSUFFIX;
	public static final String CT_TEXT_CDA = "text/xml+cda";
	public static final String CT_X_FORM_URLENCODED = "application/x-www-form-urlencoded";
	public static final String CT_XML = "application/xml";
	public static final String CT_XML_PATCH = "application/xml-patch+xml";
	public static final String ENCODING_GZIP = "gzip";
	public static final String EXTOP_PROCESS_MESSAGE = "$process-message"; //Used in messaging
	public static final String EXTOP_VALIDATE = "$validate";
	public static final String EXTOP_VALIDATE_MODE = "mode";
	public static final String EXTOP_VALIDATE_PROFILE = "profile";
	public static final String EXTOP_VALIDATE_RESOURCE = "resource";
	public static final String FORMAT_HTML = "html";
	public static final String FORMAT_JSON = "json";
	public static final String FORMAT_NDJSON = "ndjson";
	public static final String FORMAT_XML = "xml";
	public static final String CT_RDF_TURTLE_LEGACY = "text/turtle";
	public static final String FORMAT_TURTLE = "ttl";


	/**
	 * "text/html" and "html"
	 */
	public static final Set<String> FORMATS_HTML;
	public static final String FORMATS_HTML_JSON = "html/json";
	public static final String FORMATS_HTML_XML = "html/xml";
	public static final String FORMATS_HTML_TTL = "html/turtle";
	public static final String HEADER_ACCEPT = "Accept";
	public static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
	public static final String HEADER_ACCEPT_VALUE_JSON_NON_LEGACY = CT_FHIR_JSON_NEW + ";q=1.0, " + CT_FHIR_JSON + ";q=0.9";
	public static final String HEADER_ACCEPT_VALUE_XML_NON_LEGACY = CT_FHIR_XML_NEW + ";q=1.0, " + CT_FHIR_XML + ";q=0.9";
	public static final String HEADER_ACCEPT_VALUE_XML_OR_JSON_LEGACY = CT_FHIR_XML + ";q=1.0, " + CT_FHIR_JSON + ";q=1.0";
	public static final String HEADER_ACCEPT_VALUE_XML_OR_JSON_NON_LEGACY = CT_FHIR_XML_NEW + ";q=1.0, " + CT_FHIR_JSON_NEW + ";q=1.0, " + HEADER_ACCEPT_VALUE_XML_OR_JSON_LEGACY.replace("1.0", "0.9");
	public static final String HEADER_ALLOW = "Allow";
	public static final String HEADER_AUTHORIZATION = "Authorization";
	public static final String HEADER_AUTHORIZATION_VALPREFIX_BASIC = "Basic ";
	public static final String HEADER_AUTHORIZATION_VALPREFIX_BEARER = "Bearer ";
	public static final String HEADER_CACHE_CONTROL = "Cache-Control";
	public static final String HEADER_CONTENT_DISPOSITION = "Content-Disposition";
	public static final String HEADER_CONTENT_ENCODING = "Content-Encoding";
	public static final String HEADER_CONTENT_LOCATION = "Content-Location";
	public static final String HEADER_CONTENT_LOCATION_LC = HEADER_CONTENT_LOCATION.toLowerCase();
	public static final String HEADER_CONTENT_TYPE = "Content-Type";
	public static final String HEADER_CONTENT_TYPE_LC = HEADER_CONTENT_TYPE.toLowerCase();
	public static final String HEADER_COOKIE = "Cookie";
	public static final String HEADER_CORS_ALLOW_METHODS = "Access-Control-Allow-Methods";
	public static final String HEADER_CORS_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
	public static final String HEADER_CORS_EXPOSE_HEADERS = "Access-Control-Expose-Headers";
	public static final String HEADER_ETAG = "ETag";
	public static final String HEADER_ETAG_LC = HEADER_ETAG.toLowerCase();
	public static final String HEADER_IF_MATCH = "If-Match";
	public static final String HEADER_IF_MATCH_LC = HEADER_IF_MATCH.toLowerCase();
	public static final String HEADER_IF_MODIFIED_SINCE = "If-Modified-Since";
	public static final String HEADER_IF_MODIFIED_SINCE_LC = HEADER_IF_MODIFIED_SINCE.toLowerCase();
	public static final String HEADER_IF_NONE_EXIST = "If-None-Exist";
	public static final String HEADER_IF_NONE_EXIST_LC = HEADER_IF_NONE_EXIST.toLowerCase();
	public static final String HEADER_IF_NONE_MATCH = "If-None-Match";
	public static final String HEADER_IF_NONE_MATCH_LC = HEADER_IF_NONE_MATCH.toLowerCase();
	public static final String HEADER_LAST_MODIFIED = "Last-Modified";
	public static final String HEADER_LAST_MODIFIED_LOWERCASE = HEADER_LAST_MODIFIED.toLowerCase();
	public static final String HEADER_LOCATION = "Location";
	public static final String HEADER_LOCATION_LC = HEADER_LOCATION.toLowerCase();
	public static final String HEADER_ORIGIN = "Origin";
	public static final String HEADER_PREFER = "Prefer";
	public static final String HEADER_PREFER_HANDLING = "handling";
	public static final String HEADER_PREFER_HANDLING_STRICT = "strict";
	public static final String HEADER_PREFER_HANDLING_LENIENT = "lenient";
	public static final String HEADER_PREFER_RETURN = "return";
	public static final String HEADER_PREFER_RETURN_MINIMAL = "minimal";
	public static final String HEADER_PREFER_RETURN_REPRESENTATION = "representation";
	public static final String HEADER_PREFER_RETURN_OPERATION_OUTCOME = "OperationOutcome";
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
	public static final String PARAM_ASYNC = "async"; //Used in messaging
	public static final String PARAM_AT = "_at";
	public static final String PARAM_ID= "_id";
	/**
	 * Used in paging links
	 */
	public static final String PARAM_BUNDLETYPE = "_bundletype";
	public static final String PARAM_FILTER = "_filter";
	public static final String PARAM_CONTAINED = "_contained";
	public static final String PARAM_CONTAINED_TYPE = "_containedType";
	public static final String PARAM_CONTENT = "_content";
	public static final String PARAM_COUNT = "_count";
	public static final String PARAM_OFFSET = "_offset";
	public static final String PARAM_DELETE = "_delete";
	public static final String PARAM_ELEMENTS = "_elements";
	public static final String PARAM_ELEMENTS_EXCLUDE_MODIFIER = ":exclude";
	public static final String PARAM_FORMAT = "_format";
	public static final String PARAM_HAS = "_has";
	public static final String PARAM_HISTORY = "_history";
	public static final String PARAM_INCLUDE = "_include";
	public static final String PARAM_INCLUDE_QUALIFIER_RECURSE = ":recurse";
	public static final String PARAM_INCLUDE_RECURSE = "_include" + PARAM_INCLUDE_QUALIFIER_RECURSE;
	public static final String PARAM_INCLUDE_QUALIFIER_ITERATE = ":iterate";
	public static final String PARAM_INCLUDE_ITERATE = "_include" + PARAM_INCLUDE_QUALIFIER_ITERATE;
	public static final String PARAM_LASTUPDATED = "_lastUpdated";
	public static final String PARAM_NARRATIVE = "_narrative";
	public static final String PARAM_PAGINGACTION = "_getpages";
	public static final String PARAM_PAGINGOFFSET = "_getpagesoffset";
	public static final String PARAM_PRETTY = "_pretty";
	public static final String PARAM_PRETTY_VALUE_FALSE = "false";
	public static final String PARAM_PRETTY_VALUE_TRUE = "true";
	public static final String PARAM_PROFILE = "_profile";
	public static final String PARAM_QUERY = "_query";
	public static final String PARAM_RESPONSE_URL = "response-url"; //Used in messaging
	public static final String PARAM_REVINCLUDE = "_revinclude";
	public static final String PARAM_REVINCLUDE_RECURSE = PARAM_REVINCLUDE + PARAM_INCLUDE_QUALIFIER_RECURSE;
	public static final String PARAM_REVINCLUDE_ITERATE = PARAM_REVINCLUDE + PARAM_INCLUDE_QUALIFIER_ITERATE;
	public static final String PARAM_SEARCH = "_search";
	public static final String PARAM_SECURITY = "_security";
	public static final String PARAM_SINCE = "_since";
	public static final String PARAM_SORT = "_sort";
	public static final String PARAM_SORT_ASC = "_sort:asc";
	public static final String PARAM_SORT_DESC = "_sort:desc";
	public static final String PARAM_SOURCE = "_source";
	public static final String PARAM_SUMMARY = "_summary";
	public static final String PARAM_TAG = "_tag";
	public static final String PARAM_LIST = "_list";
	public static final String PARAM_TAGS = "_tags";
	public static final String PARAM_TEXT = "_text";
	public static final String PARAM_VALIDATE = "_validate";

	/**
	 * $member-match operation
	 */
	public static final String PARAM_MEMBER_PATIENT = "MemberPatient";
	public static final String PARAM_OLD_COVERAGE = "OldCoverage";
	public static final String PARAM_NEW_COVERAGE = "NewCoverage";

	public static final String PARAMQUALIFIER_MISSING = ":missing";
	public static final String PARAMQUALIFIER_MISSING_FALSE = "false";
	public static final String PARAMQUALIFIER_MISSING_TRUE = "true";
	public static final String PARAMQUALIFIER_STRING_CONTAINS = ":contains";
	public static final String PARAMQUALIFIER_STRING_EXACT = ":exact";
	public static final String PARAMQUALIFIER_TOKEN_TEXT = ":text";
	public static final String PARAMQUALIFIER_MDM = ":mdm";
	public static final String PARAMQUALIFIER_NICKNAME = ":nickname";
	public static final String PARAMQUALIFIER_TOKEN_OF_TYPE = ":of-type";
	public static final String PARAMQUALIFIER_TOKEN_NOT = ":not";
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
	public static final String TAG_SUBSETTED_SYSTEM_DSTU3 = "http://hl7.org/fhir/v3/ObservationValue";
	public static final String TAG_SUBSETTED_SYSTEM_R4 = "http://terminology.hl7.org/CodeSystem/v3-ObservationValue";
	public static final String URL_TOKEN_HISTORY = "_history";
	public static final String URL_TOKEN_METADATA = "metadata";
	public static final String OO_INFOSTATUS_PROCESSING = "processing";
	public static final String PARAM_GRAPHQL_QUERY = "query";
	public static final String HEADER_X_CACHE = "X-Cache";
	public static final String HEADER_X_SECURITY_CONTEXT = "X-Security-Context";
	public static final String POWERED_BY_HEADER = "X-Powered-By";
	public static final Charset CHARSET_US_ASCII;
	public static final String PARAM_PAGEID = "_pageId";
	/**
	 * This is provided for testing only! Use with caution as this property may change.
	 */
	public static final String TEST_SYSTEM_PROP_VALIDATION_RESOURCE_CACHES_MS = "TEST_SYSTEM_PROP_VALIDATION_RESOURCE_CACHES_MS";
	public static final String PARAM_SEARCH_TOTAL_MODE = "_total";
	public static final String CAPABILITYSTATEMENT_WEBSOCKET_URL = "http://hl7.org/fhir/StructureDefinition/capabilitystatement-websocket";
	public static final String PARAMETER_CASCADE_DELETE = "_cascade";
	public static final String HEADER_CASCADE = "X-Cascade";
	public static final String CASCADE_DELETE = "delete";
	public static final int MAX_RESOURCE_NAME_LENGTH = 100;
	public static final String CACHE_CONTROL_PRIVATE = "private";
	public static final String CT_FHIR_NDJSON = "application/fhir+ndjson";
	public static final String CT_APP_NDJSON = "application/ndjson";
	public static final String CT_NDJSON = "ndjson";
	public static final Set<String> CTS_NDJSON;
	public static final String HEADER_PREFER_RESPOND_ASYNC = "respond-async";
	public static final int STATUS_HTTP_412_PAYLOAD_TOO_LARGE = 413;
	public static final String OPERATION_NAME_GRAPHQL = "$graphql";
	/**
	 * Note that this constant is used in a number of places including DB column lengths! Be careful if you decide to change it.
	 */
	public static final int REQUEST_ID_LENGTH = 16;
	public static final int STATUS_HTTP_202_ACCEPTED = 202;
	public static final String HEADER_X_PROGRESS = "X-Progress";
	public static final String HEADER_RETRY_AFTER = "Retry-After";
	/**
	 * Operation name for the $lastn operation
	 */
	public static final String OPERATION_LASTN = "$lastn";
	public static final String PARAM_FHIRPATH = "_fhirpath";
	public static final String PARAM_TYPE = "_type";

	/**
	 * {@link org.hl7.fhir.instance.model.api.IBaseResource#getUserData(String) User metadata key} used
	 * to store the partition ID (if any) associated with the given resource. Value for this
	 * key will be of type {@link ca.uhn.fhir.interceptor.model.RequestPartitionId}.
	 */
	public static final String RESOURCE_PARTITION_ID = Constants.class.getName() + "_RESOURCE_PARTITION_ID";
	public static final String PARTITION_IDS = "partitionIds";
	public static final String CT_APPLICATION_GZIP = "application/gzip";
	public static final String[] EMPTY_STRING_ARRAY = new String[0];
	public static final String SUBSCRIPTION_MULTITYPE_PREFIX = "[";
	public static final String SUBSCRIPTION_MULTITYPE_SUFFIX = "]";
	public static final String SUBSCRIPTION_MULTITYPE_STAR = "*";
	public static final String SUBSCRIPTION_STAR_CRITERIA = SUBSCRIPTION_MULTITYPE_PREFIX + SUBSCRIPTION_MULTITYPE_STAR + SUBSCRIPTION_MULTITYPE_SUFFIX;
	public static final String INCLUDE_STAR = "*";
	public static final String PARAMQUALIFIER_TOKEN_IN = ":in";
	public static final String PARAMQUALIFIER_TOKEN_NOT_IN = ":not-in";
	public static final String PARAMQUALIFIER_TOKEN_ABOVE = ":above";
	public static final String PARAMQUALIFIER_TOKEN_BELOW = ":below";

	static {
		CHARSET_UTF8 = StandardCharsets.UTF_8;
		CHARSET_US_ASCII = StandardCharsets.ISO_8859_1;

		HashSet<String> ctsNdjson = new HashSet<>();
		ctsNdjson.add(CT_FHIR_NDJSON);
		ctsNdjson.add(CT_APP_NDJSON);
		ctsNdjson.add(CT_NDJSON);
		CTS_NDJSON = Collections.unmodifiableSet(ctsNdjson);

		HashMap<Integer, String> statusNames = new HashMap<>();
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
		statusNames.put(414, "URI Too Long");
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

		Set<String> formatsHtml = new HashSet<>();
		formatsHtml.add(CT_HTML);
		formatsHtml.add(FORMAT_HTML);
		FORMATS_HTML = Collections.unmodifiableSet(formatsHtml);

		// *********************************************************
		// Update CorsInterceptor's constructor documentation if you change these:
		// *********************************************************
		HashSet<String> corsAllowedHeaders = new HashSet<>();
		corsAllowedHeaders.add("Accept");
		corsAllowedHeaders.add("Access-Control-Request-Headers");
		corsAllowedHeaders.add("Access-Control-Request-Method");
		corsAllowedHeaders.add("Cache-Control");
		corsAllowedHeaders.add("Content-Type");
		corsAllowedHeaders.add("Origin");
		corsAllowedHeaders.add("Prefer");
		corsAllowedHeaders.add("X-FHIR-Starter");
		corsAllowedHeaders.add("X-Requested-With");
		CORS_ALLOWED_HEADERS = Collections.unmodifiableSet(corsAllowedHeaders);

		// *********************************************************
		// Update CorsInterceptor's constructor documentation if you change these:
		// *********************************************************
		HashSet<String> corsAllowedMethods = new HashSet<>();
		corsAllowedMethods.addAll(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
		CORS_ALLWED_METHODS = Collections.unmodifiableSet(corsAllowedMethods);
	}

	public static String codeSystemWithDefaultDescription(String theSystem) {
		return defaultIfBlank(theSystem, "(none)");
	}
}
