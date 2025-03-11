/*-
 * #%L
 * HAPI FHIR Subscription Server
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
package ca.uhn.fhir.jpa.subscription.submit.interceptor.validator;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import jakarta.annotation.Nonnull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class RegexEndpointUrlValidationStrategy implements RestHookChannelValidator.IEndpointUrlValidationStrategy {

	private final Pattern myEndpointUrlValidationPattern;

	public RegexEndpointUrlValidationStrategy(@Nonnull String theEndpointUrlValidationRegex) {
		try {
			myEndpointUrlValidationPattern = Pattern.compile(theEndpointUrlValidationRegex);
		} catch (PatternSyntaxException e) {
			throw new IllegalArgumentException(
					Msg.code(2546) + " invalid synthax for provided regex " + theEndpointUrlValidationRegex);
		}
	}

	@Override
	public void validateEndpointUrl(String theEndpointUrl) {
		Matcher matcher = myEndpointUrlValidationPattern.matcher(theEndpointUrl);

		if (!matcher.matches()) {
			throw new UnprocessableEntityException(
					Msg.code(2545) + "Failed validation for endpoint URL: " + theEndpointUrl);
		}
	}
}
