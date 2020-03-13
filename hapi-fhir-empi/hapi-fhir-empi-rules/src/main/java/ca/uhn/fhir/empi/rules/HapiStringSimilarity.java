package ca.uhn.fhir.empi.rules;

import info.debatty.java.stringsimilarity.interfaces.NormalizedStringSimilarity;

public class HapiStringSimilarity implements NormalizedStringSimilarity {
	private final NormalizedStringSimilarity myStringSimilarity;

	public HapiStringSimilarity(NormalizedStringSimilarity theStringSimilarity) {
		myStringSimilarity = theStringSimilarity;
	}

	@Override
	public double similarity(String theLeft, String theRight) {
		return myStringSimilarity.similarity(theLeft, theRight);
	}
}
