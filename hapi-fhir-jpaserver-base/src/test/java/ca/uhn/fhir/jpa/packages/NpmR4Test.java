package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.dao.data.INpmPackageDao;
import ca.uhn.fhir.jpa.dao.data.INpmPackageVersionDao;
import ca.uhn.fhir.jpa.dao.data.INpmPackageVersionResourceDao;
import ca.uhn.fhir.jpa.dao.r4.BaseJpaR4Test;
import ca.uhn.fhir.jpa.model.entity.NpmPackageEntity;
import ca.uhn.fhir.jpa.model.entity.NpmPackageVersionEntity;
import ca.uhn.fhir.jpa.model.entity.NpmPackageVersionResourceEntity;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.test.utilities.JettyUtil;
import ca.uhn.fhir.test.utilities.ProxyUtil;
import ca.uhn.fhir.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.ImplementationGuide;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.PractitionerRole;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.SearchParameter;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.utilities.npm.NpmPackage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

public class NpmR4Test extends BaseJpaR4Test {

	private static final Logger ourLog = LoggerFactory.getLogger(NpmR4Test.class);
	@Autowired
	public IPackageInstallerSvc igInstaller;
	@Autowired
	private IHapiPackageCacheManager myPackageCacheManager;
	@Autowired
	private NpmJpaValidationSupport myNpmJpaValidationSupport;
	private Server myServer;
	@Autowired
	private INpmPackageDao myPackageDao;
	@Autowired
	private INpmPackageVersionDao myPackageVersionDao;
	@Autowired
	private INpmPackageVersionResourceDao myPackageVersionResourceDao;
	private FakeNpmServlet myFakeNpmServlet;

	@BeforeEach
	public void before() throws Exception {
		JpaPackageCache jpaPackageCache = ProxyUtil.getSingletonTarget(myPackageCacheManager, JpaPackageCache.class);

		myServer = new Server(0);
		ServletHandler proxyHandler = new ServletHandler();
		myFakeNpmServlet = new FakeNpmServlet();
		ServletHolder servletHolder = new ServletHolder(myFakeNpmServlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		myServer.setHandler(proxyHandler);
		myServer.start();

		int port = JettyUtil.getPortForStartedServer(myServer);
		jpaPackageCache.getPackageServers().clear();
		jpaPackageCache.addPackageServer("http://localhost:" + port);

		myFakeNpmServlet.myResponses.clear();
	}

	@AfterEach
	public void after() throws Exception {
		JettyUtil.closeServer(myServer);
		myDaoConfig.setAllowExternalReferences(new DaoConfig().isAllowExternalReferences());
	}


	@Disabled("This test is super slow so don't run by default")
	@Test
	public void testInstallUsCore() {
		JpaPackageCache jpaPackageCache = ProxyUtil.getSingletonTarget(myPackageCacheManager, JpaPackageCache.class);
		jpaPackageCache.getPackageServers().clear();
		jpaPackageCache.addPackageServer("https://packages.fhir.org");

		PackageInstallationSpec spec = new PackageInstallationSpec().setName("hl7.fhir.us.core").setVersion("3.1.0").setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_AND_INSTALL).setFetchDependencies(true);
		igInstaller.install(spec);

		runInTransaction(()->{
			SearchParameterMap map = SearchParameterMap.newSynchronous(SearchParameter.SP_BASE, new TokenParam("NamingSystem"));
			IBundleProvider outcome = mySearchParameterDao.search(map);
			List<IBaseResource> resources = outcome.getResources(0, outcome.sizeOrThrowNpe());
			for (int i = 0; i < resources.size(); i++) {
				ourLog.info("**************************************************************************");
				ourLog.info("**************************************************************************");
				ourLog.info("Res " + i);
				ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(resources.get(i)));
			}
		});

		igInstaller.install(spec);
	}


	@Test
	public void testCacheDstu3Package() throws Exception {
		byte[] bytes = loadClasspathBytes("/packages/nictiz.fhir.nl.stu3.questionnaires-1.0.2.tgz");
		myFakeNpmServlet.myResponses.put("/nictiz.fhir.nl.stu3.questionnaires/1.0.2", bytes);

		PackageInstallationSpec spec = new PackageInstallationSpec().setName("nictiz.fhir.nl.stu3.questionnaires").setVersion("1.0.2").setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_ONLY);
		igInstaller.install(spec);

		// Be sure no further communication with the server
		JettyUtil.closeServer(myServer);

		// Make sure we can fetch the package by ID and Version
		NpmPackage pkg = myPackageCacheManager.loadPackage("nictiz.fhir.nl.stu3.questionnaires", "1.0.2");
		assertEquals("Nictiz NL package of FHIR STU3 conformance resources for MedMij information standard Questionnaires. Includes dependency on Zib2017 and SDC.\\n\\nHCIMs: https://zibs.nl/wiki/HCIM_Release_2017(EN)", pkg.description());

		// Make sure we can fetch the package by ID
		pkg = myPackageCacheManager.loadPackage("nictiz.fhir.nl.stu3.questionnaires", null);
		assertEquals("1.0.2", pkg.version());
		assertEquals("Nictiz NL package of FHIR STU3 conformance resources for MedMij information standard Questionnaires. Includes dependency on Zib2017 and SDC.\\n\\nHCIMs: https://zibs.nl/wiki/HCIM_Release_2017(EN)", pkg.description());

		// Fetch resource by URL
		FhirContext fhirContext = FhirContext.forCached(FhirVersionEnum.DSTU3);
		runInTransaction(() -> {
			IBaseResource asset = myPackageCacheManager.loadPackageAssetByUrl(FhirVersionEnum.DSTU3, "http://nictiz.nl/fhir/StructureDefinition/vl-QuestionnaireResponse");
			assertThat(fhirContext.newJsonParser().encodeResourceToString(asset), containsString("\"url\":\"http://nictiz.nl/fhir/StructureDefinition/vl-QuestionnaireResponse\",\"version\":\"1.0.1\""));
		});

		// Fetch resource by URL with version
		runInTransaction(() -> {
			IBaseResource asset = myPackageCacheManager.loadPackageAssetByUrl(FhirVersionEnum.DSTU3, "http://nictiz.nl/fhir/StructureDefinition/vl-QuestionnaireResponse|1.0.1");
			assertThat(fhirContext.newJsonParser().encodeResourceToString(asset), containsString("\"url\":\"http://nictiz.nl/fhir/StructureDefinition/vl-QuestionnaireResponse\",\"version\":\"1.0.1\""));
		});

		// This was saved but is the wrong version of FHIR for this server
		assertNull(myNpmJpaValidationSupport.fetchStructureDefinition("http://fhir.de/StructureDefinition/condition-de-basis/0.2"));
	}

	@Test
	public void testInstallR4Package() throws Exception {
		myDaoConfig.setAllowExternalReferences(true);

		byte[] bytes = loadClasspathBytes("/packages/hl7.fhir.uv.shorthand-0.12.0.tgz");
		myFakeNpmServlet.myResponses.put("/hl7.fhir.uv.shorthand/0.12.0", bytes);

		PackageInstallationSpec spec = new PackageInstallationSpec().setName("hl7.fhir.uv.shorthand").setVersion("0.12.0").setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_AND_INSTALL);
		PackageInstallOutcomeJson outcome = igInstaller.install(spec);
		assertEquals(1, outcome.getResourcesInstalled().get("CodeSystem"));

		// Be sure no further communication with the server
		JettyUtil.closeServer(myServer);

		// Make sure we can fetch the package by ID and Version
		NpmPackage pkg = myPackageCacheManager.loadPackage("hl7.fhir.uv.shorthand", "0.12.0");
		assertEquals("Describes FHIR Shorthand (FSH), a domain-specific language (DSL) for defining the content of FHIR Implementation Guides (IG). (built Wed, Apr 1, 2020 17:24+0000+00:00)", pkg.description());

		// Make sure we can fetch the package by ID
		pkg = myPackageCacheManager.loadPackage("hl7.fhir.uv.shorthand", null);
		assertEquals("0.12.0", pkg.version());
		assertEquals("Describes FHIR Shorthand (FSH), a domain-specific language (DSL) for defining the content of FHIR Implementation Guides (IG). (built Wed, Apr 1, 2020 17:24+0000+00:00)", pkg.description());

		// Make sure DB rows were saved
		runInTransaction(() -> {
			NpmPackageEntity pkgEntity = myPackageDao.findByPackageId("hl7.fhir.uv.shorthand").orElseThrow(() -> new IllegalArgumentException());
			assertEquals("hl7.fhir.uv.shorthand", pkgEntity.getPackageId());

			NpmPackageVersionEntity versionEntity = myPackageVersionDao.findByPackageIdAndVersion("hl7.fhir.uv.shorthand", "0.12.0").orElseThrow(() -> new IllegalArgumentException());
			assertEquals("hl7.fhir.uv.shorthand", versionEntity.getPackageId());
			assertEquals("0.12.0", versionEntity.getVersionId());
			assertEquals(3001, versionEntity.getPackageSizeBytes());
			assertEquals(true, versionEntity.isCurrentVersion());
			assertEquals("hl7.fhir.uv.shorthand", versionEntity.getPackageId());
			assertEquals("4.0.1", versionEntity.getFhirVersionId());
			assertEquals(FhirVersionEnum.R4, versionEntity.getFhirVersion());

			NpmPackageVersionResourceEntity resource = myPackageVersionResourceDao.findCurrentVersionByCanonicalUrl(Pageable.unpaged(), FhirVersionEnum.R4, "http://hl7.org/fhir/uv/shorthand/ImplementationGuide/hl7.fhir.uv.shorthand").getContent().get(0);
			assertEquals("http://hl7.org/fhir/uv/shorthand/ImplementationGuide/hl7.fhir.uv.shorthand", resource.getCanonicalUrl());
			assertEquals("0.12.0", resource.getCanonicalVersion());
			assertEquals("ImplementationGuide-hl7.fhir.uv.shorthand.json", resource.getFilename());
			assertEquals("4.0.1", resource.getFhirVersionId());
			assertEquals(FhirVersionEnum.R4, resource.getFhirVersion());
			assertEquals(6155, resource.getResSizeBytes());
		});

		// Fetch resource by URL
		runInTransaction(() -> {
			IBaseResource asset = myPackageCacheManager.loadPackageAssetByUrl(FhirVersionEnum.R4, "http://hl7.org/fhir/uv/shorthand/ImplementationGuide/hl7.fhir.uv.shorthand");
			assertThat(myFhirCtx.newJsonParser().encodeResourceToString(asset), containsString("\"url\":\"http://hl7.org/fhir/uv/shorthand/ImplementationGuide/hl7.fhir.uv.shorthand\",\"version\":\"0.12.0\""));
		});

		// Fetch resource by URL with version
		runInTransaction(() -> {
			IBaseResource asset = myPackageCacheManager.loadPackageAssetByUrl(FhirVersionEnum.R4, "http://hl7.org/fhir/uv/shorthand/ImplementationGuide/hl7.fhir.uv.shorthand|0.12.0");
			assertThat(myFhirCtx.newJsonParser().encodeResourceToString(asset), containsString("\"url\":\"http://hl7.org/fhir/uv/shorthand/ImplementationGuide/hl7.fhir.uv.shorthand\",\"version\":\"0.12.0\""));
		});

		// Search for the installed resource
		runInTransaction(() -> {
			SearchParameterMap map = SearchParameterMap.newSynchronous();
			map.add(StructureDefinition.SP_URL, new UriParam("http://hl7.org/fhir/uv/shorthand/CodeSystem/shorthand-code-system"));
			IBundleProvider result = myCodeSystemDao.search(map);
			assertEquals(1, result.sizeOrThrowNpe());
		});
	}

	@Test
	public void testInstallR4Package_NonConformanceResources() throws Exception {
		myDaoConfig.setAllowExternalReferences(true);

		byte[] bytes = loadClasspathBytes("/packages/test-organizations-package.tgz");
		myFakeNpmServlet.myResponses.put("/test-organizations/1.0.0", bytes);

		List<String> resourceList = new ArrayList<>();
		resourceList.add("Organization");
		PackageInstallationSpec spec = new PackageInstallationSpec().setName("test-organizations").setVersion("1.0.0").setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_AND_INSTALL);
		spec.setInstallResourceTypes(resourceList);
		PackageInstallOutcomeJson outcome = igInstaller.install(spec);
		assertEquals(3, outcome.getResourcesInstalled().get("Organization"));

		// Be sure no further communication with the server
		JettyUtil.closeServer(myServer);

		// Search for the installed resources
		runInTransaction(() -> {
			SearchParameterMap map = SearchParameterMap.newSynchronous();
			map.add(Organization.SP_IDENTIFIER, new TokenParam("https://github.com/synthetichealth/synthea", "organization1"));
			IBundleProvider result = myOrganizationDao.search(map);
			assertEquals(1, result.sizeOrThrowNpe());
			map = SearchParameterMap.newSynchronous();
			map.add(Organization.SP_IDENTIFIER, new TokenParam("https://github.com/synthetichealth/synthea", "organization2"));
			result = myOrganizationDao.search(map);
			assertEquals(1, result.sizeOrThrowNpe());
			map = SearchParameterMap.newSynchronous();
			map.add(Organization.SP_IDENTIFIER, new TokenParam("https://github.com/synthetichealth/synthea", "organization3"));
			result = myOrganizationDao.search(map);
			assertEquals(1, result.sizeOrThrowNpe());
		});

	}

	@Test
	public void testInstallR4Package_NoIdentifierNoUrl() throws Exception {
		myDaoConfig.setAllowExternalReferences(true);

		byte[] bytes = loadClasspathBytes("/packages/test-missing-identifier-package.tgz");
		myFakeNpmServlet.myResponses.put("/test-organizations/1.0.0", bytes);

		List<String> resourceList = new ArrayList<>();
		resourceList.add("Organization");
		PackageInstallationSpec spec = new PackageInstallationSpec().setName("test-organizations").setVersion("1.0.0").setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_AND_INSTALL);
		spec.setInstallResourceTypes(resourceList);
		try {
			PackageInstallOutcomeJson outcome = igInstaller.install(spec);
			fail();
		} catch (ImplementationGuideInstallationException theE) {
			assertThat(theE.getMessage(), containsString("Resources in a package must have a url or identifier to be loaded by the package installer."));
		}
	}

	@Test
	public void testInstallR4Package_DraftResourcesNotInstalled() throws Exception {
		myDaoConfig.setAllowExternalReferences(true);

		byte[] bytes = loadClasspathBytes("/packages/test-draft-sample.tgz");
		myFakeNpmServlet.myResponses.put("/hl7.fhir.uv.shorthand/0.11.1", bytes);

		PackageInstallationSpec spec = new PackageInstallationSpec().setName("hl7.fhir.uv.shorthand").setVersion("0.11.1").setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_AND_INSTALL);
		PackageInstallOutcomeJson outcome = igInstaller.install(spec);
		assertEquals(0, outcome.getResourcesInstalled().size(), outcome.getResourcesInstalled().toString());

	}

	@Test
	public void testInstallR4Package_Twice() throws Exception {
		myDaoConfig.setAllowExternalReferences(true);

		byte[] bytes = loadClasspathBytes("/packages/hl7.fhir.uv.shorthand-0.12.0.tgz");
		myFakeNpmServlet.myResponses.put("/hl7.fhir.uv.shorthand/0.12.0", bytes);

		PackageInstallOutcomeJson outcome;

		PackageInstallationSpec spec = new PackageInstallationSpec().setName("hl7.fhir.uv.shorthand").setVersion("0.12.0").setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_AND_INSTALL);
		outcome = igInstaller.install(spec);
		assertEquals(1, outcome.getResourcesInstalled().get("CodeSystem"));

		igInstaller.install(spec);
		outcome = igInstaller.install(spec);
		assertEquals(null, outcome.getResourcesInstalled().get("CodeSystem"));

		// Ensure that we loaded the contents
		IBundleProvider searchResult = myCodeSystemDao.search(SearchParameterMap.newSynchronous("url", new UriParam("http://hl7.org/fhir/uv/shorthand/CodeSystem/shorthand-code-system")));
		assertEquals(1, searchResult.sizeOrThrowNpe());

	}


	@Test
	public void testInstallR4PackageWithNoDescription() throws Exception {
		myDaoConfig.setAllowExternalReferences(true);

		byte[] bytes = loadClasspathBytes("/packages/UK.Core.r4-1.1.0.tgz");
		myFakeNpmServlet.myResponses.put("/UK.Core.r4/1.1.0", bytes);

		PackageInstallationSpec spec = new PackageInstallationSpec().setName("UK.Core.r4").setVersion("1.1.0").setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_AND_INSTALL);
		igInstaller.install(spec);

		// Be sure no further communication with the server
		JettyUtil.closeServer(myServer);

		// Make sure we can fetch the package by ID and Version
		NpmPackage pkg = myPackageCacheManager.loadPackage("UK.Core.r4", "1.1.0");
		assertEquals(null, pkg.description());
		assertEquals("UK.Core.r4", pkg.name());

	}

	@Test
	public void testLoadPackageMetadata() throws Exception {
		myDaoConfig.setAllowExternalReferences(true);

		myFakeNpmServlet.myResponses.put("/hl7.fhir.uv.shorthand/0.12.0", loadClasspathBytes("/packages/hl7.fhir.uv.shorthand-0.12.0.tgz"));
		myFakeNpmServlet.myResponses.put("/hl7.fhir.uv.shorthand/0.11.1", loadClasspathBytes("/packages/hl7.fhir.uv.shorthand-0.11.1.tgz"));

		PackageInstallationSpec spec = new PackageInstallationSpec().setName("hl7.fhir.uv.shorthand").setVersion("0.12.0").setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_ONLY);
		igInstaller.install(spec);
		spec = new PackageInstallationSpec().setName("hl7.fhir.uv.shorthand").setVersion("0.11.1").setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_ONLY);
		igInstaller.install(spec);

		runInTransaction(() -> {
			NpmPackageMetadataJson metadata = myPackageCacheManager.loadPackageMetadata("hl7.fhir.uv.shorthand");
			try {
				ourLog.info(JsonUtil.serialize(metadata));

				assertEquals("0.12.0", metadata.getDistTags().getLatest());

				assertThat(metadata.getVersions().keySet(), contains("0.12.0", "0.11.1"));

				NpmPackageMetadataJson.Version version0120 = metadata.getVersions().get("0.12.0");
				assertEquals(3001, version0120.getBytes());

			} catch (IOException e) {
				throw new InternalErrorException(e);
			}
		});

	}


	@Test
	public void testLoadPackageUsingImpreciseId() throws Exception {
		myDaoConfig.setAllowExternalReferences(true);

		myFakeNpmServlet.myResponses.put("/hl7.fhir.uv.shorthand/0.12.0", loadClasspathBytes("/packages/hl7.fhir.uv.shorthand-0.12.0.tgz"));
		myFakeNpmServlet.myResponses.put("/hl7.fhir.uv.shorthand/0.11.1", loadClasspathBytes("/packages/hl7.fhir.uv.shorthand-0.11.1.tgz"));
		myFakeNpmServlet.myResponses.put("/hl7.fhir.uv.shorthand/0.11.0", loadClasspathBytes("/packages/hl7.fhir.uv.shorthand-0.11.0.tgz"));

		PackageInstallationSpec spec;
		spec = new PackageInstallationSpec().setName("hl7.fhir.uv.shorthand").setVersion("0.12.0").setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_ONLY);
		PackageInstallOutcomeJson outcome = igInstaller.install(spec);
		ourLog.info("Install messages:\n * {}", outcome.getMessage().stream().collect(Collectors.joining("\n * ")));
		assertThat(outcome.getMessage(), hasItem("Marking package hl7.fhir.uv.shorthand#0.12.0 as current version"));
		assertThat(outcome.getMessage(), hasItem("Indexing CodeSystem Resource[package/CodeSystem-shorthand-code-system.json] with URL: http://hl7.org/fhir/uv/shorthand/CodeSystem/shorthand-code-system|0.12.0"));

		spec = new PackageInstallationSpec().setName("hl7.fhir.uv.shorthand").setVersion("0.11.1").setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_ONLY);
		outcome = igInstaller.install(spec);
		ourLog.info("Install messages:\n * {}", outcome.getMessage().stream().collect(Collectors.joining("\n * ")));
		assertThat(outcome.getMessage(), not(hasItem("Marking package hl7.fhir.uv.shorthand#0.11.1 as current version")));

		spec = new PackageInstallationSpec().setName("hl7.fhir.uv.shorthand").setVersion("0.11.0").setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_ONLY);
		igInstaller.install(spec);


		NpmPackage pkg;

		pkg = myPackageCacheManager.loadPackage("hl7.fhir.uv.shorthand", "0.11.x");
		assertEquals("0.11.1", pkg.version());

		pkg = myPackageCacheManager.loadPackage("hl7.fhir.uv.shorthand", "0.12.x");
		assertEquals("0.12.0", pkg.version());

	}

	@Test
	public void testInstallNewerPackageUpdatesLatestVersionFlag() throws Exception {
		myDaoConfig.setAllowExternalReferences(true);

		byte[] contents0111 = loadClasspathBytes("/packages/hl7.fhir.uv.shorthand-0.11.1.tgz");
		byte[] contents0120 = loadClasspathBytes("/packages/hl7.fhir.uv.shorthand-0.12.0.tgz");
		myFakeNpmServlet.myResponses.put("/hl7.fhir.uv.shorthand/0.11.1", contents0111);
		myFakeNpmServlet.myResponses.put("/hl7.fhir.uv.shorthand/0.12.0", contents0120);

		// Install older version
		PackageInstallationSpec spec = new PackageInstallationSpec().setName("hl7.fhir.uv.shorthand").setVersion("0.11.1").setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_ONLY);
		igInstaller.install(spec);

		// Older version is current
		runInTransaction(() -> {
			NpmPackageVersionEntity versionEntity = myPackageVersionDao.findByPackageIdAndVersion("hl7.fhir.uv.shorthand", "0.11.1").orElseThrow(() -> new IllegalArgumentException());
			assertEquals(true, versionEntity.isCurrentVersion());
		});

		// Fetching a resource should return the older version
		runInTransaction(() -> {
			ImplementationGuide ig = (ImplementationGuide) myPackageCacheManager.loadPackageAssetByUrl(FhirVersionEnum.R4, "http://hl7.org/fhir/uv/shorthand/ImplementationGuide/hl7.fhir.uv.shorthand");
			assertEquals("0.11.1", ig.getVersion());
		});

		// Now install newer version
		spec = new PackageInstallationSpec().setName("hl7.fhir.uv.shorthand").setVersion("0.12.0").setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_ONLY);
		igInstaller.install(spec);

		// Newer version is current
		runInTransaction(() -> {
			NpmPackageVersionEntity versionEntity = myPackageVersionDao.findByPackageIdAndVersion("hl7.fhir.uv.shorthand", "0.11.1").orElseThrow(() -> new IllegalArgumentException());
			assertEquals(false, versionEntity.isCurrentVersion());

			versionEntity = myPackageVersionDao.findByPackageIdAndVersion("hl7.fhir.uv.shorthand", "0.12.0").orElseThrow(() -> new IllegalArgumentException());
			assertEquals(true, versionEntity.isCurrentVersion());
		});

		// Fetching a resource should return the newer version
		runInTransaction(() -> {
			ImplementationGuide ig = (ImplementationGuide) myPackageCacheManager.loadPackageAssetByUrl(FhirVersionEnum.R4, "http://hl7.org/fhir/uv/shorthand/ImplementationGuide/hl7.fhir.uv.shorthand");
			assertEquals("0.12.0", ig.getVersion());
		});
	}

	@Test
	public void testInstallOlderPackageDoesntUpdateLatestVersionFlag() throws Exception {
		myDaoConfig.setAllowExternalReferences(true);

		myFakeNpmServlet.myResponses.put("/hl7.fhir.uv.shorthand/0.12.0", loadClasspathBytes("/packages/hl7.fhir.uv.shorthand-0.12.0.tgz"));
		myFakeNpmServlet.myResponses.put("/hl7.fhir.uv.shorthand/0.11.1", loadClasspathBytes("/packages/hl7.fhir.uv.shorthand-0.11.1.tgz"));

		// Install newer version
		PackageInstallationSpec spec = new PackageInstallationSpec().setName("hl7.fhir.uv.shorthand").setVersion("0.12.0").setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_ONLY);
		igInstaller.install(spec);


		runInTransaction(() -> {
			NpmPackageVersionEntity versionEntity = myPackageVersionDao.findByPackageIdAndVersion("hl7.fhir.uv.shorthand", "0.12.0").orElseThrow(() -> new IllegalArgumentException());
			assertEquals(true, versionEntity.isCurrentVersion());
		});

		// Fetching a resource should return the older version
		runInTransaction(() -> {
			ImplementationGuide ig = (ImplementationGuide) myPackageCacheManager.loadPackageAssetByUrl(FhirVersionEnum.R4, "http://hl7.org/fhir/uv/shorthand/ImplementationGuide/hl7.fhir.uv.shorthand");
			assertEquals("0.12.0", ig.getVersion());
		});

		// Install older version
		spec = new PackageInstallationSpec().setName("hl7.fhir.uv.shorthand").setVersion("0.11.1").setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_ONLY);
		igInstaller.install(spec);

		// Newer version is still current
		runInTransaction(() -> {
			NpmPackageVersionEntity versionEntity = myPackageVersionDao.findByPackageIdAndVersion("hl7.fhir.uv.shorthand", "0.11.1").orElseThrow(() -> new IllegalArgumentException());
			assertEquals(false, versionEntity.isCurrentVersion());

			versionEntity = myPackageVersionDao.findByPackageIdAndVersion("hl7.fhir.uv.shorthand", "0.12.0").orElseThrow(() -> new IllegalArgumentException());
			assertEquals(true, versionEntity.isCurrentVersion());
		});

		// Fetching a resource should return the newer version
		runInTransaction(() -> {
			ImplementationGuide ig = (ImplementationGuide) myPackageCacheManager.loadPackageAssetByUrl(FhirVersionEnum.R4, "http://hl7.org/fhir/uv/shorthand/ImplementationGuide/hl7.fhir.uv.shorthand");
			assertEquals("0.12.0", ig.getVersion());
		});
	}

	@Test
	public void testInstallAlreadyExistingIsIgnored() throws Exception {
		myDaoConfig.setAllowExternalReferences(true);

		myFakeNpmServlet.myResponses.put("/hl7.fhir.uv.shorthand/0.12.0", loadClasspathBytes("/packages/hl7.fhir.uv.shorthand-0.12.0.tgz"));

		// Install
		PackageInstallationSpec spec = new PackageInstallationSpec().setName("hl7.fhir.uv.shorthand").setVersion("0.12.0").setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_ONLY);
		igInstaller.install(spec);

		runInTransaction(() -> {
			NpmPackageVersionEntity versionEntity = myPackageVersionDao.findByPackageIdAndVersion("hl7.fhir.uv.shorthand", "0.12.0").orElseThrow(() -> new IllegalArgumentException());
			assertEquals(true, versionEntity.isCurrentVersion());
		});

		// Install same again
		spec = new PackageInstallationSpec().setName("hl7.fhir.uv.shorthand").setVersion("0.12.0").setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_ONLY);
		igInstaller.install(spec);

		runInTransaction(() -> {
			NpmPackageVersionEntity versionEntity = myPackageVersionDao.findByPackageIdAndVersion("hl7.fhir.uv.shorthand", "0.12.0").orElseThrow(() -> new IllegalArgumentException());
			assertEquals(true, versionEntity.isCurrentVersion());
		});

	}

	@Test
	public void testInstallPkgContainingSearchParameter() throws IOException {
		myDaoConfig.setAllowExternalReferences(true);

		byte[] contents0111 = loadClasspathBytes("/packages/test-exchange-sample.tgz");
		myFakeNpmServlet.myResponses.put("/test-exchange.fhir.us.com/2.1.1", contents0111);

		contents0111 = loadClasspathBytes("/packages/test-exchange-sample-2.tgz");
		myFakeNpmServlet.myResponses.put("/test-exchange.fhir.us.com/2.1.2", contents0111);

		// Install older version
		PackageInstallationSpec spec = new PackageInstallationSpec().setName("test-exchange.fhir.us.com").setVersion("2.1.1").setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_AND_INSTALL);
		igInstaller.install(spec);

		IBundleProvider spSearch = mySearchParameterDao.search(SearchParameterMap.newSynchronous("code", new TokenParam("network-id")));
		assertEquals(1, spSearch.sizeOrThrowNpe());
		SearchParameter sp = (SearchParameter) spSearch.getResources(0, 1).get(0);
		assertEquals("network-id", sp.getCode());
		assertEquals("2.1", sp.getVersion());
		assertEquals(Enumerations.PublicationStatus.ACTIVE, sp.getStatus());

		Organization org = new Organization();
		org.setName("Hello");
		IIdType orgId = myOrganizationDao.create(org).getId().toUnqualifiedVersionless();

		PractitionerRole pr = new PractitionerRole();
		pr.addExtension().setUrl("http://test-exchange.com/fhir/us/providerdataexchange/StructureDefinition/networkreference").setValue(new Reference(orgId));
		myPractitionerRoleDao.create(pr);

		SearchParameterMap map = SearchParameterMap.newSynchronous("network-id", new ReferenceParam(orgId.getValue()));
		spSearch = myPractitionerRoleDao.search(map);
		assertEquals(1, spSearch.sizeOrThrowNpe());
		
		// Install newer version
		spec = new PackageInstallationSpec().setName("test-exchange.fhir.us.com").setVersion("2.1.2").setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_AND_INSTALL);
		igInstaller.install(spec);

		spSearch = mySearchParameterDao.search(SearchParameterMap.newSynchronous("code", new TokenParam("network-id")));
		assertEquals(1, spSearch.sizeOrThrowNpe());
		sp = (SearchParameter) spSearch.getResources(0, 1).get(0);
		assertEquals("network-id", sp.getCode());
		assertEquals(Enumerations.PublicationStatus.ACTIVE, sp.getStatus());
		assertEquals("2.2", sp.getVersion());

	}
	

	@Test
	public void testLoadContents() throws IOException {
		byte[] contents0111 = loadClasspathBytes("/packages/hl7.fhir.uv.shorthand-0.11.1.tgz");
		byte[] contents0120 = loadClasspathBytes("/packages/hl7.fhir.uv.shorthand-0.12.0.tgz");

		PackageInstallationSpec spec = new PackageInstallationSpec().setName("hl7.fhir.uv.shorthand").setVersion("0.11.1").setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_ONLY).setPackageContents(contents0111);
		igInstaller.install(spec);
		spec = new PackageInstallationSpec().setName("hl7.fhir.uv.shorthand").setVersion("0.12.0").setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_ONLY).setPackageContents(contents0120);
		igInstaller.install(spec);


		assertArrayEquals(contents0111, myPackageCacheManager.loadPackageContents("hl7.fhir.uv.shorthand", "0.11.1").getBytes());
		assertArrayEquals(contents0120, myPackageCacheManager.loadPackageContents("hl7.fhir.uv.shorthand", "0.12.0").getBytes());
		assertArrayEquals(contents0120, myPackageCacheManager.loadPackageContents("hl7.fhir.uv.shorthand", "latest").getBytes());
		assertEquals("0.11.1", myPackageCacheManager.loadPackageContents("hl7.fhir.uv.shorthand", "0.11.1").getVersion());
		assertEquals("0.12.0", myPackageCacheManager.loadPackageContents("hl7.fhir.uv.shorthand", "0.12.0").getVersion());
		assertEquals("0.12.0", myPackageCacheManager.loadPackageContents("hl7.fhir.uv.shorthand", "latest").getVersion());
		assertEquals(null, myPackageCacheManager.loadPackageContents("hl7.fhir.uv.shorthand", "1.2.3"));
		assertEquals(null, myPackageCacheManager.loadPackageContents("foo", "1.2.3"));
	}


	@Test
	public void testDeletePackage() throws IOException {
		myDaoConfig.setAllowExternalReferences(true);

		myFakeNpmServlet.myResponses.put("/hl7.fhir.uv.shorthand/0.12.0", loadClasspathBytes("/packages/hl7.fhir.uv.shorthand-0.12.0.tgz"));
		myFakeNpmServlet.myResponses.put("/hl7.fhir.uv.shorthand/0.11.1", loadClasspathBytes("/packages/hl7.fhir.uv.shorthand-0.11.1.tgz"));
		myFakeNpmServlet.myResponses.put("/hl7.fhir.uv.shorthand/0.11.0", loadClasspathBytes("/packages/hl7.fhir.uv.shorthand-0.11.0.tgz"));

		igInstaller.install(new PackageInstallationSpec().setName("hl7.fhir.uv.shorthand").setVersion("0.12.0").setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_ONLY));
		igInstaller.install(new PackageInstallationSpec().setName("hl7.fhir.uv.shorthand").setVersion("0.11.1").setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_ONLY));
		igInstaller.install(new PackageInstallationSpec().setName("hl7.fhir.uv.shorthand").setVersion("0.11.0").setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_ONLY));

		runInTransaction(() -> {
			Slice<NpmPackageVersionResourceEntity> versions = myPackageVersionResourceDao.findCurrentVersionByCanonicalUrl(Pageable.unpaged(), FhirVersionEnum.R4, "http://hl7.org/fhir/uv/shorthand/ValueSet/shorthand-instance-tags");
			assertEquals(1, versions.getNumberOfElements());
			NpmPackageVersionResourceEntity resource = versions.getContent().get(0);
			assertEquals("0.12.0", resource.getCanonicalVersion());
		});

		myPackageCacheManager.uninstallPackage("hl7.fhir.uv.shorthand", "0.12.0");

		runInTransaction(() -> {
			Slice<NpmPackageVersionResourceEntity> versions = myPackageVersionResourceDao.findCurrentVersionByCanonicalUrl(Pageable.unpaged(), FhirVersionEnum.R4, "http://hl7.org/fhir/uv/shorthand/ValueSet/shorthand-instance-tags");
			assertEquals(1, versions.getNumberOfElements());
			NpmPackageVersionResourceEntity resource = versions.getContent().get(0);
			assertEquals("0.11.1", resource.getCanonicalVersion());
		});

		myPackageCacheManager.uninstallPackage("hl7.fhir.uv.shorthand", "0.11.0");

		runInTransaction(() -> {
			Slice<NpmPackageVersionResourceEntity> versions = myPackageVersionResourceDao.findCurrentVersionByCanonicalUrl(Pageable.unpaged(), FhirVersionEnum.R4, "http://hl7.org/fhir/uv/shorthand/ValueSet/shorthand-instance-tags");
			assertEquals(1, versions.getNumberOfElements());
			NpmPackageVersionResourceEntity resource = versions.getContent().get(0);
			assertEquals("0.11.1", resource.getCanonicalVersion());
		});

		myPackageCacheManager.uninstallPackage("hl7.fhir.uv.shorthand", "0.11.1");

		runInTransaction(() -> {
			Slice<NpmPackageVersionResourceEntity> versions = myPackageVersionResourceDao.findCurrentVersionByCanonicalUrl(Pageable.unpaged(), FhirVersionEnum.R4, "http://hl7.org/fhir/uv/shorthand/ValueSet/shorthand-instance-tags");
			assertEquals(0, versions.getNumberOfElements());
		});
	}


	static class FakeNpmServlet extends HttpServlet {

		private final Map<String, byte[]> myResponses = new HashMap<>();

		@Override
		protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
			String requestUrl = req.getRequestURI();
			if (myResponses.containsKey(requestUrl)) {
				ourLog.info("Responding to request: {}", requestUrl);

				resp.setStatus(200);

				if (StringUtils.countMatches(requestUrl, "/") == 1) {
					resp.setHeader(Constants.HEADER_CONTENT_TYPE, Constants.CT_JSON);
				}else {
					resp.setHeader(Constants.HEADER_CONTENT_TYPE, "application/gzip");
				}
				resp.getOutputStream().write(myResponses.get(requestUrl));
				resp.getOutputStream().close();
			} else {
				ourLog.warn("Unknown request: {}", requestUrl);

				resp.sendError(404);
			}

		}

		public Map<String, byte[]> getResponses() {
			return myResponses;
		}
	}
}
