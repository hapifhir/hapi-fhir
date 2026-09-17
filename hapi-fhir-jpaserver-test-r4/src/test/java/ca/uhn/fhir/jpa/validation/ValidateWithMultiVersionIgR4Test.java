package ca.uhn.fhir.jpa.validation;

import ca.uhn.fhir.jpa.entity.TermCodeSystem;
import ca.uhn.fhir.jpa.packages.PackageInstallationSpec;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.packages.NpmPackageFactory;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.ValidationModeEnum;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.ElementDefinition;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.ValueSet;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The situation the bug was reported from: two versions of the same implementation guide are installed on one
 * server, and a profile from another guide is bound to the ValueSet of one of them by {@literal url|version}.
 * The codes change between the two guide versions, so validating a resource has to use the guide version the
 * profile asked for and not the other one.
 * <p/>
 * Each guide version holds a CodeSystem and a ValueSet at that version, and the ValueSet names its own
 * CodeSystem version. The consuming guide holds only the profile. Installing with
 * {@link PackageInstallationSpec.VersionPolicyEnum#MULTI_VERSION} is what keeps both versions of each resource
 * on the server, which is how the reporting site had it set up.
 * <p/>
 * Each test runs twice, once for each guide version, and <em>always installs the version it did not ask for
 * last</em>. A URL with no version resolves by {@literal meta.lastUpdated}, so the guide installed last is the
 * one that code which drops the version would find. Running both directions also rules out a fix that just
 * picks the highest version number.
 * <p/>
 * {@link ValidateWithMultiVersionTerminologyR4Test} covers the same bug from resources written straight to the
 * DAOs. This one goes through the package installer instead, so the resources are stored the way a real guide
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

	/** A code that only version 1.0.0 of the guide has. */
	private static final String CODE_IN_OLDER_VERSION = "vermilion";

	/** A code that only version 2.0.0 of the guide has. */
	private static final String CODE_IN_NEWER_VERSION = "cerulean";

	/**
	 * Installs both guide versions, the one the profile asks for first, and then the consuming guide that
	 * carries the profile.
	 */
	private void installBothGuideVersions(String theRequestedVersion) throws IOException {
		installGuide(theRequestedVersion);
		sleepUntilTimeChange();
		installGuide(otherThan(theRequestedVersion));

		installConsumingGuide(theRequestedVersion);

		myTerminologyDeferredStorageSvc.saveAllDeferred();
	}

	/**
	 * Checks the install on its own, before validation: both guide versions have to be on the server for the
	 * tests below to mean anything.
	 */
	@ParameterizedTest
	@ValueSource(strings = {VERSION_OLDER, VERSION_NEWER})
	void install_bothGuideVersions_storesBothCodeSystemVersions(String theRequestedVersion) throws IOException {
		// Setup
		installBothGuideVersions(theRequestedVersion);

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
	 * Checks resolution on its own, before validation: asking for the ValueSet the profile names has to return
	 * that version, still naming its own CodeSystem version.
	 */
	@ParameterizedTest
	@ValueSource(strings = {VERSION_OLDER, VERSION_NEWER})
	void fetchValueSet_twoGuideVersionsInstalled_returnsTheVersionAskedFor(String theRequestedVersion)
		throws IOException {
		// Setup
		installBothGuideVersions(theRequestedVersion);

		// Test
		ValueSet valueSet = (ValueSet) myValidationSupport.fetchValueSet(VS_URL + "|" + theRequestedVersion);

		// Verify
		assertThat(valueSet).isNotNull();
		assertThat(valueSet.getVersion()).isEqualTo(theRequestedVersion);
		assertThat(valueSet.getCompose().getIncludeFirstRep().getVersion()).isEqualTo(theRequestedVersion);
	}

	/**
	 * The reported bug: the code is in the guide version the profile asked for, so validation has to accept it
	 * even though the other guide version was installed afterwards.
	 */
	@ParameterizedTest
	@ValueSource(strings = {VERSION_OLDER, VERSION_NEWER})
	void validate_codeFromTheGuideVersionTheProfileAsksFor_hasNoErrors(String theRequestedVersion)
		throws IOException {
		// Setup
		installBothGuideVersions(theRequestedVersion);

		// Test
		OperationOutcome oo = validateObservationWithCode(codeIn(theRequestedVersion));

		// Verify
		assertThat(errorDiagnostics(oo)).isEmpty();
	}

	/**
	 * The other direction: a code that only the other guide version has must be rejected. Code that passes the
	 * test above by validating against the guide installed last fails here.
	 */
	@ParameterizedTest
	@ValueSource(strings = {VERSION_OLDER, VERSION_NEWER})
	void validate_codeFromTheOtherGuideVersion_hasErrors(String theRequestedVersion) throws IOException {
		// Setup
		installBothGuideVersions(theRequestedVersion);
		String codeFromTheOtherVersion = codeIn(otherThan(theRequestedVersion));

		// Test
		OperationOutcome oo = validateObservationWithCode(codeFromTheOtherVersion);

		// Verify
		assertThat(errorDiagnostics(oo)).anyMatch(t -> t.contains(codeFromTheOtherVersion));
	}

	/** The code that only the given guide version has. */
	private static String codeIn(String theVersion) {
		return VERSION_OLDER.equals(theVersion) ? CODE_IN_OLDER_VERSION : CODE_IN_NEWER_VERSION;
	}

	/** The version the test does not ask for, and therefore the one it installs last. */
	private static String otherThan(String theVersion) {
		return VERSION_OLDER.equals(theVersion) ? VERSION_NEWER : VERSION_OLDER;
	}

	/**
	 * A guide version, holding a CodeSystem and a ValueSet that both carry that version, with the ValueSet
	 * naming the CodeSystem version it was published against.
	 */
	private void installGuide(String theGuideVersion) throws IOException {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setId("colour-codesystem");
		codeSystem.setUrl(CS_URL);
		codeSystem.setVersion(theGuideVersion);
		codeSystem.setStatus(Enumerations.PublicationStatus.ACTIVE);
		codeSystem.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		codeSystem.addConcept().setCode(codeIn(theGuideVersion)).setDisplay(codeIn(theGuideVersion));

		ValueSet valueSet = new ValueSet();
		valueSet.setId("colour-valueset");
		valueSet.setUrl(VS_URL);
		valueSet.setVersion(theGuideVersion);
		valueSet.setStatus(Enumerations.PublicationStatus.ACTIVE);
		valueSet.getCompose().addInclude().setSystem(CS_URL).setVersion(theGuideVersion);

		NpmPackageFactory guide = new NpmPackageFactory(myFhirContext)
			.name(IG_NAME)
			.version(theGuideVersion)
			.addResource("CodeSystem-colour", codeSystem)
			.addResource("ValueSet-colour", valueSet);

		install(guide);
	}

	/**
	 * The guide that consumes the one above: a profile bound to one published version of its ValueSet. This is
	 * what makes the version the profile asks for differ from the version that was installed last.
	 */
	private void installConsumingGuide(String theValueSetVersion) throws IOException {
		StructureDefinition profile = getStructureDefinition();

		ElementDefinition root = profile.getDifferential().addElement();
		root.setId("Observation");
		root.setPath("Observation");

		ElementDefinition code = profile.getDifferential().addElement();
		code.setId("Observation.code");
		code.setPath("Observation.code");
		code.getBinding()
			.setStrength(Enumerations.BindingStrength.REQUIRED)
			.setValueSet(VS_URL + "|" + theValueSetVersion);

		NpmPackageFactory consumingGuide = new NpmPackageFactory(myFhirContext)
			.name(CONSUMING_IG_NAME)
			.version("1.0.0")
			.addResource("StructureDefinition-ColourObservation", profile);

		install(consumingGuide);
	}

	private static @NonNull StructureDefinition getStructureDefinition() {
		StructureDefinition profile = new StructureDefinition();
		profile.setId("ColourObservation");
		profile.setUrl(PROFILE_URL);
		profile.setName("ColourObservation");
		profile.setStatus(Enumerations.PublicationStatus.ACTIVE);
		profile.setFhirVersion(Enumerations.FHIRVersion._4_0_1);
		profile.setKind(StructureDefinition.StructureDefinitionKind.RESOURCE);
		profile.setAbstract(false);
		profile.setType("Observation");
		profile.setBaseDefinition("http://hl7.org/fhir/StructureDefinition/Observation");
		profile.setDerivation(StructureDefinition.TypeDerivationRule.CONSTRAINT);
		return profile;
	}

	private void install(NpmPackageFactory theGuide) throws IOException {
		myPackageInstallerSvc.install(new PackageInstallationSpec()
			.setName(theGuide.getPackageName())
			.setVersion(theGuide.getPackageVersion())
			.setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_AND_INSTALL)
			.setVersionPolicy(PackageInstallationSpec.VersionPolicyEnum.MULTI_VERSION)
			.setPackageContents(theGuide.createPackageBytes()));
	}

	private OperationOutcome validateObservationWithCode(String theCode) {
		Observation observation = new Observation();
		observation.getMeta().addProfile(PROFILE_URL);
		observation.setStatus(Observation.ObservationStatus.FINAL);
		observation.getCode().addCoding().setSystem(CS_URL).setCode(theCode).setDisplay(theCode);

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
