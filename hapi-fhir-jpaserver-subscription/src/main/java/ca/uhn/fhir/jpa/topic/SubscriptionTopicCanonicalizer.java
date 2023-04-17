package ca.uhn.fhir.jpa.topic;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.SubscriptionTopic;

public final class SubscriptionTopicCanonicalizer {
	private static final FhirContext ourFhirContextR5 = FhirContext.forR5();

	private SubscriptionTopicCanonicalizer() {
	}

	// FIXME KHS use elsewhere
	public static SubscriptionTopic canonicalize(FhirContext theFhirContext, IBaseResource theSubscriptionTopic) {
		switch (theFhirContext.getVersion().getVersion()) {
			case R4B:
				String encoded = theFhirContext.newJsonParser().encodeResourceToString(theSubscriptionTopic);
				return ourFhirContextR5.newJsonParser().parseResource(SubscriptionTopic.class, encoded);
			case R5:
				return (SubscriptionTopic) theSubscriptionTopic;
			default:
				// FIXME code
				throw new UnsupportedOperationException("Subscription topics are not supported in FHIR version " + theFhirContext.getVersion().getVersion());
		}
	}
}
