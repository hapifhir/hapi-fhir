package ca.uhn.fhir.rest.server.interceptor.s13n.standardizers;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import ca.uhn.fhir.rest.server.interceptor.validation.fields.EmailValidator;
import ca.uhn.fhir.rest.server.interceptor.validation.fields.IValidator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EmailValidatorTest {

	@Test
	public void testEmailValidation() {
		IValidator val = new EmailValidator();

		assertTrue(val.isValid("show.me.the.money@email.com"));
		assertFalse(val.isValid("money@email"));
		assertFalse(val.isValid("show me the money@email.com"));
		assertFalse(val.isValid("gimme dough"));
	}

}
