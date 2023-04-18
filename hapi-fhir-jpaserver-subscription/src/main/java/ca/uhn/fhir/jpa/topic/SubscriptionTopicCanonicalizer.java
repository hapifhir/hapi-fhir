package ca.uhn.fhir.jpa.topic;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.SubscriptionTopic;

public final class SubscriptionTopicCanonicalizer {
	private static final FhirContext ourFhirContextR5 = FhirContext.forR5();

	private SubscriptionTopicCanonicalizer() {
	}

	// WIP STR5 use elsewhere
	public static SubscriptionTopic canonicalize(FhirContext theFhirContext, IBaseResource theSubscriptionTopic) {
		switch (theFhirContext.getVersion().getVersion()) {
			case R4B:
				String encoded = theFhirContext.newJsonParser().encodeResourceToString(theSubscriptionTopic);
				return ourFhirContextR5.newJsonParser().parseResource(SubscriptionTopic.class, encoded);
			case R5:
				return (SubscriptionTopic) theSubscriptionTopic;
			default:
				throw new UnsupportedOperationException(Msg.code(2337) + "Subscription topics are not supported in FHIR version " + theFhirContext.getVersion().getVersion());
		}
	}
}
