package ca.uhn.fhir.jaxrs.server.test;

/**
 * A dummy patient provider exposing no methods
 */
public class TestJaxRsDummyPatientProviderR4MimeType extends AbstractDummyPatientProvider {

	@Override public String getBaseForServer() {
		return "https://fhirserver/fhir";
	}

}
