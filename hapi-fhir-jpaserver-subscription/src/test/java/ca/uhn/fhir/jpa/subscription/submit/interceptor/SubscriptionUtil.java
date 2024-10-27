package ca.uhn.fhir.jpa.subscription.submit.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionCanonicalizer;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscriptionChannelType;
import ca.uhn.fhir.subscription.SubscriptionConstants;
import ca.uhn.fhir.util.TerserUtil;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseEnumeration;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Version independent utility class for setting fields on subscriptions
 */

public final class SubscriptionUtil {
	private static final Logger ourLog = LoggerFactory.getLogger(SubscriptionUtil.class);

	private SubscriptionUtil() {}

	public static void setStatus(FhirContext theFhirContext, IBaseResource theSubscription, String theStatus) {
		IBaseEnumeration newValue = switch (theFhirContext.getVersion().getVersion()) {
			case DSTU3 -> new org.hl7.fhir.dstu3.model.Subscription.SubscriptionStatusEnumFactory().fromType(new org.hl7.fhir.dstu3.model.StringType(theStatus));
			case R4 -> new org.hl7.fhir.r4.model.Subscription.SubscriptionStatusEnumFactory().fromType(new org.hl7.fhir.r4.model.StringType(theStatus));
			case R4B -> new org.hl7.fhir.r4b.model.Enumerations.SubscriptionStatusEnumFactory().fromType(new org.hl7.fhir.r4b.model.StringType(theStatus));
			case R5 -> new org.hl7.fhir.r5.model.Enumerations.SubscriptionStatusCodesEnumFactory().fromType(new org.hl7.fhir.r5.model.StringType(theStatus));
			default -> null;
		};
		TerserUtil.setField(theFhirContext, "status", theSubscription, newValue);
	}

	public static void setCriteria(FhirContext theFhirContext, IBaseResource theSubscription, String theCriteria) {
		SubscriptionCanonicalizer canonicalizer = new SubscriptionCanonicalizer(theFhirContext);
		CanonicalSubscription canonicalSubscription = canonicalizer.canonicalize(theSubscription);
		if (canonicalSubscription.isTopicSubscription()) {
			if (theFhirContext.getVersion().getVersion() == FhirVersionEnum.R5) {
				// Nothing to do on R5
				return;
			}
			Subscription subscription = (Subscription)theSubscription;
			subscription.getCriteriaElement().addExtension(SubscriptionConstants.SUBSCRIPTION_TOPIC_FILTER_URL, new StringType(theCriteria));
		} else {
			TerserUtil.setStringField(theFhirContext, "criteria", theSubscription, theCriteria);
		}
	}

	public static void setChannelType(FhirContext theFhirContext, IBaseResource theSubscription, String theChannelType) {
		FhirVersionEnum version = theFhirContext.getVersion().getVersion();
		IBase newValue = switch (version) {
			case DSTU3 -> new org.hl7.fhir.dstu3.model.Subscription.SubscriptionChannelTypeEnumFactory().fromType(new org.hl7.fhir.dstu3.model.StringType(theChannelType));
			case R4 -> new org.hl7.fhir.r4.model.Subscription.SubscriptionChannelTypeEnumFactory().fromType(new org.hl7.fhir.r4.model.StringType(theChannelType));
			case R4B -> new org.hl7.fhir.r4b.model.Subscription.SubscriptionChannelTypeEnumFactory().fromType(new org.hl7.fhir.r4b.model.StringType(theChannelType));
			case R5 -> CanonicalSubscriptionChannelType.valueOf(theChannelType.toUpperCase()).toR5Coding();
			default -> null;
		};
		String fhirPath = switch(version) {
			case DSTU3 -> "channel.type";
			case R4 -> "channel.type";
			case R4B -> "channel.type";
			case R5 -> "channelType";
			default -> null;
		};
		TerserUtil.setFieldByFhirPath(theFhirContext, fhirPath, theSubscription, newValue);
	}

	public static void setEndpoint(FhirContext theFhirContext, IBaseResource theSubscription, String theEndpoint) {
		FhirVersionEnum version = theFhirContext.getVersion().getVersion();
		String fhirPath = switch(version) {
			case DSTU3 -> "channel.endpoint";
			case R4 -> "channel.endpoint";
			case R4B -> "channel.endpoint";
			case R5 -> "endpoint";
			default -> null;
		};
		TerserUtil.setStringFieldByFhirPath(theFhirContext, fhirPath, theSubscription, theEndpoint);
	}
}
