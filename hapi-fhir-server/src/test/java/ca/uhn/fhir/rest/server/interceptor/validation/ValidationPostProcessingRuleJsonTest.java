package ca.uhn.fhir.rest.server.interceptor.validation;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import org.junit.jupiter.api.Test;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ValidationPostProcessingRuleJsonTest {

	@Test
	void requiresOneOfMsgIdAndMsgRegex () {
		assertThatThrownBy(() -> new ValidationPostProcessingRuleJson(
				null,
				null,
				List.of(ResultSeverityEnum.ERROR),
				null,
				ResultSeverityEnum.FATAL))
			.isInstanceOf(InvalidParameterException.class)
			.hasMessage(Msg.code(2705) + "One of 'msgId' and 'msgRegex' must be present");
	}

	@Test
	void acceptOnlyOneOfMsgIdAndMsgRegex () {
		assertThatThrownBy(() -> new ValidationPostProcessingRuleJson(
				"Terminology_TX_Error_CodeableConcept",
				"Terminology_TX*",
				null,
				null,
				ResultSeverityEnum.FATAL))
			.isInstanceOf(InvalidParameterException.class)
			.hasMessage(Msg.code(2706) + "Only one of 'msgId' and 'msgRegex' must be present");
	}

	@Test
	void requiresNewSeverity () {
		assertThatThrownBy(() -> new ValidationPostProcessingRuleJson(
				null,
				"Terminology_TX*",
				null,
				null,
				null))
			.isInstanceOf(InvalidParameterException.class)
			.hasMessage(Msg.code(2707) + "Parameter 'newSeverity' must be present");
	}

	@Test
	void nullOldSeveritiesSetsEmptyCollection() {
		ValidationPostProcessingRuleJson rule = new ValidationPostProcessingRuleJson(
			"msg-id",
			null,
			null,
			null,
			ResultSeverityEnum.ERROR);

		assertThat(rule.getOldSeverities()).isEmpty();
	}

	@Test
	void nullMessageFragmentsSetsEmptyCollection() {
		ValidationPostProcessingRuleJson rule = new ValidationPostProcessingRuleJson(
			"msg-id",
			null,
			null,
			null,
			ResultSeverityEnum.FATAL);

		assertThat(rule.getMessageFragments()).isEmpty();
	}

	@Test
	void msgRegexGetsCompiledAsPattern() {
		ValidationPostProcessingRuleJson rule = new ValidationPostProcessingRuleJson(
			null,
			"Terminology_TX.*",
			null,
			null,
			ResultSeverityEnum.FATAL);

		assertThat(rule.getMsgRegexPattern()).returns("Terminology_TX.*", Pattern::toString);
	}
}
