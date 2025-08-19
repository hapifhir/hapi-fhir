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

import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.validation.ResultSeverityEnum;

import java.util.List;

/**
 * This interceptor is for changing the severity of the Unknown Code System validation issues to a desired severity
 * after the validation has been performed.
 */
public class ValidationMessageUnknownCodeSystemPostProcessingInterceptor
		extends ValidationMessagePostProcessingInterceptor {

	public ValidationMessageUnknownCodeSystemPostProcessingInterceptor(
			IValidationSupport.IssueSeverity theDesiredSeverityForUnknownCodeSystem) {

		ResultSeverityEnum desiredResultSeverity =
				mapIssueSeverityToResultSeverityEnum(theDesiredSeverityForUnknownCodeSystem);

		super.addPostProcessingPatterns(
				// this is an unknown code system error produced by the validator
				new ValidationPostProcessingRuleJson(
						"Terminology_TX_System_Unknown",
						null,
						List.of(ResultSeverityEnum.ERROR, ResultSeverityEnum.WARNING, ResultSeverityEnum.INFORMATION),
						List.of("Unknown Code System"),
						desiredResultSeverity),
				// this is for the unknown code system error produced by HAPI-FHIR validation
				new ValidationPostProcessingRuleJson(
						"Terminology_PassThrough_TX_Message",
						null,
						List.of(ResultSeverityEnum.ERROR, ResultSeverityEnum.WARNING, ResultSeverityEnum.INFORMATION),
						List.of("CodeSystem is unknown and can't be validated"),
						desiredResultSeverity),
				// This is a related error that is caused by unknown code systems, the issue message is produced by the
				// HAPI-FHIR validator.
				// In some cases, the core validator produces additional related issue with id Terminology_TX_NoValid_12
				// and including the HAPI-FHIR's issue message.
				// This rule covers both messages.
				new ValidationPostProcessingRuleJson(
						null,
						"Terminology_PassThrough_TX_Message|Terminology_TX_NoValid_12",
						List.of(ResultSeverityEnum.ERROR, ResultSeverityEnum.WARNING, ResultSeverityEnum.INFORMATION),
						List.of("Unable to expand ValueSet because CodeSystem could not be found"),
						desiredResultSeverity));
	}

	private static ResultSeverityEnum mapIssueSeverityToResultSeverityEnum(
			IValidationSupport.IssueSeverity theIssueSeverity) {
		return switch (theIssueSeverity) {
			case FATAL -> ResultSeverityEnum.FATAL;
			case ERROR -> ResultSeverityEnum.ERROR;
			case WARNING -> ResultSeverityEnum.WARNING;
				// treat success as information level
			case INFORMATION, SUCCESS -> ResultSeverityEnum.INFORMATION;
		};
	}
}
