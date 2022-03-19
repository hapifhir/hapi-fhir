package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.dao.data.INpmPackageVersionDao;
import ca.uhn.fhir.jpa.test.BaseJpaDstu3Test;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.test.utilities.JettyUtil;
import ca.uhn.fhir.test.utilities.ProxyUtil;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.utilities.npm.IPackageCacheManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Collectors;

import static ca.uhn.fhir.util.ClasspathUtil.loadResourceAsByteArray;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class IgInstallerDstu3Test extends BaseJpaDstu3Test {

	private static final Logger ourLog = LoggerFactory.getLogger(IgInstallerDstu3Test.class);
	@Autowired
	private DaoConfig daoConfig;
	@Autowired
	private PackageInstallerSvcImpl igInstaller;
	@Autowired
	private IPackageCacheManager myPackageCacheManager;
	private Server myServer;
	private NpmR4Test.FakeNpmServlet myFakeNpmServlet;
	@Autowired
	private INpmPackageVersionDao myPackageVersionDao;
	private int myPort;

	@BeforeEach
	public void before() throws Exception {
		JpaPackageCache jpaPackageCache = ProxyUtil.getSingletonTarget(myPackageCacheManager, JpaPackageCache.class);

		myServer = new Server(0);
		ServletHandler proxyHandler = new ServletHandler();
		myFakeNpmServlet = new NpmR4Test.FakeNpmServlet();
		ServletHolder servletHolder = new ServletHolder(myFakeNpmServlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		myServer.setHandler(proxyHandler);
		myServer.start();

		myPort = JettyUtil.getPortForStartedServer(myServer);
		jpaPackageCache.getPackageServers().clear();
		jpaPackageCache.addPackageServer("http://localhost:" + myPort);

		myFakeNpmServlet.getResponses().clear();
	}

	@AfterEach
	public void after() throws Exception {
		JettyUtil.closeServer(myServer);
		daoConfig.setAllowExternalReferences(new DaoConfig().isAllowExternalReferences());
	}

	@Test
	public void testNegativeInstallFromCache() {
		daoConfig.setAllowExternalReferences(true);

		byte[] bytes = loadResourceAsByteArray("/packages/erroneous-ig.tar.gz");

		// That patient profile in this NPM package has an invalid base
		try {
			igInstaller.install(new PackageInstallationSpec().setName("erroneous-ig").setVersion("1.0.2").setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_AND_INSTALL).setPackageContents(bytes));
			fail();
		} catch (ImplementationGuideInstallationException e) {
			assertThat(e.getMessage(), containsString("Could not load NPM package erroneous-ig#1.0.2"));
		}
	}

	@Test
	public void testInstallPackageWithDependencies() {
		byte[] bytes;

		bytes = loadResourceAsByteArray("/packages/nictiz.fhir.nl.stu3.questionnaires-1.0.2.tgz");
		myFakeNpmServlet.getResponses().put("/nictiz.fhir.nl.stu3.questionnaires/1.0.2", bytes);

		bytes = loadResourceAsByteArray("/packages/nictiz.fhir.nl.stu3.zib2017.json");
		myFakeNpmServlet.getResponses().put("/nictiz.fhir.nl.stu3.zib2017", bytes);

		bytes = loadResourceAsByteArray("/packages/nictiz.fhir.nl.stu3.zib2017-1.3.10.tgz");
		myFakeNpmServlet.getResponses().put("/nictiz.fhir.nl.stu3.zib2017/1.3.10", bytes);

		daoConfig.setAllowExternalReferences(true);
		PackageInstallationSpec spec = new PackageInstallationSpec()
			.setName("nictiz.fhir.nl.stu3.questionnaires")
			.setVersion("1.0.2")
			.setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_ONLY)
			.setFetchDependencies(true)
			.addDependencyExclude("hl7\\.fhir\\.[a-zA-Z0-9]+\\.core");
		PackageInstallOutcomeJson outcome = igInstaller.install(spec);
		ourLog.info("Install messages:\n * {}", outcome.getMessage().stream().collect(Collectors.joining("\n * ")));
		assertThat(outcome.getMessage(), hasItem("Indexing StructureDefinition Resource[package/vl-QuestionnaireProvisioningTask.json] with URL: http://nictiz.nl/fhir/StructureDefinition/vl-QuestionnaireProvisioningTask|1.0.1"));

		runInTransaction(() -> {
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
		igInstaller.install(new PackageInstallationSpec().setName("nictiz.fhir.nl.stu3.questionnaires").setVersion("1.0.2").setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_AND_INSTALL).setFetchDependencies(false));

		runInTransaction(() -> {
			assertTrue(myPackageVersionDao.findByPackageIdAndVersion("nictiz.fhir.nl.stu3.questionnaires", "1.0.2").isPresent());
			assertFalse(myPackageVersionDao.findByPackageIdAndVersion("nictiz.fhir.nl.stu3.zib2017", "1.3.10").isPresent());
		});

	}

	@Test
	public void testInstallPackageByUrl_Http() {
		byte[] bytes;

		bytes = loadResourceAsByteArray("/packages/nictiz.fhir.nl.stu3.questionnaires-1.0.2.tgz");
		myFakeNpmServlet.getResponses().put("/foo.tgz", bytes);

		igInstaller.install(new PackageInstallationSpec()
			.setName("nictiz.fhir.nl.stu3.questionnaires")
			.setVersion("1.0.2")
			.setPackageUrl("http://localhost:" + myPort + "/foo.tgz")
		);

		runInTransaction(() -> {
			assertTrue(myPackageVersionDao.findByPackageIdAndVersion("nictiz.fhir.nl.stu3.questionnaires", "1.0.2").isPresent());
		});

	}

	@Test
	public void testInstallPackageByUrl_Classpath() {
		byte[] bytes;

		igInstaller.install(new PackageInstallationSpec()
			.setName("nictiz.fhir.nl.stu3.questionnaires")
			.setVersion("1.0.2")
			.setPackageUrl("classpath:/packages/nictiz.fhir.nl.stu3.questionnaires-1.0.2.tgz")
		);

		runInTransaction(() -> {
			assertTrue(myPackageVersionDao.findByPackageIdAndVersion("nictiz.fhir.nl.stu3.questionnaires", "1.0.2").isPresent());
		});

	}

	@Test
	public void testInstallPackageByUrl_WrongPackageId() {
		byte[] bytes;

		bytes = loadResourceAsByteArray("/packages/nictiz.fhir.nl.stu3.questionnaires-1.0.2.tgz");
		myFakeNpmServlet.getResponses().put("/foo.tgz", bytes);

		try {
			igInstaller.install(new PackageInstallationSpec()
				.setName("blah")
				.setVersion("1.0.2")
				.setPackageUrl("http://localhost:" + myPort + "/foo.tgz")
			);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(1297) + "Package ID nictiz.fhir.nl.stu3.questionnaires doesn't match expected: blah", e.getMessage());
		}

	}

	@Test
	public void testInstallPackageByUrl_FailingUrl() {
		try {
			igInstaller.install(new PackageInstallationSpec()
				.setName("blah")
				.setVersion("1.0.2")
				.setPackageUrl("http://localhost:" + myPort + "/foo.tgz")
			);
			fail();
		} catch (ResourceNotFoundException e) {
			assertEquals(Msg.code(1303) + "Received HTTP 404 from URL: http://localhost:" + myPort + "/foo.tgz", e.getMessage());
		}

	}

	@Test
	public void installFromCache2() {
		byte[] bytes = loadResourceAsByteArray("/packages/basisprofil.de.tar.gz");
		myFakeNpmServlet.getResponses().put("/basisprofil.de/0.2.40", bytes);

		igInstaller.install(new PackageInstallationSpec().setName("basisprofil.de").setVersion("0.2.40").setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_ONLY));
	}
}
