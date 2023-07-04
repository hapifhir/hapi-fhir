package ca.uhn.fhir.jpa.mdm.config;

import ca.uhn.fhir.mdm.blocklist.json.BlockListJson;
import ca.uhn.fhir.mdm.blocklist.svc.IBlockListRuleProvider;
import org.springframework.context.annotation.Bean;

public class BlockListConfig {

	@Bean
	public IBlockListRuleProvider ruleProvider() {
		return new IBlockListRuleProvider() {
			@Override
			public BlockListJson getBlocklistRules() {
				return null;
			}
		};
	}
}
