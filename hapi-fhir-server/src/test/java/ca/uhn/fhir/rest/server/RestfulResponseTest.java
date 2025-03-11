package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

/**
 * Unit tests of {@link BaseRestfulResponse}.
 */
public class RestfulResponseTest {
	@Test
	public void addMultipleHeaderValues() {
		@SuppressWarnings("unchecked")
		final BaseRestfulResponse<?> restfulResponse =
			mock(BaseRestfulResponse.class, withSettings()
				.useConstructor((RequestDetails) null).defaultAnswer(CALLS_REAL_METHODS));

		restfulResponse.addHeader("Authorization", "Basic");
		restfulResponse.addHeader("Authorization", "Bearer");
		restfulResponse.addHeader("Cache-Control", "no-cache, no-store");

		assertThat(restfulResponse.getHeaders()).hasSize(2);
		assertThat(restfulResponse.getHeaders().get("Authorization")).containsExactly("Basic", "Bearer");
		assertThat(restfulResponse.getHeaders().get("Cache-Control")).containsExactly("no-cache, no-store");
	}
}
