package ca.uhn.fhir.jpa.packages.loader;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.packages.FakeNpmServlet;
import ca.uhn.fhir.jpa.packages.util.PackageUtils;
import ca.uhn.fhir.test.utilities.JettyUtil;
import ca.uhn.fhir.util.ClasspathUtil;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.utilities.npm.NpmPackage;
import org.hl7.fhir.utilities.npm.PackageServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.Spy;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class PackageLoaderSvcIT {

	@Spy
	private FhirContext myFhirContext = FhirContext.forR4Cached();

	private Server myServer;

	private FakeNpmServlet myFakeNpmServlet;

	private PackageLoaderSvc myPackageLoaderSvc;

	private PackageResourceParsingSvc myResourceParsingSvc;

	@BeforeEach
	public void before() throws Exception {
		myPackageLoaderSvc = new PackageLoaderSvc();
		myResourceParsingSvc = new PackageResourceParsingSvc(myFhirContext);

		myServer = new Server(0);
		ServletHandler proxyHandler = new ServletHandler();
		myFakeNpmServlet = new FakeNpmServlet();
		ServletHolder servletHolder = new ServletHolder(myFakeNpmServlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		myServer.setHandler(proxyHandler);
		myServer.start();

		int port = JettyUtil.getPortForStartedServer(myServer);
		myPackageLoaderSvc.getPackageServers().clear();
		myPackageLoaderSvc.addPackageServer(new PackageServer("http://localhost:" + port));

		myFakeNpmServlet.getResponses().clear();
	}

	@AfterEach
	public void after() throws Exception {
		JettyUtil.closeServer(myServer);
	}

	@Test
	public void fetchPackageFromServer_thenParseoutResources_inMemory() throws IOException {
		// setup
		String id = "test-exchange.fhir.us.com/2.1.1";
		String versionId = "2.1.";
		// this package has SearchParameters in it
		byte[] bytes = ClasspathUtil.loadResourceAsByteArray("/packages/test-exchange-sample.tgz");
		myFakeNpmServlet.getResponses().put(String.format("/%s/%s", id, versionId), bytes);

		// test fetch from server by id and version
		NpmPackageData result = myPackageLoaderSvc.fetchPackageFromPackageSpec(id, versionId);

		// verify fetched data
		assertNotNull(result);
		assertNotNull(result.getPackage());
		NpmPackage npmPackage = result.getPackage();

		// test parse resources
		List<IBaseResource> resources = new ArrayList<>();
		List<String> resourcesToParse = PackageUtils.DEFAULT_INSTALL_TYPES;
		for (String resourceType : resourcesToParse) {
			resources.addAll(
				myResourceParsingSvc.parseResourcesOfType(resourceType, npmPackage)
			);
		}

		// verify fetched resources
		assertFalse(resources.isEmpty());
		assertEquals(1, resources.size());
		assertEquals("SearchParameter", resources.get(0).fhirType());
	}

	/**
	 * PackageLoaderSvc extends BasePackageCacheManger.
	 * However, we do not want this service to have any
	 * DAO dependence (ie, no cache).
	 *
	 * But since BasePackageCacheManger is in a different
	 * codebase, we cannot remove some methods and must just
	 * not support them.
	 *
	 * We'll test to make sure these stay unsupported
	 * (barring a breakup of BasePackageCacheManager itself)
	 */
	@Test
	public void anyCacheUtilizingMethod_throwsUnsupported() throws IOException {
		// loadPackageFromCacheOnly
		try {
			myPackageLoaderSvc.loadPackageFromCacheOnly("id", "versionId");
			fail();
		} catch (UnsupportedOperationException ex) {
			assertTrue(ex.getMessage().contains("Cannot load from cache."));
		}

		// addPackageToCache
		try {
			myPackageLoaderSvc.addPackageToCache("id", "version", Mockito.mock(InputStream.class), "description or url");
			fail();
		} catch (UnsupportedOperationException ex) {
			assertTrue(ex.getMessage().contains("Cannot add to cache."));
		}

		// loadPackage
		try {
			myPackageLoaderSvc.loadPackage("id", "version");
			fail();
		} catch (UnsupportedOperationException ex) {
			assertTrue(ex.getMessage().contains("No packages are cached;"));
		}
	}
}
