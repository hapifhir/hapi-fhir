package ca.uhn.fhir.jpa.packages;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.jpa.dao.data.ITermValueSetDao;
import ca.uhn.fhir.jpa.entity.TermValueSet;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.param.TokenParam;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.NamingSystem;
import org.hl7.fhir.r4.model.SearchParameter;
import org.hl7.fhir.r4.model.ValueSet;
import org.hl7.fhir.utilities.npm.NpmPackage;
import org.hl7.fhir.utilities.npm.PackageGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.annotation.Nonnull;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PackageInstallerSvcImplCreateTest extends BaseJpaR4Test {
	public static final String VERSION_1 = "abc";
	public static final String COPYRIGHT_1 = "first";
	public static final String VERSION_2 = "def";
	public static final String COPYRIGHT_2 = "second";
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
		createValueSetAndCallCreate(VALUE_SET_OID_FIRST, null, VERSION_1, FIRST_IG_URL_FIRST_OID, COPYRIGHT_1);

		final ValueSet actualValueSet1 = getFirstValueSet();

		assertThat(actualValueSet1.getIdElement().toUnqualifiedVersionless().getValue()).matches("ValueSet/[0-9]+");
		assertEquals(FIRST_IG_URL_FIRST_OID, actualValueSet1.getUrl());
		assertEquals(VERSION_1, actualValueSet1.getVersion());
		assertEquals(COPYRIGHT_1, actualValueSet1.getCopyright());
	}

	@Test
	void createWithNoExistingResourcesIdOnValueSet() throws IOException {
		createValueSetAndCallCreate(VALUE_SET_OID_FIRST, null, VERSION_1, FIRST_IG_URL_FIRST_OID, COPYRIGHT_1);
		createValueSetAndCallCreate(VALUE_SET_OID_FIRST, "43", VERSION_1, SECOND_IG_URL_FIRST_OID, COPYRIGHT_1);

		final List<TermValueSet> allTermValueSets = myTermValueSetDao.findAll();
		assertThat(allTermValueSets).hasSize(2);
		assertEquals(FIRST_IG_URL_FIRST_OID, allTermValueSets.get(0).getUrl());
		assertEquals(SECOND_IG_URL_FIRST_OID, allTermValueSets.get(1).getUrl());

		assertTwoDifferentValueSetsCreated(FIRST_IG_URL_FIRST_OID, SECOND_IG_URL_FIRST_OID, VERSION_1, VERSION_1, COPYRIGHT_1, COPYRIGHT_1);
	}

	@Test
	void createTwoValueSets_sameUrlDifferentVersions() throws IOException {
		createValueSetAndCallCreate(VALUE_SET_OID_FIRST, null, VERSION_1, FIRST_IG_URL_FIRST_OID, COPYRIGHT_1);
		createValueSetAndCallCreate(VALUE_SET_OID_FIRST, "43", VERSION_2, FIRST_IG_URL_FIRST_OID, COPYRIGHT_2);

		assertTwoDifferentValueSetsCreated(FIRST_IG_URL_FIRST_OID, FIRST_IG_URL_FIRST_OID, VERSION_1, VERSION_2, COPYRIGHT_1, COPYRIGHT_2);
	}

	@Test
	void createTwoValueSets_withDifferentUrls() throws IOException {
		createValueSetAndCallCreate(VALUE_SET_OID_FIRST, null, VERSION_1, FIRST_IG_URL_FIRST_OID, COPYRIGHT_1);
		createValueSetAndCallCreate(VALUE_SET_OID_SECOND, "43", VERSION_2, SECOND_IG_URL_SECOND_OID, COPYRIGHT_2);

		assertTwoDifferentValueSetsCreated(FIRST_IG_URL_FIRST_OID, SECOND_IG_URL_SECOND_OID, VERSION_1, VERSION_2, COPYRIGHT_1, COPYRIGHT_2);
	}

	private void assertTwoDifferentValueSetsCreated(String theExpectedValueSetUrlV1, String theExpectedValueSetUrlV2, String theV1, String theV2, String theCopyrightV1, String theCopyrightV2) {
		final List<TermValueSet> all2 = myTermValueSetDao.findAll();

		assertThat(all2).hasSize(2);

		final TermValueSet termValueSet1 = all2.get(0);
		final TermValueSet termValueSet2 = all2.get(1);

		assertEquals(theExpectedValueSetUrlV1, termValueSet1.getUrl());
		assertEquals(theExpectedValueSetUrlV2, termValueSet2.getUrl());

		final List<ValueSet> allValueSets = getAllValueSets();

		assertThat(allValueSets).hasSize(2);

		final ValueSet actualValueSet1 = allValueSets.get(0);
		String idValueSet1 = actualValueSet1.getIdElement().toUnqualifiedVersionless().getValue();

		assertThat(idValueSet1).matches("ValueSet/[0-9]+");
		assertEquals(theExpectedValueSetUrlV1, actualValueSet1.getUrl());
		assertEquals(theV1, actualValueSet1.getVersion());
		assertEquals(theCopyrightV1, actualValueSet1.getCopyright());

		final ValueSet actualValueSet2 = allValueSets.get(1);
		String idValueSet2 = actualValueSet2.getIdElement().toUnqualifiedVersionless().getValue();

		assertThat(idValueSet2).matches("ValueSet/[0-9]+");
		assertEquals(theExpectedValueSetUrlV2, actualValueSet2.getUrl());
		assertEquals(theV2, actualValueSet2.getVersion());
		assertEquals(theCopyrightV2, actualValueSet2.getCopyright());

		assertThat(idValueSet1).isNotEqualTo(idValueSet2);
	}

	@Test
	void installCompositeSearchParameterWithNoExpressionAtRoot() throws IOException {
		final String spCode = "my-test-composite-sp-with-no-expression";
		SearchParameter spR4 = new SearchParameter();
		spR4.setStatus(Enumerations.PublicationStatus.ACTIVE);
		spR4.setType(Enumerations.SearchParamType.COMPOSITE);
		spR4.setBase(List.of(new CodeType("Patient")));
		spR4.setCode(spCode);

		install(spR4);

		// verify the SP is created
		SearchParameterMap map = SearchParameterMap.newSynchronous()
			.add(SearchParameter.SP_CODE, new TokenParam(spCode));
		IBundleProvider outcome = mySearchParameterDao.search(map, mySrd);
		assertEquals(1, outcome.size());
	}

	@ParameterizedTest
	@EnumSource(PackageInstallationSpec.VersionPolicyEnum.class)
	void testVersionPolicyControlsIdAssignment(PackageInstallationSpec.VersionPolicyEnum theVersionPolicy) throws IOException {
		// Given: A ValueSet with a client-provided ID
		ValueSet valueSet = createValueSet("my-valueset-id", null, "1.0", "http://example.org/vs", "copyright");

		// When: Installing with the given version policy
		PackageInstallationSpec spec = createInstallationSpec(packageToBytes());
		spec.setVersionPolicy(theVersionPolicy);
		mySvc.install(valueSet, spec, new PackageInstallOutcomeJson());

		// Then: ID assignment follows the policy
		ValueSet actual = getFirstValueSet();
		if (theVersionPolicy == PackageInstallationSpec.VersionPolicyEnum.SINGLE_VERSION) {
			assertThat(actual.getIdElement().getIdPart()).isEqualTo("my-valueset-id");
		} else {
			assertThat(actual.getIdElement().getIdPart()).matches("[0-9]+");
		}

		// meta.source should be set regardless of version policy
		assertThat(actual.getMeta().getSource()).isEqualTo(PACKAGE_ID_1 + "|" + PACKAGE_VERSION);
	}

	@ParameterizedTest
	@EnumSource(PackageInstallationSpec.VersionPolicyEnum.class)
	void testVersionPolicyControlsUrlMatchingBehavior(PackageInstallationSpec.VersionPolicyEnum theVersionPolicy) throws IOException {
		String url = "http://example.org/vs";

		// Given: Install first version
		ValueSet vs1 = createValueSet("vs-id", null, "1.0", url, "copyright-v1");
		PackageInstallationSpec spec1 = createInstallationSpec(packageToBytes());
		spec1.setVersionPolicy(theVersionPolicy);
		mySvc.install(vs1, spec1, new PackageInstallOutcomeJson());

		// When: Install second version with same URL but different version
		ValueSet vs2 = createValueSet("vs-id", null, "2.0", url, "copyright-v2");
		PackageInstallationSpec spec2 = createInstallationSpec(packageToBytes());
		spec2.setVersionPolicy(theVersionPolicy);
		mySvc.install(vs2, spec2, new PackageInstallOutcomeJson());

		// Then: Resource count depends on policy
		List<ValueSet> allValueSets = getAllValueSets();
		if (theVersionPolicy == PackageInstallationSpec.VersionPolicyEnum.SINGLE_VERSION) {
			// SINGLE_VERSION: v2 overwrites v1
			assertThat(allValueSets).hasSize(1);
			assertThat(allValueSets.get(0).getVersion()).isEqualTo("2.0");
		} else {
			// MULTI_VERSION: both versions coexist
			assertThat(allValueSets).hasSize(2);
			assertThat(allValueSets).extracting(ValueSet::getVersion).containsExactlyInAnyOrder("1.0", "2.0");
		}

		// meta.source should be set regardless of version policy
		for (ValueSet vs : allValueSets) {
			assertThat(vs.getMeta().getSource()).isEqualTo(PACKAGE_ID_1 + "|" + PACKAGE_VERSION);
		}
	}

	@ParameterizedTest
	@EnumSource(PackageInstallationSpec.VersionPolicyEnum.class)
	void testSearchParameterUsesClientId_regardlessOfPolicy(
			PackageInstallationSpec.VersionPolicyEnum theVersionPolicy) throws IOException {
		// Given: A SearchParameter with client-provided ID
		SearchParameter sp = new SearchParameter();
		sp.setId("my-sp-id");
		sp.setUrl("http://example.org/sp");
		sp.setCode("mycode");
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.setType(Enumerations.SearchParamType.STRING);
		sp.getBase().add(new CodeType("Patient"));
		sp.setExpression("Patient.name");

		// When: Installing with the given version policy
		PackageInstallationSpec spec = createInstallationSpec(packageToBytes());
		spec.setVersionPolicy(theVersionPolicy);
		mySvc.install(sp, spec, new PackageInstallOutcomeJson());

		// Then: SearchParameter keeps client ID regardless of policy
		IBundleProvider results = mySearchParameterDao.search(SearchParameterMap.newSynchronous(), REQUEST_DETAILS);
		assertThat(results.getAllResources()).hasSize(1);
		SearchParameter actual = (SearchParameter) results.getAllResources().get(0);
		assertThat(actual.getIdElement().getIdPart()).isEqualTo("my-sp-id");

		// meta.source should be set regardless of version policy
		assertThat(actual.getMeta().getSource()).isEqualTo(PACKAGE_ID_1 + "|" + PACKAGE_VERSION);
	}

	@Test
	void testSingleVersionMode_withMultipleExistingVersions_updatesMostRecentlyCreated() throws IOException {
		String url = "http://example.org/ValueSet/test";

		// Given: Two versions already exist using MULTI_VERSION policy
		// Install v1.0 first (smaller RES_ID)
		ValueSet vs1 = createValueSet("vs-v1", null, "1.0", url, "copyright-v1");
		PackageInstallationSpec spec1 = createInstallationSpec(packageToBytes());
		spec1.setVersionPolicy(PackageInstallationSpec.VersionPolicyEnum.MULTI_VERSION);
		mySvc.install(vs1, spec1, new PackageInstallOutcomeJson());

		// Install v2.0 second (bigger RES_ID)
		ValueSet vs2 = createValueSet("vs-v2", null, "2.0", url, "copyright-v2");
		PackageInstallationSpec spec2 = createInstallationSpec(packageToBytes());
		spec2.setVersionPolicy(PackageInstallationSpec.VersionPolicyEnum.MULTI_VERSION);
		mySvc.install(vs2, spec2, new PackageInstallOutcomeJson());

		// Capture the ID of the second (newer) resource
		List<ValueSet> existing = getAllValueSets();
		assertThat(existing).hasSize(2);
		String newerResourceId = existing.stream()
			.filter(vs -> "2.0".equals(vs.getVersion()))
			.findFirst()
			.map(vs -> vs.getIdElement().toUnqualifiedVersionless().getValue())
			.orElseThrow();

		// When: Install v3.0 with SINGLE_VERSION policy (simulating user switched modes)
		ValueSet vs3 = createValueSet("vs-v3", null, "3.0", url, "copyright-v3");
		PackageInstallationSpec spec3 = createInstallationSpec(packageToBytes());
		spec3.setVersionPolicy(PackageInstallationSpec.VersionPolicyEnum.SINGLE_VERSION);
		mySvc.install(vs3, spec3, new PackageInstallOutcomeJson());

		// Then: The most recently created resource (v2.0's ID) should be updated to v3.0
		ValueSet updated = myValueSetDao.read(new IdDt(newerResourceId), REQUEST_DETAILS);
		assertThat(updated.getVersion()).isEqualTo("3.0");

		// And: v1 should still exist unchanged, v2 overwritten by v3
		List<ValueSet> all = getAllValueSets();
		assertThat(all).hasSize(2);
		assertThat(all).extracting(ValueSet::getVersion)
			.containsExactlyInAnyOrder("1.0", "3.0");
	}

	@Nonnull
	private List<ValueSet> getAllValueSets() {
		final List<IBaseResource> allResources = myValueSetDao.search(SearchParameterMap.newSynchronous(), REQUEST_DETAILS).getAllResources();

		assertThat(allResources).isNotEmpty();
		assertInstanceOf(ValueSet.class, allResources.get(0));

		return allResources.stream()
			.map(ValueSet.class::cast)
			.toList();
	}

	@Nonnull
	private ValueSet getFirstValueSet() {
		final List<IBaseResource> allResources = myValueSetDao.search(SearchParameterMap.newSynchronous(), REQUEST_DETAILS).getAllResources();

		assertThat(allResources).hasSize(1);

		final IBaseResource resource1 = allResources.get(0);
		assertInstanceOf(ValueSet.class, resource1);

		return (ValueSet) resource1;
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
