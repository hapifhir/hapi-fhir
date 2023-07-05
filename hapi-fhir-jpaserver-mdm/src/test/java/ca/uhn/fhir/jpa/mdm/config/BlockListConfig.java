package ca.uhn.fhir.jpa.mdm.config;

import ca.uhn.fhir.mdm.blocklist.svc.IBlockListRuleProvider;
import org.springframework.context.annotation.Bean;

import static org.mockito.Mockito.mock;

public class BlockListConfig {

	@Bean
	public IBlockListRuleProvider ruleProvider() {
		return mock(IBlockListRuleProvider.class);
	}
}
