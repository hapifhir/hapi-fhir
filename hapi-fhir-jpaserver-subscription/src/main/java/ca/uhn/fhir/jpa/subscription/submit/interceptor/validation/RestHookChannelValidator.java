package ca.uhn.fhir.jpa.subscription.submit.interceptor.validation;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscriptionChannelType;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import jakarta.annotation.Nonnull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class RestHookChannelValidator implements IChannelTypeValidator {

	private final Pattern myEndpointUrlValidationPattern;

	public RestHookChannelValidator(@Nonnull String theRestHookEndpointUrlValidatingRegex) {
		myEndpointUrlValidationPattern = Pattern.compile(theRestHookEndpointUrlValidatingRegex);
	}

	@Override
	public void validateChannelType(CanonicalSubscription theSubscription) {
		validateChannelPayload(theSubscription);
		validateChannelEndpoint(theSubscription);
	}

	@Override
	public CanonicalSubscriptionChannelType getSubscriptionChannelType() {
		return CanonicalSubscriptionChannelType.RESTHOOK;
	}

	protected void validateChannelEndpoint(@Nonnull CanonicalSubscription theCanonicalSubscription) {
		String endpointUrl = theCanonicalSubscription.getEndpointUrl();

		if (isBlank(endpointUrl)) {
			throw new UnprocessableEntityException(
					Msg.code(21) + "Rest-hook subscriptions must have Subscription.channel.endpoint defined");
		}

		Matcher matcher = myEndpointUrlValidationPattern.matcher(endpointUrl);

		if (!matcher.matches()) {
			throw new UnprocessableEntityException(Msg.code(2545) + "endpoint " + endpointUrl + " failed validation.");
		}
	}

	protected void validateChannelPayload(CanonicalSubscription theResource) {
		if (!isBlank(theResource.getPayloadString())
				&& EncodingEnum.forContentType(theResource.getPayloadString()) == null) {
			throw new UnprocessableEntityException(Msg.code(1985) + "Invalid value for Subscription.channel.payload: "
					+ theResource.getPayloadString());
		}
	}
}
