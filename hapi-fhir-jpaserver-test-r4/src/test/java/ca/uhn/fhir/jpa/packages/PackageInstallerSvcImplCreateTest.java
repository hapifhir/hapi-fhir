package ca.uhn.fhir.jpa.packages;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.jpa.dao.data.ITermValueSetDao;
import ca.uhn.fhir.jpa.entity.TermValueSet;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.NamingSystem;
import org.hl7.fhir.r4.model.ValueSet;
import org.hl7.fhir.utilities.npm.NpmPackage;
import org.hl7.fhir.utilities.npm.PackageGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.annotation.Nonnull;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PackageInstallerSvcImplCreateTest extends BaseJpaR4Test {
	private static final String PACKAGE_ID_1 = "package1";
	private static final String PACKAGE_VERSION = "1.0";
	private static final String VALUE_SET_OID_FIRST = "2.16.840.1.113762.1.4.1010.9";
	private static final String VALUE_SET_OID_SECOND = "2.16.840.1.113762.1.4.1010.10";
	private static final String IG_FIRST = "first.ig.gov";
	private static final String IG_SECOND = "second.ig.gov";
	private static final String FIRST_IG_URL_FIRST_OID = String.format("http://%s/fhir/ValueSet/%s", IG_FIRST, VALUE_SET_OID_FIRST);
	private static final String SECOND_IG_URL_FIRST_OID = String.format("http://%s/fhir/ValueSet/%s", IG_SECOND, VALUE_SET_OID_FIRST);
	private static final String SECOND_IG_URL_SECOND_OID = String.format("http://%s/fhir/ValueSet/%s", IG_SECOND, VALUE_SET_OID_SECOND);
	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	private static final CodeSystem CODE_SYSTEM = createCodeSystem();
	private static final NpmPackage PACKAGE = createPackage();
	private static final SystemRequestDetails REQUEST_DETAILS = new SystemRequestDetails();

	@Autowired
	private ITermValueSetDao myTermValueSetDao;

	@Autowired
	private PackageInstallerSvcImpl mySvc;
	@Test
	void createNamingSystem() throws IOException {
		final NamingSystem namingSystem = new NamingSystem();
		namingSystem.getUniqueId().add(new NamingSystem.NamingSystemUniqueIdComponent().setValue("123"));

		install(namingSystem);

		assertThat(myNamingSystemDao.search(SearchParameterMap.newSynchronous(), REQUEST_DETAILS).getAllResources()).hasSize(1);
	}

	@Test
	void createWithNoExistingResourcesNoIdOnValueSet() throws IOException {
		final String version1 = "abc";
		final String copyright1 = "first";

		createValueSetAndCallCreate(VALUE_SET_OID_FIRST, null, version1, FIRST_IG_URL_FIRST_OID, copyright1);

		final ValueSet actualValueSet1 = getFirstValueSet();

		assertEquals("ValueSet/" + VALUE_SET_OID_FIRST, actualValueSet1.getIdElement().toUnqualifiedVersionless().getValue());
		assertEquals(FIRST_IG_URL_FIRST_OID, actualValueSet1.getUrl());
		assertEquals(version1, actualValueSet1.getVersion());
		assertEquals(copyright1, actualValueSet1.getCopyright());
	}

	@Test
	void createWithNoExistingResourcesIdOnValueSet() throws IOException {
		final String version1 = "abc";
		final String copyright1 = "first";

		createValueSetAndCallCreate(VALUE_SET_OID_FIRST, null, version1, FIRST_IG_URL_FIRST_OID, copyright1);
		createValueSetAndCallCreate(VALUE_SET_OID_FIRST, "43", version1, SECOND_IG_URL_FIRST_OID, copyright1);

		final TermValueSet termValueSet = getFirstTermValueSet();

		assertEquals(FIRST_IG_URL_FIRST_OID, termValueSet.getUrl());

		final ValueSet actualValueSet1 = getFirstValueSet();

		assertEquals("ValueSet/" + VALUE_SET_OID_FIRST, actualValueSet1.getIdElement().toUnqualifiedVersionless().getValue());
		assertEquals(FIRST_IG_URL_FIRST_OID, actualValueSet1.getUrl());
		assertEquals(version1, actualValueSet1.getVersion());
		assertEquals(copyright1, actualValueSet1.getCopyright());
	}

	@Test
	void createValueSetThenUpdateSameUrl() throws IOException {
		final String version1 = "abc";
		final String version2 = "def";
		final String copyright1 = "first";
		final String copyright2 = "second";

		createValueSetAndCallCreate(VALUE_SET_OID_FIRST, null, version1, FIRST_IG_URL_FIRST_OID, copyright1);
		createValueSetAndCallCreate(VALUE_SET_OID_FIRST, "43", version2, FIRST_IG_URL_FIRST_OID, copyright2);

		final ValueSet actualValueSet1 = getFirstValueSet();

		assertEquals("ValueSet/" + VALUE_SET_OID_FIRST, actualValueSet1.getIdElement().toUnqualifiedVersionless().getValue());
		assertEquals(FIRST_IG_URL_FIRST_OID, actualValueSet1.getUrl());
		assertEquals(version2, actualValueSet1.getVersion());
		assertEquals(copyright2, actualValueSet1.getCopyright());
	}

	@Test
	void createTwoDifferentValueSets() throws IOException {
		final String version1 = "abc";
		final String version2 = "def";
		final String copyright1 = "first";
		final String copyright2 = "second";

		createValueSetAndCallCreate(VALUE_SET_OID_FIRST, null, version1, FIRST_IG_URL_FIRST_OID, copyright1);
		createValueSetAndCallCreate(VALUE_SET_OID_SECOND, "43", version2, SECOND_IG_URL_SECOND_OID, copyright2);

		final List<TermValueSet> all2 = myTermValueSetDao.findAll();

		assertThat(all2).hasSize(2);

		final TermValueSet termValueSet1 = all2.get(0);
		final TermValueSet termValueSet2 = all2.get(1);

		assertEquals(FIRST_IG_URL_FIRST_OID, termValueSet1.getUrl());
		assertEquals(SECOND_IG_URL_SECOND_OID, termValueSet2.getUrl());

		final List<ValueSet> allValueSets = getAllValueSets();

		assertThat(allValueSets).hasSize(2);

		final ValueSet actualValueSet1 = allValueSets.get(0);

		assertEquals("ValueSet/" + VALUE_SET_OID_FIRST, actualValueSet1.getIdElement().toUnqualifiedVersionless().getValue());
		assertEquals(FIRST_IG_URL_FIRST_OID, actualValueSet1.getUrl());
		assertEquals(version1, actualValueSet1.getVersion());
		assertEquals(copyright1, actualValueSet1.getCopyright());

		final ValueSet actualValueSet2 = allValueSets.get(1);

		assertEquals("ValueSet/" + VALUE_SET_OID_SECOND, actualValueSet2.getIdElement().toUnqualifiedVersionless().getValue());
		assertEquals(SECOND_IG_URL_SECOND_OID, actualValueSet2.getUrl());
		assertEquals(version2, actualValueSet2.getVersion());
		assertEquals(copyright2, actualValueSet2.getCopyright());
	}

	@Nonnull
	private List<ValueSet> getAllValueSets() {
		final List<IBaseResource> allResources = myValueSetDao.search(SearchParameterMap.newSynchronous(), REQUEST_DETAILS).getAllResources();

		assertThat(allResources).isNotEmpty();
		assertTrue(allResources.get(0) instanceof ValueSet);

		return allResources.stream()
			.map(ValueSet.class::cast)
			.toList();
	}

	@Nonnull
	private ValueSet getFirstValueSet() {
		final List<IBaseResource> allResources = myValueSetDao.search(SearchParameterMap.newSynchronous(), REQUEST_DETAILS).getAllResources();

		assertThat(allResources).hasSize(1);

		final IBaseResource resource1 = allResources.get(0);
		assertTrue(resource1 instanceof ValueSet);

		return (ValueSet) resource1;
	}

	@Nonnull
	private TermValueSet getFirstTermValueSet() {
		final List<TermValueSet> all2 = myTermValueSetDao.findAll();

		assertThat(all2).hasSize(1);

		return all2.get(0);
	}

	private void createValueSetAndCallCreate(String theOid, String theResourceVersion, String theValueSetVersion, String theUrl, String theCopyright) throws IOException {
		install(createValueSet(theOid, theResourceVersion, theValueSetVersion, theUrl, theCopyright));
	}

	@Nonnull
	private static ValueSet createValueSet(String theOid, String theResourceVersion, String theValueSetVersion, String theUrl, String theCopyright) {
		final ValueSet valueSetFromFirstIg = new ValueSet();

		valueSetFromFirstIg.setUrl(theUrl);
		valueSetFromFirstIg.setId(new IdDt(null, "ValueSet", theOid, theResourceVersion));
		valueSetFromFirstIg.setVersion(theValueSetVersion);
		valueSetFromFirstIg.setCopyright(theCopyright);

		return valueSetFromFirstIg;
	}

	private void install(IBaseResource theResource) throws IOException {
		mySvc.install(theResource, createInstallationSpec(packageToBytes()), new PackageInstallOutcomeJson());
	}

	@Nonnull
	private static CodeSystem createCodeSystem() {
		final CodeSystem cs = new CodeSystem();
		cs.setId("CodeSystem/mycs");
		cs.setUrl("http://my-code-system");
		cs.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		return cs;
	}

	@Nonnull
	private static NpmPackage createPackage() {
		PackageGenerator manifestGenerator = new PackageGenerator();
		manifestGenerator.name(PackageInstallerSvcImplCreateTest.PACKAGE_ID_1);
		manifestGenerator.version(PACKAGE_VERSION);
		manifestGenerator.description("a package");
		manifestGenerator.fhirVersions(List.of(FhirVersionEnum.R4.getFhirVersionString()));

		NpmPackage pkg = NpmPackage.empty(manifestGenerator);

		String csString = ourCtx.newJsonParser().encodeResourceToString(CODE_SYSTEM);
		pkg.addFile("package", "cs.json", csString.getBytes(StandardCharsets.UTF_8), "CodeSystem");

		return pkg;
	}

	@Nonnull
	private static PackageInstallationSpec createInstallationSpec(byte[] thePackageContents) {
		final PackageInstallationSpec spec = new PackageInstallationSpec();
		spec.setName(PACKAGE_ID_1);
		spec.setVersion(PACKAGE_VERSION);
		spec.setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_AND_INSTALL);
		spec.setPackageContents(thePackageContents);
		return spec;
	}

	@Nonnull
	private static byte[] packageToBytes() throws IOException {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		PackageInstallerSvcImplCreateTest.PACKAGE.save(stream);
		return stream.toByteArray();
	}
}
