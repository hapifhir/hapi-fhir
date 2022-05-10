package ca.uhn.fhir.rest.server;

/*
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;



import static java.util.Optional.ofNullable;


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
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpRequest(requestWrapper);
		uriBuilder.replaceQuery(null);
		HttpHeaders headers = requestWrapper.getHeaders();
		adjustSchemeWithDefault(uriBuilder, headers);
		return forwardedServerBase(serverBase, headers, uriBuilder);
	}

	/**
	 * If forward host ist defined, but no forward protocol, use the configured default.
	 * 
	 * @param uriBuilder
	 * @param headers
	 */
	private void adjustSchemeWithDefault(UriComponentsBuilder uriBuilder,
			HttpHeaders headers) {
		if (headers.getFirst(X_FORWARDED_HOST) != null
				&& headers.getFirst(X_FORWARDED_PROTO) == null) {
			uriBuilder.scheme(useHttps ? "https" : "http");
		}
	}

	private String forwardedServerBase(String originalServerBase,
			HttpHeaders headers, UriComponentsBuilder uriBuilder) {
		Optional<String> forwardedPrefix = getForwardedPrefix(headers);
		LOG.debug("serverBase: {}, forwardedPrefix: {}", originalServerBase, forwardedPrefix);
		LOG.debug("request header: {}", headers);

		String path = forwardedPrefix
				.orElseGet(() -> pathFrom(originalServerBase));
		uriBuilder.replacePath(path);
		return uriBuilder.build().toUriString();
	}

	private String pathFrom(String serverBase) {
		UriComponents build = UriComponentsBuilder.fromHttpUrl(serverBase).build();
		return StringUtils.defaultIfBlank(build.getPath(), "");
	}

	private Optional<String> getForwardedPrefix(HttpHeaders headers) {
		return ofNullable(headers.getFirst(X_FORWARDED_PREFIX));
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
