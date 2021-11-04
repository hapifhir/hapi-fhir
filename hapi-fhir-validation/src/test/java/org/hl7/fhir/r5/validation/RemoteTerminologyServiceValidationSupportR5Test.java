package org.hl7.fhir.r5.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import org.hl7.fhir.common.hapi.validation.support.RemoteTerminologyServiceValidationSupport;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

public class RemoteTerminologyServiceValidationSupportR5Test {
	private static final String ANY_NONBLANK_VALUE = "anything";
	private static FhirContext ourCtx = FhirContext.forR5();
	@RegisterExtension
	public RestfulServerExtension myRestfulServerExtension = new RestfulServerExtension(ourCtx);

	private RemoteTerminologyServiceValidationSupport mySvc;

	@BeforeEach
	public void before() {
		String baseUrl = "http://localhost:" + myRestfulServerExtension.getPort();
		mySvc = new RemoteTerminologyServiceValidationSupport(ourCtx);
		mySvc.setBaseUrl(baseUrl);
	}

	@Test
	public void testLookupCode_R5_ThrowsException() {
		Assertions.assertThrows(UnsupportedOperationException.class, () -> {
			IValidationSupport.LookupCodeResult outcome = mySvc.lookupCode(
				new ValidationSupportContext(FhirContext.forR5().getValidationSupport()), ANY_NONBLANK_VALUE, ANY_NONBLANK_VALUE);
		});
	}
}
