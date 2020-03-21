package org.hl7.fhir.common.hapi.validation;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.context.support.IValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class ValidationSupportChainTest {

	@Test
	public void testVersionCheck() {

		DefaultProfileValidationSupport ctx3 = new DefaultProfileValidationSupport(FhirContext.forDstu3());
		DefaultProfileValidationSupport ctx4 = new DefaultProfileValidationSupport(FhirContext.forR4());

		try {
			new ValidationSupportChain(ctx3, ctx4);
		} catch (ConfigurationException e) {
			assertEquals("Trying to add validation support of version R4 to chain with 1 entries of version DSTU3", e.getMessage());
		}

	}

	@Test
	public void testMissingContext() {
		IValidationSupport ctx = mock(IValidationSupport.class);
		try {
			new ValidationSupportChain(ctx);
		} catch (ConfigurationException e) {
			assertEquals("Can not add validation support: getFhirContext() returns null", e.getMessage());
		}
	}


}
