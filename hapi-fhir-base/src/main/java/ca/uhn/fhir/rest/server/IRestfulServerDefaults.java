package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;

public interface IRestfulServerDefaults {

	/**
	 * Gets the {@link FhirContext} associated with this server. For efficient processing, resource providers and plain providers should generally use this context if one is needed, as opposed to
	 * creating their own.
	 */
	FhirContext getFhirContext();

	/**
	 * Should the server "pretty print" responses by default (requesting clients can always override this default by supplying an <code>Accept</code> header in the request, or a <code>_pretty</code>
	 * parameter in the request URL.
	 * <p>
	 * The default is <code>false</code>
	 * </p>
	 * 
	 * @return Returns the default pretty print setting
	 */
	boolean isDefaultPrettyPrint();

	/**
	 * @return Returns the server support for ETags (will not be <code>null</code>). Default is {@link #DEFAULT_ETAG_SUPPORT}
	 */	
	ETagSupportEnum getETagSupport();

	/**
	 * @return Returns the setting for automatically adding profile tags
	 */
	AddProfileTagEnum getAddProfileTag();

	/**
	 * @return Returns the default encoding to return (XML/JSON) if an incoming request does not specify a preference (either with the <code>_format</code> URL parameter, or with an <code>Accept</code> header
	 * in the request. The default is {@link EncodingEnum#XML}. Will not return null.
	 */	
	EncodingEnum getDefaultResponseEncoding();

	/**
	 * @return If  <code>true</code> the server will use browser friendly content-types (instead of standard FHIR ones) when it detects that the request is coming from a browser
	 * instead of a FHIR
	 */	
	boolean isUseBrowserFriendlyContentTypes();

}
