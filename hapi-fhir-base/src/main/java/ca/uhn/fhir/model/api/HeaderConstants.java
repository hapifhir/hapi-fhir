/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.model.api;

// Created by Claude Sonnet 4
/**
 * Centralized header constants for HAPI FHIR.
 * 
 * This class consolidates header keys used throughout the HAPI FHIR framework,
 * organized by functional category. This replaces scattered header definitions
 * and provides a single source of truth for header naming conventions.
 * 
 * @since 7.2.0
 */
public final class HeaderConstants {

	/**
	 * Private constructor to prevent instantiation of utility class.
	 */
	private HeaderConstants() {
		// Utility class
	}

	// =================================================================================================================
	// AUTHENTICATION & AUTHORIZATION HEADERS
	// =================================================================================================================

	/**
	 * Standard HTTP Authorization header.
	 * @see ca.uhn.fhir.rest.api.Constants#HEADER_AUTHORIZATION
	 */
	public static final String AUTHORIZATION = "Authorization";

	/**
	 * Bearer token prefix for Authorization header.
	 * @see ca.uhn.fhir.rest.api.Constants#HEADER_AUTHORIZATION_VALPREFIX_BEARER
	 */
	public static final String AUTHORIZATION_BEARER_PREFIX = "Bearer ";

	/**
	 * Basic auth prefix for Authorization header.
	 * @see ca.uhn.fhir.rest.api.Constants#HEADER_AUTHORIZATION_VALPREFIX_BASIC
	 */
	public static final String AUTHORIZATION_BASIC_PREFIX = "Basic ";

	/**
	 * Security context header for passing security information.
	 * @see ca.uhn.fhir.rest.api.Constants#HEADER_X_SECURITY_CONTEXT
	 */
	public static final String X_SECURITY_CONTEXT = "X-Security-Context";

	// =================================================================================================================
	// CONTENT NEGOTIATION & FHIR HEADERS
	// =================================================================================================================

	/**
	 * Standard HTTP Accept header for content negotiation.
	 * @see ca.uhn.fhir.rest.api.Constants#HEADER_ACCEPT
	 */
	public static final String ACCEPT = "Accept";

	/**
	 * Standard HTTP Content-Type header.
	 * @see ca.uhn.fhir.rest.api.Constants#HEADER_CONTENT_TYPE
	 */
	public static final String CONTENT_TYPE = "Content-Type";

	/**
	 * FHIR Prefer header for client preferences.
	 * @see ca.uhn.fhir.rest.api.Constants#HEADER_PREFER
	 */
	public static final String PREFER = "Prefer";

	/**
	 * Standard HTTP Accept-Encoding header.
	 * @see ca.uhn.fhir.rest.api.Constants#HEADER_ACCEPT_ENCODING
	 */
	public static final String ACCEPT_ENCODING = "Accept-Encoding";

	/**
	 * Standard HTTP Content-Encoding header.
	 * @see ca.uhn.fhir.rest.api.Constants#HEADER_CONTENT_ENCODING
	 */
	public static final String CONTENT_ENCODING = "Content-Encoding";

	// =================================================================================================================
	// CONDITIONAL REQUEST HEADERS
	// =================================================================================================================

	/**
	 * HTTP If-Match header for conditional requests.
	 * @see ca.uhn.fhir.rest.api.Constants#HEADER_IF_MATCH
	 */
	public static final String IF_MATCH = "If-Match";

	/**
	 * HTTP If-None-Match header for conditional requests.
	 * @see ca.uhn.fhir.rest.api.Constants#HEADER_IF_NONE_MATCH
	 */
	public static final String IF_NONE_MATCH = "If-None-Match";

	/**
	 * HTTP If-Modified-Since header for conditional requests.
	 * @see ca.uhn.fhir.rest.api.Constants#HEADER_IF_MODIFIED_SINCE
	 */
	public static final String IF_MODIFIED_SINCE = "If-Modified-Since";

	/**
	 * FHIR If-None-Exist header for conditional creates.
	 * @see ca.uhn.fhir.rest.api.Constants#HEADER_IF_NONE_EXIST
	 */
	public static final String IF_NONE_EXIST = "If-None-Exist";

	// =================================================================================================================
	// RESOURCE VERSIONING & METADATA HEADERS
	// =================================================================================================================

	/**
	 * Standard HTTP ETag header for version control.
	 * @see ca.uhn.fhir.rest.api.Constants#HEADER_ETAG
	 */
	public static final String ETAG = "ETag";

	/**
	 * Standard HTTP Last-Modified header.
	 * @see ca.uhn.fhir.rest.api.Constants#HEADER_LAST_MODIFIED
	 */
	public static final String LAST_MODIFIED = "Last-Modified";

	/**
	 * Standard HTTP Location header for resource location.
	 * @see ca.uhn.fhir.rest.api.Constants#HEADER_LOCATION
	 */
	public static final String LOCATION = "Location";

	/**
	 * Content-Location header for content location.
	 * @see ca.uhn.fhir.rest.api.Constants#HEADER_CONTENT_LOCATION
	 */
	public static final String CONTENT_LOCATION = "Content-Location";

	// =================================================================================================================
	// CACHING & PERFORMANCE HEADERS
	// =================================================================================================================

	/**
	 * Standard HTTP Cache-Control header.
	 * @see ca.uhn.fhir.rest.api.Constants#HEADER_CACHE_CONTROL
	 */
	public static final String CACHE_CONTROL = "Cache-Control";

	/**
	 * Custom X-Cache header for cache status.
	 * @see ca.uhn.fhir.rest.api.Constants#HEADER_X_CACHE
	 */
	public static final String X_CACHE = "X-Cache";

	// =================================================================================================================
	// TRACING & REQUEST CORRELATION HEADERS
	// =================================================================================================================

	/**
	 * Request ID header for request correlation.
	 * @see ca.uhn.fhir.rest.api.Constants#HEADER_REQUEST_ID
	 */
	public static final String X_REQUEST_ID = "X-Request-ID";

	/**
	 * Request source header for identifying request origin.
	 * @see ca.uhn.fhir.rest.api.Constants#HEADER_REQUEST_SOURCE
	 */
	public static final String X_REQUEST_SOURCE = "X-Request-Source";

	/**
	 * Progress header for async operations.
	 * @see ca.uhn.fhir.rest.api.Constants#HEADER_X_PROGRESS
	 */
	public static final String X_PROGRESS = "X-Progress";

	// =================================================================================================================
	// CORS HEADERS
	// =================================================================================================================

	/**
	 * CORS Access-Control-Allow-Origin header.
	 * @see ca.uhn.fhir.rest.api.Constants#HEADER_CORS_ALLOW_ORIGIN
	 */
	public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";

	/**
	 * CORS Access-Control-Allow-Methods header.
	 * @see ca.uhn.fhir.rest.api.Constants#HEADER_CORS_ALLOW_METHODS
	 */
	public static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";

	/**
	 * CORS Access-Control-Expose-Headers header.
	 * @see ca.uhn.fhir.rest.api.Constants#HEADER_CORS_EXPOSE_HEADERS
	 */
	public static final String ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";

	/**
	 * Standard Origin header.
	 * @see ca.uhn.fhir.rest.api.Constants#HEADER_ORIGIN
	 */
	public static final String ORIGIN = "Origin";

	// =================================================================================================================
	// SERVER IDENTIFICATION HEADERS
	// =================================================================================================================

	/**
	 * Server identification header.
	 * @see ca.uhn.fhir.rest.api.Constants#POWERED_BY_HEADER
	 */
	public static final String X_POWERED_BY = "X-Powered-By";

	// =================================================================================================================
	// RETRY & ERROR HANDLING HEADERS
	// =================================================================================================================

	/**
	 * Retry-After header for rate limiting and async operations.
	 * @see ca.uhn.fhir.rest.api.Constants#HEADER_RETRY_AFTER
	 */
	public static final String RETRY_AFTER = "Retry-After";

	/**
	 * History rewrite header for version conflict handling.
	 * @see ca.uhn.fhir.rest.api.Constants#HEADER_REWRITE_HISTORY
	 */
	public static final String X_REWRITE_HISTORY = "X-Rewrite-History";

	/**
	 * Retry on version conflict header.
	 * @see ca.uhn.fhir.rest.api.Constants#HEADER_RETRY_ON_VERSION_CONFLICT
	 */
	public static final String X_RETRY_ON_VERSION_CONFLICT = "X-Retry-On-Version-Conflict";

	// =================================================================================================================
	// BULK OPERATIONS & CASCADE HEADERS
	// =================================================================================================================

	/**
	 * Cascade operations header.
	 * @see ca.uhn.fhir.rest.api.Constants#HEADER_CASCADE
	 */
	public static final String X_CASCADE = "X-Cascade";

	/**
	 * Async response preference header.
	 * @see ca.uhn.fhir.rest.api.Constants#HEADER_PREFER_RESPOND_ASYNC
	 */
	public static final String PREFER_RESPOND_ASYNC = "respond-async";

	// =================================================================================================================
	// CLIENT & SESSION HEADERS
	// =================================================================================================================

	/**
	 * Client timezone header.
	 * @see ca.uhn.fhir.rest.api.Constants#HEADER_CLIENT_TIMEZONE
	 */
	public static final String TIMEZONE = "Timezone";

	/**
	 * Standard HTTP Cookie header.
	 * @see ca.uhn.fhir.rest.api.Constants#HEADER_COOKIE
	 */
	public static final String COOKIE = "Cookie";

	/**
	 * Content disposition header.
	 * @see ca.uhn.fhir.rest.api.Constants#HEADER_CONTENT_DISPOSITION
	 */
	public static final String CONTENT_DISPOSITION = "Content-Disposition";

	/**
	 * Standard HTTP Allow header.
	 * @see ca.uhn.fhir.rest.api.Constants#HEADER_ALLOW
	 */
	public static final String ALLOW = "Allow";

	// =================================================================================================================
	// USERDATA KEYS FOR INTERNAL COMMUNICATION
	// =================================================================================================================

	/**
	 * UserData key for resource partition ID.
	 * @see ca.uhn.fhir.rest.api.Constants#RESOURCE_PARTITION_ID
	 */
	public static final String USERDATA_RESOURCE_PARTITION_ID = "ca.uhn.fhir.rest.api.Constants_RESOURCE_PARTITION_ID";

	/**
	 * UserData key for FhirTerser contain resources completion flag.
	 * @see ca.uhn.fhir.util.FhirTerser#USER_DATA_KEY_CONTAIN_RESOURCES_COMPLETED
	 */
	public static final String USERDATA_CONTAIN_RESOURCES_COMPLETED = "ca.uhn.fhir.util.FhirTerser_CONTAIN_RESOURCES_COMPLETED";

	/**
	 * UserData key for resolved tag definitions in transactions.
	 * @see ca.uhn.fhir.jpa.dao.tx.HapiTransactionService#XACT_USERDATA_KEY_RESOLVED_TAG_DEFINITIONS
	 */
	public static final String USERDATA_RESOLVED_TAG_DEFINITIONS = "ca.uhn.fhir.jpa.dao.tx.HapiTransactionService_RESOLVED_TAG_DEFINITIONS";

	/**
	 * UserData key for existing search parameters in transactions.
	 * @see ca.uhn.fhir.jpa.dao.tx.HapiTransactionService#XACT_USERDATA_KEY_EXISTING_SEARCH_PARAMS
	 */
	public static final String USERDATA_EXISTING_SEARCH_PARAMS = "ca.uhn.fhir.jpa.dao.tx.HapiTransactionService_EXISTING_SEARCH_PARAMS";

	/**
	 * UserData key for MDM search expansion results.
	 * @see ca.uhn.fhir.mdm.svc.MdmSearchExpansionSvc#EXPANSION_RESULTS
	 */
	public static final String USERDATA_MDM_EXPANSION_RESULTS = "ca.uhn.fhir.mdm.svc.MdmSearchExpansionSvc_EXPANSION_RESULTS";

	/**
	 * UserData key for MDM resource name.
	 * @see ca.uhn.fhir.mdm.svc.MdmSearchExpansionSvc#RESOURCE_NAME
	 */
	public static final String USERDATA_MDM_RESOURCE_NAME = "ca.uhn.fhir.mdm.svc.MdmSearchExpansionSvc_RESOURCE_NAME";

	/**
	 * UserData key for MDM query string.
	 * @see ca.uhn.fhir.mdm.svc.MdmSearchExpansionSvc#QUERY_STRING
	 */
	public static final String USERDATA_MDM_QUERY_STRING = "ca.uhn.fhir.mdm.svc.MdmSearchExpansionSvc_QUERY_STRING";

	// =================================================================================================================
	// DEPRECATED HEADERS (for migration support)
	// =================================================================================================================

	/**
	 * @deprecated Use {@link #AUTHORIZATION} instead
	 * @see ca.uhn.fhir.rest.api.Constants#HEADER_AUTHORIZATION
	 */
	@Deprecated(since = "7.2.0", forRemoval = false)
	public static final String HEADER_AUTHORIZATION = AUTHORIZATION;

	/**
	 * @deprecated Use {@link #ACCEPT} instead
	 * @see ca.uhn.fhir.rest.api.Constants#HEADER_ACCEPT
	 */
	@Deprecated(since = "7.2.0", forRemoval = false)
	public static final String HEADER_ACCEPT = ACCEPT;

	/**
	 * @deprecated Use {@link #CONTENT_TYPE} instead
	 * @see ca.uhn.fhir.rest.api.Constants#HEADER_CONTENT_TYPE
	 */
	@Deprecated(since = "7.2.0", forRemoval = false)
	public static final String HEADER_CONTENT_TYPE = CONTENT_TYPE;

	/**
	 * @deprecated Use {@link #ETAG} instead
	 * @see ca.uhn.fhir.rest.api.Constants#HEADER_ETAG
	 */
	@Deprecated(since = "7.2.0", forRemoval = false)
	public static final String HEADER_ETAG = ETAG;

	/**
	 * @deprecated Use {@link #LAST_MODIFIED} instead
	 * @see ca.uhn.fhir.rest.api.Constants#HEADER_LAST_MODIFIED
	 */
	@Deprecated(since = "7.2.0", forRemoval = false)
	public static final String HEADER_LAST_MODIFIED = LAST_MODIFIED;

	/**
	 * @deprecated Use {@link #LOCATION} instead
	 * @see ca.uhn.fhir.rest.api.Constants#HEADER_LOCATION
	 */
	@Deprecated(since = "7.2.0", forRemoval = false)
	public static final String HEADER_LOCATION = LOCATION;

	/**
	 * @deprecated Use {@link #IF_MATCH} instead
	 * @see ca.uhn.fhir.rest.api.Constants#HEADER_IF_MATCH
	 */
	@Deprecated(since = "7.2.0", forRemoval = false)
	public static final String HEADER_IF_MATCH = IF_MATCH;

	/**
	 * @deprecated Use {@link #IF_NONE_MATCH} instead
	 * @see ca.uhn.fhir.rest.api.Constants#HEADER_IF_NONE_MATCH
	 */
	@Deprecated(since = "7.2.0", forRemoval = false)
	public static final String HEADER_IF_NONE_MATCH = IF_NONE_MATCH;

	/**
	 * @deprecated Use {@link #IF_MODIFIED_SINCE} instead
	 * @see ca.uhn.fhir.rest.api.Constants#HEADER_IF_MODIFIED_SINCE
	 */
	@Deprecated(since = "7.2.0", forRemoval = false)
	public static final String HEADER_IF_MODIFIED_SINCE = IF_MODIFIED_SINCE;

	/**
	 * @deprecated Use {@link #IF_NONE_EXIST} instead
	 * @see ca.uhn.fhir.rest.api.Constants#HEADER_IF_NONE_EXIST
	 */
	@Deprecated(since = "7.2.0", forRemoval = false)
	public static final String HEADER_IF_NONE_EXIST = IF_NONE_EXIST;

	/**
	 * @deprecated Use {@link #PREFER} instead
	 * @see ca.uhn.fhir.rest.api.Constants#HEADER_PREFER
	 */
	@Deprecated(since = "7.2.0", forRemoval = false)
	public static final String HEADER_PREFER = PREFER;

	/**
	 * @deprecated Use {@link #CACHE_CONTROL} instead
	 * @see ca.uhn.fhir.rest.api.Constants#HEADER_CACHE_CONTROL
	 */
	@Deprecated(since = "7.2.0", forRemoval = false)
	public static final String HEADER_CACHE_CONTROL = CACHE_CONTROL;
}