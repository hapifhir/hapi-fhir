package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.test.utilities.ProxyUtil;
import ca.uhn.fhir.util.ClasspathUtil;
import ca.uhn.fhir.util.JsonUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class JpaPackageCacheSearchR4Test extends BaseJpaR4Test {

	private static final Logger ourLog = LoggerFactory.getLogger(JpaPackageCacheSearchR4Test.class);

	@Autowired
	public IPackageInstallerSvc myInstallerSvc;
	@Autowired
	private IHapiPackageCacheManager myPackageCacheManager;

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();
		JpaPackageCache jpaPackageCache = ProxyUtil.getSingletonTarget(myPackageCacheManager, JpaPackageCache.class);
		jpaPackageCache.getPackageServers().clear();

		byte[] bytes = ClasspathUtil.loadResourceAsByteArray("/packages/hl7.fhir.uv.shorthand-0.11.1.tgz");
		PackageInstallationSpec spec = new PackageInstallationSpec().setName("hl7.fhir.uv.shorthand").setVersion("0.11.1").setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_ONLY).setPackageContents(bytes);
		myInstallerSvc.install(spec);

		bytes = ClasspathUtil.loadResourceAsByteArray("/packages/hl7.fhir.uv.shorthand-0.12.0.tgz");
		spec = new PackageInstallationSpec().setName("hl7.fhir.uv.shorthand").setVersion("0.12.0").setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_ONLY).setPackageContents(bytes);
		myInstallerSvc.install(spec);

		bytes = ClasspathUtil.loadResourceAsByteArray("/packages/nictiz.fhir.nl.stu3.questionnaires-1.0.2.tgz");
		spec = new PackageInstallationSpec().setName("nictiz.fhir.nl.stu3.questionnaires").setVersion("1.0.2").setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_ONLY).setPackageContents(bytes);
		myInstallerSvc.install(spec);

		logAllPackageVersions();
	}

	@Test
	public void testSearch_NoParams() {
		NpmPackageSearchResultJson search = myPackageCacheManager.search(new PackageSearchSpec());
		logSearchResults(search);
		assertEquals(2, search.getTotal());

		assertThat(search.getObjects()).hasSize(2);
		assertEquals("hl7.fhir.uv.shorthand", search.getObjects().get(0).getPackage().getName());
		assertEquals("Describes FHIR Shorthand (FSH), a domain-specific language (DSL) for defining the content of FHIR Implementation Guides (IG). (built Wed, Apr 1, 2020 17:24+0000+00:00)", search.getObjects().get(0).getPackage().getDescription());
		assertEquals("HL7 International - FHIR Infrastructure Group", search.getObjects().get(0).getPackage().getAuthor());
		assertEquals("0.12.0", search.getObjects().get(0).getPackage().getVersion());
		assertEquals(3115, search.getObjects().get(0).getPackage().getBytes());
		assertThat(search.getObjects().get(0).getPackage().getFhirVersion()).as(search.getObjects().get(0).getPackage().getFhirVersion().toString()).containsExactly("4.0.1");

		assertEquals("nictiz.fhir.nl.stu3.questionnaires", search.getObjects().get(1).getPackage().getName());
		assertEquals("Nictiz NL package of FHIR STU3 conformance resources for MedMij information standard Questionnaires. Includes dependency on Zib2017 and SDC.\\n\\nHCIMs: https://zibs.nl/wiki/HCIM_Release_2017(EN)", search.getObjects().get(1).getPackage().getDescription());
		assertEquals("1.0.2", search.getObjects().get(1).getPackage().getVersion());
		assertThat(search.getObjects().get(0).getPackage().getFhirVersion()).as(search.getObjects().get(1).getPackage().getFhirVersion().toString()).containsExactly("4.0.1");

	}


	@Test
	public void testSearch_ResourceUrl() {

		PackageSearchSpec searchSpec;
		NpmPackageSearchResultJson search;

		// Matching URL
		myCaptureQueriesListener.clear();
		searchSpec = new PackageSearchSpec();
		searchSpec.setResourceUrl("http://hl7.org/fhir/uv/shorthand/ImplementationGuide/hl7.fhir.uv.shorthand");
		search = myPackageCacheManager.search(searchSpec);
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();

		logSearchResults(search);
		assertEquals(1, search.getTotal());
		assertThat(search.getObjects()).hasSize(1);
		assertEquals("hl7.fhir.uv.shorthand", search.getObjects().get(0).getPackage().getName());
		assertEquals("0.12.0", search.getObjects().get(0).getPackage().getVersion());
		assertEquals("Describes FHIR Shorthand (FSH), a domain-specific language (DSL) for defining the content of FHIR Implementation Guides (IG). (built Wed, Apr 1, 2020 17:24+0000+00:00)", search.getObjects().get(0).getPackage().getDescription());
		assertThat(search.getObjects().get(0).getPackage().getFhirVersion()).containsExactly("4.0.1");

		// Non Matching URL
		searchSpec = new PackageSearchSpec();
		searchSpec.setResourceUrl("http://foo");
		search = myPackageCacheManager.search(searchSpec);

		logSearchResults(search);
		assertEquals(0, search.getTotal());
		assertThat(search.getObjects()).isEmpty();

	}

	@Test
	public void testSearch_Version() {

		PackageSearchSpec searchSpec;
		NpmPackageSearchResultJson search;

		// Matching URL
		myCaptureQueriesListener.clear();
		searchSpec = new PackageSearchSpec();
		searchSpec.setVersion("0.12.0");
		search = myPackageCacheManager.search(searchSpec);
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();

		logSearchResults(search);
		assertEquals(1, search.getTotal());
		assertThat(search.getObjects()).hasSize(1);
		assertEquals("hl7.fhir.uv.shorthand", search.getObjects().get(0).getPackage().getName());
		assertEquals("0.12.0", search.getObjects().get(0).getPackage().getVersion());
		assertEquals("Describes FHIR Shorthand (FSH), a domain-specific language (DSL) for defining the content of FHIR Implementation Guides (IG). (built Wed, Apr 1, 2020 17:24+0000+00:00)", search.getObjects().get(0).getPackage().getDescription());
		assertThat(search.getObjects().get(0).getPackage().getFhirVersion()).containsExactly("4.0.1");

		// Non Matching URL
		searchSpec = new PackageSearchSpec();
		searchSpec.setResourceUrl("0.12.999");
		search = myPackageCacheManager.search(searchSpec);

		logSearchResults(search);
		assertEquals(0, search.getTotal());
		assertThat(search.getObjects()).isEmpty();

	}

	@Test
	public void testSearch_Author() {

		PackageSearchSpec searchSpec;
		NpmPackageSearchResultJson search;

		// Matching URL
		myCaptureQueriesListener.clear();
		searchSpec = new PackageSearchSpec();
		searchSpec.setAuthor("nterNATIonal"); // fragment with wrong case
		search = myPackageCacheManager.search(searchSpec);
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();

		logSearchResults(search);
		assertEquals(1, search.getTotal());
		assertThat(search.getObjects()).hasSize(1);
		assertEquals("hl7.fhir.uv.shorthand", search.getObjects().get(0).getPackage().getName());
		assertEquals("0.12.0", search.getObjects().get(0).getPackage().getVersion());
		assertEquals("Describes FHIR Shorthand (FSH), a domain-specific language (DSL) for defining the content of FHIR Implementation Guides (IG). (built Wed, Apr 1, 2020 17:24+0000+00:00)", search.getObjects().get(0).getPackage().getDescription());
		assertThat(search.getObjects().get(0).getPackage().getFhirVersion()).containsExactly("4.0.1");

		// Non Matching URL
		searchSpec = new PackageSearchSpec();
		searchSpec.setResourceUrl("http://foo");
		search = myPackageCacheManager.search(searchSpec);

		logSearchResults(search);
		assertEquals(0, search.getTotal());
		assertThat(search.getObjects()).isEmpty();

	}


	@Test
	public void testSearch_FhirVersion() {
		PackageSearchSpec searchSpec;
		NpmPackageSearchResultJson search;

		// Matching by name
		myCaptureQueriesListener.clear();
		searchSpec = new PackageSearchSpec();
		searchSpec.setFhirVersion("R4");
		search = myPackageCacheManager.search(searchSpec);
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();

		logSearchResults(search);
		assertEquals(1, search.getTotal());
		assertEquals("hl7.fhir.uv.shorthand", search.getObjects().get(0).getPackage().getName());
		assertEquals("4.0.1", search.getObjects().get(0).getPackage().getFhirVersion().get(0));

		// Matching FHIR version
		myCaptureQueriesListener.clear();
		searchSpec = new PackageSearchSpec();
		searchSpec.setFhirVersion("4.0.1");
		search = myPackageCacheManager.search(searchSpec);
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();

		logSearchResults(search);
		assertEquals(1, search.getTotal());
		assertEquals("hl7.fhir.uv.shorthand", search.getObjects().get(0).getPackage().getName());

		// Partial Matching FHIR version
		myCaptureQueriesListener.clear();
		searchSpec = new PackageSearchSpec();
		searchSpec.setFhirVersion("4.0");
		search = myPackageCacheManager.search(searchSpec);
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();

		logSearchResults(search);
		assertEquals(1, search.getTotal());
		assertEquals("hl7.fhir.uv.shorthand", search.getObjects().get(0).getPackage().getName());

		// Non Matching URL
		searchSpec = new PackageSearchSpec();
		searchSpec.setResourceUrl("http://foo");
		search = myPackageCacheManager.search(searchSpec);

		logSearchResults(search);
		assertEquals(0, search.getTotal());
		assertThat(search.getObjects()).isEmpty();

	}


	@Test
	public void testSearch_Description() {
		PackageSearchSpec searchSpec;
		NpmPackageSearchResultJson search;

		// Matching URL
		myCaptureQueriesListener.clear();
		searchSpec = new PackageSearchSpec();
		searchSpec.setDescription("shorthand");
		search = myPackageCacheManager.search(searchSpec);
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();

		logSearchResults(search);
		assertEquals(1, search.getTotal());
		assertThat(search.getObjects()).hasSize(1);
		assertEquals("hl7.fhir.uv.shorthand", search.getObjects().get(0).getPackage().getName());
		assertEquals("0.12.0", search.getObjects().get(0).getPackage().getVersion());
		assertEquals("Describes FHIR Shorthand (FSH), a domain-specific language (DSL) for defining the content of FHIR Implementation Guides (IG). (built Wed, Apr 1, 2020 17:24+0000+00:00)", search.getObjects().get(0).getPackage().getDescription());
		assertThat(search.getObjects().get(0).getPackage().getFhirVersion()).containsExactly("4.0.1");

		// Non Matching URL
		searchSpec = new PackageSearchSpec();
		searchSpec.setResourceUrl("http://foo");
		search = myPackageCacheManager.search(searchSpec);

		logSearchResults(search);
		assertEquals(0, search.getTotal());
		assertThat(search.getObjects()).isEmpty();

	}

	@Test
	public void testSearch_Combination() {
		PackageSearchSpec searchSpec;
		NpmPackageSearchResultJson search;

		myCaptureQueriesListener.clear();
		searchSpec = new PackageSearchSpec();
		searchSpec.setFhirVersion("R4");
		searchSpec.setAuthor("HL7 International");
		search = myPackageCacheManager.search(searchSpec);
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();

		logSearchResults(search);
		assertEquals(1, search.getTotal());
		assertEquals("hl7.fhir.uv.shorthand", search.getObjects().get(0).getPackage().getName());
		assertEquals("4.0.1", search.getObjects().get(0).getPackage().getFhirVersion().get(0));
	}


	@Test
	public void testUninstall() {
		// Arrange

		// Act
		PackageInstallationSpec spec = new PackageInstallationSpec().setName("hl7.fhir.uv.shorthand").setVersion("0.11.1");
		myInstallerSvc.uninstall(spec);
		spec = new PackageInstallationSpec().setName("hl7.fhir.uv.shorthand").setVersion("0.12.0");
		myInstallerSvc.uninstall(spec);

		// Assert
		NpmPackageSearchResultJson searchResult = myPackageCacheManager.search(new PackageSearchSpec());
		List<String> names = searchResult.getObjects().stream().map(t -> t.getPackage().getName()).toList();
		assertThat(names).containsExactly("nictiz.fhir.nl.stu3.questionnaires");
		assertEquals(1, searchResult.getTotal());

	}

	private static void logSearchResults(NpmPackageSearchResultJson search) {
		ourLog.info("Search results:\r{}", JsonUtil.serialize(search));
	}
}
