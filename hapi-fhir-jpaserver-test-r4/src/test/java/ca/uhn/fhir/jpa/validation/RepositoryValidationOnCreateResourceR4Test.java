package ca.uhn.fhir.jpa.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.interceptor.validation.IRepositoryValidatingRule;
import ca.uhn.fhir.jpa.interceptor.validation.RepositoryValidatingInterceptor;
import ca.uhn.fhir.jpa.interceptor.validation.RepositoryValidatingRuleBuilder;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r5.utils.validation.constants.BestPracticeWarningLevel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

public class RepositoryValidationOnCreateResourceR4Test extends BaseResourceProviderR4Test {
	private static final String myResourceType = "Patient";
	private final String myProfile = "http://example.org/fhir/StructureDefinition/TestPatient";

	private static final FhirContext ourCtx = FhirContext.forR4();
	private final RepositoryValidatingInterceptor myRepositoryValidatingInterceptor = new RepositoryValidatingInterceptor(ourCtx, Collections.emptyList());

	@BeforeEach
	public void before() {
		myInterceptorRegistry.registerInterceptor(myRepositoryValidatingInterceptor);
	}

	@AfterEach
	public void after() {
		myInterceptorRegistry.unregisterInterceptor(myRepositoryValidatingInterceptor);
	}

	@Test
	public void testCreatePatient_withValidationRuleNoVersion_validatesAgainstLatestVersion() {
		createProfile("1", "Patient.identifier");
		createProfile("2", "Patient.name");

		setupRepositoryValidationRules(myProfile);

		try {
			createPatient(withProfile(myProfile));
			fail();
		} catch (PreconditionFailedException e) {
			assertEquals(Msg.code(574)
				+ "Patient.name: minimum required = 1, but only found 0 (from " + myProfile + "|2)", e.getMessage());
		}
	}

	@Test
	public void testCreatePatient_withValidationRuleWithVersion_unknownProfile() {
		createProfile("1", "Patient.identifier");
		createProfile("2", "Patient.name");

		setupRepositoryValidationRules(myProfile + "|1");

		try {
			createPatient(withProfile(myProfile + "|1"));
			fail();
		} catch (PreconditionFailedException e) {
			assertEquals(Msg.code(574) + "Profile reference '" + myProfile
				+ "|1' has not been checked because it is unknown, and the validator is set to not fetch unknown profiles", e.getMessage());
		}
	}

	@Test
	public void testCreatePatient_withProfileWithVersion_doesNotConform() {
		setupRepositoryValidationRules(myProfile);

		try {
			createPatient(withProfile(myProfile + "|1"));
			fail();
		} catch (PreconditionFailedException e) {
			assertEquals(Msg.code(575) + "Resource of type \"" + myResourceType
				+ "\" does not declare conformance to profile from: [" + myProfile + "]", e.getMessage());
		}
	}

	@Test
	public void testCreatePatient_withValidationRuleWithVersion_doesNotConform() {
		setupRepositoryValidationRules(myProfile + "|1");
		try {
			createPatient(withProfile(myProfile));
			fail();
		} catch (PreconditionFailedException e) {
			assertEquals(Msg.code(575) + "Resource of type \"" + myResourceType
				+ "\" does not declare conformance to profile from: [" + myProfile + "|1]", e.getMessage());
		}
	}

	@Test
	public void testCreatePatient_withMultipleProfiles_validatesAgainstLastProfile() {
		final String sdIdentifier = myProfile + "-identifier";
		final String sdName = myProfile + "-name";

		createProfile(sdIdentifier, "1", "Patient.identifier");
		createProfile(sdName, "1", "Patient.name");

		setupRepositoryValidationRules(sdIdentifier, sdName);
		try {
			createPatient(withProfile(sdIdentifier), withProfile(sdName));
			fail();
		} catch (PreconditionFailedException e) {
			assertEquals(Msg.code(574)
				+ "Patient.name: minimum required = 1, but only found 0 (from " + sdName + "|1)", e.getMessage());
		}
	}

	private void setupRepositoryValidationRules(String... theProfiles) {
		List<IRepositoryValidatingRule> rules = myAppCtx
			.getBean(RepositoryValidatingRuleBuilder.REPOSITORY_VALIDATING_RULE_BUILDER, RepositoryValidatingRuleBuilder.class)
			.forResourcesOfType(myResourceType)
			.requireAtLeastOneProfileOf(theProfiles)
			.and()
			.requireValidationToDeclaredProfiles()
			.withBestPracticeWarningLevel(BestPracticeWarningLevel.Ignore)
			.rejectOnSeverity("error")
			.build();

		myRepositoryValidatingInterceptor.setRules(rules);
	}

	private void createProfile(String theVersion, String theRequiredPath) {
		createProfile(myProfile, theVersion, theRequiredPath);
	}

	private void createProfile(String theUrl, String theVersion, String thePath) {
		final String baseProfile = "http://hl7.org/fhir/StructureDefinition/Patient";
		final String profileId = "TestProfile";

		StructureDefinition sd = new StructureDefinition()
			.setUrl(theUrl).setVersion(theVersion)
			.setBaseDefinition(baseProfile)
			.setType(myResourceType)
			.setDerivation(StructureDefinition.TypeDerivationRule.CONSTRAINT);
		sd.setId(profileId);
		sd.getDifferential().addElement()
			.setPath(thePath)
			.setMin(1)
			.setId(thePath);

		DaoMethodOutcome outcome = myStructureDefinitionDao.update(sd, new SystemRequestDetails());
		assertNotNull(outcome.getResource());
	}
}
