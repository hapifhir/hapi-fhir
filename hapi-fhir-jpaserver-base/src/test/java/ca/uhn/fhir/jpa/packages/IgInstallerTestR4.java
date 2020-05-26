package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.dao.r4.BaseJpaR4Test;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.test.utilities.JettyUtil;
import ca.uhn.fhir.test.utilities.ProxyUtil;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.utilities.cache.IPackageCacheManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class IgInstallerTestR4 extends BaseJpaR4Test {

	private static final Logger ourLog = LoggerFactory.getLogger(FakeNpmServlet.class);
	@Autowired
	public NpmInstallerSvc igInstaller;
	@Autowired
	private IPackageCacheManager myPackageCacheManager;
	private Server myServer;
	private Map<String, byte[]> myResponses = new HashMap<>();

	@Before
	public void before() throws Exception {
		JpaPackageCache jpaPackageCache = ProxyUtil.getSingletonTarget(myPackageCacheManager, JpaPackageCache.class);

		InputStream stream;
		stream = IgInstallerTestDstu3.class.getResourceAsStream("/packages/NHSD.Assets.STU3.tar.gz");
		myPackageCacheManager.addPackageToCache("NHSD.Assets.STU3", "1.0.0", stream, "NHSD.Assets.STU3");

		myServer = new Server(0);
		ServletHandler proxyHandler = new ServletHandler();
		FakeNpmServlet fakeNpmServlet = new FakeNpmServlet();
		ServletHolder servletHolder = new ServletHolder(fakeNpmServlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		myServer.setHandler(proxyHandler);
		myServer.start();

		int port = JettyUtil.getPortForStartedServer(myServer);
		jpaPackageCache.getPackageServers().clear();
		jpaPackageCache.addPackageServer("http://localhost:" + port);

		myResponses.clear();
	}

	@After
	public void after() throws Exception {
		JettyUtil.closeServer(myServer);
	}

	@Test
	public void negativeTestInstallFromCacheVersionMismatch() {
		myDaoConfig.setAllowExternalReferences(true);

		try {
			igInstaller.install(new NpmInstallationSpec().setPackageId("NHSD.Assets.STU3").setPackageVersion("1.2.0").setInstallMode(NpmInstallationSpec.InstallModeEnum.CACHE_AND_INSTALL));
			fail();
		} catch (ImplementationGuideInstallationException e) {
			assertEquals("Could not load NPM package NHSD.Assets.STU3#1.2.0", e.getMessage());
		}

	}

	@Test
	public void installRemotely() throws IOException {
		myDaoConfig.setAllowExternalReferences(true);

		byte[] bytes = loadClasspathBytes("/packages/hl7.fhir.uv.shorthand-0.12.0.tgz");
		myResponses.put("/hl7.fhir.uv.shorthand/0.12.0", bytes);

		NpmInstallationSpec spec = new NpmInstallationSpec().setPackageId("hl7.fhir.uv.shorthand").setPackageVersion("0.12.0").setInstallMode(NpmInstallationSpec.InstallModeEnum.CACHE_AND_INSTALL);
		igInstaller.install(spec);

	}

	private class FakeNpmServlet extends HttpServlet {

		@Override
		protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			String requestUrl = req.getRequestURI();
			if (myResponses.containsKey(requestUrl)) {
				ourLog.info("Responding to request: {}", requestUrl);

				resp.setStatus(200);
				resp.setHeader(Constants.HEADER_CONTENT_TYPE, "application/gzip");
				resp.getOutputStream().write(myResponses.get(requestUrl));
				resp.getOutputStream().close();
			} else {
				ourLog.warn("Unknown request: {}", requestUrl);

				resp.sendError(404);
			}

		}
	}
}
