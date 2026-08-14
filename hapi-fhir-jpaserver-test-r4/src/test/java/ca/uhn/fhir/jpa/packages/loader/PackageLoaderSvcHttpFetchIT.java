package ca.uhn.fhir.jpa.packages.loader;

import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.test.utilities.server.HttpServletExtension;
import ca.uhn.fhir.util.ClasspathUtil;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Covers the HTTP branch of {@link PackageLoaderSvc#loadPackageUrlContents(String)}: an allow-listed URL is
 * fetched, and a redirect away from an allow-listed URL is refused rather than followed.
 * <p>
 * The allow-list is only consulted for the URL the caller supplies. If the loader followed redirects, an
 * allow-listed URL could hand off to any other location and the package bytes would come from there, so the
 * allow-list would be advisory rather than enforced.
 * <p>
 * A single server serves every path. The allow-list is scoped to a path prefix, so the redirect target is off
 * the list despite sharing an origin with the allow-listed URLs.
 */
// Created by Claude Opus 5
public class PackageLoaderSvcHttpFetchIT {

	private static final String PACKAGE_CLASSPATH = "/packages/test-exchange-sample.tgz";

	private static final String ALLOWED_PATH_PREFIX = "/allowed";
	private static final String ALLOWED_PACKAGE_PATH = ALLOWED_PATH_PREFIX + "/package.tgz";
	private static final String REDIRECTING_PACKAGE_PATH = ALLOWED_PATH_PREFIX + "/redirect.tgz";
	private static final String BLOCKED_PACKAGE_PATH = "/blocked/package.tgz";

	private final PackageServingServlet myServlet = new PackageServingServlet();

	@RegisterExtension
	public HttpServletExtension myServer = new HttpServletExtension().withServlet(myServlet);

	private byte[] myPackageContents;
	private PackageUrlAllowList myAllowList;
	private PackageLoaderSvc myPackageLoaderSvc;

	@BeforeEach
	void before() {
		myAllowList = PackageUrlAllowList.of(List.of(myServer.getBaseUrl() + ALLOWED_PATH_PREFIX), List.of());
		myPackageLoaderSvc = new PackageLoaderSvc(new PackageLoaderSettings(myAllowList));

		myPackageContents = ClasspathUtil.loadResourceAsByteArray(PACKAGE_CLASSPATH);
		myServlet.setPackageContents(myPackageContents);
		myServlet.setRedirectTarget(myServer.getBaseUrl() + BLOCKED_PACKAGE_PATH);
	}

	@Test
	void loadPackageUrlContents_whenAllowedUrlReturnsOk_returnsThePackageBytes() {
		String allowedUrl = myServer.getBaseUrl() + ALLOWED_PACKAGE_PATH;
		assertThat(myAllowList.isAllowed(allowedUrl)).isTrue();

		byte[] contents = myPackageLoaderSvc.loadPackageUrlContents(allowedUrl);

		assertThat(contents).isEqualTo(myPackageContents);
	}

	/**
	 * 304 is deliberately absent. It falls inside the range the loader rejects, but it carries no Location
	 * header and cannot arise here because no conditional request headers are sent.
	 * <p>
	 * Every other redirect status is exercised rather than just 302, because the status check spans the whole
	 * 3xx range. 308 in particular postdates {@link org.apache.http.HttpStatus}, so it has no constant to
	 * bound the range with and is only covered by that range being open-ended.
	 */
	@ParameterizedTest
	@ValueSource(ints = {300, 301, 302, 303, 305, 307, 308})
	void loadPackageUrlContents_whenAllowedUrlRedirectsToBlockedUrl_doesNotFollowTheRedirect(int theRedirectStatus) {
		myServlet.setRedirectStatus(theRedirectStatus);

		String allowedUrl = myServer.getBaseUrl() + REDIRECTING_PACKAGE_PATH;
		String blockedUrl = myServer.getBaseUrl() + BLOCKED_PACKAGE_PATH;

		// the URL being requested is on the allow-list, the URL it redirects to is not
		assertThat(myAllowList.isAllowed(allowedUrl)).isTrue();
		assertThat(myAllowList.isAllowed(blockedUrl)).isFalse();

		assertThatThrownBy(() -> myPackageLoaderSvc.loadPackageUrlContents(allowedUrl))
				.isInstanceOf(InvalidRequestException.class)
				.hasMessageContaining(String.valueOf(theRedirectStatus))
				.hasMessageContaining(allowedUrl);

		assertThat(myServlet.getBlockedPathHitCount())
				.as("the package must not be fetched from the non-allow-listed redirect target")
				.isZero();
	}

	/**
	 * Serves the package on both an allow-listed path and a blocked path, and a redirect on a third path. The
	 * blocked path serves the package too, so that a followed redirect would succeed rather than merely fail
	 * differently, and its hit count shows whether the redirect was followed.
	 * <p>
	 * The redirect is written as an explicit status plus Location rather than via
	 * {@link HttpServletResponse#sendRedirect(String)}, which can only produce 302.
	 */
	private static class PackageServingServlet extends HttpServlet {

		private final AtomicInteger myBlockedPathHitCount = new AtomicInteger();
		private String myRedirectTarget;
		private byte[] myPackageContents;
		private int myRedirectStatus;

		void setRedirectTarget(String theRedirectTarget) {
			myRedirectTarget = theRedirectTarget;
		}

		void setPackageContents(byte[] thePackageContents) {
			myPackageContents = thePackageContents;
		}

		void setRedirectStatus(int theRedirectStatus) {
			myRedirectStatus = theRedirectStatus;
		}

		int getBlockedPathHitCount() {
			return myBlockedPathHitCount.get();
		}

		@Override
		protected void doGet(HttpServletRequest theRequest, HttpServletResponse theResponse) throws IOException {
			switch (theRequest.getRequestURI()) {
				case REDIRECTING_PACKAGE_PATH -> {
					theResponse.setStatus(myRedirectStatus);
					theResponse.setHeader("Location", myRedirectTarget);
				}
				case BLOCKED_PACKAGE_PATH -> {
					myBlockedPathHitCount.incrementAndGet();
					writePackage(theResponse);
				}
				case ALLOWED_PACKAGE_PATH -> writePackage(theResponse);
				default -> theResponse.sendError(404);
			}
		}

		private void writePackage(HttpServletResponse theResponse) throws IOException {
			theResponse.setStatus(200);
			theResponse.setHeader("Content-Type", "application/gzip");
			theResponse.getOutputStream().write(myPackageContents);
			theResponse.getOutputStream().close();
		}
	}
}
