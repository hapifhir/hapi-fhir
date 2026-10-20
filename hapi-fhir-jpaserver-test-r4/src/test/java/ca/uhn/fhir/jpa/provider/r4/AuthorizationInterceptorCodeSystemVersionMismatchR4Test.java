package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.context.support.CodeSystemVersionMismatchPolicy;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor;
import ca.uhn.fhir.rest.server.interceptor.auth.IAuthRule;
import ca.uhn.fhir.rest.server.interceptor.auth.PolicyEnum;
import ca.uhn.fhir.rest.server.interceptor.auth.RuleBuilder;
import org.hl7.fhir.common.hapi.validation.support.InMemoryTerminologyServerValidationSupport;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * {@code AuthorizationInterceptor} rules using {@code withCodeInValueSet} over a ValueSet that names a CodeSystem
 * version the server does not have, while version 1.0.0 is installed holding code {@code A}.
 */
// Created by Claude Opus 5.5
class AuthorizationInterceptorCodeSystemVersionMismatchR4Test extends BaseResourceProviderR4Test {
	private static final String CS_URL = "http://example.org/CodeSystem/pinned";
	private static final String VS_URL = "http://example.org/ValueSet/pinned";

	@Autowired
	private InMemoryTerminologyServerValidationSupport myInMemoryTerminologyServerValidationSupport;

	@BeforeEach
	void beforeCreateTerminologyAndObservations() {
		CodeSystem cs = new CodeSystem();
		cs.setUrl(CS_URL);
		cs.setVersion("1.0.0");
		cs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		cs.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		cs.addConcept().setCode("A");
		myCodeSystemDao.create(cs, mySrd);

		ValueSet vs = new ValueSet();
		vs.setUrl(VS_URL);
		vs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		ValueSet.ConceptSetComponent include = vs.getCompose().addInclude().setSystem(CS_URL).setVersion("0.0.1");
		include.addConcept().setCode("A");
		include.addConcept().setCode("B");
		myValueSetDao.create(vs, mySrd);

		createObservation(withId("coded-a"), withObservationCode(CS_URL, "A"));
		createObservation(withId("coded-b"), withObservationCode(CS_URL, "B"));
		myBatch2JobHelper.awaitNoJobsRunning();

		myServer.getRestfulServer().registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
						.allow()
						.read()
						.resourcesOfType("Observation")
						.withCodeInValueSet("code", VS_URL)
						.andThen()
						.build();
			}
		}.setValidationSupport(myValidationSupport));
	}

	@AfterEach
	void afterResetPolicy() {
		myInterceptorRegistry.unregisterInterceptorsIf(t -> t instanceof AuthorizationInterceptor);
		setCodeSystemVersionMismatchPolicy(new JpaStorageSettings().getCodeSystemVersionMismatchPolicy());
	}

	@Test
	void read_defaultPolicyAndCodeInInstalledVersion_isDenied() {
		assertThatThrownBy(() -> myClient.read()
						.resource(Observation.class)
						.withId("Observation/coded-a")
						.execute())
				.isInstanceOf(ForbiddenOperationException.class);
	}

	@Test
	void read_useCurrentVersionAndCodeInInstalledVersion_isAllowed() {
		setCodeSystemVersionMismatchPolicy(CodeSystemVersionMismatchPolicy.USE_CURRENT_VERSION);

		Observation result = myClient.read()
				.resource(Observation.class)
				.withId("Observation/coded-a")
				.execute();

		assertThat(result).isNotNull();
	}

	@Test
	void read_useCurrentVersionAndCodeOnlyListed_isDenied() {
		setCodeSystemVersionMismatchPolicy(CodeSystemVersionMismatchPolicy.USE_CURRENT_VERSION);

		assertThatThrownBy(() -> myClient.read()
						.resource(Observation.class)
						.withId("Observation/coded-b")
						.execute())
				.isInstanceOf(ForbiddenOperationException.class);
	}

	private void setCodeSystemVersionMismatchPolicy(CodeSystemVersionMismatchPolicy thePolicy) {
		myStorageSettings.setCodeSystemVersionMismatchPolicy(thePolicy);
		myInMemoryTerminologyServerValidationSupport.setCodeSystemVersionMismatchPolicy(thePolicy);
	}
}
