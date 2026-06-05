package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.implementationguide.ImplementationGuideCreator;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.data.ITermValueSetDao;
import ca.uhn.fhir.jpa.entity.TermValueSet;
import ca.uhn.fhir.jpa.model.entity.NpmPackageVersionEntity;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Device;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.NamingSystem;
import org.hl7.fhir.r4.model.SearchParameter;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.ValueSet;
import org.hl7.fhir.utilities.npm.NpmPackage;
import org.hl7.fhir.utilities.npm.PackageGenerator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PackageInstallerSvcCreateR4Test extends BaseJpaR4Test {
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

	@AfterEach
	void resetStorageSettings() {
		myStorageSettings.setValidateResourceStatusForPackageUpload(new JpaStorageSettings().isValidateResourceStatusForPackageUpload());
		myPartitionSettings.setPartitioningEnabled(false);
	}

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

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	public void install_installOnly_persistsResourcesButNotThePkg(boolean theEmbedContent, @TempDir Path theTempDir) throws IOException {
		// setup
		String spUrl = "http://example.com";
		IFhirResourceDao<SearchParameter> dao = myDaoRegistry.getResourceDao(SearchParameter.class);

		ImplementationGuideCreator creator = new ImplementationGuideCreator(myFhirContext);
		creator.setDirectory(theTempDir);

		SearchParameter sp = new SearchParameter();
		sp.setUrl(spUrl);
		sp.setName("My Param");
		sp.setCode("my-param");
		sp.setDescription("My custom search parameter on Patient");
		sp.addBase("Patient");
		sp.setType(Enumerations.SearchParamType.TOKEN);
		sp.setExpression("Patient.identifier");
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);

		creator.addResourceToIG("SearchParameter", sp);

		// ensure it doesn't already exist
		SearchParameterMap spmap = new SearchParameterMap();
		spmap.setLoadSynchronous(true);
		spmap.add(SearchParameter.SP_URL, new UriParam(spUrl));
		IBundleProvider provider = dao.search(spmap, new SystemRequestDetails());
		assertTrue(provider.isEmpty());

		// create the spec
		PackageInstallationSpec installedSpec = new PackageInstallationSpec();
		installedSpec
			.setName(creator.getPackageName())
			.setVersion(creator.getPackageVersion())
			.setInstallMode(PackageInstallationSpec.InstallModeEnum.INSTALL_ONLY)
			.setVersionPolicy(PackageInstallationSpec.VersionPolicyEnum.SINGLE_VERSION)
		;

		if (theEmbedContent) {
			installedSpec.setPackageContents(Files.readAllBytes(creator.createTestIG()));
		} else {
			installedSpec.setPackageUrl("file://" + creator.createTestIG().toString());
		}

		// test
		PackageInstallOutcomeJson outcome = mySvc.install(installedSpec);

		// verify
		assertNotNull(outcome);
		assertTrue(outcome.getMessage().stream()
			.anyMatch(m -> m.contains("Resources have been successfully installed")));

		// the questionnaire should now exist
		provider = dao.search(spmap, new SystemRequestDetails());
		assertFalse(provider.isEmpty());
		assertEquals(1, provider.size());
		SearchParameter retval = (SearchParameter) provider.getResourceListComplete().get(0);
		assertEquals(spUrl, retval.getUrl());
		assertEquals(sp.getName(), retval.getName());

		// no packages stored
		runInTransaction(() -> {
			Optional<NpmPackageVersionEntity> npmVersionEntity = myPackageVersionDao.findByPackageIdAndVersion(creator.getPackageName(), creator.getPackageVersion());
			assertFalse(npmVersionEntity.isPresent());
		});
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

	// Created by claude-opus-4-6
	@Test
	void install_withNoUrlAndIdentifier_matchesByIdentifier() throws IOException {
		String identifierSystem = "http://example.com/devices";
		String identifierValue = "device-abc";

		// Create the resource directly via DAO
		Device existing = new Device();
		existing.setStatus(Device.FHIRDeviceStatus.ACTIVE);
		existing.addIdentifier().setSystem(identifierSystem).setValue(identifierValue);
		existing.addDeviceName().setName("Test Device").setType(Device.DeviceNameType.USERFRIENDLYNAME);
		myDeviceDao.create(existing, REQUEST_DETAILS);

		SearchParameterMap identifierSearch = SearchParameterMap.newSynchronous()
			.add(Device.SP_IDENTIFIER, new TokenParam(identifierSystem, identifierValue));
		IBundleProvider firstResult = myDeviceDao.search(identifierSearch, REQUEST_DETAILS);
		assertThat(firstResult.sizeOrThrowNpe()).isEqualTo(1);
		String firstId = firstResult.getResources(0, 1).get(0).getIdElement().toUnqualifiedVersionless().getValue();

		// Install via package installer — should match the existing resource by identifier
		Device updated = new Device();
		updated.setStatus(Device.FHIRDeviceStatus.ACTIVE);
		updated.addIdentifier().setSystem(identifierSystem).setValue(identifierValue);
		updated.addDeviceName().setName("Updated Device").setType(Device.DeviceNameType.USERFRIENDLYNAME);

		mySvc.install(updated, createInstallationSpec(packageToBytes()), new PackageInstallOutcomeJson());

		IBundleProvider secondResult = myDeviceDao.search(identifierSearch, REQUEST_DETAILS);
		assertThat(secondResult.sizeOrThrowNpe()).as("Should not create a duplicate").isEqualTo(1);
		Device installedDevice = (Device) secondResult.getResources(0, 1).get(0);
		assertThat(installedDevice.getIdElement().toUnqualifiedVersionless().getValue())
			.as("Should update the same resource").isEqualTo(firstId);
		assertThat(installedDevice.getDeviceName().get(0).getName())
			.as("Should have updated content").isEqualTo("Updated Device");
	}

	// Created by claude-sonnet-4-6
	// Regression for HAPI-0521: resources with IDs > 64 chars must be skipped during install
	// (even when status validation is disabled) to prevent HAPI-0521 on DB insert.
	// Boundary: exactly 64-char IDs are valid and must be installed normally.
	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	void install_resourceWithIdOver64Chars_isSkippedDoesNotThrowHapi0521(boolean theStatusValidationEnabled) throws IOException {
		myStorageSettings.setValidateResourceStatusForPackageUpload(theStatusValidationEnabled);

		ValueSet vs = new ValueSet();
		vs.setId("ValueSet/" + "a".repeat(65));
		vs.setUrl("http://example.org/vs-long-id");
		vs.setStatus(Enumerations.PublicationStatus.ACTIVE);

		install(vs);

		IBundleProvider results = myValueSetDao.search(SearchParameterMap.newSynchronous(), REQUEST_DETAILS);
		assertThat(results.getAllResources()).isEmpty();
	}

	// Created by claude-sonnet-4-6
	@Test
	void install_resourceWithIdExactly64Chars_isInstalled() throws IOException {
		// Boundary: exactly 64-char ID is valid per FHIR spec and must not be rejected.
		ValueSet vs = new ValueSet();
		vs.setId("ValueSet/" + "a".repeat(64));
		vs.setUrl("http://example.org/vs-exact-id");
		vs.setVersion("1.0");
		vs.setStatus(Enumerations.PublicationStatus.ACTIVE);

		install(vs);

		// Verify the resource was created (server may assign a different ID depending on version policy)
		IBundleProvider results = myValueSetDao.search(
			SearchParameterMap.newSynchronous().add(ValueSet.SP_URL, new UriParam("http://example.org/vs-exact-id")),
			REQUEST_DETAILS);
		assertThat(results.getAllResources()).hasSize(1);
	}

	// Created by claude-sonnet-4-6
	@Test
	void install_deviceWithBlankUrl_matchesByIdentifier() throws IOException {
		// A resource whose url element is present but blank must use identifier-based matching,
		// not URL-based matching. Previously resourceHasUrlElement only checked element existence,
		// so a blank URL would have been used in a search, potentially matching wrong resources.
		String identifierSystem = "urn:sys";
		String identifierValue = "device-blank-url";

		Device existing = new Device();
		existing.setStatus(Device.FHIRDeviceStatus.ACTIVE);
		existing.setUrl("");
		existing.addIdentifier().setSystem(identifierSystem).setValue(identifierValue);
		existing.addDeviceName().setName("Original Device").setType(Device.DeviceNameType.USERFRIENDLYNAME);
		myDeviceDao.create(existing, REQUEST_DETAILS);

		Device updated = new Device();
		updated.setStatus(Device.FHIRDeviceStatus.ACTIVE);
		updated.setUrl("");
		updated.addIdentifier().setSystem(identifierSystem).setValue(identifierValue);
		updated.addDeviceName().setName("Updated Device").setType(Device.DeviceNameType.USERFRIENDLYNAME);

		install(updated);

		SearchParameterMap identifierSearch = SearchParameterMap.newSynchronous()
			.add(Device.SP_IDENTIFIER, new TokenParam(identifierSystem, identifierValue));
		IBundleProvider results = myDeviceDao.search(identifierSearch, REQUEST_DETAILS);
		assertThat(results.sizeOrThrowNpe()).as("Should update existing, not create duplicate").isEqualTo(1);
		Device installedDevice = (Device) results.getResources(0, 1).get(0);
		assertThat(installedDevice.getDeviceName().get(0).getName()).isEqualTo("Updated Device");
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

	// Generated by claude-sonnet-4-6
	@Test
	void install_installOnly_withNameAndVersionOnly_installsResourcesFromCache(@TempDir Path theTempDir) throws IOException {
		// When INSTALL_ONLY is used with only name+version (no packageUrl, no packageContents),
		// the package should be resolved from the local cache and its resources installed into the FHIR repository.
		// Reproduces PUT /write/install/by-param with installMode=INSTALL_ONLY.

		// setup: build a test IG with a SearchParameter
		ImplementationGuideCreator creator = new ImplementationGuideCreator(myFhirContext);
		creator.setDirectory(theTempDir);

		String spUrl = "http://example.com/sp-install-only-by-name";
		SearchParameter sp = new SearchParameter();
		sp.setUrl(spUrl);
		sp.setName("My Install Only Param");
		sp.setCode("my-install-only-param");
		sp.setDescription("Test SP for INSTALL_ONLY by-name bug");
		sp.addBase("Patient");
		sp.setType(Enumerations.SearchParamType.TOKEN);
		sp.setExpression("Patient.identifier");
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		creator.addResourceToIG("SearchParameter", sp);

		// pre-store the package in the cache via STORE_ONLY so it can be resolved by name/version
		Path igPath = creator.createTestIG();
		PackageInstallationSpec storeSpec = new PackageInstallationSpec()
			.setName(creator.getPackageName())
			.setVersion(creator.getPackageVersion())
			.setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_ONLY)
			.setPackageUrl("file://" + igPath);
		mySvc.install(storeSpec);

		// confirm the SP is not yet installed
		SystemRequestDetails requestDetails = new SystemRequestDetails();
		IFhirResourceDao<SearchParameter> dao = myDaoRegistry.getResourceDao(SearchParameter.class);
		SearchParameterMap spmap = SearchParameterMap.newSynchronous();
		spmap.add(SearchParameter.SP_URL, new UriParam(spUrl));
		assertTrue(dao.search(spmap, requestDetails).isEmpty());

		// exercise: INSTALL_ONLY with name+version only — no packageUrl, no packageContents
		// this is the exact scenario from PUT /write/install/by-param?installMode=INSTALL_ONLY
		PackageInstallationSpec installOnlySpec = new PackageInstallationSpec()
			.setName(creator.getPackageName())
			.setVersion(creator.getPackageVersion())
			.setInstallMode(PackageInstallationSpec.InstallModeEnum.INSTALL_ONLY);
		// intentionally no setPackageUrl() and no setPackageContents()

		PackageInstallOutcomeJson outcome = mySvc.install(installOnlySpec);

		// verify: resources were installed
		assertNotNull(outcome);
		IBundleProvider results = dao.search(spmap, requestDetails);
		assertFalse(results.isEmpty());
		assertEquals(1, results.size());
	}

	// Generated by claude-sonnet-4-6
	@ParameterizedTest
	@EnumSource(PackageInstallationSpec.InstallModeEnum.class)
	void install_withNameAndVersionOnly_packageNotInCache_throwsMeaningfulException(
		PackageInstallationSpec.InstallModeEnum theInstallMode) {
		// the caller should receive a clear "Could not load" error, not a NullPointerException

		PackageInstallationSpec installOnlySpec = new PackageInstallationSpec()
			.setName("non.existent.package")
			.setVersion("9.9.9")
			.setInstallMode(theInstallMode);

		assertThatThrownBy(() -> mySvc.install(installOnlySpec))
			.isInstanceOf(ResourceNotFoundException.class)
			.hasMessageContaining("Unable to locate package non.existent.package#9.9.9");
	}

	// Created by Claude Opus 4.6
	@Test
	void installPackage_structureDefinitionWithoutSnapshot_generatesAndPersistsSnapshot() throws IOException {
		StructureDefinition sd = new StructureDefinition();
		sd.setUrl("http://example.com/sd-no-snapshot");
		sd.setName("TestProfileNoSnapshot");
		sd.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sd.setKind(StructureDefinition.StructureDefinitionKind.RESOURCE);
		sd.setAbstract(false);
		sd.setType("Patient");
		sd.setBaseDefinition("http://hl7.org/fhir/StructureDefinition/Patient");
		sd.setDerivation(StructureDefinition.TypeDerivationRule.CONSTRAINT);

		PackageInstallOutcomeJson outcome = installViaPackage(sd);

		assertThat(outcome.getResourcesInstalled()).containsEntry("StructureDefinition", 1);

		IBundleProvider results = myStructureDefinitionDao.search(
			SearchParameterMap.newSynchronous().add("url", new UriParam("http://example.com/sd-no-snapshot")),
			REQUEST_DETAILS);
		assertThat(results.getAllResources()).hasSize(1);
		StructureDefinition persisted = (StructureDefinition) results.getAllResources().get(0);
		assertThat(persisted.hasSnapshot()).as("Snapshot should have been generated").isTrue();
		assertThat(persisted.getSnapshot().getElement()).isNotEmpty();
	}

	// Created by Claude Opus 4.6
	@Test
	void installPackage_structureDefinitionWithSnapshot_preservesExistingSnapshot() throws IOException {
		StructureDefinition sd = new StructureDefinition();
		sd.setUrl("http://example.com/sd-with-snapshot");
		sd.setName("TestProfileWithSnapshot");
		sd.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sd.setKind(StructureDefinition.StructureDefinitionKind.RESOURCE);
		sd.setAbstract(false);
		sd.setType("Patient");
		sd.setBaseDefinition("http://hl7.org/fhir/StructureDefinition/Patient");
		sd.setDerivation(StructureDefinition.TypeDerivationRule.CONSTRAINT);
		StructureDefinition.StructureDefinitionSnapshotComponent snapshot =
			new StructureDefinition.StructureDefinitionSnapshotComponent();
		snapshot.addElement().setPath("Patient").setMin(0).setMax("*");
		sd.setSnapshot(snapshot);

		PackageInstallOutcomeJson outcome = installViaPackage(sd);

		assertThat(outcome.getResourcesInstalled()).containsEntry("StructureDefinition", 1);

		IBundleProvider results = myStructureDefinitionDao.search(
			SearchParameterMap.newSynchronous().add("url", new UriParam("http://example.com/sd-with-snapshot")),
			REQUEST_DETAILS);
		assertThat(results.getAllResources()).hasSize(1);
		StructureDefinition persisted = (StructureDefinition) results.getAllResources().get(0);
		assertThat(persisted.hasSnapshot()).isTrue();
		assertThat(persisted.getSnapshot().getElement()).hasSize(1);
		assertThat(persisted.getSnapshot().getElement().get(0).getPath()).isEqualTo("Patient");
	}

	// Created by Claude Opus 4.6
	@Test
	void installPackage_logicalStructureDefinition_doesNotGenerateSnapshot() throws IOException {
		StructureDefinition sd = new StructureDefinition();
		sd.setUrl("http://example.com/sd-logical");
		sd.setName("TestLogicalModel");
		sd.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sd.setKind(StructureDefinition.StructureDefinitionKind.LOGICAL);
		sd.setAbstract(false);
		sd.setType("http://example.com/sd-logical");
		sd.setBaseDefinition("http://hl7.org/fhir/StructureDefinition/Element");
		sd.setDerivation(StructureDefinition.TypeDerivationRule.SPECIALIZATION);

		PackageInstallOutcomeJson outcome = installViaPackage(sd);

		assertThat(outcome.getResourcesInstalled()).containsEntry("StructureDefinition", 1);

		IBundleProvider results = myStructureDefinitionDao.search(
			SearchParameterMap.newSynchronous().add("url", new UriParam("http://example.com/sd-logical")),
			REQUEST_DETAILS);
		assertThat(results.getAllResources()).hasSize(1);
		StructureDefinition persisted = (StructureDefinition) results.getAllResources().get(0);
		assertThat(persisted.hasSnapshot()).as("Logical model should not get a generated snapshot").isFalse();
	}

	// Created by Claude Opus 4.6
	@Test
	void installPackage_mixedResources_countsOnlyInstalledResources() throws IOException {
		// Seed an existing CodeSystem with content=not-present in the database
		CodeSystem existingCs = new CodeSystem();
		existingCs.setUrl("http://example.com/cs-not-present");
		existingCs.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
		existingCs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		myCodeSystemDao.create(existingCs, REQUEST_DETAILS);

		// Build a new (installable) CodeSystem
		CodeSystem newCs = new CodeSystem();
		newCs.setUrl("http://example.com/cs-new");
		newCs.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		newCs.setStatus(Enumerations.PublicationStatus.ACTIVE);

		// Build a CodeSystem that matches the not-present one — should be skipped
		CodeSystem skippedCs = new CodeSystem();
		skippedCs.setUrl("http://example.com/cs-not-present");
		skippedCs.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		skippedCs.setStatus(Enumerations.PublicationStatus.ACTIVE);

		// Build a package with both CodeSystems
		NpmPackage pkg = createPackageWithResources(
			new ResourceEntry("CodeSystem-new.json", newCs, "CodeSystem"),
			new ResourceEntry("CodeSystem-skipped.json", skippedCs, "CodeSystem"));

		PackageInstallationSpec spec = createInstallationSpec(packageToBytes(pkg));
		PackageInstallOutcomeJson outcome = mySvc.install(spec);

		assertThat(outcome.getResourcesInstalled()).containsEntry("CodeSystem", 1);
	}

	// Created by Claude Opus 4.6
	@Test
	void install_dryRun_newResource_reportsAddedResource() throws IOException {
		CodeSystem cs = new CodeSystem();
		cs.setUrl("http://example.com/cs-dryrun-new");
		cs.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		cs.setStatus(Enumerations.PublicationStatus.ACTIVE);

		PackageInstallationSpec spec = createInstallationSpec(packageToBytes());
		spec.setDryRun(true);
		PackageInstallOutcomeJson outcome = new PackageInstallOutcomeJson();

		mySvc.install(cs, spec, outcome);

		assertThat(outcome.getResourcesInstalled()).isEmpty();
		assertThat(outcome.getAddedResourceTypeToUniqueIdentifier()).containsKey("CodeSystem");
		assertThat(outcome.getAddedResourceTypeToUniqueIdentifier().get("CodeSystem"))
			.containsExactly("http://example.com/cs-dryrun-new");

		// Verify nothing was actually persisted
		IBundleProvider results = myCodeSystemDao.search(
			SearchParameterMap.newSynchronous().add("url", new UriParam("http://example.com/cs-dryrun-new")),
			REQUEST_DETAILS);
		assertThat(results.getAllResources()).isEmpty();
	}

	// Created by Claude Opus 4.6
	@Test
	void install_dryRun_existingResource_reportsReplacedResource() throws IOException {
		// Seed an existing CodeSystem
		CodeSystem existing = new CodeSystem();
		existing.setUrl("http://example.com/cs-dryrun-existing");
		existing.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		existing.setStatus(Enumerations.PublicationStatus.ACTIVE);
		myCodeSystemDao.create(existing, REQUEST_DETAILS);

		// Install a newer version via dry-run
		CodeSystem updated = new CodeSystem();
		updated.setUrl("http://example.com/cs-dryrun-existing");
		updated.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		updated.setStatus(Enumerations.PublicationStatus.ACTIVE);

		PackageInstallationSpec spec = createInstallationSpec(packageToBytes());
		spec.setDryRun(true);
		spec.setReloadExisting(true);
		PackageInstallOutcomeJson outcome = new PackageInstallOutcomeJson();

		mySvc.install(updated, spec, outcome);

		assertThat(outcome.getResourcesInstalled()).isEmpty();
		assertThat(outcome.getReplacedResourceToUniqueIdentifier()).containsKey("CodeSystem");
		assertThat(outcome.getReplacedResourceToUniqueIdentifier().get("CodeSystem"))
			.containsExactly("http://example.com/cs-dryrun-existing");
	}

	// Created by Claude Opus 4.6
	@Test
	void install_existingResourceWithReloadExistingDisabled_skipsUpdate() throws IOException {
		// Seed an existing CodeSystem
		CodeSystem existing = new CodeSystem();
		existing.setUrl("http://example.com/cs-reload-disabled");
		existing.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		existing.setStatus(Enumerations.PublicationStatus.ACTIVE);
		myCodeSystemDao.create(existing, REQUEST_DETAILS);

		// Install the same URL with reloadExisting=false
		CodeSystem updated = new CodeSystem();
		updated.setUrl("http://example.com/cs-reload-disabled");
		updated.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		updated.setStatus(Enumerations.PublicationStatus.ACTIVE);
		updated.setCopyright("should not appear");

		PackageInstallationSpec spec = createInstallationSpec(packageToBytes());
		spec.setReloadExisting(false);
		PackageInstallOutcomeJson outcome = new PackageInstallOutcomeJson();

		mySvc.install(updated, spec, outcome);

		assertThat(outcome.getResourcesInstalled()).isEmpty();

		// Verify original resource is unchanged
		IBundleProvider results = myCodeSystemDao.search(
			SearchParameterMap.newSynchronous().add("url", new UriParam("http://example.com/cs-reload-disabled")),
			REQUEST_DETAILS);
		assertThat(results.getAllResources()).hasSize(1);
		CodeSystem persisted = (CodeSystem) results.getAllResources().get(0);
		assertThat(persisted.getCopyright()).isNullOrEmpty();
	}

	// Created by Claude Opus 4.6
	@Test
	void install_contentNotPresentCodeSystem_skipsUpdateByDefault() throws IOException {
		// Seed a CodeSystem with content=not-present
		CodeSystem existing = new CodeSystem();
		existing.setUrl("http://example.com/cs-not-present-skip");
		existing.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
		existing.setStatus(Enumerations.PublicationStatus.ACTIVE);
		myCodeSystemDao.create(existing, REQUEST_DETAILS);

		// Install a complete CodeSystem with the same URL
		CodeSystem complete = new CodeSystem();
		complete.setUrl("http://example.com/cs-not-present-skip");
		complete.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		complete.setStatus(Enumerations.PublicationStatus.ACTIVE);
		complete.addConcept().setCode("A00").setDisplay("Cholera");

		PackageInstallationSpec spec = createInstallationSpec(packageToBytes());
		PackageInstallOutcomeJson outcome = new PackageInstallOutcomeJson();

		mySvc.install(complete, spec, outcome);

		assertThat(outcome.getResourcesInstalled()).isEmpty();

		// Verify the not-present CodeSystem is still not-present
		IBundleProvider results = myCodeSystemDao.search(
			SearchParameterMap.newSynchronous().add("url", new UriParam("http://example.com/cs-not-present-skip")),
			REQUEST_DETAILS);
		assertThat(results.getAllResources()).hasSize(1);
		CodeSystem persisted = (CodeSystem) results.getAllResources().get(0);
		assertThat(persisted.getContent()).isEqualTo(CodeSystem.CodeSystemContentMode.NOTPRESENT);
	}

	// Created by Claude Opus 4.6
	@Test
	void installPackage_withPartitioningEnabled_usesDefaultPartition() throws IOException {
		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionSettings.setDefaultPartitionId(0);

		CodeSystem cs = new CodeSystem();
		cs.setUrl("http://example.com/cs-partitioned");
		cs.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		cs.setStatus(Enumerations.PublicationStatus.ACTIVE);

		PackageInstallOutcomeJson outcome = installViaPackage(cs);

		assertThat(outcome.getResourcesInstalled()).containsEntry("CodeSystem", 1);

		IBundleProvider results = myCodeSystemDao.search(
			SearchParameterMap.newSynchronous().add("url", new UriParam("http://example.com/cs-partitioned")),
			REQUEST_DETAILS);
		assertThat(results.getAllResources()).hasSize(1);
	}

	private PackageInstallOutcomeJson installViaPackage(IBaseResource theResource) throws IOException {
		String resourceType = theResource.getClass().getSimpleName();
		NpmPackage pkg = createPackageWithResources(
			new ResourceEntry(resourceType + ".json", theResource, resourceType));

		PackageInstallationSpec spec = createInstallationSpec(packageToBytes(pkg));
		return mySvc.install(spec);
	}

	private record ResourceEntry(String fileName, IBaseResource resource, String resourceType) {}

	private NpmPackage createPackageWithResources(ResourceEntry... theEntries) {
		PackageGenerator manifestGenerator = new PackageGenerator();
		manifestGenerator.name(PACKAGE_ID_1);
		manifestGenerator.version(PACKAGE_VERSION);
		manifestGenerator.description("a package");
		manifestGenerator.fhirVersions(List.of(FhirVersionEnum.R4.getFhirVersionString()));

		NpmPackage pkg = NpmPackage.empty(manifestGenerator);
		for (ResourceEntry entry : theEntries) {
			String json = ourCtx.newJsonParser().encodeResourceToString(entry.resource());
			pkg.addFile("package", entry.fileName(), json.getBytes(StandardCharsets.UTF_8), entry.resourceType());
		}
		return pkg;
	}

	@Nonnull
	private static byte[] packageToBytes(NpmPackage thePackage) throws IOException {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		thePackage.save(stream);
		return stream.toByteArray();
	}

	@Nonnull
	private static NpmPackage createPackage() {
		PackageGenerator manifestGenerator = new PackageGenerator();
		manifestGenerator.name(PackageInstallerSvcCreateR4Test.PACKAGE_ID_1);
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
		PackageInstallerSvcCreateR4Test.PACKAGE.save(stream);
		return stream.toByteArray();
	}
}
