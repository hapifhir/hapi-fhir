package ca.uhn.fhir.mdm.rules.matcher.util;

import ca.uhn.fhir.mdm.rules.json.MdmFieldMatchJson;

import java.util.List;

public class MatchRuleUtil {
	/**
	 * We use the binary shift operator (<<) to determine
	 * the "vector" used by
	 *
	 * By taking the log base 2 of a number N, we will know
	 * how many digits are needed to store N as binary.
	 * 1 more digit than that hits our overflow for a long value.
	 *
	 * ie, we cannot calculate vector values accurately after this,
	 * because we're hitting overflows and the << operator might lead
	 * to collisions or negative numbers that mean nothing.
	 *
	 * Note: by log rule -> log10(N) / log10(X) -> LogX(N)
	 */
	public static long MAX_RULE_COUNT = Math.round(Math.log(Long.MAX_VALUE) / Math.log(2)) + 1;

	public static boolean canHandleRuleCount(List<MdmFieldMatchJson> theRules) {
		return theRules.size() <= MAX_RULE_COUNT;
	}
}
