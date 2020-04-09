package ca.uhn.fhir.jpa.empi.config;

import ca.uhn.fhir.jpa.empi.util.EmpiHelperR4;
import ca.uhn.fhir.jpa.subscription.channel.config.SubscriptionChannelConfig;
import ca.uhn.fhir.jpa.subscription.submit.config.SubscriptionSubmitterConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

// FIXME KHS change to broker config
@Import({SubscriptionSubmitterConfig.class, SubscriptionChannelConfig.class})
public class TestEmpiConfigR4 extends BaseTestEmpiConfig {
	@Bean
	EmpiHelperR4 empiHelper() {
		return new EmpiHelperR4();
	}
}
