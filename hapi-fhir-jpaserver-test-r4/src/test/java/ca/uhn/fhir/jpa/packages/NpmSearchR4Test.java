package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.jpa.dao.data.INpmPackageDao;
import ca.uhn.fhir.jpa.dao.data.INpmPackageVersionDao;
import ca.uhn.fhir.jpa.dao.data.INpmPackageVersionResourceDao;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.test.utilities.ProxyUtil;
import ca.uhn.fhir.util.ClasspathUtil;
import ca.uhn.fhir.util.JsonUtil;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class NpmSearchR4Test extends BaseJpaR4Test {

	private static final Logger ourLog = LoggerFactory.getLogger(NpmSearchR4Test.class);
	@Autowired
	public IPackageInstallerSvc igInstaller;
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

	@BeforeEach
	public void before() throws Exception {
		JpaPackageCache jpaPackageCache = ProxyUtil.getSingletonTarget(myPackageCacheManager, JpaPackageCache.class);
		jpaPackageCache.getPackageServers().clear();
	}

	@Test
	public void testSearch() throws IOException {
		PackageInstallationSpec spec;
		byte[] bytes;

		bytes = ClasspathUtil.loadResourceAsByteArray("/packages/hl7.fhir.uv.shorthand-0.11.1.tgz");
		spec = new PackageInstallationSpec().setName("hl7.fhir.uv.shorthand").setVersion("0.11.1").setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_ONLY).setPackageContents(bytes);
		igInstaller.install(spec);

		bytes = ClasspathUtil.loadResourceAsByteArray("/packages/hl7.fhir.uv.shorthand-0.12.0.tgz");
		spec = new PackageInstallationSpec().setName("hl7.fhir.uv.shorthand").setVersion("0.12.0").setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_ONLY).setPackageContents(bytes);
		igInstaller.install(spec);

		bytes = ClasspathUtil.loadResourceAsByteArray("/packages/nictiz.fhir.nl.stu3.questionnaires-1.0.2.tgz");
		spec = new PackageInstallationSpec().setName("nictiz.fhir.nl.stu3.questionnaires").setVersion("1.0.2").setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_ONLY).setPackageContents(bytes);
		igInstaller.install(spec);

		NpmPackageSearchResultJson search = myPackageCacheManager.search(new PackageSearchSpec());
		ourLog.info("Search rersults:\r{}", JsonUtil.serialize(search));
		assertEquals(2, search.getTotal());

		assertEquals(2, search.getObjects().size());
		assertEquals("hl7.fhir.uv.shorthand", search.getObjects().get(0).getPackage().getName());
		assertEquals("Describes FHIR Shorthand (FSH), a domain-specific language (DSL) for defining the content of FHIR Implementation Guides (IG). (built Wed, Apr 1, 2020 17:24+0000+00:00)", search.getObjects().get(0).getPackage().getDescription());
		assertEquals("0.12.0", search.getObjects().get(0).getPackage().getVersion());
		assertEquals(3115, search.getObjects().get(0).getPackage().getBytes());
		assertThat(search.getObjects().get(0).getPackage().getFhirVersion().toString(), search.getObjects().get(0).getPackage().getFhirVersion(), Matchers.contains("4.0.1"));

		assertEquals("nictiz.fhir.nl.stu3.questionnaires", search.getObjects().get(1).getPackage().getName());
		assertEquals("Nictiz NL package of FHIR STU3 conformance resources for MedMij information standard Questionnaires. Includes dependency on Zib2017 and SDC.\\n\\nHCIMs: https://zibs.nl/wiki/HCIM_Release_2017(EN)", search.getObjects().get(1).getPackage().getDescription());
		assertEquals("1.0.2", search.getObjects().get(1).getPackage().getVersion());
		assertThat(search.getObjects().get(1).getPackage().getFhirVersion().toString(), search.getObjects().get(0).getPackage().getFhirVersion(), Matchers.contains("4.0.1"));

	}


	@Test
	public void testSearchByResourceUrl() throws IOException {
		PackageInstallationSpec spec;
		byte[] bytes;

		bytes = ClasspathUtil.loadResourceAsByteArray("/packages/hl7.fhir.uv.shorthand-0.11.1.tgz");
		spec = new PackageInstallationSpec().setName("hl7.fhir.uv.shorthand").setVersion("0.11.1").setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_ONLY).setPackageContents(bytes);
		igInstaller.install(spec);

		bytes = ClasspathUtil.loadResourceAsByteArray("/packages/hl7.fhir.uv.shorthand-0.12.0.tgz");
		spec = new PackageInstallationSpec().setName("hl7.fhir.uv.shorthand").setVersion("0.12.0").setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_ONLY).setPackageContents(bytes);
		igInstaller.install(spec);

		PackageSearchSpec searchSpec;
		NpmPackageSearchResultJson search;

		// Matching URL
		myCaptureQueriesListener.clear();
		searchSpec = new PackageSearchSpec();
		searchSpec.setResourceUrl("http://hl7.org/fhir/uv/shorthand/ImplementationGuide/hl7.fhir.uv.shorthand");
		search = myPackageCacheManager.search(searchSpec);
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();

		ourLog.info("Search rersults:\r{}", JsonUtil.serialize(search));
		assertEquals(1, search.getTotal());
		assertEquals(1, search.getObjects().size());
		assertEquals("hl7.fhir.uv.shorthand", search.getObjects().get(0).getPackage().getName());
		assertEquals("0.12.0", search.getObjects().get(0).getPackage().getVersion());
		assertEquals("Describes FHIR Shorthand (FSH), a domain-specific language (DSL) for defining the content of FHIR Implementation Guides (IG). (built Wed, Apr 1, 2020 17:24+0000+00:00)", search.getObjects().get(0).getPackage().getDescription());
		assertThat(search.getObjects().get(0).getPackage().getFhirVersion(), Matchers.contains("4.0.1"));

		// Non Matching URL
		searchSpec = new PackageSearchSpec();
		searchSpec.setResourceUrl("http://foo");
		search = myPackageCacheManager.search(searchSpec);

		ourLog.info("Search rersults:\r{}", JsonUtil.serialize(search));
		assertEquals(0, search.getTotal());
		assertEquals(0, search.getObjects().size());

	}


	@Test
	public void testSearchByFhirVersion() throws IOException {
		PackageInstallationSpec spec;
		byte[] bytes;
		bytes = ClasspathUtil.loadResourceAsByteArray("/packages/hl7.fhir.uv.shorthand-0.12.0.tgz");
		spec = new PackageInstallationSpec().setName("hl7.fhir.uv.shorthand").setVersion("0.12.0").setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_ONLY).setPackageContents(bytes);
		igInstaller.install(spec);

		PackageSearchSpec searchSpec;
		NpmPackageSearchResultJson search;

		// Matching by name
		myCaptureQueriesListener.clear();
		searchSpec = new PackageSearchSpec();
		searchSpec.setFhirVersion("R4");
		search = myPackageCacheManager.search(searchSpec);
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();

		ourLog.info("Search rersults:\r{}", JsonUtil.serialize(search));
		assertEquals(1, search.getTotal());
		assertEquals("hl7.fhir.uv.shorthand", search.getObjects().get(0).getPackage().getName());
		assertEquals("4.0.1", search.getObjects().get(0).getPackage().getFhirVersion().get(0));

		// Matching FHIR version
		myCaptureQueriesListener.clear();
		searchSpec = new PackageSearchSpec();
		searchSpec.setFhirVersion("4.0.1");
		search = myPackageCacheManager.search(searchSpec);
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();

		ourLog.info("Search rersults:\r{}", JsonUtil.serialize(search));
		assertEquals(1, search.getTotal());
		assertEquals("hl7.fhir.uv.shorthand", search.getObjects().get(0).getPackage().getName());

		// Partial Matching FHIR version
		myCaptureQueriesListener.clear();
		searchSpec = new PackageSearchSpec();
		searchSpec.setFhirVersion("4.0");
		search = myPackageCacheManager.search(searchSpec);
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();

		ourLog.info("Search rersults:\r{}", JsonUtil.serialize(search));
		assertEquals(1, search.getTotal());
		assertEquals("hl7.fhir.uv.shorthand", search.getObjects().get(0).getPackage().getName());

		// Non Matching URL
		searchSpec = new PackageSearchSpec();
		searchSpec.setResourceUrl("http://foo");
		search = myPackageCacheManager.search(searchSpec);

		ourLog.info("Search rersults:\r{}", JsonUtil.serialize(search));
		assertEquals(0, search.getTotal());
		assertEquals(0, search.getObjects().size());

	}


	@Test
	public void testSearchByDescription() throws IOException {
		PackageInstallationSpec spec;
		byte[] bytes;

		bytes = ClasspathUtil.loadResourceAsByteArray("/packages/hl7.fhir.uv.shorthand-0.11.1.tgz");
		spec = new PackageInstallationSpec().setName("hl7.fhir.uv.shorthand").setVersion("0.11.1").setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_ONLY).setPackageContents(bytes);
		igInstaller.install(spec);

		bytes = ClasspathUtil.loadResourceAsByteArray("/packages/hl7.fhir.uv.shorthand-0.12.0.tgz");
		spec = new PackageInstallationSpec().setName("hl7.fhir.uv.shorthand").setVersion("0.12.0").setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_ONLY).setPackageContents(bytes);
		igInstaller.install(spec);

		PackageSearchSpec searchSpec;
		NpmPackageSearchResultJson search;

		// Matching URL
		myCaptureQueriesListener.clear();
		searchSpec = new PackageSearchSpec();
		searchSpec.setDescription("shorthand");
		search = myPackageCacheManager.search(searchSpec);
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();


		runInTransaction(() -> {
			ourLog.info("Versions:\n * {}", myPackageVersionDao.findAll().stream().map(t -> t.toString()).collect(Collectors.joining("\n * ")));
		});

		ourLog.info("Search rersults:\r{}", JsonUtil.serialize(search));
		assertEquals(1, search.getTotal());
		assertEquals(1, search.getObjects().size());
		assertEquals("hl7.fhir.uv.shorthand", search.getObjects().get(0).getPackage().getName());
		assertEquals("0.12.0", search.getObjects().get(0).getPackage().getVersion());
		assertEquals("Describes FHIR Shorthand (FSH), a domain-specific language (DSL) for defining the content of FHIR Implementation Guides (IG). (built Wed, Apr 1, 2020 17:24+0000+00:00)", search.getObjects().get(0).getPackage().getDescription());
		assertThat(search.getObjects().get(0).getPackage().getFhirVersion(), Matchers.contains("4.0.1"));

		// Non Matching URL
		searchSpec = new PackageSearchSpec();
		searchSpec.setResourceUrl("http://foo");
		search = myPackageCacheManager.search(searchSpec);

		ourLog.info("Search rersults:\r{}", JsonUtil.serialize(search));
		assertEquals(0, search.getTotal());
		assertEquals(0, search.getObjects().size());

	}

}
