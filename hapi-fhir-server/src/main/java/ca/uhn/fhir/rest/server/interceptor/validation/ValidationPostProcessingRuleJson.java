package ca.uhn.fhir.rest.server.interceptor.validation;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.collections4.CollectionUtils;

import java.security.InvalidParameterException;
import java.util.Collection;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Specification of ValidationResult matching parameters and severity to be applied in case of match
 */
public class ValidationPostProcessingRuleJson implements IModelJson {

	public ValidationPostProcessingRuleJson(
			String theMsgId,
			String theMsgRegex,
			Collection<ResultSeverityEnum> theOldSeverities,
			Collection<String> theExtraMessageFragments,
			ResultSeverityEnum theNewSeverity) {

		validateParams(theMsgId, theMsgRegex, theNewSeverity);

		myMsgId = theMsgId;
		if (isNotBlank(theMsgRegex)) {
			myMsgRegexPattern = Pattern.compile(theMsgRegex, Pattern.CASE_INSENSITIVE);
		}
		myOldSeverities = CollectionUtils.emptyIfNull(theOldSeverities);
		myNewSeverity = theNewSeverity;
		myMessageFragments = CollectionUtils.emptyIfNull(theExtraMessageFragments);
	}

	private void validateParams(String theMsgId, String theMsgRegex, ResultSeverityEnum theNewSeverity) {

		if (isBlank(theMsgId) && isBlank(theMsgRegex)) {
			throw new InvalidParameterException(Msg.code(2705) + "One of 'msgId' and 'msgRegex' must be present");
		}

		if (isNotBlank(theMsgId) && isNotBlank(theMsgRegex)) {
			throw new InvalidParameterException(Msg.code(2706) + "Only one of 'msgId' and 'msgRegex' must be present");
		}

		if (theNewSeverity == null) {
			throw new InvalidParameterException(Msg.code(2707) + "Parameter 'newSeverity' must be present");
		}
	}

	@JsonProperty("msgId")
	private String myMsgId;

	private Pattern myMsgRegexPattern;

	@JsonProperty(value = "oldSeverities", required = true)
	private Collection<ResultSeverityEnum> myOldSeverities;

	@JsonProperty("messageFragments")
	private Collection<String> myMessageFragments;

	@JsonProperty(value = "newSeverity", required = true)
	private ResultSeverityEnum myNewSeverity;

	public String getMsgId() {
		return myMsgId;
	}

	public ResultSeverityEnum getNewSeverity() {
		return myNewSeverity;
	}

	public Collection<ResultSeverityEnum> getOldSeverities() {
		return myOldSeverities;
	}

	public Collection<String> getMessageFragments() {
		return myMessageFragments;
	}

	public Pattern getMsgRegexPattern() {
		return myMsgRegexPattern;
	}
}
