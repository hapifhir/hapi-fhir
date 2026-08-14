package ca.uhn.fhir.jpa.packages.loader;

import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.test.utilities.server.HttpServletExtension;
import ca.uhn.fhir.util.ClasspathUtil;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Verifies that {@link PackageLoaderSvc#loadPackageUrlContents(String)} does not follow HTTP redirects.
 * <p>
 * The allow-list is only consulted for the URL the caller supplies. If the loader followed redirects, an
 * allow-listed URL could hand off to any other location and the package bytes would come from there, so the
 * allow-list would be advisory rather than enforced.
 * <p>
 * A single server serves both paths. The allow-list is scoped to a path prefix, so the redirect target is off
 * the list despite sharing an origin with the allow-listed URL.
 */
// Created by Claude Opus 5
public class PackageLoaderSvcRedirectIT {

	private static final String ALLOWED_PATH_PREFIX = "/allowed";
	private static final String REDIRECTING_PACKAGE_PATH = ALLOWED_PATH_PREFIX + "/package.tgz";
	private static final String BLOCKED_PACKAGE_PATH = "/blocked/package.tgz";

	private final RedirectingPackageServlet myServlet = new RedirectingPackageServlet();

	@RegisterExtension
	public HttpServletExtension myServer = new HttpServletExtension().withServlet(myServlet);

	private PackageUrlAllowList myAllowList;
	private PackageLoaderSvc myPackageLoaderSvc;

	@BeforeEach
	void before() {
		myAllowList = PackageUrlAllowList.of(List.of(myServer.getBaseUrl() + ALLOWED_PATH_PREFIX), List.of());
		myPackageLoaderSvc = new PackageLoaderSvc(new PackageLoaderSettings(myAllowList));

		myServlet.setRedirectTarget(myServer.getBaseUrl() + BLOCKED_PACKAGE_PATH);
		myServlet.setPackageContents(ClasspathUtil.loadResourceAsByteArray("/packages/test-exchange-sample.tgz"));
	}

	@Test
	void loadPackageUrlContents_whenAllowedUrlRedirectsToBlockedUrl_doesNotFollowTheRedirect() {
		String allowedUrl = myServer.getBaseUrl() + REDIRECTING_PACKAGE_PATH;
		String blockedUrl = myServer.getBaseUrl() + BLOCKED_PACKAGE_PATH;

		// the URL being requested is on the allow-list, the URL it redirects to is not
		assertThat(myAllowList.isAllowed(allowedUrl)).isTrue();
		assertThat(myAllowList.isAllowed(blockedUrl)).isFalse();

		assertThatThrownBy(() -> myPackageLoaderSvc.loadPackageUrlContents(allowedUrl))
				.isInstanceOf(InvalidRequestException.class)
				.hasMessageContaining("302")
				.hasMessageContaining(allowedUrl);

		assertThat(myServlet.getBlockedPathHitCount())
				.as("the package must not be fetched from the non-allow-listed redirect target")
				.isZero();
	}

	/**
	 * Serves a redirect on the allow-listed path, and the package itself on the blocked path so that a
	 * followed redirect would succeed rather than merely fail differently.
	 */
	private static class RedirectingPackageServlet extends HttpServlet {

		private final AtomicInteger myBlockedPathHitCount = new AtomicInteger();
		private String myRedirectTarget;
		private byte[] myPackageContents;

		void setRedirectTarget(String theRedirectTarget) {
			myRedirectTarget = theRedirectTarget;
		}

		void setPackageContents(byte[] thePackageContents) {
			myPackageContents = thePackageContents;
		}

		int getBlockedPathHitCount() {
			return myBlockedPathHitCount.get();
		}

		@Override
		protected void doGet(HttpServletRequest theRequest, HttpServletResponse theResponse) throws IOException {
			switch (theRequest.getRequestURI()) {
				case REDIRECTING_PACKAGE_PATH -> theResponse.sendRedirect(myRedirectTarget);
				case BLOCKED_PACKAGE_PATH -> {
					myBlockedPathHitCount.incrementAndGet();
					theResponse.setStatus(200);
					theResponse.setHeader("Content-Type", "application/gzip");
					theResponse.getOutputStream().write(myPackageContents);
					theResponse.getOutputStream().close();
				}
				default -> theResponse.sendError(404);
			}
		}
	}
}
