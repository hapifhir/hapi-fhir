package ca.uhn.fhir.rest.gclient;

/**
 * Interface to make non-FHIR requests using the GenericClient plumbing.
 * Useful for the interceptor infrastructure when dealing with thinks like $export poll locations
 * which are not FHIR payloads.  Boo.
 */
public interface IRawHttp {

	IGetRaw get();

	interface IGetRaw {
		IGetRawUntyped byUrl(String thePageUrl);

	}


	// fixme we might want a different response type here - maybe IHttpResponse?
	//  something like a generic Http entity (see StringEntity)  which exposes status line, mime type,
	// location headers, etc.
	interface IGetRawUntyped {
		IClientHttpExecutable<IClientHttpExecutable<?,String>,String>  forString();
	}

}
