package ca.uhn.fhir.jpa.packages.loader;

import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.test.utilities.server.HttpServletExtension;
import ca.uhn.fhir.util.ClasspathUtil;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
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
 * <p>
 * The private-network tests at the end of this class cover a second dimension: the allow list matches on scheme,
 * host and path, which says nothing about where the host resolves to, so an entry which does not declare itself
 * private must be refused when its host resolves to a non-public address. Those tests need two entries with
 * differing {@link AllowedUrlPrefix#isPrivateNetwork()} flags reaching the same server, and since the server binds
 * every interface and the allow list matches hosts as strings, {@code localhost} and {@code 127.0.0.1} serve as
 * two distinct entries for it. Every address in play is loopback, so a refusal there can only come from the flag.
 * <p>
 * The blocked-address tests cover a third dimension, which the first two cannot reach. The allow list and the
 * private-network flag are both configuration, so a deployment can permit any address it likes; some addresses
 * must be refused regardless of what configuration says. Those tests use a literal address in the URL, which is
 * the shape an SSRF attempt against a package loader takes. Denial of a <em>hostname</em> which resolves to a
 * blocked address is not covered here: it needs a resolver seam the loader does not yet expose, since the
 * resolution has to be both screened and pinned for the check to mean anything.
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

	/**
	 * The cloud instance metadata endpoint used by AWS, Azure and GCP. This is the address an SSRF against a
	 * package loader is aiming at, since reaching it yields the instance's credentials.
	 */
	private static final String CLOUD_METADATA_ADDRESS = "169.254.169.254";
	/**
	 * Carrier-grade NAT space (100.64.0.0/10). Reachable from a host on such a network, and never a legitimate
	 * package server.
	 */
	private static final String CARRIER_GRADE_NAT_ADDRESS = "100.64.0.1";
	/**
	 * The Oracle Cloud instance metadata endpoint. Serves the same purpose as {@link #CLOUD_METADATA_ADDRESS} on
	 * a different address, and unlike that one it is not caught by any JDK address predicate.
	 */
	private static final String ORACLE_CLOUD_METADATA_ADDRESS = "192.0.0.192";

	private final PackageServingServlet myServlet = new PackageServingServlet();

	@RegisterExtension
	public HttpServletExtension myServer = new HttpServletExtension().withServlet(myServlet);

	private byte[] myPackageContents;
	private PackageUrlAllowList myAllowList;
	private PackageLoaderSvc myPackageLoaderSvc;

	/**
	 * The shared prefix declares itself private because the test server is on loopback, which keeps the
	 * private-network dimension out of play for the path, scheme and redirect tests. The private-network tests
	 * build their own allow lists rather than using this one.
	 */
	@BeforeEach
	void before() {
		myAllowList = PackageUrlAllowList.of(List.of(
			new AllowedUrlPrefix(myServer.getBaseUrl() + ALLOWED_PATH_PREFIX, true)), List.of());
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
					new AllowedUrlPrefix(myServer.getBaseUrl() + ALLOWED_PATH_PREFIX, true)),
			List.of(new AllowedUrlPrefix("classpath://packages", false)));
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
	 * The control for the test below: the same loopback fetch, differing only in the flag.
	 */
	@Test
	void loadPackageUrlContents_whenPrefixPermitsPrivateNetwork_returnsThePackageBytes() {
		PackageLoaderSvc loaderSvc = newLoaderSvc(remoteAllowList(allowedPrefix(myServer.getBaseUrl(), true)));

		byte[] contents = loaderSvc.loadPackageUrlContents(myServer.getBaseUrl() + ALLOWED_PACKAGE_PATH);

		assertThat(contents).isEqualTo(myPackageContents);
	}

	@Test
	void loadPackageUrlContents_whenPrefixForbidsPrivateNetwork_refusesTheFetch() {
		PackageUrlAllowList allowList = remoteAllowList(allowedPrefix(myServer.getBaseUrl(), false));
		PackageLoaderSvc loaderSvc = newLoaderSvc(allowList);

		String packageUrl = myServer.getBaseUrl() + ALLOWED_PACKAGE_PATH;

		// the allow list permits this URL, so only the private-network check can refuse it
		assertThat(allowList.isAllowed(packageUrl)).isTrue();

		assertThatThrownBy(() -> loaderSvc.loadPackageUrlContents(packageUrl))
				.isInstanceOf(InvalidRequestException.class)
				.hasMessageContaining(packageUrl);

		assertThat(myServlet.getAllowedPackageHitCount())
				.as("the package must not be fetched from a host which is not permitted to be private")
				.isZero();
	}

	/**
	 * The control for the test below: the same redirect, differing only in the target host's flag.
	 */
	@Test
	void loadPackageUrlContents_whenRedirectTargetHostPermitsPrivateNetwork_followsTheRedirect() {
		PackageLoaderSvc loaderSvc = newLoaderSvc(remoteAllowList(
				allowedPrefix(loopbackIpBaseUrl(), true), allowedPrefix(myServer.getBaseUrl(), true)));

		myServlet.setRedirectStatus(302);
		myServlet.setRedirectTarget(myServer.getBaseUrl() + ALLOWED_TARGET_PATH);

		byte[] contents = loaderSvc.loadPackageUrlContents(loopbackIpBaseUrl() + REDIRECTING_PACKAGE_PATH);

		assertThat(contents).isEqualTo(myPackageContents);
		assertThat(myServlet.getAllowedTargetHitCount()).isEqualTo(1);
	}

	/**
	 * The first hop is permitted to be private and the second is not, so the flag has to be consulted for the
	 * redirect target rather than only for the URL the caller supplied.
	 */
	@Test
	void loadPackageUrlContents_whenRedirectTargetHostForbidsPrivateNetwork_refusesTheRedirect() {
		PackageUrlAllowList allowList = remoteAllowList(
				allowedPrefix(loopbackIpBaseUrl(), true), allowedPrefix(myServer.getBaseUrl(), false));
		PackageLoaderSvc loaderSvc = newLoaderSvc(allowList);

		String redirectingUrl = loopbackIpBaseUrl() + REDIRECTING_PACKAGE_PATH;
		String targetUrl = myServer.getBaseUrl() + ALLOWED_TARGET_PATH;
		myServlet.setRedirectStatus(302);
		myServlet.setRedirectTarget(targetUrl);

		// both hops are on the allow list, so only the private-network check can refuse the second
		assertThat(allowList.isAllowed(redirectingUrl)).isTrue();
		assertThat(allowList.isAllowed(targetUrl)).isTrue();

		assertThatThrownBy(() -> loaderSvc.loadPackageUrlContents(redirectingUrl))
				.isInstanceOf(InvalidRequestException.class)
				.hasMessageContaining(targetUrl);

		assertThat(myServlet.getAllowedTargetHitCount())
				.as("the redirect must not be followed to a host which is not permitted to be private")
				.isZero();
	}

	/**
	 * A deployment which fetches packages from its own private network sets {@code privateNetwork}, and that flag
	 * suppresses the address check entirely. Nothing about that configuration is unreasonable, yet it currently
	 * leaves the cloud metadata endpoint reachable - so the flag has to stop short of these addresses.
	 * <p>
	 * Pairs with {@link #loadPackageUrlContents_whenPrefixPermitsPrivateNetwork_returnsThePackageBytes()}, which
	 * sets the same flag against an address that is merely private rather than blocked and still fetches. The two
	 * together say the flag is honoured everywhere except here.
	 * <p>
	 * Timed out because the failure mode is an attempted connection to the blocked address, which on some
	 * networks hangs rather than failing - and on a cloud CI runner would succeed.
	 */
	@Timeout(30)
	@ParameterizedTest
	@ValueSource(strings = {CLOUD_METADATA_ADDRESS, CARRIER_GRADE_NAT_ADDRESS, ORACLE_CLOUD_METADATA_ADDRESS})
	void loadPackageUrlContents_whenAddressIsBlockedAndPrefixPermitsPrivateNetwork_refusesTheFetch(
			String theBlockedAddress) {
		PackageUrlAllowList allowList = remoteAllowList(allowedPrefix(baseUrlForAddress(theBlockedAddress), true));
		PackageLoaderSvc loaderSvc = newLoaderSvc(allowList);

		String packageUrl = baseUrlForAddress(theBlockedAddress) + ALLOWED_PACKAGE_PATH;

		// the allow list permits this URL and permits it to be private, so only an unconditional block can refuse it
		assertThat(allowList.isAllowed(packageUrl)).isTrue();
		assertThat(allowList.isPrivateNetworkAllowedForHost(theBlockedAddress)).isTrue();

		assertThatThrownBy(() -> loaderSvc.loadPackageUrlContents(packageUrl))
				.isInstanceOf(InvalidRequestException.class)
				.hasMessageContaining(theBlockedAddress);
	}

	/**
	 * The control for the test above: with the flag off, the existing private-address check already refuses these
	 * addresses. The pair separates the two mechanisms, so that a regression in either is attributable.
	 */
	@Timeout(30)
	@ParameterizedTest
	@ValueSource(strings = {CLOUD_METADATA_ADDRESS, CARRIER_GRADE_NAT_ADDRESS, ORACLE_CLOUD_METADATA_ADDRESS})
	void loadPackageUrlContents_whenAddressIsBlockedAndPrefixForbidsPrivateNetwork_refusesTheFetch(
			String theBlockedAddress) {
		PackageUrlAllowList allowList = remoteAllowList(allowedPrefix(baseUrlForAddress(theBlockedAddress), false));
		PackageLoaderSvc loaderSvc = newLoaderSvc(allowList);

		String packageUrl = baseUrlForAddress(theBlockedAddress) + ALLOWED_PACKAGE_PATH;

		assertThat(allowList.isAllowed(packageUrl)).isTrue();

		assertThatThrownBy(() -> loaderSvc.loadPackageUrlContents(packageUrl))
				.isInstanceOf(InvalidRequestException.class)
				.hasMessageContaining(theBlockedAddress);
	}

	/**
	 * A redirect reaches the blocked address without it appearing in the URL the caller supplied, so the check
	 * has to run per hop rather than once on entry. The first hop is an ordinary allow-listed loopback fetch, and
	 * both prefixes permit private addresses, so only the unconditional block can refuse the second hop.
	 */
	@Timeout(30)
	@Test
	void loadPackageUrlContents_whenRedirectTargetIsBlocked_refusesTheRedirect() {
		String blockedTargetUrl = baseUrlForAddress(CLOUD_METADATA_ADDRESS) + ALLOWED_TARGET_PATH;
		PackageUrlAllowList allowList = remoteAllowList(
				allowedPrefix(myServer.getBaseUrl(), true),
				allowedPrefix(baseUrlForAddress(CLOUD_METADATA_ADDRESS), true));
		PackageLoaderSvc loaderSvc = newLoaderSvc(allowList);

		myServlet.setRedirectStatus(302);
		myServlet.setRedirectTarget(blockedTargetUrl);

		String redirectingUrl = myServer.getBaseUrl() + REDIRECTING_PACKAGE_PATH;

		assertThat(allowList.isAllowed(blockedTargetUrl)).isTrue();

		assertThatThrownBy(() -> loaderSvc.loadPackageUrlContents(redirectingUrl))
				.isInstanceOf(InvalidRequestException.class)
				.hasMessageContaining(CLOUD_METADATA_ADDRESS);
	}

	/**
	 * The same server under a host spelling the allow list treats as distinct from the {@code localhost} of
	 * {@link HttpServletExtension#getBaseUrl()}, since it matches hosts as strings.
	 */
	private String loopbackIpBaseUrl() {
		return baseUrlForAddress("127.0.0.1");
	}

	/**
	 * A base URL naming a literal address. The blocked-address tests are refused before a connection is attempted,
	 * so nothing has to be listening on it; the server's port only keeps the URL the same shape as the others.
	 */
	private String baseUrlForAddress(String theAddress) {
		return "http://" + theAddress + ":" + myServer.getPort();
	}

	private AllowedUrlPrefix allowedPrefix(String theBaseUrl, boolean theIsPrivateNetwork) {
		return new AllowedUrlPrefix(theBaseUrl + ALLOWED_PATH_PREFIX, theIsPrivateNetwork);
	}

	private PackageUrlAllowList remoteAllowList(AllowedUrlPrefix... thePrefixes) {
		return PackageUrlAllowList.of(List.of(thePrefixes), List.of());
	}

	private PackageLoaderSvc newLoaderSvc(PackageUrlAllowList theAllowList) {
		return new PackageLoaderSvc(new PackageLoaderSettings(theAllowList));
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
		private final AtomicInteger myAllowedPackageHitCount = new AtomicInteger();
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

		int getAllowedPackageHitCount() {
			return myAllowedPackageHitCount.get();
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
				case ALLOWED_PACKAGE_PATH -> {
					myAllowedPackageHitCount.incrementAndGet();
					writePackage(theResponse);
				}
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
