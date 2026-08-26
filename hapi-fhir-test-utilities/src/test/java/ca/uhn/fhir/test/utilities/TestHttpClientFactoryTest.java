package ca.uhn.fhir.test.utilities;

import ca.uhn.fhir.test.utilities.server.HttpServletExtension;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Covers the redirect choice {@link TestHttpClientFactory} makes when building a client, which is
 * fixed at build time and cannot be changed per request.
 */
// Created by claude-opus-5
class TestHttpClientFactoryTest {

	@RegisterExtension
	private static final HttpServletExtension ourServer = new HttpServletExtension().withServlet(new RedirectServlet());

	@Test
	void create_followsRedirectsByDefault() throws IOException {
		try (CloseableHttpClient client = TestHttpClientFactory.create()) {
			HttpTestResponse response = HttpTestRequest.to(client, redirectUrl()).get();

			assertThat(response.getStatusCode()).isEqualTo(200);
			assertThat(response.getBody()).isEqualTo("landed");
		}
	}

	@Test
	void createWithRedirectsDisabled_returnsTheRedirectItself() throws IOException {
		try (CloseableHttpClient client = TestHttpClientFactory.create(false)) {
			HttpTestResponse response = HttpTestRequest.to(client, redirectUrl()).get();

			assertThat(response.getStatusCode()).isEqualTo(302);
			assertThat(response.getHeader("Location")).isNotNull();
		}
	}

	@Test
	void createFollowingRedirects_withoutRedirectsSuppressesPerRequest() throws IOException {
		try (CloseableHttpClient client = TestHttpClientFactory.create(true)) {
			HttpTestResponse response =
					HttpTestRequest.to(client, redirectUrl()).withoutRedirects().get();

			assertThat(response.getStatusCode()).isEqualTo(302);
		}
	}

	private String redirectUrl() {
		return ourServer.getBaseUrl() + "/foo?redirect=true";
	}

	private static class RedirectServlet extends HttpServlet {

		@Override
		protected void service(HttpServletRequest theRequest, HttpServletResponse theResponse) throws IOException {
			if (theRequest.getParameter("redirect") != null) {
				theResponse.setStatus(302);
				theResponse.addHeader("Location", theRequest.getRequestURL().toString());
				return;
			}
			theResponse.setStatus(200);
			theResponse.getWriter().write("landed");
		}
	}
}
