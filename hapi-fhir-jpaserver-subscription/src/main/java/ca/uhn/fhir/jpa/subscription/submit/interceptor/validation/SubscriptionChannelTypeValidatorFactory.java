package ca.uhn.fhir.jpa.subscription.submit.interceptor.validation;

import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscriptionChannelType;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class SubscriptionChannelTypeValidatorFactory {
	private static final Logger ourLog = LoggerFactory.getLogger(SubscriptionChannelTypeValidatorFactory.class);

	private Map<CanonicalSubscriptionChannelType, IChannelTypeValidator> myValidators = new EnumMap<>(CanonicalSubscriptionChannelType.class);

	public SubscriptionChannelTypeValidatorFactory(@Nonnull List<IChannelTypeValidator> theValidorList) {
		theValidorList.forEach(this::addChannelTypeValidator);
	}

	public IChannelTypeValidator getValidatorForChannelType(CanonicalSubscriptionChannelType theChannelType) {
		return myValidators.getOrDefault(theChannelType, getNoopValidatorForChannelType(theChannelType));
	}

	public void addChannelTypeValidator(IChannelTypeValidator theValidator) {
		myValidators.put(theValidator.getSubscriptionChannelType(), theValidator);
	}

	private IChannelTypeValidator getNoopValidatorForChannelType(CanonicalSubscriptionChannelType theChannelType) {
		return new IChannelTypeValidator() {
			@Override
			public void validateChannelType(CanonicalSubscription theSubscription) {
				ourLog.debug(
						"No validator for channel type {} was registered, will perform no-op validation.",
						theChannelType);
			}

			@Override
			public CanonicalSubscriptionChannelType getSubscriptionChannelType() {
				return CanonicalSubscriptionChannelType.NULL;
			}
		};
	}
}
