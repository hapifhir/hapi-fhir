package ca.uhn.fhir.rest.server.interceptor.binary;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import ca.uhn.fhir.test.utilities.server.HashMapResourceProviderExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import org.hl7.fhir.r4.model.Binary;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class BinarySecurityContextInterceptorTest {


	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	@RegisterExtension
	@Order(0)
	public static final RestfulServerExtension ourServer = new RestfulServerExtension(ourCtx)
		.registerInterceptor(new HeaderBasedBinarySecurityContextInterceptor(ourCtx));
	@RegisterExtension
	@Order(1)
	public static final HashMapResourceProviderExtension<Binary> ourBinaryProvider = new HashMapResourceProviderExtension<>(ourServer, Binary.class);
	@RegisterExtension
	@Order(1)
	public static final HashMapResourceProviderExtension<Patient> ourPatientProvider = new HashMapResourceProviderExtension<>(ourServer, Patient.class);

	@Test
	void testRead_SecurityContextIdentifierPresent_RequestAllowedByInterceptor() {
		storeBinaryWithSecurityContextIdentifier();

		Binary actual = ourServer
			.getFhirClient()
			.read()
			.resource(Binary.class)
			.withId("A")
			.withAdditionalHeader(HeaderBasedBinarySecurityContextInterceptor.X_SECURITY_CONTEXT_ALLOWED_IDENTIFIER, "http://foo|bar")
			.execute();
		assertEquals("A", actual.getIdElement().getIdPart());
		assertEquals("http://foo", actual.getSecurityContext().getIdentifier().getSystem());
	}

	@Test
	void testRead_SecurityContextIdentifierPresent_SystemRequestDetailsPermitted() {
		storeBinaryWithSecurityContextIdentifier();

		IBundleProvider results = ourBinaryProvider.searchAll(new SystemRequestDetails());
		assertEquals(1, results.sizeOrThrowNpe());
	}

	@Test
	void testRead_SecurityContextIdentifierPresent_RequestBlockedByInterceptor() {
		storeBinaryWithSecurityContextIdentifier();

		try {
			ourServer
				.getFhirClient()
				.read()
				.resource(Binary.class)
				.withId("A")
				.execute();
			fail();
		} catch (ForbiddenOperationException e) {
			assertEquals("HTTP 403 Forbidden: HAPI-2369: Security context not permitted", e.getMessage());
		}
	}

	@Test
	void testRead_SecurityContextIdentifierNotPresent() {
		storeBinaryWithoutSecurityContext();

		Binary actual = ourServer
			.getFhirClient()
			.read()
			.resource(Binary.class)
			.withId("A")
			.execute();
		assertEquals("A", actual.getIdElement().getIdPart());
		assertNull(actual.getSecurityContext().getIdentifier().getSystem());
	}

	@Test
	void testRead_NonBinaryResource() {
		Patient patient = new Patient();
		patient.setId("Patient/A");
		patient.setActive(true);
		ourPatientProvider.store(patient);

		Patient actual = ourServer
			.getFhirClient()
			.read()
			.resource(Patient.class)
			.withId("A")
			.execute();
		assertEquals("A", actual.getIdElement().getIdPart());
		assertTrue(actual.getActive());
	}





	@Test
	void testUpdate_SecurityContextIdentifierPresent_RequestAllowedByInterceptor() {
		storeBinaryWithSecurityContextIdentifier();

		Binary newBinary = new Binary();
		newBinary.setId("Binary/A");
		newBinary.setContentType("text/plain");

		MethodOutcome outcome = ourServer
			.getFhirClient()
			.update()
			.resource(newBinary)
			.withAdditionalHeader(HeaderBasedBinarySecurityContextInterceptor.X_SECURITY_CONTEXT_ALLOWED_IDENTIFIER, "http://foo|bar")
			.execute();
		assertEquals(2L, outcome.getId().getVersionIdPartAsLong());
	}

	@Test
	void testUpdate_SecurityContextIdentifierPresent_RequestBlockedByInterceptor() {
		storeBinaryWithSecurityContextIdentifier();

		try {
			Binary newBinary = new Binary();
			newBinary.setId("Binary/A");
			newBinary.setContentType("text/plain");

			ourServer
				.getFhirClient()
				.update()
				.resource(newBinary)
				.execute();
			fail();
		} catch (ForbiddenOperationException e) {
			assertEquals("HTTP 403 Forbidden: HAPI-2369: Security context not permitted", e.getMessage());
		}
	}


	private void storeBinaryWithoutSecurityContext() {
		Binary binary = new Binary();
		binary.setId("Binary/A");
		binary.setContentType("text/plain");
		ourBinaryProvider.store(binary);
	}

	/**
	 * This class also exists in hapi-fhir-docs - Make sure any changes here
	 * are reflected there too!
	 */
	private void storeBinaryWithSecurityContextIdentifier() {
		Binary binary = new Binary();
		binary.setId("Binary/A");
		binary.getSecurityContext().getIdentifier().setSystem("http://foo");
		binary.getSecurityContext().getIdentifier().setValue("bar");
		ourBinaryProvider.store(binary);
	}


	/**
	 * This class also exists in hapi-fhir-docs - Update it there too if this changes!
	 */
	public static class HeaderBasedBinarySecurityContextInterceptor extends BinarySecurityContextInterceptor {

		/**
		 * Header name
		 */
		public static final String X_SECURITY_CONTEXT_ALLOWED_IDENTIFIER = "X-SecurityContext-Allowed-Identifier";

		/**
		 * Constructor
		 *
		 * @param theFhirContext The FHIR context
		 */
		public HeaderBasedBinarySecurityContextInterceptor(FhirContext theFhirContext) {
			super(theFhirContext);
		}

		/**
		 * This method should be overridden in order to determine whether the security
		 * context identifier is allowed for the user.
		 *
		 * @param theSecurityContextSystem The <code>Binary.securityContext.identifier.system</code> value
		 * @param theSecurityContextValue  The <code>Binary.securityContext.identifier.value</code> value
		 * @param theRequestDetails        The request details associated with this request
		 */
		@Override
		protected boolean securityContextIdentifierAllowed(String theSecurityContextSystem, String theSecurityContextValue, RequestDetails theRequestDetails) {

			// In our simple example, we will use an incoming header called X-SecurityContext-Allowed-Identifier
			// to determine whether the security context is allowed. This is typically not what you
			// would want, since this is trusting the client to tell us what they are allowed
			// to see. You would typically verify an access token or user session with something
			// external, but this is a simple demonstration.
			String actualHeaderValue = theRequestDetails.getHeader(X_SECURITY_CONTEXT_ALLOWED_IDENTIFIER);
			String expectedHeaderValue = theSecurityContextSystem + "|" + theSecurityContextValue;
			return expectedHeaderValue.equals(actualHeaderValue);
		}
	}

}
