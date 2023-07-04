package ca.uhn.fhir.mdm.blocklist.json;

import ca.uhn.fhir.mdm.blocklist.models.BlockFieldBlockRuleEnum;
import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nonnull;

public class BlockedFieldJson implements IModelJson {

	/**
	 * The fhir path to the field on the resource that is being
	 * processed.
	 */
	@JsonProperty(value = "fhirPath", required = true)
	private String myFhirPath;

	/**
	 * The value to block on.
	 * If the value of the field at `fhirPath` matches this
	 * value, it will be blocked.
	 */
	@JsonProperty(value = "value", required = true)
	private String myBlockedValue;

	/**
	 * The block field block rule.
	 * See {@link BlockFieldBlockRuleEnum} for more info.
	 */
	@JsonProperty(value = "match", required = true)
	private BlockFieldBlockRuleEnum myBlockRule = BlockFieldBlockRuleEnum.EXACT;

	public String getFhirPath() {
		return myFhirPath;
	}

	public BlockedFieldJson setFhirPath(String theFhirPath) {
		myFhirPath = theFhirPath;
		return this;
	}

	public String getBlockedValue() {
		return myBlockedValue;
	}

	public BlockedFieldJson setBlockedValue(String theBlockedValue) {
		myBlockedValue = theBlockedValue;
		return this;
	}

	public BlockFieldBlockRuleEnum getBlockRule() {
		return myBlockRule;
	}

	public BlockedFieldJson setBlockRule(@Nonnull BlockFieldBlockRuleEnum theBlockRule) {
		myBlockRule = theBlockRule;
		return this;
	}
}
