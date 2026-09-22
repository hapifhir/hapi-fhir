package ca.uhn.fhir.jpa.validation;

import ca.uhn.fhir.jpa.entity.TermCodeSystem;
import ca.uhn.fhir.jpa.packages.PackageInstallationSpec;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.packages.NpmPackageFactory;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.ValidationModeEnum;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.ElementDefinition;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Two versions of the same implementation guide installed on one server, where the codes change between the
 * two versions, so validating a resource has to use the version that was specified and not the other one. The
 * nested classes cover the ways a resource ends up bound to one version:
 * <ul>
 *	   <li>{@link VersionedProfileInEachIgTest} - each IG version ships its own version of the profile,
 *	   and the resource names one of them in {@literal meta.profile} as {@literal url|version}</li>
 *	   <li>{@link ProfileFromAConsumingIgTest} - the IG versions ship only terminology, and a separate
 *	   IG holds a profile bound to one IG version's ValueSet by {@literal url|version}, named by an
 *	   unversioned {@literal meta.profile}</li>
 *	   <li>{@link MixedVersionsInOneBundleTest} - one transaction Bundle whose entries name different IG
 *	   versions, so both versions have to be honoured within a single validation</li>
 * </ul>
 * In each, the ValueSet names its own CodeSystem version, so every step of the chain specifies a version.
 * Installing with {@link PackageInstallationSpec.VersionPolicyEnum#MULTI_VERSION} is what keeps both versions
 * of each resource on the server.
 * <p/>
 * Each test runs twice, once for each IG version, and <em>always installs the version it did not specify
 * last</em>. A URL with no version resolves by {@literal meta.lastUpdated}, so the IG installed last is the
 * one that code which drops the version finds. Running both directions also rules out a fix that just picks
 * the highest version number.
 * <p/>
 * {@link ValidateWithMultiVersionTerminologyR4Test} covers the same ground from resources written straight to
 * the DAOs. This one goes through the package installer instead, so the resources are stored the way a real IG
 * install stores them.
 */
// Created by Claude Opus 5
class ValidateWithMultiVersionIgR4Test extends BaseJpaR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(ValidateWithMultiVersionIgR4Test.class);

	private static final String IG_NAME = "example.colour";
	private static final String CONSUMING_IG_NAME = "example.colour.local";

	private static final String CS_URL = "http://example.org/fhir/CodeSystem/colour";
	private static final String VS_URL = "http://example.org/fhir/ValueSet/colour";
	private static final String PROFILE_URL = "http://example.org/fhir/StructureDefinition/ColourObservation";

	private static final String VERSION_OLDER = "1.0.0";
	private static final String VERSION_NEWER = "2.0.0";

	/** A code that only version 1.0.0 of the IG has. */
	private static final String CODE_IN_OLDER_VERSION = "vermilion";

	/** A code that only version 2.0.0 of the IG has. */
	private static final String CODE_IN_NEWER_VERSION = "cerulean";

	@Nested
	class VersionedProfileInEachIgTest {

		/**
		 * Installs both IG versions, each carrying its own profile, the specified one first.
		 */
		void installBothIgVersions(String theSpecifiedVersion) throws IOException {
			installIg(theSpecifiedVersion, true);
			sleepUntilTimeChange();
			installIg(otherThan(theSpecifiedVersion), true);

			myTerminologyDeferredStorageSvc.saveAllDeferred();
		}

		/**
		 * Checks resolution on its own, before validation: naming the profile as {@literal url|version} has to
		 * return that version of it, still bound to its own ValueSet version.
		 */
		@ParameterizedTest
		@ValueSource(strings = {VERSION_OLDER, VERSION_NEWER})
		void fetchStructureDefinition_bothIgVersionsInstalled_returnsTheVersionSpecified(
				String theSpecifiedVersion) throws IOException {
			// Setup
			installBothIgVersions(theSpecifiedVersion);

			// Test
			StructureDefinition profile =
				(StructureDefinition) myValidationSupport.fetchStructureDefinition(PROFILE_URL + "|" + theSpecifiedVersion);

			// Verify
			assertThat(profile).isNotNull();
			assertThat(profile.getVersion()).isEqualTo(theSpecifiedVersion);
			assertThat(profile.getDifferential().getElement().get(1).getBinding().getValueSet())
				.isEqualTo(VS_URL + "|" + theSpecifiedVersion);
		}

		/**
		 * The code is in the IG version the resource names, so validation has to accept it even though the other
		 * IG version was installed afterwards.
		 */
		@ParameterizedTest
		@ValueSource(strings = {VERSION_OLDER, VERSION_NEWER})
		void validate_codeFromTheIgVersionSpecified_hasNoErrors(String theSpecifiedVersion) throws IOException {
			// Setup
			installBothIgVersions(theSpecifiedVersion);

			// Test
			OperationOutcome oo = validateObservationWithCode(
				codeIn(theSpecifiedVersion), PROFILE_URL + "|" + theSpecifiedVersion);

			// Verify
			assertThat(errorDiagnostics(oo)).isEmpty();
		}

		/**
		 * The other direction: a code that only the other IG version has must be rejected.
		 */
		@ParameterizedTest
		@ValueSource(strings = {VERSION_OLDER, VERSION_NEWER})
		void validate_codeFromTheOtherIgVersion_hasErrors(String theSpecifiedVersion) throws IOException {
			// Setup
			installBothIgVersions(theSpecifiedVersion);
			String codeFromTheOtherVersion = codeIn(otherThan(theSpecifiedVersion));

			// Test
			OperationOutcome oo = validateObservationWithCode(
				codeFromTheOtherVersion, PROFILE_URL + "|" + theSpecifiedVersion);

			// Verify
			assertThat(errorDiagnostics(oo)).anyMatch(t -> t.contains(codeFromTheOtherVersion));
		}
	}

	@Nested
	class ProfileFromAConsumingIgTest {

		/**
		 * Installs both IG versions, the one the profile specifies first, and then the consuming IG that
		 * carries the profile.
		 */
		void installBothIgVersions(String theSpecifiedVersion) throws IOException {
			installIg(theSpecifiedVersion, false);
			sleepUntilTimeChange();
			installIg(otherThan(theSpecifiedVersion), false);

			installConsumingIg(theSpecifiedVersion);

			myTerminologyDeferredStorageSvc.saveAllDeferred();
		}

		/**
		 * Checks the install on its own, before validation: both IG versions have to be on the server for
		 * the tests below to mean anything.
		 */
		@ParameterizedTest
		@ValueSource(strings = {VERSION_OLDER, VERSION_NEWER})
		void install_bothIgVersions_storesBothCodeSystemVersions(String theSpecifiedVersion) throws IOException {
			// Setup
			installBothIgVersions(theSpecifiedVersion);

			// Test & Verify
			runInTransaction(() -> {
				TermCodeSystem codeSystem = myTermCodeSystemDao.findByCodeSystemUri(CS_URL);
				assertThat(codeSystem).isNotNull();
				assertThat(myTermCodeSystemVersionDao.findByCodeSystemPidAndVersion(codeSystem.getPid(), VERSION_OLDER))
					.isNotNull();
				assertThat(myTermCodeSystemVersionDao.findByCodeSystemPidAndVersion(codeSystem.getPid(), VERSION_NEWER))
					.isNotNull();
			});
		}

		/**
		 * Checks resolution on its own, before validation: asking for the ValueSet the profile specifies has
		 * to return that version, still naming its own CodeSystem version.
		 */
		@ParameterizedTest
		@ValueSource(strings = {VERSION_OLDER, VERSION_NEWER})
		void fetchValueSet_twoIgVersionsInstalled_returnsTheVersionSpecified(String theSpecifiedVersion)
			throws IOException {
			// Setup
			installBothIgVersions(theSpecifiedVersion);

			// Test
			ValueSet valueSet = (ValueSet) myValidationSupport.fetchValueSet(VS_URL + "|" + theSpecifiedVersion);

			// Verify
			assertThat(valueSet).isNotNull();
			assertThat(valueSet.getVersion()).isEqualTo(theSpecifiedVersion);
			assertThat(valueSet.getCompose().getIncludeFirstRep().getVersion()).isEqualTo(theSpecifiedVersion);
		}

		/**
		 * The code is in the IG version the profile specifies, so validation has to accept it even though
		 * the other IG version was installed afterwards.
		 */
		@ParameterizedTest
		@ValueSource(strings = {VERSION_OLDER, VERSION_NEWER})
		void validate_codeFromTheIgVersionSpecified_hasNoErrors(String theSpecifiedVersion) throws IOException {
			// Setup
			installBothIgVersions(theSpecifiedVersion);

			// Test
			OperationOutcome oo = validateObservationWithCode(codeIn(theSpecifiedVersion), PROFILE_URL);

			// Verify
			assertThat(errorDiagnostics(oo)).isEmpty();
		}

		/**
		 * The other direction: a code that only the other IG version has must be rejected. Code that passes
		 * the test above by validating against the IG installed last fails here.
		 */
		@ParameterizedTest
		@ValueSource(strings = {VERSION_OLDER, VERSION_NEWER})
		void validate_codeFromTheOtherIgVersion_hasErrors(String theSpecifiedVersion) throws IOException {
			// Setup
			installBothIgVersions(theSpecifiedVersion);
			String codeFromTheOtherVersion = codeIn(otherThan(theSpecifiedVersion));

			// Test
			OperationOutcome oo = validateObservationWithCode(codeFromTheOtherVersion, PROFILE_URL);

			// Verify
			assertThat(errorDiagnostics(oo)).anyMatch(t -> t.contains(codeFromTheOtherVersion));
		}
	}

	/**
	 * One Bundle whose entries specify different IG versions. Every other test here validates a single
	 * resource against a single version, so none of them can catch version resolution leaking between
	 * resources inside one validation pass - the adapter caches the converted ValueSet on the ValueSet
	 * instance, and the validation support caches by URL, so an entry could in principle be answered from
	 * whichever version the entry before it resolved.
	 * <p/>
	 * This is not what the reporting site sends; their payloads name one IG version throughout. It covers the
	 * next case along.
	 */
	@Nested
	class MixedVersionsInOneBundleTest {

		void installBothIgVersions() throws IOException {
			installIg(VERSION_OLDER, true);
			sleepUntilTimeChange();
			installIg(VERSION_NEWER, true);

			myTerminologyDeferredStorageSvc.saveAllDeferred();
		}

		/**
		 * Each entry names its own IG version and carries a code that version has, so validation has to accept
		 * the whole Bundle.
		 */
		@Test
		void validate_entriesOnDifferentIgVersions_hasNoErrors() throws IOException {
			// Setup
			installBothIgVersions();
			Bundle bundle = newTransaction(
				observation(CODE_IN_OLDER_VERSION, PROFILE_URL + "|" + VERSION_OLDER),
				observation(CODE_IN_NEWER_VERSION, PROFILE_URL + "|" + VERSION_NEWER));

			// Test
			OperationOutcome oo = validateBundle(bundle);

			// Verify
			assertThat(errorDiagnostics(oo)).isEmpty();
		}

		/**
		 * The same Bundle with the codes swapped, so each entry carries the code its own version does not
		 * have. Both have to be reported. Without this, the test above would also pass if the entries were
		 * never validated at all.
		 */
		@Test
		void validate_entriesCarryingTheOtherVersionsCode_hasErrors() throws IOException {
			// Setup
			installBothIgVersions();
			Bundle bundle = newTransaction(
				observation(CODE_IN_NEWER_VERSION, PROFILE_URL + "|" + VERSION_OLDER),
				observation(CODE_IN_OLDER_VERSION, PROFILE_URL + "|" + VERSION_NEWER));

			// Test
			OperationOutcome oo = validateBundle(bundle);

			// Verify
			assertThat(errorDiagnostics(oo)).anyMatch(t -> t.contains(CODE_IN_NEWER_VERSION));
			assertThat(errorDiagnostics(oo)).anyMatch(t -> t.contains(CODE_IN_OLDER_VERSION));
		}
	}

	/** The code that only the given IG version has. */
	private static String codeIn(String theVersion) {
		return VERSION_OLDER.equals(theVersion) ? CODE_IN_OLDER_VERSION : CODE_IN_NEWER_VERSION;
	}

	/** The version the test does not specify, and therefore the one it installs last. */
	private static String otherThan(String theVersion) {
		return VERSION_OLDER.equals(theVersion) ? VERSION_NEWER : VERSION_OLDER;
	}

	/**
	 * A IG version, holding a CodeSystem and a ValueSet that both carry that version, with the ValueSet
	 * naming the CodeSystem version it was published against. When asked for a profile it carries one at that
	 * version too, bound to its own ValueSet version.
	 */
	private void installIg(String theIgVersion, boolean theIncludeProfile) throws IOException {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setId("colour-codesystem");
		codeSystem.setUrl(CS_URL);
		codeSystem.setVersion(theIgVersion);
		codeSystem.setStatus(Enumerations.PublicationStatus.ACTIVE);
		codeSystem.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		codeSystem.addConcept().setCode(codeIn(theIgVersion)).setDisplay(codeIn(theIgVersion));

		ValueSet valueSet = new ValueSet();
		valueSet.setId("colour-valueset");
		valueSet.setUrl(VS_URL);
		valueSet.setVersion(theIgVersion);
		valueSet.setStatus(Enumerations.PublicationStatus.ACTIVE);
		valueSet.getCompose().addInclude().setSystem(CS_URL).setVersion(theIgVersion);

		NpmPackageFactory ig = new NpmPackageFactory(myFhirContext)
			.name(IG_NAME)
			.version(theIgVersion)
			.addResource("CodeSystem-colour", codeSystem)
			.addResource("ValueSet-colour", valueSet);

		if (theIncludeProfile) {
			ig.addResource(
				"StructureDefinition-ColourObservation",
				buildProfile(theIgVersion, VS_URL + "|" + theIgVersion));
		}

		install(ig);
	}

	/**
	 * The IG that consumes the one above: a profile bound to one published version of its ValueSet. This is
	 * what makes the version the profile specifies differ from the version that was installed last.
	 */
	private void installConsumingIg(String theValueSetVersion) throws IOException {
		NpmPackageFactory consumingIg = new NpmPackageFactory(myFhirContext)
			.name(CONSUMING_IG_NAME)
			.version("1.0.0")
			.addResource(
				"StructureDefinition-ColourObservation",
				buildProfile(null, VS_URL + "|" + theValueSetVersion));

		install(consumingIg);
	}

	private StructureDefinition buildProfile(String theProfileVersion, String theValueSetUrl) {
		StructureDefinition profile = new StructureDefinition();
		profile.setId("ColourObservation");
		profile.setUrl(PROFILE_URL);
		profile.setVersion(theProfileVersion);
		profile.setName("ColourObservation");
		profile.setStatus(Enumerations.PublicationStatus.ACTIVE);
		profile.setFhirVersion(Enumerations.FHIRVersion._4_0_1);
		profile.setKind(StructureDefinition.StructureDefinitionKind.RESOURCE);
		profile.setAbstract(false);
		profile.setType("Observation");
		profile.setBaseDefinition("http://hl7.org/fhir/StructureDefinition/Observation");
		profile.setDerivation(StructureDefinition.TypeDerivationRule.CONSTRAINT);

		ElementDefinition root = profile.getDifferential().addElement();
		root.setId("Observation");
		root.setPath("Observation");

		ElementDefinition code = profile.getDifferential().addElement();
		code.setId("Observation.code");
		code.setPath("Observation.code");
		code.getBinding().setStrength(Enumerations.BindingStrength.REQUIRED).setValueSet(theValueSetUrl);

		return profile;
	}

	private void install(NpmPackageFactory theIg) throws IOException {
		myPackageInstallerSvc.install(new PackageInstallationSpec()
			.setName(theIg.getPackageName())
			.setVersion(theIg.getPackageVersion())
			.setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_AND_INSTALL)
			.setVersionPolicy(PackageInstallationSpec.VersionPolicyEnum.MULTI_VERSION)
			.setPackageContents(theIg.createPackageBytes()));
	}

	private Observation observation(String theCode, String theProfileCanonical) {
		Observation observation = new Observation();
		observation.getMeta().addProfile(theProfileCanonical);
		observation.setStatus(Observation.ObservationStatus.FINAL);
		observation.getCode().addCoding().setSystem(CS_URL).setCode(theCode).setDisplay(theCode);
		return observation;
	}

	/** A transaction Bundle posting each of the given resources. */
	private Bundle newTransaction(Observation... theObservations) {
		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.TRANSACTION);
		for (Observation next : theObservations) {
			bundle.addEntry()
				.setResource(next)
				.getRequest()
				.setMethod(Bundle.HTTPVerb.POST)
				.setUrl("Observation");
		}
		return bundle;
	}

	private OperationOutcome validateBundle(Bundle theBundle) {
		OperationOutcome oo;
		try {
			MethodOutcome outcome =
				myBundleDao.validate(theBundle, null, null, null, ValidationModeEnum.CREATE, null, mySrd);
			oo = (OperationOutcome) outcome.getOperationOutcome();
		} catch (PreconditionFailedException e) {
			oo = (OperationOutcome) e.getOperationOutcome();
		}
		ourLog.info("Bundle validation errors: {}", errorDiagnostics(oo));
		return oo;
	}

	private OperationOutcome validateObservationWithCode(String theCode, String theProfileCanonical) {
		Observation observation = observation(theCode, theProfileCanonical);

		OperationOutcome oo;
		try {
			MethodOutcome outcome =
				myObservationDao.validate(observation, null, null, null, ValidationModeEnum.CREATE, null, mySrd);
			oo = (OperationOutcome) outcome.getOperationOutcome();
		} catch (PreconditionFailedException e) {
			oo = (OperationOutcome) e.getOperationOutcome();
		}
		ourLog.info("Validation errors: {}", errorDiagnostics(oo));
		return oo;
	}

	private List<String> errorDiagnostics(OperationOutcome theOutcome) {
		return theOutcome.getIssue().stream()
			.filter(t -> t.getSeverity() == OperationOutcome.IssueSeverity.ERROR
				|| t.getSeverity() == OperationOutcome.IssueSeverity.FATAL)
			.map(OperationOutcome.OperationOutcomeIssueComponent::getDiagnostics)
			.toList();
	}
}
