package ca.uhn.fhir.test.utilities;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Subscription;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

// Created by claude-sonnet-4-6
class ITestSubscriptionBuilderTest {

	FhirContext myFhirContext = FhirContext.forR4Cached();

	ITestSubscriptionBuilder myBuilder = new ITestSubscriptionBuilder() {
		@Override
		public IIdType doCreateResource(IBaseResource theResource) {
			return null;
		}

		@Override
		public IIdType doUpdateResource(IBaseResource theResource) {
			return null;
		}

		@Override
		public FhirContext getFhirContext() {
			return myFhirContext;
		}
	};

	@Test
	void buildSubscription_withNoModifiers_returnsSubscriptionInstance() {
		IBaseResource result = myBuilder.buildSubscription();

		assertThat(result).isInstanceOf(Subscription.class);
	}

	@ParameterizedTest
	@ValueSource(strings = {"Observation?code=1234-5", "Patient?active=true", "Encounter?status=finished"})
	void withCriteria_setsCriteriaOnSubscription(String theCriteria) {
		Subscription subscription = (Subscription) myBuilder.buildSubscription(
			myBuilder.withCriteria(theCriteria)
		);

		assertThat(subscription.getCriteria()).isEqualTo(theCriteria);
	}

	@ParameterizedTest
	@ValueSource(strings = {"Observe all vitals", "Patient admission alerts", "Lab result notifications"})
	void withReason_setsReasonOnSubscription(String theReason) {
		Subscription subscription = (Subscription) myBuilder.buildSubscription(
			myBuilder.withReason(theReason)
		);

		assertThat(subscription.getReason()).isEqualTo(theReason);
	}

	@ParameterizedTest
	@ValueSource(strings = {"https://example.com/webhook", "http://internal-server/notify"})
	void withEndpoint_setsEndpointOnChannel(String theEndpoint) {
		Subscription subscription = (Subscription) myBuilder.buildSubscription(
			myBuilder.withChannel(
				myBuilder.withEndpoint(theEndpoint)
			)
		);

		assertThat(subscription.getChannel().getEndpoint()).isEqualTo(theEndpoint);
	}

	@ParameterizedTest
	@ValueSource(strings = {"application/fhir+json", "application/fhir+xml"})
	void withPayload_setsPayloadOnChannel(String thePayload) {
		Subscription subscription = (Subscription) myBuilder.buildSubscription(
			myBuilder.withChannel(
				myBuilder.withPayload(thePayload)
			)
		);

		assertThat(subscription.getChannel().getPayload()).isEqualTo(thePayload);
	}

	@ParameterizedTest
	@ValueSource(strings = {"rest-hook", "websocket", "email", "sms", "message"})
	void withType_setsTypeOnChannel(String theType) {
		Subscription subscription = (Subscription) myBuilder.buildSubscription(
			myBuilder.withChannel(
				myBuilder.withType(theType)
			)
		);

		assertThat(subscription.getChannel().getType().toCode()).isEqualTo(theType);
	}

	@Test
	void withChannel_withMultipleModifiers_setsAllChannelProperties() {
		Subscription subscription = (Subscription) myBuilder.buildSubscription(
			myBuilder.withChannel(
				myBuilder.withType("rest-hook"),
				myBuilder.withEndpoint("https://example.com/webhook"),
				myBuilder.withPayload("application/fhir+json")
			)
		);

		Subscription.SubscriptionChannelComponent channel = subscription.getChannel();
		assertThat(channel.getType().toCode()).isEqualTo("rest-hook");
		assertThat(channel.getEndpoint()).isEqualTo("https://example.com/webhook");
		assertThat(channel.getPayload()).isEqualTo("application/fhir+json");
	}

	@Test
	void withChannelExtension_addsExtensionToChannel() {
		String url = "http://example.com/my-extension";
		String value = "extension-value";

		Subscription subscription = (Subscription) myBuilder.buildSubscription(
			myBuilder.withChannel(
				myBuilder.withChannelExtension(url, value)
			)
		);

		Subscription.SubscriptionChannelComponent channel = subscription.getChannel();
		assertThat(channel.getExtension()).hasSize(1);
		assertThat(channel.getExtension().get(0).getUrl()).isEqualTo(url);
		assertThat(channel.getExtension().get(0).getValue().primitiveValue()).isEqualTo(value);
	}
}
