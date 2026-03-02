package ca.uhn.fhir.jpa.validation;

import org.hl7.fhir.r5.utils.validation.constants.ReferenceValidationPolicy;
import org.hl7.fhir.utilities.i18n.I18nConstants;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

// Created by claude-opus-4-6
class ValidatorPolicyAdvisorTest {

	@Test
	void testIsSuppressMessageId_withoutSpringContext_shouldNotThrowNpe() {
		// Given: a ValidatorPolicyAdvisor created without Spring (myValidationSettings is null)
		ValidatorPolicyAdvisor myAdvisor = new ValidatorPolicyAdvisor();

		// When/Then: calling isSuppressMessageId should return false, not throw NPE
		boolean myResult = myAdvisor.isSuppressMessageId("Patient", "some_message_id");

		assertThat(myResult).isFalse();
	}

	@Test
	void testIsSuppressMessageId_withIgnorePolicy_shouldSuppressCantMatchChoice() {
		// Given: a ValidatorPolicyAdvisor with ValidationSettings configured to IGNORE policy
		ValidatorPolicyAdvisor myAdvisor = new ValidatorPolicyAdvisor();
		ValidationSettings mySettings = new ValidationSettings();
		mySettings.setLocalReferenceValidationDefaultPolicy(ReferenceValidationPolicy.IGNORE);
		ReflectionTestUtils.setField(myAdvisor, "myValidationSettings", mySettings);

		// When: calling isSuppressMessageId with the REFERENCE_REF_CANTMATCHCHOICE message ID
		boolean myResult = myAdvisor.isSuppressMessageId("Patient", I18nConstants.REFERENCE_REF_CANTMATCHCHOICE);

		// Then: should return true because IGNORE policy means we suppress reference match errors
		assertThat(myResult).isTrue();
	}
}
