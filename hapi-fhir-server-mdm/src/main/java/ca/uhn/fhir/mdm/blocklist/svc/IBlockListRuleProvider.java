package ca.uhn.fhir.mdm.blocklist.svc;

import ca.uhn.fhir.mdm.blocklist.json.BlockListJson;

public interface IBlockListRuleProvider {
	/**
	 * Returns the provided blocklist rules.
	 * @return
	 */
	BlockListJson getBlocklistRules();
}
