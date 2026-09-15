package ca.uhn.fhir.test.utilities;

import ca.uhn.fhir.test.utilities.server.HttpServletExtension;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.SocketTimeoutException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Covers the redirect choice {@link TestHttpClientFactory} makes when building a client, which is
 * fixed at build time and cannot be changed per request.
 */
// Created by claude-opus-5
class TestHttpClientFactoryTest {

	@RegisterExtension
	private static final HttpServletExtension ourServer = new HttpServletExtension().withServlet(new EchoServlet());

	@Test
	void create_followsRedirectsByDefault() throws IOException {
		try (CloseableHttpClient client = TestHttpClientFactory.create()) {
			HttpTestResponse response = HttpTestRequest.to(client, redirectUrl()).get();

			assertThat(response.getStatusCode()).isEqualTo(200);
			assertThat(response.getBody()).contains("method=GET");
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

	@Test
	void createWithSocketTimeout_serverSlowerThanTimeout_failsTheRead() throws IOException {
		try (CloseableHttpClient client = TestHttpClientFactory.create(true, 50)) {
			assertThatThrownBy(() -> HttpTestRequest.to(client, slowUrl()).get())
					.isInstanceOf(UncheckedIOException.class)
					.hasRootCauseInstanceOf(SocketTimeoutException.class);
		}
	}

	@Test
	void createWithNoSocketTimeout_serverSlow_waitsForTheResponse() throws IOException {
		try (CloseableHttpClient client =
				TestHttpClientFactory.create(true, TestHttpClientFactory.NO_SOCKET_TIMEOUT)) {
			HttpTestResponse response = HttpTestRequest.to(client, slowUrl()).get();

			assertThat(response.getStatusCode()).isEqualTo(200);
		}
	}

	private String slowUrl() {
		return ourServer.getBaseUrl() + "/foo?delayMillis=500";
	}

	private String redirectUrl() {
		return ourServer.getBaseUrl() + "/foo?redirect=true";
	}
}
