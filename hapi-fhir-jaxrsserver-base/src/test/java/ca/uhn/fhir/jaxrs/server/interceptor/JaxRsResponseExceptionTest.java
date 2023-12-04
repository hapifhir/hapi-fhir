package ca.uhn.fhir.jaxrs.server.interceptor;

import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import jakarta.ejb.ApplicationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JaxRsResponseExceptionTest {

	@Test
	public void testException() {
		ForbiddenOperationException wrappedException = new ForbiddenOperationException("someMessage");
		JaxRsResponseException response = new JaxRsResponseException(wrappedException);
		assertEquals(response.getMessage(), wrappedException.getMessage());
		assertEquals(response.getStatusCode(), wrappedException.getStatusCode());
		assertNotNull(response.getClass().getAnnotation(ApplicationException.class));
	}

}
