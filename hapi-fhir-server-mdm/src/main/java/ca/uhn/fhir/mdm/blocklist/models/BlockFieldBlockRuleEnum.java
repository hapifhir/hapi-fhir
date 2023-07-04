package ca.uhn.fhir.mdm.blocklist.models;

/**
 * This enum provides information on how a block field is to be used.
 * This is particularly pertinent for fields that have cardinality > 1
 */
public enum BlockFieldBlockRuleEnum {
	/**
	 * Block rule will be applied if *any* of the subfields match the
	 * provided blocked value.
	 */
	ANY,

	/**
	 * Block rule will be applied if and only if *all* of the subfields
	 * match the provided block vlaue.
	 */
	EXACT
}
