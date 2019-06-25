package ca.uhn.fhir.rest.server.interceptor.consent;

public interface IConsentContextServices {

	/**
	 * Implementation of this interface that simply always throws a {@link UnsupportedOperationException}
	 */
	IConsentContextServices NULL_IMPL = new NullConsentContextServices();

}
