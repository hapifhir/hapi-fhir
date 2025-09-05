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
