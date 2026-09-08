package ca.uhn.fhir.jpa.packages.loader;

import org.apache.commons.lang3.stream.Streams;
import org.apache.http.conn.DnsResolver;
import org.hl7.fhir.utilities.http.ManagedWebAccessUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

public class PackageLoaderDnsResolverTest {

	@ParameterizedTest
	@CsvSource(value = {
		// all the 100.64.0.0 values
		"100.64.0.0,true",
		"100.100.50.25,true",
		"100.127.255.255,true",
		"100.63.255.255,false",
		"100.128.0.0,false",
		"99.64.0.1,false",
		// others
		"192.0.0.192,true",
		"169.254.169.254,true"
	})
	public void resolve_hostnameResolvesToBlockedAddress_blocksHostname(String theIp, boolean theShouldBlock) {
		// setup
		String host = "package.com";

		PackageLoaderDnsResolver pkgDnsResolver = new PackageLoaderDnsResolver(
			PackageUrlAllowList.allowAll(),
			resolvesTo(theIp)
		);

		// test
		try {
			InetAddress[] addresses = pkgDnsResolver.resolve(host);
			if (theShouldBlock) {
				fail("Redirected to blocked host " + theIp);
			} else {
				assertTrue(addresses.length >= 1);
				assertTrue(Streams.of(addresses)
					.anyMatch(a -> a.getHostName().equalsIgnoreCase(host)));
			}
		} catch (UnknownHostException ex) {
			if (!theShouldBlock) {
				fail("Blocked address it shouldn't've: " + theIp);
			} else {
				assertTrue(ex.getMessage()
					.contains("Refusing to connect to " + host
						+ "; resolved address " + theIp));
			}
		}
	}

	@Test
	public void resolve_hostnameResolvesMultiAddressSomeBlocked_blocksHostname() {
		// setup
		String host = "package.com";
		String wrongIp = "169.254.169.254";

		PackageLoaderDnsResolver pkgDnsResolver = new PackageLoaderDnsResolver(
			PackageUrlAllowList.allowAll(),
			resolvesTo("93.184.216.34", wrongIp)
		);

		// test
		try {
			pkgDnsResolver.resolve(host);
			fail("Redirected to blocked host");
		} catch (UnknownHostException ex) {
			assertTrue(ex.getMessage()
				.contains("Refusing to connect to " + host)
				&& ex.getMessage().contains(wrongIp + " is blocked"),
				ex.getMessage());
		}
	}

	@Test
	public void resolve_privateHostAllowed_allows() {
		// setup
		String host = "pkg.com";
		String ip = "93.184.216.34";
		AllowedUrlPrefix prefix = new AllowedUrlPrefix("http://" + host, true);
		PackageLoaderDnsResolver resolver = new PackageLoaderDnsResolver(
			PackageUrlAllowList.of(
				List.of(prefix),
				List.of()
			),
			resolvesTo(ip)
		);

		// test
		try (MockedStatic<ManagedWebAccessUtils> mocked = Mockito.mockStatic(ManagedWebAccessUtils.class)) {
			resolver.resolve("pkg.com");

			// verify
			mocked.verifyNoInteractions();
		} catch (UnknownHostException ex) {
			fail(ex.getMessage());
		}
	}

	@Test
	public void resolve_nonPrivateHost_blocks() {
		// setup
		String host = "pkg.com";
		String ip = "93.184.216.34";
		AllowedUrlPrefix prefix = new AllowedUrlPrefix("http://" + host, false);
		PackageLoaderDnsResolver resolver = new PackageLoaderDnsResolver(
			PackageUrlAllowList.of(
				List.of(prefix),
				List.of()
			),
			resolvesTo(ip)
		);

		// test
		try (MockedStatic<ManagedWebAccessUtils> mocked = Mockito.mockStatic(ManagedWebAccessUtils.class)) {
			mocked.when(() -> ManagedWebAccessUtils.throwExceptionIfNonPublicAddress(any(), anyString()))
					.thenThrow(new IOException("boom"));

			resolver.resolve("pkg.com");
			fail("Private not allowed, but passed anyways");
		} catch (UnknownHostException ex) {
			assertTrue(ex.getMessage().contains("non-public"));
		}
	}

	private static InetAddress toAddress(String theHost, String theAddress) {
		try {
			return InetAddress.getByAddress(theHost, InetAddress.getByName(theAddress).getAddress());
		} catch (UnknownHostException ex) {
			fail(ex);
			return null;
		}
	}

	private static DnsResolver resolvesTo(String... theAddresses) {
		return host -> Stream.of(theAddresses)
			.map(a -> toAddress(host, a))
			.toArray(InetAddress[]::new);
	}
}
