package ca.uhn.fhir.jpa.mdm.config;

import ca.uhn.fhir.jpa.mdm.helper.MdmHelperR4;
import ca.uhn.fhir.jpa.model.config.SubscriptionSettings;
import ca.uhn.fhir.jpa.channel.ChannelConfig;
import ca.uhn.fhir.jpa.subscription.submit.config.SubscriptionSubmitterConfig;
import ca.uhn.fhir.jpa.test.config.TestSubscriptionMatcherInterceptorConfig;
import org.hl7.fhir.dstu2.model.Subscription;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

@Import({TestSubscriptionMatcherInterceptorConfig.class, SubscriptionSubmitterConfig.class, ChannelConfig.class})
public class TestMdmConfigR4 extends BaseTestMdmConfig {
	@Bean
	MdmHelperR4 mdmHelperR4() {
		return new MdmHelperR4();
	}

	@Primary
	@Bean
	public SubscriptionSettings subscriptionSettings() {
		SubscriptionSettings retVal = new SubscriptionSettings();

		retVal.addSupportedSubscriptionType(Subscription.SubscriptionChannelType.MESSAGE);

		return retVal;
	}
}
