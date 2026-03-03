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
		ValidatorPolicyAdvisor advisor = new ValidatorPolicyAdvisor();

		// When/Then: calling isSuppressMessageId should return false, not throw NPE
		boolean result = advisor.isSuppressMessageId("Patient", "some_message_id");

		assertThat(result).isFalse();
	}

	@Test
	void testIsSuppressMessageId_withIgnorePolicy_shouldSuppressCantMatchChoice() {
		// Given: a ValidatorPolicyAdvisor with ValidationSettings configured to IGNORE policy
		ValidatorPolicyAdvisor advisor = new ValidatorPolicyAdvisor();
		ValidationSettings settings = new ValidationSettings();
		settings.setLocalReferenceValidationDefaultPolicy(ReferenceValidationPolicy.IGNORE);
		ReflectionTestUtils.setField(advisor, "myValidationSettings", settings);

		// When: calling isSuppressMessageId with the REFERENCE_REF_CANTMATCHCHOICE message ID
		boolean result = advisor.isSuppressMessageId("Patient", I18nConstants.REFERENCE_REF_CANTMATCHCHOICE);

		// Then: should return true because IGNORE policy means we suppress reference match errors
		assertThat(result).isTrue();
	}

	@Test
	void testIsSuppressMessageId_withCheckValidPolicy_shouldNotSuppressCantMatchChoice() {
		// Given: a ValidatorPolicyAdvisor with ValidationSettings configured to CHECK_VALID policy
		ValidatorPolicyAdvisor advisor = new ValidatorPolicyAdvisor();
		ValidationSettings settings = new ValidationSettings();
		settings.setLocalReferenceValidationDefaultPolicy(ReferenceValidationPolicy.CHECK_VALID);
		ReflectionTestUtils.setField(advisor, "myValidationSettings", settings);

		// When: calling isSuppressMessageId with the REFERENCE_REF_CANTMATCHCHOICE message ID
		boolean result = advisor.isSuppressMessageId("Patient", I18nConstants.REFERENCE_REF_CANTMATCHCHOICE);

		// Then: should return false because CHECK_VALID policy means we DO validate references
		assertThat(result).isFalse();
	}
}
