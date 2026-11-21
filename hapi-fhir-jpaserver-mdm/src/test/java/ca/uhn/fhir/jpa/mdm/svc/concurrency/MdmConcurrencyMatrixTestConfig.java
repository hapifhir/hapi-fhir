package ca.uhn.fhir.jpa.mdm.svc.concurrency;

import ca.uhn.fhir.jpa.dao.mdm.MdmMatchClaimSvcJpaImpl;
import ca.uhn.fhir.mdm.blocklist.json.BlockListJson;
import ca.uhn.fhir.mdm.blocklist.json.BlockListRuleJson;
import ca.uhn.fhir.mdm.blocklist.svc.IBlockListRuleProvider;
import ca.uhn.fhir.mdm.dao.IMdmMatchClaimSvc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * Installs the gating claim service in front of the real one, and a block rule that blocks every Patient with gender
 * {@code other} from matching.
 */
// Created by claude-opus-5-5
public class MdmConcurrencyMatrixTestConfig {

	@Bean
	public ConcurrencyScheduler concurrencyScheduler() {
		return new ConcurrencyScheduler();
	}

	@Bean
	@Primary
	public IMdmMatchClaimSvc gatingMdmMatchClaimSvc(
			MdmMatchClaimSvcJpaImpl theDelegate, ConcurrencyScheduler theScheduler) {
		return new GatingMdmMatchClaimSvc(theDelegate, theScheduler);
	}

	@Bean
	public IBlockListRuleProvider blockListRuleProvider() {
		BlockListJson blockList = new BlockListJson();
		BlockListRuleJson rule = new BlockListRuleJson();
		rule.setResourceType("Patient");
		rule.addBlockListField().setFhirPath("gender").setBlockedValue("other");
		blockList.addBlockListRule(rule);
		return () -> blockList;
	}
}
