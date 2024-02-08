package ca.uhn.fhir.jaxrs.server.interceptor;

import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import jakarta.ejb.ApplicationException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JaxRsResponseExceptionTest {

	@Test
	public void testException() {
		ForbiddenOperationException wrappedException = new ForbiddenOperationException("someMessage");
		JaxRsResponseException response = new JaxRsResponseException(wrappedException);
		assertThat(wrappedException.getMessage()).isEqualTo(response.getMessage());
		assertThat(wrappedException.getStatusCode()).isEqualTo(response.getStatusCode());
		assertThat(response.getClass().getAnnotation(ApplicationException.class)).isNotNull();
	}

}
