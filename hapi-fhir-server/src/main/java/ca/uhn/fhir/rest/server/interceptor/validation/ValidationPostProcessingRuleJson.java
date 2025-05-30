/*-
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.rest.server.interceptor.validation;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
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

	@JsonCreator
	public ValidationPostProcessingRuleJson(
			@JsonProperty("msgId") String theMsgId,
			@JsonProperty("msgIdRegex") String themsgIdRegex,
			@JsonProperty(value = "oldSeverities") Collection<ResultSeverityEnum> theOldSeverities,
			@JsonProperty("messageFragments") Collection<String> theExtraMessageFragments,
			@JsonProperty(value = "newSeverity") ResultSeverityEnum theNewSeverity) {

		validateParams(theMsgId, themsgIdRegex, theNewSeverity);

		myMsgId = theMsgId;
		if (isNotBlank(themsgIdRegex)) {
			myMsgIdRegexPattern = Pattern.compile(themsgIdRegex, Pattern.CASE_INSENSITIVE);
		}
		myOldSeverities = CollectionUtils.emptyIfNull(theOldSeverities);
		myNewSeverity = theNewSeverity;
		myMessageFragments = CollectionUtils.emptyIfNull(theExtraMessageFragments);
	}

	private void validateParams(String theMsgId, String themsgIdRegex, ResultSeverityEnum theNewSeverity) {

		if (isBlank(theMsgId) && isBlank(themsgIdRegex)) {
			throw new InvalidParameterException(Msg.code(2705) + "One of 'msgId' and 'msgIdRegex' must be present");
		}

		if (isNotBlank(theMsgId) && isNotBlank(themsgIdRegex)) {
			throw new InvalidParameterException(
					Msg.code(2706) + "Only one of 'msgId' and 'msgIdRegex' must be present");
		}

		if (theNewSeverity == null) {
			throw new InvalidParameterException(Msg.code(2707) + "Parameter 'newSeverity' must be present");
		}
	}

	@JsonProperty("msgId")
	private String myMsgId;

	private Pattern myMsgIdRegexPattern;

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

	public Pattern getMsgIdRegexPattern() {
		return myMsgIdRegexPattern;
	}
}
