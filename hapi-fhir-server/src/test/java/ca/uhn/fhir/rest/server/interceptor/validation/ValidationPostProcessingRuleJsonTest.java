package ca.uhn.fhir.rest.server.interceptor.validation;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ValidationPostProcessingRuleJsonTest {

	ObjectMapper myObjectMapper = new ObjectMapper();

	@Test
	void requiresOneOfMsgIdAndmsgIdRegex() {
		String ruleStr = """
		{
			"newSeverity": "FATAL"
		}
		""";

		assertThatThrownBy(() -> myObjectMapper.readValue(ruleStr, ValidationPostProcessingRuleJson.class))
			.isInstanceOf(ValueInstantiationException.class)
			.hasMessageContaining(Msg.code(2705) + "One of 'msgId' and 'msgIdRegex' must be present");
	}


	@Test
	void acceptOnlyOneOfMsgIdAndmsgIdRegex () {
		String ruleStr = """
		{
			"msgId": "Terminology_TX_Error_CodeableConcept",
			"msgIdRegex": "Terminology_TX_Error.*",
			"newSeverity": "FATAL"
		}
		""";

		assertThatThrownBy(() -> myObjectMapper.readValue(ruleStr, ValidationPostProcessingRuleJson.class))
			.isInstanceOf(ValueInstantiationException.class)
			.hasMessageContaining(Msg.code(2706) + "Only one of 'msgId' and 'msgIdRegex' must be present");
	}

	@Test
	void requiresNewSeverity () {
		String ruleStr = """
		{
			"msgId": "Terminology_TX_Error_CodeableConcept"
		}
		""";

		assertThatThrownBy(() -> myObjectMapper.readValue(ruleStr, ValidationPostProcessingRuleJson.class))
			.isInstanceOf(ValueInstantiationException.class)
			.hasMessageContaining(Msg.code(2707) + "Parameter 'newSeverity' must be present");
	}

	@Test
	void oldSeveritiesSetsCollection() throws JsonProcessingException {
		String ruleStr = """
		{
			"msgIdRegex": "Terminology_TX_Error.*",
			"oldSeverities": ["INFORMATION", "WARNING"],
			"newSeverity": "FATAL"
		}
		""";

		ValidationPostProcessingRuleJson ruleJson =
			myObjectMapper.readValue(ruleStr, ValidationPostProcessingRuleJson.class);

		assertThat(ruleJson.getOldSeverities()).contains(ResultSeverityEnum.INFORMATION, ResultSeverityEnum.WARNING);
	}

	@Test
	void absentOldSeveritiesSetsEmptyCollection() throws JsonProcessingException {
		String ruleStr = """
		{
			"msgIdRegex": "Terminology_TX_Error.*",
			"newSeverity": "FATAL"
		}
		""";

		ValidationPostProcessingRuleJson ruleJson =
			myObjectMapper.readValue(ruleStr, ValidationPostProcessingRuleJson.class);

		assertThat(ruleJson.getOldSeverities()).isNotNull().isEmpty();
	}

	@Test
	void messageFragmentsSetsCollection() throws JsonProcessingException {
		String ruleStr = """
		{
			"msgIdRegex": "Terminology_TX_Error.*",
			"messageFragments": ["msg-fragment-1", "msg-fragment-2"],
			"newSeverity": "FATAL"
		}
		""";

		ValidationPostProcessingRuleJson ruleJson =
			myObjectMapper.readValue(ruleStr, ValidationPostProcessingRuleJson.class);

		assertThat(ruleJson.getMessageFragments()).contains("msg-fragment-1", "msg-fragment-2");
	}

	@Test
	void absentMessageFragmentsSetsEmptyCollection() throws JsonProcessingException {
		String ruleStr = """
		{
			"msgIdRegex": "Terminology_TX_Error.*",
			"newSeverity": "FATAL"
		}
		""";

		ValidationPostProcessingRuleJson ruleJson =
			myObjectMapper.readValue(ruleStr, ValidationPostProcessingRuleJson.class);

		assertThat(ruleJson.getMessageFragments()).isNotNull().isEmpty();
	}

	@Test
	void msgIdRegexGetsCompiledAsPattern() throws JsonProcessingException {
		String ruleStr = """
		{
			"msgIdRegex": "Terminology_TX.*",
			"newSeverity": "FATAL"
		}
		""";

		ValidationPostProcessingRuleJson ruleJson =
			myObjectMapper.readValue(ruleStr, ValidationPostProcessingRuleJson.class);

		assertThat(ruleJson.getMsgIdRegexPattern())
			.isNotNull()
			.returns("Terminology_TX.*", Pattern::toString);
	}
}
