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

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import ca.uhn.fhir.validation.SingleValidationMessage;
import ca.uhn.fhir.validation.ValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Interceptor
public class ValidationMessagePostProcessingInterceptor {

	private final Logger ourLog = LoggerFactory.getLogger(ValidationMessagePostProcessingInterceptor.class);

	private final List<Rule> myRules = new ArrayList<>();

	/**
	 * Supplies one or more message definitions to post-process.
	 * Validation messages matching defined 'msgId' or 'msgRegex', 'oldSeverity' and (optionally) case-insensitive
	 * 'diagnosticsFragments' matching fragments, will have their severity replaced by the defined 'newSeverity'.
	 *
	 * @param theJsonDefinitions ValidationPostProcessingRuleJson rules
	 */
	public ValidationMessagePostProcessingInterceptor addPostProcessingPatterns(
			ValidationPostProcessingRuleJson... theJsonDefinitions) {
		return addPostProcessingPatterns(Arrays.asList(theJsonDefinitions));
	}

	/**
	 * Supplies one or more message definitions to post-process.
	 * Validation messages matching defined 'msgId' or 'msgRegex', 'oldSeverity' and (optionally) case-insensitive
	 * 'diagnosticsFragments' matching fragments, will have their severity replaced by the defined 'newSeverity'.
	 *
	 * @param theJsonDefinitions list of ValidationPostProcessingRuleJson rules
	 */
	public ValidationMessagePostProcessingInterceptor addPostProcessingPatterns(
			List<ValidationPostProcessingRuleJson> theJsonDefinitions) {
		myRules.addAll(theJsonDefinitions.stream().map(Rule::of).toList());
		return this;
	}

	@Hook(Pointcut.VALIDATION_COMPLETED)
	public ValidationResult handle(ValidationResult theResult) {
		List<SingleValidationMessage> newMessages =
				new ArrayList<>(theResult.getMessages().size());

		for (SingleValidationMessage inputMessage : theResult.getMessages()) {
			Optional<Rule> firstMatchedDefinitionOpt = findFirstMatchedDefinition(inputMessage);
			ourLog.atDebug()
					.setMessage("matching result: {}")
					.addArgument(firstMatchedDefinitionOpt
							.map(theRule -> "matched rule: " + theRule)
							.orElse("no rule matched"))
					.log();

			firstMatchedDefinitionOpt.ifPresent(
					theMatchedRule -> inputMessage.setSeverity(theMatchedRule.newSeverity()));

			newMessages.add(inputMessage);
		}

		return new ValidationResult(theResult.getContext(), newMessages);
	}

	private Optional<Rule> findFirstMatchedDefinition(SingleValidationMessage theMessage) {
		return myRules.stream()
				.filter(rule -> matchesMessageId(theMessage.getMessageId(), rule))
				.filter(rule -> rule.oldSeverities.contains(theMessage.getSeverity()))
				.filter(rule -> stringContainsAll(theMessage.getMessage(), rule.diagnosticFragmentsToMatch()))
				.findFirst();
	}

	private boolean matchesMessageId(String theMessageId, Rule theRule) {
		boolean matched = (theRule.messageId() != null && theRule.messageId.equals(theMessageId))
				|| (theRule.messagePattern != null
						&& theRule.messagePattern.matcher(theMessageId).matches());

		ourLog.atTrace()
				.setMessage("messageId match result: {} - input messageId: {} - matching rule: {}")
				.addArgument(matched)
				.addArgument(theMessageId)
				.addArgument(theRule)
				.log();

		return matched;
	}

	private boolean stringContainsAll(String theMessage, Collection<String> theMatchingFragments) {
		return theMatchingFragments.stream().allMatch(theMessage::contains);
	}

	public record Rule(
			String messageId,
			Pattern messagePattern,
			Collection<ResultSeverityEnum> oldSeverities,
			Collection<String> diagnosticFragmentsToMatch,
			ResultSeverityEnum newSeverity) {

		public static Rule of(ValidationPostProcessingRuleJson theParamsDefinitionJson) {
			return new Rule(
					theParamsDefinitionJson.getMsgId(),
					theParamsDefinitionJson.getMsgRegexPattern(),
					theParamsDefinitionJson.getOldSeverities(),
					theParamsDefinitionJson.getMessageFragments(),
					theParamsDefinitionJson.getNewSeverity());
		}
	}
}
