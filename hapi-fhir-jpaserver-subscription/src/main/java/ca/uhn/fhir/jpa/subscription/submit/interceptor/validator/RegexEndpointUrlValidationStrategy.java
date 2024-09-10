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
