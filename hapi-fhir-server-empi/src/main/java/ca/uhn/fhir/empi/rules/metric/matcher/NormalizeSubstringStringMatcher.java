package ca.uhn.fhir.empi.rules.metric.matcher;

import ca.uhn.fhir.util.StringUtil;

public class NormalizeSubstringStringMatcher implements IEmpiStringMatcher {
	@Override
	public boolean matches(String theLeftString, String theRightString) {
		String normalizedLeft = StringUtil.normalizeStringForSearchIndexing(theLeftString);
		String normalizedRight = StringUtil.normalizeStringForSearchIndexing(theRightString);
		return normalizedLeft.startsWith(normalizedRight) || normalizedRight.startsWith(normalizedLeft);
	}
}
