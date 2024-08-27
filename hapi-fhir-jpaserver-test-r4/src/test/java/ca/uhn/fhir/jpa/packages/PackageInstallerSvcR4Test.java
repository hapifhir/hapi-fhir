package ca.uhn.fhir.jpa.packages;

import static org.junit.jupiter.api.Assertions.assertNull;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.data.INpmPackageDao;
import ca.uhn.fhir.jpa.dao.data.INpmPackageVersionDao;
import ca.uhn.fhir.jpa.dao.data.INpmPackageVersionResourceDao;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.NpmPackageEntity;
import ca.uhn.fhir.jpa.model.entity.NpmPackageVersionEntity;
import ca.uhn.fhir.jpa.model.entity.NpmPackageVersionResourceEntity;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.interceptor.partition.RequestTenantPartitionInterceptor;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.test.utilities.ProxyUtil;
import ca.uhn.fhir.test.utilities.server.HttpServletExtension;
import ca.uhn.fhir.util.ClasspathUtil;
import ca.uhn.fhir.util.JsonUtil;
import ca.uhn.fhir.validation.ValidationResult;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.ImplementationGuide;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.PractitionerRole;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.SearchParameter;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.utilities.npm.NpmPackage;
import org.hl7.fhir.utilities.npm.PackageServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class PackageInstallerSvcR4Test extends BaseJpaR4Test {

	private static final Logger ourLog = LoggerFactory.getLogger(PackageInstallerSvcR4Test.class);
	@Autowired
	@Qualifier("myImplementationGuideDaoR4")
	protected IFhirResourceDao<ImplementationGuide> myImplementationGuideDao;
	@Autowired
	private IHapiPackageCacheManager myPackageCacheManager;
	@Autowired
	private NpmJpaValidationSupport myNpmJpaValidationSupport;
	@Autowired
	private INpmPackageDao myPackageDao;
	@Autowired
	private INpmPackageVersionDao myPackageVersionDao;
	@Autowired
	private INpmPackageVersionResourceDao myPackageVersionResourceDao;
	@Autowired
	private IInterceptorService myInterceptorService;
	@Autowired
	private RequestTenantPartitionInterceptor myRequestTenantPartitionInterceptor;
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

	@AfterEach
	public void after() throws Exception {
		myStorageSettings.setAllowExternalReferences(new JpaStorageSettings().isAllowExternalReferences());
		myStorageSettings.setAutoCreatePlaceholderReferenceTargets(new JpaStorageSettings().isAutoCreatePlaceholderReferenceTargets());
		myPartitionSettings.setPartitioningEnabled(false);
		myPartitionSettings.setUnnamedPartitionMode(false);
		myPartitionSettings.setDefaultPartitionId(new PartitionSettings().getDefaultPartitionId());
		myInterceptorService.unregisterInterceptor(myRequestTenantPartitionInterceptor);
	}


	@Disabled("This test is super slow so don't run by default")
	@Test
	public void testInstallUsCore() {
		JpaPackageCache jpaPackageCache = ProxyUtil.getSingletonTarget(myPackageCacheManager, JpaPackageCache.class);
		jpaPackageCache.getPackageServers().clear();
		jpaPackageCache.addPackageServer(new PackageServer("https://packages.fhir.org"));

		PackageInstallationSpec spec = new PackageInstallationSpec()
			.setName("hl7.fhir.us.core")
			.setVersion("3.1.0")
			.setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_AND_INSTALL)
			.setFetchDependencies(true);
		myPackageInstallerSvc.install(spec);

		runInTransaction(() -> {
			SearchParameterMap map = SearchParameterMap.newSynchronous(SearchParameter.SP_BASE, new TokenParam("NamingSystem"));
			IBundleProvider outcome = mySearchParameterDao.search(map);
			List<IBaseResource> resources = outcome.getResources(0, outcome.sizeOrThrowNpe());
			for (int i = 0; i < resources.size(); i++) {
				ourLog.info("**************************************************************************");
				ourLog.info("**************************************************************************");
				ourLog.info("Res " + i);
				ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(resources.get(i)));
			}
		});

		myPackageInstallerSvc.install(spec);
	}

	@Test
	public void testValidationCache_whenInstallingIG_isRefreshed() {
		Patient patient = new Patient();
		patient.setMeta(new Meta().addProfile("https://fhir.nhs.uk/R4/StructureDefinition/UKCore-Patient"));

		ValidationResult validationResultBefore = validateWithResult(patient);
		assertFalse(validationResultBefore.isSuccessful());

		byte[] bytes = ClasspathUtil.loadResourceAsByteArray("/packages/UK.Core.r4-1.1.0.tgz");
		myFakeNpmServlet.responses.put("/UK.Core.r4/1.1.0", bytes);

		PackageInstallationSpec spec = new PackageInstallationSpec().setName("UK.Core.r4").setVersion("1.1.0")
			.setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_AND_INSTALL);

		myPackageInstallerSvc.install(spec);

		ValidationResult validationResultAfter = validateWithResult(patient);
		assertTrue(validationResultAfter.isSuccessful());
	}

	@Test
	public void testValidationCache_whenUnInstallingIG_isRefreshed() {
		byte[] bytes = ClasspathUtil.loadResourceAsByteArray("/packages/UK.Core.r4-1.1.0.tgz");
		myFakeNpmServlet.responses.put("/UK.Core.r4/1.1.0", bytes);

		PackageInstallationSpec spec = new PackageInstallationSpec().setName("UK.Core.r4").setVersion("1.1.0")
			.setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_AND_INSTALL);

		myPackageInstallerSvc.install(spec);

		Patient patient = new Patient();
		patient.setMeta(new Meta().addProfile("https://fhir.nhs.uk/R4/StructureDefinition/UKCore-Patient"));

		ValidationResult validationResultBefore = validateWithResult(patient);
		assertTrue(validationResultBefore.isSuccessful());

		myPackageInstallerSvc.uninstall(spec);

		ValidationResult validationResultAfter = validateWithResult(patient);
		assertFalse(validationResultAfter.isSuccessful());
	}

	@Test
	public void testCacheDstu3Package() throws Exception {
		byte[] bytes = ClasspathUtil.loadResourceAsByteArray("/packages/nictiz.fhir.nl.stu3.questionnaires-1.0.2.tgz");
		myFakeNpmServlet.responses.put("/nictiz.fhir.nl.stu3.questionnaires/1.0.2", bytes);

		PackageInstallationSpec spec = new PackageInstallationSpec().setName("nictiz.fhir.nl.stu3.questionnaires").setVersion("1.0.2").setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_ONLY);
		myPackageInstallerSvc.install(spec);

		// Be sure no further communication with the server
		myServer.stopServer();

		// Make sure we can fetch the package by ID and Version
		NpmPackage pkg = myPackageCacheManager.loadPackage("nictiz.fhir.nl.stu3.questionnaires", "1.0.2");
		assertEquals("Nictiz NL package of FHIR STU3 conformance resources for MedMij information standard Questionnaires. Includes dependency on Zib2017 and SDC.\\n\\nHCIMs: https://zibs.nl/wiki/HCIM_Release_2017(EN)", pkg.description());

		// Make sure we can fetch the package by ID
		pkg = myPackageCacheManager.loadPackage("nictiz.fhir.nl.stu3.questionnaires", null);
		assertEquals("1.0.2", pkg.version());
		assertEquals("Nictiz NL package of FHIR STU3 conformance resources for MedMij information standard Questionnaires. Includes dependency on Zib2017 and SDC.\\n\\nHCIMs: https://zibs.nl/wiki/HCIM_Release_2017(EN)", pkg.description());

		// Fetch resource by URL
		FhirContext fhirContext = FhirContext.forDstu3Cached();
		runInTransaction(() -> {
			IBaseResource asset = myPackageCacheManager.loadPackageAssetByUrl(FhirVersionEnum.DSTU3, "http://nictiz.nl/fhir/StructureDefinition/vl-QuestionnaireResponse");
			assertThat(fhirContext.newJsonParser().encodeResourceToString(asset)).contains("\"url\":\"http://nictiz.nl/fhir/StructureDefinition/vl-QuestionnaireResponse\",\"version\":\"1.0.1\"");
		});

		// Fetch resource by URL with version
		runInTransaction(() -> {
			IBaseResource asset = myPackageCacheManager.loadPackageAssetByUrl(FhirVersionEnum.DSTU3, "http://nictiz.nl/fhir/StructureDefinition/vl-QuestionnaireResponse|1.0.1");
			assertThat(fhirContext.newJsonParser().encodeResourceToString(asset)).contains("\"url\":\"http://nictiz.nl/fhir/StructureDefinition/vl-QuestionnaireResponse\",\"version\":\"1.0.1\"");
		});

		// This was saved but is the wrong version of FHIR for this server
		assertNull(myNpmJpaValidationSupport.fetchStructureDefinition("http://fhir.de/StructureDefinition/condition-de-basis/0.2"));
	}

	@Test
	public void testInstallR4Package() throws Exception {
		myStorageSettings.setAllowExternalReferences(true);

		byte[] bytes = ClasspathUtil.loadResourceAsByteArray("/packages/hl7.fhir.uv.shorthand-0.12.0.tgz");
		myFakeNpmServlet.responses.put("/hl7.fhir.uv.shorthand/0.12.0", bytes);

		PackageInstallationSpec spec = new PackageInstallationSpec()
			.setName("hl7.fhir.uv.shorthand")
			.setVersion("0.12.0")
			.setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_AND_INSTALL);
		PackageInstallOutcomeJson outcome = myPackageInstallerSvc.install(spec);
		assertThat(outcome.getResourcesInstalled()).containsEntry("CodeSystem", 1);

		// Be sure no further communication with the server
		myServer.stopServer();

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
			assertThat(myFhirContext.newJsonParser().encodeResourceToString(asset)).contains("\"url\":\"http://hl7.org/fhir/uv/shorthand/ImplementationGuide/hl7.fhir.uv.shorthand\",\"version\":\"0.12.0\"");
		});

		// Fetch resource by URL with version
		runInTransaction(() -> {
			IBaseResource asset = myPackageCacheManager.loadPackageAssetByUrl(FhirVersionEnum.R4, "http://hl7.org/fhir/uv/shorthand/ImplementationGuide/hl7.fhir.uv.shorthand|0.12.0");
			assertThat(myFhirContext.newJsonParser().encodeResourceToString(asset)).contains("\"url\":\"http://hl7.org/fhir/uv/shorthand/ImplementationGuide/hl7.fhir.uv.shorthand\",\"version\":\"0.12.0\"");
		});

		// Search for the installed resource
		runInTransaction(() -> {
			SearchParameterMap map = SearchParameterMap.newSynchronous();
			map.add(StructureDefinition.SP_URL, new UriParam("http://hl7.org/fhir/uv/shorthand/CodeSystem/shorthand-code-system"));
			IBundleProvider result = myCodeSystemDao.search(map);
			assertEquals(1, result.sizeOrThrowNpe());
			IBaseResource resource = result.getResources(0, 1).get(0);
			assertEquals("CodeSystem/shorthand-code-system/_history/1", resource.getIdElement().toString());
		});
	}

	@Test
	public void testInstallR4PackageWithExternalizedBinaries() throws Exception {
		myStorageSettings.setAllowExternalReferences(true);

		myInterceptorService.registerInterceptor(myBinaryStorageInterceptor);
		byte[] bytes = ClasspathUtil.loadResourceAsByteArray("/packages/hl7.fhir.uv.shorthand-0.12.0.tgz");
		myFakeNpmServlet.responses.put("/hl7.fhir.uv.shorthand/0.12.0", bytes);

		PackageInstallationSpec spec = new PackageInstallationSpec().setName("hl7.fhir.uv.shorthand").setVersion("0.12.0").setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_AND_INSTALL);
		PackageInstallOutcomeJson outcome = myPackageInstallerSvc.install(spec);
		assertThat(outcome.getResourcesInstalled()).containsEntry("CodeSystem", 1);

		// Be sure no further communication with the server
		myServer.stopServer();

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
			assertThat(myFhirContext.newJsonParser().encodeResourceToString(asset)).contains("\"url\":\"http://hl7.org/fhir/uv/shorthand/ImplementationGuide/hl7.fhir.uv.shorthand\",\"version\":\"0.12.0\"");
		});

		// Fetch resource by URL with version
		runInTransaction(() -> {
			IBaseResource asset = myPackageCacheManager.loadPackageAssetByUrl(FhirVersionEnum.R4, "http://hl7.org/fhir/uv/shorthand/ImplementationGuide/hl7.fhir.uv.shorthand|0.12.0");
			assertThat(myFhirContext.newJsonParser().encodeResourceToString(asset)).contains("\"url\":\"http://hl7.org/fhir/uv/shorthand/ImplementationGuide/hl7.fhir.uv.shorthand\",\"version\":\"0.12.0\"");
		});

		// Search for the installed resource
		runInTransaction(() -> {
			SearchParameterMap map = SearchParameterMap.newSynchronous();
			map.add(StructureDefinition.SP_URL, new UriParam("http://hl7.org/fhir/uv/shorthand/CodeSystem/shorthand-code-system"));
			IBundleProvider result = myCodeSystemDao.search(map);
			assertEquals(1, result.sizeOrThrowNpe());
			IBaseResource resource = result.getResources(0, 1).get(0);
			assertEquals("CodeSystem/shorthand-code-system/_history/1", resource.getIdElement().toString());
		});

		myInterceptorService.unregisterInterceptor(myBinaryStorageInterceptor);
	}

	@Test
	public void testNumericIdsInstalledWithNpmPrefix() throws Exception {
			myStorageSettings.setAllowExternalReferences(true);

		// Load a copy of hl7.fhir.uv.shorthand-0.12.0, but with id set to 1 instead of "shorthand-code-system"
		byte[] bytes = ClasspathUtil.loadResourceAsByteArray("/packages/hl7.fhir.uv.shorthand-0.13.0.tgz");
		myFakeNpmServlet.responses.put("/hl7.fhir.uv.shorthand/0.13.0", bytes);

		PackageInstallationSpec spec = new PackageInstallationSpec().setName("hl7.fhir.uv.shorthand").setVersion("0.13.0").setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_AND_INSTALL);
		PackageInstallOutcomeJson outcome = myPackageInstallerSvc.install(spec);
		// Be sure no further communication with the server
		myServer.stopServer();

		// Search for the installed resource
		runInTransaction(() -> {
			SearchParameterMap map = SearchParameterMap.newSynchronous();
			map.add(StructureDefinition.SP_URL, new UriParam("http://hl7.org/fhir/uv/shorthand/CodeSystem/shorthand-code-system"));
			IBundleProvider result = myCodeSystemDao.search(map);
			assertEquals(1, result.sizeOrThrowNpe());
			IBaseResource resource = result.getResources(0, 1).get(0);
			assertEquals("CodeSystem/npm-1/_history/1", resource.getIdElement().toString());
		});

	}

	@Test
	public void testInstallR4Package_NonConformanceResources() throws Exception {
		myStorageSettings.setAllowExternalReferences(true);

		byte[] bytes = ClasspathUtil.loadResourceAsByteArray("/packages/test-organizations-package.tgz");
		myFakeNpmServlet.responses.put("/test-organizations/1.0.0", bytes);

		List<String> resourceList = new ArrayList<>();
		resourceList.add("Organization");
		PackageInstallationSpec spec = new PackageInstallationSpec().setName("test-organizations").setVersion("1.0.0").setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_AND_INSTALL);
		spec.setInstallResourceTypes(resourceList);
		PackageInstallOutcomeJson outcome = myPackageInstallerSvc.install(spec);
		assertThat(outcome.getResourcesInstalled()).containsEntry("Organization", 3);

		// Be sure no further communication with the server
		myServer.stopServer();

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
	public void testInstallR4Package_NonConformanceResources_Partitioned() throws Exception {
		myPartitionSettings.setPartitioningEnabled(true);
		myInterceptorService.registerInterceptor(myRequestTenantPartitionInterceptor);
		myStorageSettings.setAllowExternalReferences(true);

		byte[] bytes = ClasspathUtil.loadResourceAsByteArray("/packages/test-organizations-package.tgz");
		myFakeNpmServlet.responses.put("/test-organizations/1.0.0", bytes);

		List<String> resourceList = new ArrayList<>();
		resourceList.add("Organization");
		PackageInstallationSpec spec = new PackageInstallationSpec()
			.setName("test-organizations")
			.setVersion("1.0.0")
			.setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_AND_INSTALL);
		spec.setInstallResourceTypes(resourceList);
		PackageInstallOutcomeJson outcome = myPackageInstallerSvc.install(spec);
		assertThat(outcome.getResourcesInstalled()).containsEntry("Organization", 3);

		// Be sure no further communication with the server
		myServer.stopServer();

		// Search for the installed resources
		mySrd = mock(ServletRequestDetails.class);
		when(mySrd.getTenantId()).thenReturn(JpaConstants.DEFAULT_PARTITION_NAME);
		when(mySrd.getServer()).thenReturn(mock(RestfulServer.class));
		when(mySrd.getInterceptorBroadcaster()).thenReturn(mock(IInterceptorBroadcaster.class));
		runInTransaction(() -> {
			SearchParameterMap map = SearchParameterMap.newSynchronous();
			map.add(Organization.SP_IDENTIFIER, new TokenParam("https://github.com/synthetichealth/synthea", "organization1"));
			IBundleProvider result = myOrganizationDao.search(map, mySrd);
			assertEquals(1, result.sizeOrThrowNpe());
			map = SearchParameterMap.newSynchronous();
			map.add(Organization.SP_IDENTIFIER, new TokenParam("https://github.com/synthetichealth/synthea", "organization2"));
			result = myOrganizationDao.search(map, mySrd);
			assertEquals(1, result.sizeOrThrowNpe());
			map = SearchParameterMap.newSynchronous();
			map.add(Organization.SP_IDENTIFIER, new TokenParam("https://github.com/synthetichealth/synthea", "organization3"));
			result = myOrganizationDao.search(map, mySrd);
			assertEquals(1, result.sizeOrThrowNpe());
		});

	}

	@Test
	public void testInstallR4Package_NoIdentifierNoUrl() {
		myStorageSettings.setAllowExternalReferences(true);

		byte[] bytes = ClasspathUtil.loadResourceAsByteArray("/packages/test-missing-identifier-package.tgz");
		myFakeNpmServlet.responses.put("/test-missing-identifier-package/1.0.0", bytes);

		List<String> resourceList = new ArrayList<>();
		resourceList.add("Organization");
		PackageInstallationSpec spec = new PackageInstallationSpec()
			.setName("test-missing-identifier-package")
			.setVersion("1.0.0")
			.setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_AND_INSTALL);
		spec.setInstallResourceTypes(resourceList);
		try {
			PackageInstallOutcomeJson outcome = myPackageInstallerSvc.install(spec);
			fail(outcome.toString());
		} catch (ImplementationGuideInstallationException theE) {
			assertThat(theE.getMessage()).contains("Resources in a package must have a url or identifier to be loaded by the package installer.");
		}
	}

	/**
	 * Reproduces https://github.com/hapifhir/hapi-fhir/issues/2332
	 */
	@Test
	public void testInstallR4Package_AutoCreatePlaceholder() throws Exception {
		myStorageSettings.setAllowExternalReferences(true);
		myStorageSettings.setAutoCreatePlaceholderReferenceTargets(true);

		byte[] bytes = ClasspathUtil.loadResourceAsByteArray("/packages/test-auto-create-placeholder.tgz");
		myFakeNpmServlet.responses.put("/test-ig/1.0.0", bytes);

		List<String> resourceList = new ArrayList<>();
		resourceList.add("ImplementationGuide");
		PackageInstallationSpec spec = new PackageInstallationSpec()
			.setName("test-ig")
			.setVersion("1.0.0")
			.setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_AND_INSTALL);
		spec.setInstallResourceTypes(resourceList);
		PackageInstallOutcomeJson outcome = myPackageInstallerSvc.install(spec);
		ourLog.info("Outcome: {}", outcome);
		assertThat(outcome.getResourcesInstalled()).containsEntry("ImplementationGuide", 1);

		// Be sure no further communication with the server
		myServer.stopServer();

		// Search for the installed resources
		runInTransaction(() -> {
			SearchParameterMap map = SearchParameterMap.newSynchronous();
			IBundleProvider result = myImplementationGuideDao.search(map);
			assertEquals(1, result.sizeOrThrowNpe());
		});

	}

	@Test
	public void testInstallR4Package_DraftResourcesNotInstalled() throws Exception {
		myStorageSettings.setAllowExternalReferences(true);

		byte[] bytes = ClasspathUtil.loadResourceAsByteArray("/packages/test-draft-sample.tgz");
		myFakeNpmServlet.responses.put("/hl7.fhir.uv.onlydrafts/0.11.1", bytes);

		PackageInstallationSpec spec = new PackageInstallationSpec().setName("hl7.fhir.uv.onlydrafts").setVersion("0.11.1").setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_AND_INSTALL);
		PackageInstallOutcomeJson outcome = myPackageInstallerSvc.install(spec);
		assertThat(outcome.getResourcesInstalled().size()).as(outcome.getResourcesInstalled().toString()).isEqualTo(0);
	}

	@Test
	public void testInstallR4Package_Twice() throws Exception {
		myStorageSettings.setAllowExternalReferences(true);

		byte[] bytes = ClasspathUtil.loadResourceAsByteArray("/packages/hl7.fhir.uv.shorthand-0.12.0.tgz");
		myFakeNpmServlet.responses.put("/hl7.fhir.uv.shorthand/0.12.0", bytes);

		PackageInstallOutcomeJson outcome;

		PackageInstallationSpec spec = new PackageInstallationSpec().setName("hl7.fhir.uv.shorthand").setVersion("0.12.0").setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_AND_INSTALL);
		outcome = myPackageInstallerSvc.install(spec);
		assertThat(outcome.getResourcesInstalled()).containsEntry("CodeSystem", 1);

		myPackageInstallerSvc.install(spec);
		outcome = myPackageInstallerSvc.install(spec);
		assertNull(outcome.getResourcesInstalled().get("CodeSystem"));

		// Ensure that we loaded the contents
		IBundleProvider searchResult = myCodeSystemDao.search(SearchParameterMap.newSynchronous("url", new UriParam("http://hl7.org/fhir/uv/shorthand/CodeSystem/shorthand-code-system")));
		assertEquals(1, searchResult.sizeOrThrowNpe());

	}

	@Test
	public void testInstallR4Package_Twice_partitioningEnabled() throws Exception {
		myStorageSettings.setAllowExternalReferences(true);
		myPartitionSettings.setPartitioningEnabled(true);
		myInterceptorService.registerInterceptor(myRequestTenantPartitionInterceptor);

		byte[] bytes = ClasspathUtil.loadResourceAsByteArray("/packages/hl7.fhir.uv.shorthand-0.12.0.tgz");
		myFakeNpmServlet.responses.put("/hl7.fhir.uv.shorthand/0.12.0", bytes);

		PackageInstallOutcomeJson outcome;

		PackageInstallationSpec spec = new PackageInstallationSpec().setName("hl7.fhir.uv.shorthand").setVersion("0.12.0").setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_AND_INSTALL);
		outcome = myPackageInstallerSvc.install(spec);
		assertThat(outcome.getResourcesInstalled()).containsEntry("CodeSystem", 1);

		myPackageInstallerSvc.install(spec);
		outcome = myPackageInstallerSvc.install(spec);
		assertNull(outcome.getResourcesInstalled().get("CodeSystem"));

		// Ensure that we loaded the contents
		IBundleProvider searchResult = myCodeSystemDao.search(SearchParameterMap.newSynchronous("url", new UriParam("http://hl7.org/fhir/uv/shorthand/CodeSystem/shorthand-code-system")));
		assertEquals(1, searchResult.sizeOrThrowNpe());
	}

	@Test
	public void testInstallR4PackageWithNoDescription() throws Exception {
		myStorageSettings.setAllowExternalReferences(true);

		byte[] bytes = ClasspathUtil.loadResourceAsByteArray("/packages/UK.Core.r4-1.1.0.tgz");
		myFakeNpmServlet.responses.put("/UK.Core.r4/1.1.0", bytes);

		PackageInstallationSpec spec = new PackageInstallationSpec().setName("UK.Core.r4").setVersion("1.1.0").setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_AND_INSTALL);
		myPackageInstallerSvc.install(spec);

		// Be sure no further communication with the server
		myServer.stopServer();

		// Make sure we can fetch the package by ID and Version
		NpmPackage pkg = myPackageCacheManager.loadPackage("UK.Core.r4", "1.1.0");
		assertNull(pkg.description());
		assertEquals("UK.Core.r4", pkg.name());

	}

	@Test
	public void testLoadPackageMetadata() throws Exception {
		myStorageSettings.setAllowExternalReferences(true);

		myFakeNpmServlet.responses.put("/hl7.fhir.uv.shorthand/0.12.0", ClasspathUtil.loadResourceAsByteArray("/packages/hl7.fhir.uv.shorthand-0.12.0.tgz"));
		myFakeNpmServlet.responses.put("/hl7.fhir.uv.shorthand/0.11.1", ClasspathUtil.loadResourceAsByteArray("/packages/hl7.fhir.uv.shorthand-0.11.1.tgz"));

		PackageInstallationSpec spec = new PackageInstallationSpec().setName("hl7.fhir.uv.shorthand").setVersion("0.12.0").setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_ONLY);
		myPackageInstallerSvc.install(spec);
		spec = new PackageInstallationSpec().setName("hl7.fhir.uv.shorthand").setVersion("0.11.1").setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_ONLY);
		myPackageInstallerSvc.install(spec);

		runInTransaction(() -> {
			NpmPackageMetadataJson metadata = myPackageCacheManager.loadPackageMetadata("hl7.fhir.uv.shorthand");
			ourLog.info(JsonUtil.serialize(metadata));

			assertEquals("0.12.0", metadata.getDistTags().getLatest());

			assertThat(metadata.getVersions().keySet()).containsExactly("0.12.0", "0.11.1");

			NpmPackageMetadataJson.Version version0120 = metadata.getVersions().get("0.12.0");
			assertEquals(3001, version0120.getBytes());
		});

	}

	@Test
	public void testLoadPackageUsingImpreciseId() throws Exception {
		myStorageSettings.setAllowExternalReferences(true);

		myFakeNpmServlet.responses.put("/hl7.fhir.uv.shorthand/0.12.0", ClasspathUtil.loadResourceAsByteArray("/packages/hl7.fhir.uv.shorthand-0.12.0.tgz"));
		myFakeNpmServlet.responses.put("/hl7.fhir.uv.shorthand/0.11.1", ClasspathUtil.loadResourceAsByteArray("/packages/hl7.fhir.uv.shorthand-0.11.1.tgz"));
		myFakeNpmServlet.responses.put("/hl7.fhir.uv.shorthand/0.11.0", ClasspathUtil.loadResourceAsByteArray("/packages/hl7.fhir.uv.shorthand-0.11.0.tgz"));

		PackageInstallationSpec spec;
		spec = new PackageInstallationSpec().setName("hl7.fhir.uv.shorthand").setVersion("0.12.0").setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_ONLY);
		PackageInstallOutcomeJson outcome = myPackageInstallerSvc.install(spec);
		ourLog.info("Install messages:\n * {}", outcome.getMessage().stream().collect(Collectors.joining("\n * ")));
		assertThat(outcome.getMessage()).contains("Marking package hl7.fhir.uv.shorthand#0.12.0 as current version");
		assertThat(outcome.getMessage()).contains("Indexing CodeSystem Resource[package/CodeSystem-shorthand-code-system.json] with URL: http://hl7.org/fhir/uv/shorthand/CodeSystem/shorthand-code-system|0.12.0");

		spec = new PackageInstallationSpec().setName("hl7.fhir.uv.shorthand").setVersion("0.11.1").setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_ONLY);
		outcome = myPackageInstallerSvc.install(spec);
		ourLog.info("Install messages:\n * {}", outcome.getMessage().stream().collect(Collectors.joining("\n * ")));
		assertThat(outcome.getMessage()).doesNotContain("Marking package hl7.fhir.uv.shorthand#0.11.1 as current version");

		spec = new PackageInstallationSpec().setName("hl7.fhir.uv.shorthand").setVersion("0.11.0").setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_ONLY);
		myPackageInstallerSvc.install(spec);


		NpmPackage pkg;

		pkg = myPackageCacheManager.loadPackage("hl7.fhir.uv.shorthand", "0.11.x");
		assertEquals("0.11.1", pkg.version());

		pkg = myPackageCacheManager.loadPackage("hl7.fhir.uv.shorthand", "0.12.x");
		assertEquals("0.12.0", pkg.version());

	}

	@Test
	public void testInstallNewerPackageUpdatesLatestVersionFlag() throws Exception {
		myStorageSettings.setAllowExternalReferences(true);

		byte[] contents0111 = ClasspathUtil.loadResourceAsByteArray("/packages/hl7.fhir.uv.shorthand-0.11.1.tgz");
		byte[] contents0120 = ClasspathUtil.loadResourceAsByteArray("/packages/hl7.fhir.uv.shorthand-0.12.0.tgz");
		myFakeNpmServlet.responses.put("/hl7.fhir.uv.shorthand/0.11.1", contents0111);
		myFakeNpmServlet.responses.put("/hl7.fhir.uv.shorthand/0.12.0", contents0120);

		// Install older version
		PackageInstallationSpec spec = new PackageInstallationSpec().setName("hl7.fhir.uv.shorthand").setVersion("0.11.1").setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_ONLY);
		myPackageInstallerSvc.install(spec);

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
		myPackageInstallerSvc.install(spec);

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
		myStorageSettings.setAllowExternalReferences(true);

		myFakeNpmServlet.responses.put("/hl7.fhir.uv.shorthand/0.12.0", ClasspathUtil.loadResourceAsByteArray("/packages/hl7.fhir.uv.shorthand-0.12.0.tgz"));
		myFakeNpmServlet.responses.put("/hl7.fhir.uv.shorthand/0.11.1", ClasspathUtil.loadResourceAsByteArray("/packages/hl7.fhir.uv.shorthand-0.11.1.tgz"));

		// Install newer version
		PackageInstallationSpec spec = new PackageInstallationSpec().setName("hl7.fhir.uv.shorthand").setVersion("0.12.0").setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_ONLY);
		myPackageInstallerSvc.install(spec);


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
		myPackageInstallerSvc.install(spec);

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
		myStorageSettings.setAllowExternalReferences(true);

		myFakeNpmServlet.responses.put("/hl7.fhir.uv.shorthand/0.12.0", ClasspathUtil.loadResourceAsByteArray("/packages/hl7.fhir.uv.shorthand-0.12.0.tgz"));

		// Install
		PackageInstallationSpec spec = new PackageInstallationSpec().setName("hl7.fhir.uv.shorthand").setVersion("0.12.0").setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_ONLY);
		myPackageInstallerSvc.install(spec);

		runInTransaction(() -> {
			NpmPackageVersionEntity versionEntity = myPackageVersionDao.findByPackageIdAndVersion("hl7.fhir.uv.shorthand", "0.12.0").orElseThrow(() -> new IllegalArgumentException());
			assertEquals(true, versionEntity.isCurrentVersion());
		});

		// Install same again
		spec = new PackageInstallationSpec().setName("hl7.fhir.uv.shorthand").setVersion("0.12.0").setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_ONLY);
		myPackageInstallerSvc.install(spec);

		runInTransaction(() -> {
			NpmPackageVersionEntity versionEntity = myPackageVersionDao.findByPackageIdAndVersion("hl7.fhir.uv.shorthand", "0.12.0").orElseThrow(() -> new IllegalArgumentException());
			assertEquals(true, versionEntity.isCurrentVersion());
		});
	}

	@Test
	public void testInstallPkgContainingSearchParameter() throws IOException {
		myStorageSettings.setAllowExternalReferences(true);

		byte[] contents0111 = ClasspathUtil.loadResourceAsByteArray("/packages/test-exchange-sample.tgz");
		myFakeNpmServlet.responses.put("/test-exchange.fhir.us.com/2.1.1", contents0111);

		contents0111 = ClasspathUtil.loadResourceAsByteArray("/packages/test-exchange-sample-2.tgz");
		myFakeNpmServlet.responses.put("/test-exchange.fhir.us.com/2.1.2", contents0111);

		// Install older version
		PackageInstallationSpec spec = new PackageInstallationSpec().setName("test-exchange.fhir.us.com").setVersion("2.1.1").setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_AND_INSTALL);
		myPackageInstallerSvc.install(spec);

		IBundleProvider spSearch = mySearchParameterDao.search(SearchParameterMap.newSynchronous("code", new TokenParam("network-id")), new SystemRequestDetails());
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

		spSearch = myPractitionerRoleDao.search(map, new SystemRequestDetails());
		assertEquals(1, spSearch.sizeOrThrowNpe());

		// Install newer version
		spec = new PackageInstallationSpec().setName("test-exchange.fhir.us.com").setVersion("2.1.2").setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_AND_INSTALL);
		myPackageInstallerSvc.install(spec);

		spSearch = mySearchParameterDao.search(SearchParameterMap.newSynchronous("code", new TokenParam("network-id")));
		assertEquals(1, spSearch.sizeOrThrowNpe());
		sp = (SearchParameter) spSearch.getResources(0, 1).get(0);
		assertEquals("network-id", sp.getCode());
		assertEquals(Enumerations.PublicationStatus.ACTIVE, sp.getStatus());
		assertEquals("2.2", sp.getVersion());

	}


	@Test
	public void testLoadContents() throws IOException {
		byte[] contents0111 = ClasspathUtil.loadResourceAsByteArray("/packages/hl7.fhir.uv.shorthand-0.11.1.tgz");
		byte[] contents0120 = ClasspathUtil.loadResourceAsByteArray("/packages/hl7.fhir.uv.shorthand-0.12.0.tgz");

		PackageInstallationSpec spec = new PackageInstallationSpec().setName("hl7.fhir.uv.shorthand").setVersion("0.11.1").setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_ONLY).setPackageContents(contents0111);
		myPackageInstallerSvc.install(spec);
		spec = new PackageInstallationSpec().setName("hl7.fhir.uv.shorthand").setVersion("0.12.0").setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_ONLY).setPackageContents(contents0120);
		myPackageInstallerSvc.install(spec);


		assertThat(myPackageCacheManager.loadPackageContents("hl7.fhir.uv.shorthand", "0.11.1").getBytes()).containsExactly(contents0111);
		assertThat(myPackageCacheManager.loadPackageContents("hl7.fhir.uv.shorthand", "0.12.0").getBytes()).containsExactly(contents0120);
		assertThat(myPackageCacheManager.loadPackageContents("hl7.fhir.uv.shorthand", "latest").getBytes()).containsExactly(contents0120);
		assertEquals("0.11.1", myPackageCacheManager.loadPackageContents("hl7.fhir.uv.shorthand", "0.11.1").getVersion());
		assertEquals("0.12.0", myPackageCacheManager.loadPackageContents("hl7.fhir.uv.shorthand", "0.12.0").getVersion());
		assertEquals("0.12.0", myPackageCacheManager.loadPackageContents("hl7.fhir.uv.shorthand", "latest").getVersion());
		assertNull(myPackageCacheManager.loadPackageContents("hl7.fhir.uv.shorthand", "1.2.3"));
		assertNull(myPackageCacheManager.loadPackageContents("foo", "1.2.3"));
	}


	@Test
	public void testDeletePackage() throws IOException {
		myStorageSettings.setAllowExternalReferences(true);

		myFakeNpmServlet.responses.put("/hl7.fhir.uv.shorthand/0.12.0", ClasspathUtil.loadResourceAsByteArray("/packages/hl7.fhir.uv.shorthand-0.12.0.tgz"));
		myFakeNpmServlet.responses.put("/hl7.fhir.uv.shorthand/0.11.1", ClasspathUtil.loadResourceAsByteArray("/packages/hl7.fhir.uv.shorthand-0.11.1.tgz"));
		myFakeNpmServlet.responses.put("/hl7.fhir.uv.shorthand/0.11.0", ClasspathUtil.loadResourceAsByteArray("/packages/hl7.fhir.uv.shorthand-0.11.0.tgz"));

		myPackageInstallerSvc.install(new PackageInstallationSpec().setName("hl7.fhir.uv.shorthand").setVersion("0.12.0").setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_ONLY));
		myPackageInstallerSvc.install(new PackageInstallationSpec().setName("hl7.fhir.uv.shorthand").setVersion("0.11.1").setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_ONLY));
		myPackageInstallerSvc.install(new PackageInstallationSpec().setName("hl7.fhir.uv.shorthand").setVersion("0.11.0").setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_ONLY));

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

	@Test
	public void testInstallPkgContainingLogicalStructureDefinition() throws Exception {
		myStorageSettings.setAllowExternalReferences(true);

		byte[] bytes = ClasspathUtil.loadResourceAsByteArray("/packages/test-logical-structuredefinition.tgz");
		myFakeNpmServlet.responses.put("/test-logical-structuredefinition/1.0.0", bytes);

		PackageInstallationSpec spec = new PackageInstallationSpec().setName("test-logical-structuredefinition").setVersion("1.0.0").setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_AND_INSTALL);
		PackageInstallOutcomeJson outcome = myPackageInstallerSvc.install(spec);
		assertThat(outcome.getResourcesInstalled()).containsEntry("StructureDefinition", 2);

		// Be sure no further communication with the server
		myServer.stopServer();

		// Search for the installed resource
		runInTransaction(() -> {
			// Confirm that Laborbefund (a logical StructureDefinition) was created without a snapshot.
			SearchParameterMap map = SearchParameterMap.newSynchronous();
			map.add(StructureDefinition.SP_URL, new UriParam("https://www.medizininformatik-initiative.de/fhir/core/modul-labor/StructureDefinition/LogicalModel/Laborbefund"));
			IBundleProvider result = myStructureDefinitionDao.search(map);
			assertEquals(1, result.sizeOrThrowNpe());
			List<IBaseResource> resources = result.getResources(0, 1);
			assertFalse(((StructureDefinition) resources.get(0)).hasSnapshot());

			// Confirm that DiagnosticLab (a resource StructureDefinition with differential but no snapshot) was created with a generated snapshot.
			map = SearchParameterMap.newSynchronous();
			map.add(StructureDefinition.SP_URL, new UriParam("https://www.medizininformatik-initiative.de/fhir/core/modul-labor/StructureDefinition/DiagnosticReportLab"));
			result = myStructureDefinitionDao.search(map);
			assertEquals(1, result.sizeOrThrowNpe());
			resources = result.getResources(0, 1);
			assertTrue(((StructureDefinition) resources.get(0)).hasSnapshot());

		});
	}

	@Test
	public void testInstallPkgContainingNonPartitionedResourcesPartitionsEnabled() throws Exception {
		myStorageSettings.setAllowExternalReferences(true);
		myPartitionSettings.setPartitioningEnabled(true);
		myInterceptorService.registerInterceptor(myRequestTenantPartitionInterceptor);

		byte[] bytes = ClasspathUtil.loadResourceAsByteArray("/packages/test-logical-structuredefinition.tgz");
		myFakeNpmServlet.responses.put("/test-logical-structuredefinition/1.0.0", bytes);

		PackageInstallationSpec spec = new PackageInstallationSpec().setName("test-logical-structuredefinition").setVersion("1.0.0").setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_AND_INSTALL);
		PackageInstallOutcomeJson outcome = myPackageInstallerSvc.install(spec);
		assertThat(outcome.getResourcesInstalled()).containsEntry("StructureDefinition", 2);

		// Be sure no further communication with the server
		myServer.stopServer();

		// Search for the installed resource
		runInTransaction(() -> {
			// Confirm that Laborbefund (a logical StructureDefinition) was created without a snapshot.
			SearchParameterMap map = SearchParameterMap.newSynchronous();
			map.add(StructureDefinition.SP_URL, new UriParam("https://www.medizininformatik-initiative.de/fhir/core/modul-labor/StructureDefinition/LogicalModel/Laborbefund"));
			IBundleProvider result = myStructureDefinitionDao.search(map);
			assertEquals(1, result.sizeOrThrowNpe());
			List<IBaseResource> resources = result.getResources(0, 1);
			assertFalse(((StructureDefinition) resources.get(0)).hasSnapshot());

			// Confirm that DiagnosticLab (a resource StructureDefinition with differential but no snapshot) was created with a generated snapshot.
			map = SearchParameterMap.newSynchronous();
			map.add(StructureDefinition.SP_URL, new UriParam("https://www.medizininformatik-initiative.de/fhir/core/modul-labor/StructureDefinition/DiagnosticReportLab"));
			result = myStructureDefinitionDao.search(map);
			assertEquals(1, result.sizeOrThrowNpe());
			resources = result.getResources(0, 1);
			assertTrue(((StructureDefinition) resources.get(0)).hasSnapshot());

		});
	}

	@Test
	public void testInstallR4PackageFromFile() {

		Path currentRelativePath = Paths.get("");
		String s = currentRelativePath.toAbsolutePath().toString().replace('\\', '/');
		if (s.charAt(0) != '/' && s.charAt(1) == ':') { // is Windows..
			s = s.substring(2); // .. get rid of the "C:" part (not perfect but...
		}
		ourLog.info("Current absolute path is: " + s);

		String fileUrl = "file:" + s + "/src/test/resources/packages/de.basisprofil.r4-1.2.0.tgz";

		myPackageInstallerSvc.install(new PackageInstallationSpec()
			.setName("de.basisprofil.r4")
			.setVersion("1.2.0")
			.setPackageUrl(fileUrl)
		);
		runInTransaction(() -> {
			assertTrue(myPackageVersionDao.findByPackageIdAndVersion("de.basisprofil.r4", "1.2.0").isPresent());
		});
	}


}
