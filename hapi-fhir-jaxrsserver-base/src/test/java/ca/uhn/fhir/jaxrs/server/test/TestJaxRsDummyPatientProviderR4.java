package ca.uhn.fhir.jaxrs.server.test;

/**
 * A dummy patient provider exposing no methods
 */
public class TestJaxRsDummyPatientProviderR4 extends AbstractDummyPatientProvider {

	@Override public String getBaseForServer() {
		return "https://fhirserver/fhir/r4";
	}
}
