package ca.uhn.fhir.jpa.mdm.config;

import ca.uhn.fhir.jpa.mdm.dao.JpaMdmLinkImplFactory;
import ca.uhn.fhir.jpa.mdm.helper.MdmHelperR4;
import ca.uhn.fhir.jpa.subscription.channel.config.SubscriptionChannelConfig;
import ca.uhn.fhir.jpa.subscription.submit.config.SubscriptionSubmitterConfig;
import ca.uhn.fhir.mdm.dao.IMdmLinkImplFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import({SubscriptionSubmitterConfig.class, SubscriptionChannelConfig.class})
public class TestMdmConfigR4 extends BaseTestMdmConfig {
	@Bean
	MdmHelperR4 mdmHelperR4() {
		return new MdmHelperR4();
	}

	@Bean
	IMdmLinkImplFactory myMdmLinkImplFactory(){ return new JpaMdmLinkImplFactory();}
}
