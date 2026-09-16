package ca.uhn.fhir.test.utilities;

import ca.uhn.fhir.test.utilities.server.BaseJettyServerExtension;
import ca.uhn.fhir.test.utilities.server.HttpServletExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.io.UncheckedIOException;
import java.net.SocketTimeoutException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Covers the read timeout that {@link BaseJettyServerExtension} gives the client behind
 * {@code request(...)} and {@code fhirRequest(...)}. It is bounded by default, so a server that
 * stops responding fails the test that provoked it rather than hanging the surefire fork until the
 * build kills it; {@link BaseJettyServerExtension#withHttpClientSocketTimeoutMillis(int)} is how a
 * test that genuinely needs a longer read says so.
 */
// Created by claude-opus-5
class BaseJettyServerExtensionSocketTimeoutTest {

	private static final String SLOW_PATH = "/foo?delayMillis=500";

	@RegisterExtension
	private static final HttpServletExtension ourTimingOutServer =
			new HttpServletExtension().withServlet(new EchoServlet()).withHttpClientSocketTimeoutMillis(50);

	@RegisterExtension
	private static final HttpServletExtension ourPatientServer = new HttpServletExtension()
			.withServlet(new EchoServlet())
			.withHttpClientSocketTimeoutMillis(TestHttpClientFactory.NO_SOCKET_TIMEOUT);

	@Test
	void withHttpClientSocketTimeoutMillis_serverSlowerThanTimeout_failsTheRead() {
		assertThatThrownBy(() -> ourTimingOutServer.request(SLOW_PATH).get())
				.isInstanceOf(UncheckedIOException.class)
				.hasRootCauseInstanceOf(SocketTimeoutException.class);
	}

	@Test
	void withHttpClientSocketTimeoutMillis_noSocketTimeout_waitsForTheResponse() {
		assertThat(ourPatientServer.request(SLOW_PATH).get().getStatusCode()).isEqualTo(200);
	}

	/**
	 * The client is built when the server starts, so a setter reaching it afterwards would silently do
	 * nothing. It fails instead, the way every other pre-start setter on this class does.
	 */
	@Test
	void withHttpClientSocketTimeoutMillis_afterServerStarted_fails() {
		assertThatThrownBy(() -> ourPatientServer.withHttpClientSocketTimeoutMillis(50))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("Server is already started");
	}
}
