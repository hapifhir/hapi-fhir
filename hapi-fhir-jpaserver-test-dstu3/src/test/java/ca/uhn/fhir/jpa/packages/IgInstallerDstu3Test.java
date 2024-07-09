package ca.uhn.fhir.jpa.packages;

import static org.junit.jupiter.api.Assertions.assertEquals;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.dao.data.INpmPackageVersionDao;
import ca.uhn.fhir.jpa.packages.util.PackageUtils;
import ca.uhn.fhir.jpa.test.BaseJpaDstu3Test;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.test.utilities.ProxyUtil;
import ca.uhn.fhir.test.utilities.server.HttpServletExtension;
import org.hl7.fhir.instance.model.api.IBaseBinary;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.utilities.npm.PackageServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import static ca.uhn.fhir.util.ClasspathUtil.loadResourceAsByteArray;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IgInstallerDstu3Test extends BaseJpaDstu3Test {

	private static final Logger ourLog = LoggerFactory.getLogger(IgInstallerDstu3Test.class);
	@Autowired
	private PackageInstallerSvcImpl igInstaller;
	@Autowired
	@Qualifier(PackageUtils.LOADER_WITH_CACHE)
	private IHapiPackageCacheManager myPackageCacheManager;
	@Autowired
	private INpmPackageVersionDao myPackageVersionDao;

	private FakeNpmServlet myFakeNpmServlet = new FakeNpmServlet();
	@RegisterExtension
	public HttpServletExtension myServer = new HttpServletExtension()
		.withServlet(myFakeNpmServlet);
	
	
	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();
		JpaPackageCache jpaPackageCache = ProxyUtil.getSingletonTarget(myPackageCacheManager, JpaPackageCache.class);
		
		jpaPackageCache.getPackageServers().clear();
		jpaPackageCache.addPackageServer(new PackageServer(myServer.getBaseUrl()));

		myFakeNpmServlet.getResponses().clear();
	}

	@AfterEach
	public void after() throws Exception {
		myStorageSettings.setAllowExternalReferences(new JpaStorageSettings().isAllowExternalReferences());
	}

	@Test
	public void testNegativeInstallFromCache() {
		myStorageSettings.setAllowExternalReferences(true);

		byte[] bytes = loadResourceAsByteArray("/packages/erroneous-ig.tar.gz");

		// That patient profile in this NPM package has an invalid base
		try {
			igInstaller.install(new PackageInstallationSpec().setName("erroneous-ig").setVersion("1.0.2").setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_AND_INSTALL).setPackageContents(bytes));
			fail("");
		} catch (ImplementationGuideInstallationException e) {
			assertThat(e.getMessage()).contains("Could not load NPM package erroneous-ig#1.0.2");
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

		myStorageSettings.setAllowExternalReferences(true);
		PackageInstallationSpec spec = new PackageInstallationSpec()
			.setName("nictiz.fhir.nl.stu3.questionnaires")
			.setVersion("1.0.2")
			.setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_ONLY)
			.setFetchDependencies(true)
			.addDependencyExclude("hl7\\.fhir\\.[a-zA-Z0-9]+\\.core");
		PackageInstallOutcomeJson outcome = igInstaller.install(spec);
		ourLog.info("Install messages:\n * {}", outcome.getMessage().stream().collect(Collectors.joining("\n * ")));
		assertThat(outcome.getMessage()).contains("Indexing StructureDefinition Resource[package/vl-QuestionnaireProvisioningTask.json] with URL: http://nictiz.nl/fhir/StructureDefinition/vl-QuestionnaireProvisioningTask|1.0.1");

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

		myStorageSettings.setAllowExternalReferences(true);
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
			.setPackageUrl(myServer.getBaseUrl() + "/foo.tgz")
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

	private void ensureNoCreatesOrUpdates(Callable theCallable) throws Exception {
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED, (thePointcut, t) -> {
			IBaseResource iBaseResource = t.get(IBaseResource.class);
			if (iBaseResource instanceof IBaseBinary) {
				return;
			}
			throw new RuntimeException("Not allowed!");
		});
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED, (thePointcut, t) -> {
			IBaseResource iBaseResource = t.get(IBaseResource.class);
			if (iBaseResource instanceof IBaseBinary) {
				return;
			}
			throw new RuntimeException("Not allowed!");
		});

		try {
			theCallable.call();
		} finally {
			myInterceptorRegistry.unregisterAllAnonymousInterceptors();
		}


	}
	@Test
	public void testMultipleUploads() throws Exception {
		myStorageSettings.setAllowExternalReferences(true);
		PackageInstallationSpec installationSpec = new PackageInstallationSpec()
			.setName("nictiz.fhir.nl.stu3.questionnaires")
			.setVersion("1.0.2")
			.setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_AND_INSTALL)
			.setPackageUrl("classpath:/packages/nictiz.fhir.nl.stu3.questionnaires-1.0.2.tgz");


		igInstaller.install(installationSpec);

		installationSpec.setReloadExisting(true);
		try {
			ensureNoCreatesOrUpdates(() -> igInstaller.install(installationSpec));
			fail("");
		} catch (RuntimeException e) {
			assertThat(e.getMessage()).contains("Not allowed!");
		}

		installationSpec.setReloadExisting(false);
		ensureNoCreatesOrUpdates(() -> igInstaller.install(installationSpec));

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
				.setPackageUrl(myServer.getBaseUrl() + "/foo.tgz")
			);
			fail("");
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
				.setPackageUrl(myServer.getBaseUrl() + "/foo.tgz")
			);
			fail("");
		} catch (ResourceNotFoundException e) {
			assertEquals(Msg.code(1303) + "Received HTTP 404 from URL: " + myServer.getBaseUrl() + "/foo.tgz", e.getMessage());
		}

	}

	@Test
	public void installFromCache2() {
		byte[] bytes = loadResourceAsByteArray("/packages/basisprofil.de.tar.gz");
		myFakeNpmServlet.getResponses().put("/basisprofil.de/0.2.40", bytes);

		igInstaller.install(new PackageInstallationSpec().setName("basisprofil.de").setVersion("0.2.40").setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_ONLY));
	}
}
