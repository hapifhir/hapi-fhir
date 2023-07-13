package ca.uhn.fhir.mdm.blocklist.json;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class BlockListRuleJson implements IModelJson {
	/**
	 * The resource type that this block list rule applies to.
	 */
	@JsonProperty(value = "resourceType", required = true)
	private String myResourceType;

	/**
	 * The list of blocked fields that this rule applies to.
	 */
	@JsonProperty(value = "fields", required = true)
	private List<BlockedFieldJson> myBlockedFields;

	public String getResourceType() {
		return myResourceType;
	}

	public void setResourceType(String theResourceType) {
		myResourceType = theResourceType;
	}

	public List<BlockedFieldJson> getBlockedFields() {
		if (myBlockedFields == null) {
			myBlockedFields = new ArrayList<>();
		}
		return myBlockedFields;
	}

	public BlockedFieldJson addBlockListField() {
		BlockedFieldJson rule = new BlockedFieldJson();
		getBlockedFields().add(rule);
		return rule;
	}
}
