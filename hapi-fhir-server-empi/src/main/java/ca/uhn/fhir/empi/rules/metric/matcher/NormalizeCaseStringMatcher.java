package ca.uhn.fhir.empi.rules.metric.matcher;

import ca.uhn.fhir.util.StringNormalizer;

public class NormalizeCaseStringMatcher implements IEmpiStringMatcher {
	@Override
	public boolean matches(String theLeftString, String theRightString) {
		return StringNormalizer.normalizeStringForSearchIndexing(theLeftString).equals(StringNormalizer.normalizeStringForSearchIndexing(theRightString));
	}
}
