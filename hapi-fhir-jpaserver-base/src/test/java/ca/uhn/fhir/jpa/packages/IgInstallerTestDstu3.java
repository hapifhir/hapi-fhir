package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.dao.data.INpmPackageVersionDao;
import ca.uhn.fhir.jpa.dao.dstu3.BaseJpaDstu3Test;
import ca.uhn.fhir.test.utilities.JettyUtil;
import ca.uhn.fhir.test.utilities.ProxyUtil;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.utilities.cache.IPackageCacheManager;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;

import static ca.uhn.fhir.util.ClasspathUtil.loadResourceAsByteArray;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class IgInstallerTestDstu3 extends BaseJpaDstu3Test {

	@Autowired
	private DaoConfig daoConfig;
	@Autowired
	private NpmInstallerSvcImpl igInstaller;
	@Autowired
	private IPackageCacheManager myPackageCacheManager;
	private Server myServer;
	private NpmTestR4.FakeNpmServlet myFakeNpmServlet;
	@Autowired
	private INpmPackageVersionDao myPackageVersionDao;

	@Before
	public void before() throws Exception {
		JpaPackageCache jpaPackageCache = ProxyUtil.getSingletonTarget(myPackageCacheManager, JpaPackageCache.class);

		myServer = new Server(0);
		ServletHandler proxyHandler = new ServletHandler();
		myFakeNpmServlet = new NpmTestR4.FakeNpmServlet();
		ServletHolder servletHolder = new ServletHolder(myFakeNpmServlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		myServer.setHandler(proxyHandler);
		myServer.start();

		int port = JettyUtil.getPortForStartedServer(myServer);
		jpaPackageCache.getPackageServers().clear();
		jpaPackageCache.addPackageServer("http://localhost:" + port);

		myFakeNpmServlet.getResponses().clear();

		InputStream stream;
	}

	@After
	public void after() throws Exception {
		JettyUtil.closeServer(myServer);
		daoConfig.setAllowExternalReferences(new DaoConfig().isAllowExternalReferences());
	}

	@Test
	public void negativeTestInstallFromCache() {
		daoConfig.setAllowExternalReferences(true);

		byte[] bytes = loadResourceAsByteArray("/packages/erroneous-ig.tar.gz");

		// Unknown base of StructureDefinitions
		try {
			igInstaller.install(new NpmInstallationSpec().setPackageId("erroneous-ig").setPackageVersion("1.0.2").setInstallMode(NpmInstallationSpec.InstallModeEnum.CACHE_AND_INSTALL).setContents(bytes));
			fail();
		} catch (ImplementationGuideInstallationException e) {
			Assert.assertThat(e.getMessage(), containsString("Failure when generating snapshot of StructureDefinition"));
		}
	}

	@Test
	public void testInstallPackageWithDependencies() {
		byte[] bytes;

		bytes = loadResourceAsByteArray("/packages/nictiz.fhir.nl.stu3.questionnaires-1.0.2.tgz");
		myFakeNpmServlet.getResponses().put("/nictiz.fhir.nl.stu3.questionnaires/1.0.2", bytes);

		bytes = loadResourceAsByteArray("/packages/nictiz.fhir.nl.stu3.zib2017-1.3.10.tgz");
		myFakeNpmServlet.getResponses().put("/nictiz.fhir.nl.stu3.zib2017/1.3.x", bytes);

		daoConfig.setAllowExternalReferences(true);
		igInstaller.install(new NpmInstallationSpec().setPackageId("nictiz.fhir.nl.stu3.questionnaires").setPackageVersion("1.0.2").setInstallMode(NpmInstallationSpec.InstallModeEnum.CACHE_AND_INSTALL).setFetchDependencies(true));

		runInTransaction(()->{
			assertTrue(myPackageVersionDao.findByPackageIdAndVersion("nictiz.fhir.nl.stu3.questionnaires", "1.0.2").isPresent());
			assertTrue(myPackageVersionDao.findByPackageIdAndVersion("nictiz.fhir.nl.stu3.zib2017", "1.3.10").isPresent());
		});

	}


	@Test
	public void testInstallPackageWithoutDependencies() {
		byte[] bytes;

		bytes = loadResourceAsByteArray("/packages/nictiz.fhir.nl.stu3.questionnaires-1.0.2.tgz");
		myFakeNpmServlet.getResponses().put("/nictiz.fhir.nl.stu3.questionnaires/1.0.2", bytes);

		bytes = loadResourceAsByteArray("/packages/nictiz.fhir.nl.stu3.zib2017-1.3.10.tgz");
		myFakeNpmServlet.getResponses().put("/nictiz.fhir.nl.stu3.zib2017/1.3.x", bytes);

		daoConfig.setAllowExternalReferences(true);
		igInstaller.install(new NpmInstallationSpec().setPackageId("nictiz.fhir.nl.stu3.questionnaires").setPackageVersion("1.0.2").setInstallMode(NpmInstallationSpec.InstallModeEnum.CACHE_AND_INSTALL).setFetchDependencies(false));

		runInTransaction(()->{
			assertTrue(myPackageVersionDao.findByPackageIdAndVersion("nictiz.fhir.nl.stu3.questionnaires", "1.0.2").isPresent());
			assertFalse(myPackageVersionDao.findByPackageIdAndVersion("nictiz.fhir.nl.stu3.zib2017", "1.3.10").isPresent());
		});

	}

	@Test
	public void installFromCache2() {
		byte[] bytes = loadResourceAsByteArray("/packages/basisprofil.de.tar.gz");
		myFakeNpmServlet.getResponses().put("/basisprofil.de/0.2.40", bytes);

		igInstaller.install(new NpmInstallationSpec().setPackageId("basisprofil.de").setPackageVersion("0.2.40").setInstallMode(NpmInstallationSpec.InstallModeEnum.CACHE_AND_INSTALL));
	}
}
