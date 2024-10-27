/*-
 * #%L
 * HAPI FHIR Test Utilities
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.htmlunit.CookieManager;
import org.htmlunit.FormEncodingType;
import org.htmlunit.WebClient;
import org.htmlunit.WebConnection;
import org.htmlunit.WebRequest;
import org.htmlunit.WebResponse;
import org.htmlunit.WebResponseData;
import org.htmlunit.util.Cookie;
import org.htmlunit.util.KeyDataPair;
import org.htmlunit.util.NameValuePair;
import org.springframework.beans.Mergeable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockPart;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.SmartRequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * This class is just a duplication or the Spring class by the same name
 * (MockMvcWebConnection). It exists to use the new org.htmlunit namespace.
 *
 * This should no longer be necessary once this is fixed:
 * https://github.com/spring-projects/spring-framework/issues/30392
 */
public final class MockMvcWebConnectionForHtmlUnit3 implements WebConnection {

	private final Map<String, MockHttpSession> sessions = new HashMap<>();

	private final MockMvc mockMvc;

	@Nullable
	private final String contextPath;

	private WebClient webClient;

	private static final int MAX_FORWARDS = 100;


	/**
	 * Create a new instance that assumes the context path of the application
	 * is {@code ""} (i.e., the root context).
	 * <p>For example, the URL {@code http://localhost/test/this} would use
	 * {@code ""} as the context path.
	 * @param mockMvc the {@code MockMvc} instance to use; never {@code null}
	 * @param webClient the {@link WebClient} to use. never {@code null}
	 */
	public MockMvcWebConnectionForHtmlUnit3(MockMvc mockMvc, WebClient webClient) {
		this(mockMvc, webClient, "");
	}

	/**
	 * Create a new instance with the specified context path.
	 * <p>The path may be {@code null} in which case the first path segment
	 * of the URL is turned into the contextPath. Otherwise it must conform
	 * to {@link jakarta.servlet.http.HttpServletRequest#getContextPath()}
	 * which states that it can be an empty string and otherwise must start
	 * with a "/" character and not end with a "/" character.
	 * @param mockMvc the {@code MockMvc} instance to use (never {@code null})
	 * @param webClient the {@link WebClient} to use (never {@code null})
	 * @param contextPath the contextPath to use
	 */
	public MockMvcWebConnectionForHtmlUnit3(MockMvc mockMvc, WebClient webClient, @Nullable String contextPath) {
		Assert.notNull(mockMvc, "MockMvc must not be null");
		Assert.notNull(webClient, "WebClient must not be null");
		validateContextPath(contextPath);

		this.webClient = webClient;
		this.mockMvc = mockMvc;
		this.contextPath = contextPath;
	}

	/**
	 * Validate the supplied {@code contextPath}.
	 * <p>If the value is not {@code null}, it must conform to
	 * {@link jakarta.servlet.http.HttpServletRequest#getContextPath()} which
	 * states that it can be an empty string and otherwise must start with
	 * a "/" character and not end with a "/" character.
	 * @param contextPath the path to validate
	 */
	static void validateContextPath(@Nullable String contextPath) {
		if (contextPath == null || contextPath.isEmpty()) {
			return;
		}
		Assert.isTrue(contextPath.startsWith("/"), () -> "contextPath '" + contextPath + "' must start with '/'.");
		Assert.isTrue(!contextPath.endsWith("/"), () -> "contextPath '" + contextPath + "' must not end with '/'.");
	}


	public void setWebClient(WebClient webClient) {
		Assert.notNull(webClient, "WebClient must not be null");
		this.webClient = webClient;
	}


	@Override
	public WebResponse getResponse(WebRequest webRequest) throws IOException {
		long startTime = System.currentTimeMillis();
		HtmlUnitRequestBuilder requestBuilder = new HtmlUnitRequestBuilder(this.sessions, this.webClient, webRequest);
		requestBuilder.setContextPath(this.contextPath);

		MockHttpServletResponse httpServletResponse = getResponse(requestBuilder);
		String forwardedUrl = httpServletResponse.getForwardedUrl();
		int forwards = 0;
		while (forwardedUrl != null && forwards < MAX_FORWARDS) {
			requestBuilder.setForwardPostProcessor(new ForwardRequestPostProcessor(forwardedUrl));
			httpServletResponse = getResponse(requestBuilder);
			forwardedUrl = httpServletResponse.getForwardedUrl();
			forwards += 1;
		}
		if (forwards == MAX_FORWARDS) {
			throw new IllegalStateException("Forwarded " + forwards + " times in a row, potential infinite forward loop");
		}
		storeCookies(webRequest, httpServletResponse.getCookies());

		return new MockWebResponseBuilder(startTime, webRequest, httpServletResponse).build();
	}

	private MockHttpServletResponse getResponse(RequestBuilder requestBuilder) throws IOException {
		ResultActions resultActions;
		try {
			resultActions = this.mockMvc.perform(requestBuilder);
		}
		catch (Exception ex) {
			throw new IOException(ex);
		}

		return resultActions.andReturn().getResponse();
	}

	private void storeCookies(WebRequest webRequest, jakarta.servlet.http.Cookie[] cookies) {
		Date now = new Date();
		CookieManager cookieManager = this.webClient.getCookieManager();
		for (jakarta.servlet.http.Cookie cookie : cookies) {
			if (cookie.getDomain() == null) {
				cookie.setDomain(webRequest.getUrl().getHost());
			}
			Cookie toManage = createCookie(cookie);
			Date expires = toManage.getExpires();
			if (expires == null || expires.after(now)) {
				cookieManager.addCookie(toManage);
			}
			else {
				cookieManager.removeCookie(toManage);
			}
		}
	}

	@SuppressWarnings("removal")
	private static org.htmlunit.util.Cookie createCookie(jakarta.servlet.http.Cookie cookie) {
		Date expires = null;
		if (cookie.getMaxAge() > -1) {
			expires = new Date(System.currentTimeMillis() + cookie.getMaxAge() * 1000);
		}
		BasicClientCookie result = new BasicClientCookie(cookie.getName(), cookie.getValue());
		result.setDomain(cookie.getDomain());
		result.setComment(cookie.getComment());
		result.setExpiryDate(expires);
		result.setPath(cookie.getPath());
		result.setSecure(cookie.getSecure());
		if (cookie.isHttpOnly()) {
			result.setAttribute("httponly", "true");
		}
		return new org.htmlunit.util.Cookie(result);
	}

	@Override
	public void close() {
	}




	final class HtmlUnitRequestBuilder implements RequestBuilder, Mergeable {

		private final Map<String, MockHttpSession> sessions;

		private final WebClient webClient;

		private final WebRequest webRequest;

		@Nullable
		private String contextPath;

		@Nullable
		private RequestBuilder parentBuilder;

		@Nullable
		private SmartRequestBuilder parentPostProcessor;

		@Nullable
		private RequestPostProcessor forwardPostProcessor;


		/**
		 * Construct a new {@code HtmlUnitRequestBuilder}.
		 * @param sessions a {@link Map} from session {@linkplain HttpSession#getId() IDs}
		 * to currently managed {@link HttpSession} objects; never {@code null}
		 * @param webClient the WebClient for retrieving cookies
		 * @param webRequest the {@link WebRequest} to transform into a
		 * {@link MockHttpServletRequest}; never {@code null}
		 */
		public HtmlUnitRequestBuilder(Map<String, MockHttpSession> sessions, WebClient webClient, WebRequest webRequest) {
			Assert.notNull(sessions, "Sessions Map must not be null");
			Assert.notNull(webClient, "WebClient must not be null");
			Assert.notNull(webRequest, "WebRequest must not be null");
			this.sessions = sessions;
			this.webClient = webClient;
			this.webRequest = webRequest;
		}


		/**
		 * Set the contextPath to be used.
		 * <p>The value may be null in which case the first path segment of the
		 * URL is turned into the contextPath. Otherwise it must conform to
		 * {@link HttpServletRequest#getContextPath()} which states it can be
		 * an empty string, or it must start with a "/" and not end with a "/".
		 * @param contextPath a valid contextPath
		 * @throws IllegalArgumentException if the contextPath is not a valid
		 * {@link HttpServletRequest#getContextPath()}
		 */
		public void setContextPath(@Nullable String contextPath) {
			validateContextPath(contextPath);
			this.contextPath = contextPath;
		}

		public void setForwardPostProcessor(RequestPostProcessor forwardPostProcessor) {
			this.forwardPostProcessor = forwardPostProcessor;
		}


		@Override
		public MockHttpServletRequest buildRequest(ServletContext servletContext) {
			String httpMethod = this.webRequest.getHttpMethod().name();
			UriComponents uri = UriComponentsBuilder.fromUriString(this.webRequest.getUrl().toExternalForm()).build();

			MockHttpServletRequest request = new HtmlUnitRequestBuilder.HtmlUnitMockHttpServletRequest(
				servletContext, httpMethod, (uri.getPath() != null ? uri.getPath() : ""));

			parent(request, this.parentBuilder);

			request.setProtocol("HTTP/1.1");
			request.setScheme(uri.getScheme() != null ? uri.getScheme() : "");
			request.setServerName(uri.getHost() != null ? uri.getHost() : "");  // needs to be first for additional headers
			ports(uri, request);
			authType(request);
			contextPath(request, uri);
			servletPath(uri, request);
			request.setPathInfo(null);

			Charset charset = this.webRequest.getCharset();
			charset = (charset != null ? charset : StandardCharsets.ISO_8859_1);
			request.setCharacterEncoding(charset.name());
			content(request, charset);
			contentType(request);

			cookies(request);
			this.webRequest.getAdditionalHeaders().forEach(request::addHeader);
			locales(request);
			params(request);
			request.setQueryString(uri.getQuery());

			return postProcess(request);
		}

		private void parent(MockHttpServletRequest request, @Nullable RequestBuilder parent) {
			if (parent == null) {
				return;
			}

			MockHttpServletRequest parentRequest = parent.buildRequest(request.getServletContext());

			// session
			HttpSession parentSession = parentRequest.getSession(false);
			if (parentSession != null) {
				HttpSession localSession = request.getSession();
				Assert.state(localSession != null, "No local HttpSession");
				Enumeration<String> attrNames = parentSession.getAttributeNames();
				while (attrNames.hasMoreElements()) {
					String attrName = attrNames.nextElement();
					Object attrValue = parentSession.getAttribute(attrName);
					localSession.setAttribute(attrName, attrValue);
				}
			}

			// header
			Enumeration<String> headerNames = parentRequest.getHeaderNames();
			while (headerNames.hasMoreElements()) {
				String attrName = headerNames.nextElement();
				Enumeration<String> attrValues = parentRequest.getHeaders(attrName);
				while (attrValues.hasMoreElements()) {
					String attrValue = attrValues.nextElement();
					request.addHeader(attrName, attrValue);
				}
			}

			// parameter
			Map<String, String[]> parentParams = parentRequest.getParameterMap();
			parentParams.forEach(request::addParameter);

			// cookie
			jakarta.servlet.http.Cookie[] parentCookies = parentRequest.getCookies();
			if (!ObjectUtils.isEmpty(parentCookies)) {
				request.setCookies(parentCookies);
			}

			// request attribute
			Enumeration<String> parentAttrNames = parentRequest.getAttributeNames();
			while (parentAttrNames.hasMoreElements()) {
				String parentAttrName = parentAttrNames.nextElement();
				request.setAttribute(parentAttrName, parentRequest.getAttribute(parentAttrName));
			}
		}

		private void ports(UriComponents uriComponents, MockHttpServletRequest request) {
			int serverPort = uriComponents.getPort();
			request.setServerPort(serverPort);
			if (serverPort == -1) {
				int portConnection = this.webRequest.getUrl().getDefaultPort();
				request.setLocalPort(serverPort);
				request.setRemotePort(portConnection);
			}
			else {
				request.setRemotePort(serverPort);
			}
		}

		private void authType(MockHttpServletRequest request) {
			String authorization = getHeader("Authorization");
			String[] authSplit = StringUtils.split(authorization, ": ");
			if (authSplit != null) {
				request.setAuthType(authSplit[0]);
			}
		}

		@Nullable
		private String getHeader(String headerName) {
			return this.webRequest.getAdditionalHeaders().get(headerName);
		}

		private void contextPath(MockHttpServletRequest request, UriComponents uriComponents) {
			if (this.contextPath == null) {
				List<String> pathSegments = uriComponents.getPathSegments();
				if (pathSegments.isEmpty()) {
					request.setContextPath("");
				}
				else {
					request.setContextPath("/" + pathSegments.get(0));
				}
			}
			else {
				String path = uriComponents.getPath();
				Assert.isTrue(path != null && path.startsWith(this.contextPath),
					() -> "\"" + uriComponents.getPath() +
						"\" should start with context path \"" + this.contextPath + "\"");
				request.setContextPath(this.contextPath);
			}
		}

		private void servletPath(UriComponents uriComponents, MockHttpServletRequest request) {
			String path = uriComponents.getPath();
			String requestPath = (path != null ? path : "");
			String servletPath = requestPath.substring(request.getContextPath().length());
			servletPath = UriUtils.decode(servletPath, StandardCharsets.UTF_8);
			request.setServletPath(servletPath);
		}

		private void content(MockHttpServletRequest request, Charset charset) {
			String requestBody = this.webRequest.getRequestBody();
			if (requestBody == null) {
				return;
			}
			request.setContent(requestBody.getBytes(charset));
		}

		private void contentType(MockHttpServletRequest request) {
			String contentType = getHeader("Content-Type");
			if (contentType == null) {
				FormEncodingType encodingType = this.webRequest.getEncodingType();
				if (encodingType != null) {
					contentType = encodingType.getName();
				}
			}
			request.setContentType(contentType != null ? contentType : MediaType.ALL_VALUE);
		}

		private void cookies(MockHttpServletRequest request) {
			List<jakarta.servlet.http.Cookie> cookies = new ArrayList<>();

			String cookieHeaderValue = getHeader("Cookie");
			if (cookieHeaderValue != null) {
				StringTokenizer tokens = new StringTokenizer(cookieHeaderValue, "=;");
				while (tokens.hasMoreTokens()) {
					String cookieName = tokens.nextToken().trim();
					Assert.isTrue(tokens.hasMoreTokens(),
						() -> "Expected value for cookie name '" + cookieName +
							"': full cookie header was [" + cookieHeaderValue + "]");
					String cookieValue = tokens.nextToken().trim();
					processCookie(request, cookies, new jakarta.servlet.http.Cookie(cookieName, cookieValue));
				}
			}

			Set<org.htmlunit.util.Cookie> managedCookies = this.webClient.getCookies(this.webRequest.getUrl());
			for (org.htmlunit.util.Cookie cookie : managedCookies) {
				processCookie(request, cookies, new jakarta.servlet.http.Cookie(cookie.getName(), cookie.getValue()));
			}

			jakarta.servlet.http.Cookie[] parentCookies = request.getCookies();
			if (parentCookies != null) {
				Collections.addAll(cookies, parentCookies);
			}

			if (!ObjectUtils.isEmpty(cookies)) {
				request.setCookies(cookies.toArray(new jakarta.servlet.http.Cookie[0]));
			}
		}

		private void processCookie(MockHttpServletRequest request, List<jakarta.servlet.http.Cookie> cookies, jakarta.servlet.http.Cookie cookie) {
			cookies.add(cookie);
			if ("JSESSIONID".equals(cookie.getName())) {
				request.setRequestedSessionId(cookie.getValue());
				request.setSession(httpSession(request, cookie.getValue()));
			}
		}

		private MockHttpSession httpSession(MockHttpServletRequest request, final String sessionid) {
			MockHttpSession session;
			synchronized (this.sessions) {
				session = this.sessions.get(sessionid);
				if (session == null) {
					session = new HtmlUnitRequestBuilder.HtmlUnitMockHttpSession(request, sessionid);
					session.setNew(true);
					synchronized (this.sessions) {
						this.sessions.put(sessionid, session);
					}
					addSessionCookie(request, sessionid);
				}
				else {
					session.setNew(false);
				}
			}
			return session;
		}

		private void addSessionCookie(MockHttpServletRequest request, String sessionid) {
			this.webClient.getCookieManager().addCookie(createCookie(request, sessionid));
		}

		private void removeSessionCookie(MockHttpServletRequest request, String sessionid) {
			this.webClient.getCookieManager().removeCookie(createCookie(request, sessionid));
		}

		private org.htmlunit.util.Cookie createCookie(MockHttpServletRequest request, String sessionid) {
			return new org.htmlunit.util.Cookie(request.getServerName(), "JSESSIONID", sessionid,
				request.getContextPath() + "/", null, request.isSecure(), true);
		}

		private void locales(MockHttpServletRequest request) {
			String locale = getHeader("Accept-Language");
			if (locale == null) {
				request.addPreferredLocale(Locale.getDefault());
			}
		}

		private void params(MockHttpServletRequest request) {
			for (NameValuePair param : this.webRequest.getParameters()) {
				addRequestParameter(request, param);
			}
		}

		private void addRequestParameter(MockHttpServletRequest request, NameValuePair param) {
			if (param instanceof KeyDataPair pair) {
				File file = pair.getFile();
				MockPart part;
				if (file != null) {
					part = new MockPart(pair.getName(), file.getName(), readAllBytes(file));
				}
				else {
					// Support empty file upload OR file upload via setData().
					// For an empty file upload, getValue() returns an empty string, and
					// getData() returns null.
					// For a file upload via setData(), getData() returns the file data, and
					// getValue() returns the file name (if set) or an empty string.
					part = new MockPart(pair.getName(), pair.getValue(), pair.getData());
				}
				MediaType mediaType = (pair.getMimeType() != null ? MediaType.valueOf(pair.getMimeType()) :
					MediaType.APPLICATION_OCTET_STREAM);
				part.getHeaders().setContentType(mediaType);
				request.addPart(part);
			}
			else {
				request.addParameter(param.getName(), param.getValue());
			}
		}

		private byte[] readAllBytes(File file) {
			try {
				return Files.readAllBytes(file.toPath());
			}
			catch (IOException ex) {
				throw new IllegalStateException(ex);
			}
		}

		private MockHttpServletRequest postProcess(MockHttpServletRequest request) {
			if (this.parentPostProcessor != null) {
				request = this.parentPostProcessor.postProcessRequest(request);
			}
			if (this.forwardPostProcessor != null) {
				request = this.forwardPostProcessor.postProcessRequest(request);
			}
			return request;
		}


		/* Mergeable methods */

		@Override
		public boolean isMergeEnabled() {
			return true;
		}

		@Override
		public Object merge(@Nullable Object parent) {
			if (parent instanceof RequestBuilder requestBuilder) {
				if (parent instanceof MockHttpServletRequestBuilder) {
					MockHttpServletRequestBuilder copiedParent = MockMvcRequestBuilders.get("/");
					copiedParent.merge(parent);
					this.parentBuilder = copiedParent;
				}
				else {
					this.parentBuilder = requestBuilder;
				}
				if (parent instanceof SmartRequestBuilder smartRequestBuilder) {
					this.parentPostProcessor = smartRequestBuilder;
				}
			}
			return this;
		}


		/**
		 * An extension to {@link MockHttpServletRequest} that ensures that when a
		 * new {@link HttpSession} is created, it is added to the managed sessions.
		 */
		private final class HtmlUnitMockHttpServletRequest extends MockHttpServletRequest {

			public HtmlUnitMockHttpServletRequest(ServletContext servletContext, String method, String requestURI) {
				super(servletContext, method, requestURI);
			}

			@Override
			public HttpSession getSession(boolean create) {
				HttpSession session = super.getSession(false);
				if (session == null && create) {
					HtmlUnitRequestBuilder.HtmlUnitMockHttpSession newSession = new HtmlUnitRequestBuilder.HtmlUnitMockHttpSession(this);
					setSession(newSession);
					newSession.setNew(true);
					String sessionid = newSession.getId();
					synchronized (HtmlUnitRequestBuilder.this.sessions) {
						HtmlUnitRequestBuilder.this.sessions.put(sessionid, newSession);
					}
					addSessionCookie(this, sessionid);
					session = newSession;
				}
				return session;
			}
		}


		/**
		 * An extension to {@link MockHttpSession} that ensures when
		 * {@link #invalidate()} is called that the {@link HttpSession}
		 * is removed from the managed sessions.
		 */
		private final class HtmlUnitMockHttpSession extends MockHttpSession {

			private final MockHttpServletRequest request;

			public HtmlUnitMockHttpSession(MockHttpServletRequest request) {
				super(request.getServletContext());
				this.request = request;
			}

			private HtmlUnitMockHttpSession(MockHttpServletRequest request, String id) {
				super(request.getServletContext(), id);
				this.request = request;
			}

			@Override
			public void invalidate() {
				super.invalidate();
				synchronized (HtmlUnitRequestBuilder.this.sessions) {
					HtmlUnitRequestBuilder.this.sessions.remove(getId());
				}
				removeSessionCookie(this.request, getId());
			}
		}

	}


	final class MockWebResponseBuilder {

		private static final String DEFAULT_STATUS_MESSAGE = "N/A";


		private final long startTime;

		private final WebRequest webRequest;

		private final MockHttpServletResponse response;


		public MockWebResponseBuilder(long startTime, WebRequest webRequest, MockHttpServletResponse response) {
			Assert.notNull(webRequest, "WebRequest must not be null");
			Assert.notNull(response, "HttpServletResponse must not be null");
			this.startTime = startTime;
			this.webRequest = webRequest;
			this.response = response;
		}


		public WebResponse build() throws IOException {
			WebResponseData webResponseData = webResponseData();
			long endTime = System.currentTimeMillis();
			return new WebResponse(webResponseData, this.webRequest, endTime - this.startTime);
		}

		private WebResponseData webResponseData() throws IOException {
			List<NameValuePair> responseHeaders = responseHeaders();
			int statusCode = (this.response.getRedirectedUrl() != null ?
				HttpStatus.MOVED_PERMANENTLY.value() : this.response.getStatus());
			String statusMessage = statusMessage(statusCode);
			return new WebResponseData(this.response.getContentAsByteArray(), statusCode, statusMessage, responseHeaders);
		}

		private String statusMessage(int statusCode) {
			String errorMessage = this.response.getErrorMessage();
			if (StringUtils.hasText(errorMessage)) {
				return errorMessage;
			}

			try {
				return HttpStatus.valueOf(statusCode).getReasonPhrase();
			}
			catch (IllegalArgumentException ex) {
				// ignore
			}

			return DEFAULT_STATUS_MESSAGE;
		}

		private List<NameValuePair> responseHeaders() {
			Collection<String> headerNames = this.response.getHeaderNames();
			List<NameValuePair> responseHeaders = new ArrayList<>(headerNames.size());
			for (String headerName : headerNames) {
				List<Object> headerValues = this.response.getHeaderValues(headerName);
				for (Object value : headerValues) {
					responseHeaders.add(new NameValuePair(headerName, String.valueOf(value)));
				}
			}
			String location = this.response.getRedirectedUrl();
			if (location != null) {
				responseHeaders.add(new NameValuePair("Location", location));
			}
			return responseHeaders;
		}

	}



	final class ForwardRequestPostProcessor implements RequestPostProcessor {

		private final String forwardedUrl;


		public ForwardRequestPostProcessor(String forwardedUrl) {
			Assert.hasText(forwardedUrl, "Forwarded URL must not be null or empty");
			this.forwardedUrl = forwardedUrl;
		}

		@Override
		public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
			request.setRequestURI(this.forwardedUrl);
			request.setServletPath(initServletPath(request.getContextPath()));
			return request;
		}

		private String initServletPath(String contextPath) {
			if (StringUtils.hasText(contextPath)) {
				Assert.state(this.forwardedUrl.startsWith(contextPath), "Forward supported to same contextPath only");
				return (this.forwardedUrl.length() > contextPath.length() ?
					this.forwardedUrl.substring(contextPath.length()) : "");
			}
			else {
				return this.forwardedUrl;
			}
		}

	}


}
