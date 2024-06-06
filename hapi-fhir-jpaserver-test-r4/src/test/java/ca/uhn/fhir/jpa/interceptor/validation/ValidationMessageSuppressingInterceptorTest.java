package ca.uhn.fhir.jpa.interceptor.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.rest.server.interceptor.RequestValidatingInterceptor;
import ca.uhn.fhir.rest.server.interceptor.validation.ValidationMessageSuppressingInterceptor;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r5.utils.validation.constants.BestPracticeWarningLevel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

public class ValidationMessageSuppressingInterceptorTest extends BaseResourceProviderR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ValidationMessageSuppressingInterceptorTest.class);
	@Autowired
	private ApplicationContext myApplicationContext;
	@Autowired
	private IValidationSupport myValidationSupport;

	@Override
	@AfterEach
	public void after() throws Exception {
		super.after();
		myInterceptorRegistry.unregisterInterceptorsIf(t -> t instanceof RepositoryValidatingInterceptor);
		myInterceptorRegistry.unregisterInterceptorsIf(t -> t instanceof ValidationMessageSuppressingInterceptor);
	}

	@Test
	public void testDaoValidation() throws IOException {
		upload("/r4/uscore/CodeSystem-dummy-loinc.json");
		upload("/r4/uscore/StructureDefinition-us-core-pulse-oximetry.json");

		String input = loadResource("/r4/uscore/observation-pulseox.json");
		Observation inputObs = loadResource(myFhirContext, Observation.class, "/r4/uscore/observation-pulseox.json");

		MethodOutcome result = myObservationDao.validate(inputObs, null, input, null, null, null, null);


		ValidationMessageSuppressingInterceptor interceptor = new ValidationMessageSuppressingInterceptor();
		interceptor.addMessageSuppressionPatterns("Unknown code 'http://loinc.org#59408-5'");
		myInterceptorRegistry.registerInterceptor(interceptor);

		MethodOutcome validationOutcome = myObservationDao.validate(inputObs, null, input, null, null, null, null);
		OperationOutcome oo = (OperationOutcome) validationOutcome.getOperationOutcome();
		assertHasWarnings(oo);
		String encode = encode(oo);
		ourLog.info(encode);
		assertThat(encode).contains("In general, all observations should have a performer");
	}

	@Test
	public void testRequestValidatingInterceptor() throws IOException {
		createPatient(withActiveTrue(), withId("AmyBaxter"));
		upload("/r4/uscore/CodeSystem-dummy-loinc.json");
		upload("/r4/uscore/StructureDefinition-us-core-pulse-oximetry.json");

		FhirValidator validator = myFhirContext.newValidator();
		validator.setInterceptorBroadcaster(myInterceptorRegistry);
		validator.registerValidatorModule(new FhirInstanceValidator(myValidationSupport));

		RequestValidatingInterceptor requestInterceptor = new RequestValidatingInterceptor();
		requestInterceptor.setFailOnSeverity(ResultSeverityEnum.ERROR);
		requestInterceptor.setValidator(validator);
		myServer.registerInterceptor(requestInterceptor);


		// Without suppression
		{
			Observation inputObs = loadResource(myFhirContext, Observation.class, "/r4/uscore/observation-pulseox.json");
			try {
				myClient.create().resource(inputObs).execute().getId().toUnqualifiedVersionless().getValue();
				fail();
			} catch (UnprocessableEntityException e) {
				String encode = encode(e.getOperationOutcome());
				ourLog.info(encode);
				assertThat(encode).contains("Slice 'Observation.code.coding:PulseOx': a matching slice is required, but not found");
			}
		}

		// With suppression
		ValidationMessageSuppressingInterceptor interceptor = new ValidationMessageSuppressingInterceptor();
		interceptor.addMessageSuppressionPatterns("Unable to validate code http://loinc.org#not-a-real-code - Code is not found in CodeSystem: http://loinc.org",
		"Slice 'Observation.code.coding:PulseOx': a matching slice is required, but not found (from http://hl7.org/fhir/us/core/StructureDefinition/us-core-pulse-oximetry|3.1.1)",
		"This element does not match any known slice defined in the profile http://hl7.org/fhir/us/core/StructureDefinition/us-core-pulse-oximetry|3.1.1");

		myInterceptorRegistry.registerInterceptor(interceptor);
		{
			Observation inputObs = loadResource(myFhirContext, Observation.class, "/r4/uscore/observation-pulseox.json");
			String id = myClient.create().resource(inputObs).execute().getId().toUnqualifiedVersionless().getValue();
			assertThat(id).matches("Observation/[0-9]+");
		}
	}

	@Test
	public void testRepositoryValidation() {
		createPatient(withActiveTrue(), withId("A"));

		List<IRepositoryValidatingRule> rules = myApplicationContext.getBean(RepositoryValidatingRuleBuilder.REPOSITORY_VALIDATING_RULE_BUILDER, RepositoryValidatingRuleBuilder.class)
			.forResourcesOfType("Encounter")
			.requireValidationToDeclaredProfiles().withBestPracticeWarningLevel(BestPracticeWarningLevel.Ignore)
			.build();

		RepositoryValidatingInterceptor repositoryValidatingInterceptor = new RepositoryValidatingInterceptor();
		repositoryValidatingInterceptor.setFhirContext(myFhirContext);
		repositoryValidatingInterceptor.setRules(rules);
		myInterceptorRegistry.registerInterceptor(repositoryValidatingInterceptor);

		// Without suppression
		try {
			Encounter encounter = new Encounter();
			encounter.setSubject(new Reference("Patient/A"));
			IIdType id = myEncounterDao.create(encounter).getId();
			assertEquals("1", id.getVersionIdPart());
			fail();
		} catch (PreconditionFailedException e) {
			String encode = encode(e.getOperationOutcome());
			ourLog.info(encode);
			assertThat(encode).contains("Encounter.status: minimum required = 1");
		}

		// With suppression
		ValidationMessageSuppressingInterceptor interceptor = new ValidationMessageSuppressingInterceptor();
		interceptor.addMessageSuppressionPatterns("Encounter.status");
		interceptor.addMessageSuppressionPatterns("Encounter.class");
		myInterceptorRegistry.registerInterceptor(interceptor);

		Encounter encounter = new Encounter();
		encounter.setSubject(new Reference("Patient/A"));
		IIdType id = myEncounterDao.create(encounter).getId().toUnqualifiedVersionless();
		assertThat(id.getValue()).matches("Encounter/[0-9]+");

	}


}
