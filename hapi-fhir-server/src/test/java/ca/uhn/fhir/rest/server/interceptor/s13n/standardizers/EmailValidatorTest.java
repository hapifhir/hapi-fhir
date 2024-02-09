package ca.uhn.fhir.rest.server.interceptor.s13n.standardizers;

import ca.uhn.fhir.rest.server.interceptor.validation.fields.EmailValidator;
import ca.uhn.fhir.rest.server.interceptor.validation.fields.IValidator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EmailValidatorTest {

	@Test
	public void testEmailValidation() {
		IValidator val = new EmailValidator();

		assertThat(val.isValid("show.me.the.money@email.com")).isTrue();
		assertThat(val.isValid("money@email")).isFalse();
		assertThat(val.isValid("show me the money@email.com")).isFalse();
		assertThat(val.isValid("gimme dough")).isFalse();
	}

}
