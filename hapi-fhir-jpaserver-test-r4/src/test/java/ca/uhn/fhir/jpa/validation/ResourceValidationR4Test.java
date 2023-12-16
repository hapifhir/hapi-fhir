package ca.uhn.fhir.jpa.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.interceptor.validation.IRepositoryValidatingRule;
import ca.uhn.fhir.jpa.interceptor.validation.RepositoryValidatingInterceptor;
import ca.uhn.fhir.jpa.interceptor.validation.RepositoryValidatingRuleBuilder;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.gclient.TokenClientParam;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.rest.param.UriParamQualifierEnum;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.SingleValidationMessage;
import ca.uhn.fhir.validation.ValidationResult;
import org.hl7.fhir.common.hapi.validation.support.CachingValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.CommonCodeSystemsTerminologyService;
import org.hl7.fhir.common.hapi.validation.support.InMemoryTerminologyServerValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.PrePopulatedValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.SnapshotGeneratingValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r5.utils.validation.constants.BestPracticeWarningLevel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class ResourceValidationR4Test extends BaseResourceProviderR4Test {
	private static final FhirContext ourCtx = FhirContext.forR4();
	private static FhirValidator myFhirValidator;
	private static PrePopulatedValidationSupport ourValidationSupport;
	private static final String myResourceType = "Patient";
	private static final String myResourceTypeDefinition = "http://hl7.org/fhir/StructureDefinition/Patient";
	private static final String PATIENT_STRUCTURE_DEFINITION_URL = "http://example.org/fhir/StructureDefinition/TestPatient";

	private final RepositoryValidatingInterceptor myRepositoryValidatingInterceptor = new RepositoryValidatingInterceptor(ourCtx, Collections.emptyList());
	@Autowired
	private ApplicationContext myApplicationContext;

	private final String profile = PATIENT_STRUCTURE_DEFINITION_URL;

	@BeforeAll
	public static void setup() {
		myFhirValidator = ourCtx.newValidator();
		myFhirValidator.setValidateAgainstStandardSchema(false);
		myFhirValidator.setValidateAgainstStandardSchematron(false);

		ourValidationSupport = new PrePopulatedValidationSupport(ourCtx);

		ValidationSupportChain chain = new ValidationSupportChain(
			new DefaultProfileValidationSupport(ourCtx),
			new SnapshotGeneratingValidationSupport(ourCtx),
			new CommonCodeSystemsTerminologyService(ourCtx),
			new InMemoryTerminologyServerValidationSupport(ourCtx),
			ourValidationSupport);
		CachingValidationSupport myValidationSupport = new CachingValidationSupport(chain, true);
		FhirInstanceValidator myInstanceVal = new FhirInstanceValidator(myValidationSupport);
		myFhirValidator.registerValidatorModule(myInstanceVal);
	}

	@BeforeEach
	public void before() {
		myInterceptorRegistry.registerInterceptor(myRepositoryValidatingInterceptor);
	}

	@AfterEach
	public void after() {
		myInterceptorRegistry.unregisterInterceptor(myRepositoryValidatingInterceptor);
	}

	@Test
	public void testCreateStructureDefinition_createMultipleWithWithSameUrl_isStoredSuccessfully() {
		createPatientStructureDefinitionWithMandatoryField(PATIENT_STRUCTURE_DEFINITION_URL, "1", "Patient.identifier");
		createPatientStructureDefinitionWithMandatoryField(PATIENT_STRUCTURE_DEFINITION_URL, "2", "Patient.name");
		createPatientStructureDefinitionWithMandatoryField(PATIENT_STRUCTURE_DEFINITION_URL, "3", "Patient.birthdate");
	}


	@Nested
	public class OnDemandResourceValidationTests {
		@Test
		public void testValidatePatient_withProfileIncludingVersion_shouldUseSpecifiedVersion() {

			createPatientStructureDefinitionWithMandatoryField(profile, "1", "Patient.identifier");
			createPatientStructureDefinitionWithMandatoryField(profile, "2", "Patient.name");

			IIdType idType = createPatient(withProfile(profile + "|1"));
			Patient patient = myPatientDao.read(idType, mySrd);

			ValidationResult validationResult = myFhirValidator.validateWithResult(patient);
			assertFalse(validationResult.isSuccessful());
			assertEquals(1, validationResult.getMessages().size());

			SingleValidationMessage message = validationResult.getMessages().iterator().next();
			assertEquals("Patient.identifier: minimum required = 1, but only found 0 (from " + profile + "|1)", message.getMessage());
		}

		@Test
		public void testValidatePatient_withProfileNoVersion_shouldUsesLatestVersion() {

			createPatientStructureDefinitionWithMandatoryField(profile, "1", "Patient.identifier");
			createPatientStructureDefinitionWithMandatoryField(profile, "2", "Patient.name");

			IIdType idType = createPatient(withProfile(profile));
			Patient patient = myPatientDao.read(idType, mySrd);

			ValidationResult validationResult = myFhirValidator.validateWithResult(patient);
			assertFalse(validationResult.isSuccessful());
			assertEquals(1, validationResult.getMessages().size());

			SingleValidationMessage message = validationResult.getMessages().iterator().next();
			assertEquals("Patient.name: minimum required = 1, but only found 0 (from " + profile + "|2)", message.getMessage());
		}

		@Test
		public void testValidatePatient_withMultipleProfilesDifferentUrls_validatesAgainstAllProfiles() {
			final String sdIdentifier = PATIENT_STRUCTURE_DEFINITION_URL + "-identifier";
			final String sdName = PATIENT_STRUCTURE_DEFINITION_URL + "-name";

			createPatientStructureDefinitionWithMandatoryField(sdIdentifier, "1", "Patient.identifier");
			createPatientStructureDefinitionWithMandatoryField(sdName, "1", "Patient.name");

			IIdType idType = createPatient(withProfile(sdIdentifier), withProfile(sdName));
			Patient patient = myPatientDao.read(idType, mySrd);

			ValidationResult validationResult = myFhirValidator.validateWithResult(patient);
			assertFalse(validationResult.isSuccessful());
			assertEquals(2, validationResult.getMessages().size());

			Iterator<SingleValidationMessage> messageIterator = validationResult.getMessages().iterator();
			assertEquals("Patient.identifier: minimum required = 1, but only found 0 (from " + profile + "-identifier|1)", messageIterator.next().getMessage());
			assertEquals("Patient.name: minimum required = 1, but only found 0 (from " + profile + "-name|1)", messageIterator.next().getMessage());
		}

		@Test
		public void testValidatePatient_withMultipleProfilesSameUrlDifferentVersions_validatesAgainstFirstVersion() {

			createPatientStructureDefinitionWithMandatoryField(profile, "1", "Patient.identifier");
			createPatientStructureDefinitionWithMandatoryField(profile, "2", "Patient.name");
			createPatientStructureDefinitionWithMandatoryField(profile, "3", "Patient.birthDate");

			IIdType idType = createPatient(
				withProfile(profile + "|2"),
				withProfile(profile + "|1"),
				withProfile(profile + "|3"));
			Patient patient = myPatientDao.read(idType, mySrd);

			ValidationResult validationResult = myFhirValidator.validateWithResult(patient);
			assertFalse(validationResult.isSuccessful());
			assertEquals(1, validationResult.getMessages().size());

			SingleValidationMessage message = validationResult.getMessages().iterator().next();
			assertEquals("Patient.identifier: minimum required = 1, but only found 0 (from " + profile + "|1)", message.getMessage());
		}
	}

	@Nested
	public class RepositoryValidationOnCreateTests {
		@Test
		public void testCreatePatient_withProfileNoVersionAndRepositoryValidationRuleNoVersion_validatesAgainstFirstVersion() {
			setupRepositoryValidationRules(profile);

			createPatientStructureDefinitionWithMandatoryField(profile, "1", "Patient.identifier");
			createPatientStructureDefinitionWithMandatoryField(PATIENT_STRUCTURE_DEFINITION_URL, "2", "Patient.name");

			try {
				createPatient(withProfile(profile));
				fail();
			} catch (PreconditionFailedException e) {
				ourLog.info(e.getMessage());
				assertEquals(Msg.code(574)
					+ "Patient.identifier: minimum required = 1, but only found 0 (from " + profile + "|1)", e.getMessage());
			}
		}

		@Test
		public void testCreatePatient_withProfileWithVersionAndRepositoryValidationRuleWithVersion_unknownProfile() {
			setupRepositoryValidationRules(profile + "|1");

			createPatientStructureDefinitionWithMandatoryField(profile, "1", "Patient.identifier");
			createPatientStructureDefinitionWithMandatoryField(profile, "2", "Patient.name");

			try {
				createPatient(withProfile(profile + "|1"));
				fail();
			} catch (PreconditionFailedException e) {
				assertEquals(Msg.code(574) + "Profile reference '" + profile
					+ "|1' has not been checked because it is unknown, and the validator is set to not fetch unknown profiles", e.getMessage());
			}
		}

		@Test
		public void testCreatePatient_withProfileWithVersionAndRepositoryValidationRuleNoVersion_doesNotConform() {
			setupRepositoryValidationRules(profile);

			createPatientStructureDefinitionWithMandatoryField(profile, "1", "Patient.identifier");
			createPatientStructureDefinitionWithMandatoryField(profile, "2", "Patient.name");

			try {
				createPatient(withProfile(profile + "|1"));
				fail();
			} catch (PreconditionFailedException e) {
				assertEquals(Msg.code(575) + "Resource of type \"" + myResourceType
					+ "\" does not declare conformance to profile from: [" + profile + "]", e.getMessage());
			}
		}

		@Test
		public void testCreatePatient_withPatientProfileWithoutVersionAndRepositoryValidationRuleWithVersion_doesNotConform() {
			setupRepositoryValidationRules(profile + "|1");

			createPatientStructureDefinitionWithMandatoryField(profile, "1", "Patient.identifier");
			createPatientStructureDefinitionWithMandatoryField(profile, "2", "Patient.name");

			try {
				createPatient(withProfile(profile));
				fail();
			} catch (PreconditionFailedException e) {
				assertEquals(Msg.code(575) + "Resource of type \"" + myResourceType
					+ "\" does not declare conformance to profile from: [" + profile + "|1]", e.getMessage());
			}
		}

		private void setupRepositoryValidationRules(String... theProfiles) {
			List<IRepositoryValidatingRule> rules = myApplicationContext
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
	}

	@Nested
	public class SearchWithProfileTests {
		@Test
		public void testSearchPatient_withProfileBelowCriteria_returnsAllMatches() {
			createPatientStructureDefinitionWithMandatoryField(profile, "1", "Patient.identifier");
			IIdType id1 = createPatient(withProfile(profile));
			IIdType id2 = createPatient(withProfile(profile + "|1"));

			SearchParameterMap params = new SearchParameterMap();
			params.add("_profile", new UriParam(profile).setQualifier(UriParamQualifierEnum.BELOW));
			IBundleProvider bundleProvider = myPatientDao.search(params, mySrd);
			assertFalse(bundleProvider.isEmpty());
			assertThat(toUnqualifiedVersionlessIdValues(bundleProvider),
				containsInAnyOrder(id1.toUnqualifiedVersionless().getValue(), id2.toUnqualifiedVersionless().getValue()));
		}

		@Test
		public void testSearchPatient_withProfileExactCriteriaWithoutVersionAndPatientProfileWithoutVersion_returnsExactMatch() {
			createPatientStructureDefinitionWithMandatoryField(profile, "1", "Patient.identifier");

			IIdType id1 = createPatient(withProfile(profile));
			IIdType id2 = createPatient(withProfile(profile));

			Bundle outcome = myClient.search().forResource(myResourceType)
				.where(new TokenClientParam("_profile").exactly().code(profile))
				.returnBundle(Bundle.class).execute();
			assertThat(toUnqualifiedVersionlessIdValues(outcome),
				containsInAnyOrder(id1.toUnqualifiedVersionless().getValue(), id2.toUnqualifiedVersionless().getValue()));
		}

		@Test
		public void testSearchPatient_withProfileExactCriteriaWithVersionAndPatientProfileWithVersion_returnsExactMatch() {
			createPatientStructureDefinitionWithMandatoryField(profile, "1", "Patient.identifier");
			IIdType id1 = createPatient(withProfile(profile + "|1"));

			Bundle outcome = myClient.search().forResource(myResourceType)
				.where(new TokenClientParam("_profile").exactly().code(profile + "|1"))
				.returnBundle(Bundle.class).execute();
			assertThat(toUnqualifiedVersionlessIdValues(outcome),
				containsInAnyOrder(id1.toUnqualifiedVersionless().getValue()));
		}

		@Test
		public void testSearchPatient_withProfileExactCriteriaWithoutVersionAndPatientProfileWithVersion_returnsNoMatch() {
			final String profile = PATIENT_STRUCTURE_DEFINITION_URL;

			createPatientStructureDefinitionWithMandatoryField(profile, "1", "Patient.identifier");
			createPatient(withProfile(profile + "|1"));

			Bundle outcome = myClient.search().forResource(myResourceType)
				.where(new TokenClientParam("_profile").exactly().code(profile))
				.returnBundle(Bundle.class).execute();
			assertTrue(outcome.getEntryFirstRep().isEmpty());
		}
	}

	private void createPatientStructureDefinitionWithMandatoryField(String theUrl, String theVersion, String thePath) {
		StructureDefinition sd = new StructureDefinition()
			.setUrl(theUrl).setVersion(theVersion)
			.setBaseDefinition(myResourceTypeDefinition)
			.setType(myResourceType)
			.setDerivation(StructureDefinition.TypeDerivationRule.CONSTRAINT);
		sd.getDifferential().addElement()
			.setPath(thePath)
			.setMin(1)
			.setId(thePath);

		DaoMethodOutcome outcome = myStructureDefinitionDao.create(sd, mySrd);
		assertTrue(outcome.getCreated());
		ourValidationSupport.addStructureDefinition(sd);
	}
}
