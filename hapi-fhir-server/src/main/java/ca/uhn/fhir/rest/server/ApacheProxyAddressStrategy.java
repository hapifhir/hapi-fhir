package ca.uhn.fhir.rest.server;

import java.net.URI;

/*
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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

import java.util.Optional;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServletServerHttpRequest;

import static java.util.Optional.ofNullable;

import ca.uhn.fhir.rest.server.IncomingRequestAddressStrategy;

/**
 * Works like the normal
 * {@link ca.uhn.fhir.rest.server.IncomingRequestAddressStrategy} unless there's
 * an x-forwarded-host present, in which case that's used in place of the
 * server's address.
 * <p>
 * If the Apache Http Server <code>mod_proxy</code> isn't configured to supply
 * <code>x-forwarded-proto</code>, the factory method that you use to create the
 * address strategy will determine the default. Note that <code>mod_proxy</code>
 * doesn't set this by default, but it can be configured via
 * <code>RequestHeader set X-Forwarded-Proto http</code> (or https)
 * </p>
 * <p>
 * List of supported forward headers:
 * <ul>
 * <li>x-forwarded-host - original host requested by the client throw proxy
 * server
 * <li>x-forwarded-proto - original protocol (http, https) requested by the
 * client
 * <li>x-forwarded-port - original port request by the client, assume default
 * port if not defined
 * <li>x-forwarded-prefix - original server prefix / context path requested by
 * the client
 * </ul>
 * </p>
 * <p>
 * If you want to set the protocol based on something other than the constructor
 * argument, you should be able to do so by overriding <code>protocol</code>.
 * </p>
 * <p>
 * Note that while this strategy was designed to work with Apache Http Server,
 * and has been tested against it, it should work with any proxy server that
 * sets <code>x-forwarded-host</code>
 * </p>
 *
 */
public class ApacheProxyAddressStrategy extends IncomingRequestAddressStrategy {
	private static final String X_FORWARDED_PREFIX = "x-forwarded-prefix";
	private static final String X_FORWARDED_PROTO = "x-forwarded-proto";
	private static final String X_FORWARDED_HOST = "x-forwarded-host";
	private static final String X_FORWARDED_PORT = "x-forwarded-port";

	private static final Logger LOG = LoggerFactory
			.getLogger(ApacheProxyAddressStrategy.class);

	private final boolean useHttps;

	/**
	 * @param useHttps
	 *            Is used when the {@code x-forwarded-proto} is not set in the
	 *            request.
	 */
	public ApacheProxyAddressStrategy(boolean useHttps) {
		this.useHttps = useHttps;
	}

	@Override
	public String determineServerBase(ServletContext servletContext,
			HttpServletRequest request) {
		String serverBase = super.determineServerBase(servletContext, request);
		ServletServerHttpRequest requestWrapper = new ServletServerHttpRequest(
				request);
		HttpHeaders headers = requestWrapper.getHeaders();
		Optional<String> forwardedHost = headers
				.getValuesAsList(X_FORWARDED_HOST).stream().findFirst();
		return forwardedHost
				.map(s -> forwardedServerBase(serverBase, headers, s))
				.orElse(serverBase);
	}

	private String forwardedServerBase(String originalServerBase,
			HttpHeaders headers, String forwardedHost) {
		Optional<String> forwardedPrefix = getForwardedPrefix(headers);
		LOG.debug("serverBase: {}, forwardedHost: {}, forwardedPrefix: {}",
				originalServerBase, forwardedHost, forwardedPrefix);
		LOG.debug("request header: {}", headers);

		String host = protocol(headers) + "://" + forwardedHost;
		String hostWithOptionalPort = port(headers).map(p -> (host + ":" + p))
				.orElse(host);

		String path = forwardedPrefix
				.orElseGet(() -> pathFrom(originalServerBase));
		return joinStringsWith(hostWithOptionalPort, path, "/");
	}

	private Optional<String> port(HttpHeaders headers) {
		return ofNullable(headers.getFirst(X_FORWARDED_PORT));
	}

	private String pathFrom(String serverBase) {
		String serverBasePath = URI.create(serverBase).getPath();
		return StringUtils.defaultIfBlank(serverBasePath, "");
	}

	private static String joinStringsWith(String left, String right,
			String joiner) {
		if (left.endsWith(joiner) && right.startsWith(joiner)) {
			return left + right.substring(1);
		} else if (left.endsWith(joiner) || right.startsWith(joiner)) {
			return left + right;
		} else {
			return left + joiner + right;
		}
	}

	private Optional<String> getForwardedPrefix(HttpHeaders headers) {
		return ofNullable(headers.getFirst(X_FORWARDED_PREFIX));
	}

	private String protocol(HttpHeaders headers) {
		String protocol = headers.getFirst(X_FORWARDED_PROTO);
		if (protocol != null) {
			return protocol;
		}
		return useHttps ? "https" : "http";
	}

	/**
	 * Static factory for instance using <code>http://</code>
	 */
	public static ApacheProxyAddressStrategy forHttp() {
		return new ApacheProxyAddressStrategy(false);
	}

	/**
	 * Static factory for instance using <code>https://</code>
	 */
	public static ApacheProxyAddressStrategy forHttps() {
		return new ApacheProxyAddressStrategy(true);
	}
}
