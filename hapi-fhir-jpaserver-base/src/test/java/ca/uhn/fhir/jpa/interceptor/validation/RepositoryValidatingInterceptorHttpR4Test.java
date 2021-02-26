package ca.uhn.fhir.jpa.interceptor.validation;

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.jpa.config.BaseConfig;
import ca.uhn.fhir.jpa.dao.r4.BaseJpaR4Test;
import ca.uhn.fhir.jpa.rp.r4.ObservationResourceProvider;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.PreferReturnEnum;
import ca.uhn.fhir.rest.server.interceptor.ValidationResultEnrichingInterceptor;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import org.hl7.fhir.r4.model.Observation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

public class RepositoryValidatingInterceptorHttpR4Test extends BaseJpaR4Test {

	private static final Logger ourLog = LoggerFactory.getLogger(RepositoryValidatingInterceptorHttpR4Test.class);
	@Autowired
	protected ObservationResourceProvider myObservationResourceProvider;
	private RepositoryValidatingInterceptor myValInterceptor;
	@RegisterExtension
	protected RestfulServerExtension myRestfulServerExtension = new RestfulServerExtension(FhirVersionEnum.R4);
	@Autowired
	private ApplicationContext myApplicationContext;

	@BeforeEach
	public void before() {
		myValInterceptor = new RepositoryValidatingInterceptor();
		myValInterceptor.setFhirContext(myFhirCtx);
		myInterceptorRegistry.registerInterceptor(myValInterceptor);

		myRestfulServerExtension.getRestfulServer().registerProvider(myObservationResourceProvider);
		myRestfulServerExtension.getRestfulServer().getInterceptorService().registerInterceptor(new ValidationResultEnrichingInterceptor());
	}

	@AfterEach
	public void after() {
		myInterceptorRegistry.unregisterInterceptorsIf(t -> t instanceof RepositoryValidatingInterceptor);
	}

	@Test
	public void testValidationOutcomeAddedToRequestResponse() {
		List<IRepositoryValidatingRule> rules = newRuleBuilder()
			.forResourcesOfType("Observation")
			.requireValidationToDeclaredProfiles()
			.withBestPracticeWarningLevel("WARNING")
			.build();
		myValInterceptor.setRules(rules);

		Observation obs = new Observation();
		obs.getCode().addCoding().setSystem("http://foo").setCode("123").setDisplay("help im a bug");
		obs.setStatus(Observation.ObservationStatus.AMENDED);

		MethodOutcome outcome = myRestfulServerExtension
			.getFhirClient()
			.create()
			.resource(obs)
			.prefer(PreferReturnEnum.OPERATION_OUTCOME)
			.execute();

		String operationOutcomeEncoded = myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome.getOperationOutcome());
		ourLog.info("Outcome: {}", operationOutcomeEncoded);
		assertThat(operationOutcomeEncoded, containsString("All observations should have a subject"));

	}

	private RepositoryValidatingRuleBuilder newRuleBuilder() {
		return myApplicationContext.getBean(BaseConfig.REPOSITORY_VALIDATING_RULE_BUILDER, RepositoryValidatingRuleBuilder.class);
	}

}
