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
 * test issuing concurrent calls queue against itself. The socket timeout is generous so a test
 * paused in a debugger does not fail on a slow read.
 * </p>
 * <p>
 * The caller owns the returned client and must close it.
 * </p>
 */
// Created by claude-opus-5
public final class TestHttpClientFactory {

	private static final int MAX_CONNECTIONS = 99;
	private static final long CONNECTION_TIME_TO_LIVE_MILLIS = 5000;
	private static final int SOCKET_TIMEOUT_MILLIS = 30 * 1000;

	private TestHttpClientFactory() {}

	/**
	 * Builds a client that follows redirects.
	 *
	 * @see #create(boolean)
	 */
	public static CloseableHttpClient create() {
		return create(true);
	}

	/**
	 * @param theFollowRedirects whether this client follows a {@literal 3xx}. Note that only
	 *    {@literal true} leaves the choice open afterwards:
	 *    {@link HttpTestRequest#followRedirects(boolean)} can turn following off per request, but
	 *    cannot turn it back on for a client built with {@literal false}.
	 */
	public static CloseableHttpClient create(boolean theFollowRedirects) {
		PoolingHttpClientConnectionManager connectionManager =
				new PoolingHttpClientConnectionManager(CONNECTION_TIME_TO_LIVE_MILLIS, TimeUnit.MILLISECONDS);
		connectionManager.setMaxTotal(MAX_CONNECTIONS);
		connectionManager.setDefaultMaxPerRoute(MAX_CONNECTIONS);
		connectionManager.setDefaultSocketConfig(SocketConfig.copy(SocketConfig.DEFAULT)
				.setSoTimeout(SOCKET_TIMEOUT_MILLIS)
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
