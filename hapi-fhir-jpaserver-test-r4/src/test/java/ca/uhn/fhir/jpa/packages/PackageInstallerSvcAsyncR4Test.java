// Created by claude-opus-4-5-20250101
package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.jpa.dao.data.INpmPackageDao;
import ca.uhn.fhir.jpa.dao.data.INpmPackageVersionDao;
import ca.uhn.fhir.jpa.dao.data.INpmPackageVersionResourceDao;
import ca.uhn.fhir.jpa.model.entity.NpmPackageEntity;
import ca.uhn.fhir.jpa.model.entity.NpmPackageVersionEntity;
import ca.uhn.fhir.jpa.model.entity.NpmPackageVersionResourceEntity;
import ca.uhn.fhir.jpa.packages.batch.IAsyncPackageInstallerSvc;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.test.utilities.ProxyUtil;
import ca.uhn.fhir.test.utilities.server.HttpServletExtension;
import ca.uhn.fhir.util.ClasspathUtil;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.utilities.npm.NpmPackage;
import org.hl7.fhir.utilities.npm.PackageServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for asynchronous package installation using the Batch2 framework.
 * <p>
 * These tests verify that package installation works correctly when executed
 * asynchronously via the Batch2 job framework.
 * </p>
 *
 * @since 8.2.0
 */
public class PackageInstallerSvcAsyncR4Test extends BaseJpaR4Test {

	private static final Logger ourLog = LoggerFactory.getLogger(PackageInstallerSvcAsyncR4Test.class);

	@Autowired
	private IAsyncPackageInstallerSvc myAsyncPackageInstallerSvc;
	@Autowired
	private IHapiPackageCacheManager myPackageCacheManager;
	@Autowired
	private INpmPackageDao myPackageDao;
	@Autowired
	private INpmPackageVersionDao myPackageVersionDao;
	@Autowired
	private INpmPackageVersionResourceDao myPackageVersionResourceDao;

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
		String url = myServer.getBaseUrl();
		ourLog.info("Package server is at base: {}", url);
		jpaPackageCache.addPackageServer(new PackageServer(url));

		myFakeNpmServlet.responses.clear();
	}

	@Test
	void testAsyncInstallAvailable() {
		assertNotNull(myAsyncPackageInstallerSvc);
		assertTrue(myAsyncPackageInstallerSvc.isAsyncInstallAvailable());
	}

	@Test
	void testAsyncInstallR4Package() throws Exception {
		myStorageSettings.setAllowExternalReferences(true);

		byte[] bytes = ClasspathUtil.loadResourceAsByteArray("/packages/hl7.fhir.uv.shorthand-0.12.0.tgz");
		myFakeNpmServlet.responses.put("/hl7.fhir.uv.shorthand/0.12.0", bytes);

		PackageInstallationSpec spec = new PackageInstallationSpec()
			.setName("hl7.fhir.uv.shorthand")
			.setVersion("0.12.0")
			.setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_AND_INSTALL);

		// Start async installation
		String jobId = myAsyncPackageInstallerSvc.startAsyncInstall(spec);
		assertNotNull(jobId);
		ourLog.info("Started async package installation job: {}", jobId);

		// Wait for job to complete
		JobInstance jobInstance = myBatch2JobHelper.awaitJobCompletion(jobId);
		assertEquals(StatusEnum.COMPLETED, jobInstance.getStatus());

		// Verify the outcome
		PackageInstallOutcomeJson outcome = myAsyncPackageInstallerSvc.getCompletedOutcome(jobId);
		assertNotNull(outcome);
		assertThat(outcome.getResourcesInstalled()).containsEntry("CodeSystem", 1);

		// Be sure no further communication with the server
		myServer.stopServer();

		// Make sure we can fetch the package by ID and Version
		NpmPackage pkg = myPackageCacheManager.loadPackage("hl7.fhir.uv.shorthand", "0.12.0");
		assertEquals("Describes FHIR Shorthand (FSH), a domain-specific language (DSL) for defining the content of FHIR Implementation Guides (IG). (built Wed, Apr 1, 2020 17:24+0000+00:00)", pkg.description());

		// Make sure DB rows were saved
		runInTransaction(() -> {
			NpmPackageEntity pkgEntity = myPackageDao.findByPackageId("hl7.fhir.uv.shorthand").orElseThrow();
			assertEquals("hl7.fhir.uv.shorthand", pkgEntity.getPackageId());

			NpmPackageVersionEntity versionEntity = myPackageVersionDao.findByPackageIdAndVersion("hl7.fhir.uv.shorthand", "0.12.0").orElseThrow();
			assertEquals("hl7.fhir.uv.shorthand", versionEntity.getPackageId());
			assertEquals("0.12.0", versionEntity.getVersionId());
			assertTrue(versionEntity.isCurrentVersion());
			assertEquals("4.0.1", versionEntity.getFhirVersionId());
			assertEquals(FhirVersionEnum.R4, versionEntity.getFhirVersion());

			NpmPackageVersionResourceEntity resource = myPackageVersionResourceDao.findByCanonicalUrl(Pageable.unpaged(), FhirVersionEnum.R4, "http://hl7.org/fhir/uv/shorthand/ImplementationGuide/hl7.fhir.uv.shorthand", true).getContent().get(0);
			assertEquals("http://hl7.org/fhir/uv/shorthand/ImplementationGuide/hl7.fhir.uv.shorthand", resource.getCanonicalUrl());
			assertEquals("0.12.0", resource.getCanonicalVersion());
		});

		// Search for the installed resource
		runInTransaction(() -> {
			SearchParameterMap map = SearchParameterMap.newSynchronous();
			map.add(StructureDefinition.SP_URL, new UriParam("http://hl7.org/fhir/uv/shorthand/CodeSystem/shorthand-code-system"));
			IBundleProvider result = myCodeSystemDao.search(map);
			assertEquals(1, result.sizeOrThrowNpe());
			IBaseResource resource = result.getResources(0, 1).get(0);
			assertThat(resource.getIdElement().toString()).matches("CodeSystem/[0-9]+/_history/1");
		});
	}

	@Test
	void testAsyncInstall_JobStatusPolling() throws Exception {
		myStorageSettings.setAllowExternalReferences(true);

		byte[] bytes = ClasspathUtil.loadResourceAsByteArray("/packages/hl7.fhir.uv.shorthand-0.12.0.tgz");
		myFakeNpmServlet.responses.put("/hl7.fhir.uv.shorthand/0.12.0", bytes);

		PackageInstallationSpec spec = new PackageInstallationSpec()
			.setName("hl7.fhir.uv.shorthand")
			.setVersion("0.12.0")
			.setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_ONLY);

		// Start async installation
		String jobId = myAsyncPackageInstallerSvc.startAsyncInstall(spec);

		// Immediately check status - should be QUEUED or IN_PROGRESS
		JobInstance initialStatus = myAsyncPackageInstallerSvc.getJobStatus(jobId);
		assertNotNull(initialStatus);
		assertThat(initialStatus.getStatus()).isIn(StatusEnum.QUEUED, StatusEnum.IN_PROGRESS, StatusEnum.FINALIZE, StatusEnum.COMPLETED);

		// Wait for completion
		myBatch2JobHelper.awaitJobCompletion(jobId);

		// Check final status
		JobInstance finalStatus = myAsyncPackageInstallerSvc.getJobStatus(jobId);
		assertEquals(StatusEnum.COMPLETED, finalStatus.getStatus());

		// Verify outcome is available
		PackageInstallOutcomeJson outcome = myAsyncPackageInstallerSvc.getCompletedOutcome(jobId);
		assertNotNull(outcome);
	}

	@Test
	void testAsyncInstall_ProducesMatchingOutcomeToSync() throws Exception {
		myStorageSettings.setAllowExternalReferences(true);

		// Set up the package
		byte[] bytes = ClasspathUtil.loadResourceAsByteArray("/packages/hl7.fhir.uv.shorthand-0.11.1.tgz");
		myFakeNpmServlet.responses.put("/hl7.fhir.uv.shorthand/0.11.1", bytes);

		PackageInstallationSpec spec = new PackageInstallationSpec()
			.setName("hl7.fhir.uv.shorthand")
			.setVersion("0.11.1")
			.setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_ONLY);

		// Install via async
		String jobId = myAsyncPackageInstallerSvc.startAsyncInstall(spec);
		myBatch2JobHelper.awaitJobCompletion(jobId);

		PackageInstallOutcomeJson asyncOutcome = myAsyncPackageInstallerSvc.getCompletedOutcome(jobId);

		// Verify the package was installed correctly
		runInTransaction(() -> {
			NpmPackageVersionEntity versionEntity = myPackageVersionDao.findByPackageIdAndVersion("hl7.fhir.uv.shorthand", "0.11.1").orElseThrow();
			assertEquals("hl7.fhir.uv.shorthand", versionEntity.getPackageId());
			assertEquals("0.11.1", versionEntity.getVersionId());
		});

		// Verify the async outcome contains the expected messages
		assertNotNull(asyncOutcome);
		assertThat(asyncOutcome.getMessage()).isNotEmpty();
	}

	@Test
	void testAsyncInstall_StoreAndInstallMode() throws Exception {
		myStorageSettings.setAllowExternalReferences(true);

		byte[] bytes = ClasspathUtil.loadResourceAsByteArray("/packages/UK.Core.r4-1.1.0.tgz");
		myFakeNpmServlet.responses.put("/UK.Core.r4/1.1.0", bytes);

		PackageInstallationSpec spec = new PackageInstallationSpec()
			.setName("UK.Core.r4")
			.setVersion("1.1.0")
			.setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_AND_INSTALL);

		// Install via async
		String jobId = myAsyncPackageInstallerSvc.startAsyncInstall(spec);
		myBatch2JobHelper.awaitJobCompletion(jobId);

		PackageInstallOutcomeJson outcome = myAsyncPackageInstallerSvc.getCompletedOutcome(jobId);
		assertNotNull(outcome);

		// Verify the package was stored
		NpmPackage pkg = myPackageCacheManager.loadPackage("UK.Core.r4", "1.1.0");
		assertEquals("UK.Core.r4", pkg.name());
	}

	@Test
	void testAsyncInstall_WithPackageUrl() throws Exception {
		myStorageSettings.setAllowExternalReferences(true);

		byte[] bytes = ClasspathUtil.loadResourceAsByteArray("/packages/hl7.fhir.uv.shorthand-0.12.0.tgz");
		myFakeNpmServlet.responses.put("/hl7.fhir.uv.shorthand/0.12.0", bytes);

		// Use the package URL form
		PackageInstallationSpec spec = new PackageInstallationSpec()
			.setName("hl7.fhir.uv.shorthand")
			.setVersion("0.12.0")
			.setPackageUrl(myServer.getBaseUrl() + "/hl7.fhir.uv.shorthand/0.12.0")
			.setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_ONLY);

		// Install via async
		String jobId = myAsyncPackageInstallerSvc.startAsyncInstall(spec);
		myBatch2JobHelper.awaitJobCompletion(jobId);

		// Verify the package was installed
		runInTransaction(() -> {
			assertTrue(myPackageVersionDao.findByPackageIdAndVersion("hl7.fhir.uv.shorthand", "0.12.0").isPresent());
		});
	}
}
