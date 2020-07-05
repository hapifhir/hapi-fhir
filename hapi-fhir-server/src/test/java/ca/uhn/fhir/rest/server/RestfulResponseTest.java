package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

/**
 * Unit tests of {@link RestfulResponse}.
 */
public class RestfulResponseTest {
	@Test
	public void addMultipleHeaderValues() {
		@SuppressWarnings("unchecked")
		final RestfulResponse<?> restfulResponse =
			mock(RestfulResponse.class, withSettings()
				.useConstructor((RequestDetails) null).defaultAnswer(CALLS_REAL_METHODS));

		restfulResponse.addHeader("Authorization", "Basic");
		restfulResponse.addHeader("Authorization", "Bearer");
		restfulResponse.addHeader("Cache-Control", "no-cache, no-store");

		assertEquals(2, restfulResponse.getHeaders().size());
		assertThat(restfulResponse.getHeaders().get("Authorization"), Matchers.contains("Basic", "Bearer"));
		assertThat(restfulResponse.getHeaders().get("Cache-Control"), Matchers.contains("no-cache, no-store"));
	}
}
