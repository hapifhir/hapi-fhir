package ca.uhn.fhir.empi.rules.metric.matcher;

import ca.uhn.fhir.util.StringUtil;

public class NormalizeCaseStringMatcher implements IEmpiStringMatcher {
	@Override
	public boolean matches(String theLeftString, String theRightString) {
		return StringUtil.normalizeStringForSearchIndexing(theLeftString).equals(StringUtil.normalizeStringForSearchIndexing(theRightString));
	}
}
