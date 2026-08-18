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
 * fetched, and a redirect is followed only while every hop remains on the allow list.
 * <p>
 * The loader handles redirects itself rather than delegating to the HTTP client, because the client would apply
 * the allow list only to the URL the caller supplied. An allow-listed URL could then hand off to any other
 * location and the package bytes would come from there, leaving the allow list advisory rather than enforced.
 * <p>
 * A single server serves every path. The allow-list is scoped to a path prefix, so a redirect target can be on
 * or off the list while sharing an origin with the URL that redirected to it.
 */
// Created by Claude Opus 5
public class PackageLoaderSvcHttpFetchIT {

	private static final String PACKAGE_CLASSPATH = "/packages/test-exchange-sample.tgz";

	/**
	 * Mirrors the redirect budget in {@link PackageLoaderSvc}, which is private. The chain tests sit either side
	 * of it, so they have to agree on its value.
	 */
	private static final int MAX_REDIRECTS = 5;

	private static final String ALLOWED_PATH_PREFIX = "/allowed";
	private static final String ALLOWED_PACKAGE_PATH = ALLOWED_PATH_PREFIX + "/package.tgz";
	private static final String ALLOWED_TARGET_PATH = ALLOWED_PATH_PREFIX + "/target.tgz";
	private static final String REDIRECTING_PACKAGE_PATH = ALLOWED_PATH_PREFIX + "/redirect.tgz";
	private static final String NO_LOCATION_PATH = ALLOWED_PATH_PREFIX + "/no-location.tgz";
	private static final String LOOP_FIRST_PATH = ALLOWED_PATH_PREFIX + "/loop-first.tgz";
	private static final String LOOP_SECOND_PATH = ALLOWED_PATH_PREFIX + "/loop-second.tgz";
	private static final String CHAIN_PATH_PREFIX = ALLOWED_PATH_PREFIX + "/chain/";
	private static final String BLOCKED_PACKAGE_PATH = "/blocked/package.tgz";

	private final PackageServingServlet myServlet = new PackageServingServlet();

	@RegisterExtension
	public HttpServletExtension myServer = new HttpServletExtension().withServlet(myServlet);

	private byte[] myPackageContents;
	private PackageUrlAllowList myAllowList;
	private PackageLoaderSvc myPackageLoaderSvc;

	@BeforeEach
	void before() {
		myAllowList = PackageUrlAllowList.of(List.of(
			new AllowedUrlPrefix(myServer.getBaseUrl() + ALLOWED_PATH_PREFIX, false, false)), List.of());
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
	 * 304 is deliberately absent from the status lists here and below. It falls inside the redirect range but
	 * carries no Location header, and it cannot arise in these tests because no conditional request headers are
	 * sent.
	 * <p>
	 * Every other redirect status is exercised rather than just 302, because the status check spans the whole
	 * 3xx range. 308 in particular postdates {@link org.apache.http.HttpStatus}, so it has no constant to bound
	 * the range with and is only covered by that range being open-ended.
	 */
	@ParameterizedTest
	@ValueSource(ints = {300, 301, 302, 303, 305, 307, 308})
	void loadPackageUrlContents_whenRedirectTargetIsAllowListed_followsTheRedirect(int theRedirectStatus) {
		myServlet.setRedirectStatus(theRedirectStatus);
		myServlet.setRedirectTarget(myServer.getBaseUrl() + ALLOWED_TARGET_PATH);

		String allowedUrl = myServer.getBaseUrl() + REDIRECTING_PACKAGE_PATH;

		byte[] contents = myPackageLoaderSvc.loadPackageUrlContents(allowedUrl);

		assertThat(contents).isEqualTo(myPackageContents);
		assertThat(myServlet.getAllowedTargetHitCount())
				.as("the package must be fetched from the allow-listed redirect target")
				.isEqualTo(1);
	}

	@ParameterizedTest
	@ValueSource(ints = {300, 301, 302, 303, 305, 307, 308})
	void loadPackageUrlContents_whenRedirectTargetIsNotAllowListed_refusesTheRedirect(int theRedirectStatus) {
		myServlet.setRedirectStatus(theRedirectStatus);

		String allowedUrl = myServer.getBaseUrl() + REDIRECTING_PACKAGE_PATH;
		String blockedUrl = myServer.getBaseUrl() + BLOCKED_PACKAGE_PATH;

		// the URL being requested is on the allow-list, the URL it redirects to is not
		assertThat(myAllowList.isAllowed(allowedUrl)).isTrue();
		assertThat(myAllowList.isAllowed(blockedUrl)).isFalse();

		assertThatThrownBy(() -> myPackageLoaderSvc.loadPackageUrlContents(allowedUrl))
				.isInstanceOf(InvalidRequestException.class)
				.hasMessageContaining(allowedUrl)
				.hasMessageContaining(blockedUrl);

		assertThat(myServlet.getBlockedPathHitCount())
				.as("the package must not be fetched from the non-allow-listed redirect target")
				.isZero();
	}

	/**
	 * A Location may be relative, in which case it resolves against the URL that produced the redirect rather
	 * than against the URL the caller supplied.
	 */
	@Test
	void loadPackageUrlContents_whenLocationIsRelative_resolvesItAgainstTheRedirectingUrl() {
		myServlet.setRedirectStatus(302);
		myServlet.setRedirectTarget("target.tgz");

		byte[] contents = myPackageLoaderSvc.loadPackageUrlContents(myServer.getBaseUrl() + REDIRECTING_PACKAGE_PATH);

		assertThat(contents).isEqualTo(myPackageContents);
		assertThat(myServlet.getAllowedTargetHitCount()).isEqualTo(1);
	}

	/**
	 * A relative Location is checked after resolution, so one which climbs out of the allow-listed path prefix
	 * is refused just as an absolute URL would be.
	 */
	@Test
	void loadPackageUrlContents_whenRelativeLocationEscapesTheAllowedPrefix_refusesTheRedirect() {
		myServlet.setRedirectStatus(302);
		myServlet.setRedirectTarget(".." + BLOCKED_PACKAGE_PATH);

		String allowedUrl = myServer.getBaseUrl() + REDIRECTING_PACKAGE_PATH;

		assertThatThrownBy(() -> myPackageLoaderSvc.loadPackageUrlContents(allowedUrl))
				.isInstanceOf(InvalidRequestException.class)
				.hasMessageContaining(BLOCKED_PACKAGE_PATH);

		assertThat(myServlet.getBlockedPathHitCount()).isZero();
	}

	/**
	 * A redirect must not change scheme, and the allow-list alone does not stop it: an allow-list which permits
	 * a local prefix would answer that a {@code classpath:} target is allowed. The scheme has to be checked
	 * separately, otherwise a remote server could steer the loader at the server's own filesystem or classpath.
	 */
	@Test
	void loadPackageUrlContents_whenRedirectTargetIsNotHttp_refusesTheRedirect() {
		String classpathTarget = "classpath://packages" + PACKAGE_CLASSPATH;
		PackageUrlAllowList allowListWithLocalPrefix = PackageUrlAllowList.of(
				List.of(
					new AllowedUrlPrefix(myServer.getBaseUrl() + ALLOWED_PATH_PREFIX, false, false)),
			List.of(new AllowedUrlPrefix("classpath://packages", false, false)));
		PackageLoaderSvc loaderSvc = new PackageLoaderSvc(new PackageLoaderSettings(allowListWithLocalPrefix));

		// the allow-list permits this target, so only the scheme check can refuse it
		assertThat(allowListWithLocalPrefix.isAllowed(classpathTarget)).isTrue();

		myServlet.setRedirectStatus(302);
		myServlet.setRedirectTarget(classpathTarget);

		String allowedUrl = myServer.getBaseUrl() + REDIRECTING_PACKAGE_PATH;

		assertThatThrownBy(() -> loaderSvc.loadPackageUrlContents(allowedUrl))
				.isInstanceOf(InvalidRequestException.class)
				.hasMessageContaining(allowedUrl)
				.hasMessageContaining(classpathTarget);
	}

	@Test
	void loadPackageUrlContents_whenRedirectHasNoLocationHeader_reportsTheMissingHeader() {
		String noLocationUrl = myServer.getBaseUrl() + NO_LOCATION_PATH;

		assertThatThrownBy(() -> myPackageLoaderSvc.loadPackageUrlContents(noLocationUrl))
				.isInstanceOf(InvalidRequestException.class)
				.hasMessageContaining(noLocationUrl)
				.hasMessageContaining("Location");
	}

	/**
	 * Two allow-listed URLs which redirect to each other would otherwise be followed until the redirect budget
	 * ran out, reporting a limit rather than the loop that caused it.
	 */
	@Test
	void loadPackageUrlContents_whenRedirectsLoop_detectsTheLoop() {
		String loopUrl = myServer.getBaseUrl() + LOOP_FIRST_PATH;
		assertThat(myAllowList.isAllowed(loopUrl)).isTrue();
		assertThat(myAllowList.isAllowed(myServer.getBaseUrl() + LOOP_SECOND_PATH)).isTrue();

		assertThatThrownBy(() -> myPackageLoaderSvc.loadPackageUrlContents(loopUrl))
				.isInstanceOf(InvalidRequestException.class)
				.hasMessageContaining("loop")
				.hasMessageContaining(LOOP_FIRST_PATH)
				.hasMessageContaining(LOOP_SECOND_PATH);
	}

	/**
	 * The last chain entry serves the package, so a chain of exactly the budget's length must succeed. Paired
	 * with the test below, this pins where the budget cuts off rather than only that it does.
	 */
	@Test
	void loadPackageUrlContents_whenChainIsWithinTheRedirectLimit_returnsThePackageBytes() {
		myServlet.setChainLength(MAX_REDIRECTS);

		byte[] contents = myPackageLoaderSvc.loadPackageUrlContents(myServer.getBaseUrl() + CHAIN_PATH_PREFIX + "0");

		assertThat(contents).isEqualTo(myPackageContents);
	}

	@Test
	void loadPackageUrlContents_whenChainExceedsTheRedirectLimit_refusesTheRequest() {
		myServlet.setChainLength(MAX_REDIRECTS + 1);

		String chainStartUrl = myServer.getBaseUrl() + CHAIN_PATH_PREFIX + "0";

		assertThatThrownBy(() -> myPackageLoaderSvc.loadPackageUrlContents(chainStartUrl))
				.isInstanceOf(InvalidRequestException.class)
				.hasMessageContaining(String.valueOf(MAX_REDIRECTS));
	}

	/**
	 * Serves the package on an allow-listed path, on an allow-listed redirect target, and on a blocked path.
	 * The blocked path serves the package too, so that a followed redirect would succeed rather than merely
	 * fail differently, and the hit counts show which target the bytes came from.
	 * <p>
	 * Redirects are written as an explicit status plus Location rather than via
	 * {@link HttpServletResponse#sendRedirect(String)}, which can only produce 302 and resolves relative
	 * targets itself.
	 */
	private static class PackageServingServlet extends HttpServlet {

		private final AtomicInteger myBlockedPathHitCount = new AtomicInteger();
		private final AtomicInteger myAllowedTargetHitCount = new AtomicInteger();
		private String myRedirectTarget;
		private byte[] myPackageContents;
		private int myRedirectStatus;
		private int myChainLength;

		void setRedirectTarget(String theRedirectTarget) {
			myRedirectTarget = theRedirectTarget;
		}

		void setPackageContents(byte[] thePackageContents) {
			myPackageContents = thePackageContents;
		}

		void setRedirectStatus(int theRedirectStatus) {
			myRedirectStatus = theRedirectStatus;
		}

		/**
		 * Sets how many redirects the {@link #CHAIN_PATH_PREFIX} paths issue before one of them serves the
		 * package.
		 */
		void setChainLength(int theChainLength) {
			myChainLength = theChainLength;
		}

		int getBlockedPathHitCount() {
			return myBlockedPathHitCount.get();
		}

		int getAllowedTargetHitCount() {
			return myAllowedTargetHitCount.get();
		}

		@Override
		protected void doGet(HttpServletRequest theRequest, HttpServletResponse theResponse) throws IOException {
			String requestUri = theRequest.getRequestURI();
			if (requestUri.startsWith(CHAIN_PATH_PREFIX)) {
				serveChainEntry(requestUri, theResponse);
				return;
			}

			switch (requestUri) {
				case REDIRECTING_PACKAGE_PATH -> {
					theResponse.setStatus(myRedirectStatus);
					theResponse.setHeader("Location", myRedirectTarget);
				}
				case NO_LOCATION_PATH -> theResponse.setStatus(302);
				case LOOP_FIRST_PATH -> redirectTo(theResponse, LOOP_SECOND_PATH);
				case LOOP_SECOND_PATH -> redirectTo(theResponse, LOOP_FIRST_PATH);
				case BLOCKED_PACKAGE_PATH -> {
					myBlockedPathHitCount.incrementAndGet();
					writePackage(theResponse);
				}
				case ALLOWED_TARGET_PATH -> {
					myAllowedTargetHitCount.incrementAndGet();
					writePackage(theResponse);
				}
				case ALLOWED_PACKAGE_PATH -> writePackage(theResponse);
				default -> theResponse.sendError(404);
			}
		}

		/**
		 * Redirects each chain entry to the next until the configured length is reached, then serves the
		 * package. A request beyond the length means the loader followed more redirects than the chain has.
		 */
		private void serveChainEntry(String theRequestUri, HttpServletResponse theResponse) throws IOException {
			int index = Integer.parseInt(theRequestUri.substring(CHAIN_PATH_PREFIX.length()));
			if (index < myChainLength) {
				redirectTo(theResponse, CHAIN_PATH_PREFIX + (index + 1));
			} else {
				writePackage(theResponse);
			}
		}

		private void redirectTo(HttpServletResponse theResponse, String thePath) {
			theResponse.setStatus(302);
			theResponse.setHeader("Location", thePath);
		}

		private void writePackage(HttpServletResponse theResponse) throws IOException {
			theResponse.setStatus(200);
			theResponse.setHeader("Content-Type", "application/gzip");
			theResponse.getOutputStream().write(myPackageContents);
			theResponse.getOutputStream().close();
		}
	}
}
