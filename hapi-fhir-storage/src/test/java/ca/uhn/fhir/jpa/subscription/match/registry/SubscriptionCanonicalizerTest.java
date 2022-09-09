package ca.uhn.fhir.jpa.subscription.match.registry;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.primitive.BooleanDt;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.Subscription;
import org.junit.jupiter.api.Test;

import static ca.uhn.fhir.util.HapiExtensions.EX_SEND_DELETE_MESSAGES;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SubscriptionCanonicalizerTest {

	FhirContext r4Context = FhirContext.forR4();

	private final SubscriptionCanonicalizer testedSC = new SubscriptionCanonicalizer(r4Context);

	@Test
	void testCanonicalizeR4SendDeleteMessagesSetsExtensionValueNotPresent() {
		Subscription subscription = new Subscription();

		CanonicalSubscription canonicalSubscription = testedSC.canonicalize(subscription);

		assertFalse(canonicalSubscription.getSendDeleteMessages());
	}


	@Test
	void testCanonicalizeR4SendDeleteMessagesSetsExtensionValue() {
		Subscription subscription = new Subscription();
		Extension sendDeleteMessagesExtension = new Extension()
			.setUrl(EX_SEND_DELETE_MESSAGES)
			.setValue(new BooleanType(true));
		subscription.getChannel().addExtension(sendDeleteMessagesExtension);

		CanonicalSubscription canonicalSubscription = testedSC.canonicalize(subscription);

		assertTrue(canonicalSubscription.getSendDeleteMessages());
	}

	@Test
	public void testCanonicalizeDstu2SendDeleteMessages() {
		SubscriptionCanonicalizer dstu2Canonicalizer = new SubscriptionCanonicalizer(FhirContext.forDstu2());
		ca.uhn.fhir.model.dstu2.resource.Subscription dstu2Sub = new ca.uhn.fhir.model.dstu2.resource.Subscription();
		ExtensionDt extensionDt = new ExtensionDt();
		extensionDt.setUrl(EX_SEND_DELETE_MESSAGES);
		extensionDt.setValue(new BooleanDt(true));
		dstu2Sub.getChannel().addUndeclaredExtension(extensionDt);
		CanonicalSubscription canonicalize = dstu2Canonicalizer.canonicalize(dstu2Sub);
		assertTrue(canonicalize.getSendDeleteMessages());
	}
}
