/*-
 * #%L
 * HAPI FHIR Test Utilities
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.test.utilities;

import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.util.concurrent.TimeUnit;

/**
 * Builds the Apache HttpClient 4.x client that this module's test infrastructure hands to tests, so
 * that there is one recipe rather than several.
 * <p>
 * The pool is deliberately large — HttpClient defaults to two connections per route, which makes a
 * test issuing concurrent calls queue against itself.
 * </p>
 * <p>
 * The socket timeout is opt-in. A bounded one turns a hung server into a clear read timeout rather
 * than a fork that hangs until the build kills it, but it also breaks any test whose server
 * legitimately takes longer to respond — a slow search, an export being polled, or a test being
 * stepped through a debugger. Callers that inherited an unbounded read before this factory existed
 * pass {@link #NO_SOCKET_TIMEOUT} to keep it.
 * </p>
 * <p>
 * The caller owns the returned client and must close it.
 * </p>
 */
// Created by claude-opus-5
public final class TestHttpClientFactory {

	/**
	 * Waits indefinitely for a response, which is Apache HttpClient's own default.
	 *
	 * @see #create(boolean, int)
	 */
	public static final int NO_SOCKET_TIMEOUT = 0;

	private static final int MAX_CONNECTIONS = 99;
	private static final long CONNECTION_TIME_TO_LIVE_MILLIS = 5000;
	private static final int DEFAULT_SOCKET_TIMEOUT_MILLIS = 30 * 1000;

	private TestHttpClientFactory() {}

	/**
	 * Builds a client that follows redirects and gives up on a read after 30 seconds.
	 *
	 * @see #create(boolean, int)
	 */
	public static CloseableHttpClient create() {
		return create(true);
	}

	/**
	 * Builds a client that gives up on a read after 30 seconds.
	 *
	 * @param theFollowRedirects whether this client follows a {@literal 3xx}
	 * @see #create(boolean, int)
	 */
	public static CloseableHttpClient create(boolean theFollowRedirects) {
		return create(theFollowRedirects, DEFAULT_SOCKET_TIMEOUT_MILLIS);
	}

	/**
	 * @param theFollowRedirects whether this client follows a {@literal 3xx}. A client built with
	 *    {@literal true} can still suppress following on individual requests via
	 *    {@link HttpTestRequest#withoutRedirects()}; one built with {@literal false} cannot be made
	 *    to follow, since Apache HttpClient fixes this when the client is built.
	 * @param theSocketTimeoutMillis how long to wait for a response before failing the read, or
	 *    {@link #NO_SOCKET_TIMEOUT} to wait indefinitely
	 */
	public static CloseableHttpClient create(boolean theFollowRedirects, int theSocketTimeoutMillis) {
		PoolingHttpClientConnectionManager connectionManager =
				new PoolingHttpClientConnectionManager(CONNECTION_TIME_TO_LIVE_MILLIS, TimeUnit.MILLISECONDS);
		connectionManager.setMaxTotal(MAX_CONNECTIONS);
		connectionManager.setDefaultMaxPerRoute(MAX_CONNECTIONS);
		connectionManager.setDefaultSocketConfig(SocketConfig.copy(SocketConfig.DEFAULT)
				.setSoTimeout(theSocketTimeoutMillis)
				.build());

		HttpClientBuilder builder = HttpClientBuilder.create()
				.setConnectionManager(connectionManager)
				.setMaxConnPerRoute(MAX_CONNECTIONS);
		if (!theFollowRedirects) {
			builder.disableRedirectHandling();
		}
		return builder.build();
	}
}
