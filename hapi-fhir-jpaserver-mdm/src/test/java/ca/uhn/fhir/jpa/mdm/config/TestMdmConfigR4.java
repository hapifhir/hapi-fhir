package ca.uhn.fhir.jpa.mdm.config;

import ca.uhn.fhir.jpa.mdm.helper.MdmHelperR4;
import ca.uhn.fhir.jpa.subscription.channel.config.SubscriptionChannelConfig;
import ca.uhn.fhir.jpa.test.config.TestSubscriptionSubmitterConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import({TestSubscriptionSubmitterConfig.class, SubscriptionChannelConfig.class})
public class TestMdmConfigR4 extends BaseTestMdmConfig {
	@Bean
	MdmHelperR4 mdmHelperR4() {
		return new MdmHelperR4();
	}
}
