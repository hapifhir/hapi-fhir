package ca.uhn.fhir.rest.gclient;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Interface to make non-FHIR requests using the GenericClient plumbing.
 * Useful for the interceptor infrastructure when dealing with thinks like $export poll locations
 * which are not FHIR payloads.  Boo.
 */
public interface IRawHttp {
	IClientHttpExecutable<IClientHttpExecutable<?,IEntityResult>,IEntityResult> get(String theUrl);
}
