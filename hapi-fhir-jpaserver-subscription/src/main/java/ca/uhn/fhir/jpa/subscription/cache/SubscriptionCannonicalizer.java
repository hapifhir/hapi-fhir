package ca.uhn.fhir.jpa.subscription.cache;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.subscription.CanonicalSubscription;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriptionCannonicalizer<S extends IBaseResource> {
	@Autowired
	FhirContext myFhirContext;
	@Autowired(required = false)
	@Qualifier("myEventDefinitionDaoR4")
	public CanonicalSubscription canonicalize(S theSubscription) {
		switch (myFhirContext.getVersion().getVersion()) {
			case DSTU2:
				return canonicalizeDstu2(theSubscription);
			case DSTU3:
				return canonicalizeDstu3(theSubscription);
			case R4:
				return canonicalizeR4(theSubscription);
			default:
				throw new ConfigurationException("Subscription not supported for version: " + myFhirContext.getVersion().getVersion());
		}
	}

	protected CanonicalSubscription canonicalizeDstu2(IBaseResource theSubscription) {
		ca.uhn.fhir.model.dstu2.resource.Subscription subscription = (ca.uhn.fhir.model.dstu2.resource.Subscription) theSubscription;

		CanonicalSubscription retVal = new CanonicalSubscription();
		try {
			retVal.setStatus(org.hl7.fhir.r4.model.Subscription.SubscriptionStatus.fromCode(subscription.getStatus()));
			retVal.setChannelType(org.hl7.fhir.r4.model.Subscription.SubscriptionChannelType.fromCode(subscription.getChannel().getType()));
			retVal.setCriteriaString(subscription.getCriteria());
			retVal.setEndpointUrl(subscription.getChannel().getEndpoint());
			retVal.setHeaders(subscription.getChannel().getHeader());
			retVal.setIdElement(subscription.getIdElement());
			retVal.setPayloadString(subscription.getChannel().getPayload());
		} catch (FHIRException theE) {
			throw new InternalErrorException(theE);
		}
		return retVal;
	}

	protected CanonicalSubscription canonicalizeDstu3(IBaseResource theSubscription) {
		org.hl7.fhir.dstu3.model.Subscription subscription = (org.hl7.fhir.dstu3.model.Subscription) theSubscription;

		CanonicalSubscription retVal = new CanonicalSubscription();
		try {
			retVal.setStatus(org.hl7.fhir.r4.model.Subscription.SubscriptionStatus.fromCode(subscription.getStatus().toCode()));
			retVal.setChannelType(org.hl7.fhir.r4.model.Subscription.SubscriptionChannelType.fromCode(subscription.getChannel().getType().toCode()));
			retVal.setCriteriaString(subscription.getCriteria());
			retVal.setEndpointUrl(subscription.getChannel().getEndpoint());
			retVal.setHeaders(subscription.getChannel().getHeader());
			retVal.setIdElement(subscription.getIdElement());
			retVal.setPayloadString(subscription.getChannel().getPayload());

			if (retVal.getChannelType() == Subscription.SubscriptionChannelType.EMAIL) {
				String from;
				String subjectTemplate;
				String bodyTemplate;
				try {
					from = subscription.getChannel().getExtensionString(SubscriptionConstants.EXT_SUBSCRIPTION_EMAIL_FROM);
					subjectTemplate = subscription.getChannel().getExtensionString(SubscriptionConstants.EXT_SUBSCRIPTION_SUBJECT_TEMPLATE);
				} catch (FHIRException theE) {
					throw new ConfigurationException("Failed to extract subscription extension(s): " + theE.getMessage(), theE);
				}
				retVal.getEmailDetails().setFrom(from);
				retVal.getEmailDetails().setSubjectTemplate(subjectTemplate);
			}

			if (retVal.getChannelType() == Subscription.SubscriptionChannelType.RESTHOOK) {
				String stripVersionIds;
				String deliverLatestVersion;
				try {
					stripVersionIds = subscription.getChannel().getExtensionString(SubscriptionConstants.EXT_SUBSCRIPTION_RESTHOOK_STRIP_VERSION_IDS);
					deliverLatestVersion = subscription.getChannel().getExtensionString(SubscriptionConstants.EXT_SUBSCRIPTION_RESTHOOK_DELIVER_LATEST_VERSION);
				} catch (FHIRException theE) {
					throw new ConfigurationException("Failed to extract subscription extension(s): " + theE.getMessage(), theE);
				}
				retVal.getRestHookDetails().setStripVersionId(Boolean.parseBoolean(stripVersionIds));
				retVal.getRestHookDetails().setDeliverLatestVersion(Boolean.parseBoolean(deliverLatestVersion));
			}

		} catch (FHIRException theE) {
			throw new InternalErrorException(theE);
		}
		return retVal;
	}

	protected CanonicalSubscription canonicalizeR4(IBaseResource theSubscription) {
		org.hl7.fhir.r4.model.Subscription subscription = (org.hl7.fhir.r4.model.Subscription) theSubscription;

		CanonicalSubscription retVal = new CanonicalSubscription();
		retVal.setStatus(subscription.getStatus());
		retVal.setChannelType(subscription.getChannel().getType());
		retVal.setCriteriaString(subscription.getCriteria());
		retVal.setEndpointUrl(subscription.getChannel().getEndpoint());
		retVal.setHeaders(subscription.getChannel().getHeader());
		retVal.setIdElement(subscription.getIdElement());
		retVal.setPayloadString(subscription.getChannel().getPayload());

		if (retVal.getChannelType() == Subscription.SubscriptionChannelType.EMAIL) {
			String from;
			String subjectTemplate;
			try {
				from = subscription.getChannel().getExtensionString(SubscriptionConstants.EXT_SUBSCRIPTION_EMAIL_FROM);
				subjectTemplate = subscription.getChannel().getExtensionString(SubscriptionConstants.EXT_SUBSCRIPTION_SUBJECT_TEMPLATE);
			} catch (FHIRException theE) {
				throw new ConfigurationException("Failed to extract subscription extension(s): " + theE.getMessage(), theE);
			}
			retVal.getEmailDetails().setFrom(from);
			retVal.getEmailDetails().setSubjectTemplate(subjectTemplate);
		}

		if (retVal.getChannelType() == Subscription.SubscriptionChannelType.RESTHOOK) {
			String stripVersionIds;
			String deliverLatestVersion;
			try {
				stripVersionIds = subscription.getChannel().getExtensionString(SubscriptionConstants.EXT_SUBSCRIPTION_RESTHOOK_STRIP_VERSION_IDS);
				deliverLatestVersion = subscription.getChannel().getExtensionString(SubscriptionConstants.EXT_SUBSCRIPTION_RESTHOOK_DELIVER_LATEST_VERSION);
			} catch (FHIRException theE) {
				throw new ConfigurationException("Failed to extract subscription extension(s): " + theE.getMessage(), theE);
			}
			retVal.getRestHookDetails().setStripVersionId(Boolean.parseBoolean(stripVersionIds));
			retVal.getRestHookDetails().setDeliverLatestVersion(Boolean.parseBoolean(deliverLatestVersion));
		}

		List<Extension> topicExts = subscription.getExtensionsByUrl("http://hl7.org/fhir/subscription/topics");
		if (topicExts.size() > 0) {
			IBaseReference ref = (IBaseReference) topicExts.get(0).getValueAsPrimitive();
			if (!"EventDefinition".equals(ref.getReferenceElement().getResourceType())) {
				throw new PreconditionFailedException("Topic reference must be an EventDefinition");
			}
		}

		return retVal;
	}


}
