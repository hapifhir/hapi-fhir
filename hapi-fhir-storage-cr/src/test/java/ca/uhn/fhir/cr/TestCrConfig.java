package ca.uhn.fhir.cr;

import ca.uhn.fhir.cr.common.helper.PartitionHelper;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.subscription.channel.config.SubscriptionChannelConfig;
import ca.uhn.fhir.jpa.subscription.submit.config.SubscriptionSubmitterConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({SubscriptionSubmitterConfig.class, SubscriptionChannelConfig.class})
public class TestCrConfig {

	@Bean
	public DaoConfig daoConfig() {
		DaoConfig daoConfig = new DaoConfig();
		daoConfig.setAllowExternalReferences(true);
		daoConfig.setEnforceReferentialIntegrityOnWrite(false);
		daoConfig.setEnforceReferenceTargetTypes(false);
		daoConfig.setResourceClientIdStrategy(DaoConfig.ClientIdStrategyEnum.ANY);
		//daoConfig.setResourceServerIdStrategy(Id);
		return daoConfig;
	}

	@Bean
	public PartitionHelper partitionHelper() {
		return new PartitionHelper();
	}

}
