package ca.uhn.fhir.empi.rules.metric;

import info.debatty.java.stringsimilarity.interfaces.NormalizedStringSimilarity;

public class HapiStringMetric {
	private final NormalizedStringSimilarity myStringSimilarity;

	public HapiStringMetric(NormalizedStringSimilarity theStringSimilarity) {
		myStringSimilarity = theStringSimilarity;
	}

	public double compare(String theLeft, String theRight) {
		return myStringSimilarity.similarity(theLeft, theRight);
	}
}
