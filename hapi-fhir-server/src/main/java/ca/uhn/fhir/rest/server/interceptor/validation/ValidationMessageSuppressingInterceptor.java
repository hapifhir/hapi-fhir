package ca.uhn.fhir.rest.server.interceptor.validation;

/*-
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.validation.SingleValidationMessage;
import ca.uhn.fhir.validation.ValidationResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Interceptor
public class ValidationMessageSuppressingInterceptor {

	private List<Pattern> mySuppressPatterns = new ArrayList<>();

	/**
	 * Constructor
	 */
	public ValidationMessageSuppressingInterceptor() {
		super();
	}

	/**
	 * Supplies one or more patterns to suppress. Any validation messages (of any severity) will be suppressed
	 * if they match this pattern. Patterns are in Java Regular Expression format (as defined by the {@link Pattern} class)
	 * and are treated as partial maches. They are also case insensitive.
	 * <p>
	 *    For example, a pattern of <code>loinc.*1234</code> would suppress the following message:<br/>
	 *    <code>The LOINC code 1234 is not valid</code>
	 * </p>
	 */
	public ValidationMessageSuppressingInterceptor addMessageSuppressionPatterns(String... thePatterns) {
		return addMessageSuppressionPatterns(Arrays.asList(thePatterns));
	}

	/**
	 * Supplies one or more patterns to suppress. Any validation messages (of any severity) will be suppressed
	 * if they match this pattern. Patterns are in Java Regular Expression format (as defined by the {@link Pattern} class)
	 * and are treated as partial maches. They are also case insensitive.
	 * <p>
	 *    For example, a pattern of <code>loinc.*1234</code> would suppress the following message:<br/>
	 *    <code>The LOINC code 1234 is not valid</code>
	 * </p>
	 */
	public ValidationMessageSuppressingInterceptor addMessageSuppressionPatterns(List<String> thePatterns) {
		for (String next : thePatterns) {
			if (isNotBlank(next)) {
				Pattern pattern = Pattern.compile(next, Pattern.CASE_INSENSITIVE);
				mySuppressPatterns.add(pattern);
			}
		}
		return this;
	}


	@Hook(Pointcut.VALIDATION_COMPLETED)
	public ValidationResult handle(ValidationResult theResult) {

		List<SingleValidationMessage> newMessages = new ArrayList<>(theResult.getMessages().size());
		for (SingleValidationMessage next : theResult.getMessages()) {

			String nextMessage = next.getMessage();
			boolean suppress = false;
			for (Pattern nextSuppressPattern : mySuppressPatterns) {
				if (nextSuppressPattern.matcher(nextMessage).find()) {
					suppress = true;
					break;
				}
			}

			if (!suppress) {
				newMessages.add(next);
			}
		}

		if (newMessages.size() == theResult.getMessages().size()) {
			return null;
		}

		return new ValidationResult(theResult.getContext(), newMessages);
	}

}
