package ca.uhn.fhir.jpa.empi.config;

import ca.uhn.fhir.jpa.subscription.channel.config.SubscriptionChannelConfig;
import ca.uhn.fhir.jpa.subscription.submit.config.SubscriptionSubmitterConfig;
import org.springframework.context.annotation.Import;

@Import({SubscriptionSubmitterConfig.class, SubscriptionChannelConfig.class})
public class TestEmpiConfigR4 extends BaseTestEmpiConfig {
}
