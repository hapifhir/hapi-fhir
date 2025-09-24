package ca.uhn.fhir.rest.gclient;

/**
 * Interface to make non-FHIR requests using the GenericClient plumbing.
 * Useful for the interceptor infrastructure when dealing with thinks like $export poll locations
 * which are not FHIR payloads.
 */
public interface IRawHttp {
	IClientHttpExecutable<IClientHttpExecutable<?, IEntityResult>, IEntityResult> get(String theUrl);
}
