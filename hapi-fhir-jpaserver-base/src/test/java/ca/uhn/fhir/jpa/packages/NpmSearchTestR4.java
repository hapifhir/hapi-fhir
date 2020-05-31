package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.jpa.dao.data.INpmPackageDao;
import ca.uhn.fhir.jpa.dao.data.INpmPackageVersionDao;
import ca.uhn.fhir.jpa.dao.data.INpmPackageVersionResourceDao;
import ca.uhn.fhir.jpa.dao.r4.BaseJpaR4Test;
import ca.uhn.fhir.test.utilities.ProxyUtil;
import ca.uhn.fhir.util.JsonUtil;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class NpmSearchTestR4 extends BaseJpaR4Test {

	private static final Logger ourLog = LoggerFactory.getLogger(NpmSearchTestR4.class);
	@Autowired
	public INpmInstallerSvc igInstaller;
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

	@Before
	public void before() throws Exception {
		JpaPackageCache jpaPackageCache = ProxyUtil.getSingletonTarget(myPackageCacheManager, JpaPackageCache.class);
		jpaPackageCache.getPackageServers().clear();
	}

	@Test
	public void testSearch() throws IOException {
		NpmInstallationSpec spec;
		byte[] bytes;

		bytes = loadClasspathBytes("/packages/hl7.fhir.uv.shorthand-0.11.1.tgz");
		spec = new NpmInstallationSpec().setPackageId("hl7.fhir.uv.shorthand").setPackageVersion("0.11.1").setInstallMode(NpmInstallationSpec.InstallModeEnum.CACHE_ONLY).setContents(bytes);
		igInstaller.install(spec);

		bytes = loadClasspathBytes("/packages/hl7.fhir.uv.shorthand-0.12.0.tgz");
		spec = new NpmInstallationSpec().setPackageId("hl7.fhir.uv.shorthand").setPackageVersion("0.12.0").setInstallMode(NpmInstallationSpec.InstallModeEnum.CACHE_ONLY).setContents(bytes);
		igInstaller.install(spec);

		bytes = loadClasspathBytes("/packages/NHSD.Assets.STU3.tar.gz");
		spec = new NpmInstallationSpec().setPackageId("NHSD.Assets.STU3").setPackageVersion("1.2.0").setInstallMode(NpmInstallationSpec.InstallModeEnum.CACHE_ONLY).setContents(bytes);
		igInstaller.install(spec);

		NpmPackageSearchResultJson search = myPackageCacheManager.search(new PackageSearchSpec());
		ourLog.info("Search rersults:\r{}", JsonUtil.serialize(search));
		assertEquals(2, search.getTotal());

		assertEquals(2, search.getObjects().size());
		assertEquals("NHSD.Assets.STU3", search.getObjects().get(0).getPackage().getName());
		assertEquals("STU3 Assets from our Github account and Care Connect Profiles have been added from Github https://github.com/nhsconnect/CareConnect-profiles/tree/develop", search.getObjects().get(0).getPackage().getDescription());
		assertEquals("1.2.0", search.getObjects().get(0).getPackage().getVersion());
		assertThat(search.getObjects().get(0).getPackage().getFhirVersion(), Matchers.contains("3.0.2"));

		assertEquals("hl7.fhir.uv.shorthand", search.getObjects().get(1).getPackage().getName());
		assertEquals("0.12.0", search.getObjects().get(1).getPackage().getDescription());
		assertEquals("Describes FHIR Shorthand (FSH), a domain-specific language (DSL) for defining the content of FHIR Implementation Guides (IG). (built Wed, Apr 1, 2020 17:24+0000+00:00)", search.getObjects().get(1).getPackage().getVersion());
		assertThat(search.getObjects().get(1).getPackage().getFhirVersion(), Matchers.contains("4.0.1"));
	}


	@Test
	public void testSearchByResourceUrl() throws IOException {
		NpmInstallationSpec spec;
		byte[] bytes;

		bytes = loadClasspathBytes("/packages/hl7.fhir.uv.shorthand-0.11.1.tgz");
		spec = new NpmInstallationSpec().setPackageId("hl7.fhir.uv.shorthand").setPackageVersion("0.11.1").setInstallMode(NpmInstallationSpec.InstallModeEnum.CACHE_ONLY).setContents(bytes);
		igInstaller.install(spec);

		bytes = loadClasspathBytes("/packages/hl7.fhir.uv.shorthand-0.12.0.tgz");
		spec = new NpmInstallationSpec().setPackageId("hl7.fhir.uv.shorthand").setPackageVersion("0.12.0").setInstallMode(NpmInstallationSpec.InstallModeEnum.CACHE_ONLY).setContents(bytes);
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
	public void testSearchByDescription() throws IOException {
		NpmInstallationSpec spec;
		byte[] bytes;

		bytes = loadClasspathBytes("/packages/hl7.fhir.uv.shorthand-0.11.1.tgz");
		spec = new NpmInstallationSpec().setPackageId("hl7.fhir.uv.shorthand").setPackageVersion("0.11.1").setInstallMode(NpmInstallationSpec.InstallModeEnum.CACHE_ONLY).setContents(bytes);
		igInstaller.install(spec);

		bytes = loadClasspathBytes("/packages/hl7.fhir.uv.shorthand-0.12.0.tgz");
		spec = new NpmInstallationSpec().setPackageId("hl7.fhir.uv.shorthand").setPackageVersion("0.12.0").setInstallMode(NpmInstallationSpec.InstallModeEnum.CACHE_ONLY).setContents(bytes);
		igInstaller.install(spec);

		PackageSearchSpec searchSpec;
		NpmPackageSearchResultJson search;

		// Matching URL
		myCaptureQueriesListener.clear();
		searchSpec = new PackageSearchSpec();
		searchSpec.setDescription("shorthand");
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

}
