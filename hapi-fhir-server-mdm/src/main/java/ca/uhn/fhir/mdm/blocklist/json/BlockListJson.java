package ca.uhn.fhir.mdm.blocklist.json;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class BlockListJson implements IModelJson {

	/**
	 * List of blocklistrules.
	 * Each item can be thought of as a 'ruleset'.
	 * These rulesets are applicable to a resource type.
	 * Each ruleset is applied as an 'or' to the resource being processed.
	 */
	@JsonProperty(value = "blocklist", required = true)
	private List<BlockListRuleJson> myBlockListItemJsonList;

	public List<BlockListRuleJson> getBlockListItemJsonList() {
		if (myBlockListItemJsonList == null) {
			myBlockListItemJsonList = new ArrayList<>();
		}
		return myBlockListItemJsonList;
	}

	public void setBlockListItemJsonList(List<BlockListRuleJson> theBlockListItemJsonList) {
		myBlockListItemJsonList = theBlockListItemJsonList;
	}

	public BlockListJson addBlockListRule(BlockListRuleJson theRule) {
		getBlockListItemJsonList().add(theRule);
		return this;
	}
}
