package ca.uhn.fhir.jpa.subscription.submit.interceptor.validator;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.model.config.SubscriptionSettings;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionCanonicalizer;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r4.model.Subscription;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.hl7.fhir.r4.model.Subscription.SubscriptionStatus.REQUESTED;
import static org.junit.jupiter.api.Assertions.fail;

public class RestHookChannelValidatorTest {
	private final FhirContext myCtx = FhirContext.forR4();
	private final SubscriptionSettings mySubscriptionSettings = new SubscriptionSettings();
	private final SubscriptionCanonicalizer mySubscriptionCanonicalizer= new SubscriptionCanonicalizer(myCtx, mySubscriptionSettings);

	private final String NO_PAYLOAD = StringUtils.EMPTY;

	@ParameterizedTest
	@MethodSource("urlAndExpectedEvaluationResultProvider")
	public void testRestHookChannelValidation_withUrl(String theUrl, boolean theExpectedValidationResult){
		RegexEndpointUrlValidationStrategy regexEndpointUrlValidationStrategy = new RegexEndpointUrlValidationStrategy(SubscriptionSettings.DEFAULT_RESTHOOK_ENDPOINTURL_VALIDATION_REGEX);
		RestHookChannelValidator restHookChannelValidator = new RestHookChannelValidator(regexEndpointUrlValidationStrategy);

		CanonicalSubscription subscription = createSubscription(theUrl, NO_PAYLOAD);
		doValidateUrlAndAssert(restHookChannelValidator, subscription, theExpectedValidationResult);
	}

	@ParameterizedTest
	@MethodSource("urlAndExpectedEvaluationResultProviderForNoUrlValidation")
	public void testRestHookChannelValidation_withNoUrlValidation(String theUrl, boolean theExpectedValidationResult){
		RestHookChannelValidator restHookChannelValidator = new RestHookChannelValidator();

		CanonicalSubscription subscription = createSubscription(theUrl, NO_PAYLOAD);
		doValidateUrlAndAssert(restHookChannelValidator, subscription, theExpectedValidationResult);
	}

	@ParameterizedTest
	@MethodSource("payloadAndExpectedEvaluationResultProvider")
	public void testRestHookChannelValidation_withPayload(String thePayload, boolean theExpectedValidationResult){
		RestHookChannelValidator restHookChannelValidator = new RestHookChannelValidator();

		CanonicalSubscription subscription = createSubscription("https://acme.org", thePayload);
		doValidatePayloadAndAssert(restHookChannelValidator, subscription, theExpectedValidationResult);
	}

	private void doValidatePayloadAndAssert(RestHookChannelValidator theRestHookChannelValidator, CanonicalSubscription theSubscription, boolean theExpectedValidationResult) {
		boolean validationResult = true;

		try {
			theRestHookChannelValidator.validateChannelPayload(theSubscription);
		} catch (Exception e){
			validationResult = false;
		}

		if( validationResult != theExpectedValidationResult){
			String message = String.format("Validation result for payload %s was expected to be %b but was %b", theSubscription.getEndpointUrl(), theExpectedValidationResult, validationResult);
			fail(message);
		}
	}

	private void doValidateUrlAndAssert(RestHookChannelValidator theRestHookChannelValidator, CanonicalSubscription theSubscription, boolean theExpectedValidationResult) {
		boolean validationResult = true;

		try {
			theRestHookChannelValidator.validateChannelEndpoint(theSubscription);
		} catch (Exception e){
			validationResult = false;
		}

		if( validationResult != theExpectedValidationResult){
			String message = String.format("Validation result for URL %s was expected to be %b but was %b", theSubscription.getEndpointUrl(), theExpectedValidationResult, validationResult);
			fail(message);
		}
	}

	@Nonnull
	private CanonicalSubscription createSubscription(String theUrl, String thePayload) {
		final Subscription subscription = new Subscription();
		subscription.setStatus(REQUESTED);
		subscription.setCriteria("Patient?");
		final Subscription.SubscriptionChannelComponent channel = subscription.getChannel();
		channel.setType(Subscription.SubscriptionChannelType.RESTHOOK);
		channel.setEndpoint(theUrl);
		channel.setPayload(thePayload);
		return mySubscriptionCanonicalizer.canonicalize(subscription);
	}

	static Stream<Arguments> urlAndExpectedEvaluationResultProvider() {
		return Stream.of(
			Arguments.of("http://www.acme.corp/fhir", true),
			Arguments.of("http://acme.corp/fhir", true),
			Arguments.of("http://acme.corp:8000/fhir", true),
			Arguments.of("http://acme.corp:8000/fhir/", true),
			Arguments.of("http://acme.corp/fhir/", true),
			Arguments.of("https://foo.bar.com", true),
			Arguments.of("http://localhost:8000", true),
			Arguments.of("http://localhost:8000/", true),
			Arguments.of("http://localhost:8000/fhir", true),
			Arguments.of("http://localhost:8000/fhir/", true),
			Arguments.of("acme.corp", false),
			Arguments.of("https://acme.corp/badstuff-%%$^&& iuyi", false),
			Arguments.of("ftp://acme.corp", false));
	}

	static Stream<Arguments> urlAndExpectedEvaluationResultProviderForNoUrlValidation() {
		return Stream.of(
			Arguments.of(null, false),
			Arguments.of("", false),
			Arguments.of("   ", false),
			Arguments.of("something", true));
	}

	static Stream<Arguments> payloadAndExpectedEvaluationResultProvider() {
		return Stream.of(
			Arguments.of(null, true),
			Arguments.of("", true),
			Arguments.of("    ", true),
			Arguments.of("application/json", true),
			Arguments.of("garbage/fhir", false));
	}

}
