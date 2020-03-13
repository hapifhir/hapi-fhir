package ca.uhn.fhir.empi.rules.metric;

import ca.uhn.fhir.empi.IEmpiComparator;
import info.debatty.java.stringsimilarity.interfaces.NormalizedStringSimilarity;

public class HapiStringMetric implements IEmpiComparator<String> {
	private final NormalizedStringSimilarity myStringSimilarity;

	public HapiStringMetric(NormalizedStringSimilarity theStringSimilarity) {
		myStringSimilarity = theStringSimilarity;
	}

	@Override
	public double compare(String theLeft, String theRight) {
		return myStringSimilarity.similarity(theLeft, theRight);
	}
}
